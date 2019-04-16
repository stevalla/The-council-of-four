package it.polimi.ingsw.cg32.view.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class rapresent the components of the game out of the board.
 * 
 * It is composed by:<br>
 * -The assistant reserve<br>
 * -The councillor reserve<br>
 * -the deck of politic card<br>
 * 
 * @author giovanni
 *
 */
public class InfoBoard extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final JLabel assistantReserve = new JLabel();
	private final JPanel councillorReserve = new JPanel();
	private final JLabel politicCardDeck = new JLabel();
	private final Image assistantReserveImage;
	private final Image politicCardDeckImage;
	
	public InfoBoard() throws IOException {
		
		this.assistantReserveImage = 
				ImageIO.read(new File("src/main/resources/GUI/assistantReserve.png")).getScaledInstance(100, 100, Image.SCALE_DEFAULT); 

		this.politicCardDeckImage = 
				ImageIO.read(new File("src/main/resources/GUI/politicCardDeck.png")).getScaledInstance(100, 100, Image.SCALE_DEFAULT); 
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		assistantReserve.setIcon(new ImageIcon(assistantReserveImage));
		
		councillorReserve.setBackground(Color.red);
		councillorReserve.setPreferredSize(new Dimension(100, 100));
		
		politicCardDeck.setIcon(new ImageIcon(politicCardDeckImage));
		politicCardDeck.setPreferredSize(new Dimension(100, 100));
		
		this.add(Box.createVerticalStrut(40));
		this.add(politicCardDeck);
		this.add(Box.createVerticalStrut(50));
		this.add(councillorReserve);
		this.add(Box.createVerticalStrut(50));
		this.add(assistantReserve);
		this.add(Box.createVerticalStrut(40));
		
		this.setPreferredSize(new Dimension(200, 0));
		this.setOpaque(false);
		
	}
	
}
