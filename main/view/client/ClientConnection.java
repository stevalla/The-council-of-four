package it.polimi.ingsw.cg32.view.client;

import java.io.IOException;
import java.rmi.NotBoundException;

import it.polimi.ingsw.cg32.message.request.RequestMessage;
import it.polimi.ingsw.cg32.message.response.TokenResponse;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;
import it.polimi.ingsw.cg32.view.client.rmi.RMIClient;
import it.polimi.ingsw.cg32.view.client.socket.SocketClient;

/**
 * Abstract class to manage connection in client side.
 * Provide three abstract methods to start the client, init the connection
 * and send request to server.<br>
 * It is extended by {@link SocketClient} and {@link RMIClient} that with
 * their overrides' methods using two different type of connection.<br>
 * Thanks to extension for the client the two mechanism run in the same way,
 * because the client only see this abstract class.<br>
 * 
 * @author Stefano
 *
 */ 
public abstract class ClientConnection  {

	private final int serverPort;
	private final String host;
	private final ResponseVisitor visitor;
	
	/**
	 * Create a simple Client connection class.
	 * 
	 * @param serverPort the port of the server
	 * @param host the host of the server
	 * @param manager the manager to manage client side game
	 * @throws NullPointerException if host or manager are null
	 */
	public ClientConnection(int serverPort, String host, ResponseVisitor visitor) {
		if(host == null)
			throw new NullPointerException("Host can't be null");
		if(visitor == null)
			throw new NullPointerException("Visitor can't be null");
		
		this.serverPort = serverPort;
		this.host = host;
		this.visitor = visitor;
	}
	
	/**
	* public method used to initialize the connection structure at server side.<br>
	* It is overriden by sub-classes to differently initialize conection,
	* relying on the choosen connection [RMI/SOCKET].
	* 
	* @throws IOException if there is some errors connecting to server
	* @throws NotBoundException if there is some errors connecting to server
	*/
	protected abstract void startClient() throws IOException, NotBoundException;
	
	/**
	* public method used to send request to server.<br>
	* It is overriden by sub-classes to send request to server exploiting
	* the characteristics of the choosen connection [RMI/SOCKET].
	* 
	* @param request the {@link RequestMessage} to send to server
	*/
	public abstract void sendRequest(RequestMessage request);
	
	/**
	 * Method to connect the client to server.
	 * it's override by sub-classes to manage differently the request of connection.<br>
	 * Anyway after a client connect with the server using this method it will be notified by
	 * the server with a {@link TokenResponse} that carries the token that identfy the client.
	 * 
	 * @throws IOException if there is some errors connecting to server
	 * @throws NotBoundException if there is some errors connecting to server
	 */
	public abstract void connect() throws NotBoundException, IOException;

	/**
	 * @return the server port
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @return the server host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the {@link ClientManager} to manage client side game
	 */
	public ResponseVisitor getCliMessageVisitor() {
		return visitor;
	}
	
}
