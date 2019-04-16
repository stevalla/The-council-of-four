package it.polimi.ingsw.cg32.message.request;

import java.io.Serializable;

import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.server.Token;

/**
* This class extends {@link RequestMessage} and it rapresents
* a request to change the current map of the GameController where the client is gaming.<br>
* These kind of request could be send in every phase of the game but 
* just before game's initialization they produce the result requested, otherwise they are
* rejected by server.<br>
* 
* Implement {@link Serializable}
* 
* 
* @author giovanni
*
*/
public class ChangeMapRqst extends RequestMessage {

	private static final long serialVersionUID = 1L;
	private final String map;
	
	/**
	* Constructor for this kind of request, it initialize every fields of the request. 
	* 
	* @param token the {@link Token} associated with the {@link Player} that send the request
	* @param map the name of the map requested by the client
	*/
	public ChangeMapRqst(Token token, String map) {
		super(token);
		this.map = map;
	}

	/**
	* Get the name of the map requested by the client
	* 
	* @return map the name of the map requested by the client
	*/
	public String getChooseMap() {
		return map;
	}
	
}
