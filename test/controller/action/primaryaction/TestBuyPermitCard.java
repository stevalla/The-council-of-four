package it.polimi.ingsw.cg32.controller.action.primaryaction;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;

public class TestBuyPermitCard {

	BuyPermitCard action;
	private Player player;
	private Game game;
	private NormalRegion region;
	private List<PoliticCard> politicCards;
	private PermitCard card;
	private int moneyToPay;
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		player = game.getPlayers().get(2);
		region = game.getBoard().getRegions(RegionType.COAST);
		politicCards = new ArrayList<>(player.getPoliticCards());
		card = region.getDeckOfPermitCard().getUsableCard().get(0);
		moneyToPay = 1;
		action = new BuyPermitCard(player, game, region, politicCards, card, moneyToPay);
	}
	
	@Test
	public void testExecuteShouldRemoveThePoliticCardsPassedFromPlayersPoliticCards() {
		int politicSizeBefore = player.getPoliticCards().size();
		action.execute();
		assertFalse(player.getPoliticCards().contains(politicCards));
		assertTrue(politicSizeBefore == player.getPoliticCards().size() + politicCards.size());
	}
	
	@Test
	public void testExecuteShouldPayTheAmountPassedAsParam() {
		int coinsBefore = player.getCoinsTrack().getCurrentCoins();
		action.execute();
		assertTrue(coinsBefore == player.getCoinsTrack().getCurrentCoins() + moneyToPay);
	}
	
	@Test
	public void testExecuteIfThereAreThePermitCardInTheRequestShouldAddItToPlayerPermitCardAndAddTheBonusOfTheCardToTheResponse() {
		assertFalse(player.getPermitCards().contains(card));
		
		ActionResponseMessage response = action.execute();
		
		assertTrue(player.getPermitCards().contains(card));
		assertTrue(CollectionUtils.isEqualCollection(response.getBonus(), card.getBonus()));
	}
	
	@Test
	public void testExecuteIfThePermitCardIsNotInTheUsableCardShouldReturnAResponseWithAnEmptyListOfBonus() {
		card = region.getDeckOfPermitCard().getDeck().poll();
		region.getDeckOfPermitCard().getDeck().clear();
		int sizeBefore = player.getPermitCards().size();
		
		action = new BuyPermitCard(player, game, region, politicCards, card, moneyToPay);
		ActionResponseMessage response = action.execute();
		assertTrue(sizeBefore == player.getPermitCards().size());
		assertTrue(response.getBonus().isEmpty());
	}
	
	@Test
	public void testExecuteCoinsBonusShouldReturnAResponseMessage() {
		ResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(ResponseMessage.class)));
	}
}
