package com.jeeutils.validator;

public class CustumValidationRule extends AbstractValidationRule {

	private String errorMessage = "";

	private boolean isError;

	public CustumValidationRule(String errorMessage, boolean isError) {
		super();
		this.errorMessage = errorMessage;
		this.isError = isError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isError() {
		return isError;
	}

	public void setError(boolean isError) {
		this.isError = isError;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		if (isError) {
			return errorMessage;
		}

		return SUCCESS;
	}

}