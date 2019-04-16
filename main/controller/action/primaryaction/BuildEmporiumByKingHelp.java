package it.polimi.ingsw.cg32.controller.action.primaryaction;

import java.util.List;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByKingHelpRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.balcony.Balcony;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent a Build Emporium By King Help {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link BuildEmporiumByKingHelpRqst} is received and validated.<br>
 * This primary action allows the player to build an {@link Emporium} satisfying
 * the king's {@link Balcony}, the player have to move the king in the city
 * where he want built and pay two coins for every road crossed by the King.
 * 
 * @author Stefano
 *
 */
public class BuildEmporiumByKingHelp extends BuildEmporium {

	private final int moneyToPay;
	private final List<PoliticCard> politicCards;

	/** 
	 * Construct a Build Emporium By King Help.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param city the {@link City} where build the {@link Emporium}
	 * @param assistantsToPay the {@link Assistant} to pay to execute the actions
	 * 		  calculate one for each emporiums of the other players there are in 
	 * 		  the selected city
	 * @param moneyToPat the money to pay to execute this action, calculate baes od the game's rules
	 * @param politicCards the politic cards to satisfy the king's balcony
	 * 
	 * @see Balcony 
	 * @see King 
	 * @see PoliticCard
	 */
	public BuildEmporiumByKingHelp(Player player, Game game, City city, int assistantsToPay, 
			int moneyToPay, List<PoliticCard> politicCards) {
		super(player, game, city, assistantsToPay);
		this.moneyToPay = moneyToPay;
		this.politicCards = politicCards;
	}

	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Build the emporium
		 */
		super.execute();
		
		/*
		 * Remove from the player the politic cards played
		 */
		politicCards.stream().forEach(player.getPoliticCards()::remove);
				
		/*
		 * Move the King in the selected city
		 */
		game.getBoard().getKing().setCity(city);

		/*
		 * Pay the money to execute this action
		 */
		player.getCoinsTrack().setCoinsNumber(-moneyToPay);

		return createResponse();
	}

	@Override
	public ActionResponseMessage createResponse() {

		/*
		 * Merge the response of the build emporium super class with subclass response.
		 */
		ActionResponseMessage response = super.createResponse();
		
		String message = "Player#" + player.getId() + " perform a BUILEMPORIUMBYKINGHELP.\n"
					   + "He satisfy king's balcony usign " + politicCards + "\n"
					   + "He moved king to " + city.getName() + ", building in it.\n" + response.getMessage();
		
		return new ActionResponse(game, response.getState(), response.getBonus(), message, player.getId());
	}

}
