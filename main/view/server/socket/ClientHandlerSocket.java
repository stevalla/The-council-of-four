package it.polimi.ingsw.cg32.view.server.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.view.server.Lobby;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class manage a single client socket connection.<br>
 * It has an executor service to handle two thread: one for {@link SubscriberSocket}
 * to manage response from server to client, and one for {@link RequestHandlerSocket}
 * to manage request from client to server.
 * Implements runnable for multithreading.
 * 
 * @author Stefano
 *
 */
public class ClientHandlerSocket implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(ClientHandlerSocket.class.getName());
	private final RequestHandlerSocket requestHandlerSocket;
	private final SubscriberSocket<ResponseMessage> subscriber;
	private final Socket socket;
	private final Lobby lobby;
	private final ExecutorService executor;
	
	/**
	 * Create Subscriber and Request handler to manage request and response.
	 * 
	 * @param socket the socket used by the client
	 * @param lobby the {@link Lobby} of the server
	 * @throws IOException if there is one error in opening the relative streams
	 */
	public ClientHandlerSocket(Socket socket, Lobby lobby) throws IOException {
		this.socket = socket;
		this.subscriber = new SubscriberSocket<>(socket);
		this.requestHandlerSocket = new RequestHandlerSocket(socket);
		this.executor = Executors.newFixedThreadPool(2);
		this.lobby = lobby;
	}
	
	@Override
	public void run() {
		
		/*
		 * This collection is used to control the liveness of the two thread
		 * submitted to the Executor Service.
		 */
		Collection<Future<?>> tasks = new LinkedList<>();
		tasks.add(executor.submit(requestHandlerSocket));
		tasks.add(executor.submit(subscriber));
		
		try {
			
			/*
			 * Wait for the notify from RequestHandler that arrived when
			 * a connection request from client reached the request handler
			 * of the corresponding client
			 */
			synchronized (requestHandlerSocket) {
				requestHandlerSocket.wait();
			}
			
			//Register GameController as Observer of the requestHandler and add the player to Lobby
			requestHandlerSocket.registerObserver(lobby.getWaitingController());
			lobby.addPlayer(subscriber);
			
			/*
			 * Wait for the termination of the requestHandler, if the
			 * request handler's termination is caused by an exception
			 * forced the other thread (subscriber) to terminated too.
			 */
			for(Future<?> task : tasks) {
				try {
					task.get();
				} catch (ExecutionException e) {
					LOGGER.log(Level.WARNING, e.getMessage());
					break;
				}
			}
			
			executor.shutdownNow();

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		
		/*
		 * This finally block is used to disconnect a client from server and controller
		 * before the game starts.	
		 */
		} finally {
			
			//Take the token of the relative subscriber
			Token token = lobby.getBroker().getTokenBySuscriber(subscriber);
			
			/*
			 * If the game is not started remove the client from the controller and
			 * unsubscribe subscriber to broker, if is started do nothing because
			 * the player will be expelled from the game controller for the timeout.
			 */
			if(lobby.getWaitingController().getPlayers().remove(token)) {
				lobby.getBroker().unsubscribe(subscriber);
				lobby.getBroker().removeTopic(lobby.getWaitingController().getPlayerTopic(token));
			}
			
			try {
				socket.close();
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "Problem closing the socket.", e);
			}
		}

	}


}
