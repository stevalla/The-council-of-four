package it.polimi.ingsw.cg32.controller.action.primaryaction;

import java.util.Collections;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.PrimaryActionState;
import it.polimi.ingsw.cg32.message.request.action.ElectCouncillorRqst;
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
 * This class rapresent a Elect Councillor {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link ElectCouncillorRqst} is received and validated.<br>
 * This primary action allows the player to take one councillor from the reserve
 * and put it in the {@link Balcony} of the selected {@link Region} and get
 * four coins.
 * 
 * @author Stefano
 *
 */
public class ElectCouncillor implements Action {

	private static final int RECEIVED_COINS = 4;
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
	public ElectCouncillor(Player player, Game game, Region targetRegion, Councillor councillor) {
		this.game = game;
		this.player = player;
		this.targetRegion = targetRegion;
		this.councillor = councillor;
	}

	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Put in the balcony the councillor selected
		 */
		targetRegion.getBalcony().electCouncillor(game.getBoard().getCouncillorReserve(), councillor);
		
		/*
		 * Give to the player 4 coins
		 */
		player.getCoinsTrack().setCoinsNumber(RECEIVED_COINS);
		
		return createResponse();
	}

	@Override
	public ActionResponseMessage createResponse() {
		
		String region;
		
		if(targetRegion instanceof KingRegion)
			region = "KING";
		else
			region = ((NormalRegion) targetRegion).getRegionType().toString();
		
		String message = "Player#" + player.getId() + " perform an ELECTCOUNCILLOR.\n"
					   + "He elect a " + councillor.getColor() + " councillor in region " + region + ".\n"
					   + "The current state of the balcony of the region " + region + " is:\n"
					   + targetRegion.getBalcony().getSeats() + "\n"
					   + "Now he has " + player.getCoinsTrack().getCurrentCoins() + " coins.\n";
		
		return new ActionResponse(game, PrimaryActionState.getInstance(), Collections.emptyList(), message, player.getId());
	}

}
