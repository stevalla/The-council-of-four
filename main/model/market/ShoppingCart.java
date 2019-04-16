package it.polimi.ingsw.cg32.model.market;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;

/**
* This class models and manage a shopping cart. It contains the list of Boundles sold by a player in his market phase.
* It' provides method to find  a Boundle using {@link #findBoundleById(int)} or
* removes a Boundle using {@link #removeBoundleById(int)} either passing target ID.
* 
* @author giovanni
* @see Boundle
*
*/
public class ShoppingCart implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private final List<Boundle> listOfBoundles = new ArrayList<>();
	
	/**
	* Constructor for ShoppingCart. It collects all the Boundle contained in
	* the 3 list of Boundles recived as params, in the listOfBoundles.
	* 
	* 
	* @param politicCards the list of Boundle of {@link PoliticCard}
	* @param assistants the list of Boundle of {@link Assistant}
	* @param permitCards the list of Boundle of {@link PermitCard}
	* 
	* @throws NullPointerException if one or more params are null
	*/
	public ShoppingCart(List<PoliticCardBoundle> politicCards, 
			List<AssistantBoundle> assistants, List<PermitCardBoundle> permitCards) {
		
		if(politicCards == null || assistants == null || permitCards == null)
			throw new NullPointerException("list of Boundles can't be null");
		
		listOfBoundles.addAll(politicCards);
		listOfBoundles.addAll(permitCards);
		listOfBoundles.addAll(assistants);
	}
	
	/**
	* This method is used to check the presence of a Boundle in the shoppingCart.
	* Scroll the all list of Boundle and return a copy of the target Boundle if it's contained, null otherwise.
	* 
	* @param boundleId the number of the Boundle to find
	* @return a copy of the target Boundle or null if the Boundle is not contained in the ShoppingCart.
	*/
	public Boundle findBoundleById(int boundleId){

		return listOfBoundles.stream()
					.filter(boundle -> boundle.getBoundleId() == boundleId)
					.findFirst()
					.orElse(null);
	}		

	/**
	* This method is used to remove a Boundle in the shoppingCart.
	* Scroll the all list of Boundle and removes the target Boundle from it if it's contained, null otherwise.
	* 
	* @param boundleId the number of the Boundle to find
	* @return a the target Boundle or null if the Boundle is not contained in the ShoppingCart.
	*/
	public Boundle removeBoundleById(int boundleId){
		
		for(int i = 0; i < listOfBoundles.size(); i++)
			if(listOfBoundles.get(i).getBoundleId() == boundleId)
				return listOfBoundles.remove(i);
		
		return null;
	}

	/**
	* Get the the list of Boundle of the ShoppingCart
	* 
	* @return a copy of the list of Boundle
	*/
	public List<Boundle> getListOfBoundles() {
		return listOfBoundles;
	}

	@Override
	public String toString() {
		String stringToReturn = "";
		
		for(Boundle boundle: listOfBoundles)
			stringToReturn += boundle + "\n";
		 
		return stringToReturn;
	}
	
	
	
	
}
