package it.polimi.ingsw.cg32.model.market;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

public class TestPoliticCardBoundle {

	
	public static List<PoliticCard> politicCards = new ArrayList<>();
	public static Player player;
	public static Game game;
	public static PoliticCardPrototype prototype;
	
	@BeforeClass
	public static void initTest(){
		
		prototype = new PoliticCardPrototype();
		
		for(int i = 0; i < 3; i++)
			politicCards.add(prototype.getPoliticCard());
		
		game = new Game("src/main/resources/map/default.txt", 4);
		player = game.getPlayers().get(0);
		player.getPoliticCards().clear();
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testConstructorWithNegativePriceShouldTrhowIllegalArgumentException(){
		
		new PoliticCardBoundle(-5, politicCards);
	}
	
	@Test (expected=NullPointerException.class)
	public void testConstructorWithListOfPoliticCardNullShouldTrhowNullPointerException(){
		
		new PoliticCardBoundle(3, null);
	}
	
	@Test
	public void testConstructorWithValidParamsShouldCreateAssistantBoundleAsRequest(){
		PoliticCardBoundle boundle = new PoliticCardBoundle(7, politicCards);
		
		assertEquals(7, boundle.getPrice());
		
		Assert.assertThat(boundle.getPoliticCards(), 
			       IsIterableContainingInOrder.contains(politicCards.toArray()));
		
		Assert.assertTrue(boundle.getBoundleId() > 0);
	}
	
	@Test
	public void testUseBoundleShouldAddThePoliticCardOfTheBoundleToThePoliticCardOfThePlayerAndNotChangeFieldsOfTheBoundle(){
		
		PoliticCardBoundle boundle = new PoliticCardBoundle(3, politicCards);
		PoliticCardBoundle boundlePreUseBoundle = boundle;
	
		Assert.assertTrue(player.getPoliticCards().isEmpty());
		
		Assert.assertThat(boundle.getPoliticCards(), 
			       IsIterableContainingInOrder.contains(politicCards.toArray()));
		
		boundle.useBoundle(player);
		
		//Control if the boundle changed
		Assert.assertThat(boundle.getPoliticCards(), 
			       IsIterableContainingInOrder.contains(boundlePreUseBoundle.getPoliticCards().toArray()));
		
		Assert.assertTrue(boundlePreUseBoundle.getPrice() == boundle.getPrice());
		
		//Control if the useBoundle do what is expected
		Assert.assertThat(boundle.getPoliticCards(), 
			       IsIterableContainingInOrder.contains(player.getPoliticCards().toArray()));
		
	}
	


	@Test
	public void testBoundleGetterShouldReturnTheListOfAssistantOfTheBoundle(){
		
		PoliticCardBoundle boundle = new PoliticCardBoundle(3, politicCards);
		
		Assert.assertThat(boundle.getPoliticCards(),
			       IsIterableContainingInOrder.contains(politicCards.toArray()));
	}
}
