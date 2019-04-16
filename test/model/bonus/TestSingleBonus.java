package it.polimi.ingsw.cg32.model.bonus;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestSingleBonus {

	SingleBonus singleBonus1;
	SingleBonus singleBonus2;
	private String name1;
	private String name2;
	
	public TestSingleBonus(String name1, String name2) {
		this.name1 = name1;
		this.name2 = name2;
	}
	
	@Parameters(name = "{index}: names({0}{1})")
	public static Iterable<Object[]> data() {
	return Arrays.asList(new Object[][] {
	{ "name1", "name1" },
	{ "name1", "name2" }
	});
	}
	
	@Test(expected = NullPointerException.class)
	public void testBonusConstructorWhenArgumentIsNullShouldBeThrowAnException() {
		
		singleBonus1 = new SingleBonus(null);
	}
	
	@Test
	public void testEqualsSingleBonus() {
	
		singleBonus1 = new SingleBonus(name1);
		singleBonus2 = new SingleBonus(name2);
		
		if(!name1.equals(name2))
			assertFalse(singleBonus1.equals(singleBonus2));
		else
			assertTrue(singleBonus1.equals(singleBonus2));
	}
	
	@Test
	public void testGetterSingleBonusShouldReturnTheNameOfTheBonus() {
		
		singleBonus1 = new SingleBonus(name1);
		
		assertTrue(name1.equals(singleBonus1.getName()));
	}
	
}
