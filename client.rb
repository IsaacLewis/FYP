require 'socket'

class Client
  @@port_offset = 7000;
  @@input_length = 124;

  def initialize(player_no)
    port = @@port_offset + player_no.to_i
    puts "Connecting on #{port}"

    $sock = TCPSocket.open 'localhost', port
    $sock.send "My name is #{@name || "RandomBot"}", 0

    $input = nil

    loop do
      $input = $sock.recvfrom(@@input_length)[0]
      if $input =~ /your move/
        action = get_action $input
        puts "Sent '#{action}'"
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
    puts "Received '#{str.strip}'"
  end

  # when a response is required; should return "c", "r","b" or "f"
  def get_action(str)
    puts "Received '#{str.strip}'"
    ["f","c","r"].choice
  end
end
