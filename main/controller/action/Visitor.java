package it.polimi.ingsw.cg32.controller.action;

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
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.game.Player;

/**
 * This factory uses the visitor pattern to create an appropriate {@link
 * Action} from an {@link ActionRqst} without knowing the concrete type 
 * of the message object.
 * 
 * @author Stefano
 */
public interface Visitor {

	/**
	 * Visit a {@link BuildEmporiumByKingHelpRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(BuildEmporiumByKingHelpRqst request, Player player, Game game);
	
	
	/**
	 * Visit a {@link BuildEmporiumByPermitCardRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(BuildEmporiumByPermitCardRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link BuyPermitCardRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(BuyPermitCardRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link ElectCouncillorRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(ElectCouncillorRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link HireAssistantRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(HireAssistantRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link ChangeUsablePermitCardRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(ChangeUsablePermitCardRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link PerformAnotherPrimaryActionRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(PerformAnotherPrimaryActionRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link UseAssistantToElectCouncillorRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(UseAssistantToElectCouncillorRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link PrimaryActionBonusRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(PrimaryActionBonusRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link AssistantBonusRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(AssistantBonusRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link CityBonusRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(CityBonusRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link CoinsBonusRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(CoinsBonusRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link NobilityBonusRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(NobilityBonusRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link PermitCardBonusRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(PermitCardBonusRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link PoliticCardBonusRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(PoliticCardBonusRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link ReusePermitCardBonusRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(ReusePermitCardBonusRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link VictoryBonusRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(VictoryBonusRqst request, Player player, Game game);
	
	/**
	 * Visit a {@link MarketBuyRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(MarketBuyRqst request, Player playerThatBuy,  Game game);
	
	/**
	 * Visit a {@link MarketSellRqst} to create 
	 * an appropriate {@link Action}.
	 * 
	 * @param request the action request message
	 * @param player the player who request for this action
	 * @return the corresponding action
	 */
	public Action visit(MarketSellRqst request, Player playerThatSell, Game game);
}

