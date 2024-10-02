package com.jeeutils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaxiSolsPhoneNumberValidationRule extends AbstractValidationRule {

	private static final String NUMERIC_REG_EXP = "^\\+225(?:[0-9] ?){8,13}[0-9]$";

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		if (!paramObject.toString().equals("")) {

			Pattern pattern = Pattern.compile(NUMERIC_REG_EXP, Pattern.CASE_INSENSITIVE);

			Matcher matcher = pattern.matcher(paramObject.toString());

			if (!matcher.matches()) {
				errorMessage = fieldName + " is invalid.";
				return errorMessage;
			}

		}

		return SUCCESS;
	}

}