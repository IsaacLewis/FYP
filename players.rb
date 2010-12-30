
class RandomPlayer < Player
  def input_action
    ["c","r","f"].choice
  end
end

class HandStrengthPlayer < Player

  def initialize(quiet=nil)
    @threshold = rand(24)
    @agression = (rand * 2000).to_i.to_f / 1000
    @pair_weighting = rand(10)
    @max_hand_strength = 22 + @pair_weighting
    @quiet = quiet
    super self.print
  end

  def input_action
    if hand_strength < @threshold
      "f" 
    else
      if rand < @agression * (hand_strength.to_f / @max_hand_strength)
        "r" 
      else
        "c"
      end
    end
  end

  def hand_strength
    @hand[0].rank_index + @hand[1].rank_index +
      (@hand[0].rank_index == @hand[1].rank_index ? @pair_weighting : 0)
  end

  def print
    "["+[@threshold, @agression, @pair_weighting].join(" - ")+"]"
  end

  def inspect
    print
  end

  def send(str)
    puts str unless @quiet
  end
end

class HumanPlayer < Player
  def send(str)
    sleep 0.5
    puts str
  end

  def input_action
    print "#{name}, your move? (c)heck/call, bet/(r)aise, (f)old: "
    gets.chomp
  end
end
