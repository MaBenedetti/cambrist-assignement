/**
 * 
 */
package it.java.cambrist.assignment.exceptions;

/**
 * @author mbenedetti
 *
 */
public class ParameterNotPassedException extends GenericCambristException {

	/**
	 * 
	 */
	public ParameterNotPassedException() {
	}

	/**
	 * @param arg0
	 */
	public ParameterNotPassedException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public ParameterNotPassedException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ParameterNotPassedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public ParameterNotPassedException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
