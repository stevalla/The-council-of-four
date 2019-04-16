package it.polimi.ingsw.cg32.utilities.exception;

import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.gui.InfoAction;

/**
* A Checked Exception used to signals when a {@link Player} tries to
* execute an {@link InfoAction} but he is not allowed to perform it because he's not
* in the right phase oh his turn to execute it.
* 
* @author giovanni
*
*/
public class IllegalStateMachineException extends Exception {

	private static final long serialVersionUID = 1L;
	
	/**
	 * @param messageError
	 */
	public IllegalStateMachineException(String messageError) {
		super(messageError);
	}
}
