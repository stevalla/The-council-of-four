package it.polimi.ingsw.cg32.controller.turn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
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
import it.polimi.ingsw.cg32.controller.turn.state.OffState;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.market.AssistantBoundle;
import it.polimi.ingsw.cg32.model.market.PermitCardBoundle;
import it.polimi.ingsw.cg32.model.market.PoliticCardBoundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestOffState {

	private Game game;
	private BuildEmporiumByKingHelp buildEmporiumByKingHelp;
	private BuildEmporiumByPermitCard builEmporiumByPermitCard;
	private BuyPermitCard buyPermitCard;
	private ElectCouncillor electCouncillor;
	
	private PerformAnotherPrimaryAction performAnotherPrimaryAction;
	private HireAssistant hireAssistant;
	private UseAssistantToElectCouncillor useAssistantToElectCouncillor;
	private ChangeUsablePermitCard changeUsablePermitCard;
	
	private MarketBuy marketBuy;
	private MarketSell marketSell;
	
	private AssistantBonus assistantBonus;
	private CoinsBonus coinsBonus;
	private VictoryBonus victoryBonus;
	private NobilityBonus nobilityBonus;
	private PoliticCardBonus politicCardBonus;
	private ReusePermitCardBonus reusePermitCardBonus;
	private PermitCardBonus permitCardBonus;
	private CityBonus cityBonus;
	private PrimaryActionBonus primaryActionBonus;
	
	private ShoppingCart cart;
	private Map<Token, ShoppingCart> shoppingCarts = new HashMap<>();
	private Map<List<String>, Integer> permitCardsInt = new HashMap<>();
	private Map<List<Integer>, Integer> assistantInt = new HashMap<>();
	private Map<List<Colore>, Integer> colors = new HashMap<>();
	
	@Before
	public void initTest(){
	game = new Game("src/main/resources/map/default.txt", 4);
		
		//init ShopingCart. (need it for market's action).
		List<PoliticCardBoundle> politicCardBoundles = new ArrayList<>();
		List<AssistantBoundle> assistantsBoundle = new ArrayList<>();
		List<PermitCardBoundle> permitCardBoundles = new ArrayList<>();
		cart = new ShoppingCart(politicCardBoundles, assistantsBoundle, permitCardBoundles);
		shoppingCarts.put(new Token(), cart);
			
		buildEmporiumByKingHelp = new BuildEmporiumByKingHelp(game.getPlayers().get(0), game, game.getBoard().getCities().get(3), 0, 0, game.getPlayers().get(0).getPoliticCards());
		builEmporiumByPermitCard = new BuildEmporiumByPermitCard(game.getPlayers().get(0), game, game.getBoard().getCities().get(3), 0, game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0));
		electCouncillor = new ElectCouncillor(game.getPlayers().get(0), game, game.getBoard().getRegions(RegionType.COAST), new Councillor(Colore.ORANGE));
		buyPermitCard = new BuyPermitCard(game.getPlayers().get(0), game, game.getBoard().getRegions(RegionType.COAST), game.getPlayers().get(0).getPoliticCards(), game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0), 0);
		
		performAnotherPrimaryAction = new PerformAnotherPrimaryAction(game.getPlayers().get(0), game);
		changeUsablePermitCard = new ChangeUsablePermitCard(game.getPlayers().get(0), game, game.getBoard().getRegions(RegionType.COAST));
		hireAssistant = new HireAssistant(game.getPlayers().get(0), game);
		useAssistantToElectCouncillor = new UseAssistantToElectCouncillor(game.getPlayers().get(0), game, game.getBoard().getRegions(RegionType.COAST), new Councillor(Colore.ORANGE));
	
		marketBuy = new MarketBuy(game.getPlayers().get(0), game, game.getPlayers().get(1), 0);
		marketSell =new MarketSell(game.getPlayers().get(0), game, new Token(), permitCardsInt, assistantInt, colors);
	
		coinsBonus = new CoinsBonus(game.getPlayers().get(0), game, new QuantityBonus("COINSBONUS", 2));
		victoryBonus = new VictoryBonus(game.getPlayers().get(0), game, new QuantityBonus("VICTORYBONUS", 2));
		assistantBonus = new AssistantBonus(game.getPlayers().get(0), game, new QuantityBonus("ASSISTANTBONUS", 2));
		politicCardBonus = new PoliticCardBonus(game.getPlayers().get(0), game, new QuantityBonus("POLITICCARDBONUS", 2), new PoliticCardPrototype());
		nobilityBonus = new NobilityBonus(game.getPlayers().get(0), game, new SingleBonus("NOBILITYBONUS"));
		cityBonus = new CityBonus(game.getPlayers().get(0), game, game.getBoard().getCities().get(0), new SingleBonus("CITYBONUS"));
		permitCardBonus = new PermitCardBonus(game.getPlayers().get(0), game, game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0), game.getBoard().getRegions(RegionType.COAST), new SingleBonus("PERMITCARDBONUS"));
		reusePermitCardBonus = new ReusePermitCardBonus(game.getPlayers().get(0), game, game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0), new SingleBonus("REUSEPERMITCARDBONUS"));
		primaryActionBonus = new PrimaryActionBonus(game.getPlayers().get(0), game, 
				new BuyPermitCard(game.getPlayers().get(0), game, game.getBoard().getRegions(RegionType.COAST),
					game.getPlayers().get(0).getPoliticCards(), 
						game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0), 0), new SingleBonus("PRIMARYACTIONBONUS"));
	
	}
	
	@Test (expected = NullPointerException.class)
	public void createActionWithNullArgumentShouldThrowsNullPointereException(){
		
		OffState.getInstance().executeAction(null);
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void createActionWithAnActionArgumentShouldThrowAnIllegalStateMachineException() throws IllegalStateMachineException{
		OffState.getInstance().isActionValid(electCouncillor);
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowAnIllegalStateMachineExceptionIfArgumetIsBuildEmporiumByKingHelp() throws IllegalStateMachineException {
		Assert.assertFalse(OffState.getInstance().isActionValid(buildEmporiumByKingHelp));
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowAnIllegalStateMachineExceptionIfArgumetIsBuildEmporiumByPermitCard() throws IllegalStateMachineException {
		Assert.assertTrue(OffState.getInstance().isActionValid(builEmporiumByPermitCard));
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowAnIllegalStateMachineExceptionIfArgumetIsBuyPermitCard() throws IllegalStateMachineException {
		Assert.assertTrue(OffState.getInstance().isActionValid(buyPermitCard));
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowAnIllegalStateMachineExceptionIfArgumetIsElectCouncillor() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(electCouncillor);
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsPerformAnotherPrimaryAction() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(performAnotherPrimaryAction);
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsChangeUsablePermitCard() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(changeUsablePermitCard);
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsHireAssistant() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(hireAssistant);
	}

	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsUseAssistantToElectCouncillor() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(useAssistantToElectCouncillor);
	}

	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsCoinsBonus() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(coinsBonus);
	}
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsVictoryBonus() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(victoryBonus);
	}
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsAssistantBonus() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(assistantBonus);
	}
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsPoliticCardBonus() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(politicCardBonus);
	}
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsNobilityBonus() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(nobilityBonus);
	}
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsCityBonus() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(cityBonus);
	}
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsPermitCardBonus() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(permitCardBonus);
	}
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsReusePermitCardBonus() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(reusePermitCardBonus);
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsPrimaryActionBonus() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(primaryActionBonus);
	}
	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsMarketSell() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(marketSell);
	}	
	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsMarketBuy() throws IllegalStateMachineException {
		OffState.getInstance().isActionValid(marketBuy);
	}
}
