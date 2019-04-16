package it.polimi.ingsw.cg32.model.game;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.assistants.AssistantsReserve;
import it.polimi.ingsw.cg32.model.track.NobilityTrack;

public class TestFactoryLoaderGame {

	FactoryLoaderGame factoryGame;
	private String filePath;
	private int numberPlayers;
	private NobilityTrack track;
	private AssistantsReserve reserve;
	
	@Before
	public void initializeParams() {
		
		factoryGame = new FactoryLoaderGame();
		filePath = "src/main/resources/map/default.txt";
		numberPlayers = 3;
		Board board = factoryGame.createBoard(filePath, numberPlayers);
		track = board.getNobilityTrack();
		reserve = board.getAssistantsReserve();
	}
	
	@Test(expected = NullPointerException.class)
	public void testCreateBoardWhenFilePathIsNullShouldThrowANullPointerException() {
		
		factoryGame.createBoard(null, numberPlayers);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateBoardWhenNumberOfPlayerIsMinorThanTwoShouldThrowAnIllegalArgumentException() {
		
		factoryGame.createBoard(filePath, 1);
	}
	
	@Test
	public void testCreateBoardWithAWrongFilePathShouldReturnNull() {
		
		assertNull(factoryGame.createBoard("test", 2));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreatePlayersWhenNumberOfPlayerIsMinorThanTwoShouldThrowAnIllegalArgumentException() {
		
		factoryGame.createPlayers(track, reserve, 1);
	}
	
	@Test(expected = NullPointerException.class)
	public void testCreatePlayersWhenNobilityTrackIsNullShouldThrowANullPointerException() {
		
		factoryGame.createPlayers(null, reserve, 3);
	}
	
	@Test(expected = NullPointerException.class)
	public void testCreatePlayersWhenAssistantReserveIsNullShouldThrowANullPointerException() {
		
		factoryGame.createPlayers(track, null, 3);
	}
}
