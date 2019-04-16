package it.polimi.ingsw.cg32.view.server.pubsub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This is the Broker of the game and it is unique for all the games.
 * Implements interface {@link BrokerInt} to subscribe and unsuscribe
 * user to topic, and the methods to add or remove topics.
 * 
 * @author Stefano
 *
 * @param <M> The messages to send to Subscribers
 * @param <T> the topic
 * @see SubscriberInt
 */
public class Broker<M, T> implements BrokerInt<M, T> {

	private static final String BAD_TOPIC = "Invalid topic";
	private final Set<T> topics; 
	/**
	 * {@link Map} of subscriptions, the key is the topic and for each topic there 
	 * is a {@link List} of {@link Token}who are subscribe to this topic
	 */
	private final Map<T, List<Token>> subscriptions;
	/**
	 * This {@link Map} contain for each {@link Token} his {@link Subscriber}
	 */
	private final Map<Token, SubscriberInt<M>> subscribers;
	
	/**
	 * Simple initialization of the broker's property.
	 */
	public Broker() {
		this.topics = new HashSet<>();
		this.subscriptions = new HashMap<>();
		this.subscribers = new HashMap<>();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public synchronized <V> void subscribe(V subscriber, Token token) {
		
		if (subscriber == null) {
            throw new NullPointerException("SubscriberInterface must not be null.");
        } else if (token == null) {
            throw new NullPointerException("Token must not be null.");
        }

        subscribers.put(token, (SubscriberInt<M>) subscriber);
	}

	
	@Override
	public synchronized <V> void unsubscribe(V subscriber) {

		for(Iterator<Map.Entry<Token, SubscriberInt<M>>> iterator = subscribers.entrySet().iterator(); iterator.hasNext();) {
			
			Map.Entry<Token, SubscriberInt<M>> entry = iterator.next();
			
			if(entry.getValue().equals(subscriber)){
				unsubscribeClientToGame(entry.getKey());
				iterator.remove();
			}
		}
			
	}

	
	@Override
	public synchronized void addTopic(T topic) {

		if(topics.contains(topic)) 
			throw new IllegalArgumentException("The topic is already present in the set.");
		
		topics.add(topic);
		subscriptions.put(topic, new ArrayList<>());
	}
	
	
	@Override
	public synchronized void removeTopic(T topic) {
		if(!topics.contains(topic))
			throw new IllegalArgumentException("topic not already present in the list of topics");
		
		topics.remove(topic);
	} 
	
	
    @Override
    public synchronized void subscribeClientToTopic(T topic, Token token) {

        if (!topics.contains(topic)) {
            throw new IllegalArgumentException(BAD_TOPIC);
        } else if (token == null) {
            throw new NullPointerException("Token must not be null");
        }

        subscriptions.get(topic).add(token);
    }
    
    
    @Override
    public synchronized void unsubscribeClientToTopic(T topic, Token token) {
    	 if (!topics.contains(topic)) {
             throw new IllegalArgumentException(BAD_TOPIC);
         } else if (token == null) {
             throw new NullPointerException("Token must not be null");
         }
    	 
    	 subscriptions.get(topic).remove(token);
    }
    
    
    @Override
    public synchronized void unsubscribeClientToGame(Token token) {
  
    	for(Iterator<Map.Entry<T, List<Token>>> iterator = subscriptions.entrySet().iterator(); iterator.hasNext();) {
			
			Map.Entry<T, List<Token>> entry = iterator.next();
			
			if(entry.getValue().contains(token))
				unsubscribeClientToTopic(entry.getKey(), token); 
    	}	
   
    }

    
	@Override
	public synchronized void publish(M message, T topic)  {

		if (message == null) {
            throw new NullPointerException("Message must not be null.");
        } else if (!topics.contains(topic)) {
            throw new IllegalArgumentException(BAD_TOPIC);
        }

        subscriptions.get(topic)
        		.forEach(t -> subscribers.get(t).dispatchMessage(message));
	}
	

	@Override
	public synchronized <V> Token getTokenBySuscriber(V subscriber) {
		
		if(subscriber == null)
			throw new NullPointerException("The subscriber can't be null");
		
		for(Map.Entry<Token, SubscriberInt<M>> map : subscribers.entrySet()) {
			if(map.getValue().equals(subscriber))
				return map.getKey();
		}
		
		throw new IllegalArgumentException("Invalid subscriber.");
	}

	
	/**
	 * @return the set of topics in the broker
	 */
	public Set<T> getTopics() {
		return topics;
	}

	/**
	 * @return the map of topics and list of token subscriber to the
	 * 		   relative topic
	 */
	public Map<T, List<Token>> getSubscriptions() {
		return subscriptions;
	}

	/**
	 * @return the map of token associated with the relative subscriber
	 */
	public Map<Token, SubscriberInt<M>> getSubscribers() {
		return subscribers;
	}
}
