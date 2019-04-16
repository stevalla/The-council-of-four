package it.polimi.ingsw.cg32.controller.turn;

import it.polimi.ingsw.cg32.controller.Market;
import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.turn.state.BuyState;
import it.polimi.ingsw.cg32.controller.turn.state.SellState;
import it.polimi.ingsw.cg32.controller.turn.state.State;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;

/**
 * TurnMachine for market with two {@link State}:
 * -{@link SellState} the first state of the market phase,
 *  in this state players one by one can put on sale
 *  their boundles.
 * -{@link BuyState} the second state of the market phase,
 *  in this state all players can buy the boundles in the 
 *  {@link Market} in every moment of the phase.
 * Before the execution every state check if the action is valid
 * in that state.
 *  
 * @author Stefano
 *
 */
public class MarketTurnMachine extends TurnMachine{
	
	/**
	 * Crate a Market Turn Machine
	 */
	public MarketTurnMachine() {
		this.currentState = SellState.getInstance();
	}

	@Override
	public ActionResponseMessage executeAction(Action action) throws IllegalStateMachineException {
		
		return currentState.isActionValid(action) 
				
				? currentState.executeAction(action)
						
				: null;
	}
	
}
