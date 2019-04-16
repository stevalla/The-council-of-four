package it.polimi.ingsw.cg32.message.request.bonus;

import java.io.Serializable;

import it.polimi.ingsw.cg32.message.request.ActionRqst;
import it.polimi.ingsw.cg32.message.request.action.MainActionRqst;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This abstract class extends {@link ActionRqst} with the two method to create
 * an action bonus Overrides by its subclasses.<br>
 * Offer a method to get the {@link Bonus} of the request.
 * Implement {@link Serializable}
 * @author Stefano
 *
 */
public abstract class BonusRqst extends MainActionRqst {

	private static final long serialVersionUID = 1L;
	private final Bonus bonus;
	
	/**
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param bonus the {@link Bonus} to execute
	 * @throws NullPointerException if arguments are null
	 */
	public BonusRqst(Token token, Bonus bonus) {
		super(token);
		
		if(bonus == null || token == null)
			throw new NullPointerException("Arguments token and bonus can't be null");
		
		this.bonus = bonus;
	}


	/**
	 * @return the {@link Bonus} of the request
	 */
	public Bonus getBonus() {
		return bonus;
	}
}
