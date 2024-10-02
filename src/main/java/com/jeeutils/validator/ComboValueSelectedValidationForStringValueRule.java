package com.jeeutils.validator;

public class ComboValueSelectedValidationForStringValueRule extends AbstractValidationRule {

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		String value = "-1";

		try {
			value = String.valueOf(paramObject.toString());
		} catch (Exception e) {
			logger.info("getWholeLongParameter parsing failed: " + e.getMessage());
		}

		if (value.equals("-1")) {
			errorMessage = fieldName + " is required.";
			return errorMessage;
		} else {
			return SUCCESS;
		}

	}

}