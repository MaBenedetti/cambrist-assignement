/**
 * 
 */
package it.java.cambrist.assignment.test;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import it.java.cambrist.assignment.domain.UserObject;
import it.java.cambrist.assignment.exceptions.GenericCambristException;
import it.java.cambrist.assignment.exceptions.RESTCallFailedException;
import it.java.cambrist.assignment.helper.ScoringWebServiceConsumerHelper;

/**
 * @author mbenedetti
 *
 */
public class ScoringWebServiceConsumerTester {

	@Test
	public void calculateScoreSuccess(){
		try {
			Map<String, Object> map = ScoringWebServiceConsumerHelper.calculateScore(new UserObject(2304, "Magda John", 1921, "ZZ16802", "6980 Shallow Lake St, rRound Rock TX"));
			Assert.assertEquals(map.get("score"), new Double(518.0));
		} catch (GenericCambristException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(expected = RESTCallFailedException.class)
	public void calculateScoreFailNullParameter() throws GenericCambristException{
		ScoringWebServiceConsumerHelper.calculateScore(null);
	}

	@Test(expected = RESTCallFailedException.class)
	public void calculateScoreFailEmptyParameter() throws GenericCambristException{
		ScoringWebServiceConsumerHelper.calculateScore(new UserObject());
	}
	
	@Test(expected = RESTCallFailedException.class)
	public void calculateScoreFailFakeCustomer() throws GenericCambristException{
		ScoringWebServiceConsumerHelper.calculateScore(new UserObject(0, "Matteo Benedetti", 0, "BNDMTT86", "Roma, Italia"));
	}
}
