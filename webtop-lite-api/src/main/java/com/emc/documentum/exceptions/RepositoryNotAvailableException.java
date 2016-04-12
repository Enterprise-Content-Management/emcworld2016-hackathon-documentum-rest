package com.emc.documentum.exceptions;

public class RepositoryNotAvailableException extends DocumentumException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8005248972009074576L;

	public RepositoryNotAvailableException(String repositoryName) {
		super("Repository " + repositoryName + " not available ");
	}

	public RepositoryNotAvailableException(String repositoryName, Throwable e) {
		super("Repository " + repositoryName + " not available ");
	}
}
