package it.polimi.ingsw.cg32.controller.turn.state;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.bonus.BonusAction;
import it.polimi.ingsw.cg32.controller.action.market.MarketBuy;
import it.polimi.ingsw.cg32.controller.action.market.MarketSell;
import it.polimi.ingsw.cg32.controller.turn.ActionTurnMachine;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;



/**
* This class is a Singleton used to models the start phase of the turn.<br>
* Each Player enter in the StartState at the beginning of his turn.
* In this phase of the Player's turn is possible to perform any primary or secondary action.
* This State is used by the {@link ActionTurnMachine}.<br>
* 
* @author giovanni
* 
* @see Player
*
*/
public class StartState implements State {
	
	private static StartState instance = new StartState();
	
	/**
	* Private constructor to create an instance of StartState, it is 
	* invoke just one time. 
	*/
	private StartState() {}
	
	/**
	* Get the instance of the StartState.
	* 
	* @return instance the instance of the StartState.
	*/
	public static StartState getInstance() {
		return instance;
	}
	
	@Override
	public boolean isActionValid(Action action) throws IllegalStateMachineException {
		
		if(action instanceof BonusAction ||
		   action instanceof MarketBuy ||
		   action instanceof MarketSell)
				throw new IllegalStateMachineException("you have to perform a primary action in this phase of your turn!");
				
		return true;
	}

}
