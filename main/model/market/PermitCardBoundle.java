package it.polimi.ingsw.cg32.model.market;

import java.util.LinkedList;
import java.util.List;

import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.game.Player;

/**
* Concrete implementation of {@link Boundle}.
* It rapresents a bunch of {@link PermitCard} that a player can sell or but from market.
* 
* @author giovanni
*/
public class PermitCardBoundle extends Boundle {

	private static final long serialVersionUID = 1L;
	private final List<PermitCard> permitCards;
	
	
	/**
	* Constructor take care of initialize all the fiels of the Boundle. 
	* 
	* @param cost the price of the boundle
	* @param permitCards the List of PermitCards  to collect in the boundle
	* 
	* @throws NullPointerException if the List of PermitCard is null
	* 
	* @see PermitCards
	*/
	public PermitCardBoundle(int cost, List<PermitCard> permitCards) {
		super(cost);
		
		if(permitCards == null)
			throw new NullPointerException("List of permit cards can't be null");
		
		this.permitCards  = new LinkedList<>(permitCards);
	}

	/**
	* Get the list of PermitCard contained in the Boundle
	* 
	* @return permitCards the list of PermitCard of the Boundle
	* 
	* @see PermitCard
	*/
	public List<PermitCard> getPermitCards() {
		return permitCards;
	}


	@Override
	public void useBoundle(Player player) {
		
		permitCards.stream().forEach(player.getPermitCards()::add);
		
	}
	
	

	@Override
	public String toString() {
		return "#" + getBoundleId() + " price: " + getPrice() + "\ncontaining: " + permitCards.toString();
	}
	
}
