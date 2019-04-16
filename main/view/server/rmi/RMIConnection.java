package it.polimi.ingsw.cg32.view.server.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This Remote interface provide one Remote method to allow the client
 * to connect to RMI server.
 * It will be used by the client using RMI technology.
 * 
 * @author Stefano
 *
 */
@FunctionalInterface
public interface RMIConnection extends Remote {

	/**
	 * Put in the registry the request handler stub of the client that is trying to connect
	 * to rmi server. Then get the client stub to send responde to client.
	 * 
	 * @param clientId the id using to differentiate the various request handler's client
	 * @throws RemoteException if there is one error connecting to rmi server
	 */
	public void connectRMI(String clientId) throws RemoteException;
}
