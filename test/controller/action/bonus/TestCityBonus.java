package it.polimi.ingsw.cg32.controller.action.bonus;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

public class TestCityBonus {

	CityBonus action;
	private City city;
	private Game game;
	private Player player;
	private SingleBonus bonus;
	
	@Before
	public void initParmas() {
		
		game = new Game("src/main/resources/map/default.txt", 3);
		city = game.getBoard().getCities().get(0);
		player = game.getPlayers().get(0);
		bonus = new SingleBonus("CITYBONUS");
	}
	
	@Test
	public void testExecuteShouldReturnAResponseWithTheBonusOfTheCityPassedAsParam() {
		
		game.getBoard().getCities().stream()
					.forEach(city -> {
						action  = new CityBonus(player, game, city, bonus);
						ActionResponseMessage response = action.execute();
						assertTrue(CollectionUtils.isEqualCollection(city.getBonus(), response.getBonus()));
					});
		
	}
	
	@Test
	public void testExecuteCityBonusShouldReturnABonusResponse() {
		action = new CityBonus(player, game, city, bonus);
		ActionResponseMessage response = action.execute();
		assertThat(response, is(instanceOf(BonusResponse.class)));
	}
}
