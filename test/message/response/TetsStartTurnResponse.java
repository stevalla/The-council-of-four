package it.polimi.ingsw.cg32.message.response;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.client.cli.CliMessageVisitor;

public class TetsStartTurnResponse {

	private List<Bonus> bonuses = new ArrayList<>(); 
	private  Game game;
	private PoliticCardPrototype prototype;
	private PoliticCard cardToDraw;
	private CliMessageVisitor visitor; 
	
	@Before
	public  void initTest(){
		
		game = new Game("src/main/resources/map/default.txt", 4);
		visitor = new CliMessageVisitor();
		
		SingleBonus bonus1 = new SingleBonus("CITYBONUS");
		QuantityBonus bonus2 = new QuantityBonus("ASSISTANTBONUS", 3);
		SingleBonus bonus3 = new SingleBonus("NOBILITYBONUS");
		
		bonuses.add(bonus1);
		bonuses.add(bonus2);
		bonuses.add(bonus3);
		
		prototype = new PoliticCardPrototype();
		cardToDraw = prototype.getPoliticCard();
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfGameIsNull() {
		
		new StartTurnResponse(null, cardToDraw, "test");
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfCardToDrawIsNull() {
		
		new StartTurnResponse(game, null, "test");
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfMessageIsNull() {
		
		new StartTurnResponse(game, cardToDraw, null);
	}
	
	@Test
	public void testGetMessageShouldTReturnTheMessagePassAsParam() {

		StartTurnResponse response = new StartTurnResponse(game, cardToDraw, "test");
		Assert.assertEquals("test", response.getMessage());
	}
	
	@Test
	public void testGetGameShouldTReturnTheGamePassAsParam() {

		StartTurnResponse response = new StartTurnResponse(game, cardToDraw, "test");
		Assert.assertEquals(game, response.getGame());
	}
	
	@Test
	public void testGetCardDrawedShouldTReturnTheCardToDrawPassAsParam() {

		StartTurnResponse response = new StartTurnResponse(game, cardToDraw, "test");
		Assert.assertEquals(cardToDraw, response.getCardDrawed());
	}
	
	@Test
	public void testDisplayShouldSetTheGameItCarriesToTheGameInTheVisitor() {

		StartTurnResponse response = new StartTurnResponse(game, cardToDraw, "test");
		response.display(visitor);
		Assert.assertEquals(visitor.getGame(), response.getGame());
	}
	
}
