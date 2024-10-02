package com.utils.myhub;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.jeeutils.DateUtils;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.StringUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.AppointmentModel;
import com.webapp.models.AppointmentSettingModel;
import com.webapp.models.OrderItemModel;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorServiceCategoryModel;
import com.webapp.models.VendorStoreModel;
import com.webapp.models.VendorStoreSubVendorModel;
import com.webapp.models.WebSocketClient;

public class AppointmentUtils {

	private static Logger logger = Logger.getLogger(AppointmentUtils.class);

	public static List<String> getStatusListForAppointmentExprireCronJob() {
		// Cron will pull in "new" "new payment pending" orders
		return Arrays.asList(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW, ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW_PAYMENT_PENDING);
	}

	public static Map<String, Object> getAppointmentsEstimatedFareAndPlaceAppointment(AppointmentModel appointmentModel, boolean isEstimateFare, String loggedInUserId) {

		Map<String, Object> estimateFareMap = new HashMap<>();

		VendorStoreModel vsm = VendorStoreModel.getVendorStoreDetailsById(appointmentModel.getAppointmentVendorStoreId());
		if (vsm == null || (!vsm.isActive() && vsm.isDeleted())) {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorVendorStoreNotActive");
			return estimateFareMap;
		}

		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(vsm.getStoreAddressLat(), vsm.getStoreAddressLng());

		if (multicityCityRegionId == null) {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorNoServicesInThisRegion");
			return estimateFareMap;
		}

		logger.info("\n\n\n\tvsm.getMulticityCityRegionId()\t" + vsm.getMulticityCityRegionId());
		logger.info("\n\n\n\tmulticityCityRegionId\t" + multicityCityRegionId);

		if (!vsm.getMulticityCityRegionId().equalsIgnoreCase(multicityCityRegionId)) {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorNoServicesOfferedByThisStore");
			return estimateFareMap;
		}

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(appointmentModel.getAppointmentReceivedAgainstVendorId());

		long appointmentTime = DateUtils.getDateFromStringForRideLater(appointmentModel.getAppointmentTimeString(), WebappPropertyUtils.CLIENT_TIMEZONE);

//		if (!isEstimateFare) {
//
//			AppointmentSettingModel asm = AppointmentSettingModel.getAppointmentSettingDetailsByServiceId(vscm.getServiceId());
//			long minBookingTime = asm.getMinBookingTime();
//			long maxBookingTime = asm.getMaxBookingTime();
//			long currentTime = DateUtils.nowAsGmtMillisec();
//
//			// 22/03/2024 9pm < (22/03/2024 7.40pm + 30 mins --> 8.10)
//			if (appointmentTime < (currentTime + minBookingTime)) {
//
//				String errorMessage;
//
//				if (minBookingTime <= 60) {
//					errorMessage = String.format(BusinessAction.messageForKeyAdmin("errorAppointmentMinBookingTimeInMins"), appointmentModel.getAppointmentTimeString(), minBookingTime);
//				} else if (minBookingTime > 60 && minBookingTime < 1440) {
//					errorMessage = String.format(BusinessAction.messageForKeyAdmin("errorAppointmentMinBookingTimeInHours"), appointmentModel.getAppointmentTimeString(), MyHubUtils.convertMinutesToHours(minBookingTime));
//				} else {
//					errorMessage = String.format(BusinessAction.messageForKeyAdmin("errorAppointmentMinBookingTimeInDays"), appointmentModel.getAppointmentTimeString(), MyHubUtils.convertMinutesToDays(minBookingTime));
//				}
//
//				estimateFareMap.put("errorCode", 400);
//				estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
//				estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, errorMessage);
//				return estimateFareMap;
//			}
//		}

		boolean validPromo = PromoCodeUtils.validatePromoCode(appointmentModel, vscm, estimateFareMap);
		if (!validPromo) {
			return estimateFareMap;
		}

		AppointmentModel existingAppointmentModel = null;

		if (StringUtils.validString(appointmentModel.getPaymentToken())) {
			existingAppointmentModel = AppointmentModel.getAppointmentDetailsByPaymentToken(appointmentModel.getPaymentToken());
		}

//		AppointmentSettingModel osm = AppointmentSettingModel.getAppointmentSettingDetailsByServiceId(vscm.getServiceId());
//
//		boolean validNumberOfItems = validateNumberOfOrderItems(estimateFareMap, osm, appointmentModel.getAppointmentItemList());
//		if (!validNumberOfItems) {
//			return estimateFareMap;
//		}

		String appointmentId = null;
		long appointmentCreationTime = 0;
		long paymentTokenGeneratedTime = 0;
		boolean isExistingAppointment = false;

		appointmentCreationTime = DateUtils.nowAsGmtMillisec();

		if (existingAppointmentModel != null) {

			paymentTokenGeneratedTime = existingAppointmentModel.getPaymentTokenGeneratedTime();

			if (!isEstimateFare) {

				appointmentId = existingAppointmentModel.getAppointmentId();
				isExistingAppointment = true;

				revertProductInventoryCount(loggedInUserId, existingAppointmentModel.getAppointmentId());
			}

		} else {

			paymentTokenGeneratedTime = appointmentCreationTime;

			if (!isEstimateFare) {

				appointmentId = UUIDGenerator.generateUUID();
				isExistingAppointment = false;
			}
		}

		int appointmentNumberOfItems = 0;
		double appointmentPromoCodeDiscount = 0;
		double appointmentTotal = 0;
		double appointmentCharges = 0;

		List<String> vendorProductIds = new ArrayList<>();
		for (OrderItemModel orderItemModel : appointmentModel.getAppointmentItemList()) {
			vendorProductIds.add(orderItemModel.getVendorProductId());
		}

		Map<String, Long> productInventoryCountErrorMap = new HashMap<>();

		List<VendorProductModel> vpmList = VendorProductModel.getProductDetailsByVendorProductsIdsAndVendorStoreIdList(vendorProductIds, appointmentModel.getAppointmentVendorStoreId());
		for (VendorProductModel vendorProductModel : vpmList) {

			long numberOfItems = 0;

			for (OrderItemModel orderItemModel : appointmentModel.getAppointmentItemList()) {

				if (orderItemModel.getVendorProductId().equalsIgnoreCase(vendorProductModel.getVendorProductId())) {

					numberOfItems = orderItemModel.getNumberOfItemsOrdered();

					if (numberOfItems > vendorProductModel.getProductInventoryCount()) {

						productInventoryCountErrorMap.put(vendorProductModel.getProductName(), vendorProductModel.getProductInventoryCount());

					} else {

						if (!isEstimateFare) {

							long remainingProductInventoryCount = vendorProductModel.getProductInventoryCount() - numberOfItems;
							vendorProductModel.setProductInventoryCount(remainingProductInventoryCount);

							orderItemModel.setOrderItemId(UUIDGenerator.generateUUID());
							orderItemModel.setOrderId(appointmentId);
							orderItemModel.setProductSku(vendorProductModel.getProductSku());
							orderItemModel.setProductCategory(vendorProductModel.getProductCategory());
							orderItemModel.setProductName(vendorProductModel.getProductName());
							orderItemModel.setProductInformation(vendorProductModel.getProductInformation());
							orderItemModel.setProductActualPrice(vendorProductModel.getProductActualPrice());
							orderItemModel.setProductDiscountedPrice(vendorProductModel.getProductDiscountedPrice());
							orderItemModel.setProductWeight(vendorProductModel.getProductWeight());
							orderItemModel.setProductWeightUnit(vendorProductModel.getProductWeightUnit());
							orderItemModel.setProductSpecification(vendorProductModel.getProductSpecification());
							orderItemModel.setProductImage(vendorProductModel.getProductImage());
							orderItemModel.setPaid(vendorProductModel.isPaid());
							orderItemModel.setCreatedBy(loggedInUserId);
							orderItemModel.setUpdatedBy(loggedInUserId);
							orderItemModel.setCreatedAt(appointmentCreationTime);
							orderItemModel.setUpdatedAt(appointmentCreationTime);
						}
					}

					break;
				}
			}

			appointmentNumberOfItems += numberOfItems;
			appointmentTotal += (vendorProductModel.getProductDiscountedPrice() * numberOfItems);
		}

		if (!productInventoryCountErrorMap.isEmpty()) {
			estimateFareMap.put("productInventoryCountErrorMap", productInventoryCountErrorMap);
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorProductOutOfStocks");
			return estimateFareMap;
		}

		PromoCodeModel tempPromoCodeReturnModel = PromoCodeUtils.applyAndCalculatePromoCode(appointmentTotal, appointmentModel.getAppointmentPromoCodeId());

		appointmentModel.setPromoCode(tempPromoCodeReturnModel.getPromoCode());

		appointmentPromoCodeDiscount = tempPromoCodeReturnModel.getDiscount();

		appointmentCharges = appointmentTotal - appointmentPromoCodeDiscount;

		estimateFareMap.put("promoCode", appointmentModel.getPromoCode());
		estimateFareMap.put("appointmentPromoCodeId", appointmentModel.getAppointmentPromoCodeId());
		estimateFareMap.put("appointmentPromoCodeDiscount", BusinessAction.df.format(appointmentPromoCodeDiscount));
		estimateFareMap.put("appointmentTotal", BusinessAction.df.format(appointmentTotal));
		estimateFareMap.put("appointmentCharges", BusinessAction.df.format(appointmentCharges));
		estimateFareMap.put("appointmentTime", appointmentTime);

		if (!isEstimateFare) {

			appointmentModel.setAppointmentId(appointmentId);
			appointmentModel.setAppointmentUserId(loggedInUserId);
			appointmentModel.setAppointmentCreationTime(appointmentCreationTime);
			appointmentModel.setAppointmentTime(appointmentTime);

			if (appointmentModel.getPaymentMode().equalsIgnoreCase(ProjectConstants.ONLINE_ID)) {
				appointmentModel.setAppointmentStatus(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW_PAYMENT_PENDING);
			} else {
				appointmentModel.setAppointmentStatus(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW);
			}

			appointmentModel.setAppointmentPromoCodeDiscount(Double.parseDouble(BusinessAction.df.format(appointmentPromoCodeDiscount)));
			appointmentModel.setAppointmentTotal(Double.parseDouble(BusinessAction.df.format(appointmentTotal)));
			appointmentModel.setAppointmentCharges(Double.parseDouble(BusinessAction.df.format(appointmentCharges)));
			appointmentModel.setPaymentStatus(ProjectConstants.AppointmentConstants.APPOINTMENT_PAYMENT_PENDING);
			appointmentModel.setAppointmentNumberOfItems(appointmentNumberOfItems);
			appointmentModel.setMulticityCityRegionId(multicityCityRegionId);
			appointmentModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
			appointmentModel.setPaymentTokenGeneratedTime(paymentTokenGeneratedTime);

			appointmentModel = AppointmentUtils.placeAppointment(appointmentModel, loggedInUserId, isExistingAppointment);

			VendorProductModel.updateProductInventoryCount(loggedInUserId, vpmList);

			estimateFareMap.put("appointmentShortId", appointmentModel.getAppointmentShortId());
			estimateFareMap.put("appointmentId", appointmentModel.getAppointmentId());
		}

		return estimateFareMap;
	}

	public static AppointmentModel placeAppointment(AppointmentModel appointmentModel, String loggedInUserId, boolean isExistingAppointment) {

		if (isExistingAppointment) {
			appointmentModel = appointmentModel.updateAppointment(loggedInUserId);
		} else {
			appointmentModel = appointmentModel.insertAppointment(loggedInUserId);
		}

		sendNewAppointmentNotificationToVendorViaSocket(appointmentModel);

		return appointmentModel;
	}

	private static void sendNewAppointmentNotificationToVendorViaSocket(AppointmentModel appointmentModel) {

		String vendorId = appointmentModel.getAppointmentReceivedAgainstVendorId();
		List<VendorStoreSubVendorModel> subVendorList = VendorStoreSubVendorModel.getSubVendorsAllocatedToTheStore(appointmentModel.getAppointmentVendorStoreId());

		List<String> userList = new ArrayList<>();

		// add all the sub vendors assigned to the stores
		for (VendorStoreSubVendorModel vendorStoreSubVendorModel : subVendorList) {
			userList.add(vendorStoreSubVendorModel.getSubVendorId());
		}

		// add the main vendor assigned to the stores
		userList.add(vendorId);

		if (userList.isEmpty()) {
			return;
		}

		Map<String, Object> outPutMap = new HashMap<String, Object>();
		outPutMap.put("appointmentId", appointmentModel.getAppointmentId());
		outPutMap.put("appointmentModel", appointmentModel);
		JSONObject jsonMessage = new JSONObject(outPutMap);
		logger.info("\n\n\n\n\n\tNOR To Vendor\t" + jsonMessage);

		ApnsMessageModel apnsMessage;
		String socketMessge;
		String message = String.format(BusinessAction.messageForKeyAdmin("notificationVendorNewAppointment"), appointmentModel.getAppointmentShortId());
		List<ApnsMessageModel> apnsMessageList = new ArrayList<ApnsMessageModel>();

		List<ApnsDeviceModel> apnsDeviceModelList = ApnsDeviceModel.getDeviceListByUserIds(userList);

		for (ApnsDeviceModel apnsDevice : apnsDeviceModelList) {

			socketMessge = "TOUR`1`" + apnsDevice.getDeviceToken() + ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.NOR_SOCKET + jsonMessage.toString();
			WebSocketClient.sendDriverNotification(socketMessge, apnsDevice.getUserId(), apnsDevice.getApiSessionKey());

			apnsMessage = new ApnsMessageModel();
			apnsMessage.setApnsMessageId(UUIDGenerator.generateUUID());
			apnsMessage.setCreatedAt(DateUtils.nowAsGmtMillisec());
			apnsMessage.setUpdatedAt(DateUtils.nowAsGmtMillisec());
			apnsMessage.setMessage(message);
			apnsMessage.setMessageType("socket");
			apnsMessage.setToUserId(apnsDevice.getUserId());
			apnsMessageList.add(apnsMessage);

			MyHubNotificationUtils.sendPushNotificationToUser(apnsDevice, message, ProjectConstants.WEB_SOCKET_NOTIFICATION_TYPE.NOR);

			if (apnsMessageList.size() >= ProjectConstants.BATCH_INSERT_SIZE) {
				ApnsMessageModel.insertMultiplePushMessage(apnsMessageList);
				apnsMessageList.clear();
			}
		}

		if (!apnsMessageList.isEmpty()) {
			ApnsMessageModel.insertMultiplePushMessage(apnsMessageList);
			apnsMessageList.clear();
		}
	}

//	private static boolean validateNumberOfOrderItems(Map<String, Object> estimateFareMap, AppointmentSettingModel osm, List<OrderItemModel> orderItemList) {
//
//		int numberOfItems = 0;
//		for (OrderItemModel orderItemModel : orderItemList) {
//			numberOfItems += orderItemModel.getNumberOfItemsOrdered();
//		}
//
//		if (numberOfItems > osm.getMaxNumberOfItems()) {
//
//			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
//			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorMaxNumberOfItemsExceeded");
//			return false;
//		}
//
//		return true;
//	}

	public static void revertProductInventoryCount(String loggedInUserId, String appointmentId) {

		AppointmentModel appointmentModelWithItems = AppointmentModel.getAppointmentDetailsByAppointmentIdWithOrderItems(appointmentId);

		if (appointmentModelWithItems.getAppointmentItemList() == null || appointmentModelWithItems.getAppointmentItemList().isEmpty()) {
			return;
		}

		List<String> vendorProductIds = new ArrayList<>();
		for (OrderItemModel orderItemModel : appointmentModelWithItems.getAppointmentItemList()) {
			vendorProductIds.add(orderItemModel.getVendorProductId());
		}

		List<VendorProductModel> vpmList = VendorProductModel.getProductDetailsByVendorProductsIdsAndVendorStoreIdList(vendorProductIds, appointmentModelWithItems.getAppointmentVendorStoreId());

		for (VendorProductModel vendorProductModel : vpmList) {

			for (OrderItemModel orderItemModel : appointmentModelWithItems.getAppointmentItemList()) {

				if (orderItemModel.getVendorProductId().equalsIgnoreCase(vendorProductModel.getVendorProductId())) {

					long remainingProductInventoryCount = vendorProductModel.getProductInventoryCount() + orderItemModel.getNumberOfItemsOrdered();
					vendorProductModel.setProductInventoryCount(remainingProductInventoryCount);
				}
			}
		}

		if (!vpmList.isEmpty()) {
			VendorProductModel.updateProductInventoryCount(loggedInUserId, vpmList);
		}
	}

	public static Map<String, Object> cancelAppointment(String loggedInUserId, String appointmentId) {

		Map<String, Object> cancelAppointmentMap = new HashMap<>();
		boolean proceedWithAppointmentCancellation = false;

		AppointmentModel appointmentModel = AppointmentModel.getAppointmentLimitedDetailsByAppointmentId(appointmentId);

		switch (appointmentModel.getAppointmentStatus()) {
		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW_PAYMENT_PENDING:
			proceedWithAppointmentCancellation = true;
			break;
		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW:
			proceedWithAppointmentCancellation = true;
			break;
		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_ACCEPTED:
			proceedWithAppointmentCancellation = true;
			break;
		default:
			proceedWithAppointmentCancellation = false;
			break;
		}

		if (!proceedWithAppointmentCancellation) {
			cancelAppointmentMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			cancelAppointmentMap.put(ProjectConstants.STATUS_MESSAGE, String.format(BusinessAction.messageForKeyAdmin("errorAppointmentCancelled"), appointmentModel.getAppointmentShortId()));
			return cancelAppointmentMap;
		}

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(appointmentModel.getAppointmentReceivedAgainstVendorId());
		AppointmentSettingModel asm = AppointmentSettingModel.getAppointmentSettingDetailsByServiceId(vscm.getServiceId());

		Duration dur = Duration.between(Instant.ofEpochMilli(appointmentModel.getAppointmentCreationTime()), Instant.ofEpochMilli(DateUtils.nowAsGmtMillisec()));

		cancelAppointmentMap.put("appointmentShortId", appointmentModel.getAppointmentShortId());

		updateAppointmentStatus(loggedInUserId, appointmentModel, ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_CANCELLED);

		revertProductInventoryCount(loggedInUserId, appointmentModel.getAppointmentId());

		String notificationMessage = String.format(BusinessAction.messageForKeyAdmin("notificationAppointmentCancelled"), appointmentModel.getAppointmentShortId());
		MyHubNotificationUtils.sendPushNotificationToUser(appointmentModel.getAppointmentReceivedAgainstVendorId(), ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_CANCELLED, notificationMessage);
		MyHubNotificationUtils.sendPushNotificationToUser(appointmentModel.getAppointmentUserId(), ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_CANCELLED, notificationMessage);

		cancelAppointmentMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		String messageKey;
		if (dur.toMinutes() > asm.getFreeCancellationTimeMins()) {
			messageKey = "successAppointmentCancelledTime";
		} else {
			messageKey = "successAppointmentCancelled";
		}

		cancelAppointmentMap.put(ProjectConstants.STATUS_MESSAGE, String.format(BusinessAction.messageForKeyAdmin(messageKey), appointmentModel.getAppointmentShortId()));

		return cancelAppointmentMap;
	}

	public static Map<String, Object> updateAppointmentStatusByVendorOrAdminViaAdminPanelOrApi(AppointmentModel inputAppointmentModel, String loggedInUserId, boolean isAdminCall) {

		// Super admin or Admin or Vendor can do the following with appointment status
		// 1. Accepted
		// 2. Rejected
		// 3. Vendor Cancelled
		// 4. Service Completed
		// from API or Admin Panel

		Map<String, Object> output = new HashMap<>();

		AppointmentModel currentDbAppointmentModel = AppointmentModel.getAppointmentDetailsByAppointmentId(inputAppointmentModel.getAppointmentId());

		if (currentDbAppointmentModel.getAppointmentStatus().equalsIgnoreCase(inputAppointmentModel.getAppointmentStatus())) {
			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, String.format(BusinessAction.messageForKeyAdmin("errorAppointmentStatusUpdate"), inputAppointmentModel.getAppointmentStatus()));
			return output;
		}

		String type = null;
		String message = null;

		switch (inputAppointmentModel.getAppointmentStatus()) {

		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_ACCEPTED:

			markAppointmentAccepted(currentDbAppointmentModel, loggedInUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successAppointmentStatusChange");

			sendEndOtpForAppointmentSmsNotification(currentDbAppointmentModel);

			break;

		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_REJECTED:

			markAppointmentRejected(currentDbAppointmentModel, loggedInUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successAppointmentStatusChange");

			break;

		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_VENDOR_CANCELLED:

			markAppointmentCancelledByVendor(currentDbAppointmentModel, loggedInUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successAppointmentVendorCancelled");

			break;

		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_COMPLETED:

			if (!currentDbAppointmentModel.getAppointmentStatus().equalsIgnoreCase(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_ACCEPTED)) {

				type = ProjectConstants.STATUS_ERROR;
				message = String.format(BusinessAction.messageForKeyAdmin("errorJobStatusUpdateCompleted"), getAppointmentStatusDisplayLabels(currentDbAppointmentModel.getAppointmentStatus()));

			} else {

				boolean isOtpValid = false;

				if (isAdminCall) {
					isOtpValid = true;
				} else {
					isOtpValid = currentDbAppointmentModel.getEndOtp().equalsIgnoreCase(inputAppointmentModel.getEndOtp());
				}

				if (!isOtpValid) {
					type = ProjectConstants.STATUS_ERROR;
					message = BusinessAction.messageForKeyAdmin("errorJobStatusUpdateCompletedInvalidOtp");
				} else {

					markAppointmentCompleted(inputAppointmentModel, loggedInUserId);

					type = ProjectConstants.STATUS_SUCCESS;
					message = BusinessAction.messageForKeyAdmin("successJobStatusUpdateServiceCompleted");

					String notificationMessage = String.format(BusinessAction.messageForKeyAdmin("notificationJobStatusUpdateAppointmentCompleted"), currentDbAppointmentModel.getAppointmentUserId());
					MyHubNotificationUtils.sendPushNotificationToUser(currentDbAppointmentModel.getAppointmentReceivedAgainstVendorId(), ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_ACCEPTED, notificationMessage);
					MyHubNotificationUtils.sendPushNotificationToUser(currentDbAppointmentModel.getAppointmentUserId(), ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_ACCEPTED, notificationMessage);
				}
			}

			break;

		default:
			type = ProjectConstants.STATUS_ERROR;
			message = BusinessAction.messageForKeyAdmin("errorUpdatingJobStatus");
			break;
		}

		output.put(ProjectConstants.STATUS_TYPE, type);
		output.put(ProjectConstants.STATUS_MESSAGE, message);
		return output;
	}

	private static void markAppointmentRejected(AppointmentModel appointmentModel, String loggedInUserId) {

		updateAppointmentStatus(loggedInUserId, appointmentModel, ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_REJECTED);

		revertProductInventoryCount(loggedInUserId, appointmentModel.getAppointmentId());
	}

	private static void markAppointmentAccepted(AppointmentModel appointmentModel, String loggedInUserId) {
		updateAppointmentStatus(loggedInUserId, appointmentModel, ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_ACCEPTED);
	}

	private static void sendEndOtpForAppointmentSmsNotification(AppointmentModel appointmentModel) {

		UserProfileModel passengerModel = UserProfileModel.getAdminUserAccountDetailsById(appointmentModel.getAppointmentUserId());

		String smsForEndOtp = String.format(BusinessAction.messageForKeyAdmin("successDeliverEndOtp"), appointmentModel.getAppointmentShortId(), appointmentModel.getEndOtp());
		MetamorphSystemsSmsUtils.sendSmsToSingleUser(smsForEndOtp, passengerModel.getPhoneNoCode() + passengerModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_DELIVERY_END_OTP_TEMPLATE_ID);

		MyHubNotificationUtils.sendPushNotificationToUser(appointmentModel.getAppointmentUserId(), smsForEndOtp);
	}

	private static void markAppointmentCancelledByVendor(AppointmentModel appointmentModel, String loggedInUserId) {

		updateAppointmentStatus(loggedInUserId, appointmentModel, ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_VENDOR_CANCELLED);

		revertProductInventoryCount(loggedInUserId, appointmentModel.getAppointmentId());

		appointmentModel.setPaymentStatus(ProjectConstants.AppointmentConstants.APPOINTMENT_PAYMENT_SUCCESS);
		appointmentModel.updatePaymentStatus(loggedInUserId);
	}

	private static void markAppointmentCompleted(AppointmentModel appointmentModel, String loggedInUserId) {

		updateAppointmentStatus(loggedInUserId, appointmentModel, ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_COMPLETED);

		appointmentModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
		appointmentModel.updatePaymentStatus(loggedInUserId);
	}

	public static void updateAppointmentStatus(String loggedInUserId, AppointmentModel appointmentModel, String status) {
		appointmentModel.setAppointmentStatus(status);
		appointmentModel.updateAppointmentStatus(loggedInUserId);
	}

	public static void updateAppointmentStatus(String loggedInUserId, String appointmentId, String status) {
		AppointmentModel appointmentModel = new AppointmentModel();
		appointmentModel.setAppointmentId(appointmentId);
		appointmentModel.setAppointmentStatus(status);
		appointmentModel.updateAppointmentStatus(loggedInUserId);
	}

	public static String getAppointmentStatusDisplayLabels(String currentAppointmentStatus) {
		return getAppointmentStatusDisplayLabels(currentAppointmentStatus, false);
	}

	public static String getAppointmentStatusDisplayLabels(String currentAppointmentStatus, boolean isAction) {

		String label;

		switch (currentAppointmentStatus) {
		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW_PAYMENT_PENDING:
			label = BusinessAction.messageForKeyAdmin("labelOrdersNewPaymentPendingStatus");
			break;
		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW:
			label = BusinessAction.messageForKeyAdmin("labelOrdersNewStatus");
			break;
		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_CANCELLED:

			if (isAction) {
				label = BusinessAction.messageForKeyAdmin("labelOrdersCancelStatus");
			} else {
				label = BusinessAction.messageForKeyAdmin("labelOrdersCancelledStatus");
			}

			break;

		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_REJECTED:

			if (isAction) {
				label = BusinessAction.messageForKeyAdmin("labelOrdersRejectStatus");
			} else {
				label = BusinessAction.messageForKeyAdmin("labelOrdersRejectedStatus");
			}

			break;
		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_EXPIRED:
			label = BusinessAction.messageForKeyAdmin("labelOrdersExpiredStatus");
			break;
		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_ACCEPTED:

			if (isAction) {
				label = BusinessAction.messageForKeyAdmin("labelAppointmentAcceptStatus");
			} else {
				label = BusinessAction.messageForKeyAdmin("labelAppointmentAcceptedStatus");
			}

			break;
		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_COMPLETED:
			label = BusinessAction.messageForKeyAdmin("labelAppointmentCompleted");
			break;
		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_VENDOR_CANCELLED:

			if (isAction) {
				label = BusinessAction.messageForKeyAdmin("labelAppointmentVendorCancelStatus");
			} else {
				label = BusinessAction.messageForKeyAdmin("labelAppointmentVendorCancelledStatus");
			}

			break;
		default:
			label = BusinessAction.messageForKeyAdmin("labelAppointmentStatusNotAvailable");
			break;
		}

		return label;
	}

	public static List<String> getAppointmentStatusListAsPerAppointmentType(String type) {
		return getAppointmentStatusListAsPerAppointmentType(type, true);
	}

	public static List<String> getAppointmentStatusListAsPerAppointmentType(String type, boolean showNewPaymentPending) {

		List<String> ordersStatus = new ArrayList<>();

		switch (type) {

		case ProjectConstants.AppointmentConstants.APPOINTMENTS_NEW_TAB:
			if (showNewPaymentPending) {
				ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW_PAYMENT_PENDING);
			}
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW);
			break;

		case ProjectConstants.AppointmentConstants.APPOINTMENTS_ACTIVE_TAB:
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_ACCEPTED);
			break;

		case ProjectConstants.AppointmentConstants.APPOINTMENTS_ALL_OTHERS_TAB:
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_CANCELLED);
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_REJECTED);
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_EXPIRED);
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_COMPLETED);
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_VENDOR_CANCELLED);
			break;

		default:
			break;
		}

		return ordersStatus;
	}

	public static List<String> getAppointmentStatusForAppointmentsToBeChangedByAdmin(AppointmentModel appointmentModel, String currentAppointmentStatus) {

		List<String> ordersStatus = new ArrayList<>();

		switch (currentAppointmentStatus) {

		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW_PAYMENT_PENDING:
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW);
			break;

		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_NEW:
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_ACCEPTED);
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_REJECTED);
			break;

		case ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_ACCEPTED:
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_VENDOR_CANCELLED);
			ordersStatus.add(ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_COMPLETED);
			break;

		default:
			break;
		}

		return ordersStatus;
	}

	public static AppointmentModel setInputAppointmentModel(String appointmentId, String appointmentStatus) {
		AppointmentModel inputAppointmentModel = new AppointmentModel();
		inputAppointmentModel.setAppointmentId(appointmentId);
		inputAppointmentModel.setAppointmentStatus(appointmentStatus);
		return inputAppointmentModel;
	}

	public static void sendAppointmentExpireNotificationToUser(AppointmentModel appointmentModel) {
		String notificationMessage = String.format(BusinessAction.messageForKeyAdmin("notificationAppointmentExpired"), appointmentModel.getAppointmentShortId());
		MyHubNotificationUtils.sendPushNotificationToUser(appointmentModel.getAppointmentUserId(), ProjectConstants.AppointmentConstants.APPOINTMENT_STATUS_EXPIRED, notificationMessage);
	}
}