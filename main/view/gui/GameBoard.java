package it.polimi.ingsw.cg32.view.gui;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * This class rapresent the panel with the map of the game.
 * 
 * @author Stefano
 *
 */
public class GameBoard extends JPanel {

	private static final long serialVersionUID = 1L;
	private final Image backgroundImage;
	
	/**
	 * Load the map of the game.
	 * 
	 * @throws IOException
	 */
	public GameBoard() throws IOException {
		this.backgroundImage = ImageIO.read(new File("src/main/resources/GUI/board.jpg"));
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}

}
