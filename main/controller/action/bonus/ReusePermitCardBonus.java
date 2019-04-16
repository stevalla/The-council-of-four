package it.polimi.ingsw.cg32.controller.action.bonus;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.message.request.bonus.ReusePermitCardBonusRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent a Reuse Permit Card Bonus {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link ReusePermitCardBonusRqst} is received and validated.<br>
 * A PermitCard Bonus get to the player the possibility to get the bonus
 * of one of his permit cards.
 * 
 * @author Stefano
 *
 */
public class ReusePermitCardBonus extends BonusAction {

	private final Game game;
	private final PermitCard permitCard;
	private final Bonus bonus;
	
	/**
	 * Construct a ReusePermitCardBonus.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param permitCard the selecte {@link PermitCard}
	 * @param bonus the bonus to execute
	 * 
	 * @see Action 
	 * @see BonusAction
	 */
	public ReusePermitCardBonus(Player player, Game game, PermitCard permitCard, Bonus bonus) {
		super(player);
		this.game = game;
		this.permitCard = permitCard;
		this.bonus = bonus;
	}
	
	
	@Override
	public ActionResponseMessage execute() {
		
		return createResponse();
	}


	@Override
	public ActionResponseMessage createResponse() {
		
		String message = "Player#" + player.getId() + " perform an REUSEPERMITCARDBONUS.\n"
				+ "He reuse " + permitCard + ".\n";
		
		return new BonusResponse(game,  BonusState.getInstance(), permitCard.getBonus(), bonus, message, player.getId());
	}

}
