package it.polimi.ingsw.cg32.message.response;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.market.MarketResponse;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.market.AssistantBoundle;
import it.polimi.ingsw.cg32.model.market.PermitCardBoundle;
import it.polimi.ingsw.cg32.model.market.PoliticCardBoundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.view.client.cli.CliMessageVisitor;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestMarketResponse {
	
	private Map<Token, ShoppingCart> shoppingCart;
	private Token token;
	private CliMessageVisitor visitor; 
	
	private List<PermitCard> permitCardsFromCoast = new ArrayList<>();
	private List<PermitCard> permitCardsFromHill = new ArrayList<>();
	private List<PermitCard> permitCardsFromMountain = new ArrayList<>();
	
	private List<PoliticCard> firstpoliticCards = new ArrayList<>();
	private List<PoliticCard> secondpoliticCards = new ArrayList<>();
	private List<Assistant> assistants = new ArrayList<>();
	private Game game;
	
	private PoliticCardPrototype prototype;
	
	private List<PoliticCardBoundle> politicCardBoundles = new ArrayList<>();
	private List<AssistantBoundle> assistantBoundles = new ArrayList<>();
	private List<PermitCardBoundle> permitCardBoundles = new ArrayList<>();
	
	private ShoppingCart cart;
	
	@Before
	public void initTestCase() {
		game = new Game("src/main/resources/map/default.txt", 4);
		token = new Token();
		visitor = new CliMessageVisitor();
		
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
		 
		shoppingCart = new HashMap<>();
		shoppingCart.put(token, cart);
		game.setStore(shoppingCart);
	}

	
	//Construct all marketResponse using a 0 id for the player
	@Test 
	public void testConstructorShouldInitiazieStateToNull(){
		MarketResponse response = new MarketResponse(game, "test", 0);
		Assert.assertEquals(null, response.getState());
	}
	
	@Test 
	public void testConstructorShouldInitiazieListOfBonusToAnEmptyList(){
		MarketResponse response = new MarketResponse(game, "test", 0);
		assertEquals(0, response.getBonus().size());
	}
	
	@Test (expected = UnsupportedOperationException.class)
	public void testConstructorShouldInitiazieListOfBonusToAnImmodificableList(){
		MarketResponse response = new MarketResponse(game, "test", 0);
		response.getBonus().add(new SingleBonus("CITYBONUS"));
	}
	
	
	@Test 
	public void testGetShoppingCartShouldReturnShoppingCartPassAsParamWhenConstructed(){
		MarketResponse response = new MarketResponse(game, "test", 0);
		assertEquals(shoppingCart, response.getGame().getStore());
	}
	
	@Test
	public void testDisplayeShouldSetTheGameItCarriesToTheGameInTheVisitor() {

		MarketResponse response = new MarketResponse(game, "test", 0);
		response.display(visitor);
		Assert.assertEquals(visitor.getGame(), response.getGame());
	}

}
