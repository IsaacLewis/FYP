class RankedHand
  attr_reader :rank, :best_cards

  include Comparable

  def <=>(compared_hand)
    self.rank <=> compared_hand.rank
  end

  def initialize(cards)
    
	fail if cards.size < 5
	
    # if more than 5 cards, selects the subset of size 5
    # with the highest ranking
    if cards.size > 5
      subsets = cards.combination(5)
      @best_hand = subsets.map {|subset| RankedHand.new subset}.max
      @best_cards = @best_hand.best_cards
      @rank = @best_hand.rank
      return
    end
	
    # if there's only 5 cards, by definition this is the
    # best possible set of 5 cards

    # groups cards by ranks, then sorts them, largest groups first
    # so we can easily find pairs, trips, quads etc
    rank_sets = cards.group_by do |card| 
      card.rank_index
    end.values.sort.reverse.sort do 
      |x, y| y.size <=> x.size
    end

    highest_cards = cards.sort.reverse
    kickers = highest_cards - (rank_sets.select {|set| set.size > 1}.flatten)

    if is_straight? cards and is_flush? cards
      @rank = Rank.new :straight_flush, highest_cards[0]
      @best_cards = highest_cards

    elsif rank_sets[0].length == 4
      @rank = Rank.new :quads, rank_sets[0][0], *kickers
      @best_cards = rank_sets[0] + kickers

    elsif rank_sets[0].length == 3 and rank_sets[1].length == 2
      @rank = Rank.new :full_house, rank_sets[0][0], rank_sets[1][0]
      @best_cards = rank_sets[0] + rank_sets[1]

    elsif is_flush? cards
      @rank = Rank.new :flush, *highest_cards
      @best_cards = highest_cards

    elsif is_straight? cards
      @rank = Rank.new :straight, highest_cards[0]
      @best_cards = highest_cards

    elsif rank_sets[0].length == 3
      @rank = Rank.new :trips, rank_sets[0][0], *kickers
      @best_cards = rank_sets[0] + kickers

    elsif rank_sets[0].length == 2
      if rank_sets[1].length == 2
        @rank = Rank.new :two_pair, rank_sets[0][0], rank_sets[1][0], *kickers
        @best_cards = rank_sets[0] + rank_sets[1] + kickers
      else
        @rank = Rank.new :pair, rank_sets[0][0], *kickers
        @best_cards = rank_sets[0] + kickers
      end
    else
      @rank = Rank.new :high_card, *highest_cards
      @best_cards = highest_cards
    end
  end

  def is_flush?(cards)
    return @flush unless @flush.nil?
    @flush = cards.all? {|card| card.suit_index == cards[0].suit_index}
    return @flush
  end

  def is_straight?(cards)
    return @straight unless @straight.nil?
    cards = cards.sort
    @straight = cards.each_cons(2).all? do |card1, card2|
      card1.is_followed_by? card2
    end
    
    # check for wheels (straights of form A2345)
    if @straight == false and 
    	cards[0].rank_index == 0 and  
    	cards[1].rank_index == 1 and 
    	cards[2].rank_index == 2 and 
    	cards[3].rank_index == 3 and 
    	cards[4].rank_index == 12
    	
      @straight = true 
    end
    return @straight
  end

  def print_rank
    @rank.print_rank
  end

  def print_best_hand
    @best_cards.map {|card| card.print_short}.join " "
  end
end
