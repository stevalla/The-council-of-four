package it.polimi.ingsw.cg32.view.server.rmi;

import java.rmi.RemoteException;

import it.polimi.ingsw.cg32.view.client.rmi.ResponseHandlerInt;
import it.polimi.ingsw.cg32.view.server.pubsub.SubscriberInt;

/**
 * This class is used to send response from server to the corresponding client.
 * It used the client stub to invoke a method and send the response.
 * 
 * @author giovanni
 *
 * @param <M> messages to dispatch
 */
public class SubscriberRMI<M> implements SubscriberInt<M> {

	private final ResponseHandlerInt<M> responseHandler;
	
	/**
	 * Create a subscriber rmi passing the client's stub to send responses to client.
	 * 
	 * @param responseHandler the stub of the client side to use to send responses to client
	 */
	public SubscriberRMI(ResponseHandlerInt<M> responseHandler) {
		this.responseHandler = responseHandler;
	}

	@Override
	public void dispatchMessage(M response) {

		try {
			responseHandler.receivedResponse(response);
			
		} catch (RemoteException e) {
			/*
			 * This exception is launched when is impossible send a response back to client
			 * due to connection error and this is the unique statement of the code where is possible
			 * to check if rmi client is disconnected.
			 * When client is disconnected notify the corresponding thread of the executor in rmi server
			 * that disconnect client from server.
			 */
			synchronized (this) {
				notify();
			}
			
		}
	}

}
