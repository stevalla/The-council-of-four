package it.polimi.ingsw.cg32.message.response;

import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;

/**
* As every response this class has to extend {@link ResponseMessage}.<br>
* This response is send to the client when it begun its turn.<br>
* It carries an updated game, the {@link PoliticCard} that the {@link Player} draw
* at the beginning og his turn and a message that indicate the details of the executed operation.<br>
* This is a non-BroadcastMessage because it is send specifically only to the client that 
* is beginning his turn.<br>
* 
* @author giovanni
*
*/
public class StartTurnResponse extends ResponseMessage {

	private static final long serialVersionUID = 1L;
	private final String message;
	private final PoliticCard cardDrawed;
	private final Game game;
	
	/**
	* Constructor initialize all the fields of StartTurnResponse, it checks if params are null
	* in that case throws NullPointerException.
	* 
	* @param game the updated version of the {@link Game}
	* @param cardDrawed the card that the {@link Player} has drawn at the beginnig of his turn
	* @param message the message that indicate details of the executed operation
	* 
	* @throws NullPointerException if game, cardToDraw or message are null
	*/
	public StartTurnResponse(Game game, PoliticCard cardDrawed, String message) {
		
		if(game == null || cardDrawed == null || message == null)
			throw new NullPointerException("Param can't be null");
		
		this.message = message;
		this.game = game;
		this.cardDrawed = cardDrawed;
	}

	/**
	* Get the card that the {@link Player} has drawn at the beginnig of his turn
	* 
	* @return cardToDarw the card that the {@link Player} has drawn at the beginnig of his turn
	*/
	public PoliticCard getCardDrawed() {
		return cardDrawed;
	}
	

	/**
	* Get the message that indicate details of the executed operation 
	* 
	* @return message the message that indicate details of the executed operation
	*/
	public String getMessage() {
		return message;
	}

	
	/**
	* Get the updated version of the {@link Game}
	* 
	* @return game the updated version of the {@link Game}
	*/
	public Game getGame() {
		return game;
	}

	@Override
	public void display(ResponseVisitor visitor) {

		super.display(visitor);
		
		visitor.display(this);
	}
}
