package it.polimi.ingsw.cg32.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.game.Game;

public class TestKing {

	private static List<City> cities;
	
	@BeforeClass
	public static void initialContext() {
		Game game = new Game("src/main/resources/map/default.txt", 4);
		cities = game.getBoard().getCities();
	}
	
	
	@Test(expected=NullPointerException.class)
	public void creatingAKingWithACityNullShouldThrowANullPointerException() {
		new King(null);
	}
	
	@Test(expected=NullPointerException.class)
	public void PassACityThatIsNullToSetCityShouldThrowANullPointerException() {
		King king = new King(cities.get(0));
		king.setCity(null);
	}
	
	@Test
	public void KingCreatingWithACityNotNullShouldBeCreatedAndSituatedInTheTargetCity() {
		for(City city : cities){
			King king = new King(city);
			assertEquals(city, king.getCity());
		}
	}
	
	@Test
	public void setCityShouldsetCurrenCityToTheTargetCity() {
		
		King king = new King(cities.get(0));
		for(City city : cities){
			king.setCity(city);
			assertEquals(city, king.getCity());
		}
	}
	
	@Test
	public void getCityShouldReturnCurrenCityAndNotOterCity() {
		
		King king = new King(cities.get(0));
		assertEquals(king.getCity(), cities.get(0));
		for(int i = 1; i<cities.size(); i++){
			assertThat(king.getCity(), is(not(cities.get(i))));
		}
	}
	

}
