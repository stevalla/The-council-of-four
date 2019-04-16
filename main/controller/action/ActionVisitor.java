package it.polimi.ingsw.cg32.controller.action;

import it.polimi.ingsw.cg32.controller.action.bonus.AssistantBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.CityBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.CoinsBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.NobilityBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.PermitCardBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.PoliticCardBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.PrimaryActionBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.ReusePermitCardBonus;
import it.polimi.ingsw.cg32.controller.action.bonus.VictoryBonus;
import it.polimi.ingsw.cg32.controller.action.market.MarketBuy;
import it.polimi.ingsw.cg32.controller.action.market.MarketSell;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByKingHelp;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuildEmporiumByPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.BuyPermitCard;
import it.polimi.ingsw.cg32.controller.action.primaryaction.ElectCouncillor;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.ChangeUsablePermitCard;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.HireAssistant;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.PerformAnotherPrimaryAction;
import it.polimi.ingsw.cg32.controller.action.secondaryaction.UseAssistantToElectCouncillor;
import it.polimi.ingsw.cg32.message.request.ActionRqst;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByKingHelpRqst;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.BuyPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.ChangeUsablePermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.ElectCouncillorRqst;
import it.polimi.ingsw.cg32.message.request.action.HireAssistantRqst;
import it.polimi.ingsw.cg32.message.request.action.PerformAnotherPrimaryActionRqst;
import it.polimi.ingsw.cg32.message.request.action.UseAssistantToElectCouncillorRqst;
import it.polimi.ingsw.cg32.message.request.bonus.AssistantBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.CityBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.CoinsBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.NobilityBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.PermitCardBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.PoliticCardBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.PrimaryActionBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.ReusePermitCardBonusRqst;
import it.polimi.ingsw.cg32.message.request.bonus.VictoryBonusRqst;
import it.polimi.ingsw.cg32.message.request.market.MarketBuyRqst;
import it.polimi.ingsw.cg32.message.request.market.MarketSellRqst;
import it.polimi.ingsw.cg32.model.bonus.QuantityBonus;
import it.polimi.ingsw.cg32.model.card.politiccard.PoliticCardPrototype;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;


/**
 * This Action Visitor uses the visitor pattern to create actions from an {@link ActionRqst}
 * message without having to check with "instanceof" the concrete type of the object.
 * 
 * @author Stefano
 *
 */
public class ActionVisitor implements Visitor  {

	private final PoliticCardPrototype politicCardPrototype;
	
	/**
	 * Init the {@link PoliticCardPrototype} to create random politic cards
	 * 
	 * @throws NullPointerException if game is null
	 */
	public ActionVisitor() {
		
		this.politicCardPrototype = new PoliticCardPrototype();
	}

	
	@Override
	public Action visit(BuildEmporiumByKingHelpRqst request, Player player, Game game) {
		return new BuildEmporiumByKingHelp(player, game, 
				request.getCity(), request.getAssistantsToPay(), request.getMoneyToPay(), request.getPoliticCards());
	}
	
	
	@Override
	public Action visit(BuildEmporiumByPermitCardRqst request, Player player, Game game) {
		return new BuildEmporiumByPermitCard(player, game,
				request.getCity(), request.getAssistantsToPay(), request.getSelectedPermitCard());
	}
	
	
	@Override
	public Action visit(BuyPermitCardRqst request, Player player, Game game) {
		return new BuyPermitCard(player, game, request.getTargetRegion(), 
				request.getPoliticCards(), request.getCardToDraw(), request.getMoneyToPay());
	}
	
	
	@Override
	public Action visit(ElectCouncillorRqst request, Player player, Game game) {
		return new ElectCouncillor(player, game, 
				request.getTargetRegion(), request.getCouncillor());
	}
	
	
	@Override
	public Action visit(HireAssistantRqst request, Player player, Game game) {
		return new HireAssistant(player, game);
	}
	
	
	@Override
	public Action visit(ChangeUsablePermitCardRqst request, Player player, Game game) {
		return new ChangeUsablePermitCard(player, game, request.getTargetRegion());
	}
	
	
	@Override
	public Action visit(PerformAnotherPrimaryActionRqst request, Player player, Game game) {
		return new PerformAnotherPrimaryAction(player, game);
	}
	
	
	@Override
	public Action visit(UseAssistantToElectCouncillorRqst request, Player player, Game game) {
		return new UseAssistantToElectCouncillor(player, game, 
				request.getTargetRegion(), request.getCouncillor());
	}
	
	@Override
	public Action visit(PrimaryActionBonusRqst request, Player player, Game game) {
		
		return new PrimaryActionBonus(player, game, request.getPrimaryAction(), request.getBonus());
	}
	
	
	@Override
	public Action visit(AssistantBonusRqst request, Player player, Game game) {
		
		return new AssistantBonus(player, game, (QuantityBonus) request.getBonus());
	}
	
	
	@Override
	public Action visit(CityBonusRqst request, Player player, Game game) {
		
		return new CityBonus(player, game, request.getCity(), request.getBonus());
	}
	
	
	@Override
	public Action visit(CoinsBonusRqst request, Player player, Game game) {
		
		return new CoinsBonus(player, game, (QuantityBonus) request.getBonus());
	}
	
	
	@Override
	public Action visit(NobilityBonusRqst request, Player player, Game game) {
		
		return new NobilityBonus(player, game, request.getBonus());
	}
	
	
	@Override
	public Action visit(PermitCardBonusRqst request, Player player, Game game) {
		
		return new PermitCardBonus(player, game, request.getCardToDraw(), request.getTargetRegion(), request.getBonus());
	}
	
	
	@Override
	public Action visit(PoliticCardBonusRqst request, Player player, Game game) {
		
		return new PoliticCardBonus(player, game, (QuantityBonus)request.getBonus(), politicCardPrototype);
	}
	
	
	@Override
	public Action visit(ReusePermitCardBonusRqst request, Player player, Game game) {
		
		return new ReusePermitCardBonus(player, game, request.getTargetPermitCard(), request.getBonus());
	}
	
	
	@Override
	public Action visit(VictoryBonusRqst request, Player player, Game game) {
		
		return new VictoryBonus(player, game, (QuantityBonus) request.getBonus());
	}
	
	
	@Override
	public Action visit(MarketBuyRqst request, Player player, Game game) {
		
		return new MarketBuy(player, game, game.getPlayers().get(request.getPlayerThatSell().getPlayerNumber()), request.getBoundleId());
	}
	
	
	@Override
	public Action visit(MarketSellRqst request, Player player, Game game) {
		
		return new MarketSell(player, game, request.getToken(), request.getPermitCardsInt(), 
				request.getAssistantsInt(), request.getColors());
	}
}
