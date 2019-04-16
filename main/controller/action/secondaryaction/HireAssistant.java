package it.polimi.ingsw.cg32.controller.action.secondaryaction;

import java.util.Collections;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.SecondaryActionState;
import it.polimi.ingsw.cg32.message.request.action.HireAssistantRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;

/**
 * This class rapresent an Hire Assistant {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link HireAssistantRqst} is received and validated.<br>
 * This secondary action allows you to chagne the usable cards of a 
 * {@link NormalRegion} paying three coins.
 * 
 * @author Stefano
 *
 */
public class HireAssistant implements Action {
	
	private static final int COINS_TO_PAY = -3;
	private final Player player;
	private final Game game;
	
	/**
	 * Construct an Hire Assistant.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * 
	 * @see Action 
	 */
	public HireAssistant(Player player, Game game) {
		this.game = game;
		this.player = player;
	}

	
	@Override
	public ActionResponseMessage execute() {
			player.getCoinsTrack().setCoinsNumber(COINS_TO_PAY);
			player.getAssistants().add(game.getBoard().getAssistantsReserve().getAssistants().poll());
			
			return createResponse();
	}


	@Override
	public ActionResponseMessage createResponse() {

		String message = "Player#" + player.getId() + " perform an HIREASSISTANT.\n"
					   + "Now he has " + player.getCoinsTrack().getCurrentCoins() + " coins.\n"
					   + "Now he owns " + player.getAssistants().size() + " assistants.\n";
		
		return new ActionResponse(game, SecondaryActionState.getInstance(), Collections.emptyList(), message, player.getId());
	}

}
