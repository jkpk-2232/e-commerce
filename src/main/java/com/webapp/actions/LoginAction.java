package com.webapp.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.EncryptionUtils;
import com.utils.LoginUtils;
import com.utils.dbsession.CookieUtils;
import com.utils.dbsession.DbSession;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.SessionUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.models.RoleModel;
import com.webapp.models.UserLoginOtpModel;
import com.webapp.models.UserModel;

@Path("")
public class LoginAction extends BusinessAction {

	@GET
	@Path("/login")
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAdminLoginPage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		)throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		DbSession dbSession = SessionUtils.getDbSessionObject(request, response, false);
		Cookie cookie = CookieUtils.getCookie(DbSession.REMEMBER_ME_SESSION_COOKIE_NAME, request);

		if (cookie != null) {

			String rememberMeId = cookie.getValue();

			if (rememberMeId != null && rememberMeId.length() > 0) {

				String userId = LoginUtils.getUserIdByRememberMeId(rememberMeId);

				if (userId != null && userId.length() > 0) {

					if (dbSession == null) {

						UserModel userModel = UserModel.getUserAccountDetailsById(userId);

						if (userModel != null) {

							if (RoleModel.isUserHasLoginAccess(userId)) {

								DbSession newSession = LoginUtils.createSession(userModel, request, response);

								String url = LoginUtils.processPostLogin(userId, newSession);

								if (!StringUtils.validString(url)) {
									return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
								}

								return redirectToPage(url);
							}
						}
					}
				}
			}
		}

		if (LoginUtils.checkLogIn(request, response)) {

			UserModel userModel = UserModel.getUserAccountDetailsById(dbSession.getAttribute(LoginUtils.USER_ID));

			if (userModel == null) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			if (!RoleModel.isUserHasLoginAccess(userModel.getUserId())) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			if (dbSession != null) {

				String fUrl = getPriorityUrlByUserd(userModel.getUserId());

				if (!StringUtils.validString(fUrl)) {
					return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
				}

				return redirectToPage(fUrl);
			}
		}

		data.put(FieldConstants.ROLE_ID, UserRoles.ADMIN_ROLE_ID);

		return loadView(UrlConstants.JSP_URLS.LOGIN_JSP);
	}

	@POST
	@Path("/login")
	@Produces(MediaType.TEXT_HTML)
	@Consumes("application/x-www-form-urlencoded")
	//@formatter:off
	public Response addPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@FormParam(FieldConstants.EMAIL) String email, 
		@FormParam(FieldConstants.PASSWORD) String password
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		email = email.trim();

		data.put(FieldConstants.ROLE_ID, UserRoles.ADMIN_ROLE_ID);
		data.put(FieldConstants.EMAIL, email);
		data.put(FieldConstants.PASSWORD, password);
		data.put("invalidLoginError", "");

		List<String> roleIdList = new ArrayList<String>();
		roleIdList.add(UserRoles.SUPER_ADMIN_ROLE_ID);
		roleIdList.add(UserRoles.ADMIN_ROLE_ID);

		UserModel userModel = LoginUtils.getUserModelWithRoleIds(roleIdList, email, EncryptionUtils.encryptString(password));

		if (LoginUtils.checkLogIn(request, response)) {

			if (userModel == null) {
				return loadView(UrlConstants.JSP_URLS.LOGIN_JSP);
			}

			userModel.setTimezone(TimeZoneUtils.getTimeZone());

			if (!RoleModel.isUserHasLoginAccess(userModel.getUserId())) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			String url = getPriorityUrlByUserd(userModel.getUserId());

			if (!StringUtils.validString(url)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			return redirectToPage(url);
		}

		boolean hasErrors = hasErrors();

		if (hasErrors) {
			return loadView(UrlConstants.JSP_URLS.LOGIN_JSP);
		}

		if (userModel == null) {
			data.put("invalidLoginError", messageForKeyAdmin("errorInvalidLoginCredentials"));
			return loadView(UrlConstants.JSP_URLS.LOGIN_JSP);
		}

		data.put(FieldConstants.USER_ID, userModel.getUserId());
		data.put(FieldConstants.PHONE_NUMBER, userModel.getPhoneNo());

		UserLoginOtpModel.deleteVerificationCode(userModel.getUserId());

		String verificationCode = MyHubUtils.generateVerificationCode();

		UserLoginOtpModel userLoginOtpModel = new UserLoginOtpModel();
		userLoginOtpModel.setUserId(userModel.getUserId());
		userLoginOtpModel.setRoleId(userModel.getRoleId());
		userLoginOtpModel.setVerificationCode(verificationCode);

		String userLoginOtpId = userLoginOtpModel.addVerificationCode(userModel.getUserId());

		UserModel userLoggedIn = UserModel.getUserAccountDetailsById(userModel.getUserId());

		String message = String.format(messageForKeyAdmin("successMobileVerficationCode"), userLoggedIn.getFullName(), BusinessApiAction.convertVerificationCode(verificationCode));
		MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, userLoggedIn.getPhoneNoCode() + userLoggedIn.getPhoneNo(), ProjectConstants.SMSConstants.SMS_OTP_TEMPLATE_ID);

		return redirectToPage("/verify-otp/" + userLoginOtpId + ".do");
	}

	@GET
	@Path("/vendor/login")
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVendorLoginPage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		)throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		DbSession dbSession = SessionUtils.getDbSessionObject(request, response, false);
		Cookie cookie = CookieUtils.getCookie(DbSession.REMEMBER_ME_SESSION_COOKIE_NAME, request);

		if (cookie != null) {

			String rememberMeId = cookie.getValue();

			if (rememberMeId != null && rememberMeId.length() > 0) {

				String userId = LoginUtils.getUserIdByRememberMeId(rememberMeId);

				if (userId != null && userId.length() > 0) {

					if (dbSession == null) {

						UserModel userModel = UserModel.getUserAccountDetailsById(userId);

						if (userModel != null) {

							if (RoleModel.isUserHasLoginAccess(userId)) {

								DbSession newSession = LoginUtils.createSession(userModel, request, response);

								String url = LoginUtils.processPostLogin(userId, newSession);

								if (!StringUtils.validString(url)) {
									return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
								}

								return redirectToPage(url);
							}
						}
					}
				}
			}
		}

		if (LoginUtils.checkLogIn(request, response)) {

			UserModel userModel = UserModel.getUserAccountDetailsById(dbSession.getAttribute(LoginUtils.USER_ID));

			if (userModel == null) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			if (!RoleModel.isUserHasLoginAccess(userModel.getUserId())) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			if (dbSession != null) {

				String fUrl = getPriorityUrlByUserd(userModel.getUserId());

				if (!StringUtils.validString(fUrl)) {
					return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
				}

				return redirectToPage(fUrl);
			}
		}

		data.put(FieldConstants.ROLE_ID, UserRoles.VENDOR_ROLE_ID);

		return loadView(UrlConstants.JSP_URLS.VENDOR_LOGIN_JSP);
	}

	@POST
	@Path("/vendor/login")
	@Produces(MediaType.TEXT_HTML)
	@Consumes("application/x-www-form-urlencoded")
	//@formatter:off
	public Response vendorLogin(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@FormParam(FieldConstants.EMAIL) String email, 
		@FormParam(FieldConstants.PASSWORD) String password
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		email = email.trim();

		data.put(FieldConstants.ROLE_ID, UserRoles.VENDOR_ROLE_ID);
		data.put(FieldConstants.EMAIL, email);
		data.put(FieldConstants.PASSWORD, password);
		data.put("invalidLoginError", "");

		List<String> roleIdList = new ArrayList<String>();
		roleIdList.add(UserRoles.VENDOR_ROLE_ID);
		roleIdList.add(UserRoles.SUB_VENDOR_ROLE_ID);

		UserModel userModel = LoginUtils.getUserModelWithRoleIds(roleIdList, email, EncryptionUtils.encryptString(password));

		if (LoginUtils.checkLogIn(request, response)) {

			if (userModel == null) {
				return loadView(UrlConstants.JSP_URLS.VENDOR_LOGIN_JSP);
			}

			userModel.setTimezone(TimeZoneUtils.getTimeZone());

			if (!RoleModel.isUserHasLoginAccess(userModel.getUserId())) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			String url = getPriorityUrlByUserd(userModel.getUserId());

			if (!StringUtils.validString(url)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			return redirectToPage(url);
		}

		boolean hasErrors = hasErrors();

		if (hasErrors) {
			return loadView(UrlConstants.JSP_URLS.VENDOR_LOGIN_JSP);
		}

		if (userModel == null) {
			data.put("invalidLoginError", messageForKeyAdmin("errorInvalidLoginCredentials"));
			return loadView(UrlConstants.JSP_URLS.VENDOR_LOGIN_JSP);
		}

		data.put(FieldConstants.USER_ID, userModel.getUserId());
		data.put(FieldConstants.PHONE_NUMBER, userModel.getPhoneNo());

		UserLoginOtpModel.deleteVerificationCode(userModel.getUserId());

		String verificationCode = MyHubUtils.generateVerificationCode();

		UserLoginOtpModel userLoginOtpModel = new UserLoginOtpModel();
		userLoginOtpModel.setUserId(userModel.getUserId());
		userLoginOtpModel.setRoleId(userModel.getRoleId());
		userLoginOtpModel.setVerificationCode(verificationCode);

		String userLoginOtpId = userLoginOtpModel.addVerificationCode(userModel.getUserId());

		UserModel userLoggedIn = UserModel.getUserAccountDetailsById(userModel.getUserId());

		String message = String.format(messageForKeyAdmin("successMobileVerficationCode"), userLoggedIn.getFullName(), BusinessApiAction.convertVerificationCode(verificationCode));
		MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, userLoggedIn.getPhoneNoCode() + userLoggedIn.getPhoneNo(), ProjectConstants.SMSConstants.SMS_OTP_TEMPLATE_ID);

		return redirectToPage("/verify-otp/" + userLoginOtpId + ".do");
	}
	
	@GET
	@Path("/brand/login")
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getErpLoginPage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		)throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		DbSession dbSession = SessionUtils.getDbSessionObject(request, response, false);
		Cookie cookie = CookieUtils.getCookie(DbSession.REMEMBER_ME_SESSION_COOKIE_NAME, request);

		if (cookie != null) {

			String rememberMeId = cookie.getValue();

			if (rememberMeId != null && rememberMeId.length() > 0) {

				String userId = LoginUtils.getUserIdByRememberMeId(rememberMeId);

				if (userId != null && userId.length() > 0) {

					if (dbSession == null) {

						UserModel userModel = UserModel.getUserAccountDetailsById(userId);

						if (userModel != null) {

							if (RoleModel.isUserHasLoginAccess(userId)) {

								DbSession newSession = LoginUtils.createSession(userModel, request, response);

								String url = LoginUtils.processPostLogin(userId, newSession);

								if (!StringUtils.validString(url)) {
									return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
								}

								return redirectToPage(url);
							}
						}
					}
				}
			}
		}

		if (LoginUtils.checkLogIn(request, response)) {

			UserModel userModel = UserModel.getUserAccountDetailsById(dbSession.getAttribute(LoginUtils.USER_ID));

			if (userModel == null) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			if (!RoleModel.isUserHasLoginAccess(userModel.getUserId())) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			if (dbSession != null) {

				String fUrl = getPriorityUrlByUserd(userModel.getUserId());

				if (!StringUtils.validString(fUrl)) {
					return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
				}

				return redirectToPage(fUrl);
			}
		}

		data.put(FieldConstants.ROLE_ID, UserRoles.ERP_ROLE_ID);

		return loadView(UrlConstants.JSP_URLS.BRAND_LOGIN_JSP);
	}
	
	@POST
	@Path("/brand/login")
	@Produces(MediaType.TEXT_HTML)
	@Consumes("application/x-www-form-urlencoded")
	//@formatter:off
	public Response brandLogin(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@FormParam(FieldConstants.EMAIL) String email, 
		@FormParam(FieldConstants.PASSWORD) String password
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		email = email.trim();

		data.put(FieldConstants.ROLE_ID, UserRoles.ERP_ROLE_ID);
		data.put(FieldConstants.EMAIL, email);
		data.put(FieldConstants.PASSWORD, password);
		data.put("invalidLoginError", "");

		List<String> roleIdList = new ArrayList<String>();
		roleIdList.add(UserRoles.ERP_ROLE_ID);
		roleIdList.add(UserRoles.ERP_EMPLOYEE_ROLE_ID);

		UserModel userModel = LoginUtils.getUserModelWithRoleIds(roleIdList, email, EncryptionUtils.encryptString(password));

		if (LoginUtils.checkLogIn(request, response)) {

			if (userModel == null) {
				return loadView(UrlConstants.JSP_URLS.BRAND_LOGIN_JSP);
			}

			userModel.setTimezone(TimeZoneUtils.getTimeZone());

			if (!RoleModel.isUserHasLoginAccess(userModel.getUserId())) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			String url = getPriorityUrlByUserd(userModel.getUserId());

			if (!StringUtils.validString(url)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			return redirectToPage(url);
		}

		boolean hasErrors = hasErrors();

		if (hasErrors) {
			return loadView(UrlConstants.JSP_URLS.BRAND_LOGIN_JSP);
		}

		if (userModel == null) {
			data.put("invalidLoginError", messageForKeyAdmin("errorInvalidLoginCredentials"));
			return loadView(UrlConstants.JSP_URLS.BRAND_LOGIN_JSP);
		}

		data.put(FieldConstants.USER_ID, userModel.getUserId());
		data.put(FieldConstants.PHONE_NUMBER, userModel.getPhoneNo());

		UserLoginOtpModel.deleteVerificationCode(userModel.getUserId());

		String verificationCode = MyHubUtils.generateVerificationCode();

		UserLoginOtpModel userLoginOtpModel = new UserLoginOtpModel();
		userLoginOtpModel.setUserId(userModel.getUserId());
		userLoginOtpModel.setRoleId(userModel.getRoleId());
		userLoginOtpModel.setVerificationCode(verificationCode);

		String userLoginOtpId = userLoginOtpModel.addVerificationCode(userModel.getUserId());

		UserModel userLoggedIn = UserModel.getUserAccountDetailsById(userModel.getUserId());

		String message = String.format(messageForKeyAdmin("successMobileVerficationCode"), userLoggedIn.getFullName(), BusinessApiAction.convertVerificationCode(verificationCode));
		MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, userLoggedIn.getPhoneNoCode() + userLoggedIn.getPhoneNo(), ProjectConstants.SMSConstants.SMS_OTP_TEMPLATE_ID);

		return redirectToPage("/verify-otp/" + userLoginOtpId + ".do");
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.EMAIL, messageForKeyAdmin("labelEmail"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL, messageForKeyAdmin("labelEmail"), new EmailValidationRule());
		validator.addValidationMapping(FieldConstants.PASSWORD, messageForKeyAdmin("labelPassword"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (!resultsForValidation.isEmpty()) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		return new String[] { UrlConstants.JS_URLS.LOGIN_JS };
	}
}