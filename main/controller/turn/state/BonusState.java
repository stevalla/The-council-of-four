package it.polimi.ingsw.cg32.controller.turn.state;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.bonus.BonusAction;
import it.polimi.ingsw.cg32.controller.turn.ActionTurnMachine;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;

/**
* This class is a Singleton used to models the phase of the turn where the Player
* has to execute the {@link Bonus} he won performing his previous action.<br>
* Each Player enter in the BonusState when he won at least one {@link Bonus} 
* during his previos action.<br>
* When a Player is in this phase of the turn is only allowed to perform a bonus action, 
* as specificated in {@link #isActionValid(Action)}.<br> 
* This State is used by the {@link ActionTurnMachine}.<br>
* 
* @author giovanni
* 
* @see Player
*
*/
public class BonusState implements State {

	private static BonusState instance = new BonusState();
	
	/**
	* Private constructor to create an instance of BonusState, it is 
	* invoke just one time. 
	*/
	private BonusState() {}
	
	/**
	* Get the instance of the BonusState.
	* 
	* @return instance the instance of the BonusState.
	*/
	public static BonusState getInstance() {
		return instance; 
	}
	
	@Override
	public boolean isActionValid(Action action) throws IllegalStateMachineException {

		if(!(action instanceof BonusAction)) 
			throw new IllegalStateMachineException("You can't perform an action while bonus are in execution.");
		
		return true;
	}

}
