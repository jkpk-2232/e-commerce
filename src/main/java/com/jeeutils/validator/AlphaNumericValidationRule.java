package com.jeeutils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlphaNumericValidationRule extends AbstractValidationRule {

	private static final String alphaNumericRegExp = "^[0-9A-Za-z]+$";

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		if (!paramObject.toString().equals("")) {

			Pattern pattern = Pattern.compile(alphaNumericRegExp, Pattern.CASE_INSENSITIVE);

			Matcher matcher = pattern.matcher(paramObject.toString());

			if (!matcher.matches()) {
				errorMessage = fieldName + " should have only alpha numeric characters.";
				return errorMessage;
			}

		}

		return SUCCESS;
	}

}