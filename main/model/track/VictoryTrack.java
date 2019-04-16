package it.polimi.ingsw.cg32.model.track;

import java.io.Serializable;

/**
* This class models and manages a victory track .
* Once a track is created is possible to move exclusively forward on the track by {@link #setPoints(int)} method.
* Implement {@link Serializable}
* 
* @author giovanni
*/
public class VictoryTrack implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int currentPoints;

	
	/**
     * Instantiates a new VictoryTrack, setting currentPoints to 0.
     */
	public VictoryTrack() {
		this.currentPoints = 0;
	}

	
	/**
	 * Get currentPoints of the VictoryTrack
	 * 
	 * @return the currentPoints of VictoryTrack 
	 */
	public int getPoints() {
		return currentPoints;
	}

	
	/**
	 * Increment the number of points in VictoryTrack of the quantity indicate by params.
	 * Is not allowed to decrement currentPoints, infact an illegalArgumentException is thrown
	 * if nextVictoryPoints < 0.
	 * 
	 * @param nextVictoryPoints The number of points to increment
     * @throws IllegalArgumentException if argument is invalid (nextVictoryPoints < 0)
	 */
	public void setPoints(int nextVictoryPoints) {
		if(nextVictoryPoints < 0)
			throw new IllegalArgumentException("Impossible to move backwards VictoryTrack.");
		else
			this.currentPoints += nextVictoryPoints;
	}

	
}
