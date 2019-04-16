package it.polimi.ingsw.cg32.message.request;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import it.polimi.ingsw.cg32.view.server.Token;

public class TestConnectionRqst {

	ConnectionRqst request;
	
	@Test
	public void testConstructWhenTokenIsNullShouldReturnAConnectionRqstWithANullToken() {
		
		request = new ConnectionRqst(null);
		assertNull(request.getToken());
	}
	
	@Test
	public void testConstructWhenTokenIsNotNullShouldReturnAConnectionRqstWithANotNullToken() {
		Token token = new Token();
		request = new ConnectionRqst(token);
		assertNotNull(request.getToken());
	}
}
