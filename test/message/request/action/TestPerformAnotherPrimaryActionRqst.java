package it.polimi.ingsw.cg32.message.request.action;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.PerformAnotherPrimaryAction;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestPerformAnotherPrimaryActionRqst {

	PerformAnotherPrimaryActionRqst request;
	private Token token;
	private Game game;
	private Player player;
	private ActionVisitor visitor;
	
	@Before
	public void initParams() {
		game = new Game("src/main/resources/map/default.txt", 3);
		token = new Token();
		player = game.getPlayers().get(0);
		visitor = new ActionVisitor();
		request = new PerformAnotherPrimaryActionRqst(token);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenTokenIsNullShouldThrowANullPointerExcetpion() {
		
		new PerformAnotherPrimaryActionRqst(null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(game, null, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenBoardIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(null, player, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenVisitorIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(game, player, null);
	}
	
	@Test
	public void testAcceptShouldCreateAnPerformAnotherPrimaryAction() throws IllegalActionException {
		Player player = this.player;
		
		for(int i=0; i<10; i++)
			player.getAssistants().add(new Assistant());
		
		Action action = request.accept(game, player, visitor);

		assertThat(action, is(instanceOf(PerformAnotherPrimaryAction.class)));
		assertNotNull(action);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenPlayerNotHaveEnoughAssistantShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request.accept(game, player, visitor);
	}
}
