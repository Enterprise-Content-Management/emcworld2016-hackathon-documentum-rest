package com.emc.documentum.exceptions;

public class CanNotDeleteFolderException extends Exception{
	
	public CanNotDeleteFolderException(String folderId) {
		super("Can'te delete folder " + folderId + " as it has children");
	}

	public CanNotDeleteFolderException() {
		// TODO Auto-generated constructor stub
	}
}
