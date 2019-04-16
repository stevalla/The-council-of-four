package it.polimi.ingsw.cg32.model.bonus;

import java.io.Serializable;

/**
 * 
 * This class rapresent a bonus. A bonus can have a quantity or not.
 * This class only gives the constructor {@link #Bonus(String)} to set bonus name 
 * and overrides equals and hashcode based on his name to compare bonus. 
 * All bonus implements {@link Serializable}
 * 
 * @author Stefano
 *
 */
public abstract class Bonus implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String name;
	
	/**
	 * Initialize the final field name, so bonus name can't change at run-time.
	 * 
	 * @param name  Bonus name
	 * 
	 * @throws NullPointerException if the value pass in the constructor is null
	 */
	public Bonus(String name) {
		if(name == null)
			throw new NullPointerException("Name of bonus can't be null");
		
		this.name = name;
	}


	/**
	 * @return The value of Bonus name
	 */
	public String getName() {
		return name;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Bonus other = (Bonus) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

} 
