package it.polimi.ingsw.cg32.controller.action.market;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.market.AssistantBoundle;
import it.polimi.ingsw.cg32.model.market.PermitCardBoundle;
import it.polimi.ingsw.cg32.model.market.PoliticCardBoundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestMarketBuy {

	MarketBuy action;
	private Map<Token, ShoppingCart> shoppingCarts;
	private ShoppingCart cart;
	
	private List<PermitCard> permitCardsFromCoast = new ArrayList<>();
	private List<PermitCard> permitCardsFromHill = new ArrayList<>();
	private List<PermitCard> permitCardsFromMountain = new ArrayList<>();
	
	private List<PoliticCard> firstpoliticCards = new ArrayList<>();
	private List<PoliticCard> secondpoliticCards = new ArrayList<>();
	private List<Assistant> assistants = new ArrayList<>();
	
	private List<PoliticCardBoundle> politicCardBoundles = new ArrayList<>();
	private List<AssistantBoundle> assistantBoundles = new ArrayList<>();
	private List<PermitCardBoundle> permitCardBoundles = new ArrayList<>();
	
	Map<List<Colore>, Integer> colors;
	Map<List<Integer>, Integer> assistantsInt;
	Map<List<String>, Integer> permitCards;
	
	List<Colore> colorsList = new ArrayList<>();
	List<Integer> assistantList = new ArrayList<>();
	List<String> permitCardsList = new ArrayList<>();
	private int boundleId;
	
	private Game game;
	private Token token;
	private Player playerThatSell;
	private Player playerThatBuy;

	@Before
	public void initTestCase() {
		game = new Game("src/main/resources/map/default.txt", 4);
		playerThatSell = game.getPlayers().get(0);
		playerThatBuy = game.getPlayers().get(1);
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
		PoliticCardPrototype prototype = new PoliticCardPrototype();

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

		shoppingCarts = new HashMap<>();
		shoppingCarts.put(token, cart);
		cart = new ShoppingCart(new ArrayList<>(), assistantBoundles, permitCardBoundles);
		shoppingCarts.put(new Token(), cart);
		shoppingCarts.put(new Token(), cart);

		game.setStore(shoppingCarts);
		boundleId = shoppingCarts.get(token).getListOfBoundles().get(0).getBoundleId();

		action = new MarketBuy(playerThatBuy, game, playerThatSell, boundleId);
	}
	
	@Test
	public void testExecuteShouldRemoveTheBoundleFromTheShoppingCartsGiveTheMoneyToTheSellerAndRemoveCoinsFromTheBuyMan() {
		int coinsBeforeSeller = playerThatSell.getCoinsTrack().getCurrentCoins();
		int coinsBeforeBuyer = playerThatBuy.getCoinsTrack().getCurrentCoins();
		int costOfBoundle = shoppingCarts.get(token).findBoundleById(boundleId).getPrice();
		action.execute();
		assertTrue(playerThatSell.getCoinsTrack().getCurrentCoins() == coinsBeforeSeller + costOfBoundle);
		assertTrue(playerThatBuy.getCoinsTrack().getCurrentCoins() == coinsBeforeBuyer - costOfBoundle);
		assertNull(shoppingCarts.get(token).findBoundleById(boundleId));
	}
	
	@Test
	public void executeShouldGiveToTheBuyerTheObjectsInTheBoundle() {
		action.execute();
		assertTrue(CollectionUtils.isSubCollection(firstpoliticCards, playerThatBuy.getPoliticCards()));
	}
}
