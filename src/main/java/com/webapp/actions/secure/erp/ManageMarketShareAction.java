package com.webapp.actions.secure.erp;

import java.io.IOException;
import java.util.ArrayList;
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

import com.webapp.UrlConstants;
import com.webapp.actions.BusinessAction;

@Path("/manage-market-Shares")
public class ManageMarketShareAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response get(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {

		// Preprocess request for the new theme (if necessary)
		preprocessRequestNewTheme(request, response);
		System.out.println("*** rest ****");
		// Load the desired JSP page
		return loadView(UrlConstants.JSP_URLS.MANAGE_MARKET_SHARES_JSP);
	}

	
	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = new ArrayList<>();

		requiredJS.add("js/viewjs/erp-users/manage-market-share.js");
		requiredJS.add("new-ui/js/moment-with-locales.min.js");
		requiredJS.add("new-ui/js/bootstrap-material-datetimepicker.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}
