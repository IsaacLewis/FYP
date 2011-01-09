class Poker
  Suits = [:Clubs, :Spades, :Hearts, :Diamonds]
  Ranks = (2..9).to_a + [:Ten, :Jack, :Queen, :King, :Ace] 
  Hands = [:high_card, :pair, :two_pair, :trips, :straight, :flush,
              :full_house, :quads, :straight_flush, :royal_flush]
  MaxRaisesPerRound = 4
end
