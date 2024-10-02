package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.webapp.actions.BusinessApiAction;
import com.webapp.models.GeoCityModel;

@Path("/api/city")
public class GeoCityAction extends BusinessApiAction {

	@Path("/{stateId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCityByStateId(
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@PathParam("stateId") long stateId
			) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<GeoCityModel> cityLists = GeoCityModel.getAllCitesByStateId(stateId);

		if (cityLists != null) {
			return sendDataResponse(cityLists);
		} else {
			return sendUnexpectedError();
		}
	}

}