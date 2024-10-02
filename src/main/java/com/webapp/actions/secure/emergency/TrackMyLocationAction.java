package com.webapp.actions.secure.emergency;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.jeeutils.FrameworkConstants;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminCompanyContactModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.PassengerGeoLocationModel;
import com.webapp.models.TourModel;
import com.webapp.models.TrackLocationTokenModel;
import com.webapp.models.TrackMyLocationModel;
import com.webapp.models.UserModel;

@Path("/track-me")
public class TrackMyLocationAction extends BusinessAction {

	@GET
	@Path("/{token}")
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response addGet(
		@Context HttpServletRequest req,
		@Context HttpServletResponse res, 
		@PathParam("token") @DefaultValue("") String token
		) throws ServletException, IOException {
	//@formatter:on
		preprocessRequest(req, res);

		String lang = FrameworkConstants.LANGUAGE_ENGLISH;

		data.put("contextPath", req.getContextPath());
		data.put("labelTrackMyLocation", messageForKeyAdmin("labelTrackMyLocation", lang).replace(" ", "&#32;"));

		TrackMyLocationModel trackMyLocationModel = null;
		String notDefinedStr = "Not Defined";
		AdminCompanyContactModel adminCompanyContactModel = AdminCompanyContactModel.getAdminCompanyContactByVendorId(UserRoles.SUPER_ADMIN_USER_ID);

		TrackLocationTokenModel trackLocationTokenModel = TrackLocationTokenModel.getTrackLocationTokenDetailsById(token);

		data.put("labelDriver", messageForKeyAdmin("labelDriver", lang).replace(" ", "&#32;"));
		data.put("labelPassenger", messageForKeyAdmin("labelPassenger", lang).replace(" ", "&#32;"));
		data.put("labelCarDetails", messageForKeyAdmin("labelCarDetails", lang).replace(" ", "&#32;"));
		data.put("labelTourDetails", messageForKeyAdmin("labelTourDetails", lang).replace(" ", "&#32;"));
		data.put("labelStatus", messageForKeyAdmin("labelStatus", lang).replace(" ", "&#32;"));

		data.put("labelModel", messageForKeyAdmin("labelModel", lang).replace(" ", "&#32;"));
		data.put("labelCarType", messageForKeyAdmin("labelCarType", lang).replace(" ", "&#32;"));
		data.put("labelColor", messageForKeyAdmin("labelColor", lang).replace(" ", "&#32;"));
		data.put("labelPlateNumber", messageForKeyAdmin("labelPlateNumber", lang).replace(" ", "&#32;"));

		data.put("labelContactNumber", messageForKeyAdmin("labelContactNumber", lang).replace(" ", "&#32;"));
		data.put("labelSupportEmail", messageForKeyAdmin("labelSupportEmail", lang).replace(" ", "&#32;"));

		data.put("emergencyDownloadMsg1", messageForKeyAdmin("emergencyDownloadMsg1", lang).replace(" ", "&#32;"));
		data.put("emergencyDownloadMsg2", messageForKeyAdmin("emergencyDownloadMsg2", lang).replace(" ", "&#32;"));
		data.put("emergencyDownloadMsg3", messageForKeyAdmin("emergencyDownloadMsg3", lang).replace(" ", "&#32;"));
		data.put("emergencyFooterRightsReservedMsg", messageForKeyAdmin("emergencyFooterRightsReservedMsg", lang).replace(" ", "&#32;"));

		data.put("labelPrivacyPolicy", messageForKeyAdmin("labelPrivacyPolicy", lang).replace(" ", "&#32;"));
		data.put("labelTermsofServices", messageForKeyAdmin("labelTermsofServices", lang).replace(" ", "&#32;"));

		data.put("errorMessage", messageForKeyAdmin("errorMessageForTrackingDetails", lang).replace(" ", "&#32;"));

		if (trackLocationTokenModel == null) {

			data.put("trackLocationStatus", "INVALID TOKEN");
			data.put("driverImageUrl", "/assets/image/icon_admin.png");
			data.put("driverName", notDefinedStr);
			data.put("driverPhoneNo", notDefinedStr);
			data.put("driverEmail", notDefinedStr);
			data.put("driverAvgRating", notDefinedStr);
			data.put("passengerName", notDefinedStr);
			data.put("passengerPhoneNo", notDefinedStr);
			data.put("passengerEmail", notDefinedStr);
			data.put("carType", notDefinedStr);
			data.put("carModel", notDefinedStr);
			data.put("carColor", notDefinedStr);
			data.put("plateNumber", notDefinedStr);
			data.put("sourceAddress", notDefinedStr);
			data.put("destinationAddress", notDefinedStr);
			data.put("tourStatus", notDefinedStr);

		} else {

			if (trackLocationTokenModel.getTourId() == null || "".equals(trackLocationTokenModel.getTourId())) {

				String roleId = UserModel.getRoleByUserId(trackLocationTokenModel.getUserId());

				if (roleId == null || "".equals(roleId)) {

				} else if (roleId.equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID)) {

					trackMyLocationModel = TrackMyLocationModel.getPassengerLastUpdatedLocationByUserId(trackLocationTokenModel.getUserId());

					if (trackMyLocationModel != null) {

						data.put("trackLocationStatus", "NOT TOUR");
						data.put("passengerId", trackMyLocationModel.getPassengerId());
						data.put("currentLat", "" + trackMyLocationModel.getSourceLatitude());
						data.put("currentLng", "" + trackMyLocationModel.getSourceLongitude());
						data.put("passengerName", trackMyLocationModel.getPassengerName());
						data.put("passengerPhoneNo", trackMyLocationModel.getPassengerPhoneNo());
						data.put("passengerEmail", trackMyLocationModel.getPassengerEmail());

					} else {

						data.put("trackLocationStatus", "INVALID TOKEN");
						data.put("passengerName", notDefinedStr);
						data.put("passengerPhoneNo", notDefinedStr);
						data.put("passengerEmail", notDefinedStr);
					}

					data.put("driverImageUrl", "/assets/image/icon_admin.png");
					data.put("driverName", notDefinedStr);
					data.put("driverPhoneNo", notDefinedStr);
					data.put("driverEmail", notDefinedStr);
					data.put("driverAvgRating", notDefinedStr);
					data.put("carType", notDefinedStr);
					data.put("carModel", notDefinedStr);
					data.put("carColor", notDefinedStr);
					data.put("plateNumber", notDefinedStr);
					data.put("sourceAddress", notDefinedStr);
					data.put("destinationAddress", notDefinedStr);
					data.put("tourStatus", notDefinedStr);

				} else if (roleId.equalsIgnoreCase(UserRoles.DRIVER_ROLE_ID)) {

					trackMyLocationModel = TrackMyLocationModel.getDriverUpdatedLocationWithDetailsById(trackLocationTokenModel.getUserId());

					if (trackMyLocationModel != null) {

						data.put("trackLocationStatus", "NOT TOUR");
						data.put("passengerId", trackMyLocationModel.getDriverId());
						data.put("driverImageUrl", "/assets/image/icon_admin.png");
						data.put("driverName", trackMyLocationModel.getDriverName());
						data.put("driverPhoneNo", trackMyLocationModel.getDriverPhoneNo());
						data.put("driverEmail", trackMyLocationModel.getDriverEmail());
						data.put("driverAvgRating", "" + trackMyLocationModel.getDriverAvgRating());
						data.put("currentLat", "" + trackMyLocationModel.getSourceLatitude());
						data.put("currentLng", "" + trackMyLocationModel.getSourceLongitude());
						data.put("carType", trackMyLocationModel.getCarType());
						data.put("carModel", trackMyLocationModel.getCarModel());
						data.put("carColor", trackMyLocationModel.getCarColor());
						data.put("plateNumber", trackMyLocationModel.getCarPlateNo());

					} else {

						data.put("trackLocationStatus", "INVALID TOKEN");
						data.put("driverImageUrl", "/assets/image/icon_admin.png");
						data.put("driverName", notDefinedStr);
						data.put("driverPhoneNo", notDefinedStr);
						data.put("driverEmail", notDefinedStr);
						data.put("driverAvgRating", notDefinedStr);
						data.put("carType", notDefinedStr);
						data.put("carModel", notDefinedStr);
						data.put("carColor", notDefinedStr);
						data.put("plateNumber", notDefinedStr);
					}

					data.put("passengerName", notDefinedStr);
					data.put("passengerPhoneNo", notDefinedStr);
					data.put("passengerEmail", notDefinedStr);
					data.put("sourceAddress", notDefinedStr);
					data.put("destinationAddress", notDefinedStr);
					data.put("tourStatus", notDefinedStr);

				}

			} else {

				trackMyLocationModel = TrackMyLocationModel.getPassengerTourDetailsByTourId(trackLocationTokenModel.getTourId());
				TourModel tourModel = TourModel.getTourDetailsByTourId(trackLocationTokenModel.getTourId());
				if (tourModel != null && tourModel.getPassengerVendorId() != null) {
					adminCompanyContactModel = AdminCompanyContactModel.getAdminCompanyContactByVendorId(tourModel.getPassengerVendorId());
				}

				if (ProjectConstants.TourStatusConstants.STARTED_TOUR.equals(trackMyLocationModel.getTourStatus())) {

					data.put("trackLocationStatus", "ACTIVE TOUR");

					DriverGeoLocationModel driverGeoLocationModel = DriverGeoLocationModel.getCurrentDriverPosition(trackMyLocationModel.getDriverId());

					if (driverGeoLocationModel != null) {
						data.put("driverId", driverGeoLocationModel.getDriverId());
						data.put("driverLat", driverGeoLocationModel.getLatitude());
						data.put("driverLng", driverGeoLocationModel.getLongitude());
						data.put("tourStatus", messageForKeyAdmin("statusInTrip", lang).replace(" ", "&#32;"));
					}

				} else if (ProjectConstants.TourStatusConstants.ENDED_TOUR.equals(trackMyLocationModel.getTourStatus())) {

					data.put("trackLocationStatus", "ENDED TOUR");
					data.put("tourStatus", messageForKeyAdmin("statusNotInTrip", lang).replace(" ", "&#32;"));
				}

				data.put("passengerId", "" + trackMyLocationModel.getPassengerId());
				data.put("passengerName", trackMyLocationModel.getPassengerName());
				data.put("passengerPhoneNo", trackMyLocationModel.getPassengerPhoneNo());
				data.put("passengerEmail", trackMyLocationModel.getPassengerEmail());
				data.put("driverImageUrl", trackMyLocationModel.getDriverPhotoUrl());
				data.put("driverName", trackMyLocationModel.getDriverName());
				data.put("driverPhoneNo", trackMyLocationModel.getDriverPhoneNo());
				data.put("driverEmail", trackMyLocationModel.getDriverEmail());
				data.put("driverAvgRating", "" + trackMyLocationModel.getDriverAvgRating());
				data.put("carType", trackMyLocationModel.getCarType());
				data.put("carModel", trackMyLocationModel.getCarModel());
				data.put("carColor", trackMyLocationModel.getCarColor());
				data.put("plateNumber", trackMyLocationModel.getCarPlateNo());
				data.put("sourceAddress", trackMyLocationModel.getSourceAddress());
				data.put("destinationAddress", trackMyLocationModel.getDestinationAddress());
				data.put("sourseLat", "" + trackMyLocationModel.getSourceLatitude());
				data.put("sourseLng", "" + trackMyLocationModel.getSourceLongitude());
				data.put("destLat", "" + trackMyLocationModel.getDestLatitude());
				data.put("destLng", "" + trackMyLocationModel.getDestLongitude());
			}
		}

		if (adminCompanyContactModel != null) {
			data.put("contactNumber", adminCompanyContactModel.getPhoneNumberOne());
			data.put("supportEmail", adminCompanyContactModel.getEmail());
		} else {
			data.put("contactNumber", "");
			data.put("supportEmail", "");
		}

		request.setAttribute(FrameworkConstants.LOADED_VIEW, true);

		return loadView("/secure/emergency/track-my-location.jsp");
	}

	@Path("/passenger/last-location")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response passengerLocation(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@QueryParam("passengerId") String passengerId
			) throws ServletException, IOException {
	//@formatter:on

		Map<String, Object> outputMap = new HashMap<String, Object>();

		String roleId = UserModel.getRoleByUserId(passengerId);

		if (roleId.equalsIgnoreCase(UserRoles.PASSENGER_ROLE_ID)) {

			PassengerGeoLocationModel passengerGeoLocationModel = PassengerGeoLocationModel.getPassengerGeoLocationDetailsById(passengerId);

			if (passengerGeoLocationModel != null) {
				outputMap.put("type", "SUCCESS");
				outputMap.put("passengerGeoLocation", passengerGeoLocationModel);
			} else {
				outputMap.put("type", "INTERNAL ERROR");
			}

		} else if (roleId.equalsIgnoreCase(UserRoles.DRIVER_ROLE_ID)) {

			DriverGeoLocationModel driverGeoLocationModel = DriverGeoLocationModel.getCurrentDriverPosition(passengerId);

			if (driverGeoLocationModel != null) {

				PassengerGeoLocationModel passengerGeoLocationModel = new PassengerGeoLocationModel();
				passengerGeoLocationModel.setPassengerId(driverGeoLocationModel.getDriverId());
				passengerGeoLocationModel.setLatitude(driverGeoLocationModel.getLatitude());
				passengerGeoLocationModel.setLongitude(driverGeoLocationModel.getLongitude());

				outputMap.put("type", "SUCCESS");
				outputMap.put("passengerGeoLocation", passengerGeoLocationModel);

			} else {

				outputMap.put("type", "INTERNAL ERROR");
			}
		}

		return sendDataResponse(outputMap);

	}

	@Path("/driver/current-location")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response driverCurrentLocation(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response,
			@QueryParam("driverId") String driverId
			) throws ServletException, IOException {
	//@formatter:on

		Map<String, Object> outputMap = new HashMap<String, Object>();

		DriverGeoLocationModel driverGeoLocationModel = DriverGeoLocationModel.getCurrentDriverPosition(driverId);

		if (driverGeoLocationModel != null) {
			outputMap.put("type", "SUCCESS");
			outputMap.put("driverGeoLocation", driverGeoLocationModel);
		} else {
			outputMap.put("type", "INTERNAL ERROR");
		}

		return sendDataResponse(outputMap);

	}

	@Override
	protected String[] requiredCss() {
		return new String[] { "emergency/site-common.css" };
	}

	@Override
	protected String[] requiredJs() {
		return new String[] { "js/viewjs/emergency/track-my-location.js" };
	}

}