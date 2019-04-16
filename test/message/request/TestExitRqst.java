package it.polimi.ingsw.cg32.message.request;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import it.polimi.ingsw.cg32.view.server.Token;

public class TestExitRqst {

	ExitRqst request;
	
	@Test
	public void testConstructWhenTokenIsNullShouldReturnAConnectionRqstWithANullToken() {
		
		request = new ExitRqst(null);
		assertNull(request.getToken());
	}
	
	@Test
	public void testCnstructWhenTokenIsNotNullShouldReturnAConnectionRqstWithANotNullToken() {
		Token token = new Token();
		request = new ExitRqst(token);
		assertNotNull(request.getToken());
	}
}
