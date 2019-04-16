package it.polimi.ingsw.cg32.model.track;

import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import it.polimi.ingsw.cg32.model.game.FactoryLoaderGame;

/**
* This class models a nobility track.
* Contains a {@link SortedSet} of Space that composed the track.
* The Spaces that composed the nobility track are initialize and collect in a set in the method {@link #createSpace}
* in {@link FactoryLoaderGame}. 
* Implement {@link Serializable}
* 
* @author giovanni
* @see Space
*/
public class NobilityTrack implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private final SortedSet<Space> track;
	
	
	/**
	* Constructr for NobilityTrack. 
	* Instantiate a NobilityTrack and fills its set of bonus with the set of bonus pass as param.
	* 
	* @param spaces the set of Space that will compose the track
	* @throws IllegalArgumentException if the Set of {@link Space} is null or empty
	*/
	public NobilityTrack(Set<Space> spaces) {
		if(spaces == null || spaces.isEmpty())
			throw new IllegalArgumentException("The Set of Space can't be null or empty.");
		
		track = new TreeSet<>(spaces);
	}
	
	
	/**
	* Get the set of spaces that composed the nobility track.
	* 
	* @return the set of spaces which the nobility track consist on.
	*/
	public SortedSet<Space> getTrack() {
		return track;
	}

	
	@Override 
	public String toString() {
		
		String listString = "";
		for(Space space: track)
			listString += space;
			
		return listString;
	}

}
