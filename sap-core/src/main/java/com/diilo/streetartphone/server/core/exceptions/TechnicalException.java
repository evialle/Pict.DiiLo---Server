/**
 * 
 */
package com.diilo.streetartphone.server.core.exceptions;

/**
 * @author Eric
 */
public class TechnicalException extends Exception {

	public TechnicalException(Exception e) {
		super(e);
	}

	public TechnicalException() {
		// TODO Auto-generated constructor stub
	}

	public TechnicalException(String string) {
		super(string);
	}
}
