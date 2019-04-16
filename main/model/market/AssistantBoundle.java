package it.polimi.ingsw.cg32.model.market;

import java.util.LinkedList;
import java.util.List;

import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.game.Player;

/**
* Concrete implementation of {@link Boundle}.
* It rapresents a bunch of {@link Assistant} that a player can sell or but from market.
* 
* @author giovanni
*/
public class AssistantBoundle extends Boundle {

	private static final long serialVersionUID = 1L;
	private final List<Assistant> assistants;
	
	
	/**
	* Constructor take care of initialize all the fiels of the Boundle. 
	* 
	* @param cost the price of the boundle
	* @param assistants the List of Assistant  to collect in the boundle
	* 
	* @throws NullPointerException if the List of Assistant is null
	* 
	* @see Assistant
	*/
	public AssistantBoundle(int cost, List<Assistant> assistants) {
		super(cost);
		
		if(assistants == null)
			throw new NullPointerException("List of assistants can't be null");
		
		this.assistants = new LinkedList<>(assistants);
	}

	/**
	* Get the list of Assistant contained in the Boundle
	* 
	* @return assistants the list of Assistant of the Boundle
	* 
	* @see Assistant
	*/
	public List<Assistant> getAssistants() {
		return assistants;
	}

	@Override
	public void useBoundle(Player player) {
		
		assistants.stream().forEach(player.getAssistants()::add);
	}

	@Override
	public String toString() {
		
		
		return "#" + getBoundleId() + " price: " + getPrice() + "\ncontaining: " + assistants.toString();
	}

	
	
}
