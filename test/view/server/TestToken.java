package it.polimi.ingsw.cg32.view.server;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

public class TestToken {

	Token token;
	
	@Test
	public void testCreateTokenShouldReturnAnUUIDToString() {
		token = new Token();
		assertTrue(token.getUuid() instanceof String);
	}
	
	@Test
	public void testCreateTokenShouldInitializeTheTokenWithADefaultNumber999() {
		token = new Token();
		assertTrue(token.getPlayerNumber() == 999);
	}
	
	@Test
	public void testSetPlayerNumberShouldChangeThePlayerNumberToTheNumberPassedAsParam() {
		token = new Token();
		int number = 1;
		token.setPlayerNumber(number);
		assertTrue(token.getPlayerNumber() == number);
	}
	
	@Test
	public void testIsAdministratorShouldIsFalseAfterInitialize() {
		token = new Token();
		assertFalse(token.isAdministrator());
	}
	
	@Test
	public void testIsAdministratorAfterSetItTrueShouldReturnTrue() {
		token = new Token();
		token.setAdministrator(true);
		assertTrue(token.isAdministrator());
	} 
	
	@Test
	public void testEqualsShouldreturnFalseComparingAny2DifferentToken(){
		
		Assert.assertFalse(new Token().equals(new Token()));
	}
}
