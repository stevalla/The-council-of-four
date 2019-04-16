package it.polimi.ingsw.cg32.model.track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestMarkerDisk {

	MarkerDisk disk;
	private static NobilityTrack track;
	
	@BeforeClass
	public static void initializeTrack() {
		Set<Space> spaces = new HashSet<>();
		
		for(int i=0; i<5; i++)
			spaces.add(new Space(i));
		
		track = new NobilityTrack(spaces);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructorWhenArgumentIsNullShoulsThrowANullPointerException() {
		
		disk = new MarkerDisk(null);
	}
	
	@Test
	public void testNextCurrentSpaceShouldMoveTheMarkerDiskOnePosition() {
		
		disk = new MarkerDisk(track);
		
		int positionBefore = disk.getCurrentSpace().getSpaceNumber();
		disk.nextCurrentSpace();
		assertEquals(positionBefore + 1, disk.getCurrentSpace().getSpaceNumber());
	}
	
	@Test
	public void testNextCurrentSpaceWhenTheTrackAreFinishedReturnNullAndNotMoveTheDisk() {
		
		disk = new MarkerDisk(track);
		
		for(int i=0; i<5; i++)
			disk.nextCurrentSpace();
				
		int positionBefore = disk.getCurrentSpace().getSpaceNumber();
		assertNull(disk.nextCurrentSpace());

		assertEquals(positionBefore, disk.getCurrentSpace().getSpaceNumber());
	}
	
	@Test
	public void testCheckIsTrackFinishedReturnFalseIfTheDiskIsAtTheEndOfTheTrack() {

		disk = new MarkerDisk(track);
		for(int i=0; i<5; i++)
			disk.nextCurrentSpace();

		assertFalse(disk.checkFinishedTrack());
	}
	
	@Test
	public void testCheckIsTrackFinishedReturnTrueIfTheDiskIsNotAtTheEndOfTheTrack() {

		disk = new MarkerDisk(track);

		assertTrue(disk.checkFinishedTrack());
	}
	
	@Test
	public void testGetterCurrentSpaceAssertThatTheSpaceAtTheCreationCOrrispondingToSpaceZero() {
		
		disk = new MarkerDisk(track);
		
		assertEquals(new Space(0), disk.getCurrentSpace());	
	}
}
