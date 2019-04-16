package it.polimi.ingsw.cg32.model.city;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestEmporium {

	Emporium emporium;
	
	@Test
	public void testGetterPlayerIdShouldReturnTheSameIdThatThePassedInConstructor() {
		
		int id = 2;
		emporium = new Emporium(id);
		
		assertEquals(id, emporium.getPlayerId());
	}
}
