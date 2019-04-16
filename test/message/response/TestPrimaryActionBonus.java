package it.polimi.ingsw.cg32.message.response;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.turn.state.PrimaryActionState;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.client.cli.CliMessageVisitor;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestPrimaryActionBonus {

	
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
	
	@Test
	public void testGetResponseShouldReturnABonusResponse(){
		
		ResponseMessage bonusResponse = new BonusResponse(game, PrimaryActionState.getInstance(), bonuses, new SingleBonus("VICTORYBONUS"), "test", 0);
		assertThat(bonusResponse, is(instanceOf(BonusResponse.class)));
	}
	
	@Test
	public void testDisplayeShouldSetTheGameItCarriesToTheGameInTheVisitor() {
		
		BonusResponse bonusResponse = new BonusResponse(game, PrimaryActionState.getInstance(), bonuses, new SingleBonus("VICTORYBONUS"), "test", 0);
		TokenResponse tokenResponse = new TokenResponse(new Token());
		
		//Send a tokenResponse to init the token at visitor to avoid null pointer exception
		tokenResponse.display(visitor);
		bonusResponse.display(visitor);
		Assert.assertEquals(visitor.getGame(), bonusResponse.getGame());
	}
}
