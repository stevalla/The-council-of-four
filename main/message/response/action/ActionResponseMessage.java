package it.polimi.ingsw.cg32.message.response.action;

import java.util.List;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.turn.ActionTurnMachine;
import it.polimi.ingsw.cg32.controller.turn.state.State;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.message.response.market.MarketResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.game.Game;

/**
* This is the abstarct class that is extended by all the response that carries updates
* from server-side.<br>
* Specifically {@link ActionRescpons}, {@link BonusResponse} and {@link MarketResponse}.<br>
* It carries all the modification that occured performing an {@link Action}:<br>
* - a {@link State} that indicate the next {@link State} to set in {@link ActionTurnMachine}<br>
* - the the updated version of the {@link Game}<br>
* - the list of {@link Bonus} won performing the {@link Action}<br>
* - a message that indicate the details of the operation executed<br>
* 
* @author giovanni
*
*/
public abstract class ActionResponseMessage extends ResponseMessage {

	private static final long serialVersionUID = 1L;
	private final transient State state;
	private final Game game;
	private final List<Bonus> bonus;
	private final String message;
	private final int playerId;
	
	
	/**
	* Constructor for ActionResponseMessage it provides initialization for all 
	* the fields of the response.
	* 
	* @param game the updated version of the {@link Game}
	* @param state the next {@link State} to set in {@link ActionTurnMachine}
	* @param bonuses the list of {@link Bonus} won performing the {@link Action}
	* @param message the message that indicate details of the executed operation
	* 
	* @throws NullPointerException if one of argumets game, bonuses or message is null
	*/
	public ActionResponseMessage(Game game, State state, List<Bonus> bonuses, String message, int playerId) {
		
		if(game == null || bonuses == null || message == null)
			throw new NullPointerException();
		
		this.message = message;
		this.game = game;
		this.state = state;
		this.bonus = bonuses;
		this.playerId = playerId;
	}

	
	/**
	* Get the next {@link State} to set in {@link ActionTurnMachine}
	* 
	* @return state the next {@link State} to set in {@link ActionTurnMachine}
	*/
	public State getState() {
		return state;
	}

	
	/**
	* Get the updated version of the {@link Game}
	* 
	* @return game the updated version of the {@link Game}
	*/
	public Game getGame() {
		return game;
	}

	
	/**
	* Get the list of {@link Bonus} won performing the {@link Action}
	* 
	* @return bonus the list of {@link Bonus} won performing the {@link Action}
	*/
	public List<Bonus> getBonus() {
		return bonus;
	}

	
	/**
	* Get the message that indicate details of the executed operation 
	* 
	* @return message the message that indicate details of the executed operation
	*/
	public String getMessage() {
		return message;
	}


	/**
	 * @return the id of the player that performed the {@link Action}
	 */
	public int getPlayerId() {
		return playerId;
	}
	
	
}
