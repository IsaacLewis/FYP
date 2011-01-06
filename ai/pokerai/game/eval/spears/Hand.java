package pokerai.game.eval.spears;

import java.util.BitSet;
import java.util.Random;

public class Hand {
	private static final int NO_CARDS = 52;
	private static final Random random = new Random();
	private BitSet bitSet;

	public Hand() {
		bitSet = new BitSet(NO_CARDS);
	}
	
	public Hand(Card[] cards) {
		bitSet = new BitSet(NO_CARDS);
		for (Card card : cards) {
			bitSet.set(card.ordinal);
		}
	}

  // Indiana: reset hand by given ordinals
  public void reset(int[] cards) {
		bitSet = new BitSet(NO_CARDS);
		for (int i = 0; i < cards.length; i++) {
			bitSet.set(cards[i]);
		}
	}

  // Indiana: add card to the hand by given card ordinal
  public void addCard(int card) { bitSet.set(card); }
	
  public Hand plus(Hand hand){
		this.bitSet.or(hand.bitSet);
		return this;
	}
	
	public Hand plus(Card[] cards) {
		for (Card card : cards) {
			this.bitSet.set(card.ordinal);
		}
		return this;
	}
	
	public Hand minus(Hand hand) {
		this.bitSet.andNot(hand.bitSet);
		return this;
	}
	
	public Hand minus(Card[] cards) {
		for (Card card : cards) {
			this.bitSet.clear(card.ordinal);
		}
		return this;
	}
	
	public Card[] toCards() {
		int noCards = noCards();
		Card[] result = new Card[noCards];
		
		int j = -1;
		for (int i = 0; i < result.length; i++) {
			j = bitSet.nextSetBit(j+1);
			result[i] = Card.values()[j];
		}
		
		return result;
	}

	public int noCards() {
		return bitSet.cardinality();
	}

	public boolean intersects(Hand hand) {
		return bitSet.intersects(hand.bitSet);
	}

	public Hand plus(Card card) {
		this.bitSet.set(card.ordinal);
		return this;
	}
	
	public Hand minus(Card card) {
		this.bitSet.clear(card.ordinal);
		return this;
	}
	
	public String toString() {
		return Card.toString(this.toCards());
	}

	public Card removeRandom() {
		int cardNo;
		do {
			cardNo = random.nextInt(52);
		} while (!this.bitSet.get(cardNo));		
		
		this.bitSet.clear(cardNo);
		return Card.values()[cardNo];
	}

	public Hand clear() {
		this.bitSet.clear();
		return this;
	}

	public boolean intersects(Pair p) {
		Card[] cards = p.getCards();
		if(this.contains(cards[0])) return true;
		if(this.contains(cards[1])) return true;
		return false;
	}

	private boolean contains(Card card) {
		return this.bitSet.get(card.ordinal);
	}

	public Hand minus(Pair p) {
		Card[] cards = p.getCards();
		this.minus(cards);
		return this;
	}

	public Hand plus(Pair p) {
		Card[] cards = p.getCards();
		this.plus(cards);
		return this;
	}
	
	public static Hand parse(String s) {
		Hand result = new Hand();
		int noCards = s.length()/2;
		for (int i = 0; i < noCards; i++) {
			result.plus(Card.parse( s.substring(2*i, 2*i+2)));
		}
		return result;
	}



}
