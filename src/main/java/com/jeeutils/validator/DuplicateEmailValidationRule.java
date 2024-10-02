package com.jeeutils.validator;

import com.webapp.models.UserModel;

public class DuplicateEmailValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;
	String headerVendorId = null;

	public DuplicateEmailValidationRule(String headerVendorId) {
		this.headerVendorId = headerVendorId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = UserModel.isEmailIdExists(String.valueOf(paramObject).trim(), this.headerVendorId);

		if (isDuplicate == false) {
			return SUCCESS;
		} else {
			errorMessage = "The email is previously registered with us.";
			return errorMessage;
		}
	}

}