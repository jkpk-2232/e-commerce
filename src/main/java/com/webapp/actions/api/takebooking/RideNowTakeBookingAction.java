package com.webapp.actions.api.takebooking;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.myhub.DriverSubscriptionUtils;
import com.utils.myhub.GeoLocationUtil;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MulticityRegionUtils;
import com.utils.myhub.TourUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.ridelater.RideLaterAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.DriverCarTypeModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.TourModel;

@Path("/api/ride-now/take-bookings")
public class RideNowTakeBookingAction extends BusinessApiAction {

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRideNowBookingListForTakeBookings(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		String driverId = loggedInuserId;

		// Check for driver current location.
		DriverGeoLocationModel driverLocation = DriverGeoLocationModel.getCurrentDriverPosition(driverId);
		if (driverLocation == null) {
			return sendBussinessError(messageForKey("pushMessageToIdealDrivers", request));
		}

		// Check if the driver is currently online.
		DriverInfoModel driverInfoModel = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(driverId);
		if (!driverInfoModel.isOnDuty()) {
			return sendBussinessError(messageForKey("errorDriverNotOnline", request));
		}

		// Current driver should have active current subscription.
		String currentSubscriptionPackageId = DriverSubscriptionUtils.getDriverCurrentSubscription(driverId, headerVendorId, DateUtils.nowAsGmtMillisec());
		if (currentSubscriptionPackageId == null) {
			return sendBussinessError(messageForKey("errorSubscriptionPackageNotActive", request));
		}

		// Current driver calling the api should not have any active booking.
		TourModel driverCurrentTour = TourModel.getCurrentTourByDriverIdForRideNowTakeBooking(driverId);
		if (driverCurrentTour != null) {
			return sendBussinessError(messageForKey("errorTakeBookingCurrentActiveBooking", request));
		}

		// Current driver no upcoming ride later request.
		boolean conflictingTimeSlots = TourUtils.checkForConflictingTourRideLater(null, driverId, DateUtils.nowAsGmtMillisec());
		if (conflictingTimeSlots) {
			return sendBussinessError(messageForKey("errorTakeBookingConflictingTimeSlots", request));
		}

		List<String> regionList = MulticityRegionUtils.getUserAccessRegionList(driverId);
		List<String> statusList = Arrays.asList(ProjectConstants.TourStatusConstants.NEW_TOUR, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);

		List<String> carTypeList = new ArrayList<String>();
		List<DriverCarTypeModel> driverCarTypeModelList = DriverCarTypeModel.getDriverCarTypesListByDriverId(driverId);

		for (DriverCarTypeModel driverCarTypeModel : driverCarTypeModelList) {
			carTypeList.add(driverCarTypeModel.getCarTypeId());
		}

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		String latAndLong = GeoLocationUtil.getLatLngQuery(driverLocation.getLatitude(), driverLocation.getLongitude(), GeoLocationUtil.SOURCE_LOCATION, adminSettingsModel.getRadiusString());

		List<TourModel> tourList = TourModel.getRideNowToursForTakeBookings(driverId, regionList, carTypeList, start, length, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID, statusList, latAndLong);

		List<Map<String, Object>> outerOutPutMap = RideLaterAction.setTourListForBookings(tourList, null);

		Map<Object, Object> res = new HashMap<Object, Object>();
		res.put("tourList", outerOutPutMap);
		return sendDataResponse(res);
	}

	@Path("/{tourId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response takeBookingsRideNow(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("tourId") String tourId
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);
		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		String driverId = loggedInuserId;

		// Check for driver current location.
		DriverGeoLocationModel driverLocation = DriverGeoLocationModel.getCurrentDriverPosition(driverId);
		if (driverLocation == null) {
			return sendBussinessError(messageForKey("pushMessageToIdealDrivers", request));
		}

		// Check if the driver is currently online.
		DriverInfoModel driverInfoModel = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(driverId);
		if (!driverInfoModel.isOnDuty()) {
			return sendBussinessError(messageForKey("errorDriverNotOnline", request));
		}

		// Current driver should have active current subscription.
		String currentSubscriptionPackageId = DriverSubscriptionUtils.getDriverCurrentSubscription(driverId, headerVendorId, DateUtils.nowAsGmtMillisec());
		if (currentSubscriptionPackageId == null) {
			return sendBussinessError(messageForKey("errorSubscriptionPackageNotActive", request));
		}

		// Current driver calling the api should not have any active booking.
		TourModel driverCurrentTour = TourModel.getCurrentTourByDriverId(driverId);
		if (driverCurrentTour != null) {
			return sendBussinessError(messageForKey("errorTakeBookingCurrentActiveBooking", request));
		}

		// Current driver no upcoming ride later request.
		boolean conflictingTimeSlots = TourUtils.checkForConflictingTourRideLater(tourId, driverId, DateUtils.nowAsGmtMillisec());
		if (conflictingTimeSlots) {
			return sendBussinessError(messageForKey("errorTakeBookingConflictingTimeSlots", request));
		}

		Map<String, Object> outputMap = TourUtils.acceptTourByDriver(request, headerVendorId, tourId, driverId, true);

		if (outputMap.get(ProjectConstants.STATUS_TYPE).toString().equalsIgnoreCase(ProjectConstants.STATUS_ERROR)) {
			return sendBussinessError(outputMap.get(ProjectConstants.STATUS_MESSAGE).toString());
		} else {
			return sendDataResponse(outputMap);
		}
	}
}