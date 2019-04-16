package it.polimi.ingsw.cg32.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polimi.ingsw.cg32.model.King;
import it.polimi.ingsw.cg32.model.assistants.AssistantsReserve;
import it.polimi.ingsw.cg32.model.balcony.Balcony;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.balcony.CouncillorReserve;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.region.KingRegion;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.model.region.Region;
import it.polimi.ingsw.cg32.model.track.NobilityTrack;
import it.polimi.ingsw.cg32.model.track.Space;

/**
 * This class rapresent the board of the game and it contains all the components.
 * The only way to get a Board is to use {@link FactoryLoaderGame}.
 * <p>
 * A board contains the {@link AssistantsReserve} and the {@link CouncillorReserve},<br>
 * four {@link Region}: king's region and the other three normal region that composed the board,<br> 
 * a {@link SimpleGraph} with cities and connections,<br>
 * the {@link King} of the game and finally the {@link NobilityTrack}.<br> 
 * Furthermore the Board full fill the region's balconies and divides the permit cards
 * in three decks before give it to regions.
 * </p>
 * In board creation, if number of players is two, will be add emporiums in random 
 * some {@link City} to comply with game's rules.<br>
 * Implement {@link Serializable}
 *  
 * @author Stefano
 *
 */
public class Board implements Serializable {

	private static final long serialVersionUID = 1L;
	private final CouncillorReserve councillorReserve;
	private final AssistantsReserve assistantsReserve;
	private final EnumMap<RegionType, NormalRegion> regions;
	private final KingRegion kingRegion;
	private final SimpleGraph<City, DefaultEdge> cityGraph;
	private final King king;
	private final NobilityTrack nobilityTrack;

	
	/**
	 * Construct a board, divide the permit cards in three deck, one for each region,
	 * initilize {@link Balcony} of each region, create the {@link CouncillorReserve}
	 * and the {@link AssistantsReserve}, the {@link KingRegion} and the {@link NobilityTrack}
	 * <p>
	 * If number of players is two, it will be add a number of emporium in random cities
	 * between three and nine, to comply with game's rules.
	 * </p>
	 * 
	 * @param cityGraph the {@link SimpleGraph} of game's cities with their connections
	 * @param space the {@link Space} that form the {@link NobilityTrack}
	 * @param permitCards all game's permit cards
	 * @param numberPlayers the number of the players in this game
	 * @throws NullPointerException if cityGraph and permitcard are null
	 * @throws IllegalArgumentException if spaces are null or if the permit card
	 * 		   size is not divisible by three or if number of players is minor than 2
	 * @see PermitCard 
	 * @see Region 
	 * @see DeckOfPermitCard 
	 * @see Player
	 */
	protected Board(SimpleGraph<City, DefaultEdge> cityGraph, Set<Space> space, 
			List<PermitCard> permitCards, int numberPlayers) {
		
		if(cityGraph == null || permitCards == null)
			throw new NullPointerException("Cities graph and permit cards can't be null");
		
		if(permitCards.size()%3 != 0)
			throw new IllegalArgumentException("The deck of all permit cards must be divisible by 3");
		
		this.cityGraph = cityGraph;
		this.councillorReserve = new CouncillorReserve();
		this.assistantsReserve = new AssistantsReserve(numberPlayers);
		
		this.regions = new EnumMap<>(RegionType.class);
		
		Collections.shuffle(permitCards);
		int deckSize = permitCards.size()/3;
		
		for(RegionType region : RegionType.values()){
			List<PermitCard> permitCardsToAdd = new ArrayList<>(permitCards.subList(0, deckSize));
			permitCards.removeAll(permitCardsToAdd);
			this.regions.put(region, new NormalRegion(region, getCities(), permitCardsToAdd));
		}
		
		this.kingRegion = new KingRegion();
		
		if(numberPlayers == 2)
			initializeGameForTwoPlayers();
		
		regions.values().stream().forEach(this::initializeBalconies);
		initializeBalconies(kingRegion);
		
		this.king = initializeKing(getCities());
		
		this.nobilityTrack = new NobilityTrack(space);
	
	}
	
	/**
	 * Search in the {@link EnumMap} of RegionType
	 * and Region, return null if there is not the RegionType in the map.
	 * 
	 * @param regionType the RegionType fo the Region
	 * @return the Region with this RegionType
	 * @see Region 
	 * @see RegionType
	 */
	public NormalRegion getRegions(RegionType regionType) {
		return regions.get(regionType);
	}

	/**
	 * @return the {@link KingRegion} of the game
	 */
	public KingRegion getKingRegion() {
		return kingRegion;
	}

	/**
	 * @return a {@link List} of all the game's {@link City}
	 */
	public List<City> getCities() {

		return new ArrayList<>(this.cityGraph.vertexSet());
	}

	/**
	 * @return the {@link SimpleGrap} of all the game's {@link City}
	 */
	public SimpleGraph<City, DefaultEdge> getCityGraph() {
		
		return cityGraph;
	}

	/**
	 * @return the {@link CouncillorReserve} of the game. 
	 */
	public CouncillorReserve getCouncillorReserve() {
		
		return councillorReserve;
	}

	/**
	 * @return the {@link King} of the game
	 */
	public King getKing() {
		
		return king;
	}

	/**
	 * @return the {@link NobilityTrack} of the game
	 */
	public NobilityTrack getNobilityTrack() {
		return nobilityTrack;
	}
	

	/**
	 * @return the {@link AssistantsReserve} of the game
	 */
	public AssistantsReserve getAssistantsReserve() {
		
		return assistantsReserve;
	}
	
	
	/**
	 * @return the String of the connections city.
	 */
	public String getConnectionToString() {
		StringBuilder sb = new StringBuilder();
		cityGraph.edgeSet().stream().forEach(e -> sb.append(cityGraph.getEdgeSource(e).getName() + 
				" --> " + cityGraph.getEdgeTarget(e).getName() + "\n"));
		 
		return sb.toString();
	}

	/**
	 * Search in the cities where is the city with {@link CityColor} "KING"
	 * and put it the king, then return the king.
	 * Remember that every game can have only one King City.
	 * 
	 * @param cities all the game's {@link City}
	 * @return the {@link King} of the game
	 * @throws IllegalArgumentException if the cities with the citycolor "KING" 
	 * 		   are zero or more than one.
	 */
	private King initializeKing(List<City> cities) {
		
		if(cities.stream().filter(c -> c.getColor() == CityColor.KING).count() != 1)
			throw new IllegalArgumentException("There must be only a city of kings and not zero.");
		 
		return new King(cities.stream().filter(c -> c.getColor() == CityColor.KING)
					   					.findFirst().orElse(null));
	}
	
	/**
	 * Initialize {@link Balcony} and put it 4 {@link Councillor}
	 * 
	 * @param region the {@link Region} with the {@link Balcony} to initialize
	 */
	private void initializeBalconies(Region region){
		
		final int numberOfSeats = 4;
		
		for(int i = 0; i < numberOfSeats; i++){
			region.getBalcony().push(councillorReserve.getCouncillorInReserve().remove(0));
		}	
	}
	
	/**
	 * Take, when players in game are two, three permit cards from all game's 
	 * permit cards and builds {@link Emporium} in each {@link City} of the
	 * selected permit cards.
	 * 
	 * @see PermitCard
	 */
	private void initializeGameForTwoPlayers() {
		List<PermitCard> permitCards = new ArrayList<>();
		regions.values().stream().forEach(r -> permitCards.add(r.getDeckOfPermitCard().drawRandomPermitCard()));
		
		permitCards.stream().forEach(p -> {
			for(City city : p.getCity()) {
				city.addEmporium(new Emporium(2));
			}
		});
	}

	@Override
	public String toString() {
		return "Board [councillorReserve=" + councillorReserve + ", assistantsReserve=" + assistantsReserve
				+ ", regions=" + regions + ", kingRegion=" + kingRegion + ", cityGraph=" + cityGraph + ", king=" + king
				+ ", nobilityTrack=" + nobilityTrack + "]";
	}

	
}
