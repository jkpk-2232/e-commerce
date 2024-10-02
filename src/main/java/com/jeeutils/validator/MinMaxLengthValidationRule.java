package com.jeeutils.validator;

public class MinMaxLengthValidationRule extends AbstractValidationRule {

	private String errorMessage = "";

	private int minLength;

	private int maxLength;

	public MinMaxLengthValidationRule(int minLength, int maxLength) {
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		int paramValue = paramObject.toString().length();

		if (!paramObject.toString().equals("")) {

			if (paramValue < this.minLength) {
				errorMessage = fieldName + " should be of minimum " + this.minLength + " characters/numbers.";
				return errorMessage;
			} else if (paramValue > this.maxLength) {
				errorMessage = fieldName + " should not be greater than " + this.maxLength + " characters/numbers.";
				return errorMessage;
			}
		}

		return SUCCESS;
	}

}