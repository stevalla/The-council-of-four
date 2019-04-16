package it.polimi.ingsw.cg32.controller.turn.state;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;

/**
* This is the general interface that is implemented by all the kinf of states.<br>
* This interface provides two methods {@link #isActionValid(Action)} and 
* {@link #executeAction(Action)} to validate the execution of an {@link Action}.<br>
* 
* The implementation of {@link #executeAction(Action)} is of default, and it's used
* by all the concrete instance of this interface.
* 
* @author giovanni
*
*/
@FunctionalInterface
public interface State {

	/**
	* This method is used to convalidate the execution of an Action.<br>
	* The concrete implementation of this method check if the {@link Player} who requested
	* the execution of the Action is in the correct State to execute that Action.<br>
	* It throws an IllegalStateMachineException if the Action can't be executed because
	* the {@link Player} isn't in the correct State of his turn to execute the Action, with a
	* relative costumized message that explains the cause of the failure. 
	* 
	* 
	* @param action the Action to execute
	* @return true, if the action can be executed
	* @throws IllegalStateMachineException if the Action can't be executed because the 
	* 		  {@link Player} isn't in the correct State of his turn
	* 
	* @see Action
	* @see IllegalStateMachineException
	*/
	public boolean isActionValid(Action action) throws IllegalStateMachineException;
	
	
	/**
	* This method is default implementation.<br>
	* It is the final step of the action-chain execution. It performs the Action.<br>
	* 
	* @param action the Action to execute
	* @return the {@link ActionResponseMessage} of the type associated with the Action. 
	* 
	* @throws NullPointerException if action is null.
	* @see Action
	*/
	public default ActionResponseMessage executeAction(Action action){
			
		if(action == null)
			throw new NullPointerException("Action can't be null");
			
		return action.execute();
	}
	
}
