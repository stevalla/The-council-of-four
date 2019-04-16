package it.polimi.ingsw.cg32.model.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import it.polimi.ingsw.cg32.controller.GameController;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.assistants.AssistantsReserve;
import it.polimi.ingsw.cg32.model.card.BonusKingCard;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.track.CoinsTrack;
import it.polimi.ingsw.cg32.model.track.MarkerDisk;
import it.polimi.ingsw.cg32.model.track.NobilityTrack;
import it.polimi.ingsw.cg32.model.track.VictoryTrack;

/**
* This class models and manages a player.<br>
* A Player is characterized by:<br>
* An id that that is used to uniquely identify the player within the game.<br> 
* A List of {@link PoliticCard} that rapresent the {@link PoliticCard}s he currently owns.<br>
* A {@link MarkerDisk} used to manages his position in the {@link NobilityTrack}.<br>
* A {@link CoinsTrack} used to manages his current number of coins.<br>
* A {@link VictoryTrack} used to manages his current number of victory points.<br>
* A Queue of {@link Emporium} that rappresents the {@link Emporium}s he can still build.<br>
* A Queue of {@link Assistant} that rappresents the {@link Assistant}s he currently owns.<br>
* A List of {@link PermitCard} that rappresents the {@link PermitCard}s he currently owns
* A List of {@link BonusKingCard} that rapresents the {@link BonusKingCard} he won during the match.<br>
* A List of {@linkp City} that rappresents the {@link City} where he built an {@link Emporium}.<br><br>
* 
* Provides methods to initialize politicCards ({@link #initializePolitcCard()}), 
* emporiums ({@link #initializeEmporiums() and assistants (@link #initializeAssistants(AssistantsReserve)}.
* It also has 3 static method that return a specic {@link Comparator} to sort a list of Players.
* 
* @author giovanni
*
*/
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final int INITIAL_EMPORIUMS = 10;
	public static final int INITIAL_POLITICCARDS = 6;
	
	private final int id;
	private final List<PoliticCard> politicCards = new ArrayList<>();
	private final MarkerDisk markerDisk;
	private final CoinsTrack coinsTrack;
	private final VictoryTrack victoryTrack;
	private final Queue<Emporium> emporiums;
	private final Queue<Assistant> assistants;
	private final List<PermitCard> permitCards;
	private final List<BonusKingCard> kingCards;
	private final List<City> cities;
	
	
	/**
	* This is the protected constructor for Player, it's only used in 
	* {@link FactoryLoaderGame#createPlayers(NobilityTrack, AssistantsReserve, int)}.<br>
	* It initialize all the fields of the Player invoking {@link #initializeAssistants(AssistantsReserve)},
	* {@link #initializeEmporiums()}, {@link #initializePolitcCard()}.<br>
	* 
	* @param nobilityTrack the {@link NobilityTrack} of the game where he's gaming.
	* @param assistantsReserve the {@link AssistantReserve} of the game where he's gaming.
	* @param numberPlayers the number of players playing in the same game.
	* @param id the id of the Player.
	* 
	* @throws NullPointerException either if nobilityTrack or assistantReserve is null. 
	* @throws IllegalArgumentException either if the id or numberPlayers is less than 0.
	*/
	protected Player(NobilityTrack nobilityTrack, AssistantsReserve assistantsReserve, int numberPlayers, int id) {
		
		if(nobilityTrack == null || assistantsReserve == null)
			throw new NullPointerException("Impossible to instantiate a Player with a null "
					+ "NobilityTrack or AssistantReserve.");
			
		if(numberPlayers < 0 || id < 0)
			throw new IllegalArgumentException("Impossible to instantiate a Player with a"
					+ " negative id or number of players");
		
		
		this.id = id;
		initializePolitcCard();
		this.markerDisk = new MarkerDisk(nobilityTrack);
		this.coinsTrack = new CoinsTrack(id, numberPlayers);
		this.victoryTrack = new VictoryTrack();
		this.emporiums = initializeEmporiums();
		this.assistants = initializeAssistants(assistantsReserve);
		this.permitCards = new ArrayList<>();
		this.kingCards = new ArrayList<>();
		this.cities = new ArrayList<>();
	}
	
	/**
	* Get the id of the Player
	* 
	* @return id the id of the Player
	*/
	public int getId() {
		return id;
	}

	/**
	* Get the list of {@link PermitCard} owned by the Player
	* 
	* @return permitCards the list of {@link PermitCard} owned by the Player
	*/
	public List<PermitCard> getPermitCards() {
		return permitCards;
	}
	
	
	/**
	* Get the {@link BonusKingCard}s owned by the Player
	* 
	* @return KingCards the list of {@link BonusKingCard} owned by the Player
	*/
	public List<BonusKingCard> getKingCards() {
		return kingCards;
	}

	
	/**
	* Get the List of {@link City} where the player has already built an emporium
	* 
	* @return cities the List of {@link City} where the player has already built an emporium
	*/
	public List<City> getCities() {
		return cities;
	}

	
	/**
	* Get the {@link PoliticCard}s owned by the Player
	* 
	* @return politicCards the list of {@link PoliticCard} owned by the Player
	*/
	public List<PoliticCard> getPoliticCards() {
		return politicCards;
	}

	
	/**
	* Get the {@link MarkerDisk} associated with the Player
	* 
	* @return markerDisck the {@link MarkerDisk} associated with the Player
	*/
	public MarkerDisk getMarkerDisk() {
		return markerDisk;
	}

	
	/**
	* Get the {@link CoinsTrack} associated with the Player
	* 
	* @return coinsTrack the {@link CoinsTrack} associated with the Player
	*/
	public CoinsTrack getCoinsTrack() {
		return coinsTrack;
	}

	
	/**
	* Get the {@link VictoryTrack} associated with the Player
	* 
	* @return victoryTrack the {@link VictoryTrack} associated with the Player
	*/
	public VictoryTrack getVictoryTrack() {
		return victoryTrack;
	}

	
	/**
	* Get the {@link Emporium}s that a Player can still build
	* 
	* @return emporiums the Queue of the {@link Emporium} that a Player can still build
	*/
	public Queue<Emporium> getEmporiums() {
		return emporiums;
	}
	

	/**
	* Get the {@link Assistant}s owned by the Player
	* 
	* @return assistants the Queue of {@link Assistant} owned by the Player
	*/
	public Queue<Assistant> getAssistants() {
		return assistants;
	}
	
	
	/**
	* This method is used to fill the List of PoliticCard owned by the Player at the beginnig of the game.<br>
	* Each added PoliticCard is create by {@link PoliticCardPrototype#getPoliticCard()}.  <br>
	* The number of PoliticCard added depending on the value of initialPoliticCard which is set to 6. 
	* 
	* @see PoliticCard
	*/
	private void initializePolitcCard() {
		
		PoliticCardPrototype politicCardPrototype = new PoliticCardPrototype();

		for(int i=0; i<INITIAL_POLITICCARDS; i++) 
			politicCards.add(politicCardPrototype.getPoliticCard());
	}
	
	
	/**
	* This method is used to fill the Queue of Emporium owned by the Player at the beginnig of the game.<br>
	* Each Emporium adds to the Queue of the Player is construct passing to it the id of the player.<br>
	* The number of Emporium added depending on the value of initialEmporium which is set to 10. 
	* 
	* @return emporiums the Queue of Emporium owned by the player at the beginnig of the game.
	* @see Emporium
	*/
	private Queue<Emporium> initializeEmporiums() {
		Queue<Emporium> emporiumsToAdd = new LinkedList<>();
		
		for(int i=0; i<INITIAL_EMPORIUMS; i++) 
			emporiumsToAdd.add(new Emporium(this.getId()));
		
		return emporiumsToAdd;
	}
	
	/**
	* This method is used to fill the Queue of Assistant owned by the Player , at the beginning of the game,
	* with the Assistant contained in the AssistantsReserve.<br>
	* Each Assistant adds to the Queue of the Player is earlier {@link Queue#poll()} from the AssistantsReserve.<br>
	* The number of Assistant added depending on the Player's ID. 
	* 
	* @param assistantsReserve the {@link AssistantsReserve} of the {@link Board} where the player is playing
	* @return playerAssistant the Queue of Assistant owned by the player at the beginnig of the game.
	* 
	* @see Assistant
	* @see AssistantsReserve
	*/
	private Queue<Assistant> initializeAssistants(AssistantsReserve assistantsReserve){
		Queue<Assistant> playerAssistants = new LinkedList<>();
		
		for(int i = 0; i <= this.id ;i++){
			playerAssistants.add(assistantsReserve.getAssistants().poll());
		}
		
		return playerAssistants;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	/**
	* This static method is used to order the list of players comparing 
	* them by their position on the {@link NobilityTrack}.<br>
	* It's used in {@link GameController} to order the list.
	* 
	* @return a Comparator <Player>
	* 
	* @see Comparator
	*/
	public static Comparator<Player> compareForNobilityTrack() {
		return (Player one, Player two) -> 
				one.getMarkerDisk().getCurrentSpace().compareTo(two.getMarkerDisk().getCurrentSpace());
	}
	

	/**
	* This static method is used to order the list of players 
	* comparing them by their VictoryPoints.<br>
	* It's used in {@link GameController} to order the list.
	* 
	* @return a Comparator<Player>
	* 
	* @see Comparator
	*/
	public static Comparator<Player> compareForVictoryTrack() {
		return (Player one, Player two) ->
		 		one.getVictoryTrack().getPoints() - two.getVictoryTrack().getPoints();        	    
	 }
	
	   
	/**
	* This static method is used to order the list of players comparing them by 
	* their number of owned {@link PermitCard}s.<br>
	* It's used in {@link GameController} to order the list.
	* 
	* @return a Comparator<Player>
	* 
	* @see Comparator
	*/
	 public static Comparator<Player> compareForPermitCard() {
		 return (Player one, Player two) ->
				 one.getPermitCards().size() - two.getPermitCards().size();	
	 }
	 
	 
	/**
	* This static method is used to order the list of players comparing them by their total number 
	* of {@link PoliticCard} and {@link Assistant}.<br>
	* It's used in {@link GameController} to order the list.
	* 
	* @return a Comparator<Player>
	* 
	* @see Comparator
	*/
	public static Comparator<Player> compareForAssistantPoliticCard() {
		 return (Player one, Player two) ->
				 (one.getPoliticCards().size()+one.getAssistants().size()) - (two.getPoliticCards().size()+two.getAssistants().size());	
	 }

	@Override
	public String toString() {
		return "Player [id= " + id + ", politicCards= " + politicCards + ", markerDisk at space n= " + markerDisk.getCurrentSpace() + ", coinsTrack="
				+ coinsTrack.getCurrentCoins() + ", victoryTrack= " + victoryTrack.getPoints() + ", emporiums= " + emporiums.size() + ", assistants= "
				+ assistants.size() + "]";
	}

}
