package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.myhub.DriverSubscriptionUtils;
import com.utils.myhub.MultiTenantUtils;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DriverSubscriptionPackageHistoryModel;
import com.webapp.models.SubscriptionPackageModel;

@Path("/api/subscription-package")
public class SubscriptionPackageAction extends BusinessApiAction {

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCountryList(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("carTypeId") String carTypeId
		) throws IOException, SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		if (carTypeId != null && carTypeId.equalsIgnoreCase("-1")) {
			carTypeId = null;
		}

		Map<String, Object> outputMap = new HashMap<>();
		outputMap.put("subscriptionList", SubscriptionPackageModel.getAllActiveSubscriptionPackagesList(start, length, carTypeId));
		return sendDataResponse(outputMap);
	}

	@Path("/{driverId}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getMyPackageList(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("driverId") String driverId,
		@PathParam("start") int start,
		@PathParam("length") int length
		) throws IOException, SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> outputMap = new HashMap<>();
		outputMap.put("driverSubscriptionList", DriverSubscriptionPackageHistoryModel.getDriverSubscriptionPackageHistoryList(driverId, start, length));
		return sendDataResponse(outputMap);
	}

	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response subscribePackage(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		SubscriptionPackageModel subscriptionPackageModel
		) throws IOException, SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		errorMessages = SubscriptionPackageModelValidation(subscriptionPackageModel);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		return sendBussinessError(messageForKey("errorMoreThanOnePackageSubscribed", request));

//		boolean test = DriverSubscriptionUtils.processDriverSubscription(subscriptionPackageModel, loggedInuserId, headerVendorId, ProjectConstants.PAYMENT_TYPE_CASH);
//
//		if (!test) {
//			return sendBussinessError(messageForKey("errorMoreThanOnePackageSubscribed", request));
//		}
//
//		return sendSuccessMessage(messageForKey("successDriverSubscriptionPackage", request));
	}

	@Path("/check-package-validity")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response checkPackageValidity(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader
		) throws IOException, SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		String currentSubscriptionPackageId = DriverSubscriptionUtils.getDriverCurrentSubscription(loggedInuserId, headerVendorId, DateUtils.nowAsGmtMillisec());
		if (currentSubscriptionPackageId == null) {
			return sendBussinessError(messageForKey("errorSubscriptionPackageNotValid", request));
		}

		long time = DateUtils.nowAsGmtMillisec();
		boolean packageValid = false;
		List<DriverSubscriptionPackageHistoryModel> list = DriverSubscriptionPackageHistoryModel.getDriverSubscriptionHistoryByPackageIdDriverId(currentSubscriptionPackageId, loggedInuserId, headerVendorId);
		for (DriverSubscriptionPackageHistoryModel driverSubscriptionPackageHistoryModel : list) {

			//@formatter:off
			if (driverSubscriptionPackageHistoryModel.isCurrentPackage() 
				&& driverSubscriptionPackageHistoryModel.getPackageStartTime() <= time 
				&& time <= driverSubscriptionPackageHistoryModel.getPackageEndTime()) {
			//@formatter:on

				// package is valid
				packageValid = true;

				String driverSubscriptionPackageHistoryId = driverSubscriptionPackageHistoryModel.getDriverSubscriptionPackageHistoryId();

				// IMP mark rest of driverSubscriptionPackageHistoryId "current package" flag as
				// false
				driverSubscriptionPackageHistoryModel.setDriverSubscriptionPackageHistoryId(null);
				driverSubscriptionPackageHistoryModel.setDriverId(driverSubscriptionPackageHistoryModel.getDriverId());
				driverSubscriptionPackageHistoryModel.setVendorId(driverSubscriptionPackageHistoryModel.getVendorId());
				driverSubscriptionPackageHistoryModel.setCurrentPackage(false);
				driverSubscriptionPackageHistoryModel.updateExistingPackagesAsNotCurrent();

				// IMP mark the current driverSubscriptionPackageHistoryId as current flag as
				// true
				driverSubscriptionPackageHistoryModel.setDriverSubscriptionPackageHistoryId(driverSubscriptionPackageHistoryId);
				driverSubscriptionPackageHistoryModel.setCurrentPackage(true);
				driverSubscriptionPackageHistoryModel.updateExistingPackagesAsNotCurrent();

				break;
			}
		}

		return (packageValid) ? sendSuccessMessage(messageForKey("successSubscriptionPackageValid", request)) : sendBussinessError(messageForKey("errorSubscriptionPackageNotValid", request));
	}

	private List<String> SubscriptionPackageModelValidation(SubscriptionPackageModel subscriptionPackageModel) {

		Validator validator = new Validator();

		validator.addValidationMapping("subscriptionPackageId", "Subscription Package Id", new RequiredValidationRule());
		errorMessages = validator.validate(subscriptionPackageModel);

		return errorMessages;
	}
}