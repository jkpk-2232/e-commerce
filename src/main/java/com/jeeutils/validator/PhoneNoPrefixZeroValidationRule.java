package com.jeeutils.validator;

public class PhoneNoPrefixZeroValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		if (!paramObject.toString().equals("")) {

			if ((paramObject.toString()).startsWith("0")) {
				errorMessage = fieldName + " should not start with zero.";
				return errorMessage;
			}
		}

		return SUCCESS;
	}

}