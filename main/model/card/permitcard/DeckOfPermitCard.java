package it.polimi.ingsw.cg32.model.card.permitcard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import it.polimi.ingsw.cg32.model.game.Board;
import it.polimi.ingsw.cg32.model.region.NormalRegion;

/**
 * This class rapresent the deck of permit card for each {@link NormalRegion}.
 * The deck is composed by two usable cards (a {@link List}) and by a deck with other cards to
 * replace the usable cards drawed.<br>
 * The class offer the method to handle the deck for the game.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 * @see PermitCard
 */
public class DeckOfPermitCard implements Serializable{

	private static final long serialVersionUID = 1L;
	private final Queue<PermitCard> deck;
	private final List<PermitCard> usableCards;
	
	
	/**
	* Construct a deck and add the first two cards into the 
	* usable cards list.
	* 
	* @param permitCards list of permit card to compose the deck
	* @throws IllegalArgumentException if the list of permitCards is empty
	* @throws NullPointerException if the list of Permit card is null
	* 
	* @see PermitCard
	*/
	public DeckOfPermitCard(List<PermitCard> permitCards) {
		
		if(permitCards.isEmpty())
			throw new IllegalArgumentException("The list of cities can't be empty.");
		
		if(permitCards.size() < 2)
			throw new IllegalArgumentException("The deck size of permit card can't be less than two");
		
		this.deck = new LinkedList<>(permitCards);
			
		usableCards = new ArrayList<>();
		usableCards.add(deck.poll());
		usableCards.add(deck.poll());
	}
	
	
	/**
	* Used to initialize a game for 2 players in {@link Board#initializeGameForTwoPlayers} method
	* This method take all the permit card, usable and not, shuffle them
	* and than peek one permit card from those.
	* Note that the permit card is not poll from the deck, it still stay there
	* and a copy is returned.
	* 
	* @return a copy of a random permit card to use but not poll from the deck.
	* 		   
	* @see PermitCard
	*/
	public PermitCard drawRandomPermitCard() {
		
		Queue<PermitCard> tempPermitCards = new LinkedList<>(deck);
		usableCards.stream().forEach(tempPermitCards::add);
		
		Collections.shuffle((List<?>) tempPermitCards);
		return tempPermitCards.peek();
	}

	
	/**
	* Used to draw a permitcard from the usableCards.
	* If the card is not contained in the list of usableCards null is returned.
	* Otherwise the card is remove from the list of usableCards and return, then
	* if the id deck is not empty another card is add to the list of usableCards.
	* 
	* @param selectedCard a copy of the permit card to draw
	* @return the permit card drawed
	* @throws NullPointerException if the argument is null
	* 
	* @see PermitCard
	*/
	public PermitCard drawUsableCard(PermitCard selectedCard){
		
		PermitCard drawedCard = null;
		
		if(selectedCard == null)
			throw new NullPointerException("The permit card to draw can't be null");
		
		if(usableCards.contains(selectedCard)){
			drawedCard = usableCards.remove(usableCards.indexOf(selectedCard));
			if(!deck.isEmpty()) 
				usableCards.add(deck.poll());
		}
		
		return drawedCard;
	}
	
	
	/**
	* Allow to swap the usablecards with the firsts 2 cards of the deck.
	* If the deck doesn't contain at least 2 cards an IllegalStateException is thrown.
	* Otherwise swap the cards adding the firts 2 cards of the deck to the usableCards
	* and then put the previous usableCards in the end of the deck.
	* 
	* @throws IllegalStateException
	*/
	public void changeUsableCard(){
		
		if(size() < 2)
			throw new IllegalStateException("Can't do this action because the deck isn't enough full.");
		
		List<PermitCard> permitCardsToAdd = new ArrayList<>();
		usableCards.stream().forEach(permitCardsToAdd::add);
		usableCards.clear();
		
		usableCards.add(deck.poll());
		usableCards.add(deck.poll());
		
		deck.addAll(permitCardsToAdd);
	}

	
	/**
	 * @return the {@link List} of usable {@link PermitCard}
	 */
	public List<PermitCard> getUsableCard(){
		return Collections.unmodifiableList(usableCards);
	}
	
	
	/**
	 * @return the collections rapresents the deck.
	 */
	public Queue<PermitCard> getDeck() {
		return deck;
	}

	
	/**
	 * @return the size of the deck.
	 */
	public int size() {
		return deck.size();
	}

	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		for(PermitCard card : usableCards)
			sb.append(card).append("\n");
		
		return "Deck size =" + deck.size() + "\n" + sb;
	}

	
}
	