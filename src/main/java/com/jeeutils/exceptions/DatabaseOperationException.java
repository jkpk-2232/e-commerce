package com.jeeutils.exceptions;

public class DatabaseOperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DatabaseOperationException() {
		super();
	}

	public DatabaseOperationException(String message) {
		super(message);
	}

	public DatabaseOperationException(Exception e) {
		super(e);
	}

}