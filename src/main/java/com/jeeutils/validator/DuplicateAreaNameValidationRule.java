package com.jeeutils.validator;

import com.webapp.models.AdminAreaModel;

public class DuplicateAreaNameValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = AdminAreaModel.isAdminAreaExists(String.valueOf(paramObject).trim());

		if (isDuplicate == false) {
			return SUCCESS;
		} else {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}
	}

}