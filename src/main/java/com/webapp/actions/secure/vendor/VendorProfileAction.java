package com.webapp.actions.secure.vendor;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
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
import com.jeeutils.StringUtils;
import com.jeeutils.validator.DuplicatePhoneNumberValidationRule;
import com.jeeutils.validator.EmailPresentForOtherUserValidationRule;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.PhoneNoPrefixZeroValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.TaxiSolsAlphaNumericDashCharactersValidationRule;
import com.jeeutils.validator.Validator;
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
import com.webapp.models.UserAccountModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorServiceCategoryModel;

@Path("/vendor-profile")
public class VendorProfileAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response vendorProfile(
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

		UserAccountModel userAccountModel = UserAccountModel.getAccountBalanceDetailsByUserId(loginSessionMap.get(LoginUtils.USER_ID));
		data.put(FieldConstants.CURRENT_BALANCE, userAccountModel != null ? StringUtils.valueOf(userAccountModel.getCurrentBalance()) : "0");

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(loginSessionMap.get(LoginUtils.USER_ID));

		data.put(FieldConstants.SERVICE_NAME, vscm != null ? vscm.getServiceName() : ProjectConstants.NOT_AVAILABLE);
		data.put(FieldConstants.CATEGORY_NAME, vscm != null ? vscm.getCategoryName() : ProjectConstants.NOT_AVAILABLE);

		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN, loggedInUserModelViaSession.getPhotoUrl());
		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, StringUtils.validString(loggedInUserModelViaSession.getPhotoUrl()) ? loggedInUserModelViaSession.getPhotoUrl() : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.VENDOR_PROFILE_URL);
		return loadView(UrlConstants.JSP_URLS.VENDOR_PROFILE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response editVendorProfile(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.FIRST_NAME) String firstName,
		@FormParam(FieldConstants.LAST_NAME) String lastName,
		@FormParam(FieldConstants.EMAIL_ADDRESS) String emailAddress,
		@FormParam(FieldConstants.ADDRESS) String address,
		@FormParam(FieldConstants.PHONE) String phone,
		@FormParam(FieldConstants.COUNTRY_CODE) String countryCode,
		@FormParam(FieldConstants.CITY) String city,
		@FormParam(FieldConstants.PROFILE_IMAGE_URL_HIDDEN) String hiddenPhotoUrl
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
		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN, hiddenPhotoUrl);
		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenPhotoUrl) ? hiddenPhotoUrl : ProjectConstants.DEFAULT_IMAGE);

		if (hasErrors(loginSessionMap.get(LoginUtils.USER_ID), countryCode)) {

			VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(loginSessionMap.get(LoginUtils.USER_ID));
			data.put(FieldConstants.SERVICE_NAME, vscm != null ? vscm.getServiceName() : ProjectConstants.NOT_AVAILABLE);
			data.put(FieldConstants.CATEGORY_NAME, vscm != null ? vscm.getCategoryName() : ProjectConstants.NOT_AVAILABLE);

			UserAccountModel userAccountModel = UserAccountModel.getAccountBalanceDetailsByUserId(loginSessionMap.get(LoginUtils.USER_ID));
			data.put(FieldConstants.CURRENT_BALANCE, userAccountModel != null ? StringUtils.valueOf(userAccountModel.getCurrentBalance()) : "0");

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.VENDOR_PROFILE_URL);
			return loadView(UrlConstants.JSP_URLS.VENDOR_PROFILE_JSP);
		}

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(loginSessionMap.get(LoginUtils.USER_ID));
		userProfileModel.setEmail(emailAddress);
		userProfileModel.setPhotoUrl(null);
		userProfileModel.setFirstName(firstName);
		userProfileModel.setLastName(lastName);
		userProfileModel.setPhoneNo(phone);
		userProfileModel.setPhoneNoCode(countryCode);
		userProfileModel.setMailAddressLineOne(address);
		userProfileModel.setMailStateId(null);
		userProfileModel.setMailCityId(city);
		userProfileModel.setUpdatedBy(loginSessionMap.get(LoginUtils.USER_ID));
		userProfileModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		userProfileModel.setPhotoUrl(hiddenPhotoUrl);

		userProfileModel.updateAdminProfile();

		SessionUtils.updateAttribute(request, response, LoginUtils.USER_FULL_NAME, MyHubUtils.formatFullName(userProfileModel.getFirstName(), userProfileModel.getLastName()));
		SessionUtils.updateAttribute(request, response, LoginUtils.PHOTO_URL, hiddenPhotoUrl);

		return redirectToPage(UrlConstants.PAGE_URLS.VENDOR_PROFILE_URL);
	}

	public boolean hasErrors(String userId, String countryCode) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.FIRST_NAME, messageForKeyAdmin("labelFirstName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.LAST_NAME, BusinessAction.messageForKeyAdmin("labelLastName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new EmailValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new EmailPresentForOtherUserValidationRule(userId, UserRoles.VENDOR_ROLE_ID));
		validator.addValidationMapping(FieldConstants.ADDRESS, messageForKeyAdmin("labelAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.CITY, messageForKeyAdmin("labelCity"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new DuplicatePhoneNumberValidationRule(countryCode, UserRoles.DRIVER_ROLE_ID, userId, null));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.VENDOR_PROFILE_JS, UrlConstants.JS_URLS.CHANGE_PASSWORD_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}