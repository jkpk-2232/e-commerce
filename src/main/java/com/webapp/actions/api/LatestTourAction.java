package com.webapp.actions.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.TourModel;

@Path("/api/latest-tours")
public class LatestTourAction extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response getTourId(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-api-key") String sessionKeyHeader) {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		TourModel tourModel = TourModel.getCurrentEndedTourByPassangerId(loggedInuserId, ProjectConstants.TourStatusConstants.ENDED_TOUR);

		if (tourModel != null) {

			String tourId = tourModel.getTourId();

			Map<String, Object> outPutMap = new HashMap<String, Object>();

			outPutMap.put("tourId", tourId);

			return sendDataResponse(outPutMap);
		} else {
			return sendBussinessError(messageForKey("errorNoTourFound", request));
		}
	}

}