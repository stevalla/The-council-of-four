package it.polimi.ingsw.cg32.view.server.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.view.client.rmi.ResponseHandlerInt;
import it.polimi.ingsw.cg32.view.server.Lobby;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class rapresent the server RMI, it provides one public Remote method
 * to allow clients to connect to server.
 * It also provide a private method that instantiate a new Runnable for each client
 * to handle the client disconnection.
 * 
 * @author giovanni
 *
 */
public class RMIServer implements RMIConnection {

	private static final Logger LOGGER = Logger.getLogger(RMIServer.class.getName());
	private final Registry registry;
	private final List<RequestHandlerRMI> requestHandlers;
	private final List<SubscriberRMI<ResponseMessage>> subscribers;
	private final Lobby lobby;
	private final ExecutorService executor;
	
	/**
	 * Create a new RMI server with the empty list of subscribers and request handlers
	 * and the executor to handle the connection of each client.
	 * 
	 * @param registry the registry where write the stub
	 * @param lobby the {@link Lobby} of the server
	 */
	public RMIServer(Registry registry, Lobby lobby) {
		this.registry = registry;
		this.requestHandlers = new ArrayList<>();
		this.subscribers = new ArrayList<>();
		this.lobby = lobby;
		this.executor = Executors.newCachedThreadPool();
	} 
	
	@Override
	public synchronized void connectRMI(String clientId) throws RemoteException {
		
		try {
			/*
			 * Create a new Request Handler and put it into registry using the id to differentiate it 
			 * from the other requst handlers rmi of the other clients
			 */
			RequestHandlerRMI requestHandlerRMI = new RequestHandlerRMI(lobby);
			
			RequestHandlerInt requestHandlerStub = (RequestHandlerInt) UnicastRemoteObject.exportObject(requestHandlerRMI, 0);
			
			registry.rebind("REQUEST" + clientId, requestHandlerStub);
			
			//Search in the registry the response handler of the client to send response using clientId
			@SuppressWarnings("unchecked")
			ResponseHandlerInt<ResponseMessage> responseHandlerStub = 
					(ResponseHandlerInt<ResponseMessage>) registry.lookup("RESPONSE" + clientId);
			
			SubscriberRMI<ResponseMessage> subscriberRMI = new SubscriberRMI<>(responseHandlerStub);
			
			requestHandlers.add(requestHandlerRMI);
			subscribers.add(subscriberRMI);
			
			//Add client to lobby
			lobby.addPlayer(subscriberRMI);
			
			//Start runnable to handle the client disconnection
			catchErrorConnection(subscriberRMI, requestHandlerRMI, clientId);
			
		} catch(NotBoundException e) {
			throw new RemoteException("Error while creating a new RMI server.", e);
		}
	}
	
	/**
	 * This method use a Runnable to manage the client disconnection.
	 * Every time that a client is connected the executor submit a new task, that has to
	 * wait until the subscriber RMI notify it to singals that the connection is lost or closed.
	 * When this occur the task expell the client from the game and do the unbind of all his stub.
	 * 
	 * @param subscriberRMI the created subscriber associated to the client that is trying to connect
	 * @param requestHandlerRMI the created request handler associated to the client that is trying to connect
	 * @param clientId the string id sent from the client
	 */
	private void catchErrorConnection(SubscriberRMI<ResponseMessage> subscriberRMI, RequestHandlerRMI requestHandlerRMI,
			String clientId) {
		
		Runnable handlerConnectionRMI = () -> {
			try {
				synchronized (subscriberRMI) {
					subscriberRMI.wait();
				}

				//Take the token of the relative subscriber
				Token token = lobby.getBroker().getTokenBySuscriber(subscriberRMI);
				
				/*
				 * If the game is not started remove the client from the controller and
				 * unsubscribe subscriber to broker, if is started do nothing because
				 * the player will be expelled from the game controller for the timeout.
				 */
				if(lobby.getWaitingController().getPlayers().remove(token)) {
					lobby.getBroker().unsubscribe(subscriberRMI);
					lobby.getBroker().removeTopic(lobby.getWaitingController().getPlayerTopic(token));
				}
				
				/*
				 * Unbind the request handler of the disconnected client and remove his corresponding
				 * subscriber and request handler.
				 */
				registry.unbind("REQUEST" + clientId);
	            UnicastRemoteObject.unexportObject(requestHandlerRMI, true);
				subscribers.remove(subscriberRMI);
				requestHandlers.remove(requestHandlerRMI);
				
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (NotBoundException | RemoteException e) {
				LOGGER.log(Level.WARNING, "Exception thrown while releasing RMI.", e);
			}
		};
		
		
		this.executor.submit(handlerConnectionRMI);
	}
	
	/**
	 * Shutdown the executor and unbind the RMI connection stub to
	 * avoid the connection of clients.
	 */
	public void stopRMI() {
		
		try {
			
			executor.shutdownNow();
			registry.unbind("CONNECTION");
			UnicastRemoteObject.unexportObject(this, true);
			
		} catch (RemoteException | NotBoundException e) {
			LOGGER.log(Level.SEVERE, "Error occurring in closing RMI server.", e);
		}
	}
	
}
