package it.polimi.ingsw.cg32.utilities.observer;

/**
 * Functional Interface with the method to received
 * notifies from Observable and implement and update for
 * the notify.
 * 
 * @author Stefano
 *
 * @param <C> the object notified.
 */
@FunctionalInterface
public interface Observer<C> {

	/**
	 * Permit perform an update on the object to
	 * notify.
	 * 
	 * @param o the object notified
	 */
	public void update(C o);

	/**
	 * Perform an update when called by notify.
	 */
	public default void update() {}
}
