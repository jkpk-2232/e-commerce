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

import com.webapp.models.GeoCityModel;

@Path("/city")
public class GeoCityAction extends BusinessAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getMessageList(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("stateId") long stateId
			) throws IOException, SQLException {
	//@formatter:on

		List<GeoCityModel> cityLists = GeoCityModel.getAllCitesByStateId(stateId);

		LinkedHashMap<String, String> cityListMap = new LinkedHashMap<String, String>();

		for (GeoCityModel geoCityModel : cityLists) {
			cityListMap.put(geoCityModel.getCityId() + "", geoCityModel.getCityName());
		}

		return sendDataResponse(cityListMap);
	}

}