package it.polimi.ingsw.cg32.controller.action.secondaryaction;


import java.util.Collections;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.SecondaryActionState;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;

/**
 * This class rapresent a Change Usable Permit Card {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link ChanegeUsablePermitCardRqst} is received and validated.<br>
 * This secondary action allows you to change the usable cards of a region
 * paying one assistant.
 * 
 * @author Stefano
 *
 */
public class ChangeUsablePermitCard implements Action {

	private final Player player;
	private final Game game;
	private final NormalRegion targetRegion;
	
	/**
	 * Construct a Change Usable Permit Card.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param targetRegion the selected {@link NormalRegion} where change the usable cards
	 * 
	 * @see Action 
	 */
	public ChangeUsablePermitCard(Player player, Game game, NormalRegion targetRegion) {
		this.player = player;
		this.game = game;
		this.targetRegion = targetRegion;
	}
	
	@Override 
	public ActionResponseMessage execute() {
		
		/*
		 * Change the usable card of the selected region
		 */
		targetRegion.getDeckOfPermitCard().changeUsableCard();
		
		/*
		 * Pay one assistant to the reserve
		 */
		game.getBoard().getAssistantsReserve().getAssistants().add(player.getAssistants().poll());
		
		return createResponse();
	}

	@Override
	public ActionResponseMessage createResponse() {

		String message = "Player#" + player.getId() + " perform a CHANGEUSABLEPERMITCARD.\n"
					   + "He swap cards in region " + targetRegion.getRegionType() + ".\n"
					   + "Now he owns " + player.getAssistants().size() + " assistants.\n";
		
		return new ActionResponse(game, SecondaryActionState.getInstance(), Collections.emptyList(), message, player.getId());
	}

}
