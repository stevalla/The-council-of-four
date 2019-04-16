package it.polimi.ingsw.cg32.message.request.bonus;

import java.io.Serializable;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.bonus.PoliticCardBonus;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link BonusRqst} and create a request
 * to execute an {@link PoliticCardBonus} action controlling its validation.<br>
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public class PoliticCardBonusRqst extends BonusRqst {

	private static final long serialVersionUID = 1L;

	/**
	 * Construct a PoliticCardBonusRqst using the super constructor
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param bonus the {@link Bonus} to execute
	 * @throws NullPointerException if arguments are null
	 */
	public PoliticCardBonusRqst(Token token, Bonus bonus) {
		super(token, bonus);
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
		 * Control if the bonus is a QuantityBonus and the name must be equals to POLITICCARDBONUS
		 */
		if(!("POLITICCARDBONUS".equals(getBonus().getName()) && getBonus() instanceof QuantityBonus)) 
			throw new IllegalActionException("ILLEGAL ACTION!");
		
		return true;
	}

}
