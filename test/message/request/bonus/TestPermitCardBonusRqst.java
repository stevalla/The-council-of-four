package it.polimi.ingsw.cg32.message.request.bonus;

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
import it.polimi.ingsw.cg32.controller.action.bonus.PermitCardBonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestPermitCardBonusRqst {

	PermitCardBonusRqst request;
	private Game game;
	private Player player;
	private NormalRegion region;
	private ActionVisitor visitor;
	private Action action;
	private String permitCardId;
	
	@Test(expected = NullPointerException.class)
	public void testConsturctorWithTokenNullValuesShouldThrowANullPointerException() {
		
		request = new PermitCardBonusRqst(null, null, null, new SingleBonus("PERMITCARDBONUS"));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConsturctorWithBonusNullValuesShouldThrowANullPointerException() {
		
		request = new PermitCardBonusRqst(new Token(), null, null, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructorWhenTheTargetRegionIsNullShouldThrowANullPointerException() {
		
		request = new PermitCardBonusRqst(new Token(), 
				permitCardId, null, new SingleBonus("PERMITCARDBONUS"));
	}
	
	@Before
	public void initializeRequest() {
		game = new Game("src/main/resources/map/default.txt", 4);
		player = game.getPlayers().get(0);
		visitor = new ActionVisitor();
		region = game.getBoard().getRegions(RegionType.COAST);
		permitCardId = region.getDeckOfPermitCard().getUsableCard().get(0).getId();
		request = new PermitCardBonusRqst(new Token(), 
				permitCardId, region, new SingleBonus("PERMITCARDBONUS"));
	}
	
	@Test
	public void testConstructorWhenPermitCardIdAreNullShouldCreateARequestCorrectly() {
		
		request = new PermitCardBonusRqst(new Token(), 
				null, region, new SingleBonus("PERMITCARDBONUS"));
		return;
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheBonusIsAQuantityBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new PermitCardBonusRqst(new Token(), permitCardId, region, 
				new QuantityBonus("PERMITCARDBONUS", 2));
		request.accept(game, player, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenArgumentsAreNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(null, null, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerAndBoardAreNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(null, null, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		assertNull(request.accept(null, player, visitor));
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenBoardIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		assertNull(request.accept(game, null, visitor));
	}
	
	@Test
	public void testAcceptShouldReturnAPermitCardBonusActionNotNull() throws IllegalActionException {
		
		action = request.accept(game, player, visitor);
		
		assertThat(action, is(instanceOf(PermitCardBonus.class)));
		assertNotNull(action);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheBonusNameIsNotPermitCardBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new PermitCardBonusRqst(new Token(), 
				permitCardId, region, new SingleBonus("NOBILITY"));
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenPermitCardIsNullShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new PermitCardBonusRqst(new Token(), 
				null, region, new SingleBonus("PERMITCARDBONUS"));
		
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testCreateActoinWhenThePermitCardIsNotInTheSelectedRegionShouldThrowAnIllegalActionException() throws IllegalActionException {
		String permitCardId = game.getBoard().getRegions(RegionType.MOUNTAIN).getDeckOfPermitCard().getUsableCard().get(0).getId();
		
		request = new PermitCardBonusRqst(new Token(), 
				permitCardId, region, new SingleBonus("PERMITCARDBONUS"));
		
		request.accept(game, player, visitor);
	}
	
	@Test
	public void testGetterCardToDrawWithoutAcceptShouldReturnNull() {
		request = new PermitCardBonusRqst(new Token(), 
				permitCardId, region, new SingleBonus("PERMITCARDBONUS"));
		
		assertNull(request.getCardToDraw());
	}
	
	@Test
	public void testGetterCardToDrawAfterAcceptMustBeTheSameIdThatTheIdPassInTheConstructorOfRqst() throws IllegalActionException {
		
		request.accept(game, player, visitor);
		assertEquals(permitCardId, request.getCardToDraw().getId());
	}
	
	@Test
	public void testGetterRegionBeforeAndAfterAcceptShouldReturnTheSameRegion() throws IllegalActionException {
		
		NormalRegion region = request.getTargetRegion();
		
		request.accept(game, player, visitor);
		
		assertEquals(region, request.getTargetRegion());
	}
}
