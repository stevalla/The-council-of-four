package it.polimi.ingsw.cg32.model.track;

import java.io.Serializable;
import java.util.Iterator;

/**
 * This class model and manage a marker disk to slide the NobilityTrack.
 * It's mainly consisted of a NobilityTrack's {@link Iterator} that iterate over the {@link Space} that compose the track.
 * Implement {@link Serializable}
 * 
 * @author giovanni
 * @see NobilityTrack
 */
public class MarkerDisk implements Serializable{

	private static final long serialVersionUID = 1L;
	private Space currentSpace;
	private transient Iterator<Space> trackIterator;
	
	/**
	 * The constructor for the MarkerDisk set the {@link Iterator} to operate over the target NobilityTrack 
	 * and sets currentSpace to the first {@link Space} of NobilityTrack.
	 * 
	 * @param track the NobilityTrack on which iterate over
	 * @throws NullPointerException if the argument is null
	 * @see NobilityTrack
	 */
	public MarkerDisk(NobilityTrack track) {
		if(track == null)
			throw new NullPointerException("The track can't be null");
		
		trackIterator = track.getTrack().iterator();
		this.currentSpace = trackIterator.next();
	}
	
	/**
	 * Advances the current position of the marker disk to the next Space of the NobilityTrack.
	 * If the marker disk has already reached the last Space of the NobilityTrack return null otherwise return 
	 * the next Space.
	 * 
	 * @return currentSpace the next Space of the nobility track, 
	 *         null if the markerDisk is already pointing on the last Space
	 * @see Space
	 * @see NobilityTrack
	 */
	public Space nextCurrentSpace() {

		if(trackIterator.hasNext()) {
			this.currentSpace = trackIterator.next();
			return this.currentSpace;
		}	
		else {
			return null; 
		}
	}
	
	/**
	 * Checks if the marker disk has reached the last {@link Space} of the {@link NobilityTrack}.
     *
     * @return true, if the marker disk hasn't already reached the last Space of the nobility track
	 */
	public boolean checkFinishedTrack() {
		
		return trackIterator.hasNext();
	}

	/**
	 * Get the current {@link Space} where the marker is pointing.
	 * 
	 * @return the currentSpace where the marker is pointing
	 */
	public Space getCurrentSpace() {
		return currentSpace;
	}
	
	@Override
	public String toString() {
		return "MarkerDisk [currentSpace=" + currentSpace + "]";
	}
		
}
	
