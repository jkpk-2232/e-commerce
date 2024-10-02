package com.webapp.actions.api.takebooking;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MulticityRegionUtils;
import com.utils.myhub.TourUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.ridelater.RideLaterAction;
import com.webapp.actions.secure.ridelater.ManageRideLaterAction;
import com.webapp.models.DriverCarTypeModel;
import com.webapp.models.RideLaterSettingsModel;
import com.webapp.models.TourModel;

@Path("/api/ride-later/take-bookings")
public class RideLaterTakeBookingAction extends BusinessApiAction {

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAdvanceBookingListForTakeBookings(
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

		String currentSubscriptionPackageId = DriverSubscriptionUtils.getDriverCurrentSubscription(driverId, headerVendorId, DateUtils.nowAsGmtMillisec());
		if (currentSubscriptionPackageId == null) {
			return sendBussinessError(messageForKey("errorSubscriptionPackageNotActive", request));
		}

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		List<String> regionList = MulticityRegionUtils.getUserAccessRegionList(driverId);

		List<String> carTypeList = new ArrayList<String>();
		List<DriverCarTypeModel> driverCarTypeModelList = DriverCarTypeModel.getDriverCarTypesListByDriverId(driverId);

		for (DriverCarTypeModel driverCarTypeModel : driverCarTypeModelList) {
			carTypeList.add(driverCarTypeModel.getCarTypeId());
		}

		long maxTime = DateUtils.nowAsGmtMillisec() + (rideLaterSettingsModel.getTakeBookingForNextXDays() * ProjectConstants.ONE_DAY_MILLISECONDS_LONG);

		List<TourModel> tourList = TourModel.getRideLaterToursForTakeBookings(driverId, regionList, carTypeList, maxTime, start, length, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		List<Map<String, Object>> outerOutPutMap = RideLaterAction.setTourListForBookings(tourList, null);

		Map<Object, Object> res = new HashMap<Object, Object>();
		res.put("tourList", outerOutPutMap);
		return sendDataResponse(res);
	}

	@Path("/{tourId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response takeBookings(
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

		String currentSubscriptionPackageId = DriverSubscriptionUtils.getDriverCurrentSubscription(loggedInuserId, headerVendorId, DateUtils.nowAsGmtMillisec());
		if (currentSubscriptionPackageId == null) {
			return sendBussinessError(messageForKey("errorSubscriptionPackageNotActive", request));
		}

		String driverId = loggedInuserId;
		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

//		"Driver should be able to do take booking
//		--> Maintain flag and timestamp
//		--> If not rl-new then return error -> Booking not available to take
//		--> Check for max number of bookings allowed
//		--> Check for any collision of time
//		--> Assign the driver booking by rl-accepted"

		// if the tour status is not rl-new then do not allow the driver to do take
		// booking
		if (!tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR)) {
			return sendBussinessError(messageForKey("errorTakeBookingNotAvailableToTake", request));
		}

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		int driverCurrentTakeBookingCount = TourModel.getTakeBookingDriverCurrentCount(driverId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		// check for maximum number of bookings allowed for the driver as take bookings
		if (driverCurrentTakeBookingCount >= rideLaterSettingsModel.getTakeBookingMaxNumberAllowed()) {
			return sendBussinessError(messageForKey("errorMaxTakeBookingLimitReached", request));
		}

		// IMPORTANT NOTE ===> ADD getPassengerTourAfterTime instead of
		// getPassengerTourBeforeTime for calculating beforePickupTime
		boolean conflictingTimeSlots = TourUtils.checkForConflictingTourRideLater(tourId, driverId, tourModel.getRideLaterPickupTime(), rideLaterSettingsModel);
		if (conflictingTimeSlots) {
			return sendBussinessError(messageForKey("errorTakeBookingConflictingTimeSlots", request));
		}

		// step 1 move the trip to driver assigned
		ManageRideLaterAction.assignRideLaterTourToDriver(tourId, driverId, tourModel, ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR, false);

		// step 2 move the trip to driver accepted
		RideLaterAction.driverAcceptsRideLaterRequest(tourId, driverId, tourModel);

//		--> Maintain flag and timestamp
		TourUtils.updateTakeBookingByDriverParams(driverId, tourModel);

		return sendSuccessMessage(messageForKey("successTakeBooking", request));
	}
}