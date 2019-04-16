package it.polimi.ingsw.cg32.message.request.bonus;

import java.io.Serializable;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.bonus.PrimaryActionBonus;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByKingHelp;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuyPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.ElectCouncillor;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByKingHelpRqst;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.BuyPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.ElectCouncillorRqst;
import it.polimi.ingsw.cg32.message.request.action.MainActionRqst;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link BonusRqst} and create a request
 * to execute an {@link PrimaryActionBonus} action controlling its validation.<br>
 * <p>
 * Remember that there are four primary action:<br>
 * -{@link BuyPermitCard}<br>
 * -{@link BuildEmporiumByKingHelp}<br>
 * -{@link BuilEmporiumByPermitCard}<br>
 * -{@link ElectCouncillor}<br>
 * </p>
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public class PrimaryActionBonusRqst extends BonusRqst {

	private static final long serialVersionUID = 1L;
	private final MainActionRqst primaryRqst;
	private transient Action primaryAction;
	private transient ActionVisitor visitor;
	
	/**
	 * Construct a PoliticCardBonusRqst using the super constructor
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param primaryRqst the primary {@link MainActionRqst} to execute
	 * @param bonus the {@link Bonus} to execute
	 * @throws NullPointerException if token or bonus are null
	 */
	public PrimaryActionBonusRqst(Token token, MainActionRqst primaryRqst, Bonus bonus) {
		super(token, bonus);
		this.primaryRqst = primaryRqst;
	}

	
	@Override
	public Action accept(Game game, Player player, ActionVisitor visitor) throws IllegalActionException {
		
		super.accept(game, player, visitor);
		
		this.visitor = visitor;
		
		return isValid(game, player) ? visitor.visit(this, player, game) : null;
	}
	
	
	@Override
	protected boolean isValid(Game game, Player player) throws IllegalActionException {
		
		super.isValid(game, player);
		
		if(!("PRIMARYACTIONBONUS".equals(getBonus().getName()) && getBonus() instanceof SingleBonus)) 
			throw new IllegalActionException("ILLEGAL ACTION!");

		if(!(primaryRqst instanceof BuildEmporiumByKingHelpRqst ||
			 primaryRqst instanceof BuildEmporiumByPermitCardRqst ||
			 primaryRqst instanceof ElectCouncillorRqst ||
			 primaryRqst instanceof BuyPermitCardRqst)) 
			 throw new IllegalActionException("You have to perform a primary action.");
		
		primaryAction = primaryRqst.accept(game, player, visitor);
		
		return true;
	}


	/**
	 * @return the primary {@link Action} choice by the {@link Player}
	 */
	public Action getPrimaryAction() {
		return primaryAction;
	}
}
