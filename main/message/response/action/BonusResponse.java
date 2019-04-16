package it.polimi.ingsw.cg32.message.response.action;

import java.util.List;

import it.polimi.ingsw.cg32.controller.turn.ActionTurnMachine;
import it.polimi.ingsw.cg32.controller.turn.state.State;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;

/**
* This class is one of the concretes implementation of {@link ActionResponseMessage}.<br>
* It is the response associated with all the {@link Action} that executes Bonus.<br>
* It contains all the fields of the super-class {@link ActionResponseMessage}.
* In addition it carries the Bonus that was performed, in order to remove it from the 
* list of Bonus still to execute after the {@link Action} was performed.<br> 
* 
* @author giovanni
* 
* @see Bonus
*
*/
public class BonusResponse extends ActionResponseMessage {

	
	private static final long serialVersionUID = 1L;
	private final Bonus typeOfBonus;

	/**
	* Constructor for ActionResponse, provides initialization of all the fields using
	* the super-constructor of {@link ActionResponseMessage}.
	* 
	* @param game the updated version of the {@link Game}
	* @param state the next {@link State} to set in {@link ActionTurnMachine}
	* @param bonuses the list of {@link Bonus} won performing the {@link Action}
	* @param typeOfBonus the Kind of {@link Bonus} that was executed by the {@link Action} that generates this response
	* @param message the message that indicate details of the executed operation
	* 
	* @throws NullPointerException if typeOfBonus is null
	*/
	public BonusResponse(Game game, State state, List<Bonus> bonuses, Bonus typeOfBonus, String message, int playerId) {
		super(game, state, bonuses, message, playerId);
		
		if(typeOfBonus == null)
			throw new NullPointerException("typeOfBonus can't be null");
		
		this.typeOfBonus = typeOfBonus;
	}

	
	/**
	* Get typeOfBonus the Kind of {@link Bonus} that was executed by the {@link Action} that generates this response
	*
	* @return typeOfBonus the Kind of {@link Bonus} that was executed by the {@link Action} that generates this response
	*/
	public Bonus getTypeOfBonus() {
		return typeOfBonus;
	}

	@Override
	public void display(ResponseVisitor visitor) {
		
		super.display(visitor);
		
		visitor.display(this);
	}

}
