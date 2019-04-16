package it.polimi.ingsw.cg32.message.request.bonus;

import java.io.Serializable;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.bonus.CityBonus;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

/**
 * This class extends {@link BonusRqst} and create a request
 * to execute an {@link CityBonus} action controlling its validation.<br>
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public class CityBonusRqst extends BonusRqst {

	private static final long serialVersionUID = 1L;
	private City targetCity;
	
	/**
	 * Construct a CityBonusRqst using the super constructor.
	 * 
	 * @param token the {@link Token} associated with the {@link Player} that send the request
	 * @param city the selected {@link City} for the request
	 * @param bonus the {@link Bonus} to execute
	 * @throws NullPointerException if token and/or bonus are null
	 */
	public CityBonusRqst(Token token, City city, Bonus bonus) {
		super(token, bonus);
		this.targetCity = city;
	}

	
	@Override
	public Action accept(Game game, Player player, ActionVisitor visitor) throws IllegalActionException {
		
		super.accept(game, player, visitor);
		
		return isValid(game, player) ? visitor.visit(this, player, game) : null;
	}

	
	@Override
	protected boolean isValid(Game game, Player player) throws IllegalActionException {
		
		super.isValid(game, player);
		
		/*
		 * Control if the bonus is a SingleBonus and the name must be equals to CITYBONUS
		 */
		if(!("CITYBONUS".equals(getBonus().getName()) && getBonus() instanceof SingleBonus)) 
			throw new IllegalActionException("ILLEGAL ACTION!");
		
		/*
		 * Control if the recived city is really part of the game and
		 * change it with the corresponding server attribute
		 */
		targetCity = game.getBoard().getCities().stream()
							.filter(city -> city.equals(targetCity))
							.findFirst()
							.orElseThrow(() -> new IllegalActionException("The selected city doesn't exist."));

		if(!player.getCities().contains(targetCity)) 
			throw new IllegalActionException("You have selected a city where you haven't already place an emporium!");

		for(Bonus bonus : targetCity.getBonus())
			if("NOBILITYBONUS".equals(bonus.getName())) 
				throw new IllegalActionException("You can't select a city with a Nobility Bonus.");

		return true;
	}

	
	/**
	 * @return the selected city for the request
	 */
	public City getCity() {
		return targetCity;
	}
}
