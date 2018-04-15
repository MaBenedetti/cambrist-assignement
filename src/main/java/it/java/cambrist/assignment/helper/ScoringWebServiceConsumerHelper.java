/**
 * 
 */
package it.java.cambrist.assignment.helper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import it.java.cambrist.assignment.domain.UserObject;
import it.java.cambrist.assignment.exceptions.GenericCambristException;
import it.java.cambrist.assignment.exceptions.RESTCallFailedException;

/**
 * @author mbenedetti
 *
 */
public class ScoringWebServiceConsumerHelper {

	private static final String CAMBRIST_URI = "http://test.cambrist.net:8080/submissions/scorecheck";
	private static final String METHOD = "POST";
	private static final int TIMEOUT = 5000;

	private static final String CHARSET = "UTF-8";
	
	private static final String CONTENT_TYPE_HEADER_KEY = "Content-Type";
	private static final String CONTENT_TYPE_HEADER_VALUE = "application/json; charset="+CHARSET;

	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String AUTH_HEADER_VALUE = "APIKEY ha871ba6rds7x";
	
	/**
	 * Create the connection with fixed params
	 * */
	private static HttpURLConnection getConnection() throws IOException{

		URL url = new URL(CAMBRIST_URI);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(TIMEOUT);
		conn.setRequestProperty(CONTENT_TYPE_HEADER_KEY, CONTENT_TYPE_HEADER_VALUE);
		conn.setRequestProperty(AUTH_HEADER_KEY, AUTH_HEADER_VALUE);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod(METHOD);
		
		return conn;
	}

	/**
	 * Open connection, send data to REST Endpoint.
	 * If response code not equals 200 throw an exception, otherwise read response and deserialize it
	 * */
	public static Map<String, Object> calculateScore(UserObject user) throws GenericCambristException{

		Gson gson = new Gson();

		//Serialize input parameter into a json string
		String json = gson.toJson(user);
		
		HttpURLConnection connection = null;
		InputStream inpuntStream = null;
		OutputStream outputStream = null;
		
		try{
			
			//Open connection
			try{
				connection = getConnection();
			}catch(IOException e){
				throw new RESTCallFailedException("Error contacting the endpoint ["+CAMBRIST_URI+"]", e);
			}
			
			//Write parameter into request
			try{
				OutputStream os = connection.getOutputStream();
				os.write(json.getBytes(CHARSET));
			}catch(IOException e){
				throw new RESTCallFailedException("Error sending data to ["+CAMBRIST_URI+"]", e);
			}finally {
				if(outputStream != null)
					try {
						outputStream.close();
					} catch (IOException e) { }
			}
			
			try{
				//If response code not equals 200 there is an error
				if(connection.getResponseCode() != 200){
					throw new RESTCallFailedException("The ENDOPOINT answered with "+connection.getResponseMessage(), connection.getResponseCode());
				}
				
				//read the response e put it into a String variable
				inpuntStream = new BufferedInputStream(connection.getInputStream());
				String result = IOUtils.toString(inpuntStream, CHARSET);
		
				//Deserialize the response in a map
				Map<String, Object> map = gson.fromJson(result, new TypeToken<Map<String, Object>>(){}.getType());
		
				return map;
			}catch(IOException e){
				throw new RESTCallFailedException("Error retrieving response from ["+CAMBRIST_URI+"]", e);
			}
		}finally {
			if (inpuntStream != null)
				try {
					inpuntStream.close();
				} catch (IOException e) { }
			
			if (connection != null){
				connection.disconnect();
			}
		}
	}

}
