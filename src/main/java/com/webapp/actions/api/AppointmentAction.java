package com.webapp.actions.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.myhub.AppointmentUtils;
import com.utils.myhub.MultiTenantUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AppointmentModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorStoreModel;

@Path("/api/appointments")
public class AppointmentAction extends BusinessApiAction {

	@Path("/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getAppointments(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("start") int start,
		@PathParam("length") int length,
		@QueryParam("appointmentShortId") int appointmentShortId
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, List<AppointmentModel>> finalList = new HashMap<>();

		List<String> statusNotToBeConsidered = Arrays.asList(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW_PAYMENT_PENDING);

		List<AppointmentModel> appointmentList = AppointmentModel.getAppointmentsByUserId(loggedInUserId, start, length, appointmentShortId, statusNotToBeConsidered);
		finalList.put("appointmentList", appointmentList);

		return sendDataResponse(finalList);
	}

	@Path("/estimate")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getApppointmentsEstimate(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		AppointmentModel appointmentModel
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> estimateFareMap = AppointmentUtils.getAppointmentsEstimatedFareAndPlaceAppointment(appointmentModel, true, loggedInUserId);

		if (estimateFareMap.containsKey(ProjectConstants.STATUS_TYPE)) {

			if (estimateFareMap.containsKey("productInventoryCountErrorMap")) {

				estimateFareMap.put("errorCode", 400);
				estimateFareMap.put("errorType", "OutOfStock");
				estimateFareMap.put("message", messageForKey(estimateFareMap.get(ProjectConstants.STATUS_MESSAGE_KEY).toString(), request));
				return sendDataResponse(estimateFareMap);
			}

			return sendBussinessError(messageForKey(estimateFareMap.get(ProjectConstants.STATUS_MESSAGE_KEY).toString(), request));
		}

		return sendDataResponse(estimateFareMap);
	}

	@Path("/place-appointment")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response placeAppointment(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		AppointmentModel appointmentModel
		) throws SQLException, IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> estimateFareMap = AppointmentUtils.getAppointmentsEstimatedFareAndPlaceAppointment(appointmentModel, false, loggedInUserId);

		if (estimateFareMap.containsKey(ProjectConstants.STATUS_TYPE)) {

			if (estimateFareMap.containsKey("productInventoryCountErrorMap")) {

				estimateFareMap.put("errorCode", 400);
				estimateFareMap.put("errorType", "OutOfStock");
				estimateFareMap.put("message", messageForKey(estimateFareMap.get(ProjectConstants.STATUS_MESSAGE_KEY).toString(), request));
				return sendDataResponse(estimateFareMap);
			}

			if (estimateFareMap.containsKey("errorCode")) {
				return sendBussinessError(estimateFareMap.get(ProjectConstants.STATUS_MESSAGE_KEY).toString());
			} else {
				return sendBussinessError(messageForKey(estimateFareMap.get(ProjectConstants.STATUS_MESSAGE_KEY).toString(), request));
			}
		}

		if (appointmentModel.getPaymentMode().equalsIgnoreCase(ProjectConstants.ONLINE_ID)) {

			AppointmentModel outPutAppointmentModel = AppointmentModel.getAppointmentDetailsByAppointmentId(estimateFareMap.get("appointmentId").toString());
			return sendDataResponse(outPutAppointmentModel);

		} else {
			return sendSuccessMessage(String.format(messageForKey("successAppointmentPlaced", request), estimateFareMap.containsKey("appointmentShortId") ? estimateFareMap.get("appointmentShortId") : ProjectConstants.NOT_AVAILABLE));
		}
	}

	@Path("/cancel/{appointmentId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response cancelOrder(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("appointmentId") String appointmentId
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> cancelAppointmentMap = AppointmentUtils.cancelAppointment(loggedInUserId, appointmentId);

		if (cancelAppointmentMap.get(ProjectConstants.STATUS_TYPE).toString().equalsIgnoreCase(ProjectConstants.STATUS_ERROR)) {
			return sendBussinessError(cancelAppointmentMap.get(ProjectConstants.STATUS_MESSAGE).toString());
		} else {
			return sendSuccessMessage(cancelAppointmentMap.get(ProjectConstants.STATUS_MESSAGE).toString());
		}
	}

	@Path("/appointments-status")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response orderStatusUpdateByVendor(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		AppointmentModel inputAppointmentModel
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInUserId);

		Map<String, Object> output = new HashMap<>();

		if (loggedInUserModel.getRoleId().equalsIgnoreCase(UserRoles.VENDOR_ROLE_ID)) {
			output = AppointmentUtils.updateAppointmentStatusByVendorOrAdminViaAdminPanelOrApi(inputAppointmentModel, loggedInUserId, false);
		} else {
			return sendBussinessError(messageForKey("errorNotAuthorized", request));
		}

		if (output.get(ProjectConstants.STATUS_TYPE).toString().equalsIgnoreCase(ProjectConstants.STATUS_ERROR)) {
			return sendBussinessError(output.get(ProjectConstants.STATUS_MESSAGE).toString());
		} else {
			return sendSuccessMessage(output.get(ProjectConstants.STATUS_MESSAGE).toString());
		}
	}

	@Path("/{appointmentId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response appointmentDetails(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader,
		@PathParam("appointmentId") String appointmentId
		) throws SQLException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);
		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		Map<String, Object> output = new HashMap<>();

		AppointmentModel appointmentModel = AppointmentModel.getAppointmentDetailsByAppointmentIdWithOrderItems(appointmentId);
		VendorStoreModel vendorStoreModel = VendorStoreModel.getVendorStoreDetailsById(appointmentModel.getAppointmentVendorStoreId());

		output.put("appointmentModel", appointmentModel);
		output.put("customerName", appointmentModel.getCustomerName());
		output.put("customerPhoneNo", appointmentModel.getCustomerPhoneNo());
		output.put("customerPhoneNoCode", appointmentModel.getCustomerPhoneNoCode());
		output.put("vendorStoreModel", vendorStoreModel);

		return sendDataResponse(output);
	}
}