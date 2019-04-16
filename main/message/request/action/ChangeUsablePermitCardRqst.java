package it.polimi.ingsw.cg32.message.request.action;

import java.io.Serializable;
import java.util.Arrays;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.ChangeUsablePermitCard;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.model.region.Region;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link MainActionRqst} and create a request
 * to execute an {@link ChangeUsablePermitCard} action controlling
 * its validation.<br>
 * This action is a secondary action.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 * @see Action
 *
 */
public class ChangeUsablePermitCardRqst extends MainActionRqst {

	private static final long serialVersionUID = 1L;
	private NormalRegion targetRegion;
	
	/**
	 * Construct a {@link MainActionRqst} to change the {@link PermitCard}
	 * int the selected {@link NormalRegion}
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param targetRegion the {@link NormalRegion} where change usable {@link PermitCard}
	 * @throws NullPointerException if region or token are null
	 */
	public ChangeUsablePermitCardRqst(Token token, NormalRegion normalRegion) {
		super(token);
		
		if(normalRegion == null)
			throw new NullPointerException("Region can't be null");
		
		this.targetRegion = normalRegion;
	}

	@Override
	public Action accept(Game game, Player player, ActionVisitor visitor) throws IllegalActionException {
		
		super.accept(game, player, visitor);
		
		return isValid(game, player) ? visitor.visit(this, player, game) : null;
	}
	
	@Override
	protected boolean isValid(Game game, Player player) throws IllegalActionException{
		
		super.isValid(game, player);
		
		/*
		 * Control if the recived region is really part of the game and
		 * change region provided by player with its equivalent server's version.
		 */
		RegionType type = Arrays.asList(RegionType.values()).stream()
								.filter(r -> r == targetRegion.getRegionType())
								.findFirst()
								.orElseThrow(() -> new IllegalActionException("The selected region doesn't exist"));
		
		targetRegion = game.getBoard().getRegions(type);
		
		if(targetRegion.getDeckOfPermitCard().size() < 2) 
			throw new IllegalActionException("There aren't enough card in the deck to execute the action");
		
		if(player.getAssistants().isEmpty()) 
			throw new IllegalActionException("you need at least 1 assistants to execute this action.");
		
		return true;
	}
	
	/**
	 * @return the selected {@link Region}
	 */
	public NormalRegion getTargetRegion() {
		return targetRegion;
	}

}
