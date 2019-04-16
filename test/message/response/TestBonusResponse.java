package it.polimi.ingsw.cg32.message.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import it.polimi.ingsw.cg32.controller.turn.state.PrimaryActionState;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.client.cli.CliMessageVisitor;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestBonusResponse {

	private List<Bonus> bonuses = new ArrayList<>(); 
	private Game game;
	private CliMessageVisitor visitor; 
	
	@Rule
	public final SystemOutRule system = new SystemOutRule().enableLog().muteForSuccessfulTests();
	
	@Before
	public void initTest(){
		
		game = new Game("src/main/resources/map/default.txt", 4);
		visitor = new CliMessageVisitor();
		
		SingleBonus bonus1 = new SingleBonus("CITYBONUS");
		QuantityBonus bonus2 = new QuantityBonus("ASSISTANTBONUS", 3);
		SingleBonus bonus3 = new SingleBonus("NOBILITYBONUS");
		
		bonuses.add(bonus1);
		bonuses.add(bonus2);
		bonuses.add(bonus3);
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfGameIsNull() {
		
		new BonusResponse(null, PrimaryActionState.getInstance(), bonuses, new SingleBonus("VICTORYBONUS"), "test", 0);
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfListOfBonusesIsNull() {
		
		new BonusResponse(game, PrimaryActionState.getInstance(), null, new SingleBonus("VICTORYBONUS"), "test", 0);
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfMessageIsNull() {
		
		new BonusResponse(game, PrimaryActionState.getInstance(), bonuses, new SingleBonus("VICTORYBONUS"), null, 0);
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfTypeOfBonusIsNull() {
		
		new BonusResponse(game, PrimaryActionState.getInstance(), bonuses, null, "test", 0);
	}
	
	@Test
	public void testGetMessageShouldTReturnTheMessagePassAsParam() {

		BonusResponse response = new BonusResponse(game, PrimaryActionState.getInstance(), bonuses, new SingleBonus("VICTORYBONUS"), "test", 0);
		Assert.assertEquals("test", response.getMessage());
	}
	
	@Test
	public void testGetGameShouldTReturnTheGamePassAsParam() {

		BonusResponse response = new BonusResponse(game, PrimaryActionState.getInstance(), bonuses, new SingleBonus("VICTORYBONUS"), "test", 0);
		Assert.assertEquals(game, response.getGame());
	}
	
	@Test
	public void testGetStateShouldTReturnTheStatePassAsParam() {

		BonusResponse response = new BonusResponse(game, PrimaryActionState.getInstance(), bonuses, new SingleBonus("VICTORYBONUS"), "test", 0);
		Assert.assertEquals(PrimaryActionState.getInstance(), response.getState());
	}
	
	@Test
	public void testGetBonusShouldTReturnTheListOfBonusesPassAsParam() {

		BonusResponse response = new BonusResponse(game, PrimaryActionState.getInstance(), bonuses, new SingleBonus("VICTORYBONUS"), "test", 0);
		Assert.assertEquals(bonuses, response.getBonus());
	}
	
	@Test
	public void testGetTypeOfBonusShouldTReturnTheTypeOfBonusPassAsParam() {

		BonusResponse response = new BonusResponse(game, PrimaryActionState.getInstance(), bonuses, new SingleBonus("VICTORYBONUS"), "test", 0);
		Assert.assertEquals(new SingleBonus("VICTORYBONUS"), response.getTypeOfBonus());
	}
	
	@Test
	public void testDisplayeShouldSetTheGameItCarriesToTheGameInTheVisitor() {

		BonusResponse response = new BonusResponse(game, PrimaryActionState.getInstance(), bonuses, new SingleBonus("VICTORYBONUS"), "test", 0);
		TokenResponse tokenResponse = new TokenResponse(new Token());
		
		//Send a tokenResponse to init the token at visitor to avoid null pointer exception
		tokenResponse.display(visitor);
		response.display(visitor);
		Assert.assertEquals(visitor.getGame(), response.getGame());
	}
	
	@Test
	public void testDisplayeShouldSetTheGameItCarriesToTheGameInTheVisitorAndNotPrintTheIfInVisitorIfListOfBonusIsEmpty() {
		
		Token token = new Token();
		
		TokenResponse responseToken = new TokenResponse(token);
		BonusResponse response = new BonusResponse(game, PrimaryActionState.getInstance(), Collections.emptyList(), new SingleBonus("VICTORYBONUS"), "test", 0);
		
		responseToken.display(visitor);
		response.display(visitor);
		Assert.assertEquals(visitor.getGame(), response.getGame());
		
	}
	
}
