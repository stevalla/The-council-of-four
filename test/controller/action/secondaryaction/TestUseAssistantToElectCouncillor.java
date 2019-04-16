package it.polimi.ingsw.cg32.controller.action.secondaryaction;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.balcony.Balcony;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.Region;

public class TestUseAssistantToElectCouncillor {

	UseAssistantToElectCouncillor action;
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
		action = new UseAssistantToElectCouncillor(player, game, region, councillor);
	}
	
	@Test
	public void textExecuteShouldRemoveOneAssistantsFromPlayerAndAddItToTheReserve() {
		int assistantBefore = player.getAssistants().size();
		int reserveBefore = game.getBoard().getAssistantsReserve().getAssistants().size();
		action.execute();
		assertTrue(assistantBefore - 1 == player.getAssistants().size());
		assertTrue(reserveBefore + 1 == game.getBoard().getAssistantsReserve().getAssistants().size());
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
	public void testExecuteShouldReturnAResponseMessage() {
		ResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(ResponseMessage.class)));
	}
	
	@Test
	public void testExecuteWhenRegionIsKingRegionShouldReturnAResponseWithRegionKing() {
		action = new UseAssistantToElectCouncillor(player, game, game.getBoard().getKingRegion(), 
				game.getBoard().getKingRegion().getBalcony().getSeats().get(0));
		ActionResponseMessage response = action.execute();
		assertTrue(response.getMessage().contains("KING"));
	}
}
