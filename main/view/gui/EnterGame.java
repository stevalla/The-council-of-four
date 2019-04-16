package it.polimi.ingsw.cg32.view.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import it.polimi.ingsw.cg32.view.client.GamePlayer;

/**
 * Frame for enter game with the possibility of choose the type of connection
 * (Socket or RMI) and the type of view (CLI or GUI).
 * 
 * @author Stefano
 *
 */
public class EnterGame {

	private final JFrame frame;
	private final GamePlayer gamePlayer;
	private final EnterPanel container;
	
	/**
	 * Create the start frame of the game.
	 * 
	 * @param gamePlayer 
	 */
	public EnterGame(GamePlayer gamePlayer) {
		this.frame = new JFrame("The Council Of Four");
		this.frame.setIconImage(new ImageIcon("src/main/resources/GUI/icon.jpg").getImage());
		this.container = new EnterPanel();
		this.gamePlayer = gamePlayer;
	}
	
	/**
	 * Create the panel to chose the connection.
	 */
	public void startConnection() {
		
		container.fullFillContainerForConnections(gamePlayer);

		frame.add(container);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	
	/**
	 * Create the panel to chose the type of view
	 */
	public void startView() {
		container.removeAll();
		frame.repaint();
		
		container.fullFillContainerForViews(gamePlayer);
		
		frame.setVisible(true);
	}
	
	/**
	 * Stop the GUI.
	 */
	public void stopGUI() {
		frame.dispose();
	}
	
	/**
	 * Start the GUI (Not implemented)
	 */
	public void startGUI() {
		
		container.removeAll();
		frame.repaint();
		
		JButton start = new JButton("START");
		start.setForeground(Color.RED);
		start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		container.add(start);
		
		frame.setVisible(true);
	}

}
