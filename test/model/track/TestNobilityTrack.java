package it.polimi.ingsw.cg32.model.track;

import java.util.HashSet;

import org.junit.Test;

public class TestNobilityTrack {

	NobilityTrack track;
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWhenArgumentIsNullShouldThrowAnIllegalArgumentException() {
		
		track = new NobilityTrack(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructorWhenArgumentSetIsEmptyShouldThrowAnIllegalArgumentException() {
		
		track = new NobilityTrack(new HashSet<>());
	}
}
