package it.polimi.ingsw.cg32.controller.action.primaryaction;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByPermitCardRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent a Build Emporium By Permit Card {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link BuildEmporiumByPermitCardRqst} is received and validated.<br>
 * This primary action allows the player to build an {@link Emporium} used one of the
 * players's permit cards. The player must pay assistants for each emporiums
 * of the other players in the selected city.
 * After this action the permit card is used.
 * 
 * @author Stefano
 *
 */
public class BuildEmporiumByPermitCard extends BuildEmporium {
	
	private final PermitCard selectedPermitCard;

	/** 
	 * Construct a Build Emporium By Permit Card.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param city the {@link City} where build the {@link Emporium}
	 * @param assistantsToPay the {@link Assistant} to pay to execute the actions
	 * 		  calculate one for each emporiums of the other players there are in 
	 * 		  the selected city
	 * @param selectedPermitCard the permit card to use to build the emporium
	 */
	public BuildEmporiumByPermitCard(Player player, Game game, City city, int assistantsToPay, PermitCard selectedPermitCard) {
		super(player, game, city, assistantsToPay);
		this.selectedPermitCard = selectedPermitCard;
	}

	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Build the emporium
		 */
		super.execute();
		
		/*
		 * Set to used the permit card
		 */
		selectedPermitCard.useCard();
		
		return createResponse();
	}

	@Override
	public ActionResponseMessage createResponse() {
		
		/*
		 * Merge the response of the build emporium super class with subclass response.
		 */
		ActionResponseMessage response = super.createResponse();
		
		String message = "Player#" + player.getId() + " perform a BUILEMPORIUMBYPERMITCARD.\n"
					   + "He used " + selectedPermitCard
					   + "To build an emporium in " + city.getName() + "\n" + response.getMessage();
		
		return new ActionResponse(game, response.getState(), response.getBonus(), message, player.getId());
	}
	
	
}
