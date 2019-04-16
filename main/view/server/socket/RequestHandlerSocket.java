package it.polimi.ingsw.cg32.view.server.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.cg32.message.request.ConnectionRqst;
import it.polimi.ingsw.cg32.message.request.ExitRqst;
import it.polimi.ingsw.cg32.message.request.RequestMessage;
import it.polimi.ingsw.cg32.utilities.observer.Observable;

/**
 * This class is used to manage request from client and send it to the controller
 * of the game using Observer pattern.
 * Implements Runnable for multithreading
 * 
 * @author giovanni
 *
 */
public class RequestHandlerSocket extends Observable<RequestMessage> implements Runnable {

	private static final Logger LOGGER = Logger.getLogger(RequestHandlerSocket.class.getName());
	private final Socket socket;
	private final ObjectInputStream socketIn;
	private boolean isStopped;
	
	/**
	 * Create the input stream to listen the socket and set isStopped to false.
	 * 
	 * @param socket the socket used by the client
	 * @throws IOException if there is some error in creation of the input stream
	 */
	public RequestHandlerSocket(Socket socket) throws IOException {
		this.socket = socket;
		this.socketIn = new ObjectInputStream(socket.getInputStream());
		this.isStopped = false;
	}
	
	@Override
	public void run() {
		
		try {
			while(!isStopped) {
				
				RequestMessage request = (RequestMessage) socketIn.readObject();

				if(request == null)
					continue;
				
				/*
				 * Notify the ClientHandler when a connection rqst arrived
				 * to add the client to Lobby
				 */
				else if(request instanceof ConnectionRqst) {
					synchronized (this) {
						notify();
					}
				}
				/*
				 * Forward the request to game controller, closed the input stream
				 * and stop the thread.
				 */
				else if(request instanceof ExitRqst){
					notifyObserver(request);
					socket.shutdownInput();
					isStopped = true;
				}
					
				else
					notifyObserver(request);
			}
			
		} catch(SocketException e) {

			//Throw this exception to trigger the clientHandler to close the connection.
			throw new RuntimeException("Client lost connection");
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error occoured reading socket", e);
		} catch (ClassNotFoundException e ) {
			LOGGER.log(Level.WARNING, "Can't read from input stream", e);
		}  
		
	}

}
