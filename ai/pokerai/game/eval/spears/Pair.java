package pokerai.game.eval.spears;

public class Pair {
	public final static int count = (Card.count * (Card.count - 1)) / 2;
	private static Pair[] values = new Pair[count];
	private static Pair[][] valuesByCard = new Pair[Card.count][Card.count];
	private static boolean[][] intersectsPair  = new boolean[count][count];
	private static boolean[][] intersectsCard  = new boolean[count][Card.count];

	public static Pair get(Card c1, Card c2) {
		return valuesByCard[c1.ordinal][c2.ordinal];
	}
	
	public static int getIndex(int i, int j) {
		return valuesByCard[i][j].ordinal;
	}
	
	public final int ordinal;
	private final Card[] cards = new Card[2];


	
	public String toString() {
		return cards[0].toString + cards[1].toString;
	}
	
	
	
	public Card[] getCards() {
		return cards;
	}
	
	public boolean intersects(Pair p) {
		return intersectsPair[this.ordinal][p.ordinal];
	}

	public boolean intersects(Card c) {
		return intersectsCard[this.ordinal][c.ordinal];
	}

	private static void findPairIntersections() {
		for (Pair p1 : values) {
			Card[] c1 = p1.cards;
			int i1 = p1.ordinal;
			for (Pair p2 : values) {
				int i2 = p1.ordinal;
				Card[] c2 = p2.cards;
				intersectsPair[i1][i2] = false;
				if(c1[0] == c2[0]) intersectsPair[i1][i2] = true;
				if(c1[0] == c2[1]) intersectsPair[i1][i2] = true;
				if(c1[1] == c2[0]) intersectsPair[i1][i2] = true;
				if(c1[1] == c2[1]) intersectsPair[i1][i2] = true;
			}
		}
	}
	
	private static void findCardIntersections() {
		for (Pair p1 : values) {
			Card[] c1 = p1.cards;
			int i1 = p1.ordinal;
			for (Card c2 : Card.values()) {
				int i2 = c2.ordinal;
				intersectsCard[i1][i2] = false;
				if(c1[0] == c2) intersectsCard[i1][i2] = true;
				if(c1[1] == c2) intersectsCard[i1][i2] = true;
			}
		}
	}
	
	private Pair(Card c1, Card c2, int ordinal) {
		this.cards[0] = c1;
		this.cards[1] = c2;
		this.ordinal = ordinal;
	}
	
	static {
		findValues();
		findPairIntersections();
		findCardIntersections();
	}

	private static void findValues() {
		Card[] cards = Card.values();
		int k = 0;
		for (int i = 0; i < Card.count; i++) {
			for (int j = i + 1; j < Card.count; j++) {
				Pair pair = new Pair(cards[i], cards[j], k);
				values[k] = pair;
				valuesByCard[i][j] = pair;
				valuesByCard[j][i] = pair;
				k++;
			}
		}
	}

	public static Pair[] values() {
		return values;
	}
	
	public static Pair parse(String s) {
		Card[] cards = new Card[2];
	
		for (int i = 0; i < 2; i++) {
			cards[i] = (Card.parse( s.substring(2*i, 2*i+2)));
		}
		
		Pair result = Pair.get(cards[0], cards[1]);
		return result;
	}

	public static boolean[][] getIntersectsCard() {
		return intersectsCard;
	}

	public static boolean[][] getIntersectsPair() {
		return intersectsPair;
	}
	
	


}
