package it.polimi.ingsw.cg32.model.market;

import java.io.Serializable;

import it.polimi.ingsw.cg32.controller.Market;
import it.polimi.ingsw.cg32.controller.action.market.MarketBuy;
import it.polimi.ingsw.cg32.model.game.Player;

/**
* Abstract class for Boundle.
* <p>
* A Boundle rappresents a group of gaming-object of the same type that a player sells or buys in his market phase.
* It mainly raprresents the kind of object that a player can sell or buy.
* <\p>
* It has an ID (Unique in the market) used to recognize each Boundle and a cost that
* indicate the price of the Boundle.
* 
* It's extended by {@link AssistantBoundle}, {@link PermitCardBoundle} and {@link PoliticCardBoundle},
* which are the 3 different concrete implementation of a Boundle avaiable in the game.
* 
* Infact In the market-sell phase is possible to sell several objects of the same type grouped in a Boundle
* of the correct sub-class.
* 
* Once a player buy a boundle paying the necessary price, he became the owner of all the object contained in
* the Boundle he purchased.
*   
* 
* @author giovanni
*
*/
public abstract class Boundle implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final int boundleId;
	private final int price;
	private static int boundleIdGenerator = 0;
	
	/**
	* Abstarct constructor for a new Boundle.
	* It sets the price of the Boundle to the price recived as param and the BoundleId to the
	* current value of the boundleIdGenerator.
	* Lastly increment the boundleIdGenerator to the next value.
	* 
	* @param price the price of the Boundle
	* 
	* @throws IllegalArgumentException if price is negative
	*/
	public Boundle(int price) {
		
		if(price < 0)
			throw new IllegalArgumentException("Price can't be negative.");
		
		this.price = price;
		this.boundleId = boundleIdGenerator++;
	}
	
	/**
	* Get BoundleId of the Boundle
	* 
	* @return boundleId the Id of the Boundle
	*/
	public int getBoundleId() {
		return boundleId;
	}
	

	/**
	* Get the cost of the Boundle
	* 
	* @return price the price of the Boundle
	*/
	public int getPrice() {
		return price;
	}
	
	
	/**
	* Abstarct declaration of useBoundle.
	* 
	* This method is use in {@link Market#restoreMarket} to return all the objects contained in unsold 
	* Boundles to the player who sold it.
	* 
	* As well as in {@link MarketBuy#execute} to allocate all the object contained in
	* a Boundle to the player who purchased it
	* 
	* @param player the player who become the owner of the objects contained in the Boundle
	* 
	*/
	public abstract void useBoundle(Player player);
	

}