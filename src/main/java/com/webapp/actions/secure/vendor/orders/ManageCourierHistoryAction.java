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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.webapp.FieldConstants;
import com.webapp.ProjectConstants;
import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;

@Path("/manage-courier-history")
public class ManageCourierHistoryAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response getCourierHistory(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(request, response);

		if (loginSessionMap == null) {
			return logoutUser();
		}

		if (!loginSessionMap.containsKey(UrlConstants.PAGE_URLS.MANAGE_BOOK_LATER_URL)) {
			return loadView(UrlConstants.JSP_URLS.ERROR_JSP);
		}

		data.put(FieldConstants.SERVICE_TYPE_ID, ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID);

		return loadView(UrlConstants.JSP_URLS.MANAGE_COURIER_HISTORY_JSP);
	}

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = Arrays.asList(UrlConstants.JS_URLS.MANAGE_COURIER_HISTORY_JS, UrlConstants.JS_URLS.RIDE_LATER_DRIVER_ASSIGNMENT_JS);
		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}