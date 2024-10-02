package com.webapp.actions.secure.ridelater;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.DriverTourRequestUtils;
import com.utils.myhub.GeoLocationUtil;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.UserRoleUtils;
import com.utils.myhub.VendorMonthlySubscriptionHistoryUtils;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverTourStatusModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.RideLaterSettingsModel;
import com.webapp.models.TourModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.WebSocketClient;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-ride-later")
public class ManageRideLaterAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getManageRideLater(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.SERVICE_TYPE_ID, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		data.put(FieldConstants.SUCCESS_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_BOOK_LATER_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_BOOK_LATER_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRideLaterList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));
		String serviceTypeId = dtu.getRequestParameter(FieldConstants.SERVICE_TYPE_ID);

		int total;
		List<TourModel> tourList;
		boolean isTakeRide = false;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> statusList = new ArrayList<String>();
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);

			total = TourModel.getVendorRideLaterTourListCount(dtu.getStartDatelong(), dtu.getEndDatelong(), statusList, loginSessionMap.get(LoginUtils.USER_ID), assignedRegionList, isTakeRide, serviceTypeId);
			tourList = TourModel.getVendorRideLaterTourListBySearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), statusList, loginSessionMap.get(LoginUtils.USER_ID),
						assignedRegionList, isTakeRide, serviceTypeId);
		} else {
			total = TourModel.getRideLaterTourListCount(dtu.getStartDatelong(), dtu.getEndDatelong(), isTakeRide, serviceTypeId);
			tourList = TourModel.getRideLaterTourListBySearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), isTakeRide, serviceTypeId);
		}

		for (TourModel tourModel : tourList) {

			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(tourModel.getTourId());
			dtuInnerJsonArray.put(tourModel.getUserTourId());

			if (StringUtils.validString(tourModel.getSourceAddress()) && tourModel.getSourceAddress().length() > 100) {
				dtuInnerJsonArray.put(MyHubUtils.getTrimmedTo(tourModel.getSourceAddress(), 98) + "..");
			} else {

				if (StringUtils.validString(tourModel.getSourceAddress())) {
					dtuInnerJsonArray.put(tourModel.getSourceAddress());
				} else {
					dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				}
			}

			if (StringUtils.validString(tourModel.getDestinationAddress()) && tourModel.getDestinationAddress().length() > 100) {
				dtuInnerJsonArray.put(MyHubUtils.getTrimmedTo(tourModel.getDestinationAddress(), 98) + "..");
			} else {

				if (StringUtils.validString(tourModel.getDestinationAddress())) {
					dtuInnerJsonArray.put(tourModel.getDestinationAddress());
				} else {
					dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
				}
			}

			// Passengre vendor is equal to the logged in vendor, the show passenger details
			if (tourModel.getPassengerVendorId().equalsIgnoreCase(loginSessionMap.get(LoginUtils.USER_ID))) {
				dtuInnerJsonArray.put(MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(tourModel.getRideLaterPickupTimeLogs())) {
				dtuInnerJsonArray.put(tourModel.getRideLaterPickupTimeLogs());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			if (StringUtils.validString(tourModel.getDriverId()) && !tourModel.getDriverId().equalsIgnoreCase(ProjectConstants.DEFAULT_DRIVER_ID)) {
				dtuInnerJsonArray.put(MyHubUtils.formatFullName(tourModel.getFirstName(), tourModel.getLastName()));
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuInnerJsonArray.put(tourModel.getCarType());

			dtuInnerJsonArray.put(TourUtils.getRideLaterTourStatus(tourModel));

			if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR)) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputButton("btnRideLaterAssign_" + tourModel.getTourId(), messageForKeyAdmin("labelAssign"), FieldConstants.RIDE_LATER_ASSIGN));
			} else if (TourUtils.rideLaterDriverAssignmentCriteriaStatus(tourModel)) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputButton("btnRideLaterReassign_" + tourModel.getTourId(), messageForKeyAdmin("labelReassign"), FieldConstants.RIDE_LATER_REASSIGN));
			} else {
				dtuInnerJsonArray.put(tourModel.getStatus());
			}

			if (tourModel.getServiceTypeId().equalsIgnoreCase(ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewDetails"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.ADMIN_BOOKING_DETAILS_URL + "?tourId=" + tourModel.getTourId() + "&tourType=rideLater")));
			} else {
				btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewDetails"),
							UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.VIEW_COURIER_DETAILS_URL + "?tourId=" + tourModel.getTourId())));
			}

			if (!tourModel.isTakeRide() && tourModel.getDriverId().equalsIgnoreCase(ProjectConstants.DEFAULT_DRIVER_ID)) {
				// if tour is not marked as take ride, and no driver is assigned to it
				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(null, null, UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.TAKE_RIDE_URL + "?type=takeride&tourId=" + tourModel.getTourId()), "take-ride"));
			}

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? tourList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/ride-later-driver-list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRideLaterDriverList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!(loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_BOOK_LATER_URL) || loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_CRITICAL_BOOK_LATER_URL) || loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_TAKE_RIDE_URL))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);
		String tourId = dtu.getRequestParameter(FieldConstants.TOUR_ID);
		String tourType = dtu.getRequestParameter(FieldConstants.TOUR_TYPE);

		TourModel tour = TourModel.getTourDetailsByTourId(tourId);
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		String distance = GeoLocationUtil.getDistanceQuery(tour.getsLatitude(), tour.getsLongitude(), GeoLocationUtil.CAR_LOCATION);
		String latAndLong = "ST_DWithin(car_location,ST_GeographyFromText('SRID=4326;POINT(" + tour.getsLongitude() + " " + tour.getsLatitude() + ")'),  " + adminSettingsModel.getRadiusString() + ")";

		Map<String, Object> inputMap = new HashMap<String, Object>();

		String status = ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST + "," + ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR + "," + ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR;

		String[] statusArray = MyHubUtils.splitStringByCommaArray(status);

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		inputMap.put("tourId", tourId);
		inputMap.put("carTypeId", tour.getCarTypeId());
		inputMap.put("globalSearchString", dtu.getGlobalSearchStringWithPercentage());
		inputMap.put("startOffSet", dtu.getStartInt());
		inputMap.put("recordOffset", dtu.getLengthInt());
		inputMap.put("latAndLong", latAndLong);
		inputMap.put("distance", distance);
		inputMap.put("driverTripBeforeTime", (tour.getRideLaterPickupTime() - (rideLaterSettingsModel.getDriverAllocateBeforeTime() * ProjectConstants.RIDE_LATER_CRON_JOB_TIME_1MIN)));
		inputMap.put("driverTripAfterTime", (tour.getRideLaterPickupTime() + (rideLaterSettingsModel.getDriverAllocateAfterTime() * ProjectConstants.RIDE_LATER_CRON_JOB_TIME_1MIN)));
		inputMap.put("statusArray", statusArray);
		inputMap.put("transmissionTypeIdList", DriverGeoLocationModel.getDriveTransmissionListFromType(tour.getTransmissionTypeId(), tour.getCarTypeId()));

		if (tourType.equalsIgnoreCase("Assign")) {
			inputMap.put("assignStatus", null);
		} else {
			inputMap.put("assignStatus", "-");
		}

		inputMap.put("tourPickupTime", tour.getRideLaterPickupTime());

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			inputMap.put("vendorId", loginSessionMap.get(LoginUtils.USER_ID));
		} else {
			inputMap.put("vendorId", null);
		}

		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

		double minimumWalletAmount = 0.0;

		if (driverWalletSettingsModel != null) {
			minimumWalletAmount = driverWalletSettingsModel.getMinimumAmount();
		}

		inputMap.put("minimumWalletAmount", minimumWalletAmount);

		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);
		MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(inputMap, loginSessionMap.get(LoginUtils.USER_ID), tour.getRideLaterPickupTime());

		int total = UserProfileModel.getRideLaterDriverListCount(inputMap);
		List<UserProfileModel> driverList = UserProfileModel.getRideLaterDriverListBySearch(inputMap);

		int count = dtu.getStartInt();

		for (UserProfileModel user : driverList) {

			count++;
			btnGroupStr = new StringBuilder();

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(user.getUserId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(MyHubUtils.formatFullName(user.getFirstName(), user.getLastName()));

			DriverTourStatusModel driverTourStatusModel = DriverTourStatusModel.getDriverTourStatus(user.getUserId());

			if (driverTourStatusModel != null) {
				dtuInnerJsonArray.put(driverTourStatusModel.getStatus());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuInnerJsonArray.put(NewThemeUiUtils.outputButton("btnRideLaterAssign_" + tourId + "_" + user.getUserId(), messageForKeyAdmin("labelAssign"), FieldConstants.RIDE_LATER_DRIVER_ASSIGN));

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? driverList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		dtuJsonObject.put("sourceAddress", tour.getSourceAddress());
		dtuJsonObject.put("destinationAddress", tour.getDestinationAddress());

		return sendDataResponse(dtuJsonObject.toString());
	}

	@GET
	@Path("/assign-driver")
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response assignDriverRideLater(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.TOUR_TYPE) String tourType,
		@QueryParam(FieldConstants.TOUR_ID) String tourId,
		@QueryParam(FieldConstants.DRIVER_ID) String driverId
		) throws IOException, ServletException {
	//@formatter:on		

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!(loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_BOOK_LATER_URL) || loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_CRITICAL_BOOK_LATER_URL) || loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_TAKE_RIDE_URL))) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		Map<String, Object> output = new HashMap<String, Object>();

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

		if (tourModel == null) {
			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorFailedToAssignTour"));
			return sendDataResponse(output);
		}

		if (tourModel.getDriverId().equalsIgnoreCase(driverId)) {
			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorTourAssignedAlreadyToDriver"));
			return sendDataResponse(output);
		}

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			TourModel driverTourModel = TourModel.getDriverDetailsByTourId(tourId);

			if (driverTourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR) && !(driverTourModel.getVendorId().equals(loginSessionMap.get(LoginUtils.USER_ID)))) {
				output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
				output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorTourAssignedAlreadyToOtherDriver"));
				return sendDataResponse(output);
			}
		}

		//@formatter:off
		if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR) 
			|| tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING_REQUEST)
			|| tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST)
			|| tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR)
			|| tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR)) {
		//@formatter:on

			if (!ProjectConstants.DEFAULT_DRIVER_ID.equalsIgnoreCase(tourModel.getDriverId()) && !tourModel.getDriverId().equalsIgnoreCase(driverId)) {
				String message = String.format(BusinessAction.messageForKeyAdmin("rideLaterTripAssignedToAnotherDriver"), tourModel.getUserTourId());
				MyHubNotificationUtils.sendPushNotificationToUser(tourModel.getDriverId(), message);
			}

			String status = "";

			if ((tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR)) || (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR))) {
				status = ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR;
			} else {
				status = ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR;
			}

			assignRideLaterTourToDriver(tourId, driverId, tourModel, status, true);

			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);
			output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("successDriverAssignedSuccessfully"));

			return sendDataResponse(output);

		} else {

			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);

			if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {
				output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorTourCancelledByPassenger"));
			} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.EXPIRED_TOUR)) {
				output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorTourExpired"));
			} else {
				output.put(ProjectConstants.STATUS_MESSAGE, messageForKeyAdmin("errorFailedToAssignTour"));
			}

			return sendDataResponse(output);
		}
	}

	public static void assignRideLaterTourToDriver(String tourId, String driverId, TourModel tourModel, String status, boolean sendSocket) {

		TourUtils.assignTourDriver(tourId, driverId, status, driverId);

		TourUtils.updateTourCriticalStatus(tourModel, false);

		DriverTourRequestUtils.createDriverTourRequest(tourId, driverId);

		if (sendSocket) {

			RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

			Map<String, Object> outPutMap = new HashMap<String, Object>();
			outPutMap.put("tourId", tourModel.getTourId());
			outPutMap.put("userTourId", tourModel.getUserTourId());
			outPutMap.put("passengerId", tourModel.getPassengerId());
			outPutMap.put("driverId", tourModel.getDriverId());
			outPutMap.put("dateTime", String.valueOf(tourModel.getCreatedAt()));
			outPutMap.put("firstName", tourModel.getpFirstName());
			outPutMap.put("lastName", tourModel.getpLastName());
			outPutMap.put("email", tourModel.getpEmail());
			outPutMap.put("phone", tourModel.getpPhone());
			outPutMap.put("phoneCode", tourModel.getpPhoneCode());
			outPutMap.put("photoUrl", tourModel.getpPhotoUrl());
			outPutMap.put("sourceLatitude", tourModel.getsLatitude());
			outPutMap.put("sourceLongitude", tourModel.getsLongitude());
			outPutMap.put("destinationLatitude", tourModel.getdLatitude());
			outPutMap.put("destinationLongitude", tourModel.getdLongitude());
			outPutMap.put("sourceAddress", tourModel.getSourceAddress());
			outPutMap.put("destinationAddress", tourModel.getDestinationAddress());
			outPutMap.put("status", tourModel.getStatus());
			outPutMap.put("jobExpireTime", rideLaterSettingsModel.getDriverJobRequestTime());
			outPutMap.put("rideLaterPickupTime", tourModel.getRideLaterPickupTime());
			outPutMap.put("isRideLater", tourModel.isRideLater());
			outPutMap.put("isAcknowledged", tourModel.isAcknowledged());

			JSONObject jsonMessage = new JSONObject(outPutMap);

			ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(driverId);

			if (apnsDevice != null) {
				String messge = "TOUR`1`" + apnsDevice.getDeviceToken() + ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.RLNJ_SOCKET + jsonMessage.toString();
				WebSocketClient.sendDriverNotification(messge, driverId, apnsDevice.getApiSessionKey());
			}

			String message = BusinessAction.messageForKeyAdmin("successRideLaterNewJobMessage");
			MyHubNotificationUtils.insertSocketNotification(driverId, message);
		}
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_BOOK_LATER_JS, UrlConstants.JS_URLS.RIDE_LATER_DRIVER_ASSIGNMENT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}