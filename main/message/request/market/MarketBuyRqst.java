package it.polimi.ingsw.cg32.message.request.market;

import java.util.Map;

import it.polimi.ingsw.cg32.controller.Market;
import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.market.Boundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
* This class rappresent a request that a client has to formulate
* to buy stuff from {@link Market}.<br>
* It has:<br> 
* a boundleId that indicate the Boundle that the Player wants to buy.<bd>
* a Map of {@link Token} and {@link ShoppingCart} that contains all the Boundle 
* previously sold, used to checks the presence of the Boundle requested, in it.<br> 
* And the {@link Token} associated with the clent who made it.<br>
* It also find the Token associated with the Player , if there is any, who is the owner
* of the Boundle requested. This Token will be use to compose the {@link MaretBuy} (the concrete Action).<br>
* 
* @author giovanni
* 
* @see Player
* @see Boundle
*
*/
public class MarketBuyRqst extends MarketRqst {

	private static final long serialVersionUID = 1L;
	private final int boundleId;
	private Token playerThatSell;
	
	/**
	* Constructor of MarketBuyRqst, take care to initialize the {@link Token}
	* of the {@link Player} and the Boundle's Id that indicate the Boundle to buy. 
	* 
	* @param token the Token of the client
	* @param boundelId the Boundle's Id that uniquely identify it.
	* @throws NullPointerException if token is null
	* @throws IllegalArgumentException if params is minor than zero
	* 
	* @see Boundle
	* 
	*/
	public MarketBuyRqst(Token token, int boundelId) {
		super(token);
		 
		if(boundelId < 0 )
			throw new IllegalArgumentException("The bound is can't be minor than zero");
		
		this.boundleId = boundelId;
	}

	
	@Override
	public Action accept(Game game, Player player, ActionVisitor visitor) throws IllegalActionException {
		
		super.accept(game, player, visitor);
		
		return isValid(game, player) ? visitor.visit(this, player, game) : null;
	}

	
	@Override
	protected boolean isValid(Game game, Player player) throws IllegalActionException {
		
		super.isValid(game, player);
		
		//Control over the real presence in the market of the boundle provided by the client.
		Boundle targetBoundle = null;

		for(Map.Entry<Token, ShoppingCart> map : game.getStore().entrySet()) {
			Boundle boundle = map.getValue().findBoundleById(boundleId);
			if(boundle != null) {
				targetBoundle = boundle;
				playerThatSell = map.getKey();
				break;
			}
		}

		if(targetBoundle == null)
			throw new IllegalActionException("You can't buy a boundle that isn't corrently present in the market!");
		
		if(targetBoundle.getPrice() > player.getCoinsTrack().getCurrentCoins())
			throw new IllegalActionException("You haven't enough money to buy the target boundle!");
		
		//Control if the client is trying to purchased one Boundle that he sold.
		if(playerThatSell.getPlayerNumber() == player.getId()) 
			throw new IllegalActionException("You are not allow to buy boundle that you've sold!");

		return true;
	}

	public int getBoundleId() {
		return boundleId;
	}

	public Token getPlayerThatSell() {
		return playerThatSell;
	}
	
}
