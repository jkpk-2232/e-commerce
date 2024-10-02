package com.jeeutils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalValidationRule extends AbstractValidationRule {

	private static final String DECIMAL_REG_EXP = "^[-+]?\\d{1,6}(\\.\\d{1,2})?$";

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		Pattern pattern = Pattern.compile(DECIMAL_REG_EXP, Pattern.CASE_INSENSITIVE);

		Matcher matcher = pattern.matcher(String.valueOf(paramObject));

		if (!matcher.matches()) {
			errorMessage = fieldName + " maximum 6 digit allowed(eg.###.##).";
			return errorMessage;
		}

		return SUCCESS;
	}

}