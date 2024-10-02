package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.webapp.actions.BusinessApiAction;
import com.webapp.models.GeoStateModel;

@Path("/api/state")
public class GeoStateAction extends BusinessApiAction {

	@Path("/{countryId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getStateByCountryId(
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("countryId") long countryId
			) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<GeoStateModel> stateList = new ArrayList<GeoStateModel>();

		stateList = GeoStateModel.getAllStatesByCountryId(countryId);

		if (stateList != null) {
			return sendDataResponse(stateList);
		} else {
			return sendUnexpectedError();
		}
	}
}