package it.polimi.ingsw.cg32.model.card.permitcard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.city.City;;

/**
 * This class rapresent a Permit Card that contained 
 * a List of cities and a List of Bonus.<br>
 * It can be identified by the {@link String} id passed in the
 * constructor.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 * @see City 
 * @see Bonus 
 */
public class PermitCard implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private final String id;
	private final List<Bonus> bonus;
	private final List<City> city;	
	private boolean used;	
	
	
	/**
	 * Construct a Permit Card passing a list of City a list of Bonus
	 * and a String id.
	 * Set the field used to false.
	 * 
	 * @param id the id to identify the card
	 * @param cities the list of City of the card
	 * @param bonus the list of Bonus of the card
	 * @throws NullPointerException if one of the arguments is null
	 * @throws IllegalArgumentException if one of the list is empty
	 * 
	 * @see City 
	 * @see Bonus
	 */
	public PermitCard(String id, List<City> cities, List<Bonus> bonus) {
		if(id == null)
			throw new NullPointerException("The id can't be null");
		if(cities.isEmpty() || bonus.isEmpty())
			throw new IllegalArgumentException("The list of cities can't be empty.");
		
		this.bonus = new ArrayList<>(bonus);
		this.city = new ArrayList<>(cities);
		this.used = false;
		this.id = id;
	}
	
	
	/**
	 * Set to true the {@link boolean} field used of the class, but it can be
	 * called only if used=false.
	 */
	public void useCard() {
		if(!used)
			this.used = true;
		else
			throw new IllegalStateException("This card has already been used.");
	}
	
	
	/**
	 * @return true if the card is not used, false otherwise
	 */
	public boolean isUsed() {
		return used;
	}
	
	 
	/**
	 * @return the list of {@link Bonus} of the card
	 */
	public List<Bonus> getBonus() {
		return bonus;
	}

	
	/**
	 * @return the list of {@link City} of the card
	 */
	public List<City> getCity() {
		return city;
	}

	
	/**
	 * @return the {@link String} id of the class
	 */
	public String getId() {
		return id;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bonus == null) ? 0 : bonus.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (used ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermitCard other = (PermitCard) obj;
		if (bonus == null) {
			if (other.bonus != null)
				return false;
		} else if (!bonus.equals(other.bonus))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (used != other.used)
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		List<String> nameOfCity = new ArrayList<>();
		city.stream().forEach(c -> nameOfCity.add(c.getName()));
		return "PermitCard id=" + id + " Bonus " + bonus + " City " + nameOfCity;
	}
}
