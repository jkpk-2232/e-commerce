package com.webapp.actions.secure.users.admin;

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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.validator.DuplicatePhoneNumberValidationRule;
import com.jeeutils.validator.EmailPresentForOtherUserValidationRule;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.MaxLengthValidationRule;
import com.jeeutils.validator.MinMaxFareValidationRule;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.PhoneNoPrefixZeroValidationRule;
import com.jeeutils.validator.RequiredListValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.TaxiSolsAlphaNumericDashCharactersValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MulticityRegionUtils;
import com.utils.myhub.UrlAccessUtils;
import com.utils.myhub.UserExportUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UserProfileModel;

@Path("/edit-admin-user")
public class EditAdminUserAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadEditAdminUserForm(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.USER_ID) String userId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(userId);
		if (userProfileModel == null) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.USER_ID, userId);
		data.put(FieldConstants.FIRST_NAME, userProfileModel.getFirstName());
		data.put(FieldConstants.LAST_NAME, userProfileModel.getLastName());
		data.put(FieldConstants.EMAIL_ADDRESS, userProfileModel.getEmail());
		data.put(FieldConstants.ADDRESS, userProfileModel.getMailAddressLineOne());
		data.put(FieldConstants.CITY, userProfileModel.getMailCityId() + "");
		data.put(FieldConstants.PHONE, userProfileModel.getPhoneNo().replace(userProfileModel.getPhoneNoCode(), ""));
		data.put(FieldConstants.MAXIMUM_MARKUP_FARE, Double.toString(userProfileModel.getMaximumMarkupFare()));

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(userProfileModel.getPhoneNoCode());
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

		List<String> urlAccessList = UrlAccessUtils.getUserAccessList(userId);
		String accessListOptions = DropDownUtils.getAdminAccessListOptions(urlAccessList);
		data.put(FieldConstants.ACCESS_LIST_OPTIONS, accessListOptions);

		List<String> exportAccessList = UserExportUtils.setAdminExportUrlAccessList(userId);
		String exportAccessListOptions = DropDownUtils.getAdminExportAccessListOptions(exportAccessList);
		data.put(FieldConstants.EXPORTACCESSLIST_OPTIONS, exportAccessListOptions);

		List<String> regionAccessList = MulticityRegionUtils.getUserAccessRegionList(userId);
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(regionAccessList, assignedRegionList);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ADMIN_USERS_URL);
		return loadView(UrlConstants.JSP_URLS.EDIT_ADMIN_USER_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response editAdminUserForm(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.USER_ID) String userId,
		@FormParam(FieldConstants.REGION_LIST) List<String> regionList,
		@FormParam(FieldConstants.FIRST_NAME) String firstName,
		@FormParam(FieldConstants.LAST_NAME) String lastName,
		@FormParam(FieldConstants.EMAIL_ADDRESS) String emailAddress,
		@FormParam(FieldConstants.ADDRESS) String address,
		@FormParam(FieldConstants.PHONE) String phone,
		@FormParam(FieldConstants.COUNTRY_CODE) String countryCode,
		@FormParam(FieldConstants.CITY) String city,
		@FormParam(FieldConstants.MAXIMUM_MARKUP_FARE) String maximumMarkupFare,
		@FormParam(FieldConstants.EXPORTACCESSLIST) List<String> exportAccessList,
		@FormParam(FieldConstants.ACCESS_LIST) List<String> accessList
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(userId);
		if (userProfileModel == null) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String loggedInUserId = loginSessionMap.get(LoginUtils.USER_ID);
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loggedInUserId);

		data.put(FieldConstants.USER_ID, userId);
		data.put(FieldConstants.FIRST_NAME, firstName);
		data.put(FieldConstants.LAST_NAME, lastName);
		data.put(FieldConstants.EMAIL_ADDRESS, emailAddress);
		data.put(FieldConstants.ADDRESS, address);
		data.put(FieldConstants.PHONE, phone);
		data.put(FieldConstants.CITY, city);
		data.put(FieldConstants.MAXIMUM_MARKUP_FARE, maximumMarkupFare);
		data.put(FieldConstants.REGION_LIST, regionList + "");

		String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(regionList, assignedRegionList);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		if (hasErrors(userId, regionList, countryCode)) {

			String countryCodeOptions = DropDownUtils.getCountryCodeOptions(countryCode);
			data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

			String exportAccessListOptions = DropDownUtils.getAdminExportAccessListOptions(exportAccessList);
			data.put(FieldConstants.EXPORTACCESSLIST_OPTIONS, exportAccessListOptions);

			String accessListOptions = DropDownUtils.getAdminAccessListOptions(accessList);
			data.put(FieldConstants.ACCESS_LIST_OPTIONS, accessListOptions);

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ADMIN_USERS_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_ADMIN_USER_JSP);
		}

		userProfileModel.setUserId(userId);
		userProfileModel.setEmail(emailAddress);
		userProfileModel.setPhotoUrl(null);
		userProfileModel.setFirstName(firstName);
		userProfileModel.setLastName(lastName);
		userProfileModel.setPhoneNo(phone);
		userProfileModel.setPhoneNoCode(countryCode);
		userProfileModel.setMailAddressLineOne(address);
		userProfileModel.setMaximumMarkupFare(Double.parseDouble(maximumMarkupFare));
		userProfileModel.setMailStateId(null);
		userProfileModel.setMailCityId(city);
		userProfileModel.setUpdatedBy(loggedInUserId);
		userProfileModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		userProfileModel.setActive(true);

		userProfileModel.updateUser();

		UrlAccessUtils.processAdminUrlAccesses(userId, accessList);

		MulticityRegionUtils.addUserRegions(regionList, loggedInUserId, userId, UserRoles.ADMIN_ROLE_ID);

		UserExportUtils.processAdminUserExport(exportAccessList, loggedInUserId, userId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_ADMIN_USERS_URL);
	}

	public boolean hasErrors(String userId, List<String> regionList, String countryCode) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.FIRST_NAME, messageForKeyAdmin("labelFirstName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.LAST_NAME, messageForKeyAdmin("labelLastName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new EmailValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new MaxLengthValidationRule(200));
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new EmailPresentForOtherUserValidationRule(userId, UserRoles.ADMIN_ROLE_ID));
		validator.addValidationMapping(FieldConstants.ADDRESS, messageForKeyAdmin("labelAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new DuplicatePhoneNumberValidationRule(countryCode, UserRoles.ADMIN_ROLE_ID, userId, null));
		validator.addValidationMapping(FieldConstants.CITY, messageForKeyAdmin("labelCity"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.REGION_LIST, messageForKeyAdmin("labelRegion"), new RequiredListValidationRule(regionList.size()));
		validator.addValidationMapping(FieldConstants.MAXIMUM_MARKUP_FARE, messageForKeyAdmin("labelMarkupFare"), new MinMaxFareValidationRule(0, 5000));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_ADMIN_USER_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}