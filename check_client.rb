require "client.rb"

class CheckClient < Client
  def initialize(player_no,quiet)
    @name = "AlwaysCheck"
    super(player_no,quiet)
  end

  def get_action(str)
    'c'
  end
end
