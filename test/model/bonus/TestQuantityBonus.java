package it.polimi.ingsw.cg32.model.bonus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestQuantityBonus {

	QuantityBonus bonus1;
	QuantityBonus bonus2;
	private String name1;
	private String name2;
	private int quantity1;
	private int quantity2;
	
	public TestQuantityBonus(String name1, String name2, int quantity1, int quantity2) {
		this.name1 = name1;
		this.name2 = name2;
		this.quantity1 = quantity1;
		this.quantity2 = quantity2;
	}
	
	@Parameters(name = "{index}: setBonus({0}{1}{2}{3})")
	public static Iterable<Object[]> data() {
	return Arrays.asList(new Object[][] {
	{ "name1", "name2", 1, 2 },
	{ "name1", "name1", 1, 2 },
	{ "name1", "name2", 1, 1 },
	{ "name1", "name1", 1, 1 }
	});
	}

	@Test(expected = NullPointerException.class)
	public void testBonusQuantityConstructorWhenArgumentIsNullShouldBeThrowAnException() {
		
		bonus1 = new QuantityBonus(null, (int) (Math.random()*10));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void  testBonusQuantityCOnstructorWhenQuantityIsLessThenOneShouldThowAnException() {
		
		bonus1 = new QuantityBonus("quantityBonus", 0);
	}
	
	@Test
	public void testEqualsQuantityBonus() {
		
		bonus1 = new QuantityBonus(name1, quantity1);
		bonus2 = new QuantityBonus(name2, quantity2);
		
		if(name1.equals(name2) && quantity1 == quantity2)
			assertTrue(bonus1.equals(bonus2));
		else
			assertFalse(bonus1.equals(bonus2));
	}
	@Test
	public void testGetterSingleBonusShouldReturnTheNameOfTheBonus() {
		
		bonus1 = new QuantityBonus(name1,quantity1);
		
		assertTrue(name1.equals(bonus1.getName()));
		assertTrue((quantity1 == bonus1.getQuantity()));
	}
}
