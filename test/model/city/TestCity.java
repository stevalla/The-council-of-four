package it.polimi.ingsw.cg32.model.city;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.color.RegionType;

@RunWith(value = Parameterized.class)
public class TestCity {

	private static List<Bonus> listOfBonus = new ArrayList<>();
	private RegionType region;
	private CityColor cityColor;
	
	@BeforeClass
	public static void InitialContext(){
		listOfBonus.add(new QuantityBonus("ASSUSTANTS", 2));
		listOfBonus.add(new QuantityBonus("VICTORYBONUS", 4));
		listOfBonus.add(new SingleBonus("ASSUSTANTS"));
	}
	
	@Parameters(name = "{index}: regionType({0}) cityColor({1})")
	public static Iterable<Object[]> data() {
	return Arrays.asList(new Object[][] {
	{ RegionType.HILL, CityColor.BRONZE },
	{ RegionType.HILL, CityColor.GOLD },
	{ RegionType.HILL, CityColor.IRON },
	{ RegionType.HILL, CityColor.KING },
	{ RegionType.HILL, CityColor.SILVER },
	{ RegionType.COAST, CityColor.BRONZE },
	{ RegionType.COAST, CityColor.GOLD },
	{ RegionType.COAST, CityColor.IRON },
	{ RegionType.COAST, CityColor.KING },
	{ RegionType.COAST, CityColor.SILVER },
	{ RegionType.MOUNTAIN, CityColor.BRONZE },
	{ RegionType.MOUNTAIN, CityColor.GOLD },
	{ RegionType.MOUNTAIN, CityColor.IRON },
	{ RegionType.MOUNTAIN, CityColor.KING },
	{ RegionType.MOUNTAIN, CityColor.SILVER }
	});
	}
	
	public TestCity(RegionType region, CityColor cityColor) {
		this.cityColor = cityColor;
		this.region = region;
	}
	
	@Test (expected=NullPointerException.class)
	public void instatiateACityWithNameNullShouldThrowsNullPointerException() {

		new City(null, region, cityColor, listOfBonus);
	}
	
	@Test (expected=NullPointerException.class)
	public void instatiateACityWithAListOfBonusNullShouldThrowsNullPointerException() {
		
		new City("NAME", region, cityColor, null);
	}
	
	@Test 
	public void aCityShouldHasSetOfEmporiumEmptyWhenCreated() {

		City city = new City("NAME", region, cityColor, listOfBonus);
		assertEquals(0, city.getEmporium().size());
	}
	
	@Test 
	public void getNameShouldReturnTheNameOfTheCity() {
		
		City city = new City("NAME", region, cityColor, listOfBonus);
		assertEquals("NAME", city.getName());
	}
	
	@Test 
	public void getRegionShouldReturnTheKindOfTheRegion() {
		
		City city = new City("NAME", region, cityColor, listOfBonus);
		assertEquals(region, city.getRegion());
	}
	
	@Test 
	public void getColorShouldReturnTheKindOfThecColorOfTheCity() {
		
		City city = new City("NAME", region, cityColor, listOfBonus);
		assertEquals(cityColor, city.getColor());
	}
	
	@Test 
	public void getBonusShouldReturnTheListOfBonusOfTheRegion() {
		
		City city = new City("NAME", region, cityColor, listOfBonus);
		
		Assert.assertThat(city.getBonus(), 
			       IsIterableContainingInOrder.contains(listOfBonus.toArray()));
	}
	
	@Test 
	public void addEmporiumNotNullShouldIncrementSizeOfSetAndAddThattEmporiumToTheSet() {
		
		City city = new City("NAME", region, cityColor, listOfBonus);
		Emporium emporium = new Emporium(3);
		city.addEmporium(emporium);
		assertEquals(1, city.getEmporium().size());
		assertTrue(city.getEmporium().contains(emporium));
	}
	
	@Test (expected=NullPointerException.class)
	public void addEmporiumNullShouldThrowsNullPointerException() {
		
		City city = new City("NAME", region, cityColor, listOfBonus);
		Emporium emporium = null;
		city.addEmporium(emporium);
		assertEquals(0, city.getEmporium().size());
	}
	
	@Test 
	public void addEmporiumNotNullThatIsAlreadyPresentInTheSetShouldDoNothing() {
		
		City city = new City("NAME", region, cityColor, listOfBonus);
		Emporium emporium = new Emporium(3);
		city.addEmporium(emporium);
		assertEquals(1, city.getEmporium().size());
		assertTrue(city.getEmporium().contains(emporium));
		city.addEmporium(emporium);
		assertEquals(1, city.getEmporium().size());
		assertTrue(city.getEmporium().contains(emporium));
	}
	

	
}
