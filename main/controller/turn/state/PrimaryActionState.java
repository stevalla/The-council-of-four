package it.polimi.ingsw.cg32.controller.turn.state;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.ChangeUsablePermitCard;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.HireAssistant;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.PerformAnotherPrimaryAction;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.UseAssistantToElectCouncillor;
import it.polimi.ingsw.cg32.controller.turn.ActionTurnMachine;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;



/**
* This class is a Singleton used to models the phase of the turn where the Player
* has already performed his primary action.<br>
* Each Player enter in the PrimaryActionState once he performed a primary action.<br>
* When a Player is in this phase of the turn is only allowed to perform a secondary action, 
* as specificated in {@link #isActionValid(Action)}.<br> 
* This State is used by the {@link ActionTurnMachine}.<br>
* 
* @author giovanni
* 
* @see Player
*
*/
public class PrimaryActionState implements State {

	private static PrimaryActionState instance = new PrimaryActionState();
	
	/**
	* Private constructor to create an instance of PrimaryActionState, it is 
	* invoke just one time. 
	*/
	private PrimaryActionState() {}
	
	/**
	* Get the instance of the PrimaryActionState.
	* 
	* @return instance the instance of the PrimaryActionState.
	*/
	public static PrimaryActionState getInstance() {
		return instance;
	}
	
	@Override
	public boolean isActionValid(Action action) throws IllegalStateMachineException {

		if(!(action instanceof ChangeUsablePermitCard ||
				action instanceof HireAssistant ||
				action instanceof PerformAnotherPrimaryAction ||
				action instanceof UseAssistantToElectCouncillor)) 
			throw new IllegalStateMachineException("you have to perform a secondary action in this phase of your turn!");

		return true;
	}

}
