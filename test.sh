rm /tmp/player*
ruby -e "load 'server.rb'; PokerServer.new" &
ruby launch_client.rb client 1 &
ruby launch_client.rb web_client 2 &
