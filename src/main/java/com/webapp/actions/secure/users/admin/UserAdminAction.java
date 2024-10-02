package com.webapp.actions.secure.users.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.jeeutils.StringUtils;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.EncryptionUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UserModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/manage-admin-users")
public class UserAdminAction extends BusinessAction {

	private final static String OLD_PASSWORD = "oldPassword";
	private final static String NEW_PASSWORD = "newPassword";
	private final static String CONFIRM_PASSWORD = "confirmPassword";

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadAdminUsersList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.ADD_URL, UrlConstants.PAGE_URLS.ADD_ADMIN_USERS_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_ADMIN_USER_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAdminUserList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String ownUserId = null;

		if (!UserRoleUtils.isSuperAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			ownUserId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		int total = UserModel.getTotalUserCount(UserRoles.ADMIN_ROLE_ID, dtu.getStartDatelong(), dtu.getEndDatelong(), ownUserId, assignedRegionList, null);

		List<UserModel> userModelList = UserModel.getAdminUserListForSearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), UserRoles.ADMIN_ROLE_ID, dtu.getGlobalSearchStringWithPercentage(),
					dtu.getStartDatelong(), dtu.getEndDatelong(), assignedRegionList, ownUserId);

		int count = dtu.getStartInt();

		for (UserModel userProfileModel : userModelList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(userProfileModel.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(userProfileModel.getFirstName());
			dtuInnerJsonArray.put(userProfileModel.getLastName());
			dtuInnerJsonArray.put(userProfileModel.getEmail());
			dtuInnerJsonArray.put(userProfileModel.getPhoneNo());

			if (userProfileModel.isActive()) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.ACTIVE, messageForKeyAdmin("labelActive")));
			} else {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, messageForKeyAdmin("labelDeactive")));
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.EDIT, messageForKeyAdmin("labelEdit"), UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.EDIT_ADMIN_USERS_URL + "?userId=" + userProfileModel.getUserId())));
			btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(userProfileModel.getUserId(), "Admin user", UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.MANAGE_ADMIN_USERS_URL), userProfileModel.isActive() ? "userDeactivate" : "userReactivate"));

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? userModelList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/change-password")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response businessOwnerChangePassword(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@FormParam(OLD_PASSWORD) String oldPassword,
			@FormParam(NEW_PASSWORD) String newPassword,
			@FormParam(CONFIRM_PASSWORD) String confirmPassword
			) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		String loggedInUserId = "";

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);

		loggedInUserId = userInfo.get("user_id").toString();

		boolean hasErrors = false;

		data.put(OLD_PASSWORD, oldPassword);
		data.put(NEW_PASSWORD, newPassword);
		data.put(CONFIRM_PASSWORD, confirmPassword);

		hasErrors = hasPasswordErrorsEnglish();

		if (hasErrors) {

			return sendDataResponse(data);

		} else {

			Map<String, Object> passwordMap = new HashMap<String, Object>();

			UserModel dbUserModel = UserModel.getUserAccountDetailsById(loggedInUserId);

			if (dbUserModel == null) {

				passwordMap.put("type", "Failure");
				passwordMap.put("message", messageForKeyAdmin("labelNoSuchemailidexistsinrecords"));
				return sendDataResponse(passwordMap);
			}

			String password = EncryptionUtils.decryptByte(dbUserModel.getPassword());
			String passwordNew = EncryptionUtils.encryptString(newPassword);

			UserModel userModel = new UserModel();

			userModel.setUserId(loggedInUserId);

			if (!newPassword.equals(confirmPassword)) {

				passwordMap.put("type", "Failure");
				passwordMap.put("message", messageForKeyAdmin("labelNewpasswordandConfirmpasswordshouldbesame"));
				return sendDataResponse(passwordMap);
			}

			if (password.equals(oldPassword)) {

				userModel.setPassword(passwordNew);

			} else {

				passwordMap.put("type", "Failure");
				passwordMap.put("message", messageForKeyAdmin("errorInvalidOldPassword"));
				return sendDataResponse(passwordMap);
			}

			boolean result = userModel.updatePassword();

			if (result) {

				passwordMap.put("type", "Success");
				passwordMap.put("message", messageForKeyAdmin("successPasswordChanged"));
				return sendDataResponse(passwordMap);

			} else {

				passwordMap.put("type", "Failure");
				passwordMap.put("message", messageForKeyAdmin("errorPasswordChanged"));
				return sendDataResponse(passwordMap);
			}
		}
	}

	public boolean hasPasswordErrorsEnglish() {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping(OLD_PASSWORD, messageForKeyAdmin("labelOldPassword"), new RequiredValidationRule());
		validator.addValidationMapping(NEW_PASSWORD, messageForKeyAdmin("labelNewPassword"), new RequiredValidationRule());
		validator.addValidationMapping(CONFIRM_PASSWORD, messageForKeyAdmin("labelConfirmPassword"), new RequiredValidationRule());
		validator.addValidationMapping(NEW_PASSWORD, messageForKeyAdmin("labelNewPassword"), new MinMaxLengthValidationRule(6, 15));
		validator.addValidationMapping(CONFIRM_PASSWORD, messageForKeyAdmin("labelConfirmPassword"), new MinMaxLengthValidationRule(6, 15));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_ADMIN_USER_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}