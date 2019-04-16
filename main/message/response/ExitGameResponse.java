package it.polimi.ingsw.cg32.message.response;

import it.polimi.ingsw.cg32.message.request.ExitRqst;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;

/**
* This response is send by server to handle a client disconnection.<br>
* When a client disconnect with an {@link ExitRqst}, server forced the classes that 
* are managing the connesion of the target client to disconnect sending back this response.<br>
* 
* @author giovanni
*
*/
public class ExitGameResponse extends ResponseMessage {

	private static final long serialVersionUID = 1L;

	@Override
	public void display(ResponseVisitor visitor) {
		//DO NOTHING
	}

}
