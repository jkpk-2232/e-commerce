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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.StringUtils;
import com.jeeutils.validator.DuplicateEmailWithRolesValidationRule;
import com.jeeutils.validator.DuplicatePhoneNumberValidationRule;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.MaxLengthValidationRule;
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
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorStoreSubVendorUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UrlAccessesModel;
import com.webapp.models.UserProfileModel;

@Path("/edit-sub-vendor")
public class EditSubVendorAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditSubVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.USER_ID) String userId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SUB_VENDOR_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (!UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(userId);

		List<String> regionList = MulticityRegionUtils.getUserAccessRegionList(userId.trim());
		String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(regionList, assignedRegionList);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(userProfileModel.getPhoneNoCode());
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

		List<String> urlAccessList = UrlAccessUtils.getUserAccessList(userId);
		String accessListOptions = DropDownUtils.getSubVendorAccessListOptions(urlAccessList);
		data.put(FieldConstants.ACCESS_LIST_OPTIONS, accessListOptions);

		List<String> vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(userId, true);
		String vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(vendorStoreIdList, loginSessionMap.get(LoginUtils.USER_ID), assignedRegionList);
		data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);

		data.put(FieldConstants.USER_ID, userId);
		data.put(FieldConstants.FIRST_NAME, userProfileModel.getFirstName());
		data.put(FieldConstants.LAST_NAME, userProfileModel.getLastName());
		data.put(FieldConstants.EMAIL_ADDRESS, userProfileModel.getEmail());
		data.put(FieldConstants.ADDRESS, userProfileModel.getMailAddressLineOne());
		data.put(FieldConstants.CITY, userProfileModel.getMailCityId());
		data.put(FieldConstants.PHONE, userProfileModel.getPhoneNo().replace(userProfileModel.getPhoneNoCode(), ""));
		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN, userProfileModel.getPhotoUrl());
		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, StringUtils.validString(userProfileModel.getPhotoUrl()) ? userProfileModel.getPhotoUrl() : ProjectConstants.DEFAULT_IMAGE);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_SUB_VENDOR_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_SUB_VENDOR_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response postEditVendor(
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
		@FormParam(FieldConstants.PROFILE_IMAGE_URL_HIDDEN) String hiddenPhotoUrl,
		@FormParam(FieldConstants.VENDOR_STORE_ID) List<String> vendorStoreId,
		@FormParam(FieldConstants.ACCESS_LIST) List<String> accessList
		
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_SUB_VENDOR_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (!UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		data.put(FieldConstants.REGION_LIST, "");
		data.put(FieldConstants.FIRST_NAME, firstName);
		data.put(FieldConstants.LAST_NAME, lastName);
		data.put(FieldConstants.EMAIL_ADDRESS, emailAddress);
		data.put(FieldConstants.ADDRESS, address);
		data.put(FieldConstants.PHONE, phone);
		data.put(FieldConstants.CITY, city);
		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenPhotoUrl) ? hiddenPhotoUrl : ProjectConstants.DEFAULT_IMAGE);

		String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(regionList, assignedRegionList);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(countryCode);
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

		String accessListOptions = DropDownUtils.getSubVendorAccessListOptions(accessList);
		data.put(FieldConstants.ACCESS_LIST_OPTIONS, accessListOptions);

		String vendorStoreIdOptions = DropDownUtils.getVendorStoreFilterListOptions(vendorStoreId, loginSessionMap.get(LoginUtils.USER_ID), assignedRegionList);
		data.put(FieldConstants.VENDOR_STORE_ID_OPTIONS, vendorStoreIdOptions);

		if (hasErrorsForEnglish(regionList, countryCode, userId)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_SUB_VENDOR_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_SUB_VENDOR_JSP);
		}

		UserProfileModel userProfileModel = new UserProfileModel();

		userProfileModel.setUserId(userId);
		userProfileModel.setEmail(emailAddress);
		userProfileModel.setFirstName(firstName);
		userProfileModel.setLastName(lastName);
		userProfileModel.setPhoneNo(phone);
		userProfileModel.setPhoneNoCode(countryCode);
		userProfileModel.setMailAddressLineOne(address);
		userProfileModel.setMaximumMarkupFare(0);
		userProfileModel.setMailStateId(null);
		userProfileModel.setMailCityId(city);
		userProfileModel.setUpdatedBy(loginSessionMap.get(LoginUtils.USER_ID));
		userProfileModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		userProfileModel.setPhotoUrl(hiddenPhotoUrl);
		userProfileModel.setActive(true);
		userProfileModel.setVendorBrandName(null);
		userProfileModel.setVendorBrandImage(null);
		userProfileModel.setVendorBrandSearchKeywords(null);
		userProfileModel.setSelfDeliveryFee(0);
		userProfileModel.updateUser();

		accessList.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_MY_ACCOUNT_ID + "");
		String[] urlIds = accessList.toArray(new String[accessList.size()]);
		UrlAccessesModel.addCustomUrlAccesses(userId, urlIds);

		MulticityRegionUtils.addUserRegions(regionList, loginSessionMap.get(LoginUtils.USER_ID), userId, UserRoles.SUB_VENDOR_ROLE_ID);

		VendorStoreSubVendorUtils.mapSubVendorsToVendorStore(vendorStoreId, loginSessionMap.get(LoginUtils.USER_ID), userId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_SUB_VENDOR_URL);
	}

	public boolean hasErrorsForEnglish(List<String> regionList, String countryCode, String userId) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.FIRST_NAME, messageForKeyAdmin("labelFirstName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.FIRST_NAME, messageForKeyAdmin("labelFirstName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.LAST_NAME, messageForKeyAdmin("labelLastName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.LAST_NAME, messageForKeyAdmin("labelLastName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new EmailValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new MaxLengthValidationRule(200));
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new DuplicateEmailWithRolesValidationRule(Arrays.asList(UserRoles.SUB_VENDOR_ROLE_ID), userId));
		validator.addValidationMapping(FieldConstants.ADDRESS, messageForKeyAdmin("labelAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new DuplicatePhoneNumberValidationRule(countryCode, UserRoles.SUB_VENDOR_ROLE_ID, userId, null));
		validator.addValidationMapping(FieldConstants.CITY, messageForKeyAdmin("labelCity"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.REGION_LIST, messageForKeyAdmin("labelRegion"), new RequiredListValidationRule(regionList.size()));

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_SUB_VENDOR_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}