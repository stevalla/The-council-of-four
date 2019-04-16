package it.polimi.ingsw.cg32.view.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * This class rapresent the panel with the info action card that the player
 * can perform.
 * 
 * @author Stefano
 *
 */
public class InfoAction extends JDialog {

	private static final long serialVersionUID = 1L;
	static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
	private final JLabel backgroundAction = new JLabel();
	private final Image action;
	
	/**
	 * Crate the panel with the image of info action.
	 * 
	 * @throws IOException
	 */
	public InfoAction() throws IOException {
	
		int dialogWidth = SCREEN_DIMENSION.width / 4; //example; a quarter of the screen size
		int dialogHeight = SCREEN_DIMENSION.height / 4;
		int dialogX = SCREEN_DIMENSION.width / 2 - dialogWidth / 2; //position right in the middle of the screen
		int dialogY = SCREEN_DIMENSION.height / 2 - dialogHeight / 2;
		
		this.action = ImageIO.read(new File("src/main/resources/GUI/action.jpg")).getScaledInstance(500, 300, Image.SCALE_DEFAULT); 
		backgroundAction.setIcon(new ImageIcon(action));
		backgroundAction.setLayout(new GridLayout(4, 2));
		
		this.setContentPane(backgroundAction);
		
		JLabel buyPermitCard = new JLabel();
		JLabel buildEmporiumByKingHelpy = new JLabel();
		JLabel bildEmporiumByPermitCard = new JLabel();
		JLabel electCouncillor = new JLabel();
		JLabel hireAssistant = new JLabel();
		JLabel useAssistantToElectCouncillor = new JLabel();
		JLabel performAnotherPrimaryAction = new JLabel();
		JLabel changeUsablePermitCard = new JLabel();
		
		buyPermitCard.setToolTipText("ciao bpc");
		bildEmporiumByPermitCard.setToolTipText("ciao bebpc");
		buildEmporiumByKingHelpy.setToolTipText("ciao bebkh");
		electCouncillor.setToolTipText("ciao elect");
		hireAssistant.setToolTipText("ciao hire");
		useAssistantToElectCouncillor.setToolTipText("cia use");
		performAnotherPrimaryAction.setToolTipText("ciao Perform");
		changeUsablePermitCard.setToolTipText("ciaochange");
		
		backgroundAction.add(buyPermitCard);
		backgroundAction.add(hireAssistant);
		
		this.setBounds(dialogX, dialogY, action.getWidth(backgroundAction), action.getHeight(backgroundAction));
		
		this.setResizable(false);
		
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	
	public static void main(String[] args) throws IOException {
		new InfoAction();
	}
	
	
	
}
