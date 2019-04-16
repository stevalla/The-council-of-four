package it.polimi.ingsw.cg32.view.server.pubsub;

import java.util.Collection;
import java.util.Map;

import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This interface provide methods to subscribe {@link Subscriber}
 * to topic and to manage topic and subscriptions.
 * 
 * @author Stefano
 *
 * @param <M> The messages to send to Subscribers
 * @param <T> the topic
 */
public interface BrokerInt<M, T> {

	/**
	 * This method publish a message to all {@link Subscriber}
	 * that are subscribed to the topic.
	 * 
	 * @param msg the message to publish
	 * @param topic the topic for the message
	 */
	 public void publish(M msg, T topic);
	
	/**
	 * This methos verify if the subscriber are already subscribed,
	 * if not subscribe the subscriber with the relative token.
	 * The token is used to maintain the subscriptions of
	 * clients to topics.
	 * 
	 * @param <V> subscriber the subscriber to subscribe
	 * @param token the token associated to the subscriber
	 * @throws NullPointerException if params are null
	 * @see Subscriber 
	 * @see Token
	 */
	public <V> void subscribe(V subscriber, Token token);
	
	/**
	 * This method unsubscribe a client from the game. So remove
	 * it from all the {@link Broker}'s property.
	 * 
	 * @param subscriber to unsubscribe
	 */
	public <V> void unsubscribe(V subscriber);
	
	/**
	 * This method subscribe a token to a topic, from this moment
	 * all messages with this topic will be send to the client.
	 * 
	 * @param topic the topic to which subscribe the client
	 * @param token the token that identify the client
	 * @see Subscriber 
	 * @see Token
	 */
	public void subscribeClientToTopic(T topic, Token token);

	/**
	 * This method unsubscribe a token to a topic, from this moment
	 * all messages with this topic will be not send to the client.
	 * 
	 * @param topic the topic to which unsubscribe the client
	 * @param token the token that identify the client
	 */
	public void unsubscribeClientToTopic(T topic, Token token);
	
	/**
	 * This method unsubscribe the client from the game. From this 
	 * moment no message will be send to client.
	 * 
	 * @param token the client to unsubscribe
	 */
	public void unsubscribeClientToGame(Token token);
	
	/**
	 * Add a specific topic to the {@link Collection} of topics
	 * 
	 * @param topic the topic to add
	 */
	public void addTopic(T topic);
	
	/**
	 * Remove a specific topoc to the {@link Collection} of topics
	 * @param topic the topic to remove
	 */
	public void removeTopic(T topic);

	/**
	* This method search in the {@link Map} of subscribers the token
	* associated to this subscriber.
	*
	* @param <V> the type of subscriber to look for
	* @param subscriber the subscriber to find the relative token
	* 
	* @return the token of the specific subscriber
	* 
	* @throws NullPointerException if the subscriber is null
	* @throws IllegalArgumentException if the subscriber is not present 
	* 		  in the map of subscribers
	*/
	public <V> Token getTokenBySuscriber(V subscriber);

}
