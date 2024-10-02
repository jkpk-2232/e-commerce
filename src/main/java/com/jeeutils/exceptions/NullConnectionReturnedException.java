package com.jeeutils.exceptions;

public class NullConnectionReturnedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NullConnectionReturnedException() {
		super();
	}

	public NullConnectionReturnedException(String message) {
		super(message);
	}

	public NullConnectionReturnedException(Exception e) {
		super(e);
	}

}