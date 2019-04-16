package it.polimi.ingsw.cg32.controller.action.bonus;

import java.util.Collections;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.message.request.bonus.AssistantBonusRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent a Coins Bonus {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link AssistantBonusRqst} is received and validated.<br>
 * A Coins Bonus allows you to get so many coins as the amount 
 * of the bonus.
 * 
 * @author Stefano
 *
 */
public class CoinsBonus extends BonusAction {
	
	private final Game game;
	private final QuantityBonus bonus;
	
	/**
	 * Construct a Coins Bonus.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param bonus the bonus to execute
	 * 
	 * @see Action 
	 * @see BonusAction
	 */
	public CoinsBonus(Player player, Game game, QuantityBonus bonus) {
		super(player);
		this.game = game;
		this.bonus = bonus;
	}
	
	
	@Override
	public ActionResponseMessage execute() {

		/*
		 * Set the coins win to the player
		 */
		player.getCoinsTrack().setCoinsNumber(bonus.getQuantity());
		
		return createResponse();
	}

	@Override
	public ActionResponseMessage createResponse() {
		
		String message = "Player#" + player.getId() + " perform a COINSBONUS.\n"
				+ "He currently has " + player.getCoinsTrack().getCurrentCoins() + " coins.\n";
		
		return new BonusResponse(game,  BonusState.getInstance(), Collections.emptyList(), bonus, message, player.getId());
	}

}
