class Card
  attr_reader :card_id, :suit_index, :rank_index
  include Comparable

  def <=>(compared_card)
    @rank_index <=> compared_card.rank_index
  end

  def ==(card2)
    @card_id == card2.card_id
  end

  def initialize(rank, suit)
    fail unless Poker::Suits.include? suit and Poker::Ranks.include? rank
    @suit_index = Poker::Suits.index(suit)
    @rank_index = Poker::Ranks.index(rank)
    @card_id = @suit_index * 13 + @rank_index
  end

  def print
    "#{rank} of #{suit}"
  end

  def print_short
    "#{rank.to_s[0..0]}#{suit.to_s.downcase[0..0]}"
  end

  def rank
    Poker::Ranks[@rank_index]
  end

  def suit
    Poker::Suits[@suit_index]
  end

  def same_rank_as(card2)
    @rank_index == card2.rank_index
  end

  def same_suit_as(card2)
    @suit_index == card2.suit_index
  end
end
