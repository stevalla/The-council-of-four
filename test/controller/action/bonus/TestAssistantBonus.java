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

import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

@RunWith(Parameterized.class)
public class TestAssistantBonus {

	AssistantBonus action;
	private Game game;
	private Player player;
	private QuantityBonus bonus;
	private int quantity;
	private int quantityAssistant;
	
	public TestAssistantBonus(int quantity, int quantityAssistant) {
		this.quantityAssistant = quantityAssistant;
		this.quantity = quantity;
	}
	
	@Parameters(name = "{index}: setQuantityBonusAndNumberAssistant({0}{1})")
	public static Iterable<Object[]> data() {
	return Arrays.asList(new Object[][] {
	{ 1, 3 },
	{ 2, 5 },
	{ 3, 2 },
	{ 5, 7 },
	{ 8, 20 },
	{ 13, 14 },
	{ 25, 25 },
	{ 6, 6 },
	{ 18, 30 },
	{ 20, 50 },
	{ 50, 49 },
	{ 15, 0 },
	{ 4, 40 },
	{ 1, 1 }
	});
	}
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		player = game.getPlayers().get(0);
		bonus = new QuantityBonus("ASSISTANTBONUS", 3);
	}
	
	@Test
	public void testExecuteShouldAddTheQuantityOfAssistantsOfTheBonusToThePlayerIfTheReserveIsEmptyStopToAdd() {
		QuantityBonus bonus = new QuantityBonus("ASSISTANTBONUS", quantity);
		game.getBoard().getAssistantsReserve().getAssistants().clear();
		for(int i=0; i<quantityAssistant; i++)
			game.getBoard().getAssistantsReserve().getAssistants().add(new Assistant());
		
		int reserve = game.getBoard().getAssistantsReserve().getAssistants().size();
		int playerAss = player.getAssistants().size();
		
		action = new AssistantBonus(player, game, bonus);
		action.execute();
		
		if(reserve - quantity < 0) {
			assertTrue(game.getBoard().getAssistantsReserve().getAssistants().isEmpty());
			assertTrue(player.getAssistants().size() == playerAss + reserve);
		}
		else {
			assertTrue(game.getBoard().getAssistantsReserve().getAssistants().size() == reserve - quantity);
			assertTrue(player.getAssistants().size() == playerAss + quantity);
		}
	}
	
	@Test
	public void testExecuteAssistantBonusShouldReturnAResponseMessage() {
		action = new AssistantBonus(player, game, bonus);
		ResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(ResponseMessage.class)));
	}
}
