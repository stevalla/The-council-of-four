package it.polimi.ingsw.cg32.controller.action.bonus;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.message.request.bonus.NobilityBonusRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.track.MarkerDisk;
import it.polimi.ingsw.cg32.model.track.NobilityTrack;
import it.polimi.ingsw.cg32.model.track.Space;

/**
 * This class rapresent a Nobility Bonus {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link NobilityBonusRqst} is received and validated.<br>
 * A Nobility Bonus allows to move one position the
 * {@link MarkerDisk} of the player in the {@link NobilityTrack}
 * and get the bonus of the new current {@link Space} if
 * there are any.
 * 
 * @author Stefano
 *
 */
public class NobilityBonus extends BonusAction {

	private final Game game;
	private final Bonus bonus;
	private final List<Bonus> bonusWon;
	
	/**
	 * Construct a NobilityBonus.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param bonus the bonus to execute
	 * 
	 * @see Action 
	 * @see BonusAction
	 */
	public NobilityBonus(Player player, Game game, Bonus bonus) {
		super(player);
		this.bonusWon = new ArrayList<>();
		this.game = game;
		this.bonus = bonus;
	}
	
	@Override
	public ActionResponseMessage execute() {
 
		/*
		 * Set the marker disk to the next space
		 */
		Space currentSpace = player.getMarkerDisk().nextCurrentSpace();
		
		/*
		 * The space is null only if the nobility track is finished, in this case
		 * we don't wanna add another time the bonus of the last space.
		 */
		if(currentSpace != null)
			bonusWon.addAll(currentSpace.getBonus());
		
		return createResponse();
	}

	@Override
	public ActionResponseMessage createResponse() {

		String message = "Player#" + player.getId() + " perform a NOBILITYBONUS.\n"
				+ "His marker disk is on " + player.getMarkerDisk().getCurrentSpace().getSpaceNumber() 
				+ "the Space of the nobility track.\n";
		
		return new BonusResponse(game,  BonusState.getInstance(), bonusWon, bonus, message, player.getId());
	}

}
