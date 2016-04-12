package com.emc.documentum.exceptions;

public class DelegateNotFoundException extends Exception{

	public DelegateNotFoundException(String delegateIdentifier) {
		super("There are no repositories configured for " + delegateIdentifier);
	}

	public DelegateNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
