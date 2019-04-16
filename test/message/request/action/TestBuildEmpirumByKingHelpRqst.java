package it.polimi.ingsw.cg32.message.request.action;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByKingHelp;
import it.polimi.ingsw.cg32.model.assistants.Assistant;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.city.Emporium;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestBuildEmpirumByKingHelpRqst {

	BuildEmporiumByKingHelpRqst request;
	private Token token;
	private Game game;
	private Player player;
	private List<Colore> colors;
	private ActionVisitor visitor;
	private PoliticCardPrototype prototype;
	private List<PoliticCard> politicCards;
	private City city;
	
	@Before
	public void initParams() throws IllegalActionException {
		game = new Game("src/main/resources/map/default.txt", 3);
		token = new Token();
		player = game.getPlayers().get(0);
		visitor = new ActionVisitor();
		colors = new ArrayList<>();
		prototype = new PoliticCardPrototype();
		city = game.getBoard().getCities().get(0);
		player.getPoliticCards().clear();
		
		for(int j=0; j<4; j++) {
			List<Colore> allColors = new ArrayList<>(Arrays.asList(Colore.values()));
			List<PoliticCard> cardsToAdd = new ArrayList<>();
			while(cardsToAdd.size() != 7) {
				PoliticCard card = prototype.getPoliticCard();
				for(int i=0; i<allColors.size(); i++)
					if(card.getCardColor() == allColors.get(i)) {
						cardsToAdd.add(card);
						allColors.remove(i);
					}
			}
			player.getPoliticCards().addAll(cardsToAdd);
		}
		
		for(Councillor councillor : game.getBoard().getKingRegion().getBalcony().getSeats())
			colors.add(councillor.getColor());
		
		request = new BuildEmporiumByKingHelpRqst(token, city, colors);
		politicCards = request.getPoliticCardByColors(colors, player);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenTokenIsNullShouldThrowANullPointerExcetpion() {
		
		 new BuildEmporiumByKingHelpRqst(null, city, colors);
	}
	
	@Test
	public void testConstructWhenCityIsNullShouldReturnANullCity() {
		
		request = new BuildEmporiumByKingHelpRqst(token, null, colors);
		assertNull(request.getCity());
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenColorsIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		 new BuildEmporiumByKingHelpRqst(token, city, null);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenPlayerHasNoEmporiumShouldThrowAnIllegalActionException() throws IllegalActionException {
		player.getEmporiums().clear();
		request.accept(game, player, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(game, null, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenBoardIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(null, player, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenVisitorIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(game, player, null);
	}
	
	@Test
	public void testAcceptShouldCreateBuildEmporiumByKingHelp() throws IllegalActionException {

		Action action = request.accept(game, player, visitor);
		
		assertThat(action, is(instanceOf(BuildEmporiumByKingHelp.class)));
		assertNotNull(action);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenPlayerHasNotThePoliticCardsWithTheSelectedColorsShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		Player player = this.player;
		player.getPoliticCards().clear();

		request.accept(game, player, visitor);
	}
	
	@Test 
	public void testGetPoliticCardByColorShouldReturnAListOfPlayerPoliticCardOfTheColorsPassedAsArgument() throws IllegalActionException {
		
		List<PoliticCard> politicCards = request.getPoliticCardByColors(colors, player);
		List<Colore> colorsOfCards = new ArrayList<>();
		politicCards.stream().forEach(p -> colorsOfCards.add(p.getCardColor()));
		
		Assert.assertThat(colors, IsIterableContainingInOrder.contains(colorsOfCards.toArray()));
	}
	
	@Test
	public void testGetPoliticCardByColorShouldReturnASubCollectionOfPlayerPoliticCardFilterByColorPassedAsParam() throws IllegalActionException {
		
		List<PoliticCard> politicCards = request.getPoliticCardByColors(colors, player);
		
		assertTrue(CollectionUtils.isSubCollection(politicCards, player.getPoliticCards()));
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetPolitcCardByColorWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.getPoliticCardByColors(colors, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testGetPolitcCardByColorWhenColorsIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.getPoliticCardByColors(null, player);
	}
	
	@Test
	public void testGetPolitcCardByColorWhenColorsIsEmptyShouldReturnAnEmptyList() throws IllegalActionException {
		
		assertTrue(request.getPoliticCardByColors(new ArrayList<>(), player).isEmpty());
	}
	
	@Test(expected = IllegalActionException.class)
	public void testCountMoneyToSatisfyCouncillorWhenListOfPoliticCardsIsEmptyShouldThrowAnIllegalActionException() throws IllegalActionException {
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.clear();
		request.countMoneyToSatisfyCouncillor(cards, player, game.getBoard().getKingRegion());
	}
	
	@Test(expected = NullPointerException.class)
	public void testCountMoneyToSatisfyCouncillorrWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.countMoneyToSatisfyCouncillor(politicCards, null, game.getBoard().getKingRegion());
	}
	
	@Test(expected = NullPointerException.class)
	public void testCountMoneyToSatisfyCouncillorrWhenPoliticCardsIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.countMoneyToSatisfyCouncillor(null, player, game.getBoard().getKingRegion());
	}
	
	@Test(expected = NullPointerException.class)
	public void testCountMoneyToSatisfyCouncillorrWhenRegionIsNullShouldThrowANullPointerException() throws IllegalActionException {

		request.countMoneyToSatisfyCouncillor(politicCards, player, null);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testCountMoneyToSatisfyCouncillorWhenOnePoliticCardsNotSatisfyTheCouncillorShouldThrowAnIllegalActionException() throws IllegalActionException {
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.remove(0);
		
		PoliticCard card = prototype.getPoliticCard();
		List<Colore> colors = new ArrayList<>();
		game.getBoard().getKingRegion().getBalcony().getSeats().stream().forEach(c -> colors.add(c.getColor()));

		while(colors.contains(card.getCardColor()) || card.getCardColor() == Colore.JOLLY)
			card = prototype.getPoliticCard();
		
		cards.add(card);
		request.countMoneyToSatisfyCouncillor(cards, player, game.getBoard().getKingRegion());
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorWhenPoliticCardsAreFourAndThereAreNotJollyShouldReturnZero() throws IllegalActionException {

		assertTrue(request.countMoneyToSatisfyCouncillor(politicCards, player, game.getBoard().getKingRegion()) == 0);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorWhenPoliticCardsAreThreeAndThereAreNotJollyShouldReturnFour() throws IllegalActionException {
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.remove(0);
		
		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, game.getBoard().getKingRegion()) == 4);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorWhenPoliticCardsAreTwoAndThereAreNotJollyShouldReturnSeven() throws IllegalActionException {
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.remove(0);
		cards.remove(0);

		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, game.getBoard().getKingRegion()) == 7);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorWhenPoliticCardsAreOneAndThereAreNotJollyShouldReturnTen() throws IllegalActionException {
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.remove(0);
		cards.remove(0);
		cards.remove(0);
		
		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, game.getBoard().getKingRegion()) == 10);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorWithFourPoliticCardsIncludingAJollySHouldReturnOne() throws IllegalActionException {
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.remove(0);
		PoliticCard card = prototype.getPoliticCard();
		while(card.getCardColor() != Colore.JOLLY) {
			card = prototype.getPoliticCard();
		}
		cards.add(card);

		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, game.getBoard().getKingRegion()) == 1);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorWithFourPoliticCardsIncludingTwoJollySHouldReturnTwo() throws IllegalActionException {
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		for(int i=0; i<2; i++) {
			cards.remove(0);
			PoliticCard card = prototype.getPoliticCard();
			while(card.getCardColor() != Colore.JOLLY) {
				card = prototype.getPoliticCard();
			}
			cards.add(card);
		}

		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, game.getBoard().getKingRegion()) == 2);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorShouldPayOneCoinsMoreForEachJolly() throws IllegalActionException {
		
		int coinsWithoutJolly = request.countMoneyToSatisfyCouncillor(politicCards, player, game.getBoard().getKingRegion());
		
		int numberOfJolly = (int) (Math.random()*4 + 1);
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		for(int i=0; i<numberOfJolly; i++) {
			cards.remove(0);
			PoliticCard card = prototype.getPoliticCard();
			while(card.getCardColor() != Colore.JOLLY) {
				card = prototype.getPoliticCard();
			}
			cards.add(card);
		}

		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, game.getBoard().getKingRegion()) == coinsWithoutJolly + numberOfJolly);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testCountMoneyToSatisfyCouncillorWhenPlayerNotHaveEnoughCoinsShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		Player player = this.player;
		player.getCoinsTrack().setCoinsNumber(-player.getCoinsTrack().getCurrentCoins());
		
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.remove(0);

		request.countMoneyToSatisfyCouncillor(cards, player, game.getBoard().getKingRegion());
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenThePlayerNotHaveEnoughMoneyToSetTheKingInTheSelectedCityShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		player.getCoinsTrack().setCoinsNumber(-player.getCoinsTrack().getCurrentCoins());
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenThePlayerNotHaveEnoughAssistantShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		player.getAssistants().clear();
		city.getEmporium().add(new Emporium(2));
		
		request = new BuildEmporiumByKingHelpRqst(token, city, colors);
		request.accept(game, player, visitor);
	}

	@Test
	public void testAcceptWhenInTheSelectedCityThereIsEmporiumsOfOthersPlayerMustSetAssistantsToPayOneAssistantForEachEmporium() throws IllegalActionException {
		int n=5;
		for(int i=0; i<n; i++) {
			city.getEmporium().add(new Emporium(4));
			player.getAssistants().add(new Assistant());
		}
		
		request = new BuildEmporiumByKingHelpRqst(token, city, colors);
		request.accept(game, player, visitor);
		
		assertTrue(request.getAssistantsToPay() == n);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheCityIsAlreadyInPlayersCityShouldThrowAnIllegalActionException() throws IllegalActionException {
		player.getCities().add(city);
		request = new BuildEmporiumByKingHelpRqst(token, city, colors);
		request.accept(game, player, visitor);
	}
	
	@Test
	public void testAcceptWhenPlayerHasBeenBuildEmporiumsButNotInTheSelectedCityShouldGoWell() throws IllegalActionException {
		player.getCities().add(game.getBoard().getCities().get(4));
		request = new BuildEmporiumByKingHelpRqst(token, city, colors);
		request.accept(game, player, visitor);
		return;
	}
	
	@Test
	public void testGetMoneyToPayBeforeAcceptShouldReturnZero() {
		
		request = new BuildEmporiumByKingHelpRqst(token, city, colors);
		assertTrue(request.getMoneyToPay() == 0);
	}
	
	@Test
	public void testGetAssistantToPayBeforeAcceptShouldReturnZero() {
		
		request = new BuildEmporiumByKingHelpRqst(token, city, colors);
		assertTrue(request.getAssistantsToPay() == 0);
	}
	
	@Test
	public void testGetPoliticCardsBeforeAcceptShouldReturnAnEMptyList() {
		
		request = new BuildEmporiumByKingHelpRqst(token, city, colors);
		assertTrue(request.getPoliticCards().isEmpty());
	}
}
