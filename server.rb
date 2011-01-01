["chip_store_module","card","game","player","ranked_hand","deck","poker","rank","chip_store"].each do |file| 
  load (file+".rb")
end

require 'server'

class PokerServer

  $player1_port = 7001
  $player2_port = 7002

  attr_reader :player1, :player2

  def initialize
    puts "Waiting for Player 1 to connect on #{$player1_port}"
    $player1 = Player.new  $player1_port
    $player1.send "#{$player1.name}, you have connected succesfully, " +
      "waiting for an opponent."
    puts "Waiting for Player 2 to connect on #{$player2_port}"
    $player2 = Player.new $player2_port
    $players = [$player1, $player2]
    $server = self

    send "Two players have connected. #{$player1.name} and #{$player2.name}" +
      ", get ready to fight. The battleground: HEADS-UP LIMIT HOLDEM!"
  end

  def send(str)
    puts str
    $players.each {|player| player.send str}
  end

  def match(starting_stacks)
    small_bet = 2
    $players.each {|player| player.chips = starting_stacks}
    $pot = ChipStore.new
    
    match_won = false
    hand_no = 1

    until match_won
      send "\n-------\n\n"
      send "Hand No: #{hand_no}"
      
      game = Game.new $players, $pot, small_bet
      $players, $pot = game.play!
      
      $players.each do |player|
        if player.chips <= 0
          if player == $player1
            winner, loser = $player2, $player1
          else
            winner, loser = $player1, $player2
          end
          send "#{loser.name} is out of chips. #{winner.name} is victorious!"
          match_won = true
          return winner, loser
        end
      end
      $players.reverse! # so blinds rotate
      small_bet += 1
      hand_no += 1
    end
  end

  def close
    send "SHUTDOWN"
    $players.each do |player|
      begin
        player.shutdown
      rescue
        "Error when attempting to shutdown port assigned to #{player.name}"
      end
    end
  end
end
