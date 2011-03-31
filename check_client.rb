require "client.rb"

class CheckClient < Client
  def initialize(player_no)
    @name = "AlwaysCheck"
    super(player_no)
  end

  def get_action(str)
    'c'
  end
end
