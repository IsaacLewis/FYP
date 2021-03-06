package me.saac.i.ai;
import java.util.regex.*;

import pokerai.game.eval.spears.Card;
import pokerai.game.eval.spears.FiveCardEvaluator;
import pokerai.game.eval.spears.SevenCardEvaluator;

public class RegexTest {
	
    public static void main(String[] args) {
	Pattern p = Pattern.compile("is dealt [A-z0-9 ]+ \\((..),(..)\\)");
	String s = "JavaBot is dealt the 2 of Spades and the Ace of Hearts (2s,Ah).";
	Matcher m = p.matcher(s);
	System.out.println("Matches: " + m.find());
	System.out.println("Group: " + m.group(1));
	System.out.println("Group: " + m.group(2));
	
	Card[] cardArray5 = Card.parseArray("2h3c4s5d7h");
	Card[] cardArray7 = Card.parseArray("AsKsQsJs9d8c5c");
	SevenCardEvaluator sce = new SevenCardEvaluator();
	int c7v = sce.evaluate(cardArray7);
	FiveCardEvaluator fce = new FiveCardEvaluator();
	int c5v = fce.evaluate(cardArray5);
	System.out.println("c5v: " + c5v + " c7v: " + c7v);
	
    Pattern inputPrompt = Pattern.compile("your move\\? \\(c\\)heck/call \\(([0-9]+)\\), bet/\\(r\\)aise \\(([0-9]+)\\), \\(f\\)old");
    String ss = "your move? (c)heck/call (0), bet/(r)aise (2), (f)old";
    m = inputPrompt.matcher(ss); 
	System.out.println("Matches: " + m.find());
	System.out.println("Group: " + m.group(1));
	System.out.println("Group: " + m.group(2));
    }

}