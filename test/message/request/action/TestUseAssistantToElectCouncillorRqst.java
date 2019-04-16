package it.polimi.ingsw.cg32.message.request.action;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.UseAssistantToElectCouncillor;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.KingRegion;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestUseAssistantToElectCouncillorRqst {

	UseAssistantToElectCouncillorRqst request;
	private NormalRegion region;
	private Token token;
	private Game game;
	private Player player;
	private ActionVisitor visitor;
	private Councillor councillor;
	
	@Before
	public void initParams() {
		game = new Game("src/main/resources/map/default.txt", 3);
		region = game.getBoard().getRegions(RegionType.COAST);
		token = new Token();
		player = game.getPlayers().get(0);
		visitor = new ActionVisitor();
		councillor = game.getBoard().getCouncillorReserve().getCouncillorInReserve().get(0);
		request = new UseAssistantToElectCouncillorRqst(token, region, councillor.getColor());
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenTokenIsNullShouldThrowANullPointerExcetpion() {
		
		new UseAssistantToElectCouncillorRqst(null, region, Colore.BLACK);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenRegionIsNullShouldThrowANullPointerException() {
		
		new UseAssistantToElectCouncillorRqst(token, null, Colore.BLACK);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenColorIsNullShouldThrowANullPointerException() {
		
		new UseAssistantToElectCouncillorRqst(token, region, null);
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
	public void testAcceptShouldCreateAnUseAssistantToElectCouncillor() throws IllegalActionException {

		Action action = request.accept(game, player, visitor);

		assertThat(action, is(instanceOf(UseAssistantToElectCouncillor.class)));
		assertNotNull(action);
	}
	
	//
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenPlayerHasNoAssistantsShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		Player player = this.player;
		player.getAssistants().clear();

		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheCouncillorIsNotInTheCouncillorReserveShouldThrowAnIllegalActionException() throws IllegalActionException {

		UseAssistantToElectCouncillorRqst request = new UseAssistantToElectCouncillorRqst(token, region, Colore.JOLLY);
		
		request.accept(game, player, visitor);
	}
	
	@Test
	public void testGetTargetRegionMustBeTheSameOfTheRegionPassedInTheConstructor() {
		
		assertEquals(region, request.getTargetRegion());
	}
	
	@Test
	public void testGetCouncillorMustBeTheSameColorOfTheArgPassedInTheConstructor() throws IllegalActionException {
		
		request.accept(game, player, visitor);
		assertEquals(councillor.getColor(), request.getCouncillor().getColor());
	}
	
	@Test
	public void testGetCouncillorAfterAcceptMustBeNull() {
		
		assertNull(request.getCouncillor());
	}
	
	@Test
	public void testGetTargetRegionWHenRegionIsKingRegionMustReturnAKingRegion() throws IllegalActionException {
		KingRegion kingRegion = new KingRegion();
		UseAssistantToElectCouncillorRqst request = new UseAssistantToElectCouncillorRqst(token, kingRegion, councillor.getColor());
		request.accept(game, player, visitor);
		
		assertThat(request.getTargetRegion(), is(instanceOf(KingRegion.class)));
	}
	
}
