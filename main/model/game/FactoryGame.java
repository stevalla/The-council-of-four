package it.polimi.ingsw.cg32.model.game;

import java.io.IOException;
import java.util.List;

import it.polimi.ingsw.cg32.model.assistants.AssistantsReserve;
import it.polimi.ingsw.cg32.model.track.NobilityTrack;

/**
 * This interface offer two method to create the {@link Board} and the {@link Player}
 * for the game.
 * 
 * @author Stefano
 *
 */
public interface FactoryGame {

	/**
	 * Create the board of the game, reading a configuration file.
	 * 
	 * @param pathFile the path of the configuration file
	 * @param numberPlayers the numbers of players of the game
	 * @return the Board created
	 * @throws NullPointerException if the path file is null
	 * @throws IllegalArgumentException if number of players is minor than two
	 * @throws IOException if there is some error reading the file
	 * @see Game 
	 * @see Board 
	 * @see Player
	 */
	public Board createBoard(String pathFile, int numberPlayers);
	
	/**
	 * Create the players of the game, with the information about
	 * nobility track, assistants reserve and number of players.
	 * 
	 * @param nobilityTrack the nobility track of the game
	 * @param assistantsReserve the assistants reserve of the game
	 * @param numberPlayers the number of players of the game
	 * @return the List of players created
	 * @throws NullPointerException if nobility track or assistant reserve are null
	 * @throws IllegalArgumentException if number of players is minor than two
	 * @see Board 
	 * @see Player 
	 * @see NobilityTrack 
	 * @see AssistantsReserve
	 */
	public List<Player> createPlayers(NobilityTrack nobilityTrack, AssistantsReserve assistantsReserve, int numberPlayers);
}
