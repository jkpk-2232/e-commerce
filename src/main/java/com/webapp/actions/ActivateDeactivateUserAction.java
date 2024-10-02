package com.webapp.actions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.utils.LoginUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.models.CarModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.TourModel;
import com.webapp.models.UserModel;

@Path("/activate-deactivate-user")
public class ActivateDeactivateUserAction extends BusinessAction {

	Logger logger = Logger.getLogger(ActivateDeactivateUserAction.class);

	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getMessageList(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("userId") String userId
			) throws IOException, SQLException {
	//@formatter:on

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		Map<String, String> outputMap = new HashMap<String, String>();

		if (loggedInUserId.equalsIgnoreCase(userId)) {
			outputMap.put("type", "SELF");
			return sendDataResponse(outputMap);
		}

		int status = 0;

		UserModel user = UserModel.getUserActiveDeativeDetailsById(userId);
		UserModel user1 = UserModel.getUserActiveDeativeDetailsById(userId);

		outputMap.put("labelFailToActiavate", messageForKeyAdmin("labelFailToActiavate"));
		outputMap.put("labelFailToDeactivate", messageForKeyAdmin("labelFailToDeactivate"));

		if (user != null && user1 != null) {

			user.setUserId(userId);

			if (user.isActive() && !user.isDeleted()) {

				user.setActive(false);
				user.setDeleted(true);

				outputMap.put("past", "active");
				outputMap.put("labelDeactivatedSucesfully", messageForKeyAdmin("labelDeactivatedSucesfully"));
				outputMap.put("labelCanontDeactivateOwn", messageForKeyAdmin("labelCanontDeactivateOwn"));

			} else {

				user.setActive(true);
				user.setDeleted(false);

				outputMap.put("past", "inactive");
				outputMap.put("labelActivatedSucesfully", messageForKeyAdmin("labelActivatedSucesfully"));
			}

			if (UserRoles.ADMIN_ROLE_ID.equalsIgnoreCase(user.getRoleId())) {

				status = user.activateDeactivateUserByAdmin();

				outputMap.put("userType", "admin");
				outputMap.put("userTypeCommon", messageForKeyAdmin("labelAdmin"));

			}
			if (UserRoles.VENDOR_ROLE_ID.equalsIgnoreCase(user.getRoleId())) {

				status = user.activateDeactivateUserByAdmin();

				outputMap.put("userType", "vendor");
				outputMap.put("userTypeCommon", messageForKeyAdmin("labelVendor"));

			} else if (UserRoles.SUB_VENDOR_ROLE_ID.equalsIgnoreCase(user.getRoleId())) {

				status = user.activateDeactivateUserByAdmin();

				outputMap.put("userType", "sub vendor");
				outputMap.put("userTypeCommon", messageForKeyAdmin("labelSubVendor"));
				
			} else if (UserRoles.ERP_EMPLOYEE_ROLE_ID.equalsIgnoreCase(user.getRoleId())) {

				status = user.activateDeactivateUserByAdmin();

				outputMap.put("userType", "erp employee");
				outputMap.put("userTypeCommon", messageForKeyAdmin("labelERPEmployee"));

			} else if (UserRoles.BUSINESS_OWNER_ROLE_ID.equalsIgnoreCase(user.getRoleId())) {

				status = user.activateDeactivateUserByAdmin();

				outputMap.put("userType", "business owner");
				outputMap.put("userTypeCommon", messageForKeyAdmin("labelBusinessOwner"));

			} else if (UserRoles.BUSINESS_OPERATOR_ROLE_ID.equalsIgnoreCase(user.getRoleId())) {

				status = user.activateDeactivateUserByAdmin();

				outputMap.put("userType", "business operator");
				outputMap.put("userTypeCommon", messageForKeyAdmin("labelBusinessOperator"));

			} else if (UserRoles.PASSENGER_ROLE_ID.equalsIgnoreCase(user.getRoleId())) {

				if (user1.isActive() && !user1.isDeleted()) {

					TourModel tourModel = TourModel.getCurrentTourByPassangerId(userId);

					if (tourModel == null) {

						status = user.activateDeactivateUserByAdmin();
						outputMap.put("flag", "1");
					} else {

						String message = messageForKeyAdmin("labelUserActivateFail");
						outputMap.put("userTypeFailMessage", message);
						outputMap.put("flag", "0");
					}

				} else {

					status = user.activateDeactivateUserByAdmin();
					outputMap.put("flag", "1");
				}

				outputMap.put("userType", "passenger");
				outputMap.put("userTypeCommon", messageForKeyAdmin("labelPassenger"));

			} else if (UserRoles.DRIVER_ROLE_ID.equalsIgnoreCase(user.getRoleId())) {

				if (user1.isActive() && !user1.isDeleted()) {

					TourModel tourModel = TourModel.getAdminCurrentTourByDriverId(userId);

					if (tourModel == null) {

						user.setActive(false);
						user.setDeleted(true);

						status = user.activateDeactivateUserByAdmin();
						outputMap.put("flag", "1");

					} else {

						outputMap.put("userTypeFailMessage", messageForKeyAdmin("labelDriverInTourFail"));
						outputMap.put("flag", "0");
					}

				} else {

					status = user.activateDeactivateUserByAdmin();
					outputMap.put("flag", "1");
				}

				outputMap.put("userType", "driver");
				outputMap.put("userTypeCommon", messageForKeyAdmin("labelDriver"));
			}
		}

		if (status > 0) {
			outputMap.put("type", "SUCCESS");
		} else {
			outputMap.put("type", "FAILED");
		}

		return sendDataResponse(outputMap);
	}

	@Path("/status")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getStatus(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("userId") String userId
			) throws IOException, SQLException {
	//@formatter:on

		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		Map<String, String> outputMap = new HashMap<String, String>();

		if (loggedInUserId.equalsIgnoreCase(userId)) {
			outputMap.put("type", "SELF");
			return sendDataResponse(outputMap);
		}

		UserModel user = UserModel.getUserActiveDeativeDetailsById(userId);
		if (user != null) {

			user.setUserId(userId);

			if (user.isActive() && !user.isDeleted()) {
				outputMap.put("status", "active");
				outputMap.put("type", "SUCCESS");

			} else {
				outputMap.put("status", "inactive");
			}
		}

		return sendDataResponse(outputMap);
	}

	@Path("/approvel-status")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getApprovelStatus(
		@Context HttpServletRequest req, 
		@Context HttpServletResponse res,
		@QueryParam("userId") String userId
		) throws IOException, SQLException {
	//@formatter:on

		Map<String, String> outputMap = new HashMap<String, String>();

		int status = 0;

		UserModel user = UserModel.getUserActiveDeativeDetailsById(userId);

		if (UserRoles.DRIVER_ROLE_ID.equalsIgnoreCase(user.getRoleId())) {

			DriverInfoModel driverInfoModel = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(userId);

			if (user.isApprovelStatus() && !user.isDeleted()) {
				user.setApprovelStatus(false);
				status = user.approvedUserByAdmin();
				outputMap.put("flag", "0");
				outputMap.put("past", "notapprove");
				outputMap.put("labelApprovelRejectedSucesfully", messageForKeyAdmin("labelApprovelRejectedSucesfully"));

				driverInfoModel.setApplicationStatus(ProjectConstants.DRIVER_APPLICATION_REJECTED);
			} else {
				user.setApprovelStatus(true);
				status = user.approvedUserByAdmin();
				outputMap.put("flag", "1");
				outputMap.put("past", "approve");
				outputMap.put("labelApprovedSucesfully", messageForKeyAdmin("labelApprovedSucesfully"));

				driverInfoModel.setApplicationStatus(ProjectConstants.DRIVER_APPLICATION_ACCEPTED);
			}

			driverInfoModel.updateDriverApplicationStatus(userId);

			outputMap.put("userType", "driver");
			outputMap.put("userTypeCommon", messageForKeyAdmin("labelDriver"));
		}

		if (status > 0) {
			outputMap.put("type", "SUCCESS");
		} else {
			outputMap.put("type", "FAILED");
		}

		return sendDataResponse(outputMap);
	}

	@Path("/driver-status")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response approvelStatus(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("userId") String userId
			) throws IOException, SQLException {
	//@formatter:on
		Map<String, Object> userInfo = LoginUtils.getLoggedInActor(req, res);
		String loggedInUserId = userInfo.get("user_id").toString();

		Map<String, String> outputMap = new HashMap<String, String>();

		if (loggedInUserId.equalsIgnoreCase(userId)) {
			outputMap.put("type", "SELF");
			return sendDataResponse(outputMap);
		}

		UserModel user = UserModel.getUserActiveDeativeDetailsById(userId);

		if (user != null) {

			user.setUserId(userId);

			DriverInfoModel driverInfoModel = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(userId);

			String carStatus = ProjectConstants.NOT_ATTATCHED;

			if (driverInfoModel != null && driverInfoModel.getCarModel() != null) {
				carStatus = driverInfoModel.getCarModel().isApprovelStatus() ? ProjectConstants.APPROVED : ProjectConstants.NOT_APPROVED;
			}

			if (driverInfoModel.getCarModel() == null && driverInfoModel.getDriveTransmissionTypeId() != null && !driverInfoModel.getDriveTransmissionTypeId().trim().equals("")) {
				carStatus = "driver";
			}

			outputMap.put("carStatus", carStatus);

			if (user.isApprovelStatus() && !user.isDeleted()) {
				outputMap.put("status", "approve");
				outputMap.put("type", "SUCCESS");
			} else {
				outputMap.put("status", "inactive");
			}
		}

		return sendDataResponse(outputMap);
	}

	@Path("/car-approvel-status")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getCarApprovelStatus(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("carId") String carId
			) throws IOException, SQLException {
	//@formatter:on
		Map<String, String> outputMap = new HashMap<String, String>();

		int status = 0;

		CarModel carModel = CarModel.getCarDetailsByCarId(carId);

		if (!carModel.isApprovelStatus()) {
			carModel.setApprovelStatus(true);
			status = carModel.approvedCarByAdmin();

			outputMap.put("flag", "1");
			outputMap.put("past", "approve");
			outputMap.put("labelApprovedSucesfully", messageForKeyAdmin("labelApprovedSucesfully"));
		}

		outputMap.put("userType", "car");
		outputMap.put("userTypeCommon", messageForKeyAdmin("labelCar"));

		if (status > 0) {
			outputMap.put("type", "SUCCESS");
		} else {
			outputMap.put("type", "FAILED");
		}

		return sendDataResponse(outputMap);
	}

	@Path("/car-status")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response approvelCarStatus(
			@Context HttpServletRequest req, 
			@Context HttpServletResponse res,
			@QueryParam("carId") String carId
			) throws IOException, SQLException {
	//@formatter:on

		Map<String, String> outputMap = new HashMap<String, String>();

		CarModel car = CarModel.getCarActiveDeativeDetailsById(carId);

		if (car != null) {

			car.setCarId(carId);

			if (car.isApprovelStatus()) {
				outputMap.put("status", "approve");
				outputMap.put("type", "SUCCESS");
			} else {
				outputMap.put("status", "inactive");
			}
		}

		return sendDataResponse(outputMap);
	}

}