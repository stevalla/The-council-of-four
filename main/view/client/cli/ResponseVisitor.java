package it.polimi.ingsw.cg32.view.client.cli;

import it.polimi.ingsw.cg32.message.response.CommunicationResponse;
import it.polimi.ingsw.cg32.message.response.InitializeGameResponse;
import it.polimi.ingsw.cg32.message.response.StartTurnResponse;
import it.polimi.ingsw.cg32.message.response.TokenResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.message.response.market.MarketResponse;

/**
 * Visitor interface to avoid the use of instance of
 * in client side with Server Response. His function is to 
 * show message in console with information about the running game
 * and update the model in client side.<br>
 * The use of visitor in this case helps to reduce the use of instance of
 * because every object passed into socket is send as a generic
 * {@link Object}.
 * 
 * @author Stefano
 *
 */
public interface ResponseVisitor {

	/**
	 * Display an action response message and update game
	 * 
	 * @param response the response to display
	 */
	public void display(ActionResponse response);
	
	/**
	 * Display a bonuss response message and update game
	 * 
	 * @param response the response to display
	 */
	public void display(BonusResponse response);
	
	/**
	 * Display a market response message and update game
	 * 
	 * @param response the response to display
	 */
	public void display(MarketResponse response);
	
	/**
	 * Display the InitializeGame response message,
	 * this method is used to set the game at the
	 * start too.
	 * 
	 * @param response the response to display
	 */
	public void display(InitializeGameResponse response);
	
	/**
	 * Display the StartTurn response message
	 * 
	 * @param response the response to display
	 */
	public void display(StartTurnResponse response);
	
	/**
	 * @param response the response with the token
	 */
	public void display(TokenResponse response);
	
	/**
	 * Use to communicate with client
	 * 
	 * @param response the response with the communication
	 */
	public void display(CommunicationResponse response);

}