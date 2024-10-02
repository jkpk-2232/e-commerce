package com.webapp.actions.secure.report;

import java.io.IOException;
import java.util.ArrayList;
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

import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.models.UrlAccessesModel;
import com.webapp.models.UserProfileModel;

@Path("/reports")
public class ReportAction extends BusinessAction {

	private final static String REPORTS_URL = "/reports.do";

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadList(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("searchString") String searchString,
			@QueryParam("businessOwnerId") String businessOwnerId) 
			throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(loggedInUserId);

		if (userProfileModel == null) {
			String url = "/logout.do";
			return redirectToPage(url);
		}

		if (!UrlAccessesModel.hasUserUrlAccess(REPORTS_URL, loggedInUserId)) {
			return loadView("/error.jsp");
		}

		data.put("searchBooking", searchString);

		String businessOwnerOptions = DropDownUtils.getBusinessOwnerList(businessOwnerId, UserRoles.BUSINESS_OWNER_ROLE_ID);
		data.put("businessOwnerOptions", businessOwnerOptions);

		return loadView("/secure/business-operator/manage-business-operator-admin.jsp");

	}

	//	@Path("/list")
	//	@GET
	//	@Produces({ "application/json", "application/xml" })
	//	public Response getUserList(@Context HttpServletRequest request, @Context HttpServletResponse response) {
	//
	//		String sStart = request.getParameter("iDisplayStart");
	//		String length = request.getParameter("iDisplayLength");
	//		String order = request.getParameter("sSortDir_0");
	//		String globalSearchString = request.getParameter("more_data");
	//		String businessOwnerId = request.getParameter("business_owner_id");
	//
	//		if (businessOwnerId.equals("-1")) {
	//			businessOwnerId = null;
	//		}
	//
	//		DbSession session = DbSession.getSession(request, response, false);
	//		String timeZone = session.getAttribute(LoginUtils.TIME_ZONE);
	//		if (timeZone == null) {
	//			timeZone = Action.CLIENT_TIMEZONE;
	//		}
	//
	//		List<UserModel> userModelList = null;
	//
	//		int total = UserModel.getTotalOperatorCountByBusinessOwnerId(businessOwnerId);
	//
	//		int lengthh = Integer.parseInt(length);
	//		int start = Integer.parseInt(sStart);
	//
	//		globalSearchString = globalSearchString.trim();
	//		userModelList = UserModel.getBusinessOpertorListForSearch(businessOwnerId, start, lengthh, order, ProjectConstants.BUSINESS_OPERATOR_ROLE_ID, "%" + globalSearchString + "%");
	//
	//		JSONObject jsonObject = new JSONObject();
	//		JSONArray outerJsonArray = new JSONArray();
	//		JSONArray innerJsonArray = null;
	//
	//		int count = start;
	//
	//		for (UserModel userProfileModel : userModelList) {
	//
	//			count++;
	//
	//			innerJsonArray = new JSONArray();
	//			innerJsonArray.put(userProfileModel.getUserId());
	//			innerJsonArray.put(count);
	//			innerJsonArray.put(userProfileModel.getCompanyName());
	//			innerJsonArray.put(userProfileModel.getFullName());
	//			innerJsonArray.put(userProfileModel.getEmail());
	//			innerJsonArray.put(DateUtils.dbTimeStampToSesionDate(userProfileModel.getCreatedAt(), timeZone, DateUtils.Date_Picker_Format));
	//
	//			if (userProfileModel.isActive()) {
	//
	//				innerJsonArray.put("<div class='activeDeactive' style='width:22px;' id='" + userProfileModel.getUserId() + "'> <img src=" + request.getContextPath() + "/assets/image/active.png" + "></div>");
	//
	//			} else {
	//
	//				innerJsonArray.put("<div class='activeDeactive' style='width:22px;' id='" + userProfileModel.getUserId() + "'> <img src=" + request.getContextPath() + "/assets/image/inactive.png" + "></div>");
	//			}
	//
	//			outerJsonArray.put(innerJsonArray);
	//		}
	//
	//		int filterCount = 0;
	//
	//		if (globalSearchString != null && !globalSearchString.equals("")) {
	//
	//			filterCount = userModelList.size();
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

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/business-operator/manage-business-operator-admin.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}