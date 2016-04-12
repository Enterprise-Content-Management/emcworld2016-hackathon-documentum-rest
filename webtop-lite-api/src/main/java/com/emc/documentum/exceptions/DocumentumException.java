package com.emc.documentum.exceptions;

public class DocumentumException extends Exception {

	public DocumentumException(String msg) {
		super(msg);
	}
	
	public DocumentumException(String msg , Throwable throwable) {
		super(msg, throwable);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
