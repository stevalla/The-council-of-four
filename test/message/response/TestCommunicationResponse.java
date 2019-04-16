package it.polimi.ingsw.cg32.message.response;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class TestCommunicationResponse {
	
	@Rule
	public final SystemOutRule system = new SystemOutRule().enableLog().muteForSuccessfulTests();
	
	@Test (expected = NullPointerException.class)
	public void testDisplayShouldThrowNullPointerExceptionIfTheVisitorPassAsParamIsNull() {

		CommunicationResponse response = new CommunicationResponse("test");
		response.display(null);
	}
	
	@Test
	public void testGetMessageShouldTReturnTheMessagePassAsParam() {

		CommunicationResponse response = new CommunicationResponse("test");
		Assert.assertEquals("test", response.getMessage());
	}
	
	@Test (expected = NullPointerException.class)
	public void testCreatorShouldThrowNullPointerExceptionIfMessageIsNull() {

		new CommunicationResponse(null);
	}
	
//	@Test 
//	public void testDisplayShouldDisplayIfTheVisitorPassAsParam() {
//		ResponseVisitor visitor = new CliMessageVisitor();
//		CommunicationResponse response = new CommunicationResponse("test");
//		response.display(visitor);
//		assertTrue(response.getMessage().equals(system.getLog()));
//	}
	

	
}
