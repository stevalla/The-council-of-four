package it.polimi.ingsw.cg32.view.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import it.polimi.ingsw.cg32.message.response.CommunicationResponse;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.view.server.pubsub.Broker;
import it.polimi.ingsw.cg32.view.server.pubsub.SubscriberInt;

public class TestBroker {

	Broker<ResponseMessage, String> broker;
	private Token token;
	private SubscriberInt<ResponseMessage> subscriber;
	private String topic;
	private ResponseMessage message;
	
	@Before
	public void initParams() {
		broker = new Broker<>();
		token = new Token();
		subscriber = new ProxySubscriber();
		topic = "topic1";
		message = new CommunicationResponse("message");
	}
	
	public class ProxySubscriber implements SubscriberInt<ResponseMessage> {
		@Override
		public void dispatchMessage(ResponseMessage msg) {
			System.out.print(msg);
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void testSubscribeWhenTokenIsNullShouldThrowANullPointerException() {
		broker.subscribe(subscriber, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testSubscribeWhenSubscriberIsNullShouldThrowANullPointerException() {
		broker.subscribe(null, token);
	}
	
	@Test
	public void testSubscribeWhenArgumentAreCorrectShouldAddInSubscriberTheTokenAsKeyAndTheSubscriberAsValueOfTheMap() {
		assertNull(broker.getSubscribers().get(token));
		broker.subscribe(subscriber, token);
		assertEquals(subscriber, broker.getSubscribers().get(token));
	}
	
	@Test
	public void testUnSubscribeShouldRemoveTheTokenFromTheMapOfSubscribers() {
		SubscriberInt<ResponseMessage> sub = new ProxySubscriber();
		Token token2 = new Token();
		broker.subscribe(sub, token2);
		broker.subscribe(subscriber, token);
		assertEquals(subscriber, broker.getSubscribers().get(token));
		broker.unsubscribe(subscriber);
		assertNull(broker.getSubscribers().get(token));
	}
	
	@Test
	public void testUnsubscribeWhenTheSubscribeIsNotInTheMapOfTokenAndSubscribersShouldNotDoNothing() {
		broker.unsubscribe(subscriber);
		return;
	}
	
	@Test
	public void testAddTopicIfIsNotContainedShouldAddTheTopicInTheSetOfTopics() {
		assertFalse(broker.getTopics().contains(topic));
		broker.addTopic(topic);
		assertTrue(broker.getTopics().contains(topic));
	}
	
	@Test
	public void testAddTopicWhenTopicIsNotPresentShouldAddTheTopicInTheMapOfSubScritpionsWithAnEmptyListAsValue() {
		assertNull(broker.getSubscriptions().get(topic));
		broker.addTopic(topic);
		assertNotNull(broker.getSubscriptions().get(topic));
		assertTrue(broker.getSubscriptions().get(topic).isEmpty());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddTopicIfTheTopicIsAlreadyPresentShouldThrowAnIllegalArgumentException() {
		broker.addTopic(topic);
		broker.addTopic(topic);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveTopicWhenTopicIsNotInTheSetOfTopicsShouldThrowAnIllegalArgumentException() {
		broker.removeTopic(topic);
	}
	
	@Test
	public void testRemoveTopicWhenTopicIsInTheSetOfTopicsShouldRemoveTheTopicFromTheSet() {
		broker.addTopic(topic);
		assertTrue(broker.getTopics().contains(topic));
		broker.removeTopic(topic);
		assertFalse(broker.getTopics().contains(topic));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSubscribeClientToTopicWhenTopicIsNotInTheSetOfTopicShouldThrowAnIllegalArgumentException() {
		broker.subscribeClientToTopic(topic, token);
	}
	
	@Test(expected = NullPointerException.class)
	public void testSubscriberClientToTopicWhenTokenIsNullShouldThrowANullPointerException() {
		broker.addTopic(topic);
		broker.subscribeClientToTopic(topic, null);
	}
	
	@Test
	public void testSubscribeClientToTopicShouldAddInTheListOfTokenOfTheRelativeTopicTheToken() {
		broker.addTopic(topic);
		assertFalse(broker.getSubscriptions().get(topic).contains(token));
		broker.subscribeClientToTopic(topic, token);
		assertTrue(broker.getSubscriptions().get(topic).contains(token));
		broker.subscribeClientToTopic(topic, token);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testUnSubscribeClientToTopicWhenTopicIsNotInTheSetOfTopicShouldThrowAnIllegalArgumentException() {
		broker.unsubscribeClientToTopic(topic, token);
	}
	
	@Test(expected = NullPointerException.class)
	public void testUnsubscriberClientToTopicWhenTokenIsNullShouldThrowANullPointerException() {
		broker.addTopic(topic);
		broker.unsubscribeClientToTopic(topic, null);
	}
	
	@Test
	public void testUnsubscribeClientToTopicShouldAddInTheListOfTokenOfTheRelativeTopicTheToken() {
		broker.addTopic(topic);
		broker.subscribeClientToTopic(topic, token);
		assertTrue(broker.getSubscriptions().get(topic).contains(token));
		broker.unsubscribeClientToTopic(topic, token);
		assertFalse(broker.getSubscriptions().get(topic).contains(token));
		broker.subscribeClientToTopic(topic, token);
	}
	
	@Test(expected = NullPointerException.class)
	public void testPublishWhenMessageIsNullShouldThrowANullPointerException() {
		broker.addTopic(topic);
		broker.publish(null, topic);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPublishWhenTopicIsNotInTheSetOfTopicShouldThrowAnIllegalArgumentException() {
		broker.publish(message, topic);
	}
	
	@Rule
	public final SystemOutRule system = new SystemOutRule().enableLog().muteForSuccessfulTests();
	@Test
	public void testPublishWhenSubscriberIsNotInTheMapOfSubscribersShouldNotDoNothing() {
		broker.addTopic(topic);
		broker.publish(message, topic);
		assertTrue(system.getLog().length() == 0);
		system.enableLog();
	}
	
	@Test
	public void testPublishWhenSubscriberIsInTheMapOfSubscribersShouldPrintTheMessage() {
		broker.addTopic(topic);
		broker.subscribe(subscriber, token);
		broker.subscribeClientToTopic(topic, token);
		broker.publish(message, topic);
		assertEquals(message.toString(), system.getLog());
		system.enableLog();
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetTokenBySubscriberWhenSubscriberIsNullShouldThrowANullPointerException() {
		broker.getTokenBySuscriber(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetTokenBySubscriberWhenTheSubscriberIsNotPresentInTheMapOfSubscribersShouldThrowAnIllegalArgumentException() {
		broker.getTokenBySuscriber(subscriber);
	}
	
	@Test
	public void testGetTokenBySubscriberWhenSubscriberIsInRegisterInBrokerShouldReturnTheTokenAssociatedWithThisSubscriber() {
		SubscriberInt<ResponseMessage> sub = new ProxySubscriber();
		Token token2 = new Token();
		broker.addTopic(topic);
		broker.subscribe(sub, token2);
		broker.subscribeClientToTopic(topic, token2);
		broker.subscribe(subscriber, token);
		broker.subscribeClientToTopic(topic, token);
		assertTrue(broker.getSubscribers().containsKey(token));
		assertTrue(broker.getSubscriptions().get(topic).contains(token));
		assertEquals(token, broker.getTokenBySuscriber(subscriber));
	}
	
	@Test
	public void testUnsubscribeClientToGameShouldRemoveSubscriptions() {
		SubscriberInt<ResponseMessage> sub = new ProxySubscriber();
		Token token2 = new Token();
		broker.addTopic(topic);
		broker.subscribe(sub, token2);
		broker.subscribeClientToTopic(topic, token2);
		broker.subscribe(subscriber, token);
		broker.subscribeClientToTopic(topic, token);
		assertTrue(broker.getSubscribers().containsKey(token));
		assertTrue(broker.getSubscriptions().get(topic).contains(token));
		broker.unsubscribeClientToGame(token);
		assertFalse(broker.getSubscriptions().get(topic).contains(token));
	}
}
