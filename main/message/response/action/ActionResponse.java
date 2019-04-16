package it.polimi.ingsw.cg32.message.response.action;

import java.util.List;

import it.polimi.ingsw.cg32.controller.turn.ActionTurnMachine;
import it.polimi.ingsw.cg32.controller.turn.state.State;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;

/**
* This class is one of the concretes implementation of {@link ActionResponseMessage}.<br>
* It is the response associated with all the main {@link Action} of the game (primary and secondary actions).<br>
* 
* 
* @author giovanni
*
*/
public class ActionResponse extends ActionResponseMessage {

	
	private static final long serialVersionUID = 1L;

	/**
	* Constructor for ActionResponse, provides initialization of all the fields using
	* the super-constructor of {@link ActionResponseMessage}.
	* 
	* @param game the updated version of the {@link Game}
	* @param state the next {@link State} to set in {@link ActionTurnMachine}
	* @param bonuses the list of {@link Bonus} won performing the {@link Action}
	* @param message the message that indicate details of the executed operation
	*/
	public ActionResponse(Game game, State state, List<Bonus> bonuses, String message, int playerId) {
		super(game, state, bonuses, message, playerId);
	}

	
	@Override
	public void display(ResponseVisitor visitor) {
		
		super.display(visitor);
		
		visitor.display(this);
	}

}
