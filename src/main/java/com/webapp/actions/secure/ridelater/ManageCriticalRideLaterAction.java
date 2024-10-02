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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import com.jeeutils.DateUtils;
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

@Path("/manage-critical-ride-later")
public class ManageCriticalRideLaterAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getManageCriticalRideLater(
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

		loggedInUserModelViaSession.setRideLaterVisitedTime(DateUtils.nowAsGmtMillisec());
		loggedInUserModelViaSession.updateRideLaterVisitedTime();

		data.put(FieldConstants.SUCCESS_PAGE_REDIRECT_URL, UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL);

		return loadView(UrlConstants.JSP_URLS.MANAGE_CRITICAL_BOOK_LATER_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getManageCriticalRideLaterList(
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

		int total = 0;
		List<TourModel> tourList = null;

		if (UserRoleUtils.isVendorRole(loginSessionMap.get(LoginUtils.ROLE_ID))) {

			List<String> statusList = new ArrayList<String>();

			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING_REQUEST);
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR);
			statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);

			total = TourModel.getVendorCriticalRideLaterTourListCount(0, dtu.getStartDatelong(), dtu.getEndDatelong(), statusList, loginSessionMap.get(LoginUtils.USER_ID), assignedRegionList);
			tourList = TourModel.getVendorCriticalRideLaterTourListBySearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), 0, dtu.getStartDatelong(), dtu.getEndDatelong(), statusList,
						loginSessionMap.get(LoginUtils.USER_ID), assignedRegionList);

		} else {
			total = TourModel.getCriticalRideLaterTourListCount(0, dtu.getStartDatelong(), dtu.getEndDatelong());
			tourList = TourModel.getCriticalRideLaterTourListBySearch(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), 0, dtu.getStartDatelong(), dtu.getEndDatelong());
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
				dtuInnerJsonArray.put(NewThemeUiUtils.outputButton("btnRideLaterAssign_" + tourModel.getTourId(), messageForKeyAdmin("labelReassign"), FieldConstants.RIDE_LATER_ASSIGN));
			} else if (TourUtils.rideLaterDriverAssignmentCriteriaStatus(tourModel)) {
				dtuInnerJsonArray.put(NewThemeUiUtils.outputButton("btnRideLaterReassign_" + tourModel.getTourId(), messageForKeyAdmin("labelReassign"), FieldConstants.RIDE_LATER_REASSIGN));
			} else {
				dtuInnerJsonArray.put(tourModel.getStatus());
			}

			btnGroupStr.append(NewThemeUiUtils.outputFormButtonLink(NewThemeUiUtils.OUTPUT_BUTTON_TYPES.VIEW, messageForKeyAdmin("labelViewDetails"),
						UrlConstants.getUrl(request, UrlConstants.PAGE_URLS.ADMIN_BOOKING_DETAILS_URL + "?tourId=" + tourModel.getTourId() + "&tourType=criticalRideLater")));

			dtuInnerJsonArray.put(btnGroupStr.toString());

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = StringUtils.validString(dtu.getGlobalSearchString()) ? tourList.size() : total;

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Path("/critical-tour-count")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCriticalTourCount(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		long crticalTourCount = TourModel.getCriticalRideLaterTourListCount(loggedInUserModelViaSession.getRideLaterVisitedTime(), 0, 0);

		Map<String, Object> outputMap = new HashMap<String, Object>();
		outputMap.put("crticalTourCount", crticalTourCount);
		return sendDataResponse(outputMap);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_CRITICAL_BOOK_LATER_JS, UrlConstants.JS_URLS.RIDE_LATER_DRIVER_ASSIGNMENT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}