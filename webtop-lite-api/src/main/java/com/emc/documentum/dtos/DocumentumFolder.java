package com.emc.documentum.dtos;

public class DocumentumFolder extends DocumentumObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2139458938262043348L;

	public DocumentumFolder() {
		super();
		setType("Folder");
	}
	
	public DocumentumFolder(String id ,String name, String type) {
		super(id, name, type);
	}
	
	public DocumentumFolder(String id ,String name) {
		super(id, name);
		setType("Folder");
	}
}
