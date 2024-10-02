package com.webapp.actions.secure.vendor.feeds;

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

@Path("/manage-add-newbrands")
public class ManageAddNewBrands extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getProducts(@Context HttpServletRequest request, @Context HttpServletResponse response) throws ServletException, IOException {

		// Preprocess request for the new theme (if necessary)
		preprocessRequestNewTheme(request, response);
		System.out.println("*** rest ****");
		// Directly load the desired JSP page
		return loadView(UrlConstants.JSP_URLS.MANAGE_ADD_NEW_BRANDS_JSP);
	}

	

	@Override
	protected String[] requiredJs() {
		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/vendor-feeds/manage-add-newbrands.js");
		requiredJS.add("new-ui/js/moment-with-locales.min.js");
		requiredJS.add("new-ui/js/bootstrap-material-datetimepicker.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}

}