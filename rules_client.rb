require "client.rb"
require "card.rb"
require "ranked_hand.rb"

class RulesClient < Client
  def initialize(player_no, quiet)
    @call_threshold = 11
    @raise_threshold = 18
    @pair_weighting = 4

    @postflop_call_threshold = 
      RankedHand.new(["As","2d","Jc","5d","4h"].map {|c| Card.parse c})
    @postflop_raise_threshold = 
      RankedHand.new(["2s","2d","3c","3d","6s"].map {|c| Card.parse c})

    @max_hand_strength = 24 + @pair_weighting
    @name = "RulesBot"
    @current_round = :Preflop
    super(player_no, quiet)
  end

  def receive(str)
    case str
    when /Hand No: /
      STDOUT.puts str.chomp unless @quiet
      @current_round = :Preflop
      @board_cards = []

    when /Dealing the/
      STDOUT.puts str.chomp unless @quiet
      @cached_best_hand = nil
      @current_round = :Postflop
      
    when /RulesBot is dealt .* \((..),(..)\)/
      STDOUT.puts str.chomp unless @quiet
      @hand = [Card.parse($1), Card.parse($2)]
      
    when /\((..)\) is dealt to the board/
      STDOUT.puts str.chomp unless @quiet
      @board_cards << Card.parse($1)

    end
  end

  def hand_strength
    throw "@hand not set!" if @hand.nil?
    @hand[0].rank_index + @hand[1].rank_index +
      (@hand[0].rank_index == @hand[1].rank_index ? @pair_weighting : 0)
  end

  def get_best_hand
    if(@cached_best_hand.nil?)
      @cached_best_hand = RankedHand.new @hand + @board_cards
    end
    @cached_best_hand
  end

  def get_action(str)
    str.match /call \(([0-9]*)\), bet\/\(r\)aise \(([0-9]*)\)/
    call_cost = $1.to_i

    if @current_round == :Preflop
      STDOUT.puts "Hand strength: " + hand_strength.to_s unless @quiet
      if hand_strength < @call_threshold and call_cost != 0
        "f" 
      else
        hand_strength < @raise_threshold ? "c" : "r"
      end
    else
      best_hand = get_best_hand
      STDOUT.puts "Hand ranking: " + best_hand.print_rank unless @quiet
      if best_hand < @postflop_call_threshold and call_cost != 0
        "f" 
      else
        best_hand < @postflop_raise_threshold ? "c" : "r"
      end
    end
  end
end

