package it.polimi.ingsw.cg32.utilities.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * This class rapresent and abstract type Observable, with the method
 * to register the observers and notify them.
 * 
 * @author Stefano
 *
 * @param <C> the object to notify to observers.
 * @see Observer
 */
public abstract class Observable<C> {

	private List<Observer<C>> observers;
	
	/**
	 * Initialize List Of Observer
	 */
	public Observable(){
		this.observers=new ArrayList<>();
	}
	
	/**
	 * Register the observer pass as param
	 * 
	 * @param o the {@link Observer} to register
	 */
	public void registerObserver(Observer<C> o){
		this.observers.add(o);
	}
	
	/**
	 * Unregister the observer pass as param
	 * 
	 * @param o the {@link Observer} to unregister
	 */
	public void unregisterObserver(Observer<C> o){
		this.observers.remove(o);
	}
	
	/**
	 * Notify to all the observer the object pass as param.
	 * This method call the method {@link Observer#update(Object)}
	 * for all the register observer.
	 * 
	 * @param c the object to notify to observers
	 */
	public void notifyObserver(C c){
		this.observers.forEach(o -> o.update(c));	
	}
	
	/**
	 * Notify to all the observer..
	 * This method call the method {@link Observer#update()}
	 * for all the register observer.
	 */
	protected void notifyObserver() {
		this.observers.forEach(o -> o.update());		
	}

	public List<Observer<C>> getObservers() {
		return observers;
	}
	
}
