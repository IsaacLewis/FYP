require 'socket'

class Player
  attr_accessor :hand, :acted
  attr_reader :name, :bet, :session

  include ChipStoreModule

  def initialize(port)
    @port = port
    @sock = TCPServer.new('localhost', port)
    @session = @sock.accept
    input = @session.recvfrom 124
    STDOUT.puts input
    @name = input[0].match(/My name is ([A-z0-9]*)/)[1]
    STDOUT.puts @name + " connected"
    @hand = []
    @bet = ChipStore.new
    @acted = false
  end

  def discard
    @hand = []
  end
  
  def send(str)
    begin
      @session.send(str.ljust(124), 0)
    rescue Exception => e
      puts "There was an error sending on port #{@port}"
    end  
  end

  def input_action(call_cost, raise_cost)
    send "#{name}, your move? " +
      "(c)heck/call (#{call_cost}), bet/(r)aise (#{raise_cost}), (f)old: "
    input = @session.recvfrom 124
    input[0]
  end

  def shutdown
    @sock.close
  end
end
