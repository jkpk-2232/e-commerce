package com.webapp.actions.secure.vendor.settings;

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

import com.jeeutils.StringUtils;
import com.jeeutils.validator.DecimalValidationRule;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.VendorCarTypeModel;

@Path("/set-vendor-car-priority")
public class SetVendorCarPriorityAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getSetVendorCarPriority(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String ECOMMERCE_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		data.put(FieldConstants.VENDOR_ID, vendorId);

		String dataAvailableCarListEcommerce = "";
		List<VendorCarTypeModel> vendorCarTypeListEcommerce = VendorCarTypeModel.getVendorCarTypeListByVendorId(vendorId, ECOMMERCE_ID);

		for (VendorCarTypeModel vendorCarTypeModel : vendorCarTypeListEcommerce) {
			data.put(vendorCarTypeModel.getCarTypeId() + "_Priority_" + ECOMMERCE_ID, StringUtils.valueOf(vendorCarTypeModel.getPriority()));
			dataAvailableCarListEcommerce += vendorCarTypeModel.getCarTypeId() + ",";
		}

		if (dataAvailableCarListEcommerce.length() > 0) {
			data.put("dataAvailableCarListEcommerce", dataAvailableCarListEcommerce.substring(0, dataAvailableCarListEcommerce.length() - 1));
		}

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.SET_VENDOR_CAR_PRIORITY_URL);

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.SET_VENDOR_CAR_PRIORITY_URL);
		} else {
			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.SET_VENDOR_CAR_PRIORITY_URL + "?vendorId=" + vendorId);
		}

		return loadView(UrlConstants.JSP_URLS.SET_VENDOR_CAR_PRIORITY_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postSetVendorCarPriority(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.QUERY_STRING) String queryString,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException {
	//@formatter:on	

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!UserRoleUtils.hasSettingsAccess(loginSessionMap)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			vendorId = loginSessionMap.get(LoginUtils.USER_ID);
		}

		data.put(FieldConstants.VENDOR_ID, vendorId);

		Map<String, Object> inputMap = MultiTenantUtils.parseInputParameters(queryString);

		data.put(FieldConstants.QUERY_STRING, queryString);

		String ECOMMERCE_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID;
		List<VendorCarTypeModel> vendorCarTypeListEcommerce = VendorCarTypeModel.getVendorCarTypeListByVendorId(vendorId, ECOMMERCE_ID);

		if (hasErrors(inputMap, vendorCarTypeListEcommerce)) {

			String dataAvailableCarListEcommerce = "";
			String carTypeIdForQueryString = "";

			for (VendorCarTypeModel vendorCarTypeModel : vendorCarTypeListEcommerce) {

				carTypeIdForQueryString = vendorCarTypeModel.getCarTypeId() + "_Priority_" + ECOMMERCE_ID;

				data.put(carTypeIdForQueryString, inputMap.containsKey(carTypeIdForQueryString) ? inputMap.get(carTypeIdForQueryString).toString() : "");

				dataAvailableCarListEcommerce += vendorCarTypeModel.getCarTypeId() + ",";
			}

			if (dataAvailableCarListEcommerce.length() > 0) {
				data.put("dataAvailableCarListEcommerce", dataAvailableCarListEcommerce.substring(0, dataAvailableCarListEcommerce.length() - 1));
			}

			if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.SET_VENDOR_CAR_PRIORITY_URL);
			} else {
				data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.SET_VENDOR_CAR_PRIORITY_URL + "?vendorId=" + vendorId);
			}

			return loadView(UrlConstants.JSP_URLS.SET_VENDOR_CAR_PRIORITY_JSP);
		}

		VendorCarTypeModel tempModel = new VendorCarTypeModel();
		tempModel.setVendorId(vendorId);
		tempModel.setServiceTypeId(ECOMMERCE_ID);
		tempModel.deleteExistingCarTypes();

		for (VendorCarTypeModel vendorCarTypeModel : vendorCarTypeListEcommerce) {

			String carTypeIdForQueryString = vendorCarTypeModel.getCarTypeId() + "_Priority_" + ECOMMERCE_ID;

			if (inputMap.containsKey(carTypeIdForQueryString)) {

				String priority = inputMap.get(carTypeIdForQueryString).toString();

				vendorCarTypeModel.setCarTypeId(vendorCarTypeModel.getCarTypeId());
				vendorCarTypeModel.setVendorId(vendorId);
				vendorCarTypeModel.setServiceTypeId(ECOMMERCE_ID);
				vendorCarTypeModel.setPriority(StringUtils.intValueOf(priority));
				vendorCarTypeModel.insertVendorCarType(vendorId);
			}
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			return redirectToPage(UrlConstants.PAGE_URLS.SET_VENDOR_CAR_PRIORITY_URL);
		} else {
			return redirectToPage(UrlConstants.PAGE_URLS.SET_VENDOR_CAR_PRIORITY_URL + "?vendorId=" + vendorId);
		}

	}

	public boolean hasErrors(Map<String, Object> inputMap, List<VendorCarTypeModel> vendorCarTypeListEcommerce) {

		boolean hasErrors = false;
		Validator validator = new Validator();

		String ECOMMERCE_ID = ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID;

		for (VendorCarTypeModel vendorCarTypeModel : vendorCarTypeListEcommerce) {

			String carTypeIdForQueryString = vendorCarTypeModel.getCarTypeId() + "_Priority_" + ECOMMERCE_ID;

			if (!inputMap.containsKey(carTypeIdForQueryString)) {

				data.put(carTypeIdForQueryString, "");

				validator.addValidationMapping(carTypeIdForQueryString, messageForKeyAdmin("labelPriority"), new RequiredValidationRule());
				validator.addValidationMapping(carTypeIdForQueryString, messageForKeyAdmin("labelPriority"), new DecimalValidationRule());
			}
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
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.SET_VENDOR_CAR_PRIORITY_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}