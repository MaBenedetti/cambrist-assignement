/**
 * 
 */
package it.java.cambrist.assignment.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import it.java.cambrist.assignment.domain.UserObject;
import it.java.cambrist.assignment.exceptions.CSVRecordIncompleteException;
import it.java.cambrist.assignment.exceptions.CSVRecordNotValidExcpetion;
import it.java.cambrist.assignment.exceptions.CSVRecordParsingException;
import it.java.cambrist.assignment.exceptions.EndOfCSVDocumentReadched;
import it.java.cambrist.assignment.exceptions.GenericCambristException;

/**
 * @author mbenedetti
 *
 */
public class CSVUtilHelper {
	
	//The character used as values's separator in CSV file
	private static final char SEPARATOR = ',';
	//The character as QUOTE in CSV file
	private static final char QUOTE = '"';
	
	/**
	 * This method create an User Object from a CSV Line
	 * */
	public static UserObject parseLine(Scanner scanner) throws GenericCambristException{

		//get the parsed values
		List<String> parsedLine = parseLine(scanner, null);
		
		try{
			//Creating a domain object, according to the CSV template
			UserObject user = new UserObject(Long.parseLong(parsedLine.get(0)), parsedLine.get(1), Long.parseLong(parsedLine.get(2)), parsedLine.get(3), parsedLine.get(4));
			
			return user;
		}catch (Exception e) {
			throw new CSVRecordNotValidExcpetion("The parsed record is not valid: "+parsedLine);
		}
	}

	/**
	 * This method parse the CSV line. 
	 * If the line contains a carriage return between two QUOTES, the method read the next lines until the QUOTE is closed
	 * */
	private static List<String> parseLine(Scanner scanner, List<String> parsedLine) throws GenericCambristException{
		
		//Check again if the end of document has been reached
		if(!scanner.hasNext())
			throw new EndOfCSVDocumentReadched();
		
		//Get the next line
		String csvLine = scanner.nextLine();
		
		try{
			//this boolean points out if we are among two QUOTES
			boolean isInQuote = false;
			
			//The temporary variable where to insert characters. 
			String currentValue = new String();
			
			//If the list is populated means that QUOTE not been closes in previous line 
			if(parsedLine != null && parsedLine.size() > 0){
				isInQuote = true;
				currentValue = parsedLine.get(parsedLine.size()-1);
				parsedLine.remove(parsedLine.size()-1);
			}else
				parsedLine = new ArrayList<>();
			
			//iterate all characters 
			//when a SEPARATOR is found, the current value is added to the list, and the temporary variable is initialized again
			//when a QUOTE is found, the inQuote boolean change is state
			for(char c : csvLine.toCharArray()){
				if(c == SEPARATOR && !isInQuote){
					parsedLine.add(currentValue);
					currentValue = new String();
				}else if(c == QUOTE)
					isInQuote = !isInQuote;
				else
					currentValue += String.valueOf(c);
			}
			
			//the end of line is reached, we add the last word to the list
			parsedLine.add(currentValue);
			
			//if the QUOTE is not closed, we try to read the next line
			if(isInQuote){
				if(scanner.hasNext())
					return parseLine(scanner, parsedLine);
				else
					throw new CSVRecordIncompleteException("The row has a not closed QUOTE: "+csvLine);
			}
			
			return parsedLine;
		}catch(Exception e){
			throw new CSVRecordParsingException("Impossibile to parse the line: "+csvLine);
		}
	}

	/**
	 * The method return a csv line from the domain objects
	 * */
	public static CharSequence tocsvLine(UserObject value) {
		
		StringBuilder b = new StringBuilder();
		
		b.append(value.getRecn());
		b.append(SEPARATOR);
		b.append(value.getName());
		b.append(SEPARATOR);
		b.append(value.getYob());
		b.append(SEPARATOR);
		b.append(value.getLegalID());
		b.append(SEPARATOR);
		b.append(value.getAddress());
		b.append(SEPARATOR);
		
		return b.toString();
	}
}
