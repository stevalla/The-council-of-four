package it.polimi.ingsw.cg32.controller.turn.state;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByKingHelp;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuyPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.ElectCouncillor;
import it.polimi.ingsw.cg32.controller.turn.ActionTurnMachine;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;


/**
* This class is a Singleton used to models the phase of the turn where the Player
* has already performed his secondary action.<br>
* Each Player enter in the SecondaryActionState once he performed a secondary action.<br>
* When a Player is in this phase of the turn is only allowed to perform a primary action, 
* as specificated in {@link #isActionValid(Action)}.<br> 
* This State is used by the {@link ActionTurnMachine}.<br>
* 
* @author giovanni
* 
* @see Player
*
*/
public class SecondaryActionState implements State {

	private static SecondaryActionState instance = new SecondaryActionState();
	
	/**
	* Private constructor to create an instance of SecondaryActionState, it is 
	* invoke just one time. 
	*/
	private SecondaryActionState() {}
	
	/**
	* Get the instance of the SecondaryActionState.
	* 
	* @return instance the instance of the SecondaryActionState.
	*/
	public static SecondaryActionState getInstance() {
		return instance;
	}
	
	@Override
	public boolean isActionValid(Action action) throws IllegalStateMachineException {
		
		if(!(action instanceof BuildEmporiumByKingHelp ||
			 action instanceof BuildEmporiumByPermitCard ||
			 action instanceof BuyPermitCard ||
			 action instanceof ElectCouncillor)) 
			throw new IllegalStateMachineException("you have to perform a primary action in this phase of your turn!");
			
		return  true;
	}	
}
