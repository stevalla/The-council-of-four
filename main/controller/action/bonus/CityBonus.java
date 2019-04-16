package it.polimi.ingsw.cg32.controller.action.bonus;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.message.request.bonus.CityBonusRqst;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.message.response.action.BonusResponse;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent a City Bonus {@link Action}.
 * It is created from a {@link ActionVisitor} when a
 * {@link CityBonusRqst} is received and validated.<br>
 * A City Bonus permit the player to select one {@link City}
 * where his build an {@link Emporium} and obtain its {@link Bonus}.
 * 
 * @author Stefano
 *
 */
public class CityBonus extends BonusAction {

	private final Game game;
	private final City city;
	private final Bonus bonus;
	
	/**
	 * Construct a City bonus.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param city the selected {@link City}
	 * @param bonus the bonus to execute
	 * 
	 * @see Action 
	 * @see BonusAction
	 */
	public CityBonus(Player player, Game game, City city, Bonus bonus) {
		super(player);
		this.game = game;
		this.city = city;
		this.bonus = bonus;
	}
	
	
	@Override
	public ActionResponseMessage execute() {
		
		return createResponse();
	}


	@Override
	public ActionResponseMessage createResponse() {
		
		String message = "Player#" + player.getId() + " perform a CITYBONUS on " + city.getName() + ".\n"
				+ "The Bonus he won " + city.getBonus() + "\n";
		
		return new BonusResponse(game,  BonusState.getInstance(), city.getBonus(), bonus, message, player.getId());
	}
}
