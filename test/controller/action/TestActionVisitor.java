package it.polimi.ingsw.cg32.controller.action;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.bonus.AssistantBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.CityBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.CoinsBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.NobilityBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.PermitCardBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.PoliticCardBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.PrimaryActionBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.ReusePermitCardBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.VictoryBonus;
import it.polimi.ingsw.cg32.controller.action.market.MarketBuy;
import it.polimi.ingsw.cg32.controller.action.market.MarketSell;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByKingHelp;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuyPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.ElectCouncillor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.ChangeUsablePermitCard;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.HireAssistant;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.PerformAnotherPrimaryAction;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.UseAssistantToElectCouncillor;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByKingHelpRqst;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.BuyPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.ChangeUsablePermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.ElectCouncillorRqst;
import it.polimi.ingsw.cg32.message.request.action.HireAssistantRqst;
import it.polimi.ingsw.cg32.message.request.action.PerformAnotherPrimaryActionRqst;
import it.polimi.ingsw.cg32.message.request.action.UseAssistantToElectCouncillorRqst;
import it.polimi.ingsw.cg32.message.request.bonus.AssistantBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.CityBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.CoinsBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.NobilityBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.PermitCardBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.PoliticCardBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.PrimaryActionBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.ReusePermitCardBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.VictoryBonusRqst;
import it.polimi.ingsw.cg32.message.request.market.MarketBuyRqst;
import it.polimi.ingsw.cg32.message.request.market.MarketSellRqst;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.market.AssistantBoundle;
import it.polimi.ingsw.cg32.model.market.PermitCardBoundle;
import it.polimi.ingsw.cg32.model.market.PoliticCardBoundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestActionVisitor {

	ActionVisitor visitor;
	private Game game;
	private Map<Token, ShoppingCart> cart;
	private Player player;
	private Token token;
	private List<Colore> colors;
	private City city;
	private Token tokenTwo;
	
	@Before
	public void initParams() {
		game = new Game("src/main/resources/map/default.txt", 3);
		player = game.getPlayers().get(0);
		token = new Token();
		token.setPlayerNumber(0);
		player.getPermitCards().addAll(game.getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getUsableCard());
		loadShoppingCart();
		visitor = new ActionVisitor();
		colors = new ArrayList<>();
		colors.add(Colore.BLACK);
		city = game.getBoard().getCities().get(0);
	}
	
	private void loadShoppingCart() {
		List<PoliticCard> politicCards = player.getPoliticCards();
		List<Assistant> assistants = new ArrayList<>(player.getAssistants());
		List<PermitCard> permitCards = player.getPermitCards();
		
		List<PoliticCardBoundle> politicCardsBoundle = new ArrayList<>();
		politicCardsBoundle.add(new PoliticCardBoundle(10, politicCards));
		
		List<AssistantBoundle> assistantsBoundle = new ArrayList<>();
		assistantsBoundle.add(new AssistantBoundle(3, assistants));
		
		List<PermitCardBoundle> permitCardsBoundle = new ArrayList<>();
		permitCardsBoundle.add(new PermitCardBoundle(20, permitCards));
		
		tokenTwo = new Token();
		tokenTwo.setPlayerNumber(1);
		cart = new HashMap<>();
		cart.put(tokenTwo, new ShoppingCart(politicCardsBoundle, 
				assistantsBoundle, permitCardsBoundle));
	}
	
	@Test
	public void testVisitBuildEmporiumByKingHelpShouldCreateThisAction() {
		
		BuildEmporiumByKingHelpRqst request = new BuildEmporiumByKingHelpRqst(token, city, colors);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(BuildEmporiumByKingHelp.class)));
	}
	
	@Test
	public void testVisitBuildEmporiumByPermitCardShouldCreateThisAction() {
		
		BuildEmporiumByPermitCardRqst request = new BuildEmporiumByPermitCardRqst(token, city, player.getPermitCards().get(0).getId());
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(BuildEmporiumByPermitCard.class)));
	}
	
	@Test
	public void testVisitBuyPermitCardShouldCreateThisAction() {

		BuyPermitCardRqst request = new BuyPermitCardRqst(token, game.getBoard().getRegions(RegionType.COAST),
				player.getPermitCards().get(0).getId(), colors);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(BuyPermitCard.class)));
	}
	
	@Test
	public void testVisitElectCouncillorShouldCreateThisAction() {

		ElectCouncillorRqst request = new ElectCouncillorRqst(token, game.getBoard().getRegions(RegionType.COAST), colors.get(0));
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(ElectCouncillor.class)));
	}
	
	@Test
	public void testVisitChangeUsablePermitCardsShouldCreateThisAction() {

		ChangeUsablePermitCardRqst request = new ChangeUsablePermitCardRqst(token, game.getBoard().getRegions(RegionType.COAST));
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(ChangeUsablePermitCard.class)));
	}
	
	@Test
	public void testVisitHireAssistantsShouldCreateThisAction() {

		HireAssistantRqst request = new HireAssistantRqst(token);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(HireAssistant.class)));
	}
	
	@Test
	public void testVisitPerformAnotherPrimaryActionShouldCreateThisAction() {

		PerformAnotherPrimaryActionRqst request = new PerformAnotherPrimaryActionRqst(token);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(PerformAnotherPrimaryAction.class)));
	}
	
	@Test
	public void testVisitUseAssistantsToElectCouncillorShouldCreateThisAction() {

		UseAssistantToElectCouncillorRqst request = new UseAssistantToElectCouncillorRqst(token, game.getBoard().getKingRegion(), colors.get(0));
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(UseAssistantToElectCouncillor.class)));
	}
	
	@Test
	public void testVisitCoinsBonusShouldCreateThisAction() {
		QuantityBonus bonus = new QuantityBonus("COINSBONUS", 3);
		CoinsBonusRqst request = new CoinsBonusRqst(token, bonus);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(CoinsBonus.class)));
	}
	
	@Test
	public void testVisitAssistantBonusShouldCreateThisAction() {
		QuantityBonus bonus = new QuantityBonus("ASSISTANTBONUS", 3);
		AssistantBonusRqst request = new AssistantBonusRqst(token, bonus);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(AssistantBonus.class)));
	}
	
	@Test
	public void testVisitVictoryBonusShouldCreateThisAction() {
		QuantityBonus bonus = new QuantityBonus("", 3);
		VictoryBonusRqst request = new VictoryBonusRqst(token, bonus);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(VictoryBonus.class)));
	}
	
	@Test
	public void testVisitPoliticCardBonusShouldCreateThisAction() {
		QuantityBonus bonus = new QuantityBonus("POLITICCARDBONUS", 3);
		PoliticCardBonusRqst request = new PoliticCardBonusRqst(token, bonus);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(PoliticCardBonus.class)));
	}
	
	@Test
	public void testVisitNobilityBonusShouldCreateThisAction() {
		SingleBonus bonus = new SingleBonus("NOBILITYBONUS");
		NobilityBonusRqst request = new NobilityBonusRqst(token, bonus);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(NobilityBonus.class)));
	}

	@Test
	public void testVisitPermitCardBonusShouldCreateThisAction() {
		SingleBonus bonus = new SingleBonus("PERMITCARDBONUS");
		PermitCardBonusRqst request = new PermitCardBonusRqst(token, player.getPermitCards().get(0).getId(),
				game.getBoard().getRegions(RegionType.COAST), bonus);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(PermitCardBonus.class)));
	}
	
	@Test
	public void testVisitReusePermitCardBonusShouldCreateThisAction() {
		SingleBonus bonus = new SingleBonus("REUSEPERMITCARDBONUS");
		ReusePermitCardBonusRqst request = new ReusePermitCardBonusRqst(token, player.getPermitCards().get(0).getId(), bonus);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(ReusePermitCardBonus.class)));
	}
	
	@Test
	public void testVisitPrimaryActionBonusShouldCreateThisAction() {
		SingleBonus bonus = new SingleBonus("PRIMARYACTIONBONUS");
		PrimaryActionBonusRqst request = new PrimaryActionBonusRqst(token, 
				new ElectCouncillorRqst(token, game.getBoard().getRegions(RegionType.COAST), colors.get(0)), bonus);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(PrimaryActionBonus.class)));
	}
	
	@Test
	public void testVisitCityBonusShouldCreateThisAction() {
		SingleBonus bonus = new SingleBonus("CITYBONUS");
		CityBonusRqst request = new CityBonusRqst(token, city, bonus);
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(CityBonus.class)));
	}
	
	@Test
	public void testVisitMarketBuyShouldCreateThisAction() throws IllegalActionException {
		MarketBuyRqst request = new MarketBuyRqst(token, cart.get(tokenTwo).getListOfBoundles().get(0).getBoundleId());
		MarketBuyRqst requestCopy = request;
		game.setStore(cart);
		request.accept(game, player, visitor);
		Action action = visitor.visit(requestCopy, player, game);
		assertThat(action, is(instanceOf(MarketBuy.class)));
	}
	
	@Test
	public void testVisitMarketSellShouldCreateThisAction() {
		
		MarketSellRqst request = new MarketSellRqst(token, new HashMap<>(), new HashMap<>(), new HashMap<>());
		Action action = visitor.visit(request, player, game);
		assertThat(action, is(instanceOf(MarketSell.class)));
	}
	
}
