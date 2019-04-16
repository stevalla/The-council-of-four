package it.polimi.ingsw.cg32.controller;

import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.MarketTurnMachine;
import it.polimi.ingsw.cg32.controller.turn.state.BuyState;
import it.polimi.ingsw.cg32.controller.turn.state.OffState;
import it.polimi.ingsw.cg32.message.request.market.MarketBuyRqst;
import it.polimi.ingsw.cg32.message.request.market.MarketRqst;
import it.polimi.ingsw.cg32.message.request.market.MarketSellRqst;
import it.polimi.ingsw.cg32.message.response.CommunicationResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.market.Boundle;
import it.polimi.ingsw.cg32.model.market.ShoppingCart;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
* This class models and manages a Market.<br>
* It is created the firs time by GameController, when this is istanziated.
* It is regenerated each time game reaches market's phase to handle request that
* operates over the store.<br>
* It is equipped with a {@link MarketTurnMachine} to very if the request is executable
* in the State which the Market is currently operating. It has to handle request of buy and sell replacing the 
* GameController as executor of request, using {@link #buy(MarketBuyRqst)} and {@link #sell(MarketSellRqst)}.<br>
* As GameController it has to iterare over the clients to change the current Player
* who is allow to sell stuff in the store using {@link #changeSeller()} and process
* request through request's isValid and {@link State}'s isActionAvlid.<br>
* 
* @author giovanni
*
* @see GameController
*/
public class Market  {

	private final Map<Token, ShoppingCart> store;
	private final Game game;
	private final MarketTurnMachine turnMachine;
	private final ActionVisitor visitor;
	private final int buyTimeOut;
	private final GameController controller;
	private TimerTask buyTask;
	private ConcurrentLinkedQueue<MarketBuyRqst> buffer;
	private boolean stopBuy;
	private Thread buyThread;
	
	 
	/**
	* Constructor of the Market, initialize all the fields and particularly:<br>
	* - set the store held by the {@link Game}<br>
	* - starts the thread to manage {@link MarketBuyRqst} using {@link #manageBuyBuffer()}.<br><br>
	* 
	* A new Market is constructed every time {@link Player}s are allow to send {@link MarketRqst}
	* (market-phase).
	* 
	* 
	* @param gameController the {@link GameController} associated with the Market
	* 
	* @throws NullPointerException if the {@link GameController} pass as param is null
	* 
	*/
	public Market(GameController gameController) {
		
		if(gameController == null)
			throw new NullPointerException("Impossible to instantiate a Market with a null GmeController");
		
		this.controller = gameController; 
		this.game = gameController.getGame();
		this.store = game.getStore();
		this.turnMachine = new MarketTurnMachine();
		this.visitor = gameController.getActionVisitor();
		this.buyTimeOut = 60;
		this.buffer = new ConcurrentLinkedQueue<>();
		this.stopBuy = false;
		manageBuyBuffer();
		
	}
	
	/**
	* this method is used to process {@link MarketSellRqst} and execute them.
	* It also calls {@link #changeSeller()} if the {@link Player} successfully executes his request.
	* 
	* @param request the {@link MarketSellRqst} provided by the client
	*/
	public void sell(MarketSellRqst request){
		try {

			Action action = request.accept(game, controller.getPlayerByToken(request.getToken()), visitor);

			ActionResponseMessage response = turnMachine.executeAction(action);

			controller.getBroker().publish(response, controller.getTopic());

			changeSeller();

		} catch (IllegalActionException | IllegalStateMachineException e) {

			controller.getBroker().publish(new CommunicationResponse(e.getMessage()), 
					controller.getPlayerTopic(request.getToken()));
		} 
	}
	
	
	/**
	* this method is used to process {@link MarketBuyRqst} and execute them.<br>
	* 
	* @param request the {@link MarketBuyRqst} provided by the client
	*/
	private void buy(MarketBuyRqst request){
		try{
			Action action = request.accept(game, controller.getPlayerByToken(request.getToken()), visitor);
			ActionResponseMessage response = turnMachine.executeAction(action);
			controller.getBroker().publish(response, controller.getTopic());
		}
		catch (IllegalActionException | IllegalStateMachineException e){
			controller.getBroker().publish(new CommunicationResponse(e.getMessage()), 
					controller.getPlayerTopic(request.getToken()));
		}
	}
	
	
	/**
	* This method starts a thread to proces {@link MarketBuyRqst} in the sequence these
	* reached the Market. It is implementating using a synchronized buffer. It also provides check
	* over the condition of life of the previous thread started by manageBuyBuffer and kills it if it's still alive.
	* 
	*/
	private void manageBuyBuffer() {
		
		if(buyThread != null)
			buyThread.interrupt();
		
		Runnable bufferHandler = () -> {

				while(!stopBuy) {
					MarketBuyRqst request = buffer.poll();

					if(request != null)
						buy(request);
					else {

						try {
							synchronized (buffer) {
								buffer.wait();
							}
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
				}
		};
		
		buyThread = new Thread(bufferHandler);
		buyThread.start();
			
	}
	
	/**
	* This method is used to starts the timer that has to shoot when the
	* phase of the Market where {@link Player}'s are allow to buy {@link Boundle} is finished.<br>
	* The task it scheduled in the Timer has to close the Market, {@link #restoreMarket()} it and
	* predispose the {@link GameController} to accept request of gaming.<br>
	* 
	*/
	private void buyingTimer() {
		
		buyTask = new TimerTask() {
			@Override
			public void run() {
				stopBuy = true;
				restoreMarket();
				turnMachine.setCurrentState(OffState.getInstance());
				controller.getBroker().publish(new CommunicationResponse("Market finished!"),controller.getTopic());
				controller.playersIterator = controller.getPlayers().iterator();
				controller.changeCurrentPlayer();
				
				if(controller.isGameStarted())
					controller.actionTimer();
			} 
		};

		controller.timer.schedule(buyTask, (long) buyTimeOut*1000);
	}

	
	/**
	* This method is used at the end of the market selling phase as well as if a Player
	* remain the last Player in the game.<br>
	* It gives back to each Player all the game-stuff contained in the unsold
	* boundle that he sold.<br>
	* Then it emptys all the {@link ShopingCart} contained in the store.<br>
	* 
	* @see Player
	* 
	*/
	protected void restoreMarket(){
		for(Map.Entry<Token, ShoppingCart> sc : this.store.entrySet())
			sc.getValue().getListOfBoundles()
				.forEach(b -> b.useBoundle(controller.getPlayerByToken(sc.getKey())));
		
		store.keySet().forEach(t -> store.get(t).getListOfBoundles().clear());
	} 
	 
	/**
	* This method as the same function of {@link GameController#changeCurrentPlayer()}, it is used
	* to scroll all the client playing the game to change the currentPlayer who is
	* avaiable to sell stuff in the Market.<br>
	* it is call each time the currentPlayer suxceed in selling stuff in Market.
	* When the last player sell his stuff in the market {@link #buyingTimer()} is 
	* calls, {@link MarketTurnMachine} change its {@link State} to {@link BuyState},
	* currentPlayer sets to defaultPlayer and the buffer that hold the request clears.<br>
	* 
	*/
	protected void changeSeller(){ 
		 
		if(controller.playersIterator.hasNext()){
			controller.setCurrentPlayer(controller.playersIterator.next());
			
			controller.getBroker().publish(new CommunicationResponse("It's your turn to sell stuff in market, "
					+ "proced:\n"), controller.getPlayerTopic(controller.getCurrentPlayer()));
			
		} 
		else{
			controller.setCurrentPlayer(controller.defaultPlayer);
			turnMachine.setCurrentState(BuyState.getInstance());
			controller.getBroker().publish(new CommunicationResponse("Market Buy started! [60 seconds to buy]\n\n"
					+ "The current state of the Market is:\n" + game.storeToString()), controller.getTopic());
			buffer.clear();
			buyingTimer();
		}
		
	}

	 
	/**
	* This method is used by {@link GameController} to forward the {@link MarketBuyRqst} to the Market
	* 
	* @param request the {@link MarketBuyRqst} provided by the client
	*/
	public void forwardBuyRqstToMarket(MarketBuyRqst request) {
		
		buffer.add(request);
		synchronized (buffer) {
			buffer.notify();
		}

	}

	/**
	* Get the turnMachine used by the Market
	* 
	* @return the turnMachine used by the Market
	*/
	public MarketTurnMachine getTurnMachine() {
		return turnMachine;
	}
	
}
