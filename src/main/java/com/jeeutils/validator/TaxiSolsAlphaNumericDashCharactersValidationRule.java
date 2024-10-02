package com.jeeutils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaxiSolsAlphaNumericDashCharactersValidationRule extends AbstractValidationRule {

	private static final String alphaDashCharsRegExp = "^[A-Za-z0-9@._-]+$";

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		if (!paramObject.toString().equals("")) {

			Pattern pattern = Pattern.compile(alphaDashCharsRegExp, Pattern.CASE_INSENSITIVE);

			Matcher matcher = pattern.matcher(paramObject.toString());

			if (!matcher.matches()) {
				errorMessage = fieldName + " should have only alphanumeric,dash and underscores.";
				return errorMessage;
			}

		}

		return SUCCESS;
	}

}