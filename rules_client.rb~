require "client.rb"

class TerminalClient < Client
  def initialize(player_no)
    puts "What\'s your name?"
    @name = STDIN.gets.chomp
    super(player_no)
  end

  def receive(str)
    puts str.chomp
  end

  def get_action(str)
    puts str.strip
    STDIN.gets.chomp
  end
end
