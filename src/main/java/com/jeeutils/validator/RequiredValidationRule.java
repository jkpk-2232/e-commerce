package com.jeeutils.validator;

public class RequiredValidationRule extends AbstractValidationRule {

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		long value = -1;

		try {
			value = Long.parseLong(paramObject.toString());

			if (value == -1) {
				errorMessage = fieldName + " is required.";
				return errorMessage;
			}

		} catch (Exception e) {
			logger.info("getWholeLongParameter parsing failed: " + e.getMessage());
		}

		if (String.valueOf(paramObject) == "" || String.valueOf(paramObject) == "-1") {
			errorMessage = fieldName + " is required.";
			return errorMessage;
		} else {
			return SUCCESS;
		}

	}

}