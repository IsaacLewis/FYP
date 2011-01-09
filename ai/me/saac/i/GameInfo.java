package me.saac.i;

public class GameInfo {
	public enum Dealer { PLAYER, OPPONENT };
	
	public int smallBetSize;
	public Dealer dealer;
	public OpponentModel opponentModel;
	
	public GameInfo(int sbs, Dealer d, OpponentModel om) {
		smallBetSize = sbs;
		dealer = d;
		opponentModel = om;
	}
}
