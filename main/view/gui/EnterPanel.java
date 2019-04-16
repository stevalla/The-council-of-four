package it.polimi.ingsw.cg32.view.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import it.polimi.ingsw.cg32.view.client.GamePlayer;


/**
 * This class rapresent the enter panel of the game, it is composed by a
 * frame with an image of the game as background and a Label with
 * the possibility of chose the type of connection and the type of view
 * use for the client.
 * 
 * @author Stefano
 *
 */
public class EnterPanel extends JLayeredPane {
	
	private static final long serialVersionUID = 1L;
	private Image image;
	private final JPanel modalPanel;

	/**
	 * Initialize enter panel.
	 */
	public EnterPanel() {
		
		this.modalPanel = new JPanel();
		modalPanel.setBackground(new Color(192, 192, 192, 230));

		try {
			image = ImageIO.read(new File("src/main/resources/GUI/TheCouncilOfFour.jpg"));
			this.setSize(getPreferredSize());
			this.setLayout(new GridBagLayout());
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}  
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(650, 800);
	}
	
	/**
	 * Compose the label with the choice of connections.
	 * 
	 * @param gamePlayer the {@link GamePlayer}
	 */
	public void fullFillContainerForConnections(GamePlayer gamePlayer) {

		addText("Choose connection");
		addButton("RMI", "SOCKET", gamePlayer);
		
		this.add(modalPanel);
	}
	
	/**
	 * Compose the label with the choice of views.
	 * 
	 * @param gamePlayer the {@link GamePlayer}
	 */
	public void fullFillContainerForViews(GamePlayer gamePlayer) {
		modalPanel.removeAll();
		addText("Choose view");
		addButton("CLI", "GUI", gamePlayer);
		
		this.add(modalPanel);
	}
	
	/**
	 * Create a label question with two choice using
	 * the text as title.
	 * 
	 * @param text the text to add in the label
	 */
	private void addText(String text) {
		JLabel question = new JLabel(text, SwingUtilities.CENTER);
		question.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
		question.setOpaque(false);
		
		modalPanel.add(question, BorderLayout.NORTH);
	}
	
	/**
	 * Create two generics button to add at the label question with the
	 * strings passed as params.
	 * 
	 * @param one text first button
	 * @param two text second button
	 * @param gamePlayer the {@link GamePlayer}
	 */
	private void addButton(String one, String two, GamePlayer gamePlayer) {
		JPanel buttonPanel = new JPanel(new FlowLayout());
		ButtonGroup buttons = new ButtonGroup();
		
		JButton button1 = new JButton(one);
		button1.setContentAreaFilled(false);
		button1.setBorderPainted(false);
		button1.setActionCommand(one.toUpperCase());
		button1.addActionListener(gamePlayer);
		JButton button2 = new JButton(two);
		button2.setActionCommand(two.toUpperCase());
		button2.setContentAreaFilled(false);
		button2.setBorderPainted(false);
		button2.addActionListener(gamePlayer);
		
		buttons.add(button1);
		buttons.add(button2);
		
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.setOpaque(false);
		
		modalPanel.add(buttonPanel, BorderLayout.CENTER);
	}

}

