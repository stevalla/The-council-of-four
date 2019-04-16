package it.polimi.ingsw.cg32.model.balcony;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import it.polimi.ingsw.cg32.model.color.Colore;

public class TestCouncillor {
	
	Councillor councillor;

	@Test(expected = NullPointerException.class)
	public void testWhenColorInCostructorIsNullShouldReturnNullPointerException() {
		
		councillor = new Councillor(null);
	}
	
	public void testGetterMethodShouldReturnTheColorOfTheCOuncillor() {
		
		Arrays.asList(Colore.values()).stream()
						.forEach(c -> {
							councillor = new Councillor(c);
							
							assertEquals(c, councillor.getColor());
						});
	}
}
