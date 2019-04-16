package it.polimi.ingsw.cg32.model.bonus;

/**
 * This class rapresent a bonus with no quantity.
 * Single bonus give you only one object based on the type of bonus(name).<br>
 * <p>
 * It extends the abstract class {@link Bonus} and it inherits 
 * the consturctor. Every bonus are override equals based on his name.
 * </p>
 * 
 * @author Stefano
 *
 */
public class SingleBonus extends Bonus {

	private static final long serialVersionUID = 1L;

	/**
	 * @param name Bonus name
	 */
	public SingleBonus(String name) {
		super(name);
	}

	
	@Override
	public String toString() {
		return "[" + getName() + "]";
	}

}
