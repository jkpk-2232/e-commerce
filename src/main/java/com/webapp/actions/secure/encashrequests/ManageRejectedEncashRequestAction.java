package com.webapp.actions.secure.encashrequests;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
import com.utils.myhub.DatatableUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.TimeZoneUtils;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.EncashRequestsModel;
import com.webapp.viewutils.NewThemeUiUtils;
import com.webapp.viewutils.NewThemeUiUtils.OUTPUT_BADGE_TYPES;

@Path("/encash-requests/rejected")
public class ManageRejectedEncashRequestAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadRejectedEncashRequests(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_REJECTED_ENCASH_REQUEST_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		return loadView(UrlConstants.JSP_URLS.MANAGE_REJECTED_ENCASH_REQUEST_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response loadRejectedEncashRequestsList(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_REJECTED_ENCASH_REQUEST_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		DatatableUtils dtu = new DatatableUtils(request);

		int total = EncashRequestsModel.getTotalCountOfEncashRequestForSearchByStatus(ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED, dtu.getStartDatelong(), dtu.getEndDatelong());
		List<EncashRequestsModel> encashRequestsModelList = EncashRequestsModel.getEncashRequestForSearchByStatus(dtu.getStartInt(), dtu.getLengthInt(), dtu.getOrder(), dtu.getGlobalSearchStringWithPercentage(), ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED,
					dtu.getStartDatelong(), dtu.getEndDatelong());

		int count = dtu.getStartInt();
		String timeZone = TimeZoneUtils.getTimeZone();

		for (EncashRequestsModel encashRequestsModel : encashRequestsModelList) {

			count++;

			dtuInnerJsonArray = new JSONArray();

			dtuInnerJsonArray.put(encashRequestsModel.getEncashRequestId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(encashRequestsModel.getFirstName());
			dtuInnerJsonArray.put(encashRequestsModel.getEmail());
			dtuInnerJsonArray.put(MyHubUtils.formatPhoneNumber(encashRequestsModel.getPhoneNoCode(), encashRequestsModel.getPhoneNo()));
			dtuInnerJsonArray.put(StringUtils.valueOf(encashRequestsModel.getRequestedAmount()));
			dtuInnerJsonArray.put(DateUtils.dbTimeStampToSesionDate(encashRequestsModel.getRejectedDate(), timeZone, DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS));

			if (StringUtils.validString(encashRequestsModel.getRemark())) {
				dtuInnerJsonArray.put(encashRequestsModel.getRemark());
			} else {
				dtuInnerJsonArray.put(ProjectConstants.NOT_AVAILABLE);
			}

			dtuInnerJsonArray.put(NewThemeUiUtils.outputBadge(OUTPUT_BADGE_TYPES.DEACTIVE, encashRequestsModel.getStatus()));

			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = EncashRequestsModel.getFilteredCountOfEncashRequestForSearchByStatus(ProjectConstants.ENCASH_REQUEST_STATUS_REJECTED, dtu.getGlobalSearchStringWithPercentage(), dtu.getStartDatelong(), dtu.getEndDatelong());
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_REJECTED_ENCASH_REQUEST_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}