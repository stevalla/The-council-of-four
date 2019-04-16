package it.polimi.ingsw.cg32.model.market;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

public class TestPermitCardBoundle {

	public static List<PermitCard> permitCards = new ArrayList<>();
	public static Player player;
	public static Game game;
	
	@BeforeClass
	public static void initTest(){
		game = new Game("src/main/resources/map/default.txt", 4);
		player = game.getPlayers().get(0);
		
		permitCards.add(game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0));
		permitCards.add(game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(1));
		
		
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testConstructorWithNegativePriceShouldTrhowIllegalArgumentException(){
		
		new PermitCardBoundle(-5, permitCards);
	}
	
	@Test (expected=NullPointerException.class)
	public void testConstructorWithListOfPoliticCardNullShouldTrhowNullPointerException(){
		
		new PermitCardBoundle(3, null);
	}
	
	@Test
	public void testConstructorWithValidParamsShouldCreatePermitCardBoundleAsRequest(){
		PermitCardBoundle boundle = new PermitCardBoundle(7, permitCards);
		
		assertEquals(7, boundle.getPrice());
		
		Assert.assertThat(boundle.getPermitCards(), 
			       IsIterableContainingInOrder.contains(permitCards.toArray()));
		
		Assert.assertTrue(boundle.getBoundleId() > 0);
	}
	
	@Test
	public void testUseBoundleShouldAddThePermitCardOfTheBoundleToThePermitCardOfThePlayerAndNotChangeFieldsOfTheBoundle(){
		
		PermitCardBoundle boundle = new PermitCardBoundle(3, permitCards);
		PermitCardBoundle boundlePreUseBoundle = boundle;
	
		Assert.assertThat(boundle.getPermitCards(), 
			       IsIterableContainingInOrder.contains(permitCards.toArray()));
		
		boundle.useBoundle(player);
		
		//Control if the boundle changed
		Assert.assertThat(boundle.getPermitCards(), 
			       IsIterableContainingInOrder.contains(boundlePreUseBoundle.getPermitCards().toArray()));
		
		Assert.assertTrue(boundlePreUseBoundle.getPrice() == boundle.getPrice());
		
		//Control if the useBoundle do what is expected
		
		Assert.assertThat(boundle.getPermitCards(), 
			       IsIterableContainingInOrder.contains(player.getPermitCards().toArray()));
		
	}


	@Test
	public void testBoundleGetterShouldReturnTheListOfPermitcardOfTheBoundle(){
		
		PermitCardBoundle boundle = new PermitCardBoundle(3, permitCards);
		
		Assert.assertThat(boundle.getPermitCards(),
			       IsIterableContainingInOrder.contains(permitCards.toArray()));
	}
}
