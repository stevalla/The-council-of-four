package it.polimi.ingsw.cg32.message.response;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.client.cli.CliMessageVisitor;

public class TestInitializeGameResponse {

	private static List<Bonus> bonuses = new ArrayList<>(); 
	private static Game game;
	private static CliMessageVisitor visitor; 
	
	
	@BeforeClass
	public static void initTest(){
		
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
		
		new InitializeGameResponse(null);
	}
	
	@Test
	public void testGetGameShouldTReturnTheGamePassAsParam() {

		InitializeGameResponse response = new InitializeGameResponse(game);
		Assert.assertEquals(game, response.getGame());
	}
	
	@Test
	public void testDisplayeShouldSetTheGameItCarriesToTheGameInTheVisitor() {

		InitializeGameResponse response = new InitializeGameResponse(game);		
		response.display(visitor);
		Assert.assertEquals(visitor.getGame(), response.getGame());
	}
	

}
