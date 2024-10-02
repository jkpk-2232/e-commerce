package com.webapp.actions.secure.subscription.packages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

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

import com.utils.myhub.DriverSubscriptionUtils;
import com.utils.myhub.DropDownUtils;
import com.utils.myhub.MultiTenantUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.SubscriptionPackageModel;

@Path("/subscribe-package")
public class SubscribePackageAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getSubscribePackage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.DRIVER_ID) String driverId
		) throws ServletException, IOException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		String subscriptionPackageListOptions = DropDownUtils.getSubscriptionPackageListOptions("", driverId);
		data.put(FieldConstants.SUBSCRIPTION_PACKAGE_LIST_OPTIONS, subscriptionPackageListOptions);

		String paymentModeOptions = DropDownUtils.getPaymentTypeOption("");
		data.put(FieldConstants.PAYMENT_MODE_OPTIONS, paymentModeOptions);

		DriverInfoModel driverInfo = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(driverId);

		data.put(FieldConstants.DRIVER_ID, driverId);
		data.put(FieldConstants.DRIVER_NAME, driverInfo.getFullName());

		data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_URL + "?driverId=" + driverId);

		return loadView(UrlConstants.JSP_URLS.SUBSCRIBE_PACKAGE_JSP);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//@formatter:off
	public Response postSubscribePackage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@FormParam(FieldConstants.DRIVER_ID) String driverId,
		@FormParam(FieldConstants.SUBSCRIPTION_PACKAGE_LIST) String subscriptionPackageList,
		@FormParam(FieldConstants.PAYMENT_MODE) String paymentMode
		) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		SubscriptionPackageModel subscriptionPackageModel = new SubscriptionPackageModel();
		subscriptionPackageModel.setSubscriptionPackageId(subscriptionPackageList);

		boolean test = DriverSubscriptionUtils.processDriverSubscription(subscriptionPackageModel, driverId, MultiTenantUtils.getVendorIdByUserId(driverId), ProjectConstants.PAYMENT_TYPE_CASH);

		if (!test) {

			DriverInfoModel driverInfo = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(driverId);

			data.put(FieldConstants.DRIVER_ID, driverId);
			data.put(FieldConstants.DRIVER_NAME, driverInfo.getFullName());

			String subscriptionPackageListOptions = DropDownUtils.getSubscriptionPackageListOptions(subscriptionPackageList, driverId);
			data.put(FieldConstants.SUBSCRIPTION_PACKAGE_LIST_OPTIONS, subscriptionPackageListOptions);

			String paymentModeOptions = DropDownUtils.getPaymentTypeOption(paymentMode);
			data.put(FieldConstants.PAYMENT_MODE_OPTIONS, paymentModeOptions);

			data.put(FieldConstants.PACKAGE_SUBSCRIBE_ERROR, messageForKey("errorMoreThanOnePackageSubscribed"));

			data.put(FieldConstants.CANCEL_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_URL + "?driverId=" + driverId);

			return loadView(UrlConstants.JSP_URLS.SUBSCRIBE_PACKAGE_JSP);
		}

		return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_DRIVER_SUBSCRIPTION_PACKAGE_REPORTS_URL + "?driverId=" + driverId);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.SUBSCRIBE_PACKAGE_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}