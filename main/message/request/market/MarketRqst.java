package it.polimi.ingsw.cg32.message.request.market;

import it.polimi.ingsw.cg32.controller.Market;
import it.polimi.ingsw.cg32.message.request.ActionRqst;
import it.polimi.ingsw.cg32.view.server.Token;

/**
* This is the abstract type for request that have to interact with the {@link Market}.<br>
* It's extended by {@link MarketBuyRqst} and {@link TestMarketSellRqst}.<br>
* 
* @author giovanni
*  
*/
public abstract class MarketRqst extends ActionRqst {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @param token the {@link Token} corresponding at the client who made the request
	 */
	public MarketRqst(Token token) {
		super(token);
	}

}
