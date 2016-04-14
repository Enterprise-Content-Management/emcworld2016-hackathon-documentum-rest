package com.emc.documentum.exceptions;

public class CanNotDeleteFolderException extends DocumentumException {
	
	public CanNotDeleteFolderException(String folderId) {
		super("Can'te delete FOLDERS " + folderId + " as it has children");
	}
}
