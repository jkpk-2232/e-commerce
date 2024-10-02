package com.webapp.actions.secure.vendor;

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
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.utils.myhub.VendorMonthlySubscriptionHistoryUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.UserProfileModel;

@Path("/vendor-monthly-subscription")
public class VendorMonthlySubscriptionAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getVendorMonthlySubscription(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.VENDOR_ID) String vendorId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!(loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL) || loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_URL))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		UserProfileModel vendorModel = UserProfileModel.getAdminUserAccountDetailsById(vendorId);

		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.VENDOR_NAME, vendorModel.getFullName());
		data.put(FieldConstants.EMAIL_ADDRESS, vendorModel.getEmail());
		data.put(FieldConstants.PHONE_NO, MyHubUtils.formatPhoneNumber(vendorModel.getPhoneNoCode(), vendorModel.getPhoneNo()));
		data.put(FieldConstants.VENDOR_MONTHLY_SUBCRIPTION_FEE, StringUtils.valueOf(vendorModel.getVendorMonthlySubscriptionFee()));

		VendorMonthlySubscriptionHistoryUtils.setVendorMonthlySubscriptionStartEndTimeUiFieldsForAdd(data, timeZone);

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_URL + "?vendorId=" + vendorId);
		
		return loadView(UrlConstants.JSP_URLS.VENDOR_MONTHLY_SUBSCRIPTION_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response addCitySurge(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.VENDOR_ID) String vendorId,
		@FormParam(FieldConstants.PAYMENT_TYPE) String paymentType,
		@FormParam(FieldConstants.TRANSACTION_ID) String transactionId
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!(loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_RETAIL_SUPPLY_URL) || loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_URL))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		UserProfileModel vendorModel = UserProfileModel.getAdminUserAccountDetailsById(vendorId);

		data.put(FieldConstants.VENDOR_ID, vendorId);
		data.put(FieldConstants.VENDOR_NAME, vendorModel.getFullName());
		data.put(FieldConstants.EMAIL_ADDRESS, vendorModel.getEmail());
		data.put(FieldConstants.PHONE_NO, MyHubUtils.formatPhoneNumber(vendorModel.getPhoneNoCode(), vendorModel.getPhoneNo()));
		data.put(FieldConstants.VENDOR_MONTHLY_SUBCRIPTION_FEE, StringUtils.valueOf(vendorModel.getVendorMonthlySubscriptionFee()));

		data.put(FieldConstants.PAYMENT_TYPE, paymentType);
		data.put(FieldConstants.TRANSACTION_ID, transactionId);

		VendorMonthlySubscriptionHistoryUtils.setVendorMonthlySubscriptionStartEndTimeUiFieldsForAdd(data, timeZone);

		boolean hasErrors = hasErrors();

		if (hasErrors) {
			return loadView(UrlConstants.JSP_URLS.VENDOR_MONTHLY_SUBSCRIPTION_JSP);
		}

		VendorMonthlySubscriptionHistoryUtils.addPaidVendorMonthlySubscriptionEntry(vendorModel, loginSessionMap.get(LoginUtils.USER_ID), paymentType, transactionId);

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_VENDOR_MONTHLY_SUBSCRIPTION_HISTORY_URL + "?vendorId=" + vendorId);
	}

	public boolean hasErrors() {

		boolean hasErrors = false;

		Validator validator = new Validator();

		validator.addValidationMapping(FieldConstants.PAYMENT_TYPE, messageForKeyAdmin("labelPaymentType"), new RequiredValidationRule());
		validator.addValidationMapping(FieldConstants.TRANSACTION_ID, messageForKeyAdmin("labelTransactionId"), new RequiredValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}

		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.VENDOR_MONTHLY_SUBSCRIPTION_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}