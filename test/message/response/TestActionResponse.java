package it.polimi.ingsw.cg32.message.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.turn.state.PrimaryActionState;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.client.cli.CliMessageVisitor;

public class TestActionResponse {

	private List<Bonus> bonuses = new ArrayList<>(); 
	private Game game;
	private CliMessageVisitor visitor; 
	
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
		
		new ActionResponse(null, PrimaryActionState.getInstance(), bonuses, "test", 0);
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfListOfBonusesIsNull() {
		
		new ActionResponse(game, PrimaryActionState.getInstance(), null, "test", 0);
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfMessageIsNull() {
		
		new ActionResponse(game, PrimaryActionState.getInstance(), bonuses, null, 0);
	}
	
	@Test
	public void testGetMessageShouldTReturnTheMessagePassAsParam() {

		ActionResponse response = new ActionResponse(game, PrimaryActionState.getInstance(), bonuses, "test", 0);
		Assert.assertEquals("test", response.getMessage());
	}
	
	@Test
	public void testGetGameShouldTReturnTheGamePassAsParam() {

		ActionResponse response = new ActionResponse(game, PrimaryActionState.getInstance(), bonuses, "test", 0);
		Assert.assertEquals(game, response.getGame());
	}
	
	@Test
	public void testGetStateShouldTReturnTheStatePassAsParam() {

		ActionResponse response = new ActionResponse(game, PrimaryActionState.getInstance(), bonuses, "test", 0);
		Assert.assertEquals(PrimaryActionState.getInstance(), response.getState());
	}
	
	@Test
	public void testGetBonusShouldTReturnTheListOfBonusesPassAsParam() {

		ActionResponse response = new ActionResponse(game, PrimaryActionState.getInstance(), bonuses, "test", 0);
		Assert.assertEquals(bonuses, response.getBonus());
	}
	
	@Test
	public void testDisplayeShouldSetTheGameItCarriesToTheGameInTheVisitor() {

		ActionResponse response = new ActionResponse(game, PrimaryActionState.getInstance(), Collections.emptyList(), "test", 0);
		response.display(visitor);
		Assert.assertEquals(visitor.getGame(), response.getGame());
	}

}
