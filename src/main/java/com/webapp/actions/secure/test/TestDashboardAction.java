package com.webapp.actions.secure.test;

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

import com.utils.LoginUtils;
import com.webapp.actions.BusinessAction;

@Path("/test-dashboard")
public class TestDashboardAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadSettings(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res
		) throws ServletException, IOException {
	//@formatter:on

		preprocessRequestNewTheme(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = redirectToLoginPage(req, res);
			return redirectToPage(url);
		}

//		return loadView("/secure/test-dashboard/test-dashboard.jsp");
		return loadView("/new-theme-login.jsp");
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/test-dashboard/test-dashboard.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}