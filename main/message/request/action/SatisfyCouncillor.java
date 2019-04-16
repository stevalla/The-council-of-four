package it.polimi.ingsw.cg32.message.request.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import it.polimi.ingsw.cg32.model.balcony.Balcony;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.Region;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;

/**
 * This interface provide two default method to verify if a player can verify a Balcony.
 * To satisfy a balcony you should to discard between 1 from 4 {@link PoliticCard}
 * corresponding to the Color of the {@link Councillor} present in the Balcony.<br>
 * The color "JOLLY" can be any colour.
 * The method calculate how money coins the player should pay to satisfy the councillor
 * based on the number of politic cards:<br>
 * 		-1 cards = 10 coins<br>
 * 		-2 cards = 7 coins<br>
 * 		-3 cards = 4 coins<br>
 * 		-4 cards = 0 coins<br>
 * 		-For eache JOLLY one more coin.<br>
 * 
 * @author Stefano
 *
 *@see Colore 
 *@see Balcony 
 *@see Player
 */
public interface SatisfyCouncillor {

	/**
	 * Verify if the cards passed as param are player's cards, if the
	 * number of politic cards is correct and if there are councillor in the
	 * Balcony with color that not match with the colors of the politic cards.
	 * Then return the money to pay to satisfy the balcony.
	 * 
	 * @param politicCards the PoliticCard of the Player played
	 * @param player the Player of the request
	 * @param targetRegion the {@link Region} when draw the usable {@link PermitCard} and satisfy the Councillor
	 * @return the coins to pay to satisfy the Councillor
	 * @throws IllegalActionException if the Player can't satisfy the balcony
	 * @throws NullPointerException if params are null
	 * @see PoliticCard 
	 * @see Councillor
	 */
	public default Integer countMoneyToSatisfyCouncillor(List<PoliticCard> politicCards, Player player, Region targetRegion) 
			throws IllegalActionException {
		
		if(player == null || politicCards == null || targetRegion == null)
			throw new NullPointerException("Arguments can't be null");
		
		Integer moneyToPay = 0;
		
		List<Colore> councillorColor = new ArrayList<>();
		targetRegion.getBalcony().getSeats().stream().forEach(c -> councillorColor.add(c.getColor()));

		//Count jolly
		int jollyCounter = (int) politicCards.stream().filter(p -> p.getCardColor() == Colore.JOLLY).count();

		List<Colore> colors = new ArrayList<>();
		politicCards.stream().filter(p -> p.getCardColor() != Colore.JOLLY)
							 .forEach(p -> colors.add(p.getCardColor()));

		//Verify how many money the player should pay to satisfy the balcony
		switch (politicCards.size()) {
		case 1:
			moneyToPay += jollyCounter + 10;
			break;
		case 2:
			moneyToPay += jollyCounter + 7;
			break;
		case 3:
			moneyToPay += jollyCounter + 4;
			break;
		case 4:
			moneyToPay += jollyCounter;
			break;
		default:
			throw new IllegalActionException("You can choose max 4 politc cards.");
		}
		
		if(!CollectionUtils.isSubCollection(colors, councillorColor)) 
			throw new IllegalActionException("The politic cards don't satisfy the councillor.");
		
		if(player.getCoinsTrack().getCurrentCoins() < moneyToPay) 
			throw new IllegalActionException("You don't have enough money to satisfy the councillor, "
					+ "you need to discard more politic cards.");

		return moneyToPay;
	}
	
	/**
	 * Generate e List of Politic cards that corresponding to the colors
	 * passed as param by the player. Furthermore controll if are there the politic cards
	 * in player's politic cards with the correspondent colors passed as param.
	 * 
	 * @param colors the selected Color by the player
	 * @param player the Player who select the colors
	 * @return the List of Politic cards corresponding to the colors passed as params
	 * @throws IllegalActionException if any selected color not corresponding to the
	 * 		   colors of the player's politic cards
	 * @throws NullPointerException if params are null
	 */
	public default List<PoliticCard> getPoliticCardByColors(List<Colore> colors, Player player) throws IllegalActionException {
		
		if(player == null || colors == null)
			throw new NullPointerException("Player and colors can't be null");
		
		List<PoliticCard> selectedPoliticCards = new ArrayList<>();
		List<PoliticCard> copyPlayerPoliticCards = new ArrayList<>(Arrays.asList(new PoliticCard[player.getPoliticCards().size()]));
		Collections.copy(copyPlayerPoliticCards, player.getPoliticCards());
		
		List<Colore> playerCardsColor = new ArrayList<>();
		player.getPoliticCards().stream().forEach( p -> playerCardsColor.add(p.getCardColor()));

		if(!CollectionUtils.isSubCollection(colors, playerCardsColor)) 
			throw new IllegalActionException("You have selected one or more politic cards that not correspondign with yours.");
		
		for(Colore colore : colors) {
			for(int i=0; i<copyPlayerPoliticCards.size(); i++) {
				if(colore == copyPlayerPoliticCards.get(i).getCardColor()) {
					selectedPoliticCards.add(copyPlayerPoliticCards.remove(i));
					break;
				}
			}
		}
		
		return selectedPoliticCards;
	}
}
