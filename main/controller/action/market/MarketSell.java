package it.polimi.ingsw.cg32.controller.action.market;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.cg32.controller.Market;
import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.message.request.market.MarketSellRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.market.MarketResponse;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.market.AssistantBoundle;
import it.polimi.ingsw.cg32.model.market.Boundle;
import it.polimi.ingsw.cg32.model.market.PermitCardBoundle;
import it.polimi.ingsw.cg32.model.market.PoliticCardBoundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class rapresent a Market Sell {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link MarketSellRqst} is received and validated.<br>
 * This is a {@link Market} action, and the execution
 * full fill the shoppingCarts with the {@link Boundle}
 * that the player decide to sell.
 * 
 * @author Stefano
 *
 */
public class MarketSell implements Action {
	
	private final Player player;
	private final Game game;
	private final Token token;
	private final Map<List<Colore>, Integer> colors;
	private final Map<List<Integer>, Integer> assistantsInt;
	private final Map<List<String>, Integer> permitCardsInt;

	
	/**
	 * Construct a Market Sell
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param token the token of the player to put it as key in the Map<Token, ShoppingCart>
	 * @param permitCardsInt the Permit Card boundles to sell
	 * @param assistantInt the Assistants buondles to sell
	 * @param colors the politic cards boundles to sell
	 */
	public MarketSell(Player player, Game game, Token token, Map<List<String>, Integer> permitCardsInt, 
			Map<List<Integer>, Integer> assistantInt, Map<List<Colore>, Integer> colors) {
		this.player = player;
		this.game = game;
		this.token = token;
		this.colors = colors;
		this.assistantsInt = assistantInt;
		this.permitCardsInt = permitCardsInt;
	}
	
	
	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Create Politic Card Boundles only if the player request to sell some politic cards
		 */
		List<PoliticCardBoundle> politicCardBoundles = new ArrayList<>();
		List<PoliticCard> politicCardList = new LinkedList<>();
		for(Map.Entry<List<Colore>, Integer> map : colors.entrySet()) {
			map.getKey().stream().forEach(c -> {
				for(PoliticCard politicCard : new ArrayList<>(player.getPoliticCards())) 
					if(politicCard.getCardColor() == c) {
						politicCardList.add(politicCard);
						player.getPoliticCards().remove(politicCard);
						break;
					}
			});
			politicCardBoundles.add(new PoliticCardBoundle(map.getValue(), politicCardList));
			politicCardList.clear();
		}
		
		/*
		 * Create Permit Card Boundles only if the player request to sell some permits cards
		 */
		List<PermitCardBoundle> permitCardBoundles = new ArrayList<>();
		List<PermitCard> permitCardList = new LinkedList<>();
		for(Map.Entry<List<String>, Integer> map : permitCardsInt.entrySet()) {
			map.getKey().stream().forEach(i -> {
				for(PermitCard permitCard : new ArrayList<>(player.getPermitCards())) 
					if(permitCard.getId().equals(i)) {
						permitCardList.add(permitCard);
						player.getPermitCards().remove(permitCard);
						break;
					}
			});
			permitCardBoundles.add(new PermitCardBoundle(map.getValue(), permitCardList));
			permitCardList.clear();
		}
		
		/*
		 * Create Assistants Boundles only if the player request to sell some Assistants
		 */
		List<AssistantBoundle> assistantBoundles = new ArrayList<>();
		List<Assistant> assistantList = new LinkedList<>();
		for(Map.Entry<List<Integer>, Integer> map : assistantsInt.entrySet()) {
			map.getKey().stream().forEach(i -> {
				for(int j=0; j<i; j++)
					assistantList.add(player.getAssistants().poll());
			});
			assistantBoundles.add(new AssistantBoundle(map.getValue(), assistantList));
			assistantList.clear();
		}
		
		/*
		 * Put the player's boundles in the Shopping Cart
		 */
		game.getStore().put(token, new ShoppingCart(politicCardBoundles, assistantBoundles, permitCardBoundles));
		
		return createResponse();
	}


	@Override
	public ActionResponseMessage createResponse() {
		
		String message = "Player#" + player.getId() + " sells:\n";
				         
			         

		return new MarketResponse(game, message, player.getId());
	}

}
