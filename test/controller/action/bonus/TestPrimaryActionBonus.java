package it.polimi.ingsw.cg32.controller.action.bonus;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.primaryaction.ElectCouncillor;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

public class TestPrimaryActionBonus {

	PrimaryActionBonus action;
	private Player player;
	private SingleBonus bonus;
	private Game game;
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		player = game.getPlayers().get(0);
		bonus = new SingleBonus("PRIMARYACTIONBONUS");
	}
	
	@Test
	public void testExecuteShouldExecuteTheSelectedPrimaryActionAndReturnTheResponseInTheResponseOfTheSecondaryAction() {
		
		ElectCouncillor primaryAction = new ElectCouncillor(player, game, game.getBoard().getRegions(RegionType.COAST), 
				game.getBoard().getCouncillorReserve().getCouncillorInReserve().get(0));
		
		action = new PrimaryActionBonus(player, game, primaryAction, bonus);
		ActionResponseMessage response = action.execute();
		assertTrue(response.getMessage().contains("ELECTCOUNCILLOR"));
	}
}
