package it.polimi.ingsw.cg32.model.track;

import java.io.Serializable;


/**
* This class models and manages a coins track.
* Creating a track sets automatically currentCoins to INFERIOR_COINS_START_THRESHOLD + playerID.
* Once a track is created is possible to move forward and backward on the track by {@link #setCoinsNumber(int)} method.
* Implement {@link Serializable}
* 
* @author giovanni
*/
public class CoinsTrack implements Serializable{
	
	/**
	 * Used to set CurrentCoinsNumber in constructor as indicate by game's rules.
	 */
	public static final int INFERIOR_COINS_START_THRESHOLD = 10;
	private static final long serialVersionUID = 1L;
	private int currentCoins;
	private final int coinsTrackSize;
	
	/**
	 * Instantiates a new CoinsTrack, setting currentCoins to INFERIOR_COINS_START_THRESHOLD + playerID
	 * and coinsTrackSize invocating calculateCoinsTrackSize.
	 * 
	 * @param playerID the Id of the player who owns this CoinsTrack
	 * @param numberPlayers the number of player playing the game
	 * @throws IllegalArgumentException if argument numberPlayers is invalid (numberPlayers < 2)
	 */
	public CoinsTrack(int playerID, int numberPlayers){
		
		if(numberPlayers<2)
			throw new IllegalArgumentException("Impossible to instatiate a CoinsTrack for a game with less than 2 players");
		
		this.currentCoins = INFERIOR_COINS_START_THRESHOLD + playerID;
		this.coinsTrackSize = calculateCoinsTrackSize(numberPlayers);
	}
	
	
	/**
	* Used to set currentCoins to a new value.
	* It's not allowed to pay an amount of coins that would set curentCoins to a negative number throwing
	* an illegalArgumentException, as well as gain an amount of coins that would overflow the size of the track 
	* setting currentCoins to coinsTrackSize.
	*   
	* @param coinsMove the amount of coins to update track 
    * @throws IllegalArgumentException if argument is invalid (currentCoinsNumber + coinsMove < 0)
	*/
	public void setCoinsNumber(int coinsMove) {
		if(currentCoins + coinsMove > coinsTrackSize){
				this.currentCoins = coinsTrackSize;
				return;
		}
		else if(currentCoins + coinsMove < 0){
			throw new IllegalArgumentException("Impossible to set CoinsTrack to a negative number.");
		}
		
		this.currentCoins += coinsMove;		
	}
	
	
	/**
	* Method used to calculate the maximum size of the track proportionally to number the of
    * players in the game.
	* 
	* @param numberPlayers the number of players playing the game
	* @return coinsTrackSize the max number of coins 
	*/
	private int calculateCoinsTrackSize(int numberPlayers) {
		int standardQuantityCoins = 20;
		if(numberPlayers<5)
			return standardQuantityCoins;
		else
			return standardQuantityCoins + (numberPlayers - 4);
	}

	
	/**
	* Get the current number of coins of the track
	* 
	* @return the currentCoins of CoinsTrack 
	*/
	public int getCurrentCoins() {
		return currentCoins;
	}
	
	
	/**
	* Get the size of the track
	* 
	* @return the coinsTrackSize of CoinsTrack 
	*/
	public int getCoinsTrackSize() {
		return coinsTrackSize;
	}
	
}
