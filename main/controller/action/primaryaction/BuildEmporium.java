package it.polimi.ingsw.cg32.controller.action.primaryaction;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.turn.state.BonusState;
import it.polimi.ingsw.cg32.controller.turn.state.PrimaryActionState;
import it.polimi.ingsw.cg32.controller.turn.state.State;
import it.polimi.ingsw.cg32.message.response.action.ActionResponse;
import it.polimi.ingsw.cg32.message.response.action.ActionResponseMessage;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.card.BonusKingCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.color.CityColor;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This abstract class offer the possibility to build an emporium in a city.
 * <p>
 * To build an emporium the player must be some stuff:<br>
 * -Pay As more assistants as the number of other players' emporiums in the
 *  selected city<br>
 * -Put one of the player's emporium in the city<br>
 * -Win the bonus of the city<br>
 * -Check if the player has built an emporiums in all the citieis
 *  of the selected region, if yes draw a RegionBonusCard and a 
 *  BonusKingCard if they are not finished<br>
 * -Check if the player has built an emporiums in all the citieis
 *  with the same color of the selected city, if yes draw a ColorBonusCard and a 
 *  BonusKingCard if they are not finished<br>
 * </p>
 *   
 * @author Stefano
 * @see Player 
 * @see City 
 * @see Emporium 
 * @see BonusKingCard
 */
public abstract class BuildEmporium implements Action {

	protected final Player player;
	protected final Game game;
	protected final City city;
	protected final int assistantsToPay;
	protected final List<Bonus> bonusWon;
	private static final int END_GAME_VICTORY_BONUS = 3;
	
	
	/**
	 * 	  
	 * Construct a Build Emporium By King Help.
	 * 
	 * @param player the {@link Player} that perform the action
	 * @param game the {@link Game} with all components
	 * @param city the {@link City} where build the {@link Emporium}
	 * @param assistantsToPay the {@link Assistant} to pay to execute the actions
	 * 		  calculate one for each emporiums of the other players there are in 
	 * 		  the selected city
	 */
	public BuildEmporium(Player player, Game game, City city, int assistantsToPay) {
		this.game = game;
		this.player = player;
		this.city = city;
		this.assistantsToPay = assistantsToPay;
		this.bonusWon = new ArrayList<>();
	}

	
	@Override
	public ActionResponseMessage execute() {
		
		/*
		 * The player pay the assistants for each emporium fo the others
		 * players already build in the selecte city.
		 */
		for(int i=0; i<assistantsToPay; i++)
			game.getBoard().getAssistantsReserve().getAssistants().add(player.getAssistants().poll());
		
		/*
		 * Put one of the player's emporiums in the city
		 */
		city.addEmporium(player.getEmporiums().poll());
		player.getCities().add(city);
		
		/*
		 * Only if the build emporium was the last of the player's emporiums
		 */
		if(player.getEmporiums().isEmpty())
			player.getVictoryTrack().setPoints(END_GAME_VICTORY_BONUS);
		
		/*
		 * Get the bonus of the city near of the selected city where the player
		 * has been built and emporium yet.
		 */
		bonusWon.addAll(findBonusCities());
		
		return null;
	}


	@Override
	public ActionResponseMessage createResponse() {
		StringBuilder sb = new StringBuilder();
		
		/*
		 * Check if the player has built in all the cities of the selected region
		 */
		if(checkBonusRegion()) {
			sb.append("Build an emporium in all the cities of the region " + city.getRegion() + "\n");
			BonusKingCard regionCard = drawRegionCard();
			if(regionCard != null)
				sb.append("Won the region card " + regionCard + "\n");
			BonusKingCard kingCard = drawKingCard();
			if(kingCard != null)
				sb.append("Won the king card " + kingCard + "\n");
		}
		
		/*
		 * Check if the player has built in all the cities with the same color of the
		 * selected cities
		 */
		if(checkBonusColor()) {
			sb.append("Build an emporium in all the cities whit the color " + city.getColor() + "\n");
			BonusKingCard colorCard = drawColorCard();
			if(colorCard != null)
				sb.append("Won the color card " + colorCard + "\n");
			BonusKingCard kingCard = drawKingCard();
			if(kingCard != null)
				sb.append("Won the king card " + kingCard + "\n");
		}
		
		State state;
		
		/*
		 * If the are bonus set the State to action bonus, otherwise
		 * to primary action
		 */
		if(bonusWon.isEmpty())
			state = PrimaryActionState.getInstance();
		else
			state = BonusState.getInstance();

		return new ActionResponse(game, state, bonusWon, sb.toString(), player.getId());
	}
	

	/**
	 * Check if near the selected cities there are cities with an emporium of the player that
	 * execute the action, if there are return the list of the bonus of these cities.
	 * 
	 * @return the List of bonus of the cities near of the city or an empty list if there aren't
	 */
	private List<Bonus> findBonusCities() {
		NeighborIndex<City, DefaultEdge> neighborIndex = new NeighborIndex<>(game.getBoard().getCityGraph());
		List<Bonus> bonus = new ArrayList<>();
		
		List<City> controlledCities = new ArrayList<>();
		controlledCities.add(this.city);
		
		for(int i=0; i<controlledCities.size(); i++) {
			List<City> tempList = neighborIndex.neighborListOf(controlledCities.get(i));
			for(City currentCity : tempList){
				currentCity.getEmporium().stream()
						.filter(e -> e.getPlayerId() == player.getId() && !controlledCities.contains(currentCity))
						.forEach(e -> controlledCities.add(currentCity));
			}
		}
		
		for(City currentCity : controlledCities)
			currentCity.getBonus().stream().forEach(bonus::add);
		
		return bonus;
	}
	
	
	/**
	 * Check if the Player has built an emporium in all the cities of the selected region.
	 * 
	 * @return true if the player has built an emporium in all the cities of the selected region,
	 * 		   false otherwise
	 */
	private boolean checkBonusRegion() {
		int numberOfCityInTargetRegion = (int) game.getBoard().getCities().stream()
									.filter(c -> c.getRegion() == this.city.getRegion())
									.count();
		
		int numberOfCityInTargetRegionWherePlayerBuilt = (int) player.getCities().stream()
									.filter(c -> c.getRegion() == this.city.getRegion())
									.count();
		
		return numberOfCityInTargetRegion == numberOfCityInTargetRegionWherePlayerBuilt;
				
	}
	
	
	/**
	 * Check if the Player has built an emporium in all the cities with the same color
	 * of the selected city.
	 * 
	 * @return if the Player has built an emporium in all the cities with the same color
	 * 		   of the selected city, false otherwise
	 */
	private boolean checkBonusColor() {
		
		/*
		 * Check if the player has built in all the cities with the same colors of
		 * the selected city. 
		 * The King city has not a color
		 */
		if(city.getColor() == CityColor.KING)
			return false;
		
		CityColor color = this.city.getColor();
		int numberOfCityOfTargetColor = (int) game.getBoard().getCities().stream()
									.filter(c -> c.getColor() == color)
									.count();
		
		int numberOfCityOfTargetColorWherePlayerBuilt = (int) player.getCities().stream()
									.filter(c -> c.getColor() == color)
									.count();
		
		return numberOfCityOfTargetColor == numberOfCityOfTargetColorWherePlayerBuilt;
	}
	
	
	/**
	 * Draw the region card, and add it to player's {@link BonusKingCard} if the
	 * card is not null.
	 * 
	 * @return the BonusKingCard or null, if they are finished
	 */
	private BonusKingCard drawRegionCard() {

		BonusKingCard cardToDraw = game.getBoard().getRegions(city.getRegion()).getBonusRegionCard();
		if(cardToDraw != null) 
			player.getKingCards().add(cardToDraw);

		return cardToDraw;
		
	}
	
	
	/**
	 * Draw the king card, and add it to player's {@link BonusKingCard} if the
	 * card is not null.
	 * 
	 * @return the BonusKingCard or null, if they are finished
	 */
	private BonusKingCard drawKingCard() {
		BonusKingCard cardToDraw = game.getBoard().getKingRegion().drawKingCard();
		if(cardToDraw != null) 
			player.getKingCards().add(cardToDraw);
		
		return cardToDraw;
	}
	
	
	/**
	 * Draw the color card, and add it to player {@link BonusKingCard} if the
	 * card is not null.
	 * 
	 * @return the BonusKingCard or null, if they are finished
	 */
	private BonusKingCard drawColorCard() {
		BonusKingCard cardToDraw = game.getBoard().getKingRegion().getCityColorBonusCard(this.city.getColor());
		if(cardToDraw != null) 
			player.getKingCards().add(cardToDraw);
		
		return cardToDraw;
	}
	
}
