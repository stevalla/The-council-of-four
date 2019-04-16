package it.polimi.ingsw.cg32.controller.action.secondaryaction;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

public class TestPerformAnotherPrimaryAction {
	
	PerformAnotherPrimaryAction action;
	private Player player;
	private Game game;
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		player = game.getPlayers().get(2);
		action = new PerformAnotherPrimaryAction(player, game);
	}
	
	@Test
	public void textExecuteShouldRemoveThreeAssistantsFromPlayerAndAddThemToTheReserve() {
		int assistantBefore = player.getAssistants().size();
		int reserveBefore = game.getBoard().getAssistantsReserve().getAssistants().size();
		action.execute();
		assertTrue(assistantBefore - 3 == player.getAssistants().size());
		assertTrue(reserveBefore + 3 == game.getBoard().getAssistantsReserve().getAssistants().size());
	}
	
	@Test
	public void testExecuteShouldReturnAResponseMessage() {
		ResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(ResponseMessage.class)));
	}
}
