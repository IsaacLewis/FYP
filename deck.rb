class Deck

  def Deck.reset
    @@cards_file = File.open "preset_hands.txt", "r"
  end

  self.reset

  def initialize
    if $use_preset_hands
      @cards = @@cards_file.readline.chomp.split(",").map {|c| Card.parse c}
    else
      @cards = []
      Poker::Suits.each do |suit| 
        Poker::Ranks.each do |rank|
          @cards << Card.new(rank, suit)
        end
      end
      @cards.shuffle!
    end
  end

  def draw
    throw "Deck empty" if @cards.empty?
    @cards.shift # removes the first element of @cards and returns it
  end
end
