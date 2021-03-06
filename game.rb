class Game
  def inspect
    self.to_s
  end

  def initialize(players, pot, small_bet)
    @players = players
    @pot = pot
    @small_bet = small_bet
    @big_bet = small_bet * 2
    @small_blind = small_bet / 2
    @deck = Deck.new
    @board = []

    $server.send "Dealer: " + @players[1].name
    $server.send "Small bet: " + @small_bet.to_s
    $server.send chip_distribution
  end

  def play!
    deal_hands
    post_blinds
    betting_round(@small_bet)
    flop unless @game_won
    betting_round(@small_bet) unless @all_in or @game_won
    turn unless @game_won
    betting_round(@big_bet) unless @all_in or @game_won
    river unless @game_won
    betting_round(@big_bet) unless @all_in or @game_won
    showdown unless @game_won
    
    @players.each {|player| player.discard}
    return @players, @pot
  end

  def chip_distribution
    "Chips: #{$player1.chips},#{$player1.bet.chips}," +
      "#{@pot.chips},#{$player2.bet.chips},#{$player2.chips}"
  end

  def deal_hands
    @players.each do |player| 
      2.times {player.hand << @deck.draw}
    end

    @players.each do |player|
      player.send "#{player.name} is dealt " +
        "the #{player.hand[0].print} and the #{player.hand[1].print}" +
        " (#{player.hand[0].print_short},#{player.hand[1].print_short})."
    end
  end

  def post_blinds
    small_blind, big_blind = @players[1], @players[0]
    amt_posted = small_blind.give_chips small_blind.bet, @small_blind

    $server.send "#{small_blind.name} is the small blind, " +
      "they post #{amt_posted} chips."
    $server.send chip_distribution
    
    amt_posted = big_blind.give_chips big_blind.bet, @small_bet

    $server.send "#{big_blind.name} is the big blind, " +
      "they post #{amt_posted} chips."
    $server.send chip_distribution
  end

  def betting_round(bet_size)
    @bets_this_round = 0
    player = @players[1]
    opponent = @players[0]
    @players.each {|p| p.acted = false}
    while not @game_won and 
    	  not @all_in and
         (not player.acted or not opponent.acted or
              player.bet.chips != opponent.bet.chips) 


      take_player_action player, opponent, bet_size
      $server.send chip_distribution
      return if @game_won

      if player.all_in
        @all_in = true
        $server.send "#{player.name} is all-in."
        take_player_action opponent, player, bet_size
        if player.bet.chips < opponent.bet.chips
          amt_extra = opponent.bet.chips - player.bet.chips
          opponent.bet.give_chips opponent, amt_extra
          $server.send "(As #{player.name} is all-in, " +
            "#{opponent.name} reduces their bet to " +
            "#{opponent.bet.chips} chips)"
          $server.send chip_distribution
        end
      end

      player, opponent = opponent, player
    end

    @players.each {|player| player.bet.give_chips @pot, :all}
    $server.send chip_distribution
  end

  def take_player_action(player, opponent, bet_size)
    player.acted = true
    call_cost = opponent.bet.chips - player.bet.chips
    raise_cost = (opponent.bet.chips + bet_size) - player.bet.chips

    if @bets_this_round < Poker::MaxRaisesPerRound
      permitted_actions = ["c","r","f","b"]
    else
      permitted_actions = ["c","f"]
    end

    action = player.input_action call_cost, raise_cost    
    action = 'c' unless permitted_actions.include? action
    
    case action
    when "c"
      if player.bet.chips < opponent.bet.chips
        player.give_chips player.bet, call_cost
        $server.send "Player action: #{player.name} calls."
      else
        $server.send "Player action: #{player.name} checks."
      end
      
    when "r", "b"
      player.give_chips player.bet, raise_cost
      if opponent.bet.chips == 0
        $server.send "Player action: #{player.name} bets #{player.bet.chips}."
      else
        $server.send "Player action: #{player.name} raises to #{player.bet.chips}."
      end
      @bets_this_round += 1
      
    when "f"
      @players.each {|p| p.bet.give_chips @pot, :all}
      $server.send "Player action: #{player.name} folds!"
      opponent.hands_won_by_opponent_folds += 1
      opponent.chips_won_by_opponent_folds += @pot.chips
      game_won_by opponent

    end
  end

  def deal_to_board
    next_card = @deck.draw
    @board << next_card
    $server.send "The #{next_card.print} (#{next_card.print_short}) " +
      "is dealt to the board."
  end

  def flop
    $server.send "Dealing the flop"
    3.times {deal_to_board}
  end

  def turn
    $server.send "Dealing the turn"
    deal_to_board
 end

  def river
    $server.send "Dealing the river"
    deal_to_board
  end

  def showdown
    hands = @players.map {|player| RankedHand.new(player.hand + @board)}
    @players.each_with_index do |player, i|
      $server.send "Player #{player.name} shows " +
        "#{player.hand[0].print_short} #{player.hand[1].print_short}, " +
        "giving them #{hands[i].print_rank} " + 
        "(#{hands[i].print_best_hand})"
    end

    if (hands[0] <=> hands[1]) == 0
      its_a_draw
    else
      winning_hand = hands.max
      winner = @players[hands.index winning_hand]
      winner.hands_won_at_showdown += 1
      winner.chips_won_at_showdown += @pot.chips

      game_won_by winner
    end
  end

  def its_a_draw
    win_amount = @pot.chips / 2
    @players.each {|player| @pot.give_chips player, win_amount}
    $server.send "It's a draw! Both players win #{win_amount} chips."
    $server.send chip_distribution
  end

  def game_won_by(winner)
    $server.send "#{winner.name} wins #{@pot.chips} chips!"
    @pot.give_chips winner, :all
    $server.send chip_distribution
    @pot_size = 0
    @game_won = true
  end
end
