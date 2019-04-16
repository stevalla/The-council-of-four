package it.polimi.ingsw.cg32.message.request.action;

import java.io.Serializable;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByPermitCard;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link MainActionRqst} and create a request
 * to execute an {@link BuildEmporiumByPermitCard} action controlling
 * its validation.<br>
 * This action is a primary action.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 * @see Action
 *
 */
public class BuildEmporiumByPermitCardRqst extends MainActionRqst {

	private static final long serialVersionUID = 1L;
	private City targetCity;
	private PermitCard selectedPermitCard;
	private final String permitCardId;
	private int assistantsToPay;
	
	/**
	 * Construct a {@link MainActionRqst} to build an {@link Emporium}
	 * by a {@link PermitCard}.
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param city the {@link City} where build the emporium
	 * @param permitCardId the id of the {@link PermitCard} to use
	 * @throws NullPointerException if token is null
	 */
	public BuildEmporiumByPermitCardRqst(Token token, City city, String permitCardId) {
		super(token);
		this.targetCity = city;
		this.permitCardId = permitCardId;
		this.assistantsToPay = 0;
	}

	
	@Override
	public Action accept(Game game, Player player, ActionVisitor visitor) throws IllegalActionException {
		
		super.accept(game, player, visitor);
		
		return isValid(game, player) ? visitor.visit(this, player, game) : null;
	}


	@Override
	protected boolean isValid(Game game, Player player) throws IllegalActionException {
		
		/*
		 * Check if the Player still has an emporium to build.
		 */
		if(player.getEmporiums().isEmpty())
			throw new IllegalActionException("You don't have avaiable emporium to execute this action.");
		
		/*
		 * Setting selectedPermitCard with its server version.
		 */
		selectedPermitCard = player.getPermitCards().stream()
								.filter(p -> p.getId().equals(permitCardId))
								.findFirst()
								.orElseThrow(() -> new IllegalActionException("The selected permit card is not yours."));
		
		/*
		 * Change city provided by player with its equivalent server's version.
		 */
		targetCity = game.getBoard().getCities().stream()
						 .filter(city -> city.equals(targetCity))
						 .findFirst()
						 .orElseThrow(() -> new IllegalActionException("The selected city doesn't exist."));
			
		/*
		 * Control that the permit card isn't already use.
		 */
		if(selectedPermitCard.isUsed()) 
			throw new IllegalActionException("You have already used the selected permit card.");
		
		/*
		 * Control if the city provided by the player is present in the city of the permitCard.
		 */
		if(!selectedPermitCard.getCity().contains(targetCity)) 
			throw new IllegalActionException("The selected city is not in the selected permit card.");
		
		/*
		 * Control if an emporium of the player is already built in the city
		 */
		for(City playerCity : player.getCities()) {
			if(playerCity.equals(this.targetCity)) 
				throw new IllegalActionException("You can't build two emporiums in the same city.");
		}
		
		/*
		 * Control if the player has enough assistant to build an emporium in the target city
		 */
		assistantsToPay = (int) targetCity.getEmporium().stream()
									.filter(e -> e.getPlayerId() != player.getId())
									.count();
		
		if(player.getAssistants().size() < assistantsToPay) 
			throw new IllegalActionException("You don't have enough assistants to build an emporium in the selected city.");
		
		return true;
	}
	

	/**
	 * @return the {@link City} where build the {@link Emporium}
	 */
	public City getCity() {
		return targetCity;
	}
	
	
	/**
	 * @return the player's {@link PermiCard} with the {@link City} 
	 */
	public PermitCard getSelectedPermitCard() {
		return selectedPermitCard;
	}

	
	/**
	 * @return the number of {@link Assistant} to pay to perform this {@link Action}
	 */
	public int getAssistantsToPay() {
		return assistantsToPay;
	}
}
