package me.saac.i;

import java.util.ArrayList;

import me.saac.i.GameState.Action;

public interface OpponentModel {
	public double winPossibility(ArrayList<Action> history, int playerHandStrength);
	
	public double[] actionProbabilities(ArrayList<Action> history);
	
	public void input(ArrayList<Action> history, int handStrength);
}
