package it.polimi.ingsw.cg32.model.region;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.cg32.model.card.BonusKingCard;
import it.polimi.ingsw.cg32.model.color.CityColor;

public class TestKingRegion {

	KingRegion region;
	
	@Test
	public void testDrawAKingCardShouldReturnABonusKingCardWithAVictoryBonus() {
		
		region = new KingRegion();
		
		assertTrue(region.drawKingCard().getBonus().getName().equals("VICTORYBONUS"));
	}
	
	@Test
	public void testDrawAKingCardWhenTheDeckIsEmptyShouldReturnNull() {
		
		region = new KingRegion();
		region.getDeckOfKingCard().clear();
		
		assertNull(region.drawKingCard());
	}
	
	@Test
	public void testConstructKingRegionShouldHaveADeckOfFiveBonusKingCard() {
		
		region = new KingRegion();

		assertEquals(5, region.getDeckOfKingCard().size());
		
	}
	
	@Test
	public void testGetCityColorBonusCardWithArgumentNullShouldReturnNull() {
		
		region = new KingRegion();
		
		assertNull(region.getCityColorBonusCard(null));
	}
	
	@Test
	public void testGetCityColorBonusCardShouldReturnOnlyOneCardForEachCityColor() {
		
		region = new KingRegion();
		List<BonusKingCard> cards = new ArrayList<>();
		Arrays.asList(CityColor.values()).stream()
					.forEach(color -> cards.add(region.getCityColorBonusCard(color)));
		
		assertEquals(5, cards.size());
	}
	
	@Test
	public void testGetCityColorBonusCardTryToTakeTheSameCardTwiceShouldReturnNull(){
		
		region = new KingRegion();
		
		region.getCityColorBonusCard(CityColor.BRONZE);
		assertNull(region.getCityColorBonusCard(CityColor.BRONZE));
	}
}
