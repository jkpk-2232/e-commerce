package com.jeeutils.exceptions;

public class UnexpectedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnexpectedException() {
		super();
	}

	public UnexpectedException(String message) {
		super(message);
	}

	public UnexpectedException(Exception e) {
		super(e);
	}

}