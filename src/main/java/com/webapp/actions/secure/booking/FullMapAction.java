package com.webapp.actions.secure.booking;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.utils.LoginUtils;
import com.webapp.actions.BusinessAction;
import com.webapp.models.UserProfileModel;

@Path("/dashboard/map")
public class FullMapAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	//@formatter:off
	public Response loadList(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res) 
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

		data.put("labelFullMap", messageForKeyAdmin("labelShowFullMap"));
		data.put("labelClose", messageForKeyAdmin("labelClose"));

		return loadView("/secure/booking/full-map.jsp");
	}

	@Override
	protected String[] requiredCss() {

		return new String[] { "emergency/site-common.css" };
	}

	@Override
	protected String[] requiredJs() {

		List<String> requiredJS = new ArrayList<String>();

		requiredJS.add("js/viewjs/booking/full-map.js");

		return requiredJS.toArray(new String[requiredJS.size()]);
	}
}