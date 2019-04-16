package it.polimi.ingsw.cg32.message.request.bonus;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.bonus.CoinsBonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestCoinsBonus {

	CoinsBonusRqst request;
	private Game game;
	private Player player;
	private ActionVisitor visitor;
	private Action action;
	
	@Test(expected = NullPointerException.class)
	public void testConsturctorWithTokenNullValuesShouldThrowANullPointerException() {
		
		request = new CoinsBonusRqst(null, new QuantityBonus("COINSBONUS", 3));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConsturctorWithBonusNullValuesShouldThrowANullPointerException() {
		
		request = new CoinsBonusRqst(new Token(), null);
	}
	
	@Before
	public void initializeRequest() {
		game = new Game("src/main/resources/map/default.txt", 4);
		player = game.getPlayers().get(0);
		visitor = new ActionVisitor();
		request = new CoinsBonusRqst(new Token(), new QuantityBonus("COINSBONUS", 2));
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheBonusIsASingleBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new CoinsBonusRqst(new Token(), new SingleBonus("COINSBONUS"));
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
	public void testAcceptShouldReturnACoinsBonusActionNotNull() throws IllegalActionException {
		
		action = request.accept(game, player, visitor);
		
		assertThat(action, is(instanceOf(CoinsBonus.class)));
		assertNotNull(action);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheBonusNameIsNotCoinsBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new CoinsBonusRqst(new Token(), new QuantityBonus("VICTORYBONUS", 2));
		request.accept(game, player, visitor);
	}
}
