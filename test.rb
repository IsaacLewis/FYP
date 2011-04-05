load "server.rb"

def start_match(player1, player2)
  Thread.new {sleep 5; `ruby launch_client.rb #{player1} 1 --quiet`}
  Thread.new {sleep 10; `ruby launch_client.rb #{player2} 2 --quiet`}
  $s = PokerServer.new true, true
  $s.match 100000, 10000, "#{player1}_vs_#{player2}.csv"
  $s.close
end

start_match "check_client", "rules_client"
start_match "rules_client", "check_client"
start_match "raise_client", "rules_client"
start_match "rules_client", "raise_client"
start_match "check_client", "raise_client"
start_match "raise_client", "check_client"
