package it.polimi.ingsw.cg32.model.assistant;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import it.polimi.ingsw.cg32.model.assistants.AssistantsReserve;

@RunWith(value = Parameterized.class)
public class TestAssistantReserve {

	AssistantsReserve assistantsReserve;
	private int input;
	
	public TestAssistantReserve(int input) {
		this.input = input;
	}
	
	@Parameters(name = "{index}: input({0})")
	public static Iterable<Object[]> data() {
	return Arrays.asList(new Object[][] {
	{ 5 },
	{ 6 },
	{ 7 },
	{ 8 },
	{ 9 },
	{ 10 },
	{ 11 },
	{ 12 },
	{ 13 },
	{ 14 },
	{ 16 },
	{ 20 },
	{ 30 },
	{ 100 }
	});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCatchExceptionInitializingAssistantReerverWithLessThenTwoPlayers() {
		
		assistantsReserve = new AssistantsReserve(1);
	}
	
	@Test
	public void testAssistantReserveSizeWhenNumberOfPlayersIsBetween2And4ShouldBe30() {
		
		assistantsReserve = new AssistantsReserve(2);
		
		assertEquals(30,assistantsReserve.getAssistants().size());
	}
	
	@Test
	public void testAssistantReserveSizeWhenNumberOfPlayerIsMoreThen4() {
		
		assistantsReserve = new AssistantsReserve(input);

		int calculate = 0;
		for(int i=0; i<=input; i++)
			calculate += i;
		
		assertEquals(calculate + (input*5), assistantsReserve.getAssistants().size());
	}

}
