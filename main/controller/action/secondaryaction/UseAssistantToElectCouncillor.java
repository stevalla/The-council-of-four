package it.polimi.ingsw.cg32.controller.action.secondaryaction;

import java.util.Collections;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.SecondaryActionState;
import it.polimi.ingsw.cg32.message.request.action.UseAssistantToElectCouncillorRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.balcony.Balcony;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.KingRegion;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.model.region.Region;

/**
 * This class rapresent an Use Assistant To Elect Councillor {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link UseAssistantToElectCouncillorRqst} is received and validated.<br>
 * This secondary action allows you to take one councillor from the reserve
 * and put it in the {@link Balcony} of the selected {@link Region} paying
 * one assistant.
 * 
 * @author Stefano
 *
 */
public class UseAssistantToElectCouncillor implements Action {

	private final Player player;
	private final Game game;
	private final Region targetRegion;
	private final Councillor councillor;
	
	/**
	 * Construct an Use Assistant To Elect Councillor.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param targetRegion the selected {@link NormalRegion} where change the usable cards
	 * @param councillor the {@link Councillor} to put in the {@link Balcony}
	 * 
	 * @see Action 
	 */
	public UseAssistantToElectCouncillor(Player player, Game game, Region targetRegion, Councillor councillor) {
		this.game = game;
		this.player = player;
		this.targetRegion = targetRegion;
		this.councillor = councillor;
	}
	
	
	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Pay one assistant to execute this action
		 */
		game.getBoard().getAssistantsReserve().getAssistants().add(player.getAssistants().poll());
		
		/*
		 * Put in the balcony the councillor elected
		 */
		targetRegion.getBalcony().electCouncillor(game.getBoard().getCouncillorReserve(), councillor);
		
		
		return createResponse();
	}


	@Override
	public ActionResponseMessage createResponse() {
		String region;
		
		if(targetRegion instanceof KingRegion)
			region = "KING";
		else
			region = ((NormalRegion) targetRegion).getRegionType().toString();
		
		String message = "Player#" + player.getId() + " perform an USEASSISTANTTOELECTCOUNCILLOR.\n"
					   + "He elect a " + councillor.getColor() + " councillor in region " + region + ".\n"
					   + "The current state of the balcony of the region " + region + " is:\n"
					   + targetRegion.getBalcony().getSeats() + "\n"
					   + "Now he owns " + player.getAssistants().size() + " assistants.\n";
		
		return new ActionResponse(game, SecondaryActionState.getInstance(), Collections.emptyList(), message, player.getId());
	}

}
