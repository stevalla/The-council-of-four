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
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

@RunWith(Parameterized.class)
public class TestPoliticCardBonus {

	PoliticCardBonus action;
	private Player player;
	private Game game;
	private QuantityBonus bonus;
	private int quantity;
	
	public TestPoliticCardBonus(int quantity) {
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
	}
	
	@Test
	public void testExecuteShouldDrawAsManyPoliticCardAsQuantityOfTheBonusToExecuteAndGiveItToThePlayerThatExecuteTheAction() {
		int sizeBefore = player.getPoliticCards().size();
		bonus = new QuantityBonus("POLITICCARDBONUS", quantity);
		action = new PoliticCardBonus(player, game, bonus, new PoliticCardPrototype());
		action.execute();
		assertTrue(sizeBefore + quantity == player.getPoliticCards().size());
	}
	
	@Test
	public void testExecutePoliticCardBonusShouldReturnAResponseMessageWithANullListOfBonus() {
		bonus = new QuantityBonus("POLITICCARDBONUS", quantity);
		action = new PoliticCardBonus(player, game, bonus, new PoliticCardPrototype());
		ActionResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(ActionResponseMessage.class)));
	}
}
