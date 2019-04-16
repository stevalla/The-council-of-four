package it.polimi.ingsw.cg32.controller.turn;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.bonus.CoinsBonus;
import it.polimi.ingsw.cg32.controller.action.primaryaction.ElectCouncillor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.HireAssistant;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.PerformAnotherPrimaryAction;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.controller.turn.state.PerformAnotherPrimaryState;
import it.polimi.ingsw.cg32.controller.turn.state.PrimaryActionState;
import it.polimi.ingsw.cg32.controller.turn.state.SecondaryActionState;
import it.polimi.ingsw.cg32.controller.turn.state.StartState;
import it.polimi.ingsw.cg32.controller.turn.state.State;
import it.polimi.ingsw.cg32.message.response.ResponseMessage;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.utilities.exception.IllegalStateMachineException;

public class TestActionTurnMachine {

	private static Game game;
	private static ElectCouncillor electCouncillor;
	private ActionTurnMachine turnMachine;
	private static HireAssistant hireAssistant;
	private static CoinsBonus coinsBonus;
	private static PerformAnotherPrimaryAction performAnotherPrimaryAction;
	
	@BeforeClass
	public static void initTest(){
		game = new Game("src/main/resources/map/default.txt", 4);
		
		electCouncillor = new ElectCouncillor(game.getPlayers().get(0), game, game.getBoard().getRegions(RegionType.COAST), new Councillor(Colore.ORANGE));
		hireAssistant = new HireAssistant(game.getPlayers().get(0), game);
		coinsBonus = new CoinsBonus(game.getPlayers().get(0), game, new QuantityBonus("COINSBONUS", 2));
		performAnotherPrimaryAction = new PerformAnotherPrimaryAction(game.getPlayers().get(0), game);
	}
	
	@Before
	public void initTurnMachine() {
		
		turnMachine = new ActionTurnMachine();
		
	}
	
	@Test
	public void CreateANewActionTurnMachineShouldInitializeItCorrectly(){
		
		assertEquals(StartState.getInstance(), turnMachine.getCurrentState());
		
		List<State> executedStateChecker = new ArrayList<>();
		executedStateChecker.add(StartState.getInstance());
		Assert.assertThat(turnMachine.getExecutedStates(), 
			       IsIterableContainingInOrder.contains(executedStateChecker.toArray()));
	}
	
	@Test (expected = NullPointerException.class)
	public void testExecuteActionWithActionNullShoulThrowNullPointerException() throws IllegalStateMachineException{
		turnMachine.executeAction(null);
	}
	
	@Test (expected = NullPointerException.class)
	public void testSetCurrentStateWithNextStateNullShoulThrowNullPointerException() throws IllegalStateMachineException{
		turnMachine.setCurrentState(null);
	}
	
	@Test
	public void testCheckEndTurnShouldReturnTrueIfAPrimaryActionSatetWasAlreadyExecuted() throws IllegalStateMachineException{
		turnMachine.executeAction(electCouncillor);
		Assert.assertTrue(turnMachine.checkEndTurn());
	}
	
	@Test
	public void testCheckEndTurnShouldReturnfalseIfAPrimaryActionSatetWasNotExecuted() throws IllegalStateMachineException{
		Assert.assertFalse(turnMachine.checkEndTurn());
		turnMachine.setCurrentState(BonusState.getInstance());
		turnMachine.setCurrentState(SecondaryActionState.getInstance());
		turnMachine.setCurrentState(PerformAnotherPrimaryState.getInstance());
		turnMachine.setCurrentState(BonusState.getInstance());
		
		Assert.assertFalse(turnMachine.checkEndTurn());
	}
	
	@Test
	public void testSetNextCurrentStateWithASecondaryActionShouldSetCurrentStateToSecondaryActionStateAndDoAddSecondaryStateToTheListOfExecutedStates(){
		turnMachine.setCurrentState(SecondaryActionState.getInstance());
		Assert.assertEquals(turnMachine.getCurrentState(), SecondaryActionState.getInstance());
		
		List<State> executedStatesChecker = new ArrayList<>();
		executedStatesChecker.add(StartState.getInstance());
		executedStatesChecker.add(SecondaryActionState.getInstance());
		Assert.assertThat(turnMachine.getExecutedStates(), 
			       IsIterableContainingInOrder.contains(executedStatesChecker.toArray()));
		
	}
	
	@Test
	public void testSetNextCurrentStateWithAPrimaryActionAndPerformAnotherPrimaryActionCheckerFalseShouldSetCurrentStateToPrimaryActionStateAndAddPrimaryStateToTheListOfExecutedStates(){
		turnMachine.setCurrentState(PrimaryActionState.getInstance());
		Assert.assertEquals(turnMachine.getCurrentState(), PrimaryActionState.getInstance());
		
		List<State> executedStatesChecker = new ArrayList<>();
		executedStatesChecker.add(StartState.getInstance());
		executedStatesChecker.add(PrimaryActionState.getInstance());
		Assert.assertThat(turnMachine.getExecutedStates(), 
			       IsIterableContainingInOrder.contains(executedStatesChecker.toArray()));
	}
	
	@Test
	public void testSetNextCurrentStateWithAPrimaryActionAndPerformAnotherPrimaryActionCheckerTrueShouldSetCurrentStateToSecondaryActionStateAndAddSecondaryStateToTheListOfExecutedStates() throws IllegalStateMachineException{
		turnMachine.executeAction(performAnotherPrimaryAction);
		Assert.assertEquals(turnMachine.getCurrentState(), PerformAnotherPrimaryState.getInstance());
		
		turnMachine.setCurrentState(PrimaryActionState.getInstance());
		
		Assert.assertEquals(turnMachine.getCurrentState(), SecondaryActionState.getInstance());
		
		List<State> executedStatesChecker = new ArrayList<>();
		executedStatesChecker.add(StartState.getInstance());
		executedStatesChecker.add(SecondaryActionState.getInstance());
		Assert.assertThat(turnMachine.getExecutedStates(), 
			       IsIterableContainingInOrder.contains(executedStatesChecker.toArray()));
	}
	
	@Test
	public void testSetNextCurrentStateWithAPerformAnotherPrimaryStateShouldSetCurrentStateToPerformAnotherPrimaryStateAndDoNotChangeExecutedStates(){
	
		turnMachine.setCurrentState(PerformAnotherPrimaryState.getInstance());
		Assert.assertEquals(turnMachine.getCurrentState(), PerformAnotherPrimaryState.getInstance());
		
		List<State> executedStatesChecker = new ArrayList<>();
		executedStatesChecker.add(StartState.getInstance());
		
		Assert.assertThat(turnMachine.getExecutedStates(), 
			       IsIterableContainingInOrder.contains(executedStatesChecker.toArray()));
	}
	
	@Test
	public void testSetNextCurrentStateWithABonusStateShouldSetCurrentStateToBonusStateAndDoNotChangeExecutedStates(){
	
		turnMachine.setCurrentState(BonusState.getInstance());
		Assert.assertEquals(turnMachine.getCurrentState(), BonusState.getInstance());
		
		List<State> executedStatesChecker = new ArrayList<>();
		executedStatesChecker.add(StartState.getInstance());
		
		Assert.assertThat(turnMachine.getExecutedStates(), 
			       IsIterableContainingInOrder.contains(executedStatesChecker.toArray()));
	}
	
	@Test
	public void testExecutePrimaryActionShoulReturnAnActionResponseMessage() throws IllegalStateMachineException{
		assertThat(turnMachine.executeAction(electCouncillor), is(instanceOf(ResponseMessage.class)));
	}
	
	@Test (expected = IllegalStateMachineException.class)
	public void testExecutePrimaryActionShouldThrowsIllegalStateMachineExceptionIfTheIfConditionIsFalse() throws IllegalStateMachineException{
		turnMachine.setCurrentState(BonusState.getInstance());
		turnMachine.executeAction(electCouncillor);
	}
	
	@Test
	public void testExecuteSecondaryActionShoulReturnAnActionResponseMessage() throws IllegalStateMachineException{
		assertThat(turnMachine.executeAction(hireAssistant), is(instanceOf(ResponseMessage.class)));
	}
	
	@Test (expected = IllegalStateMachineException.class)
	public void testExecuteSecondaryActionShouldThrowsIllegalStateMachineExceptionIfTheIfConditionIsFalse() throws IllegalStateMachineException{
		turnMachine.setCurrentState(BonusState.getInstance());
		turnMachine.executeAction(hireAssistant);
	}
	
	@Test
	public void testExecuteBonusActionShoulReturnAnActionResponseMessage() throws IllegalStateMachineException{
		turnMachine.setCurrentState(BonusState.getInstance());
		assertThat(turnMachine.executeAction(coinsBonus), is(instanceOf(ResponseMessage.class)));
	}
	
	@Test (expected = IllegalStateMachineException.class)
	public void testExecuteBonusActionShoulThrowsIllegalStateMachineExceptionIfTheIfConditionIsFalse() throws IllegalStateMachineException{
		turnMachine.executeAction(coinsBonus);
	}
	
	@Test
	public void testGetCurrentStateShouldReturnTheCurrentStateOfTheActionTurnMachine(){
		assertEquals(turnMachine.getCurrentState(), StartState.getInstance());
	}
	
	
	@Test
	public void testGetExecutedStatesShouldReturnACopyOfTheExecutedStates(){
		
		turnMachine.setCurrentState(SecondaryActionState.getInstance());
		turnMachine.setCurrentState(PrimaryActionState.getInstance());
		List<State> executedStatesChecker = new ArrayList<>();
		executedStatesChecker.add(StartState.getInstance());
		executedStatesChecker.add(SecondaryActionState.getInstance());
		executedStatesChecker.add(PrimaryActionState.getInstance());
		Assert.assertThat(turnMachine.getExecutedStates(), 
			       IsIterableContainingInOrder.contains(executedStatesChecker.toArray()));
	}
	
	@Test (expected = UnsupportedOperationException.class)
	public void testGetExecutedStatesShouldReturnAnImmodificableCopyOfTheExecutedStates(){
		turnMachine.getExecutedStates().add(PrimaryActionState.getInstance());
	}
	
}
