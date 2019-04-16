package it.polimi.ingsw.cg32.model.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polimi.ingsw.cg32.model.assistants.AssistantsReserve;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.track.NobilityTrack;
import it.polimi.ingsw.cg32.model.track.Space;
import it.polimi.ingsw.cg32.utilities.MapIO;

/**
 * This class implements {@link FactoryGame} and offer two method to create 
 * {@link Player} and {@link Board} reading a file.<br>
 * This implementaion specifically reading a configuration file with format "txt"
 * using a static method of class {@link MapIo} that return a {@link Map} of 
 * String and {@link List} of String.<br>
 * The key of the map is one word to recognize the objects to parse. For example
 * in the key "CITY" the value should be the List of String with the information
 * to create the cities.<br>
 * To load the resources to create {@link Board} and {@link Player} it uses its
 * private method.<br>
 * <p>
 * The format of the file configuration must be in order:<br>
 * -The key of the Map one per line.<br>
 * -The world "END_KEY" after.<br>
 * -Than the blocks with the informations of the objects, in this in particular<br>
 * game we load from file cities and their connections, permit cards, bonus's name,
 * and the nobility track.<br>
 * -Every block should start with line "START_(key)", and end with line "END_(key)",
 * where key is the key of the map load at the start.<br>
 * -In a block the information about objects must be one por line<br>
 * -City line format: "name,region,color,bonus1-bonus2"<br>
 * -Connection line format: "cityToConnect,otherCityToConnect"<br>
 * -SingleBonus format: "name"<br>
 * -QuantityBonus format: "name/quantity", quantity must be a number<br>
 * -Permit card format line: "id,city1/city2,bonus1-bonus2"<br>
 * -NobilityTrack format line: "spaceNumber,bonus1-bonus2", spaceNumber must be a number<br>
 * </p>
 * 
 * @author Stefano
 * @see PermitCard 
 * @see Bonus 
 * @see SingleBonus 
 * @see QuantityBonus 
 * @see NobilityTrack 
 * @see City
 */
public class FactoryLoaderGame implements FactoryGame {
	
	private static final Logger LOGGER = Logger.getLogger(FactoryLoaderGame.class.getName());
	
	
	@Override
	public Board createBoard(String filePath, int numberPlayers) {
		
		if(filePath == null)
			throw new NullPointerException("The fila path can't be null");
		
		if(numberPlayers < 2)
			throw new IllegalArgumentException("Numnber of players can't be minor than two");
		
		Map<String, List<String>> infoString = new HashMap<>();
		
		try {
			infoString = MapIO.readFile(filePath);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error reading file in game creation");
			return null;
		}
		
		FactoryCityGraph factoryCity = new FactoryCityGraph();
		List<String> bonusName = infoString.get("BONUS");
		List<City> cities = createCity(infoString.get("CITY"), bonusName);
		SimpleGraph<City, DefaultEdge> citiesGraph = factoryCity.initializeCitiesGraph(cities, infoString.get("CONNECTION"));
		
		List<PermitCard> permitCards = new ArrayList<>(); 
		permitCards.addAll(createPermitCard(infoString.get("PERMITCARD"), cities, bonusName));
		
		return new Board(citiesGraph, createSpace(infoString.get("NOBILITYTRACK"), bonusName), permitCards, numberPlayers);
	}

	
	@Override
	public List<Player> createPlayers(NobilityTrack nobilityTrack, AssistantsReserve assistantsReserve, int numberPlayers) {
		
		if(numberPlayers < 2)
			throw new IllegalArgumentException("Numnber of players can't be minor than two");
		
		List<Player> players = new ArrayList<>();
		for(int i=0; i<numberPlayers; i++)
			players.add(new Player(nobilityTrack, assistantsReserve, numberPlayers, i));
		
		return players;
	}
	
	
	/**
	 * This method parse the strings with the information to create the permit cards
	 * and return a list of permitcards.
	 * 
	 * @param permitCardsToString the list of permit cards read from the file
	 * @param cities the list of cities of the game
	 * @param bonusName the list of bonus's name to verify the validity of bonus in permit cards
	 * @return the created list of permit cards
	 * @throws IllegalArgumentException if the number of permit cards is not divisible by 3.
	 */
	private List<PermitCard> createPermitCard(List<String> permitCardsToString, List<City> cities, List<String> bonusName) {
		
		List<PermitCard> permitCards = new ArrayList<>();
		
		for(String currentPermitCard : permitCardsToString) {
			String[] permitCard = currentPermitCard.split(",");
			String[] citiesToString = permitCard[1].split("/");
			String[] bonusToString = permitCard[2].split("-");
			
			List<City> citiesToAdd = new ArrayList<>();
			for(String currentCity : citiesToString)
				citiesToAdd.add(findCity(currentCity, cities));
			
			permitCards.add(new PermitCard(permitCard[0], citiesToAdd, createBonus(bonusToString, bonusName)));
		}
		
		if(permitCards.size() % 3 != 0)
			throw new IllegalArgumentException("The number of permit card must be divisible for 3");
		
		return permitCards;
	}
	
	
	/**
	 * This method parse the strings with the information to create the spacebonus for
	 * nobilityTrack.
	 * 
	 * @param spaceBonusToString list of strings with the information of the spaces that composed the nobilitytrack
	 * @param bonusName the list of bonus's name to verify the validity of bonus in permit cards
	 * @return a {@link Set} of spaces created
	 */
	private Set<Space> createSpace(List<String> spaceBonusToString, List<String> bonusName) {
	
		Set<Space> space = new TreeSet<>();
		
		spaceBonusToString.stream()
					.forEach( currentSpaceBonus -> {
						String[] spaceBonusString = currentSpaceBonus.split(",");

						if(spaceBonusString.length == 1)
							space.add(new Space(Integer.parseInt(spaceBonusString[0])));
						else{
							String[] infoBonus = currentSpaceBonus.substring(currentSpaceBonus.lastIndexOf(",") + 1).split("-");
							space.add(new Space(Integer.parseInt(spaceBonusString[0]), createBonus(infoBonus, bonusName)));
						}
					});
		return space;
	}
	
	
	/**
	 * This methos parse the strings with the information to create cities for the game.
	 * 
	 * @param citiesToString list of strings with the information of the cities
	 * @param bonusName the list of bonus's name to verify the validity of bonus in permit cards
	 * @return a list of the cities created
	 */
	private List<City> createCity(List<String> citiesToString, List<String> bonusName) {
		
		List<City> cities = new ArrayList<>();
		
		citiesToString.stream()
					.forEach(currentCity -> {
						String[] infoCity = currentCity.split(",");
						
						if(infoCity.length > 3) {
							String[] infoBonus = currentCity.substring(currentCity.lastIndexOf(",") + 1).split("-");
							List<Bonus> bonus = createBonus(infoBonus, bonusName);
							cities.add(new City(infoCity[0], RegionType.valueOf(infoCity[1]), CityColor.valueOf(infoCity[2]), bonus));
						}
						else
							cities.add(new City(infoCity[0], RegionType.valueOf(infoCity[1]), 
									CityColor.valueOf(infoCity[2]), Collections.emptyList()));
					});

		return cities;
	}
	
	
	/**
	 * This method parse the strings with the information about bonus for the
	 * components of the game.
	 * 
	 * @param bonusToString the list of strings with the information about bonus
	 * @param bonusName the list of bonus's name to verify the validity of bonus in permit cards
	 * @return the list of bonus created
	 * @throws IllegalArgumentException if a bonus is write wrong in the file configuration
	 * @see Bonus
	 */
	private List<Bonus> createBonus(String[] bonusToString, List<String> bonusName) {
		
		List<Bonus> bonus = new ArrayList<>();
		
		Arrays.asList(bonusToString).stream()
					.forEach(currentBonus -> {
						if(currentBonus.matches(".*[0-9].*")) {
							String[] quantityBonus = currentBonus.split("/");
							
							if(checkBonusName(bonusName, quantityBonus[0]) && quantityBonus.length == 2 
									&& quantityBonus[1].matches("\\d+"))
								bonus.add(new QuantityBonus(quantityBonus[0], Integer.parseInt(quantityBonus[1])));
							else 
								throw new IllegalArgumentException("Error in config file, "
										+ "a quantity bonus can only have a name and a quantity number.");
						}
						else {
							if(checkBonusName(bonusName, currentBonus))
								bonus.add(new SingleBonus(currentBonus));
						}
					});

		return bonus;
	}
	
	
	/**
	 * Check if the bonus to add is a valid method comparing it with the list bonus's name
	 * read from file.
	 * 
	 * @param bonusName the list of bonus's name to verify the validity of bonus in permit cards
	 * @param bonusToCheck the bonus to check
	 * @return true if the bonus is a valid bonus, otherwise throw an {@link IllegalArgumentException}
	 */
	private boolean checkBonusName(List<String> bonusName, String bonusToCheck) {
		
		if(bonusName.stream().anyMatch(bonus -> bonus.equals(bonusToCheck)))
			return true;
		else
			throw new IllegalArgumentException("Error in config file, the bonus " + bonusToCheck + " doesn't exist.");

	}
	
	
	/**
	 * Control if the name of the city is valid, so if it match with any name of
	 * the cities created.
	 * 
	 * @param cityToString the string name of the city to control its validation
	 * @param cities the list of cities 
	 * @return the city if it found, otherwise {@link IllegalArgumentException}
	 */
	private City findCity(String cityToString, List<City> cities) {
		
		return cities.stream()
					 .filter(city -> city.getName().equals(cityToString))
					 .findFirst()
					 .orElseThrow(() -> new IllegalArgumentException("One city in permitCard don't match with any city."));
	}
	
}
