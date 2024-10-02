package com.jeeutils.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidationRuleEmptyEmail extends AbstractValidationRule {

	private static final String emailRegExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

	@Override
	public String validate(Object paramObject, String fieldName) {

		if (paramObject != null && !paramObject.toString().equals("")) {

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