package it.polimi.ingsw.cg32.model.market;

import java.util.LinkedList;
import java.util.List;

import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.game.Player;

/**
* Concrete implementation of {@link Boundle}.
* It rapresents a bunch of {@link PoliticCard} that a player can sell or but from market.
* 
* @author giovanni
*/
public class PoliticCardBoundle extends Boundle {


	private static final long serialVersionUID = 1L;
	private final List<PoliticCard> politicCards;
	
	/**
	* Constructor take care of initialize all the fiels of the Boundle. 
	* 
	* @param cost the price of the boundle
	* @param politicCards the List of PoliticCard to collect in the boundle
	* 
	* @throws NullPointerException if the List of PoliticCard is null
	* 
	* @see PoliticCard
	*/
	public PoliticCardBoundle(int cost, List<PoliticCard> politicCards) {
		super(cost);
		
		if(politicCards == null)
			throw new NullPointerException("List of politic cards can't be null");
		
		this.politicCards = new LinkedList<>(politicCards);
	}

	/**
	* Get the list of politicCard contained in the Boundle
	* 
	* @return politicCards the list of PolitiCard of the Boundle
	* 
	* @see PoliticCard
	*/
	public List<PoliticCard> getPoliticCards() {
		return politicCards;
	}

	@Override
	public void useBoundle(Player player) {
		
		politicCards.stream().forEach(player.getPoliticCards()::add);
		
	}

	@Override
	public String toString() {
		return "#" + getBoundleId() + " price: " + getPrice() + "\ncontaining: " + politicCards.toString();
	}
	
	
}
