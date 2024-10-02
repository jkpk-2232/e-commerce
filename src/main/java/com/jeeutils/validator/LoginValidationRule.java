package com.jeeutils.validator;

import com.utils.LoginUtils;

public class LoginValidationRule extends AbstractValidationRule {

	private String username;

	private String password;

	public LoginValidationRule(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public String validate(Object paramObject, String fieldName) {

		if (username == null || password == null) {
			return SUCCESS;
		}

		if (username.equals("") || password.equals("")) {
			return SUCCESS;
		}

		String userId = LoginUtils.isValidLoginCredentials(username, password);

		if (userId != null && !"".equals(userId)) {
			return SUCCESS;
		}

		errorMessage = " Invalid login credentials.";

		return errorMessage;
	}

}