$party = (0..20).map {HandStrengthPlayer.new :quiet}

50.times do |i|
  puts "\n\n---Generation #{i}\n\n"
  puts "\n--" + $party.map {|x| x.print}.join("\n--")

  $party.sort! {|p1,p2| w, l = match p1, p2, 100; w == p1 ? -1 : 1}
  $party = $party.take 17

  puts "\n--" + $party.map {|x| x.print}.join("\n--")
  $party += (0..3).map {HandStrengthPlayer.new :quiet}
end
