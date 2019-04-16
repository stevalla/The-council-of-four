package it.polimi.ingsw.cg32.message.response;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Test;

import it.polimi.ingsw.cg32.view.client.cli.CliMessageVisitor;

public class TestExitGameResponse {
	
	@Test
	public void testDisplayeShouldDoNothing() {

		CliMessageVisitor visitor;
		visitor = new CliMessageVisitor();
		
	    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    
		ExitGameResponse response = new ExitGameResponse();
		response.display(visitor);
		String output = new String(outContent.toByteArray());
		
		Assert.assertTrue(output.isEmpty());
	}

}
