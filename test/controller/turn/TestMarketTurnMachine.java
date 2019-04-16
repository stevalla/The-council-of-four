package it.polimi.ingsw.cg32.controller.turn;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.market.MarketBuy;
import it.polimi.ingsw.cg32.controller.action.market.MarketSell;
import it.polimi.ingsw.cg32.controller.turn.state.BuyState;
import it.polimi.ingsw.cg32.controller.turn.state.SellState;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Board;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.market.AssistantBoundle;
import it.polimi.ingsw.cg32.model.market.PermitCardBoundle;
import it.polimi.ingsw.cg32.model.market.PoliticCardBoundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestMarketTurnMachine {

	MarketTurnMachine machine;
	private Map<Token, ShoppingCart> shopingCart;
	
	private List<PermitCard> permitCardsFromCoast = new ArrayList<>();
	private List<PermitCard> permitCardsFromHill = new ArrayList<>();
	private List<PermitCard> permitCardsFromMountain = new ArrayList<>();
	
	private List<PoliticCard> firstpoliticCards = new ArrayList<>();
	private List<PoliticCard> secondpoliticCards = new ArrayList<>();
	private List<Assistant> assistants = new ArrayList<>();
	
	Map<List<Colore>, Integer> colors;
	Map<List<Integer>, Integer> assistantsInt;
	Map<List<String>, Integer> permitCards;
	
	List<Colore> colorsList = new ArrayList<>();
	List<Integer> assistantList = new ArrayList<>();
	List<String> permitCardsList = new ArrayList<>();
	
	private List<PoliticCardBoundle> politicCardBoundles = new ArrayList<>();
	private List<AssistantBoundle> assistantBoundles = new ArrayList<>();
	private List<PermitCardBoundle> permitCardBoundles = new ArrayList<>();
	
	private Game game;
	private Token token;
	private Board board;
	private Player player;
	private Token token2;
	private Player player2;
	private int boundleId;

	@Before
	public void initTestCase() {
		game = new Game("src/main/resources/map/default.txt", 4);
		board = game.getBoard();
		player = game.getPlayers().get(0);
		player2 = game.getPlayers().get(1);
		token = new Token();
		token.setPlayerNumber(0);
		token2 = new Token();
		token2.setPlayerNumber(1);
		colors = new HashMap<>();
		player.getPoliticCards().stream().forEach(p -> colorsList.add(p.getCardColor()));
		colors.put(colorsList, 5);
		
		player.getPermitCards().add(board.getRegions(RegionType.COAST).getDeckOfPermitCard().getDeck().poll());
		
		assistantsInt = new HashMap<>();
		assistantList.add(1);
		assistantsInt.put(assistantList, 10);
		
		permitCards = new HashMap<>();
		permitCardsList.add(player.getPermitCards().get(0).getId());
		permitCards.put(permitCardsList, 3);
		loadShoppingCarts();
		game.setStore(shopingCart);
	}
	
	private void loadShoppingCarts() {
		
		//init PermitCardBoundle
		permitCardsFromCoast.add(game.getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getUsableCard().get(0));
		permitCardsFromCoast.add(game.getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getUsableCard().get(1));
		permitCardsFromHill.add(game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0));
		permitCardsFromHill.add(game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(1));
		permitCardsFromMountain.add(game.getBoard().getRegions(RegionType.MOUNTAIN).getDeckOfPermitCard().getUsableCard().get(0));
		permitCardsFromMountain.add(game.getBoard().getRegions(RegionType.MOUNTAIN).getDeckOfPermitCard().getUsableCard().get(1));
		
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
		
		ShoppingCart cart = new ShoppingCart(politicCardBoundles, assistantBoundles, permitCardBoundles);
		
		shopingCart = new HashMap<>();
		cart = new ShoppingCart(new ArrayList<>(), assistantBoundles, permitCardBoundles);
		shopingCart.put(token2, cart);
		shopingCart.put(new Token(), cart);
		
		boundleId = shopingCart.get(token2).getListOfBoundles().get(0).getBoundleId();
		
		game.setStore(shopingCart);
	}
	
	@Test
	public void testConstructAMarketTurnMachinShouldSetTheSellStateAtTheBegin() {
		machine = new MarketTurnMachine();
		assertEquals(SellState.getInstance(), machine.getCurrentState());
	}
	
	@Test
	public void testExecuteActionWhenStateIsSellStateAndActionIsMarketSellShouldReturnAResponseMessage() throws IllegalStateMachineException {
		machine = new MarketTurnMachine();
		MarketSell action = new MarketSell(player, game, token, permitCards, assistantsInt, colors);
		ResponseMessage response = machine.executeAction(action);
		assertThat(response, is(instanceOf(ResponseMessage.class)));
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testExecuteActionWhenStateIsNotSellStateAndActionIsMarketSellShouldThrowAnIllegalStateMachineException() throws IllegalStateMachineException {
		machine = new MarketTurnMachine();
		machine.setCurrentState(BuyState.getInstance());
		MarketSell action = new MarketSell(player, game, token, permitCards, assistantsInt, colors);
		ResponseMessage response = machine.executeAction(action);
		assertThat(response, is(instanceOf(ResponseMessage.class)));
	}
	
	@Test
	public void testExecuteActionWhenStateIsBuyStateAndActionIsMarketBuyShouldReturnAResponseMessage() throws IllegalStateMachineException {
		machine = new MarketTurnMachine();
		machine.setCurrentState(BuyState.getInstance());
		MarketBuy action = new MarketBuy(player, game, player2, boundleId);
		ResponseMessage response = machine.executeAction(action);
		assertThat(response, is(instanceOf(ResponseMessage.class)));
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testExecuteActionWhenStateIsNotBuyStateAndActionIsMarketBuyShouldThrowAnIllegalStateMachineException() throws IllegalStateMachineException {
		machine = new MarketTurnMachine();
		MarketBuy action = new MarketBuy(player, game, player2, boundleId);
		machine.executeAction(action);
	}
}
