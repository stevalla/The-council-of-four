package it.polimi.ingsw.cg32.view.client.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg32.message.response.CommunicationResponse;
import it.polimi.ingsw.cg32.message.response.InitializeGameResponse;
import it.polimi.ingsw.cg32.message.response.StartTurnResponse;
import it.polimi.ingsw.cg32.message.response.TokenResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.message.response.market.MarketResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class use Visitor pattern to avoid the use of instance of
 * in client side with Server Response. His function is to set the
 * game in client side, do updates and show message in console
 * with information about the running game and to save the bonus
 * to execute when the player won some bonus.<br>
 * The use of visitor in this case helps to reduce the use of instance of
 * because every object passed into socket is send as a generic
 * {@link Object}.
 * 
 * @author Stefano
 *
 */
public class CliMessageVisitor implements ResponseVisitor {
 
	private Game game;
	private Token token;
	private final List<Bonus> bonusToExecute = new ArrayList<>();
	private PrintStream out = new PrintStream(System.out);

	@Override
	public void display(ActionResponse response) {
		game = response.getGame();
		cli(response.getMessage());
		 
		if(!response.getBonus().isEmpty()) {
			if(response.getPlayerId() == token.getPlayerNumber()){
				bonusToExecute.addAll(0, response.getBonus());
				cli("You've won:\n" + response.getBonus() + "\nProced with the execution");
			}
			else
				cli("He won:\n" + response.getBonus() + "\n");
		}
		else if(!bonusToExecute.isEmpty()) {
			cli("He still has to execute:\n" + bonusToExecute + "\nProced with the execution\n");
		}
	} 
	  
	@Override
	public void display(BonusResponse response) {

		game = response.getGame();
		
		cli(response.getMessage());
		
		if(!response.getBonus().isEmpty()) {
			if(response.getPlayerId() == token.getPlayerNumber()){
				bonusToExecute.remove(response.getTypeOfBonus());
				bonusToExecute.addAll(0, response.getBonus());
				cli("You've won:\n" + response.getBonus() + "\nProced with the execution");
			}
			else
				cli("He won:\n" + response.getBonus() + "\n");
		}
		else if(!bonusToExecute.isEmpty()) {
			cli("He still has to execute:\n" + bonusToExecute + "\nProced with the execution\n");
		}
	}

	@Override
	public void display(MarketResponse response) {
		
		game = response.getGame();
		
		cli(response.getMessage());
		cli("\nThe current state of the store:\n\n");
		cli(game.storeToString());
	}

	@Override
	public void display(InitializeGameResponse response) {

		game = response.getGame();
		cli("GAME STARTED!\n\n");
	}

	@Override
	public void display(StartTurnResponse response) {

		game = response.getGame();
		cli(response.getMessage());
		cli("[type info to know how to type info]");
		cli("[type action to know how to type actions]");
	}
	
	@Override
	public void display(TokenResponse response) {
		token = response.getToken();
	}

	@Override
	public void display(CommunicationResponse response) {
		
		cli(response.getMessage());
	}
	
	
	/**
	 * @return the list of bonusToExecute
	 */
	public List<Bonus> getBonusToExecute() {
		return bonusToExecute;
	}

	/**
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * @return the token of the client
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * Display the message in consolle.
	 * 
	 * @param message the message to display in stdOut
	 */
	private void cli(String message) {
		out.println(message);
	}
	
}
