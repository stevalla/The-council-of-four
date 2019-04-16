package it.polimi.ingsw.cg32.controller.action.primaryaction;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.controller.turn.state.PrimaryActionState;
import it.polimi.ingsw.cg32.controller.turn.state.State;
import it.polimi.ingsw.cg32.message.request.action.BuyPermitCardRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;

/**
 * This class rapresent a Buy Permit Card {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link BuyPermitCardRqst} is received and validated.<br>
 * This primary action allows the player to take one permit cards
 * from the selected region, if he satisfy the councillor of the
 * selected region.<br>
 * The player get the bonus of the permit cards too.
 * 
 * @author Stefano
 *
 */
public class BuyPermitCard implements Action {

	private final Player player;
	private final Game game;
	private final NormalRegion targetRegion;
	private final List<PoliticCard> politicCards;
	private final PermitCard cardToDraw;
	private final int moneyToPay;
	private final List<Bonus> bonusWon;
	
	
	/**
	 * 
	 * Construct a Buy Permit Card.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param targetRegion the selected {@link NormalRegion} where draw the permit card
	 * @param politicCards the player's politiccards to satisfy the councillor
	 * @param cardToDraw the card that the player want to draw
	 * @param moneyToPay the money to pay to execute this action, depend from the number and
	 * 		  the color of the politic cards
	 * 
	 * @see Action 
	 */
	public BuyPermitCard(Player player, Game game, NormalRegion targetRegion, List<PoliticCard> politicCards,
			PermitCard cardToDraw, int moneyToPay) {
		this.bonusWon = new ArrayList<>();
		this.player = player;
		this.game = game;
		this.targetRegion = targetRegion;
		this.politicCards = politicCards;
		this.cardToDraw = cardToDraw;
		this.moneyToPay = moneyToPay;
	}

	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Remove from the player the politic cards played
		 */
		politicCards.stream().forEach(player.getPoliticCards()::remove);
		
		/*
		 * Pay the money to execute this action
		 */
		player.getCoinsTrack().setCoinsNumber(-moneyToPay);
		

		/*
		 * Take the card from the usable cards of the selected region
		 */
		PermitCard selectedPermitCard = targetRegion.getDeckOfPermitCard().drawUsableCard(cardToDraw);
		
		
		/*
		 * If selectedPermitCard is null return an empty list of bonus in the response, anyway we
		 */
		if(selectedPermitCard != null){
			player.getPermitCards().add(selectedPermitCard);
			bonusWon.addAll(selectedPermitCard.getBonus());
		}
		
		return createResponse();
	}

	@Override
	public ActionResponseMessage createResponse() {
		
		State state;
		 
		/*
		 * If the are bonus set the State to action bonus, otherwise
		 * to primary action
		 */
		if(bonusWon.isEmpty())
			state = PrimaryActionState.getInstance();
		else
			state = BonusState.getInstance();
		
		String message = "Player#" + player.getId() + " perform a BUYPERMITCARD.\n"
				       + "He satisfy " + targetRegion.getRegionType() + " balcony usign " + politicCards + "\n"
					   + "He bought: " + cardToDraw
					   + "From the region " + targetRegion.getRegionType() + "\n";
		
		return new ActionResponse(game, state, bonusWon, message, player.getId());
	}

}
