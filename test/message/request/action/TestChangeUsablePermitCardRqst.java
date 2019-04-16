package it.polimi.ingsw.cg32.message.request.action;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.ChangeUsablePermitCard;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestChangeUsablePermitCardRqst {

	ChangeUsablePermitCardRqst request;
	private NormalRegion region;
	private Token token;
	private Game game;
	private Player player;
	private ActionVisitor visitor;
	
	@Before
	public void initParams() {
		game = new Game("src/main/resources/map/default.txt", 3);
		region = game.getBoard().getRegions(RegionType.COAST);
		token = new Token();
		player = game.getPlayers().get(0);
		visitor = new ActionVisitor();
		request = new ChangeUsablePermitCardRqst(token, region);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenTokenIsNullShouldThrowANullPointerExcetpion() {
		
		new ChangeUsablePermitCardRqst(null, region);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenRegionIsNullShouldThrowANullPointerException() {
		
		new ChangeUsablePermitCardRqst(token, null);
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
	public void testAcceptShouldCreateChangeUsablePermitCard() throws IllegalActionException {

		Action action = request.accept(game, player, visitor);

		assertThat(action, is(instanceOf(ChangeUsablePermitCard.class)));
		assertNotNull(action);
	}
	
	@Test
	public void testGetTargetRegionMustBeTheSameOfTheRegionPassedInTheConstructor() {
		
		assertEquals(region, request.getTargetRegion());
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenDeckOfPermitSizeSizeIsLessThanTwoShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		NormalRegion region = this.region;
		region.getDeckOfPermitCard().getDeck().clear();
		
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenPlayerHasNotAssistantShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		Player player = this.player;
		player.getAssistants().clear();
		
		request.accept(game, player, visitor);
	}
}
