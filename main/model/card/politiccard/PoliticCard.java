package it.polimi.ingsw.cg32.model.card.politiccard;

import java.io.Serializable;

import it.polimi.ingsw.cg32.model.color.Colore;

/**
 * This class rapresent a politicCard, it have a copy constructor {@link #PoliticCard(PoliticCard)}
 * because the only way to generate a politic card is with the
 * {@link PoliticCardPrototype} that choose a random {@link Colore} to
 * generate the politic card.<br>
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public class PoliticCard implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private final Colore cardColor;
	
	
	/**
	 * Constructor used only by {@link PoliticCrdPrototype}
	 * that generate random politic cards for players.
	 * A Politic card can't have no {@link Colore}.
	 * 
	 * @param color
	 * @throws NullPointerException if argumenti is null
	 */
	protected PoliticCard(Colore color) {
		if(color == null)
			throw new NullPointerException("The color of the politic card can't be null.");
		
		this.cardColor = color;
	}
	
	
	/**
	 * Copy constructor to generate a politic card copying another.
	 * 
	 * @param politicCard the politic card to be copied
	 * @throws NullPointerException if argument is null
	 */
	public PoliticCard(PoliticCard politicCard) {
		this(politicCard.cardColor);
	}

	
	/**
	 * @return the card's {@link Colore} 
	 */
	public Colore getCardColor() {
		return cardColor;
	}

	
	@Override
	public String toString() {
		return "[" + cardColor + "]";
	}

}
