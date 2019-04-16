package it.polimi.ingsw.cg32.message.request.bonus;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg32.controller.action.Action;
import it.polimi.ingsw.cg32.controller.action.ActionVisitor;
import it.polimi.ingsw.cg32.controller.action.bonus.PrimaryActionBonus;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByKingHelp;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuyPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.ElectCouncillor;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByKingHelpRqst;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.BuyPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.ElectCouncillorRqst;
import it.polimi.ingsw.cg32.message.request.action.HireAssistantRqst;
import it.polimi.ingsw.cg32.message.request.action.MainActionRqst;
import it.polimi.ingsw.cg32.model.balcony.Councillor;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.card.permitcard.PermitCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCard;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.model.region.Region;
import it.polimi.ingsw.cg32.utilities.exception.IllegalActionException;
import it.polimi.ingsw.cg32.view.server.Token;

public class TestPrimaryActionBonusRqst {

	PrimaryActionBonusRqst request;
	private MainActionRqst primaryRequest;
	private Game game;
	private Player player;
	private ActionVisitor visitor;
	private Region region;
	private Action action;
	private Token token;
	
	@Before
	public void initializeRequest() {
		game = new Game("src/main/resources/map/default.txt", 4);
		player = game.getPlayers().get(0);
		visitor = new ActionVisitor();
		region = game.getBoard().getRegions(RegionType.COAST);
		Councillor councillor = game.getBoard().getCouncillorReserve().getCouncillorInReserve().get(0); 
		token = new Token();
		primaryRequest = new ElectCouncillorRqst(token, region, councillor.getColor());
		request = new PrimaryActionBonusRqst(token, primaryRequest, 
				new SingleBonus("PRIMARYACTIONBONUS"));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConsturctorWithTokenNullValuesShouldThrowANullPointerException() {
		
		request = new PrimaryActionBonusRqst(null, primaryRequest, new SingleBonus("PRIMARYACTIONBONUS"));
	}
	
	@Test(expected = NullPointerException.class)
	public void testConsturctorWithBonusNullValuesShouldThrowANullPointerException() {
		
		request = new PrimaryActionBonusRqst(token, primaryRequest, null);
	}
	
	@Test
	public void testConsturctorWithPrimaryRqstNullShouldGoCorrectly() {
		
		request = new PrimaryActionBonusRqst(token, null, new SingleBonus("PRIMARYACTIONBONUS"));
		return;
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheBonusIsAQuantityBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new PrimaryActionBonusRqst(token, primaryRequest, new QuantityBonus("PRIMARYACTIONBONUS", 3));
		request.accept(game, player, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenArgumentsAreNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(null, null, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerAndBoardAreNullShouldThrowANullPointerException() throws IllegalActionException {
		
		request.accept(null, null, visitor);
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenPlayerIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		assertNull(request.accept(null, player, visitor));
	}
	
	@Test(expected = NullPointerException.class)
	public void testAcceptWhenBoardIsNullShouldThrowANullPointerException() throws IllegalActionException {
		
		assertNull(request.accept(game, null, visitor));
	}
	
	@Test
	public void testAcceptShouldReturnAPrimaryActionBonusActionNotNull() throws IllegalActionException {
		
		action = request.accept(game, player, visitor);
		
		assertThat(action, is(instanceOf(PrimaryActionBonus.class)));
		assertNotNull(action);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptWhenTheBonusNameIsNotCoinsBonusShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new PrimaryActionBonusRqst(token, primaryRequest, new SingleBonus("VICTORYBONUS"));
		request.accept(game, player, visitor);
	}
	
	@Test(expected = IllegalActionException.class)
	public void testConsturctorWithPrimaryRqstIsNullValuesShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		request = new PrimaryActionBonusRqst(token, null, new SingleBonus("PRIMARYACTIONBONUS"));
		request.accept(game, player, visitor);
	}
	
	@Test
	public void testAcceptPassedABuyPermitCardRqstShouldCreateAnActionBuyPermitCard() throws IllegalActionException {
		PoliticCardPrototype prototype = new PoliticCardPrototype();
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
		
		List<Colore> colors = new ArrayList<>();
		for(Councillor councillor : region.getBalcony().getSeats())
			colors.add(councillor.getColor());
		
		primaryRequest = new BuyPermitCardRqst(token, (NormalRegion) region, 
				((NormalRegion)region).getDeckOfPermitCard().getUsableCard().get(0).getId(), colors);
		
		request = new PrimaryActionBonusRqst(token, primaryRequest, new SingleBonus("PRIMARYACTIONBONUS"));
		request.accept(game, player, visitor);
		
		assertThat(request.getPrimaryAction(), is(instanceOf(BuyPermitCard.class)));
		
	}
	
	@Test
	public void testAcceptPassedAnElectCouncillorRqstShouldCreateAnActionElectCouncillor() throws IllegalActionException {

		request.accept(game, player, visitor);
		
		assertThat(request.getPrimaryAction(), is(instanceOf(ElectCouncillor.class)));
		
	}
	
	@Test
	public void testAcceptPassedABuildEmporiumByPermitCardRqstShouldCreateAnActionBuildEmporiumByPermitCard() throws IllegalActionException {
		
		player.getPermitCards().add(game.getBoard().getRegions(RegionType.COAST).getDeckOfPermitCard().getDeck().poll());
		
		PermitCard card = player.getPermitCards().get(0);
		primaryRequest = new BuildEmporiumByPermitCardRqst(token, card.getCity().get(0), card.getId());
				
		request = new PrimaryActionBonusRqst(token, primaryRequest, new SingleBonus("PRIMARYACTIONBONUS"));
		request.accept(game, player, visitor);
		
		assertThat(request.getPrimaryAction(), is(instanceOf(BuildEmporiumByPermitCard.class)));
		
	}
	
	@Test
	public void testAcceptPassedABuildEmporiumByKingHelpRqstShouldCreateAnActionBuildEmporiumByKingHelp() throws IllegalActionException {
		PoliticCardPrototype prototype = new PoliticCardPrototype();
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
		
		List<Colore> colors = new ArrayList<>();
		for(Councillor councillor : game.getBoard().getKingRegion().getBalcony().getSeats())
			colors.add(councillor.getColor());
		
		primaryRequest = new BuildEmporiumByKingHelpRqst(token, game.getBoard().getCities().get(0), colors);
		
		request = new PrimaryActionBonusRqst(token, primaryRequest, new SingleBonus("PRIMARYACTIONBONUS"));
		request.accept(game, player, visitor);
		
		assertThat(request.getPrimaryAction(), is(instanceOf(BuildEmporiumByKingHelp.class)));
		
	}
	
	@Test(expected = IllegalActionException.class)
	public void testAcceptPassedASecondaryActionRqstShouldThrowAnIllegalActionException() throws IllegalActionException {
		
		primaryRequest = new HireAssistantRqst(token);
		
		request = new PrimaryActionBonusRqst(token, primaryRequest, new SingleBonus("PRIMARYACTIONBONUS"));
		request.accept(game, player, visitor);
	}

}
