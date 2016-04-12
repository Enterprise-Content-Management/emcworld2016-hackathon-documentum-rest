package com.emc.documentum.exceptions;

public class DocumentCreationException extends CreationException {

	public DocumentCreationException(String documentName) {
		super("Unable to Create Document " + documentName );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5050524908997297160L;

}
