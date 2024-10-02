package com.webapp.actions.api.ridelater;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.jeeutils.DateUtils;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.validator.RequiredValidationRule;
import com.jeeutils.validator.Validator;
import com.utils.CommonUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.CourierUtils;
import com.utils.myhub.DriverTourStatusUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.OrderUtils;
import com.utils.myhub.PromoCodeUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.webapp.ProjectConstants;
import com.webapp.ProjectConstants.UserRoles;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarFareModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverTourRequestModel;
import com.webapp.models.DriverTripRatingsModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.PassangerNotificationThread;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.RentalPackageFareModel;
import com.webapp.models.RideLaterSettingsModel;
import com.webapp.models.SurgePriceModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourTimeModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.UserPromoCodeModel;
import com.webapp.models.UtilizedUserPromoCodeModel;
import com.webapp.models.WebSocketClient;

@Path("/api/ride-later")
public class RideLaterAction extends BusinessApiAction {

	private static final String SOURCE_LATITUDE = "sLatitude";
	private static final String SOURCE_LATITUDE_LABEL = "Source Latitude";

	private static final String DESTINATION_LATITUDE = "dLatitude";
	private static final String DESTINATION_LATITUDE_LABEL = "Destination Latitude";

	private static final String SOURCE_LONGITUDE = "sLongitude";
	private static final String SOURCE_LONGITUDE_LABEL = "Source Longitude";

	private static final String DESTINATION_LONGITUDE = "dLongitude";
	private static final String DESTINATION_LONGITUDE_LABEL = "Destination Longitude";

	private static final String CAR_TYPE_ID = "carTypeId";
	private static final String CAR_TYPE_ID_LABEL = "Car Type Id";

	private static final String SOURCE_ADDRESS = "sourceAddress";
	private static final String SOURCE_ADDRESS_LABEL = "Source Address";

	private static final String DESTINATION_ADDRESS = "destinationAddress";
	private static final String DESTINATION_ADDRESS_LABEL = "Destination Address ";

	private static final String DISTANCE = "distance";
	private static final String DISTANCE_LABEL = "Distance";

	private static final String CHARGES = "charges";
	private static final String CHARGES_LABEL = "Charges";

	private static final String TRANSMISSION_TYPE_ID = "transmissionTypeId";
	private static final String TRANSMISSION_TYPE_LABEL = "Transmission Type";

	private static final String IS_AIRPORT_FIXED_FARE_APPLIED = "airportFixedFareApplied";
	private static final String IS_AIRPORT_FIXED_FARE_APPLIED_LABEL = "Airport Fixed Fare Flag";

	@Path("/driver-assign-cron-job")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response rideLaterCronJob(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response
			) throws SQLException, IOException {
	//@formatter:on

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();
		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		long currentTime = DateUtils.nowAsGmtMillisec() - (2 * ProjectConstants.ONE_MINUTE_MILLISECONDS_LONG);
		long nextOneHour = currentTime + ProjectConstants.ONE_HOUR_MILLISECONDS_LONG;
		long endTime = currentTime + (rideLaterSettingsModel.getMaxBookingTime() * ProjectConstants.ONE_MINUTE_MILLISECONDS_LONG);

		Map<String, Object> inputMap = new HashMap<String, Object>();

		inputMap.put("currentTime", currentTime);
		inputMap.put("nextOneHour", nextOneHour);
		inputMap.put("endTime", endTime);

		List<TourModel> rideLaterTourlist = TourModel.getCronJobRideLaterTourList(inputMap);
		List<TourModel> rideLaterTourNotificationlist = TourModel.getCronJobRideLaterTourListForNotification(inputMap);

		for (TourModel tourModel : rideLaterTourNotificationlist) {

			//@formatter:off
			if (!tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) && 
			    !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.STARTED_TOUR) && 
			    !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR) && 
			    !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER) && 
			    !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.EXPIRED_TOUR)) {
			//@formatter:on

				//@formatter:off
				if ((tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR)) ||
						tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR) ||
						tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR) ||
						tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING)) {
				//@formatter:on

					long currentTime2 = DateUtils.nowAsGmtMillisec();
					long tourCreatedAt = tourModel.getCreatedAt();

					if (ProjectConstants.DEFAULT_DRIVER_ID.equalsIgnoreCase(tourModel.getDriverId()) && (currentTime2 - tourCreatedAt) > ProjectConstants.ONE_HOUR_MILLISECONDS_LONG) {
						TourUtils.updateTourCriticalStatus(tourModel, true);
					}

					if (!ProjectConstants.DEFAULT_DRIVER_ID.equalsIgnoreCase(tourModel.getDriverId())) {

						DriverTourRequestModel driverTourRequestModel = DriverTourRequestModel.getTourRequestByDriverIdAndTourId(tourModel.getDriverId(), tourModel.getTourId());
						long currentTime1 = DateUtils.nowAsGmtMillisec();
						long driverTourRequestedTime = driverTourRequestModel.getCreatedAt();

						if ((currentTime1 - driverTourRequestedTime) > (rideLaterSettingsModel.getDriverJobRequestTime() * ProjectConstants.RIDE_LATER_CRON_JOB_TIME_1MIN)) {

							TourUtils.assignTourDriver(tourModel.getTourId(), ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR, tourModel.getCreatedBy());

							TourUtils.updateTourCriticalStatus(tourModel, true);
						}
					}
				}

			}
		}

		for (TourModel tourModel : rideLaterTourlist) {

			//@formatter:off
			if (!tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) && 
			    !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.STARTED_TOUR) && 
			    !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR) && 
			    !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER) && 
			    !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.EXPIRED_TOUR)) {
			//@formatter:on

				if (!tourModel.getDriverId().equalsIgnoreCase(ProjectConstants.DEFAULT_DRIVER_ID)) {

					TourModel tour = TourModel.getCurrentTourByDriverId(tourModel.getDriverId());

					if (tour != null) {

						if (tour.getTourId().equalsIgnoreCase(tourModel.getTourId())) {
							continue;
						} else {

							long a = DateUtils.nowAsGmtMillisec();
							long b = tour.getRideLaterLastNotification();

							if (tourModel.getRideLaterPickupTime() <= DateUtils.nowAsGmtMillisec()) {

								TourModel tour4 = new TourModel();
								tour4.setTourId(tourModel.getTourId());
								tour4.setDriverId(tourModel.getDriverId());
								tour4.setStatus(ProjectConstants.TourStatusConstants.EXPIRED_TOUR);

								tour4.updateTourStatusByTourId();

								sendNotificationToPassengerForTripExpired(tourModel);

								sendNotificationToDriverForTripExpired(tourModel);

								TourModel tour1 = new TourModel();
								tour1.setTourId(tourModel.getTourId());
								tour1.setDriverId(tourModel.getDriverId());
								tour1.setTourRideLater(false);

								tour1.updateRideLaterTourFlag();

								TourUtils.updateTourCriticalStatus(tourModel, false);

							} else if ((a - b) > ProjectConstants.RIDE_LATER_CRON_JOB_TIME) {

								String message = BusinessApiAction.messageForKey("cronJobDriverTripMessage", request);

								ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourModel.getDriverId());

								ApnsMessageModel apnsMessage = new ApnsMessageModel();
								apnsMessage.setMessage(message);
								apnsMessage.setMessageType("push");
								apnsMessage.setToUserId(tourModel.getDriverId());
								apnsMessage.insertPushMessage();

								if (apnsDevice != null) {
									apnsDevice.sendNotification("1", "Push", message, ProjectConstants.RIDE_LATER_CRON_JOB, WebappPropertyUtils.getWebAppProperty("certificatePath"));
								}

								TourModel tour6 = new TourModel();
								tour6.setTourId(tour.getTourId());
								tour6.setRideLaterLastNotification(DateUtils.nowAsGmtMillisec());

								tour6.updateTourRideLaterLastNotification();

								ApnsDeviceModel passengerDeviceDetails = ApnsDeviceModel.getDeviseByUserId(tourModel.getPassengerId());

								if (passengerDeviceDetails != null) {

									String messageForPassenger = BusinessApiAction.messageForKey("cronJobPassengerTripMessage1", request) + " "
												+ DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), passengerDeviceDetails.getTimezone(), DateUtils.DATE_TIME_PICKER_FORMAT_FOR_12_HOURS) + " "
												+ BusinessApiAction.messageForKey("cronJobPassengerTripMessage2", request);

									ApnsMessageModel apnsMessageToPassenger = new ApnsMessageModel();
									apnsMessageToPassenger.setMessage(messageForPassenger);
									apnsMessageToPassenger.setMessageType("push");
									apnsMessageToPassenger.setToUserId(tourModel.getPassengerId());
									apnsMessageToPassenger.insertPushMessage();

									passengerDeviceDetails.sendNotification("1", "Push", messageForPassenger, ProjectConstants.RIDE_LATER_CRON_JOB, WebappPropertyUtils.getWebAppProperty("certificatePath"));

									MetamorphSystemsSmsUtils.sendSmsToSingleUser(messageForPassenger, tourModel.getpPhoneCode() + tourModel.getpPhone().trim(), null);
								}
							}
						}

					} else {

						//@formatter:off
						if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST)) {
						//@formatter:on

							if (tourModel.getRideLaterPickupTime() <= DateUtils.nowAsGmtMillisec()) {

								DriverTourStatusUtils.updateDriverTourStatus(tourModel.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);

								TourModel tour4 = new TourModel();
								tour4.setTourId(tourModel.getTourId());
								tour4.setDriverId(tourModel.getDriverId());
								tour4.setStatus(ProjectConstants.TourStatusConstants.EXPIRED_TOUR);

								tour4.updateTourStatusByTourId();

								sendNotificationToPassengerForTripExpired(tourModel);

								sendNotificationToDriverForTripExpired(tourModel);

								TourModel tour1 = new TourModel();
								tour1.setTourId(tourModel.getTourId());
								tour1.setDriverId(tourModel.getDriverId());
								tour1.setTourRideLater(false);

								tour1.updateRideLaterTourFlag();

								TourUtils.updateTourCriticalStatus(tourModel, false);

							} else {

								long a = DateUtils.nowAsGmtMillisec();
								long b = tourModel.getRideLaterPickupTime();

								if ((b - a) < (rideLaterSettingsModel.getDriverAllocateBeforeTime() * ProjectConstants.RIDE_LATER_CRON_JOB_TIME_1MIN)) {

									TourModel passengerCurrentTour = TourModel.getCurrentTourByPassangerId(tourModel.getPassengerId());

									boolean isPassengerBusyWithAnotherTrip = false;

									if (passengerCurrentTour != null && tourModel.getBookingType().equalsIgnoreCase(ProjectConstants.INDIVIDUAL_BOOKING)) {
										if (!(passengerCurrentTour.getTourId().equals(tourModel.getTourId()))) {
											isPassengerBusyWithAnotherTrip = true;
										}
									}

									if (isPassengerBusyWithAnotherTrip) {

										ApnsDeviceModel passengerDeviceDetails = ApnsDeviceModel.getDeviseByUserId(tourModel.getPassengerId());

										if (passengerDeviceDetails != null) {
											String messageForPassenger = BusinessApiAction.messageForKey("cronJobPassengerTripMessage1", request) + " "
														+ DateUtils.dbTimeStampToSesionDate(tourModel.getCreatedAt(), passengerDeviceDetails.getTimezone(), DateUtils.DATE_TIME_PICKER_FORMAT_FOR_12_HOURS) + " "
														+ BusinessApiAction.messageForKey("cronJobPassengerTripMessage2", request);

											ApnsMessageModel apnsMessageToPassenger = new ApnsMessageModel();
											apnsMessageToPassenger.setMessage(messageForPassenger);
											apnsMessageToPassenger.setMessageType("push");
											apnsMessageToPassenger.setToUserId(tourModel.getPassengerId());
											apnsMessageToPassenger.insertPushMessage();

											passengerDeviceDetails.sendNotification("1", "Push", messageForPassenger, ProjectConstants.RIDE_LATER_CRON_JOB, WebappPropertyUtils.getWebAppProperty("certificatePath"));

											MetamorphSystemsSmsUtils.sendSmsToSingleUser(messageForPassenger, tourModel.getpPhoneCode() + tourModel.getpPhone().trim(), null);
										}
									} else {

										InvoiceModel invoiceModel = InvoiceModel.getPendingPaymentTourByPassengerId(tourModel.getPassengerId());

										if (invoiceModel != null && invoiceModel.isPaymentPaid() == false && invoiceModel.getFinalAmountCollected() > 0) {

											long time1 = tourModel.getRideLaterPickupTime() - ((1000 * 60) * 10);
											long time2 = time1 + (1000 * 59);

											long current = DateUtils.nowAsGmtMillisec();

											if ((current >= time1) && (current <= time2)) {

												ApnsDeviceModel passengerDeviceDetails = ApnsDeviceModel.getDeviseByUserId(tourModel.getPassengerId());

												if (passengerDeviceDetails != null) {

													String messageForPassenger = BusinessApiAction.messageForKey("msgPaymentPendingToPassForRL", request) + " " + tourModel.getRideLaterPickupTimeLogs();

													ApnsMessageModel apnsMessageToPassenger = new ApnsMessageModel();
													apnsMessageToPassenger.setMessage(messageForPassenger);
													apnsMessageToPassenger.setMessageType("push");
													apnsMessageToPassenger.setToUserId(tourModel.getPassengerId());
													apnsMessageToPassenger.insertPushMessage();

													passengerDeviceDetails.sendNotification("1", "Push", messageForPassenger, ProjectConstants.RIDE_LATER_CRON_JOB, WebappPropertyUtils.getWebAppProperty("certificatePath"));

												}

											}

										} else {

											DriverTourStatusUtils.updateDriverTourStatus(tourModel.getDriverId(), ProjectConstants.DRIVER_HIRED_STATUS);

											TourModel tour3 = new TourModel();
											tour3.setTourId(tourModel.getTourId());
											tour3.setDriverId(tourModel.getDriverId());
											tour3.setStatus(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);
											tour3.updateTourStatusByTourId();

											MultiTenantUtils.changeTourFareParametersWithVendorFare(tourModel.getTourId(), tourModel.getDriverId());

											TourModel tour1 = new TourModel();
											tour1.setTourId(tourModel.getTourId());
											tour1.setDriverId(tourModel.getDriverId());
											tour1.setTourRideLater(false);

											tour1.updateRideLaterTourFlag();

											TourUtils.updateTourCriticalStatus(tourModel, false);

											sendDriverNotification(tourModel.getDriverId(), tourModel.getTourId());

											ApnsDeviceModel driverAapnsDevice = ApnsDeviceModel.getDeviseByUserId(tourModel.getDriverId());

											String tripPickupTimeString = DateUtils.dbTimeStampToSesionDate(tourModel.getRideLaterPickupTime(), driverAapnsDevice.getTimezone(), DateUtils.DATE_FORMAT_FOR_VIEW_FOR_12_HOURS);

											String driverPushMsg = BusinessAction.messageForKeyAdmin("driverPushMsgForRideLater", tourModel.getLanguage()) + " " + tripPickupTimeString;

											ApnsMessageModel driverApnsMessage = new ApnsMessageModel();
											driverApnsMessage.setMessage(driverPushMsg);
											driverApnsMessage.setMessageType("push");
											driverApnsMessage.setToUserId(tourModel.getDriverId());
											driverApnsMessage.insertPushMessage();

											if (driverAapnsDevice != null) {

												driverAapnsDevice.sendNotification("1", "Push", driverPushMsg, ProjectConstants.NOTIFICATION_TYPE_MB_NEW_JOB, WebappPropertyUtils.getWebAppProperty("certificatePath"));
											}

											if (tourModel.getBookingType().equals(ProjectConstants.INDIVIDUAL_BOOKING)) {

												DriverGeoLocationModel driverLocation = DriverGeoLocationModel.getCurrentDriverPosition(tourModel.getDriverId());

												double distance = CommonUtils.distance(Double.parseDouble(tourModel.getsLatitude()), Double.parseDouble(tourModel.getsLongitude()), Double.parseDouble(driverLocation.getLatitude()),
															Double.parseDouble(driverLocation.getLongitude()), 'K');
												double min = CommonUtils.calculateETA(distance, adminSettingsModel.getDistanceUnits());
												DecimalFormat df = new DecimalFormat("0");

												String message = String.format(BusinessAction.messageForKeyAdmin("driverAction_1", tourModel.getLanguage()), df.format(min));
												String templateId = ProjectConstants.SMSConstants.SMS_ARRIVING_TEMPLATE_ID;

												ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourModel.getPassengerId());

												ApnsMessageModel apnsMessage = new ApnsMessageModel();
												apnsMessage.setMessage(message);
												apnsMessage.setMessageType("push");
												apnsMessage.setToUserId(tourModel.getPassengerId());
												apnsMessage.insertPushMessage();

												AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

												if (adminSmsSendingModel.ispAcceptByDriver()) {
													MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourModel.getpPhoneCode() + tourModel.getpPhone().trim(), templateId);
												}

												if (apnsDevice != null) {

													if (tourModel.getBookingType().equalsIgnoreCase(ProjectConstants.INDIVIDUAL_BOOKING)) {
														new PassangerNotificationThread(apnsDevice.getDeviceToken(), tourModel.getTourId(), apnsDevice.getApiSessionKey());
													}

													apnsDevice.sendNotification("1", "Push", message, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, WebappPropertyUtils.getWebAppProperty("certificatePath"));
												}

											}
										}

									}
								}
							}
						} else {

							//@formatter:off
							if(tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR) ||
									tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR) ||
									tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR) ||
									tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING_REQUEST))
							//@formatter:on

								if (tourModel.getRideLaterPickupTime() <= DateUtils.nowAsGmtMillisec()) {

									TourModel tour4 = new TourModel();
									tour4.setTourId(tourModel.getTourId());
									tour4.setDriverId(tourModel.getDriverId());
									tour4.setStatus(ProjectConstants.TourStatusConstants.EXPIRED_TOUR);

									tour4.updateTourStatusByTourId();

									sendNotificationToPassengerForTripExpired(tourModel);

									sendNotificationToDriverForTripExpired(tourModel);

									TourModel tour1 = new TourModel();
									tour1.setTourId(tourModel.getTourId());
									tour1.setDriverId(tourModel.getDriverId());
									tour1.setTourRideLater(false);

									tour1.updateRideLaterTourFlag();

									TourUtils.updateTourCriticalStatus(tourModel, false);
								}
						}
					}
				} else {

					if (tourModel.getRideLaterPickupTime() <= DateUtils.nowAsGmtMillisec()) {

						TourModel tour4 = new TourModel();
						tour4.setTourId(tourModel.getTourId());
						tour4.setDriverId(tourModel.getDriverId());
						tour4.setStatus(ProjectConstants.TourStatusConstants.EXPIRED_TOUR);

						tour4.updateTourStatusByTourId();

						sendNotificationToPassengerForTripExpired(tourModel);

						sendNotificationToDriverForTripExpired(tourModel);

						TourModel tour1 = new TourModel();
						tour1.setTourId(tourModel.getTourId());
						tour1.setDriverId(tourModel.getDriverId());
						tour1.setTourRideLater(false);

						tour1.updateRideLaterTourFlag();

						TourUtils.updateTourCriticalStatus(tourModel, false);
					}
				}
			}
		}

		expireToursThosePickupTimeIsLessThanCurrent();

		return sendSuccessMessage("\n\n\n\n\tBook later cron job executed for tour of count\t" + rideLaterTourlist.size());
	}

	private void expireToursThosePickupTimeIsLessThanCurrent() {

		long currentTimeBeforeOneHour = DateUtils.nowAsGmtMillisec() - (10 * ProjectConstants.RIDE_LATER_CRON_JOB_TIME_1MIN);

		List<String> statusList = new ArrayList<>();
		statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);
		statusList.add(ProjectConstants.TourStatusConstants.EXPIRED_TOUR);
		statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR);
		statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR);
		statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_EXPIRED_TOUR);
		statusList.add(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING);

		List<TourModel> tourModelList = TourModel.getRideLaterTourListToExpire(currentTimeBeforeOneHour, statusList);

		if (tourModelList != null && tourModelList.size() > 0) {

			List<String> tourIds = new ArrayList<>();

			for (TourModel tourModel : tourModelList) {
				tourIds.add(tourModel.getTourId());
			}

			if (tourIds.size() > 0) {

				TourModel.expireToursBatch(tourIds);
			}
		}

	}

	private void sendNotificationToPassengerForTripExpired(TourModel tourModel) {

		String message = "";

		InvoiceModel invoiceModel = InvoiceModel.getPendingPaymentTourByPassengerId(tourModel.getPassengerId());

		if (invoiceModel != null && invoiceModel.isPaymentPaid() == false && invoiceModel.getFinalAmountCollected() > 0) {
			message = "As your earlier trip payment was pending your scheduled trip is cancelled.";
		} else {
			message = "Your book later request has been expired.";
		}

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourModel.getPassengerId());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("push");
		apnsMessage.setToUserId(tourModel.getPassengerId());
		apnsMessage.insertPushMessage();

		if (apnsDevice != null) {
			apnsDevice.sendNotification("1", "Push", message, ProjectConstants.RIDE_LATER_CRON_JOB, WebappPropertyUtils.getWebAppProperty("certificatePath"));
		}
	}

	private void sendNotificationToDriverForTripExpired(TourModel tourModel) {

		if ((tourModel.getDriverId() != null) && (!"".equals(tourModel.getDriverId())) && (!"-1".equals(tourModel.getDriverId()))) {

			String message = "Trip is cancelled by the system.";

			ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourModel.getDriverId());

			ApnsMessageModel apnsMessage = new ApnsMessageModel();
			apnsMessage.setMessage(message);
			apnsMessage.setMessageType("push");
			apnsMessage.setToUserId(tourModel.getDriverId());
			apnsMessage.insertPushMessage();

			if (apnsDevice != null) {
				apnsDevice.sendNotification("1", "Push", message, ProjectConstants.RIDE_LATER_CRON_JOB, WebappPropertyUtils.getWebAppProperty("certificatePath"));
			}
		}
	}

	@Path("/passenger/min-max-time/{carTypeId}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response bookRideLaterMinMaxTime(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("carTypeId") String carTypeId
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		CarFareModel carFareModel = CarFareModel.getById(carTypeId, ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

		HashMap<String, Object> outputMap = new HashMap<String, Object>();

		outputMap.put("minBookingTime", rideLaterSettingsModel.getMinBookingTime());
		outputMap.put("maxBookingTime", rideLaterSettingsModel.getMaxBookingTime());

		outputMap.put("initialFare", carFareModel.getInitialFare());
		outputMap.put("perKmFare", carFareModel.getPerKmFare());
		outputMap.put("perMinuteFare", carFareModel.getPerMinuteFare());
		outputMap.put("bookingFees", carFareModel.getBookingFees());
		outputMap.put("minimumFare", carFareModel.getMinimumFare());
		outputMap.put("discount", carFareModel.getDiscount());
		outputMap.put("cancellationCharges", CancellationChargeModel.getAdminCancellationCharges().getCharge());
		outputMap.put("freeDistance", carFareModel.getFreeDistance());

		return sendDataResponse(outputMap);
	}

	@Path("/passenger/car-book")
	@POST
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response bookRideLater(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			TourModel tourModel
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		headerVendorId = MultiTenantUtils.validateVendor(headerVendorId);

		if (headerVendorId == null) {
			return sendBussinessError(messageForKey("errorInvalidVendor", request));
		}

		errorMessages = CarBookModelValidtion(tourModel);

		if (errorMessages.size() != 0) {

			return sendBussinessError(errorMessages);
		}

		ApnsDeviceModel apnsDeviceModel = ApnsDeviceModel.getDeviseByUserId(loggedInuserId);

		long pickupTime = DateUtils.getDateFromStringForRideLater(tourModel.getRideLaterPickupTimeLogs(), apnsDeviceModel.getTimezone());

		tourModel.setRideLaterPickupTime(pickupTime);

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		long beforePickupTime = pickupTime - (rideLaterSettingsModel.getPassengerTourBeforeTime() * ProjectConstants.ONE_MINUTE_MILLISECONDS_LONG);
		long afterPickupTime = pickupTime + (rideLaterSettingsModel.getPassengerTourAfterTime() * ProjectConstants.ONE_MINUTE_MILLISECONDS_LONG);

		int count = TourModel.getRideLaterPassengerDetailsBetweenTimeSlot(loggedInuserId, beforePickupTime, afterPickupTime);

		if (count > 0) {

			return sendBussinessError(messageForKey("errorPassengerFutureRideLaterRequestExist", request));
		}

		InvoiceModel invoiceModel = InvoiceModel.getPendingPaymentTourByPassengerId(loggedInuserId);

		if (invoiceModel != null) {

			if (invoiceModel.isPaymentPaid() == false && invoiceModel.getFinalAmountCollected() > 0) {

				Map<String, Object> outputMap = new HashMap<String, Object>();

				outputMap.put("statusCode", 202);
				outputMap.put("type", "ERROR-BUSSINESS");
				outputMap.put("messages", messageForKey("errorPendingPaymentOfPreviousBooking", request));

				return sendDataResponse(outputMap);
			}
		}

		if (!tourModel.getPaymentType().equalsIgnoreCase(ProjectConstants.WALLET_ID) && !tourModel.getPaymentType().equalsIgnoreCase(ProjectConstants.CASH_ID) && !tourModel.getPaymentType().equalsIgnoreCase(ProjectConstants.CARD_ID)) {
			return sendBussinessError(messageForKey("errorInvlidPaymentType", request));
		}

		if (tourModel.getMulticityCityRegionId() == null || "".equals(tourModel.getMulticityCityRegionId())) {

			return sendBussinessError(messageForKey("errorMulticityCityRegionIdRequired", request));
		}

		if (tourModel.getMulticityCountryId() == null || "".equals(tourModel.getMulticityCountryId())) {

			return sendBussinessError(messageForKey("errorMulticityCountryIdRequired", request));
		}

		if (tourModel.isRentalBooking()) {

			RentalPackageFareModel rentalPackageFareModel = RentalPackageFareModel.getRentalPackageFareDetailsByRentalIdnCarType(tourModel.getRentalPackageId(), tourModel.getCarTypeId());
			if (rentalPackageFareModel == null) {
				return sendBussinessError(messageForKey("errorInvalidRentalPackage", request));
			}

			// TODO: rental api check for rental car type available for vendor
			if (!MultiTenantUtils.validateVendorCarTypeRental(tourModel.getCarTypeId(), headerVendorId, tourModel.getMulticityCityRegionId(), tourModel.getRentalPackageId())) {
				return sendBussinessError(messageForKey("errorInvalidCarTypeForVendorAndRegionForRental", request));
			}
		}

		boolean isSurgePriceApplied = false;
		String surgePriceId = "-1";
		double surgePrice = 1;

		if (tourModel.isRentalBooking() || tourModel.isAirportFixedFareApplied()) {

			isSurgePriceApplied = false;
			surgePriceId = "-1";
			surgePrice = 1;

		} else {

			String timeZone = WebappPropertyUtils.CLIENT_TIMEZONE; // Used webapps properties time zone as default

			ApnsDeviceModel userDeviceDetails = ApnsDeviceModel.getDeviseByUserId(loggedInuserId);

			if (userDeviceDetails != null) {

				timeZone = userDeviceDetails.getTimezone();
			}

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			Calendar calender = Calendar.getInstance();
			calender.setTimeZone(TimeZone.getTimeZone(WebappPropertyUtils.CLIENT_TIMEZONE));

			int currentHourOfDay = calender.get(Calendar.HOUR_OF_DAY);
			int currentMinute = calender.get(Calendar.MINUTE);

			long requestTimeInMilli = DateUtils.getTimeInMillisForSurge(currentHourOfDay, currentMinute, false, timeZone);

			Map<String, Object> ac = TourUtils.checkBookingForAirportPickupOrDrop(tourModel.getsLatitude(), tourModel.getsLongitude(), tourModel.getdLatitude(), tourModel.getdLongitude());
			if (((boolean) ac.get("isAirportPickUp") || (boolean) ac.get("isAirportDrop"))) {
				tourModel.setAirportBooking(true);
			}

			SurgePriceModel surgePriceModel = null;
			if (!tourModel.isAirportBooking()) {
				surgePriceModel = SurgePriceModel.getSurgePriceDetailsByRequestTimeAndRegionId(requestTimeInMilli, tourModel.getMulticityCityRegionId());
			}

			isSurgePriceApplied = false;
			surgePriceId = "-1";
			surgePrice = 1;

			if (surgePriceModel != null) {

				if (tourModel.isSurgePriceApplied()) {

					if (surgePriceModel.getSurgePrice() > tourModel.getSurgePrice()) {

						isSurgePriceApplied = true;
						surgePriceId = surgePriceModel.getSurgePriceId();
						surgePrice = surgePriceModel.getSurgePrice();
					}
				} else {

					isSurgePriceApplied = true;
					surgePriceId = surgePriceModel.getSurgePriceId();
					surgePrice = surgePriceModel.getSurgePrice();
				}

			}

			if (isSurgePriceApplied) {

				double estimateFareWithoutDiscount = 0.0;
				double estimateFareWithDiscount = 0.0;

				if (((tourModel.getdLatitude() != null) && (!"".equals(tourModel.getdLatitude()))) && ((tourModel.getdLongitude() != null) && (!"".equals(tourModel.getdLongitude())))) {

					Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(tourModel.getsLatitude(), tourModel.getsLongitude(), tourModel.getdLatitude(), tourModel.getdLongitude());

					double distanceInMeters = distanceMatrix.get("distanceInMeters");
					double durationInMin = distanceMatrix.get("durationInMin");

					CarFareModel carFareModel = CarFareModel.getCarFareDetailsByRegionCountryAndId(tourModel.getCarTypeId(), tourModel.getMulticityCityRegionId(), tourModel.getMulticityCountryId(), headerVendorId,
								ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID);

					if (carFareModel != null) {

						double distanceInKm = (distanceInMeters - carFareModel.getFreeDistance()) / adminSettingsModel.getDistanceUnits();

						double baseFare = carFareModel.getInitialFare(); // + carFareModel.getBookingFees();
						double distanceFare = (distanceInKm > 0 ? distanceInKm : 0) * carFareModel.getPerKmFare();
						double timeFare = durationInMin * carFareModel.getPerMinuteFare();

						estimateFareWithoutDiscount = baseFare + distanceFare + timeFare;

					}
				}

				if (tourModel.isPromoCodeApplied()) {

					estimateFareWithDiscount = estimateFareWithoutDiscount - tourModel.getPromoDiscount();

					if (estimateFareWithDiscount <= 0) {

						estimateFareWithDiscount = 0;
					}
				} else {

					estimateFareWithDiscount = estimateFareWithoutDiscount;
				}

				Map<String, Object> output = new HashMap<String, Object>();

				output.put("type", "SUCCESS");
				output.put("messages", surgePrice + "x " + messageForKey("surgeApplicableMessage", request));
				output.put("tourId", "");
				output.put("isSurgePriceApplied", isSurgePriceApplied);
				output.put("surgePriceId", surgePriceId);
				output.put("surgePrice", surgePrice);
				output.put("estimateFareWithoutDiscount", estimateFareWithoutDiscount);
				output.put("estimateFareWithDiscount", estimateFareWithDiscount);
				output.put("isAirportFixedFareApplied", tourModel.isAirportFixedFareApplied());
				output.put("airportBookingType", tourModel.getAirportBookingType());

				return sendDataResponse(output);
			}
		}

		boolean promoCodeValid = false;

		if (tourModel.isPromoCodeApplied()) {

			PromoCodeModel promoCode = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tourModel.getPromoCodeId());

			long currentTime = DateUtils.nowAsGmtMillisec();

			if (promoCode != null) {

				if (currentTime >= promoCode.getStartDate() && currentTime <= promoCode.getEndDate()) {

					UtilizedUserPromoCodeModel utilizedUserPromoCodeModel = UtilizedUserPromoCodeModel.getUtilizedUserPromoCodeDetailsByPromoCodeIdAndUserId(loggedInuserId, promoCode.getPromoCodeId());

					if (utilizedUserPromoCodeModel != null) {
						return sendBussinessError(messageForKey("errorUsedPromoCode", request));
					}

					if (promoCode.getUsageType().equalsIgnoreCase(ProjectConstants.ALL_ID)) {

						if (promoCode.getUsage().equalsIgnoreCase(ProjectConstants.UNLIMITED_ID)) {

							promoCodeValid = true;

						} else {

							if (promoCode.getUsedCount() < promoCode.getUsageCount()) {
								promoCodeValid = true;
							} else {
								return sendBussinessError(messageForKey("errorPromoCodeExpired", request));
							}
						}

					} else {

						UserPromoCodeModel userPromoCodeModel = UserPromoCodeModel.getUserPromoCodeDetailsByPromoCodeIdAndUserId(loggedInuserId, promoCode.getPromoCodeId());

						if (userPromoCodeModel != null) {
							promoCodeValid = true;
						} else {
							return sendBussinessError(messageForKey("errorInvalidPromoCode", request));
						}
					}

				} else {

					return sendBussinessError(messageForKey("errorPromoCodeExpired", request) + "1");
				}
			} else {

				return sendBussinessError(messageForKey("errorInvalidPromoCode", request));
			}

			long newPromoCodeCount = promoCode.getUsedCount() + 1;

			if (promoCode.getUsage().equalsIgnoreCase(ProjectConstants.UNLIMITED_ID)) {

				promoCodeValid = true;

				PromoCodeModel updatePromoCodeModel = new PromoCodeModel();

				updatePromoCodeModel.setPromoCodeId(promoCode.getPromoCodeId());
				updatePromoCodeModel.setUsedCount(newPromoCodeCount);

				updatePromoCodeModel.updatePromoCodeCount();

			} else {

				if ((promoCode.getUsedCount() < promoCode.getUsageCount()) && (newPromoCodeCount <= promoCode.getUsageCount())) {

					promoCodeValid = true;

					PromoCodeModel updatePromoCodeModel = new PromoCodeModel();

					updatePromoCodeModel.setPromoCodeId(promoCode.getPromoCodeId());
					updatePromoCodeModel.setUsedCount(newPromoCodeCount);

					updatePromoCodeModel.updatePromoCodeCount();

				} else {

					return sendBussinessError(messageForKey("errorPromoCodeExpired", request) + "3");
				}
			}
		}

		if (tourModel.isPromoCodeApplied()) {

			if (promoCodeValid) {

			} else {
				return sendBussinessError(messageForKey("errorInvalidPromoCode", request));
			}
		}

		UserModel userInfo = UserModel.getUserAccountDetailsById(loggedInuserId);

		tourModel.setPassengerId(loggedInuserId);
		tourModel.setTourId(UUIDGenerator.generateUUID());
		tourModel.setDriverId("-1");
		tourModel.setpFirstName(userInfo.getFirstName());
		tourModel.setpLastName(userInfo.getLastName());
		tourModel.setpEmail(userInfo.getEmail());
		tourModel.setpPhone(userInfo.getPhoneNo());
		tourModel.setpPhoneCode(userInfo.getPhoneNoCode());
		tourModel.setpPhotoUrl(userInfo.getPhotoUrl());
		tourModel.setStatus(ProjectConstants.TourStatusConstants.RIDE_LATER_NEW_TOUR);
		tourModel.setTourBookedBy(ProjectConstants.TOUR_BOOKED_BY_PASSENGER);

		if (userInfo.getUserRole().equalsIgnoreCase(UserRoles.PASSENGER_ROLE)) {
			tourModel.setBookingType(ProjectConstants.INDIVIDUAL_BOOKING);
		} else {
			tourModel.setBookingType(ProjectConstants.BUSINESS_OWNER_BOOKING);
		}

		tourModel.setSourceGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + "  " + tourModel.getsLatitude() + ")')");

		if (tourModel.isRentalBooking()) {

			tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getsLongitude() + "  " + tourModel.getsLatitude() + ")')");

		} else {

			tourModel.setDestinationGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + tourModel.getdLongitude() + "  " + tourModel.getdLatitude() + ")')");
		}

		tourModel.setRideLater(true);
		tourModel.setTourRideLater(true);

		if (tourModel.isPromoCodeApplied()) {

			if (!promoCodeValid) {

				tourModel.setPromoCodeId(null);
				tourModel.setPromoCodeApplied(false);

				return sendBussinessError(messageForKey("errorInvalidPromoCode", request));
			}

		} else {

			tourModel.setPromoCodeId(null);
			tourModel.setPromoCodeApplied(false);
		}

		lang = ProjectConstants.ENGLISH_ID;

		tourModel.setLanguage(lang);
		tourModel.setAcknowledged(false);

		if (!MultiTenantUtils.validateVendorCarType(tourModel.getCarTypeId(), headerVendorId, tourModel.getMulticityCityRegionId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.TRANSPORTATION_ID)) {
			return sendBussinessError(messageForKey("errorInvalidCarTypeForVendorAndRegion", request));
		}

		String tourId = tourModel.createTourRideLater(loggedInuserId);

		if (tourId != null) {

			TourTimeModel tourTimeModel = new TourTimeModel();
			tourTimeModel.setTourTimeId(UUIDGenerator.generateUUID());
			tourTimeModel.setTourId(tourId);
			tourTimeModel.setBookingTime(DateUtils.nowAsGmtMillisec());
			tourTimeModel.createTourTime();

			Map<String, Object> output = new HashMap<String, Object>();

			output.put("tourId", tourId);
			output.put("type", "SUCCESS");
			output.put("messages", messageForKey("successRequestSent", request));
			output.put("isAirportFixedFareApplied", tourModel.isAirportFixedFareApplied());
			output.put("airportBookingType", tourModel.getAirportBookingType());
			
			if(apnsDeviceModel != null) {
				apnsDeviceModel.sendFCMNotification("1", "Push", "scheduled", messageForKey("tripScheduledConfirmation", request));
			}
			
			return sendDataResponse(output);

		} else {
			return sendBussinessError(messageForKey("errorFailedSendRequest", request));
		}
	}

	@Path("/booking-list/{status}/{start}/{length}")
	@GET
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getRideLaterBookingList(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@PathParam("status") String status,
			@PathParam("start") long start,
			@PathParam("length") long length
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		List<TourModel> tourList = new ArrayList<TourModel>();

		UserModel user = UserModel.getUserActiveDeativeDetailsById(loggedInuserId);

		if (user.getUserRole().equals(UserRoles.PASSENGER_ROLE) || user.getUserRole().equals(UserRoles.BUSINESS_OWNER_ROLE) || user.getUserRole().equals(UserRoles.OPERATOR_ROLE)) {

			if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_PENDING)) {

			} else if (status.equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_ADVANCED)) {

			}

			tourList = TourModel.getRideLaterToursByPassengerIdPagination(loggedInuserId, start, length);
		} else {
			tourList = TourModel.getRideLaterToursByDriverIdPagination(loggedInuserId, start, length);
		}

		List<Map<String, Object>> outerOutPutMap = setTourListForBookings(tourList, user);

		if (tourList.size() != 0) {
			Map<Object, Object> res = new HashMap<Object, Object>();
			res.put("tourList", outerOutPutMap);
			return sendDataResponse(res);
		} else {
			return sendBussinessError(messageForKey("errorNoDataAvailableInvoice", request));
		}
	}

	public static List<Map<String, Object>> setTourListForBookings(List<TourModel> tourList, UserModel user) {

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();
		List<Map<String, Object>> outerOutPutMap = new ArrayList<Map<String, Object>>();

		for (TourModel tourModel : tourList) {

			Map<String, Object> outPutMap = new HashMap<String, Object>();

			outPutMap.put("tourId", tourModel.getTourId());
			outPutMap.put("userTourId", tourModel.getUserTourId());
			outPutMap.put("passengerId", tourModel.getPassengerId());

			outPutMap.put("driverId", tourModel.getDriverId());

			outPutMap.put("distance", tourModel.getDistance());
			outPutMap.put("charges", tourModel.getCharges());
			outPutMap.put("paymentType", tourModel.getPaymentType());

			outPutMap.put("isRideLater", tourModel.isRideLater());
			outPutMap.put("rideLaterPickupTime", tourModel.isRideLater() ? tourModel.getRideLaterPickupTime() : tourModel.getCreatedAt());
			outPutMap.put("rideLaterPickupTimeLogs", tourModel.getRideLaterPickupTimeLogs());

			if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR)) {

				outPutMap.put("isInvoice", true);

			} else if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) || tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {

				if (InvoiceModel.getInvoiceByTourId(tourModel.getTourId()) != null) {
					outPutMap.put("isInvoice", true);
				} else {
					outPutMap.put("isInvoice", false);
				}
			} else {
				outPutMap.put("isInvoice", false);
			}

			outPutMap.put("dateTime", String.valueOf(tourModel.getCreatedAt()));

			if (user != null && user.getUserRole().equals(UserRoles.DRIVER_ROLE)) {

				outPutMap.put("firstName", tourModel.getpFirstName());
				outPutMap.put("lastName", tourModel.getpLastName());
				outPutMap.put("email", tourModel.getpEmail());
				outPutMap.put("phone", tourModel.getpPhone());
				outPutMap.put("phoneCode", tourModel.getpPhoneCode());
				outPutMap.put("photoUrl", tourModel.getpPhotoUrl());

			} else {
				if (!tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {
					outPutMap.put("firstName", tourModel.getFirstName());
					outPutMap.put("lastName", tourModel.getLastName());
					outPutMap.put("email", tourModel.getEmail());
					outPutMap.put("phone", tourModel.getPhoneNo());
					outPutMap.put("phoneCode", tourModel.getPhoneNoCode());
					outPutMap.put("photoUrl", tourModel.getPhotoUrl());
				} else {
					outPutMap.put("firstName", "");
					outPutMap.put("lastName", "");
					outPutMap.put("email", "");
					outPutMap.put("phone", "");
					outPutMap.put("phoneCode", "");
					outPutMap.put("photoUrl", "");
				}
			}

			outPutMap.put("carTypeId", tourModel.getCarTypeId());
			outPutMap.put("sourceLatitude", tourModel.getsLatitude());
			outPutMap.put("sourceLongitude", tourModel.getsLongitude());
			outPutMap.put("destinationLatitude", tourModel.getdLatitude());
			outPutMap.put("destinationLongitude", tourModel.getdLongitude());

			outPutMap.put("initialFare", tourModel.getInitialFare());
			outPutMap.put("perKmFare", tourModel.getPerKmFare());
			outPutMap.put("perMinuteFare", tourModel.getPerMinuteFare());
			outPutMap.put("bookingFees", tourModel.getBookingFees());
			outPutMap.put("minimumFare", tourModel.getMinimumFare());
			outPutMap.put("discount", tourModel.getDiscount());

			outPutMap.put("promoCodeApplied", tourModel.isPromoCodeApplied());
			outPutMap.put("total", tourModel.getTotal());
			outPutMap.put("promoDiscount", tourModel.getPromoDiscount());
			outPutMap.put("usedCredits", tourModel.getUsedCredits());

			if (tourModel.isPromoCodeApplied()) {

				PromoCodeModel promoCodeModel = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tourModel.getPromoCodeId());

				if (promoCodeModel != null) {
					outPutMap.put("promoCodeId", promoCodeModel.getPromoCodeId());
					outPutMap.put("promoCode", promoCodeModel.getPromoCode());
					outPutMap.put("usage", promoCodeModel.getUsage());
					outPutMap.put("usageCount", promoCodeModel.getUsageCount());
					outPutMap.put("mode", promoCodeModel.getMode());
					outPutMap.put("promoCodeDiscount", promoCodeModel.getDiscount());
					outPutMap.put("startDate", promoCodeModel.getStartDate());
					outPutMap.put("usedCount", promoCodeModel.getUsedCount());
					outPutMap.put("endDate", promoCodeModel.getEndDate());
				} else {
					outPutMap.put("promoCodeId", "-");
					outPutMap.put("promoCode", "-");
					outPutMap.put("usage", "-");
					outPutMap.put("usageCount", "-");
					outPutMap.put("mode", "-");
					outPutMap.put("promoCodeDiscount", "-");
					outPutMap.put("startDate", "-");
					outPutMap.put("usedCount", "-");
					outPutMap.put("endDate", "-");
				}
			} else {

				outPutMap.put("promoCodeId", "-");
				outPutMap.put("promoCode", "-");
				outPutMap.put("usage", "-");
				outPutMap.put("usageCount", "-");
				outPutMap.put("mode", "-");
				outPutMap.put("promoCodeDiscount", "-");
				outPutMap.put("startDate", "-");
				outPutMap.put("usedCount", "-");
				outPutMap.put("endDate", "-");
			}

			outPutMap.put("sourceAddress", tourModel.getSourceAddress());
			outPutMap.put("destinationAddress", tourModel.getDestinationAddress());
			outPutMap.put("status", tourModel.getStatus());

			// For rental
			outPutMap.put("isRentalBooking", tourModel.isRentalBooking());

			Map<String, Object> rentalPackageDetails = new HashMap<String, Object>();

			if (tourModel.isRentalBooking()) {

				outPutMap.put("rentalPackageId", tourModel.getRentalPackageId());
				outPutMap.put("rentalPackageTime", tourModel.getRentalPackageTime());

				rentalPackageDetails.put("carTypeId", tourModel.getCarTypeId());
				rentalPackageDetails.put("baseFare", df_new.format(tourModel.getInitialFare()));
				rentalPackageDetails.put("additionalPerKmFare", df_new.format(tourModel.getPerKmFare()));
				rentalPackageDetails.put("additionalPerMinuteFare", df_new.format(tourModel.getPerMinuteFare()));

				if (tourModel.getRentalPackageTime() == 1) {

					rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hour");

				} else {

					rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hours");
				}

				rentalPackageDetails.put("packageDistance", df_new.format(((tourModel.getFreeDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());

				outPutMap.put("rentalPackageDetails", rentalPackageDetails);

			} else {

				outPutMap.put("rentalPackageId", "-1");
				outPutMap.put("rentalPackageTime", 0);
				outPutMap.put("rentalPackageDetails", rentalPackageDetails);
			}
			// ------------------------------------------------------------------------------

			outerOutPutMap.add(outPutMap);
		}

		return outerOutPutMap;
	}

	@Path("/driver/accept/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response acceptTour(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader, 
		@PathParam("tourId") String tourId) {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

		RideLaterSettingsModel rideLaterSettingsModel = RideLaterSettingsModel.getRideLaterSettingsDetails();

		if (ProjectConstants.TourStatusConstants.EXPIRED_TOUR.equalsIgnoreCase(tourModel.getStatus())) {
			return sendBussinessError(messageForKey("labelTourExpired", request));
		}

		if (tourModel.getDriverId().equals(loggedInuserId) && !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) && !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {

			if (tourModel.getRideLaterPickupTime() <= DateUtils.nowAsGmtMillisec()) {

				if (tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR) || tourModel.getStatus().equalsIgnoreCase(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR)) {

					TourModel tour4 = new TourModel();
					tour4.setTourId(tourModel.getTourId());
					tour4.setDriverId(tourModel.getDriverId());
					tour4.setStatus(ProjectConstants.TourStatusConstants.EXPIRED_TOUR);

					tour4.updateTourStatusByTourId();

					TourModel tour1 = new TourModel();
					tour1.setTourId(tourModel.getTourId());
					tour1.setDriverId(tourModel.getDriverId());
					tour1.setTourRideLater(false);

					tour1.updateRideLaterTourFlag();

					TourUtils.updateTourCriticalStatus(tourModel, false);

					return sendBussinessError(messageForKey("labelTourAlreadyExpired", request));
				}
			}

			DriverTourRequestModel driverTourRequestModel = DriverTourRequestModel.getTourRequestByDriverIdAndTourId(loggedInuserId, tourId);
			long currentTime = DateUtils.nowAsGmtMillisec();
			long driverTourRequestedTime = driverTourRequestModel.getCreatedAt();

			if ((currentTime - driverTourRequestedTime) > (rideLaterSettingsModel.getDriverJobRequestTime() * ProjectConstants.RIDE_LATER_CRON_JOB_TIME_1MIN)) {

				TourUtils.assignTourDriver(tourId, ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR, loggedInuserId);

				return sendBussinessError(messageForKey("errorTripHasExiredAnAssignedToAnotherDriver", request));
			}

			driverAcceptsRideLaterRequest(tourId, loggedInuserId, tourModel);

			return sendSuccessMessage(messageForKey("successTripAccepted", request));

		} else if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {
			String errorMessage = String.format(messageForKey("errorTripCancelledByPassenger", request), MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
			return sendBussinessError(errorMessage);
		} else {

			return sendBussinessError(messageForKey("errorTripAssignedToOtherDriver", request));
		}
	}

	public static void driverAcceptsRideLaterRequest(String tourId, String driverId, TourModel tourModel) {

		TourUtils.updateTourStatusByTourId(tourId, driverId, ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST);

		TourUtils.updateTourCarIdByTourId(tourId, driverId);

		TourTimeModel tourTimeModel = new TourTimeModel();
		tourTimeModel.setTourId(tourId);
		tourTimeModel.setAcceptTime(DateUtils.nowAsGmtMillisec());
		tourTimeModel.updateTourAcceptedTime();

		if (tourModel.getBookingType().equals(ProjectConstants.INDIVIDUAL_BOOKING)) {

			String message = BusinessAction.messageForKeyAdmin("successDriverAcceptedTrip", tourModel.getLanguage());

			ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourModel.getPassengerId());

			ApnsMessageModel apnsMessage = new ApnsMessageModel();
			apnsMessage.setMessage(message);
			apnsMessage.setMessageType("push");
			apnsMessage.setToUserId(tourModel.getPassengerId());
			apnsMessage.insertPushMessage();

			AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

			if (adminSmsSendingModel.ispAcceptByDriver()) {
				MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourModel.getpPhoneCode() + tourModel.getpPhone().trim(), ProjectConstants.SMSConstants.SMS_RIDE_LATER_TEMPLATE_ID);
			}

			if (apnsDevice != null) {
				apnsDevice.sendNotification("1", "Push", message, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, WebappPropertyUtils.getWebAppProperty("certificatePath"));
			}
		}
	}

	@Path("/driver/cancel/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response cancelTour(
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@PathParam("tourId") String tourId
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		TourModel tour = TourModel.getTourDetailsByTourId(tourId);

		if (tour.getDriverId().equals(loggedInuserId)) {

			if (!tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {

				if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST)) {

					TourUtils.assignTourDriver(tourId, ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR, loggedInuserId);

					TourUtils.updateTourCriticalStatus(tour, true);
					
					ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tour.getPassengerId());
					if(apnsDevice != null) {
						apnsDevice.sendFCMNotification("1", "Push", ProjectConstants.CANCELLED, messageForKey("successTripCancelled", request));
					}

					return sendSuccessMessage(messageForKey("successTripCancelled", request));

				} else {

					return sendSuccessMessage(messageForKey("errorTripAssignedToOtherDriver", request));
				}

			} else {

				return sendSuccessMessage(messageForKey("successPassengerCancelledTrip", request));
			}

		} else {

			return sendSuccessMessage(messageForKey("errorTripAssignedToOtherDriver", request));
		}
	}

	@Path("/passenger/cancel/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response cancelTripRequest(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@PathParam("tourId") String tourId
			) throws SQLException, IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		TourModel tour = TourModel.getTourDetailsByTourId(tourId);
		TourModel tourDetails = TourModel.getTourDetailsByTourId(tourId);

		String driverId = tour.getDriverId();

		boolean directCancelTripWithoutPayment = false;

		//@formatter:off
		if (!tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.STARTED_TOUR) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.ENDED_TOUR) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER) && 
		    !tour.getStatus().equals(ProjectConstants.TourStatusConstants.EXPIRED_TOUR)) {
		//@formatter:on

			PromoCodeUtils.promoCodeCancel(tourDetails, false);

			TourModel tour1 = new TourModel();
			tour1.setTourId(tour.getTourId());
			tour1.setDriverId(tour.getDriverId());
			tour1.setTourRideLater(false);

			tour1.updateRideLaterTourFlag();

			TourUtils.updateTourCriticalStatus(tour, false);

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_ASSIGNED_TOUR) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_REASSIGNED_TOUR)) {

				tour = new TourModel();
				tour.setUpdatedAt(DateUtils.nowAsGmtMillisec());
				tour.setUpdatedBy(loggedInuserId);
				tour.setTourId(tourId);
				tour.setStatus(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER);
				tour.updateTourStatusByPassenger(loggedInuserId);
			}

			if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.RIDE_LATER_ACCEPTED_REQUEST)) {

				InvoiceModel invoice = new InvoiceModel();

				invoice.setTourId(tourId);
				invoice.setInitialFare(tourDetails.getInitialFare());
				invoice.setPerKmFare(tourDetails.getPerKmFare());
				invoice.setPerMinuteFare(tourDetails.getPerMinuteFare());
				invoice.setBookingFees(tourDetails.getBookingFees());
				invoice.setDiscount(tourDetails.getDiscount());
				invoice.setMinimumFare(tourDetails.getMinimumFare());

				invoice.setDistance(tour.getDistance());
				invoice.setPromoDiscount(0);
				invoice.setPromoCodeApplied(false);
				invoice.setPromoCodeId(null);
				invoice.setUsedCredits(0);

				if (directCancelTripWithoutPayment) {

					invoice.setPaymentMode(ProjectConstants.CASH);
					invoice.setTotal(0);
					invoice.setFine(0);
					invoice.setCharges(0);

				} else {

					invoice.setTotal(CancellationChargeModel.getAdminCancellationCharges().getCharge());
					invoice.setFine(CancellationChargeModel.getAdminCancellationCharges().getCharge());
					invoice.setCharges(CancellationChargeModel.getAdminCancellationCharges().getCharge());

					//@formatter:off					
					if ((tour.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING)) 
							|| (tour.getBookingType().equals(ProjectConstants.ADMIN_BOOKING))) {
					//@formatter:on

					} else {

						// payment mode

						UserProfileModel userProfile = UserProfileModel.getAdminUserAccountDetailsById(tourDetails.getPassengerId());
						userProfile.setCredit(userProfile.getCredit() - invoice.getFine());
						userProfile.updateUserCredits();

						invoice.setPaymentMode(ProjectConstants.CASH);
						invoice.setCard(false);
						invoice.setCashCollected(0);
						invoice.setCashToBeCollected(0);
						invoice.setCashNotReceived(true);
					}
				}

				invoice.generateInvoice(loggedInuserId, false);
			}

			tour = new TourModel();
			tour.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			tour.setUpdatedBy(loggedInuserId);
			tour.setTourId(tourId);
			tour.setStatus(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER);
			int status = tour.updateTourStatusByPassenger(loggedInuserId);

			if (status > 0) {

				if (!driverId.equalsIgnoreCase("-1")) {

					AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

					String message = String.format(messageForKey("errorTripCancelledByPassenger", request), MyHubUtils.formatFullName(tourDetails.getpFirstName(), tourDetails.getpLastName()));

					ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(driverId);

					ApnsMessageModel apnsMessage = new ApnsMessageModel();
					apnsMessage.setMessage(message);
					apnsMessage.setMessageType("push");
					apnsMessage.setToUserId(driverId);
					apnsMessage.insertPushMessage();

					if ((tour.getpPhone() != null) && (tour.getpPhoneCode() != null)) {

						if (adminSmsSendingModel.isdCancelledByPassengerBusinessO()) {
							MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tour.getpPhoneCode() + tour.getpPhone().trim(), ProjectConstants.SMSConstants.SMS_CANCELLED_BY_PASSENGER_TEMPLATE_ID);
						}
					}

					if (apnsDevice != null) {
						apnsDevice.sendNotification("1", "Push", message, ProjectConstants.DRIVER_APPLICATION_ACCEPTED, WebappPropertyUtils.getWebAppProperty("certificatePath"));
					}
				}

				return sendSuccessMessage(messageForKey("successTripCancelled", request));

			} else {

				return sendBussinessError(messageForKey("errorTripCancelled", request));
			}
		} else {
			return sendSuccessMessage(messageForKey("errorTripCantBeCancelled", request));
		}
	}

	@Path("/driver/acknowledge/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response acknowledgeTour(
		@Context HttpServletRequest request,
		@Context HttpServletResponse response, 
		@HeaderParam("x-language-code") String lang,
		@HeaderParam("x-vendor-id") String headerVendorId,
		@HeaderParam("x-api-key") String sessionKeyHeader, 
		@PathParam("tourId") String tourId) {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

		if (ProjectConstants.TourStatusConstants.EXPIRED_TOUR.equalsIgnoreCase(tourModel.getStatus())) {
			return sendBussinessError(messageForKey("labelTourExpired", request));
		}

		if (tourModel.getDriverId().equals(loggedInuserId) && !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) && !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {

			tourModel.setAcknowledged(true);
			tourModel.updateRideLaterTourAcknowledgeByTourId();
			
			ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourModel.getPassengerId());
			if (apnsDevice != null) {
				apnsDevice.sendFCMNotification("1", "Push", ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST, messageForKey("acceptedScheduleTrip", request) );
			}
			
			
			return sendSuccessMessage(messageForKey("sucessRideLaterTourAcknowledge", request));

		} else if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {

			String errorMessage = String.format(messageForKey("errorTripCancelledByPassenger", request), MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
			return sendBussinessError(errorMessage);

		} else {

			return sendBussinessError(messageForKey("errorTripAssignedToOtherDriver", request));
		}
	}

	private void sendDriverNotification(String driverId, String tourId) {

		ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(driverId);
		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);
		CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(tourModel.getCarTypeId());

		AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

		Map<String, Object> outPutMap = new HashMap<String, Object>();
		outPutMap.put("tourId", tourModel.getTourId());
		outPutMap.put("userTourId", tourModel.getUserTourId());
		outPutMap.put("passengerId", tourModel.getPassengerId());
		outPutMap.put("driverId", tourModel.getDriverId());
		outPutMap.put("dateTime", String.valueOf(tourModel.getCreatedAt()));
		outPutMap.put("firstName", tourModel.getpFirstName());
		outPutMap.put("lastName", tourModel.getpLastName());
		outPutMap.put("email", tourModel.getpEmail());
		outPutMap.put("phone", tourModel.getpPhone());
		outPutMap.put("phoneCode", tourModel.getpPhoneCode());
		outPutMap.put("dPhotoUrl", tourModel.getPhotoUrl());

		double rating = 0.0;

		List<DriverTripRatingsModel> ratingList = DriverTripRatingsModel.getAllPassangerRatings(tourModel.getPassengerId());

		for (DriverTripRatingsModel paRating : ratingList) {
			rating = rating + paRating.getRate();
		}

		DecimalFormat df = new DecimalFormat("#.#");

		if (rating != 0.0) {
			rating = (double) rating / ratingList.size();
			outPutMap.put("rating", df.format(rating));
		} else {
			outPutMap.put("rating", -1);
		}

		outPutMap.put("sourceLatitude", tourModel.getsLatitude());
		outPutMap.put("sourceLongitude", tourModel.getsLongitude());
		outPutMap.put("destinationLatitude", tourModel.getdLatitude());
		outPutMap.put("destinationLongitude", tourModel.getdLongitude());

		outPutMap.put("sourceAddress", tourModel.getSourceAddress());
		outPutMap.put("destinationAddress", tourModel.getDestinationAddress());
		outPutMap.put("status", tourModel.getStatus());
		outPutMap.put("updatedAt", tourModel.getUpdatedAt());

		outPutMap.put("initialFare", tourModel.getInitialFare());
		outPutMap.put("perKmFare", tourModel.getPerKmFare());
		outPutMap.put("perMinuteFare", tourModel.getPerMinuteFare());
		outPutMap.put("bookingFees", tourModel.getBookingFees());
		outPutMap.put("minimumFare", tourModel.getMinimumFare());
		outPutMap.put("discount", tourModel.getDiscount());

		outPutMap.put("cancellationCharges", CancellationChargeModel.getAdminCancellationCharges().getCharge());

		outPutMap.put("jobExpireTime", ProjectConstants.JOB_EXPIRE_TIME);

		outPutMap.put("isRideLater", tourModel.isRideLater());
		outPutMap.put("isFixedFare", tourModel.isFixedFare());
		outPutMap.put("promoCodeApplied", tourModel.isPromoCodeApplied());
		outPutMap.put("total", tourModel.getTotal());
		outPutMap.put("promoDiscount", tourModel.getPromoDiscount());
		outPutMap.put("usedCredits", tourModel.getUsedCredits());

		if (tourModel.isPromoCodeApplied()) {

			PromoCodeModel promoCodeModel = PromoCodeModel.getPromoCodeDetailsByPromoCodeId(tourModel.getPromoCodeId());

			if (promoCodeModel != null) {
				outPutMap.put("promoCodeId", promoCodeModel.getPromoCodeId());
				outPutMap.put("promoCode", promoCodeModel.getPromoCode());
				outPutMap.put("usage", promoCodeModel.getUsage());
				outPutMap.put("usageCount", promoCodeModel.getUsageCount());
				outPutMap.put("mode", promoCodeModel.getMode());
				outPutMap.put("promoCodeDiscount", promoCodeModel.getDiscount());
				outPutMap.put("startDate", promoCodeModel.getStartDate());
				outPutMap.put("usedCount", promoCodeModel.getUsedCount());
				outPutMap.put("endDate", promoCodeModel.getEndDate());
			} else {
				outPutMap.put("promoCodeId", "-");
				outPutMap.put("promoCode", "-");
				outPutMap.put("usage", "-");
				outPutMap.put("usageCount", "-");
				outPutMap.put("mode", "-");
				outPutMap.put("promoCodeDiscount", "-");
				outPutMap.put("startDate", "-");
				outPutMap.put("usedCount", "-");
				outPutMap.put("endDate", "-");
			}
		} else {

			outPutMap.put("promoCodeId", "-");
			outPutMap.put("promoCode", "-");
			outPutMap.put("usage", "-");
			outPutMap.put("usageCount", "-");
			outPutMap.put("mode", "-");
			outPutMap.put("promoCodeDiscount", "-");
			outPutMap.put("startDate", "-");
			outPutMap.put("usedCount", "-");
			outPutMap.put("endDate", "-");
		}

		outPutMap.put("distance", tourModel.getDistance());
		outPutMap.put("charges", tourModel.getCharges());
		outPutMap.put("paymentType", tourModel.getPaymentType());

		if (tourModel.isRideLater()) {
			outPutMap.put("dateTime", tourModel.getRideLaterPickupTime());
		} else {
			outPutMap.put("dateTime", tourModel.getCreatedAt());
		}

		outPutMap.put("isAcknowledged", tourModel.isAcknowledged());

		outPutMap.put("carTypeId", tourModel.getCarTypeId());

		if (carTypeModel != null) {

			outPutMap.put("carType", carTypeModel.getCarType());

		} else {

			outPutMap.put("carType", "");
		}

		outPutMap.put("isSurgePriceApplied", tourModel.isSurgePriceApplied());

		if (tourModel.isSurgePriceApplied()) {

			outPutMap.put("surgePriceId", tourModel.getSurgePriceId());
			outPutMap.put("surgePrice", tourModel.getSurgePrice());
			outPutMap.put("totalWithSurge", tourModel.getTotalWithSurge());

		} else {

			outPutMap.put("surgePriceId", "-1");
			outPutMap.put("surgePrice", 1);
			outPutMap.put("totalWithSurge", 0);
		}

		// For rental
		outPutMap.put("isRentalBooking", tourModel.isRentalBooking());

		Map<String, Object> rentalPackageDetails = new HashMap<String, Object>();

		if (tourModel.isRentalBooking()) {

			outPutMap.put("rentalPackageId", tourModel.getRentalPackageId());
			outPutMap.put("rentalPackageTime", tourModel.getRentalPackageTime());

			rentalPackageDetails.put("carTypeId", tourModel.getCarTypeId());
			rentalPackageDetails.put("baseFare", df_new.format(tourModel.getInitialFare()));
			rentalPackageDetails.put("additionalPerKmFare", df_new.format(tourModel.getPerKmFare()));
			rentalPackageDetails.put("additionalPerMinuteFare", df_new.format(tourModel.getPerMinuteFare()));

			if (tourModel.getRentalPackageTime() == 1) {

				rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hour");

			} else {

				rentalPackageDetails.put("packageTime", tourModel.getRentalPackageTime() + " Hours");
			}

			rentalPackageDetails.put("packageDistance", df_new.format(((tourModel.getFreeDistance()) / adminSettingsModel.getDistanceUnits())) + " " + adminSettingsModel.getDistanceType().toUpperCase());

			outPutMap.put("rentalPackageDetails", rentalPackageDetails);

		} else {

			outPutMap.put("rentalPackageId", "-1");
			outPutMap.put("rentalPackageTime", 0);
			outPutMap.put("rentalPackageDetails", rentalPackageDetails);
		}
		// ------------------------------------------------------------------------------

		CourierUtils.formCourierData(tourModel, outPutMap);

		OrderUtils.formOrdersNGBData(null, outPutMap);

		JSONObject jsonMessage = new JSONObject(outPutMap);

		String messge = "TOUR`1`" + apnsDevice.getDeviceToken() + ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.NGB_SOCKET + jsonMessage.toString();

		WebSocketClient.sendDriverNotification(messge, driverId, apnsDevice.getApiSessionKey());

		UserModel userModel = UserModel.getUserAccountDetailsById(driverId);

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

		String message = BusinessAction.messageForKeyAdmin("successNewJobMessage", adminSmsSendingModel.getLanguage());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(message);
		apnsMessage.setMessageType("socket");
		apnsMessage.setToUserId(driverId);
		apnsMessage.insertPushMessage();

		if (adminSmsSendingModel.isdBookingRequest()) {
			MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, userModel.getPhoneNoCode() + userModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_NEW_JOB_TEMPLATE_ID);
		}

	}

	private List<String> CarBookModelValidtion(TourModel tourModel) throws IOException {

		Validator validator = new Validator();

		validator.addValidationMapping(SOURCE_LATITUDE, SOURCE_LATITUDE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(SOURCE_LONGITUDE, SOURCE_LONGITUDE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(CAR_TYPE_ID, CAR_TYPE_ID_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(SOURCE_ADDRESS, SOURCE_ADDRESS_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(DISTANCE, DISTANCE_LABEL, new RequiredValidationRule());
		validator.addValidationMapping(CHARGES, CHARGES_LABEL, new RequiredValidationRule());
		validator.addValidationMapping("rideLaterPickupTimeLogs", "Pickup time", new RequiredValidationRule());

		if (tourModel.getCarTypeId().equals("5")) {
			validator.addValidationMapping(TRANSMISSION_TYPE_ID, TRANSMISSION_TYPE_LABEL, new RequiredValidationRule());
		}
		if (!tourModel.isRentalBooking()) {

			validator.addValidationMapping(DESTINATION_LATITUDE, DESTINATION_LATITUDE_LABEL, new RequiredValidationRule());
			validator.addValidationMapping(DESTINATION_LONGITUDE, DESTINATION_LONGITUDE_LABEL, new RequiredValidationRule());
			validator.addValidationMapping(DESTINATION_ADDRESS, DESTINATION_ADDRESS_LABEL, new RequiredValidationRule());
		}

		validator.addValidationMapping(IS_AIRPORT_FIXED_FARE_APPLIED, IS_AIRPORT_FIXED_FARE_APPLIED_LABEL, new RequiredValidationRule());

		errorMessages = validator.validate(tourModel);

		return errorMessages;
	}

}