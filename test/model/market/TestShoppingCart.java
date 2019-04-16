package it.polimi.ingsw.cg32.model.market;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;


public class TestShoppingCart {

	public static List<PermitCard> permitCardsFromCoast = new ArrayList<>();
	public static List<PermitCard> permitCardsFromHill = new ArrayList<>();
	public static List<PermitCard> permitCardsFromMountain = new ArrayList<>();
	
	public static List<PoliticCard> FirstpoliticCards = new ArrayList<>();
	public static List<PoliticCard> SecondpoliticCards = new ArrayList<>();
	public static List<Assistant> assistants = new ArrayList<>();
	public static Game game;
	public static PoliticCardPrototype prototype;
	
	public static List<PoliticCardBoundle> politicCardBoundles = new ArrayList<>();
	public static List<AssistantBoundle> assistantBoundles = new ArrayList<>();
	public static List<PermitCardBoundle> permitCardBoundles = new ArrayList<>();
	

	@BeforeClass
	public static void initTest() {
	
		game = new Game("src/main/resources/map/default.txt", 4);
		
		//init PermitCardBoundle
		permitCardsFromCoast.add(game.getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getUsableCard().get(0));
		permitCardsFromCoast.add(game.getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getUsableCard().get(1));
		permitCardsFromHill.add(game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(0));
		permitCardsFromHill.add(game.getBoard().getRegions(RegionType.HILL).getDeckOfPermitCard().getUsableCard().get(1));
		permitCardsFromMountain.add(game.getBoard().getRegions(RegionType.MOUNTAIN).getDeckOfPermitCard().getUsableCard().get(0));
		permitCardsFromMountain.add(game.getBoard().getRegions(RegionType.MOUNTAIN).getDeckOfPermitCard().getUsableCard().get(1));
		
		PermitCardBoundle permitCoastBoundle = new PermitCardBoundle(2, permitCardsFromCoast);
		PermitCardBoundle permitHillBoundle = new PermitCardBoundle(5, permitCardsFromHill);
		PermitCardBoundle permitMountainBoundle = new PermitCardBoundle(9, permitCardsFromMountain);
		
		permitCardBoundles.add(permitCoastBoundle);
		permitCardBoundles.add(permitHillBoundle);
		permitCardBoundles.add(permitMountainBoundle);
		
		//init PoliticCardBoundle
		prototype = new PoliticCardPrototype();
		
		for(int i = 0; i < 3; i++)
			FirstpoliticCards.add(prototype.getPoliticCard());
	
		for(int i = 0; i < 3; i++)
			SecondpoliticCards.add(prototype.getPoliticCard());
		
		PoliticCardBoundle FirstPoliticBoundle = new PoliticCardBoundle(3, FirstpoliticCards);
		PoliticCardBoundle SecondPoliticBoundle = new PoliticCardBoundle(18, SecondpoliticCards);
		
		politicCardBoundles.add(FirstPoliticBoundle);
		politicCardBoundles.add(SecondPoliticBoundle);
		
		//init AssistantBoundle
		for(int i = 0; i < 3; i++)
			assistants.add(new Assistant());
		
		AssistantBoundle assistantBoundle = new AssistantBoundle(16, assistants);
		
		assistantBoundles.add(assistantBoundle);
	}
	
	
	@Test (expected=NullPointerException.class)
	public void testConstructorWithTheListOfPoliticCardBoundleNullShouldThrowNullPointerException(){
		new ShoppingCart(null, assistantBoundles, permitCardBoundles);
	}
	
	@Test (expected=NullPointerException.class)
	public void testConstructorWithTheListOfAssistantBoundleNullShouldThrowNullPointerException(){
		new ShoppingCart(politicCardBoundles, null, permitCardBoundles);
	}
	
	@Test (expected=NullPointerException.class)
	public void testConstructorWithTheListOfPermitCardBoundleNullShouldThrowNullPointerException(){
		new ShoppingCart(politicCardBoundles, assistantBoundles, null);
	}
	
	@Test
	public void testConstructorwithVaildsArgumentsShouldCreateShoppingcartAsRequest(){
		ShoppingCart shoppingCart = new ShoppingCart(politicCardBoundles, assistantBoundles, permitCardBoundles);
		
		Assert.assertTrue(CollectionUtils.isSubCollection(politicCardBoundles, shoppingCart.getListOfBoundles()));
		Assert.assertTrue(CollectionUtils.isSubCollection(assistantBoundles, shoppingCart.getListOfBoundles()));
		Assert.assertTrue(CollectionUtils.isSubCollection(permitCardBoundles, shoppingCart.getListOfBoundles()));
	}
	
	@Test
	public void testRemoveBoundleByIdShouldReturnTheBoundleIfTheTargetBoundleIsContainedInTheListOfBoundlesAndRemovesItFromTheList(){
		ShoppingCart shoppingCart = new ShoppingCart(politicCardBoundles, assistantBoundles, permitCardBoundles);
		
		//Take a random Id of Boundle from the list
		int targetBoundleId = shoppingCart.getListOfBoundles().get((int) (Math.random() * (shoppingCart.getListOfBoundles().size() - 1))).getBoundleId();
		
		Boundle boundleFromFind = shoppingCart.findBoundleById(targetBoundleId);
		
		Assert.assertTrue((shoppingCart.getListOfBoundles().contains(boundleFromFind)));
		
		Boundle boundleFromRemove = shoppingCart.removeBoundleById(targetBoundleId);
		
		Assert.assertFalse((shoppingCart.getListOfBoundles().contains(boundleFromRemove)));
		
		assert(boundleFromFind.getBoundleId() == boundleFromRemove.getBoundleId());
		
	}
	
	@Test
	public void testRemoveBoundleByIdShouldReturnNullIfTheTargetBoundleIsNotContainedInTheListOfBoundlesAndNotChangingTheListOfBoundles(){
		ShoppingCart shoppingCart = new ShoppingCart(politicCardBoundles, assistantBoundles, permitCardBoundles);
		
		//Note that is impossible to construct a Boundle with an id minor than 0,
		//thus assicure that there are no Boundle in the list with such Id.
		int targetBoundleId = -50;
		
		Boundle boundleFromRemove = shoppingCart.removeBoundleById(targetBoundleId);
		
		Assert.assertNull(boundleFromRemove);
	}
	
	@Test
	public void testFindBoundleByIdShouldReturnTheBoundleIfTheTargetBoundleIsContainedInTheListOfBoundlesAndNotChangingTheListOfBoundles(){
		ShoppingCart shoppingCart = new ShoppingCart(politicCardBoundles, assistantBoundles, permitCardBoundles);
		
		//Take a random Id of Boundle from the list
		int targetBoundleId = shoppingCart.getListOfBoundles().get((int) (Math.random() * (shoppingCart.getListOfBoundles().size() - 1))).getBoundleId();
		
		Boundle boundleFromFind = shoppingCart.findBoundleById(targetBoundleId);
		
		Assert.assertTrue((shoppingCart.getListOfBoundles().contains(boundleFromFind)));
		
		assert(boundleFromFind.getBoundleId() == targetBoundleId);
		
	}
	
	@Test
	public void testFindBoundleByIdShouldReturnNullIfTheTargetBoundleIsNotContainedInTheListOfBoundlesAndNotChangingTheListOfBoundles(){
		ShoppingCart shoppingCart = new ShoppingCart(politicCardBoundles, assistantBoundles, permitCardBoundles);
		
		//Note that is impossible to construct a Boundle with an id minor than 0,
		//thus assicure that there are no Boundle in the list with such Id.
		int targetBoundleId = -50;
		
		Boundle boundleFromRemove = shoppingCart.findBoundleById(targetBoundleId);
		
		Assert.assertNull(boundleFromRemove);
	}
	
	
	@Test
	public void testGetListOfBoundlesShouldReturnACopyOfTheListOfBoundleOfTheShopingCart(){
		ShoppingCart shoppingCart = new ShoppingCart(politicCardBoundles, assistantBoundles, permitCardBoundles);
		
		List<Boundle> listOfBoundles = new ArrayList<>();
		
		listOfBoundles.addAll(politicCardBoundles);
		listOfBoundles.addAll(permitCardBoundles);
		listOfBoundles.addAll(assistantBoundles);
		
		
		Assert.assertThat(shoppingCart.getListOfBoundles(), 
			       IsIterableContainingInOrder.contains(listOfBoundles.toArray()));
		
	}
	
	

}
