package it.polimi.ingsw.cg32.model.track;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class TestVictoryTrack {
	
	private int increment;
	private static VictoryTrack track;
	
	public TestVictoryTrack(int increment) {
		this.increment = increment;
	}
	
	@BeforeClass
	public static void initClass() {
		track = new VictoryTrack();	
	}
	   
	

	@Parameters(name = "{index}: setPoints({0})")
	public static Iterable<Object[]> data() {
	return Arrays.asList(new Object[][] {
	{ 1 },
	{ 2 },
	{ 4 },
	{ 6 },
	{ 7 },
	{ 8 },
	{ 9 },
	{ 12 },
	{ 15 },
	{ 19 },
	{ 24 }
	});
	}
	
	@Test
	public void initializeVictoryTrackShouldSetCurrentPonitsTo0() {
		VictoryTrack initializeTrack = new VictoryTrack();
		assertEquals(0, initializeTrack.getPoints());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void setVictoryTrackWithNegativeNumberShouldThrowsIllegalArgumetException() {
		track.setPoints(-increment);
	}
	
	@Test
	public void setVictoryTrackWith0ShouldDoNothing() {
		int precedent = track.getPoints();
		track.setPoints(0);
		assertEquals(precedent, track.getPoints());
	}
	
	@Test
	public void setVictoryTrackWithPositiveNumberShouldIncrementCurrentPointsOfTheValureOfTheInput() {
		int precedent = track.getPoints();
		track.setPoints(increment);
		assertEquals(precedent + increment, track.getPoints());
	}
	
	@Test
	public void getPointsShouldReturnTheCurrentPointsOfVictoryTrack(){
		VictoryTrack getTrack = new VictoryTrack();
		getTrack.setPoints(5);
		assertEquals(5, getTrack.getPoints());
	}

}
