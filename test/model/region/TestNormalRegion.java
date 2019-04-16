package it.polimi.ingsw.cg32.model.region;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.BonusKingCard;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.color.RegionType;

public class TestNormalRegion {

	NormalRegion region;
	private List<Bonus> bonus;
	private List<PermitCard> cards;
	private List<City> cities;
	private BonusKingCard card;
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhereParamsAreNullShouldThrowANullPOinterException() {
		
		region = new NormalRegion(null, null, null);
	}
	
	@Before
	public void initializeParamsForConstructANormalRegion() {
		bonus = new ArrayList<>();
		bonus.add(new SingleBonus("bonus"));
		
		cities = new ArrayList<>();
		cities.add(new City("city", RegionType.COAST, CityColor.BRONZE, bonus));
		
		cards = new ArrayList<>();
		for(int i=0; i<10; i++)
			cards.add(new PermitCard(Integer.toString(i), cities, bonus));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhereRegionIsNullShouldThrowANullPOinterException() {
		
		region = new NormalRegion(null, cities, cards);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhereCitiesIsNullShouldThrowANullPOinterException() {
		
		region = new NormalRegion(RegionType.COAST, null, cards);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWherePermtiCardsIsShouldThrowANullPOinterException() {
		
		region = new NormalRegion(RegionType.COAST, cities, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructWhereBonusListIsEmptyShouldThrowANullPOinterException() {
		
		region = new NormalRegion(RegionType.COAST, cities, new ArrayList<>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructWherePermitCardListIsEmptyShouldThrowANullPOinterException() {
		
		region = new NormalRegion(RegionType.COAST, new ArrayList<>(), cards);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructWhenThereIsNotCitiesInTheRegionShouldThrowAnIllegalArgumentException() {

		region = new NormalRegion(RegionType.MOUNTAIN, cities, cards);
	}
	
	@Test
	public void testConstructThatSelectTheCitiesOfHisRegionFromAListSoAllSelectedCitiesMustHaveTheSameRegionThatTheRegion() {
		
		for(int i=0; i<10; i++) {
			if(i%2==0)
				cities.add(new City("city", RegionType.COAST, CityColor.BRONZE, bonus));
			else
				cities.add(new City("city", RegionType.MOUNTAIN, CityColor.BRONZE, bonus));
		}
		
		region = new NormalRegion(RegionType.MOUNTAIN, cities, cards);
		
		region.getCities().stream()
				.forEach(c -> assertTrue(region.getRegionType()==c.getRegion()));
		
	}
	
	@Test
	public void testConstructThatSelectTheCitiesOfHisRegionFromAListSoTheSizeOfListInRegionMustBeTheSameThatTheSubListOfAllCities() {
		
		for(int i=0; i<10; i++) {
			if(i%2==0)
				cities.add(new City("city", RegionType.COAST, CityColor.BRONZE, bonus));
			else
				cities.add(new City("city", RegionType.MOUNTAIN, CityColor.BRONZE, bonus));
		}
		
		region = new NormalRegion(RegionType.MOUNTAIN, cities, cards);
		
		List<City> subList = new ArrayList<>();
		cities.stream()
				.filter(c -> c.getRegion()==RegionType.MOUNTAIN)
				.forEach(subList::add);
		
		Assert.assertThat(region.getCities(), IsIterableContainingInAnyOrder.containsInAnyOrder(subList.toArray()));
	}
	
	@Before
	public void useBonusKingCard() {
		
		region = new NormalRegion(RegionType.COAST, cities, cards);
		
		card = region.getBonusRegionCard();
	}
	
	@Test
	public void testGetBonusRegionCardMustReturnABonusKingCardWithAVictoryBonus() {
		
		assertEquals("VICTORYBONUS", card.getBonus().getName());
	}
	
	@Test
	public void testGetBonusRegionCardMustReturnNullTheSecondTimeThatCallIt() {

		assertNull(region.getBonusRegionCard());
	}
}
