package com.jeeutils.validator;

public class MaxLengthValidationRuleEmptyField extends AbstractValidationRule {

	public String errorMessage = "";

	public int maxLength;

	public MaxLengthValidationRuleEmptyField(int maxLength) {
		this.maxLength = maxLength;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		int paramValue = 0;

		if (paramObject != null && !"".equalsIgnoreCase(paramObject.toString())) {
			logger.debug("paramObject" + paramObject.toString());
			paramValue = paramObject.toString().length();
		}

		if (paramValue > this.maxLength) {
			errorMessage = fieldName + " should not be greater than " + this.maxLength + " characters.";
			return errorMessage;
		}

		return SUCCESS;
	}

}