package com.webapp.actions.secure.passenger;

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

import org.json.JSONArray;

import com.jeeutils.StringUtils;
import com.utils.myhub.DatatableUtils;
import com.webapp.FieldConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.models.DriverSubscriberModel;

@Path("/manage-driver-subscribers")
public class ManageDriverSubcribersAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getDriverSubscribers(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response,
		@QueryParam(FieldConstants.SUBSCRIBER_USER_ID) String subscriberUserId
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_LOGISTICS_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.SUBSCRIBER_USER_ID, subscriberUserId);

		return loadView(UrlConstants.JSP_URLS.MANAGE_DRIVER_SUBSCRIBERS_JSP);
	}

	@Path("/list")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverSubscribersList(
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
		String subscriberUserId = dtu.getRequestParameter(FieldConstants.SUBSCRIBER_USER_ID);

		int total = DriverSubscriberModel.getDriverSubscribedBySubsciberIdCount(dtu.getStartDatelong(), dtu.getEndDatelong(), subscriberUserId);
		List<DriverSubscriberModel> driverSubscriberList = DriverSubscriberModel.getDriverSubscribedBySubsciberIdSearch(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), dtu.getStartInt(), dtu.getLengthInt(), subscriberUserId);

		int count = dtu.getStartInt();

		for (DriverSubscriberModel driverSubscriberModel : driverSubscriberList) {

			count++;

			dtuInnerJsonArray = new JSONArray();
			dtuInnerJsonArray.put(driverSubscriberModel.getDriverSubscriberId());
			dtuInnerJsonArray.put(count);
			dtuInnerJsonArray.put(driverSubscriberModel.getDriverName());
			dtuInnerJsonArray.put(driverSubscriberModel.getSubscriberName());
			dtuInnerJsonArray.put(driverSubscriberModel.getPriorityNumber());
			dtuOuterJsonArray.put(dtuInnerJsonArray);
		}

		int filterCount = 0;

		if (StringUtils.validString(dtu.getGlobalSearchString())) {
			filterCount = DriverSubscriberModel.getDriverSubscribedBySubsciberIdSearchCount(dtu.getStartDatelong(), dtu.getEndDatelong(), dtu.getGlobalSearchStringWithPercentage(), subscriberUserId);
		} else {
			filterCount = total;
		}

		DatatableUtils.processData(dtuJsonObject, dtuOuterJsonArray, total, filterCount);

		return sendDataResponse(dtuJsonObject.toString());
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_DRIVER_SUBSCRIBERS_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}