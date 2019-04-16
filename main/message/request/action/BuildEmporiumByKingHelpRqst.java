package it.polimi.ingsw.cg32.message.request.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByKingHelp;
import it.polimi.ingsw.cg32.model.King;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.balcony.Balcony;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link MainActionRqst} and create a request
 * to execute an {@link BuildEmporiumByKingHelp} action controlling
 * its validation.<br>
 * This action is a primary action.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 * @see Action
 *
 */
public class BuildEmporiumByKingHelpRqst extends MainActionRqst implements SatisfyCouncillor {

	private static final long serialVersionUID = 1L;
	private City targetCity;
	private int moneyToPay;
	private final List<Colore> colors;
	private final List<PoliticCard> politicCards;
	private int assistantsToPay;
	
	/**
	 * Construct a {@link MainActionRqst} to build an {@link Emporium}
	 * by the {@link King} help.
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param city the {@link City} where build the emporium
	 * @param colore the List of {@link Colore} of the List of {@link PoliticCard} to satisfy the balcony of {@link King}
	 * @throws NullPointerException if token or colors are null
	 */
	public BuildEmporiumByKingHelpRqst(Token token, City city, List<Colore> colors) {
		super(token);
		
		if(colors == null)
			throw new NullPointerException("Colors can't be null");
		
		this.targetCity = city;
		this.colors = colors;
		this.assistantsToPay = 0;
		this.moneyToPay = 0;
		this.politicCards = new ArrayList<>();
	}


	@Override
	public Action accept(Game game, Player player, ActionVisitor visitor) throws IllegalActionException {
		
		super.accept(game, player, visitor);
		
		return isValid(game, player) ? visitor.visit(this, player, game) : null;
	}

	
	@Override
	public boolean isValid(Game game, Player player) throws IllegalActionException {
		
		/*
		 * Check if the Player still has an emporium to build.
		 */
		if(player.getEmporiums().isEmpty())
			throw new IllegalActionException("You don't have avaiable emporium to execute this action.");
		
		/*
		 * Find which cards the player want to discard, controlling that he is corrently owning that cards.
		 */
		this.politicCards.addAll(getPoliticCardByColors(colors, player));
		
		/*
		 * Change city provided by player with its equivalent server's version.
		 */
		targetCity = game.getBoard().getCities().stream()
						 .filter(city -> city.equals(targetCity))
						 .findFirst()
						 .orElseThrow(() -> new IllegalActionException("The selected city doesn't exist."));
		
		/*
		 * Control if an emporium of the player is already built in the city
		 */
		for(City playerCity : player.getCities()) {
			if(playerCity.equals(this.targetCity)) 
				throw new IllegalActionException("You can't build two emporiums in the same city.");
		}

		assistantsToPay = (int) targetCity.getEmporium().stream()
									.filter(e -> e.getPlayerId() != player.getId())
									.count();
		
		if(player.getAssistants().size() < assistantsToPay) 
			throw new IllegalActionException("You don't have enough assistants to build an emporium in the selected city.");
		
		/*
		 * Calculate how much money he has to pay to move the king to the target city
		 */
		this.moneyToPay = 2 * DijkstraShortestPath.findPathBetween(game.getBoard().getCityGraph(), game.getBoard().getKing().getCity(), targetCity)
								.size();
		
		/*
		 * Check if the discard politic card satisfy councillor and return the amount of coins to pay.
		 */
		this.moneyToPay += countMoneyToSatisfyCouncillor(politicCards, player, game.getBoard().getKingRegion());
		
		if(player.getCoinsTrack().getCurrentCoins() < moneyToPay) 
			throw new IllegalActionException("You need more coins to execute this action.");
		
		return true;	
	}

	/**
	 * @return the {@link City} where build the {@link Emporium}
	 */
	public City getCity() {
		return targetCity;
	}

	/**
	 * @return the List of {@link PoliticCard} of the {@link Player}
	 */
	public List<PoliticCard> getPoliticCards() {
		return politicCards;
	}

	/**
	 * @return the number of {@link Assistant} to pay to perform this {@link Action}
	 */
	public int getAssistantsToPay() {
		return assistantsToPay;
	}
	
	/**
	 * @return the coins to pay to satisfy the {@link Balcony}
	 */
	public int getMoneyToPay() {
		return moneyToPay;
	}
}
