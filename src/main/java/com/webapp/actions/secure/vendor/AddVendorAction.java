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
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.DuplicateEmailWithRolesValidationRule;
import com.jeeutils.validator.DuplicatePhoneNumberValidationRule;
import com.jeeutils.validator.EmailValidationRule;
import com.jeeutils.validator.MaxLengthValidationRule;
import com.jeeutils.validator.MinMaxFareValidationRule;
import com.jeeutils.validator.MinMaxLengthValidationRule;
import com.jeeutils.validator.MinMaxValueValidationRule;
import com.jeeutils.validator.NumericValidationRule;
import com.jeeutils.validator.PhoneNoPrefixZeroValidationRule;
import com.jeeutils.validator.RequiredListValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.TaxiSolsAlphaNumericDashCharactersValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.FeedAutomationUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MulticityRegionUtils;
import com.utils.myhub.ServiceCategoryUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.UserExportUtils;
import com.utils.myhub.VendorMonthlySubscriptionHistoryUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.ServiceModel;
import com.webapp.models.UrlAccessesModel;
import com.webapp.models.UserAccountModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorProductCategoryAssocModel;

@Path("/add-vendor")
public class AddVendorAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

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

		VendorMonthlySubscriptionHistoryUtils.setVendorMonthlySubscriptionStartEndTimeUiFieldsForAdd(data, timeZone);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_URL);

		return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addVendorForm(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.FIRST_NAME) String firstName,
		@FormParam(FieldConstants.LAST_NAME) String lastName,
		@FormParam(FieldConstants.EMAIL_ADDRESS) String emailAddress,
		@FormParam(FieldConstants.ADDRESS) String address,
		@FormParam(FieldConstants.PHONE) String phone,
		@FormParam(FieldConstants.COUNTRY_CODE) String countryCode,
		@FormParam(FieldConstants.CITY) String city,
		@FormParam(FieldConstants.STATUS) String status,
		@FormParam(FieldConstants.MAXIMUM_MARKUP_FARE) String maximumMarkupFare,
		@FormParam(FieldConstants.REGION_LIST) List<String> regionList,
		@FormParam(FieldConstants.MULTICITY_COUNTRY_ID) String multicityCountryId,
		@FormParam(FieldConstants.EXPORTACCESSLIST) List<String> exportAccessList,
		@FormParam(FieldConstants.ACCESS_LIST) List<String> accessList,
		@FormParam(FieldConstants.SERVICE_ID) String serviceId,
		@FormParam(FieldConstants.CATEGORY_ID) String categoryId,
		@FormParam(FieldConstants.VENDOR_BRAND_NAME) String vendorBrandName,
		@FormParam(FieldConstants.VENDOR_BRAND_SEARCH_KEYWORDS) String vendorBrandSearchKeywords,
		@FormParam(FieldConstants.VENDOR_BRAND_IMAGE_URL_HIDDEN) String hiddenVendorBrandImage,
		@FormParam(FieldConstants.PROFILE_IMAGE_URL_HIDDEN) String hiddenPhotoUrl,
		@FormParam(FieldConstants.VENDOR_MONTHLY_SUBCRIPTION_FEE) String vendorMonthlySubscriptionFee,
		@FormParam(FieldConstants.IS_SELF_DELIVERY_WITHIN_X_KM) String isSelfDeliveryWithinXKm,
		@FormParam(FieldConstants.SELF_DELIVERY_FEE) String selfDeliveryFee,
		@FormParam(FieldConstants.PHONEPE_MERCHANT_ID) String phonepeMerchantId,
		@FormParam(FieldConstants.PHONEPE_MERCHANT_NAME) String phonepeMerchantName,
		@FormParam(FieldConstants.SALT_KEY) String saltKey,
		@FormParam(FieldConstants.SALT_INDEX) String saltIndex,
		@FormParam(FieldConstants.PRODUCT_CATEGORY_ID) List<String> productCategoryIdList
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		data.put(FieldConstants.REGION_LIST, "");
		data.put(FieldConstants.FIRST_NAME, firstName);
		data.put(FieldConstants.LAST_NAME, lastName);
		data.put(FieldConstants.EMAIL_ADDRESS, emailAddress);
		data.put(FieldConstants.ADDRESS, address);
		data.put(FieldConstants.PHONE, phone);
		data.put(FieldConstants.CITY, city);
		data.put(FieldConstants.MAXIMUM_MARKUP_FARE, maximumMarkupFare);
		data.put(FieldConstants.VENDOR_MONTHLY_SUBCRIPTION_FEE, vendorMonthlySubscriptionFee);
		data.put(FieldConstants.VENDOR_BRAND_NAME, vendorBrandName);
		data.put(FieldConstants.VENDOR_BRAND_SEARCH_KEYWORDS, vendorBrandSearchKeywords);
		data.put(FieldConstants.SELF_DELIVERY_FEE, selfDeliveryFee);
		data.put(FieldConstants.PHONEPE_MERCHANT_ID, phonepeMerchantId);
		data.put(FieldConstants.PHONEPE_MERCHANT_NAME, phonepeMerchantName);
		data.put(FieldConstants.SALT_KEY, saltKey);
		data.put(FieldConstants.SALT_INDEX, saltIndex);

		String serviceIdOptions = DropDownUtils.getSuperServicesList(false, ProjectConstants.ALL_ID, serviceId);
		data.put(FieldConstants.SERVICE_ID_OPTIONS, serviceIdOptions);

		String categoryIdOptions = DropDownUtils.getCategoryList(false, ProjectConstants.ALL_ID, serviceId, categoryId);
		data.put(FieldConstants.CATEGORY_ID_OPTIONS, categoryIdOptions);

		data.put(FieldConstants.VENDOR_BRAND_IMAGE_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenVendorBrandImage) ? hiddenVendorBrandImage : ProjectConstants.DEFAULT_IMAGE);
		data.put(FieldConstants.PROFILE_IMAGE_URL_HIDDEN_DUMMY, StringUtils.validString(hiddenPhotoUrl) ? hiddenPhotoUrl : ProjectConstants.DEFAULT_IMAGE);

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

		String isSelfDeliveryWithinXKmOptions = DropDownUtils.getYesNoOption(isSelfDeliveryWithinXKm);
		data.put(FieldConstants.IS_SELF_DELIVERY_WITHIN_X_KM_OPTIONS, isSelfDeliveryWithinXKmOptions);

		VendorMonthlySubscriptionHistoryUtils.setVendorMonthlySubscriptionStartEndTimeUiFieldsForAdd(data, timeZone);

		boolean isSelfDeliveryWithinXKmBoolean = StringUtils.booleanValueOf(isSelfDeliveryWithinXKm);

		if (hasErrorsForEnglish(regionList, countryCode, isSelfDeliveryWithinXKmBoolean)) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_URL);

			return loadView(UrlConstants.JSP_URLS.ADD_VENDOR_JSP);
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
		userProfileModel.setMaximumMarkupFare(Double.parseDouble(maximumMarkupFare));
		userProfileModel.setRoleId(UserRoles.VENDOR_ROLE_ID);
		userProfileModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
		userProfileModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());
		userProfileModel.setVendorBrandName(vendorBrandName);
		userProfileModel.setVendorBrandImage(hiddenVendorBrandImage);
		userProfileModel.setVendorBrandSearchKeywords(vendorBrandSearchKeywords);
		userProfileModel.setPhotoUrl(hiddenPhotoUrl);
		userProfileModel.setActive(true);

		userProfileModel.setSelfDeliveryWithinXKm(isSelfDeliveryWithinXKmBoolean);
		if (StringUtils.validString(selfDeliveryFee)) {
			userProfileModel.setSelfDeliveryFee(Double.parseDouble(selfDeliveryFee));
		} else {
			userProfileModel.setSelfDeliveryFee(0);
		}
		
		userProfileModel.setPhonepeMerchantId(phonepeMerchantId);
		userProfileModel.setPhonepeMerchantName(phonepeMerchantName);
		userProfileModel.setSaltKey(saltKey);
		userProfileModel.setSaltIndex(saltIndex);

		VendorMonthlySubscriptionHistoryUtils.setAddVendorMonthlySubscription(userProfileModel, vendorMonthlySubscriptionFee);

		String userId = userProfileModel.addUser();

		VendorMonthlySubscriptionHistoryUtils.addFreeVendorMonthlySubscriptionEntry(userProfileModel, loginSessionMap.get(LoginUtils.USER_ID));

		accessList.add(ProjectConstants.MENU_CONSTANTS.MENU_VENDOR_MY_ACCOUNT_ID + "");
		String[] urlIds = accessList.toArray(new String[accessList.size()]);
		UrlAccessesModel.addCustomUrlAccesses(userId, urlIds);

		MulticityRegionUtils.addUserRegions(regionList, loginSessionMap.get(LoginUtils.USER_ID), userId, UserRoles.VENDOR_ROLE_ID);

		MultiTenantUtils.assignVendorAllCityFare(userId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);
		MultiTenantUtils.assignVendorAllCityFare(userId, ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID);
		MultiTenantUtils.assignVendorAllCityFare(userId, ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);
		MultiTenantUtils.assignVendorAllExistingAirportRegionsAndCarFare(userId);

		UserExportUtils.processVendorExportAccessList(exportAccessList, userId, loginSessionMap.get(LoginUtils.USER_ID));

		UserAccountModel.createUserAccountBalance(loginSessionMap.get(LoginUtils.USER_ID), userId);

		ServiceCategoryUtils.mapVendorServiceCategory(loginSessionMap.get(LoginUtils.USER_ID), userId, serviceId, categoryId);

		VendorProductCategoryAssocModel.addVenorProductCategoryAssoc(userId, productCategoryIdList);
		
		FeedAutomationUtils.uploadAutoFeed(null, userId, loginSessionMap.get(LoginUtils.USER_ID), serviceId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_URL);
	}

	public boolean hasErrorsForEnglish(List<String> regionList, String countryCode, boolean isSelfDeliveryWithinXKmBoolean) {

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
		validator.addValidationMapping(FieldConstants.MAXIMUM_MARKUP_FARE, messageForKeyAdmin("labelMarkupFare"), new MinMaxFareValidationRule(0, 5000));
		validator.addValidationMapping(FieldConstants.VENDOR_BRAND_NAME, messageForKeyAdmin("labelBrandName"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.VENDOR_BRAND_SEARCH_KEYWORDS, messageForKeyAdmin("labelSearchKeywords"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.VENDOR_MONTHLY_SUBCRIPTION_FEE, messageForKeyAdmin("labelVendorMonthlySubscriptionFee"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.VENDOR_MONTHLY_SUBCRIPTION_FEE, messageForKeyAdmin("labelVendorMonthlySubscriptionFee"), new DecimalValidationRule());
		validator.addValidationMapping(FieldConstants.VENDOR_MONTHLY_SUBCRIPTION_FEE, messageForKeyAdmin("labelVendorMonthlySubscriptionFee"), new MinMaxValueValidationRule(0, 1000000));
		// validator.addValidationMapping(FieldConstants.PRODUCT_CATEGORY_ID,
		// messageForKeyAdmin("labelProductCategory"), new RequiredValidationRule());

		if (isSelfDeliveryWithinXKmBoolean) {
			validator.addValidationMapping(FieldConstants.SELF_DELIVERY_FEE, messageForKeyAdmin("labelSelfDeliveryFee"), new NumericValidationRule());
			validator.addValidationMapping(FieldConstants.SELF_DELIVERY_FEE, messageForKeyAdmin("labelSelfDeliveryFee"), new MinMaxValueValidationRule(0, 1000000));
		}

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_VENDOR_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}