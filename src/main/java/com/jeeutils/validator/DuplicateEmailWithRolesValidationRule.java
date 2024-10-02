package com.jeeutils.validator;

import java.util.ArrayList;
import java.util.List;

import com.webapp.models.UserModel;

public class DuplicateEmailWithRolesValidationRule extends AbstractValidationRule {

	boolean isDuplicate = false;

	List<String> roleIdList = new ArrayList<String>();

	String userId;

	public DuplicateEmailWithRolesValidationRule(List<String> roleIdList, String userId) {
		this.roleIdList = roleIdList;
		this.userId = userId;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		String nullCheck = validateNotNull(paramObject, fieldName);

		if (!SUCCESS.equals(nullCheck)) {
			return nullCheck;
		}

		isDuplicate = UserModel.isEmailIdExistsForRoleIds(this.roleIdList, String.valueOf(paramObject).trim(), this.userId);

		if (isDuplicate == false) {
			return SUCCESS;
		} else {
			errorMessage = "The email is previously registered with us.";
			return errorMessage;
		}
	}

}