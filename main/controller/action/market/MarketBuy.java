package it.polimi.ingsw.cg32.controller.action.market;

import java.util.Map;

import it.polimi.ingsw.cg32.controller.Market;
import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.message.request.market.MarketBuyRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.market.MarketResponse;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.market.Boundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class rapresent a Market Buy {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link MarketBuyRqst} is received and validated.<br>
 * This is a {@link Market} action, and the execution
 * get the selected {@link Boundle} to the player that buy
 * and pay the player that sell the cost of the boundle
 * take the money from the player that buy.
 * 
 * @author Stefano
 *
 */
public class MarketBuy implements Action {

	private final Game game;
	private final Player playerThatBuy;
	private final Player playerThatSell;
	private final int boundleId;
	private Boundle targetBoundle;
	
	
	/**
	 * Construct a Market Buy
	 * 
	 * @param playerThatBuy the {@link Player} that buy
	 * @param game the {@link Game} with all components
	 * @param playerThatSell the {@link Player} that sell
	 * @param boundleId the {@link Boundle} to buy
	 */
	public MarketBuy(Player playerThatBuy, Game game, Player playerThatSell, int boundleId) {
		this.playerThatBuy = playerThatBuy;
		this.game = game;
		this.playerThatSell = playerThatSell;
		this.boundleId = boundleId;
	}
	
	
	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * Search the target Boundle in the shopping card, than call a method that
		 * used the inheritance of the three subclasses of Boundle to use the boundle
		 * correctly and so give the objects in the boundle to the player that buy.
		 */
		for(Map.Entry<Token, ShoppingCart> map : game.getStore().entrySet()){
			targetBoundle = map.getValue().removeBoundleById(boundleId);
			if(targetBoundle != null) {
				
				/*
				 * Pay the amount to cover the cost of the boundle and set to the player
				 * that buy.
				 */
				playerThatBuy.getCoinsTrack().setCoinsNumber(-targetBoundle.getPrice());
				playerThatSell.getCoinsTrack().setCoinsNumber(targetBoundle.getPrice());
				
				targetBoundle.useBoundle(playerThatBuy);
				break;
			}
		}

		return createResponse();
	}

	@Override
	public ActionResponseMessage createResponse() {

		
		String message = "Player#" + playerThatBuy.getId() + " boght boundle#" + boundleId + " from market.\n"
			           + "He payed " + targetBoundle.getPrice() + " coins to the player#" + playerThatSell.getId() + ".\n"
					   + "The boundle contains:\n" + targetBoundle.toString();
					   
		return new MarketResponse(game, message, playerThatBuy.getId());
	}

}
