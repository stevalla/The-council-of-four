package it.polimi.ingsw.cg32.model.region;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Queue;

import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.card.BonusKingCard;
import it.polimi.ingsw.cg32.model.color.CityColor;

/**
 * This class extends {@link Region} and offer two deck of cards.
 * One for the 5 {@link BonusKingCard} and another for the 5
 * {@link BonusKingCard} of the bonus city color cards.
 * 
 * @author Stefano
 *
 */
public class KingRegion extends Region {
	
	private static final long serialVersionUID = 1L;
	private static final int MAX_KINGCARD_NUMBER = 5;
	private final Queue<BonusKingCard> deckOfKingCard;
	private final EnumMap<CityColor, BonusKingCard> deckOfCityColorBonus;
	
	/**
	 * Create A King Region and initialize the two decks.
	 */
	public KingRegion(){
		super();
		
		deckOfKingCard = new LinkedList<>();
		for(int i = 0; i < MAX_KINGCARD_NUMBER; i++)
			deckOfKingCard.add(new BonusKingCard(new QuantityBonus("VICTORYBONUS", (int) (Math.random()*30 +1))));
		
		deckOfCityColorBonus = new EnumMap<>(CityColor.class);
		Arrays.asList(CityColor.values()).stream()
										 .filter(c -> c != CityColor.KING)
										 .forEach(c -> deckOfCityColorBonus.put(c, new BonusKingCard(new QuantityBonus("VICTORYBONUS", (int) (Math.random()*30 +1)))));
	}
	

	/**
	 * Permit draw a BonusKingCard.
	 * 
	 * @return a {@link BonusKingCard} or null if the deck is empty
	 */
	public BonusKingCard drawKingCard() {
		
		return deckOfKingCard.poll();
	}

	
	/**
	 * @return the {@link Queue} of {@link BonuskIngCard}
	 */
	public Queue<BonusKingCard> getDeckOfKingCard() {
		
		return deckOfKingCard;
	}

	
	/**
	 * Allow to get the {@link BonusKingCard} of the same {@link CityColor}
	 * of the param to be passed. Returns null if the cards was already drawed.
	 * 
	 * @param color the {@link CityColor} of the city color bonus card to draw.
	 * @return the {@link BonuskIngCard} or null if the card of the 
	 * 		   {@link CityColor} it was already drawed.
	 */
	public BonusKingCard getCityColorBonusCard(CityColor color) {
		
		return deckOfCityColorBonus.remove(color);
	}

	
	@Override
	public String toString() {
		return "KingRegion:\nRemaning cards in the deck of the king = " + deckOfCityColorBonus.size() + "\nThe first drawable card is " + getDeckOfKingCard().peek() + "\nCurrent state of the balcony:\n" + super.toString();
	}
 

}
