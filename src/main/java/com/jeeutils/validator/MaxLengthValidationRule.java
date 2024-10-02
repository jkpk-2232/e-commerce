package com.jeeutils.validator;

public class MaxLengthValidationRule extends AbstractValidationRule {

	public String errorMessage = "";

	public int maxLength;

	public MaxLengthValidationRule(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		int paramValue = paramObject.toString().length();

		if (paramValue > this.maxLength) {
			errorMessage = fieldName + " should not be greater than " + this.maxLength + " characters.";
			return errorMessage;
		}

		return SUCCESS;
	}

}