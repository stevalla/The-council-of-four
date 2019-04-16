package it.polimi.ingsw.cg32.controller;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.message.request.ChangeMapRqst;
import it.polimi.ingsw.cg32.message.request.EndTurnRqst;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByKingHelpRqst;
import it.polimi.ingsw.cg32.message.request.action.BuyPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.ElectCouncillorRqst;
import it.polimi.ingsw.cg32.message.request.action.HireAssistantRqst;
import it.polimi.ingsw.cg32.message.request.action.PerformAnotherPrimaryActionRqst;
import it.polimi.ingsw.cg32.message.request.bonus.AssistantBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.CityBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.CoinsBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.NobilityBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.PermitCardBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.PoliticCardBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.PrimaryActionBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.ReusePermitCardBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.VictoryBonusRqst;
import it.polimi.ingsw.cg32.message.request.market.MarketSellRqst;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.server.Token;
import it.polimi.ingsw.cg32.view.server.pubsub.BrokerInt;
import it.polimi.ingsw.cg32.view.server.pubsub.SubscriberInt;
import it.polimi.ingsw.cg32.view.server.rmi.SubscriberRMI;

public class TestGameController {

	GameController controller;
	private BrokerInt<ResponseMessage, String> broker;
	private SubscriberInt<ResponseMessage> subscriber;
	
	private Token token0; //First player
	private Token token1; //Second player
	private Token token2; //Third player only if the game is with three players
	
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
			System.out.println(topic);
		}

		@Override
		public <V> Token getTokenBySuscriber(V subscriber) {
			return null;
		}
		
	}
	
	
	@Rule
	public final SystemOutRule system = new SystemOutRule().enableLog().muteForSuccessfulTests();
	
	@Before
	public void initTest(){
		
		broker = new ProxyBroker();
		controller = new GameController(broker, 20);
		subscriber = new SubscriberRMI<>(null);
		
		token0 = new Token();
		token1 = new Token();
		token2 = new Token();
		system.enableLog();
		
	}
	
	 
	@Test (expected = NullPointerException.class)
	public void testConstructorWithBrokerNullShouldThrowsNullPointerException() {
		new GameController(null, 20);
	}

	@Test 
	public void testConstructorWithValidParamsShouldCreateGameControllerCorrectly() {
		GameController controller = new GameController(broker, 20);
		assertNotNull(controller.getPlayers());
		assertNull(controller.getGame());
		assertFalse(controller.isGameStarted());
		assertEquals("src/main/resources/map/default.txt", controller.getCurrentMap());
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddPlayerWhenTheTokenIsNullShouldThrowANullPointerExcetpion() {
		controller.addPlayer(null, subscriber);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAddPlayerWhenTheSubscriberIsNullShouldThrowANullPointerExcetpion() {
		controller.addPlayer(token0, null);
	}
	
	@Test
	public void testAddPlayerWhenTokenIsAdministratorAndGameIsNotStartedShouldPublishACommunicationResponseToTheAdministratorToken() {
		token0.setAdministrator(true);
		controller.addPlayer(token0, subscriber);
		assertTrue(system.getLog().contains(controller.getPlayerTopic(token0)));
	}
	
	@Test
	public void testAddPlayerShouldAddTokenRecivedToTheListOfTokenIfTheGameIsNull() throws IOException{
		controller.addPlayer(token0, subscriber);
		Assert.assertTrue(controller.getPlayers().size() == 1);
		Assert.assertTrue(controller.getPlayers().contains(token0));
	}
	
	@Test
	public void testAddPlayerShouldDoNothingIfGameIsAlreadyStarted() throws IOException{
	
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		
		controller.initGame();
		
		Assert.assertTrue(controller.getPlayers().size() == 2);
		
		controller.addPlayer(token2, subscriber);
		
		Assert.assertTrue(controller.getPlayers().size() == 2);
		Assert.assertFalse(controller.getPlayers().contains(token2));
	}
	
	@Test
	public void testChangeMapIfTheTokenIsNotTheAdministratorTheMapIsCorrectAndTheGameIsNotStartedShouldNotChangeTheMap() {
		controller.addPlayer(token0, subscriber);
		Assert.assertEquals("src/main/resources/map/default.txt", controller.getCurrentMap());
		controller.update(new ChangeMapRqst(token0, "circle"));
		Assert.assertEquals("src/main/resources/map/default.txt", controller.getCurrentMap());
	}
	
	@Test
	public void testChangeMapShouldChangeTheCurrentMapAsRequestedIfMapProvidedIsValidTheGameIsNotStartedAndTheTokenIsAdministrator(){
		token0.setAdministrator(true);
		controller.addPlayer(token0, subscriber);
		controller.update(new ChangeMapRqst(token0, "circle"));
		Assert.assertTrue(controller.getPlayers().contains(token0));
		Assert.assertEquals("src\\main\\resources\\map\\circle.txt", controller.getCurrentMap());
	}
	
	@Test 
	public void testChangeMapShouldNotChangeCurentMapIfMapProvidedIsNotValid(){
		token0.setAdministrator(true);
		controller.addPlayer(token0, subscriber);
		Assert.assertEquals("src/main/resources/map/default.txt", controller.getCurrentMap());
		controller.update(new ChangeMapRqst(token0, "error"));
		Assert.assertEquals("src/main/resources/map/default.txt", controller.getCurrentMap());
	}
	
	@Test 
	public void testChangeMapShouldNotChangeCurentMapIfTheGameIsAltreayStarted(){
		token0.setAdministrator(true);
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		
		controller.initGame();
		Assert.assertEquals("src/main/resources/map/default.txt", controller.getCurrentMap());
		controller.update(new ChangeMapRqst(token0, "circle"));
		Assert.assertEquals("src/main/resources/map/default.txt", controller.getCurrentMap());
	}
	
	@Test 
	public void testGetCurrentMapShouldReturnTheCurrentMap(){
		controller.addPlayer(token0, subscriber);
		
		Assert.assertEquals("src/main/resources/map/default.txt", controller.getCurrentMap());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitGameShouldThrowsIllegalArgumentExceptionIfThereIsJustOnePlayerOnTheListOfPlayer(){
		
		controller.addPlayer(token0, subscriber);
		
		controller.initGame();
	}
	
	@Test 
	public void testInitGameShouldInitializeAllTheFieldsOfGameControllerAsExpectedIfNoErrorOccurred(){
		
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		assertNull(controller.getGame());
		assertNull(controller.timer);
		assertNull(controller.playersIterator);
		assertNull(controller.getMarket());
		
		controller.initGame();
		
		assertNotNull(controller.getMarket());
		assertNotNull(controller.playersIterator);
		assertNotNull(controller.timer);
		assertNotNull(controller.getGame());
		assertNotNull(controller.getCurrentPlayer());
	}
	
	@Test 
	public void testInitGameShouldChangeThePlayerIdInToken(){
		
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		
		controller.initGame();
		assertEquals(2, controller.getPlayers().stream().filter(t -> t.getPlayerNumber() < 2).count());
	}
	
	@Test
	public void testInitGameShouldChangeTheCurrentPlayerAndSetThePlayerWithPlayerNumberEqualsToZero() {
		
		assertFalse(controller.getCurrentPlayer().getPlayerNumber() == 0);

		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		assertTrue(controller.getCurrentPlayer().getPlayerNumber() == 0);
	}
	
	@Test
	public void testIsGameStartedShouldReturnFalseBeforeInitGameAndTrueAfterInitGame(){
		
		assertFalse(controller.isGameStarted());
		
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		
		controller.initGame();
		
		Assert.assertTrue(controller.isGameStarted());
	}
	
	@Test
	public void testExecuteActionWhenActionIsAValidActionWithOutBonusShouldPublicAResponseToAllTheClientsWithTheTopicOfTheGameAndNotAddBonusToBonusToExecute() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		HireAssistantRqst request = new HireAssistantRqst(token0);
		assertTrue(controller.getBonusToExecute().isEmpty());
		controller.update(request);
		assertTrue(system.getLog().contains(controller.getTopic()));
		assertTrue(controller.getBonusToExecute().isEmpty());
	}
	
	@Test
	public void testExecuteActionWhenActionIsNotValidShouldPublishToTheClientTheErrorInTheRequestWithTheClinetTopic() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		PerformAnotherPrimaryActionRqst request = new PerformAnotherPrimaryActionRqst(token0);
		//The first player has no enough money to execute this action
		controller.update(request);
		assertTrue(system.getLog().contains(controller.getPlayerTopic(token0)));
	}
	
	@Test
	public void testExecuteActionWhenThereAreBonusToExecuteAndTheRequestIsAnActionRqstShouldPublishToTheClientTheErrorInTheRequestWithTheClinetTopic() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		controller.getBonusToExecute().add(new SingleBonus("NOBILITYBONUS"));
		HireAssistantRqst request = new HireAssistantRqst(token0);
		controller.update(request);
		assertTrue(system.getLog().contains(controller.getTopic()));
	}
	
	@Test
	public void testExecuteActionWHenActionReturnBonusToExecuteShouldAddTheBonusTTheBonusToExecute() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		assertTrue(controller.getBonusToExecute().isEmpty());
		
		//Give to the player four jolly politic card to make this action valid for him
		PoliticCardPrototype prototype = new PoliticCardPrototype();
		List<Colore> colors = new ArrayList<>();
		for(;;) {
			PoliticCard card = prototype.getPoliticCard();
			if(card.getCardColor() != Colore.JOLLY)
				continue;
			controller.getGame().getPlayers().get(0).getPoliticCards().add(card);
			colors.add(card.getCardColor());
			if(colors.size() == 4)
				break;
		}
		
		String permitCardId = controller.getGame().getBoard().getRegions(RegionType.COAST)
				.getDeckOfPermitCard().getUsableCard().get(0).getId();
		BuyPermitCardRqst request = new BuyPermitCardRqst(token0, controller.getGame().getBoard().getRegions(RegionType.COAST), 
				permitCardId, colors);
		controller.update(request);
		assertFalse(controller.getBonusToExecute().isEmpty());
	}
	
	@Test
	public void testExecuteBonusWhenTheRequestIsABonusRequestButTheBonusIsNotContainedInBonusToExecuteShouldPublishToTheClientTheErrorInTheRequestWithTheClinetTopic() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		CoinsBonusRqst request = new CoinsBonusRqst(token0, new QuantityBonus("COINSBONUS", 2));
		controller.update(request);
		assertTrue(system.getLog().contains(controller.getPlayerTopic(token0)));
	}
	
	@Test
	public void testExecuteBonusWhenTheRequestIsABonusRequestAndTheBonusIsContainedInBonusToExecuteAndTurnMachineIsInBonusStateShouldExecuteTheActionAndRemoveTheBonusFromBonusToExecute() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		//Give to the player four jolly politic card to make this action valid for him
		PoliticCardPrototype prototype = new PoliticCardPrototype();
		List<Colore> colors = new ArrayList<>();
		for(;;) {
			PoliticCard card = prototype.getPoliticCard();
			if(card.getCardColor() != Colore.JOLLY)
				continue;
			controller.getGame().getPlayers().get(0).getPoliticCards().add(card);
			colors.add(card.getCardColor());
			if(colors.size() == 4)
				break;
		}
		
		PermitCard permitCard = controller.getGame().getBoard().getRegions(RegionType.COAST)
				.getDeckOfPermitCard().getUsableCard().get(0);
		BuyPermitCardRqst request = new BuyPermitCardRqst(token0, controller.getGame().getBoard().getRegions(RegionType.COAST), 
				permitCard.getId(), colors);
		//Execute this action to get the bonus and change turn machine to BonusState
		controller.update(request);
		
		List<Bonus> bonus = permitCard.getBonus();
		
		assertTrue(controller.getBonusToExecute().size() == bonus.size());
		
		City city = controller.getGame().getBoard().getCities().get(0);
		
		bonus.forEach(b -> {
			switch (b.getName()) {
			case "COINSBONUS":
				assertTrue(controller.getBonusToExecute().contains(b));
				CoinsBonusRqst coins = new CoinsBonusRqst(token0, b);
				controller.update(coins);
				assertFalse(controller.getBonusToExecute().contains(b));
				break;
			case "VICTORYBONUS":
				assertTrue(controller.getBonusToExecute().contains(b));
				VictoryBonusRqst victory = new VictoryBonusRqst(token0, b);
				controller.update(victory);
				assertFalse(controller.getBonusToExecute().contains(b));
				break;
			case "NOBILITYBONUS":
				assertTrue(controller.getBonusToExecute().contains(b));
				NobilityBonusRqst nobility = new NobilityBonusRqst(token0, b);
				controller.update(nobility);
				assertFalse(controller.getBonusToExecute().contains(b));
				break;
			case "POLITICCARDBONUS":
				assertTrue(controller.getBonusToExecute().contains(b));
				PoliticCardBonusRqst politic = new PoliticCardBonusRqst(token0, b);
				controller.update(politic);
				assertFalse(controller.getBonusToExecute().contains(b));
				break;
			case "ASSISTANTBONUS":
				assertTrue(controller.getBonusToExecute().contains(b));
				AssistantBonusRqst assistant = new AssistantBonusRqst(token0, b);
				controller.update(assistant);
				assertFalse(controller.getBonusToExecute().contains(b));
				break;
			case "CITYBONUS":
				assertTrue(controller.getBonusToExecute().contains(b));
				CityBonusRqst cityBonus = new CityBonusRqst(token0, city, b);
				controller.update(cityBonus);
				assertFalse(controller.getBonusToExecute().contains(b));
				break;
			case "PERMITCARDBONUS":
				assertTrue(controller.getBonusToExecute().contains(b));
				PermitCard card = controller.getGame().getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getUsableCard().get(0);
				PermitCardBonusRqst permitCardBonus = new PermitCardBonusRqst(token0, card.getId(), 
						controller.getGame().getBoard().getRegions(RegionType.COAST), b);
				controller.update(permitCardBonus);
				assertFalse(controller.getBonusToExecute().contains(b));
				break;
			case "REUSEPERMITCARDBONUS":
				assertTrue(controller.getBonusToExecute().contains(b));
				ReusePermitCardBonusRqst reuse = new ReusePermitCardBonusRqst(token0, permitCard.getId(), b);
				controller.update(reuse);
				assertFalse(controller.getBonusToExecute().contains(b));
				break;
			case "PRIMARYACTIONBONUS":
				assertTrue(controller.getBonusToExecute().contains(b));
				Colore color = controller.getGame().getBoard().getCouncillorReserve().getCouncillorInReserve().get(0).getColor();
				ElectCouncillorRqst requestPrimary = new ElectCouncillorRqst(token0, 
						controller.getGame().getBoard().getKingRegion(), color);
				PrimaryActionBonusRqst primary = new PrimaryActionBonusRqst(token0, requestPrimary, b);
				controller.update(primary);
				assertFalse(controller.getBonusToExecute().contains(b));
				break;
			default:
				break;
			}
		});
	}
	
	/**
	 * When a player build his last emporium the controller set the last turn foreach player starting from the player
	 * that build the last emporium. When the current player is the player that has build his last emporium
	 * the game is finished and so the boolean {@link GameController#isGameStarted()} return false
	 */
	@Test
	public void testExecuteActionWhenAPlyerBuildTheLastEmporiumShouldTriggerTheLastTurnForAllTheOtherPlayersAndWhenAllTheOtherPlayersAreFinishedTheTurnSetIsGameStartedToNull() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		assertTrue(controller.isGameStarted());
		
		//Give to the player four jolly politic card to make this action valid for him
		PoliticCardPrototype prototype = new PoliticCardPrototype();
		List<Colore> colors = new ArrayList<>();
		for(;;) {
			PoliticCard card = prototype.getPoliticCard();
			if(card.getCardColor() != Colore.JOLLY)
				continue;
			controller.getGame().getPlayers().get(0).getPoliticCards().add(card);
			colors.add(card.getCardColor());
			if(colors.size() == 4)
				break;
		}
		
		//Delete nine emporiums of the first so in the next build emporium he finished the emporium
		for(int i=0; i<9; i++)
			controller.getGame().getPlayers().get(0).getEmporiums().poll();
		
		//Create and execute a primary action for the first player
		
		//Select a city without bonus
		City city = controller.getGame().getBoard().getCities().get(9);
		BuildEmporiumByKingHelpRqst rqst = new BuildEmporiumByKingHelpRqst(token0, city, colors);
		controller.update(rqst);
		controller.update(new EndTurnRqst(token0));
		
		//Make a primary action with the second player to finished the action phase
		Colore color = controller.getGame().getBoard().getCouncillorReserve().getCouncillorInReserve().get(0).getColor();
		ElectCouncillorRqst requestPrimary = new ElectCouncillorRqst(token1, 
				controller.getGame().getBoard().getKingRegion(), color);
		controller.update(requestPrimary);
		controller.update(new EndTurnRqst(token1));
		
		//Market sell phase only for finished and return to the players that has built his last emporium
		controller.update(new MarketSellRqst(token0, new HashMap<>(), new HashMap<>(), new HashMap<>()));
		controller.update(new MarketSellRqst(token1, new HashMap<>(), new HashMap<>(), new HashMap<>()));
		
		//Wait 60 seconds to finish the market buy phase
		try {
			Thread.sleep(65 * 1000);
		} catch (InterruptedException e) {
		}
		
		//Now the method isGameStarted should return false
		assertFalse(controller.isGameStarted());
	}
	
	@Test
	public void testExecuteActionWhenThePlayerHasExecuteAPrimaryAndASecondaryActionShouldChangeTheCurrentPlayerToTheNext() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		assertEquals(token0, controller.getCurrentPlayer());
		
		//Make a primary action
		Colore color = controller.getGame().getBoard().getCouncillorReserve().getCouncillorInReserve().get(0).getColor();
		ElectCouncillorRqst requestPrimary = new ElectCouncillorRqst(token0, 
				controller.getGame().getBoard().getKingRegion(), color);
		controller.update(requestPrimary);
		
		//Make a secondary action
		HireAssistantRqst request = new HireAssistantRqst(token0);
		controller.update(request);
		
		//Now the current player should be the token1
		assertEquals(token1, controller.getCurrentPlayer());
	}
	
	
	@Test
	public void testEndTurnShouldNotChangeCurrentPlayerIfHeHasNotAlreadyPerformsAPrimaryAction(){
		
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		
		controller.initGame();
		
		Assert.assertEquals(token0, controller.getCurrentPlayer());
		controller.update(new EndTurnRqst(token0));
		Assert.assertEquals(token0, controller.getCurrentPlayer());
	}
	
	@Test
	public void testEndTurnWhenThereAreBonusToExecuteShouldNotChangeTheCurrentPlayer() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		//Perform an action that return some bonus to make not empty the list of bonus to execute
		PoliticCardPrototype prototype = new PoliticCardPrototype();
		List<Colore> colors = new ArrayList<>();
		for(;;) {
			PoliticCard card = prototype.getPoliticCard();
			if(card.getCardColor() != Colore.JOLLY)
				continue;
			controller.getGame().getPlayers().get(0).getPoliticCards().add(card);
			colors.add(card.getCardColor());
			if(colors.size() == 4)
				break;
		}
		
		PermitCard permitCard = controller.getGame().getBoard().getRegions(RegionType.COAST)
				.getDeckOfPermitCard().getUsableCard().get(0);
		BuyPermitCardRqst request = new BuyPermitCardRqst(token0, controller.getGame().getBoard().getRegions(RegionType.COAST), 
				permitCard.getId(), colors);
		
		controller.update(request);
		assertEquals(token0, controller.getCurrentPlayer());
		controller.update(new EndTurnRqst(token0));
		assertEquals(token0, controller.getCurrentPlayer());
	}
	
	@Test
	public void testEndTurnInMarketSellPhaseShouldNotChangeTheCurrentPlayer() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		//First player
		//Make a primary action
		Colore color = controller.getGame().getBoard().getCouncillorReserve().getCouncillorInReserve().get(0).getColor();
		ElectCouncillorRqst requestPrimary = new ElectCouncillorRqst(token0, 
				controller.getGame().getBoard().getKingRegion(), color);
		controller.update(requestPrimary);
		//EndTurn
		controller.update(new EndTurnRqst(token0));
		
		//Second player
		//Make a primary action
		color = controller.getGame().getBoard().getCouncillorReserve().getCouncillorInReserve().get(0).getColor();
		requestPrimary = new ElectCouncillorRqst(token1, 
				controller.getGame().getBoard().getKingRegion(), color);
		controller.update(requestPrimary);
		//EndTurn
		controller.update(new EndTurnRqst(token1));
		
		//MarketSell first player
		assertEquals(token0, controller.getCurrentPlayer());
		controller.update(new EndTurnRqst(token0));
		assertEquals(token0, controller.getCurrentPlayer());
	}
	
	@Test
	public void testEndTurnWhenThePlayerHadPerformAPrimaryActionShouldChangeTheCurrentPlayer() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		//First player
		//Make a primary action
		Colore color = controller.getGame().getBoard().getCouncillorReserve().getCouncillorInReserve().get(0).getColor();
		ElectCouncillorRqst requestPrimary = new ElectCouncillorRqst(token0, 
				controller.getGame().getBoard().getKingRegion(), color);
		controller.update(requestPrimary);
		//EndTurn
		assertEquals(token0, controller.getCurrentPlayer());
		controller.update(new EndTurnRqst(token0));
		assertNotEquals(token0, controller.getCurrentPlayer());
	}
	
	@Test
	public void testChangeCurrentPlayerIsNotTheLastPlayerShouldChangeToTheNextPlayer() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		assertEquals(token0, controller.getCurrentPlayer());
		controller.changeCurrentPlayer();
		assertEquals(token1, controller.getCurrentPlayer());
	}
	
	@Test
	public void testChangeCurrentPlayerIsTheLastPlayerShouldStartsTheMarketSellPhaseAndSetTheFirstPlayerAsCurrentPlayer() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		//Go to the second player turn
		controller.changeCurrentPlayer();
		assertEquals(token1, controller.getCurrentPlayer());
		controller.changeCurrentPlayer();
		assertEquals(token0, controller.getCurrentPlayer());
	}
	

	@Test
	public void testChangeCurrentPlayerWhenThePlayerThatWasPreviouslySetAsIsGameFinishedIsReachedShouldPutIsGameStartedToFalseAndCancelAllTheTimerTask() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		//Give four jolly card to player 1 to make avaible the construction of his last emporium
		PoliticCardPrototype prototype = new PoliticCardPrototype();
		List<Colore> colors = new ArrayList<>();
		for(;;) {
			PoliticCard card = prototype.getPoliticCard();
			if(card.getCardColor() != Colore.JOLLY)
				continue;
			controller.getGame().getPlayers().get(1).getPoliticCards().add(card);
			colors.add(card.getCardColor());
			if(colors.size() == 4)
				break;
		}
		
		//Make a turn with the first player
		Colore color = controller.getGame().getBoard().getCouncillorReserve().getCouncillorInReserve().get(0).getColor();
		ElectCouncillorRqst requestPrimary = new ElectCouncillorRqst(token0, 
				controller.getGame().getBoard().getKingRegion(), color);
		controller.update(requestPrimary);
		controller.update(new EndTurnRqst(token0));
		
		//Delete nine emporiums of the second player so in the next build emporium he finished the emporium
		for(int i=0; i<9; i++)
			controller.getGame().getPlayers().get(1).getEmporiums().poll();
		
		//Create and execute a primary action for the second player the end the turn
		
		//Select a city without bonus
		City city = controller.getGame().getBoard().getCities().get(9);
		BuildEmporiumByKingHelpRqst rqst = new BuildEmporiumByKingHelpRqst(token1, city, colors);
		controller.update(rqst);
		controller.update(new EndTurnRqst(token1));
		
		//Market sell phase only for finished and return to the players that has built his last emporium
		controller.update(new MarketSellRqst(token0, new HashMap<>(), new HashMap<>(), new HashMap<>()));
		controller.update(new MarketSellRqst(token1, new HashMap<>(), new HashMap<>(), new HashMap<>()));
		
		//Wait 60 seconds to finish the market buy phase
		try {
			Thread.sleep(65 * 1000);
		} catch (InterruptedException e) {
		}
		
		//Now we returned at the head of the players' list and with the next command the game should finished
		controller.changeCurrentPlayer();
		assertFalse(controller.isGameStarted());
	}
	
	@Test
	public void testUpdateWhenTheTokenIsNotPresentInPlayersShouldNotExecuteTheActionALsoIfItIsValidSoTheModelNotChange() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		int sizeBefore = controller.getGame().getBoard().getAssistantsReserve().getAssistants().size();
		//This action should change the size of the assistant reserve
		controller.update(new HireAssistantRqst(token2));
		assertTrue(sizeBefore == controller.getGame().getBoard().getAssistantsReserve().getAssistants().size());
	}
	
	@Test
	public void testUpdateWhenArriveAnActionRqstWhenThePlayerIsNotTheCurrentPlayerShouldNotChangeTheModelALdoIfTheActionIsValid() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		int sizeBefore = controller.getGame().getBoard().getAssistantsReserve().getAssistants().size();
		//Set coins to player to make sure that the action is valid
		controller.getGame().getPlayers().get(1).getCoinsTrack().setCoinsNumber(3);
		
		//This action should change the size of the assistant reserve but current player is token0
		controller.update(new HireAssistantRqst(token1));
		assertTrue(sizeBefore == controller.getGame().getBoard().getAssistantsReserve().getAssistants().size());
	}
	
	@Test
	public void testUpdateWhenTheRqstArrivedWhileTheTimerTaskIsRunningShouldNotChangeTheModel() {
		controller = new GameController(broker, 5);
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		int sizeBefore = controller.getGame().getBoard().getAssistantsReserve().getAssistants().size();
		//Set coins to player to make sure that the action is valid
		controller.getGame().getPlayers().get(1).getCoinsTrack().setCoinsNumber(3);
		
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
		}
		
		//This action should change the size of the assistant reserve but current player is token0
		controller.update(new HireAssistantRqst(token1));
		assertTrue(sizeBefore == controller.getGame().getBoard().getAssistantsReserve().getAssistants().size());
	}
	
	@Test
	public void testEndGameWHenTwoPlayerBuildTheLastEmporiumShouldFinishedTheGameWhenTheTurnIsOfTheFirstPlayerWhoBuiltTheEmporium() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		//Give four jolly card to player 1 to make avaible the construction of his last emporium
		PoliticCardPrototype prototype = new PoliticCardPrototype();
		List<Colore> colors = new ArrayList<>();
		for(;;) {
			PoliticCard card = prototype.getPoliticCard();
			if(card.getCardColor() != Colore.JOLLY)
				continue;
			controller.getGame().getPlayers().get(0).getPoliticCards().add(card);
			colors.add(card.getCardColor());
			if(colors.size() == 4)
				break;
		}
		
		//Put the last emporium with the first player
		//Delete nine emporiums of the second player so in the next build emporium he finished the emporium
		for(int i=0; i<9; i++)
			controller.getGame().getPlayers().get(0).getEmporiums().poll();

		//Create and execute a primary action for the second player the end the turn

		//Select a city without bonus
		City city = controller.getGame().getBoard().getCities().get(9);
		BuildEmporiumByKingHelpRqst rqst = new BuildEmporiumByKingHelpRqst(token0, city, colors);
		controller.update(rqst);
		controller.update(new EndTurnRqst(token1));
		
		//Put the last emporium with the seconda player
		//Delete nine emporiums of the second player so in the next build emporium he finished the emporium
		for(int i=0; i<9; i++)
			controller.getGame().getPlayers().get(1).getEmporiums().poll();
		
		//Create and execute a primary action for the second player the end the turn
		
		//Select a city without bonus
		rqst = new BuildEmporiumByKingHelpRqst(token1, city, colors);
		controller.update(rqst);
		controller.update(new EndTurnRqst(token1));
		
		//Market sell phase only for finished and return to the players that has built his last emporium
		controller.update(new MarketSellRqst(token0, new HashMap<>(), new HashMap<>(), new HashMap<>()));
		controller.update(new MarketSellRqst(token1, new HashMap<>(), new HashMap<>(), new HashMap<>()));
		
		//Wait 60 seconds to finish the market buy phase
		try {
			Thread.sleep(65 * 1000);
		} catch (InterruptedException e) {
		}
		
		//Now the game is finished
		assertFalse(controller.isGameStarted());
	}
	
	@Test
	public void testGetCurrentMapWhenTheGameIsJustStartedShouldReturnTheDefaultMap() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		assertEquals("src/main/resources/map/default.txt", controller.getCurrentMap());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetPlayerByTokenWhenTheTokenIsNullShouldThrowAIllegalArgumentException() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		controller.getPlayerByToken(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetPlayerByTokenWhenTheTokenIsInvalidShouldThrowAIllegalArgumentException() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		controller.getPlayerByToken(token2);
	}
	
	@Test
	public void testGetPlayerByTokenShouldReturnThePlayerWithTheSameIdOfTheTokenPlayerNumber() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		Player player = controller.getPlayerByToken(token0);
		assertEquals(player.getId(), token0.getPlayerNumber());
	}
	
	@Test(expected = NullPointerException.class)
	public void testSetTheCurrentWhenPlayerIsNullShouldThrowANullPointerException() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		controller.setCurrentPlayer(null);
	}
	
	@Test
	public void testSetTheCurrentShouldSetThePlayerPassedAsParam() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		assertEquals(token0, controller.getCurrentPlayer());
		controller.setCurrentPlayer(token1);
		assertEquals(token1, controller.getCurrentPlayer());
	}
	
	@Test
	public void testGetMarketBeforeInitGameShouldReturnNull() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		assertNull(controller.getMarket());
	}
	
	@Test
	public void testGetMarketAfteInitGameShouldReturnTheMarketOfTheGame() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		assertThat(controller.getMarket(), is(instanceOf(Market.class)));
	}
	
	@Test
	public void testGetGameBeforeInitGameShouldReturnNull() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		assertNull(controller.getGame());
	}
	
	@Test
	public void testGetGameAfterInitGameShouldReturnTheGame() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		assertThat(controller.getGame(), is(instanceOf(Game.class)));
	}
	
	@Test
	public void testGetTopicShouldReturnTheTopicOfThisGameThatIsAStringWithTheWordNameAndANumber() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		assertTrue(controller.getTopic().matches("^GAME\\d+$"));
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetPlayerTopicIfTheTokenIsNullShouldThrowANullPointerException() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		controller.getPlayerTopic(null);
	}
	
	@Test
	public void testGetPlayerTopicShouldReturnAStringWithTheTopicOfTheGameThenTheWordPlayerAndThenTheUUIDOfTheTokenPassedAsParam() {
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		assertTrue(controller.getPlayerTopic(token0).matches(controller.getTopic() + "PLAYER" + token0.getUuid()));
	}
	
	@Test
	public void testGetBrokerShouldReturnTheBrokerPassedInTheConstructor() {
		assertEquals(broker, controller.getBroker());
	}
	
	@Test
	public void testGetActionVisitorShouldReturnTheActionVisitor() {
		assertThat(controller.getActionVisitor(), is(instanceOf(ActionVisitor.class)));
	}
	
	@Test
	public void testActionTimerWhenThePlayerIsTheFirstPlayerShouldRemoveThePlayerAndPassToTheNextPlayer() {
		controller = new GameController(broker, 5);
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.addPlayer(token2, subscriber);
		controller.initGame();
		
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
		}
		
		assertFalse(controller.getPlayers().contains(token0));
		assertEquals(token1, controller.getCurrentPlayer());
	}
	
	@Test
	public void testActionTimerWhenThePlayerAreTwoAndTheTimerTaskRunShouldRemoveThePlayerForTheGameAndFinishedTheGame() {
		controller = new GameController(broker, 5);
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.initGame();
		
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
		}
		
		assertFalse(controller.getPlayers().contains(token0));
		assertFalse(controller.isGameStarted());
	}
	
	@Test
	public void testActionTimerRunWhenTheLastPlayerIsTheCurrentPlayerShouldRemoveThePlayer() {
		controller = new GameController(broker, 5);
		controller.addPlayer(token0, subscriber);
		controller.addPlayer(token1, subscriber);
		controller.addPlayer(token2, subscriber);
		controller.initGame();
		
		controller.timerTask.cancel();
		controller.changeCurrentPlayer();
		controller.actionTimer();
		controller.timerTask.cancel();
		controller.changeCurrentPlayer();
		controller.actionTimer();
		
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
		}
		
		assertFalse(controller.getPlayers().contains(token2));
	}

}
