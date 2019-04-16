package it.polimi.ingsw.cg32.model.region;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg32.model.balcony.Balcony;

/**
 * This abstract class provide the skeleton for a region, it is the 
 * {@link Balcony} of the region. 
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public abstract class Region implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final Balcony balcony;
	
	/**
	 * Construct a basic Region with only a {@link Balcony}.
	 */
	public Region(){
		balcony = new Balcony();
	}

	/**
	 * @return the {@link Balcony} of the Region
	 */
	public Balcony getBalcony() {
		return balcony;
	}

	@Override
	public String toString() {
		List<String> colorCouncillor = new ArrayList<>();
		balcony.getSeats().stream().forEach(c -> colorCouncillor.add(c.getColor().toString()));
		return colorCouncillor + "\n";
	}
	
}
