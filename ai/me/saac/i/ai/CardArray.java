package me.saac.i.ai;

import pokerai.game.eval.spears.Card;
import pokerai.game.eval.spears.FiveCardEvaluator;
import pokerai.game.eval.spears.SevenCardEvaluator;
import pokerai.game.eval.spears.SixCardEvaluator;

// a class to make it convenient to keep track of observed cards and
// evaluate their strength
public class CardArray {
	static FiveCardEvaluator fce = new FiveCardEvaluator();
	static SixCardEvaluator xce = new SixCardEvaluator();
	static SevenCardEvaluator sce = new SevenCardEvaluator();
	
	int count;
	public Card[] cards;
	
	public CardArray() {
		count = 0;
		cards = new Card[7];
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
		else
			result = sce.evaluate(cards);
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
}
