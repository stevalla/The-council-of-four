package it.polimi.ingsw.cg32.view.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import it.polimi.ingsw.cg32.view.client.cli.CliGame;
import it.polimi.ingsw.cg32.view.client.cli.CliMessageVisitor;
import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;
import it.polimi.ingsw.cg32.view.client.rmi.RMIClient;
import it.polimi.ingsw.cg32.view.client.socket.SocketClient;
import it.polimi.ingsw.cg32.view.gui.EnterGame;

/**
 * Entry point for a game Player with a simple GUI to
 * select the type of connection and the game mode.
 * Implements {@link ActionListener} to manage the initial GUI.
 * 
 * @author Stefano
 *
 */
public class GamePlayer implements ActionListener{
	
	private static final Logger LOGGER = Logger.getLogger(GamePlayer.class.getName());
	private static final int SOCKET_PORT = 29999;
	private static final int RMI_PORT = 777;
	private ClientConnection connection;
	private final EnterGame enterGame;
	private final ResponseVisitor visitor;
	
	/**
	 * Create the enter GUI panel and the {@link CliMessageVisitor} to
	 * manage server response.
	 */
	public GamePlayer() {
		this.enterGame = new EnterGame(this);
		this.visitor = new CliMessageVisitor();
	}
	
	/**
	 * Start the initial GUI.
	 */
	private void start() {
		SwingUtilities.invokeLater(() -> enterGame.startConnection());
	}
	
	/**
	 * Create a new connection based on the player selection.
	 * If occurred some errors throw an Exception.
	 * 
	 * @param typeOfConnection the selected connection
	 * @throws RemoteException | NotBoundException if there is some error connecting through RMI
	 * @throws IOException if there is some error connecting through Socket.
	 */
	private void connect(String typeOfConnection){
	
		try {

			if(typeOfConnection.matches("^(SOCKET)$"))
				connection = new SocketClient(SOCKET_PORT, "localhost", visitor);

			else 
				connection = new RMIClient(RMI_PORT, "localhost", visitor);

			connection.startClient();

		} catch (RemoteException | NotBoundException e) {
			LOGGER.log(Level.SEVERE, "An Error occur while connecting to RMI server", e);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "An Error occur while connecting to SOCKET server", e);
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JButton button = (JButton) e.getSource();
		
		if(button.getText().matches("^(RMI|SOCKET)$")){
			connect(button.getText().toUpperCase());
			enterGame.startView();
		}
		else {
			if(button.getText().matches("^CLI$")) {
				enterGame.stopGUI();
				new CliGame(connection, (CliMessageVisitor) visitor).initCliGame();
			}
			else if (button.getText().matches("^GUI$")) {
				//GUI not already implemented, there is only a little part but
				//the game must be run only in CLI mode.
			} else {
				throw new IllegalArgumentException();
			}
		}
		
	}

	/**
	 * Start the Game Player
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		GamePlayer gamePlayer = new GamePlayer();
		gamePlayer.start();
	}
}
