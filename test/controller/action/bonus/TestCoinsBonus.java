package it.polimi.ingsw.cg32.controller.action.bonus;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

@RunWith(Parameterized.class)
public class TestCoinsBonus {

	CoinsBonus action;
	private Player player;
	private Game game;
	private QuantityBonus bonus;
	private int quantity;
	
	public TestCoinsBonus(int quantity) {
		this.quantity = quantity;
	}
	
	@Parameters(name = "{index}: quantityBonus({0})")
	public static Iterable<Object[]> data() {
	return Arrays.asList(new Object[][] {
	{ 1 },
	{ 3 },
	{ 5 },
	{ 10 },
	{ 15 },
	{ 18 },
	{ 21 },
	{ 27 },
	{ 30 },
	{ 35 },
	{ 39 },
	{ 43 },
	{ 50 },
	{ 57 },
	{ 70 },
	{ 88 },
	{ 99 },
	{ 100 }
	});
	}
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		player = game.getPlayers().get(0);
		bonus = new QuantityBonus("COINSBONUS", 3);
	}
	
	@Test
	public void testExecuteShouldAddAsManyCoinsAsQuantityOfTheBonusToExecuteAndIfTheCoinsTrackIsFinishMustReturnTheLastCoinsTrackPoints() {
		QuantityBonus bonus = new QuantityBonus("CITYBONUS", quantity);
		action = new CoinsBonus(player, game, bonus);
		int coinsTrackSize = player.getCoinsTrack().getCoinsTrackSize();
		int playerCoinsBefore = player.getCoinsTrack().getCurrentCoins();
		action.execute();
		
		if(playerCoinsBefore + quantity > coinsTrackSize) 
			assertTrue(coinsTrackSize == player.getCoinsTrack().getCurrentCoins());
		else
			assertTrue(playerCoinsBefore + quantity == player.getCoinsTrack().getCurrentCoins());
	}
	
	@Test
	public void testExecuteCoinsBonusShouldReturnABonusResponse() {
		action = new CoinsBonus(player, game, bonus);
		ActionResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(BonusResponse.class)));
	}
	
}
