package it.polimi.ingsw.cg32.controller.turn.state;

import it.polimi.ingsw.cg32.controller.Market;
import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.market.MarketSell;
import it.polimi.ingsw.cg32.controller.turn.MarketTurnMachine;
import it.polimi.ingsw.cg32.model.market.Boundle;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;


/**
* This class is a Singleton used to models the phase of the turn where the Players
* have to sell {@link Boundle}s in the Market.<br>
* This State is used by the MarketTurnMachine.<br>
* Once the Market is created, MarketTurnMachine set this State as its starting
* phase.
* When MarketTurnMachine has its current state set to SellState it only permitted
* to execute {@link MarketSell} as specificated in {@link #isActionValid(Action)}.<br> 
* 
* @author giovanni
* 
* @see Market
* @see MarketTurnMachine
*
*/
public class SellState implements State {

	private static SellState instance = new SellState();

	/**
	* Private constructor to create an instance of SellState, it is 
	* invoke just one time. 
	*/
	private SellState() {}

	/**
	* Get the instance of the SellState.
	*  
	* @return instance the instance of the SellState.
	*/
	public static SellState getInstance() {
		return instance;
	}
	
	@Override
	public boolean isActionValid(Action action) throws IllegalStateMachineException {

		if(!(action instanceof MarketSell))
			throw new IllegalStateMachineException("you have to sell something from market in this phase of your turn!");

		return  true;
	}
	
}
