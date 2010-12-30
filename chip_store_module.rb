# something that can possess chips, ie a player, the pot,
# or the player's bets on the table (before they enter the pot)

module ChipStoreModule
  attr_accessor :chips

  def all_in
    chips == 0
  end

  def give_chips(target, amount)
    amount = self.chips if amount == :all
    fail if amount < 0
    if self.chips <= amount
      chips_given = self.chips
      # self.all_in = true
    else
      chips_given = amount
    end
    self.chips -= chips_given
    target.chips += chips_given
    # target.all_in = false
    chips_given
  end

  def receive_chips(target, amount)
    target.give_chips self, amount
  end
end
