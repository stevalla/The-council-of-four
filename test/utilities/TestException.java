package it.polimi.ingsw.cg32.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.utilities.exception.IllegalInputException;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;

public class TestException {

	IllegalActionException actionException;
	IllegalStateMachineException machineException;
	IllegalInputException inputException;
	
	@Test
	public void testConstructIllegalActionExceptionShouldReturnTheMessage() {
		String errorAction = "Error action";
		String errorState = "Error state";
		String errorInput = "Error input";
		actionException = new IllegalActionException(errorAction);
		machineException = new IllegalStateMachineException(errorState);
		inputException = new IllegalInputException(errorInput);
		assertEquals(errorAction, actionException.getMessage());
		assertEquals(errorInput, inputException.getMessage());
		assertEquals(errorState, machineException.getMessage());
	}
}
