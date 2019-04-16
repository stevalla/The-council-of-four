package it.polimi.ingsw.cg32.view.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.cg32.view.server.Lobby;

/**
 * This thread is the server for request-response connections, it listens on a port for
 * incoming connections and instantiate a new SocketHandler when that happens using an
 * {@link ExecutorService}.
 * 
 * @author Stefano
 *
 */
public class SocketServer extends Thread{
	
	private static final Logger LOG = Logger.getLogger(SocketServer.class.getName());
	private final int port;
	private final Lobby lobby;
	private final ExecutorService executor;
	private boolean isStopped;
	private final ServerSocket serverSocket;
	
	/**
	 * Create a new SocketServer and initalize Executor to handle the client connections.
	 * 
	 * @param port the port where to listen
	 * @param lobby the {@link Lobby} of the server
	 * @throws IOException if there is an error in creation of the ServerSocket
	 */
	public SocketServer(int port, Lobby lobby) throws IOException {
		this.lobby = lobby;
		this.port = port;
		this.isStopped = false;
		this.executor  = Executors.newCachedThreadPool();
		this.serverSocket = new ServerSocket(port);
	}

	@Override
	public void run() {

		while(!isStopped){

			try{

				Socket socket = serverSocket.accept();

				ClientHandlerSocket client = new ClientHandlerSocket(socket, lobby);

				executor.submit(client);

			}
			catch(IOException e){
				LOG.log(Level.WARNING, "Error in establishing a new connection", e);
			}
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Can't close socket server.", e);
		}
	}

	/**
	 * Shutdown the executor to reject all furthers connection request
	 * and change the state of the boolean to stop the thread.
	 */
	public void stopServer() {

		executor.shutdown();
		this.isStopped = true;

	}

	/**
	 * @return the port where to listen
	 */
	public int getPort() {
		return port;
	}
	
}
