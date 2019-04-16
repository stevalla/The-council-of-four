package it.polimi.ingsw.cg32.message.request.bonus;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.bonus.CityBonus;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestCityBonusRqst {

	CityBonusRqst request;
	private Game game;
	private Player player;
	private ActionVisitor visitor;
	private City city;
	private Action action;
	
	@Test(expected = NullPointerException.class)
	public void testConsturctorWithTokenNullValuesShouldThrowANullPointerException() {
		
		request = new CityBonusRqst(null, null, new SingleBonus("CITYBONUS"));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConsturctorWithBonusNullValuesShouldThrowANullPointerException() {
		
		request = new CityBonusRqst(new Token(), null, null);
	}
	
	@Test
	public void testConstructorPassedANullValueForTheCityShouldReturnACityBonusRqstCorrectly() {
		
		request = new CityBonusRqst(new Token(), null, new QuantityBonus("CITYBONUS", 3));
		assertNull(request.getCity());
	}
	
	@Before
	public void initializeRequest() {
		
		game = new Game("src/main/resources/map/default.txt", 4);
		player = game.getPlayers().get(0);
		visitor = new ActionVisitor();
		city = game.getBoard().getCities().get(0);
		request = new CityBonusRqst(new Token(), city, new SingleBonus("CITYBONUS"));
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheBonusIsAQuantityBonusShouldThrowAnIllegalActionException() throws IllegalActionException {

		request = new CityBonusRqst(new Token(), city, new QuantityBonus("CITYBONUS", 2));
		request.accept(game, player, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenArgumentsAreNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(null, null, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerAndBoardAreNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(null, null, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		assertNull(request.accept(null, player, visitor));
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenBoardIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		assertNull(request.accept(game, null, visitor));
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheBonusNameIsNotCityBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new CityBonusRqst(new Token(), city, new SingleBonus("VICTORYBONUS"));
		request.accept(game, player, visitor);
	}
	
	@Test
	public void testAcceptShouldReturnACityBonusActionNotNull() throws IllegalActionException {
		city.addEmporium(new Emporium(1));
		player.getCities().add(city);
		action = request.accept(game, player, visitor);
		
		assertThat(action, is(instanceOf(CityBonus.class)));
		assertNotNull(action);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenCityAnIncorrectNameShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		city = new City("ciao", RegionType.COAST, CityColor.BRONZE, new ArrayList<>());
		request = new CityBonusRqst(new Token(), city, new SingleBonus("CITYBONUS"));
		
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenCityAnIncorrectListOfBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		city = new City("ARKON", RegionType.COAST, CityColor.BRONZE, new ArrayList<>());
		
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenCityAnIncorrectRegionShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		city = new City("JUVELAR", RegionType.COAST, CityColor.BRONZE, new ArrayList<>());
		
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenCityAnIncorrectColorShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		city = new City("JUVELAR", RegionType.HILL, CityColor.BRONZE, new ArrayList<>());
		
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testCreateCityWhenTheSelectedCityNotHaveAnEmporiumOfThePlayerShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testCreateCityWhenTheSelectedCityHasANobilityBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		for(City city : game.getBoard().getCities())
			city.getBonus().stream()
						   .filter(b -> b.getName().equals("NOBILITYBONUS"))
						   .forEach(b -> this.city = city);
		
		city.addEmporium(new Emporium(1));
		player.getCities().add(city);
		
		request = new CityBonusRqst(new Token(), city, new SingleBonus("CITYBONUS"));
		request.accept(game, player, visitor);
	}
}
