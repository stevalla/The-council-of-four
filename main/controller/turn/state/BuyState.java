package it.polimi.ingsw.cg32.controller.turn.state;

import it.polimi.ingsw.cg32.controller.Market;
import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.market.MarketBuy;
import it.polimi.ingsw.cg32.controller.action.market.MarketSell;
import it.polimi.ingsw.cg32.controller.turn.MarketTurnMachine;
import it.polimi.ingsw.cg32.model.market.Boundle;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;

/**
* This class is a Singleton used to models the phase of the turn where the Players
* can buy {@link Boundle}s from the Market.<br>
* This State is used by the MarketTurnMachine.<br>
* Once every player have performed their {@link MarketSell} the MarketTurnMachine
* sets its current state to BuyState. <br>
* When MarketTurnMachine has its current state set to BuState it only permitted
* to execute {@link MarketBuy} as specificated in {@link #isActionValid(Action)}.<br> 
* 
* @author giovanni
* 
* @see Market
* @see MarketTurnMachine
*
*/
public class BuyState implements State {

	private static BuyState instance = new BuyState();

	/**
	* Private constructor to create an instance of BuyState, it is 
	* invoke just one time. 
	*/
	private BuyState() {}

	/**
	* Get the instance of the BuyState.
	* 
	* @return instance the instance of the BuyState.
	*/
	public static BuyState getInstance() {
		return instance; 
	}

	@Override
	public boolean isActionValid(Action action) throws IllegalStateMachineException {
	
		if(!(action instanceof MarketBuy))
			throw new IllegalStateMachineException("you have to buy something from market in this phase of your turn!");

		return  true;
	}	
}
