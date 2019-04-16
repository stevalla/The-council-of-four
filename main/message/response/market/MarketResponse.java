package it.polimi.ingsw.cg32.message.response.market;

import java.util.Collections;

import it.polimi.ingsw.cg32.controller.Market;
import it.polimi.ingsw.cg32.controller.action.market.MarketBuy;
import it.polimi.ingsw.cg32.controller.turn.MarketTurnMachine;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;

/**
* This class models a response forthe phase of market.<br>
* It extends ActionResponseMessage as all the response that
* notify clients about changes of the model on server-side.<br>
* 
* It is used either as response for {@link MarketBuy} and {@link Marketsell}.<br>
* 
* it carries the updated shopping-cart as well as all the ActionResponseMessage's fields but
* State is set to null because {@link MarketTurnMachine} doesn't check the state of the responses
* to change its current State 
* 
* @author giovanni
* 
* @see Market ActionResponseMessage State
*
*/
public class MarketResponse extends ActionResponseMessage {

	private static final long serialVersionUID = 1L;

	/**
	* Constuctor for MarketResponse, provides initialization of all the fields of the response
	* using super-constructor of {@link ActionResponseMessage}.<br>
	* State is set to null because {@link MarketTurnMachine} doesn't check the state of the responses
	* to change its current State
	* 
	* @param game the updated game
	* @param message the message that indicate details of the executed operation
	*/
	public MarketResponse(Game game, String message,int playerId) {
		super(game, null, Collections.emptyList(), message, playerId);
	}
	
	
	@Override
	public void display(ResponseVisitor visitor) {

		super.display(visitor);
		
		visitor.display(this);

	}

}
