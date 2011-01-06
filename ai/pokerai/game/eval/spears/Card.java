package pokerai.game.eval.spears;

public class Card {
	public static final int count = 52;
	private static Card[] values = new Card[count];
	public final Rank rank;
	public final Suit suit;
  //public int rank;
  //public int suit;
  int ordinal;
	final String toString;
	final String name;
	
	public int ordinal() {
		return ordinal;
	}
	
	public String toString() {	/* As */
		return toString;
	}
	
	public String name() { /* AceOfSpades */
		return name;
	}
	
	public static Card get(Rank rank, Suit suit) {
		return Card.values()[ordinal(rank, suit)];
	}

  public static Card get(int ord) {
    return values[ord];
  }
  

  public static Card[] values() {
		return values;
	} 

	public static String toString(Card[] hand) {
		if(hand == null) return "NULL";
		if(hand.length == 0)return "NULL";
		StringBuffer b = new StringBuffer();
		for (Card card : hand) {
			b.append(card.toString);
		}
		return b.toString();
	}


    public static Card parse(String s) {
    	Rank rank = Rank.parse(s.substring(0, 1));
    	Suit suit = Suit.parse(s.substring(1, 2));

    	return Card.get(rank, suit);
    }
    
	
	static {
		for (Rank rank : Rank.values()) {
			for (Suit suit : Suit.values()) {
				Card c = new Card(rank, suit);
				int ordinal = c.ordinal;
				values[ordinal] = c;
			}
		}
	}

	private Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
		this.ordinal = ordinal(rank, suit);
		this.name = rank.name() + "Of" + suit.name();
		this.toString = rank.toString() + suit.toString();
	}


  private static int ordinal(Rank rank, Suit suit) {
		return rank.ordinal() + suit.ordinal() * 13;
	}

  public static Card[] parseArray(String s) {
		int noCards = s.length()/2;
		Card[] result = new Card[noCards];
		for (int i = 0; i < noCards; i++) {
			result[i] = Card.parse( s.substring(2*i, 2*i+2));
		}
		return result;
	}



}



