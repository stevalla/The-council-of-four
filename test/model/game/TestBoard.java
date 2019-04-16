package it.polimi.ingsw.cg32.model.game;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.After;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.track.Space;

public class TestBoard {

	Board board;
	private Set<Space> spaces;
	private List<PermitCard> permitCards;
	private int numberPlayers;
	private SimpleGraph<City, DefaultEdge> cityGraph;
	private List<Bonus> bonus;
	private List<City> cities;
	
	public TestBoard() {
		spaces = new HashSet<>();
		spaces.add(new Space(0));
		
		bonus = new ArrayList<>();
		bonus.add(new SingleBonus("VICTORYBONUS"));
		
		cities = new ArrayList<>();
		City cityCoast = new City("ARKON", RegionType.COAST, CityColor.KING, bonus);
		cities.add(cityCoast);
		
		permitCards = new ArrayList<>();
		permitCards.add(new PermitCard("21", cities, bonus));
		
		numberPlayers = 3;
		
		cityGraph = new SimpleGraph<>(DefaultEdge.class);
		cityGraph.addVertex(cityCoast);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructBoardWhenCityGraphIsNullShouldThrowANullPointerException() {
		
		board = new Board(null, spaces, permitCards, numberPlayers);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstuctWhenPermitCardsAreLessThenSixShouldReturnAnIllegalArgumentException() {

		board = new Board(cityGraph, spaces, permitCards, numberPlayers);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructBoardWhenSpacesAreNullShouldReturnAnIllegalArgumentException() {
		
		board = new Board(cityGraph, null, permitCards, numberPlayers);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructBoardWhenThereAreNotCityOfOneRegionShouldReturnAnIllegalArgumentException() {

		permitCards.add(new PermitCard("2", cities, bonus));
		permitCards.add(new PermitCard("3", cities, bonus));
		board = new Board(cityGraph, spaces, permitCards, numberPlayers);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenPermitCardsAreNullShouldThrowANullPointerException() {
		
		board = new Board(cityGraph, spaces, null, numberPlayers);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructWhenNumberOfPlayersAreLessThen2ShouldThrownAnIllegalArgumentException() {
		
		board = new Board(cityGraph, spaces, permitCards, 1);
	}
	
	@Test
	public void testConstructWhenPermitCardsAreSixOrMoreShouldCreateABoardCorrectly() {
		
		City cityMountain = new City("LYRAM", RegionType.MOUNTAIN, CityColor.BRONZE, bonus);
		City cityHill = new City("JUVELAR", RegionType.HILL, CityColor.BRONZE, bonus);
		
		cities.add(cityMountain);
		cities.add(cityHill);
		
		cityGraph.addVertex(cityMountain);
		cityGraph.addVertex(cityHill);
		
		for(int i=0; i<20; i++)
			permitCards.add(new PermitCard(Integer.toString(i), cities, bonus));
		
		board = new Board(cityGraph, spaces, permitCards, numberPlayers);
		return;
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConctructBoardWhenPermitCardsAreNotDIvisibleBy3ShouldThrowAnIllegalArgumentException() {

		board = new Board(cityGraph, spaces, permitCards, numberPlayers);
	}
	
	public void initializeParamsForBoard() {
		
		City cityMountain = new City("LYRAM", RegionType.MOUNTAIN, CityColor.BRONZE, bonus);
		City cityHill = new City("JUVELAR", RegionType.HILL, CityColor.BRONZE, bonus);
		
		cities.add(cityMountain);
		cities.add(cityHill);
		
		cityGraph.addVertex(cityMountain);
		cityGraph.addVertex(cityHill);
		
		for(int i=0; i<20; i++)
			permitCards.add(new PermitCard(Integer.toString(i), cities, bonus));
	}
	
	@Test
	public void testConstructBoardWhenPermitCardsAreDivisibleBy3ShouldPutAllPermitCardsInItsRegion() {
		
		initializeParamsForBoard();
		board = new Board(cityGraph, spaces, new ArrayList<>(permitCards), numberPlayers);

		Arrays.asList(RegionType.values()).stream()
				.forEach(r -> {
					assertTrue(CollectionUtils.isSubCollection(board.getRegions(r).getDeckOfPermitCard().getDeck(), 
							permitCards));
					assertTrue(CollectionUtils.isSubCollection(board.getRegions(r).getDeckOfPermitCard().getUsableCard(), 
							permitCards));
				});
	}
	
	@Test
	public void testIfThePermitCardsAreAllInTheireRegions() {

		initializeParamsForBoard();

		board = new Board(cityGraph, spaces, new ArrayList<>(permitCards), numberPlayers);
		
		List<PermitCard> cards = new ArrayList<>(permitCards);
		Arrays.asList(RegionType.values()).stream()
					.forEach(r -> {
						board.getRegions(r).getDeckOfPermitCard().getDeck()
								.forEach(p -> cards.remove(p));
						board.getRegions(r).getDeckOfPermitCard().getUsableCard()
								.forEach(p -> cards.remove(p));
					});

		assertTrue(cards.isEmpty());
	}
	
	@Test
	public void testGameWithTwoPlayerShouldPutFromThreeTwoNineEmporiumsAtTheStart() {
		
		List<Emporium> emporiums = new ArrayList<>();

		for(int i=0; i<100; i++) {
			cities.clear();
			permitCards.clear();
			emporiums.clear();
			
			Game game = new Game("src/main/resources/map/default.txt", 3);
			cities.addAll(game.getBoard().getCities());
			
			Arrays.asList(RegionType.values()).stream()
					.forEach(r -> {
						permitCards.addAll(game.getBoard().getRegions(r).getDeckOfPermitCard().getDeck());
						permitCards.addAll(game.getBoard().getRegions(r).getDeckOfPermitCard().getUsableCard());
					});
			
			SimpleGraph<City, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
			for(City city : cities)
				graph.addVertex(city);
			
			board = new Board(graph, spaces, permitCards, 2);
			
			board.getCities().stream().forEach(c -> emporiums.addAll(c.getEmporium()));
			
			assertTrue(emporiums.size() >= 3 && emporiums.size() <= 9);
		}

	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructBoardWhenThereIsNoCityKingShouldThroAnIllegalArgumentException() {
		
		initializeParamsForBoard();
		SimpleGraph<City, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
		for(City city : cityGraph.vertexSet())
			graph.addVertex(city);
		
		City city = cities.stream().filter(c -> c.getColor()==CityColor.KING).findFirst().orElse(null);
		graph.removeVertex(city);

		board = new Board(graph, spaces, permitCards, numberPlayers);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructBoardWhenThereIsMoreCityKingShouldThroAnIllegalArgumentException() {
		
		initializeParamsForBoard();
		SimpleGraph<City, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
		for(City city : cityGraph.vertexSet())
			graph.addVertex(city);
		
		graph.addVertex(new City("OSLUM", RegionType.COAST, CityColor.KING, bonus));

		board = new Board(graph, spaces, permitCards, numberPlayers);
	}
	
	@Test
	public void testGetterAfterInitializeBoardShouldReturnNoNullValues() {
		
		initializeParamsForBoard();
		board = new Board(cityGraph, spaces, permitCards, numberPlayers);
		
		assertNotNull(board.getAssistantsReserve());
		assertNotNull(board.getCities());
		assertNotNull(board.getCityGraph());
		assertNotNull(board.getCouncillorReserve());
		assertNotNull(board.getKing());
		assertNotNull(board.getKingRegion());
		assertNotNull(board.getNobilityTrack());
	}
	
	@Test
	public void testGetRegionsWhenParamaterIsNullShouldReturnNull() {
		
		initializeParamsForBoard();
		board = new Board(cityGraph, spaces, permitCards, numberPlayers);
		
		assertNull(board.getRegions(null));
	}
	
	@Test
	public void testGetCitiesShouldReturnTheSameCitiesOfTheCityGraph() {
		
		initializeParamsForBoard();
		board = new Board(cityGraph, spaces, permitCards, numberPlayers);
		
		assertThat(cityGraph.vertexSet(), 
			       IsIterableContainingInAnyOrder.containsInAnyOrder(board.getCities().toArray()));
	}
	
	@After
	public void clearParamsForOtherTest() {
		
		cities.clear();
		permitCards.clear();
	}

}
