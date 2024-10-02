package com.jeeutils.validator;

public class ComboValueSelectedValidationRule extends AbstractValidationRule {

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		long value = -1;

		try {
			value = Long.parseLong(paramObject.toString());
		} catch (Exception e) {
			logger.info("getWholeLongParameter parsing failed: " + e.getMessage());
		}

		if (value == -1) {
			errorMessage = fieldName + " is required.";
			return errorMessage;
		} else {
			return SUCCESS;
		}

	}

}