package it.polimi.ingsw.cg32.view.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * This class rapresent the info panel of the player, it is composed by:<br>
 * -The victory points progress bar<br>
 * -The number of emporiums to build<br>
 * -The coins<br>
 * -the politic card<br>
 * -the permit card<br>
 * -the bonus king card<br>
 * -the number of assistants<br>
 * 
 * @author Stefano
 *
 */
public class PlayerPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Init size and layour of the panel and create the panel.
	 */
	public PlayerPanel() {
		this.setPreferredSize(new Dimension(0, 100));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		initPlayerPanel();
	}

	/**
	 * Init panel.
	 */
	private void initPlayerPanel() {

		JPanel cards = new JPanel();
		cards.setOpaque(false);
		cards.setLayout(new BoxLayout(cards, BoxLayout.LINE_AXIS));
		JLabel permitCard = new JLabel();
		JLabel politicCard = new JLabel();
		JLabel bonusKingCard = new JLabel();

		permitCard.setIcon(new ImageIcon(new ImageIcon("src/main/resources/GUI/permitCard.jpg")
				.getImage().getScaledInstance(95, 95, Image.SCALE_DEFAULT)));
		politicCard.setIcon(new ImageIcon(new ImageIcon("src/main/resources/GUI/politicCard.jpg")
				.getImage().getScaledInstance(95, 95, Image.SCALE_DEFAULT)));
		bonusKingCard.setIcon(new ImageIcon(new ImageIcon("src/main/resources/GUI/bonusKingCard.jpg")
				.getImage().getScaledInstance(95, 95, Image.SCALE_DEFAULT)));
		
		cards.add(bonusKingCard);
		cards.add(Box.createHorizontalStrut(20));
		cards.add(politicCard);
		cards.add(Box.createHorizontalStrut(20));
		cards.add(permitCard);
		
		JPanel statics = new JPanel();
		statics.setOpaque(false);
		statics.setLayout(new BoxLayout(statics, BoxLayout.LINE_AXIS));
		
		Font font = new Font("Sans", Font.BOLD, 16);
		
		JPanel progressBars = new JPanel();
		progressBars.setOpaque(false);
		progressBars.setLayout(new BoxLayout(progressBars, BoxLayout.PAGE_AXIS));
		progressBars.add(new JProgressBar());
		progressBars.add(Box.createVerticalStrut(10));
		progressBars.add(new JProgressBar());
		progressBars.add(Box.createVerticalStrut(10));
		progressBars.add(new JProgressBar());
		progressBars.add(Box.createVerticalStrut(10));
		progressBars.add(new JProgressBar());

		
		JPanel labels = new JPanel();
		labels.setPreferredSize(statics.getPreferredSize());
		labels.setOpaque(false);
		labels.setLayout(new BoxLayout(labels, BoxLayout.PAGE_AXIS));
		JLabel victory = new JLabel("VictoryPoints");
		victory.setFont(font);
		labels.add(victory);
		labels.add(Box.createVerticalStrut(5));
		JLabel coins = new JLabel("Coins");
		coins.setFont(font);
		labels.add(coins);
		labels.add(Box.createVerticalStrut(5));
		JLabel assistant = new JLabel("Assistants");
		assistant.setFont(font);
		labels.add(assistant);
		labels.add(Box.createVerticalStrut(5));
		JLabel emporiums = new JLabel("Emporiums");
		emporiums.setFont(font);
		labels.add(emporiums);
		
		statics.add(labels);
		statics.add(progressBars);
		statics.add(Box.createHorizontalGlue());
		
		this.add(Box.createHorizontalStrut(30));
		this.add(statics);
		this.add(Box.createHorizontalGlue());
		this.add(cards);
		this.add(Box.createHorizontalStrut(30));
	}

}
