package it.polimi.ingsw.cg32.message.request.action;

import java.io.Serializable;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.HireAssistant;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link MainActionRqst} and create a request
 * to execute a {@link HireAssistant} action controlling
 * its validation.<br>
 * This action offer the possibility to an assistant from the {@link AssistantReserve}
 * paying three coins.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 * @see Action
 *
 */
public class HireAssistantRqst extends MainActionRqst {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Construct a {@link MainActionRqst} to perform hire an {@link Assistant}.
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @throws NullPointerException if token is null
	 */
	public HireAssistantRqst(Token token) {
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
		
		if(player.getCoinsTrack().getCurrentCoins() < 3) 
			throw new IllegalActionException("You need at least 3 coins to execute this action!");
		
		if(game.getBoard().getAssistantsReserve().getAssistants().isEmpty()) 
			throw new IllegalActionException("There aren't enough assistant in reserve to execute the action!");
		
		return true;
	}

}
