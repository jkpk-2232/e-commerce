package com.webapp.actions.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.webapp.actions.BusinessApiAction;
import com.webapp.models.CreateUserAccountsThread;

@Path("/api/script/user-account")
public class UserAccountScriptAction extends BusinessApiAction {

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response postCreateUserAccounts(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response
			) throws IOException {
	//@formatter:on

		new CreateUserAccountsThread();

		return sendSuccessMessage("Default driver/vendor accounts created successfully.");
	}

}