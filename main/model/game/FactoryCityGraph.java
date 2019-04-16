package it.polimi.ingsw.cg32.model.game;

import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polimi.ingsw.cg32.model.city.City;

/**
 * This class create a {@link SimpleGraph} of {@link City}.
 * It has only one method that accept for arguments, the {@link List}
 * of game's city, and a {@link List} of {@link String}.<br>
 * <p>
 * The List of String must be write with one connection for each line
 * of the configuration file, and every line must be like "CITY1,CITY2"
 * to not catch {@link IllegalArgumentException}.<br>
 * </p>
 * 
 * @author Stefano
 *
 */
public class FactoryCityGraph {
	
	/**
	 * Create a SimpleGraph of the cities passed as param and the connection
	 * write in the second param List of String.
	 * The graph's vertex are the cities, the graph's edges are the {@link DefaultEdge}.
	 * 
	 * @param cities The list of {@link City} of the game.
	 * @param connectionToString the String of connection between cities 
	 * @return A SimpleGraph of all the cities with their connection.
	 * @throws NullPointerException if arguments are null
	 * @throws IllegalArgumentException if the List of connections to string have wrong city's name
	 * @see SimpleGraph 
	 * @see City
	 */
	public SimpleGraph<City, DefaultEdge> initializeCitiesGraph(List<City> cities, List<String> connectionToString) {
		
		if(cities == null || connectionToString == null)
			throw new NullPointerException("The List of cities can't be null");
		
		SimpleGraph<City, DefaultEdge> simpleGraph = new SimpleGraph<>(DefaultEdge.class);
		
		cities.stream().forEach(simpleGraph::addVertex);

		simpleGraph = createConnectionGraph(simpleGraph, connectionToString);
		
		return simpleGraph;
		
	}
	
	
	/**
	 * This method parse the list of connection, than add to the graph the relative connection and
	 * finally return the graph.
	 * 
	 * @param simpleGraph the SimpleGraph of the cities
	 * @param connectionToString the list of connections to string
	 * @return the SimpleGraph with the connection
	 * @throws IllegalArgumentException if the List of connections to string have wrong city's name
	 * @see SimpleGraph 
	 * @see City
	 */
	private SimpleGraph<City, DefaultEdge> createConnectionGraph(SimpleGraph<City, DefaultEdge> simpleGraph, 
				List<String> connectionToString) {
		
		connectionToString.stream()
						  .forEach(string -> {
							  	String[] connections = string.split(",");
								City city = findCity(simpleGraph.vertexSet(), connections[0]);
								City city2 = findCity(simpleGraph.vertexSet(), connections[1]);
								simpleGraph.addEdge(city, city2);
						  });
		
		return simpleGraph;
	}
	
	
	/**
	 * Find a {@link City} with the same name of the second param, if don't found
	 * return an IllegalArgumentException.
	 * 
	 * @param cities A {@link Set} of all the cities of the {@link SimpleGraph}
	 * @param name the name of the {@link City} to compare with the name of the cities in the Set
	 * @return the City with the name equals to the param, or an {@link IllegalArgumentException} 
	 * 		   if the name doasn't match with any name of the cities graph
	 */
	private City findCity(Set<City> cities, String name) {
		return cities.stream()
				.filter(c -> c.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("One name in connections don't match with any city."));
	}
	
}
