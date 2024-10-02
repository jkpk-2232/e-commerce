package com.jeeutils.validator;

import com.webapp.models.UserModel;

public class EmailPresentForOtherUserValidationRule extends AbstractValidationRule {

	private String userId;
	private String roleId;

	public EmailPresentForOtherUserValidationRule(String userId, String roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		boolean isPresent = UserModel.isEmailIdExistsForOtherUser(paramObject.toString(), userId, roleId);

		if (isPresent) {
			errorMessage = fieldName + " is duplicate.";
			return errorMessage;
		}

		return SUCCESS;
	}

}