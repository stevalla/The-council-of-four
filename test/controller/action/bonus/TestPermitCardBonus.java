package it.polimi.ingsw.cg32.controller.action.bonus;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;

public class TestPermitCardBonus {

	PermitCardBonus action;
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
		bonus = new SingleBonus("PERMITCARDBONUS");
	}
	
	@Test
	public void testExecuteIfThereAreThePermitCardInTheRequestShouldAddItToPlayerPermitCardAndAddTheBonusOfTheCardToTheResponse() {
		action = new PermitCardBonus(player, game, card, region, bonus);
		
		assertFalse(player.getPermitCards().contains(card));
		
		ActionResponseMessage response = action.execute();
		
		assertTrue(player.getPermitCards().contains(card));
		assertTrue(CollectionUtils.isEqualCollection(response.getBonus(), card.getBonus()));
	}
	
	@Test
	public void testExecuteIfTheDeckOfPermitCardIsEmptyShouldNotAddTheSelectedPermitCardToThePlayerCardsAndTheResponseHaveAnEmptyListOfBonus() {
		card = region.getDeckOfPermitCard().getDeck().poll();
		region.getDeckOfPermitCard().getDeck().clear();
		int sizeBefore = player.getPermitCards().size();
		
		action = new PermitCardBonus(player, game, card, region, bonus);
		ActionResponseMessage response = action.execute();
		assertTrue(sizeBefore == player.getPermitCards().size());
		assertTrue(response.getBonus().isEmpty());
	}
	
	@Test
	public void testExecuteIfTheUsablePermitCardOfItsRegionIsFinishedShouldNotAddThePermitCardAndReturnAnEmptyListOfBonus() {
		card = game.getBoard().getRegions(RegionType.MOUNTAIN).getDeckOfPermitCard().getDeck().poll();
		int sizeBefore = player.getPermitCards().size();
		
		action = new PermitCardBonus(player, game, card, region, bonus);
		ActionResponseMessage response = action.execute();
		assertTrue(sizeBefore == player.getPermitCards().size());
		assertTrue(response.getBonus().isEmpty());
	}
	
	@Test
	public void testExecuteCoinsBonusShouldReturnABonusMessage() {
		action = new PermitCardBonus(player, game, card, region, bonus);
		ActionResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(BonusResponse.class)));
	}
}
