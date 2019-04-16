package it.polimi.ingsw.cg32.view.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import it.polimi.ingsw.cg32.controller.GameController;
import it.polimi.ingsw.cg32.message.response.CommunicationResponse;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.view.server.pubsub.Broker;
import it.polimi.ingsw.cg32.view.server.pubsub.BrokerInt;
import it.polimi.ingsw.cg32.view.server.pubsub.SubscriberInt;
import it.polimi.ingsw.cg32.view.server.rmi.SubscriberRMI;
import it.polimi.ingsw.cg32.view.server.socket.SubscriberSocket;

/**
* This class models and manages a waiting room.<br>
* It handles all the GameController that have been created.<br>
* its equiped with the {@link Broker} pass as parmam when it's constructed, which
* is pass to all the GameController created to permit them to send response to their clents.<br>
* 
* Evert times a client joins the server the Lobby adds it to the waitingGameController
* using {@link #addPlayer(SubscriberInt)}. Once at least 2 clients await within the same
* GameController a {@link TimerTask} to manages the initialization of the martch 
* is schedule to a Timer.<br>
* The Timer is restart every time a new client join the waitingGameController before the Timer 
* finished.
* Once the Timer expires the waitingGameController is initialize by {@link GameController#initGame()} then
* it's added to the List of running GameController and a new GameController is created to handle new client.<br>
* 
* 
* @author giovanni
*
* @see GameConroller
* @see Timer
*  
*/
public class Lobby {

	private static final Logger LOGGER = Logger.getLogger(Lobby.class.getName());
	private static final int COUNTDOWN = 20;
	private GameController waitingController;
	private final List<GameController> runningGames;
	private final BrokerInt<ResponseMessage, String> broker;

	private final int timeOut;
	private Timer timer;
	private TimerTask timerTask;

	/**
	* Constructor for the Lobby, it initialize the Broker with the one recived as param
	* and creates the first {@link GameController}
	* 
	* @param broker the Broker
	* @param timeout the timeout set to the timer
	* 
	* @see Broker
	* 
	* @throws NullPointerException if Broker is null
	* 
	*/
	public Lobby(BrokerInt<ResponseMessage, String> broker, int timeout) {
		
		if(broker == null)
			throw new NullPointerException("Broker can't be null");
		
		waitingController = new GameController(broker, timeout);
		runningGames = new ArrayList<>();
		this.broker = broker;
		this.timeOut = timeout;
		timer = new Timer();
	} 
 
	/**
	* This is the main method that manages the Lobby, it is used to add a client
	* who join the server to the waiting GameController.
	* When a client join the server several operations occurred:\n
	* - a Token is associated with the client, in order to identify its request.<br>
	* - the Token created and the subscriber of the client are pass to the waiting GameController 
	*   that use {@link GameController#addPlayer(Token, SubscriberInt)} to handle the pub/sub 
	*   (hooking the client to the rights topics).<br>
	* - if there is at least one other client waiting in the same GameContorller
	* 	the {@link Timer} is cancel and a new {@link TimerTask} schedule.<br><br>
	* 
	* 
	* The Timer task take care about lanching a waiting GameController that has enough Player
	* to start. Firstly it sends a CommunicationResponse to all the clients that waitin it,
	* in order to forced the RMI clients that disconneted before the timer finished
	* to leave the waitingGameController (thus avoid a GameController to start with clients that have
	* been already exited from the game).
	* Then the runnable sleeping for 5oo ms to let all the RMI clients to disconnect correctly.
	* At the end the condition of beginning game is checked again, then (if true)
	* the waiting GameController is initialize by {@link GameController#initGame()} and the match starts.<br>
	* Waitnig GameController is stored in the list of running GameController and then
	* a new GameController is created to wait new request of connection.<br>
	*
	* 
	* @param subscriber the {@link SubscriberInt} associated withe the client, it
	* 		 could be any of its concrete implementation ({@link SubscriberRMI} or {@link SubscriberSocket}
	* 
	* @throws NullPointerException is the subscriber recived as param is null
	* 
	* @see Token
	* @see GameController
	* 
	*/
	public synchronized void addPlayer(SubscriberInt<ResponseMessage> subscriber) {

		if(subscriber == null)
			throw new NullPointerException("Subscriber can't be null");
		
		Token token = createToken();
		waitingController.addPlayer(token, subscriber);
		 
		LOGGER.log(Level.INFO, "Player " + token.getUuid() + " added to waiting room.");
		
		if (waitingController.getPlayers().size() >= 2) {
			
			timer.cancel();

			timerTask = new TimerTask() {
				
				@Override
				public synchronized void run() {
					/*
					 * This message forced the RMI client that disconneted before the timer finished
					 * to leave the waitingGameController.
					 */
					broker.publish(new CommunicationResponse(""), waitingController.getTopic());
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					
					if(waitingController.getPlayers().size() < 2) 
						return;
						  
					waitingController.initGame();
					runningGames.add(waitingController);
					waitingController = new GameController(broker, timeOut);
					garbageController();
					LOGGER.log(Level.INFO, "New game created");
				}
			};
 
			timer = new Timer();
			
			timer.schedule(timerTask, (long) COUNTDOWN * 1000);
		}
	}

	/** 
	* Creates new {@link Token} with a unique UUID and a momentary {@link Player}'s id (999), to
	* associates it with the client that join the Lobby.
	* It set the {@link Token} as administrator of the waiting {@link GameController}.
	* 
	* @return the {@link Token} associated with the client
	*/
	private Token createToken() {
		//Il numero del giocatore è 999 per tutti i clients prima che la partita iniz. InitGame si occupa di settare il numero giusto.
		Token token = new Token();
		
		if(waitingController.getPlayers().isEmpty())
			token.setAdministrator(true);
		
		return token;
	}

	/**
	* Get the {@link GameController} awaiting to start
	* 
	* @return waitingController the {@link GameController} awaiting to start
	*/
	public GameController getWaitingController() {
		return waitingController;
	}

	/**
	* Get the {@link Broker} used to send response to clients (using pub/sub patern)
	* 
	* @return broker the {@link Broker} used to send response to client 
	*/
	public BrokerInt<ResponseMessage, String> getBroker() {
		return broker;
	}

	/**
	* Get the List of {@link GameController} that the server is running
	* 
	* @return the List of {@link GameController} that the server is running
	*/
	public List<GameController> getRunningGames() {
		return runningGames;
	}
	
	/**
	* This method is call each time a new GameController is cretaed in #{@link Lobby#addPlayer(SubscriberInt)}.<br>
	* It is used to removed the GameControllers that have been finished their matches from the list of running
	* GameController.
	* 
	*/
	private void garbageController() {

		List<GameController> filteredList = runningGames.stream().filter(g -> !g.isGameStarted()).collect(Collectors.toList());
		runningGames.removeAll(filteredList);
	}
		
		
}
