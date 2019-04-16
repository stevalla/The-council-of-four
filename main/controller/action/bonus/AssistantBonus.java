package it.polimi.ingsw.cg32.controller.action.bonus;

import java.util.Collections;

import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.message.request.bonus.AssistantBonusRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.gui.InfoAction;

/**
 * This class rapresent an Assistant Bonus {@link InfoAction}.
 * It is created from a {@link ActionVisitor} when a
 * {@link AssistantBonusRqst} is received and validated.<br>
 * An Assistant allows you to get so many {@link Assistant}
 * as the amount of the bonus.
 * 
 * @author Stefano
 *
 */
public class AssistantBonus extends BonusAction {

	private final Game game;
	private final QuantityBonus bonus;
	
	/**
	 * COnstruct an Assistant bonus.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param bonus the bonus to execute
	 * 
	 * @see InfoAction 
	 * @see BonusAction
	 */
	public AssistantBonus(Player player, Game game,  QuantityBonus bonus) {
		super(player);
		this.game = game;
		this.bonus = bonus;
	}
	
	
	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Get the assistants to the player, fi the reserve is empty stopped.
		 */
		for(int i = 0; i<bonus.getQuantity(); i++) {
			if(game.getBoard().getAssistantsReserve().getAssistants().isEmpty())
				break;
			player.getAssistants().add(game.getBoard().getAssistantsReserve().getAssistants().poll());
		}
		
		return createResponse();
	}


	@Override
	public ActionResponseMessage createResponse() {
		
		String message = "Player#" + player.getId() + " perform an ASSISTANTBONUS.\n"
				+ "He currently owns " + player.getAssistants().size() + " assistants.\n";
		
		return new BonusResponse(game,  BonusState.getInstance(), Collections.emptyList(), bonus, message, player.getId());
	}

}
