package it.polimi.ingsw.cg32.model.card;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.DeckOfPermitCard;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.color.RegionType;

public class TestDeckOfPermitCard {

	DeckOfPermitCard deck;
	private static List<PermitCard> cards;
	private int counter = 0;
	private PermitCard cardToDraw;
	private PermitCard cardToDraw2;
	
	@BeforeClass
	public static void loadPermitCards() {
		
		cards = new ArrayList<>();
		//Create list of bonus for permit card constructor
		List<Bonus> bonus = new ArrayList<>();
		bonus.add(new SingleBonus("bonus"));
		//Create list of cities for permit card constructor
		List<City> cities = new ArrayList<>();
		cities.add(new City("city", RegionType.COAST, CityColor.BRONZE, new ArrayList<>()));
		
		for(int i=0; i<20; i++)
			cards.add(new PermitCard(Integer.toString(i), cities, bonus));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructADeckPermitCardWhenArgumentIsNullShouldThrowAnIllegalArgumentException() {
		deck = new DeckOfPermitCard(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructADeckPermitCardWhenArgumentIsAnEmptyListShouldThrowAnIllegalArgumentException() {
		deck = new DeckOfPermitCard(new ArrayList<>());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructDeckOfPermitCardsWhenListOfCardsAreLessThanTwoShouldThrowAnIllegalArgumentException() {
		
		List<PermitCard> permitCards = new ArrayList<>();
		permitCards.add(cards.get(0));
		deck = new DeckOfPermitCard(permitCards);
	}
	
	@Test
	public void testDrawArandomPermitCardShouldNotChangeTheSizeOfTheDeck() {
		
		deck = new DeckOfPermitCard(cards);
		int expected = deck.size();
		deck.drawRandomPermitCard();
		assertTrue(expected == deck.size());
	}
	
	@Before
	public void initializeEmptyDeck() {
		deck = new DeckOfPermitCard(cards);
		deck.getDeck().clear();
		cardToDraw = deck.getUsableCard().get(0);
		cardToDraw2 = deck.getUsableCard().get(1);
		
		deck.drawUsableCard(cardToDraw);
		deck.drawUsableCard(cardToDraw2);
	}
	
	@Test
	public void testDrawArandomPermitCardShouldReturnNullIfTheDeckIsEmpty() {
		
		PermitCard permitCard = deck.drawRandomPermitCard();
		assertTrue(permitCard == null);
	}
	
	@Test(expected = IndexOutOfBoundsException.class)
	public void testDrawAPermiCardWhenTheDeckIsEmptyShouldThrowAnIndexOutOfBoundException() {
		
		deck.getUsableCard().get(0);
	}
	
	@Test(expected = NullPointerException.class)
	public void testDrawAPermitCardWhenArgumentIsNullShouldThrowANullPointerException() {
		
		deck = new DeckOfPermitCard(cards);
		
		deck.drawUsableCard(null);
	}
	
	@Test
	public void testDrawApermitCardShouldGiveMeOneOfTheUsableCards() {
		
		deck = new DeckOfPermitCard(cards);
		PermitCard firstUsableCard = deck.getUsableCard().get(0);
		PermitCard card = deck.drawUsableCard(firstUsableCard);
		
		assertTrue(firstUsableCard.getId()==card.getId());
		
		PermitCard secondUsableCard = deck.getUsableCard().get(1);
		card = deck.drawUsableCard(secondUsableCard);
		
		assertTrue(secondUsableCard.getId()==card.getId());
	}
	
	@Test
	public void testDrawAPermitCardWHenArgumentIsNotAnUsableCardsShouldThrowAnIndexOutOfBoundException() {
		
		deck = new DeckOfPermitCard(cards);
		
		PermitCard cardNotUsable = deck.getDeck().peek();
		
		assertNull(deck.drawUsableCard(cardNotUsable));
	}
	
	@Test
	public void testChangeUsableCardShouldEnsureThatUsableCardsAreChange() {
		
		deck = new DeckOfPermitCard(cards);
		List<PermitCard> permitCards = new ArrayList<>(deck.getUsableCard());
		deck.changeUsableCard();
		
		for(PermitCard permitCard : deck.getUsableCard()) {
			assertThat(permitCard.getId(), is(not(permitCards.get(0).getId())));
			assertThat(permitCard.getId(), is(not(permitCards.get(1).getId())));
		}
	}
	
	@Test
	public void testChangeUsableCardShouldChangeTheTwoOldUsableCardAreNowInTheDeck() {
		
		deck = new DeckOfPermitCard(cards);
		List<PermitCard> permitCards = new ArrayList<>(deck.getUsableCard());
		deck.changeUsableCard();

		for(PermitCard permitCard : permitCards) {
			deck.getDeck().stream()
						  .filter(p -> p.getId() == permitCard.getId())
						  .forEach(p -> counter++);
		}
		assertEquals(2, counter);
	}
	
	@Test(expected = IllegalStateException.class)
	public void testChangeUsableCardWhenOneOrMoreOfUsableCardsAreNullShouldThrowAnException() {
		deck = new DeckOfPermitCard(cards);
		deck.getDeck().clear();
		deck.drawUsableCard(deck.getUsableCard().get(0));
		
		deck.changeUsableCard();
	}
}
