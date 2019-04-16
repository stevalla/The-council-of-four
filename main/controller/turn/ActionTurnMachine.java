package it.polimi.ingsw.cg32.controller.turn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.PerformAnotherPrimaryAction;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.controller.turn.state.PerformAnotherPrimaryState;
import it.polimi.ingsw.cg32.controller.turn.state.PrimaryActionState;
import it.polimi.ingsw.cg32.controller.turn.state.SecondaryActionState;
import it.polimi.ingsw.cg32.controller.turn.state.StartState;
import it.polimi.ingsw.cg32.controller.turn.state.State;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;

/**
* This class models and manages turn machine used to handle the different phases
* of a Player's turn.<br>
* Each Player recives a new ActionTurnMachine when it stars a new turn. When he
* ends his turn the ActionTurnMachine is destroyed and a new ActionTurnMachine is
* generated to handle the turn of the next Player.<br><br>
* 
* Each Action requested by a Player, pass trhough the ActionTurnMachine in {@link #executeAction(Action)}
* where it'is checked if is possible to execute the target Action in the actual phase of the turn using
* {@link State#isActionValid(Action)} method of the currentState.<br>
* Once an Action is executed, it returns an {@link ActionResponseMessage} that is
* processed by the ActionTurnMachine to get the next State to set.
* 
*  
* @author giovanni
* 
* @see Player
* @see State
* @see Action
*/
public class ActionTurnMachine extends TurnMachine{
	
	private final List<State> executedStates;
	private boolean performAnotherPrimaryActionChecker;

	/**
	* Constructor for ActionTurnMachine.<br>
	* It sets the currentState to {@link StartState} using {@link #setCurrentState(State)}
	* and the boolean performAnotherPrimaryActionChecker to false.
	* 
	*/
	public ActionTurnMachine() {
		this.executedStates = new ArrayList<>();
		setCurrentState(StartState.getInstance());
		this.performAnotherPrimaryActionChecker = false;
	}
	
	
	/**
	* Get an immodificable copy of the List of {@link State}s already executing
	* during the turn of the {@link Player}.
	* 
	* @return an immodificable copy of the List of {@link State}s already executing
	*/
	public List<State> getExecutedStates() {
		return Collections.unmodifiableList(executedStates);
	}
	
	
	/**
	* Check if a player is allowed to end his turn.
	* 
	* @return true if the {@link Player} has already perform a primary action.
	*/
	public boolean checkEndTurn() {
		for(State state : executedStates) {
			if(state.equals(PrimaryActionState.getInstance()))
				return true;
		}
		return false;
	}
	
	
	@Override
	public ActionResponseMessage executeAction(Action action) throws IllegalStateMachineException{
		
		if(action == null)
			throw new NullPointerException("Action can't be null.");
		
		if(currentState.isActionValid(action)) {
			
			ActionResponseMessage response = currentState.executeAction(action);
		
			setCurrentState(response.getState());
			
			return response;
		}
		
		//This statemeant couldn't be reachead because an IllegalStateMachineException is thrown
		//in the if condition, if it's not true.
		return null;
	}
	
	 
	/**
	* This methos is used to set a new State as currentState of the ActionTurnMachine.<br>
	* It checks over the instance of the nextState and adds it to the List of
	* executedStates if it's not a {@link BonusState} or a {@link PerformAnotherPrimaryState}.<br>
	* If nextState is a PerformAnotherPrimaryState it set the boolean performAnotherPrimaryActionChecker to true.<br>
	* This boolean is used to check if the primary action that is executed  chase a 
	* {@link PerformAnotherPrimaryAction} in order to set nextState to {@link SecondaryActionState}
	* instead of {@link PrimaryActionState}.<br>
	* 
	* In all the cases it set currentState to the nextState recived as param.
	* 
	* @param nextState the next State to set
	* @throws NullPointerException if nextState is null
	*/
	@Override
	public void setCurrentState(State nextState) {
		
		if(nextState == null)
			throw new NullPointerException("Next State can't be null.");
		
		if(performAnotherPrimaryActionChecker && nextState instanceof PrimaryActionState){
			setCurrentState(SecondaryActionState.getInstance());
			performAnotherPrimaryActionChecker = false;
			return;
		}
	
		if(!(nextState instanceof BonusState || nextState instanceof PerformAnotherPrimaryState))
			executedStates.add(nextState);
		
		if(nextState instanceof PerformAnotherPrimaryState)
			performAnotherPrimaryActionChecker = true;
			
		this.currentState = nextState;
	}
}
