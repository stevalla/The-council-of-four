package it.polimi.ingsw.cg32.model.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.Colore;

public class TestPoliticCardProrotype {

	PoliticCardPrototype prototype;
	
	@Test
	public void testCatchAllTypeOfPolitiCardWithGetterMethodInManyLoops() {
		
		prototype = new PoliticCardPrototype();
		List<Colore> colors = new ArrayList<>(Arrays.asList(Colore.values()));
		
		for(;;) {
			PoliticCard card = prototype.getPoliticCard();
			if(colors.contains(card.getCardColor()))
				colors.remove(card.getCardColor());
			if(colors.isEmpty())
				return;
		}
			
	}
}
