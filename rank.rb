class Rank
  
  attr_reader :rank_name
  attr_reader :highest_cards
  include Comparable

  def <=>(compared_hand)
    if self.rank_index == compared_hand.rank_index
      self.compare_high_cards(compared_hand)
    else
      self.rank_index <=> compared_hand.rank_index
    end
  end

  def compare_high_cards(compared_hand)
    if self.rank_name != compared_hand.rank_name
      throw "hands are different ranks" 
    end
    if self.highest_cards.length != compared_hand.highest_cards.length
      throw "highest_cards arrays are different sizes"
    end
    for i in (0..self.highest_cards.length - 1)
      comparison = self.highest_cards[i] <=> compared_hand.highest_cards[i]
      return comparison if comparison != 0
    end
    return 0
  end

  # rank_name is :flush, :straight, :pair etc.
  # best is highest card in case of a straight, 
  # 

  def initialize(rank_name, *highest_cards)
    @rank_name = rank_name
    @highest_cards = highest_cards

  end

  def rank_index
    Poker::Hands.index(self.rank_name)
  end

  def print_rank
    case @rank_name
    when :straight_flush
      top_card = #{@highest_cards[0].rank}
      if top_card == :Ace then "a royal flush"
      else "a straight flush, #{top_card}-high"
      end
    when :quads
      "quad #{@highest_cards[0].rank}s"
    when :full_house
      "a full house, #{@highest_cards[0].rank}s over #{@highest_cards[1].rank}s"
    when :flush
      "a flush, #{@highest_cards[0].rank}-high"
    when :straight
      "a straight, #{@highest_cards[0].rank}-high"
    when :trips
      "trip #{@highest_cards[0].rank}s"
    when :two_pair
      "two pair, #{@highest_cards[0].rank}s and #{@highest_cards[1].rank}s"
    when :pair
      "a pair of #{@highest_cards[0].rank}s"
    when :high_card
      "high card, #{@highest_cards[0].rank}"
    end
  end

end
