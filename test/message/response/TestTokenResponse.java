package it.polimi.ingsw.cg32.message.response;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.view.client.cli.CliMessageVisitor;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestTokenResponse {

	
	private static CliMessageVisitor visitor; 
	private static Token token;
	
	@BeforeClass
	public static void initTest(){
		
		visitor = new CliMessageVisitor();
		token = new Token();
	}
	
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfTokensNull() {
		
		new TokenResponse(null);
	}
	
	@Test
	public void testGetTokenShouldReturnTheTokenPassAsParam(){
		
		
		TokenResponse response = new TokenResponse(token);
		Assert.assertEquals(token, response.getToken());
	}
	
	@Test
	public void testDisplayeShouldSetTheTokenItCarriesToTheTokenInTheVisitor() {

		TokenResponse response = new TokenResponse(token);		
		response.display(visitor);
		Assert.assertEquals(visitor.getToken(), response.getToken());
	}

}
