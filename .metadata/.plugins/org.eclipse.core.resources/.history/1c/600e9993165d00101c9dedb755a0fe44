package me.saac.i.ai;



// interface for different opponent models
// (as we'll be experimenting with several and comparing)
public interface OpponentModel {
	public double winPossibility(ActionList history, GameInfo gameInfo, int playerHandStrength);
	
	public double[] actionProbabilities(ActionList history, GameState gameState);
	
	public void inputAction(ActionList history, GameState gameState);
	
	public void inputEndOfHand(ActionList history, GameState gameState);
}
