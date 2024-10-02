package com.webapp.actions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.webapp.models.GeoStateModel;

@Path("/state")
public class GeoStateAction extends BusinessAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getMessageList(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("countryId") long countryId
			) throws IOException, SQLException {
	//@formatter:on

		List<GeoStateModel> stateList = GeoStateModel.getAllStatesByCountryId(countryId);

		LinkedHashMap<String, Object> stateListMap = new LinkedHashMap<String, Object>();

		for (GeoStateModel geoStateModel : stateList) {
			stateListMap.put("" + geoStateModel.getStateId(), geoStateModel.getStateName());
		}

		return sendDataResponse(stateListMap);
	}

}