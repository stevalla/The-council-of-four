package it.polimi.ingsw.cg32.view.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.WindowConstants;

/**
 * Boarder game for GUI game. It rapresent the view for clients
 * and it is composed by the board of the game, a panel with the information about
 * the state of the player during the game, and a MenuBar with the information
 * about the others players.
 * This is only a static model of the GUI view.
 * 
 * @author Stefano
 *
 */
public class BoarderGame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String CUSTOM_FONT = "SERIF";
	private final JMenuBar menuBarBoard;
	private final GameBoard board;
	private final PlayerPanel infoPlayer;
	private final JLabel backgroundBoard;
	private final InfoBoard infoBoard;
	
	/**
	 * Create the boarder game.
	 * 
	 * @throws IOException 
	 */
	public BoarderGame() throws IOException {
		this.menuBarBoard = new JMenuBar();
		this.board = new GameBoard();
		this.infoPlayer = new PlayerPanel();
		this.infoBoard = new InfoBoard();
		this.backgroundBoard = new JLabel(new ImageIcon("src/main/resources/GUI/backgroun.jpg"));
		this.setContentPane(backgroundBoard);
		backgroundBoard.setLayout( new BorderLayout());
		initBoard();
	}

	/**
	 * Init board components
	 */
	private void initBoard() {
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screen.height * 4/5;
		int width = screen.width * 3/5;
		this.setPreferredSize(new Dimension(width, height));
		
		this.setTitle("The Councillor of Four");
		this.setIconImage(new ImageIcon("src/main/resources/GUI/icon.jpg").getImage());
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		initMenuBar();
		
		infoBoard.setBackground(Color.green);
		infoBoard.setPreferredSize(new Dimension(350, 0));
		infoBoard.setOpaque(false);

		this.add(infoBoard, BorderLayout.EAST);
		
		backgroundBoard.add(board, BorderLayout.CENTER);
		
		infoPlayer.setOpaque(false);
		backgroundBoard.add(infoPlayer, BorderLayout.NORTH);
		
		this.pack();
		this.setVisible(true);
	}
	
	
	/**
	 * Init Menu bar of the Frame.
	 */
	private void initMenuBar() {
		JButton actions = new JButton("ACTIONS");
		actions.setBackground(new Color(64, 64, 64));
		actions.setFocusable(false);
		actions.setContentAreaFilled(false);
		actions.setBorderPainted(false);
		actions.setFont(new Font(CUSTOM_FONT, Font.BOLD, 16));
		actions.setForeground(new Color(202, 15, 15));
		actions.setPreferredSize(new Dimension(150, 30));
		actions.setToolTipText("Click to perform an action");
		actions.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				actions.setContentAreaFilled(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				actions.setContentAreaFilled(false);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
			}
		});
		
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
		for(int i=0; i<5; i++)
			model.addElement("Player" + i);
		JComboBox<String> playersList = new JComboBox<>(model);
		playersList.setOpaque(false);
		playersList.setMaximumSize(new Dimension(3150, actions.getPreferredSize().height));
		playersList.setToolTipText("Select a player to see his statics");
		playersList.setFont(new Font(CUSTOM_FONT, Font.BOLD, 16));
		playersList.setBackground(new Color(64, 64, 64));
		playersList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
			}
		});
		
		JButton rules = new JButton("RULES");
		rules.setBackground(new Color(64, 64, 64));
		rules.setFocusable(false);
		rules.setContentAreaFilled(false);
		rules.setBorderPainted(false);
		rules.setFont(new Font(CUSTOM_FONT, Font.BOLD, 16));
		rules.setForeground(new Color(202, 15, 15));
		rules.setPreferredSize(new Dimension(150, 30));
		rules.setToolTipText("Click to see the rules");
		rules.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				rules.setContentAreaFilled(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				rules.setContentAreaFilled(false);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
			}
		});
		

		menuBarBoard.setBackground(Color.BLACK);
		menuBarBoard.add(actions);
		menuBarBoard.add(playersList);
		menuBarBoard.add(Box.createHorizontalGlue());
		menuBarBoard.add(rules);
		this.setJMenuBar(menuBarBoard);
	}

	/**
	 * Simple main to try it.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		new BoarderGame();
	}
}
