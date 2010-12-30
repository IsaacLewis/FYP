
class Deck
  def initialize
    @cards = []
    Poker::Suits.each do |suit| 
      Poker::Ranks.each do |rank|
        @cards << Card.new(rank, suit)
      end
    end
    @cards.shuffle!
  end

  def draw
    throw "Deck empty" if @cards.empty?
    @cards.shift # removes the first element of @cards and returns it
  end
end
