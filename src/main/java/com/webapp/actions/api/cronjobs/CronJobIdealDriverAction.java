package com.webapp.actions.api.cronjobs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.utils.myhub.notifications.SendDeviceNotifications;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DriverGeoLocationModel;

@Path("/api/ideal-driver")
public class CronJobIdealDriverAction extends BusinessApiAction {

	@Path("/notify/cron-job")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response idealDriverCronJob(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response
			) throws SQLException, IOException {
	//@formatter:on

		long idealTimeInMillis = DateUtils.nowAsGmtMillisec() - ((60 * 1000) * (30));

		List<DriverGeoLocationModel> driverGeoLocationModelList = DriverGeoLocationModel.getIdealDriverListForCronJob(idealTimeInMillis);

		List<String> toUserList = new ArrayList<String>();

		for (DriverGeoLocationModel driverGeoLocationModel : driverGeoLocationModelList) {

			toUserList.add(driverGeoLocationModel.getDriverId());
		}

		if (toUserList.size() > 0) {

			String pushMessage = messageForKey("pushMessageToIdealDrivers", request);
			new SendDeviceNotifications(toUserList, pushMessage, Integer.parseInt(WebappPropertyUtils.getWebAppProperty("push_msg_batch")), WebappPropertyUtils.getWebAppProperty("certificatePath"), false);
		}

		return sendSuccessMessage(messageForKey("successPushMessageSentToIdealDrivers", request));
	}

}