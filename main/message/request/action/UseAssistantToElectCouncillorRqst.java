package it.polimi.ingsw.cg32.message.request.action;

import java.io.Serializable;
import java.util.Arrays;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.UseAssistantToElectCouncillor;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.balcony.CouncillorReserve;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.KingRegion;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.model.region.Region;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link MainActionRqst} and create a request
 * to execute an {@link UseAssistantToElectCouncillor} action controlling
 * its validation.<br>
 * This action is a secondary action.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 * @see Action
 *
 */
public class UseAssistantToElectCouncillorRqst extends MainActionRqst {

	private static final long serialVersionUID = 1L;
	private Region targetRegion;
	private final Colore colore;
	private Councillor councillor;
	
	/**
	 * Construct a {@link MainActionRqst} to elect a {@link Councillor}.
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param targetRegion the {@link Region} where there is the balcony where elect the {@link Councillor}
	 * @param colore the {@link Colore} of the councillor to elect
	 * @throws NullPointerException if region or color or token are null
	 */
	public UseAssistantToElectCouncillorRqst(Token token, Region targetRegion, Colore colore) {
		super(token);
		
		if(targetRegion == null || colore == null)
			throw new NullPointerException("Region and Color can't be null");
		
		this.targetRegion = targetRegion;
		this.colore = colore;
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
		 * Control if the recived region is really part of the game and
		 * change region provided by player with its equivalent server's version.
		 */
		if(targetRegion instanceof KingRegion) {
			
			targetRegion = game.getBoard().getKingRegion();
			
		} else{
			
			RegionType type = Arrays.asList(RegionType.values()).stream()
									.filter(r -> r == ((NormalRegion)targetRegion).getRegionType())
									.findFirst()
									.orElseThrow(() -> new IllegalActionException("The selected region doesn't exist"));
			
			targetRegion = game.getBoard().getRegions(type);
		}
		
		if(player.getAssistants().isEmpty()) 
			throw new IllegalActionException("You haven't enough assistants to execute this action.");
		
		if(!game.getBoard().getCouncillorReserve().containCouncillor(colore)) 
			throw new IllegalActionException("There isn't a councillor of the selected color. Change color.");
		
		/*
		 * Take the councillor with the selected color from the councillor reserve.
		 */
		this.councillor = game.getBoard().getCouncillorReserve().getCouncillorByColor(colore);
		
		return true;
	}
	
	
	/**
	 * @return the selected {@link Region}
	 */
	public Region getTargetRegion() {
		return targetRegion;
	}

	
	/**
	 * @return the {@link Councillor} to elect take from the {@link CouncillorReserve}
	 */
	public Councillor getCouncillor() {
		return councillor;
	}

}
