package it.polimi.ingsw.cg32.model.track;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;

public class TestSpace {

	Space space;
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructASpaceWithANumberMinorThanZeroShouldThrowAnIllegalArgumentExeption() {
		
		space = new Space(-1);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConstructASpaceWithANumberMinorThanZeroAndAListOfBonusShouldThrowAnIllegalArgumentExeption() {
		
		space = new Space(-1, new ArrayList<>());
	}
	
	@Test
	public void testConstructASpaceWithOnlyANumberAsParamShouldGoCorrectly() {
		
		space = new Space(1);
		return;
	}
	
	@Test
	public void testConstructASpaceWithANumberAndAnEmptyListOfBonusAsParamShouldGoCorrectly() {
		
		space = new Space(1, new ArrayList<>());
		return;
	}
	
	@Test
	public void testConstructASpaceWithANumberAndAListOfBonusAsParamAndGetTheList() {
		List<Bonus> bonus = new ArrayList<>();
		bonus.add(new SingleBonus("bonus"));
		space = new Space(1, bonus);
		
		assertEquals(bonus, space.getBonus());
	}
}
