package it.polimi.ingsw.cg32.controller.turn;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.turn.state.State;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;

/**
 * Abstract class to rapresent a simple TurnMachine with
 * the method to set and get the current state and an abstract
 * method to execute an {@link Action} and return a {@link ResponseMessage}
 * 
 * @author Stefano
 *
 */
public abstract class TurnMachine {

	protected State currentState;

	/**
	* Get the curentState on which the ActionTurnMachine is working
	* 
	* @return currentState the instance of the State on which the ActionTurnMachine is working
	*/
	public State getCurrentState() {
		return currentState;
	}

	
	/**
	 * @param currentState the {@link State} to be set
	 */
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
	
	/**
	* This method is used to execute an Action.<br>
	* An Action has to pass through the {@link State#isActionValid(Action)}
	* of the currentState before his execution.<br>
	* It's used to check over the possibility of execution relative to 
	* the State of the {@link Player}'s turn .<br>
	* If is not possible to execute the Action an IllegalStateMachineException is thrown.<br> 
	* Once an Action is executed it set the currentState to the State obtained by the
	* {@link ActionResponseMessage} return by the Action.<br>
	* 
	* 
	* @param action the Action to perform.
	* 
	* @return the {@link ActionResponseMessage} associated with the action executed.
	* 
	* @throws IllegalStateMachineException if the player is not allowed
	* 		   to execute the Action in this phase of hi turn.
	* 
	* @throws NullPointerException if action is null
	* 
	* @see Action
	* @see State
	* 
	*/
	public abstract ActionResponseMessage executeAction(Action action) throws IllegalStateMachineException;

}
