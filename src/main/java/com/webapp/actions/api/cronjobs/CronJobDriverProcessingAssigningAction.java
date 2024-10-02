package com.webapp.actions.api.cronjobs;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.utils.myhub.AppointmentUtils;
import com.utils.myhub.CourierUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.TourSearchDriverUtils;
import com.utils.myhub.TourUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AppointmentModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.OrderModel;
import com.webapp.models.TourModel;

@Path("/api/cron-job-process-assign-drivers")
public class CronJobDriverProcessingAssigningAction extends BusinessApiAction {

	@Path("/orders")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response assignDriverToOrders(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException, IOException {
	//@formatter:on

		int start = 0;
		int length = 50;
		long counter = 0;
		long orderCreationTime = 0;
		Duration dur;
		List<String> orderStatus = OrderUtils.getStatusListForOrderProcessingCronJob();
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();
		List<OrderModel> orderList;

		while (true) {

			orderList = OrderModel.getOrdersForProcessingCronJob(orderStatus, start, length);

			if (orderList.isEmpty()) {
				break;
			}

			for (OrderModel orderModel : orderList) {

				counter++;
				orderCreationTime = orderModel.getOrderCreationTime();
				dur = Duration.between(Instant.ofEpochMilli(orderCreationTime), Instant.now());

				if (dur.toHours() > orderModel.getOrderJobCancellationTimeHours()) {
					OrderUtils.updateOrderDeliveryStatus(ProjectConstants.CRON_JOB_USER_ID, orderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_EXPIRED);
					OrderUtils.revertProductInventoryCount(ProjectConstants.CRON_JOB_USER_ID, orderModel.getOrderId());
					continue;
				}

				String driverId = OrderUtils.searchDriver(adminSettings, orderModel, driverWalletSettingsModel);

				logger.info("\n\n\n\t1\t" + driverId);

				if (driverId != null) {
					TourSearchDriverUtils.sendDriverNotification(driverId, ProjectConstants.TRIP_TYPE_ORDER_ID, orderModel);
				}

			}

			start += length;
		}

		return sendSuccessMessage("Cron job for processing driver assignments for orders completed :: " + counter);
	}

	@Path("/couriers")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response assignDriverToCouriers(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException, IOException {
	//@formatter:on

		int start = 0;
		int length = 50;
		long counter = 0;
//		long courierCreationTime = 0;
//		Duration dur;
		List<String> courierStatus = CourierUtils.getStatusListForCourierProcessingCronJob();
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
		DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();
		List<TourModel> courierList;

		long currentTime = DateUtils.nowAsGmtMillisec() - (2 * ProjectConstants.ONE_MINUTE_MILLISECONDS_LONG);
		long nextOneHour = currentTime + ProjectConstants.ONE_HOUR_MILLISECONDS_LONG;

		while (true) {

			courierList = TourModel.getCouriersForProcessingCronJob(courierStatus, start, length, ProjectConstants.SUPER_SERVICE_TYPE_ID.COURIER_ID, currentTime, nextOneHour);

			if (courierList.isEmpty()) {
				break;
			}

			for (TourModel tourModel : courierList) {

				counter++;
//				courierCreationTime = tourModel.getRideLaterPickupTime();
//				dur = Duration.between(Instant.ofEpochMilli(courierCreationTime), Instant.now());
//
//				if (dur.toHours() > orderModel.getOrderJobCancellationTimeHours()) {
//					OrderUtils.updateOrderDeliveryStatus(ProjectConstants.CRON_JOB_USER_ID, orderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_EXPIRED);
//					OrderUtils.revertProductInventoryCount(ProjectConstants.CRON_JOB_USER_ID, orderModel.getOrderId());
//					continue;
//				}

				String driverId = CourierUtils.searchDriver(adminSettings, tourModel, driverWalletSettingsModel);

				logger.info("\n\n\n\t1\t" + driverId);

				if (driverId != null) {
					TourSearchDriverUtils.sendDriverNotification(driverId, ProjectConstants.TRIP_TYPE_TOUR_ID, null);
				}

			}

			start += length;
		}

		return sendSuccessMessage("Cron job for processing driver assignments for couriers completed :: " + counter);
	}

	@Path("/tours")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response assignDriverToTours(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException, IOException {
	//@formatter:on

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		if (adminSettingsModel.getDriverProcessingVia() == ProjectConstants.DRIVER_PROCESSING_VIA_THREAD_BASED_ID) {
			return sendSuccessMessage("Driver processing is set via Thread based. No driver assignment will happen via Cron Job. Please check admin settings");
		}

		int start = 0;
		int length = 50;
		long counter = 0;
		List<String> tourStatus = TourUtils.getStatusListForToursProcessingCronJob();
		List<TourModel> tourList;

		// Step 1- > Get new and assigned tours -> Done
		// Step 2-> If new then simply assign driver -> Done
		// Step 2-> If assigned, then check if more than 45 seconds, if yes then search
		// new driver
		// Step 3-> If more than 10 mins, then mark the trip as expired (Admin settings
		// min 5 to max 20) -> Done

		Instant currentTourProcessingTime;
		Instant tourLastDriverProcessingViaCronTime;
		Instant tourCreatedTime;
		Duration dur;
		long currentTime = DateUtils.nowAsGmtMillisec() - (30 * ProjectConstants.ONE_MINUTE_MILLISECONDS_LONG);

		while (true) {

			tourList = TourModel.getToursForProcessingCronJob(tourStatus, start, length, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID, currentTime);

			logger.info("\n\n\n\ttourList size :: \t" + tourList.size());

			if (tourList.isEmpty()) {
				break;
			}

			for (TourModel tourModel : tourList) {

				logger.info("\n\n\n\tprocessing for TourSearchDriverUtils.assignDriverToTours\t" + tourModel.getTourId());

				counter++;
				currentTourProcessingTime = Instant.now();

				tourCreatedTime = Instant.ofEpochMilli(tourModel.getCreatedAt());
				dur = Duration.between(tourCreatedTime, currentTourProcessingTime);

				// if tour is in new or assigned state for X mins from admin settings, then
				// expire the tour.
				if (dur.toMinutes() >= adminSettingsModel.getCronJobTripExpiryAfterXMins()) {
					logger.info("\n\n\n\tTourSearchDriverUtils.updateTourStatusExpired\t" + tourModel.getTourId());
					TourSearchDriverUtils.updateTourStatusExpired(tourModel);
				} else {

					switch (tourModel.getStatus()) {

					case ProjectConstants.TourStatusConstants.NEW_TOUR:

						// search for new driver
						logger.info("\n\n\n\tassignDriver\t" + tourModel.getTourId());
						assignDriver(tourModel, adminSettingsModel);
						break;

					case ProjectConstants.TourStatusConstants.ASSIGNED_TOUR:

						tourLastDriverProcessingViaCronTime = Instant.ofEpochMilli(tourModel.getDriverProcessingViaCronTime());
						dur = Duration.between(tourLastDriverProcessingViaCronTime, currentTourProcessingTime);

						// If assigned, then check if more than X seconds, if yes then search
						// new driver
						if (dur.getSeconds() > ProjectConstants.REQUEST_TIME_ONE) {
							logger.info("\n\n\n\tassignDriver ProjectConstants.ALGORITHM_EXPIRE_TIME\t" + tourModel.getTourId());
							assignDriver(tourModel, adminSettingsModel);
						}

						break;
					}
				}
			}

			start += length;
		}

		return sendSuccessMessage("Cron job for processing driver assignments for tours completed :: " + counter);
	}

	private void assignDriver(TourModel tourModel, AdminSettingsModel adminSettingsModel) {

		String driverId = TourSearchDriverUtils.assignDriver(tourModel, adminSettingsModel);

		logger.info("\n\n\n\t1 assignDriverToTours\t" + driverId);

		if (driverId != null) {

			TourSearchDriverUtils.updateDriverProcessingViaCronTime(tourModel);

			TourSearchDriverUtils.sendDriverNotification(driverId, ProjectConstants.TRIP_TYPE_TOUR_ID, null);
		}
	}

	@Path("/new-order-expire")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response expireNewJobsOrders(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException, IOException {
	//@formatter:on

		int start = 0;
		int length = 50;
		long counter = 0;
		long orderCreationTime = 0;
		Duration dur;
		List<String> orderStatus = OrderUtils.getStatusListForOrderExprireCronJob();
		List<OrderModel> orderList;

		while (true) {

			orderList = OrderModel.getOrdersForProcessingCronJob(orderStatus, start, length);

			if (orderList.isEmpty()) {
				break;
			}

			for (OrderModel orderModel : orderList) {

				counter++;
				orderCreationTime = orderModel.getOrderCreationTime();
				dur = Duration.between(Instant.ofEpochMilli(orderCreationTime), Instant.now());

				if ((orderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW) && dur.toHours() > orderModel.getOrderNewCancellationTimeHours())
							|| (orderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW_PAYMENT_PENDING)
										&& dur.toMinutes() > ProjectConstants.OrderDeliveryConstants.NEW_PAYMENT_PENDING_ORDERS_EXPIRY_TIME)) {

					OrderUtils.updateOrderDeliveryStatus(ProjectConstants.CRON_JOB_USER_ID, orderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_EXPIRED);

					OrderUtils.revertProductInventoryCount(ProjectConstants.CRON_JOB_USER_ID, orderModel.getOrderId());

					OrderUtils.sendOrderExpireNotificationToUser(orderModel);
				}
			}

			start += length;
		}

		return sendSuccessMessage("Cron job for expiring new orders completed :: " + counter);
	}

	@Path("/new-appointment-expire")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response expireNewAppointments(
		@Context HttpServletRequest request, 
		@Context HttpServletResponse response
		) throws SQLException, IOException {
	//@formatter:on

		int start = 0;
		int length = 50;
		long counter = 0;
		long appointmentTime = 0;
		Duration dur;
		List<String> appointmentStatus = AppointmentUtils.getStatusListForAppointmentExprireCronJob();
		List<AppointmentModel> appointmentList;

		while (true) {

			appointmentList = AppointmentModel.getAppointmentForProcessingCronJob(appointmentStatus, start, length);

			if (appointmentList.isEmpty()) {
				break;
			}

			for (AppointmentModel appointmentModel : appointmentList) {

				counter++;
				appointmentTime = appointmentModel.getAppointmentTime();
				dur = Duration.between(Instant.ofEpochMilli(appointmentTime), Instant.now());

				if ((appointmentModel.getAppointmentStatus().equalsIgnoreCase(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW) && dur.toMinutes() > appointmentModel.getCronJobExpireTimeMins())
							|| (appointmentModel.getAppointmentStatus().equalsIgnoreCase(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW_PAYMENT_PENDING)
										&& dur.toMinutes() > ProjectConstants.AppointmentConstants.NEW_PAYMENT_PENDING_APPOINTMENTS_EXPIRY_TIME)) {

					AppointmentUtils.updateAppointmentStatus(ProjectConstants.CRON_JOB_USER_ID, appointmentModel, ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_EXPIRED);

					AppointmentUtils.revertProductInventoryCount(ProjectConstants.CRON_JOB_USER_ID, appointmentModel.getAppointmentId());

					AppointmentUtils.sendAppointmentExpireNotificationToUser(appointmentModel);
				}
			}

			start += length;
		}

		return sendSuccessMessage("Cron job for expiring new appointment completed :: " + counter);
	}
}