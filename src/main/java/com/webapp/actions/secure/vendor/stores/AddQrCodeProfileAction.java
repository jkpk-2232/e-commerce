package com.webapp.actions.secure.vendor.stores;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.DuplicateQrCodeIdValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.QrProfileModel;

@Path("/add-qr-code-profile")
public class AddQrCodeProfileAction extends BusinessAction {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getAddVendorStore(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId,
		@QueryParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			vendorId = loginSessionMap.get(LoginUtils.USER_ID);

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.VENDOR_STORE_ID, vendorStoreId);
		
		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.QR_CODE_PROFILES_URL + "?vendorStoreId=" + vendorStoreId );
		} else {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.QR_CODE_PROFILES_URL + "?vendorStoreId=" + vendorStoreId);
		}

		return loadView(UrlConstants.JSP_URLS.ADD_QR_CODE_PROFILE_JSP);
	}
	
	
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postAddFeed(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.VENDOR_STORE_ID) String vendorStoreId,
		@FormParam(FieldConstants.QR_CODE_ID) String qrCodeId,
		@FormParam(FieldConstants.TERMINAL_ID) String terminalId
		) throws ServletException, SQLException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}
		
		if (UserRoleUtils.isVendorAndSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_STORE_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}

			vendorId = loginSessionMap.get(LoginUtils.USER_ID);

		} else {

			if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL)) {
				return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
			}
		}

		if (UserRoleUtils.isSubVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}
		

		
		
		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.VENDOR_STORE_ID, vendorStoreId);
		data.put(FieldConstants.QR_CODE_ID, qrCodeId);
		data.put(FieldConstants.TERMINAL_ID, terminalId);

		
		if (hasErrors()) {

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.QR_CODE_PROFILES_URL + "?vendorStoreId=" + vendorStoreId);

			return loadView(UrlConstants.JSP_URLS.ADD_QR_CODE_PROFILE_JSP);
		}
		
		QrProfileModel qrProfileModel = new QrProfileModel();
		qrProfileModel.setQrCodeId(qrCodeId);
		qrProfileModel.setVendorStoreId(vendorStoreId);
		qrProfileModel.setTerminalId(terminalId);
		qrProfileModel.setCreatedBy(loginSessionMap.get(LoginUtils.USER_ID));
		qrProfileModel.setUpdatedBy(loginSessionMap.get(LoginUtils.USER_ID));
		qrProfileModel.insertQrProfile();

		return redirectToPage(UrlConstants.PAGE_URLS.QR_CODE_PROFILES_URL + "?vendorStoreId=" + vendorStoreId);
	}
	
	private boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.QR_CODE_ID, messageForKeyAdmin("labelQrCodeId"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.QR_CODE_ID, messageForKeyAdmin("labelQrCodeId"), new DuplicateQrCodeIdValidationRule(null));
		//validator.addValidationMapping(FieldConstants.TERMINAL_ID, messageForKeyAdmin("labelTerminalId"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}
	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.ADD_QR_CODE_PROFILE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}
