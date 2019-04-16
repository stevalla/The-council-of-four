package it.polimi.ingsw.cg32.model.card;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;

public class TestPoliticCard {

	PoliticCard card;
	private PoliticCardPrototype prototype;
	
	@Test(expected = NullPointerException.class)
	public void testConstructAPoliticCardWhenArgumentIsNullShouldThrowANullPointerException() {
		card = new PoliticCard(null);
	}
	
	@Before
	public void initializaPoliticCardPrototypeToGetPoliticCardAndUseItInCopyConstructor() {
		prototype = new PoliticCardPrototype();
	}
	
	@Test
	public void testConstructAPoliticCardWithACopyCOnstructShouldReturnAnotherPoliticCardWithTheSameColor() {
		card = prototype.getPoliticCard();
		
		PoliticCard card2 = new PoliticCard(card);
		
		assertEquals(card.getCardColor(), card2.getCardColor());
	}
}
