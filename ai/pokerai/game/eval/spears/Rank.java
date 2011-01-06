package pokerai.game.eval.spears;

public enum Rank {
	Deuce	("2"), 
	Three	("3"), 
	Four	("4"), 
	Five	("5"), 
	Six		("6"),
	Seven	("7"), 
	Eight	("8"), 
	Nine	("9"), 
	Ten		("T"), 
	Jack	("J"), 
	Queen	("Q"), 
	King	("K"), 
	Ace		("A");
	
	private final String toString;


	private Rank(String toString) {
		this.toString = toString;
	}
	
	public String toString() {
		return toString;
	}
	
	public static Rank parse(String s)  {
		for (Rank r : Rank.values()) {
			if(s.equalsIgnoreCase(r.toString)) return r;
		}
		throw new RuntimeException("Unrecognized rank: " + s);
	}
	

}
