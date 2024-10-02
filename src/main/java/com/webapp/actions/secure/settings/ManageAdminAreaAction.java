package com.webapp.actions.secure.settings;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jeeutils.validator.DuplicateAreaNameValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.LoginUtils;
import com.utils.myhub.DropDownUtils;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.AdminAreaModel;
import com.webapp.models.UrlAccessesModel;

@Path("/manage-admin-area")
public class ManageAdminAreaAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadSettings(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("searchString") String searchString
			) throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		data.put("searchBooking", searchString);

		String radiusOptions = DropDownUtils.getAdminAreaRadius(ProjectConstants.DEFAULT_OPERATING_RADIUS);

		data.put("radiusOptions", radiusOptions);

		data.put("labelLocations", messageForKeyAdmin("labelLocations"));
		data.put("labelDisplayName", messageForKeyAdmin("labelDisplayName"));
		data.put("labelAreaName", messageForKeyAdmin("labelAreaName"));
		data.put("labelRadius", messageForKeyAdmin("labelRadius"));
		data.put("labelAddArea", messageForKeyAdmin("labelAddArea"));
		data.put("InfoThisInformationIsAvailableOnWebsite", messageForKeyAdmin("InfoThisInformationIsAvailableOnWebsite"));
		data.put("labelAreaCountry", messageForKeyAdmin("labelAreaCountry"));
		data.put("labelAreaRadius", messageForKeyAdmin("labelAreaRadius"));
		data.put("labelAction", messageForKeyAdmin("labelAction"));
		data.put("labelCancel", messageForKeyAdmin("labelCancel"));
		data.put("labelSave", messageForKeyAdmin("labelSave"));

		data.put("labelSearch", messageForKeyAdmin("labelSearch"));
		data.put("labelSrNo", messageForKeyAdmin("labelSrNo"));

		data.put("labelCarFare", messageForKeyAdmin("labelCarFare"));
		data.put("labelPayablePercentage", messageForKeyAdmin("labelPayablePercentage"));
		data.put("labelCancellationCharges", messageForKeyAdmin("labelCancellationCharges"));
		data.put("labelNearbyDrivers", messageForKeyAdmin("labelNearbyDrivers"));
		data.put("labelSMSSettings", messageForKeyAdmin("labelSMSSettings"));
		data.put("labelAboutUs", messageForKeyAdmin("labelAboutUs"));
		data.put("labelPrivacyPolicy", messageForKeyAdmin("labelPrivacyPolicy"));
		data.put("labelTermsAndConditions", messageForKeyAdmin("labelTermsAndConditions"));
		data.put("labelLocations", messageForKeyAdmin("labelLocations"));
		data.put("labelContactUs", messageForKeyAdmin("labelContactUs"));
		data.put("labelAdminFAQ", messageForKeyAdmin("labelAdminFAQ"));

		return loadView("/secure/settings/manage-admin-area.jsp");
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response postSettings(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@FormParam("areaDisplayName") String areaDisplayName,
			@FormParam("areaName") String areaName,
			@FormParam("areaPlaceId") String areaPlaceId,
			@FormParam("areaLatitude") String areaLatitude,
			@FormParam("areaLongitude") String areaLongitude,
			@FormParam("areaRadius") String areaRadius,
			@FormParam("areaCountry") String areaCountry
			) throws ServletException, IOException, NumberFormatException, SQLException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		if (!UrlAccessesModel.hasUserUrlAccess(UrlConstants.PAGE_URLS.ADMIN_BOOKING_URL, loggedInUserId)) {
			return loadView("/error.jsp");
		}

		AdminAreaModel adminAreaModel = new AdminAreaModel();

		adminAreaModel.setAreaDisplayName(areaDisplayName.trim());
		adminAreaModel.setAreaName(areaName.trim());
		adminAreaModel.setAreaPlaceId(areaPlaceId.trim());
		adminAreaModel.setAreaLatitude(areaLatitude.trim());
		adminAreaModel.setAreaLongitude(areaLongitude.trim());
		adminAreaModel.setAreaRadius(Long.parseLong(areaRadius));
		adminAreaModel.setAreaCountry(areaCountry.trim());

		adminAreaModel.addAdminArea(loggedInUserId);

		return redirectToPage("/manage-admin-area.do");
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getannouncementDetailsList(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res
			) {
	//@formatter:on

		String sStart = req.getParameter("iDisplayStart");
		String length = req.getParameter("iDisplayLength");
		String order = req.getParameter("sSortDir_0");
		String globalSearchString = req.getParameter("more_data");

		List<AdminAreaModel> adminAreaModelList = null;

		int total = AdminAreaModel.getAdminAreaCount();

		int lengthh = Integer.parseInt(length);
		int start = Integer.parseInt(sStart);

		globalSearchString = globalSearchString.trim();

		adminAreaModelList = AdminAreaModel.getAdminAreaListForSearch(start, lengthh, order, "%" + globalSearchString + "%");

		JSONObject jsonObject = new JSONObject();
		JSONArray outerJsonArray = new JSONArray();
		JSONArray innerJsonArray = null;

		int count = start;

		for (AdminAreaModel adminArea : adminAreaModelList) {

			count++;
			innerJsonArray = new JSONArray();

			innerJsonArray.put(adminArea.getAdminAreaId());
			innerJsonArray.put(count);
			innerJsonArray.put(adminArea.getAreaDisplayName());
			innerJsonArray.put(adminArea.getAreaName());
			innerJsonArray.put(adminArea.getAreaCountry());
			innerJsonArray.put(adminArea.getAreaRadius());
			innerJsonArray.put("<div class='activeDeactive' style='width:22px;' id='" + adminArea.getAdminAreaId() + "'> <img src=" + req.getContextPath() + "/assets/image/delete_icon.png" + "></div>");

			outerJsonArray.put(innerJsonArray);
		}

		int filterCount = 0;

		if (globalSearchString != null && !globalSearchString.equals("")) {

			filterCount = AdminAreaModel.getTotalAdminAreaCountBySearch("%" + globalSearchString + "%");

		} else {

			filterCount = total;
		}

		try {

			jsonObject.put("iTotalRecords", total);
			jsonObject.put("iTotalDisplayRecords", filterCount);
			jsonObject.put("aaData", outerJsonArray);

		} catch (JSONException e) {

			e.printStackTrace();
		}

		return sendDataResponse(jsonObject.toString());
	}

	@Path("/delete-area")
	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response deleteArea(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("adminAreaId") String adminAreaId
			) throws ServletException, IOException {
	//@formatter:on

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		AdminAreaModel adminAreaModel = new AdminAreaModel();

		adminAreaModel.setAdminAreaId(adminAreaId);

		adminAreaModel.deleteAdminArea(loggedInUserId);

		return redirectToPage("/manage-admin-area.do");
	}

	public boolean hasErrors() {

		boolean hasErrors = false;
		Validator validator = new Validator();

		validator.addValidationMapping("areaPlaceId", "Area name", new DuplicateAreaNameValidationRule());

		Map<String, String> resultsForValidation = validator.validate(data);

		if (resultsForValidation.size() != 0) {
			data.putAll(resultsForValidation);
			hasErrors = true;
		}
		return hasErrors;
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs//settings/manage-admin-area.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}