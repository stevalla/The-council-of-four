package it.polimi.ingsw.cg32.controller.action.primaryaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.controller.turn.state.PrimaryActionState;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;

public class TestBuildEmporiumByPermitCard {

	BuildEmporiumByPermitCard action;
	private Player player;
	private Game game;
	private NormalRegion region;
	private int assistantsToPay;
	private City city;
	private PermitCard selectedPermitCard;
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		player = game.getPlayers().get(2);
		region = game.getBoard().getRegions(RegionType.COAST);
		selectedPermitCard = region.getDeckOfPermitCard().getDeck().poll();
		player.getPermitCards().add(selectedPermitCard);
		city = game.getBoard().getCities().get(0);
		assistantsToPay = 1;
		action = new BuildEmporiumByPermitCard(player, game, city, assistantsToPay, selectedPermitCard);
	}
	
	@Test
	public void testExecuteShouldRemoveFromPlayersAssistantTheNumberOfAssistantPassedInTheConstructor() {
		int assistantBefore = player.getAssistants().size();
		action.execute();
		assertTrue(assistantBefore == player.getAssistants().size() + assistantsToPay);
	}
	
	@Test
	public void testExecuteShouldAddTheCityPassedAsParamToThePlayersCityAndAddInItAnEMporiumOfThePlayer() {
		Emporium emporiumToAdd = player.getEmporiums().peek();
		assertFalse(player.getCities().contains(city));
		player.getEmporiums().forEach(e -> assertFalse(city.getEmporium().contains(e)));
		
		action.execute();
		assertTrue(player.getCities().contains(city));
		assertTrue(city.getEmporium().contains(emporiumToAdd));
	}
	
	@Test
	public void testExecuteWhenBuildTheLastEmporiumShouldGiveToThePlayerThreeVictoryPoints() {
		int victoryPointsBefore = player.getVictoryTrack().getPoints();
		for(int i=0; i<9; i++)
			player.getEmporiums().poll();
		action = new BuildEmporiumByPermitCard(player, game, city, assistantsToPay, selectedPermitCard);
		action.execute();
		assertTrue(victoryPointsBefore + 3 == player.getVictoryTrack().getPoints());
	}
	
	@Test
	public void testExecuteShouldReturnAnListWithOnlyTheSelectedCityBonusIfThereAreNotInTheNearCitiesOtherPlayersEmporiums() {
		ActionResponseMessage response = action.execute();
		assertTrue(CollectionUtils.isEqualCollection(response.getBonus(), city.getBonus()));
	}
	
	@Test
	public void testExecuteShouldReturnTheBonusOfTheNearCitiesOfTheSelectedCitiesWhereThereAreAnEmporiumOfThePlayer() {
		game.getBoard().getCities().get(1).getEmporium().add(player.getEmporiums().poll());
		game.getBoard().getCities().get(3).getEmporium().add(player.getEmporiums().poll());
		
		ActionResponseMessage response = action.execute();
		assertTrue(CollectionUtils.isSubCollection(city.getBonus(), response.getBonus()));
		assertTrue(CollectionUtils.isSubCollection(game.getBoard().getCities().get(1).getBonus(), response.getBonus()));
		assertTrue(CollectionUtils.isSubCollection(game.getBoard().getCities().get(3).getBonus(), response.getBonus()));
		
	}
	
	@Test
	public void testExecuteWhenThePlayerHasBuiltAnEmporiumInAllTheCitiesOfTheSelectedRegionShouldAddTwoBonusKingCardIfThereAre() {
		for(int i=1; i<5; i++)
			player.getCities().add(game.getBoard().getCities().get(i));
		assertTrue(player.getKingCards().isEmpty());
		action.execute();
		assertFalse(player.getKingCards().isEmpty());
		assertTrue(player.getKingCards().size() == 2);
	}
	
	@Test
	public void testExecuteWhenBonusKingCardAreFinishedButBonusRegionAreNotFinishedShouldAddOnlyOneKingCard() {
		for(int i=1; i<5; i++)
			player.getCities().add(game.getBoard().getCities().get(i));
		game.getBoard().getKingRegion().getDeckOfKingCard().clear();
		assertTrue(player.getKingCards().isEmpty());
		action.execute();
		assertFalse(player.getKingCards().isEmpty());
		assertTrue(player.getKingCards().size() == 1);
	}
	
	@Test
	public void testExecuteWhenTheBonusKingCardOfTheRegionIsAlreadyDrawedButThereIsTheBonusKingCardShouldReturnOnlyOneKingCard() {
		for(int i=1; i<5; i++)
			player.getCities().add(game.getBoard().getCities().get(i));
		region.getBonusRegionCard();
		assertTrue(player.getKingCards().isEmpty());
		action.execute();
		assertFalse(player.getKingCards().isEmpty());
		assertTrue(player.getKingCards().size() == 1);
	}
	
	@Test
	public void testExecuteWhenThePlayerHasBuiltAnEmporiumInAllTheCitiesWithTheSameColorOfTheSelectedCityShouldAddTwoBonusKingCardIfThereAre() {
		player.getCities().add(game.getBoard().getCities().get(12));
		assertTrue(player.getKingCards().isEmpty());
		action.execute();
		assertFalse(player.getKingCards().isEmpty());
		assertTrue(player.getKingCards().size() == 2);
	}
	
	@Test
	public void testExecuteWhenBonusKingCardAreFinishedButBonusColorsAreNotFinishedShouldAddOnlyOneKingCard() {
		player.getCities().add(game.getBoard().getCities().get(12));
		game.getBoard().getKingRegion().getDeckOfKingCard().clear();
		assertTrue(player.getKingCards().isEmpty());
		action.execute();
		assertFalse(player.getKingCards().isEmpty());
		assertTrue(player.getKingCards().size() == 1);
	}
	
	@Test
	public void testExecuteWhenTheBonusColorsAreFinishedButThereIsTheBonusKingCardShouldReturnOnlyOneKingCard() {
		player.getCities().add(game.getBoard().getCities().get(12));
		Arrays.asList(CityColor.values()).stream().forEach(color -> game.getBoard().getKingRegion().getCityColorBonusCard(color));
		assertTrue(player.getKingCards().isEmpty());
		action.execute();
		assertFalse(player.getKingCards().isEmpty());
		assertTrue(player.getKingCards().size() == 1);
	}
	
	@Test
	public void testExecuteShouldSetToUsedThePermitCardPassedAsParam() {
		assertFalse(selectedPermitCard.isUsed());
		action.execute();
		assertTrue(selectedPermitCard.isUsed());
	}
	
	@Test
	public void testExecuteWhenBuildTheFirstEmporiumInTheCityJuvelarShouldReturnAResponseWithAPrimaryActionState() {
		city = game.getBoard().getCities().get(9);
		action = new BuildEmporiumByPermitCard(player, game, city, assistantsToPay, selectedPermitCard);
		ActionResponseMessage response = action.execute();
		assertEquals(response.getState(), PrimaryActionState.getInstance());

	}
	
	@Test
	public void testExecuteWhenBuildAnEMporiumInACItyWithBonusShouldReturnAResponseWithABonusState() {
		ActionResponseMessage response = action.execute();
		assertEquals(response.getState(), BonusState.getInstance());

	}
}
