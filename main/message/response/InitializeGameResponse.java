package it.polimi.ingsw.cg32.message.response;

import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;

/**
* This response is used to initialize the Game at client's side once the game started.<br>
* It carries a snapshot of the Game when this is created. It's used to give to clients
* their initial copy of server's model.
* 
* @author giovanni
* 
* @see Game
*
*/
public class InitializeGameResponse extends ResponseMessage {

	private static final long serialVersionUID = 1L;
	private final Game game;
	
	/**
	* Constructor for InitializeGameResponse, it set the field game
	* with the game pass as parmam that Should be its initial snapshot. 
	* 
	* @param game the game at his beginnig state
	* 
	* @throws NullPointerException if the argument game is nul 
	*/
	public InitializeGameResponse(Game game) {
		
		if(game == null)
			throw new NullPointerException("Game can't be null");
		
		this.game = game;
			
	}

	/**
	* Get the game stored in the response
	* 
	* @return game the game at his beginnig state
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
