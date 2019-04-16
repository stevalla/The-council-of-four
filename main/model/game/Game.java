package it.polimi.ingsw.cg32.model.game;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class rapresent the Model of the game. It have a mutable property {@link Board}
 * and a immutable List of {@link Player}. The constructor of this class use a {@link FactoryLoaderGame}
 * to create board and players with the correct map and number of players.<br>
 * The property {@link Board} is mutable to permit reset in client side after an update.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public class Game implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Board board;
	private final List<Player> players;
	private Map<Token, ShoppingCart> store;
			
	/**
	 * This constructor use a {@link FactoryLoaderGame} to build the game and
	 * create the {@link Board} and the {@link List} of {@link Player}.
	 * 
	 * @param file the file txt with the map for this game
	 * @param numberPlayers the n of players for this game
	 * @throws NullPointerException if file is null
	 * @throws IllegalAregumentException if number of players is minor than 2 or if there is
	 * 		   some error in board creation
	 */
	public Game(String file, int numberPlayers) {
		if(file == null)
			throw new NullPointerException("The argumenti String can't be null");
		if(numberPlayers<2)
			throw new IllegalArgumentException("The number of players can't be minor than 2");
		
		FactoryLoaderGame factoryLoaderGame = new FactoryLoaderGame();
		this.board = factoryLoaderGame.createBoard(file, numberPlayers);
		if(board == null)
			throw new IllegalArgumentException("Error in board creation");
		
		this.players = factoryLoaderGame.createPlayers(board.getNobilityTrack(), board.getAssistantsReserve(), numberPlayers);
		this.store = new HashMap<>();
	}

	/**
	 * To reset the {@link Board} in client side after an update of the model.
	 * 
	 * @param board the new {@link Board}
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * @return the actual {@link Board} of the game.
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @return the {@link List} of {@link Player} of the game
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @return the shoppingCarts
	 */
	public Map<Token, ShoppingCart> getStore() {
		return store;
	}

	/**
	 * @param shoppingCarts the shoppingCarts to set
	 */
	public void setStore(Map<Token, ShoppingCart> store) {
		this.store = store;
	}
	
	/**
	* this method is use to return a formatted string screenshot of the Store
	* to play by CLI.
	* 
	* @return a formatted String to print the current composition of the Store
	*/
	public String storeToString(){
		
		String listString = ""; 
			
		for(Map.Entry<Token, ShoppingCart> map : this.store.entrySet()){
			
			listString += "PLAYER #" + map.getKey().getPlayerNumber() + " SELL:\n";
			listString += map.getValue() + "\n";
		}
		return listString;
	}
}
