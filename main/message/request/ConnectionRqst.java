package it.polimi.ingsw.cg32.message.request;

import java.io.Serializable;

import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link RequestMessage} and it rapresents
 * a request to connect to a game from a client.<br>
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public class ConnectionRqst extends RequestMessage {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 */
	public ConnectionRqst(Token token) {
		super(token);
	}	

}
