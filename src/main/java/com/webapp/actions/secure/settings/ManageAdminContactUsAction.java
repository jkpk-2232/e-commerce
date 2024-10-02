package com.webapp.actions.secure.settings;

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

import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.PhoneNoPrefixZeroValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminCompanyContactModel;

@Path("/manage-admin-contact-us")
public class ManageAdminContactUsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAdminContactUs(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		AdminCompanyContactModel adminCompanyContactModel = AdminCompanyContactModel.getAdminCompanyContactByVendorId(loginSessionMap.get(LoginUtils.USER_ID));

		if (adminCompanyContactModel == null) {
			adminCompanyContactModel = AdminCompanyContactModel.getAdminCompanyContactByVendorId(UserRoles.SUPER_ADMIN_USER_ID);
		}

		String countryCodeOneOptions = DropDownUtils.getCountryCodeOptions(adminCompanyContactModel.getPhoneNumberOneCode());
		data.put(FieldConstants.COUNTRY_CODE_ONE_OPTIONS, countryCodeOneOptions);

		String countryCodeTwoOptions = DropDownUtils.getCountryCodeOptions(adminCompanyContactModel.getPhoneNumberTwoCode());
		data.put(FieldConstants.COUNTRY_CODE_TWO_OPTIONS, countryCodeTwoOptions);

		data.put(FieldConstants.ADMIN_COMPANY_CONTACT_ID, adminCompanyContactModel.getAdminCompanyContactId());
		data.put(FieldConstants.FAX, adminCompanyContactModel.getFax());
		data.put(FieldConstants.EMAIL, adminCompanyContactModel.getEmail());
		data.put(FieldConstants.LATITUDE, adminCompanyContactModel.getLatitude());
		data.put(FieldConstants.LONGITUDE, adminCompanyContactModel.getLongitude());
		data.put(FieldConstants.ADDRESS, adminCompanyContactModel.getAddress());
		data.put(FieldConstants.PHONE_NUMBER_ONE, adminCompanyContactModel.getPhoneNumberOne().replace(adminCompanyContactModel.getPhoneNumberOneCode(), ""));
		data.put(FieldConstants.PHONE_NUMBER_TWO, adminCompanyContactModel.getPhoneNumberTwo().replace(adminCompanyContactModel.getPhoneNumberTwoCode(), ""));

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ADMIN_CONTACT_US_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_ADMIN_CONTACT_US_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response postSettings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.ADDRESS) String address,
		@FormParam(FieldConstants.AREA_NAME) String areaName,
		@FormParam(FieldConstants.FAX) String fax,
		@FormParam(FieldConstants.EMAIL) String email,
		@FormParam(FieldConstants.LATITUDE) String latitude,
		@FormParam(FieldConstants.LONGITUDE) String longitude,
		@FormParam(FieldConstants.PHONE_NUMBER_ONE) String phoneNumberOne,
		@FormParam(FieldConstants.PHONE_NUMBER_TWO) String phoneNumberTwo,
		@FormParam(FieldConstants.COUNTRY_CODE_ONE) String countryCodeOne,
		@FormParam(FieldConstants.COUNTRY_CODE_TWO) String countryCodeTwo,
		@FormParam(FieldConstants.ADMIN_COMPANY_CONTACT_ID) String adminCompanyContactId
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String countryCodeOneOptions = DropDownUtils.getCountryCodeOptions(countryCodeOne);
		data.put(FieldConstants.COUNTRY_CODE_ONE_OPTIONS, countryCodeOneOptions);

		String countryCodeTwoOptions = DropDownUtils.getCountryCodeOptions(countryCodeTwo);
		data.put(FieldConstants.COUNTRY_CODE_TWO_OPTIONS, countryCodeTwoOptions);

		data.put(FieldConstants.AREA_NAME, areaName);
		data.put(FieldConstants.FAX, fax);
		data.put(FieldConstants.EMAIL, email);
		data.put(FieldConstants.ADDRESS, address);
		data.put(FieldConstants.PHONE_NUMBER_ONE, phoneNumberOne);
		data.put(FieldConstants.PHONE_NUMBER_TWO, phoneNumberTwo);
		data.put(FieldConstants.LATITUDE, latitude);
		data.put(FieldConstants.LONGITUDE, longitude);
		data.put(FieldConstants.ADMIN_COMPANY_CONTACT_ID, adminCompanyContactId);

		if (hasErrorsForEnglish()) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ADMIN_CONTACT_US_URL);

			return loadView(UrlConstants.JSP_URLS.MANAGE_ADMIN_CONTACT_US_JSP);
		}

		boolean insert = false;

		AdminCompanyContactModel adminCompanyContactModel = AdminCompanyContactModel.getAdminCompanyContactByVendorId(loginSessionMap.get(LoginUtils.USER_ID));

		if (adminCompanyContactModel == null) {
			insert = true;
			adminCompanyContactModel = new AdminCompanyContactModel();
		}

		adminCompanyContactModel.setAddress(address.trim());
		adminCompanyContactModel.setPhoneNumberOne(countryCodeOne + phoneNumberOne);
		adminCompanyContactModel.setPhoneNumberTwo(countryCodeTwo + phoneNumberTwo);
		adminCompanyContactModel.setLatitude(latitude.trim());
		adminCompanyContactModel.setLongitude(longitude.trim());
		adminCompanyContactModel.setPhoneNumberOneCode(countryCodeOne);
		adminCompanyContactModel.setPhoneNumberTwoCode(countryCodeTwo);
		adminCompanyContactModel.setFax(fax);
		adminCompanyContactModel.setEmail(email);

		if (insert) {
			adminCompanyContactModel.addAdminCompanyContact(loginSessionMap.get(LoginUtils.USER_ID));
		} else {
			adminCompanyContactModel.updateAdminCompanyContact(loginSessionMap.get(LoginUtils.USER_ID));
		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_ADMIN_CONTACT_US_URL);
	}

	public boolean hasErrorsForEnglish() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.AREA_NAME, messageForKeyAdmin("labelAreaName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.ADDRESS, messageForKeyAdmin("labelAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.ADDRESS, messageForKeyAdmin("labelAddress"), new MinMaxLengthValidationRule(10, 500));
		validator.addValidationMapping(FieldConstants.FAX, messageForKeyAdmin("labelFax"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL, messageForKeyAdmin("labelEmail"), new RequiredValidationRule());

		validator.addValidationMapping(FieldConstants.PHONE_NUMBER_ONE, messageForKeyAdmin("labelPhoneNumber1"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE_NUMBER_ONE, messageForKeyAdmin("labelPhoneNumber1"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE_NUMBER_ONE, messageForKeyAdmin("labelPhoneNumber1"), new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE_NUMBER_ONE, messageForKeyAdmin("labelPhoneNumber1"), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));

		validator.addValidationMapping(FieldConstants.PHONE_NUMBER_TWO, messageForKeyAdmin("labelPhoneNumber2"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE_NUMBER_TWO, messageForKeyAdmin("labelPhoneNumber2"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE_NUMBER_TWO, messageForKeyAdmin("labelPhoneNumber2"), new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE_NUMBER_TWO, messageForKeyAdmin("labelPhoneNumber2"), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_ADMIN_CONTACT_US_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}