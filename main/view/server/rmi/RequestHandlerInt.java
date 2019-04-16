package it.polimi.ingsw.cg32.view.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.cg32.message.request.RequestMessage;

/**
 * This interface provides a method that can be call by
 * client that choose RMI-connection to send requests to the server.
 * 
 * @author giovanni
 */
@FunctionalInterface
public interface RequestHandlerInt extends Remote {

	/**
	* This remote method is call by the client to send request to the server. 
	* Once the request arrives it forwards the request to the associated controller.<br>
	* 
	* @param request the request sent by the client
	* @throws RemoteException if an error happened reciving request from the client
	*/
	public void receivedRequest(RequestMessage request) throws RemoteException;
}
