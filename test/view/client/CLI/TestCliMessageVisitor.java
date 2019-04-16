package it.polimi.ingsw.cg32.view.client.CLI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.InitializeGameResponse;
import it.polimi.ingsw.cg32.message.response.TokenResponse;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.client.cli.CliMessageVisitor;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestCliMessageVisitor {

	CliMessageVisitor visitor;
	private Game game;
	
	@Before
	public void initParams() {
		game = new Game("src/main/resources/map/default.txt", 3);
		visitor = new CliMessageVisitor();
	}
	
	@Test
	public void testbonusToExecuteShouldReturnAnEMptyListAtTheStart() {
		assertTrue(visitor.getBonusToExecute().isEmpty());
	}
	
	@Test
	public void testDisplayInitializeGameResponseHaveToSetTheGameInTheVisitor() {
		assertNull(visitor.getGame());
		InitializeGameResponse response = new InitializeGameResponse(game);
		response.display(visitor);
		assertEquals(game, visitor.getGame());
	}
	
	@Test
	public void testDisplayTokenResponseHaveToSetTheTokenInTheVisitor() {
		assertNull(visitor.getToken());
		Token token = new Token();
		TokenResponse response = new TokenResponse(token);
		response.display(visitor);
		assertEquals(token, visitor.getToken());
	}
}
