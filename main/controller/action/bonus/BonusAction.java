package it.polimi.ingsw.cg32.controller.action.bonus;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This abstract class implements {@link Action} and offer
 * an abstract implementation for an Action Bonus.
 * 
 * @author Stefano
 *
 */
public abstract class BonusAction implements Action {

	protected final Player player;
	
	/**
	 * @param player the player that execute the bonus action
	 */
	public BonusAction(Player player) {
		this.player = player;
	}
}
