package it.polimi.ingsw.cg32.model.balcony;

import java.io.Serializable;

import it.polimi.ingsw.cg32.model.color.Colore;

/**
 * This class rapresent a single Councillor.
 * Every Councillor must have a {@link Colore}.
 * Implement {@link Serializable}.
 * 
 * @author Stefano
 *
 */
public class Councillor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Colore color;
	
	
	/**
	 * Construct a councillor with the param color.
	 * 
	 * @param color {@link Colore} of the councillor
	 * @throws NullPointerException if the color is null
	 * @see Colore 
	 * @see Councillor
	 */
	public Councillor(Colore color) {
		if(color == null)
			throw new NullPointerException("The color of councillor can't be null.");
		
		this.color = color;
	}

	
	/**
	 * @return the councillor's {@link Colore}
	 */
	public Colore getColor() {
		return color;
	}

	
	@Override
	public String toString() {
		return "[" + this.getColor() + "]";
	}

}
