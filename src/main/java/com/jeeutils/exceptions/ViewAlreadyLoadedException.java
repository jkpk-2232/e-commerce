package com.jeeutils.exceptions;

public class ViewAlreadyLoadedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ViewAlreadyLoadedException() {
		super();
	}

	public ViewAlreadyLoadedException(String message) {
		super(message);
	}

	public ViewAlreadyLoadedException(Exception e) {
		super(e);
	}

}