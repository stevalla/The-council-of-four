package it.polimi.ingsw.cg32.view.server.socket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.cg32.message.response.ExitGameResponse;
import it.polimi.ingsw.cg32.view.server.pubsub.SubscriberInt;


/**
 * This class is used to send response from server to the corresponding client.
 * Manage the received response with a synchronized buffer.
 * Implement Runnable for multithreading.
 * 
 * @author giovanni
 *
 * @param <M> messages to dispatch
 */
public class SubscriberSocket<M> implements Runnable, SubscriberInt<M> {

	private static final Logger LOGGER = Logger.getLogger(SubscriberSocket.class.getName());
	private ConcurrentLinkedQueue<M> buffer = new ConcurrentLinkedQueue<>();
	private boolean isDone;
	private ObjectOutputStream out;
	
	/**
	 * Create the output stream to write in the socket.
	 * 
	 * @param socket the socket used by the client
	 * @throws IOException if there is one error in creation of the output stream
	 */
	public SubscriberSocket(Socket socket) throws IOException {
		this.isDone = false;
		this.out = new ObjectOutputStream(socket.getOutputStream());
	}
	
	@Override
	public void run() {

		while(!isDone) {
			M response = buffer.poll();
			if(response instanceof ExitGameResponse)
				finish();
			
			if(response != null)
				send(response);
			else {

				try {
					synchronized (buffer) {
						buffer.wait();
					}
				} catch (InterruptedException e) {
					/*
					 * If the client lost connection request handler is automatically closed 
					 * throwing a RunTimeException, so the clientHandler forces this thread
					 * to terminate and this exception is reached.
					 */
					finish();
					Thread.currentThread().interrupt();
				}
			}
		}
		
	}
	
	/**
	 * Write the response in the socket to send to the client.
	 * 
	 * @param response the message to send in the socket
	 */
	private void send(M response) {
		try {
			out.reset();
			out.writeObject(response);
			out.flush();
			
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error while writing to socket.", e);
			finish();
		}
	}

	/**
	 * Stop the thread that write response in the socket
	 */
	public void finish() {
		this.isDone = true;
	}

	@Override
	public void dispatchMessage(M response) {
		buffer.add(response);
		synchronized(buffer){
			buffer.notify();
		}
	}

}
