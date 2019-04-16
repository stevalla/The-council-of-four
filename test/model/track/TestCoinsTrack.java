package it.polimi.ingsw.cg32.model.track;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class TestCoinsTrack {
	
	private int absValureOfInput;
	private static CoinsTrack coinsTrack;
	
	public TestCoinsTrack(int increment) {
		this.absValureOfInput = increment;
	}

	@BeforeClass
	public static void initClass() {
		coinsTrack = new CoinsTrack(0, 4);
	}
	   
	  
	@Parameters(name = "{index}: setCoins({0})")
	public static Iterable<Object[]> data() {
	return Arrays.asList(new Object[][] {
	{ 1 },
	{ 2 },
	{ 3 },
	{ 4 },
	{ 5 },
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
	public void InitialCoinsNumerForFirstPlayerShouldBe10() {
		
		if(absValureOfInput > 1){
			CoinsTrack coinsTrackToCheckPlayerCurrentCoinsAtBeging = new CoinsTrack(0, absValureOfInput);
			assertEquals(10, coinsTrackToCheckPlayerCurrentCoinsAtBeging.getCurrentCoins());
		}
	}
	
	@Test
	public void InitialCoinsNumerForSecondPlayerShouldBe11() {
	
		if(absValureOfInput > 1){
			CoinsTrack coinsTrackToCheckPlayerCurrentCoinsAtBeging = new CoinsTrack(1, absValureOfInput);
			assertEquals(11, coinsTrackToCheckPlayerCurrentCoinsAtBeging.getCurrentCoins());
		}
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void coinsTrackShouldThrowAnIllegalArgumentExceptionIfInstantiatedForJust1Player() {
	
			new CoinsTrack(0, 1);
			
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void coinsTrackShouldThrowAnIllegalArgumentExceptionIfInstantiatedWithANegativeNumberOfPlayer() {
	
			new CoinsTrack(0, -absValureOfInput);
			
	}
	
	@Test
	public void coinsTrackSizeShouldBe20ForAGameWithLessThan5Players() {
	
		if(absValureOfInput <= 4 && absValureOfInput > 1){
			CoinsTrack coinsTrackToCheckCoinsTrackSize = new CoinsTrack(0, absValureOfInput);
			assertEquals(20, coinsTrackToCheckCoinsTrackSize.getCoinsTrackSize());
		}
	}
	
	@Test
	public void coinsTrackSizeShouldBe20PlusNumberOfPlayerMinus4ForAGameWithMoreThan4Players() {
	
		if(absValureOfInput > 4){
			CoinsTrack coinsTrackToCheckCoinsTrackSize = new CoinsTrack(0, absValureOfInput);
			assertEquals(20 + (absValureOfInput - 4), coinsTrackToCheckCoinsTrackSize.getCoinsTrackSize());
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void SetCoinsNumberWithANegativeNumberWithAbsoluteValueGreaterThanCurrentCoinsShouldThrowIllegaleArgumentException(){
		
		int decrementation = -(coinsTrack.getCurrentCoins() + absValureOfInput);
		
		coinsTrack.setCoinsNumber(decrementation);
	}
	
	@Test
	public void SetCoinsNumberWithANegativeNumberWithAbsoluteValueEqualsToCurrentCoinsShouldSetCurrentCoinsNumberTo0(){
		
		int decrementation = -coinsTrack.getCurrentCoins();
		
		coinsTrack.setCoinsNumber(decrementation);
		assertEquals(0, coinsTrack.getCurrentCoins());
	}
	
	
	@Test
	public void SetCoinsNumeberWithANegativeNumberWithAbsoluteValueMinorThanCurrentCoinsNumberShouldDecrementCurrentCoinsNumber(){
		
		int previousValue = coinsTrack.getCurrentCoins();
		int decrementation = -(absValureOfInput);
	
		if(Math.abs(decrementation) < coinsTrack.getCurrentCoins()){
			coinsTrack.setCoinsNumber(decrementation);
			assertTrue(previousValue > coinsTrack.getCurrentCoins());
		}
	}
	
	
	@Test
	public void SetCoinsNumberWith0ShouldDoNothing(){
		int previous = coinsTrack.getCurrentCoins();
		coinsTrack.setCoinsNumber(0);
		assertEquals(previous, coinsTrack.getCurrentCoins());
	}
	
	@Test
	public void SetCoinsNumeberWithAPositiveNumberMinorThanCoinsTrackSizeMinusCurrentCoinsNumberShouldIncrementCurrentCoinsNumber(){
		
		int previousValue = coinsTrack.getCurrentCoins();
		int incrementation = absValureOfInput;
		
		if(incrementation < coinsTrack.getCoinsTrackSize() - coinsTrack.getCurrentCoins()){
			coinsTrack.setCoinsNumber(incrementation);
			assertTrue(previousValue < coinsTrack.getCurrentCoins());
		}
	}
	
	@Test
	public void SetCoinsNumeberWithAPositiveNumberEqualsToCoinsTrackSizeMinusCurrentCoinsNumberShouldSetCurrentCoinsNumberToCoinsTrackSize(){
		
		int incrementation = coinsTrack.getCoinsTrackSize() - coinsTrack.getCurrentCoins();
		
		coinsTrack.setCoinsNumber(incrementation);
		assertEquals(coinsTrack.getCoinsTrackSize(), coinsTrack.getCurrentCoins());
	}
	
	@Test
	public void SetCoinsNumeberWithAPositiveNumberGreaterThanCoinsTrackSizeMinusCurrentCoinsNumberShouldSetCurrentCoinsNumberToCoinsTrackSize(){
		
		int incrementation = coinsTrack.getCoinsTrackSize() - coinsTrack.getCurrentCoins() + absValureOfInput;
		
		assertTrue(incrementation > coinsTrack.getCoinsTrackSize() - coinsTrack.getCurrentCoins());
		coinsTrack.setCoinsNumber(incrementation);
		assertEquals(coinsTrack.getCoinsTrackSize(), coinsTrack.getCurrentCoins());
	}
	
}
