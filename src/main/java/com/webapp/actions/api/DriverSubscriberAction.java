package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DriverSubscriberModel;
import com.webapp.models.UserProfileModel;

@Path("/api/driver-subscriber")
public class DriverSubscriberAction extends BusinessApiAction {

	private static final String DRIVER_ID = "driverId";
	private static final String DRIVER_ID_LABEL = "Driver Id";

	private static final String PRIORITY_NUMBER = "priorityNumber";
	private static final String PRIORITY_NUMBER_LABEL = "Priority Number";

	@Path("/search-drivers/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response searchDrivers(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		if (searchKey == null || "".equalsIgnoreCase(searchKey) || searchKey.length() < 3) {
			return sendBussinessError(messageForKey("errorSearchKey", request));
		}

		List<Map<String, Object>> driverList = new ArrayList<>();

		List<UserProfileModel> list = UserProfileModel.getDriverListForSearch(loggedInuserId, UserRoles.DRIVER_ROLE_ID, "%" + searchKey + "%", start, length);

		for (UserProfileModel userProfileModel : list) {
			driverList.add(DriverSubscriberModel.getLimitedData(userProfileModel));
		}

		Map<String, List<Map<String, Object>>> finalOuput = new HashMap<>();
		finalOuput.put("driverList", driverList);

		return sendDataResponse(finalOuput);
	}

	@Path("/my-subscribed-drivers/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response listUserSubscription(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("searchKey") String searchKey
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<Map<String, Object>> driverList = new ArrayList<>();

		List<UserProfileModel> list = UserProfileModel.getUserDriverSubscriptionByUserId(loggedInuserId, start, length, "%" + searchKey + "%");

		for (UserProfileModel userProfileModel : list) {
			driverList.add(DriverSubscriberModel.getLimitedData(userProfileModel));
		}

		Map<String, List<Map<String, Object>>> finalOuput = new HashMap<>();
		finalOuput.put("driverList", driverList);

		return sendDataResponse(finalOuput);
	}

	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response driverSubscriber(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		DriverSubscriberModel driverSubscriberModel
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		errorMessages = driverSubscriberModelValidation(driverSubscriberModel, headerVendorId);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		boolean isUserSubscribedToDriver = DriverSubscriberModel.isUserSubscribedToDriver(driverSubscriberModel.getDriverId(), loggedInuserId);

		if (isUserSubscribedToDriver) {
			return sendBussinessError(messageForKey("errorAlreadyDriverSubscribed", request));
		}

		driverSubscriberModel.setUserId(loggedInuserId);
		driverSubscriberModel.insertDriverSubscriber(loggedInuserId);

		return sendSuccessMessage(messageForKey("successDriverSubscribed", request));
	}

	@PUT
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response editDriverSubscriber(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		DriverSubscriberModel driverSubscriberModel
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		errorMessages = driverSubscriberModelValidation(driverSubscriberModel, headerVendorId);
		if (errorMessages.size() != 0) {
			return sendBussinessError(errorMessages);
		}

		boolean isUserSubscribedToDriver = DriverSubscriberModel.isUserSubscribedToDriver(driverSubscriberModel.getDriverId(), loggedInuserId);

		if (!isUserSubscribedToDriver) {
			return sendBussinessError(messageForKey("errorNotDriverSubscribed", request));
		}

		// delete old entry of priority
		driverSubscriberModel.setUserId(loggedInuserId);
		driverSubscriberModel.deleteDriverSubscriber();

		// insert new entry with new priority
		driverSubscriberModel.insertDriverSubscriber(loggedInuserId);

		return sendSuccessMessage(messageForKey("successDriverSubscribedPriority", request));
	}

	@Path("/{driverId}")
	@DELETE
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response driverUnSubscriber(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("driverId") String driverId
		) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		boolean isUserSubscribedToDriver = DriverSubscriberModel.isUserSubscribedToDriver(driverId, loggedInuserId);

		if (!isUserSubscribedToDriver) {
			return sendBussinessError(messageForKey("errorNotDriverSubscribed", request));
		}

		DriverSubscriberModel dsm = new DriverSubscriberModel();
		dsm.setDriverId(driverId);
		dsm.setUserId(loggedInuserId);
		dsm.deleteDriverSubscriber();

		return sendSuccessMessage(messageForKey("successDriverUnSubscribed", request));
	}

	private List<String> driverSubscriberModelValidation(DriverSubscriberModel driverSubscriberModel, String headerVendorId) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(DRIVER_ID, DRIVER_ID_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(PRIORITY_NUMBER, PRIORITY_NUMBER_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(driverSubscriberModel);

		return errorMessages;
	}
}