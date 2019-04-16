package it.polimi.ingsw.cg32.message.request.action;

import java.io.Serializable;

import it.polimi.ingsw.cg32.message.request.ActionRqst;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * Abstract class for the main actions fo the game.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public abstract class MainActionRqst extends ActionRqst {

	private static final long serialVersionUID = 1L;

	/**
	 * @param token the {@link Token} of the request
	 * @throws NullPointerException if token is null
	 */
	public MainActionRqst(Token token) {
		super(token);
	}

}
