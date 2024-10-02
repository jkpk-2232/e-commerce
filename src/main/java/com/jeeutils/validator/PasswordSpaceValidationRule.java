package com.jeeutils.validator;

public class PasswordSpaceValidationRule extends AbstractValidationRule {

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		long value = -1;

		try {
			value = Long.parseLong(paramObject.toString());

			if (value == 0) {
				errorMessage = fieldName + " is required.";
				return errorMessage;
			}

		} catch (Exception e) {
			
			logger.info("getWholeLongParameter parsing failed: " + e.getMessage());
		}

		double doubleValue = -1.0d;

		try {
			doubleValue = Double.parseDouble(paramObject.toString());

			if (doubleValue == 0.0d) {
				errorMessage = fieldName + " is required.";
				return errorMessage;
			}

		} catch (Exception e) {
			
			logger.info("getWholeDoubleParameter parsing failed: " + e.getMessage());
		}

		if (String.valueOf(paramObject) == "") {
			
			errorMessage = fieldName + " is required.";
			return errorMessage;

		} else if (String.valueOf(paramObject).replace(" ", "").trim().equalsIgnoreCase("")) {

			errorMessage = fieldName + " is not allowed space .";
			return errorMessage;

		} else if (String.valueOf(paramObject).trim().contains(" ")) {
			
			errorMessage = fieldName + " is not allowed space.";
			return errorMessage;
			
		} else {
			
			return SUCCESS;
		}

	}

}