package com.jeeutils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlphaCharacterWithOutSpaceValidationRule extends AbstractValidationRule {

	private static final String alphaCharsRegExp = "^[A-Z a-z]+$";

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		if (!paramObject.toString().equals("")) {

			Pattern pattern = Pattern.compile(alphaCharsRegExp, Pattern.CASE_INSENSITIVE);

			Matcher matcher = pattern.matcher(paramObject.toString());

			if (!matcher.matches()) {
				errorMessage = fieldName + " should have only alpha characters.";
				logger.debug("errorMessage" + errorMessage);
				return errorMessage;
			}
		}

		return SUCCESS;
	}

}