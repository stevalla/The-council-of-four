package it.polimi.ingsw.cg32.model.card;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.track.Space;

@RunWith(value = Parameterized.class)
public class TestBonusKingCard {

	BonusKingCard card;
	private QuantityBonus bonus;
	private String name;
	private int n;
	private Player player;
	
	public TestBonusKingCard(String name, int n) {
		this.name = name;
		this.n = n;
	}
	
	@Parameters(name = "{index}: quantityBonus({0}{1})")
	public static Iterable<Object[]> data() {
	return Arrays.asList(new Object[][] {
	{ "VICTORYBONUS", 1 },
	{ "VICTORYBONUS", 2 },
	{ "VICTORYBONUS", 3 },
	{ "VICTORYBONUS", 10 },
	{ "VICTORYBONUS", 15 },
	{ "VICTORYBONUS", 20 },
	{ "VICTORYBONUS", 30 },
	{ "VICTORYBONUS", 45 },
	{ "VICTORYBONUS", 78 },
	{ "VICTORYBONUS", 82 },
	{ "VICTORYBONUS", 93 },
	{ "VICTORYBONUS", 99 }
	});
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructABonusKingCardWhenArgumentIsNullShouldThrowANullPointerException() {
		card = new BonusKingCard(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructABonusKingCardWithADifferentBonusThatVictoryBonusThrowAnIllegalArgumentExceptoin () {
		
		card = new BonusKingCard(new QuantityBonus("differentBonus", 3));
	}
	
	@Test(expected = NullPointerException.class)
	public void testUseCardWhenArgumentIsNullShouldThrowANullPointerException() {
		
		card = new BonusKingCard(new QuantityBonus("VICTORYBONUS", 2));
		card.useCard(null);
	}
	
	@Before
	public void initializePlayerForTest() {
		Set<Space> spaces = new HashSet<>();
		spaces.add(new Space(0));
		Game game = new Game("src/main/resources/map/default.txt", 4);
		player = game.getPlayers().get(0);
	}
	
	@Test
	public void testUseCardShouldSumTheQuantityOfBonusToVictoryPointsOfThePlayer() {
		
		card = new BonusKingCard(new QuantityBonus(name, n));
		
		int quantityBonus = card.getBonus().getQuantity();
		int victoryPointsPlayer = player.getVictoryTrack().getPoints();
		
		card.useCard(player);
		
		assertEquals(quantityBonus + victoryPointsPlayer, player.getVictoryTrack().getPoints());
	}
	
	@Test
	public void testGetterBonusKingCardShouldReturnTheBonusOfTheCard() {
		bonus = new QuantityBonus(name, n);
		card = new BonusKingCard(bonus);
		
		assertEquals(bonus, card.getBonus());
	}
	
}
