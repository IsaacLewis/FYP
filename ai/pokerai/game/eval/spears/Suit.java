package pokerai.game.eval.spears;


public enum Suit {
	Clubs("c"),
	Diamonds("d"),
	Hearts("h"),
	Spades("s");
	
	private final String toString;
	
	private Suit(String toString) {
		this.toString = toString;
	}
	
	public String toString() {
		return toString;
	}
	
	public static Suit parse(String s)  {
		for (Suit suit : Suit.values()) {
			if(suit.toString.equalsIgnoreCase(s)) return suit;
		}
		throw new RuntimeException("Unrecognized suit: " + s);
	}

}
