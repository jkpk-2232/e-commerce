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
import com.utils.myhub.UserExportUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.ServiceModel;
import com.webapp.models.UrlAccessesModel;
import com.webapp.models.UserAccountModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorProductCategoryAssocModel;

@Path("/add-erp-user")
public class AddErpUserAction extends BusinessAction {
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddErpUser(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);
		
		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		String countryCodeOptions = DropDownUtils.getCountryCodeOptions(adminSettings.getCountryCode());
		data.put(FieldConstants.COUNTRY_CODE_OPTIONS, countryCodeOptions);

		String regionListOptions = DropDownUtils.getMultiCityRegionOptions(null, ProjectConstants.DEFAULT_COUNTRY_ID);
		data.put(FieldConstants.REGION_LIST_OPTIONS, regionListOptions);

		String exportAccessListOptions = DropDownUtils.getVendorExportAccessListOptions(null);
		data.put(FieldConstants.EXPORTACCESSLIST_OPTIONS, exportAccessListOptions);

		String accessListOptions = DropDownUtils.getVendorAccessListOptions(null);
		data.put(FieldConstants.ACCESS_LIST_OPTIONS, accessListOptions);

		ServiceModel defaultServiceModel = ServiceModel.getDefaultServiceModel();

		String serviceIdOptions = DropDownUtils.getSuperServicesList(false, ProjectConstants.ALL_ID, defaultServiceModel.getServiceId());
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		String categoryIdOptions = DropDownUtils.getCategoryList(false, ProjectConstants.ALL_ID, defaultServiceModel.getServiceId(), ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		data.put(FieldConstants.CATEGORY_ID_OPTIONS, categoryIdOptions);

		data.put(FieldConstants.VENDOR_BRAND_IMAGE_URL_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, ProjectConstants.DEFAULT_IMAGE);

		String isSelfDeliveryWithinXKmOptions = DropDownUtils.getYesNoOption(false + "");
		data.put(FieldConstants.IS_SELF_DELIVERY_WITHIN_X_KM_OPTIONS, isSelfDeliveryWithinXKmOptions);

		String productCategoryIdOptions = DropDownUtils.getProductCategoryOption("");
		data.put(FieldConstants.PRODUCT_CATEGORY_ID_OPTIONS, productCategoryIdOptions);
		
		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ERP_USERS_URL);
		
		return loadView(UrlConstants.JSP_URLS.ADD_ERP_USER_JSP);
	}
	
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addErpUserForm(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
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



		if (hasErrorsForEnglish(regionList, countryCode)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_ERP_USERS_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_ERP_USER_JSP);
		}

		UserProfileModel userProfileModel = new UserProfileModel();
		userProfileModel.setEmail(emailAddress);
		userProfileModel.setFirstName(firstName);
		userProfileModel.setLastName(lastName);
		userProfileModel.setPhoneNo(phone);
		userProfileModel.setMailAddressLineOne(address);
		userProfileModel.setMailStateId(null);
		userProfileModel.setMailCityId(city);
		userProfileModel.setPhoneNoCode(countryCode);
		userProfileModel.setRoleId(UserRoles.ERP_ROLE_ID);
		userProfileModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
		userProfileModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		userProfileModel.setVendorBrandName(vendorBrandName);
		userProfileModel.setVendorBrandImage(hiddenVendorBrandImage);
		userProfileModel.setVendorBrandSearchKeywords(vendorBrandSearchKeywords);
		userProfileModel.setActive(true);
		userProfileModel.setSelfDeliveryFee(0);
		userProfileModel.setVendorSubscriptionCurrentActive(true);

		String userId = userProfileModel.addUser();

		String[] urlIds = accessList.toArray(new String[accessList.size()]);
		UrlAccessesModel.addCustomUrlAccesses(userId, urlIds);

		MulticityRegionUtils.addUserRegions(regionList, loginSessionMap.get(LoginUtils.USER_ID), userId, UserRoles.ERP_ROLE_ID);

		UserExportUtils.processVendorExportAccessList(exportAccessList, userId, loginSessionMap.get(LoginUtils.USER_ID));

		UserAccountModel.createUserAccountBalance(loginSessionMap.get(LoginUtils.USER_ID), userId);

		VendorProductCategoryAssocModel.addVenorProductCategoryAssoc(userId, productCategoryIdList);
		
		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_ERP_USERS_URL);
	}
	
	public boolean hasErrorsForEnglish(List<String> regionList, String countryCode) {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.FIRST_NAME, messageForKeyAdmin("labelFirstName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.FIRST_NAME, messageForKeyAdmin("labelFirstName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.LAST_NAME, messageForKeyAdmin("labelLastName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.LAST_NAME, messageForKeyAdmin("labelLastName"), new TaxiSolsAlphaNumericDashCharactersValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new EmailValidationRule());
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new MaxLengthValidationRule(200));
		validator.addValidationMapping(FieldConstants.EMAIL_ADDRESS, messageForKeyAdmin("labelEmailAddress"), new DuplicateEmailWithRolesValidationRule(Arrays.asList(UserRoles.VENDOR_ROLE_ID), null));
		validator.addValidationMapping(FieldConstants.ADDRESS, messageForKeyAdmin("labelAddress"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new NumericValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new PhoneNoPrefixZeroValidationRule());
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new MinMaxLengthValidationRule(ProjectConstants.PHONE_NO_MIN, ProjectConstants.PHONE_NO_MAX));
		validator.addValidationMapping(FieldConstants.PHONE, messageForKeyAdmin("labelPhoneNumber"), new DuplicatePhoneNumberValidationRule(countryCode, UserRoles.VENDOR_ROLE_ID, null, null));
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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_ERP_USER_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
