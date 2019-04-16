package it.polimi.ingsw.cg32.controller.turn.state;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByKingHelp;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuyPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.ElectCouncillor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.PerformAnotherPrimaryAction;
import it.polimi.ingsw.cg32.controller.turn.ActionTurnMachine;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;


/**
*
* This class is a Singleton used to models the phase of the turn where the Player
* has previously performed a PerformAnotherPrimaryAction.<br>
* Each Player enter in the PerformAnotherPrimaryState once he performed a PerformAnotherPrimaryAction.<br>
* When a Player is in this phase of the turn is only allowed to perform a primary action, 
* as specificated in {@link #isActionValid(Action)}.<br> 
* This State is used by the {@link ActionTurnMachine}.<br>
* 
* @author giovanni
* 
* @see Player
* @see PerformAnotherPrimaryAction
*
*/
public class PerformAnotherPrimaryState implements State {

	private static PerformAnotherPrimaryState instance = new PerformAnotherPrimaryState();
	
	/**
	* Private constructor to create an instance of PerformAnotherPrimaryState, it is 
	* invoke just one time. 
	*/
	private PerformAnotherPrimaryState() {}
	
	/**
	* Get the instance of the PerformAnotherPrimaryState.
	* 
	* @return instance the instance of the PerformAnotherPrimaryState.
	*/
	public static PerformAnotherPrimaryState getInstance() {
		
		return instance;
	}
	
	@Override
	public boolean isActionValid(Action action) throws IllegalStateMachineException {

		if(!(action instanceof BuildEmporiumByKingHelp ||
				action instanceof BuildEmporiumByPermitCard ||
				action instanceof BuyPermitCard ||
				action instanceof ElectCouncillor))
				throw new IllegalStateMachineException("you have to perform a primary action in this phase of your turn!");

		return true;
	}

}
