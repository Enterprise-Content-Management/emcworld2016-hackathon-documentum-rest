package com.emc.documentum.exceptions;

public class FolderCreationException extends CreationException {

	public FolderCreationException(String folderName) {
		super("Unable to Create Folder " + folderName );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5050524908997297160L;

}
