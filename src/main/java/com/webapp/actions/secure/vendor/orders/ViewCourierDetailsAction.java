package com.webapp.actions.secure.vendor.orders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

import com.jeeutils.DateUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.TourModel;

@Path("/view-courier-details")
public class ViewCourierDetailsAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response viewCourierDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.TOUR_ID) String tourId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_BOOK_LATER_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);
		if (tourModel == null) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		String timeZone = TimeZoneUtils.getTimeZone();

		data.put("tourId", tourModel.getTourId());
		data.put("userTourId", tourModel.getUserTourId());
		data.put("createdAt", DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
		data.put("rideLaterPickupTime", DateUtils.dbTimeStampToSesionDate(tourModel.getRideLaterPickupTime(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));
		data.put("customerName", MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
		data.put("customerPhoneNo", MyHubUtils.formatPhoneNumber(tourModel.getpPhoneCode(), tourModel.getpPhone()));
		data.put("promoCode", tourModel.getPromoCode() != null ? tourModel.getPromoCode() : ProjectConstants.NOT_AVAILABLE);
		data.put("sourceAddress", MyHubUtils.getTrimmedTo(tourModel.getSourceAddress()));
		data.put("destinationAddress", MyHubUtils.getTrimmedTo(tourModel.getDestinationAddress()));

		data.put("sLatitude", tourModel.getsLatitude());
		data.put("sLongitude", tourModel.getsLongitude());
		data.put("dLatitude", tourModel.getdLatitude());
		data.put("dLongitude", tourModel.getdLongitude());

		data.put("courierPickupAddress", tourModel.getCourierPickupAddress());
		data.put("courierContactPersonName", tourModel.getCourierContactPersonName());
		data.put("courierContactPhoneNo", tourModel.getCourierContactPhoneNo());
		data.put("courierDropAddress", tourModel.getCourierDropAddress());
		data.put("courierDropContactPersonName", tourModel.getCourierDropContactPersonName());
		data.put("courierDropContactPhoneNo", tourModel.getCourierDropContactPhoneNo());
		data.put("courierDetails", tourModel.getCourierDetails());

		data.put("total", BusinessApiAction.df.format(tourModel.getTotal()));
		data.put("charges", BusinessApiAction.df.format(tourModel.getCharges()));

		String driverId = tourModel.getDriverId();
		if (driverId != null && !ProjectConstants.DEFAULT_DRIVER_ID.equalsIgnoreCase(driverId)) {
			DriverInfoModel driverInfo = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(driverId);
			data.put("driverName", driverInfo.getFullName());
			data.put("driverPhone", MyHubUtils.formatFullName(driverInfo.getPhoneNoCode(), driverInfo.getPhoneNo()));
		} else {
			data.put("driverName", ProjectConstants.NOT_AVAILABLE);
			data.put("driverPhone", ProjectConstants.NOT_AVAILABLE);
		}

		String tourStatus = "";

		if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER)) {
			tourStatus += messageForKeyAdmin("labelCancelledByDriver");
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {
			tourStatus += messageForKeyAdmin("labelCancelledByPassenger");
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {
			tourStatus += messageForKeyAdmin("labelPassengerUnavailable");
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {
			tourStatus += messageForKeyAdmin("labelDriverArrivedAndWaiting");
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.STARTED_TOUR)) {
			tourStatus += messageForKeyAdmin("labelTourStarted");
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ENDED_TOUR)) {
			tourStatus += messageForKeyAdmin("labelTourEnded");
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {
			tourStatus += messageForKeyAdmin("labelTourAssigned");
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.EXPIRED_TOUR)) {
			tourStatus += messageForKeyAdmin("labelTourExpired");
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.NEW_TOUR)) {
			tourStatus += messageForKeyAdmin("labelNewTour");
		} else if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)) {
			tourStatus += messageForKeyAdmin("labelTourAccepted");
		} else if (ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR.equalsIgnoreCase(tourModel.getStatus())) {
			tourStatus = messageForKeyAdmin("labelRLNew");
		} else if (ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR.equalsIgnoreCase(tourModel.getStatus())) {
			tourStatus = messageForKeyAdmin("labelRLAssigned");
		} else if (ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR.equalsIgnoreCase(tourModel.getStatus())) {
			tourStatus = messageForKeyAdmin("labelRLReassigned");
		} else if (ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST.equalsIgnoreCase(tourModel.getStatus())) {
			tourStatus = messageForKeyAdmin("labelRLAccepted");
		} else if (ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING_REQUEST.equalsIgnoreCase(tourModel.getStatus())) {
			tourStatus = messageForKeyAdmin("labelRLPending");
		} else {
			tourStatus = tourModel.getStatus();
		}

		data.put("tourStatus", tourStatus);

		return loadView(UrlConstants.JSP_URLS.VIEW_COURIER_DETAILS_JSP);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.VIEW_COURIER_DETAILS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}