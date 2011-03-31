package me.saac.i.ai;

import pokerai.game.eval.spears.Card;
import pokerai.game.eval.spears.FiveCardEvaluator;
import pokerai.game.eval.spears.SevenCardEvaluator;
import pokerai.game.eval.spears.SixCardEvaluator;

// a class to make it convenient to keep track of observed cards and
// evaluate their strength

// note: first two cards represent player's hole cards, next 5 cards are the board cards, last 2 cards are
// the opponent's hole cards

public class CardArray {
	static FiveCardEvaluator fce = new FiveCardEvaluator();
	static SixCardEvaluator xce = new SixCardEvaluator();
	static SevenCardEvaluator sce = new SevenCardEvaluator();
	
	int count;
	public Card[] cards;
	
	public CardArray() {
		count = 0;
		cards = new Card[9];
	}

	public CardArray(Card[] cards, int count) {
		this.count = count;
		this.cards = cards;
	}
	
	public void add(Card c) {
		cards[count] = c;
		count++;
	}
	
	public int getCount() {
		return count;
	}
	
	public int evaluate(){
		int result = 0;
		if(count == 5) 
			result = fce.evaluate(cards);
		else if(count == 6)
			result = xce.evaluate(cards);
		else if(count == 7)
			result = sce.evaluate(cards);
		else
			result = -1;
		return result;
	}

	public int evaluateOpponentHand() {
		int result;
		if(count != 9) {
			result = -1;
		} else {
			result = this.subset(2,9).evaluate();
		}
		return result;
	}
    // returns a sub array from start (inclusive) to end (exclusive)
	public CardArray subset(int start, int end) {
		Card[] newCards = new Card[7];
		for(int i = start; i < end; i++) {
			newCards[i - start] = cards[i];
		}
		return new CardArray(newCards, end - start);
	}
	
	// finds the suit with the highest number of cards on the board,
	// and returns that number
	public int numberOfSameSuitOnBoard() {
		return 3;
	}
}
