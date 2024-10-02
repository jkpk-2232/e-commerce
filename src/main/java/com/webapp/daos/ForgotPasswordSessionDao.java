package com.webapp.daos;

import java.util.Map;

import com.webapp.models.ForgotPasswordSessionModel;

public interface ForgotPasswordSessionDao {

	int createForgotPasswordSession(ForgotPasswordSessionModel model);

	boolean isValidFPSession(Map<String, Object> map);

	int markSessionAsConsume(Map<String, Object> map);

	public ForgotPasswordSessionModel getForgotPasswordSessionById(String forgotpasswordSessionId);
}
