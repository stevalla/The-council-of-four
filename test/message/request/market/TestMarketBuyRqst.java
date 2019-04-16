package it.polimi.ingsw.cg32.message.request.market;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.market.MarketBuy;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.market.AssistantBoundle;
import it.polimi.ingsw.cg32.model.market.PermitCardBoundle;
import it.polimi.ingsw.cg32.model.market.PoliticCardBoundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestMarketBuyRqst {

	MarketBuyRqst request;
	private Map<Token, ShoppingCart> shoppingCart;
	
	private List<PermitCard> permitCardsFromCoast = new ArrayList<>();
	private List<PermitCard> permitCardsFromHill = new ArrayList<>();
	private List<PermitCard> permitCardsFromMountain = new ArrayList<>();
	
	private List<PoliticCard> firstpoliticCards = new ArrayList<>();
	private List<PoliticCard> secondpoliticCards = new ArrayList<>();
	private List<Assistant> assistants = new ArrayList<>();
	private Game game;
	private Token token;
	private PoliticCardPrototype prototype;
	private ActionVisitor visitor;
	private Player player;
	private int boundleId;
	private Token tokenTwo;
	private Player playerTwo;
	
	private List<PoliticCardBoundle> politicCardBoundles = new ArrayList<>();
	private List<AssistantBoundle> assistantBoundles = new ArrayList<>();
	private List<PermitCardBoundle> permitCardBoundles = new ArrayList<>();
	
	private ShoppingCart cart;

	@Before
	public void initTestCase() {
		game = new Game("src/main/resources/map/default.txt", 4);
		visitor = new ActionVisitor();
		playerTwo = game.getPlayers().get(3);
		player = game.getPlayers().get(0);
		token = new Token();
		token.setPlayerNumber(0);
		
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
		
		cart = new ShoppingCart(politicCardBoundles, assistantBoundles, permitCardBoundles);
		tokenTwo = new Token();
		tokenTwo.setPlayerNumber(3);
		
		shoppingCart = new HashMap<>();
		shoppingCart.put(tokenTwo, cart);
		cart = new ShoppingCart(new ArrayList<>(), assistantBoundles, permitCardBoundles);
		shoppingCart.put(new Token(), cart);
		shoppingCart.put(new Token(), cart);
		
		game.setStore(shoppingCart);

		boundleId = shoppingCart.get(tokenTwo).getListOfBoundles().get(0).getBoundleId();
	}
	
	@Test
	public void testConstructorShouldInitializeMarketRqstAsSpecified(){
		MarketBuyRqst rqst = new MarketBuyRqst(token, 7);
		assertEquals(7, rqst.getBoundleId());
		assertEquals(token, rqst.getToken());
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenTokenIsNullShouldThrowANullPointerException() {
		new MarketBuyRqst(null, 4);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructWhenTheBoundIdIsMinorThanZeroShouldThrowAnIllegalArgumentException() {
		new MarketBuyRqst(token, -3);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		request = new MarketBuyRqst(token, boundleId);
		request.accept(game, null, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenBoardIsNullShouldThrowANullPointerException() throws IllegalActionException {
		request = new MarketBuyRqst(token, boundleId);
		request.accept(null, player, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenVisitorIsNullShouldThrowANullPointerException() throws IllegalActionException {
		request = new MarketBuyRqst(token, boundleId);
		request.accept(game, player, null);
	}
	
	@Test
	public void testAcceptShouldCreateSetThePlayerThatSellToTheRelativeAttributeOfTheClass() throws IllegalActionException {
		request = new MarketBuyRqst(token, boundleId);
		assertNull(request.getPlayerThatSell());
		request.accept(game, player, visitor);
		assertNotNull(request.getPlayerThatSell());
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenBoundedIsNotPresentInTheShoppingCartsShouldThrowAnIllegalActionException() throws IllegalActionException {
		request = new MarketBuyRqst(token, 1000);
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenPlayerHasNoEnoughMoneyToBuyTheBoundleShouldThrowAnIllegalActionException() throws IllegalActionException {
		player.getCoinsTrack().setCoinsNumber(-player.getCoinsTrack().getCurrentCoins());
		request = new MarketBuyRqst(token, boundleId);
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenThePlayerBuyHisOwnBoundleSHouldThrowAnIllegalActionException() throws IllegalActionException {
		request = new MarketBuyRqst(tokenTwo, boundleId);
		request.accept(game, playerTwo, visitor);
	}
	
	@Test
	public void testAcceptWhenArgumentsAreValidShouldCreateAMarketBuy() throws IllegalActionException {
		request = new MarketBuyRqst(token, boundleId);
		Action action =request.accept(game, player, visitor);
		assertThat(action, is(instanceOf(MarketBuy.class)));
	}
	
}
