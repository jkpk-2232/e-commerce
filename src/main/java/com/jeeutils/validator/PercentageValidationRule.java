package com.jeeutils.validator;

public class PercentageValidationRule extends AbstractValidationRule {

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		double value = -1;

		try {
			value = Double.parseDouble(String.valueOf(paramObject));
		} catch (Exception e) {
			logger.info("getWholeDoubleParameter parsing failed: " + e.getMessage());
		}

		if (value < 0) {
			errorMessage = fieldName + " should have greater than Or Equal to 0%.";
			return errorMessage;
		} else if (value > 100) {
			errorMessage = fieldName + " should have less than Or Equal to 100%.";
			return errorMessage;
		}

		return SUCCESS;
	}

}