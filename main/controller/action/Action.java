package it.polimi.ingsw.cg32.controller.action;

import it.polimi.ingsw.cg32.controller.turn.TurnMachine;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;


/**
 * This interface provide two method two execute an {@link Action} 
 * and create a custom message response for the clinet with all
 * the information about the changes in the model.
 * 
 * @author Stefano
 *
 */
public interface Action {

	/**
	 * Executes the action.
	 * 
	 * @return the {@link ResponseMessage} of the executed action
	 */
	public ActionResponseMessage execute();
	
	/**
	 * Create a personalize message response for every type of {@link Action}
	 * with all the information about the executution.
	 * Every response must be have a {@link State} not null
	 * that will be set in the {@link TurnMachine}.
	 * 
	 * @return the {@link ResponseMessage} of the executed action
	 */
	public ActionResponseMessage createResponse(); 
}
