package com.jeeutils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidationRule extends AbstractValidationRule {

	private static final String emailRegExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		if (!paramObject.toString().equals("")) {

			String paramObjects[] = paramObject.toString().split(",");

			for (int i = 0; i < paramObjects.length; i++) {

				Pattern pattern = Pattern.compile(emailRegExp, Pattern.CASE_INSENSITIVE);

				Matcher matcher = pattern.matcher(paramObjects[i].toString().trim());

				if (!matcher.matches()) {
					errorMessage = fieldName + " is invalid.";
					return errorMessage;
				}
			}
		}

		return SUCCESS;
	}

}