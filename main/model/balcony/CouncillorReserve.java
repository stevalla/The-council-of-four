package it.polimi.ingsw.cg32.model.balcony;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import it.polimi.ingsw.cg32.model.color.Colore;

/**
 * This class rapresent the councillor reserve and it have a {@link List}
 * of councillor that rapresent the councillor remained in reserve.
 * Implement {@link Serializable}
 * 
 * @see Councillor
 * @see Colore
 * 
 * @author Stefano
 *
 */
public class CouncillorReserve implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * Rapresent the number of councillor 
	 * for each colors in reserve when starting a game
	 */
	private static final int COUNCILLOR_NUMBER_FOREACH_COLOR = 4;
	private final List<Councillor> councillorInReserve;
	
	
	/**
	 * FullFill the reserve with four councillor
	 * for each colore
	 * 
	 * @see Colore 
	 * @see Councillor
	 */
	public CouncillorReserve(){

		councillorInReserve = new LinkedList<>();
		
		for(int i=0; i < COUNCILLOR_NUMBER_FOREACH_COLOR; i++){
			Arrays.asList(Colore.values())
				.stream()
				.filter(c -> c != Colore.JOLLY)
				.forEach(c -> councillorInReserve.add(new Councillor(c)));
		}

		Collections.shuffle(councillorInReserve);
	}

	
	/**
	 * @return a {@link List} of councillors in reserve
	 */
	public List<Councillor> getCouncillorInReserve() {
		return councillorInReserve;
	}
	
	
	/**
	 * This methos verify if there is a councillor with the color
	 * passed as a parameter.
	 * 
	 * @param colore the color of the councillor
	 * @return true if there is the councillor in reserve, false otherwise
	 * @throws NullPointerException if the argument is null
	 * @see Colore 
	 * @see Councillor
	 */
	public boolean containCouncillor(Colore colore) {
		if(colore == null)
			throw new NullPointerException("The color can't be null.");
		
		for(Councillor councillor : councillorInReserve)
			if(councillor.getColor().equals(colore))
				return true;
		return false;
	}
	
	
	/**
	 * This method search in the reserve the councillor with the color
	 * passed as a parameter.
	 * 
	 * @param colore the color of the councillor
	 * @return the councillor with the color parameter if there is in reserve, null otherwise
	 * @throws NullPointerException if the argument is null
	 * @see Colore 
	 * @see Councillor
	 */
	public Councillor getCouncillorByColor(Colore colore) {
		if(colore == null)
			throw new NullPointerException("The color can't be null.");
		
		for(Councillor councillor : councillorInReserve)
			if(councillor.getColor() == colore) {
				int index = councillorInReserve.indexOf(councillor);
				councillorInReserve.get(index);
				return councillor;
			}
		
		return null;
	}

	
	@Override
	public String toString() {
		
		String listString = "Councillor reserve:\n";
		for(Councillor councillor : councillorInReserve)
			listString += councillor + "\n";
			
		return listString;
	
	}

}
