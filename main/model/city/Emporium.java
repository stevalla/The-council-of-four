package it.polimi.ingsw.cg32.model.city;

import java.io.Serializable;

import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent an emporium that the player can build 
 * on the city. It is identificated from the player Id ({@link int}).<br>
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 * @see City 
 * @see Player
 */
public class Emporium implements Serializable{

	private static final long serialVersionUID = 1L;
	private final int playerId;
	
	
	/**
	 * Construct an emporium setting the id of the {@link Player} that
	 * take it.
	 * 
	 * @param playerId {@link int} the {@link Player} id
	 */
	public Emporium(int playerId) {
		this.playerId = playerId;
	}

	
	/**
	 * @return {@link int} the {@link Player} id
	 */
	public int getPlayerId() {
		return playerId;
	}

	
	@Override
	public String toString() {
		return "[" + playerId + "]";
	}
	
}
