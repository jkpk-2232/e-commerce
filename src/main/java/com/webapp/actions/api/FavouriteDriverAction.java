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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.FavouriteDriverModel;
import com.webapp.models.PassengerTripRatingsModel;

@Path("/api/favourite-driver")
public class FavouriteDriverAction extends BusinessApiAction {

	@Path("/{driverId}")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarFare(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("driverId") String driverId
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		FavouriteDriverModel favouriteDriverModelCheck = FavouriteDriverModel.getFavouriteDriverDetails(loggedInuserId, driverId);

		if (favouriteDriverModelCheck != null) {
			return sendBussinessError(messageForKey("errorFavouriteDriverExits", request));
		}

		FavouriteDriverModel favouriteDriverModel = new FavouriteDriverModel();

		favouriteDriverModel.setPassengerId(loggedInuserId);
		favouriteDriverModel.setDriverId(driverId);

		favouriteDriverModel.addFavouriteDriver(loggedInuserId);

		return sendSuccessMessage(messageForKey("successFavouriteDriver", request));
	}

	@Path("/{driverId}")
	@DELETE
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response deleteFavourite(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("driverId") String driverId
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		FavouriteDriverModel favouriteDriverModelCheck = FavouriteDriverModel.getFavouriteDriverDetails(loggedInuserId, driverId);

		if (favouriteDriverModelCheck == null) {
			return sendBussinessError(messageForKey("errorFavouriteDriverNotExits", request));
		}

		FavouriteDriverModel favouriteDriverModel = new FavouriteDriverModel();

		favouriteDriverModel.setPassengerId(loggedInuserId);
		favouriteDriverModel.setDriverId(driverId);

		favouriteDriverModel.deleteFavouriteDriver(loggedInuserId);

		return sendSuccessMessage(messageForKey("successUnFavouriteDriver", request));
	}

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getFavouroteList(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("start") int start,
			@PathParam("length") int length
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<Map<String, Object>> outputMapMain = new ArrayList<Map<String, Object>>();

		List<FavouriteDriverModel> favouriteDriverList = FavouriteDriverModel.getFavouriteDriverList(loggedInuserId, start, length);

		for (FavouriteDriverModel favouriteDriverModel : favouriteDriverList) {

			Map<String, Object> innerMap = new HashMap<String, Object>();

			DriverInfoModel driverInfoModel = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(favouriteDriverModel.getDriverId());

			innerMap.put("favouriteDriverId", favouriteDriverModel.getFavouriteDriverId());

			if (driverInfoModel != null) {

				innerMap.put("firstName", driverInfoModel.getFirstName());
				innerMap.put("lastName", driverInfoModel.getLastName());
				innerMap.put("driverId", driverInfoModel.getUserId());
				innerMap.put("phoneNo", driverInfoModel.getPhoneNo());
				innerMap.put("phoneNoCode", driverInfoModel.getPhoneNoCode());
				innerMap.put("photoUrl", driverInfoModel.getPhotoUrl());
				innerMap.put("email", driverInfoModel.getEmail());

				if (driverInfoModel.getCarModel() != null) {
					innerMap.put("modelName", driverInfoModel.getCarModel().getModelName());
					innerMap.put("carColor", driverInfoModel.getCarModel().getCarColor());
					innerMap.put("carPlateNo", driverInfoModel.getCarModel().getCarPlateNo());
					innerMap.put("carYear", driverInfoModel.getCarModel().getCarYear());
					innerMap.put("noOfPassenger", driverInfoModel.getCarModel().getNoOfPassenger());
				} else {
					innerMap.put("modelName", "-");
					innerMap.put("carColor", "-");
					innerMap.put("carPlateNo", "-");
					innerMap.put("carYear", "-");
					innerMap.put("noOfPassenger", "-");
				}

			} else {

				innerMap.put("firstName", "-");
				innerMap.put("lastName", "-");
				innerMap.put("driverId", "-");
				innerMap.put("phoneNo", "-");
				innerMap.put("phoneNoCode", "-");
				innerMap.put("photoUrl", "-");
				innerMap.put("email", "-");
				innerMap.put("modelName", "-");
				innerMap.put("carColor", "-");
				innerMap.put("carPlateNo", "-");
				innerMap.put("carYear", "-");
				innerMap.put("noOfPassenger", "-");
			}

			double rating = 0.0;

			List<PassengerTripRatingsModel> ratingList = PassengerTripRatingsModel.getAlldriverRatings(favouriteDriverModel.getDriverId());

			for (PassengerTripRatingsModel paRating : ratingList) {
				rating = rating + paRating.getRate();
			}

			if (rating != 0.0) {
				rating = (double) rating / ratingList.size();
				favouriteDriverModel.setDriverRatings(rating);
			} else {
				favouriteDriverModel.setDriverRatings(-1);
			}

			innerMap.put("rating", rating);

			outputMapMain.add(innerMap);
		}

		return sendDataResponse(outputMapMain);
	}

}