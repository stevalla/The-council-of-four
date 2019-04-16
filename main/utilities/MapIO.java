package it.polimi.ingsw.cg32.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* This class is used to manage the reading of File.txt for the configuration of the match.<br>
* It provides method {@link #readLines(BufferedReader, String, String)} to read several lines of the file enclosed
* between 2 tags that it's used from {@link #readFile(String)} to return a Map of tag and List of String 
* that are the values associated with the tag.
* 
* @author giovanni
*
*/
public class MapIO {

	private static final String START = "START_";
	private static final String END = "END_";
	
	
	/**
    * Suppress the default constructor for noninstantiability 
    */
	private MapIO(){
		throw new AssertionError();
	}
	
	
	/**
	* This method facilitates the reading of the configuration file.
	* It return a map that collect associations of tags and List of String.
	* These List of String rappresnts the file rappresentation of the different objects of the game
	* as the {@link NobilityTarck} and the deck of {@linlk PermitCard}, tags are
	* name to identify them in the Map.   
	* 
	* @param file the file used to configure the match.
	* 
	* @return a Map of String-tags and List of Strings the rappresent the lines associated with the
	* 		  corresponding tag.
	* @throws IOException if an error occured proccessing the file
	*/
	public static Map<String, List<String>> readFile(String file) throws IOException {
		
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		Map<String, List<String>> infoString = new LinkedHashMap<>();

		for(String currentLine = bufferedReader.readLine(); !(END + "KEYS").equals(currentLine); currentLine = bufferedReader.readLine())
			infoString.put(currentLine, new ArrayList<>());
		
		for(Map.Entry<String, List<String>> map : infoString.entrySet())
			map.getValue().addAll(readLines(bufferedReader, START + map.getKey(), END + map.getKey()));
		
		fileReader.close();

		return infoString;
	}
	
	
	/**
	* This method reads all the lines of the file between 2 tags and return a List
	* of String that are the lines read.
	* 
	* @param bufferedReader a bufferReader associated with the file of configuration.
	* @param start a String tag that indicate the first line from where begin the reading.
	* @param end a String tag that indicate the last line to read.
	* @return the lines read collected in a list of String
	* @throws IOException if a error occured reading file.
	*/
	private static List<String> readLines(BufferedReader bufferedReader, String start, String end) throws IOException {
		
		List<String> cached = new ArrayList<>();
		
		while(!bufferedReader.readLine().equals(start));

		for(String currentLine = bufferedReader.readLine(); !currentLine.equals(end); currentLine = bufferedReader.readLine()) {
			if(!currentLine.isEmpty())
				cached.add(currentLine.toUpperCase());
		}
		
		return cached;
	}

}
