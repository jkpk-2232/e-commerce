package com.webapp.actions.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.LoginUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.DriverDutyLogsModel;
import com.webapp.models.UserModel;

@Path("/api/logout")
public class LogoutAction extends BusinessApiAction {

	@DELETE
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response userLogOut(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader
			) {
	//@formatter:on		

		String userId = checkValidSession(sessionKeyHeader);

		if (userId == null) {
			return sendUnauthorizedRequestError();
		}

		UserModel userModel = UserModel.getUserAccountDetailsById(userId);

		if (userModel == null) {

			return sendUnauthorizedRequestError();
		}

		int logoutStatus = LoginUtils.deleteApiSessionKey(userId);

		if (logoutStatus > 0) {

			userModel.setUserId(userId);
			userModel.setOnDuty(false);
			userModel.driverOnOffDuty();

			DriverDutyLogsModel driverDutyLogsModel = new DriverDutyLogsModel();
			driverDutyLogsModel.setDriverId(userId);
			driverDutyLogsModel.setDutyStatus(false);
			driverDutyLogsModel.setCreatedAt(DateUtils.nowAsGmtMillisec());

			DriverDutyLogsModel lastDriverDutyLogDetails = DriverDutyLogsModel.getLastDriverDutyLogDetails(userId);

			if (lastDriverDutyLogDetails != null) {

				if (lastDriverDutyLogDetails.isDutyStatus() ^ driverDutyLogsModel.isDutyStatus()) {

					driverDutyLogsModel.addDriverDutyLogs();

					if ((!driverDutyLogsModel.isDutyStatus()) && (lastDriverDutyLogDetails.isDutyStatus())) {

						long startTimeInMilliInDefaultTimeZone = lastDriverDutyLogDetails.getCreatedAt() + ProjectConstants.EXTRA_TIME_OF_DEFAULT_TIMEZONE_ON_GMT;
						long endTimeInMilliInDefaultTimeZone = driverDutyLogsModel.getCreatedAt() + ProjectConstants.EXTRA_TIME_OF_DEFAULT_TIMEZONE_ON_GMT;

						DriverAction.calculateLoggedInTimeAndSave(userId, userId, startTimeInMilliInDefaultTimeZone, endTimeInMilliInDefaultTimeZone);
					}
				}
			}

			return sendSuccessMessage(messageForKey("successLogout", request));
		}

		return sendUnexpectedError();
	}

}