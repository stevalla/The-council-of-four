package it.polimi.ingsw.cg32.controller.action.secondaryaction;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;

public class TestChangeUsablePermitCard {

	ChangeUsablePermitCard action;
	private Player player;
	private Game game;
	private NormalRegion region;
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		region = game.getBoard().getRegions(RegionType.COAST);
		player = game.getPlayers().get(0);
		action = new ChangeUsablePermitCard(player, game, region);
	}
	
	@Test
	public void testExecuteShouldChangeTheUsableCardsOfTheSelectedRegionAndTakeOffAnAssistantFromThePlayer() {
		List<PermitCard> usableBefore = region.getDeckOfPermitCard().getUsableCard();
		int assistantBefore = player.getAssistants().size();
		action.execute();
		assertThat(usableBefore, IsIterableContainingInOrder.contains(region.getDeckOfPermitCard().getUsableCard().toArray()));
		assertTrue(assistantBefore - 1 == player.getAssistants().size());
	}
	
	@Test
	public void testExecuteShouldReturnAnActionResponse() {
		ActionResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(ActionResponse.class)));
	}
}
