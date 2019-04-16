package it.polimi.ingsw.cg32.controller.turn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
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
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestBounsState {

	private static Game game;
	private static BuildEmporiumByKingHelp buildEmporiumByKingHelp;
	private static BuildEmporiumByPermitCard builEmporiumByPermitCard;
	private static BuyPermitCard buyPermitCard;
	private static ElectCouncillor electCouncillor;
	private static PerformAnotherPrimaryAction performAnotherPrimaryAction;
	private static HireAssistant hireAssistant;
	private static UseAssistantToElectCouncillor useAssistantToElectCouncillor;
	private static ChangeUsablePermitCard changeUsablePermitCard;

	private static MarketBuy marketBuy;
	private static MarketSell marketSell;
	
	private static AssistantBonus assistantBonus;
	private static CoinsBonus coinsBonus;
	private static VictoryBonus victoryBonus;
	private static NobilityBonus nobilityBonus;
	private static PoliticCardBonus politicCardBonus;
	private static ReusePermitCardBonus reusePermitCardBonus;
	private static PermitCardBonus permitCardBonus;
	private static CityBonus cityBonus;
	private static PrimaryActionBonus primaryActionBonus;
	private static Map<List<String>, Integer> permitCardsInt = new HashMap<>();
	private static Map<List<Integer>, Integer> assistantInt = new HashMap<>();
	private static Map<List<Colore>, Integer> colors = new HashMap<>();
	
	@BeforeClass
	public static void initTest(){
		game = new Game("src/main/resources/map/default.txt", 4);
		
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
		
		BonusState.getInstance().executeAction(null);
	}
	

	@Test (expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsBuildEmporiumByKingHelp() throws IllegalStateMachineException {
		BonusState.getInstance().isActionValid(buildEmporiumByKingHelp);
	}
	@Test (expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsBuildEmporiumByPermitCard() throws IllegalStateMachineException {
		BonusState.getInstance().isActionValid(builEmporiumByPermitCard);
	}
	@Test (expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsBuyPermitCard() throws IllegalStateMachineException {
		BonusState.getInstance().isActionValid(buyPermitCard);
	}
	@Test (expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsElectCouncillor() throws IllegalStateMachineException {
		BonusState.getInstance().isActionValid(electCouncillor);
	}
	@Test (expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsPerformAnotherPrimaryAction() throws IllegalStateMachineException {
		BonusState.getInstance().isActionValid(performAnotherPrimaryAction);
	}
	@Test (expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsChangeUsablePermitCard() throws IllegalStateMachineException {
		BonusState.getInstance().isActionValid(changeUsablePermitCard);
	}
	@Test (expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsHireAssistant() throws IllegalStateMachineException {
		BonusState.getInstance().isActionValid(hireAssistant);
	}
	@Test (expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsUseAssistantToElectCouncillor() throws IllegalStateMachineException {
		BonusState.getInstance().isActionValid(useAssistantToElectCouncillor);
	}
	@Test
	public void testIsActionValidReturnTrueIfArgumetIsCoinsBonus() throws IllegalStateMachineException {
		Assert.assertTrue(BonusState.getInstance().isActionValid(coinsBonus));
	}
	@Test
	public void testIsActionValidReturnTrueIfArgumetIsVictoryBonus() throws IllegalStateMachineException {
		Assert.assertTrue(BonusState.getInstance().isActionValid(victoryBonus));
	}
	@Test
	public void testIsActionValidReturnTrueIfArgumetIsAssistantBonus() throws IllegalStateMachineException {
		Assert.assertTrue(BonusState.getInstance().isActionValid(assistantBonus));
	}
	@Test
	public void testIsActionValidReturnTrueIfArgumetIsPoliticCardBonus() throws IllegalStateMachineException {
		Assert.assertTrue(BonusState.getInstance().isActionValid(politicCardBonus));
	}
	@Test
	public void testIsActionValidReturnTrueIfArgumetIsNobilityBonus() throws IllegalStateMachineException {
		Assert.assertTrue(BonusState.getInstance().isActionValid(nobilityBonus));
	}
	@Test
	public void testIsActionValidReturnTrueIfArgumetIsCityBonus() throws IllegalStateMachineException {
		Assert.assertTrue(BonusState.getInstance().isActionValid(cityBonus));
	}
	@Test
	public void testIsActionValidReturnTrueIfArgumetIsPermitCardBonus() throws IllegalStateMachineException {
		Assert.assertTrue(BonusState.getInstance().isActionValid(permitCardBonus));
	}
	@Test
	public void testIsActionValidReturnTrueIfArgumetIsReusePermitCardBonus() throws IllegalStateMachineException {
		Assert.assertTrue(BonusState.getInstance().isActionValid(reusePermitCardBonus));
	}
	@Test
	public void testIsActionValidReturnTrueIfArgumetIsPrimaryActionBonus() throws IllegalStateMachineException {
		Assert.assertTrue(BonusState.getInstance().isActionValid(primaryActionBonus));
	}
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsMarketSell() throws IllegalStateMachineException {
		BonusState.getInstance().isActionValid(marketSell);
	}	
	@Test(expected = IllegalStateMachineException.class)
	public void testIsActionValidShoulThrowsIllegalStateMachineExceptionIfArgumetIsMarketBuy() throws IllegalStateMachineException {
		BonusState.getInstance().isActionValid(marketBuy);
	}
}