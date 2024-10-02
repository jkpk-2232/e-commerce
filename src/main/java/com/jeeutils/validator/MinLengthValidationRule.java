package com.jeeutils.validator;

public class MinLengthValidationRule extends AbstractValidationRule {

	public String errorMessage = "";

	public int minLength;

	public MinLengthValidationRule(int minLength) {
		this.minLength = minLength;
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
				errorMessage = fieldName + " should be of minimum " + this.minLength + " characters.";
				return errorMessage;
			}
		}

		return SUCCESS;
	}

}