package it.polimi.ingsw.cg32.model.city;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Player;

/**
* This class model and manage a city.
* It could either rappresent a normal city or a king city.
* It has several fields:<b>
* - name<br>
* - set of Emporium that collects all the Emporuims of the players built on the city<br>
* - {@link RegionColor} field that indicates the kind of region where is settled<br>
* - list of Bonus that indicates which Bonus a {@link Player} gain when build an Emporium on the City<br>
* - {@link CityColor} field that indicates the color of the city.<br>
* 
* The class provide method to add Emporium of a player by {@link #addEmporium(Emporium)} method and
* interrogate City about is field by getters.
* 
* @author giovanni
* 
* @see Emporium
* @see Bonus
*
*/
public class City implements Serializable{

	private static final long serialVersionUID = 1L;
	private final String name;
	private final Set<Emporium> emporium;
	private final RegionType region;
	private final List<Bonus> bonus;
	private final CityColor color;
	
	
	/**
	* Create a new City and provide initialization for all its fields.
	* 
	* @param name the name of the city
	* @param region the kind of the region where the city is settled
	* @param color the kind of city
	* @param bonus the List of {@link Bonus} of the city
	* 
	* @throws NullPointerException if name is null or either bonus is null
	*/
	public City(String name, RegionType region, CityColor color, List<Bonus> bonus) {
		
		if(bonus == null || name == null)
			throw new NullPointerException("Impossible to instantiate a ciy with null argument");
			
		this.name=name;
		this.emporium = new HashSet<>();
		this.region = region;
		this.bonus = new ArrayList<>(bonus);
		this.color = color;
	}
	
	
	/**
	* Add an emporium of a Player to the Set of Emporium of the City.
	* If it tryes to add an Emporium that is already present in the Set it doesn't add anything.
	* 
	* @param emporium the emporium of th player to add to the city
	* @throws NullPointerException if argument is null
	*/
	public void addEmporium(Emporium emporium){
		
		if(emporium == null)
			throw new NullPointerException("Impossible to add an emporium that is null.");
		
		this.emporium.add(emporium);
	}
		
	
	/**
	 * Get the Set of Emporium of the City
	 *
	 * @return emporium the Set of Emporium presents in the city
	 */
	public Set<Emporium> getEmporium() {
		return emporium;
	}

	/**
	* Get the name of the city
	* 
	* @return name the name of the city
	*/
	public String getName() {
		return name;
	}

	
	/**
	 * Get the kind of the region where the city is settled
	 * 
	 * @return region the kind of the region where the city is settled
	 */
	public RegionType getRegion() {
		return region;
	}
	
	
	/**
	 * Get the bonus contained in the city
	 * 
	 * @return bonus the bonus contained in the city
	 */
	public List<Bonus> getBonus() {
		return bonus;
	}
	
	
	/**
	* Get the kind of the city 
	* 
	* @return color the kind of city
	*/
	public CityColor getColor() {
		return color;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bonus == null) ? 0 : bonus.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((region == null) ? 0 : region.hashCode());
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
		City other = (City) obj;
		if (bonus == null) {
			if (other.bonus != null)
				return false;
		} else if (!bonus.equals(other.bonus))
			return false;
		if (color != other.color)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (region != other.region)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[name=" + name + ", emporium=" + emporium + ", region=" + region + ", bonus=" + bonus + ", color="
				+ color + "]";
	}
	
}
