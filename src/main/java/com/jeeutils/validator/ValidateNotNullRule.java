package com.jeeutils.validator;

public class ValidateNotNullRule extends AbstractValidationRule {

	@Override
	public String validate(Object paramObject, String fieldName) {

		String errorMessage = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(errorMessage)) {
			return errorMessage;
		} else {
			return SUCCESS;
		}

	}

}