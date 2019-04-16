package it.polimi.ingsw.cg32.model.region;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.card.BonusKingCard;
import it.polimi.ingsw.cg32.model.card.permitcard.DeckOfPermitCard;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.color.RegionType;


/**
 * This class extends {@link Region} and it has a {@link DeckOfPermitCard}
 * the region's {@link BonusKingCard}, the List of cities and his region type.<br>
 * <p>
 * The construct accept a list of all cities, but save in this region only the city with
 * the same region type.
 * </p>
 * Implement {@link Serializable}
 * 
 * @author Stefano
 * 
 * @see RegionType 
 * @see City 
 *
 */
public class NormalRegion extends Region {
	
	private static final long serialVersionUID = 1L;
	private BonusKingCard bonusRegionCard;
	private DeckOfPermitCard deckOfPermitCard;
	private final List<City> cities;
	private final RegionType regionType;

	
	/**
	 * Construct The Region, don't accept params null, don't accept empty {@link List}.
	 * With a private method the constructor selects from a List of all cities the cities that have 
	 * the same {@link RegionType}.
	 * 
	 * @param region the Region's Region Type
	 * @param cities the {@link List} of all cities of the game
	 * @param permitCards the Region's {@link List} of Permit Card
	 * @throws NullPointerException if params are null
	 * @throws IllegalArgumentException is {@link List} are empty
	 */
	public NormalRegion(RegionType region, List<City> cities, List<PermitCard> permitCards) {
		super();
		if(region == null || cities == null || permitCards == null)
			throw new NullPointerException("Params can't be null");
		if(cities.isEmpty() || permitCards.isEmpty())
			throw new IllegalArgumentException("List can't be empty.");
		
		this.regionType = region;
		this.deckOfPermitCard = new DeckOfPermitCard(permitCards);
		this.cities = selectCity(cities, region);
		this.bonusRegionCard = new BonusKingCard(new QuantityBonus("VICTORYBONUS", (int) (Math.random()*30 +1)));
	}

	
	/**
	 * Get the Bonus Card of the Region but only the first time that
	 * it is called in a game, after that set a null the card. All the others calls
	 * in the game for this method will be return null.
	 * 
	 * @return the region's {@link BonusKingCard}
	 */
	public BonusKingCard getBonusRegionCard() {
		
		if(bonusRegionCard == null)
			return null;
		else {
				BonusKingCard card = this.bonusRegionCard;
				this.bonusRegionCard = null;
				return card;
			}	
	}

	
	/**
	 * @return the Region's {@link DeckOfPermitCard} 
	 */
	public DeckOfPermitCard getDeckOfPermitCard() {
		return deckOfPermitCard;
	}

	
	/**
	 * @return the Region's List of {@link City}
	 */
	public List<City> getCities() {
		return cities;
	}

	
	/**
	 * @return the Region's {@link RegionType}
	 */
	public RegionType getRegionType() {
		return regionType;
	}

	
	/**
	 * Select the cities with the region type like the region passed
	 * as param.
	 * 
	 * @param cities A {@link List} of all {@link City} of the game.
	 * @param region The Region's {@link RegionType}
	 * @return the Region's {@link RegionType}
	 * @throws IllegalAegumentException if there isn't in param cities,
	 * 		   the cities with the same {@link RegionType} that param region 
	 */
	private List<City> selectCity(List<City> cities, RegionType region) {
		List<City> citiesRegion = new ArrayList<>();
		cities.stream().filter(c -> c.getRegion()==region)
					   .forEach(citiesRegion::add);
		
		if(citiesRegion.isEmpty())
			throw new IllegalArgumentException("In region" + region + "there isn't cities. It is impossible.");

		return citiesRegion;
	}

	 
	@Override
	public String toString() {
		List<String> nameOfCity = new ArrayList<>();
		cities.stream().forEach(c -> nameOfCity.add(c.getName()));
		return "Region " + regionType + "\nCity contained in the region:\n" + nameOfCity + 
			   "\nCurrent state of the balcony:\n" + super.toString() + 
			   "\nPermit card of the region:\n" +
			   deckOfPermitCard;
	}

}	
