package it.polimi.ingsw.cg32.message.request.action;

import java.io.Serializable;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.PerformAnotherPrimaryAction;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link MainActionRqst} and create a request
 * to execute a {@link PerformAnotherPrimaryAction} action controlling
 * its validation.<br>
 * This action offer the possibility to execute a new primary action even if
 * it is a secondary action.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 * @see Action
 *
 */
public class PerformAnotherPrimaryActionRqst extends MainActionRqst {
	
	private static final long serialVersionUID = 1L;
	private static final int ASSISTANT_FOR_ANOTHER_PRIMARY_ACTION = 3;

	/**
	 * Construct a {@link MainActionRqst} to perform another primary {@link Action}.
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @throws NullPointerException if token is null
	 */
	public PerformAnotherPrimaryActionRqst(Token token) {
		super(token);
	}

	@Override
	public Action accept(Game game, Player player, ActionVisitor visitor) throws IllegalActionException {
		
		super.accept(game, player, visitor);
		
		return isValid(game, player) ? visitor.visit(this, player, game) : null;
	}
	
	@Override
	protected boolean isValid(Game game, Player player) throws IllegalActionException{
		
		super.isValid(game, player);
		
		if(player.getAssistants().size() <  ASSISTANT_FOR_ANOTHER_PRIMARY_ACTION) 
			throw new IllegalActionException("you need at least 3 assistants to execute this action.");
		
		return true;
	}

}
