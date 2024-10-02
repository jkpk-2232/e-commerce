package com.webapp.actions.api.cronjobs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.actions.api.DriverAction;
import com.webapp.models.DriverDutyLogsModel;
import com.webapp.models.UserModel;

@Path("/api/driver-duty")
public class CronJobDriverDutyLoggedInTimeAction extends BusinessApiAction {

	@Path("/logged-in-time/cron-job")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response driverDutyLoggedInTimeCronJob(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response
			) throws SQLException, IOException {
	//@formatter:on

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, (calendar.get(Calendar.SECOND) - 1));

		long endTimeInMillies = calendar.getTimeInMillis();

		List<UserModel> userModelList = UserModel.getDriverListForLoggedInTimeCronJob(UserRoles.DRIVER_ROLE_ID, true);

		if (userModelList != null) {

			for (UserModel userModel : userModelList) {

				long startTimeInMilliInDefaultTimeZone = userModel.getCreatedAt() + ProjectConstants.EXTRA_TIME_OF_DEFAULT_TIMEZONE_ON_GMT;
				long endTimeInMilliInDefaultTimeZone = endTimeInMillies + ProjectConstants.EXTRA_TIME_OF_DEFAULT_TIMEZONE_ON_GMT;

				DriverAction.calculateLoggedInTimeAndSave("1", userModel.getUserId(), startTimeInMilliInDefaultTimeZone, endTimeInMilliInDefaultTimeZone);

				updateDutyLogs("1", userModel.getUserId(), endTimeInMillies, userModel.getLatitude(), userModel.getLongitude());
			}
		}

		return sendSuccessMessage(messageForKey("successUpdatedOnDutyDriverInTime", request));
	}

	private void updateDutyLogs(String loggedInUserId, String driverId, long dutyLogOffAt, String latitude, String longitude) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dutyLogOffAt);
		calendar.set(Calendar.SECOND, (calendar.get(Calendar.SECOND) + 2));

		long dutyLogOnAt = calendar.getTimeInMillis();

		List<DriverDutyLogsModel> driverDutyLogsModelList = new ArrayList<DriverDutyLogsModel>();

		DriverDutyLogsModel driverOffDutyLogsModel = new DriverDutyLogsModel();
		driverOffDutyLogsModel.setDriverDutyStatusLogsId(UUIDGenerator.generateUUID());
		driverOffDutyLogsModel.setDriverId(driverId);
		driverOffDutyLogsModel.setLatitude(latitude);
		driverOffDutyLogsModel.setLongitude(longitude);
		driverOffDutyLogsModel.setDutyStatus(false);
		driverOffDutyLogsModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
		driverOffDutyLogsModel.setCreatedAt(dutyLogOffAt);
		driverOffDutyLogsModel.setCreatedBy(loggedInUserId);
		driverOffDutyLogsModel.setUpdatedAt(dutyLogOffAt);
		driverOffDutyLogsModel.setUpdatedBy(loggedInUserId);

		driverDutyLogsModelList.add(driverOffDutyLogsModel);

		DriverDutyLogsModel driverOnDutyLogsModel = new DriverDutyLogsModel();
		driverOnDutyLogsModel.setDriverDutyStatusLogsId(UUIDGenerator.generateUUID());
		driverOnDutyLogsModel.setDriverId(driverId);
		driverOnDutyLogsModel.setLatitude(latitude);
		driverOnDutyLogsModel.setLongitude(longitude);
		driverOnDutyLogsModel.setDutyStatus(true);
		driverOnDutyLogsModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
		driverOnDutyLogsModel.setCreatedAt(dutyLogOnAt);
		driverOnDutyLogsModel.setCreatedBy(loggedInUserId);
		driverOnDutyLogsModel.setUpdatedAt(dutyLogOnAt);
		driverOnDutyLogsModel.setUpdatedBy(loggedInUserId);

		driverDutyLogsModelList.add(driverOnDutyLogsModel);

		DriverDutyLogsModel.insertDriverDutyLogsBatch(driverDutyLogsModelList);
	}

}