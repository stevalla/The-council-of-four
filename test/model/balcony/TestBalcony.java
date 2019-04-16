package it.polimi.ingsw.cg32.model.balcony;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.color.Colore;

public class TestBalcony {

	static Balcony balcony;
	CouncillorReserve councillorReserve;
	Councillor councillor;
	
	@BeforeClass
	public static void initializeBalcony() {

		balcony = new Balcony();
	}
	
	@Test(expected = NullPointerException.class)
	public void testElectCouncillorWhenReserveIsNull() {

		balcony.electCouncillor(null, new Councillor(Colore.BLACK));
	}
	
	@Test(expected = NullPointerException.class)
	public void testElectCouncillorWhenCouncillorIsNull() {

		balcony.electCouncillor(new CouncillorReserve(), null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRemoveCouncillorFromBalconyWhenBalconyIsEmptyShouldThrowAnException() {
		balcony = new Balcony();
		balcony.pollLast();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPushACouncillorWhenBalconyHave4CouncillorShouldBeThrowAnException() {
		balcony = new Balcony();

		for(int i=0; i<4; i++) {
			this.councillor = new Councillor(Colore.BLACK);
			balcony.push(councillor);
		}
		
		balcony.push(new Councillor(Colore.BLACK));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testToRemoveACouncillorWhenBalconyNotHave4CouncillorShouldBeThrowAnException() {
		balcony = new Balcony();

		for(int i=0; i<4; i++) {
			this.councillor = new Councillor(Colore.BLACK);
			balcony.push(councillor);
		}
		
		while(balcony.getSeats().size() != 0)
			balcony.pollLast();
	}
	
	@Test
	public void testPush4CouncillorWhenBalconyHaveThreeCouncillorsTheSizeOfSeatsInBalconyShouldBe4() {
		balcony = new Balcony();

		for(int i=0; i<3; i++) {
			this.councillor = new Councillor(Colore.BLACK);
			balcony.push(councillor);
		}
		
		balcony.push(new Councillor(Colore.BLACK));
		
		assertEquals(4, balcony.getSeats().size());
	}
	
	@Test
	public void testRemoveOneCouncillorWhenBalconyIsEmptyTheSizeOfSeatsInBalconyShouldBe3() {

		balcony = new Balcony();

		for(int i=0; i<4; i++) {
			this.councillor = new Councillor(Colore.BLACK);
			balcony.push(councillor);
		}
		
		balcony.pollLast();
		
		assertEquals(3, balcony.getSeats().size());
		
	}
	
	@Test
	public void testRemoveLastCouncillorAndGetIt() {
		balcony = new Balcony();

		for(int i=0; i<4; i++) {
			this.councillor = new Councillor(Colore.BLACK);
			balcony.push(councillor);
		}
		//Get last councillor to assert the result of remove
		Councillor councillor = balcony.getSeats().get(3);
		
		Councillor councillorRemove = balcony.pollLast();
		
		assertEquals(councillor, councillorRemove);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testUnmodifiableSeatsAdd() {
		
		balcony.getSeats().add(new Councillor(Colore.ORANGE));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void testUnmodifiableSeatsRemove() {
		
		balcony.getSeats().remove(0);
	}
	
	@Test
	public void testElectCouncillorSizeOfCouncillorInBalconyDoNotChangeAfterExecution() {
		this.councillorReserve = new CouncillorReserve();
		balcony = new Balcony();

		for(int i=0; i<4; i++) {
			this.councillor = new Councillor(Colore.BLACK);
			balcony.push(councillor);
		}
		
		int councillorReserveSize = balcony.getSeats().size();
		
		balcony.electCouncillor(councillorReserve, councillor);
		
		assertEquals(councillorReserveSize, balcony.getSeats().size());
	}
	
}
