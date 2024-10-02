package com.jeeutils.validator;

public class ConformPasswordValidationRule extends AbstractValidationRule {

	private String conformPassword;

	public ConformPasswordValidationRule(String conformPassword) {
		this.conformPassword = conformPassword;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		String password = paramObject.toString();

		if (!password.equalsIgnoreCase(this.conformPassword)) {
			errorMessage = fieldName + " not match.";
			return errorMessage;
		}

		return SUCCESS;
	}

}