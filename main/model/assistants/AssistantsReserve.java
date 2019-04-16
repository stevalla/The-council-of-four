package it.polimi.ingsw.cg32.model.assistants;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class rapresent the assistant reserve and it 
 * calculate how much assistants putting in the reserve based on the number of player.
 * Implement {@link Serializable}.
 * 
 * @author Stefano
 *
 */
public class AssistantsReserve implements Serializable {

	/**
	 * The collection that contains assistants in reserve.
	 */
	private final Queue<Assistant> assistants = new LinkedList<>();
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Instantiates a new AssistantReserve, and fullfill it.
	 * 
	 * @param  numberPlayers  Number of player of the game
	 */
	public AssistantsReserve(int numberPlayers) {
		initializeAssistantsReserve(numberPlayers);
	}
	
	
	/**
	 * Fullfill the assistant reserve based on the return of
	 * a method that calculate the assistant reserve size.
	 * 
	 * @param numberPlayers Number of player of the game
	 */
	private void initializeAssistantsReserve(int numberPlayers) {
		int assistantQuantity = calculateQuantityAssistants(numberPlayers);
		
		for(int i=0; i < assistantQuantity; i++)
			assistants.add(new Assistant());
	}
	
	
	/**
	 * Calculate the number of {@link Assistant} to put in reserve
	 * based on the number of players of the game.
	 * 
	 * @param numberPlayers Number of player of the game
	 * @return The assistant reserve size for the game 
	 * 
	 * @throws IllegalArgumentException if the number of
	 * 		   players is less then two, the game doesn't
	 * 		   allow this
	 */
	private int calculateQuantityAssistants(int numberPlayers) {
		
		if(numberPlayers<2)
			throw new IllegalArgumentException("Number of players can't be less then two.");
		
		int standardQuantityAssistants = 0;
		
		/*
		 * Default assistant reserve size for a standard game,
		 * when numberPlayers is between 2 and 4.
		 */
		if(numberPlayers < 5){
			standardQuantityAssistants = 30;
			return standardQuantityAssistants;
		}
		
		/*
		 * This piece of code ensure that there will be enough
		 * assitant for players in games where number of players is more
		 * than 4
		 */
		else{
			for(int i = 0; i <= numberPlayers; i++)
				standardQuantityAssistants += i;

			return standardQuantityAssistants + (numberPlayers * 5);
		}
	}

	
	/**
	 * @return The collections of assistant in reserve
	 */
	public Queue<Assistant> getAssistants() {
		return assistants;
	}

	
	@Override
	public String toString() {
		return "AssistantsReserve: " + assistants.size();
	}
}
