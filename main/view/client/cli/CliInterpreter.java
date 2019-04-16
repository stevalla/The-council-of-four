 package it.polimi.ingsw.cg32.view.client.cli;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import it.polimi.ingsw.cg32.message.request.ChangeMapRqst;
import it.polimi.ingsw.cg32.message.request.EndTurnRqst;
import it.polimi.ingsw.cg32.message.request.ExitRqst;
import it.polimi.ingsw.cg32.message.request.RequestMessage;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByKingHelpRqst;
import it.polimi.ingsw.cg32.message.request.action.BuildEmporiumByPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.BuyPermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.ChangeUsablePermitCardRqst;
import it.polimi.ingsw.cg32.message.request.action.ElectCouncillorRqst;
import it.polimi.ingsw.cg32.message.request.action.HireAssistantRqst;
import it.polimi.ingsw.cg32.message.request.action.MainActionRqst;
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
import it.polimi.ingsw.cg32.model.bonus.Bonus;
import it.polimi.ingsw.cg32.model.city.City;
import it.polimi.ingsw.cg32.model.color.Colore;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.model.region.NormalRegion;
import it.polimi.ingsw.cg32.model.region.Region;
import it.polimi.ingsw.cg32.utilities.exception.IllegalInputException;
import it.polimi.ingsw.cg32.view.server.Token;


/**
 * This class is used to parse the player's messages in a {@link RequestMessage}
 * to send in the server. If the string is incorrect throw an {@link IllegalInputException}
 * Every action command must be write in a single line with the specific format
 * explained in every method of the class.<br>
 * To make the game more easy the request for some information about the game
 * are not parse and send to the server, but they are displayed from this class
 * immediately.
 * 
 * @author Stefano
 *
 */
public class CliInterpreter {
	 
	
	/**
	 * This map is used to parse the message without the use of if else clause to reduce
	 * the complexity of the algorithm.
	 */
	private final Map<String, Parser<List<String>, Token, Game, RequestMessage>> parser = new HashMap<>();
	/**
	 * This map is used to display the correct info request from the client.
	 */
	private final Map<String, BiConsumer<Game, Token>> infoCli = new HashMap<>();
	private static final String INPUT_ERROR = "Wrong action command";
	private List<Bonus> bonusToExecute;
	private final PrintStream out = new PrintStream(System.out);
	private final String info;
	private final String actionFormat;
	
	
	/**
	 * Load the parser and the maps to display info.
	 * Save the list where add the bonus to execute when there are someone.
	 * 
	 * @param bonusToExecute the reference of the bonus to execute
	 */
	public CliInterpreter(List<Bonus> bonusToExecute) {
		loaderMaps();
		this.info = loadInfo();
		this.actionFormat = loadActionFormat();
		this.bonusToExecute = bonusToExecute;
	}
	
	/**
	 * This interface rapresent a ThreeFunction, it has only one method
	 * to apply the three input params on the specific function and return
	 * the result. This implementation is take from the
	 * {@link java.util.function.Function} interface of Java8.
	 * 
	 * @author Stefano
	 *
	 * @param <T> First input param
	 * @param <V> Second input param
	 * @param <W> Third input param
	 * @param <R> The result of the application of the function with the three params
	 * @see java.util.function.Function
	 */
	@FunctionalInterface
	private interface Parser<T, V, W, R> {
		
		public R apply(T t, V v, W w) throws IllegalInputException;
	}
	
	/**
	 * Load the map parser, this map is composed by a String as key, this is the first
	 * word of the strings and it's represent the action that the player request to perform,
	 * and a Functional Interface as value that represent a function that take three params and return
	 * the correct request message.<br>
	 * Furthermore load the map to display info to the client immediately without pass from server.
	 * This map has as key a string that represent the info to display, and a {@link BiConsumer} of
	 * Game and Token which display the correct info.
	 */
	private void loaderMaps() { 
		parser.put("BUILDEMPORIUMBYKINGHELP", (s, t, g) -> buildEmporiumByKingHelp(g, t, s));
		parser.put("BUILDEMPORIUMBYPERMITCARD", (s, t, g) -> buildEmporiumByPermitCard(g, t, s));
		parser.put("BUYPERMITCARD", (s, t, g) -> buyPermitCard(g, t, s));
		parser.put("ELECTCOUNCILLOR", (s, t, g) -> electCouncillor(g, t, s));
		parser.put("CHANGEUSABLEPERMITCARD", (s, t, g) -> changeUsablePermitCard(g, t, s));
		parser.put("PERFORMANOTHERPRIMARYACTION", (s, t, g) -> performAnotherPrimaryAction(t));
		parser.put("USEASSISTANTTOELECTCOUNCILLOR", (s, t, g) -> useAssistantToElectCouncillor(g, t, s));
		parser.put("HIREASSISTANT", (s, t, g) -> hireAssistant(t));
		parser.put("PERMITCARDBONUS", (s, t, g) -> permitCardBonus(g, t, s));
		parser.put("REUSEPERMITCARDBONUS", (s, t, g) -> reusePermitCardBonus(t, s));
		parser.put("CITYBONUS", (s, t, g) -> cityBonus(g, t, s));
		parser.put("PRIMARYACTIONBONUS", (s, t, g) -> primaryActionBonus(g, t, s));
		parser.put("ASSISTANTBONUS", (s, t, g) -> assistantBonus(t));
		parser.put("VICTORYBONUS", (s, t, g) -> victoryBonus(t));
		parser.put("COINSBONUS", (s, t, g) -> coinsBonus(t));
		parser.put("NOBILITYBONUS", (s, t, g) -> nobilityBonus(t));
		parser.put("POLITICCARDBONUS", (s, t, g) -> politicCardBonus(t));
		parser.put("MARKETSELL", (s, t, g) -> marketSell(s, t));
		parser.put("MARKETBUY", (s, t, g) -> marketBuy(s, t));
		parser.put("ENDTURN", (s, t, g) -> endTurn(t));
		parser.put("EXIT", (s, t, g) -> exit(t));
		parser.put("CHANGEMAP", (s, t, g) -> changeMap(t, g, s));

		infoCli.put("INFOMOUNTAIN", (g, t) -> out.print(g.getBoard().getRegions(RegionType.MOUNTAIN)));
		infoCli.put("INFOCOAST", (g, t) -> out.print(g.getBoard().getRegions(RegionType.COAST)));
		infoCli.put("INFOHILL", (g, t) -> out.print(g.getBoard().getRegions(RegionType.HILL)));
		infoCli.put("INFOREGIONKING", (g, t) -> out.print(g.getBoard().getKingRegion()));
		infoCli.put("INFOPOLITICCARD", (g, t) -> out.print(g.getPlayers().get(t.getPlayerNumber()).getPoliticCards()));
		infoCli.put("INFOPERMITCARD", (g, t) -> out.print(g.getPlayers().get(t.getPlayerNumber()).getPermitCards()));
		infoCli.put("INFOCOINS", (g, t) -> out.print(g.getPlayers().get(t.getPlayerNumber()).getCoinsTrack().getCurrentCoins()));
		infoCli.put("INFOVICTORY", (g, t) -> out.print(g.getPlayers().get(t.getPlayerNumber()).getVictoryTrack().getPoints()));
		infoCli.put("INFOCITYGRAPH", (g, t) -> out.print(g.getBoard().getConnectionToString()));
		infoCli.put("INFOCITY", (g, t) -> out.print(g.getBoard().getCities()));
		infoCli.put("INFOASSISTANTRESERVE", (g, t) -> out.print(g.getBoard().getAssistantsReserve()));
		infoCli.put("INFOCOUNCILLORRESERVE", (g, t) -> out.print(g.getBoard().getCouncillorReserve()));
		infoCli.put("INFOMARKET", (g, t) -> out.print(g.storeToString()));
		infoCli.put("INFONOBILITYTRACK", (g, t) -> out.print(g.getBoard().getNobilityTrack()));
		infoCli.put("INFOKING", (g, t) -> out.print(g.getBoard().getKing()));
		infoCli.put("INFOEMPORIUM", (g, t) -> out.print(g.getPlayers().get(t.getPlayerNumber()).getEmporiums().size()));
		infoCli.put("INFOMYCITIES", (g, t) -> out.print(g.getPlayers().get(t.getPlayerNumber()).getCities()));
		infoCli.put("INFOCURRENTSPACE", (g, t) -> out.print(g.getPlayers().get(t.getPlayerNumber()).getMarkerDisk().getCurrentSpace()));
		infoCli.put("INFOPLAYERS", (g, t) -> infoPlayers(g));
		infoCli.put("INFO", (g, t) -> out.print(info));
		infoCli.put("ACTION", (g, t) -> out.print(actionFormat));
	} 

	
	/**
	 * Format the request message to delete some error character and parse it to create 
	 * a valid {@link RequestMessage} to send to the server.
	 * If the game is null the only accepted message are:<br>
	 * -Exit<br>
	 * -Changemap<br>
	 * -Action<br>
	 * They are explaneid in the relatives methods.
	 * 
	 * @param input the command of the client
	 * @param game the game, if it is null the game is not started
	 * @param token the token of the client to put in the message
	 * @return the appropriate {@link RequestMessage}
	 * @throws IllegalInputException if the string is made bad
	 */
	public RequestMessage parserMessage(String input, Game game, Token token) throws IllegalInputException {
		
		List<String> params = new ArrayList<>();
		
		String cmd = formatString(input);
		String action = new String();
		
		if(game == null && !input.matches("^EXIT|ACTION|(CHANGEMAP \\w.*)$"))
			throw new IllegalInputException("The match isn't already begin.");
		
		if(input.matches("^(INFOMOUNTAIN|INFOCOAST|INFOHILL|INFOCITYGRAPH|INFOCITY|INFOASSISTANTRESERVE|"
				+ "INFOPERMITCARD|INFOCOINS|INFOVICTORY|INFOCITYGRAPH|INFOCITY|INFOASSISTANTRESERVE|INFOPLAYERS|"
				+ "INFOCOUNCILLORRESERVE|INFOMARKET|INFONOBILITYTRACK|INFOKING|INFOREGIONKING|INFOPOLITICCARD|"
				+ "INFOCURRENTSPACE|INFOMYCITIES|INFOEMPORIUM|INFO|ACTION)$")) {
			infoCli.get(input).accept(game, token);
			return null;
		}
		
		/*
		 * If the string doesn't contained white spaces it has no params, if not 
		 * split every param.
		 */ 
		if(!cmd.contains(" ")) 
			action = cmd;
		else {
			action = cmd.substring(0, cmd.indexOf(" ")); 
			params.addAll(Arrays.asList(cmd.replaceFirst(action + " ", "").split(" ")));
		}
		
		/*
		 * Use the first param of the string to select the correct fuction and then apply
		 * the params to the corresponding function to obtained the correct request message.
		 */ 
		return parser.get(action).apply(params, token, game);
	}
	
	
	/**
	 * Format the string and control if is one of the game's action.
	 * 
	 * @param input the string to be formatted
	 * @return the formatted string
	 * @throws IllegalInputException if the string is incorrect
	 */
	private String formatString(String input) throws IllegalInputException {
		
		/*
		 * Delete all the unnecessary white spaces
		 */ 
		String cmd = input.replaceAll("( )+", " ").replaceAll("\\s+$", "").replaceAll(",\\s+", ",").replaceAll("\\s+,", ",");
		
		if(cmd.matches("^\\s.*"))
			cmd = cmd.replaceFirst(" ", "");
		
		/*
		 * If the first word don't match with anything, the message is incorrect
		 */ 
		if(!cmd.matches("^(((BUILDEMPORIUMBYKINGHELP|BUILDEMPORIUMBYPERMITCARD|BUYPERMITCARD|ELECTCOUNCILLOR|"
				+ "CHANGEUSABLEPERMITCARD|USEASSISTANTTOELECTCOUNCILLOR|"
				+ "PERMITCARDBONUS|REUSEPERMITCARDBONUS|CITYBONUS|PRIMARYACTIONBONUS|MARKETSELL|MARKETBUY|CHANGEMAP) \\w.*)|"
				+ "(HIREASSISTANT|PERFORMANOTHERPRIMARYACTION|ASSISTANTBONUS|VICTORYBONUS|NOBILITYBONUS|POLITICCARDBONUS|"
				+ "COINSBONUS|MARKETSELL|ENDTURN|CITYBONUS|REUSEPERMITCARDBONUS|EXIT|INFOMOUNTAIN|INFOCOAST|INFOHILL|INFOREGIONKING|INFOPOLITICCARD|"
				+ "INFOPERMITCARD|INFOCOINS|INFOCITYGRAPH|INFOCITY|INFOVICTORY|INFOASSISTANTRESERVE|"
				+ "INFOCOUNCILLORRESERVE|INFOMARKET|INFONOBILITYTRACK|INFOKING|INFOPLAYERS|"
				+ "INFOCURRENTSPACE|INFOMYCITIES|INFOEMPORIUM|INFO|ACTION))$")) 
			throw new IllegalInputException(INPUT_ERROR);
		
		return cmd;
	}

	
	/**
	 * This method create a {@link BuildEmporiumByKingHelpRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -one string with the name of the city where build the emporium<br>
	 * -Between one and four colors separated from commas that rapresent the color of the
	 *  politiccard played to satisfy the king's balcony.<br>
	 *  
	 * To execute this action the string must be:<br>
	 * BUILDMEPORIUMBYKINGHELP nameofthecity color,color,color,color<br>
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link BuildEmporiumByKingHelpRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected BuildEmporiumByKingHelpRqst buildEmporiumByKingHelp(Game game, Token token, List<String> params) 
			throws IllegalInputException {
		
		if(params.size() != 2)
			throw new IllegalInputException(INPUT_ERROR);
		
		List<Colore> colors = new ArrayList<>();
		for(String string : params.get(1).split(","))
			colors.add(findColor(string));

		return new BuildEmporiumByKingHelpRqst(token, findCity(params.get(0), game), colors);
	}
	
	/**
	 * This method create a {@link BuildEmporiumByPermitCardRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -one string with the name of the city where build the emporium<br>
	 * -the number of the player's permit card to use.<br>
	 *  
	 * To execute this action the string must be:<br>
	 * BUILDMEPORIUMBYPERMITCARD nameofthecity #permitcardtouse
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link BuildEmporiumByPermitCardRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected BuildEmporiumByPermitCardRqst buildEmporiumByPermitCard(Game game, Token token, List<String> params) 
			throws IllegalInputException {
		
		if(params.size() != 2 || !params.get(1).matches("\\d+"))
			throw new IllegalInputException(INPUT_ERROR);
		
		return new BuildEmporiumByPermitCardRqst(token, findCity(params.get(0), game), params.get(1));
	}
	
	/**
	 * This method create a {@link BuyPermitCardRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -the NormalRegion where buy the permit card<br>
	 * -the number of permit card to buy<br>
	 * -Between one and four colors separated from commas that rapresent the color of the
	 *  politiccard played to satisfy the selected region's balcony.<br>
	 *  
	 * To execute this action the string must be:<br>
	 * BUYPERMITCARD typeofregion #permitcardtobuy color,color,color,color
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link BuyPermitCardRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected BuyPermitCardRqst buyPermitCard(Game game, Token token, List<String> params) throws IllegalInputException {

		if(params.size() != 3 || !params.get(1).matches("\\d+"))
			throw new IllegalInputException(INPUT_ERROR);
		
		List<Colore> colors = new ArrayList<>();
		for(String string : params.get(2).split(","))
			colors.add(findColor(string));
		
		return new BuyPermitCardRqst(token, findNormalRegion(params.get(0), game), params.get(1), colors);
	}
	
	/**
	 * This method create a {@link ElectCouncillorRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -the Region where elect the councillor<br>
	 * -the color that rapresent the color of the councillor to elect<br>
	 *  
	 * To execute this action the string must be:<br>
	 * ELECTCOUNCILLOR region color
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return an {@link ElectCouncillorRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected ElectCouncillorRqst electCouncillor(Game game, Token token, List<String> params) throws IllegalInputException {
		
		if(params.size() != 2)
			throw new IllegalInputException(INPUT_ERROR);

		return new ElectCouncillorRqst(token, findRegion(params.get(0), game), findColor(params.get(1)));
	}
	
	/**
	 * This method create a {@link ChangeUsablePermitCardRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -the {@link NormalRegion} where change the usable permit cards<br>
	 *  
	 * To execute this action the string must be:<br>
	 * CHANGEUSABLEPERMITCARD normalregion 
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link BuyPermitCardRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected ChangeUsablePermitCardRqst changeUsablePermitCard(Game game, Token token, List<String> params) 
			throws IllegalInputException {
		
		if(params.size() != 1)
			throw new IllegalInputException(INPUT_ERROR);
		
		return new ChangeUsablePermitCardRqst(token, findNormalRegion(params.get(0), game));
	}
	
	/**
	 * This method create a {@link PerformAnotherPrimaryActionRqst} to send to the server.
	 * It no accept params.<br>
	 *  
	 * To execute this action the string must be:<br>
	 * PERFORMANOTHERPRIMARYACTION
	 * 
	 * @param token the client's {@link Token}
	 * @return a {@link PerformAnotherPrimaryActionRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected PerformAnotherPrimaryActionRqst performAnotherPrimaryAction(Token token) 
			throws IllegalInputException {
		
		return new PerformAnotherPrimaryActionRqst(token);
	}

	/**
	 * This method create a {@link UseAssistantToElectCouncillorRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -the Region where elect the councillor<br>
	 * -the color that rapresent the color of the councillor to elect<br>
	 *  
	 * To execute this action the string must be:<br>
	 * USEASSISTANTTOELECTCOUNCILLOR region color
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return an {@link UseAssistantToElectCouncillorRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected UseAssistantToElectCouncillorRqst useAssistantToElectCouncillor(Game game, Token token, List<String> params) 
			throws IllegalInputException {
		
		if(params.size() != 2)
			throw new IllegalInputException(INPUT_ERROR);

		return new UseAssistantToElectCouncillorRqst(token, findRegion(params.get(0), game), findColor(params.get(1)));
	}
	
	/**
	 * This method create a {@link HireAssistantRqst} to send to the server.
	 * It no accept params <br>
	 *  
	 * To execute this action the string must be:<br>
	 * HIREASSISTANT
	 * 
	 * @param token the client's {@link Token}
	 * @return an {@link HireAssistantRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected HireAssistantRqst hireAssistant(Token token) {
		
		return new HireAssistantRqst(token);
	}
	
	/**
	 * This method create a {@link PermitCardBonusRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -the NormalRegion where buy the permit card<br>
	 * -the number of permit card to buy<br>
	 *  
	 * To execute this action the string must be:<br>
	 * PERMITCARDBONUS normalregion #permitcard
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link PermiCardBonusRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected PermitCardBonusRqst permitCardBonus(Game game, Token token, List<String> params) throws IllegalInputException {
		
		if(params.size() != 2 || !params.get(1).matches("\\d+"))
			throw new IllegalInputException(INPUT_ERROR);

		NormalRegion region = findNormalRegion(params.get(0), game);
		
		return new PermitCardBonusRqst(token, params.get(1), region, findBonus("PERMITCARDBONUS"));
	}
	
	/**
	 * This method create a {@link ReusePermitCardBonusRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -the number of permit card to reuse<br>
	 *  
	 * To execute this action the string must be:<br>
	 * REUSEPERMITCARDBONUS #permitcard
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link ReusePermiCardBonusRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected ReusePermitCardBonusRqst reusePermitCardBonus(Token token, List<String> params) throws IllegalInputException {
		
		if(params.size() != 1 || !params.get(0).matches("\\d+"))
			throw new IllegalInputException(INPUT_ERROR);

		return new ReusePermitCardBonusRqst(token, params.get(0), findBonus("REUSEPERMITCARDBONUS"));
	}
	
	/**
	 * This method create a {@link CityBonusRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -the city with the bonus to get<br>
	 *  
	 * To execute this action the string must be:<br>
	 * CITYBONUS cityname
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link CityBonusRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected CityBonusRqst cityBonus(Game game, Token token, List<String> params) throws IllegalInputException {
		if(params.isEmpty()) {
			Bonus bonus = bonusToExecute.stream()
							.filter(b -> b.getName().equals("CITYBONUS"))
							.findFirst()
							.orElseThrow(() -> new IllegalInputException("You don't won a " + "CITYBONUS"));
			bonusToExecute.remove(bonus);
		}
		if(params.size() != 1)
			throw new IllegalInputException(INPUT_ERROR);

		return new CityBonusRqst(token, findCity(params.get(0), game), findBonus("CITYBONUS"));
	}
	
	/**
	 * This method create a {@link PrimaryActionBonusRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -the string with the primary action to perform<br>
	 *  
	 * To execute this action the string must be:<br>
	 * PRIMARYACTIONBONUS primaryactiontoperform
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link PrimaryActionBonusRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected PrimaryActionBonusRqst primaryActionBonus(Game game, Token token, List<String> params) throws IllegalInputException {
		
		return new PrimaryActionBonusRqst(token, (MainActionRqst) findPrimaryAction(game, token, params), findBonus("PRIMARYACTIONBONUS"));
	}
	
	/**
	 * This method create a {@link AssistantBonusRqst} to send to the server.
	 * It no accept params<br>
	 *  
	 * To execute this action the string must be:<br>
	 * ASSISTANTBONUS
	 * 
	 * @param token the client's {@link Token}
	 * @return an {@link AssistantBonusRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected AssistantBonusRqst assistantBonus(Token token) throws IllegalInputException {
		
		return new AssistantBonusRqst(token, findBonus("ASSISTANTBONUS"));
	}
	
	/**
	 * This method create a {@link CoinsBonusRqst} to send to the server.
	 * It no accept params<br>
	 *  
	 * To execute this action the string must be:<br>
	 * COINSBONUS
	 * 
	 * @param token the client's {@link Token}
	 * @return a {@link CoinsBonusRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected CoinsBonusRqst coinsBonus(Token token) throws IllegalInputException {
		
		return new CoinsBonusRqst(token, findBonus("COINSBONUS"));
	}
	
	/**
	 * This method create a {@link VictoryBonusRqst} to send to the server.
	 * It no accept params<br>
	 *  
	 * To execute this action the string must be:<br>
	 * VICTORYBONUS
	 * 
	 * @param token the client's {@link Token}
	 * @return a {@link VictoryBonusRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected VictoryBonusRqst victoryBonus(Token token) throws IllegalInputException {
		
		return new VictoryBonusRqst(token, findBonus("VICTORYBONUS"));
	}
	
	/**
	 * This method create a {@link NobilityBonusRqst} to send to the server.
	 * It no accept params<br>
	 *  
	 * To execute this action the string must be:<br>
	 * NOBILITYBONUS
	 * 
	 * @param token the client's {@link Token}
	 * @return a {@link NobilityBonusRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected NobilityBonusRqst nobilityBonus(Token token) throws IllegalInputException {
		
		return new NobilityBonusRqst(token, findBonus("NOBILITYBONUS"));
	}
	
	/**
	 * This method create a {@link PoliticCardBonusRqst} to send to the server.
	 * It no accept params<br>
	 *  
	 * To execute this action the string must be:<br>
	 * POLITICCARDBONUS
	 * 
	 * @param token the client's {@link Token}
	 * @return a {@link PoliticCardBonusRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected PoliticCardBonusRqst politicCardBonus(Token token) throws IllegalInputException {
		
		return new PoliticCardBonusRqst(token, findBonus("POLITICCARDBONUS"));
	}
	
	/**
	 * This method create a {@link MarketBuyRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -the number of the boundle to buy<br>
	 *  
	 * To execute this action the string must be:<br>
	 * MARKETBUY #boundle
	 * 
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link MarketBuyRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected MarketBuyRqst marketBuy(List<String> params, Token token) throws IllegalInputException {

		if(params.size() != 1 || !params.get(0).matches("^\\d+$"))
			throw new IllegalInputException(INPUT_ERROR);
		
		return new MarketBuyRqst(token, Integer.parseInt(params.get(0)));
	}

	/**
	 * This method create a {@link MarketSellRqst} to send to the server.
	 * The List of params is composed in order by:<br>
	 * -Every position of the list has the list of boundle of the same type that
	 *  the player want to sell.<br>
	 * 
	 * <p> 
	 * Every boundle is composed by:<br>
	 * -The name of the type of boundle (politiccard, permitcard, assistant)<br>
	 * -UnderScore<br>
	 * -The object of one single boundle separated with commas<br>
	 * -slash<br>
	 * -the price of the boundle<br>
	 * -underscore if the client want to sell another boundle of the same type OR<br>
	 * -white space to separated the lists of boundle<br>
	 * For politic card must write the color of the card<br>
	 * For permit card must write the number of the card<br>
	 * For assistant must write the number of the assistant <br>
	 * <p>
	 *  
	 * To execute this action the string must be:<br>
	 * MARKETSELL POLITICCARD_color,color/price_color/price ASSISTANT_number/price PERMITCARD_#card,#card/price_#card/price
	 * 
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link MarketSellRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected MarketSellRqst marketSell(List<String> params, Token token) throws IllegalInputException {
		
		Map<List<Colore>, Integer> colors = new HashMap<>();
		Map<List<String>, Integer> permitCards = new HashMap<>();
		Map<List<Integer>, Integer> assistants = new HashMap<>();
		
		if(params.size() > 3)
			throw new IllegalInputException(INPUT_ERROR);

		if(params.isEmpty())
			return new MarketSellRqst(token, colors, assistants, permitCards);

		//For each list of boundles of the same type..
		for(String string : params) {

			//Simple filter to discart all the incorrect strings
			if(string.matches("^(POLITICCARD|ASSISTANT|PERMITCARD)_.*/[0-9].*$")) {
				
				//Split the boundles
				String bundlesToSell = string.substring(0, string.indexOf("_"));
				String[] bundles = string.replaceFirst(bundlesToSell + "_", "").split("_");

				//For each boundle
				for(String bundle : bundles) {

					if(!bundle.matches("^.*/\\d+$"))
						throw new IllegalInputException(INPUT_ERROR);
					
					//Split the objects and the price
					String[] objectsWithPrice = bundle.split("/");
					String[] objectToString = objectsWithPrice[0].split(",");
					
					if(bundlesToSell.matches("^POLITICCARD$")) {
						
						colors.put(findColorsForMarket(objectToString), Integer.parseInt(objectsWithPrice[1]));

					} else if (bundlesToSell.matches("^PERMITCARD$")) {
						if(Arrays.asList(objectToString).stream().anyMatch(s -> !s.matches("\\d+")))
							throw new IllegalInputException(INPUT_ERROR);
						
						permitCards.put(Arrays.asList(objectToString), Integer.parseInt(objectsWithPrice[1]));

					} else {
						
						assistants.put(findIntegerForMarket(objectToString), Integer.parseInt(objectsWithPrice[1]));
					}
				}

			} else {
				throw new IllegalInputException(INPUT_ERROR);
			}
		}
		return new MarketSellRqst(token, colors, assistants, permitCards);
	}
	
	/**
	 * This method create a {@link EndTurnRqst} to send to the server.
	 * It no accept params<br>
	 *  
	 * To execute this action the string must be:<br>
	 * ENDTURN
	 * 
	 * @param token the client's {@link Token}
	 * @return an {@link EndTurnRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected EndTurnRqst endTurn(Token token) {
		
		return new EndTurnRqst(token);
	}
	
	/**
	 * This method create a {@link ExitRqst} to send to the server.
	 * It no accept params<br>
	 *  
	 * To execute this action the string must be:<br>
	 * EXIT
	 * 
	 * @param token the client's {@link Token}
	 * @return an {@link ExitRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected ExitRqst exit(Token token) {
		
		return new ExitRqst(token);
	}
	
	/**
	 * This method create a {@link ChangeMapRqst} to send to the server.
	 * It use by the administrator of the game (the first player) only
	 * before the game beginning.<br>
	 * 
	 * The List of params is composed in order by:<br>
	 * -the name of the map<br>
	 *  
	 * To execute this action the string must be:<br>
	 * CHANGEMAP nameofthemap
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return a {@link PrimaryActionBonusRqst}
	 * @throws IllegalInputException if the params are incorrect
	 */
	protected ChangeMapRqst changeMap(Token token, Game game, List<String> params) throws IllegalInputException {
		
		if(params.size() != 1)
			throw new IllegalInputException(INPUT_ERROR);
		if(game != null)
			throw new IllegalInputException("Impossible to change map when the Game is either init or finished");
		
		return new ChangeMapRqst(token, params.get(0).toLowerCase());
	}
	
	
	/**
	 * Load the info of the player and display it.
	 * 
	 * @param game the {@link Game}
	 */
	private void infoPlayers(Game game) {
		game.getPlayers().stream().forEach(p -> {
			StringBuilder sb = new StringBuilder();
			sb.append("PLAYER" + p.getId() + "\n").append("ASSISTANTS ");
			sb.append(p.getAssistants().size() + "\n");
			sb.append("CITIES WHERE HE HAS BUILT ");
			p.getCities().forEach(c -> sb.append(c.getName() + ", "));
			sb.append("\nPERMITCARD ");
			p.getPermitCards().forEach(sb::append);
			sb.append(p.getPermitCards().toString() + "\n");
			sb.append("POSITION IN NOBILITY TRACK ");
			sb.append(p.getMarkerDisk().getCurrentSpace());
			sb.append("VICTORYPOINTS ").append(p.getVictoryTrack().getPoints() + "\n");
			sb.append("COINS ");
			sb.append(p.getCoinsTrack().getCurrentCoins() + "\n");
			sb.append("NUMBER EMPORIUMS ");
			sb.append(p.getEmporiums().size());
			sb.append("\n\n");
			out.print(sb.toString());
		});
	}
	
	
	/**
	 * Load the string with the command info for the client.
	 * 
	 * @return the String with the command info to display to the client.
	 */
	private String loadInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Type the correct string to get the corresponding message info:\n");
		sb.append("INFOPLAYERS -> info of all the players of the game\n");
		sb.append("INFOCITIES -> info of all the cities in the game\n");
		sb.append("INFOCITYGRAPH -> info of the graph of the cities with connections\n");
		sb.append("INFOMOUNTAIN -> info of the region mountain with balcony and permit cards\n");
		sb.append("INFOCOAST -> info of the region coast with balcony and permit cards\n");
		sb.append("INFOHILL -> info of the region hill with balcony and permit cards\n");
		sb.append("INFOKINGREGION -> info of the region king with balcony and king cards bonus\n");
		sb.append("INFOKING -> info about the city where the king is\n");
		sb.append("INFOCOUNCILLORRESERVE -> info about the councillor reserve\n");
		sb.append("INFOASSISTANTRESERVE -> info about the number of assistants in reserve\n");
		sb.append("INFONOBILITYTRACK -> info about the nobility track of the game\n");
		sb.append("INFOMARKET -> info about the store\n");
		sb.append("INFOPOLITICCARD -> show yout politc cards\n");
		sb.append("INFOPERMITCARD -> show your permit cards\n");
		sb.append("INFOCOINS -> show your coins\n");
		sb.append("INFOVICTORY -> show your victory points\n");
		sb.append("INFOEMPORIUM -> show your number of emporiums to build\n");
		sb.append("INFOMYCITIES -> show the cities where you has built\n");
		sb.append("INFOCURRENTSPACE -> show the current space of your marker disk in nobility track\n");
		return sb.toString();
	}
	
	/**
	 * Load the string with the example of actions string for the client.
	 * 
	 * @return the String with the example of actions string to display to the client
	 */
	private String loadActionFormat() {
		StringBuilder sb = new StringBuilder();
		sb.append("INFORMATIONS:\n");
		sb.append("The strings with hyphens are the Request String.\n");
		sb.append("All the text without apexes is obligatory and must be write as it has been written.\n");
		sb.append("The strings are not case sensitive.\n");
		sb.append("You can put between one and four colors separated with commas.\n");
		sb.append("You can put one or more boundles separated with underscores.\n");
		sb.append("The world in the apexes must be custom by the player's choices.\n");
		sb.append("PRIMARYACTION:\n");
		sb.append("-BuildEmporiumByKingHelp 'cityWhereBuild' 'color','color','color','color'\n");
		sb.append("-BuildEmporiumByPermitCard '#playerPermitCard'\n");
		sb.append("-BuyPermitCard 'regionWhereDrawPermitCard' '#permitCardToDraw' 'color','color','color','color'\n");
		sb.append("-ElectCouncillor 'regionWhereElectCouncllor' 'ColorCouncillorToElect'\n");
		sb.append("SECONDARYACTION:\n");
		sb.append("-ChangeUsablePermitCard 'regionWhereChangeUsableCards'\n");
		sb.append("-PerformAnotherPrimaryAction\n");
		sb.append("-UseAssistantToElectCouncillor 'regionWhereElectCouncillor' 'ColorCouncillorToElect'\n");
		sb.append("-HireAssistant\n");
		sb.append("BONUS:\n");
		sb.append("-PermitCardBonus 'regionWhereDrawPermitCard' '#permitCardToDraw'\n");
		sb.append("-ReusePermitCardBonus '#playerPermitCard'\n");
		sb.append("-CoinsBonus\n");
		sb.append("-VictoryBonu\n");
		sb.append("-NobilityBonus\n");
		sb.append("-PoliticCardBonus\n");
		sb.append("-AssistantBonus\n");
		sb.append("-CityBonus 'cityWherePlayerBuilt'\n");
		sb.append("-PrimaryActionBonus 'primaryActionToExecute(see over)'\n");
		sb.append("MARKET:\n");
		sb.append("-MarketSell politiccard_'boundle'_'boundle' assistant_'boundle'_boundle' permitcard_'boundle'_boundle'\n");
		sb.append("FORMAT POLITICCARDBOUNDLE: 'colorPoliticCard','colorPoliticCard'/'price(number)'\n");
		sb.append("FORMAT ASSISTANTBOUNDLE: '#assistants'/'price(number)'\n");
		sb.append("FORMAT PERMITCARDBOUNDLE: '#permitCard','#permitCard'/'(price)'\n");
		sb.append("If you don't want to sell nothing just type marketsell.\n");
		sb.append("You can put one or more boundle for each type, and every boundle can have more objects of the same type.\n");
		sb.append("-MarketBuy '#boundle'\n");
		sb.append("You can buy only a boundle at a time\n");
		sb.append("OTHERS:\n");
		sb.append("-EndTurn\n");
		sb.append("-Exit\n");
		sb.append("-ChangeMap 'mapName'\n");
		sb.append("Only the administrator can change the map before the game begin\n");
		return sb.toString();
	}
	
	
	/**
	 * Parse the name of the colors and create a list with the corresponding 
	 * {@link Colore}.
	 * 
	 * @param colors the colors to parse
	 * @return the list of {@link Colore} parsed
	 * @throws IllegalInputException if the array contain an incorrect color
	 */
	private List<Colore> findColorsForMarket(String[] colors) throws IllegalInputException {
		List<Colore> colorsList = new ArrayList<>();
		
		for(String color : colors)
			colorsList.add(findColor(color));
		
		return colorsList;
	}
	
	
	/**
	 * Parse the integer to create a list of {@link Integer}.
	 * 
	 * @param numbers the numbers to parse to int
	 * @return the list of {@link Integer} parsed
	 * @throws IllegalInputException if the array contain an incorrect number
	 */
	private List<Integer> findIntegerForMarket(String[] numbers) throws IllegalInputException {
		List<Integer> integers = new ArrayList<>();
		
		for(String integer : numbers)
			integers.add(parseToNumber(integer));
		
		return integers;
	}
	
	
	/**
	 * Control if the primary action selected is correct and than return
	 * the corresponding request message.
	 * 
	 * @param game the {@link Game}
	 * @param token the client's {@link Token}
	 * @param params the array of params to parse
	 * @return the {@link RequestMessage} of the primary action selected
	 * @throws IllegalInputException if the params are incorrect
	 */
	private RequestMessage findPrimaryAction(Game game, Token token, List<String> params) throws IllegalInputException {
		
		if(!params.get(0).matches("^(BUILDEMPORIUMBYKINGHELP|BUILDEMPORIUMBYPERMITCARD|BUYPERMITCARD|ELECTCOUNCILLOR)$")) 
			throw new IllegalInputException("You have to select one primary action.");
		
		List<String> actionParams = new ArrayList<>();
		for(int i=1; i<params.size(); i++)
			actionParams.add(i-1, params.get(i)); 
			
		return  parser.get(params.get(0)).apply(actionParams, token, game);
	}
	
	
	/**
	 * Search the city in the list of cities and return the city with
	 * the name passed as param or throw and IllegalInputException.
	 * 
	 * @param cityToParse the name of the city to parse
	 * @param game the {@link Game}
	 * @return the {@link City} parsed
	 * @throws IllegalInputException if the city is incorrect
	 */
	private City findCity(String cityToParse, Game game) throws IllegalInputException {

		return game.getBoard().getCities().stream()
						.filter(c -> cityToParse.equals(c.getName()))
						.findFirst()
						.orElseThrow(() -> new IllegalInputException("The selected city doesn't exist."));
	}
	
	
	/**
	 * Control if the string is a number and parse it or throw an
	 * IllegalInputException.
	 * 
	 * @param integerToParse the integer to parse
	 * @return the corresponding int
	 * @throws IllegalInputException if the string is not a number
	 */
	private int parseToNumber(String integerToParse) throws IllegalInputException {
		
		if(integerToParse.matches("\\d+"))
			return Integer.parseInt(integerToParse);
		
		throw new IllegalInputException("You have to write a legal permit card id, it must be a number.");
	}
	

	/**
	 * Search the normal region in {@link RegionType} and return the normalregion
	 * with the same type or throw and IllegalInputException.
	 * 
	 * @param regionToParse the normal region to parse
	 * @param game the {@link Game}
	 * @return the the {@link NormalRegion} parsed
	 * @throws IllegalInputException if the normal region is incorrect
	 */
	private NormalRegion findNormalRegion(String regionToParse, Game game) throws IllegalInputException {
		
		RegionType type =Arrays.asList(RegionType.values()).stream()
								.filter(r -> r.name().equals(regionToParse))
								.findFirst()
								.orElseThrow(() -> new IllegalInputException("You have inserted a region that doesn't exist."));
		
		return game.getBoard().getRegions(type);
	}
	
	
	/**
	 * Control if is a king region or search the normal region.
	 * Throw an exception if the region is incorrect.
	 * 
	 * @param regionToParse the region to parse
	 * @param game the {@link Game}
	 * @return the {@link Region} parsed
	 * @throws IllegalInputException if the region is incorrect
	 */
	private Region findRegion(String regionToParse, Game game) throws IllegalInputException {
		if("KING".equals(regionToParse))
			return game.getBoard().getKingRegion();
		else
			return findNormalRegion(regionToParse, game);
	}
	

	/**
	 * Search the color in {@link Colore} and return the color
	 * passed as param or throw and IllegalInputException.
	 * 
	 * @param coloreToParse the colors to parse
	 * @return the {@link Colore} parsed
	 * @throws IllegalInputException if the color is incorrect
	 */
	private Colore findColor(String coloreToParse) throws IllegalInputException {
		
		return Arrays.asList(Colore.values()).stream()
					.filter(c -> c.toString().equals(coloreToParse))
					.findFirst()
					.orElseThrow(() -> new IllegalInputException("One or more of selected colors don't exist."));
	}
	
	
	/**
	 * Search the name of the bonus in the bonus to execute and
	 * return the corresponding bonus or throw an IllegalInputException.
	 * 
	 * @param bonusToString the name of the bonus to execute
	 * @return the {@link Bonus} to execute
	 * @throws IllegalInputException if the bonus is incorrect
	 */
	private Bonus findBonus(String bonusToString) throws IllegalInputException {
		
		return bonusToExecute.stream()
					.filter(b -> b.getName().equalsIgnoreCase(bonusToString))
					.findFirst()
					.orElseThrow(() -> new IllegalInputException("You don't won a " + bonusToString));
	}

	
	/**
	 * @return the list of bonus to execute by the client
	 */
	public List<Bonus> getBonusToExecute() {
		return bonusToExecute;
	}

}
