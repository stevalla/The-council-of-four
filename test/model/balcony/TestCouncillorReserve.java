package it.polimi.ingsw.cg32.model.balcony;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.cg32.model.color.Colore;

public class TestCouncillorReserve {
	
	CouncillorReserve councillorReserve;

	@Test
	public void testCreateCouncillorReserveShouldCreateAListOfCOuncillorWithSize24() {
		councillorReserve = new CouncillorReserve();
		
		assertEquals(24, councillorReserve.getCouncillorInReserve().size());
	}
	
	@Test
	public void testCreateCouncillorReserveShouldCreateAListWith4CouncillorForEachColor() {
		
		councillorReserve = new CouncillorReserve();
		
		List<Councillor> councillors = new ArrayList<>();

		for(Colore c : Colore.values()){
			if(c == Colore.JOLLY)
				continue;
			councillorReserve.getCouncillorInReserve()
							.stream()
							.filter(councillor -> councillor.getColor() == c)
							.forEach(councillors::add);
			assertEquals(4, councillors.size());
			councillors.clear();
		}
	}
	
	@Test
	public void testContainCouncillorShouldTrueIfThereIsTheCouncillorWithTheColorParameter() {
		
		councillorReserve = new CouncillorReserve();
		
		Arrays.asList(Colore.values())
					.stream()
					.filter(c -> c != Colore.JOLLY)
					.forEach(c -> assertTrue(councillorReserve.containCouncillor(c)));
	}
	
	@Test
	public void testContainCouncillorShouldReturnFalseIfThereIsNotTheCouncillorWithTheColorParameter() {
		
		councillorReserve = new CouncillorReserve();
		
		//Pull all the councillor with color black
		new ArrayList<>(councillorReserve.getCouncillorInReserve())
				.stream()
				.filter(c -> c.getColor() == Colore.BLACK)
				.forEach(c -> councillorReserve.getCouncillorInReserve().remove(c));
		
		assertFalse(councillorReserve.containCouncillor(Colore.BLACK));
	}
	
	@Test(expected = NullPointerException.class)	
	public void testContainCouncillotIfArgumentIsNullShouldThrowANullPointerException() {
		
		councillorReserve = new CouncillorReserve();
		councillorReserve.containCouncillor(null);
	}
	
	@Test
	public void testGetCouncillorByColorShouldReturnACouncillorForEachColorParameterWhenReserveIsFull() {
		
		councillorReserve = new CouncillorReserve();
		
		List<Councillor> councillors = new ArrayList<>();
		Arrays.asList(Colore.values())
					.stream()
					.filter(c -> c != Colore.JOLLY)
					.forEach(c -> {
						councillors.add(councillorReserve.getCouncillorByColor(c));
					});
		
		assertEquals(6, councillors.size());
	}
	
	@Test
	public void testGetCouncillorByColorShouldReturnNullIfTheCouncillorWithTheColorParameterThereIsNotInReserve() {
		
		councillorReserve = new CouncillorReserve();
		
		//Pull all the councillor with color black
		new ArrayList<>(councillorReserve.getCouncillorInReserve())
					.stream()
					.filter(c -> c.getColor() == Colore.BLACK)
					.forEach(c -> councillorReserve.getCouncillorInReserve().remove(c));
		
		assertNull(councillorReserve.getCouncillorByColor(Colore.BLACK));
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetCouncillorByColorWhenArgumentIsNullShouldThrowANullPointerException() {

		councillorReserve = new CouncillorReserve();
		councillorReserve.getCouncillorByColor(null);
	}
}
