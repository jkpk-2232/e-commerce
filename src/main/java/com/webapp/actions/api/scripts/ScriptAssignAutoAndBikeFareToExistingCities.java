package com.webapp.actions.api.scripts;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.webapp.actions.BusinessApiAction;

@Path("/api/script-assign-non-existing-carfaretypes-to-existing-cities")
public class ScriptAssignAutoAndBikeFareToExistingCities extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarFare(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException {
	//@formatter:on

		/**
		 * 1. For the all the car types, check whether the mapping exits for that
		 * region, if not then insert default fare.
		 **/

		MultiTenantUtils.assignNonExistingCarTypesDefaultFareToAllCities("1");

		return sendSuccessMessage("All car types fares adjusted for all regsions");
	}
}