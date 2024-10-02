package com.jeeutils.validator;

public class GenderValueSelectedValidation extends AbstractValidationRule {

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		if (paramObject.toString().equals("M") || paramObject.toString().equals("F")) {
			return SUCCESS;
		} else {
			errorMessage = fieldName + " is required.";
			return errorMessage;
		}

	}

}