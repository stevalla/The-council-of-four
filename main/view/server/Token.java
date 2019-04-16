package it.polimi.ingsw.cg32.view.server;

import java.io.Serializable;
import java.util.UUID;

import it.polimi.ingsw.cg32.controller.GameController;
import it.polimi.ingsw.cg32.message.request.RequestMessage;
import it.polimi.ingsw.cg32.model.game.Player;

/**
* Token is a unique identifier that is associated with each player to 
* recognize them once their {@link RequestMessage} arrive at server.<br>
* Token are uniquely identify by a random UUID on which they implements equals.<br>
* Token are also characterized by playerNumber that is the Player's Id of the one the 
* client is using.<br>
* This number is set by the Lobby as 999 when a client is added to a waiting GameControler.
* When Lobby init GameController by {@link GameController#initGame()} the playerNumber is
* set to a valid Player's Id.<br>
* Finally Token have also a boolean administrator That is set as true (by the Lobby) only 
* to the first client that join the GameController.<br>
* Just GameController's administrator is allowed to change the map.<br>
* 
* 
* @author giovanni
* 
* @see GameController
* @see Lobby
* @see Player
*
*/
public class Token implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final int DEFAULT_TOKEN = 999;
	private final String uuid;
	private int playerNumber;
	private boolean administrator = false;
	
	/**
	 * Construct a Token with a random UUID anda default number player
	 */
	public Token() {
		this.uuid = UUID.randomUUID().toString();
		this.playerNumber = DEFAULT_TOKEN;
	}
 
	/**
	* Get the String that uniquely identify Token
	* 
	* @return uuid the String that uniquely identify Token
	*/
	public String getUuid() {
		return uuid;
	}

	/**
	* Get the playerNumber associated with the Token
	* 
	* @return the playerNumber associated with the Token
	*/
	public int getPlayerNumber() {
		return playerNumber;
	}
	 
	/**
	* Set the playerNumber associated with the Token
	* 
	* @param playerNumber playerNumber associated with the Token
	*/
	public void setPlayerNumber(int playerNumber) {
		this.playerNumber = playerNumber;
	}
	

	/**
	 * interrogate Token if it is the administrator of the {@link GameController} where it's operating
	 * 
	 * @return administrator a boolean condition that indicate if the Token is the
	 * 		   administrator of the {@link GameController} where it's operating
	 */
	public boolean isAdministrator() {
		return administrator;
	}

	/**
	* Set Administartor, is used by {@link Lobby}.
	* 
	* @param administrator valure on which set administrator
	*/
	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
}
