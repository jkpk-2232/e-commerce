package com.webapp.actions.secure.erp;

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
import com.utils.myhub.UserExportUtils;
import com.utils.myhub.VendorProductCategoryAssocUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.UrlAccessesModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorProductCategoryAssocModel;

@Path("/edit-erp-user")
public class EditErpUserAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditVendor(
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

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));
		
		UserProfileModel userProfileModel = UserProfileModel.getAdminUserAccountDetailsById(userId);
		
		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(userProfileModel.getPhoneNoCode());
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

		data.put(FieldConstants.USER_ID, userId);
		data.put(FieldConstants.FIRST_NAME, userProfileModel.getFirstName());
		data.put(FieldConstants.LAST_NAME, userProfileModel.getLastName());
		data.put(FieldConstants.EMAIL_ADDRESS, userProfileModel.getEmail());
		data.put(FieldConstants.ADDRESS, userProfileModel.getMailAddressLineOne());
		data.put(FieldConstants.CITY, userProfileModel.getMailCityId());
		data.put(FieldConstants.PHONE, userProfileModel.getPhoneNo().replace(userProfileModel.getPhoneNoCode(), ""));

		data.put(FieldConstants.VENDOR_BRAND_NAME, userProfileModel.getVendorBrandName());
		data.put(FieldConstants.VENDOR_BRAND_SEARCH_KEYWORDS, userProfileModel.getVendorBrandSearchKeywords());
		data.put(FieldConstants.VENDOR_BRAND_IMAGE_URL_HIDDEN, userProfileModel.getVendorBrandImage());

		data.put(FieldConstants.VENDOR_BRAND_IMAGE_URL_HIDDEN_DUMMY, StringUtils.validString(userProfileModel.getVendorBrandImage()) ? userProfileModel.getVendorBrandImage() : ProjectConstants.DEFAULT_IMAGE);

		List<String> urlAccessList = UrlAccessUtils.getUserAccessList(userId);
		String accessListOptions = DropDownUtils.getVendorAccessListOptions(urlAccessList);
		data.put(FieldConstants.ACCESS_LIST_OPTIONS, accessListOptions);

		List<String> exportAccessList = UserExportUtils.setVendorExportUrlAccessList(userId);
		String exportAccessListOptions = DropDownUtils.getVendorExportAccessListOptions(exportAccessList);
		data.put(FieldConstants.EXPORTACCESSLIST_OPTIONS, exportAccessListOptions);

		List<String> regionList = MulticityRegionUtils.getUserAccessRegionList(userId.trim());
		String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(regionList, assignedRegionList);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		List<String> VPCAssocList = VendorProductCategoryAssocUtils.getVendorProductCategoryAssocByvendorId(userId);
		String productCategoryIdOptions = DropDownUtils.getProductCategoryListOption(VPCAssocList);
		data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);

		data.put(FieldConstants.EDIT_VENDOR_FLOW, true + "");

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ERP_USERS_URL);

		return loadView(UrlConstants.JSP_URLS.EDIT_ERP_USER_JSP);
	}
	
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response postEditVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.USER_ID) String userId,
		@FormParam(FieldConstants.FIRST_NAME) String firstName,
		@FormParam(FieldConstants.LAST_NAME) String lastName,
		@FormParam(FieldConstants.EMAIL_ADDRESS) String emailAddress,
		@FormParam(FieldConstants.ADDRESS) String address,
		@FormParam(FieldConstants.PHONE) String phone,
		@FormParam(FieldConstants.COUNTRY_CODE) String countryCode,
		@FormParam(FieldConstants.CITY) String city,
		@FormParam(FieldConstants.REGION_LIST) List<String> regionList,
		@FormParam(FieldConstants.MULTICITY_COUNTRY_ID) String multicityCountryId,
		@FormParam(FieldConstants.EXPORTACCESSLIST) List<String> exportAccessList,
		@FormParam(FieldConstants.ACCESS_LIST) List<String> accessList,
		@FormParam(FieldConstants.VENDOR_BRAND_NAME) String vendorBrandName,
		@FormParam(FieldConstants.VENDOR_BRAND_SEARCH_KEYWORDS) String vendorBrandSearchKeywords,
		@FormParam(FieldConstants.VENDOR_BRAND_IMAGE_URL_HIDDEN) String hiddenVendorBrandImage,
		@FormParam(FieldConstants.PRODUCT_CATEGORY_ID) List<String> productCategoryIdList
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
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
		data.put(FieldConstants.VENDOR_BRAND_NAME, vendorBrandName);
		data.put(FieldConstants.VENDOR_BRAND_SEARCH_KEYWORDS, vendorBrandSearchKeywords);
		data.put(FieldConstants.USER_ID, userId);

		data.put(FieldConstants.VENDOR_BRAND_IMAGE_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenVendorBrandImage) ? hiddenVendorBrandImage : ProjectConstants.DEFAULT_IMAGE);

		String regionListOptions = DropDownUtils.getRegionListByMulticityCountryIdOptions(regionList, assignedRegionList);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(countryCode);
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

		String exportAccessListOptions = DropDownUtils.getVendorExportAccessListOptions(exportAccessList);
		data.put(FieldConstants.EXPORTACCESSLIST_OPTIONS, exportAccessListOptions);

		String accessListOptions = DropDownUtils.getVendorAccessListOptions(accessList);
		data.put(FieldConstants.ACCESS_LIST_OPTIONS, accessListOptions);
		
		String productCategoryIdOptions = DropDownUtils.getProductCategoryListOption(productCategoryIdList);
		data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);

		data.put(FieldConstants.EDIT_VENDOR_FLOW, true + "");


		if (hasErrorsForEnglish(regionList, countryCode, userId)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ERP_USERS_URL);

			return loadView(UrlConstants.JSP_URLS.EDIT_ERP_USER_JSP);
		}

		UserProfileModel userProfileModel = new UserProfileModel();

		userProfileModel.setUserId(userId);
		userProfileModel.setEmail(emailAddress);
		userProfileModel.setFirstName(firstName);
		userProfileModel.setLastName(lastName);
		userProfileModel.setPhoneNo(phone);
		userProfileModel.setPhoneNoCode(countryCode);
		userProfileModel.setMailAddressLineOne(address);
		userProfileModel.setMailStateId(null);
		userProfileModel.setMailCityId(city);
		userProfileModel.setUpdatedBy(loginSessionMap.get(LoginUtils.USER_ID));
		userProfileModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		userProfileModel.setActive(true);
		userProfileModel.setVendorBrandName(vendorBrandName);
		userProfileModel.setVendorBrandImage(hiddenVendorBrandImage);
		userProfileModel.setVendorBrandSearchKeywords(vendorBrandSearchKeywords);
		userProfileModel.setSelfDeliveryFee(0);
		
		userProfileModel.updateUser();

		// accessList.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_MY_ACCOUNT_ID + "");
		String[] urlIds = accessList.toArray(new String[accessList.size()]);
		UrlAccessesModel.addCustomUrlAccesses(userId, urlIds);

		// ServiceCategoryUtils.mapVendorServiceCategory(loginSessionMap.get(LoginUtils.USER_ID), userId, serviceId, categoryId);

		MulticityRegionUtils.addUserRegions(regionList, loginSessionMap.get(LoginUtils.USER_ID), userId, UserRoles.ERP_ROLE_ID);

		UserExportUtils.processVendorExportAccessList(exportAccessList, userId, loginSessionMap.get(LoginUtils.USER_ID));
		
		VendorProductCategoryAssocModel.addVenorProductCategoryAssoc(userId,productCategoryIdList);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_ERP_USERS_URL);
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
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new DuplicateEmailWithRolesValidationRule(Arrays.asList(UserRoles.VENDOR_ROLE_ID), userId));
		validator.addValidationMapping(FieldConstants.ADDRESS, messageForKeyAdmin("labelAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new DuplicatePhoneNumberValidationRule(countryCode, UserRoles.VENDOR_ROLE_ID, userId, null));
		validator.addValidationMapping(FieldConstants.CITY, messageForKeyAdmin("labelCity"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.REGION_LIST, messageForKeyAdmin("labelRegion"), new RequiredListValidationRule(regionList.size()));
		validator.addValidationMapping(FieldConstants.VENDOR_BRAND_NAME, messageForKeyAdmin("labelBrandName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.VENDOR_BRAND_SEARCH_KEYWORDS, messageForKeyAdmin("labelSearchKeywords"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}
	
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_ERP_USER_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}