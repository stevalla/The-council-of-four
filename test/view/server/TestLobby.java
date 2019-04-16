package it.polimi.ingsw.cg32.view.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.GameController;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.view.server.pubsub.BrokerInt;
import it.polimi.ingsw.cg32.view.server.rmi.SubscriberRMI;

public class TestLobby {


	private BrokerInt<ResponseMessage, String> broker;
	private Lobby lobby;
	
	
	@Before
	public void initTest(){
		
		broker = new ProxyBroker(); 
		lobby = new Lobby(broker, 20);
		
	}
	
	//create a stupid broker that don't send message but allow easy-testing.
	public class ProxyBroker implements BrokerInt<ResponseMessage, String> {

		@Override
		public <V> void subscribe(V subscriber, Token token) {
		}

		@Override
		public <V> void unsubscribe(V subscriber) {
		}

		@Override
		public void subscribeClientToTopic(String topic, Token token) {
		}

		@Override
		public void unsubscribeClientToTopic(String topic, Token token) {
		}

		@Override
		public void unsubscribeClientToGame(Token token) {
		}

		@Override
		public void addTopic(String topic) {
		}

		@Override
		public void removeTopic(String topic) {
		}

		@Override
		public void publish(ResponseMessage msg, String topic) {
		}

		@Override
		public <V> Token getTokenBySuscriber(V subscriber) {
			return null;
		}
		
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldTrowsNullPointerExceptionIfBrokerIsNull(){
		new Lobby(null, 30);
	}
	
	@Test 
	public void testCreatorShouldInitializeAllTheFieldsOfTheLobbyAsRequire(){
		Lobby lobby = new Lobby(broker, 30);
		Assert.assertNotNull(lobby.getWaitingController());
		Assert.assertFalse(lobby.getWaitingController().isGameStarted());
		Assert.assertTrue("RunningGames should be empty at construction", lobby.getRunningGames().isEmpty());
	}

	@Test 
	public void testGetBrokerShouldReturnTheBrokerPassAsParamAtItsConstruction(){
		Lobby lobby = new Lobby(broker, 30);
		Assert.assertEquals(broker, lobby.getBroker());
	}
	
	@Test 
	public void testGetWaitngGameControllerShouldReturnTheCurrentWaitngGameController() throws InterruptedException{
		
		GameController firstWaitingGameController = lobby.getWaitingController();
		
		lobby.addPlayer(new SubscriberRMI<>(null));
		lobby.addPlayer(new SubscriberRMI<>(null));
		
		//Let the Lobby's timer to shot
		Thread.sleep(30 * 1000);
		
		Assert.assertNotEquals(firstWaitingGameController, lobby.getWaitingController());
	}
	
	@Test (expected = NullPointerException.class)
	public void testAddPlayerWithANullSubscriberShouldThrowsNullPointerException(){
		lobby.addPlayer(null);
	}
	
	@Test
	public void testAddPlayerWithAValidSubscriberShouldAddGeneratedTokenToTheWaitingGameControllerAndShouldNotStartTimerIfThereJustOnePlayerInIt() throws InterruptedException{
	
		Assert.assertEquals(0, lobby.getWaitingController().getPlayers().size());
		lobby.addPlayer(new SubscriberRMI<>(null));
		Assert.assertEquals(1, lobby.getWaitingController().getPlayers().size());
		GameController firstWaitingGameController = lobby.getWaitingController();
		Thread.sleep(30 * 1000);
		Assert.assertEquals(firstWaitingGameController, lobby.getWaitingController());
	}
	
	@Test 
	public void testAddMoreThan2PlayersToLobbyShouldStartTimerAndAfter20SecondsShouldInitializeGameAndSetAnotherWaitingController() throws InterruptedException{
		
		GameController firstWaitingGameController = lobby.getWaitingController();
		
		lobby.addPlayer(new SubscriberRMI<>(null));
		lobby.addPlayer(new SubscriberRMI<>(null));
		lobby.addPlayer(new SubscriberRMI<>(null));
		
		Thread.sleep(30 * 1000);
		
		Assert.assertTrue(firstWaitingGameController.isGameStarted());
		Assert.assertEquals(3, firstWaitingGameController.getPlayers().size());
		Assert.assertNotEquals(firstWaitingGameController, lobby.getWaitingController());
	}
	

	
	@Test 
	public void testGarbageControllerShouldDeleteFromeTheListOfRunningGamesTheGamesThatAreAlreadyTerminated() throws InterruptedException{
		
		GameController firstWaitingGameController = lobby.getWaitingController();
		
		lobby.addPlayer(new SubscriberRMI<>(null));
		lobby.addPlayer(new SubscriberRMI<>(null));

		Thread.sleep(21 * 1000);

		Assert.assertTrue(firstWaitingGameController.isGameStarted());
		Assert.assertEquals(2, firstWaitingGameController.getPlayers().size());
		Assert.assertNotEquals(firstWaitingGameController, lobby.getWaitingController());
		Assert.assertFalse(lobby.getRunningGames().isEmpty());

		Thread.sleep(61 * 1000);
		
		//firstWaitingGameController is now terminated, start another gameControleer in order to call
		//garbageController.
		lobby.addPlayer(new SubscriberRMI<>(null));
		lobby.addPlayer(new SubscriberRMI<>(null));
		lobby.addPlayer(new SubscriberRMI<>(null));
		
		Thread.sleep(21 * 1000);

		Assert.assertFalse(firstWaitingGameController.isGameStarted());
		Assert.assertFalse(lobby.getRunningGames().contains(firstWaitingGameController));
	}
	
	@Test 
	public void testGarbageControllerShouldDoNothingIsThereAreNotGameControllerToDelete() throws InterruptedException{
		GameController firstWaitingGameController = lobby.getWaitingController();
		
		lobby.addPlayer(new SubscriberRMI<>(null));
		lobby.addPlayer(new SubscriberRMI<>(null));
		lobby.addPlayer(new SubscriberRMI<>(null));
		
		Thread.sleep(21 * 1000);
		
		Assert.assertTrue(firstWaitingGameController.isGameStarted());
		Assert.assertEquals(3, firstWaitingGameController.getPlayers().size());
		Assert.assertNotEquals(firstWaitingGameController, lobby.getWaitingController());
		Assert.assertTrue(lobby.getRunningGames().contains(firstWaitingGameController));
		
		//firstWaitingGameController is now terminated, start another gameController in order to call
		//garbageController.
		lobby.addPlayer(new SubscriberRMI<>(null));
		lobby.addPlayer(new SubscriberRMI<>(null));
		lobby.addPlayer(new SubscriberRMI<>(null));
		
		Thread.sleep(21 * 1000);
		
		Assert.assertTrue(firstWaitingGameController.isGameStarted());
		Assert.assertTrue(lobby.getRunningGames().contains(firstWaitingGameController));
	}
	
	@Test
	public void testWhenAPlayerExitDuringTheTimerBeforeTheGameInitializationAndPlayersAreAldoTwoShouldNotCreateANewGameController() throws InterruptedException {
		GameController firstWaitingGameController = lobby.getWaitingController();
		
		Token token = new Token();
		//Add a player to controller
		firstWaitingGameController.addPlayer(token, new SubscriberRMI<>(null));
		
		lobby.addPlayer(new SubscriberRMI<>(null));
		
		Thread.sleep(5 * 1000);
		
		//Delete one player during timer execution
		firstWaitingGameController.getPlayers().remove(token);
		
		Thread.sleep(16 * 1000);
		
		Assert.assertFalse(firstWaitingGameController.isGameStarted());
		Assert.assertEquals(1, firstWaitingGameController.getPlayers().size());
		Assert.assertEquals(firstWaitingGameController, lobby.getWaitingController());
		Assert.assertFalse(lobby.getRunningGames().contains(firstWaitingGameController));
	}
	
}
