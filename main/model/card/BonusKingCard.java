package it.polimi.ingsw.cg32.model.card;

import java.io.Serializable;

import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This class rapresent a BonusKingCard, it has a VictoryPoints
 * {@link QuantityBonus} and the method {@link #useCard(Player)} permit add
 * the points of the {@link QuantityBonus} to the player.<br>
 * The only type of bonus that this card accept it's "VICTORYBONUS".
 * Implement {@link Serializable}
 * 
 * @author Stefano
 *
 */
public class BonusKingCard implements Serializable{

	private static final long serialVersionUID = 1L;
	private final QuantityBonus bonus;
	
	
	/**
	 * Construct a BonusKingCard passing a victorybonus
	 * (See QuantityBonus)
	 * 
	 * @param bonus the bonus of the card
	 * @throws NullPointerException if argument is null
	 * @throws IllegalArgumentException if name of bonus is different than "VICTORYBONUS"
	 * 
	 * @see QuantityBonus
	 */
	public BonusKingCard(QuantityBonus bonus){
		if(bonus == null)
			throw new NullPointerException("Argument bonus can't be null");
		
		if(!"VICTORYBONUS".equals(bonus.getName()))
			throw new IllegalArgumentException("This type of card can only accept a victory bonus.");
		
		this.bonus = bonus;
	}
	
	
	/**
	 * Uses the card's bonus and give its victory points
	 * to the player passed as argument.
	 * 
	 * @param player the player that uses this card
	 * @throws NullPointerException if the argument is null
	 * 
	 * @see Player
	 */
	public void useCard(Player player) {
		if(player == null)
			throw new NullPointerException("Player can't be null");
		
		player.getVictoryTrack().setPoints(bonus.getQuantity());
	}

	
	/**
	 * @return {@link QuantityBonus} of this card
	 */
	public QuantityBonus getBonus() {
		return bonus;
	}

	
	@Override
	public String toString() {
		return "[" + bonus + "]";
	}

}
