package it.polimi.ingsw.cg32.message.request.market;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.cg32.controller.Market;
import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.message.request.action.SatisfyCouncillor;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.game.Board;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.market.Boundle;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
* This class rappresent a request that a client has to formulate
* to sell stuff in the {@link Market}.<br>
* it has:<br>
* the {@link Token} associated with the clent who made it.<br>
* It's has 3 Maps that collect all the Boundle that a Player request to sell.<br>
* It Overrides {@link #isValid(Board, Player)} method to verify the ownership of what
* he requested to sell.<br>
* 
* @author giovanni
* 
* @see Player
* @see Boundle
*
*/
public class MarketSellRqst extends MarketRqst implements SatisfyCouncillor {

	private static final long serialVersionUID = 1L;
	private final Map<List<Colore>, Integer> colors;
	private final Map<List<Integer>, Integer> assistantsInt;
	private final Map<List<String>, Integer> permitCardsId;

	/**
	* Constructor of MarketSellRqst, take care to initialize the {@link Token}
	* of the {@link Player} and the 3 Maps. 
	* 
	* @param token the {@link Token} of the client that send the request
	* @param colors a Map<List<Colore>, Integer>, the colors of the {@link PoliticCard} 
	* 		  of the {@link Boundle} and its price.<br>
	* @param assistantsInt a Map<List<Integer>, Integer>, the number of {@link Assistant}s 
	* 		  of the {@link Boundle} and its price.<br>
	* @param permitCards a Map<List<String>, Integer>, the ids of the {@link PermitCard}s 
	* 		  of the {@link Boundle} and its price.<br>
	* @throws NullPointerException if token is null
	*/
	public MarketSellRqst(Token token, Map<List<Colore>, Integer> colors,
			Map<List<Integer>, Integer> assistantsInt, Map<List<String>, Integer> permitCards) {
		super(token);
		
		this.colors = colors;
		this.permitCardsId = permitCards;
		this.assistantsInt = assistantsInt;
	}

	@Override
	public Action accept(Game game, Player player, ActionVisitor visitor) throws IllegalActionException {
		
		super.accept(game, player, visitor);
		
		return isValid(game, player) ? visitor.visit(this, player, game) : null;
	}

	@Override
	protected boolean isValid(Game game, Player player) throws IllegalActionException {

		super.isValid(game, player);
		
		//Carte politiche
		List<Colore> colorsToAdd = new ArrayList<>();
		for(Map.Entry<List<Colore>, Integer> map : colors.entrySet()) {
			colorsToAdd.addAll(map.getKey());
		}
		
		getPoliticCardByColors(colorsToAdd, player);
		
		//Assistenti
		int assistantToAdd = 0;
		for(Map.Entry<List<Integer>, Integer> map : assistantsInt.entrySet()) {
			for(Integer i : map.getKey()) {
				assistantToAdd += i;
			}
		}
		
		if(assistantToAdd > player.getAssistants().size())
			throw new IllegalActionException("You don't have enough assistants to sell.");
		
		//Carte permesso
		List<PermitCard> permitCardsToAdd = new ArrayList<>();
		int numberPermitCards = 0;
		for(Map.Entry<List<String>, Integer> map : permitCardsId.entrySet()) {
			for(String i : map.getKey()) {
				player.getPermitCards().stream()
						.filter(p -> p.getId().equals(i))
						.forEach(permitCardsToAdd::add);
				numberPermitCards++;
			}
		}
		
		if(permitCardsToAdd.size() < numberPermitCards)
			throw new IllegalActionException("You have selected one or more permit card to sell that isn't yours.");

		return true;
	}

	public Map<List<Colore>, Integer> getColors() {
		return colors;
	}

	public Map<List<Integer>, Integer> getAssistantsInt() {
		return assistantsInt;
	}

	public Map<List<String>, Integer> getPermitCardsInt() {
		return permitCardsId;
	}


}
