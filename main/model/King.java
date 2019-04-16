package it.polimi.ingsw.cg32.model;

import java.io.Serializable;

import it.polimi.ingsw.cg32.model.city.City;

/**
 * This class models and manages the king of the game.
 * Is possible to move the king to another {@link City} by {@link #setCity(City)} method
 * and interrogate the king to ask for the currentCity where it's situated by {@link #getCity(City)} method.
 * 
 * @author giovanni
 *
 */
public class King implements Serializable {

	private static final long serialVersionUID = 1L;
	private City currentCity;
	
	/**
	 * Instantiate a King setting its currentCity to the {@link City} pass as parameter, if argument city
	 * is null a NullPointerException is thrown.
	 * 
	 * @param city the city where the king is situated at the beginning of the game
	 * @throws NullPointerException if the argument city is null
	 */
	public King(City city) {
		
		if(city == null)
			throw new NullPointerException("Target city must not be null.");
		
		this.currentCity = city;
	}

	
	/**
	* Move the king to the target {@link City}.
	* 
	* @param nextCity the city in which the king has to move
	* @throws NullPointerException if the argument nextCity is null
	*/
	public void setCity(City nextCity) {
		
		if(nextCity == null)
			throw new NullPointerException("Target city must not be null.");
		
		this.currentCity = nextCity;
	}
	
	
	/**
	 * Get the {@link City} where the king is situated.
	 * 
	 * @return currentCity the city where the king is situated
	 */
	public City getCity() {
		return currentCity;
	}

	
	@Override
	public String toString() {
		return "King situated in " + currentCity.getName();
	}
	
}
