package pokerai.game.eval.spears;

public class SevenCardEvaluator implements HandEvaluatorSpears {
	private static SixCardEvaluator sixCardEvaluator;
	private static final int[][] choose6From7 = new int[][] {
			{0, 1, 2, 3, 4, 5},
			{0, 1, 2, 3, 4, 6},
			{0, 1, 2, 3, 5, 6},
			{0, 1, 2, 4, 5, 6},
			{0, 1, 3, 4, 5, 6},
			{0, 2, 3, 4, 5, 6},
			{1, 2, 3, 4, 5, 6}
	};
	private Card[] sixCards = new Card[6];
	
	public SevenCardEvaluator()  {
		sixCardEvaluator = new SixCardEvaluator();
	}
	
	public int evaluate(Card[] sevenCards)  {
		int minRank = Integer.MAX_VALUE;
		
		for (int[] c67 : choose6From7) {
			for (int i = 0; i < 6; i++) {
				sixCards[i] = sevenCards[c67[i]];
			}
			int rank = sixCardEvaluator.evaluate(sixCards);
			minRank = Math.min(rank, minRank);	
		}
		return minRank;
	}
	
}
