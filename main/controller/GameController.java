 package it.polimi.ingsw.cg32.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Lists;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.Visitor;
import it.polimi.ingsw.cg32.controller.turn.ActionTurnMachine;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.controller.turn.state.OffState;
import it.polimi.ingsw.cg32.controller.turn.state.PrimaryActionState;
import it.polimi.ingsw.cg32.controller.turn.state.SellState;
import it.polimi.ingsw.cg32.controller.turn.state.State;
import it.polimi.ingsw.cg32.message.request.ChangeMapRqst;
import it.polimi.ingsw.cg32.message.request.EndTurnRqst;
import it.polimi.ingsw.cg32.message.request.ExitRqst;
import it.polimi.ingsw.cg32.message.request.RequestMessage;
import it.polimi.ingsw.cg32.message.request.action.MainActionRqst;
import it.polimi.ingsw.cg32.message.request.bonus.BonusRqst;
import it.polimi.ingsw.cg32.message.request.market.MarketBuyRqst;
import it.polimi.ingsw.cg32.message.request.market.MarketSellRqst;
import it.polimi.ingsw.cg32.message.response.CommunicationResponse;
import it.polimi.ingsw.cg32.message.response.ExitGameResponse;
import it.polimi.ingsw.cg32.message.response.InitializeGameResponse;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.message.response.StartTurnResponse;
import it.polimi.ingsw.cg32.message.response.TokenResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;
import it.polimi.ingsw.cg32.utilities.observer.Observer;
import it.polimi.ingsw.cg32.view.server.Lobby;
import it.polimi.ingsw.cg32.view.server.Token;
import it.polimi.ingsw.cg32.view.server.pubsub.Broker;
import it.polimi.ingsw.cg32.view.server.pubsub.BrokerInt;
import it.polimi.ingsw.cg32.view.server.pubsub.SubscriberInt;
import it.polimi.ingsw.cg32.view.server.rmi.RequestHandlerRMI;
import it.polimi.ingsw.cg32.view.server.socket.RequestHandlerSocket;
 

/**
* This class manages a single game.<br>

* It is equipped with:<br>
* 
* - The unique Broker used by all the GameControllers istantiated, to send response back
*   to the client associated with it.
* 
* - An {@link ActionVisitor} to create {@link Action} based on the {@link RequestMessage} recived.<br>
*
* - A {@link PoliticCardPrototype} to generate new a {@link PoliticCard} each time a Player draw it.
* 
* - A list of {@link Token} that collect the {@link Token}s of the client associated with this GameController
* 
* - An {@link ActionTurnMachine} ,that is reistantiate each time a Player starts his turn, to handle
* 	the differnts {@link State} that could occured during a Player's turn.<br>
* 
* - A list of {@link Bonus} that colects all the {@link Bonus} won by the currentPlayer during his 
* 	turn, this is checked each time a {@link BonusRqst} reached the GameController to check if
* 	the request is valis.
* 
* - A {@link Market} on which it delegate the burden to hold {@link MarketBuyRqst} and {@link MarketSellRqst}.
* 	The GameController strictly cowork with the {@link Market} (providing is initialization each time market-phase starts).<br>
* 	
* - a timer that schedule different task relying on the current state
*   of the game. To be specific it sets tasks that have to expell the currentplayer if he doesn't
*   perform any Action in his turn or sets a task to finish the market-buy phase once this begin.<br>
*   
* It is notified either by {@link RequestHandlerRMI} and {@link RequestHandlerSocket}
* of the clients associated with the game.<br>
* All the request recived are process in the update then forward it (relying on the Player
* that provides the request and its type) to the rigth method, to be executed.<br>
* 
* 
* 
* @author giovanni
* 
* @see Player
* @see Action
*
*/
public class GameController implements Observer<RequestMessage> {
	
	private static final Logger LOGGER = Logger.getLogger(GameController.class.getName());
	private static int gameNumber = 1;
	private final int gameId;
	
	private final BrokerInt<ResponseMessage, String> broker;
	private final ActionVisitor actionVisitor;
	private final PoliticCardPrototype politicCardPrototype;

	private final List<Token> players;
	protected Iterator<Token> playersIterator;
	private Token currentPlayer;
	protected Token defaultPlayer; //Token set as current player before the game starts and when the market is operating in BuyState
	private Token isGameFinished;

	private boolean isGameStarted;
	private final List<Bonus> bonusToExecute;
	private ActionTurnMachine turnMachine;
	private Game game;
	private Market market;
	
	private final Map<String, String> filesName;
	private String map; 

	private final int timeout; 
	protected Timer timer;
	protected TimerTask timerTask;
	
	
	
	/**
	* Constructor for GameController provide initialization for:<br>
	* - {@link Broker} (used to send message to client)<br>
	* - gameId incrementating static number gameNumber<br> 
	* - List of token for clients in the game<br>
	* - List of bonus to use when a client won some bonus<br>
	* - ActionVisitor and politic card prototype to manage the actions game<br>
	* - load all the path in filesName<br>
	* - set default map as current map<br>
	* - set isGameStarted to false<br>
	* - set timeout<br>
	*  
	* The initialization of the remaning fields of GameControler is managed by {@link GameController#initGame()}.<br>
	* 
	* @param broker the {@link Broker} used to send response to client 
	* @param timeout the number of seconds used by a {@link Timer} to extimates when a client has to
	* 	     be removed from the GameControler due to inactivity.
	* 
	*/
	public GameController(BrokerInt<ResponseMessage, String> broker, int timeout) {
		if(broker == null)
			throw new NullPointerException("Broker can't be null");
		
		this.gameId = gameNumber++;
		this.players = new CopyOnWriteArrayList<>();
		this.defaultPlayer = new Token();
		this.currentPlayer = defaultPlayer;
		
		this.broker = broker;
		broker.addTopic(getTopic());
		
		this.filesName = loadMaps();
		this.map = "src/main/resources/map/default.txt";
		this.isGameStarted = false;
		
		this.timeout = timeout;
		this.bonusToExecute = new ArrayList<>();
		this.actionVisitor = new ActionVisitor();
		this.politicCardPrototype = new PoliticCardPrototype();
	}
	  
	/**
	* This method is call by the {@link Lobby} once the GameController is ready to begin the match.<br>
	* Provides initialization for all the fields that wasn't initialize in constructor. It also has to
	* change the temporary player's number of {@link Token} (999) with a correct {@link Player}
    * number.<br>
    * It sends back an {@link InitializeGameResponse} to give to clients the model
    * of the server at the beginning of the game and sets the boolean of game started to true.
    * 
    * Finally it load the first player to allow him to perform his {@link Action} and
    * starts the timer.
	* 
	* @throws IllegalArgumentException if players are less than two.
	*/
	public void initGame() {
		
		//Init game
		this.game = new Game(map, players.size());
		
		/*
		 * Init players in order and set the token's players number, than send
		 * the changed token to the corresponding client
		 */
		this.playersIterator = this.players.iterator();
		int i = 0;
		for(Token token : players) {
			token.setPlayerNumber(i++);
			broker.publish(new TokenResponse(token), getPlayerTopic(token));
		}


		this.timer = new Timer();
		this.market = new Market(this);
		market.getTurnMachine().setCurrentState(OffState.getInstance());
		this.isGameFinished = null;
		
		//Start the game and send the model to clients
		this.isGameStarted = true;
		broker.publish(new InitializeGameResponse(game), getTopic());
		
		//Load the first player and start the timer
		changeCurrentPlayer();
		actionTimer();
	}
	
	/**
	* This method is used to registry a new client that join the game.<br>
	* Hook a new client, that join the game, in the pub/sub structure.<br>
	* Fisrt of all the Token associated with the player is add to the 
	* list of Token of the GameController.
	* Then the subscriber and the token recived as params are put in the pub/sub structure
	* to allow the GameController to comunicate with the new client.<br>
	* Every client is hooked to the topic of the GameController (Broadcast response) and
	* to his personal topic (private response).<br>
	* Finally if the added token is the administrator of the game, send a response to him
	* to inform that he is the administrator and can change map<br>
	* 
	* @param token the Token associated with the client
	* @param subscriber the subscriber that manages the message to forward to the client
	* 
	* @see Broker 
	* @see Token 
	* @see SubscriberInt
	*/
	public void addPlayer(Token token, SubscriberInt<ResponseMessage> subscriber) {
		
		if(token == null || subscriber == null)
			throw new NullPointerException("Arguments can't be null");
 
		if(game == null) {
			players.add(token);
			broker.subscribeClientToTopic(getTopic(), token);
			broker.addTopic(getPlayerTopic(token));
			broker.subscribeClientToTopic(getPlayerTopic(token), token);
			broker.subscribe(subscriber, token);
			broker.publish(new TokenResponse(token), getPlayerTopic(token));
			
			if(token.isAdministrator()) {
				StringBuilder sb = new StringBuilder();
				filesName.keySet().forEach(s -> sb.append(s).append("\n"));
				broker.publish(new CommunicationResponse("Administrator of the game:\n"
						+ "You're allow to change map.\nThe maps are:\n" + sb), getPlayerTopic(token));
			}
		}
		else
			LOGGER.log(Level.WARNING, "Impossible to add players when the game is started");
	} 

	@Override
	public void update(RequestMessage request) {
	
		if(players.contains(request.getToken())) {
		
			if(request instanceof ExitRqst){
				//This response is send back to the client to properly close his connection.
				broker.publish(new ExitGameResponse(), getPlayerTopic(request.getToken()));
			}
			else if(request instanceof ChangeMapRqst)
				changeMap((ChangeMapRqst)request);
			
			else if(isGameStarted) {
				if(isPlayerTurn(request.getToken())){
					//If arrived a request from the currentPlayer the GameController is locked
					if(timerTask.cancel()) {
						synchronized (this) {
							handleRequest(request);
						}
					}
				}
				else if(request instanceof MarketBuyRqst) 
					market.forwardBuyRqstToMarket((MarketBuyRqst) request);
				else
					broker.publish(new CommunicationResponse("You can't perform this action in "
								+ "this phase of the game"),getPlayerTopic(request.getToken()));
			}
		}
		else
			LOGGER.log(Level.WARNING, "Recived a request from an unknown client");
	}	
	
	/**
	* This private method is call every time a client send a request to the GameController.<br>
	* Its task is to sort the {@link RequestMesage} recived by its instance and forward it to 
	* the correct method to handle it.
	* 
	* It also work as a barrier in order to rejects requests coming from clients not
	* registrated with this GameController (analyzing the {@link Token} of the request and check
	* over its presence in {@link #players}).
	* 
	*  
	* @param request the {@link RequestMesage} send by client
	*/
	private void handleRequest(RequestMessage request) { 
		
		if(isPlayerTurn(request.getToken())) {
			if (request instanceof MarketSellRqst) 
				market.sell((MarketSellRqst) request);
			
			else if(request instanceof MainActionRqst) 
				this.executeAction((MainActionRqst) request);
			
			else if(request instanceof EndTurnRqst)
				endTurn();
			
			else {
				//Only use to manage a MarketBuyrqst arrived in a wrong phase of the game  
				broker.publish(new CommunicationResponse("You can't perform this action in this phase of the game"),
						getPlayerTopic(request.getToken()));
			}
		}
		
		if(market.getTurnMachine().getCurrentState() == SellState.getInstance())
			sellingTimer();
		else if(turnMachine.getCurrentState() != OffState.getInstance())
			actionTimer();
	}
	
	/**
	 * Handle a {@link MainActionRqst} from the client, a Message is passed to {@link Visitor} that
     * uses the Visitor pattern to create an appropriate atomic Action, 
     * then the action is passed to a {@link ActionTurnMachine} (state pattern) to be executed.<br>
     * If the request is a {@link BonusRqst} control if the bonus is contained in bonus to execute
     * and after the execution delete the bonus from the list.<br>
     * Check the response controlling if during the action the player
     * won some bonus, if this occour adds it to the list of bonus to execute.
     * Then sends back the response to the client using the broker.
     * 
     * 
	 * @param request the {@link MainActionRqst} from the client
	 */
	private void executeAction(MainActionRqst request) {

		try {
			//Control if the bonus is in the list of bonus to execute if is a bonus request
			if(request instanceof BonusRqst)
				if(!containBonus(((BonusRqst) request).getBonus())) 
					throw new IllegalStateMachineException("You don't win a " + ((BonusRqst) request).getBonus());

			//Control if is a valid request and create the appropriate action using Visitor pattern
			Action action = request.accept(game, getPlayerByToken(request.getToken()), actionVisitor);

			//Execute the action and return the appropriate response to send to clients
			ActionResponseMessage response = turnMachine.executeAction(action);

			//Add the bonus to bonus to execute or add an empty list if the response don't carries bonus
			bonusToExecute.addAll(response.getBonus());
			
			//Remove the executed bonus if is an action bonus
			if(request instanceof BonusRqst)
				removeBonus(((BonusRqst) request).getBonus());

			//Publish the response to all the client
			broker.publish(response, getTopic());

			//Control if the game is finished
			endGame();

		} catch(IllegalActionException | IllegalStateMachineException e) {
			LOGGER.log(Level.INFO, "Invalid request");

			//Publish to the client if the action is an incorrect action
			broker.publish(new CommunicationResponse(e.getMessage()), getPlayerTopic(request.getToken()));

		} finally {

			//Check if the client has terminated his turn
			if(turnMachine.getExecutedStates().size() == 3) 
				endTurn();
		}
	}
	
	/**
	* This method is used to check if the bonus provieded by the player in his rqst
	* is also present in the list of bonus that the player have won during his action (The one at server-side).
	* 
	* @param bonusInExecution the bonus the player want to execute
	* @return true if the bonus is pass as param is contained in the list of bonus to execute
	*/
	private boolean containBonus(Bonus bonusInExecution) {

		if(bonusToExecute.contains(bonusInExecution)) 
			return true;

		return false;
	}
	
	/**
	* This method is used to remove Bonuses from the List of bonusToExecute (containing Bonuses won
	* performing his previous {@link Action}).<br>
	* It removes the bonus pass as param from the list. If, after this operation, the list is empty
	* it set the current{@link State} of the {@link ActionTurnMachine} to {@link PrimaryAction }.
	* 
	* @param bonusToRemove the Bonus that was executed and has tobe removed from bonusToExecute
	* 
	* @see Bonus
	* 
	*/
	private void removeBonus(Bonus bonusToRemove) {
		
		bonusToExecute.remove(bonusToRemove);
		
		if(bonusToExecute.isEmpty())  {
			turnMachine.setCurrentState(PrimaryActionState.getInstance());
		}
	}
		
	/**
	* Private method used to schedule a new TimerTask into the timer.<br>
	* Containes an inner TimerTask class that specify the operations that have to be executed
	* once a {@link Player} leaves the game due to inactivity while he have to execute a main {@link Action}.<br>
	* This task calls {@link #deletePlayer()} to unhook the client from the game and changes the currentPlayer.
	* If the {@link Player} that exited from the game was the penultimate the game is terminated calling
	* {@link #finishGame()}.<br>
	* 
	*/
	protected void actionTimer() {
		
		timerTask = new TimerTask() {
			@Override
			public synchronized void run() {	
					
				deletePlayer();  

				if(players.size() == 1){

					finishGame();

					return;
				}
 
				else
					changeCurrentPlayer();

				if(turnMachine.getCurrentState() == OffState.getInstance())
					sellingTimer();
				else
					actionTimer();
			}
		};

		timer.schedule(timerTask, (long) timeout*1000);
	} 
	
	/**
	* private method used to schedule a new TimerTask into the timer.<br>
	* It has the same function of {@link #actionTimer()} but used when the market is operating.<br>
	* Containes an inner TimerTask class that specify the operations that have to be executed
	* once a {@link Player} leaves the game due to inactivity during his selling-phase.<br>
	* As {@link #actionTimer()} it calls {@link #deletePlayer()} to unhook the client from the game but
	* bnlike the timerTask beheld by that this task has to change the current
	* seller of the market istead of the currentPlayer and eventually restore the market if
	* the player that exited was the penultimate and terminates the game calling {@link #finishGame()}.<br>
	* 
	*/
	private void sellingTimer() { 

		timerTask = new TimerTask() {
			@Override
			public synchronized void run() {	

				deletePlayer(); 

				if(players.size() == 1){

					market.restoreMarket();

					finishGame();

					return;
				}

				market.changeSeller();
				
				if(market.getTurnMachine().getCurrentState() == SellState.getInstance())
					sellingTimer();
				
			}
		};

		timer.schedule(timerTask, (long) timeout*1000);
	}	
	
	/**
	* Private method used to remove a client due to inactivity.<br>
	* It is called when a timerTask is executed, it unhooks the client from 
	* the pub/sub structure, remove his {@link Token} from the list of {@link Token}
	* and cear the list of bonus won during his turn.<br>
	* 
	* It also signals to all the remaning clients that the current {@link Player} has been 
	* expulsed from the match due to inactivity.
	*
	* 
	* 
	* 
	*/
	private void deletePlayer() {
		
		bonusToExecute.clear();
		
		broker.unsubscribeClientToGame(currentPlayer); 
		broker.removeTopic(getPlayerTopic(currentPlayer));
		players.remove(currentPlayer);
		broker.publish(new CommunicationResponse("Player #" + 
				currentPlayer.getPlayerNumber() + " exited from the game"), getTopic());  
	}
			
	/** 
	* This method is used to manage {@link Player}'s turn iterating over the list of Token.<br>
	* It provides check to init a new {@link Market} when all the Player have performed their turns.
	* This is achived using boolean condition over the presence of another Token in the list using
	* {@link Iterator#hasNext()}.<br>
	* When a new Player begin is turn this method has to:<br>
	* - regenerates a new {@link ActionTurnMachine} to held Palyer's request<br>
	* - signal to this Player that he is allowed
	* to perform {@link Action}s using a {@link StartTurnResponse} message, loaded with the
	* {@link PoliticCard} drawed.<br> 
	* 
	* @see Token
	* @see Player
	*
	*/
	protected void changeCurrentPlayer() {
		
		if(this.playersIterator.hasNext()){
			
			currentPlayer = playersIterator.next();
			
			if(currentPlayer.equals(isGameFinished)){
				finishGame();
				timer.cancel();
				return;
			}
				
			this.turnMachine = new ActionTurnMachine();
			PoliticCard cardDrawed = politicCardPrototype.getPoliticCard();
			getPlayerByToken(currentPlayer).getPoliticCards().add(cardDrawed);

			String message = "Is your turn!\nDrawed a " + cardDrawed.getCardColor() + " politic card.\n"
					+ "LE TUE CARTE POLITICHE:\n" + getPlayerByToken(currentPlayer).getPoliticCards() + "\n";

			broker.publish(new StartTurnResponse(game, cardDrawed, message), getPlayerTopic(currentPlayer));
		}		
		else {
			playersIterator = players.iterator();
			turnMachine = new ActionTurnMachine();
			turnMachine.setCurrentState(OffState.getInstance());
			market = new Market(this);
			currentPlayer = playersIterator.next();
			broker.publish(new CommunicationResponse("MARKET SELLING STATE BEGIN:\n"), getTopic());
			broker.publish(new CommunicationResponse("It's your turn to sell stuff in market, "
					+ "proced:\n"), getPlayerTopic(currentPlayer));
		}
	}
	
	/**
	* A private method used to manage Player's turn ending.<b>
	* It is invocated each time a Player performs both a primary action 
	* and a secondary action in his turn or he requested to end his turn
	* by {@link EndTurnRqst}.<br>
	* Once it is invokated it checks if the Player is allowed to end his turn, if this is possible
	* it call {@link #changeCurrentPlayer()} to end Player's turn.
	* 
	* @see Player
	*/
	private void endTurn() {
		
		if(turnMachine.getCurrentState().equals(BonusState.getInstance())) {
			broker.publish(new CommunicationResponse("You have to execute the bonus you won then you're "
					+ "allows to end turn."), getPlayerTopic(currentPlayer));
		}
		
		else if(turnMachine.checkEndTurn()) {
			broker.publish(new CommunicationResponse("\nPlayer #" + getPlayerByToken(currentPlayer).getId() +
					" has terminated is turn\n"), getTopic());
			changeCurrentPlayer();
		}
		else if (turnMachine.getCurrentState() == OffState.getInstance()) {
			broker.publish(new CommunicationResponse("You can't perform an end turn in market sell phase."), 
					getPlayerTopic(currentPlayer));
		}
		else {
			broker.publish(new CommunicationResponse("You have to perfom at least one primary action."), 
					getPlayerTopic(currentPlayer));
		}
	}
	
	/**
	* Used to check if the currentPlayer had built his last {@link Emporium} during his last turn.<br>
	* It's call every times before the current {@link Player} is changed by {@link #changeCurrentPlayer()}.<br>
	* If the condition is true, it sets isGameFinished to the currentPlayer and publish a message to 
	* advise of the clients.<br>
	* This method is executed just one time during a game because once isGameFinished is setted 
	* the if condition will be false for the remaning turn.
	* 
	*/
	private void endGame(){
		if(getPlayerByToken(currentPlayer).getEmporiums().isEmpty() && isGameFinished == null) {
			isGameFinished = currentPlayer;

			String message = "Player #" + getPlayerByToken(currentPlayer).getId() + " had build " + Player.INITIAL_EMPORIUMS + " Emporium.\n" 
					+ "This is your last turn!";

			broker.publish(new CommunicationResponse(message), getTopic());
		}
	}
	
	/**
	* private method used to finish a game.<br>
	* It send a {@link CommunicationResponse} to all the {@link Player}s
	* with the final ranking of the match.
	* Finally it sets isGameStarted to false to reject all the request arrived at GameController
	* after the match finished. 
	* 
	*/
	private void finishGame(){
		
		List<Player> ranking = calculateWinner();
		
		String rankingFormatted = "";
		
		for(Player player : ranking)
			rankingFormatted += "Player#" + player.getId() + " : " + player.getVictoryTrack().getPoints() + "\n";
		
		String message = "Match terminated! FINAL RANKING:\n "
				       + rankingFormatted;
		broker.publish(new CommunicationResponse(message), getTopic());
		
		isGameStarted = false; 
	}

	/**
	* private method used to calculate the winner of the game.<br>
	* It manages the end of the game, adding victory points to Players according to 
	* the game's rules.<br>
	* It used the 3 Comparator provided by the Player to order the List of Player
	* according to different needs.<br>
	* 
	* It return a List of Player that rapresents the final ranking.
	* 
	* @return a List of Player ordered by their vicytory points. 
	* 
	* @see Player
	* 
	*/
	private List<Player> calculateWinner(){
		
		final int MAX_PERMIT_CARD_NUMBER_BONUS = 3;
		final int FIRST_NOBILITY_BONUS = 5;
		final int SECOND_NOBILITY_BONUS = 2;
		List<Player> playerTemp = new ArrayList<>(game.getPlayers());
		List<Player> winner = new ArrayList<>();
		List<Player> seconds = new ArrayList<>();
		
		/*
		 * Use the Bonus card of each player
		 */
		game.getPlayers().stream().forEach(p -> 
								p.getKingCards()
								.stream()
								.forEach(c -> c.useCard(p)));
		
		/*
		 * Get the player/s with more permit cards and add him/them three victory points.
		 */
		playerTemp.stream()
				  .filter(player -> player.getPermitCards().size() == 
				  		Collections.max(game.getPlayers(), Player.compareForPermitCard()).getPermitCards().size())
		          .forEach(player -> player.getVictoryTrack().setPoints(MAX_PERMIT_CARD_NUMBER_BONUS));
		
		/*
		 * Get the players with the markerdisk more advanced in the nobility track.
		 */
		playerTemp.stream()
		 		  .filter(player -> player.getMarkerDisk().getCurrentSpace().compareTo(Collections.max(
		 				  game.getPlayers(), Player.compareForNobilityTrack()).getMarkerDisk().getCurrentSpace()) == 0)
		 		 .forEach(winner::add);
		
		playerTemp.removeAll(winner);
	
		/*
		 * Get the second players with the markerdisk more advanced in the nobility track.
		 */
		playerTemp.stream()
				  .filter(player -> player.getMarkerDisk().getCurrentSpace().compareTo(Collections.max(
						  playerTemp, Player.compareForNobilityTrack()).getMarkerDisk().getCurrentSpace()) == 0)
				  .forEach(seconds::add);
		
		/*
		 * Add to the first player/s in nobility track 5 victory points.
		 */
		winner.stream().forEach(p -> p.getVictoryTrack().setPoints(FIRST_NOBILITY_BONUS));
		
		/*
		 * If two players are tied do nothing, otherwise add three victory points to the second player/s 
		 * in nobility track.
		 */
		if(winner.size() == 1)
			seconds.stream().forEach(p -> p.getVictoryTrack().setPoints(SECOND_NOBILITY_BONUS));
		
		/*
		 * Calculate The player with more victory points.
		 */
		winner.clear();
		playerTemp.clear();
		playerTemp.addAll(game.getPlayers());
		game.getPlayers().stream()
						 .filter(player -> player.getVictoryTrack().getPoints() == 
						 		Collections.max(game.getPlayers(), Player.compareForVictoryTrack()).getVictoryTrack().getPoints())
						 .forEach(winner::add);
		
		/*
		 * Return the only winner if the winner is only one
		 */
		if (winner.size() == 1){
			Collections.sort(playerTemp, Player.compareForVictoryTrack());
			return Collections.unmodifiableList(playerTemp);
		}
		
		playerTemp.removeAll(winner);
		
		/*
		 * If more players have the same victory points compare their number of
		 * assistant and politic card to calculate the winner
		 */
		Collections.sort(playerTemp, Player.compareForVictoryTrack());
		Collections.sort(winner, Player.compareForAssistantPoliticCard());
		
		playerTemp.addAll(0, winner);
		
		//Return the final ranking
		return Lists.reverse(playerTemp);
	}

	
	/**
	* This method is used to load all the path of the avaiable maps in a {@link Map} of 
	* String-tags (the names of the maps) and path of the relative file.<br>
	* This map is used to manage the change of maps before that the game starts.
	* 
	* @return files a Map of String-tags (the names of the maps) and path of file
	*/
	private Map<String, String> loadMaps() {
		Map<String, String> files = new HashMap<>();
		
		File folder = new File("src/main/resources/map");
		
		for(File file : folder.listFiles()) 
			files.put(file.getName().substring(0, file.getName().indexOf(".")), file.getPath());
		
		return files;
	}
	         
	/**
	* This method is used to change the current map associated with the GameController.<br>
	* Before the game starts the first client connected to the game is the administrator and
	* only he has the possibility to change the map.<br>
	* The path of the map are stored in a Map of String-tags and file's path.
	* Using {@link GameController#findMap(String)} is possible to change the current map.<br>
	* 
	* @param request the {@link ChangeMapRqst} send by client
	*/
	private void changeMap(ChangeMapRqst request){
		
		if(isGameStarted){
			broker.publish(new CommunicationResponse("Impossible to change map once the game is started"),
					getPlayerTopic(request.getToken()));
			return;
		} 
		
		else if(!request.getToken().isAdministrator()){
			broker.publish(new CommunicationResponse("You're not allowed to change map because not the administrator."),
					getPlayerTopic(request.getToken()));
			return;
		}
		
		else {
			try {
				map = findMap(request.getChooseMap());
				String message = "Map change to " + request.getChooseMap();
				broker.publish(new CommunicationResponse(message), getTopic());
				
			}  catch(IllegalActionException e) {
				
				LOGGER.log(Level.INFO, e.getMessage());
				broker.publish(new CommunicationResponse(e.getMessage()), getPlayerTopic(request.getToken()));
			}
		}
	}
	 
	/**
	* Find the path file for the choosen map
	*
	* @return the path for the choosen map file
	* @throws IllegalActionException if the file name doesn't exists.
    */
	private String findMap(String map) throws IllegalActionException {
		
		if(!filesName.keySet().contains(map))
			throw new IllegalActionException("Selected map doesn't exist");
		
		return this.filesName.get(map);
	}
	
	/**
	* Get the {@link ActionVisitor} of the GameController
	* 
	* @return actionVisitor the {@link ActionVisitor} of the GameController
	*/
	public ActionVisitor getActionVisitor() {
		return actionVisitor;
	}
	
	/**
	* Get the current running state of the GameCotroller.
	* 
	* @return isGameStarted boolean condition that indicate if the GameCotroller is running
	*/
	public boolean isGameStarted() {
		return isGameStarted;
	}
	
	/**
	* Get the broker, used from {@link Market} to publish message to client.
	* 
	* @return broker the Broker 
	*/
	protected BrokerInt<ResponseMessage, String> getBroker() {
		return broker;
	}
	
	/**
	* Get the topic associated with the target token, for publisher-subscriber communication.
	*
	* @param token the token of the player to get the topic.
	* @return the topic associated with the target token.
	* @throws NullPointerException if the token is null
    */
	public String getPlayerTopic(Token token) {
		
		if(token == null)
			throw new NullPointerException("Token can't be null");
		
		return  getTopic() + "PLAYER" + token.getUuid();
	}
	
	/**
	* Get the topic of this game for publisher-subscriber communication.
	*
	* @return the main topic of this game
    */
	public String getTopic() {

        return "GAME" + Integer.toString(gameId);
    }
	  
	/**
	* Get the game associated with this GameController. 
	*
	* @return the game associated with this GameController.
    */
	public Game getGame() {
		return game;
	}
	 
	/**
	 * Get the bonus to execute if there are any, or return an empty list.
	 * 
	 * @return the bonusToExecute of the current player or an empty list if there aren't any
	 */
	public List<Bonus> getBonusToExecute() {
		return bonusToExecute;
	}

	/**
	 * @return the market
	 */
	public Market getMarket() {
		return market;
	}
	
	/**
	 * Get The list of {@link Token} of the clents that are currently playing the game.
	 * 
	 * @return the list of token of the clients that are playing the game
	 */
	public List<Token> getPlayers() {
		return players;
	}
	
	/**
	* Get currentPlayer's token.
	* 
	* @return currentPlayer the token of- he player that is playing
	*/
	public Token getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
	* protected method to set currentPlayer without using {@link #changeCurrentPlayer()},
	* it is call by market to change the current seller.<br>
	* 
	* @param currentPlayer the currentPlayer to set
	* @throws NullPointerException if player is null
	*/
	protected void setCurrentPlayer(Token currentPlayer) {
		
		if(currentPlayer == null)
			throw new NullPointerException("The player can't be null");
		
		this.currentPlayer = currentPlayer;
	}

	/**
	 * Get the player associated with the token
	 * 
	 * @param token the token associated with the player to return
	 * @return the player associated with the token
	 * @throws IllegalArgumentException if token is null or toke is invalid
	 */
	protected Player getPlayerByToken(Token token) {
		
		if(token == null || token.getPlayerNumber() >= game.getPlayers().size())
			throw new IllegalArgumentException("Invalid token");
		
		return game.getPlayers().get(token.getPlayerNumber());
	}
	
	/**
	 * Check if it is the turn of the player with the given token
	 *
	 * @param token the token of the player to check
	 * @return true, if it is his turn
	 */
	private boolean isPlayerTurn(Token token) {

		return token.equals(currentPlayer);
	}
	
	/**
	* Get the current map of the Game
	* 
	* @return map the current map of the Game
	*/
	public String getCurrentMap() {
		
		return map;
	}

}