package it.polimi.ingsw.cg32.controller.action.bonus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.message.request.bonus.PoliticCardBonusRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent a PolitcCard Bonus {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link PoliticCardBonusRqst} is received and validated.<br>
 * A Politic card Bonus allows you to get so many politic cards
 * as the amount of the bonus.
 * 
 * @author Stefano
 *
 */
public class PoliticCardBonus extends BonusAction {

	private final Game game;
	private final PoliticCardPrototype politicCardPrototype;
	private final QuantityBonus bonus;
	private PoliticCard drawedCard;
	
	
	/**
	 * Construct a Politic card bonus.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param bonus the bonus to execute
	 * @param politicCardPrototype the prototype to create a random {@link PolticCard}
	 * 
	 * @see Action 
	 * @see BonusAction
	 */
	public PoliticCardBonus(Player player, Game game, QuantityBonus bonus, PoliticCardPrototype politicCardPrototype) {
		super(player);
		this.game = game;
		this.politicCardPrototype = politicCardPrototype;
		this.bonus = bonus;
	}
	
	
	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Create randoms politic cards and give it to the player
		 */
		List<PoliticCard> drawedCards = new ArrayList<>();
		for(int i = 0; i < bonus.getQuantity(); i++){
			drawedCard = politicCardPrototype.getPoliticCard();
			drawedCards.add(drawedCard);
			player.getPoliticCards().add(drawedCard);
		}
		
		return createResponse();
	}


	@Override
	public ActionResponseMessage createResponse() {
		
		String message = "Player#" + player.getId() + " perform an POLITICCARDBONUS.\n"
				+ "He drawn a " + drawedCard.getCardColor() + " politic card";
		
		return new BonusResponse(game,  BonusState.getInstance(), Collections.emptyList(), bonus, message, player.getId());
	}

}
