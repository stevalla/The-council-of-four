package it.polimi.ingsw.cg32.model.balcony;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.list.UnmodifiableList;

/**
 * This class rapresent a balcony that has four Councillor.
 * A Councillor can only be {@link #push()} if there are less than 4 Councillor 
 * in Balcony, or {@link #remove} only if there are 4 Councillor in Balcony.
 * Getter method return an {@link UnmodifiableList} to get councillors.
 * 
 * @see Councillor
 * @author Stefano
 *
 */
public class Balcony implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private final Deque<Councillor> seats;
	
    
    /**
     * Construct a {@link LinkedList} of {@link Councillor}
     */
    public Balcony() {
		seats = new LinkedList<>();
	}

    
	/**
	 * @return {@link UnmodifiableList} of {@link Councillor} to get only access 
	 * 		    to councillors in balcony
	 */
	public List<Councillor> getSeats() {
		
		return Collections.unmodifiableList(new ArrayList<>(seats));
	}
	
	
	/**
	 * Permit to elect a councillor, it means that
	 * the last councillor in balcony will be removed and the
	 * elected councillor will be inserted at the head of seats.
	 * <p>
	 * At same time it take care about removing the councillor to elect
	 * from CouncillorReserve and refilling that with councillor 
	 * removed from the Balcony.
	 * </p>
	 * @param reserve the {@link CouncillorReserve} of the game
	 * @param councillor the {@link Councillor} that want to elect
	 * 
	 * @see Councillor
	 * @see CouncillorReserve
	 * 
	 * @throws NullPointerException if parameters are null.
	 */
	public void electCouncillor(CouncillorReserve reserve, Councillor councillor) {
		
		if(reserve == null || councillor == null)
			throw new NullPointerException("Reserve and Councillor can't be null");
		
		reserve.getCouncillorInReserve().remove(councillor);
		reserve.getCouncillorInReserve().add(pollLast());
		push(councillor);
	}
	
	
	/**
	 * Wrapped method to allow to push a {@link Councillor} in seats but only if
	 * the size of the seats are more than 3.
	 * 
	 * @param the councillor to add into Balcony
	 * 
	 * @throws IllegalArgumentException if the size of the seats are more then 3
	 */
	public void push(Councillor councillor) {
		
		if(seats.size() >= 4) 
			throw new IllegalArgumentException("The balcony can only have 4 councillors.");
		
		seats.push(councillor);
	}
	
	
	/**
	 * Wrapped method to allow to remove a {@link Councillor} from the seats but only if
	 * the size of the seats is 4.
	 * 
	 * @return the councillor removed
	 * 
	 * @throws IllegalArgumentException if the size of the seats are different than 4
	 */
	public Councillor pollLast() {
		
		if(seats.size() != 4)
			throw new IllegalArgumentException("The Balcony can't have less then 4 councillors.");
		
		return seats.pollLast();
	}

	
	@Override
	public String toString() {
		return "Balcony [" + seats + "]";
	}
	 
}
