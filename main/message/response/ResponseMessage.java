package it.polimi.ingsw.cg32.message.response;

import java.io.Serializable;

import it.polimi.ingsw.cg32.view.client.cli.ResponseVisitor;

/**
* This is the abstract class that all the classes that model responses
* that the server send to clients must extends.<br>
* It provides an abstract method {@link ResponseMessage#display(ResponseVisitor)},<br>
* Every class of response need to override it to be conformed to Visitor pattern
* and passed it to the concrete implementaion of {@link VisitorResponse} to manage the response.
* 
* Implement {@link Serializable}.<br>
* 
* @author giovanni
*
*/
public abstract class ResponseMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	*  This method is ovveride by each concreate implementation of ResponseMessage to costumize
	*  the way a response is handled.
	*  
	*  @param visitor the concrete implementation of {@link ResponseVisitor} to manage the response
	*  
	*  @throws NullPointerException if argument visitor is null
	*/
	public void display(ResponseVisitor visitor){
		
		if(visitor == null)
			throw new NullPointerException("Visitor can't be null");
		
	}

}
