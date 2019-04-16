package it.polimi.ingsw.cg32.message.request.market;

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

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.market.MarketSell;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Board;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestMarketSellRqst {

	MarketSellRqst request;
	private Map<Token, ShoppingCart> shopingCart;
	
	Map<List<Colore>, Integer> colors;
	Map<List<Integer>, Integer> assistantsInt;
	Map<List<String>, Integer> permitCards;
	
	List<Colore> colorsList = new ArrayList<>();
	List<Integer> assistantList = new ArrayList<>();
	List<String> permitCardsList = new ArrayList<>();
	
	private Game game;
	private Token token;
	private ActionVisitor visitor;
	private Board board;
	private Player player;

	@Before
	public void initTestCase() {
		game = new Game("src/main/resources/map/default.txt", 4);
		board = game.getBoard();
		visitor = new ActionVisitor();
		player = game.getPlayers().get(0);
		token = new Token();
		
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

		game.setStore(shopingCart);
		request = new MarketSellRqst(token, colors, assistantsInt, permitCards);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructorWithTokenNullShouldThrowANullPointerException() {
		request = new MarketSellRqst(null, colors, assistantsInt, permitCards);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructorWithTokenNullShouldGoCorrectly() throws IllegalActionException {
		request = new MarketSellRqst(null, null, null, null);
	}
	
	@Test
	public void testConstructorShouldInitializeMarketRqstAsSpecified(){
		assertEquals(assistantsInt, request.getAssistantsInt());
		assertEquals(colors, request.getColors());
		assertEquals(token, request.getToken());
		assertEquals(permitCards, request.getPermitCardsInt());
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(game, null, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenGameIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(null, player, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenVisitorIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(game, player, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenListOfColorsInMapColorsIsNullShouldReturnANullPointerException() throws IllegalActionException {
		request = new MarketSellRqst(token, null, assistantsInt, permitCards);
		request.accept(game, player, visitor);
		
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenListOfColorsInMapColorsNotCOrrespondingToThePlayersPoliticCardsShouldReturnAnIllegalActionException() throws IllegalActionException {
		player.getPoliticCards().clear();
		request = new MarketSellRqst(token, colors, assistantsInt, permitCards);
		request.accept(game, player, visitor);
		
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenThePlayerSelectMoreThanHisNumberOfAssistantShouldThrowAnIllegalActionException() throws IllegalActionException {
		player.getAssistants().clear();
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWHenThePlayerSelectMoreThanHisNumberOfPermitCardsShouldThrowAnIllegalActionException() throws IllegalActionException {
		player.getPermitCards().clear();
		request.accept(game, player, visitor);
	}
	
	@Test
	public void testAcceptWhenArgumentsAreValidShouldCreateAMarketSell() throws IllegalActionException {
		Action action =request.accept(game, player, visitor);
		assertThat(action, is(instanceOf(MarketSell.class)));
	}
}
