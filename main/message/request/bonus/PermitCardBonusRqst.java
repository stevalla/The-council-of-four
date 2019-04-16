package it.polimi.ingsw.cg32.message.request.bonus;

import java.io.Serializable;
import java.util.Arrays;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.bonus.PermitCardBonus;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link BonusRqst} and create a request
 * to execute an {@link PermitCardBonus} action controlling its validation.<br>
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public class PermitCardBonusRqst extends BonusRqst {

	private static final long serialVersionUID = 1L;
	private PermitCard cardToDraw;
	private NormalRegion targetRegion;
	private final String permitCardId;
	
	/**
	 * Construct a PermitCardBonusRqst using the super constructor
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param permitCardId the id of the permitCard to use for the bonus
	 * @param targetRegion the {@link NormalRegion} where the card is located
	 * @param bonus the {@link Bonus} to execute
	 * @throws NullPointerException if token or region or bonus are null
	 * @see PermitCard
	 */
	public PermitCardBonusRqst(Token token, String permitCardId, NormalRegion targetRegion, Bonus bonus) {
		super(token, bonus);
		if(targetRegion == null)
			throw new NullPointerException("Target region can't be null");
		
		this.permitCardId = permitCardId;
		this.targetRegion = targetRegion;
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
		 * Control if the bonus is a SingleBonus and the name must be equals to PERMITCARDBONUS
		 */
		if(!("PERMITCARDBONUS".equals(getBonus().getName()) && getBonus() instanceof SingleBonus)) 
			throw new IllegalActionException("ILLEGAL ACTION!");
		
		/*
		 * Control if the recived region is really part of the game and
		 * change region provided by player with its equivalent server's version.
		 */
		RegionType type = Arrays.asList(RegionType.values()).stream()
				.filter(r -> r == targetRegion.getRegionType())
				.findFirst()
				.orElseThrow(() -> new IllegalActionException("The selected region doesn't exist"));
	
		targetRegion = game.getBoard().getRegions(type);
		
		/*
		 * Check if the permit card provided by client's rqst is really 
		 * one fo the drawable cards of the specified region.
		 */
		targetRegion.getDeckOfPermitCard().getUsableCard().stream()
					.filter(c -> c.getId().equals(permitCardId))
					.forEach(c -> this.cardToDraw = c);
		if(cardToDraw == null) 
			throw new IllegalActionException("The selected permit card isn't in the selected region's usable cards.");
		
		return true;
	}
	

	/**
	 * @return the selected permit card
	 */
	public PermitCard getCardToDraw() {
		return cardToDraw;
	}

	/**
	 * @return the region where the permit card is located
	 */
	public NormalRegion getTargetRegion() {
		return targetRegion;
	}

}
