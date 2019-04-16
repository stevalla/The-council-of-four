package it.polimi.ingsw.cg32.view.server;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.cg32.view.server.socket.SocketServer;

/**
 * Entry point to start the two servers: {@link RMIServer} and {@link SocketServer}.
 * 
 * @author Stefano
 *
 */
public class GameManager {
	
	private static final Logger LOG = Logger.getLogger(GameManager.class.getName());
	private static final int TIMEOUT = 60;	//Seconds
	
	/**
	 * Suppress the default constructor for noninstantiability 
	 */
	private GameManager() {
		throw new AssertionError();
	}
	
    /**
     * Start RMI and Socket servers.
     * 
     * @param args
     */
    public static void main(String[] args) {

    	//Initialize servers
        ServerConnectionFactory serverInitializer = new ServerConnectionFactory(TIMEOUT);

        try {

        	serverInitializer.startServers();
			
		} catch (RemoteException e) {
			LOG.log(Level.WARNING, "Error while creating a new RMI server.", e);
		} catch (IOException e){
			LOG.log(Level.WARNING, "Error while creating a new socket server.", e);
		}
        
        LOG.log(Level.INFO, "Game manager started, listening for RMI and Socket " +
                "incoming connections.");
    }

}
