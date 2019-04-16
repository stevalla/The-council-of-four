package it.polimi.ingsw.cg32.controller.action.primaryaction;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.balcony.Balcony;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.Region;

public class TestElectCouncillor {

	ElectCouncillor action;
	private Player player;
	private Game game;
	private Region region;
	private Councillor councillor;
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		player = game.getPlayers().get(2);
		region = game.getBoard().getRegions(RegionType.COAST);
		councillor = game.getBoard().getCouncillorReserve().getCouncillorInReserve().get(0);
		action = new ElectCouncillor(player, game, region, councillor);
	}
	
	@Test
	public void textExecuteShouldPayFourCoinsToThePlayer() {
		int coinsBefore = player.getCoinsTrack().getCurrentCoins();
		action.execute();
		assertTrue(coinsBefore + 4 == player.getCoinsTrack().getCurrentCoins());
	}
	
	@Test
	public void testExecuteShouldTakeTheCouncillorPassedFromTheReserveAndPutItInTheBalconyOfTheSelectedRegion() {
		Balcony balconyBefore = region.getBalcony();
		assertTrue(game.getBoard().getCouncillorReserve().getCouncillorInReserve().contains(councillor));
		assertFalse(balconyBefore.getSeats().contains(councillor));
		action.execute();
		assertTrue(region.getBalcony().getSeats().contains(councillor));
		assertFalse(game.getBoard().getCouncillorReserve().getCouncillorInReserve().contains(councillor));
	}
	
	@Test
	public void testExecuteWhenRegionIsKingRegionShouldReturnAResponseWithRegionKing() {
		action = new ElectCouncillor(player, game, game.getBoard().getKingRegion(), 
				game.getBoard().getKingRegion().getBalcony().getSeats().get(0));
		ActionResponseMessage response = action.execute();
		assertTrue(response.getMessage().contains("KING"));
	}
}
