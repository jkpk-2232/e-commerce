package com.webapp.actions.secure.ridelater;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

import org.json.JSONArray;

import com.jeeutils.StringUtils;
import com.utils.LoginUtils;
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.UserRoleUtils;
import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.TourModel;
import com.webapp.viewutils.NewThemeUiUtils;

@Path("/manage-take-ride")
public class ManageTakeRideAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getManageTakeRides(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}
		
		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.SUCCESS_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_LOGISTICS);

		return loadView(UrlConstants.JSP_URLS.MANAGE_TAKE_RIDE_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getManageTakeRidesList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		List<String> assignedRegionList = MultiCityAction.getAssignedRegionList(loginSessionMap.get(LoginUtils.ROLE), loginSessionMap.get(LoginUtils.USER_ID));

		int total = 0;
		List<TourModel> tourList = new ArrayList<TourModel>();
		boolean isTakeRide = true;
		// Show tours only with Take Ride flag as true

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {
			// vendor is the logged in user. show all the tours that are marked as take
			// ride. hence pass the loggedInUserId as null

			List<String> statusList = new ArrayList<String>();
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);

			total = TourModel.getVendorRideLaterTourListCount(dtu.getStartDatelong(), dtu.getEndDatelong(), statusList, null, assignedRegionList, isTakeRide, null);
			tourList = TourModel.getVendorRideLaterTourListBySearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), statusList, null, assignedRegionList, isTakeRide, null);
		} else {
			// admin is the logged in user
			total = TourModel.getRideLaterTourListCount(dtu.getStartDatelong(), dtu.getEndDatelong(), isTakeRide, null);
			tourList = TourModel.getRideLaterTourListBySearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong(), isTakeRide, null);
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

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewDetails"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.ADMIN_BOOKING_DETAILS_URL + "?tourId=" + tourModel.getTourId() + "&tourType=rideLater")));

			//@formatter:off
			if (((tourModel.getPassengerVendorId().equalsIgnoreCase(loginSessionMap.get(LoginUtils.USER_ID)))
				|| (UserRoleUtils.isSuperAdminAndAdminRole(loginSessionMap.get(LoginUtils.ROLE_ID)))) 
				&& tourModel.getDriverId().equalsIgnoreCase(ProjectConstants.DEFAULT_DRIVER_ID)) {
			//@formatter:on

				// if tour passenger vendor id is the loggedInUserId or admin user AND driver is
				// not yet assigned, the tour can be marked as untake tour

				btnGroupStr.append(NewThemeUiUtils.outputDeleteFormButtonLink(null, null, UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.TAKE_RIDE_URL + "?type=untakeride&tourId=" + tourModel.getTourId()), "untake-ride"));
			}

			dtuInnerJsonArray.put(btnGroupStr);

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? tourList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/take-ride")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response takeRide(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.TOUR_ID) String tourId,
		@QueryParam(FieldConstants.TYPE) String type
		) throws IOException, SQLException, ServletException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);
		if (type.equalsIgnoreCase("takeride")) {
			tourModel.setTakeRide(true);
			tourModel.setTourTakeRide(true);
		} else {
			tourModel.setTakeRide(false);
			tourModel.setTourTakeRide(false);
		}

		tourModel.setMarkedTakeRideUserId(loginSessionMap.get(LoginUtils.USER_ID));
		tourModel.updateTourAsTakeRide();

		if (type.equalsIgnoreCase("takeride")) {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS);
		} else {
			return redirectToPage(UrlConstants.PAGE_URLS.MANAGE_BOOK_LATER_URL);
		}
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_TAKE_RIDE_JS, UrlConstants.JS_URLS.RIDE_LATER_DRIVER_ASSIGNMENT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}