package it.polimi.ingsw.cg32.message.request;

import java.io.Serializable;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.model.game.Board;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This abstract class extends {@link RequestMessage} and provide two methods
 * to validate a request and then create an action command using pattern Visitor.<br>
 * This methods have only a simple implementation because every class of request,
 * to be conformed with Visitor pattern that encodes a logical operation on the 
 * whole hierarchy into a single class containing one method per type, should 
 * overrides the two methods to customize them.<br>
 * Implement {@link Serializable}.
 * 
 * @author Stefano
 * @see Visitor
 */
public abstract class ActionRqst extends RequestMessage {

	private static final long serialVersionUID = 1L;

	/**
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 */
	public ActionRqst(Token token) {
		super(token);
		
		if(token == null)
			throw new NullPointerException("Token can't be null");
	}

	
	/**
	 * Use the Visitor pattern to create an Action controlling
	 * before the creation the validation of the request's params.<br>
	 * Every class of request need overrides it to be conformed to Visitor pattern
	 * and passed it the visitor to create the Action.
	 * 
	 * @param board the {@link Board} of the {@link Game}
	 * @param player the {@link Player} who made the request
	 * @param visitor the {@link ActionVisitor} to create the relative {@link Action}
	 * @return the {@link Action} created
	 * @throws IllegalActionException if {@link Action} is not valid
	 * @throws NullPointerExeption if arguments are null
	 */
	public Action accept(Game game, Player player, ActionVisitor visitor) throws IllegalActionException {
		if(player == null || visitor == null)
			throw new NullPointerException("Arguments can't be null");
		
		return null;
	}
	
	/**
	 * Control if an {@link ActionRqst} is valid controlling
	 * if the request's params are correct using the game's rules.
	 * 
	 * @param board the {@link Board} of the {@link Game}
	 * @param player the {@link Player} who made the request
	 * @return true if the {@link Action} otherwise throw an {@link IllegalActionException}.
	 * @throws IllegalActionException if {@link Action} is not valid
	 * @throws NullPointerExeption if arguments are null
	 */
	protected boolean isValid(Game game, Player player) throws IllegalActionException {
		if(player == null || game == null)
			throw new NullPointerException("Board and Player can't be null");

		return true;
	}
}
