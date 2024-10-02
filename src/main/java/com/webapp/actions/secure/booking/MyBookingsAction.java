//package com.webapp.actions.secure.booking;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.jeeutils.DateUtils;
//import com.utils.LoginUtils;
//import com.utils.myhub.TimeZoneUtils;
//import com.webapp.actions.BusinessAction;
//import com.webapp.models.TourModel;
//import com.webapp.models.UrlAccessesModel;
//import com.webapp.models.UserProfileModel;
//import com.webapp.viewutils.NewUiViewUtils;
//
//@Path("/my-bookings")
//public class MyBookingsAction extends BusinessAction {
//
//	private final static String MY_BOOKINGS_URL = "/my-bookings.do";
//
//	@GET
//	@Produces(MediaType.TEXT_HTML)
//	//@formatter:off
//	public Response loadMyBookingsList(
//			@Context HttpServletRequest req, 
//			@Context HttpServletResponse res) 
//			throws ServletException, IOException {
//	//@formatter:on
//		preprocessRequest(req, res);
//
//		if (!LoginUtils.checkValidSession(req, res)) {
//			String url = redirectToLoginPage(req, res);
//			return redirectToPage(url);
//		}
//
//		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
//		String loggedInUserId = userInfo.get("user_id").toString();
//
//		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(loggedInUserId);
//
//		if (userProfileModel == null) {
//			String url = "/logout.do";
//			return redirectToPage(url);
//		}
//
//		if (!UrlAccessesModel.hasUserUrlAccess(MY_BOOKINGS_URL, loggedInUserId)) {
//			return loadView("/error.jsp");
//		}
//
//		data.put("labelSearch", messageForKeyAdmin("labelSearch"));
//		data.put("labelMyBookings", messageForKeyAdmin("labelMyBookings"));
//		data.put("labelBusinessOwnersBookings", messageForKeyAdmin("labelBusinessOwnersBookings"));
//		data.put("labelAll", messageForKeyAdmin("labelAll"));
//		data.put("labelByDate", messageForKeyAdmin("labelByDate"));
//		data.put("labelFromDate", messageForKeyAdmin("labelFromDate"));
//		data.put("labelToDate", messageForKeyAdmin("labelToDate"));
//		data.put("labelExport", messageForKeyAdmin("labelExport"));
//		data.put("labelTripId", messageForKeyAdmin("labelTripId"));
//		data.put("labelSrNo", messageForKeyAdmin("labelSrNo"));
//		data.put("labelTourId", messageForKeyAdmin("labelTourId"));
//		data.put("labelPassengerName", messageForKeyAdmin("labelPassengerName"));
//		data.put("labelPickUpTime", messageForKeyAdmin("labelPickUpTime"));
//		data.put("labelPickUpLocation", messageForKeyAdmin("labelPickUpLocation"));
//		data.put("labelDropLocation", messageForKeyAdmin("labelDropLocation"));
//		data.put("labelStatus", messageForKeyAdmin("labelStatus"));
//
//		data.put("labelStartDateRequiredHidden", messageForKeyAdmin("labelStartDateRequired"));
//		data.put("labelEndDateRequiredHidden", messageForKeyAdmin("labelEndDateRequired"));
//		data.put("labelStartDateshouldnotbegreaterthanToDateHidden", messageForKeyAdmin("labelStartDateshouldnotbegreaterthanToDate"));
//
//		data.put("userId", loggedInUserId);
//
//		return loadView("/secure/my-bookings/manage-my-bookings.jsp");
//	}
//
//	@Path("/list")
//	@GET
//	@Produces({ "application/json", "application/xml" })
//	//@formatter:off
//	public Response getMyBookingsUserList(
//			@Context HttpServletRequest request, 
//			@Context HttpServletResponse res
//			) throws ServletException, IOException {
//	//@formatter:on
//
//		String timeZone = TimeZoneUtils.getTimeZone();
//
//		String sStart = request.getParameter("iDisplayStart");
//		String length = request.getParameter("iDisplayLength");
//		String order = request.getParameter("sSortDir_0");
//		String globalSearchString = request.getParameter("sSearch");
//		String startDate = request.getParameter("startDate");
//		String endDate = request.getParameter("endDate");
//
//		long startDatelong = DateUtils.getStartOfDayDatatable(startDate, timeZone);
//		long endDatelong = DateUtils.getEndOfDayDatatable(endDate, timeZone);
//
//		String userId = "";
//		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(request, res);
//		userId = userInfo.get("user_id").toString();
//
//		int total = TourModel.getBusinessOwnerTotalToursByUserId(userId, startDatelong, endDatelong);
//
//		long userTourId = -1;
//
//		try {
//			userTourId = Long.parseLong(globalSearchString);
//		} catch (Exception e) {
//			userTourId = -1;
//		}
//
//		int lengthh = Integer.parseInt(length);
//		int start = Integer.parseInt(sStart);
//
//		List<TourModel> tourList = null;
//
//		globalSearchString = globalSearchString.trim();
//
//		tourList = TourModel.getBusinessOwnerTourListBySearch(start, lengthh, order, userId, "%" + globalSearchString + "%", userTourId, startDatelong, endDatelong);
//
//		JSONObject jsonObject = new JSONObject();
//		JSONArray outerJsonArray = new JSONArray();
//		JSONArray innerJsonArray = null;
//
//		int count = start;
//
//		for (TourModel tourModel : tourList) {
//
//			count++;
//
//			innerJsonArray = new JSONArray();
//			innerJsonArray.put(tourModel.getTourId());
//			innerJsonArray.put(count);
//			innerJsonArray.put(tourModel.getUserTourId());
//			innerJsonArray.put(tourModel.getpFirstName() + " " + tourModel.getpLastName());
//			innerJsonArray.put(DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), timeZone, DateUtils.DATE_TIME_PICKER_FORMAT_FOR_12_HOURS));
//			innerJsonArray.put(tourModel.getSourceAddress());
//			innerJsonArray.put(tourModel.getDestinationAddress());
//
//			innerJsonArray.put(tourModel.getStatus());
//
//			String btnGroupStr = "";
//			btnGroupStr += NewUiViewUtils.outputFormButtonLink(request.getContextPath() + "/admin-bookings-details.do?tourId=" + tourModel.getTourId() + "&tourType=mybookings", "view");
//
//			innerJsonArray.put(btnGroupStr);
//
//			outerJsonArray.put(innerJsonArray);
//		}
//
//		int filterCount = 0;
//
//		if (globalSearchString != null && !globalSearchString.equals("")) {
//
//			filterCount = tourList.size();
//
//		} else {
//
//			filterCount = total;
//		}
//
//		try {
//
//			jsonObject.put("iTotalRecords", total);
//			jsonObject.put("iTotalDisplayRecords", filterCount);
//			jsonObject.put("aaData", outerJsonArray);
//
//		} catch (JSONException e) {
//
//			e.printStackTrace();
//		}
//
//		return sendDataResponse(jsonObject.toString());
//	}
//
//	@Override
//	protected String[] requiredJs() {
//
//		List<String> requiredJS = new ArrayList<String>();
//
//		requiredJS.add("js/viewjs/my-bookings/manage-my-bookings.js");
//
//		return requiredJS.toArray(new String[requiredJS.size()]);
//	}
//}