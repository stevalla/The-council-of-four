package it.polimi.ingsw.cg32.model.card.politiccard;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import it.polimi.ingsw.cg32.model.color.Colore;

/**
 * This class use Prototype Pattern to generate random
 * Politic card using a cache of Politic card
 * one for each {@link Colore}.
 * 
 * @author Stefano
 *
 * @see PoliticCard
 */
public class PoliticCardPrototype {

	/**
	 * The Map that contained one Politic Card for each {@link Colore}
	 * to copy {@link PoliticCard}.
	 */
	private Map<Integer, PoliticCard> politicCards = new HashMap<>();
	
	/**
	 * The number of color for this game.
	 */
	private static final int BOUND_COLOR = 7;

	
	/**
	 * Ensure that map was load from the first called
	 * and copy a random Politic card from the cache.
	 * 
	 * @return a random PoliticCard
	 * 
	 * @see PoliticCard
	 */
	public PoliticCard getPoliticCard() {
		if(politicCards.isEmpty()) 
			loadCache();
		Random random = new Random();
		PoliticCard cachedCard = politicCards.get(random.nextInt(BOUND_COLOR));
		return new PoliticCard(cachedCard);
	}
	
	
	/**
	 * Load the {@link Map} of {@link Integer}
	 * and {@link PoliticCard} and so create the cache to
	 * copy the PoliticCard.
	 */
	private void loadCache() {
		politicCards.put(0, new PoliticCard(Colore.BLACK));
		politicCards.put(1, new PoliticCard(Colore.LIGHTBLUE));
		politicCards.put(2, new PoliticCard(Colore.ORANGE));
		politicCards.put(3, new PoliticCard(Colore.PINK));
		politicCards.put(4, new PoliticCard(Colore.PURPLE));
		politicCards.put(5, new PoliticCard(Colore.WHITE));
		politicCards.put(6, new PoliticCard(Colore.JOLLY));
	}
}