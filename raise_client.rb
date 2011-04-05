require "client.rb"

class RaiseClient < Client
  def initialize(player_no,quiet)
    @name = "AlwaysRaise"
    super(player_no,quiet)
  end

  def get_action(str)
    'r'
  end
end
