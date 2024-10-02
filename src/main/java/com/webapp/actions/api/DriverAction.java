package com.webapp.actions.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.jeeutils.DateUtils;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.DriverSubscriptionUtils;
import com.utils.myhub.DriverTourRequestUtils;
import com.utils.myhub.DriverTourStatusUtils;
import com.utils.myhub.MultiTenantUtils;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.PromoCodeUtils;
import com.utils.myhub.TourUtils;
import com.utils.myhub.WebappPropertyUtils;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.BusinessApiAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.AdminSmsSendingModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.CancellationChargeModel;
import com.webapp.models.CarModel;
import com.webapp.models.CarTypeModel;
import com.webapp.models.DriverDutyLogsModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverInfoModel;
import com.webapp.models.DriverLoggedInTimeModel;
import com.webapp.models.DriverSubscriberModel;
import com.webapp.models.DriverSubscriptionPackageHistoryModel;
import com.webapp.models.DriverTripRatingsModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.FreeWaitingTimeModel;
import com.webapp.models.InvoiceModel;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.TaxModel;
import com.webapp.models.TourModel;
import com.webapp.models.TourTaxModel;
import com.webapp.models.TourTimeModel;
import com.webapp.models.UserAccountModel;
import com.webapp.models.UserModel;
import com.webapp.models.UserProfileModel;

@Path("/api/driver")
public class DriverAction extends BusinessApiAction {

	@Path("/accept/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response acceptTour(@Context HttpServletRequest request,
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

		Map<String, Object> outputMap = TourUtils.acceptTourByDriver(request, headerVendorId, tourId, loggedInuserId, false);

		if (outputMap.get(ProjectConstants.STATUS_TYPE).toString().equalsIgnoreCase(ProjectConstants.STATUS_ERROR)) {
			return sendBussinessError(outputMap.get(ProjectConstants.STATUS_MESSAGE).toString());
		} else {
			return sendDataResponse(outputMap);
		}
	}

	@Path("/arrived-waiting/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response arrivedAndWaitingTour(@Context HttpServletRequest request,
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

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();
		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

		//@formatter:off
		if (tourModel.getDriverId().equals(loggedInuserId) 
				&& !tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER) 
				&& tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)) {
		//@formatter:on

			TourUtils.updateTourStatusByTourId(tourId, loggedInuserId, ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR);

			if (!tourModel.isRentalBooking()) {

				DriverInfoModel driverInfoModel = DriverInfoModel.getActiveDeactiveDriverAccountDetailsById(tourModel.getDriverId());
				double driverAmount = (driverInfoModel.getDriverPayablePercentage() * (tourModel.getTotal())) / 100;
				double percentage = driverInfoModel.getDriverPayablePercentage();
				tourModel.setDriverAmount(driverAmount);
				tourModel.setPercentage(percentage);
				tourModel.updateDriverPercentageByTourId();
			}

			TourTimeModel tourTimeModel = new TourTimeModel();
			tourTimeModel.setTourId(tourId);
			tourTimeModel.setArrivedWaitingTime(DateUtils.nowAsGmtMillisec());
			tourTimeModel.updateTourArrivedWaitingTime();

			TourModel tourDetails = TourModel.getTourDetailsByTourId(tourId);

			String message = BusinessAction.messageForKeyAdmin("successArrivedWaiting", tourModel.getLanguage());

			if (tourDetails.getBookingType().equals(ProjectConstants.INDIVIDUAL_BOOKING)) {

				ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetails.getPassengerId());

				ApnsMessageModel apnsMessage = new ApnsMessageModel();
				apnsMessage.setMessage(message);
				apnsMessage.setMessageType("push");
				apnsMessage.setToUserId(tourDetails.getPassengerId());
				apnsMessage.insertPushMessage();

				if (adminSmsSendingModel.ispArrivedAndWaiting()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetails.getpPhoneCode() + tourDetails.getpPhone().trim(), ProjectConstants.SMSConstants.SMS_DRIVER_ARRIVED_TEMPLATE_ID);
				}

				if (apnsDevice != null) {

					if (tourModel.getBookingType().equalsIgnoreCase(ProjectConstants.INDIVIDUAL_BOOKING)) {
						// new PassangerNotificationThread(apnsDevice.getDeviceToken(), tourId,
						// apnsDevice.getApiSessionKey());
					}

					//apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR, WebappPropertyUtils.getWebAppProperty("certificatePath"));
					apnsDevice.sendFCMNotification("1", "Push", ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR, message);
				}

			} else {

				if (adminSmsSendingModel.ispAcceptByDriver()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetails.getpPhoneCode() + tourDetails.getpPhone(), ProjectConstants.SMSConstants.SMS_DRIVER_ARRIVED_TEMPLATE_ID);
				}

				if (adminSmsSendingModel.isBoAccepted()) {

					UserModel user = UserModel.getUserAccountDetailsById(tourDetails.getCreatedBy());

					String businessOwnermessage = String.format(BusinessAction.messageForKeyAdmin("driver_arrived_2", tourDetails.getLanguage()), user.getFirstName(), tourDetails.getpFirstName() + " " + tourDetails.getpLastName());

					if (user != null) {
						MetamorphSystemsSmsUtils.sendSmsToSingleUser(businessOwnermessage, user.getPhoneNoCode() + user.getPhoneNo(), ProjectConstants.SMSConstants.SMS_WAITING_TEMPLATE_ID);
					}
				}

				ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetails.getPassengerId());

				ApnsMessageModel apnsMessage = new ApnsMessageModel();
				apnsMessage.setMessage(message);
				apnsMessage.setMessageType("push");
				apnsMessage.setToUserId(tourDetails.getPassengerId());
				apnsMessage.insertPushMessage();

				if (adminSmsSendingModel.ispArrivedAndWaiting()) {
					MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetails.getpPhoneCode() + tourDetails.getpPhone().trim(), ProjectConstants.SMSConstants.SMS_DRIVER_ARRIVED_TEMPLATE_ID);
				}

				if (apnsDevice != null) {
					// apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR, WebappPropertyUtils.getWebAppProperty("certificatePath"));
					apnsDevice.sendFCMNotification("1", "Push", ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR, message);
				}
			}

			return sendSuccessMessage(messageForKey("successArrivedWaiting", request));

		} else if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {
			String errorMessage = String.format(messageForKey("errorTripCancelledByPassenger", request), MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
			return sendBussinessError(errorMessage);
		} else {

			return sendBussinessError(messageForKey("errorTripAssignedToOtherDriver", request));
		}
	}

	@Path("/cancel/{tourId}")
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
			@PathParam("tourId") String tourId) throws SQLException,
			IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		TourModel tour = TourModel.getTourDetailsByTourId(tourId);
		TourModel tourDetails = TourModel.getTourDetailsByTourId(tourId);

		AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

		if (tour.getDriverId().equals(loggedInuserId)) {

			if (!tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {

				if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

					DriverTourRequestUtils.updateDriverTourRequest(tourId, tour.getDriverId(), ProjectConstants.TourStatusConstants.REJECTED_TOUR);

					TourUtils.assignTourDriver(tourId, ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.ASSIGNED_TOUR, loggedInuserId);

					DriverTourStatusUtils.updateDriverTourStatus(loggedInuserId, ProjectConstants.DRIVER_FREE_STATUS);

					PromoCodeUtils.promoCodeCancel(tourDetails, true);

					return sendSuccessMessage(messageForKey("successTripCancelled", request));

				} else if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {

					PromoCodeUtils.promoCodeCancel(tourDetails, true);

					TourUtils.updateTourStatusByTourId(tourId, loggedInuserId, ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER);

					String message = BusinessAction.messageForKeyAdmin("errorDriverCancelledTrip", tourDetails.getLanguage());
					String bo_message = BusinessAction.messageForKeyAdmin("errorDriverCancelledTrip", adminSmsSendingModel.getLanguage());

					ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetails.getPassengerId());

					ApnsMessageModel apnsMessage = new ApnsMessageModel();
					apnsMessage.setMessage(message);
					apnsMessage.setMessageType("push");
					apnsMessage.setToUserId(tourDetails.getPassengerId());
					apnsMessage.insertPushMessage();

					if (adminSmsSendingModel.ispCancelledByDriver()) {
						MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourDetails.getpPhoneCode() + tourDetails.getpPhone(), ProjectConstants.SMSConstants.SMS_DRIVER_CANCELLED_TEMPLATE_ID);
					}

					UserModel user = UserModel.getUserAccountDetailsById(tourDetails.getCreatedBy());

					//@formatter:off
						if ((tourDetails.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING)) || (tourDetails.getBookingType().equals(ProjectConstants.ADMIN_BOOKING))) {
						//@formatter:on

						if (user != null) {
							MetamorphSystemsSmsUtils.sendSmsToSingleUser(bo_message, user.getPhoneNoCode() + user.getPhoneNo(), ProjectConstants.SMSConstants.SMS_DRIVER_CANCELLED_TEMPLATE_ID);
						}
					}

					if (apnsDevice != null) {
						//apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER, WebappPropertyUtils.getWebAppProperty("certificatePath"));
						apnsDevice.sendFCMNotification("1", "Push", ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_DRIVER, message );
					}

					DriverTourStatusUtils.updateDriverTourStatus(tourDetails.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);

					TourModel tour1 = new TourModel();
					tour1.setTourId(tourId);
					tour1.setCharges(0);
					tour1.updateCharges();

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

	@Path("/passenger-unavailable/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response unAvailabePassenger(
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@PathParam("tourId") String tourId) throws SQLException,
			IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {

			return sendUnauthorizedRequestError();
		}

		TourModel tour = TourModel.getTourDetailsByTourId(tourId);
		TourModel tourDetails = TourModel.getTourDetailsByTourId(tourId);

		FreeWaitingTimeModel freeWaitingTimeModel = FreeWaitingTimeModel.getFreeWaitingTime();
		TourTimeModel tourTimeModel = TourTimeModel.getTourTimesDetailsByTourId(tourId);

		double currentTime = DateUtils.nowAsGmtMillisec();

		double timeDiff = currentTime - tourTimeModel.getAcceptTime();

		boolean directCancelTripWithoutPayment = false;

		if (timeDiff <= (freeWaitingTimeModel.getCancelTime() * (60 * 1000))) {

			directCancelTripWithoutPayment = true;
		}

		if (tour != null && tour.getDriverId().equals(loggedInuserId)) {

			if (!tour.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {

				if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST) || tour.getStatus().equals(ProjectConstants.TourStatusConstants.ARRIVED_WAITING_TOUR)) {

					TourUtils.updateTourStatusByTourId(tourId, loggedInuserId, ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE);

					InvoiceModel invoice = InvoiceModel.getInvoiceByTourId(tourId);

					if (invoice == null) {

						invoice = new InvoiceModel();

						invoice.setTourId(tourId);
						invoice.setInitialFare(tourDetails.getInitialFare());
						invoice.setPerKmFare(tourDetails.getPerKmFare());
						invoice.setPerMinuteFare(tourDetails.getPerMinuteFare());
						invoice.setBookingFees(tourDetails.getBookingFees());
						invoice.setDiscount(tourDetails.getDiscount());
						invoice.setMinimumFare(tourDetails.getMinimumFare());

						if (directCancelTripWithoutPayment) {

							invoice.setFine(0);
							invoice.setTotal(0);
							invoice.setCharges(0);
							invoice.setSubTotal(0);

						} else {

							CancellationChargeModel cancellationChargeModel = CancellationChargeModel.getAdminCancellationCharges();

							invoice.setSubTotal(cancellationChargeModel.getCharge());
							invoice.setFine(cancellationChargeModel.getCharge());
							invoice.setTotal(cancellationChargeModel.getCharge());

							List<TaxModel> taxModelList = TaxModel.getActiveTaxList();

							double totalTaxAmount = 0.0;

							if (taxModelList != null && cancellationChargeModel.getCharge() > 0) {

								for (TaxModel taxModel : taxModelList) {

									totalTaxAmount = totalTaxAmount + ((taxModel.getTaxPercentage() / 100) * cancellationChargeModel.getCharge());
								}
							}

							invoice.setTotalTaxAmount(totalTaxAmount);

							invoice.setCharges(cancellationChargeModel.getCharge() + totalTaxAmount);

							if (taxModelList != null && cancellationChargeModel.getCharge() > 0) {

								updateTaxDetails(taxModelList, cancellationChargeModel.getCharge(), tourId, loggedInuserId);
							}

						}

						invoice.setPromoCodeApplied(false);
						invoice.setPromoCodeId(null);
						invoice.setPromoDiscount(0);
						invoice.setUsedCredits(0);

						PromoCodeUtils.promoCodeCancel(tourDetails, false);

						// @formatter:off
							if ((tourDetails.getBookingType().equals(ProjectConstants.BUSINESS_OWNER_BOOKING) && 
								tourDetails.getCardOwner() != null && tourDetails.getCardOwner().equals(ProjectConstants.BUSINESS_OWNER_BOOKING_B)) || 
								(tourDetails.getBookingType().equals(ProjectConstants.ADMIN_BOOKING) && tourDetails.getCardOwner() != null && 
								tourDetails.getCardOwner().equals(ProjectConstants.ADMIN_BOOKING_A))) {
							// @formatter:on

							invoice.setPaymentMode(ProjectConstants.CASH);

						} else {

							UserProfileModel userProfile = UserProfileModel.getAdminUserAccountDetailsById(tourDetails.getPassengerId());
							userProfile.setCredit(userProfile.getCredit() - invoice.getCharges());

							userProfile.updateUserCredits();

							if (userProfile != null) {

								if (userProfile.getCredit() >= 0 && invoice.getCharges() > 0) {

									if (invoice.getCharges() >= userProfile.getCredit()) {

										invoice.setCharges(invoice.getCharges() - userProfile.getCredit());
										invoice.setUsedCredits(userProfile.getCredit());

									} else {

										invoice.setUsedCredits(invoice.getCharges());
										invoice.setCharges(0);
									}

								}

							}

							invoice.setPaymentMode(ProjectConstants.CASH);
							invoice.setCard(false);
							invoice.setCashCollected(0);
							invoice.setCashToBeCollected(0);
							invoice.setCashNotReceived(true);
						}

						invoice.generateInvoice(loggedInuserId, true);
					}

					String message = messageForKey("errorPassengerNotAvailable", request);

					ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourDetails.getPassengerId());

					ApnsMessageModel apnsMessage = new ApnsMessageModel();
					apnsMessage.setMessage(message);
					apnsMessage.setMessageType("push");
					apnsMessage.setToUserId(tourDetails.getPassengerId());
					apnsMessage.insertPushMessage();

					if (apnsDevice != null) {
						apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE, WebappPropertyUtils.getWebAppProperty("certificatePath"));
					}

					DriverTourStatusUtils.updateDriverTourStatus(tourDetails.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);

					return sendSuccessMessage(messageForKey("successTripCancelled", request));

				} else if (tour.getStatus().equals(ProjectConstants.TourStatusConstants.PASSENGER_UNAVAILABLE)) {

					return sendBussinessError(messageForKey("errorPassengerNotAvauilableOneMoreTime", request));

				} else {

					return sendBussinessError(messageForKey("errorFailedToCancelTrip", request));
				}

			} else {

				return sendBussinessError(messageForKey("successPassengerCancelledTrip", request));
			}

		} else {

			return sendBussinessError(messageForKey("errorTripNotFound", request));
		}

	}

	@Path("/started/{tourId}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response startTour(
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@PathParam("tourId") String tourId) throws SQLException,
			IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {

			return sendUnauthorizedRequestError();
		}

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);

		if (!tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.CANCELLED_REQUEST_BY_PASSENGER)) {

			TourUtils.updateTourStatusByTourId(tourId, loggedInuserId, ProjectConstants.TourStatusConstants.STARTED_TOUR);

			TourTimeModel tourTimeModel = new TourTimeModel();
			tourTimeModel.setTourId(tourId);
			tourTimeModel.setStartTime(DateUtils.nowAsGmtMillisec());
			tourTimeModel.updateTourStartTime();

			AdminSmsSendingModel adminSmsSendingModel = AdminSmsSendingModel.getAdminSmsSendingDetails();

			ApnsDeviceModel apnsDevice = ApnsDeviceModel.getDeviseByUserId(tourModel.getPassengerId());

			String message = BusinessAction.messageForKeyAdmin("labelTripHasStarted", tourModel.getLanguage());

			ApnsMessageModel apnsMessage = new ApnsMessageModel();
			apnsMessage.setMessage(message);
			apnsMessage.setMessageType("push");
			apnsMessage.setToUserId(tourModel.getPassengerId());
			apnsMessage.insertPushMessage();

			if (adminSmsSendingModel.ispAcceptByDriver()) {
				MetamorphSystemsSmsUtils.sendSmsToSingleUser(message, tourModel.getpPhoneCode() + tourModel.getpPhone().trim(), ProjectConstants.SMSConstants.SMS_TRIP_STARTED_TEMPLATE_ID);
			}

			if (apnsDevice != null) {

				if (tourModel.getBookingType().equalsIgnoreCase(ProjectConstants.INDIVIDUAL_BOOKING)) {
					// new PassangerNotificationThread(apnsDevice.getDeviceToken(), tourId,
					// apnsDevice.getApiSessionKey());
				}

				// apnsDevice.sendNotification("1", "Push", message, ProjectConstants.TourStatusConstants.STARTED_TOUR, WebappPropertyUtils.getWebAppProperty("certificatePath"));
				apnsDevice.sendFCMNotification("1", "Push", ProjectConstants.TourStatusConstants.STARTED_TOUR, message);
			}

			return sendSuccessMessage(messageForKey("successTripStarted", request));

		} else {
			String errorMessage = String.format(messageForKey("errorTripCancelledByPassenger", request), MyHubUtils.formatFullName(tourModel.getpFirstName(), tourModel.getpLastName()));
			return sendBussinessError(errorMessage);
		}
	}

	@Path("/current-location")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public Response getdriverCurrentLocation(@HeaderParam("x-api-key") String sessionKeyHeader, @Context HttpServletRequest request, @Context HttpServletResponse response) {

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		TourModel tourModel = TourModel.getCurrentTourByPassangerId(loggedInuserId);
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		if (tourModel != null) {

			long currentTime = DateUtils.nowAsGmtMillisec();
			long oldTime = tourModel.getCreatedAt();
			long diffTime = ProjectConstants.REQUEST_TIME * 1000 * adminSettings.getNoOfCars();

			if ((currentTime - oldTime) > diffTime) {

				//@formatter:off
				if (tourModel.getStatus().equals(ProjectConstants.PENDING_REQUEST) || tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.NEW_TOUR)) {
				//@formatter:on

					PromoCodeUtils.promoCodeCancel(tourModel, true);

					TourUtils.assignTourDriver(tourModel.getTourId(), ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.EXPIRED_TOUR, loggedInuserId);

					return sendBussinessError(messageForKey("errorNotTripAvailable", request));

				} else if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

					PromoCodeUtils.promoCodeCancel(tourModel, true);

					TourUtils.assignTourDriver(tourModel.getTourId(), ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.EXPIRED_TOUR, loggedInuserId);

					DriverTourStatusUtils.updateDriverTourStatus(tourModel.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);

					return sendBussinessError(messageForKey("errorNotTripAvailable", request));
				}
			}
		}

		if (tourModel != null) {

			Map<String, Object> outPutMap = new HashMap<String, Object>();

			DriverGeoLocationModel driverLocation = DriverGeoLocationModel.getCurrentDriverPosition(tourModel.getDriverId());
			UserModel user = UserModel.getUserAccountDetailsById(tourModel.getDriverId());

			CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(tourModel.getCarTypeId());

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			if (driverLocation != null && user != null) {

				CarModel car = CarModel.getCarDetailsByDriverId(tourModel.getDriverId());

				if (car != null) {

					outPutMap.put("modelName", car.getModelName());
					outPutMap.put("carColor", car.getCarColor());
					outPutMap.put("carPlateNo", car.getCarPlateNo());
					outPutMap.put("carYear", car.getCarYear());
					outPutMap.put("make", car.getMake());
					outPutMap.put("noOfPassenger", car.getNoOfPassenger());
					outPutMap.put("owner", car.getOwner());
				} else {

					outPutMap.put("modelName", "");
					outPutMap.put("carColor", "");
					outPutMap.put("carPlateNo", "");
					outPutMap.put("carYear", "");
					outPutMap.put("make", "");
					outPutMap.put("noOfPassenger", "");
					outPutMap.put("owner", "");
				}

				outPutMap.put("carTypeId", tourModel.getCarTypeId());

				if (carTypeModel != null) {

					outPutMap.put("carType", carTypeModel.getCarType());

				} else {

					outPutMap.put("carType", "");
				}

				outPutMap.put("latitude", driverLocation.getLatitude());
				outPutMap.put("longitude", driverLocation.getLongitude());

				outPutMap.put("fullName", tourModel.getFirstName());
				outPutMap.put("firstName", tourModel.getFirstName());
				outPutMap.put("lastName", tourModel.getLastName());
				outPutMap.put("dPhotoUrl", tourModel.getPhotoUrl());
				outPutMap.put("tourId", tourModel.getTourId());
				outPutMap.put("driverId", tourModel.getDriverId());
				outPutMap.put("driverPhoneNumber", user.getPhoneNo());
				outPutMap.put("driverPhoneNumberCode", user.getPhoneNoCode());

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

				outPutMap.put("isAcknowledged", tourModel.isAcknowledged());
				outPutMap.put("multicityCountryId", tourModel.getMulticityCountryId());
				outPutMap.put("multicityCityRegionId", tourModel.getMulticityCityRegionId());

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

				outPutMap.put("paymentType", tourModel.getPaymentType());

				if (tourModel.isRideLater()) {
					outPutMap.put("dateTime", tourModel.getRideLaterPickupTime());
				} else {
					outPutMap.put("dateTime", tourModel.getCreatedAt());
				}

				List<DriverTripRatingsModel> driverTripRatingsModelList = DriverTripRatingsModel.getDriversTripRatingsList(tourModel.getDriverId());

				int driverAvgRate = 0;

				if (!driverTripRatingsModelList.isEmpty()) {

					int size = driverTripRatingsModelList.size();
					int rate = 0;

					for (DriverTripRatingsModel driverTripRatingsModel : driverTripRatingsModelList) {

						rate += driverTripRatingsModel.getRate();
					}

					driverAvgRate = rate / size;
				}

				outPutMap.put("rating", driverAvgRate);

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

			} else {
				outPutMap.put("latitude", "-1");
				outPutMap.put("longitude", "-1");
				outPutMap.put("carTypeId", "-1");
				outPutMap.put("modelName", "-1");
				outPutMap.put("carColor", "-1");
				outPutMap.put("carPlateNo", "-1");
				outPutMap.put("carYear", "-1");
				outPutMap.put("noOfPassenger", "-1");
				outPutMap.put("carTpeId", "-1");
				outPutMap.put("owner", "-1");
				outPutMap.put("fullName", "-1");
				outPutMap.put("photoUrl", "-1");
				outPutMap.put("tourId", "-1");
				outPutMap.put("driverId", "-1");
				outPutMap.put("driverPhoneNumber", "-1");

				outPutMap.put("initialFare", 0);
				outPutMap.put("perKmFare", 0);
				outPutMap.put("perMinuteFare", 0);
				outPutMap.put("bookingFees", 0);
				outPutMap.put("minimumFare", 0);
				outPutMap.put("discount", 0);

				outPutMap.put("promoCodeApplied", false);
				outPutMap.put("total", 0);
				outPutMap.put("promoDiscount", 0);

				outPutMap.put("promoCodeId", "-1");
				outPutMap.put("promoCode", "-1");
				outPutMap.put("usage", "-1");
				outPutMap.put("usageCount", "-1");
				outPutMap.put("mode", "-1");
				outPutMap.put("promoCodeDiscount", "-1");
				outPutMap.put("startDate", "-1");
				outPutMap.put("usedCount", "-1");
				outPutMap.put("endDate", "-1");
			}

			outPutMap.put("slatitude", tourModel.getsLatitude());
			outPutMap.put("slongitude", tourModel.getsLongitude());
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

			return sendDataResponse(outPutMap);
		} else {
			return sendBussinessError(messageForKey("errorNotTripAvailable", request));
		}
	}

	@Path("/{status}")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response driverOnOffDuty(
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@Context HttpServletRequest request,
			@Context HttpServletResponse response,
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			@PathParam("status") boolean status
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		if (status) {

			String currentSubscriptionPackageId = DriverSubscriptionUtils.getDriverCurrentSubscription(loggedInuserId, headerVendorId, DateUtils.nowAsGmtMillisec());
			if (currentSubscriptionPackageId == null) {
				return sendBussinessError(messageForKey("errorSubscriptionPackageNotValid", request));
			}

			DriverWalletSettingsModel driverWalletSettingsModel = DriverWalletSettingsModel.getDriverWalletSettings();

			UserProfileModel vendorProfileModel = UserProfileModel.getUserAccountDetailsById(MultiTenantUtils.getVendorIdByUserId(loggedInuserId));
			UserAccountModel userAccountModel = UserAccountModel.getAccountBalanceDetailsByUserId(loggedInuserId);

			if (!vendorProfileModel.isVendorDriverSubscriptionAppliedInBookingFlow()) {

				if (userAccountModel != null && driverWalletSettingsModel != null) {
					if (userAccountModel.getCurrentBalance() < driverWalletSettingsModel.getMinimumAmount()) {
						return sendBussinessError(messageForKey("errorDriverNotHaveSufficientBalance", request));
					}
				} else {
					return sendBussinessError(messageForKey("errorDriverNotHaveSufficientBalance", request));
				}
			}
		}

		UserModel userModel = new UserModel();
		userModel.setUserId(loggedInuserId);
		userModel.setOnDuty(status);

		int result = userModel.driverOnOffDuty();

		if (result > 0) {

			DriverDutyLogsModel driverDutyLogsModel = new DriverDutyLogsModel();
			driverDutyLogsModel.setDriverId(loggedInuserId);
			driverDutyLogsModel.setDutyStatus(status);
			driverDutyLogsModel.setCreatedAt(DateUtils.nowAsGmtMillisec());

			DriverDutyLogsModel lastDriverDutyLogDetails = DriverDutyLogsModel.getLastDriverDutyLogDetails(loggedInuserId);

			if (lastDriverDutyLogDetails != null) {

				if (lastDriverDutyLogDetails.isDutyStatus() ^ driverDutyLogsModel.isDutyStatus()) {

					driverDutyLogsModel.addDriverDutyLogs();

					if ((!driverDutyLogsModel.isDutyStatus()) && (lastDriverDutyLogDetails.isDutyStatus())) {

						long startTimeInMilliInDefaultTimeZone = lastDriverDutyLogDetails.getCreatedAt() + ProjectConstants.EXTRA_TIME_OF_DEFAULT_TIMEZONE_ON_GMT;
						long endTimeInMilliInDefaultTimeZone = driverDutyLogsModel.getCreatedAt() + ProjectConstants.EXTRA_TIME_OF_DEFAULT_TIMEZONE_ON_GMT;

						calculateLoggedInTimeAndSave(loggedInuserId, loggedInuserId, startTimeInMilliInDefaultTimeZone, endTimeInMilliInDefaultTimeZone);
					}
				}
			} else {
				driverDutyLogsModel.addDriverDutyLogs();
			}

			return sendSuccessMessage(messageForKey("successDriverDutyStatusUpdatedSuccessfully", request));

		} else {

			return sendBussinessError(messageForKey("errorDriverDutyStatusUpdatedSuccessfully", request));
		}
	}

	@Path("/dashboard")
	@GET
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getSecurityDeposit(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-api-key") String sessionKeyHeader,
			@QueryParam("offset") int offset,
			@QueryParam("length") int length,
			@QueryParam("summaryType") String summaryType,
			@QueryParam("year") int year
			) throws IOException {
	//@formatter:on

		String loggedInUserId = checkValidSession(sessionKeyHeader);
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}

		if (length == 0) {
			length = ProjectConstants.LIST_LIMIT;
		}

		Map<String, Object> outPutMap = new HashMap<String, Object>();

		if (summaryType == null || "".equals(summaryType)) {
			summaryType = ProjectConstants.WEEK_STRING;
		}

		if (year == 0) {
			year = Calendar.getInstance().get(Calendar.YEAR);
		}

		long startDateLong = DateUtils.getStartDayOfYearInMillis(year);
		long endDateLong = DateUtils.getLastDayOfYearInMillis(year);

		List<Map<String, Object>> dashboardSummary = new ArrayList<Map<String, Object>>();
		List<InvoiceModel> driverSecurityDepositLogModelList = null;

		if ((ProjectConstants.MONTH_STRING).equalsIgnoreCase(summaryType)) {

			driverSecurityDepositLogModelList = InvoiceModel.getMonthlyDriverDashboardSummary(offset, length, loggedInUserId, startDateLong, endDateLong);

		} else if ((ProjectConstants.DAY_STRING).equalsIgnoreCase(summaryType)) {

			driverSecurityDepositLogModelList = InvoiceModel.getDailyDriverDashboardSummary(offset, length, loggedInUserId, startDateLong, endDateLong);

		} else {

			driverSecurityDepositLogModelList = InvoiceModel.getWeeklyDriverDashboardSummary(offset, length, loggedInUserId, startDateLong, endDateLong);
		}

		if ((driverSecurityDepositLogModelList != null) && (driverSecurityDepositLogModelList.size() > 0)) {

			for (InvoiceModel invoiceModel : driverSecurityDepositLogModelList) {

				Map<String, Object> map = new HashMap<String, Object>();

				Calendar calender = Calendar.getInstance();
				calender.clear();

				if ((ProjectConstants.MONTH_STRING).equalsIgnoreCase(summaryType)) {

					calender.set(Calendar.YEAR, year);
					calender.set(Calendar.MONTH, (invoiceModel.getMonthOnly() - 1));
					calender.set(Calendar.DAY_OF_MONTH, 1);

				} else if ((ProjectConstants.DAY_STRING).equalsIgnoreCase(summaryType)) {

					calender.set(Calendar.YEAR, year);
					calender.set(Calendar.DAY_OF_YEAR, (invoiceModel.getDayOnly()));

				} else {

					calender.set(Calendar.YEAR, year);

					if (year == 2016) {

						calender.set(Calendar.WEEK_OF_YEAR, (invoiceModel.getWeekOnly() + 1));

					} else {

						calender.set(Calendar.WEEK_OF_YEAR, (invoiceModel.getWeekOnly()));
					}

					calender.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				}

				map.put("date", calender.getTimeInMillis());
				map.put("tripCount", invoiceModel.getTripCount());
				map.put("totalEarning", invoiceModel.getTotalEarning());
				map.put("avgRating", invoiceModel.getAvgRating());

				dashboardSummary.add(map);
			}
		}

		outPutMap.put("summaryType", summaryType);
		outPutMap.put("dashboardSummary", dashboardSummary);

		long startTime = 0;
		Instant todayInstantObject = DateUtils.getNowInstant();
		long endTime = DateUtils.atEndOfDay(todayInstantObject);

		if ((ProjectConstants.MONTH_STRING).equalsIgnoreCase(summaryType)) {
			startTime = DateUtils.getStartOfMonth();
			endTime = DateUtils.getEndOfMonth();
		} else if ((ProjectConstants.DAY_STRING).equalsIgnoreCase(summaryType)) {
			startTime = DateUtils.atStartOfDay(todayInstantObject);
			endTime = DateUtils.atEndOfDay(todayInstantObject);
		} else {
			startTime = DateUtils.getStartOfWeek();
			endTime = DateUtils.getEndOfWeek();
		}

		int driverSubscriptionCount = DriverSubscriberModel.getDriverSubscriptionCount(loggedInUserId, startTime, endTime);

		outPutMap.put("driverSubscriptionCount", driverSubscriptionCount);

		return sendDataResponse(outPutMap);
	}

	@Path("/location-update")
	@PUT
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	//@formatter:off
	public Response updateDriverLocation(
			@HeaderParam("x-api-key") String sessionKeyHeader, 
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-vendor-id") String headerVendorId,
			DriverGeoLocationModel driverGeo
			) throws SQLException, IOException {
	//@formatter:on

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {

			return sendUnauthorizedRequestError();
		}

		driverGeo.setDriverId(loggedInuserId);
		driverGeo.saveCarLocation(loggedInuserId);

		return sendSuccessMessage(messageForKey("successDriverLocationUpdated", request));
	}

	private void updateTaxDetails(List<TaxModel> taxModelList, double finalAmountForTaxCalculation, String tourId, String userId) {

		List<TourTaxModel> tourTaxModelList = new ArrayList<TourTaxModel>();

		long currentTime = DateUtils.nowAsGmtMillisec();

		for (TaxModel taxModel : taxModelList) {

			double taxAmount = ((taxModel.getTaxPercentage() / 100) * finalAmountForTaxCalculation);

			taxAmount = roundUpDecimalValueWithDownMode(taxAmount, 2);

			TourTaxModel tourTaxModel = new TourTaxModel();

			tourTaxModel.setTourTaxId(UUIDGenerator.generateUUID());
			tourTaxModel.setTourId(tourId);
			tourTaxModel.setTaxId(taxModel.getTaxId());
			tourTaxModel.setTaxPercentage(taxModel.getTaxPercentage());
			tourTaxModel.setTaxAmount(taxAmount > 0 ? taxAmount : 0);

			tourTaxModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
			tourTaxModel.setCreatedAt(currentTime);
			tourTaxModel.setCreatedBy(userId);
			tourTaxModel.setUpdatedAt(currentTime);
			tourTaxModel.setUpdatedBy(userId);

			tourTaxModelList.add(tourTaxModel);
		}

		if (tourTaxModelList.size() > 0) {

			TourTaxModel.insertTourTaxBatch(tourTaxModelList);
		}
	}
	
	
	@Path("/current-location-new")
	@GET
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public Response getdriverCurrentLocationNew(@HeaderParam("x-api-key") String sessionKeyHeader, @Context HttpServletRequest request, @Context HttpServletResponse response) {

		String loggedInuserId = checkValidSession(sessionKeyHeader);

		if (loggedInuserId == null) {
			return sendUnauthorizedRequestError();
		}

		TourModel tourModel = TourModel.getCurrentTourByPassangerIdNew(loggedInuserId);
		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();

		if (tourModel != null) {

			long currentTime = DateUtils.nowAsGmtMillisec();
			long oldTime = tourModel.getCreatedAt();
			long diffTime = ProjectConstants.REQUEST_TIME * 1000 * adminSettings.getNoOfCars();

			if ((currentTime - oldTime) > diffTime) {

				//@formatter:off
				if (tourModel.getStatus().equals(ProjectConstants.PENDING_REQUEST) || tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.NEW_TOUR)) {
				//@formatter:on

					PromoCodeUtils.promoCodeCancel(tourModel, true);

					TourUtils.assignTourDriver(tourModel.getTourId(), ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.EXPIRED_TOUR, loggedInuserId);

					return sendBussinessError(messageForKey("errorNotTripAvailable", request));

				} else if (tourModel.getStatus().equals(ProjectConstants.TourStatusConstants.ASSIGNED_TOUR)) {

					PromoCodeUtils.promoCodeCancel(tourModel, true);

					TourUtils.assignTourDriver(tourModel.getTourId(), ProjectConstants.DEFAULT_DRIVER_ID, ProjectConstants.TourStatusConstants.EXPIRED_TOUR, loggedInuserId);

					DriverTourStatusUtils.updateDriverTourStatus(tourModel.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);

					return sendBussinessError(messageForKey("errorNotTripAvailable", request));
				}
			}
		}

		if (tourModel != null) {

			Map<String, Object> outPutMap = new HashMap<String, Object>();

			DriverGeoLocationModel driverLocation = DriverGeoLocationModel.getCurrentDriverPosition(tourModel.getDriverId());
			UserModel user = UserModel.getUserAccountDetailsById(tourModel.getDriverId());

			CarTypeModel carTypeModel = CarTypeModel.getCarTypeByCarTypeId(tourModel.getCarTypeId());

			AdminSettingsModel adminSettingsModel = AdminSettingsModel.getAdminSettingsDetails();

			if (driverLocation != null && user != null) {

				CarModel car = CarModel.getCarDetailsByDriverId(tourModel.getDriverId());

				if (car != null) {

					outPutMap.put("modelName", car.getModelName());
					outPutMap.put("carColor", car.getCarColor());
					outPutMap.put("carPlateNo", car.getCarPlateNo());
					outPutMap.put("carYear", car.getCarYear());
					outPutMap.put("make", car.getMake());
					outPutMap.put("noOfPassenger", car.getNoOfPassenger());
					outPutMap.put("owner", car.getOwner());
				} else {

					outPutMap.put("modelName", "");
					outPutMap.put("carColor", "");
					outPutMap.put("carPlateNo", "");
					outPutMap.put("carYear", "");
					outPutMap.put("make", "");
					outPutMap.put("noOfPassenger", "");
					outPutMap.put("owner", "");
				}

				outPutMap.put("carTypeId", tourModel.getCarTypeId());

				if (carTypeModel != null) {

					outPutMap.put("carType", carTypeModel.getCarType());

				} else {

					outPutMap.put("carType", "");
				}

				outPutMap.put("latitude", driverLocation.getLatitude());
				outPutMap.put("longitude", driverLocation.getLongitude());

				outPutMap.put("fullName", tourModel.getFirstName());
				outPutMap.put("firstName", tourModel.getFirstName());
				outPutMap.put("lastName", tourModel.getLastName());
				outPutMap.put("dPhotoUrl", tourModel.getPhotoUrl());
				outPutMap.put("tourId", tourModel.getTourId());
				outPutMap.put("driverId", tourModel.getDriverId());
				outPutMap.put("driverPhoneNumber", user.getPhoneNo());
				outPutMap.put("driverPhoneNumberCode", user.getPhoneNoCode());

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

				outPutMap.put("isAcknowledged", tourModel.isAcknowledged());
				outPutMap.put("multicityCountryId", tourModel.getMulticityCountryId());
				outPutMap.put("multicityCityRegionId", tourModel.getMulticityCityRegionId());

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

				outPutMap.put("paymentType", tourModel.getPaymentType());

				if (tourModel.isRideLater()) {
					outPutMap.put("dateTime", tourModel.getRideLaterPickupTime());
				} else {
					outPutMap.put("dateTime", tourModel.getCreatedAt());
				}

				List<DriverTripRatingsModel> driverTripRatingsModelList = DriverTripRatingsModel.getDriversTripRatingsList(tourModel.getDriverId());

				int driverAvgRate = 0;

				if (!driverTripRatingsModelList.isEmpty()) {

					int size = driverTripRatingsModelList.size();
					int rate = 0;

					for (DriverTripRatingsModel driverTripRatingsModel : driverTripRatingsModelList) {

						rate += driverTripRatingsModel.getRate();
					}

					driverAvgRate = rate / size;
				}

				outPutMap.put("rating", driverAvgRate);

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

			} else {
				outPutMap.put("latitude", "-1");
				outPutMap.put("longitude", "-1");
				outPutMap.put("carTypeId", "-1");
				outPutMap.put("modelName", "-1");
				outPutMap.put("carColor", "-1");
				outPutMap.put("carPlateNo", "-1");
				outPutMap.put("carYear", "-1");
				outPutMap.put("noOfPassenger", "-1");
				outPutMap.put("carTpeId", "-1");
				outPutMap.put("owner", "-1");
				outPutMap.put("fullName", "-1");
				outPutMap.put("photoUrl", "-1");
				outPutMap.put("tourId", "-1");
				outPutMap.put("driverId", "-1");
				outPutMap.put("driverPhoneNumber", "-1");

				outPutMap.put("initialFare", 0);
				outPutMap.put("perKmFare", 0);
				outPutMap.put("perMinuteFare", 0);
				outPutMap.put("bookingFees", 0);
				outPutMap.put("minimumFare", 0);
				outPutMap.put("discount", 0);

				outPutMap.put("promoCodeApplied", false);
				outPutMap.put("total", 0);
				outPutMap.put("promoDiscount", 0);

				outPutMap.put("promoCodeId", "-1");
				outPutMap.put("promoCode", "-1");
				outPutMap.put("usage", "-1");
				outPutMap.put("usageCount", "-1");
				outPutMap.put("mode", "-1");
				outPutMap.put("promoCodeDiscount", "-1");
				outPutMap.put("startDate", "-1");
				outPutMap.put("usedCount", "-1");
				outPutMap.put("endDate", "-1");
			}

			outPutMap.put("slatitude", tourModel.getsLatitude());
			outPutMap.put("slongitude", tourModel.getsLongitude());
			outPutMap.put("dlatitude", tourModel.getdLatitude());
			outPutMap.put("dlongitude", tourModel.getdLongitude());
			outPutMap.put("dAddress", tourModel.getDestinationAddress());
			outPutMap.put("sAddress", tourModel.getSourceAddress());
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

			return sendDataResponse(outPutMap);
		} else {
			return sendBussinessError(messageForKey("errorNotTripAvailable", request));
		}
	}
	
	@Path("/new-dashboard")
	@GET
	@Consumes({ "application/json", "application/xml" })
	@Produces({ "application/json", "application/xml" })
	//@formatter:off
	public Response getDashboardData(
			@Context HttpServletRequest request, 
			@Context HttpServletResponse response, 
			@HeaderParam("x-vendor-id") String headerVendorId,
			@HeaderParam("x-language-code") String lang,
			@HeaderParam("x-api-key") String sessionKeyHeader
			
			) throws IOException {
	//@formatter:on
		
		String loggedInUserId = checkValidSession(sessionKeyHeader);
		
		if (loggedInUserId == null) {
			return sendUnauthorizedRequestError();
		}
		
		Map<String, Object> outPutMap = new HashMap<String, Object>();
		long currentTime = DateUtils.nowAsGmtMillisec();
		
		Instant todayInstantObject = DateUtils.getNowInstant();
		long startOfDay = DateUtils.getStartOfDayLong(todayInstantObject);
		long endOfDay = DateUtils.getEndOfDayLong(todayInstantObject);
		
		int userSubscribedCount = DriverSubscriberModel.getUserSubscribedCountByDriverId(loggedInUserId);
		
		DriverSubscriptionPackageHistoryModel currentPackage =  DriverSubscriptionPackageHistoryModel.getCurrentPackageByDriverId(loggedInUserId, currentTime);
		
		double totalEarningAmount = InvoiceModel.getTotalEarningsPerDay(loggedInUserId, startOfDay, endOfDay);
		
		outPutMap.put("userSubscribedCount", userSubscribedCount);
		outPutMap.put("currentPackage", currentPackage);
		outPutMap.put("totalEarningAmount", totalEarningAmount);
		
		return sendDataResponse(outPutMap);
	}
	public static double roundUpDecimalValueWithDownMode(double value, int numberOfDigitsAfterDecimalPoint) {

		BigDecimal bigDecimal = new BigDecimal(value);
		bigDecimal = bigDecimal.setScale(numberOfDigitsAfterDecimalPoint, BigDecimal.ROUND_DOWN);
		return bigDecimal.doubleValue();
	}

	public static void calculateLoggedInTimeAndSave(String loggedInUserId, String driverId, long startTime, long endTime) {

		Calendar calStartDateTime = Calendar.getInstance();
		calStartDateTime.setTimeInMillis(startTime);
		calStartDateTime.set(Calendar.HOUR_OF_DAY, 0);
		calStartDateTime.set(Calendar.MINUTE, 0);
		calStartDateTime.set(Calendar.SECOND, 0);
		calStartDateTime.set(Calendar.MILLISECOND, 1);

		long startTimeInMillies = calStartDateTime.getTimeInMillis();

		Calendar calEndDateTime = Calendar.getInstance();
		calEndDateTime.setTimeInMillis(endTime);
		calEndDateTime.set(Calendar.HOUR_OF_DAY, 23);
		calEndDateTime.set(Calendar.MINUTE, 59);
		calEndDateTime.set(Calendar.SECOND, 59);
		calEndDateTime.set(Calendar.MILLISECOND, 999);

		long endTimeInMillies = calEndDateTime.getTimeInMillis();

		List<DriverLoggedInTimeModel> previousLoggedInTimesList = DriverLoggedInTimeModel.getLoggedInTimesListByDriverIdAndTime(driverId, startTimeInMillies, endTimeInMillies);

		List<Map<String, Object>> loggedInTimesList = BusinessApiAction.calculateLoggedInTimeDayWise(startTime, endTime);

		List<DriverLoggedInTimeModel> driverLoggedInTimeModelList = new ArrayList<DriverLoggedInTimeModel>();

		long currentTime = DateUtils.nowAsGmtMillisec();

		if (previousLoggedInTimesList == null) {

			for (Map<String, Object> loggedInTimesMap : loggedInTimesList) {

				DriverLoggedInTimeModel driverLoggedInTimeModel = new DriverLoggedInTimeModel();
				driverLoggedInTimeModel.setDriverLoggedInTimeId(UUIDGenerator.generateUUID());
				driverLoggedInTimeModel.setDriverId(driverId);
				driverLoggedInTimeModel.setLoggedInTime((long) loggedInTimesMap.get("loggedInTimeInMillies"));
				driverLoggedInTimeModel.setDateTime((long) loggedInTimesMap.get("dateTimeInMillies"));

				driverLoggedInTimeModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
				driverLoggedInTimeModel.setCreatedAt(currentTime);
				driverLoggedInTimeModel.setCreatedBy(loggedInUserId);
				driverLoggedInTimeModel.setUpdatedAt(currentTime);
				driverLoggedInTimeModel.setUpdatedBy(loggedInUserId);

				driverLoggedInTimeModelList.add(driverLoggedInTimeModel);
			}

		} else {

			for (Map<String, Object> loggedInTimesMap : loggedInTimesList) {

				boolean alreadyExist = false;

				String driverLoggedInTimeId = "";
				long loggedInTime = 0;

				for (DriverLoggedInTimeModel previousLoggedInTimeModel : previousLoggedInTimesList) {

					if (previousLoggedInTimeModel.getDateTime() == (long) loggedInTimesMap.get("dateTimeInMillies")) {
						alreadyExist = true;
						driverLoggedInTimeId = previousLoggedInTimeModel.getDriverLoggedInTimeId();
						loggedInTime = previousLoggedInTimeModel.getLoggedInTime();
					}
				}

				if (alreadyExist) {

					DriverLoggedInTimeModel driverLoggedInTimeModel = new DriverLoggedInTimeModel();
					driverLoggedInTimeModel.setDriverLoggedInTimeId(driverLoggedInTimeId);
					driverLoggedInTimeModel.setLoggedInTime(loggedInTime + ((long) loggedInTimesMap.get("loggedInTimeInMillies")));

					driverLoggedInTimeModel.updateLoggedInTimeById(loggedInUserId);

				} else {

					DriverLoggedInTimeModel driverLoggedInTimeModel = new DriverLoggedInTimeModel();
					driverLoggedInTimeModel.setDriverLoggedInTimeId(UUIDGenerator.generateUUID());
					driverLoggedInTimeModel.setDriverId(driverId);
					driverLoggedInTimeModel.setLoggedInTime((long) loggedInTimesMap.get("loggedInTimeInMillies"));
					driverLoggedInTimeModel.setDateTime((long) loggedInTimesMap.get("dateTimeInMillies"));

					driverLoggedInTimeModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
					driverLoggedInTimeModel.setCreatedAt(currentTime);
					driverLoggedInTimeModel.setCreatedBy(loggedInUserId);
					driverLoggedInTimeModel.setUpdatedAt(currentTime);
					driverLoggedInTimeModel.setUpdatedBy(loggedInUserId);

					driverLoggedInTimeModelList.add(driverLoggedInTimeModel);
				}
			}
		}

		if (driverLoggedInTimeModelList.size() > 0) {
			DriverLoggedInTimeModel.insertDriverLoggedInTimeBatch(driverLoggedInTimeModelList);
		}

	}

}