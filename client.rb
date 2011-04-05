require 'socket'

class Client
  @@port_offset = 7000;
  @@input_length = 124;

  def initialize(player_no,quiet=false)
    port = @@port_offset + player_no.to_i
    puts "Connecting on #{port}"
    @quiet = quiet
    $sock = TCPSocket.open 'localhost', port
    $sock.send "My name is #{@name || "RandomBot"}", 0

    $input = nil
 
    loop do
      $input = $sock.recvfrom(@@input_length)[0]
      if $input =~ /your move/
        action = get_action $input
        puts "Sent '#{action}'" unless @quiet
        $sock.send action, 0
      elsif $input =~ /SHUTDOWN/
        shutdown
      else
        receive $input
      end
    end
  end

  def shutdown
    $sock.close
    exit
  end

  # for input that doesn't require a response
  def receive(str)
    puts "Received '#{str.strip}'" unless @quiet
  end

  # when a response is required; should return "c", "r","b" or "f"
  def get_action(str)
    puts "Received '#{str.strip}'" unless @quiet
    ['f','c','r'].choice
  end
end
