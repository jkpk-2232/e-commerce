package com.webapp.actions.secure;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.dbsession.CookieUtils;
import com.utils.dbsession.DbSession;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.RoleModel;
import com.webapp.models.UserLoginOtpModel;
import com.webapp.models.UserModel;

@Path("")
public class UserLoginOtpAction extends BusinessAction {

	@GET
	@Path("/verify-otp/{userLoginOtpId}")
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAdminVerifyOtp(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@PathParam(FieldConstants.USER_LOGIN_OTP_ID) String userLoginOtpId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		UserLoginOtpModel userLoginOtpModel = UserLoginOtpModel.getUserDetails(userLoginOtpId);

		if (userLoginOtpModel == null) {
			return redirectToPage(UrlConstants.PAGE_URLS.LOGIN_URL);
		}

		if (LoginUtils.checkLogIn(request, response)) {

			if (!RoleModel.isUserHasLoginAccess(userLoginOtpModel.getUserId())) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			String url = getPriorityUrlByUserd(userLoginOtpModel.getUserId());

			if (!StringUtils.validString(url)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			return redirectToPage(url);
		}

		data.put(FieldConstants.USER_ID, userLoginOtpModel.getUserId());
		data.put(FieldConstants.USER_LOGIN_OTP_ID, userLoginOtpId);
		data.put(FieldConstants.PHONE_NUMBER, MyHubUtils.formatPhoneNumber(userLoginOtpModel.getPhoneNoCode(), userLoginOtpModel.getPhoneNo()));

		return loadView(UrlConstants.JSP_URLS.USER_LOGIN_OTP_JSP);
	}

	@POST
	@Path("/verify-otp/{userLoginOtpId}")
	@Produces(MediaType.TEXT_HTML)
	@Consumes("application/x-www-form-urlencoded")
	//@formatter:off
	public Response postAdminVerifyOtp(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@FormParam(FieldConstants.VERIFICATION_CODE) String verificationCode,
		@FormParam(FieldConstants.USER_ID) String userId,
		@FormParam(FieldConstants.USER_LOGIN_OTP_ID) String userLoginOtpId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		UserLoginOtpModel userLoginOtpModel = UserLoginOtpModel.getUserDetails(userLoginOtpId);

		data.put(FieldConstants.USER_ID, userLoginOtpModel.getUserId());
		data.put(FieldConstants.PHONE_NUMBER, MyHubUtils.formatPhoneNumber(userLoginOtpModel.getPhoneNoCode(), userLoginOtpModel.getPhoneNo()));
		data.put(FieldConstants.USER_LOGIN_OTP_ID, userLoginOtpId);
		data.put(FieldConstants.VERIFICATION_CODE, verificationCode);

		UserLoginOtpModel verifyOtpLogsModel = UserLoginOtpModel.getUserDetailsByOtp(verificationCode, userId);

		if (verifyOtpLogsModel == null) {
			data.put("invalidLoginError", messageForKeyAdmin("errorInvalidVerificationCode"));
			return loadView(UrlConstants.JSP_URLS.USER_LOGIN_OTP_JSP);
		}

		UserModel userModel = UserModel.getUserAccountDetailsById(userId);

		if (!RoleModel.isUserHasLoginAccess(userId)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		userModel.setTimezone(TimeZoneUtils.getTimeZone());

		DbSession session = LoginUtils.createSession(userModel, request, response);

		if (verifyOtpLogsModel != null) {

			Cookie cookie = CookieUtils.getCookie(DbSession.REMEMBER_ME_SESSION_COOKIE_NAME, request);

			if (cookie != null) {
				LoginUtils.deleteRememberMeKey(cookie.getValue());
			}
		}

		String url = LoginUtils.processPostLogin(userId, session);

		if (!StringUtils.validString(url)) {
			return redirectToPage(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		return redirectToPage(url);
	}

	@POST
	@Path("/resend-otp")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response resendOtp(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@FormParam("userId") String userId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		Map<String, String> outputMap = new HashMap<>();

		String verifyCode = UserLoginOtpModel.getVerificationCodeOfUser(userId);

		if (!StringUtils.validString(verifyCode)) {
			outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			outputMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorVerificationCodeSent"));
			return sendDataResponse(outputMap);
		}

		UserModel user = UserModel.getUserAccountDetailsById(userId);

		String message = String.format(messageForKeyAdmin("successMobileVerficationCode"), user.getFullName(), verifyCode);
		MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, user.getPhoneNoCode() + user.getPhoneNo(), ProjectConstants.SMSConstants.SMS_OTP_TEMPLATE_ID);

		outputMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		outputMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successVerificationCodeSent"));
		return sendDataResponse(outputMap);
	}

	@Override
	protected String[] requiredJs() {
		return new String[] { UrlConstants.JS_URLS.USER_LOGIN_OTP_JS };
	}
}