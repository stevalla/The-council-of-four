package it.polimi.ingsw.cg32.message.request.action;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByPermitCard;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestBuildEmporiumByPermitCardRqst {

	BuildEmporiumByPermitCardRqst request;
	private City city;
	private String permitCardId;
	private Game game;
	private Player player;
	private Token token;
	private ActionVisitor visitor;
	
	@Before
	public void initParams() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		token = new Token();
		player = game.getPlayers().get(0);
		player.getPermitCards().add(game.getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getDeck().poll());
		permitCardId = player.getPermitCards().get(0).getId();
		city = player.getPermitCards().get(0).getCity().get(0);
		visitor = new ActionVisitor();
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenTokenIsNullShouldReturnANullPointerException() {
		
		new BuildEmporiumByPermitCardRqst(null, city, permitCardId);
	}
	
	@Test
	public void testConstructWhenpermitCardIdIsNullShouldGoCorrectlyAndInitializeToNullTheValues() throws IllegalActionException {
		
		request = new BuildEmporiumByPermitCardRqst(token, null, null);
		assertNull(request.getCity());
		assertNull(request.getSelectedPermitCard());
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(game, null, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenPlayerHasNoEmporiumShouldThrowAnIllegalActionException() throws IllegalActionException {
		player.getEmporiums().clear();
		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		request.accept(game, player, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenBoardIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(null, player, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenVisitorIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(game, player, null);
	}
	
	@Test
	public void testAcceptShouldCreateBuildEmporiumByPermitCard() throws IllegalActionException {

		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		Action action = request.accept(game, player, visitor);

		assertThat(action, is(instanceOf(BuildEmporiumByPermitCard.class)));
		assertNotNull(action);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenThePermitCardIsNotInPlayersPermitCardsShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		player.getPermitCards().clear();
		
		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheCityIsNullShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new BuildEmporiumByPermitCardRqst(token, null, permitCardId);
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenThePermitCardIdIsNullShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new BuildEmporiumByPermitCardRqst(token, city, null);
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheSelectedPermitCardIsUsedShouldThrowAnIllegalActionException() throws IllegalActionException {
		player.getPermitCards().get(0).useCard();
		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheCityIsAlreadyInPlayersCityShouldThrowAnIllegalActionException() throws IllegalActionException {
		player.getCities().add(city);
		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		request.accept(game, player, visitor);
	}
	
	@Test
	public void testAcceptWhenPlayerHasBeenBuildEmporiumsButNotInTheSelectedCityShouldGoWell() throws IllegalActionException {
		City cityToAdd = game.getBoard().getCities().stream().filter(c -> !c.getName().equals(city.getName())).findFirst().orElse(null);
		player.getCities().add(cityToAdd);
		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		request.accept(game, player, visitor);
		return;
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheCityIsNotInTheSelectedPermitCardShouldThrowAnIllegalActionException() throws IllegalActionException {
		int i=1;
		while(player.getPermitCards().get(0).getCity().contains(city))
			city = game.getBoard().getCities().get(i++);
		
		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenThePlayerNotHaveEnoughAssistantShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		player.getAssistants().clear();
		city.getEmporium().add(new Emporium(2));
		
		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		request.accept(game, player, visitor);
	}
	
	@Test
	public void testAcceptWhenInTheSelectedCityThereIsEmporiumsOfOthersPlayerMustSetAssistantsToPayToOneAssistantForEachEmporium() throws IllegalActionException {
		int n=5;
		for(int i=0; i<n; i++) {
			city.getEmporium().add(new Emporium(4));
			player.getAssistants().add(new Assistant());
		}
		
		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		request.accept(game, player, visitor);
		
		assertTrue(request.getAssistantsToPay() == n);
	}
	
	@Test
	public void testGetAssistantToPayBeforeAcceptShouldReturnZero() {
		
		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		assertTrue(request.getAssistantsToPay() == 0);
	}
	
	@Test
	public void testGetPoliticCardsBeforeAcceptShouldReturnAnEMptyList() {
		
		request = new BuildEmporiumByPermitCardRqst(token, city, permitCardId);
		assertNull(request.getSelectedPermitCard());
	}
}
