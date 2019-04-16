package it.polimi.ingsw.cg32.controller.action.bonus;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.message.request.bonus.PermitCardBonusRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;

/**
 * This class rapresent a PermitCard Bonus {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link PermitCardBonusRqst} is received and validated.<br>
 * A PermitCard Bonus get to the player the possibility to draw one
 * of the usable permit cards of the regions and win others bonus.
 * 
 * @author Stefano
 *
 */
public class PermitCardBonus extends BonusAction {

	private final Game game;
	private final PermitCard permitCard;
	private final NormalRegion targetRegion;
	private final Bonus bonus;
	private final List<Bonus> bonusWon;;
	
	/**
	 * Construct a PermitCardBonus.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param permitCard the {@link PermitCard} to draw
	 * @param region the selected {@link NormaleRegion}
	 * @param bonus the bonus to execute
	 * 
	 * @see Action 
	 * @see BonusAction
	 */
	public PermitCardBonus(Player player, Game game, PermitCard permitCard, NormalRegion region, Bonus bonus) {
		super(player);
		this.bonusWon = new ArrayList<>();
		this.game = game;
		this.permitCard = permitCard;
		this.targetRegion = region;
		this.bonus = bonus;
	}
	
	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Take the card from the usable cards of the selected region
		 */
		PermitCard selectedPermitCard = targetRegion.getDeckOfPermitCard().drawUsableCard(permitCard);

		
		/*
		 * If selectedPermitCard null return an empty list of bonus in the response
		 */
		if(selectedPermitCard != null) {
			player.getPermitCards().add(selectedPermitCard);
			bonusWon.addAll(selectedPermitCard.getBonus());
		}
		
		return createResponse();
	}

	@Override
	public ActionResponseMessage createResponse() {
		
		String message = "Player#" + player.getId() + " perform an PERMITCARDBONUS.\n"
				+ "He drawn " + permitCard + ".\n";
		
		return new BonusResponse(game,  BonusState.getInstance(), bonusWon, bonus, message, player.getId());
	}

}
