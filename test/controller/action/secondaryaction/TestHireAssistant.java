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

public class TestHireAssistant {

	HireAssistant action;
	private Player player;
	private Game game;
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		player = game.getPlayers().get(0);
		action = new HireAssistant(player, game);
	}
	
	@Test
	public void textExecuteShouldRemoveThreeCoinsFromPlayerCoinsTrackAndAddOneAssistantToFromTheReserve() {
		int assistantBefore = player.getAssistants().size();
		int reserveBefore = game.getBoard().getAssistantsReserve().getAssistants().size();
		int coinsBefore = player.getCoinsTrack().getCurrentCoins();
		action.execute();
		assertTrue(reserveBefore - 1 == game.getBoard().getAssistantsReserve().getAssistants().size());
		assertTrue(assistantBefore + 1 == player.getAssistants().size());
		assertTrue(coinsBefore - 3 == player.getCoinsTrack().getCurrentCoins());
	}
	
	@Test
	public void testExecuteShouldReturnAResponseMessage() {
		ResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(ResponseMessage.class)));
	}
}
