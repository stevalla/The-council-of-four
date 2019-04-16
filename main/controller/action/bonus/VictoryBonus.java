package it.polimi.ingsw.cg32.controller.action.bonus;

import java.util.Collections;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.message.request.bonus.VictoryBonusRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent a Victory Bonus {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link VictoryBonusRqst} is received and validated.<br>
 * A Victory Bonus allows you to get so many victory points
 * as the amount of the bonus.
 * 
 * @author Stefano
 *
 */
public class VictoryBonus extends BonusAction {

	private final Game game;
	private final QuantityBonus bonus;
	
	/**
	 * Construct a Victory Bonus.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param bonus the bonus to execute
	 * 
	 * @see Action 
	 * @see BonusAction
	 */
	public VictoryBonus(Player player, Game game, QuantityBonus bonus) {
		super(player);
		this.game = game;
		this.bonus = bonus;
	}
	
	
	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Add victory points to the player
		 */
		player.getVictoryTrack().setPoints(bonus.getQuantity());
		
		return createResponse();
	}


	@Override
	public ActionResponseMessage createResponse() {
		
		String message = "Player#" + player.getId() + " perform a VICTORYBONUS.\n"
				+ "He currently has " + player.getVictoryTrack().getPoints() + " victory points.\n";
		
		return new BonusResponse(game,  BonusState.getInstance(), Collections.emptyList(), bonus, message, player.getId());
	}

}
