package it.polimi.ingsw.cg32.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.turn.MarketTurnMachine;
import it.polimi.ingsw.cg32.controller.turn.state.BuyState;
import it.polimi.ingsw.cg32.controller.turn.state.OffState;
import it.polimi.ingsw.cg32.controller.turn.state.SellState;
import it.polimi.ingsw.cg32.message.request.market.MarketSellRqst;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.market.AssistantBoundle;
import it.polimi.ingsw.cg32.model.market.PermitCardBoundle;
import it.polimi.ingsw.cg32.model.market.PoliticCardBoundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.view.server.Token;
import it.polimi.ingsw.cg32.view.server.pubsub.BrokerInt;
import it.polimi.ingsw.cg32.view.server.rmi.SubscriberRMI;

public class TestMarket {

	private GameController controller;
	private BrokerInt<ResponseMessage, String> broker;
	
	private Token token1;
	private Token token2;
	private Token token3;
	
	private List<PermitCard> permitCardsFromCoast = new ArrayList<>();
	private List<PermitCard> permitCardsFromHill = new ArrayList<>();
	private List<PermitCard> permitCardsFromMountain = new ArrayList<>();
	private List<PoliticCard> firstpoliticCards = new ArrayList<>();
	private List<PoliticCard> secondpoliticCards = new ArrayList<>();
	private List<Assistant> assistants = new ArrayList<>();
	
	private List<PoliticCardBoundle> politicCardBoundles = new ArrayList<>();
	private List<AssistantBoundle> assistantBoundles = new ArrayList<>();
	private List<PermitCardBoundle> permitCardBoundles = new ArrayList<>();
	
	private Map<List<String>, Integer> permitCardsInt = new HashMap<>();
	private Map<List<Integer>, Integer> assistantInt = new HashMap<>();
	private Map<List<Colore>, Integer> colors = new HashMap<>();
	
	//create a broker that doesn't do anything message but allowa easy-testing
	//For the class that need it.
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
		
	@Before
	public void initTest(){
		broker = new ProxyBroker();
		controller = new GameController(broker, 40);
		
		token1 = new Token();
		token2 = new Token();
		token3 = new Token();
		
		controller.addPlayer(token1, new SubscriberRMI<>(null));
		controller.addPlayer(token2, new SubscriberRMI<>(null));
		controller.addPlayer(token3, new SubscriberRMI<>(null));
		
	}
	
	private void loadShoppingCarts() {
		
		//init PermitCardBoundle
		permitCardsFromCoast.add(controller.getGame().getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getUsableCard().get(0));
		permitCardsFromCoast.add(controller.getGame().getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getUsableCard().get(1));
		permitCardsFromHill.add(controller.getGame().getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0));
		permitCardsFromHill.add(controller.getGame().getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(1));
		permitCardsFromMountain.add(controller.getGame().getBoard().getRegions(RegionType.MOUNTAIN).getDeckOfPermitCard().getUsableCard().get(0));
		permitCardsFromMountain.add(controller.getGame().getBoard().getRegions(RegionType.MOUNTAIN).getDeckOfPermitCard().getUsableCard().get(1));
		
		PermitCardBoundle permitCoastBoundle = new PermitCardBoundle(2, permitCardsFromCoast);
		PermitCardBoundle permitHillBoundle = new PermitCardBoundle(5, permitCardsFromHill);
		PermitCardBoundle permitMountainBoundle = new PermitCardBoundle(9, permitCardsFromMountain);
		
		permitCardBoundles.add(permitCoastBoundle);
		permitCardBoundles.add(permitHillBoundle);
		permitCardBoundles.add(permitMountainBoundle);
		
		//init PoliticCardBoundle
		PoliticCardPrototype prototype = new PoliticCardPrototype();
		
		prototype = new PoliticCardPrototype();
				
		for(int i = 0; i < 3; i++)
			firstpoliticCards.add(prototype.getPoliticCard());

		for(int i = 0; i < 3; i++)
			secondpoliticCards.add(prototype.getPoliticCard());

		PoliticCardBoundle firstPoliticBoundle = new PoliticCardBoundle(3, firstpoliticCards);
		PoliticCardBoundle secondPoliticBoundle = new PoliticCardBoundle(18, secondpoliticCards);

		politicCardBoundles.add(firstPoliticBoundle);
		politicCardBoundles.add(secondPoliticBoundle);

		//init AssistantBoundle
		for(int i = 0; i < 3; i++)
			assistants.add(new Assistant());

		AssistantBoundle assistantBoundle = new AssistantBoundle(16, assistants);

		assistantBoundles.add(assistantBoundle);
	}
	
	@Test (expected = NullPointerException.class)
	public void testConstructorShouldThrowsNullPointerExceptionIfGameControllerIsNull() {
		new Market(null);
	}
	
	@Test 
	public void testConstructorShouldSetSellStateInMarketTurnMachine() {
		controller.initGame();
		//stop the timer associated with the game controller
		controller.timerTask.cancel();
		Market market = new Market(controller);
		assertEquals(market.getTurnMachine().getCurrentState(), SellState.getInstance());
	}

	@Test
	public void testSellShouldChangeSellerIfTheActionIsSuccessfullyExecuted(){
		controller.initGame();
		//stop the timer associated with the game controller
		controller.timerTask.cancel();
		
		//Pass the initialize gameController to the market to be tested
		Market market = new Market(controller);
		
		market.getTurnMachine().setCurrentState(SellState.getInstance());
		controller.setCurrentPlayer(controller.getPlayers().get(0));
		MarketSellRqst rqst = new MarketSellRqst(controller.getPlayers().get(0), colors, assistantInt, permitCardsInt);
		Token currentPlayer = controller.getCurrentPlayer();
		market.sell(rqst);
		Assert.assertFalse(controller.getCurrentPlayer().equals(currentPlayer));
	}
	
	@Test
	public void testSellShouldNotChangeSellerIfTheActionIsNotSuccessfullyExecuted(){
		
		controller.initGame();
		//stop the timer associated with the game controller
		controller.timerTask.cancel();
		
		//Pass the initialize gameController to the market to be tested
		Market market = new Market(controller);
		
		//set to buyState to reject the request
		market.getTurnMachine().setCurrentState(BuyState.getInstance());
		
		MarketSellRqst rqst = new MarketSellRqst(controller.getPlayers().get(0), colors, assistantInt, permitCardsInt);
		controller.setCurrentPlayer(controller.getPlayers().get(0));
		Token currentPlayer = controller.getCurrentPlayer();
		market.sell(rqst);
		assertEquals(currentPlayer, controller.getCurrentPlayer());
	}
	
	@Test
	public void testBuyingTimerShouldScheduleBuyYaskInTheTimer(){
		
	}
	
	@Test
	public void testChangeCurrentSellerShouldChangeCurrentPlayerIfHeIsNotTheLast(){
		controller.initGame();
		//stop the timer associated with the game controller
		controller.timerTask.cancel();
		
		//Pass the initialize gameController to the market to be tested
		Market market = new Market(controller);
		controller.playersIterator = controller.getPlayers().iterator();
		controller.setCurrentPlayer(controller.playersIterator.next());
		
		Token currentPlayer = controller.getCurrentPlayer();
		market.changeSeller();
		Assert.assertFalse(controller.getCurrentPlayer().equals(currentPlayer));
	}
	
	@Test
	public void testChangeCurrentSellerShouldSetCurrentPlayerToDefaultPlayerSetCurrentStateOfMarketTurnMachineToBuyStateIfThePlayerIsTheLast(){
		controller.initGame();
		//stop the timer associated with the game controller
		controller.timerTask.cancel();
		
		//Pass the initialize gameController to the market to be tested
		Market market = new Market(controller);
		controller.playersIterator = controller.getPlayers().iterator();
		controller.setCurrentPlayer(controller.playersIterator.next());
		
		market.changeSeller();
		market.changeSeller();
		market.changeSeller();
		
		assertEquals(controller.getCurrentPlayer(), controller.defaultPlayer);
		assertEquals(market.getTurnMachine().getCurrentState(), BuyState.getInstance());
	}
	
	@Test
	public void testGetTurnMachineShouldReturnAnInstanceOfMarketTurnMachine(){
		controller.initGame();
		//stop the timer associated with the game controller
		controller.timerTask.cancel();
		
		//Pass the initialize gameController to the market to be tested
		Market market = new Market(controller);
		
		assertThat(market.getTurnMachine(), is(instanceOf(MarketTurnMachine.class)));
	}
	
	@Test
	public void testRestoreMarketShouldEmptyTheStoreAndGiveBackRelativeUnsolBoundleToPlayer(){
		controller.initGame();
		//stop the timer associated with the game controller
		controller.timerTask.cancel();
		
		loadShoppingCarts();
		
		//Pass the initialize gameController to the market to be tested
		Market market = new Market(controller);
		
		controller.getGame().getStore().put(token1, new ShoppingCart(politicCardBoundles, Collections.emptyList(), Collections.emptyList()));
		controller.getGame().getStore().put(token2, new ShoppingCart(Collections.emptyList(), assistantBoundles, Collections.emptyList()));
		controller.getGame().getStore().put(token3, new ShoppingCart(Collections.emptyList(), Collections.emptyList(), permitCardBoundles));
		
		List<PoliticCard> politicCardBeforeRestore = new ArrayList<>(controller.getGame().getPlayers().get(token1.getPlayerNumber()).getPoliticCards());
		Queue<Assistant> assistantBeforeRestore = new LinkedList<>(controller.getGame().getPlayers().get(token2.getPlayerNumber()).getAssistants());
		List<PermitCard> permitCardBeforeRestore = new ArrayList<>(controller.getGame().getPlayers().get(token3.getPlayerNumber()).getPermitCards());
		
		market.restoreMarket();
		
		for(ShoppingCart cart : controller.getGame().getStore().values())
			Assert.assertTrue(cart.getListOfBoundles().isEmpty());
		
		Assert.assertTrue(controller.getGame().getPlayers().get(token1.getPlayerNumber()).getPoliticCards().size() == politicCardBeforeRestore.size() + 6 );
		Assert.assertTrue(controller.getGame().getPlayers().get(token2.getPlayerNumber()).getAssistants().size() == assistantBeforeRestore.size() + 3);
		Assert.assertTrue(controller.getGame().getPlayers().get(token3.getPlayerNumber()).getPermitCards().size() == permitCardBeforeRestore.size() + 6);
	}

	@Test
	public void testBuyTaskShouldRestoreMarketChangeStateOfMarketTurnMachineToOffStateAndChangeCurrentPlayerAndStartsActionTimer() throws InterruptedException{
		controller.initGame();
		//stop the timer associated with the game controller
		controller.timerTask.cancel();
		
		loadShoppingCarts();
		
		//Pass the initialize gameController to the market to be tested
		Market market = new Market(controller);
		
		controller.getGame().getStore().put(token1, new ShoppingCart(politicCardBoundles, Collections.emptyList(), Collections.emptyList()));
		controller.getGame().getStore().put(token2, new ShoppingCart(Collections.emptyList(), assistantBoundles, Collections.emptyList()));
		controller.getGame().getStore().put(token3, new ShoppingCart(Collections.emptyList(), Collections.emptyList(), permitCardBoundles));
		
		List<PoliticCard> politicCardBeforeRestore = new ArrayList<>(controller.getGame().getPlayers().get(token1.getPlayerNumber()).getPoliticCards());
		Queue<Assistant> assistantBeforeRestore = new LinkedList<>(controller.getGame().getPlayers().get(token2.getPlayerNumber()).getAssistants());
		List<PermitCard> permitCardBeforeRestore = new ArrayList<>(controller.getGame().getPlayers().get(token3.getPlayerNumber()).getPermitCards());
		
		controller.playersIterator = controller.getPlayers().iterator();
		controller.setCurrentPlayer(controller.playersIterator.next());
		
		market.changeSeller();
		market.changeSeller();
		market.changeSeller();

		Assert.assertTrue(controller.getCurrentPlayer() == controller.defaultPlayer);
		Assert.assertTrue(market.getTurnMachine().getCurrentState() == BuyState.getInstance());
		
		Thread.sleep(65 * 1000);
		
		Assert.assertTrue(market.getTurnMachine().getCurrentState() == OffState.getInstance());
		
		//timerTask.cancel() return true only if it cancel a task that was already scheduled but not execute yet,
		//this ensure that the actionTimer was executed correctly
		Assert.assertTrue(controller.timerTask.cancel());
		
		//This is to ensure that restore market occours
		for(ShoppingCart cart : controller.getGame().getStore().values())
			Assert.assertTrue(cart.getListOfBoundles().isEmpty());
		
		//politiCard has to be equals to previous size of politicCard + PoliticaCard obtainby restore + PolitiCard drawn
		Assert.assertTrue(controller.getGame().getPlayers().get(token1.getPlayerNumber()).getPoliticCards().size() == politicCardBeforeRestore.size() + 6 + 1);
		Assert.assertTrue(controller.getGame().getPlayers().get(token2.getPlayerNumber()).getAssistants().size() == assistantBeforeRestore.size() + 3);
		Assert.assertTrue(controller.getGame().getPlayers().get(token3.getPlayerNumber()).getPermitCards().size() == permitCardBeforeRestore.size() + 6);
	
		//Ensure that game is restarted normally in gaming phase
		Assert.assertTrue(controller.getCurrentPlayer() == controller.getPlayers().get(0));
	}

}
