package it.polimi.ingsw.cg32.utilities.exception;

import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.gui.InfoAction;

/**
* A Checked Exception used to signals when a {@link Player} tries to
* execute an {@link InfoAction} but he is not allowed to perform it due to
* game's rules.
* 
* @author giovanni
*
*/
public class IllegalActionException extends Exception{

	private static final long serialVersionUID = 6164315119101952260L;
	
	/**
	 * @param messageError
	 */
	public IllegalActionException(String messageError) {
		super(messageError);
	}

}
