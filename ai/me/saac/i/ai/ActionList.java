package me.saac.i.ai;

import java.util.ArrayList;

import me.saac.i.ai.GameInfo.Dealer;
import me.saac.i.ai.GameState.Action;

public class ActionList extends ArrayList<Action> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	int numberOfRaises() { return numberOfActions(Action.RAISE);}
	
	int numberOfChecks() { return numberOfActions(Action.CHECK);}
	
	int numberOfFolds() { return numberOfActions(Action.FOLD);}
	
	int numberOfActions(Action action) {
	    // calculate number of actions of type a in history
		int num = 0;
		for(Action a : this) {
			if(a == action) {
				num++;
			}
		}
		return num;
	}

	// returns just the actions performed by the opponent
	// TODO: error NullPointer when player in no1

	ActionList opponentActions(GameInfo gameInfo) {
		if(this.size() == 0) return this;
		
		ActionList opponentActions = new ActionList();
		
		// keeps track of which actions belong to OPPONENT
		boolean opponentsTurn;
	
		// initialize playerTurn as appropriate
		if(gameInfo.dealer == Dealer.OPPONENT) opponentsTurn = true;
		else opponentsTurn = false;
		
		for(Action a : this) {
			if(a == Action.DEAL) {
				if(gameInfo.dealer == Dealer.OPPONENT) opponentsTurn = true;
				else opponentsTurn = false;		
			} else {
				if(opponentsTurn) {
					opponentActions.add(a);
					opponentsTurn = false;
				} else {
					opponentsTurn = true;
				}
			}
		}
		
		return opponentActions;
	}
	
	public Action lastAction() {
		return this.get(this.size() - 1);
	}
}
