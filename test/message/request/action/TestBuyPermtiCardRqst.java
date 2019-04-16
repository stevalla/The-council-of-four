package it.polimi.ingsw.cg32.message.request.action;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
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
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuyPermitCard;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestBuyPermtiCardRqst {

	BuyPermitCardRqst request;
	private NormalRegion region;
	private Token token;
	private Game game;
	private Player player;
	private List<Colore> colors;
	private String permitCardId;
	private ActionVisitor visitor;
	private PoliticCardPrototype prototype;
	private List<PoliticCard> politicCards;
	
	@Before
	public void initParams() throws IllegalActionException {
		game = new Game("src/main/resources/map/default.txt", 3);
		region = game.getBoard().getRegions(RegionType.COAST);
		token = new Token();
		player = game.getPlayers().get(0);
		visitor = new ActionVisitor();
		colors = new ArrayList<>();
		prototype = new PoliticCardPrototype();
		
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
		
		for(Councillor councillor : region.getBalcony().getSeats())
			colors.add(councillor.getColor());
		
		permitCardId = region.getDeckOfPermitCard().getUsableCard().get(0).getId();
		request = new BuyPermitCardRqst(token, region, permitCardId, colors);
		politicCards = request.getPoliticCardByColors(colors, player);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenTokenIsNullShouldThrowANullPointerExcetpion() {
		
		new BuyPermitCardRqst(null, region, permitCardId, colors);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenRegionIsNullShouldThrowANullPointerException() {
		
		new BuyPermitCardRqst(token, null, permitCardId, colors);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenColorsIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request = new BuyPermitCardRqst(token, region, permitCardId, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testConstructWhenPermitCardIdIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request = new BuyPermitCardRqst(token, region, null, colors);
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
	public void testAcceptShouldCreateBuyPermitCard() throws IllegalActionException {

		Action action = request.accept(game, player, visitor);

		assertThat(action, is(instanceOf(BuyPermitCard.class)));
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
		request.countMoneyToSatisfyCouncillor(cards, player, region);
	}
	
	@Test(expected = NullPointerException.class)
	public void testCountMoneyToSatisfyCouncillorrWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.countMoneyToSatisfyCouncillor(politicCards, null, region);
	}
	
	@Test(expected = NullPointerException.class)
	public void testCountMoneyToSatisfyCouncillorrWhenPoliticCardsIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.countMoneyToSatisfyCouncillor(null, player, region);
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
		region.getBalcony().getSeats().stream().forEach(c -> colors.add(c.getColor()));

		while(colors.contains(card.getCardColor()) || card.getCardColor() == Colore.JOLLY)
			card = prototype.getPoliticCard();
		
		cards.add(card);
		request.countMoneyToSatisfyCouncillor(cards, player, region);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorWhenPoliticCardsAreFourAndThereAreNotJollyShouldReturnZero() throws IllegalActionException {

		assertTrue(request.countMoneyToSatisfyCouncillor(politicCards, player, region) == 0);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorWhenPoliticCardsAreThreeAndThereAreNotJollyShouldReturnFour() throws IllegalActionException {
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.remove(0);
		
		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, region) == 4);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorWhenPoliticCardsAreTwoAndThereAreNotJollyShouldReturnSeven() throws IllegalActionException {
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.remove(0);
		cards.remove(0);

		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, region) == 7);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorWhenPoliticCardsAreOneAndThereAreNotJollyShouldReturnTen() throws IllegalActionException {
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.remove(0);
		cards.remove(0);
		cards.remove(0);
		
		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, region) == 10);
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

		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, region) == 1);
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

		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, region) == 2);
	}
	
	@Test
	public void testCountMoneyToSatisfyCouncillorShouldPayOneCoinsMoreForEachJolly() throws IllegalActionException {
		
		int coinsWithoutJolly = request.countMoneyToSatisfyCouncillor(politicCards, player, region);
		
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

		assertTrue(request.countMoneyToSatisfyCouncillor(cards, player, region) == coinsWithoutJolly + numberOfJolly);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testCountMoneyToSatisfyCouncillorWhenPlayerNotHaveEnoughCoinsShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		Player player = this.player;
		player.getCoinsTrack().setCoinsNumber(-player.getCoinsTrack().getCurrentCoins());
		
		List<PoliticCard> cards = new ArrayList<>(politicCards);
		cards.remove(0);

		request.countMoneyToSatisfyCouncillor(cards, player, region);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheSelectedPermitCardsIsNotInTheUsableCardsOfTheSelectedRegionShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		String id = region.getDeckOfPermitCard().getDeck().peek().getId();
		
		BuyPermitCardRqst request = new BuyPermitCardRqst(token, region, id, colors);
		request.accept(game, player, visitor);
	}
	
	@Test
	public void testGetMoneyToPayBeforeAcceptShouldReturnZero() {
		
		BuyPermitCardRqst request = new BuyPermitCardRqst(token, region, permitCardId, colors);
		assertTrue(request.getMoneyToPay() == 0);
	}
	
	@Test
	public void testGetPoliticCardsBeforeAcceptShouldReturnAnEMptyList() {
		
		BuyPermitCardRqst request = new BuyPermitCardRqst(token, region, permitCardId, colors);
		assertTrue(request.getPoliticCards().isEmpty());
	}
	
}
