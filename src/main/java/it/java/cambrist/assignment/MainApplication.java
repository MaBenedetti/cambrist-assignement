/**
 * 
 */
package it.java.cambrist.assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.java.cambrist.assignment.domain.Scores;
import it.java.cambrist.assignment.domain.UserObject;
import it.java.cambrist.assignment.domain.UserScore;
import it.java.cambrist.assignment.exceptions.GenericCambristException;
import it.java.cambrist.assignment.exceptions.ParameterNotPassedException;
import it.java.cambrist.assignment.exceptions.ParameterNotReadableException;
import it.java.cambrist.assignment.helper.CSVUtilHelper;
import it.java.cambrist.assignment.helper.ScoringWebServiceConsumerHelper;

/**
 * @author mbenedetti
 *
 */
public class MainApplication {

	//Temporary variables, used for calculating thresold 
	private static int 
	maxScore = Integer.MIN_VALUE, 
	minScore = Integer.MAX_VALUE;

	/**
	 * @param args
	 * The main method read the csv file, analyze the records, and create a new CSV with all records that have a score greatest of the thresold 
	 */
	public static void main(String[] args) {

		Scanner scanner = null;
		
		//The REST call will be made through asynchronous processes, the the maximum number of threads will be twice as available cores. 
		ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

		try{
			if(args == null || args.length <= 0){
				throw new ParameterNotPassedException("Provide a valid CSV File");
			}

			//The input csv file. It will contains the records to be analyzed
			File csvInputFile = new File(args[0]);

			if(!csvInputFile.exists() || !csvInputFile.canRead())
				throw new ParameterNotReadableException("The file is not readable: ["+args[0]+"]");

			scanner = new Scanner(csvInputFile);

			//this list will contain the asynchronous tasks. Each task will call the REST endpoint to get customer's score
			List<Callable<UserScore>> tasks = new ArrayList<>();

			//this list will contain the user's unique keys. If a key is duplicate, all records with that key must be excluded from the calculation
			Map<String, UserObject> userObjects = new HashMap<>();

			//reading the file, until there is a line.
			while (scanner.hasNext()) {
				try{
					//The parsed record 
					UserObject userObject = CSVUtilHelper.parseLine(scanner);

					//If the map already contains the unique KEY, the record is duplicate, and must be excluded from the calculation 
					if(userObjects.containsKey(userObject.getLegalID())){
						userObjects.get(userObject.getLegalID()).setDuplicate(true);
					}else
						userObjects.put(userObject.getLegalID(), userObject);

					//Creating the new asynchronous TASK
					Callable<UserScore> task = () -> {

						//If the record is duplicate must be excluded from the calculation 
						if(userObjects.containsKey(userObject.getLegalID()) && userObjects.get(userObject.getLegalID()).isDuplicate()){
							System.err.println("User's LegalID "+userObject.getLegalID()+" is duplicate: "+userObject);
							return null;
						}

						//Call the REST endpoint and retrieve the SCORE
						Map<String, Object> results = ScoringWebServiceConsumerHelper.calculateScore(userObject);

						//Create a domain object with the parsed record and his score
						UserScore score = new UserScore(userObject, results.get("id").toString(), Double.parseDouble(results.get("score").toString()));

						return score;
					};

					//add the created task to the list
					tasks.add(task);
				}catch(GenericCambristException ex){
					System.err.println(ex.getMessage());
				}catch(Exception ex){
					System.err.println("Error parsing the line");
				}

			}

			//this map will contain all scores and the list of the records that have that score
			Scores scores = new Scores();

			//Process all asynchronous TASKS, for each element, update the maximum and minimum score, and add it to the scores map.
			service.invokeAll(tasks).stream().map(future -> {
				try {

					//the result of the task
					return future.get();
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}).forEach((user) -> {
				//analyze the result of the 
				if(user != null){

					Double score = user.getScore();

					//if this is the maximum score until now, update the variable
					setSynchronizedMax(score.intValue());
					//if this is the minimum score until now, update the variable
					setSynchronizedMin(score.intValue());

					scores.putUserObject(score, user.getUserObject());
				}
			});			

			//calculating the thresold
			double thresold = maxScore - ((maxScore-minScore) * 3 / 100); 

			File outputFile = new File(csvInputFile.getParentFile(), "output.csv");
			FileWriter writer = new FileWriter(outputFile);

			//for each element if the score is greater or equals thresold we can write it on the output file 
			scores.getScores().forEach((key, value) -> {
				if(key >= thresold) 
					value.forEach(object -> {
						try {
							writer.append(CSVUtilHelper.tocsvLine(object) + "\n");
						} catch (Exception e) {
							System.err.println("Error writing on output CSV: "+object);
						}
					});
			});

			if(writer != null)
				try {
					writer.close();
				} catch (IOException e) { }
			
			System.out.println("Output can be found at: "+outputFile.getAbsolutePath());
			
		}catch(GenericCambristException e){
			System.err.println(e.getMessage());
			System.exit(-1);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		} catch (InterruptedException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}finally {
			if (scanner != null)
				scanner.close();

			service.shutdown();
		}

		System.exit(0);
	}	

	private static synchronized void setSynchronizedMax(int score){
		maxScore = Math.max(maxScore, score);
	}

	private static synchronized void setSynchronizedMin(int score){
		minScore = Math.min(minScore, score);
	}
}
