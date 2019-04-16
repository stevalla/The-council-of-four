package it.polimi.ingsw.cg32.view.server.rmi;

import java.rmi.RemoteException;

import it.polimi.ingsw.cg32.message.request.RequestMessage;
import it.polimi.ingsw.cg32.utilities.observer.Observable;
import it.polimi.ingsw.cg32.view.server.Lobby;

/**
 * This class is used to manage request from client and send it to the controller
 * of the game using Observer pattern.<br>
 * It implements one remote interface to allow the client to send request to server
 * through RMI.
 * 
 * @author Stefano
 *
 */
public class RequestHandlerRMI extends Observable<RequestMessage> implements RequestHandlerInt {

	/**
	 * Register the GameController as observer of this class to send
	 * request from the client to the server.
	 * 
	 * @param lobby the {@link Lobby} of the game
	 */
	public RequestHandlerRMI(Lobby lobby) {
		this.registerObserver(lobby.getWaitingController());
	}

	@Override
	public void receivedRequest(RequestMessage request) throws RemoteException {

		if(request == null)
			return;

		notifyObserver(request);
	}
}
