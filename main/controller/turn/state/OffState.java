package it.polimi.ingsw.cg32.controller.turn.state;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.turn.ActionTurnMachine;
import it.polimi.ingsw.cg32.controller.turn.MarketTurnMachine;
import it.polimi.ingsw.cg32.controller.turn.TurnMachine;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;


/**
* This class is a Singleton used to models the state of a TurnMachine when
* it is unavaiable.<br>
* It implements State as every other State used by TurnMachines.<br>
* This State is used as well in {@link ActionTurnMachine} as {@link MarketTurnMachine}.<br>
* It is setted when target TurnMachine as to avoid all action to be rejected when
* the other TurnMachine is operating.<br>
* 
* @author giovanni
* 
* @see TurnMachine
* @see State
*
*/
public class OffState implements State {

	private static OffState instance = new OffState();

	/**
	* Private constructor to create an instance of OffState, it is 
	* invoke just one time. 
	*/
	private OffState() {}

	/**
	* Get the instance of the OffState.
	* 
	* @return instance the instance of the OffState.
	*/
	public static OffState getInstance() {
		return instance; 
	}

	@Override
	public boolean isActionValid(Action action) throws IllegalStateMachineException {
	
		throw new IllegalStateMachineException("Not permitted to execute this action.");
	}	

}
