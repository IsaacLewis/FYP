package me.saac.i.ai;
import java.util.ArrayList;
import me.saac.i.ai.GameInfo.*;
import me.saac.i.ai.GameState.*;

public class GameStateTest {
    public static void main(String[] args) {
    GameInfo gi = new GameInfo(10, Dealer.PLAYER, new BasicOpponentModel());
    ActionList ah = new ActionList();
    ArrayList<GameState> gs = new ArrayList<GameState>();
	gs.add(new GameState(NodeType.CHANCE, BettingRound.FLOP, 0, 0, new CardArray(), ah, gi));
	gs.add(gs.get(gs.size() - 1).successor(null));
	gs.add(gs.get(gs.size() - 1).successor(Action.CHECK));
	gs.add(gs.get(gs.size() - 1).successor(Action.CHECK));
	gs.add(gs.get(gs.size() - 1).successor(null));
	gs.add(gs.get(gs.size() - 1).successor(Action.CHECK));
	gs.add(gs.get(gs.size() - 1).successor(Action.RAISE));
	gs.add(gs.get(gs.size() - 1).successor(Action.RAISE));
	gs.add(gs.get(gs.size() - 1).successor(Action.RAISE));
	gs.add(gs.get(gs.size() - 1).successor(Action.RAISE));
	gs.add(gs.get(gs.size() - 1).successor(Action.RAISE));
	gs.add(gs.get(gs.size() - 1).successor(Action.CHECK));
	gs.add(gs.get(gs.size() - 1).successor(null));
	gs.add(gs.get(gs.size() - 1).successor(Action.CHECK));
	gs.add(gs.get(gs.size() - 1).successor(Action.CHECK));
	for(GameState g : gs) {
		System.out.println(g.print() + "\nEV: " + g.EV());
	}
    }
}