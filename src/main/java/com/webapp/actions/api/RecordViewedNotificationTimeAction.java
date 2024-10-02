package com.webapp.actions.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.ApnsDeviceModel;

@Path("api/notificationtimerecord")
public class RecordViewedNotificationTimeAction extends BusinessApiAction {

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response recordNotificationTime(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader
			) throws IOException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);

		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		ApnsDeviceModel apnsDeviceModel = new ApnsDeviceModel();
		apnsDeviceModel.setLastNotificationsViewedTimeId(UUIDGenerator.generateUUID());
		apnsDeviceModel.setUserId(userId);
		apnsDeviceModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
		apnsDeviceModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		if (ApnsDeviceModel.checkRecordViewedNotificationTime(userId)) {

			ApnsDeviceModel.deleteNotificationTime(userId);
		}

		boolean insertStatus = apnsDeviceModel.recordViewedNotificationTime();

		if (insertStatus) {
			return sendSuccessMessage(messageForKey("successNotificationTimeRecorded", request));
		} else {
			return sendBussinessError(messageForKey("errorNotificationTimeRecorded", request));
		}
	}

}