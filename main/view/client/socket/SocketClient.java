package it.polimi.ingsw.cg32.view.client.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.cg32.message.request.ConnectionRqst;
import it.polimi.ingsw.cg32.message.request.ExitRqst;
import it.polimi.ingsw.cg32.message.request.RequestMessage;
import it.polimi.ingsw.cg32.message.response.ExitGameResponse;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.view.client.ClientConnection;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;

/**
 * This class rapresent the client side of the game in Socket connection.
 * <p>
 * It is possible to ask for a new connection sending a {@link ConnectionRqst} 
 * (a Token will be received to authenticate the client), send requests to 
 * the server and received response from the server.
 * </p>
 *  
 * @author Stefano
 *
 */
public class SocketClient extends ClientConnection {
	
	public static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());
	private ObjectOutputStream socketOut;
	private ObjectInputStream socketIn;
	private Socket socket;

	/**
	 * Create a new SocketClient to manage request to server and response to client.
	 * 
     * @param server the port of the game manager server
     * @param the hostname of the game manager
	 * @param visitor the {@link ResponseVisitor} to manage response from server
	 * @throws IOException if there is an error in creation of the socket
	 * @throws NullPointerException if params are null
	 */
	public SocketClient(int serverPort, String host, ResponseVisitor visitor) throws IOException {
		super(serverPort, host, visitor);
		this.socket = new Socket(host, serverPort);
	}

	@Override
	public void connect() throws IOException {
		
		ConnectionRqst connectionRqst = new ConnectionRqst(null);
		
		//Send the connection request to the server
		sendRequest(connectionRqst);
	}


	@Override
	protected void startClient() throws IOException {

		//Set up socket configuration
		socketOut = new ObjectOutputStream(socket.getOutputStream());
		socketIn = new ObjectInputStream(socket.getInputStream());

		//Start thread to received response
		receivedResponse();
	}

	@Override
	public void sendRequest(RequestMessage request) {
		
		try {
			socketOut.reset();
			socketOut.writeObject(request);
			socketOut.flush();

			//Close the out of the socket if the client send the exit request
			if(request instanceof ExitRqst)
				socket.shutdownOutput();
		
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "Error occoured writing in socket", e);
		}
		
	}
	
	/**
	 * This method use a {@link Thread} to listen the socket and received response
	 * from the server to display at the client using the response visitor.
	 */
	public void receivedResponse() {
		
		Runnable receive = () -> {
			while(true) {
				try {
					//Received a ResponseMessage cast obligatory
					ResponseMessage received = (ResponseMessage) socketIn.readObject();
					
					//If is an Exit Game close the socket and stop the thread
					if(received instanceof ExitGameResponse){
						socket.shutdownInput();
						socket.close();
						break;
					}
					
					//Display the response using visitor pattern
					received.display(getCliMessageVisitor());

				} catch (ClassNotFoundException | IOException e) {
					LOGGER.log(Level.WARNING, "Error occoured reading socket, try to disconnect.", e);
					try {
						socket.close();
						break;
					} catch (IOException exception) {
						LOGGER.log(Level.WARNING, "Error occoured closing socket", exception);
					}
				}
			}
		};
		
		Thread receiver = new Thread(receive);
		receiver.start();
	}


}
