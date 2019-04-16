package it.polimi.ingsw.cg32.message.request.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuyPermitCard;
import it.polimi.ingsw.cg32.model.balcony.Balcony;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.model.region.Region;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link MainActionRqst} and create a request
 * to execute an {@link BuyPermitCard} action controlling
 * its validation with two method of the interface {@link SatisfyCouncillor}
 * implmented from this class.<br>
 * This action is a primary action.
 * Implement {@link Serializable}
 * 
 * @author Stefano
 * @see Action
 *
 */
public class BuyPermitCardRqst extends MainActionRqst implements SatisfyCouncillor {

	private static final long serialVersionUID = 1L;
	private NormalRegion targetRegion;
	private final List<PoliticCard> politicCards;
	private final List<Colore> colors;
	private PermitCard cardToDraw;
	private final String permitCardId;
	private int moneyToPay;
	
	/**
	 * Construct a {@link MainActionRqst} to buy a {@link PermitCard}.
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param targetRegion the {@link Region} where there is the balcony to satisfy
	 * @param permitCardId the id of the {@link PermitCard} to buy
	 * @param colore the List of {@link Colore} of the List of {@link PoliticCard} to satisfy the balcony
	 * @throws NullPointerException if region or color or token  or permitCardId are null
	 * @see Balcony
	 */
	public BuyPermitCardRqst(Token token, NormalRegion targetRegion, String permitCardId, List<Colore> colors) {
		super(token);
		
		if(colors == null || targetRegion == null || permitCardId == null)
			throw new NullPointerException("Colors, targetRegion and permitCardId can't be null");
		
		this.targetRegion = targetRegion;
		this.politicCards = new ArrayList<>();
		this.colors = colors;
		this.permitCardId = permitCardId;	
		this.moneyToPay = 0;
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
		RegionType type = Arrays.asList(RegionType.values()).stream()
					.filter(r -> r == targetRegion.getRegionType())
					.findFirst()
					.orElseThrow(() -> new IllegalActionException("The selected region doesn't exist"));
		
		targetRegion = game.getBoard().getRegions(type);
		
		/*
		 * Find which cards the player want to discard, controlling that he is corrently owning that cards.
		 */
		this.politicCards.addAll(getPoliticCardByColors(colors, player));
		
		/*
		 * Check if the discard politic card satisfy councillor and return the amount of coins to pay.
		 */
		this.moneyToPay = countMoneyToSatisfyCouncillor(politicCards, player, targetRegion);
		
		/*
		 * Check if the permit card provided by client's rqst is really one of the drawable cards of the region he specified.
		 */
		this.cardToDraw = targetRegion.getDeckOfPermitCard().getUsableCard().stream()
								.filter(c -> c.getId().equals(permitCardId))
								.findFirst()
								.orElseThrow(() -> 
									new IllegalActionException("The selected permit card isn't in selected region's usable cards."));
		
		return true;
	}
	
	/**
	 * @return the selected {@link Region}
	 */
	public NormalRegion getTargetRegion() {
		return targetRegion;
	}
	
	
	/**
	 * @return the List of {@link PoliticCard} of the {@link Player}
	 */
	public List<PoliticCard> getPoliticCards() {
		return politicCards;
	}

	
	/**
	 * @return the {@link PermitCard} to draw
	 */
	public PermitCard getCardToDraw() {
		return cardToDraw;
	}

	
	/**
	 * @return the coins to pay to satisfy the {@link Balcony}
	 */
	public int getMoneyToPay() {
		return moneyToPay;
	}

}
