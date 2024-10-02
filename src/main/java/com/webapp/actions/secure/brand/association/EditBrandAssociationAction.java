package com.webapp.actions.secure.brand.association;

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

import com.jeeutils.StringUtils;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.BrandModel;
import com.webapp.models.VendorStoreModel;

@Path("edit-brand-association")
public class EditBrandAssociationAction extends BusinessAction {

	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getEditVendorStore(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		VendorStoreModel vsm = VendorStoreModel.getVendorStoreDetailsById(vendorStoreId);

		data.put(FieldConstants.VENDOR_STORE_ID, vsm.getVendorStoreId());
		data.put(FieldConstants.VENDOR_ID, vsm.getVendorId());
		data.put(FieldConstants.STORE_NAME, vsm.getStoreName());
		data.put(FieldConstants.STORE_ADDRESS, vsm.getStoreAddress());
		data.put(FieldConstants.STORE_ADDRESS_LAT, vsm.getStoreAddressLat());
		data.put(FieldConstants.STORE_ADDRESS_LNG, vsm.getStoreAddressLng());
		data.put(FieldConstants.STORE_PLACE_ID, vsm.getStorePlaceId());
		data.put(FieldConstants.STORE_IMAGE_HIDDEN, vsm.getStoreImage());
		data.put(FieldConstants.STORE_IMAGE_HIDDEN_DUMMY, StringUtils.validString(vsm.getStoreImage()) ? vsm.getStoreImage() : ProjectConstants.DEFAULT_IMAGE);
		
		String brandIdOptions = DropDownUtils.getBrandFilterListOptions(null, vsm.getBrandId(), "1");
		data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String multicityCityRegionIdOptions = DropDownUtils.getRegionListByMulticityCountryIdOptionsSingleSelect(vsm.getMulticityCityRegionId(), ProjectConstants.DEFAULT_COUNTRY_ID, assignedRegionList);
		data.put(FieldConstants.MULTCITY_REGION_ID_OPTIONS, multicityCityRegionIdOptions);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_URL);
		} else {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_URL + "?vendorId=" + vsm.getVendorId());
		}

		return loadView(UrlConstants.JSP_URLS.EDIT_BRAND_ASSOCIATION_JSP);
	}
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addVendorForm(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.BRAND_ID) String brandId,
		@FormParam(FieldConstants.STORE_ADDRESS) String storeAddress,
		@FormParam(FieldConstants.STORE_ADDRESS_LAT) String storeAddressLat,
		@FormParam(FieldConstants.STORE_ADDRESS_LNG) String storeAddressLng,
		@FormParam(FieldConstants.STORE_PLACE_ID) String storePlaceId,
		@FormParam(FieldConstants.STORE_IMAGE_HIDDEN) String hiddenStoreImage,
		@FormParam(FieldConstants.MULTICITY_REGION_ID) String multicityCityRegionId
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isErpRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_USERS_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.VENDOR_STORE_ID, vendorStoreId);
		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.STORE_ADDRESS, storeAddress);
		data.put(FieldConstants.STORE_ADDRESS_LAT, storeAddressLat);
		data.put(FieldConstants.STORE_ADDRESS_LNG, storeAddressLng);
		data.put(FieldConstants.STORE_PLACE_ID, storePlaceId);
		data.put(FieldConstants.STORE_IMAGE_HIDDEN, hiddenStoreImage);
		data.put(FieldConstants.STORE_IMAGE_HIDDEN_DUMMY, StringUtils.validString(hiddenStoreImage) ? hiddenStoreImage : ProjectConstants.DEFAULT_IMAGE);
		
		String brandIdOptions = DropDownUtils.getBrandFilterListOptions(null, brandId, "1");
		data.put(FieldConstants.BRAND_ID_OPTIONS, brandIdOptions);
		
		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		String multicityCityRegionIdOptions = DropDownUtils.getRegionListByMulticityCountryIdOptionsSingleSelect(multicityCityRegionId, ProjectConstants.DEFAULT_COUNTRY_ID, assignedRegionList);
		data.put(FieldConstants.MULTCITY_REGION_ID_OPTIONS, multicityCityRegionIdOptions);
		
		

		if (hasErrors()) {

			if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_URL);
			} else {
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_URL + "?vendorId=" + vendorId);
			}

			return loadView(UrlConstants.JSP_URLS.EDIT_VENDOR_STORE_JSP);
		}

		VendorStoreModel vsm = new VendorStoreModel();
		
		BrandModel brandModel = BrandModel.getBrandDetailsByBrandId(brandId);
		
		if (brandModel != null) {
			vsm.setStoreName(brandModel.getBrandName());
		}
		vsm.setVendorStoreId(vendorStoreId);
		vsm.setVendorId(vendorId);
		vsm.setBrandId(brandId);
		vsm.setStoreAddress(storeAddress);
		vsm.setStoreAddressLat(storeAddressLat);
		vsm.setStoreAddressLng(storeAddressLng);
		vsm.setStorePlaceId(storePlaceId);
		vsm.setStoreImage(hiddenStoreImage);
		vsm.setMulticityCityRegionId(multicityCityRegionId);
		vsm.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
		
		vsm.updateVendorStore(loginSessionMap.get(LoginUtils.USER_ID));

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_URL);
		} else {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_BRAND_ASSOCIATION_URL + "?vendorId=" + vendorId);
		}
	}
	
	
	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping("storeAddress", messageForKeyAdmin("labelAddress"), new RequiredValidationRule());
		/*
		 * validator.addValidationMapping("storeName",
		 * messageForKeyAdmin("labelStoreName"), new MinMaxLengthValidationRule(1, 25));
		 */
		
		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.EDIT_BRAND_ASSOCIATION_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}
