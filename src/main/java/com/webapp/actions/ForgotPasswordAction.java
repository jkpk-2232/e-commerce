package com.webapp.actions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.StringUtils;
import com.utils.EncryptionUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.models.UserModel;

@Path("/forgot-password")
public class ForgotPasswordAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response forgotPasswordForm(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.ROLE_ID) String roleId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		data.put(FieldConstants.ROLE_ID, roleId);

		if (UserRoleUtils.isVendorRole(roleId)) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.VENDOR_LOGIN_URL);
		} else {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.LOGIN_URL);
		}

		return loadView(UrlConstants.JSP_URLS.FORGOT_PASSWORD_JSP);
	}

	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response addPost(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.EMAIL) String email,
		@FormParam(FieldConstants.ROLE_ID) String roleId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		Map<String, Object> forgotPasswordDetailsMap = new HashMap<String, Object>();

		if (!StringUtils.validString(email)) {
			forgotPasswordDetailsMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			forgotPasswordDetailsMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorEmailIsRequiredMsg"));
			return sendDataResponse(forgotPasswordDetailsMap);
		}

		List<String> roleIds = UserRoleUtils.getRoleIdsListForLogin(roleId);

		UserModel userModelForAccountCheck = UserModel.getUserAccountDetailsByRoleIdAndEmailId(roleIds, email.trim());

		if (userModelForAccountCheck == null) {
			forgotPasswordDetailsMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			forgotPasswordDetailsMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorAccountNotExists"));
			return sendDataResponse(forgotPasswordDetailsMap);
		}

		if (!UserRoleUtils.hasAdminForgotPasswordAccess(userModelForAccountCheck.getRoleId())) {
			forgotPasswordDetailsMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			forgotPasswordDetailsMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorInvalidUser"));
			return sendDataResponse(forgotPasswordDetailsMap);
		}

		String password = EncryptionUtils.decryptByte(userModelForAccountCheck.getPassword());

		if (password == null) {
			forgotPasswordDetailsMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			forgotPasswordDetailsMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorFailedToGetPassword"));
			return sendDataResponse(forgotPasswordDetailsMap);
		}

		String subject = String.format(BusinessAction.messageForKeyAdmin("labelLoginCredentials", ProjectConstants.ENGLISH_ID), WebappPropertyUtils.PROJECT_NAME);
		String message = Action.getForgotPasswordMessage(userModelForAccountCheck.getEmail(), password);

		if (UserRoleUtils.isVendorRole(userModelForAccountCheck.getRoleId())) {
			message = Action.getForgotPasswordMessageForVendor(userModelForAccountCheck.getEmail(), password);
		}

		MyHubNotificationUtils.sendEmailToUser(userModelForAccountCheck.getEmail(), subject, message);

		String tempUrl = "";

		if (UserRoleUtils.isVendorRole(userModelForAccountCheck.getRoleId())) {
			tempUrl = UrlConstants.PAGE_URLS.VENDOR_LOGIN_URL;
		} else {
			tempUrl = UrlConstants.PAGE_URLS.LOGIN_URL;
		}

		forgotPasswordDetailsMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		forgotPasswordDetailsMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successAccountDetailsSentToRegisteredEmailMsg"));
		forgotPasswordDetailsMap.put(FieldConstants.SUCCESS_PAGE_REDIRECT_URL, tempUrl);
		return sendDataResponse(forgotPasswordDetailsMap);
	}

	@Override
	protected String[] requiredJs() {
		return new String[] { UrlConstants.JS_URLS.FORGOT_PASSWORD_JS };
	}
}