package it.polimi.ingsw.cg32.model.bonus;

/**
 * This class rapresent a quantity bonus.
 * It can be rapresent by a lot of single bonus as quantity is.<br>
 * <p>
 * It extends the abstract class {@link Bonus} and it inherits 
 * the consturctor. Every bonus are override equals based on his 
 * name and his quantity.
 * </p>
 * @author Stefano
 *
 */
public class QuantityBonus extends Bonus {

	private static final long serialVersionUID = 1L;
	private final int quantity;
	
	
	/**
	 * The name can't be null and quantity must be more than zero.
	 * 
	 * @param name Bonus name
	 * @param quantity Quantity bonus
	 */
	public QuantityBonus(String name, int quantity) {
		super(name);
		
		if(quantity <= 0)
			throw new IllegalArgumentException("Quantity bonus can't have quantity equals or less then zero.");
		
		this.quantity = quantity;
	}
	
	
	/**
	 * @return the quantity of the bonus
	 */
	public int getQuantity() {
		return quantity;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + quantity;
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuantityBonus other = (QuantityBonus) obj;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "[ " + quantity + " " + getName() + "]";
	}

}
