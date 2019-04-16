package it.polimi.ingsw.cg32.message.response;

import it.polimi.ingsw.cg32.message.request.RequestMessage;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;
import it.polimi.ingsw.cg32.view.server.Lobby;
import it.polimi.ingsw.cg32.view.server.Token;

/**
* As every response this class has to extend {@link ResponseMessage}.<br>
* This response is used to send a copy of Token that the server assign to each client
* to the client itself.<br>
* Thus enable the client to put the Token recived in all his {@link RequestMessage}
* in order to create a unique method to identify each client once the message arrived at server-side.<br>
* 
* This response is send firstly when the client join a {@link Lobby} waitingGame, carrying a Token with
* a special player number: 999<br>
* Secondly it is send when the game begin to change the Token at client-side with 
* the new version recived by server (this new version has a real Player's id to associated the 
* Token with a Player of the {@link Game}).
*  
* @author giovanni
* 
* @see Token
* @see Player
*
*/
public class TokenResponse extends ResponseMessage {

	private static final long serialVersionUID = 1L;
	private final Token token;
	
	/**
	* Constructor provides initialization of th {@link Token} carrying by the TokenResponse
	* avoiding a null token pass as param.
	* 
	* @param token the {@link Token} associated with the client
	* 
	* @throw NullPointerException if {@link Token} is nul
	*/
	public TokenResponse(Token token) {
		
		if(token == null)
			throw new NullPointerException("Token can't be null");
		
		this.token = token;
	}

	/**
	* Get the token that the server associated with the client
	* 
	* @return token the token that the server associated with the client
	*/
	public Token getToken() {
		return token;
	}

	
	@Override
	public void display(ResponseVisitor visitor) {

		super.display(visitor);
		
		visitor.display(this);
	}
}
