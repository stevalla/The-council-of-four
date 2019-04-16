package it.polimi.ingsw.cg32.model.game;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByKingHelp;
import it.polimi.ingsw.cg32.model.card.BonusKingCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.track.CoinsTrack;
import it.polimi.ingsw.cg32.model.track.VictoryTrack;

public class TestPlayer {
	
	public Game game;
	
	@Before
	public void initTest(){
		game = new Game("src/main/resources/map/default.txt", 4);
		
	}
	
	
	@Test (expected=NullPointerException.class)
	public void instantiateAPlayerWithNobilityTrackNullShoudThrowNullPointerException(){
	
		new Player(null, game.getBoard().getAssistantsReserve(), 4, 0);
	}
	
	@Test (expected=NullPointerException.class)
	public void instantiateAPlayerWithAssistantReserveNullShoudThrowNullPointerException(){
	
		new Player(game.getBoard().getNobilityTrack(), null, 4, 0);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void instantiateAPlayerWithIdMinorThanShoudThrowIllegalArgumentException(){
	
		new Player(game.getBoard().getNobilityTrack(), game.getBoard().getAssistantsReserve(), 4, -5);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void instantiateAPlayerWithNumberOfPlayersMinorThanShoudThrowIllegalArgumentException(){
	
		new Player(game.getBoard().getNobilityTrack(),game.getBoard().getAssistantsReserve(), -2, 3);
	}
	
	@Test
	public void aPlayerInstantiateWithValidsArgumentsShouldBeCreatedHowRequested(){
	
		Player player = new Player(game.getBoard().getNobilityTrack(),game.getBoard().getAssistantsReserve(), 4, 0);
		assertEquals(player.getId(), 0);
		assertEquals(player.getId() + 1 , player.getAssistants().size());
		assertEquals(player.getMarkerDisk().getCurrentSpace().getSpaceNumber(), 0);
		player.getCoinsTrack();
		assertEquals(player.getCoinsTrack().getCurrentCoins(), CoinsTrack.INFERIOR_COINS_START_THRESHOLD + player.getId());
		assertEquals(0, player.getVictoryTrack().getPoints());
		assertEquals(10, player.getEmporiums().size());
		assertEquals(0, player.getEmporiums().peek().getPlayerId());
		Assert.assertTrue(player.getPermitCards().isEmpty());
		Assert.assertTrue(player.getKingCards().isEmpty());
		Assert.assertTrue(player.getCities().isEmpty());
		assertEquals(6, player.getPoliticCards().size());
	}
	
	@Test
	public void sortingAListOfPlayersUsingCompareForNobilityTrackShouldCorrectlyOrderIt(){
		
		game.getPlayers().get(0).getMarkerDisk().nextCurrentSpace();
		game.getPlayers().get(0).getMarkerDisk().nextCurrentSpace();
		game.getPlayers().get(0).getMarkerDisk().nextCurrentSpace();
		game.getPlayers().get(1).getMarkerDisk().nextCurrentSpace();
		game.getPlayers().get(2).getMarkerDisk().nextCurrentSpace();
		game.getPlayers().get(2).getMarkerDisk().nextCurrentSpace();
		game.getPlayers().get(3).getMarkerDisk().nextCurrentSpace();
		game.getPlayers().get(1).getMarkerDisk().nextCurrentSpace();
		game.getPlayers().get(2).getMarkerDisk().nextCurrentSpace();
		game.getPlayers().get(0).getMarkerDisk().nextCurrentSpace();
		
		assertEquals(game.getPlayers().get(0), Collections.max(game.getPlayers(), Player.compareForNobilityTrack()));
	}
	
	@Test
	public void sortingAListOfPlayersUsingCompareForVictoryTrackShouldCorrectlyOrderIt(){
		
		game.getPlayers().get(0).getVictoryTrack().setPoints(5);
		game.getPlayers().get(0).getVictoryTrack().setPoints(1);
		game.getPlayers().get(0).getVictoryTrack().setPoints(2);
		game.getPlayers().get(1).getVictoryTrack().setPoints(1);
		game.getPlayers().get(2).getVictoryTrack().setPoints(2);
		game.getPlayers().get(3).getVictoryTrack().setPoints(5);
		
		assertEquals(game.getPlayers().get(0), Collections.max(game.getPlayers(), Player.compareForVictoryTrack()));
	}
	
	@Test
	public void sortingAListOfPlayersUsingCompareForPermitCardShouldCorrectlyOrderIt(){
		
		game.getPlayers().get(0).getPermitCards().add(game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0));
		
		assertEquals(game.getPlayers().get(0), Collections.max(game.getPlayers(), Player.compareForPermitCard()));
	}
	
	
	@Test
	public void sortingAListOfPlayersUsingCompareForAssistantPoliticCardShouldCorrectlyOrderIt(){
		
		game.getPlayers().get(0).getPermitCards().add(game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0));
		
		assertEquals(game.getPlayers().get(3), Collections.max(game.getPlayers(), Player.compareForAssistantPoliticCard()));
	}
	
	@Test
	public void testGetKingCardsShouldReturnTheKingCardCurrentlyOwnedByThePlayer(){
		
		List<BonusKingCard> list = new ArrayList<>();
		BonusKingCard card1 = game.getBoard().getKingRegion().drawKingCard();
		BonusKingCard card2 = game.getBoard().getKingRegion().drawKingCard();
		list.add(card1);
		list.add(card2);
		game.getPlayers().get(0).getKingCards().add(card1);
		game.getPlayers().get(0).getKingCards().add(card2);
		
		Assert.assertThat(game.getPlayers().get(0).getKingCards(), 
			       IsIterableContainingInOrder.contains(list.toArray()));
	}
	
	@Test
	public void tetsGetIdShouldReturnTheIdOfThePlayer(){
		
		assertEquals(0, game.getPlayers().get(0).getId());
	}
	
	@Test
	public void testGetPermitCardsShouldReturnTheListOfPermitCardCurrentlyOwnedByThePlayer(){
		Game game = new Game("src/main/resources/map/default.txt", 4);
		assertEquals(game.getPlayers().get(0).getMarkerDisk().getCurrentSpace(), game.getBoard().getNobilityTrack().getTrack().first());
				
	}
	
	@Test
	public void tetstGetPoliticCardsShouldReturnTheListOfPoliticCardCurrentlyOwnedByThePlayer(){
		
		game.getPlayers().get(1).getPoliticCards().clear();
		
		PoliticCard politicCard1 = new PoliticCardPrototype().getPoliticCard();
		PoliticCard politicCard2 = new PoliticCardPrototype().getPoliticCard();
		
		List<PoliticCard> list = new ArrayList<>();
		list.add(politicCard1);
		list.add(politicCard2);
		game.getPlayers().get(1).getPoliticCards().add(politicCard1);
		game.getPlayers().get(1).getPoliticCards().add(politicCard2);
		
		Assert.assertThat(game.getPlayers().get(1).getPoliticCards(), 
			       IsIterableContainingInOrder.contains(list.toArray()));
		
	}
	
	@Test
	public void tetsGetMarkerDiskShouldReturnTheMarkerdiskOwnedByThePlayer(){
		
		Game gameForMarkerDisk = new Game("src/main/resources/map/default.txt", 4);
		assertEquals(gameForMarkerDisk.getPlayers().get(0).getMarkerDisk().getCurrentSpace(), gameForMarkerDisk.getBoard().getNobilityTrack().getTrack().first());
		
		for(int i = 0; i < 20; i++)
			gameForMarkerDisk.getPlayers().get(0).getMarkerDisk().nextCurrentSpace();
		
		assertEquals(gameForMarkerDisk.getPlayers().get(0).getMarkerDisk().getCurrentSpace().getSpaceNumber(), gameForMarkerDisk.getBoard().getNobilityTrack().getTrack().last().getSpaceNumber());
	}
	
	@Test
	public void testGetAssistantShouldReturnTheQueueOfAssistantOfThePlayer(){
		
		assertEquals(1, game.getPlayers().get(0).getAssistants().size());
		
	}
	
	@Test
	public void testGetEmporiumShouldReturnTheQueueOfEmporiumOfThePlayer(){
		
		Game gameForEmporium = new Game("src/main/resources/map/default.txt", 4);
		assertEquals(10, gameForEmporium.getPlayers().get(0).getEmporiums().size());
		
		gameForEmporium.getPlayers().get(0).getEmporiums().remove();
		
		assertEquals(9, gameForEmporium.getPlayers().get(0).getEmporiums().size());
	}
	
	@Test
	public void testGetVictoryTrackShouldReturnTheVictoryTarckOfThePlayer(){
		
		VictoryTrack targetVictoryTrack = game.getPlayers().get(0).getVictoryTrack();
		targetVictoryTrack.setPoints(3);
		assertEquals(3, game.getPlayers().get(0).getVictoryTrack().getPoints());
		
	}
	
	@Test
	public void testGetCoinsTrackShouldReturnTheVictoryTarckOfThePlayer(){
		
		CoinsTrack targetCoinsTrack = game.getPlayers().get(0).getCoinsTrack();
		targetCoinsTrack.setCoinsNumber(3);
		assertEquals(13, game.getPlayers().get(0).getCoinsTrack().getCurrentCoins());
		
	}
	
	@Test
	public void testGetCityShouldReturnTheCityWhereThePlayerBuiltAnEmporum(){
		
		BuildEmporiumByKingHelp buildEmporium = new BuildEmporiumByKingHelp(game.getPlayers().get(0), 
				game, game.getBoard().getCities().get(8), 0, 0, new ArrayList<>(game.getPlayers().get(0).getPoliticCards()));
		
		buildEmporium.execute();
		
		Assert.assertTrue(CollectionUtils.isSubCollection(game.getPlayers().get(0).getCities(), game.getBoard().getCities()));
		
		List<City> cities = new ArrayList<>();
		cities.add(game.getBoard().getCities().get(8));
		
		Assert.assertThat(game.getPlayers().get(0).getCities(), 
			       IsIterableContainingInOrder.contains(cities.toArray()));
	}
	
	
}