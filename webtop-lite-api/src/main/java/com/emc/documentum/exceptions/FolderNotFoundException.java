package com.emc.documentum.exceptions;

import com.emc.documentum.exceptions.DocumentumException;

public class FolderNotFoundException extends DocumentumException {

	public FolderNotFoundException(String folderName) {
		super("Folder " + folderName + " not found");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3280390739075450146L;
	
}
