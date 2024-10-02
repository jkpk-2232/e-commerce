package com.webapp.actions.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.UUIDGenerator;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;

@Path("/api/user/notification")
public class UserNotificationAction extends BusinessApiAction {

	@GET
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getUserAllNotification(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@QueryParam("pageNo") int pageNo, 
			@QueryParam("afterTime") long afterTime
			) throws IOException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);
		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		int count = ApnsMessageModel.getTotalNotificationCount(userId, afterTime);

		int recordOffset = 100;
		int startOffSet = 0;

		if (count > 100) {
			startOffSet = count - 100;
		} else {
			startOffSet = 0;
		}

		ApnsMessageModel apnsMessageModel = new ApnsMessageModel();
		apnsMessageModel.setToUserId(userId);
		apnsMessageModel.setOffset(startOffSet);
		apnsMessageModel.setDataLimit(recordOffset);
		apnsMessageModel.setAfterTime(afterTime);

		List<ApnsMessageModel> notificationList = apnsMessageModel.getAllNotificationsByUserId();

		List<Map<String, Object>> notificationMap = new ArrayList<Map<String, Object>>();

		if (notificationList.size() != 0) {

			for (ApnsMessageModel notificationModel : notificationList) {

				Map<String, Object> map = new HashMap<String, Object>();

				map.put("apnsNotificationMessageId", notificationModel.getApnsMessageId());
				map.put("message", notificationModel.getMessage());
				map.put("createdAt", notificationModel.getCreatedAt());
				map.put("extraInfoId", notificationModel.getExtraInfoId());
				map.put("extraInfoType", notificationModel.getExtraInfoType());

				notificationMap.add(map);
			}

			Map<String, Object> outputMap = new HashMap<String, Object>();
			outputMap.put("totalPages", 1);
			outputMap.put("notificationMap", notificationMap);

			ApnsDeviceModel apnsDeviceModel = new ApnsDeviceModel();
			apnsDeviceModel.setLastNotificationsViewedTimeId(UUIDGenerator.generateUUID());
			apnsDeviceModel.setUserId(userId);
			apnsDeviceModel.setCreatedAt(DateUtils.nowAsGmtMillisec());
			apnsDeviceModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

			if (ApnsDeviceModel.checkRecordViewedNotificationTime(userId)) {

				ApnsDeviceModel.deleteNotificationTime(userId);
			}

			apnsDeviceModel.recordViewedNotificationTime();

			return sendDataResponse(outputMap);

		} else {

			return sendBussinessError(messageForKey("errorNoDataAvailableInvoice", request));
		}
	}

	@DELETE
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response deleteUserNotification(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@QueryParam("apnsNotificationMessageId") String apnsNotificationMessageId
			) throws IOException {
	//@formatter:on

		String userId = checkValidSession(sessionKeyHeader);

		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		ApnsMessageModel apnsMessageModel = new ApnsMessageModel();
		apnsMessageModel.setApnsMessageId(apnsNotificationMessageId);
		apnsMessageModel.setUpdatedAt(DateUtils.nowAsGmtMillisec());

		int status = apnsMessageModel.deleteNotificationById();

		if (status > 0) {
			return sendSuccessMessage(messageForKey("successNotificationDeleteSuccessfully", request));
		} else {
			return sendBussinessError(messageForKey("errorNotificationDelete", request));
		}
	}

}