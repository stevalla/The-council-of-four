package it.polimi.ingsw.cg32.controller.action.bonus;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.message.request.bonus.PrimaryActionBonusRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent a Primary action Bonus {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link PrimaryActionBonusRqst} is received and validated.<br>
 * A Primary action Bonus permit the player to do another 
 * primary action, note that should be passed in
 * the constructor the primary action to execute.
 * 
 * @author Stefano
 *
 */
public class PrimaryActionBonus extends BonusAction {

	private final Game game;
	private final Action action;
	private final Bonus bonus;
	private ActionResponseMessage response;
	
	/**
	 * Construct a PrimaryActionBonusBonus.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param action the primary {@link Action} to execute
	 * @param bonus the bonus to execute
	 * 
	 * @see Action 
	 * @see BonusAction
	 */
	public PrimaryActionBonus(Player player, Game game, Action action, Bonus bonus) {
		super(player);
		this.game = game;
		this.action = action;
		this.bonus = bonus;
	}
	
	
	@Override
	public ActionResponseMessage execute() {

		response  = action.execute();
			
		return createResponse();
	}


	@Override
	public ActionResponseMessage createResponse() {
		
		String message = "Player#" + player.getId() + " perform an PRIMARYACTIONBONUS.\n"
			           + "He perform a " + action.getClass().getSimpleName() + " action.\n"
			           + response.getMessage();
		
		return new BonusResponse(game, BonusState.getInstance(), response.getBonus(), bonus, message, player.getId());
	}

}
