package it.polimi.ingsw.cg32.controller.action.bonus;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;

public class TestReusePermitCardBonus {

	ReusePermitCardBonus action;
	private Player player;
	private SingleBonus bonus;
	private Game game;
	private PermitCard card;
	private NormalRegion region;
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		region = game.getBoard().getRegions(RegionType.COAST);
		card = region.getDeckOfPermitCard().getUsableCard().get(0);
		player = game.getPlayers().get(0);
		bonus = new SingleBonus("REUSEPERMITCARDBONUS");
	}
	
	@Test
	public void testExecuteShouldReturnAResponseWithTheBonusOfTheCardPassed() {
		List<Bonus> bonusList = card.getBonus();
		action = new ReusePermitCardBonus(player, game, card, bonus);
		ActionResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(BonusResponse.class)));
		assertThat(bonusList, IsIterableContainingInOrder.contains(response.getBonus().toArray()));
	}
}
