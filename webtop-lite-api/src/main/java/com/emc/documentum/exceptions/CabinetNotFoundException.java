package com.emc.documentum.exceptions;


public class CabinetNotFoundException extends FolderNotFoundException {

	public CabinetNotFoundException(String cabinetName) {
		super("Cabinet " + cabinetName + " not found");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3280390739075450146L;
	
}
