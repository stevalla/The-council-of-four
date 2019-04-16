package it.polimi.ingsw.cg32.controller.action.bonus;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.game.Board;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.track.Space;

public class TestNobilityBonus {

	NobilityBonus action;
	private Player player;
	private Game game;
	private SingleBonus bonus;
	private Board board;
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		board = game.getBoard();
		player = game.getPlayers().get(0);
		bonus = new SingleBonus("NOBILITYBONUS");
	}
	
	@Test
	public void testExecuteShouldSetInTheNextCurrentSpaceTheMarkerDiskOfThePlayerOrNoMoveItIfTheNobilityTrackIsFinish() {
		int markerDiskBefore = player.getMarkerDisk().getCurrentSpace().getSpaceNumber();
		int nobilityTrackSize = board.getNobilityTrack().getTrack().size();
		
		while(true) {
			action = new NobilityBonus(player,game, bonus);
			action.execute();
			
			if(markerDiskBefore == player.getMarkerDisk().getCurrentSpace().getSpaceNumber())
				break;
			markerDiskBefore = player.getMarkerDisk().getCurrentSpace().getSpaceNumber();
		}

		assertTrue(nobilityTrackSize == player.getMarkerDisk().getCurrentSpace().getSpaceNumber()+1);
	}
	
	@Test 
	public void testExecuteWhenNobilityTrackIsFinishedShouldReturnAResponseWithAnEmptyListOfBonus() {
		
		while(true) {
			player.getMarkerDisk().nextCurrentSpace();
			if(!player.getMarkerDisk().checkFinishedTrack())
				break;
		}
		action = new NobilityBonus(player, game, bonus);
		ActionResponseMessage response = action.execute();
		
		assertTrue(response.getBonus().isEmpty());
	}
	
	@Test
	public void testExecuteShouldReturnAResponseWithBonusIfThereIsBonusInTheCurrentSpaceAfterTheActionOrAnEmptyListIfThereAreNot() {
		
		while(true) {
			action = new NobilityBonus(player, game, bonus);
			ActionResponseMessage response = action.execute();
			Space markerDiskBefore = player.getMarkerDisk().getCurrentSpace();
			
			if(markerDiskBefore.getBonus().isEmpty())
				assertTrue(response.getBonus().isEmpty());
			else 
				assertTrue(!(response.getBonus().isEmpty()));
			
			if(!player.getMarkerDisk().checkFinishedTrack())
				break;
		}
	}
	
	@Test
	public void testExecuteCoinsBonusShouldReturnABonusResponse() {
		action = new NobilityBonus(player, game, bonus);
		ActionResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(BonusResponse.class)));
	}
}
