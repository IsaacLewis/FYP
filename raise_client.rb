require "client.rb"

class RaiseClient < Client
  def initialize(player_no)
    @name = "AlwaysRaise"
    super(player_no)
  end

  def get_action(str)
    'r'
  end
end
