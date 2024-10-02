package com.jeeutils.validator;

import com.webapp.models.UserModel;

public class DuplicatePhoneNumberValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	private String roleId;

	private String phoneCode;

	private String userId;

	private String vendorId;

	public DuplicatePhoneNumberValidationRule(String phoneCode, String roleId, String userId, String vendorId) {
		this.roleId = roleId;
		this.phoneCode = phoneCode;
		this.userId = userId;
		this.vendorId = vendorId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = UserModel.isPhoneNumberExistsForRole(String.valueOf(paramObject).trim(), this.phoneCode, this.roleId, this.userId, this.vendorId);

		if (isDuplicate == false) {
			return SUCCESS;
		} else {
			errorMessage = "The number is previously registered with us.";
			return errorMessage;
		}
	}

}