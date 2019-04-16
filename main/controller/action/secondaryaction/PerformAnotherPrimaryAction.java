package it.polimi.ingsw.cg32.controller.action.secondaryaction;

import java.util.Collections;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.PerformAnotherPrimaryState;
import it.polimi.ingsw.cg32.message.request.action.PerformAnotherPrimaryActionRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent a Perform Another Primary {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link PerformAnotherPrimaryActionRqst} is received and validated.<br>
 * This secondary action allows you to perform a second primary action
 * in the turn paying three assistants.
 * 
 * @author Stefano
 *
 */
public class PerformAnotherPrimaryAction implements Action {

	private static final int ASSISTANT_FOR_ANOTHER_PRIMARY_ACTION = 3;
	private final Player player;
	private final Game game;
	
	/**
	 * Construct a Perform Another Primary Action.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * 
	 * @see Action 
	 */
	public PerformAnotherPrimaryAction(Player player, Game game) {
		this.game = game;
		this.player = player;
	}
	
	@Override
	public ActionResponseMessage execute() {

		/*
		 * Pay three assistant to execute the primary action
		 */
		for(int i = 0; i < ASSISTANT_FOR_ANOTHER_PRIMARY_ACTION; i++)
			game.getBoard().getAssistantsReserve().getAssistants().add(player.getAssistants().poll());
		
		return createResponse();

	}

	@Override
	public ActionResponseMessage createResponse() {
		
		String message = "Player#" + player.getId() + " perform a PERFORMANOTHERPRIMARYACTION.\n"
					   + "He is now allowed to perform another primary action.\n"
					   + "Now he owns " + player.getAssistants().size() + " assistants.\n";
						
		
		return new ActionResponse(game, PerformAnotherPrimaryState.getInstance(), Collections.emptyList(), message, player.getId());
	}

}
