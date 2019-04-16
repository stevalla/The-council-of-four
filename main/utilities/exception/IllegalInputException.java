package it.polimi.ingsw.cg32.utilities.exception;

/**
* A Checked Exception used to signals when a {@link Client} 
* doesn't enter a right command.
* 
* @author giovanni
*
*/
public class IllegalInputException extends Exception {


	private static final long serialVersionUID = -7228279971588705176L;

	/**
	 * @param messageError
	 */
	public IllegalInputException(String messageError) {
		super(messageError);
	}
}
