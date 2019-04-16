package it.polimi.ingsw.cg32.model.card;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.color.RegionType;

public class TestPermitCard {

	PermitCard card;
	private static List<City> cities;
	private static List<Bonus> bonus;
	
	@BeforeClass
	public static void initiliazeArrayList() {
		cities = new ArrayList<>();
		bonus = new ArrayList<>();
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructAPermitCardWhenArgumentsAreNullShouldThrowANullPointerException() {
		card = new PermitCard(null, null, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructAPermitCardWhenListsAreNullShouldThrowANullPointerException() {
		card = new PermitCard("1", null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructAPermitCardWithAnEmptyListOfCitiesShouldThrowAnIllegalArgumentException() {
		List<Bonus> bonus = new ArrayList<>();
		bonus.add(new SingleBonus("bonus"));
		card = new PermitCard("1", new ArrayList<>(), bonus);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructAPermitCardWithAnEmptyListOfBonusShouldThrowAnIllegalArgumentException() {
		List<City> cities = new ArrayList<>();
		cities.add(new City("city", RegionType.COAST, CityColor.BRONZE, new ArrayList<>()));
		card = new PermitCard("1", cities, new ArrayList<>());
	}
	
	@Before
	public void initializeListOfBonusAndCities() {
		cities.add(new City("city", RegionType.COAST, CityColor.BRONZE, new ArrayList<>()));
		bonus.add(new SingleBonus("bonus"));
		card = new PermitCard("1", cities, bonus);
	}
	
	@Test
	public void testUseCardShouldTurnTrueTheFiledUsedOfTheClass() {
		boolean beforeUsed = card.isUsed();
		card.useCard();
		
		assertNotEquals(beforeUsed, card.isUsed());
	}
	
	@Test(expected = IllegalStateException.class)
	public void testUseCardWhenTheCardWasAlreadyUsedShouldThroeAnIllegalStateException() {
		
		card.useCard();
		card.useCard();
	}
	
	@Test
	public void testGetterMethodShouldReturnTheValuePassedInTheConstructor() {
		
		assertEquals(cities, card.getCity());
		assertEquals(bonus, card.getBonus());
		assertEquals("1", card.getId());
	}
}
