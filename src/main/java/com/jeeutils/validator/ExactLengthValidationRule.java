package com.jeeutils.validator;

public class ExactLengthValidationRule extends AbstractValidationRule {

	public String errorMessage = "";

	public int exactLength;

	public ExactLengthValidationRule(int exactLength) {
		this.exactLength = exactLength;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		int paramValue = paramObject.toString().length();

		if (!paramObject.toString().equals("")) {

			if (paramValue != this.exactLength) {
				errorMessage = fieldName + " should be of exact " + this.exactLength + " characters.";
				return errorMessage;
			}
		}

		return SUCCESS;
	}

}