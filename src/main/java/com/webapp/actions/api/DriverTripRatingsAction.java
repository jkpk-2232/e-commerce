package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DriverTripRatingsModel;

@Path("/api/driver-trip-ratings")
public class DriverTripRatingsAction extends BusinessApiAction {

	private static final String TRIPID = "tripId";
	private static final String TRIPID_LABEL = "Trip Id";

	@POST
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response ratingsByDriver(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			DriverTripRatingsModel driverTripRatingsModel
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		errorMessages = DriverTripRatingsModelValidation(driverTripRatingsModel);

		if (errorMessages.size() != 0) {

			return sendBussinessError(errorMessages);
		}

		if (DriverTripRatingsModel.getRatingDetailsByTourId(driverTripRatingsModel.getTripId()) == null) {

			String passengerId = DriverTripRatingsModel.getPassengerIdFromTours(driverTripRatingsModel.getTripId());

			if (passengerId == null) {

				return sendBussinessError(messageForKey("errorInvalidTrip", request));
			}

			int status = driverTripRatingsModel.ratingsByDriver(loggedInuserId, passengerId);

			if (status > 0) {
				return sendSuccessMessage(messageForKey("successPassengerRatedSucessfully", request));
			} else {
				return sendBussinessError(messageForKey("errorFailedToRatePassenger", request));
			}

		} else {

			return sendSuccessMessage(messageForKey("errorCannotRateAgain", request));
		}
	}

	private List<String> DriverTripRatingsModelValidation(DriverTripRatingsModel driverTripRatingsModel) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(TRIPID, TRIPID_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(driverTripRatingsModel);

		return errorMessages;
	}

}