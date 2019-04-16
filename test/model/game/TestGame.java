package it.polimi.ingsw.cg32.model.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestGame {

	Game game;
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenFileIsNullShouldThrowANullPointerException(){
		
		game = new Game(null, (int) (Math.random()*10));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructWhenNumberOfPlayersIsMinorThan2ShouldThrowAnIllegalArgumentException() {
		
		game = new Game("src/main/resources/map/default.txt", 1);
	}
	
	@Test
	public void testNumberOfPlayersAfterCreationGameShouldBeEqualsToSecondArgumentsOfConstructor() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		
		assertEquals(3, game.getPlayers().size());
	}
	
	@Test
	public void testSetBoardNullShouldChangeTheReferenceOfTheBoardToNull() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		
		game.setBoard(null);
		assertNull(game.getBoard());
	}
	
	@Test
	public void testGetterAndSetterShoppingCart() {
		game = new Game("src/main/resources/map/default.txt", 3);
		Map<Token, ShoppingCart> shoppingCarts = new HashMap<>();
		game.setStore(shoppingCarts);
		assertEquals(shoppingCarts, game.getStore());
	}
}
