package org.openmrs.module;

public class CyclomaticDependencyException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6667726729004407964L;
	
	public CyclomaticDependencyException() {
		super();
	}
	
	public CyclomaticDependencyException(String message, Throwable cause, boolean enableSuppression,
	    boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public CyclomaticDependencyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CyclomaticDependencyException(String message) {
		super(message);
	}
	
	public CyclomaticDependencyException(Throwable cause) {
		super(cause);
	}
	
}
