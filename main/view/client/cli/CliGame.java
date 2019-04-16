package it.polimi.ingsw.cg32.view.client.cli;

import java.io.IOException;
import java.io.PrintStream;
import java.rmi.NotBoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.polimi.ingsw.cg32.message.request.ExitRqst;
import it.polimi.ingsw.cg32.message.request.RequestMessage;
import it.polimi.ingsw.cg32.utilities.exception.IllegalInputException;
import it.polimi.ingsw.cg32.view.client.ClientConnection;
import it.polimi.ingsw.cg32.view.client.rmi.RMIClient;
import it.polimi.ingsw.cg32.view.client.socket.SocketClient;

/**
 * Class game for a client in CLI mode.
 * 
 * @author Stefano
 *
 */
public class CliGame{

	private static final Logger LOGGER = Logger.getLogger(CliGame.class.getName());
	private final Scanner stdin;
	private final CliInterpreter interpreter;
	private final ClientConnection connection;
	private final CliMessageVisitor visitor;
	private boolean isConnected;
	private final PrintStream out;
	
	/**
	 * Construct a CLIGame with an interface connection that
	 * is implemented from two classes, {@link SocketClient} and
	 * {@link RMIClient} two perform the selected connection
	 * using the Strategy pattern.
	 * 
	 * @param connection the generic {@link ClientConnection}.
	 * @param visitor the {@link CliMessageVisitor} which take the response
	 * 		  from server and display it to the client after the update
	 * 		  of the game in client side
	 * @throws NullPointerException if params are null
	 */
	public CliGame(ClientConnection connection, CliMessageVisitor visitor) {
		
		if(connection == null || visitor == null)
			throw new NullPointerException("Params can't be null");
		
		this.out = new PrintStream(System.out);
		this.stdin = new Scanner(System.in);
		this.connection = connection;
		this.interpreter = new CliInterpreter(visitor.getBonusToExecute());
		this.visitor = visitor;
	}
	
	/**
	 * Method that initialize the Cli game and listen from
	 * stdin to catch client strings and send to server.
	 * Use an abstract class connection to use Strategy pattern
	 * and create the right connection based on the choice
	 * of the player.
	 */
	public void initCliGame() {
		
		String cmd;
		
		do {
			out.println("Type START to enter in a game");
			cmd = stdin.nextLine().toUpperCase();
		} while (!cmd.matches("^START$"));
		
		try {
			
			connection.connect();
			
		} catch (NotBoundException e) {
			LOGGER.log(Level.SEVERE, "Problem connetting to RMI server", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Problem connetting to Socket server", e);
		}
		 
		isConnected = true;
		
		while(isConnected) {
			
			try {
				cmd = stdin.nextLine().toUpperCase();

				RequestMessage request = interpreter.parserMessage(cmd, visitor.getGame(), visitor.getToken());
				
				if (request != null) 
					connection.sendRequest(request);
				else
					continue;
				
				if(request instanceof ExitRqst) {
					stdin.close();
					isConnected = false;
				}
				
			} catch (IllegalInputException e) {
				LOGGER.log(Level.INFO, e.getMessage());
			} 
		}
	}

}
