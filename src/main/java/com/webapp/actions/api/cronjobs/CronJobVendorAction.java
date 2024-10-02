package com.webapp.actions.api.cronjobs;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
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
import com.utils.myhub.VendorMonthlySubscriptionHistoryUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorMonthlySubscriptionHistoryModel;
import com.webapp.models.VendorStoreModel;

@Path("/api/cron-job-vendor")
public class CronJobVendorAction extends BusinessApiAction {

	@Path("/open-vendor-store")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response openVendorStoreCronJob(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException, IOException {
	//@formatter:on

		Instant todayInstantObject = DateUtils.getNowInstant();
		String currentDayOfWeekValue = DateUtils.getDayOfWeek(todayInstantObject);
		List<VendorStoreModel> vendorStoreList = VendorStoreModel.getVendorStoresByOpenClosedStatus(true, todayInstantObject.toEpochMilli(), currentDayOfWeekValue);

		int counter = 0;
		List<VendorStoreModel> updateList = new ArrayList<>();

		for (VendorStoreModel vendorStoreModel : vendorStoreList) {

			counter++;

			vendorStoreModel.setClosedToday(false);
			updateList.add(vendorStoreModel);

			if (updateList.size() >= ProjectConstants.BATCH_UPDATE_SIZE) {
				VendorStoreModel.updateClosedTodayFlag(updateList);
				updateList.clear();
			}
		}

		if (!updateList.isEmpty()) {
			VendorStoreModel.updateClosedTodayFlag(updateList);
			updateList.clear();
		}

		return sendSuccessMessage("Cron job for reopening the closed vendor stores ran successfully : " + counter);
	}

	@Path("/monthly-subscription-expiry")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response monthlySubscriptionExpiryCronJob(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException, IOException {
	//@formatter:on

		long currentTime = DateUtils.nowAsGmtMillisec() - (24 * ProjectConstants.ONE_HOUR_MILLISECONDS_LONG);
		long next3Days = currentTime + (3 * 24 * ProjectConstants.ONE_HOUR_MILLISECONDS_LONG);

		Instant todayInstantObject = DateUtils.getNowInstant();
		List<UserProfileModel> vendorList = UserProfileModel.getVendorListForVendorSubscriptionExpiry(UserRoles.VENDOR_ROLE_ID, currentTime, next3Days);

		int counter = 0;
		List<UserProfileModel> updateAccountToExpiredList = new ArrayList<>();
		List<VendorMonthlySubscriptionHistoryModel> updateVendorMonthlySubscriptionToExpiredList = new ArrayList<>();

		for (UserProfileModel userProfileModel : vendorList) {

			counter++;

			if (userProfileModel.getVendorCurrentSubscriptionEndDateTime() <= todayInstantObject.toEpochMilli()) {

				userProfileModel.setVendorSubscriptionCurrentActive(false);
				userProfileModel.setVendorSubscriptionMarkedExpiredByCronJob(true);
				userProfileModel.setVendorSubscriptionMarkedExpiredByCronJobTiming(todayInstantObject.toEpochMilli());
				updateAccountToExpiredList.add(userProfileModel);

				VendorMonthlySubscriptionHistoryModel vmshm = new VendorMonthlySubscriptionHistoryModel();
				vmshm.setVendorMonthlySubscriptionHistoryId(userProfileModel.getVendorMonthlySubscriptionHistoryId());
				vmshm.setVendorSubscriptionCurrentActive(false);
				updateVendorMonthlySubscriptionToExpiredList.add(vmshm);
			}

			if (updateAccountToExpiredList.size() >= ProjectConstants.BATCH_UPDATE_SIZE) {
				VendorMonthlySubscriptionHistoryUtils.takeExpiryAction(updateAccountToExpiredList, updateVendorMonthlySubscriptionToExpiredList);
			}
		}

		if (!updateAccountToExpiredList.isEmpty()) {
			VendorMonthlySubscriptionHistoryUtils.takeExpiryAction(updateAccountToExpiredList, updateVendorMonthlySubscriptionToExpiredList);
		}

		return sendSuccessMessage("Cron job for marking the vendor account as inactive for monthly subscription expiry : " + counter);
	}
}