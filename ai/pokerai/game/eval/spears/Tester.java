package pokerai.game.eval.spears;

import java.util.Random;

public class Tester {
	static SevenCardEvaluator evaluator = new SevenCardEvaluator();
	
	public static void main(String[] args) {
		
		
		//test1();
		//test2();
		//test3();
		testBoundaries();
	}

	private static void test1() {
		String[][] hands = new String[][] {
				{"Straight Flush", 	"AsKsQsJsTs9s8s"},
				{"Quads", 			"AcAdAhAsKcKdKs"},
				{"Full House", 		"AcAdAhKcKdQcQd"},
				{"Flush", 			"AcKcQcJc9c8c7c"},
				{"Straight", 		"AcKcQcJcTd9d8d"},
				{"Trips", 			"AcAdAhKcQcJc9d"},
				{"Two Pair", 		"AcAdKcKdQcJc9d"},
				{"Pair", 			"AcAdKcQcJc9d8d"},				
				{"High Card", 		"AcKcQcJc9d8d7d"},
		};
		
		int[] cactusKevHandRanks = new int[]{
				1,
				11,
				167,
				323,
				1600,
				1610,
				2468,
				3326,
				6186
		};
		
	
		for (int i = 0; i < hands.length; i++) {
			String name = hands[i][0];
			String handString = hands[i][1];
			int correctRank = cactusKevHandRanks[i];
			Hand hand = Hand.parse(handString);
			int rank = evaluator.evaluate(hand.toCards());
			String check = (rank == correctRank) ? "CORRECT" : "INCORRECT";
		
			System.out.println("Hand: " + name + " " + hand + " rank: " + rank + " " + check);
		}
		System.out.println();
	}
	
	private static void test2() {
		String[][] hands = new String[][] {
				{"Straight Flush", "2h3hTsJsQsKsAs"},
				{"Quads", "2cAcAd3hAhTsAs"},
				{"Boat", "Ac4d2hJhAhJsAs"},
				{"Flush", "JhAh2s4s7sJsAs"},
				{"Trips", "6dAdJhAh2s4sAs"},
				{"2 Pair", "4dJhAh2s7sJsAs"},
				{"High Card", "4d9hJh2s7sQsAs"},
		};
		
		for (int i = 0; i < hands.length; i++) {
			String name = hands[i][0];
			String handString = hands[i][1];
			Hand hand = Hand.parse(handString);
			int rank = evaluator.evaluate(hand.toCards());
			System.out.println("Hand: " + name + " " + hand + " " + handString + " rank: " + rank);
		}
		System.out.println();
	}
	
	private static void test3() {
		String s = "9s";
		String ss = "Ah5d3c4cJd";
		Card c = Card.parse(s);
		Card[] cc = Card.parseArray(ss);
		System.out.println(c.name());
		for(Card c1 : cc) {
			System.out.println(c1.name());
		}
	}
	
	private static void testBoundaries() {
		int[] boundaries = {
				1602,
				540,
				488,
				238,
				245,
				258,
				430,
				441,
				440,
				441,
				441,
				422,
				298,
				430,
				1000};
		int[] histogram = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		Hand h;
		for(int i = 0; i < 1000000; i++) {
			if(i % 10000 == 0) System.out.print("."); 
			h = randHand();
			int rank = evaluator.evaluate(h.toCards());
			for(int j = 0; j < 15; j++) {
				if(rank < boundaries[j]) {
					histogram[j] += 1;
					break;
				} else if(j == 14) {
					histogram[j] += 1;
					break;
				} else {
					rank -= boundaries[j];
				}
			}
		}
		for(int i = 0; i < 15; i++) {
			System.out.println(i + ": " + histogram[i]);
		}
		
	}

	private static Hand randHand() {
		Random r = new Random();
		String[] ranks = new String[] {"2","3","4","5","6","7","8","9","T","J","Q","K","A"};
		String[] suits = new String[] {"h","s","c","d"};
		String s = "";
		for(int i = 0; i < 7; i++) {
			s += ranks[r.nextInt(13)] + suits[r.nextInt(4)];
		}
		Hand h = Hand.parse(s);
		if(h.noCards() < 7) h = randHand();
		return h;
	}
}
