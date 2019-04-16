package it.polimi.ingsw.cg32.message.request;

import java.io.Serializable;

import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class rapresent an abstract Request message
 * that use the client to communicate with the server.
 * 
 * @author Stefano
 *
 */
public abstract class RequestMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	protected final Token token;
	
	/**
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 */
	public RequestMessage(Token token) {
		this.token = token;
	}
	
	/**
	 * @return the {@link Token} associated with the {@link Player} that send the request
	 */
	public Token getToken() {
		return token;
	}
}
