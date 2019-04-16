package it.polimi.ingsw.cg32.view.client.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.cg32.message.request.ExitRqst;
import it.polimi.ingsw.cg32.message.request.RequestMessage;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.view.client.ClientConnection;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;
import it.polimi.ingsw.cg32.view.server.rmi.RMIConnection;
import it.polimi.ingsw.cg32.view.server.rmi.RequestHandlerInt;

/**
 * This class rapresent the client side of the game in RMI connection.
 * <p>
 * It is possible to ask for a new connection taking the stub of {@link RequestHandlerInt}
 * from the Registry (a Token will be received to authenticate the client), send requests to 
 * the server and received response from the server.
 * </p>
 * Implement the Remote interface {@link ResponseHandlerInt} to allow the server
 * send response.
 * 
 * @author Stefano
 *
 */
public class RMIClient extends ClientConnection implements ResponseHandlerInt<ResponseMessage> {

	private static final Logger LOGGER = Logger.getLogger(RMIClient.class.getName());
	
	//The stub to allow the client connect to server
	private RMIConnection rmiConnectionStub;
	
	//The stub to allow the client send request to server
	private RequestHandlerInt requestHandlerStub;
	private static final String CONNECTION = "CONNECTION";
	private Registry registry;
	private final String clientId;
	
	
	/**
	 * Create a new RMIClient to manage request to server and response to client.
	 * 
     * @param server the port of the game manager server
     * @param the hostname of the game manager
	 * @param visitor the {@link ResponseVisitor} to manage response from server
	 * @throws RemoteException if there is some error connecting RMI
	 */
	public RMIClient(int serverPort, String host, ResponseVisitor visitor) throws RemoteException {
		super(serverPort, host, visitor);
		registry = LocateRegistry.getRegistry(getHost(), getServerPort());

		//Use this Id to differentiate the clients stub in the registry
		this.clientId = UUID.randomUUID().toString();
	}

	
	@Override
	protected void startClient() throws RemoteException, NotBoundException {
		//Get the registry to connect to server	
		rmiConnectionStub = (RMIConnection) registry.lookup(CONNECTION);

		//Unchecked cast because we are sure that the stub is a ResponseHandlerRmi of ResponseMessage
		@SuppressWarnings("unchecked")
		ResponseHandlerInt<ResponseMessage> stubResponse = 
			(ResponseHandlerInt<ResponseMessage>) UnicastRemoteObject.exportObject(this, 0);

		//Put in the registry the stub so the server can take it and send response to client
		registry.rebind("RESPONSE" + clientId, stubResponse);

	}

	@Override
	public void connect() throws RemoteException, NotBoundException {

		rmiConnectionStub.connectRMI(clientId);

		//Search the stub to send request to server
		requestHandlerStub = (RequestHandlerInt) registry.lookup("REQUEST" + clientId);
		
	}

	@Override
	public void sendRequest(RequestMessage request) {
		
		try {
			
			/*
			 * If is an exit request unbind of the response stub to don't allow
			 * the server from now to send response 
			 */
			if(request instanceof ExitRqst) {
				try {
					registry.unbind("RESPONSE" + clientId);
		            UnicastRemoteObject.unexportObject(this, true);

				} catch (NotBoundException e) {
					LOGGER.log(Level.WARNING, "Exception thrown while releasing RMI.", e);
				}
			}
			
			//Send response to server
			requestHandlerStub.receivedRequest(request);
		} catch (RemoteException e) {
			LOGGER.log(Level.WARNING, "Error occoured sending a request to server through RMI", e);
		}
	}

	@Override
	public void receivedResponse(ResponseMessage response) throws RemoteException {

		//Received response and display it using visitor pattern
		response.display(getCliMessageVisitor());

	}

}
