package it.polimi.ingsw.cg32.model.game;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.utilities.MapIO;

public class TestFactoryCityGraph {

	FactoryCityGraph factoryCity;
	private List<String> connection;
	private List<City> cities;
	
	@Before
	public void initializeParams() {
		
		factoryCity = new FactoryCityGraph();
		cities = new ArrayList<>();
		Map<String, List<String>> infoString = new HashMap<>();
		
		try {
			infoString = MapIO.readFile("src/main/resources/map/default.txt");
		} catch (IOException e) {}
		
		connection = infoString.get("CONNECTION");
		cities = new Game("src/main/resources/map/default.txt", 3).getBoard().getCities();
	}
	
	@Test(expected = NullPointerException.class)
	public void testCreateConstructorWhenListOfCitiesIsNullShouldThrowNullPointerException() {

		factoryCity.initializeCitiesGraph(null, connection);
	}
	
	@Test(expected = NullPointerException.class)
	public void testCreateConstructorWhenListOfStringConnectionsIsNullShouldThrowNullPointerException() {

		factoryCity.initializeCitiesGraph(cities, null);
	}
	
	@Test
	public void testCreateConstructorWhenListOfStringConnectionsIsEmptyShouldGoCorrectly() {

		factoryCity.initializeCitiesGraph(cities, new ArrayList<>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateConstructorWhenListOfCitiesIsEmptyShouldReturnAnIllegalArgumentException() {

		factoryCity.initializeCitiesGraph(new ArrayList<>(), connection);
	}
	
	@Test
	public void testInitializeCityGraphShouldCreateAGraphWithAllTheCitiesPassedAsParam() {
		
		SimpleGraph<City, DefaultEdge> graph = factoryCity.initializeCitiesGraph(cities, connection);
		
		Assert.assertThat(cities, IsIterableContainingInAnyOrder.containsInAnyOrder(graph.vertexSet().toArray()));
	}
	
	@Test
	public void testInitializeCityGraphShouldCreateOneConnectionForEveryStringInConnectionList() {
		
		SimpleGraph<City, DefaultEdge> graph = factoryCity.initializeCitiesGraph(cities, connection);
		
		assertTrue(graph.edgeSet().size() == connection.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInitializeCityGraphWhenOneOrMoreConnectionHaveAnIncorrectCityShouldThrowAnIllegalArgumentException() {
		
		connection.add(new String("test"));
		
		factoryCity.initializeCitiesGraph(cities, connection);
	}
}
