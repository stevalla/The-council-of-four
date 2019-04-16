package it.polimi.ingsw.cg32.message.request;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.view.server.Token;

public class TestChangeMapRqst {

	private Token token;
	
	@Before
	public void initTest(){
		token = new Token();
	}
	
	@Test
	public void testGetChoosenMapShouldReturnTheNameOfTheMapPassAsParamInitsConstruction() {
		ChangeMapRqst request = new ChangeMapRqst(token, "Pippo");
		Assert.assertEquals(request.getChooseMap(), "Pippo");
	}

}
