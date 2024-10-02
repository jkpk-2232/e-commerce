package com.webapp.actions.secure.users.admin;

import java.io.IOException;
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

import com.jeeutils.FrameworkConstants;
import com.utils.LoginUtils;
import com.webapp.actions.BusinessAction;
import com.webapp.models.UserProfileModel;

@Path("/secure/users/admin/admin-dashboard")
public class AdminDashboardAction extends BusinessAction {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response addForm(@Context HttpServletRequest req, @Context HttpServletResponse res) throws ServletException, IOException {

		preprocessRequest(req, res);

		if (!LoginUtils.checkValidSession(req, res)) {
			String url = "/logout.do";
			return redirectToPage(url);
		}

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		UserProfileModel userProfileModel = UserProfileModel.getUserAccountDetailsById(loggedInUserId);

		if (userProfileModel == null) {
			String url = "/logout.do";
			return redirectToPage(url);
		}

		data.put("contextPath", req.getContextPath());
		data.put("heading", "Dashboard");
		request.setAttribute(FrameworkConstants.LOADED_VIEW, true);
		return loadView("/secure/users/admin/admin-dashboard.jsp");
	}

	@Override
	protected String[] requiredJs() {
		return new String[] { "js/watermark.js", "js/admin-dashboard.js" };
	}

}
