package it.polimi.ingsw.cg32.view.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.view.server.pubsub.Broker;
import it.polimi.ingsw.cg32.view.server.pubsub.BrokerInt;
import it.polimi.ingsw.cg32.view.server.rmi.RMIConnection;
import it.polimi.ingsw.cg32.view.server.rmi.RMIServer;
import it.polimi.ingsw.cg32.view.server.socket.SocketServer;

/**
 * This class manages the starting and the stopping of the servers:
 * {@link RMIServer} and {@link SocketServer} using {@link #startServers()}.<br>
 * It provides methods to stop the server, but they are not used because in our
 * implementation the servers keep running.<br>
 * 
 * @author Stefano
 *
 */
public class ServerConnectionFactory {

	private static final int SOCKET_SERVER_PORT = 29999;
	private static final int RMI_SERVER_PORT = 777;
	private final Lobby lobby;
	private final BrokerInt<ResponseMessage, String> broker;
	private SocketServer socketServer;
	private Registry registry;
	private RMIServer rmiServer;

	/**
	 * Create the unique {@link Lobby} to manage the creation of new matchs
	 * and the unique {@link Broker} that will be used by all the games controller 
	 * to send back response to their clients.
	 * 
	 * @param timeout time to check over the incactivity of a client
	 */
	public ServerConnectionFactory(int timeout) {
		if(timeout < 0)
			throw new IllegalArgumentException("The timeout can't be minor than zero");
		this.broker = new Broker<>();
		this.lobby = new Lobby(broker, timeout);
	}

	/**
	 * Start servers.
	 * 
	 * @throws IOException if there is one error starting servers
	 */
	public void startServers() throws IOException {
		startSocket();
		startRMI();
	}
	
	
	/**
	 * Stop servers.
	 */
	public void stopServers() {
		
		socketServer.stopServer();
		rmiServer.stopRMI();
		
	}

	/**
	 * Instantiate {@link SocketServer} and start it.
	 * 
	 * @throws IOException if there is one error starting the socket server
	 */
	private void startSocket() throws IOException {
		
		socketServer = new SocketServer(SOCKET_SERVER_PORT, lobby);

		socketServer.start();
	}
	
	/**
	 * Instantiate {@link RMIServer}.
	 * Create the {@link Registry} and add the {@link RMIConnection} to allow
	 * clients that chose RMI connection to connect to server.
	 * 
	 * @throws RemoteException if there is one error starting rmi server
	 */
	private void startRMI() throws RemoteException {
		
		registry = LocateRegistry.createRegistry(RMI_SERVER_PORT);

		rmiServer = new RMIServer(registry, lobby);

		RMIConnection connectionStub = (RMIConnection) UnicastRemoteObject.exportObject(rmiServer, 0);

		registry.rebind("CONNECTION", connectionStub);
			
	}

}
