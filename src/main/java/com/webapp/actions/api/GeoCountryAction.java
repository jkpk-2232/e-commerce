package com.webapp.actions.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.webapp.actions.BusinessApiAction;
import com.webapp.models.GeoCountryModel;

@Path("/api/country")
public class GeoCountryAction extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAllCountries(
			@HeaderParam("x-api-key") String sessionKeyHeader
			) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<GeoCountryModel> countryList = new ArrayList<GeoCountryModel>();

		countryList = GeoCountryModel.getAllCountries();

		if (countryList != null) {
			return sendDataResponse(countryList);
		} else {
			return sendUnexpectedError();
		}
	}

}