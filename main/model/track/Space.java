package it.polimi.ingsw.cg32.model.track;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.cg32.model.bonus.Bonus;


/**
* This class models a space for the NobilityTrack.
* A space is mark by a number that indicate its position in NobilityTrack and a list of
* {@link Bonus} that potentially could be empty.
* Implement {@link Serializable}
* 
* @author giovanni
* 
* @see NobilityTrack
*
*/
public class Space implements Comparable<Space>, Serializable{

	private static final long serialVersionUID = 1L;
	private final List<Bonus> bonus = new ArrayList<>();
	private final int spaceNumber;
	
	/**
	* This constructor is used to instanziate a space with bonus.
	* Instantiates a new Space, setting spaceNumber and filling the Set of Bonus with the set pass as param.
	*
	* @param number the number of the Space that indicate its position inside the NobilityTrack
	* @param bonus the set of Bonus for the target space 
	* @throws IllegalArgumentException if the param is minor than zero
	* @see Bonus
	*/
	public Space(int number, List<Bonus> bonus) {
		this(number);
		this.bonus.addAll(bonus);
	}
	
	
	/**
	* This constructor is used to instanziate a Space without Bonus.
	* Instantiates a new Space, setting spaceNumber and the list of Bonus as an {@link Collections.emptyList}.
	* 
	* @param number the number of the space that indicate its position inside the track
	* @throws IllegalArgumentException if the param is minor than zero
	* @see Bonus
	*/
	public Space(int number) {
		if(number < 0)
			throw new IllegalArgumentException("The space can only have a positive number");
		
		this.spaceNumber = number;
	}
	
	
	/**
	* Get the list of {@link Bonus} associated with the space
	* 
	* @return the list of bonus of the target space
	*/
	public List<Bonus> getBonus() {
		return bonus;
	}
	

	/**
	 * @return the number fo the space
	 */
	public int getSpaceNumber() {
		return spaceNumber;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + spaceNumber;
		result = prime * result + ((bonus == null) ? 0 : bonus.hashCode());
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
		Space other = (Space) obj;
		if (spaceNumber != other.spaceNumber)
			return false;
		if (bonus == null) {
			if (other.bonus != null)
				return false;
		} else if (!bonus.equals(other.bonus))
			return false;
		return true;
	}

	
	@Override
	public int compareTo(Space o){
		return this.spaceNumber - o.spaceNumber;
	}


	@Override
	public String toString() {
		return "[#" + spaceNumber + " bonus = " + bonus+"]\n";
	}
	
}