package it.java.cambrist.assignment.test;
import java.util.Scanner;

import org.junit.Assert;
import org.junit.Test;

import it.java.cambrist.assignment.domain.UserObject;
import it.java.cambrist.assignment.exceptions.CSVRecordNotValidExcpetion;
import it.java.cambrist.assignment.exceptions.EndOfCSVDocumentReadched;
import it.java.cambrist.assignment.exceptions.GenericCambristException;
import it.java.cambrist.assignment.helper.CSVUtilHelper;

/**
 * 
 */

/**
 * @author mbenedetti
 *
 */
public class CSVUtilTester {

	@Test
	public void testCorrectParsing(){
		Scanner scanner = new Scanner("7581,Bob Mary,1955,SN14859,\"5388 Fireoak Av, London\"");

		try {
			UserObject obj = CSVUtilHelper.parseLine(scanner);
			Assert.assertEquals(obj, new UserObject(7581, "Bob Mary", 1955, "SN14859", "5388 Fireoak Av, London"));
		} catch (GenericCambristException e) { 
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test
	public void testCorrectParsingWithCarriageReturn(){
		Scanner scanner = new Scanner("2304,Magda John,1921,ZZ16802,\"6980 Shallow Lake St, \n\rRound Rock TX\"");

		try {
			UserObject obj = CSVUtilHelper.parseLine(scanner);
			Assert.assertEquals(obj, new UserObject(2304, "Magda John", 1921, "ZZ16802", "6980 Shallow Lake St, rRound Rock TX"));
		} catch (GenericCambristException e) { 
			e.printStackTrace();
			Assert.fail();
		}
	}

	@Test(expected = CSVRecordNotValidExcpetion.class)
	public void testWrongParsingWithHeader() throws GenericCambristException{
		Scanner scanner = new Scanner("RECN,NAME,YOB,LEGALID,ADDRESS");

		CSVUtilHelper.parseLine(scanner);
	}

	@Test(expected = EndOfCSVDocumentReadched.class)
	public void testParsingEmptyRow() throws GenericCambristException{
		Scanner scanner = new Scanner("");

		CSVUtilHelper.parseLine(scanner);

	}

}
