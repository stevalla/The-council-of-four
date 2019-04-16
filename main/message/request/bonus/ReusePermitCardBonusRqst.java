package it.polimi.ingsw.cg32.message.request.bonus;

import java.io.Serializable;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.bonus.ReusePermitCardBonus;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link BonusRqst} and create a request
 * to execute an {@link ReusePermitCardBonus} action controlling its validation.<br>
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public class ReusePermitCardBonusRqst extends BonusRqst {

	private static final long serialVersionUID = 1L;
	private final String permitCardId;
	private PermitCard permitCard;
	
	
	/**
	 * Construct a ReusePermitCardBonusRqst using the super constructor
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param permitCardId the id of the {@link Player}'s permitCards to use for the bonus
	 * @param bonus the {@link Bonus} to execute
	 * @throws NullPointerException if token or bonus are null
	 * @see PermitCard
	 */
	public ReusePermitCardBonusRqst(Token token, String permitCardId, Bonus bonus) {
		super(token, bonus);
		this.permitCardId = permitCardId;
	}

	
	@Override
	public Action accept(Game game, Player player, ActionVisitor visitor) throws IllegalActionException {
		
		super.accept(game, player, visitor);
		
		return isValid(game, player) ? visitor.visit(this, player, game) : null;
	}

	
	@Override
	protected boolean isValid(Game game, Player player) throws IllegalActionException {
		
		super.isValid(game, player);
		
		/*
		 * Control if the bonus is a SingleBonus and the name must be equals to REUSEPERMITCARDBONUS
		 */
		if(!("REUSEPERMITCARDBONUS".equals(getBonus().getName()) && getBonus() instanceof SingleBonus)) 
			throw new IllegalActionException("ILLEGAL ACTION!");
		
		/*
		 * Check if the permit card provided by client's rqst is really 
		 * one fo the player's cards.
		 */
		player.getPermitCards().stream()
					.filter(c -> c.getId().equals(permitCardId))
					.forEach(c -> this.permitCard = c);
		if(!player.getPermitCards().contains(permitCard)) 
			throw new IllegalActionException("The selected permit card is not yours.");

		return true;
	}

	
	/**
	 * @return the selected permit card
	 */
	public PermitCard getTargetPermitCard() {
		return permitCard;
	}
}
