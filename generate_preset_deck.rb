file = File.open "preset_hands.txt","w"
20000.times do
deck = Deck.new
cards = []
9.times {cards << deck.draw}
str = cards.map {|c| c.print_short}.join ','
file.write str + "\n"
end

file.close
