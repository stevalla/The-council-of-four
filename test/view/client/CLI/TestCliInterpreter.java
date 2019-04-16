package it.polimi.ingsw.cg32.view.client.CLI;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.googlecode.zohhak.api.TestWith;
import com.googlecode.zohhak.api.runners.ZohhakRunner;

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
import it.polimi.ingsw.cg32.model.bonus.SingleBonus;
import it.polimi.ingsw.cg32.model.color.RegionType;
import it.polimi.ingsw.cg32.model.game.Game;
import it.polimi.ingsw.cg32.utilities.exception.IllegalInputException;
import it.polimi.ingsw.cg32.view.client.cli.CliInterpreter;
import it.polimi.ingsw.cg32.view.server.Token;

@RunWith(ZohhakRunner.class)
public class TestCliInterpreter {

	CliInterpreter interpreter;
	private Game game;
	private Token token;
	
	@Before
	public void initParams() {
		interpreter = new CliInterpreter(new ArrayList<>());
		game = new Game("src/main/resources/map/default.txt", 3);
		token = new Token();
		token.setPlayerNumber(0);
	}
	
	@Rule
	public ExpectedException exprected = ExpectedException.none();
	@Test
	public void testParseMessageWhenGameIsNullAndStringIsNotExitShouldThrowAnIllegalInputExceptionWithStringTheMatchIsntAlreadyBegin() {
		try {
			interpreter.parserMessage("HIREASSISTANT", null, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("The match isn't already begin.");
		} catch (IllegalInputException e) {
		}

	}
	
	@Test
	public void testParseMessageWithStringExitWhenTheGameIsNullShouldReturnnAnExitRqst() throws IllegalInputException {
		RequestMessage request = interpreter.parserMessage("EXIT", null, token);
		assertThat(request, is(instanceOf(ExitRqst.class)));
	}
	
	@Test
	public void testParseMessageWithARandomStringShouldReturnAnIllegalInputExceptionWithMessageWrongAction() {
		
		try {
			interpreter.parserMessage("sbauicsahubjsnaokm", game, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("Wrong action");
		} catch (IllegalInputException e) {
		}
	}
	
	@Test
	public void testParseMessageWhenStringHasALotOfSpacesButIsCorrectShouldGoCorrectly() throws IllegalInputException {
		RequestMessage request = interpreter.parserMessage("  EXIT     ", game, token);
		assertThat(request, is(instanceOf(ExitRqst.class)));
	}
	
	/*
	 * The String should be "BUILDEMPORIUMBYKINGHELP CITYNAME COLOR1,COLOR2,..."
	 * Where -CITYNAME must be a valid name of a game's cities
	 * 		 -COLORS must be the colors of the politic cards that the players played
	 */
	@TestWith(value = {
		"BUILDEMPORIUMBYKINGHELP",
		"BUILDEMPORIUMBYKINGHELP LYRAM",
		"BUILDEMPORIUMBYKINGHELP ",
		"BUILDEMPORIUMBYKINGHELP CITY",
		"BUILDEMPORIUMBYKINGHELP LYRAM ",
		"BUILDEMPORIUMBYKINGHELP ARKON COLOR",
		"BUILDEMPORIUMBYKINGHELP CITY BLACK,PURPLE",
		"BUILDEMPORIUMBYKINGHELP BLACK JUVELAR",
		"BUILDEMPORIUMBYKINGHELP JUVELAR. BLACK,PURPLE",
		"BUILDEMPORIUMBYKINGHELPJUVELARBLACK",
		"BUILDEMPORIUMBYKINGHELP JUVELARBLACK",
		"BUILDEMPORIUMBYKINGHELP ARKON",
		"BUILDEMPORIUMBYKINGHELP ARKON BLACK,PURPL",
		"BUILDEMPORIUMBYPERMITCARD ARKON BLACK,PURPLE",
		"ELECTCOUNCILLOR ARKON BLACK,PURPLE",
		"PERMITCARDBONUS ARKON BLACK"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectBuildEmporiumByKingHelpShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"BUILDEMPORIUMBYKINGHELP LYRAM BLACK",
			"BUILDEMPORIUMBYKINGHELP JUVELAR BLACK,PURPLE,WHITE",
			"BUILDEMPORIUMBYKINGHELP   ARKON   WHITE,LIGHTBLUE, JOLLY,PINK",
			"   BUILDEMPORIUMBYKINGHELP ARKON WHITE ,LIGHTBLUE   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectBuildEmporiumByKingHelpShouldReturnABuildEmporiumByKingHelpRqst(String correctInput) throws IllegalInputException {
		
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(BuildEmporiumByKingHelpRqst.class)));
	}
	
	
	/*
	 * The String should be "BUILDEMPORIUMBYPERMITCARD CITYNAME PERMITCARDID"
	 * Where -CITYNAME must be a valid name of a game's cities
	 * 		 -PERMITCARDID must be a number
	 */
	@TestWith(value = {
		"BUILDEMPORIUMBYPERMITCARD",
		"BUILDEMPORIUMBYPERMITCARD LYRAM",
		"BUILDEMPORIUMBYPERMITCARD ",
		"BUILDEMPORIUMBYPERMITCARD CITY",
		"BUILDEMPORIUMBYPERMITCARD LYRAM ",
		"BUILDEMPORIUMBYPERMITCARD ARKON ID",
		"BUILDEMPORIUMBYPERMITCARD CITY 2",
		"BUILDEMPORIUMBYPERMITCARD ARKON BLACK,PURPLE",
		"BUILDEMPORIUMBYPERMITCARD ARKON 12D",
		"BUILDEMPORIUMBYPERMITCARD ARKON 1 2",
		"BUILDEMPORIUMBYPERMITCARDJUVELARBLACK",
		"BUILDEMPORIUMBYPERMITCARD JUVELARBLACK",
		"BUILDEMPORIUMBYPERMITCARD ARKON 12,23",
		"ELECTCOUNCILLOR ARKON 12",
		"PERMITCARDBONUS ARKON 13",
		"BUILDEMPORIUMBYKINGHELP JUVELAR 12"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectBuildEmporiumByPermitCardShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"BUILDEMPORIUMBYPERMITCARD LYRAM 12",
			"BUILDEMPORIUMBYPERMITCARD JUVELAR    13   ",
			"   BUILDEMPORIUMBYPERMITCARD   ARKON   1   ",
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectBuildEmporiumByPermitCardShouldReturnABuildEmporiumByPermitCardRqst(String correctInput) throws IllegalInputException {
		
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(BuildEmporiumByPermitCardRqst.class)));
	}
	
	/*
	 * The String should be "BUYPERMITCARD REGION PERMITCARDID COLOR1,COLOR2,..."
	 * Where -REGION the normalregion where buy the permit card
	 * 		 -PERMITCARDID must be a number
	 * 		 -COLORS must be the colors of the politic cards that the players played
	 */
	@TestWith(value = {
		"BUYPERMITCARD",
		"BUYPERMITCARD HILL",
		"BUYPERMITCARD ",
		"BUYPERMITCARD REGION",
		"BUYPERMITCARD HILL ",
		"BUYPERMITCARD COAST 2D",
		"BUYPERMITCARD HILLR 2",
		"BUYPERMITCARD COAST VENTI",
		"BUYPERMITCARD COAST 2,23",
		"BUYPERMITCARD COAST,MOUNTAIN 2D",
		"BUYPERMITCARD COAST MOUNTAIN 12",
		"BUYPERMITCARD 12 HILL",
		"BUYPERMITCARD COAST 2 3",
		"BUYPERMITCARDCOAST12",
		"BUYPERMITCARD COAST2",
		"BUYPERMITCARD KING 2 WHITE",
		"BUYPERMITCARD COAST 2D BLACK,PURPLE",
		"BUYPERMITCARD COAST 2 PURP,WHITE",
		"BUYPERMITCARD COAST 13 WHITE.PURPLE",
		"BUYPERMITCARD COAST WHITE 12",
		"BUYPERMITCARD WHITE,LIGHTBLUE HILL 12",
		"BUYPERMITCARD COAST 2 YELLOW"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectBuyPermitCardShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"BUYPERMITCARD COAST 2 PURPLE,BLACK",
			"BUYPERMITCARD  COAST    223 WHITE,JOLLY,PINK,PINK",
			"  BUYPERMITCARD   COAST     1    PURPLE,   JOLLY   ,   ORANGE",
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectBuyPermitCardShouldReturnABuyPermitCardRqst(String correctInput) throws IllegalInputException {
		
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(BuyPermitCardRqst.class)));
	}
	
	
	/*
	 * The String should be "ELECTCOUNCILLOR REGION COLOR"
	 * Where -REGION the normalregion where buy the permit card
	 * 		 -COLOR the color of the councillor to elect (only one)
	 */
	@TestWith(value = {
		"ELECTCOUNCILLOR",
		"ELECTCOUNCILLOR HILL",
		"ELECTCOUNCILLOR ",
		"ELECTCOUNCILLOR HILL ",
		"ELECTCOUNCILLOR REGION BLACK",
		"ELECTCOUNCILLOR KING PURPLE.",
		"ELECTCOUNCILLOR MOUNTAIN COLOR",
		"ELECTCOUNCILLOR BLACK HILL",
		"ELECTCOUNCILLOR ARKON BLACK",
		"ELECTCOUNCILLORCOASTBLACK",
		"ELECTCOUNCILLOR COASTBLACK",
		"ELECTCOUNCILLOR 12 BLACK",
		"ELECTCOUNCILLOR HILL 12",
		"ELECTCOUNCILLOR HILL BLACK,PURPLE WHITE",
		"BUILDEMPORIUMBYPERMITCARD KING WHITE",
		"PERMITCARDBONUS KING WHITE",
		"BUILDEMPORIUMBYKINGHELP KING WHITE"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectElectCouncillorShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"ELECTCOUNCILLOR KING PURPLE",
			"ELECTCOUNCILLOR COAST PURPLE",
			"   ELECTCOUNCILLOR MOUNTAIN   PURPLE  "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectElectCouncillorShouldReturnAnElectCouncillorRqst(String correctInput) throws IllegalInputException {
		
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(ElectCouncillorRqst.class)));
	}
	
	/*
	 * The String should be "CHANGEUSABLEPERMITCARD REGION"
	 * Where -REGION the normalregion where buy the permit card
	 */
	@TestWith(value = {
		"CHANGEUSABLEPERMITCARD",
		"CHANGEUSABLEPERMITCARD ",
		"CHANGEUSABLEPERMITCARD KING",
		"CHANGEUSABLEPERMITCARD HILL BLACK",
		"CHANGEUSABLEPERMITCARD MOUTAINS",
		"CHANGEUSABLEPERMITCARD REGION",
		"CHANGEUSABLEPERMITCARD 12",
		"CHANGEUSABLEPERMITCARD BLACK",
		"CHANGEUSABLEPERMITCARD ARKON",
		"CHANGEUSABLEPERMITCARD COAST. ",
		"CHANGEUSABLEPERMITCARDCOAST",
		"CITYBONUS HILL",
		"MARKETBUY COAST"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectChangeUsablePermitCardShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"CHANGEUSABLEPERMITCARD COAST",
			"CHANGEUSABLEPERMITCARD HILL",
			"    CHANGEUSABLEPERMITCARD    MOUNTAIN    "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectChangeUsablePermitCardShouldReturnAChangeUsablePermitCardRqst(String correctInput) throws IllegalInputException {
		
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(ChangeUsablePermitCardRqst.class)));
	}
	
	
	/*
	 * The String should be "PERFORMANOTHERPRIMARYACTION"
	 */
	@TestWith(value = {
		"PERFORMANOTHERPRIMARYACTION PROVAERROR",
		"PERFORMANOTHERPRIMARYACTION COAST",
		"PERFORMANOTHERPRIMARYACTION BLACK",
		"PERFORMANOTHERPRIMARYACTIONBLACK"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectPerformAnotherPrimaryActionShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"PERFORMANOTHERPRIMARYACTION",
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectPerformAnotherPrimaryActionShouldReturnAPerformAnotherPrimaryActionRqst(String correctInput) throws IllegalInputException {
		
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(PerformAnotherPrimaryActionRqst.class)));
	}
	
	
	/*
	 * The String should be "USEASSISTANTTOELECTCOUNCILLOR REGION COLOR"
	 * Where -REGION the normalregion where buy the permit card
	 * 		 -COLOR the color of the councillor to elect (only one)
	 */
	@TestWith(value = {
		"USEASSISTANTTOELECTCOUNCILLOR",
		"USEASSISTANTTOELECTCOUNCILLOR HILL",
		"USEASSISTANTTOELECTCOUNCILLOR ",
		"USEASSISTANTTOELECTCOUNCILLOR HILL ",
		"USEASSISTANTTOELECTCOUNCILLOR REGION BLACK",
		"USEASSISTANTTOELECTCOUNCILLOR KING PURPLE.",
		"USEASSISTANTTOELECTCOUNCILLOR MOUNTAIN COLOR",
		"USEASSISTANTTOELECTCOUNCILLOR BLACK HILL",
		"USEASSISTANTTOELECTCOUNCILLOR ARKON BLACK",
		"USEASSISTANTTOELECTCOUNCILLORCOASTBLACK",
		"USEASSISTANTTOELECTCOUNCILLOR COASTBLACK",
		"USEASSISTANTTOELECTCOUNCILLOR 12 BLACK",
		"USEASSISTANTTOELECTCOUNCILLOR HILL 12",
		"USEASSISTANTTOELECTCOUNCILLOR HILL BLACK,PURPLE WHITE",
		"BUILDEMPORIUMBYPERMITCARD KING WHITE",
		"PERMITCARDBONUS KING WHITE",
		"BUILDEMPORIUMBYKINGHELP KING WHITE"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectUseAssistantToElectCouncillorShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"USEASSISTANTTOELECTCOUNCILLOR KING PURPLE",
			"USEASSISTANTTOELECTCOUNCILLOR COAST PURPLE",
			"   USEASSISTANTTOELECTCOUNCILLOR MOUNTAIN   PURPLE  "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectUseAssistantToElectCouncillorShouldReturnAnUseAssistantToElectCouncillorRqst(String correctInput) throws IllegalInputException {
		
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(UseAssistantToElectCouncillorRqst.class)));
	}
	
	
	/*
	 * The String should be "HIREASSISTANT"
	 */
	@TestWith(value = {
		"HIREASSISTANT PROVAERROR",
		"HIREASSISTANT COAST",
		"HIREASSISTANT BLACK",
		"HIREASSISTANTBLACK"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectHireAssistantShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"HIREASSISTANT",
			"   HIREASSISTANT   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectHireAssistantShouldReturnAHireAssistantRqst(String correctInput) throws IllegalInputException {
		
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(HireAssistantRqst.class)));
	}
	
	
	/*
	 * The String should be "PERMITCARDBONUS REGION PERMITCARDID"
	 * Where -REGION the normalregion where buy the permit card
	 * 		 -PERMITCARDID must be a number
	 */
	@TestWith(value = {
		"PERMITCARDBONUS",
		"PERMITCARDBONUS HILL",
		"PERMITCARDBONUS ",
		"PERMITCARDBONUS REGION",
		"PERMITCARDBONUS HILL ",
		"PERMITCARDBONUS COAST 2D",
		"PERMITCARDBONUS HILLR 2",
		"PERMITCARDBONUS COAST VENTI",
		"PERMITCARDBONUS COAST 2,23",
		"PERMITCARDBONUS COAST,MOUNTAIN 2D",
		"PERMITCARDBONUS COAST MOUNTAIN 12",
		"PERMITCARDBONUS 12 HILL",
		"PERMITCARDBONUS COAST 2 3",
		"PERMITCARDBONUSCOAST12",
		"PERMITCARDBONUS KING 2",
		"PERMITCARDBONUS COAST2",
		"BUILDEMPORIUMBYPERMITCARD COAST 2",
		"BUILDEMPORIUMBYKINGHELP COAST 2",
		"ELECTCOUNCILLOR COAST 2",
		"USEASSISTANTTOELECTCOUNCILLOR HILL 12"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectPermitCardBonusToExecuteShouldThrowAnIllegalInputException(String errorInput) {
			interpreter.getBonusToExecute().add(new SingleBonus("PERMITCARDBONUS"));
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"PERMITCARDBONUS COAST 2",
			"PERMITCARDBONUS HILL    2",
			"   PERMITCARDBONUS    MOUNTAIN    2    ",
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectPermitCardBonusToExecuteShouldReturnAPermitCardBonusRqst(String correctInput) throws IllegalInputException {
		interpreter.getBonusToExecute().add(new SingleBonus("PERMITCARDBONUS"));
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(PermitCardBonusRqst.class)));
	}
	
	@TestWith(value = {
			"PERMITCARDBONUS COAST 2",
			"PERMITCARDBONUS HILL    2",
			"   PERMITCARDBONUS    MOUNTAIN    2    ",
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectPermitCardBonusNotToExecuteShouldReturnAnIllegalInputExceptionWithStringYouDontWonThisAPermitCardBonus(String correctInput)  {
		try {
			interpreter.parserMessage(correctInput, game, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("You don't won a PERMITCARDBONUS");
		} catch (IllegalInputException e) {
		}
	}
	
	/*
	 * The String should be "REUSEPERMITCARDBONUS PERMITCARDID"
	 * Where -PERMITCARDID must be a number
	 */
	@TestWith(value = {
		"REUSEPERMITCARDBONUS",
		"REUSEPERMITCARDBONUS 12d",
		"REUSEPERMITCARDBONUS ",
		"REUSEPERMITCARDBONUS BLACK",
		"REUSEPERMITCARDBONUS COAST 12",
		"REUSEPERMITCARDBONUS 2 MOUNTAIN",
		"REUSEPERMITCARDBONUS VENTI",
		"CHANGEUSABLEPERMITCARD 12",
		"REUSEPERMITCARDBONUS 12,13",
		"CITYBONUS 1"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectReusePermitCardBonusToExecuteShouldThrowAnIllegalInputException(String errorInput) {
			interpreter.getBonusToExecute().add(new SingleBonus("REUSEPERMITCARDBONUS"));
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"REUSEPERMITCARDBONUS 12",
			"  REUSEPERMITCARDBONUS      14  "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectReusePermitCardBonusToExecuteShouldReturnAReusePermitCardBonusRqst(String correctInput) throws IllegalInputException {
		interpreter.getBonusToExecute().add(new SingleBonus("REUSEPERMITCARDBONUS"));
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(ReusePermitCardBonusRqst.class)));
	}
	
	@TestWith(value = {
			"REUSEPERMITCARDBONUS 12",
			"  REUSEPERMITCARDBONUS      14  "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectReusePermitCardBonusNotToExecuteShouldReturnAnIllegalInputExceptionWithStringYouDontWonThisAReusePermitCardBonus(String correctInput)  {
		try {
			interpreter.parserMessage(correctInput, game, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("You don't won a REUSEPERMITCARDBONUS");
		} catch (IllegalInputException e) {
		}
	}
	
	
	/*
	 * The String should be "CITYBONUS CITYNAME"
	 * Where -CITYNAME must be a valid name of a game's cities
	 */
	@TestWith(value = {
		"CITYBONUS",
		"CITYBONUS ",
		"CITYBONUS ERRORNAME",
		"CITYBONUS COAST",
		"CITYBONUS BLACK",
		"CITYBONUS ARKON BLACK",
		"CITYBONUSARKON",
		"CITYBONUS 12",
		"CITYBONUS ARKON,JUVELAR",
		"CITYBONUS ARKON JUVELAR",
		"CHANGEUSABLEPERMITCARD ARKON",
		"REUSEPERMITCARDBONUS ARKON"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectCityBonusToExecuteShouldThrowAnIllegalInputException(String errorInput) {
			interpreter.getBonusToExecute().add(new SingleBonus("CITYBONUS"));
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"CITYBONUS ARKON",
			"  CITYBONUS      JUVELAR   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectCityBonusToExecuteShouldReturnACityBonusRqst(String correctInput) throws IllegalInputException {
		interpreter.getBonusToExecute().add(new SingleBonus("CITYBONUS"));
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(CityBonusRqst.class)));
	}
	
	@TestWith(value = {
			"CITYBONUS ARKON",
			"  CITYBONUS      JUVELAR   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectCityBonusNotToExecuteShouldReturnAnIllegalInputExceptionWithStringYouDontWonThisACityBonus(String correctInput)  {
		try {
			interpreter.parserMessage(correctInput, game, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("You don't won a CITYBONUS");
		} catch (IllegalInputException e) {
		}
	}
	
	
	/*
	 * The String should be "PRIMARYACTIONBONUS PRIMARYACTION"
	 * Where -PRIMARYACTION the primary action to execute
	 */
	@TestWith(value = {
		"PRIMARYACTIONBONUS",
		"PRIMARYACTIONBONUS ",
		"PRIMARYACTIONBONUS ACTIONERROR",
		"PRIMARYACTIONBONUS HIREASSISTANT",
		"PRIMARYACTIONBONUS CHANGEUSABLEPERMITCARD",
		"PRIMARYACTIONBONUS USEASSISTANTTOELECTCOUNCILLOR",
		"PRIMARYACTIONBONUS PERFORMANOTHERPRIMARYACTION",
		"PRIMARYACTIONBONUS 12",
		"PRIMARYACTIONBONUS ELECTCOUNCILLOR BUILDEMPORIUMBYKINGHELP",
		"PRIMARYACTIONBONUS ELECTCOUNCILLOR COAST LIGHTBLUE BUILDEMPORIUMBYKINGHELP LYRAM BLACK,WHITE",
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectPrimaryActionBonusToExecuteShouldThrowAnIllegalInputException(String errorInput) {
			interpreter.getBonusToExecute().add(new SingleBonus("CITYBONUS"));
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"PRIMARYACTIONBONUS ELECTCOUNCILLOR HILL WHITE",
			" PRIMARYACTIONBONUS  BUILDEMPORIUMBYKINGHELP LYRAM WHITE",
			"   PRIMARYACTIONBONUS    BUILDEMPORIUMBYPERMITCARD LYRAM   14",
			"  PRIMARYACTIONBONUS     BUYPERMITCARD  MOUNTAIN 19 WHITE,BLACK"
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectPrimaryActionBonusToExecuteShouldReturnAPrimaryActionBonusRqst(String correctInput) throws IllegalInputException {
		interpreter.getBonusToExecute().add(new SingleBonus("PRIMARYACTIONBONUS"));
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(PrimaryActionBonusRqst.class)));
	}
	
	@TestWith(value = {
			"PRIMARYACTIONBONUS ELECTCOUNCILLOR HILL WHITE",
			" PRIMARYACTIONBONUS  BUILDEMPORIUMBYKINGHELP LYRAM WHITE",
			"   PRIMARYACTIONBONUS    BUILDEMPORIUMBYPERMITCARD COAST   14",
			"  PRIMARYACTIONBONUS     BUYPERMITCARD  MOUNTAIN 19    WHITE,BLACK"
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectPrimaryActionBonusNotToExecuteShouldReturnAnIllegalInputExceptionWithStringYouDontWonThisAPrimaryActionBonus(String correctInput)  {
		try {
			interpreter.parserMessage(correctInput, game, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("You don't won a PRIMARYACTIONBONUS");
		} catch (IllegalInputException e) {
		}
	}
	
	
	/*
	 * The String should be "ASSISTANTBONUS"
	 */
	@TestWith(value = {
		"ASSISTANTBONUS PROVAERROR",
		"ASSISTANTBONUS COAST",
		"ASSISTANTBONUS BLACK",
		"ASSISTANTBONUSBLACK"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectAssistantBonusShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"ASSISTANTBONUS",
			"   ASSISTANTBONUS   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectAssistantBonusToExecuteShouldReturnAnAssistantBonusRqst(String correctInput) throws IllegalInputException {
		interpreter.getBonusToExecute().add(new QuantityBonus("ASSISTANTBONUS", 4));
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(AssistantBonusRqst.class)));
	}
	
	@TestWith(value = {
			"ASSISTANTBONUS",
			"    ASSISTANTBONUS    "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectAssistantBonusNotToExecuteShouldReturnAnIllegalInputExceptionWithStringYouDontWonThisAnAssistantBonus(String correctInput)  {
		try {
			interpreter.parserMessage(correctInput, game, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("You don't won a ASSISTANTBONUS");
		} catch (IllegalInputException e) {
		}
	}
	
	
	/*
	 * The String should be "COINSBONUS"
	 */
	@TestWith(value = {
		"COINSBONUS PROVAERROR",
		"COINSBONUS COAST",
		"COINSBONUS BLACK",
		"COINSBONUSBLACK"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectCoinsBonusShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"COINSBONUS",
			"   COINSBONUS   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectCoinsBonusToExecuteShouldReturnACoinsBonusRqst(String correctInput) throws IllegalInputException {
		interpreter.getBonusToExecute().add(new QuantityBonus("COINSBONUS", 4));
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(CoinsBonusRqst.class)));
	}
	
	@TestWith(value = {
			"COINSBONUS",
			"   COINSBONUS   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectCoinsBonusNotToExecuteShouldReturnAnIllegalInputExceptionWithStringYouDontWonThisACoinsBonus(String correctInput)  {
		try {
			interpreter.parserMessage(correctInput, game, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("You don't won a COINSBONUS");
		} catch (IllegalInputException e) {
		}
	}
	
	
	
	/*
	 * The String should be "POLITICCARDBONUS"
	 */
	@TestWith(value = {
		"POLITICCARDBONUS PROVAERROR",
		"POLITICCARDBONUS COAST",
		"POLITICCARDBONUS BLACK",
		"POLITICCARDBONUSBLACK"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectPoliticCardBonusShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"POLITICCARDBONUS",
			"   POLITICCARDBONUS   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectPoliticCardBonusToExecuteShouldReturnAPoliticCardBonusRqst(String correctInput) throws IllegalInputException {
		interpreter.getBonusToExecute().add(new QuantityBonus("POLITICCARDBONUS", 4));
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(PoliticCardBonusRqst.class)));
	}
	
	@TestWith(value = {
			"POLITICCARDBONUS",
			"   POLITICCARDBONUS   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectPoliticCardBonusNotToExecuteShouldReturnAnIllegalInputExceptionWithStringYouDontWonThisAPoliticCardBonus(String correctInput)  {
		try {
			interpreter.parserMessage(correctInput, game, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("You don't won a POLITICCARDBONUS");
		} catch (IllegalInputException e) {
		}
	}
	
	
	/*
	 * The String should be "VICTORYBONUS"
	 */
	@TestWith(value = {
		"VICTORYBONUS PROVAERROR",
		"VICTORYBONUS COAST",
		"VICTORYBONUS BLACK",
		"VICTORYBONUSBLACK"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectVictoryBonusShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"VICTORYBONUS",
			"   VICTORYBONUS   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectVictoryBonusToExecuteShouldReturnAVictoryBonusRqst(String correctInput) throws IllegalInputException {
		interpreter.getBonusToExecute().add(new QuantityBonus("VICTORYBONUS", 4));
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(VictoryBonusRqst.class)));
	}
	
	@TestWith(value = {
			"VICTORYBONUS",
			"    VICTORYBONUS   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectVictoryBonusNotToExecuteShouldReturnAnIllegalInputExceptionWithStringYouDontWonThisAVictoryBonus(String correctInput)  {
		try {
			interpreter.parserMessage(correctInput, game, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("You don't won a VICTORYBONUS");
		} catch (IllegalInputException e) {
		}
	}
	
	
	/*
	 * The String should be "NOBILITYBONUS"
	 */
	@TestWith(value = {
		"NOBILITYBONUS PROVAERROR",
		"NOBILITYBONUS COAST",
		"NOBILITYBONUS BLACK",
		"NOBILITYBONUSBLACK"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectNobilityBonusShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"NOBILITYBONUS",
			"   NOBILITYBONUS   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectNobilityBonusToExecuteShouldReturnANobilityBonusRqst(String correctInput) throws IllegalInputException {
		interpreter.getBonusToExecute().add(new SingleBonus("NOBILITYBONUS"));
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(NobilityBonusRqst.class)));
	}
	
	@TestWith(value = {
			"NOBILITYBONUS",
			"   NOBILITYBONUS   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectNobilityBonusNotToExecuteShouldReturnAnIllegalInputExceptionWithStringYouDontWonThisANobilityBonus(String correctInput)  {
		try {
			interpreter.parserMessage(correctInput, game, token);
			exprected.expect(IllegalInputException.class);
			exprected.expectMessage("You don't won a NOBILITYBONUS");
		} catch (IllegalInputException e) {
		}
	}
	
	
	/*
	 * The String should be "MARKETBUY NUMBOUNDLE"
	 * Where: -NUMBOUNDLE is the number of the boundle to buy
	 */
	@TestWith(value = {
		"MARKETBUY PROVAERROR",
		"MARKETBUY 12d",
		"MARKETBUY BLACK 12",
		"MARKETBUY 12,14",
		"MARKETBUY 12.14",
		"MARKETBUY12",
		"CITYBONUS 12",
		"CHANGEUSABLEPERMITCARD 14",
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectMarketBuyShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"MARKETBUY 12",
			"    MARKETBUY     143    "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectMarketBuyToExecuteShouldReturnAMarketBuyRqst(String correctInput) throws IllegalInputException {
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(MarketBuyRqst.class)));
	}
	
	
	/*
	 * The String should be "MARKETSELL POLITICCARD_COLOR1,COLOR2,../COST ASSISTANT_NUMASS/COST PERMITCARD_NUMPERMIT,NUMPERMIT,.../COST"
	 * Where: -COLORS the colors of the politic cards to sell
	 * 		  -NUMASS the number of assistant to sell
	 * 		  -NUMPERMIT the id of the permit card to sell
	 * 		  -COST the price of the boundle to sell
	 * 
	 * For every component its possible write more boundles separates them with a "_"
	 */
	@TestWith(value = {
		"MARKETSELL WRONG",
		"MARKETSELL POLITICCARD ",
		"MARKETSELL POLITICCARD_ARKON/2",
		"MARKETSELL POLITICCARD_WHITE,PURPLE/ARKON",
		"MARKETSELL POLITICCARD__WHITE/12",
		"MARKETSELL POLITICCARD_WHITE/6_PURPLE,BLACK,ARKON/8",
		"MARKETSELL POLITICCARD_12/16_BLACK/8",
		"MARKETSELL POLITICCARD_BLACK,WHITE/12 BLACK/3",
		"MARKETSELL ASSISTANT ",
		"MARKETSELL ASSISTANT_21/ARKON",
		"MARKETSELL ASSISTANT_ARKON/21",
		"MARKETSELL ASSISTANT 12/3_4/BLACK",
		"MARKETSELL ASSISTANT_12/3 3/6",
		"MARKETSELL PERMITCARD ",
		"MARKETSELL PERMITCARD_12/COAST",
		"MARKETSELL PERMITCARD_12/16_COAST/6",
		"MARKETSELL PERMITCARD_ARKON/1_16/1",
		"MARKETSELL PERMITCARD_17,7/4 18/8",
		"MARKETSELL POLITICCARD_BLACK,WHITE/8 ASSISTANT_4/3 PERMITCARD 8/7",
		"MARKETSELL POLITICCARD_BLACK,8/8 ASSISTANT_4/3 PERMITCARD_8/7",
		"MARKETSELL POLITICCARD_BLACK,WHITE/8_ASSISTANT_4/3 PERMITCARD_8/7",
		"MARKETSELL POLITICCARD_BLACK,WHITE/8_LIGHTBLUE/8 ASSISTANT_COAST/3 PERMITCARD_8/7",
		"MARKETSELL POLITICCARD_BLACK,WHITE/ARKON PERMITCARD_8/7",
		"MARKETSELL POLITICCARD_BLACK,WHITE/8 ASSISTANT_4/3 PERMITCARD_7.8/7",
		"MARKETSELL POLITICCARD_BLACK,ARKON/8 ASSISTANT_4/3",
		"MARKETSELL ASSISTANT_4/3_PERMITCARD_8/7",
		"MARKETSELL POLITICCARD_BLACK/BLACK",
		"MARKETSELL ASSISTANT_COAST/3"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectMarketSellShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"MARKETSELL",
			"MARKETSELL POLITICCARD_BLACK,WHITE/8 ASSISTANT_4/3 PERMITCARD_8/7",
			"MARKETSELL ASSISTANT_4/3 PERMITCARD_8/7",
			"MARKETSELL POLITICCARD_BLACK,WHITE/8 ASSISTANT_4/3",
			"MARKETSELL POLITICCARD_BLACK,WHITE/8 PERMITCARD_8/7",
			"MARKETSELL POLITICCARD_BLACK,WHITE/8_BLACK,PURPLE,LIGHTBLUE,PINK,PINK/18 ASSISTANT_4/3_6/8 PERMITCARD_8/7_18,19/20"
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectMarketSellToExecuteShouldReturnAMarketSellRqst(String correctInput) throws IllegalInputException {
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(MarketSellRqst.class)));
	}
	
	
	/*
	 * The String should be "ENDTURN"
	 */
	@TestWith(value = {
		"ENDTURN PROVAERROR",
		"ENDTURN COAST",
		"ENDTURN BLACK",
		"ENDTURNBLACK"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectEndTurnShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"ENDTURN",
			"   ENDTURN    "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectEndTurnShouldReturnAnEndTurnRqst(String correctInput) throws IllegalInputException {
		
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(EndTurnRqst.class)));
	}
	
	
	/*
	 * The String should be "EXIT"
	 */
	@TestWith(value = {
		"EXIT PROVAERROR",
		"EXIT COAST",
		"EXIT BLACK",
		"EXITBLACK"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectExitShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"EXIT",
			"   EXIT    "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectExitShouldReturnAnExitRqst(String correctInput) throws IllegalInputException {
		
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(ExitRqst.class)));
	}
	
	/*TODO NOMI MAPPE
	 * The String should be "CHANGEMAP MAPNAME"
	 * Where: -MAPNAME is the name of the selected map
	 */
	@TestWith(value = {
		"CHANGEMAP COAST BLACK",
		"CHANGEMAP",
		"CHANGEMAP ",
		"CHANGEMAPDEFAULT"
		}, separator = ";"	
	)
	public void testParseMessageWhenMessageIsAnIncorrectChangeMapShouldThrowAnIllegalInputException(String errorInput) {
		
		try {
			interpreter.parserMessage(errorInput, game, token);
			exprected.expect(IllegalInputException.class);
		} catch (IllegalInputException e) {
		}
	}
	
	@TestWith(value = {
			"CHANGEMAP DEFAULT",
			"  CHANGEMAP PIPPO   "
		}, separator = ";"
	)
	public void testParseMessageWhenMessageIsACorrectChangeMapAndTheGameIsNotStartedShouldReturnAChangeMapRqst(String correctInput) throws IllegalInputException {
		game = null;
		RequestMessage request = interpreter.parserMessage(correctInput, game, token);
		assertThat(request, is(instanceOf(ChangeMapRqst.class)));
	}
	
	@Test(expected = IllegalInputException.class)
	public void testParseMessageWhenMessageIsACorrectChangeMapAndTheGameIsStartedShouldReturnAnIllegalInputException() throws IllegalInputException {
		interpreter.parserMessage("CHANGEMAP DEFAULT", game, token);
		
	}
	
	@Test
	public void testParseMessageCallShouldTimeShouldInitializeMapOnlyOnce() throws IllegalInputException {
		interpreter.parserMessage("EXIT", game, token);
		interpreter.parserMessage("EXIT", game, token);
		return;
	}
	
	@Rule
	public final SystemOutRule system = new SystemOutRule().enableLog().muteForSuccessfulTests();
	@Test
	public void testParseMessageWhenMessageIsInfoMountainShouldPrintTheRegionMountainAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOMOUNTAIN", game, token));
		assertTrue(game.getBoard().getRegions(RegionType.MOUNTAIN).toString().equals(system.getLog()));
		system.enableLog();

	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoCoastShouldPrintTheRegionCoastAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOCOAST", game, token));
		assertTrue(game.getBoard().getRegions(RegionType.COAST).toString().equals(system.getLog()));
		system.enableLog();

	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoHillShouldPrintTheRegionHillAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOHILL", game, token));
		assertTrue(game.getBoard().getRegions(RegionType.HILL).toString().equals(system.getLog()));
		system.enableLog();

	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoAssistantReserveShouldPrintTheAssistantReserveAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOASSISTANTRESERVE", game, token));
		assertTrue(game.getBoard().getAssistantsReserve().toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoKingRegionShouldPrintTheKingRegionAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOREGIONKING", game, token));
		assertTrue(game.getBoard().getKingRegion().toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoKingShouldPrintTheKingAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOKING", game, token));
		assertTrue(game.getBoard().getKing().toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoCouncillorReserveShouldPrintTheCouncillorReserveAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOCOUNCILLORRESERVE", game, token));
		assertTrue(game.getBoard().getCouncillorReserve().toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoMarketShouldPrintTheStoreAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOMARKET", game, token));
		assertTrue(game.getStore().toString().replaceAll("\\{", "").replaceAll("\\}", "").equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoPoliticCardShouldPrintThePlayersPoliticCardsAndReturnNull() throws IllegalInputException {
		token.setPlayerNumber(0);
		assertNull(interpreter.parserMessage("INFOPOLITICCARD", game, token));
		assertTrue(game.getPlayers().get(0).getPoliticCards().toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoVictoryShouldPrintThePlayersVictoryPointsAndReturnNull() throws IllegalInputException {
		token.setPlayerNumber(0);
		assertNull(interpreter.parserMessage("INFOVICTORY", game, token));
		assertTrue(game.getPlayers().get(0).getVictoryTrack().getPoints() == Integer.parseInt(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoCoinsShouldPrintThePlayersCoinsAndReturnNull() throws IllegalInputException {
		token.setPlayerNumber(0);
		assertNull(interpreter.parserMessage("INFOCOINS", game, token));
		assertTrue(game.getPlayers().get(0).getCoinsTrack().getCurrentCoins() == Integer.parseInt(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoNobilityTrackShouldPrintTheNobilityTrackAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFONOBILITYTRACK", game, token));
		assertTrue(game.getBoard().getNobilityTrack().toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoPermitCardShouldPrintThePlayersPermitCardsAndReturnNull() throws IllegalInputException {
		token.setPlayerNumber(0);
		assertNull(interpreter.parserMessage("INFOPERMITCARD", game, token));
		assertTrue(game.getPlayers().get(0).getPermitCards().toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoCitiesShouldPrintTheCitiesAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOCITY", game, token));
		assertTrue(game.getBoard().getCities().toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoCityGraphShouldPrintTheCitiesWithConnectionsAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOCITYGRAPH", game, token));
		assertTrue(game.getBoard().getConnectionToString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoCurrentSpaceShouldPrintTheCurrentSpaceOfMarkerDiskAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOCURRENTSPACE", game, token));
		assertTrue(game.getPlayers().get(token.getPlayerNumber()).getMarkerDisk().getCurrentSpace().toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoCitiesWherePlayerHasBuiltShouldPrintTheCitiesAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOMYCITIES", game, token));
		assertTrue(game.getPlayers().get(token.getPlayerNumber()).getCities().toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoEmporiumsShouldPrintTheNumberOfThePlayersEmporiumsAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFOEMPORIUM", game, token));
		assertTrue(game.getPlayers().get(token.getPlayerNumber()).getEmporiums().size() == Integer.parseInt(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoShouldPrintTheInfoCommandsAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("INFO", game, token));
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
		assertTrue(sb.toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsInfoPlayersShouldPrintThePlayersAndReturnNull() throws IllegalInputException {
		game.getPlayers().forEach(p -> p.getCities().add(game.getBoard().getCities().get(0)));
		assertNull(interpreter.parserMessage("INFOPLAYERS", game, token));
		StringBuilder sb = new StringBuilder();
		game.getPlayers().forEach(p -> {
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
		});
		assertTrue(sb.toString().equals(system.getLog()));
		system.enableLog();
	}
	
	@Test
	public void testParseMessageWhenMessageIsActionShouldPrintTheActionFormatStringAndReturnNull() throws IllegalInputException {
		assertNull(interpreter.parserMessage("ACTION", game, token));
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
		assertTrue(sb.toString().equals(system.getLog()));
		system.enableLog();
	}
	
}
