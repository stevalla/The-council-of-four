package it.polimi.ingsw.cg32.model.market;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

public class TestAssistantBoundle {

	public static List<Assistant> assistants = new ArrayList<>();
	public static Player player;
	public static Game game;
	
	@BeforeClass
	public static void initTest(){
		
		for(int i = 0; i < 3; i++)
			assistants.add(new Assistant());
		
		game = new Game("src/main/resources/map/default.txt", 4);
		player = game.getPlayers().get(0);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testConstructorWithNegativePriceShouldTrhowIllegalArgumentException(){
		
		new AssistantBoundle(-5, assistants);
	}
	
	@Test (expected=NullPointerException.class)
	public void testConstructorWithListOfAssistantsNullShouldTrhowNullPointerException(){
		
		new AssistantBoundle(3, null);
	}
	
	@Test
	public void testConstructorWithValidParamsShouldCreateAssistantBoundleAsRequest(){
		AssistantBoundle boundle = new AssistantBoundle(7, assistants);
		
		assertEquals(7, boundle.getPrice());
		
		Assert.assertThat(boundle.getAssistants(), 
			       IsIterableContainingInOrder.contains(assistants.toArray()));
		
		Assert.assertTrue(boundle.getBoundleId() > 0);
		
		
	}
	
	@Test
	public void testUseBoundleShouldIncrementTheNumberOfAssistantsOfThePlayerToTheNumberOfAssistantsInTheBoundleAndNotChangeFieldOfTheBoundle(){
		assertEquals(1, player.getAssistants().size());
		AssistantBoundle boundle = new AssistantBoundle(3, assistants);
		assertEquals(3, boundle.getAssistants().size());
		boundle.useBoundle(player);
		assertEquals(3, boundle.getAssistants().size());
		assertEquals(4, player.getAssistants().size());
	}
	
	@Test
	public void testBoundleGetterShouldReturnTheListOfAssistantOfTheBoundle(){
	
		AssistantBoundle boundle = new AssistantBoundle(3, assistants);
		
		Assert.assertThat(boundle.getAssistants(), 
			       IsIterableContainingInOrder.contains(assistants.toArray()));
	}


}
