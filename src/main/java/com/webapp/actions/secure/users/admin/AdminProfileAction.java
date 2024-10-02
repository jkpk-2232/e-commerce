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

import com.jeeutils.DateUtils;
import com.jeeutils.validator.EmailPresentForOtherUserValidationRule;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.PhoneNoPrefixZeroValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.TaxiSolsAlphaNumericDashCharactersValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.EncryptionUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.SessionUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.MulticityUserRegionModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;

@Path("/admin-profile")
public class AdminProfileAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response adminProfile(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasProfileAccess(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, DropDownUtils.getCountryCodeOptions(loggedInUserModelViaSession.getPhoneNoCode()));
		data.put(FieldConstants.USER_ID, loginSessionMap.get(LoginUtils.USER_ID));
		data.put(FieldConstants.FIRST_NAME, loggedInUserModelViaSession.getFirstName());
		data.put(FieldConstants.LAST_NAME, loggedInUserModelViaSession.getLastName());
		data.put(FieldConstants.EMAIL_ADDRESS, loggedInUserModelViaSession.getEmail());
		data.put(FieldConstants.ADDRESS, loggedInUserModelViaSession.getMailAddressLineOne());
		data.put(FieldConstants.CITY, loggedInUserModelViaSession.getMailCityId());
		data.put(FieldConstants.PHONE, loggedInUserModelViaSession.getPhoneNo().replace(loggedInUserModelViaSession.getPhoneNoCode(), ""));
		data.put(FieldConstants.REGIONS, null);

		if (!UserRoles.SUPER_ADMIN_ROLE.equalsIgnoreCase(loggedInUserModelViaSession.getUserRole())) {

			List<MulticityUserRegionModel> multicityUserRegionModelList = MulticityUserRegionModel.getMulticityUserRegionByUserId(loggedInUserModelViaSession.getUserId());

			StringBuilder sb = new StringBuilder();

			String assignedRegionList = null;

			if (multicityUserRegionModelList != null && multicityUserRegionModelList.size() > 0) {

				for (MulticityUserRegionModel multicityUserRegionModel : multicityUserRegionModelList) {

					sb.append(multicityUserRegionModel.getRegionName() + ",");
				}
				assignedRegionList = sb.toString();
				assignedRegionList = assignedRegionList.substring(0, assignedRegionList.length() - 1);
			}

			data.put(FieldConstants.REGIONS, assignedRegionList);
		}

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.ADMIN_PROFILE_URL);
		return loadView(UrlConstants.JSP_URLS.ADMIN_PROFILE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response editAdminProfile(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.USER_ID) String userIdLoc,
		@FormParam(FieldConstants.FIRST_NAME) String firstName,
		@FormParam(FieldConstants.LAST_NAME) String lastName,
		@FormParam(FieldConstants.EMAIL_ADDRESS) String emailAddress,
		@FormParam(FieldConstants.ADDRESS) String address,
		@FormParam(FieldConstants.PHONE) String phone,
		@FormParam(FieldConstants.COUNTRY_CODE) String countryCode,
		@FormParam(FieldConstants.CITY) String city
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasProfileAccess(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.USER_ID, loginSessionMap.get(LoginUtils.USER_ID));
		data.put(FieldConstants.FIRST_NAME, firstName);
		data.put(FieldConstants.LAST_NAME, lastName);
		data.put(FieldConstants.EMAIL_ADDRESS, emailAddress);
		data.put(FieldConstants.ADDRESS, address);
		data.put(FieldConstants.PHONE, phone);
		data.put(FieldConstants.CITY, city);
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, DropDownUtils.getCountryCodeOptions(countryCode));

		if (hasErrors(userIdLoc)) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.ADMIN_PROFILE_URL);
			return loadView(UrlConstants.JSP_URLS.ADMIN_PROFILE_JSP);
		}

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(userIdLoc);
		userProfileModel.setEmail(emailAddress);
		userProfileModel.setPhotoUrl(null);
		userProfileModel.setFirstName(firstName);
		userProfileModel.setLastName(lastName);
		userProfileModel.setPhoneNo(phone);
		userProfileModel.setPhoneNoCode(countryCode);
		userProfileModel.setMailAddressLineOne(address);
		userProfileModel.setMailStateId(null);
		userProfileModel.setMailCityId(city);
		userProfileModel.setUpdatedBy(userIdLoc);
		userProfileModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		userProfileModel.updateAdminProfile();

		SessionUtils.updateAttribute(request, response, LoginUtils.USER_FULL_NAME, MyHubUtils.formatFullName(userProfileModel.getFirstName(), userProfileModel.getLastName()));

		return redirectToPage(UrlConstants.PAGE_URLS.ADMIN_PROFILE_URL);
	}

	@Path("/change-password")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response changePassword(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.OLD_PASSWORD) String oldPassword,
		@FormParam(FieldConstants.NEW_PASSWORD) String newPassword,
		@FormParam(FieldConstants.CONFIRM_PASSWORD) String confirmPassword
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasProfileAccess(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.OLD_PASSWORD, oldPassword);
		data.put(FieldConstants.NEW_PASSWORD, newPassword);
		data.put(FieldConstants.CONFIRM_PASSWORD, confirmPassword);

		if (hasPasswordErrors()) {
			return sendDataResponse(data);
		}

		Map<String, Object> passwordMap = new HashMap<String, Object>();

		String currentDbPassword = EncryptionUtils.decryptByte(loggedInUserModelViaSession.getPassword());

		if (!currentDbPassword.equals(oldPassword)) {
			passwordMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			passwordMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorInvalidOldPassword"));
			return sendDataResponse(passwordMap);

		}

		if (!newPassword.equals(confirmPassword)) {
			passwordMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			passwordMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("labelNewpasswordandConfirmpasswordshouldbesame"));
			return sendDataResponse(passwordMap);
		}

		UserModel userModel = new UserModel();
		userModel.setUserId(loginSessionMap.get(LoginUtils.USER_ID));
		userModel.setPassword(EncryptionUtils.encryptString(newPassword));
		userModel.updatePassword();

		passwordMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
		passwordMap.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successPasswordChanged"));

		return sendDataResponse(passwordMap);
	}

	public boolean hasPasswordErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.OLD_PASSWORD, messageForKeyAdmin("labelOldPassword"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.NEW_PASSWORD, messageForKeyAdmin("labelNewPassword"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.CONFIRM_PASSWORD, messageForKeyAdmin("labelConfirmPassword"), new RequiredValidationRule());

		validator.addValidationMapping(FieldConstants.NEW_PASSWORD, messageForKeyAdmin("labelNewPassword"), new MinMaxLengthValidationRule(6, 15));
		validator.addValidationMapping(FieldConstants.CONFIRM_PASSWORD, messageForKeyAdmin("labelConfirmPassword"), new MinMaxLengthValidationRule(6, 15));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	public boolean hasErrors(String userId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.FIRST_NAME, messageForKeyAdmin("labelFirstName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.LAST_NAME, BusinessAction.messageForKeyAdmin("labelLastName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new EmailValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new EmailPresentForOtherUserValidationRule(userId, UserRoles.ADMIN_ROLE_ID));
		validator.addValidationMapping(FieldConstants.ADDRESS, messageForKeyAdmin("labelAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.CITY, messageForKeyAdmin("labelCity"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADMIN_PROFILE_JS, UrlConstants.JS_URLS.CHANGE_PASSWORD_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}