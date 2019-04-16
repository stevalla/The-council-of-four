package it.polimi.ingsw.cg32.message.response;

import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;

/**
* It directly extends the main class {@link ResponseMessage} for responses.<br>
* This response is used in general to comunicate with clients.<br>
* It covers several features as:<br>
* 
* - signals when is the turn of a client to sell stuff in the market<br>
* - signals when a {@link Player} ends its turn<br>
* - signals that an error occure performing an {@link Action} due to game's
* rule, specifying the cause of failure in the message<br>
* - signals when the game finished specifying the final ranking<br><br>
* 
* It contains a String that collect all the information to communicate to the client. 
* 
* @author giovanni
*
*/
public class CommunicationResponse extends ResponseMessage {

	private static final long serialVersionUID = 1L;
	private final String message;
	
	
	/**
	* Constructor for CommunicationResponse take care to set
	* the message that has to be delivered to the client
	* 
	* @param message the message that collect all the information to communicate to the client
	*/
	public CommunicationResponse(String message) {
		
		if(message == null)
			throw new NullPointerException("Message can't be null");
		
		this.message = message;
	}

	
	/**
	* Get the message that collect all the information to communicate to the client
	* 
	* @return message the message that collect all the information to communicate to the client
	*/
	public String getMessage() {
		return message;
	}

	
	@Override
	public void display(ResponseVisitor visitor) {

		super.display(visitor);
		
		visitor.display(this);
	}
}
