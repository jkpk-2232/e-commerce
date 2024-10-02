package com.webapp.actions.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.EmergencyNumbersPersonalModel;
import com.webapp.models.TourModel;
import com.webapp.models.TrackLocationTokenModel;
import com.webapp.models.UserModel;

@Path("/api/emergency")
public class EmergencyAction extends BusinessApiAction {

	private static final String CONTACTNUMBERLIST = "contactNumberList";
	private static final String CONTACTNUMBERLIST_LABEL = "Contact Number List";

	@GET
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getEmergencyDetails(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		List<EmergencyNumbersPersonalModel> emergencyNumbersPersonalList = EmergencyNumbersPersonalModel.getEmergencyNumbersPersonalListById(loggedInUserId);
		outPutMap.put("emergencyContact", emergencyNumbersPersonalList);

		return sendDataResponse(outPutMap);
	}

	@Path("/numbers/personal")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response addContactNos(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			EmergencyNumbersPersonalModel emergencyNumbersPersonalModel
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {

			return sendUnauthorizedRequestError();
		}

		errorMessages = emergencyNumbersPersonalModelValidation(emergencyNumbersPersonalModel);

		if (errorMessages.size() != 0) {

			return sendBussinessError(errorMessages);
		}

		emergencyNumbersPersonalModel.addEmergencyNumbersPersonal(loggedInUserId);

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		List<EmergencyNumbersPersonalModel> emergencyNumbersPersonalList = EmergencyNumbersPersonalModel.getEmergencyNumbersPersonalListById(loggedInUserId);
		outPutMap.put("emergencyContact", emergencyNumbersPersonalList);

		return sendDataResponse(outPutMap);
	}

	@Path("/numbers/personal/{emergencyNumberPersonalId}")
	@DELETE
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response deleteContactNo(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@PathParam("emergencyNumberPersonalId") String emergencyNumberPersonalId
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {

			return sendUnauthorizedRequestError();
		}

		EmergencyNumbersPersonalModel.deleteEmergencyNumberPersonal(emergencyNumberPersonalId, loggedInUserId);

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		List<EmergencyNumbersPersonalModel> emergencyNumbersPersonalList = EmergencyNumbersPersonalModel.getEmergencyNumbersPersonalListById(loggedInUserId);
		outPutMap.put("emergencyContact", emergencyNumbersPersonalList);

		return sendDataResponse(outPutMap);
	}

	@Path("/send/message")
	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response sendEmergencyMsg(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@QueryParam("emergencyNumberPersonalId") String emergencyNumberPersonalId
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);

		if (loggedInUserId == null) {

			return sendUnauthorizedRequestError();
		}

		UserModel userProfile = UserModel.getUserAccountDetailsById(loggedInUserId);

		TourModel tourModel = TourModel.getCurrentEndedTourByPassangerId(loggedInUserId, ProjectConstants.TourStatusConstants.STARTED_TOUR);

		if (emergencyNumberPersonalId != null && (!"".equals(emergencyNumberPersonalId))) {

			EmergencyNumbersPersonalModel emergencyNumbersPersonalModel = EmergencyNumbersPersonalModel.getEmergencyNumbersPersonalDetailsById(emergencyNumberPersonalId);

			if (emergencyNumbersPersonalModel != null) {

				TrackLocationTokenModel trackLocationTokenModel = new TrackLocationTokenModel();
				trackLocationTokenModel.setUserId(loggedInUserId);

				if (tourModel != null) {
					if (tourModel.getTourId() != null && (!"".equals(tourModel.getTourId()))) {
						trackLocationTokenModel.setTourId(tourModel.getTourId());
					}
				}

				String token = trackLocationTokenModel.insertTrackLocationDetails();

				String link = WebappPropertyUtils.BASE_PATH + "/track-me/" + token + ".do";

				String message = String.format(messageForKey("emergencySmsText", request), userProfile.getFirstName(), link);

				MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, "" + emergencyNumbersPersonalModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_TRACK_TEMPLATE_ID);
			}

		} else {

			List<EmergencyNumbersPersonalModel> emergencyNumbersPersonalModelList = EmergencyNumbersPersonalModel.getEmergencyNumbersPersonalListById(loggedInUserId);

			if (emergencyNumbersPersonalModelList.size() > 0) {

				TrackLocationTokenModel trackLocationTokenModel = new TrackLocationTokenModel();
				trackLocationTokenModel.setUserId(loggedInUserId);

				if (tourModel != null) {
					if (tourModel.getTourId() != null && (!"".equals(tourModel.getTourId()))) {
						trackLocationTokenModel.setTourId(tourModel.getTourId());
					}
				}

				String token = trackLocationTokenModel.insertTrackLocationDetails();

				String link = WebappPropertyUtils.BASE_PATH + "/track-me/" + token + ".do";

				String message = String.format(messageForKey("emergencySmsText", request), userProfile.getFirstName(), link);

				for (EmergencyNumbersPersonalModel emergencyNumbersPersonalModel : emergencyNumbersPersonalModelList) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message + "  " + link, "" + emergencyNumbersPersonalModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_TRACK_TEMPLATE_ID);
				}

			}
		}

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		List<EmergencyNumbersPersonalModel> emergencyNumbersPersonalList = EmergencyNumbersPersonalModel.getEmergencyNumbersPersonalListById(loggedInUserId);
		outPutMap.put("emergencyContact", emergencyNumbersPersonalList);

		return sendDataResponse(outPutMap);
	}

	private List<String> emergencyNumbersPersonalModelValidation(EmergencyNumbersPersonalModel emergencyNumbersPersonalModel) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(CONTACTNUMBERLIST, CONTACTNUMBERLIST_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(emergencyNumbersPersonalModel);

		return errorMessages;
	}

}