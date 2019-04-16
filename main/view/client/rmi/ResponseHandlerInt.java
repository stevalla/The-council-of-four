package it.polimi.ingsw.cg32.view.client.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface that provide a method to received response from server and manage it
 * in client side.
 * Implements {@link Remote} for use it use in RMI mode.
 * 
 * @author Stefano
 *
 * @param <M> the Message to received from the server
 */
@FunctionalInterface
public interface ResponseHandlerInt<M> extends Remote {

	/**
	 * Received a response from the server and manage it in client side.
	 * This method can be used also in RMI connection.
	 * 
	 * @param response the received response
	 * @throws RemoteException if there was some error in RMI connection
	 */
	public void receivedResponse(M response) throws RemoteException;
}
