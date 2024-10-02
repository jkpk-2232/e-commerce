package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.validator.DuplicateFavouriteNicknameValidationRule;
import com.jeeutils.validator.Validator;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.FavouriteLocationsModel;
import com.webapp.models.TourModel;

@Path("/api/favourite-locations")
public class FavouriteLocationsAction extends BusinessApiAction {

	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response addFavouriteLocations(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			FavouriteLocationsModel favouriteLocationsModel
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		favouriteLocationsModel.setUserId(loggedInuserId);

		errorMessages = FavouriteLocationsModelValidtion(favouriteLocationsModel);

		if (errorMessages.size() != 0) {

			return sendBussinessError(errorMessages);
		}

		favouriteLocationsModel.setUserId(loggedInuserId);

		String favouriteLocationsId = favouriteLocationsModel.addFavouriteLocations(loggedInuserId);

		if (favouriteLocationsModel.getTourId() != null) {

			TourModel tour = TourModel.getTourDetailsByTourId(favouriteLocationsModel.getTourId());

			if (favouriteLocationsModel.isSource()) {
				tour.setPickupFavouriteLocationsId(favouriteLocationsId);
			} else {
				tour.setDestinationFavouriteLocationsId(favouriteLocationsId);
			}

			tour.updateFavouriteLocations(loggedInuserId);
		}

		Map<String, Object> output = new HashMap<String, Object>();
		output.put("favouriteLocationsId", favouriteLocationsId);
		output.put("message", messageForKey("successAddFavouriteLocations", request));

		return sendDataResponse(output);
	}

	@PUT
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response editFavouriteLocations(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			FavouriteLocationsModel favouriteLocationsModel
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		favouriteLocationsModel.setUserId(loggedInuserId);

		errorMessages = FavouriteLocationsModelValidtion(favouriteLocationsModel);

		if (errorMessages.size() != 0) {

			return sendBussinessError(errorMessages);
		}

		favouriteLocationsModel.setUserId(loggedInuserId);
		favouriteLocationsModel.updateFavouriteLocations(loggedInuserId);

		return sendSuccessMessage(messageForKey("successFavouriteLocations", request));
	}

	@Path("/locations")
	@PUT
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateFavouriteTour(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			TourModel tourModel
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		tourModel.updateFavouriteLocations(loggedInuserId);

		return sendSuccessMessage(messageForKey("successFavouriteLocations", request));
	}

	@Path("/{favouriteLocationsId}")
	@DELETE
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarFare(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("favouriteLocationsId") String favouriteLocationsId
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		FavouriteLocationsModel favouriteLocationsModel = new FavouriteLocationsModel();

		favouriteLocationsModel.setUserId(loggedInuserId);
		favouriteLocationsModel.setFavouriteLocationsId(favouriteLocationsId);

		favouriteLocationsModel.deletefavouriteLocation(loggedInuserId);

		return sendSuccessMessage(messageForKey("successDeleteFavouriteLocations", request));
	}

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarFare(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			//			@PathParam("locationType") String locationType,
			@PathParam("start") int start,
			@PathParam("length") int length,
			@QueryParam("searchText") String searchText
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<FavouriteLocationsModel> favouriteLocationsModel = new ArrayList<FavouriteLocationsModel>();

		boolean isSource = false;

		favouriteLocationsModel = FavouriteLocationsModel.getFavouriteLocationsList(loggedInuserId, start, length, "%" + searchText + "%", isSource);

		return sendDataResponse(favouriteLocationsModel);
	}

	private List<String> FavouriteLocationsModelValidtion(FavouriteLocationsModel favouriteLocationsModel) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping("favouriteNickname", "Nick name", new DuplicateFavouriteNicknameValidationRule(favouriteLocationsModel.getFavouriteLocationsId(), favouriteLocationsModel.getUserId()));

		errorMessages = validator.validate(favouriteLocationsModel);

		return errorMessages;
	}

}