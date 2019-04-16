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
import it.polimi.ingsw.cg32.controller.action.bonus.ReusePermitCardBonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestReusePermitCardBonusRqst {

	ReusePermitCardBonusRqst request;
	private Game game;
	private Player player;
	private ActionVisitor visitor;
	private Action action;
	private String permitCardId;
	
	@Test(expected = NullPointerException.class)
	public void testConsturctorWithTokenNullValuesShouldThrowANullPointerException() {
		
		request = new ReusePermitCardBonusRqst(null, null, new SingleBonus("REUSEPERMITCARDBONUS"));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConsturctorWithBonusNullValuesShouldThrowANullPointerException() {
		
		request = new ReusePermitCardBonusRqst(new Token(), null, null);
	}
	
	@Before
	public void initializeRequest() {
		game = new Game("src/main/resources/map/default.txt", 4);
		player = game.getPlayers().get(0);
		visitor = new ActionVisitor();
		player.getPermitCards().add(game.getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getUsableCard().get(0));
		permitCardId = player.getPermitCards().get(0).getId();
		request = new ReusePermitCardBonusRqst(new Token(), 
				permitCardId, new SingleBonus("REUSEPERMITCARDBONUS"));
	}
	
	@Test
	public void testConstructorWhenPermitCardIdAreNullShouldCreateARequestCorrectly() {
		
		request = new ReusePermitCardBonusRqst(new Token(), 
				null, new SingleBonus("REUSEPERMITCARDBONUS"));
		return;
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheBonusIsAQuantityBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new ReusePermitCardBonusRqst(new Token(), permitCardId, 
				new QuantityBonus("REUSEPERMITCARDBONUS", 2));
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
	public void testAcceptShouldReturnAReusePermitCardBonusActionNotNull() throws IllegalActionException {
		
		action = request.accept(game, player, visitor);
		
		assertThat(action, is(instanceOf(ReusePermitCardBonus.class)));
		assertNotNull(action);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheBonusNameIsNotReusePermitCardBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new ReusePermitCardBonusRqst(new Token(), 
				permitCardId, new SingleBonus("NOBILITY"));
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenPermitCardIsNullShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new ReusePermitCardBonusRqst(new Token(), 
				null, new SingleBonus("REUSEPERMITCARDBONUS"));
		
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testCreateActoinWhenThePermitCardIsNotInPlayersPermitCardShouldThrowAnIllegalActionException() throws IllegalActionException {
		String permitCardId = game.getBoard().getRegions(RegionType.MOUNTAIN).getDeckOfPermitCard().getUsableCard().get(0).getId();
		
		request = new ReusePermitCardBonusRqst(new Token(), 
				permitCardId, new SingleBonus("REUSEPERMITCARDBONUS"));
		
		request.accept(game, player, visitor);
	}
	
	@Test
	public void testGetterTargetPermitCardWithoutAcceptShouldReturnNull() {
		request = new ReusePermitCardBonusRqst(new Token(), 
				permitCardId, new SingleBonus("PERMITCARDBONUS"));
		
		assertNull(request.getTargetPermitCard());
	}
	
	@Test
	public void testGetterTargetPermitCardAfterAcceptMustBeTheSameIdThatTheIdPassInTheConstructorOfRqst() throws IllegalActionException {
		
		request.accept(game, player, visitor);
		assertEquals(permitCardId, request.getTargetPermitCard().getId());
	}

}
