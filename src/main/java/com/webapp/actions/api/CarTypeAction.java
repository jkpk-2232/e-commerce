package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.MultiTenantUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.CarModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.MulticityCityRegionModel;
import com.webapp.models.MulticityCountryModel;

@Path("/api/car-type")
public class CarTypeAction extends BusinessApiAction {

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAllCars(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader
			) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<CarTypeModel> carTypeLists = new ArrayList<CarTypeModel>();

		carTypeLists = CarTypeModel.getActiveDeactaiveCarDetails();

		if (carTypeLists != null) {

			return sendDataResponse(carTypeLists);

		} else {

			return sendUnexpectedError();
		}
	}

	@Path("/vendor")
	@GET
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarTypesByRegionAndVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@QueryParam("latitude") String latitude,
		@QueryParam("longitude") String longitude
		) throws IOException, SQLException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);
		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> output = new HashMap<String, Object>();

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(latitude, longitude);
		if (multicityCityRegionId == null) {
			return sendBussinessError(messageForKey("errorNoServicesInThisCountry", request));
		}

		MulticityCityRegionModel multicityCityRegionModel = MulticityCityRegionModel.getMulticityCityRegionDetailsByCityId(multicityCityRegionId);
		MulticityCountryModel multicityCountryModel = MulticityCountryModel.getMulticityCountryIdDetailsById(multicityCityRegionModel.getMulticityCountryId());

		List<CarTypeModel> carTypeList = MultiTenantUtils.getCarTypeListOfVendor(headerVendorId, multicityCityRegionId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		output.put("multicityCountryId", multicityCountryModel.getMulticityCountryId());
		output.put("multicityCityRegionId", multicityCityRegionId);
		output.put("carTypeList", carTypeList);

		return sendDataResponse(output);
	}

	@Path("/driver")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDriverCar(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader
			) throws SQLException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		CarModel carModel = CarModel.getCarDetailsByDriverId(loggedInuserId);

		if (carModel != null) {

			if ((carModel.getCarTypeId() != null) && (!("".equals(carModel.getCarTypeId()))) && (!("-1".equals(carModel.getCarTypeId())))) {

				outPutMap.put("carTypeId", carModel.getCarTypeId());

			} else {

				outPutMap.put("carTypeId", ProjectConstants.Fifth_Vehicle_ID);
			}

		} else {

			outPutMap.put("carTypeId", ProjectConstants.Fifth_Vehicle_ID);
		}

		return sendDataResponse(outPutMap);
	}

}