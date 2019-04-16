package it.polimi.ingsw.cg32.view.server.pubsub;

/**
 * This interface rapresent a SubScriber with only one method
 * to dispatch message. Every client have a Subscriber and the {@link Broker}
 * occupies to send messages to the correct Subscriber based on the topic of the message.
 * 
 * @author Stefano
 *
 * @param <M> messages to dispatch
 */
@FunctionalInterface
public interface SubscriberInt<M> {

	/**
	 * @param msg message to dispatch
	 */
	public void dispatchMessage(M msg);
}
