package com.utils.myhub;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.jeeutils.DateUtils;
import com.jeeutils.MetamorphSystemsSmsUtils;
import com.jeeutils.StringUtils;
import com.utils.CommonUtils;
import com.utils.UUIDGenerator;
import com.utils.myhub.notifications.MyHubNotificationUtils;
import com.webapp.ProjectConstants;
import com.webapp.actions.BusinessAction;
import com.webapp.actions.api.multicity.MultiCityAction;
import com.webapp.models.AdminSettingsModel;
import com.webapp.models.ApnsDeviceModel;
import com.webapp.models.ApnsMessageModel;
import com.webapp.models.AppointmentModel;
import com.webapp.models.CcavenueResponseLogModel;
import com.webapp.models.DriverGeoLocationModel;
import com.webapp.models.DriverWalletSettingsModel;
import com.webapp.models.InputModel;
import com.webapp.models.OrderItemModel;
import com.webapp.models.OrderModel;
import com.webapp.models.OrderSettingModel;
import com.webapp.models.PromoCodeModel;
import com.webapp.models.StatsModel;
import com.webapp.models.UserProfileModel;
import com.webapp.models.VendorCarTypeModel;
import com.webapp.models.VendorProductModel;
import com.webapp.models.VendorServiceCategoryModel;
import com.webapp.models.VendorStoreModel;
import com.webapp.models.VendorStoreSubVendorModel;
import com.webapp.models.VendorSubscriberModel;
import com.webapp.models.WebSocketClient;

public class OrderUtils {

	private static Logger logger = Logger.getLogger(OrderUtils.class);

	public static boolean isVendorStoreOpen(Instant todayInstantObject, UserProfileModel userProfileModel) {

		logger.info("userProfileModel.isClosedToday()\t" + userProfileModel.getVendorStoreId());
		logger.info("userProfileModel.isClosedToday()\t" + userProfileModel.isClosedToday());
		logger.info("userProfileModel.isVendorSubscriptionCurrentActive()\t" + userProfileModel.isVendorSubscriptionCurrentActive());

		if (userProfileModel.isClosedToday() || !userProfileModel.isVendorSubscriptionCurrentActive()) {
			logger.info("return isClosedToday || isVendorSubscriptionCurrentActive");
			return false;
		}

		long startOfDayLong = DateUtils.getStartOfDayLong(todayInstantObject);
		long todayInstantObjectEpochMillis = todayInstantObject.toEpochMilli() + DateUtils.getTimeZoneOffset();

		logger.info("userProfileModel.getDateType()\t" + userProfileModel.getDateType());
		logger.info("todayInstantObjectEpochMillis\t" + todayInstantObjectEpochMillis);
		logger.info("todayInstantObject.toEpochMilli()\t" + todayInstantObject.toEpochMilli());
		logger.info("DateUtils.getTimeZoneOffset()\t" + DateUtils.getTimeZoneOffset());
		logger.info("------------------------------------------------------------------------");

		if (userProfileModel.getDateType() == ProjectConstants.VENDOR_STORE_TIME_SPECIFIC_DATE) {

			logger.info("todayInstantObjectEpochMillis\t" + todayInstantObjectEpochMillis);
			logger.info("userProfileModel.getStartDate()\t" + userProfileModel.getStartDate());
			logger.info("userProfileModel.getEndDate()\t" + userProfileModel.getEndDate());

			if (todayInstantObjectEpochMillis < userProfileModel.getStartDate() || todayInstantObjectEpochMillis > userProfileModel.getEndDate()) {
				logger.info("return false 000\t");
				return false;
			}

			long dateOpeningMorningHoursMillis = DateUtils.getVendorStoreTimingInGmt(startOfDayLong, userProfileModel.getDateOpeningMorningHours());
			long dateClosingMorningHoursMillis = DateUtils.getVendorStoreTimingInGmt(startOfDayLong, userProfileModel.getDateClosingMorningHours());

			logger.info("todayInstantObjectEpochMillis\t" + todayInstantObjectEpochMillis);
			logger.info("dateOpeningMorningHoursMillis\t" + dateOpeningMorningHoursMillis);
			logger.info("dateClosingMorningHoursMillis\t" + dateClosingMorningHoursMillis);

			if (todayInstantObjectEpochMillis >= dateOpeningMorningHoursMillis && todayInstantObjectEpochMillis <= dateClosingMorningHoursMillis) {
				logger.info("return true 111\t");
				return true;
			}

			long dateOpeningEveningHoursMillis = DateUtils.getVendorStoreTimingInGmt(startOfDayLong, userProfileModel.getDateOpeningEveningHours());
			long dateClosingEveningHoursMillis = DateUtils.getVendorStoreTimingInGmt(startOfDayLong, userProfileModel.getDateClosingEveningHours());

			logger.info("todayInstantObjectEpochMillis\t" + todayInstantObjectEpochMillis);
			logger.info("dateOpeningEveningHoursMillis\t" + dateOpeningEveningHoursMillis);
			logger.info("dateClosingEveningHoursMillis\t" + dateClosingEveningHoursMillis);

			if (todayInstantObjectEpochMillis >= dateOpeningEveningHoursMillis && todayInstantObjectEpochMillis <= dateClosingEveningHoursMillis) {
				logger.info("return true 222\t");
				return true;
			}

		} else {

			long openingMorningHoursMillis = DateUtils.getVendorStoreTimingInGmt(startOfDayLong, userProfileModel.getOpeningMorningHours());
			long closingMorningHoursMillis = DateUtils.getVendorStoreTimingInGmt(startOfDayLong, userProfileModel.getClosingMorningHours());

			logger.info("todayInstantObjectEpochMillis\t" + todayInstantObjectEpochMillis);
			logger.info("openingMorningHoursMillis\t" + openingMorningHoursMillis);
			logger.info("closingMorningHoursMillis\t" + closingMorningHoursMillis);

			if (todayInstantObjectEpochMillis >= openingMorningHoursMillis && todayInstantObjectEpochMillis <= closingMorningHoursMillis) {
				logger.info("return true 333\t");
				return true;
			}

			long openingEveningHoursMillis = DateUtils.getVendorStoreTimingInGmt(startOfDayLong, userProfileModel.getOpeningEveningHours());
			long closingEveningHoursMillis = DateUtils.getVendorStoreTimingInGmt(startOfDayLong, userProfileModel.getClosingEveningHours());

			logger.info("todayInstantObjectEpochMillis\t" + todayInstantObjectEpochMillis);
			logger.info("openingEveningHoursMillis\t" + openingEveningHoursMillis);
			logger.info("closingEveningHoursMillis\t" + closingEveningHoursMillis);

			if (todayInstantObjectEpochMillis >= openingEveningHoursMillis && todayInstantObjectEpochMillis <= closingEveningHoursMillis) {
				logger.info("return true 444\t");
				return true;
			}
		}

		logger.info("return false 555\t");
		return false;
	}

	public static String getPaymentModeString(String paymentMode) {

		String paymentModeString = null;

		switch (paymentMode) {
		case ProjectConstants.CASH_ID:
			paymentModeString = ProjectConstants.C_CASH;
			break;
		case ProjectConstants.CARD_ID:
			paymentModeString = ProjectConstants.C_CARD;
			break;
		case ProjectConstants.WALLET_ID:
			paymentModeString = ProjectConstants.C_WALLET;
			break;
		case ProjectConstants.ONLINE_ID:
			paymentModeString = ProjectConstants.C_ONLINE;
			break;
		default:
			break;
		}

		return paymentModeString;
	}

	public static String getOrderStatusDisplayLabels(String currentOrderDeliveryStatus) {
		return getOrderStatusDisplayLabels(currentOrderDeliveryStatus, false);
	}

	public static String getOrderStatusDisplayLabels(String currentOrderDeliveryStatus, boolean isAction) {

		String label;

		switch (currentOrderDeliveryStatus) {
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW_PAYMENT_PENDING:
			label = BusinessAction.messageForKeyAdmin("labelOrdersNewPaymentPendingStatus");
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW:
			label = BusinessAction.messageForKeyAdmin("labelOrdersNewStatus");
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_CANCELLED:

			if (isAction) {
				label = BusinessAction.messageForKeyAdmin("labelOrdersCancelStatus");
			} else {
				label = BusinessAction.messageForKeyAdmin("labelOrdersCancelledStatus");
			}

			break;

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_REJECTED:

			if (isAction) {
				label = BusinessAction.messageForKeyAdmin("labelOrdersRejectStatus");
			} else {
				label = BusinessAction.messageForKeyAdmin("labelOrdersRejectedStatus");
			}

			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_EXPIRED:
			label = BusinessAction.messageForKeyAdmin("labelOrdersExpiredStatus");
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_OWN_DRIVER:

			if (isAction) {
				label = BusinessAction.messageForKeyAdmin("labelOrdersAcceptOwnDriverStatus");
			} else {
				label = BusinessAction.messageForKeyAdmin("labelOrdersAcceptedOwnDriverStatus");
			}

			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER:

			if (isAction) {
				label = BusinessAction.messageForKeyAdmin("labelOrdersAcceptPlatformDriverStatus");
			} else {
				label = BusinessAction.messageForKeyAdmin("labelOrdersAcceptedPlatformDriverStatus");
			}

			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ASSIGNED:
			label = BusinessAction.messageForKeyAdmin("labelOrdersDriverAssignedStatus");
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ACCEPTED:
			label = BusinessAction.messageForKeyAdmin("labelOrdersDriverAcceptedStatus");
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED:
			label = BusinessAction.messageForKeyAdmin("labelOrdersDriverStartedStatus");
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP:
			label = BusinessAction.messageForKeyAdmin("labelOrdersDriverArrivedAtPickupStatus");
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_DELIVERED:
			label = BusinessAction.messageForKeyAdmin("labelOrdersDriverDeliveredStatus");
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_CANCELLED:
			label = BusinessAction.messageForKeyAdmin("labelOrdersDriverCancelledStatus");
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_VENDOR_CANCELLED:
			label = BusinessAction.messageForKeyAdmin("labelOrdersVendorCancelledStatus");
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_OFFLINE:
			label = BusinessAction.messageForKeyAdmin("labelOfflineOrderStatus");
			break;
		default:
			label = BusinessAction.messageForKeyAdmin("labelOrderStatusNotAvailable");
			break;
		}

		return label;
	}

	public static List<String> getOrderStatusListAsPerOrderType(String type) {
		return getOrderStatusListAsPerOrderType(type, true);
	}

	public static List<String> getOrderStatusListAsPerOrderType(String type, boolean showNewPaymentPending) {

		List<String> ordersStatus = new ArrayList<>();

		switch (type) {

		case ProjectConstants.OrderDeliveryConstants.ORDERS_NEW_TAB:
			if (showNewPaymentPending) {
				ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW_PAYMENT_PENDING);
			}
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW);
			break;

		case ProjectConstants.OrderDeliveryConstants.ORDERS_ACTIVE_TAB:
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ASSIGNED);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_OWN_DRIVER);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ACCEPTED);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP);
			break;

		case ProjectConstants.OrderDeliveryConstants.ORDERS_ALL_OTHERS_TAB:
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_CANCELLED);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_REJECTED);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_EXPIRED);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_DELIVERED);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_CANCELLED);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_VENDOR_CANCELLED);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_OFFLINE);
			break;

		default:
			break;
		}

		return ordersStatus;
	}

	public static List<String> getOrderStatusForOrdersToBeChangedByAdmin(OrderModel orderModel, String currentOrderDeliveryStatus, boolean isDelieveryManagedByVendorDriver) {

		List<String> ordersStatus = new ArrayList<>();

		switch (currentOrderDeliveryStatus) {

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW_PAYMENT_PENDING:
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW);
			break;

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW:

			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_OWN_DRIVER);

			// if self delivery with X km is true, then hide the accepted platform driver
			// option
			if (!orderModel.isSelfDeliveryWithinXKm()) {
				ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER);
			}

			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_REJECTED);
			break;

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_OWN_DRIVER:
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED);
			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_VENDOR_CANCELLED);
			break;

		// if "accepted own driver" and current status is "driver started" then
		// vendor can move it to "driver delivered"
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED:

			if (isDelieveryManagedByVendorDriver) {
				ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_DELIVERED);
			}

			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_VENDOR_CANCELLED);

			break;

		default:
			break;
		}

		if (currentOrderDeliveryStatus.equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ASSIGNED) || currentOrderDeliveryStatus.equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ACCEPTED)
					|| currentOrderDeliveryStatus.equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP)) {

			ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_VENDOR_CANCELLED);
		}

		return ordersStatus;
	}

	public static List<String> getOrderStatusForCurrentDriverOrders() {

		List<String> ordersStatus = new ArrayList<>();
		ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ASSIGNED);
		ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ACCEPTED);
		ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP);
		ordersStatus.add(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED);
		return ordersStatus;
	}

	public static OrderModel placeOrder(OrderModel orderModel, String loggedInUserId, boolean isExistingOrder) {

		if (isExistingOrder) {
			orderModel = orderModel.updateOrder(loggedInUserId);
		} else {
			orderModel = orderModel.insertOrder(loggedInUserId);
		}

		if (orderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW)) {
			OrderUtils.sendNewOrderNotificationToVendorViaSocket(orderModel);
		}

		return orderModel;
	}

	public static void sendNewOrderNotificationToVendorViaSocket(OrderModel orderModel) {

		String vendorId = orderModel.getOrderReceivedAgainstVendorId();
		List<VendorStoreSubVendorModel> subVendorList = VendorStoreSubVendorModel.getSubVendorsAllocatedToTheStore(orderModel.getVendorStoreId());

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
		outPutMap.put("orderId", orderModel.getOrderId());
		outPutMap.put("orderModel", orderModel);
		JSONObject jsonMessage = new JSONObject(outPutMap);
		logger.info("\n\n\n\n\n\tNOR To Vendor\t" + jsonMessage);

		ApnsMessageModel apnsMessage;
		String socketMessge;
		String message = String.format(BusinessAction.messageForKeyAdmin("notificationVendorNewOrder"), orderModel.getOrderShortId());
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

	public static Map<String, Object> getOrdersEstimatedFareAndPlaceOrder(OrderModel orderModel, boolean isEstimateFare, String loggedInUserId) {

		Map<String, Object> estimateFareMap = new HashMap<>();
		String multicityCityRegionId = MultiCityAction.getMulticityRegionId(orderModel.getOrderDeliveryLat(), orderModel.getOrderDeliveryLng());

		if (multicityCityRegionId == null) {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorNoServicesInThisRegion");
			return estimateFareMap;
		}

		VendorStoreModel vsm = VendorStoreModel.getVendorStoreDetailsById(orderModel.getVendorStoreId());
		if (vsm == null || (!vsm.isActive() && vsm.isDeleted())) {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorVendorStoreNotActive");
			return estimateFareMap;
		}

		logger.info("\n\n\n\tvsm.getMulticityCityRegionId()\t" + vsm.getMulticityCityRegionId());
		logger.info("\n\n\n\tmulticityCityRegionId\t" + multicityCityRegionId);

		if (!vsm.getMulticityCityRegionId().equalsIgnoreCase(multicityCityRegionId)) {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorNoServicesOfferedByThisStore");
			return estimateFareMap;
		}

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(orderModel.getOrderReceivedAgainstVendorId());

		boolean validPromo = PromoCodeUtils.validatePromoCode(orderModel, vscm, estimateFareMap);
		if (!validPromo) {
			return estimateFareMap;
		}

		OrderModel existingOrderModel = null;

		if (StringUtils.validString(orderModel.getPaymentToken())) {
			existingOrderModel = OrderModel.getOrderDetailsByPaymentToken(orderModel.getPaymentToken());
		}

		OrderSettingModel osm = OrderSettingModel.getOrderSettingDetailsByServiceId(vscm.getServiceId());

		boolean validNumberOfItems = validateNumberOfOrderItems(estimateFareMap, osm, orderModel.getOrderItemList());
		if (!validNumberOfItems) {
			return estimateFareMap;
		}

		String orderId = null;
		long orderCreationTime = 0;
		long paymentTokenGeneratedTime = 0;
		boolean isExistingOrder = false;

		orderCreationTime = DateUtils.nowAsGmtMillisec();

		if (existingOrderModel != null) {

			paymentTokenGeneratedTime = existingOrderModel.getPaymentTokenGeneratedTime();

			if (!isEstimateFare) {

				orderId = existingOrderModel.getOrderId();
				isExistingOrder = true;

				revertProductInventoryCount(loggedInUserId, orderModel.getOrderId());
			}

		} else {

			paymentTokenGeneratedTime = orderCreationTime;

			if (!isEstimateFare) {

				orderId = UUIDGenerator.generateUUID();
				isExistingOrder = false;
			}
		}

		int orderNumberOfItems = 0;
		double orderPromoCodeDiscount = 0;
		double orderTotal = 0;
		double orderDeliveryCharges = 0;
		double orderCharges = 0;

		List<String> vendorProductIds = new ArrayList<>();
		for (OrderItemModel orderItemModel : orderModel.getOrderItemList()) {
			vendorProductIds.add(orderItemModel.getVendorProductId());
		}

		Map<String, Long> productInventoryCountErrorMap = new HashMap<>();

		List<VendorProductModel> vpmList = VendorProductModel.getProductDetailsByVendorProductsIdsAndVendorStoreIdList(vendorProductIds, orderModel.getVendorStoreId());
		for (VendorProductModel vendorProductModel : vpmList) {

			long numberOfItems = 0;

			for (OrderItemModel orderItemModel : orderModel.getOrderItemList()) {

				if (orderItemModel.getVendorProductId().equalsIgnoreCase(vendorProductModel.getVendorProductId())) {

					numberOfItems = orderItemModel.getNumberOfItemsOrdered();

					if (numberOfItems > vendorProductModel.getProductInventoryCount()) {

						productInventoryCountErrorMap.put(vendorProductModel.getProductName(), vendorProductModel.getProductInventoryCount());

					} else {

						if (!isEstimateFare) {

							long remainingProductInventoryCount = vendorProductModel.getProductInventoryCount() - numberOfItems;
							vendorProductModel.setProductInventoryCount(remainingProductInventoryCount);

							orderItemModel.setOrderItemId(UUIDGenerator.generateUUID());
							orderItemModel.setOrderId(orderId);
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
							orderItemModel.setCreatedAt(orderCreationTime);
							orderItemModel.setUpdatedAt(orderCreationTime);
						}
					}

					break;
				}
			}

			orderNumberOfItems += numberOfItems;
			orderTotal += (vendorProductModel.getProductDiscountedPrice() * numberOfItems);
		}

		if (!productInventoryCountErrorMap.isEmpty()) {
			estimateFareMap.put("productInventoryCountErrorMap", productInventoryCountErrorMap);
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorProductOutOfStocks");
			return estimateFareMap;
		}

		Map<String, Double> distanceMatrix = CommonUtils.getDistanceMatrixInbetweenLocations(vsm.getStoreAddressLat(), vsm.getStoreAddressLng(), orderModel.getOrderDeliveryLat(), orderModel.getOrderDeliveryLng());
		double orderDeliveryDistance = distanceMatrix.get("distanceInMeters");
		double distanceInKm = distanceMatrix.get("distanceInKm");

		AdminSettingsModel adminSettings = AdminSettingsModel.getAdminSettingsDetails();
		if (distanceInKm > adminSettings.getRadiusDeliveryDriver()) {
			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorDeliveryDistanceOutsideDeliveryRadius");
			return estimateFareMap;
		}

		double chargeableKms = 0;
		if (distanceInKm > osm.getDeliveryBaseKm()) {
			chargeableKms = distanceInKm - osm.getDeliveryBaseKm();
		}

		PromoCodeModel tempPromoCodeReturnModel = PromoCodeUtils.applyAndCalculatePromoCode(orderTotal, orderModel.getOrderPromoCodeId());

		orderModel.setPromoCode(tempPromoCodeReturnModel.getPromoCode());

		orderPromoCodeDiscount = tempPromoCodeReturnModel.getDiscount();

		orderDeliveryCharges = osm.getDeliveryBaseFee() + (chargeableKms * osm.getDeliveryFeePerKm());

		// if the distance between the store and destination is less than X kms, then
		// override the delivery fee with the fee set against the vendor.
		boolean isSelfDeliveryWithinXKm = false;
		if (distanceInKm <= adminSettings.getRadiusSelfDelivery()) {

			UserProfileModel vendorModel = UserProfileModel.getAdminUserAccountDetailsById(orderModel.getOrderReceivedAgainstVendorId());

			if (vendorModel != null && vendorModel.isSelfDeliveryWithinXKm()) {
				isSelfDeliveryWithinXKm = true;
				orderDeliveryCharges = vendorModel.getSelfDeliveryFee();
			}
		}

		orderCharges = orderTotal + orderDeliveryCharges - orderPromoCodeDiscount;

		estimateFareMap.put("promoCode", orderModel.getPromoCode());
		estimateFareMap.put("orderPromoCodeId", orderModel.getOrderPromoCodeId());
		estimateFareMap.put("orderPromoCodeDiscount", BusinessAction.df.format(orderPromoCodeDiscount));
		estimateFareMap.put("orderTotal", BusinessAction.df.format(orderTotal));
		estimateFareMap.put("orderDeliveryCharges", BusinessAction.df.format(orderDeliveryCharges));
		estimateFareMap.put("orderCharges", BusinessAction.df.format(orderCharges));
		estimateFareMap.put("orderDeliveryDistance", BusinessAction.df.format(orderDeliveryDistance));

		if (!isEstimateFare) {

			orderModel.setOrderId(orderId);
			orderModel.setOrderUserId(loggedInUserId);
			orderModel.setOrderCreationTime(orderCreationTime);

			if (orderModel.getPaymentMode().equalsIgnoreCase(ProjectConstants.ONLINE_ID)) {
				orderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW_PAYMENT_PENDING);
			} else {
				orderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW);
			}

			orderModel.setOrderPromoCodeDiscount(Double.parseDouble(BusinessAction.df.format(orderPromoCodeDiscount)));
			orderModel.setOrderTotal(Double.parseDouble(BusinessAction.df.format(orderTotal)));
			orderModel.setOrderDeliveryCharges(Double.parseDouble(BusinessAction.df.format(orderDeliveryCharges)));
			orderModel.setOrderCharges(Double.parseDouble(BusinessAction.df.format(orderCharges)));
			orderModel.setOrderDeliveryDistance(Double.parseDouble(BusinessAction.df.format(orderDeliveryDistance)));
			orderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_PENDING);
			orderModel.setOrderNumberOfItems(orderNumberOfItems);
			orderModel.setMulticityCityRegionId(multicityCityRegionId);
			orderModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
			orderModel.setCarTypeId(ProjectConstants.CAR_TYPES.BIKE_ID);
			orderModel.setSelfDeliveryWithinXKm(isSelfDeliveryWithinXKm);
			orderModel.setPaymentTokenGeneratedTime(paymentTokenGeneratedTime);

			orderModel = OrderUtils.placeOrder(orderModel, loggedInUserId, isExistingOrder);

			VendorProductModel.updateProductInventoryCount(loggedInUserId, vpmList);

			estimateFareMap.put("orderShortId", orderModel.getOrderShortId());
			estimateFareMap.put("orderId", orderModel.getOrderId());
		}

		return estimateFareMap;
	}

	private static boolean validateNumberOfOrderItems(Map<String, Object> estimateFareMap, OrderSettingModel osm, List<OrderItemModel> orderItemList) {

		int numberOfItems = 0;
		for (OrderItemModel orderItemModel : orderItemList) {
			numberOfItems += orderItemModel.getNumberOfItemsOrdered();
		}

		if (numberOfItems > osm.getMaxNumberOfItems()) {

			estimateFareMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			estimateFareMap.put(ProjectConstants.STATUS_MESSAGE_KEY, "errorMaxNumberOfItemsExceeded");
			return false;
		}

		return true;
	}

	public static Map<String, Object> cancelOrder(String loggedInUserId, String orderId) {

		Map<String, Object> cancelOrderMap = new HashMap<>();
		boolean proceedWithOrderCancellation = false;

		OrderModel orderModel = OrderModel.getOrderLimitedDetailsByOrderId(orderId);

		switch (orderModel.getOrderDeliveryStatus()) {
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW_PAYMENT_PENDING:
			proceedWithOrderCancellation = true;
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW:
			proceedWithOrderCancellation = true;
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_OWN_DRIVER:
			proceedWithOrderCancellation = true;
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER:
			proceedWithOrderCancellation = true;
			break;
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ACCEPTED:
			proceedWithOrderCancellation = true;
			break;
		default:
			proceedWithOrderCancellation = false;
			break;
		}

		if (!proceedWithOrderCancellation) {
			cancelOrderMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			cancelOrderMap.put(ProjectConstants.STATUS_MESSAGE, String.format(BusinessAction.messageForKeyAdmin("errorOrderCancelled"), orderModel.getOrderShortId()));
			return cancelOrderMap;
		}

		VendorServiceCategoryModel vscm = VendorServiceCategoryModel.getVendorServiceCategoryByVendorId(orderModel.getOrderReceivedAgainstVendorId());
		OrderSettingModel osm = OrderSettingModel.getOrderSettingDetailsByServiceId(vscm.getServiceId());

		Duration dur = Duration.between(Instant.ofEpochMilli(orderModel.getOrderCreationTime()), Instant.ofEpochMilli(DateUtils.nowAsGmtMillisec()));

		cancelOrderMap.put("orderShortId", orderModel.getOrderShortId());

		updateOrderDeliveryStatus(loggedInUserId, orderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_CANCELLED);

		revertProductInventoryCount(loggedInUserId, orderModel.getOrderId());

		String notificationMessage = String.format(BusinessAction.messageForKeyAdmin("notificationOrderCancelled"), orderModel.getOrderShortId());
		MyHubNotificationUtils.sendPushNotificationToUser(orderModel.getOrderReceivedAgainstVendorId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_CANCELLED, notificationMessage);
		MyHubNotificationUtils.sendPushNotificationToUser(orderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_CANCELLED, notificationMessage);

		cancelOrderMap.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_SUCCESS);

		String messageKey;
		if (dur.toMinutes() > osm.getFreeCancellationTimeMins()) {
			messageKey = "successOrderCancelledTime";
		} else {
			messageKey = "successOrderCancelled";
		}

		cancelOrderMap.put(ProjectConstants.STATUS_MESSAGE, String.format(BusinessAction.messageForKeyAdmin(messageKey), orderModel.getOrderShortId()));

		return cancelOrderMap;
	}

	public static void revertProductInventoryCount(String loggedInUserId, String orderId) {

		OrderModel orderModelWithItems = OrderModel.getOrderDetailsByOrderIdWithOrderItems(orderId);

		//if (orderModelWithItems.getOrderItemList() == null || orderModelWithItems.getOrderItemList().isEmpty()) {
		if (orderModelWithItems == null || orderModelWithItems.getOrderItemList() == null || orderModelWithItems.getOrderItemList().isEmpty()) {	
			return;
		}

		List<String> vendorProductIds = new ArrayList<>();
		for (OrderItemModel orderItemModel : orderModelWithItems.getOrderItemList()) {
			vendorProductIds.add(orderItemModel.getVendorProductId());
		}

		List<VendorProductModel> vpmList = VendorProductModel.getProductDetailsByVendorProductsIdsAndVendorStoreIdList(vendorProductIds, orderModelWithItems.getVendorStoreId());

		for (VendorProductModel vendorProductModel : vpmList) {

			for (OrderItemModel orderItemModel : orderModelWithItems.getOrderItemList()) {

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

	public static void updateOrderDeliveryStatus(String loggedInUserId, OrderModel orderModel, String status) {
		orderModel.setOrderDeliveryStatus(status);
		orderModel.updateOrderDeliveryStatus(loggedInUserId);
	}

	public static void updateOrderDeliveryStatus(String loggedInUserId, String orderId, String status) {
		OrderModel orderModel = new OrderModel();
		orderModel.setOrderId(orderId);
		orderModel.setOrderDeliveryStatus(status);
		orderModel.updateOrderDeliveryStatus(loggedInUserId);
	}

	public static List<String> getStatusListForOrderProcessingCronJob() {
		// Cron will pull in "accepted by system driver" orders
		return Arrays.asList(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER);
	}

	public static List<String> getStatusListForOrderExprireCronJob() {
		// Cron will pull in "new" "new payment pending" orders
		return Arrays.asList(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW_PAYMENT_PENDING);
	}

	public static String searchDriver(AdminSettingsModel adminSettingsModel, OrderModel orderModel, DriverWalletSettingsModel driverWalletSettingsModel) {

		String driverId = null;

		List<VendorCarTypeModel> vendorCarTypesList = VendorCarTypeModel.getVendorCarTypeListByVendorIdSortPriority(orderModel.getOrderReceivedAgainstVendorId(), ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);

		for (VendorCarTypeModel vendorCarTypeModel : vendorCarTypesList) {

			// 16. Search near by driver -> minimumWalletAmount not needed as of now
			Map<String, Object> tempInputMap = new HashMap<>();
			MultiTenantUtils.setVendorDriverSubscriptionAppliedInBookingFlowInputData(tempInputMap, orderModel.getOrderReceivedAgainstVendorId(), DateUtils.nowAsGmtMillisec());

			Map<String, Object> inputMap = formCommonInputMapForDriverSearch(adminSettingsModel, driverWalletSettingsModel, orderModel, vendorCarTypeModel.getCarTypeId());

			// 1. search for specific vendor driver with search for subscribed driver 1st
			boolean searchSubscribedDriver = true;
			driverId = getNearestDriver(orderModel, inputMap, tempInputMap, searchSubscribedDriver, "1");

			// 2. search for specific vendor driver with search for all subscribed driver
			// and unsubscribed drivers
			if (driverId == null) {
				searchSubscribedDriver = false;
				driverId = getNearestDriver(orderModel, inputMap, tempInputMap, searchSubscribedDriver, "1");
			}

			// 3. search for default vendor driver with search for subscribed driver 1st
			if (driverId == null) {
				searchSubscribedDriver = true;
				driverId = getNearestDriver(orderModel, inputMap, tempInputMap, searchSubscribedDriver, "2");
			}

			// 4. search for default vendor driver with search for all subscribed driver and
			// unsubscribed drivers
			if (driverId == null) {
				searchSubscribedDriver = false;
				driverId = getNearestDriver(orderModel, inputMap, tempInputMap, searchSubscribedDriver, "2");
			}

			// 5. search for all vendor driver with search for subscribed driver 1st
			if (driverId == null) {
				searchSubscribedDriver = true;
				driverId = getNearestDriver(orderModel, inputMap, tempInputMap, searchSubscribedDriver, "3");
			}

			// 6. search for all vendor driver with search for all subscribed driver and
			// unsubscribed drivers
			if (driverId == null) {
				searchSubscribedDriver = false;
				driverId = getNearestDriver(orderModel, inputMap, tempInputMap, searchSubscribedDriver, "3");
			}

			if (driverId != null && checkForProcessingStatus(orderModel)) {

				OrderUtils.updateOrderDeliveryStatus(driverId, orderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ASSIGNED);

				DriverTourStatusUtils.updateDriverTourStatus(driverId, ProjectConstants.DRIVER_HIRED_STATUS);

				DriverOrderRequestUtils.createDriverOrderRequest(driverId, orderModel.getOrderId(), ProjectConstants.TourStatusConstants.ASSIGNED_TOUR);

				updateDriverIdAgainstOrder(orderModel, driverId);

				updateCarTypeIdAgainstOrder(orderModel, vendorCarTypeModel.getCarTypeId());
			}

			if (driverId != null) {
				break;
			}
		}

		return driverId;
	}

	private static boolean checkForProcessingStatus(OrderModel orderModel) {

		if (orderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER)) {
			return true;
		}

		return false;
	}

	private static String getNearestDriver(OrderModel orderModel, Map<String, Object> inputMap, Map<String, Object> tempInputMap, boolean searchSubscribedDriver, String vendorType) {

		String vendorId = null;
		if (vendorType.equalsIgnoreCase("1")) {
			// search specific vendor
			vendorId = orderModel.getOrderReceivedAgainstVendorId();
		} else if (vendorType.equalsIgnoreCase("2")) {
			// search default vendor
			vendorId = WebappPropertyUtils.DEFAULT_VENDOR_ID;
		} else {
			// search all the vendors
			vendorId = null;
		}

		inputMap.put("vendorId", vendorId);
		inputMap.put("searchSubscribedDriver", searchSubscribedDriver);

		// 16. Search near by driver -> minimumWalletAmount not needed as of now
		// inputMap.putAll(tempInputMap);

		DriverGeoLocationModel driverGeoLocationModel = DriverGeoLocationModel.getNearestSingleDriverForOrders(inputMap);
		return driverGeoLocationModel != null ? driverGeoLocationModel.getDriverId() : null;
	}

	private static Map<String, Object> formCommonInputMapForDriverSearch(AdminSettingsModel adminSettingsModel, DriverWalletSettingsModel driverWalletSettingsModel, OrderModel orderModel, String carTypeId) {

		String distance = GeoLocationUtil.getDistanceQuery(orderModel.getStoreAddressLat(), orderModel.getStoreAddressLng(), GeoLocationUtil.CAR_LOCATION);
		String latAndLong = GeoLocationUtil.getLatLngQuery(orderModel.getStoreAddressLat(), orderModel.getStoreAddressLng(), GeoLocationUtil.CAR_LOCATION, adminSettingsModel.getRadiusDeliveryDriverString());

		AdminSettingsModel.overrideSettingForDriverIdealTime(adminSettingsModel);

		long timeBeforeDriverIdealTimeInMillis = DateUtils.nowAsGmtMillisec() - adminSettingsModel.getDriverIdealTime();

		Map<String, Object> inputMap = new HashMap<String, Object>();

		inputMap.put("orderId", orderModel.getOrderId());
		inputMap.put("carTypeId", carTypeId);
		inputMap.put("startOffSet", 0);
		inputMap.put("recordOffset", 1);
		inputMap.put("latAndLong", latAndLong);
		inputMap.put("distance", distance);
		inputMap.put("timeBeforeDriverIdealTimeInMillis", timeBeforeDriverIdealTimeInMillis);
		inputMap.put("orderUserId", orderModel.getOrderUserId());
		inputMap.put("multicityCityRegionId", orderModel.getMulticityCityRegionId());
		inputMap.put("multicityCountryId", orderModel.getMulticityCountryId());
		inputMap.put("minimumWalletAmount", driverWalletSettingsModel != null ? driverWalletSettingsModel.getMinimumAmount() : 0.0);
		VendorMonthlySubscriptionHistoryUtils.setInputParamForVendorMonthlySubscriptionRestrictingDriver(adminSettingsModel, inputMap);

		return inputMap;
	}

	public static boolean checkIfCurrentUserAssignedAsDriverForThisRequest(OrderModel orderModel, String driverId) {
		return orderModel.getDriverId().equalsIgnoreCase(driverId);
	}

	public static void updateDriverIdAgainstOrder(String orderId, String driverId) {
		OrderModel orderModel = new OrderModel();
		orderModel.setOrderId(orderId);
		orderModel.setDriverId(driverId);
		orderModel.updateDriverIdAgainstOrder();
	}

	public static void updateDriverIdAgainstOrder(OrderModel orderModel, String driverId) {
		orderModel.setDriverId(driverId);
		orderModel.updateDriverIdAgainstOrder();
	}

	public static void updateCarTypeIdAgainstOrder(OrderModel orderModel, String carTypeId) {
		orderModel.setCarTypeId(carTypeId);
		orderModel.updateCarTypeIdAgainstOrder();
	}

	public static Map<String, Object> updateOrderJobAssignedStatusByDriverOnly(String orderId, String driverJobStatus, String loggedInDriverUserId) {

		// Only driver can do the following,
		// 1. Reject the assigned order job
		// 2. Accept the assigned order job
		// from API only.

		Map<String, Object> output = new HashMap<>();

		// 1. Fetch current order status model from the database
		OrderModel currentDbOrderModel = OrderModel.getOrderDetailsByOrderId(orderId);

		// 2. Check if the OrderDeliveryStatus is in Driver Assigned status or not.
		// If yes, then only driver can accept reject the job assigned.
		if (!currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ASSIGNED)) {

			if (driverJobStatus.equalsIgnoreCase(ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST)) {
				output.put(ProjectConstants.STATUS_MESSAGE, BusinessAction.messageForKeyAdmin("errorFailedToAcceptOrderRequest"));
			} else {
				output.put(ProjectConstants.STATUS_MESSAGE, BusinessAction.messageForKeyAdmin("errorFailedToRejectOrderRequest"));
			}

			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);

			return output;
		}

		// 3. Check if the logged in driver is same as the assigned driver.
		if (!OrderUtils.checkIfCurrentUserAssignedAsDriverForThisRequest(currentDbOrderModel, loggedInDriverUserId)) {
			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, BusinessAction.messageForKeyAdmin("errorJobStatusUpdateDriver"));
			return output;
		}

		String type = null;
		String message = null;

		switch (driverJobStatus) {

		// to be called by driver only.
		case ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST:

			markOrderJobStatusDriverAccepted(orderId, loggedInDriverUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successAcceptedOrderRequest");

			String notificationMessage = String.format(BusinessAction.messageForKeyAdmin("notificationAcceptedOrderRequest"), currentDbOrderModel.getOrderShortId());
			MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderReceivedAgainstVendorId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP, notificationMessage);
			// MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP, notificationMessage);
			MyHubNotificationUtils.sendFCMPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP, notificationMessage);

			break;

		// to be called by driver only.
		case ProjectConstants.TourStatusConstants.REJECTED_TOUR:

			markOrderJobStatusDriverRejected(orderId, loggedInDriverUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successRejectedOrderRequest");
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

	private static void markOrderJobStatusDriverAccepted(String orderId, String loggedInDriverUserId) {

		// If driver accepts the request then,
		// 1. Mark in driver_order_request table as accepted
		// 2. Mark in driver id as current user.
		// 3. Mark the order status as "driver accepted"

		DriverOrderRequestUtils.updateDriverOrderRequest(orderId, loggedInDriverUserId, ProjectConstants.TourStatusConstants.ACCEPTED_REQUEST);

		OrderUtils.updateDriverIdAgainstOrder(orderId, loggedInDriverUserId);

		OrderUtils.updateOrderDeliveryStatus(loggedInDriverUserId, orderId, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ACCEPTED);
	}

	private static void markOrderJobStatusDriverRejected(String orderId, String loggedInDriverUserId) {

		// If driver rejects the request then,
		// 1. Mark in driver_order_request table as rejected
		// 2. If the order has driver id marked as current user then assign -1.
		// 3. Change order status back to "accepted platform driver"

		DriverOrderRequestUtils.updateDriverOrderRequest(orderId, loggedInDriverUserId, ProjectConstants.TourStatusConstants.REJECTED_TOUR);

		OrderUtils.updateDriverIdAgainstOrder(orderId, ProjectConstants.DEFAULT_DRIVER_ID);

		OrderUtils.updateOrderDeliveryStatus(loggedInDriverUserId, orderId, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER);
	}

	public static Map<String, Object> updateOrderDeliveryStatusByVendorOrAdminViaAdminPanelOrApi(OrderModel inputOrderModel, String loggedInUserId) {

		// Super admin or Admin or Vendor can do the following with order delivery
		// status,
		// 1. Rejected
		// 2. Accepted Own Driver
		// 3. Accepted Plaform Driver
		// 4. Driver Started
		// 5. Driver Delivered
		// 6. Driver Cancelled
		// 7. Vendor Cancelled
		// from API or Admin Panel

		Map<String, Object> output = new HashMap<>();

		OrderModel currentDbOrderModel = OrderModel.getOrderDetailsByOrderId(inputOrderModel.getOrderId());

		if (currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(inputOrderModel.getOrderDeliveryStatus())) {
			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, String.format(BusinessAction.messageForKeyAdmin("errorOrderStatusUpdate"), inputOrderModel.getOrderDeliveryStatus()));
			return output;
		}

		if (currentDbOrderModel.isSelfDeliveryWithinXKm() && inputOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER)) {
			inputOrderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_OWN_DRIVER);
		}

		String type = null;
		String message = null;

		switch (inputOrderModel.getOrderDeliveryStatus()) {

		// when the current order delivery status is new
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_REJECTED:

			markOrderRejected(currentDbOrderModel, loggedInUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successOrderStatusChange");

			break;

		// when the current order delivery status is new
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER:

			markOrderAcceptedPlatformDriver(currentDbOrderModel, loggedInUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successOrderStatusChange");

			sendEndOtpForDeliverySmsNotification(currentDbOrderModel);

			break;

		// when the current order delivery status is new
		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_OWN_DRIVER:

			currentDbOrderModel.setDelieveryManagedByVendorDriver(true);
			currentDbOrderModel.updateDelieveryManagedByVendorDriver(loggedInUserId);

			markOrderAcceptedOwnDriver(currentDbOrderModel, loggedInUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successOrderStatusChange");

			break;

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED:

//			if (!(currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ACCEPTED)
//						|| currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP))) {
//
//				type = ProjectConstants.STATUS_ERROR;
//				message = String.format(BusinessAction.messageForKeyAdmin("errorJobStatusUpdateStarted"), OrderUtils.getOrderStatusDisplayLabels(currentDbOrderModel.getOrderDeliveryStatus()));
//			} else {

			OrderUtils.markOrderStarted(inputOrderModel, loggedInUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successJobStatusUpdateStarted");
//			}

			break;

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_DELIVERED:

			if (!currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED)) {

				type = ProjectConstants.STATUS_ERROR;
				message = String.format(BusinessAction.messageForKeyAdmin("errorJobStatusUpdateDelivered"), OrderUtils.getOrderStatusDisplayLabels(currentDbOrderModel.getOrderDeliveryStatus()));

			} else {

				OrderUtils.markOrderDelivered(currentDbOrderModel, loggedInUserId);

				type = ProjectConstants.STATUS_SUCCESS;
				message = BusinessAction.messageForKeyAdmin("successJobStatusUpdateDelivered");
			}

			break;

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_CANCELLED:

			if (currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED)) {
				OrderUtils.markOrderCancelledByDriver(currentDbOrderModel, loggedInUserId);
			} else {
				OrderUtils.markOrderToBeProcessedByCronJonAgain(inputOrderModel, loggedInUserId);
			}

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successJobStatusUpdateDriverCancelled");

			break;

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_VENDOR_CANCELLED:

			OrderUtils.markOrderCancelledByVendor(currentDbOrderModel, loggedInUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successOrderVendorCancelled");
			MyHubNotificationUtils.sendFCMPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_VENDOR_CANCELLED, message);
			
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

	private static void sendEndOtpForDeliverySmsNotification(OrderModel currentDbOrderModel) {

		UserProfileModel passengerModel = UserProfileModel.getAdminUserAccountDetailsById(currentDbOrderModel.getOrderUserId());

		String smsForEndOtp = String.format(BusinessAction.messageForKeyAdmin("successDeliverEndOtp"), currentDbOrderModel.getOrderShortId(), currentDbOrderModel.getEndOtp());
		MetamorphSystemsSmsUtils.sendSmsToSingleUser(smsForEndOtp, passengerModel.getPhoneNoCode() + passengerModel.getPhoneNo(), ProjectConstants.SMSConstants.SMS_DELIVERY_END_OTP_TEMPLATE_ID);

		//MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderUserId(), smsForEndOtp);
		String smsForOtp = String.format(BusinessAction.messageForKeyAdmin("successDeliverOtp"), currentDbOrderModel.getEndOtp());
		MyHubNotificationUtils.sendFCMPushNotificationToUser(currentDbOrderModel.getOrderUserId(), "OTP", smsForOtp);
	}

	public static Map<String, Object> updateOrderDeliveryStatusByDriverApiOnly(OrderModel inputOrderModel, String loggedInDriverUserId, String otpDelivery, String endOtp) {

		// Only driver can do the following with order delivery status,
		// 1. Arrived at pickup
		// 2. Driver Started
		// 3. Driver Delivered
		// 4. Driver Cancelled
		// from API ONLY

		Map<String, Object> output = new HashMap<>();

		OrderModel currentDbOrderModel = OrderModel.getOrderDetailsByOrderId(inputOrderModel.getOrderId());

		// if same order status is being updated, then send error
		if (currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(inputOrderModel.getOrderDeliveryStatus())) {
			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, String.format(BusinessAction.messageForKeyAdmin("errorOrderStatusUpdate"), inputOrderModel.getOrderDeliveryStatus()));
			return output;
		}

		// check if order assigned driver and logged in driver changing status are same.
		if (!loggedInDriverUserId.equalsIgnoreCase(currentDbOrderModel.getDriverId())) {
			output.put(ProjectConstants.STATUS_TYPE, ProjectConstants.STATUS_ERROR);
			output.put(ProjectConstants.STATUS_MESSAGE, BusinessAction.messageForKeyAdmin("errorOrderAssignedToAnotherDriver"));
			return output;
		}

		String type = null;
		String message = null;
		String notificationMessage = null;

		switch (inputOrderModel.getOrderDeliveryStatus()) {

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP:

			if (!currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ACCEPTED)) {
				type = ProjectConstants.STATUS_ERROR;
				message = String.format(BusinessAction.messageForKeyAdmin("errorJobStatusUpdateArrivedAtPickup"), OrderUtils.getOrderStatusDisplayLabels(currentDbOrderModel.getOrderDeliveryStatus()));
			} else {

				OrderUtils.markOrderArrivedAtPickup(inputOrderModel, loggedInDriverUserId);

				type = ProjectConstants.STATUS_SUCCESS;
				message = BusinessAction.messageForKeyAdmin("successJobStatusUpdateArrivedAtPickup");

				notificationMessage = String.format(BusinessAction.messageForKeyAdmin("notificationJobStatusUpdateArrivedAtPickup"), currentDbOrderModel.getOrderShortId());
				MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderReceivedAgainstVendorId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP, notificationMessage);
				//MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP, notificationMessage);
				MyHubNotificationUtils.sendFCMPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP, notificationMessage);
			}

			break;

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED:

//			if (!(currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ACCEPTED)
//						|| currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP))) {
//
//				type = ProjectConstants.STATUS_ERROR;
//				message = String.format(BusinessAction.messageForKeyAdmin("errorJobStatusUpdateStarted"), OrderUtils.getOrderStatusDisplayLabels(currentDbOrderModel.getOrderDeliveryStatus()));
//			} else {

			OrderUtils.markOrderStarted(inputOrderModel, loggedInDriverUserId);

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successJobStatusUpdateStarted");

			notificationMessage = String.format(BusinessAction.messageForKeyAdmin("notificationJobStatusUpdateStarted"), currentDbOrderModel.getOrderShortId());
			MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderReceivedAgainstVendorId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED, notificationMessage);
			// MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED, notificationMessage);
			MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED, notificationMessage);
//			}

			break;

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_DELIVERED:

			if (!currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED)) {

				type = ProjectConstants.STATUS_ERROR;
				message = String.format(BusinessAction.messageForKeyAdmin("errorJobStatusUpdateDelivered"), OrderUtils.getOrderStatusDisplayLabels(currentDbOrderModel.getOrderDeliveryStatus()));

			} else {

				boolean isOtpValid = false;

				if (StringUtils.validString(otpDelivery) && StringUtils.booleanValueOf(otpDelivery)) {
					isOtpValid = currentDbOrderModel.getEndOtp().equalsIgnoreCase(endOtp);
				} else {
					isOtpValid = true;
				}

				if (isOtpValid) {

					OrderUtils.markOrderDelivered(currentDbOrderModel, loggedInDriverUserId);

					type = ProjectConstants.STATUS_SUCCESS;
					message = BusinessAction.messageForKeyAdmin("successJobStatusUpdateDelivered");

					notificationMessage = String.format(BusinessAction.messageForKeyAdmin("notificationJobStatusUpdateDelivered"), currentDbOrderModel.getOrderShortId());
					MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderReceivedAgainstVendorId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_DELIVERED, notificationMessage);
					// MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_DELIVERED, notificationMessage);
					MyHubNotificationUtils.sendFCMPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_DELIVERED, notificationMessage);

				} else {

					type = ProjectConstants.STATUS_ERROR;
					message = BusinessAction.messageForKeyAdmin("errorInvalidOtp");
				}
			}

			break;

		case ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_CANCELLED:

			if (currentDbOrderModel.getOrderDeliveryStatus().equalsIgnoreCase(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED)) {
				OrderUtils.markOrderCancelledByDriver(currentDbOrderModel, loggedInDriverUserId);
			} else {
				OrderUtils.markOrderToBeProcessedByCronJonAgain(inputOrderModel, loggedInDriverUserId);
			}

			type = ProjectConstants.STATUS_SUCCESS;
			message = BusinessAction.messageForKeyAdmin("successJobStatusUpdateDriverCancelled");

			notificationMessage = String.format(BusinessAction.messageForKeyAdmin("notificationJobStatusUpdateDriverCancelled"), currentDbOrderModel.getOrderShortId());
			MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderReceivedAgainstVendorId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_CANCELLED, notificationMessage);
			// MyHubNotificationUtils.sendPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_CANCELLED, notificationMessage);
			MyHubNotificationUtils.sendFCMPushNotificationToUser(currentDbOrderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_CANCELLED, notificationMessage);

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

	private static void markOrderRejected(OrderModel orderModel, String loggedInUserId) {

		updateOrderDeliveryStatus(loggedInUserId, orderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_REJECTED);

		revertProductInventoryCount(loggedInUserId, orderModel.getOrderId());
	}

	private static void markOrderAcceptedOwnDriver(OrderModel orderModel, String loggedInUserId) {
		updateOrderDeliveryStatus(loggedInUserId, orderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_OWN_DRIVER);
	}

	private static void markOrderAcceptedPlatformDriver(OrderModel orderModel, String loggedInUserId) {
		updateOrderDeliveryStatus(loggedInUserId, orderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER);
	}

	private static void markOrderArrivedAtPickup(OrderModel orderModel, String loggedInUserId) {
		updateOrderDeliveryStatus(loggedInUserId, orderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_ARRIVED_AT_PICKUP);
	}

	private static void markOrderStarted(OrderModel orderModel, String loggedInUserId) {
		updateOrderDeliveryStatus(loggedInUserId, orderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_STARTED);
	}

	private static void markOrderDelivered(OrderModel currentDbOrderModel, String loggedInUserId) {

		updateOrderDeliveryStatus(loggedInUserId, currentDbOrderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_DELIVERED);

		currentDbOrderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
		currentDbOrderModel.updatePaymentStatus(loggedInUserId);

		DriverTourStatusUtils.updateDriverTourStatus(currentDbOrderModel.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);
	}

	private static void markOrderCancelledByDriver(OrderModel currentDbOrderModel, String loggedInUserId) {

		updateOrderDeliveryStatus(loggedInUserId, currentDbOrderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_DRIVER_CANCELLED);

		revertProductInventoryCount(loggedInUserId, currentDbOrderModel.getOrderId());

		currentDbOrderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
		currentDbOrderModel.updatePaymentStatus(loggedInUserId);

		DriverTourStatusUtils.updateDriverTourStatus(currentDbOrderModel.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);
	}

	private static void markOrderCancelledByVendor(OrderModel currentDbOrderModel, String loggedInUserId) {

		updateOrderDeliveryStatus(loggedInUserId, currentDbOrderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_VENDOR_CANCELLED);

		revertProductInventoryCount(loggedInUserId, currentDbOrderModel.getOrderId());

		currentDbOrderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
		currentDbOrderModel.updatePaymentStatus(loggedInUserId);

		DriverTourStatusUtils.updateDriverTourStatus(currentDbOrderModel.getDriverId(), ProjectConstants.DRIVER_FREE_STATUS);
	}

	private static void markOrderToBeProcessedByCronJonAgain(OrderModel inputOrderModel, String driverId) {

		updateOrderDeliveryStatus(driverId, inputOrderModel, ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_ACCEPTED_PLATFORM_DRIVER);

		DriverTourStatusUtils.updateDriverTourStatus(driverId, ProjectConstants.DRIVER_FREE_STATUS);

		OrderUtils.updateDriverIdAgainstOrder(inputOrderModel, ProjectConstants.DEFAULT_DRIVER_ID);
	}

	public static OrderModel setInputOrderModel(String orderId, String orderDeliveryStatus) {
		OrderModel inputOrderModel = new OrderModel();
		inputOrderModel.setOrderId(orderId);
		inputOrderModel.setOrderDeliveryStatus(orderDeliveryStatus);
		return inputOrderModel;
	}

	public static void formOrdersNGBData(OrderModel orderModel, Map<String, Object> outPutMap) {

		if (orderModel != null) {

			orderModel.setOrderItemList(new ArrayList<>());

			Map<String, Object> orderDetails = new HashMap<String, Object>();

			orderDetails.put("orderId", orderModel.getOrderId());
			orderDetails.put("orderShortId", orderModel.getOrderShortId());
			orderDetails.put("orderUserId", orderModel.getOrderUserId());
			orderDetails.put("orderReceivedAgainstVendorId", orderModel.getOrderReceivedAgainstVendorId());
			orderDetails.put("orderCreationTime", orderModel.getOrderCreationTime());
			orderDetails.put("orderDeliveryStatus", orderModel.getOrderDeliveryStatus());
			orderDetails.put("orderDeliveryAddress", orderModel.getOrderDeliveryAddress());
			orderDetails.put("orderDeliveryAddressGeolocation", orderModel.getOrderDeliveryAddressGeolocation());
			orderDetails.put("orderDeliveryLat", orderModel.getOrderDeliveryLat());
			orderDetails.put("orderDeliveryLng", orderModel.getOrderDeliveryLng());
			orderDetails.put("orderPromoCodeId", orderModel.getOrderPromoCodeId());
			orderDetails.put("orderPromoCodeDiscount", orderModel.getOrderPromoCodeDiscount());
			orderDetails.put("orderTotal", orderModel.getOrderTotal());
			orderDetails.put("orderDeliveryCharges", orderModel.getOrderDeliveryCharges());
			orderDetails.put("orderCharges", orderModel.getOrderCharges());
			orderDetails.put("orderDeliveryDistance", orderModel.getOrderDeliveryDistance());
			orderDetails.put("paymentMode", orderModel.getPaymentMode());
			orderDetails.put("paymentStatus", orderModel.getPaymentStatus());
			orderDetails.put("orderNumberOfItems", orderModel.getOrderNumberOfItems());
			orderDetails.put("vendorStoreId", orderModel.getVendorStoreId());
			orderDetails.put("isDelieveryManagedByVendorDriver", orderModel.isDelieveryManagedByVendorDriver());
			orderDetails.put("driverId", orderModel.getDriverId());
			orderDetails.put("multicityCityRegionId", orderModel.getMulticityCityRegionId());
			orderDetails.put("multicityCountryId", orderModel.getMulticityCountryId());
			orderDetails.put("carTypeId", orderModel.getCarTypeId());
			orderDetails.put("promoCode", orderModel.getPromoCode());
			orderDetails.put("customerName", orderModel.getCustomerName());
			orderDetails.put("customerPhoneNo", orderModel.getCustomerPhoneNo());
			orderDetails.put("customerPhoneNoCode", orderModel.getCustomerPhoneNoCode());
			orderDetails.put("storeName", orderModel.getStoreName());
			orderDetails.put("vendorName", orderModel.getVendorName());
			orderDetails.put("storeAddressLat", orderModel.getStoreAddressLat());
			orderDetails.put("storeAddressLng", orderModel.getStoreAddressLng());
			orderDetails.put("storeAddress", orderModel.getStoreAddress());
			orderDetails.put("orderJobCancellationTimeHours", orderModel.getOrderJobCancellationTimeHours());
			orderDetails.put("orderNewCancellationTimeHours", orderModel.getOrderNewCancellationTimeHours());
			orderDetails.put("isSelfDeliveryWithinXKm", orderModel.isSelfDeliveryWithinXKm());
			orderDetails.put("endOtp", orderModel.getEndOtp());
			orderDetails.put("paymentToken", orderModel.getPaymentToken());
			orderDetails.put("paymentTokenGeneratedTime", orderModel.getPaymentTokenGeneratedTime());

			outPutMap.put("orderId", orderModel.getOrderId());
			outPutMap.put("orderModel", orderDetails);
			outPutMap.put("jobExpireTime", ProjectConstants.JOB_EXPIRE_TIME_ORDERS);
		} else {
			outPutMap.put("orderId", null);
			outPutMap.put("orderModel", null);
		}
	}

	public static List<Map<String, Object>> getListOfHighestSpendingCustomers(InputModel inputModel, String vendorId, List<String> vendorStoreIdList, long startTime, long endTime) {

		Set<String> userIds = new HashSet<>();
		Map<String, Double> userRevenueLookupMap = new HashMap<>();
		Map<String, Long> userNoOfOrdersLookupMap = new HashMap<>();
		List<Map<String, Object>> highestSpendingCustomersList = new ArrayList<>();

		List<StatsModel> orderListForHighestSpendingCustomers = OrderModel.getOrderListForHighestSpendingCustomers(vendorId, vendorStoreIdList, startTime, endTime, inputModel.getHighestSpendingCustomersStart(), inputModel.getHighestSpendingCustomersLength());
		for (StatsModel statsModel : orderListForHighestSpendingCustomers) {
			userIds.add(statsModel.getUserId());
			userRevenueLookupMap.put(statsModel.getUserId(), statsModel.getRevenue());
			userNoOfOrdersLookupMap.put(statsModel.getUserId(), statsModel.getNumberOfOrders());
		}

		List<StatsModel> appointmentListForHighestSpendingCustomers = AppointmentModel.getAppointmentListForHighestSpendingCustomers(vendorId, vendorStoreIdList, startTime, endTime, inputModel.getHighestSpendingCustomersStart(),
					inputModel.getHighestSpendingCustomersLength());
		for (StatsModel statsModel : appointmentListForHighestSpendingCustomers) {
			userIds.add(statsModel.getUserId());

			if (userRevenueLookupMap.containsKey(statsModel.getUserId())) {
				userRevenueLookupMap.put(statsModel.getUserId(), (statsModel.getRevenue() + userRevenueLookupMap.get(statsModel.getUserId())));
			} else {
				userRevenueLookupMap.put(statsModel.getUserId(), statsModel.getRevenue());
			}

			if (userNoOfOrdersLookupMap.containsKey(statsModel.getUserId())) {
				userNoOfOrdersLookupMap.put(statsModel.getUserId(), (statsModel.getNumberOfOrders() + userNoOfOrdersLookupMap.get(statsModel.getUserId())));
			} else {
				userNoOfOrdersLookupMap.put(statsModel.getUserId(), statsModel.getNumberOfOrders());
			}
		}

		if (!userIds.isEmpty()) {

			List<UserProfileModel> usersList = UserProfileModel.getUserDetailsForGorupOfIds(new ArrayList<>(userIds));

			for (UserProfileModel userProfileModel : usersList) {
				highestSpendingCustomersList.add(getUserLimitedData(userProfileModel, userRevenueLookupMap, userNoOfOrdersLookupMap));
			}
		}

		Comparator<Map<String, Object>> amountSpentTillDateComparator = new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> e1, Map<String, Object> e2) {
				Double v1 = Double.parseDouble(e1.get("amountSpentTillDate").toString());
				Double v2 = Double.parseDouble(e2.get("amountSpentTillDate").toString());
				return v2.compareTo(v1);
			}
		};

		Collections.sort(highestSpendingCustomersList, amountSpentTillDateComparator);

		if (highestSpendingCustomersList.size() > inputModel.getHighestSpendingCustomersLength()) {
			highestSpendingCustomersList = highestSpendingCustomersList.subList(0, inputModel.getHighestSpendingCustomersLength());
		}

		return highestSpendingCustomersList;
	}

	private static Map<String, Object> getUserLimitedData(UserProfileModel userProfileModel, Map<String, Double> userRevenueLookupMap, Map<String, Long> userNoOfOrdersLookupMap) {

		Map<String, Object> map = new HashMap<>();

		map.put("roleId", userProfileModel.getRoleId());
		map.put("userId", userProfileModel.getUserId());
		map.put("photoUrl", userProfileModel.getPhotoUrl());
		map.put("phoneNo", userProfileModel.getPhoneNo());
		map.put("phoneNoCode", userProfileModel.getPhoneNoCode());
		map.put("email", userProfileModel.getEmail());
		map.put("firstName", userProfileModel.getFirstName());
		map.put("lastName", userProfileModel.getLastName());

		if (userRevenueLookupMap != null && userRevenueLookupMap.containsKey(userProfileModel.getUserId())) {
			map.put("amountSpentTillDate", BusinessAction.df.format(userRevenueLookupMap.get(userProfileModel.getUserId())));
		} else {
			map.put("amountSpentTillDate", 0.0);
		}

		if (userNoOfOrdersLookupMap != null && userNoOfOrdersLookupMap.containsKey(userProfileModel.getUserId())) {
			map.put("numberOfOrders", BusinessAction.df.format(userNoOfOrdersLookupMap.get(userProfileModel.getUserId())));
		} else {
			map.put("numberOfOrders", 0);
		}

		return map;
	}

	private static Map<String, Object> getVendorProductLimitedData(VendorProductModel vendorProductModel, Map<String, Long> vendorProductSkuLookupMap, Map<String, Long> vendorProductNumberOfItemsOrderedLookupMap) {

		Map<String, Object> map = new HashMap<>();

		map.put("productName", vendorProductModel.getProductName());
		map.put("productInformation", vendorProductModel.getProductInformation());
		map.put("productActualPrice", Double.parseDouble(BusinessAction.df.format(vendorProductModel.getProductActualPrice())));
		map.put("productDiscountedPrice", Double.parseDouble(BusinessAction.df.format(vendorProductModel.getProductDiscountedPrice())));
		map.put("productWeight", vendorProductModel.getProductWeight());
		map.put("productWeightUnit", vendorProductModel.getProductWeightUnit());
		map.put("productSpecification", vendorProductModel.getProductSpecification());
		map.put("productImage", vendorProductModel.getProductImage());
		map.put("productSku", vendorProductModel.getProductSku());
		map.put("productCategory", vendorProductModel.getProductCategory());

		if (vendorProductSkuLookupMap != null && vendorProductSkuLookupMap.containsKey(vendorProductModel.getProductSku())) {
			map.put("productSkuCount", vendorProductSkuLookupMap.get(vendorProductModel.getProductSku()));
		} else {
			map.put("productSkuCount", 0);
		}

		if (vendorProductNumberOfItemsOrderedLookupMap != null && vendorProductNumberOfItemsOrderedLookupMap.containsKey(vendorProductModel.getProductSku())) {
			map.put("numberOfItemsOrdered", vendorProductNumberOfItemsOrderedLookupMap.get(vendorProductModel.getProductSku()));
		} else {
			map.put("numberOfItemsOrdered", 0);
		}

		return map;
	}

	public static List<Map<String, Object>> getTrendingProductList(InputModel inputModel, String vendorId, List<String> vendorStoreIdList, long startTime, long endTime) {

		List<String> productSkus = new ArrayList<>();
		Map<String, Long> vendorProductSkuLookupMap = new HashMap<>();
		Map<String, Long> vendorProductNumberOfItemsOrderedLookupMap = new HashMap<>();
		List<Map<String, Object>> trendingProductsList = new ArrayList<>();

		List<StatsModel> orderItemsWithTrendingProductSkuForOrders = OrderItemModel.getOrderItemListForTrendingProductSkuForOrders(vendorId, vendorStoreIdList, startTime, endTime, inputModel.getTrendingProductsStart(), inputModel.getTrendingProductsLength());
		List<StatsModel> orderItemsWithTrendingProductSkuForAppointments = OrderItemModel.getOrderItemListForTrendingProductSkuForAppointments(vendorId, vendorStoreIdList, startTime, endTime, inputModel.getTrendingProductsStart(), inputModel.getTrendingProductsLength());
		logger.info("\n\n\n\n\torderItemsWithTrendingProductSkuForOrders size :: " + orderItemsWithTrendingProductSkuForOrders.size());
		logger.info("\n\n\n\n\torderItemsWithTrendingProductSkuForAppointments size :: " + orderItemsWithTrendingProductSkuForAppointments.size());

		for (StatsModel statsModel : orderItemsWithTrendingProductSkuForOrders) {
			logger.info("\nstatsModel.getProductSku() :: " + statsModel.getProductSku());
			productSkus.add(statsModel.getProductSku());
			vendorProductSkuLookupMap.put(statsModel.getProductSku(), statsModel.getProductSkuCount());
			vendorProductNumberOfItemsOrderedLookupMap.put(statsModel.getProductSku(), statsModel.getNumberOfItemsOrdered());
		}

		for (StatsModel statsModel : orderItemsWithTrendingProductSkuForAppointments) {
			logger.info("\nstatsModel.getProductSku() :: " + statsModel.getProductSku());
			productSkus.add(statsModel.getProductSku());
			vendorProductSkuLookupMap.put(statsModel.getProductSku(), statsModel.getProductSkuCount());
			vendorProductNumberOfItemsOrderedLookupMap.put(statsModel.getProductSku(), statsModel.getNumberOfItemsOrdered());
		}

		logger.info("\nproductSkus :: " + productSkus);
		logger.info("\nvendorProductLookupMap :: " + vendorProductSkuLookupMap);

		Set<String> processedSkuInList = new HashSet<>();

		if (!productSkus.isEmpty()) {

			List<VendorProductModel> vendorProductList = VendorProductModel.getVendorProductListByProductSkus(productSkus, vendorId, vendorStoreIdList);

			for (VendorProductModel vendorProductModel : vendorProductList) {

				if (!processedSkuInList.contains(vendorProductModel.getProductSku())) {
					trendingProductsList.add(getVendorProductLimitedData(vendorProductModel, vendorProductSkuLookupMap, vendorProductNumberOfItemsOrderedLookupMap));
					processedSkuInList.add(vendorProductModel.getProductSku());
				}
			}
		}

		Comparator<Map<String, Object>> productSkuCountComparator = new Comparator<Map<String, Object>>() {

			@Override
			public int compare(Map<String, Object> e1, Map<String, Object> e2) {
				Long v1 = Long.parseLong(e1.get("productSkuCount").toString());
				Long v2 = Long.parseLong(e2.get("productSkuCount").toString());
				return v2.compareTo(v1);
			}
		};

		Collections.sort(trendingProductsList, productSkuCountComparator);

		return trendingProductsList;
	}

	public static Map<String, Object> getVendorDashboardData(InputModel inputModel, String loggedInuserId) {

		long startTime = 0;
		Instant todayInstantObject = DateUtils.getNowInstant();
		long endTime = DateUtils.atEndOfDay(todayInstantObject);

		switch (inputModel.getFilterDayType()) {

		case ProjectConstants.DATE_FILTER.ALL:

			startTime = 0;
			endTime = 0;

			break;

		case ProjectConstants.DATE_FILTER.TODAY:

			startTime = DateUtils.atStartOfDay(todayInstantObject);
			endTime = DateUtils.atEndOfDay(todayInstantObject);

			break;

		case ProjectConstants.DATE_FILTER.THIS_WEEK:

			startTime = DateUtils.getStartOfWeek();
			endTime = DateUtils.getEndOfWeek();

			break;
		case ProjectConstants.DATE_FILTER.THIS_MONTH:

			startTime = DateUtils.getStartOfMonth();
			endTime = DateUtils.getEndOfMonth();

			break;
		case ProjectConstants.DATE_FILTER.CUSTOM_LAST_X_DAYS:

			todayInstantObject = todayInstantObject.minus(inputModel.getNumberOfLastXDays(), ChronoUnit.DAYS);
			startTime = DateUtils.atStartOfDay(todayInstantObject);

			break;
		default:
			startTime = 0;
			endTime = 0;
			break;
		}

		logger.info("\n\n\n\n\tstartTime :: " + startTime);
		logger.info("\n\n\n\n\tendTime   :: " + endTime);

		Map<String, Object> outputMap = new HashMap<>();
		outputMap.put("trendingProductsList", new ArrayList<>());
		outputMap.put("revenue", BusinessAction.df.format(0));
		outputMap.put("numberOfOrders", 0);
		outputMap.put("numberOfSubscribers", 0);
		outputMap.put("discountDiff", BusinessAction.df.format(0));
		outputMap.put("highestSpendingCustomersList", 0);

		UserProfileModel loggedInUserModel = UserProfileModel.getAdminUserAccountDetailsById(loggedInuserId);

		String vendorId = loggedInuserId;
		if (UserRoleUtils.isSubVendorRole(loggedInUserModel.getRoleId())) {
			vendorId = UserRoleUtils.getParentVendorId(loggedInuserId);
		}

		List<String> vendorStoreIdList = new ArrayList<>();

		// for vendor and sub vendor -> if the specific vendor store id is sent then
		// return the data only for that store
		if (inputModel.getVendorStoreId() != null && !ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE.equalsIgnoreCase(inputModel.getVendorStoreId())) {
			vendorStoreIdList.add(inputModel.getVendorStoreId());
		} else {

			// if vendor store id from UI is -1, then send all the stores assigned to the
			// sub vendor
			if (UserRoleUtils.isSubVendorRole(loggedInUserModel.getRoleId())) {

				// by default send it all for sub vendor
				vendorStoreIdList = VendorStoreSubVendorUtils.getVendorStoresAssignedToTheSubVendor(loggedInuserId, false);

				if (vendorStoreIdList.isEmpty()) {
					return outputMap;
				}

			} else {
				// if vendor store id from UI is -1, then send all the data irrespective of
				// vendor store
				vendorStoreIdList = null;
			}
		}

		List<Map<String, Object>> trendingProductsList = OrderUtils.getTrendingProductList(inputModel, vendorId, vendorStoreIdList, startTime, endTime);
		outputMap.put("trendingProductsList", trendingProductsList);

		if (!inputModel.isOnlyTrendingProductsApiCall()) {

			StatsModel orderStatsModel = OrderModel.getOrderStatsByTimeForVendor(vendorId, vendorStoreIdList, startTime, endTime);
			StatsModel appointmentStatsModel = AppointmentModel.getAppointmentStatsByTimeForVendor(vendorId, vendorStoreIdList, startTime, endTime);
			double revenue = orderStatsModel.getRevenue() + appointmentStatsModel.getRevenue();
			long numberOfOrders = orderStatsModel.getNumberOfOrders() + appointmentStatsModel.getNumberOfOrders();

			StatsModel orderItemStatsModelForOrders = OrderItemModel.getOrderItemStatsByTimeForVendorForOrders(vendorId, vendorStoreIdList, startTime, endTime);
			StatsModel orderItemStatsModelForAppointments = OrderItemModel.getOrderItemStatsByTimeForVendorForAppointments(vendorId, vendorStoreIdList, startTime, endTime);
			double discountDiff = orderItemStatsModelForOrders.getDiscountDiff() + orderItemStatsModelForAppointments.getDiscountDiff();

			StatsModel vendorSubscriberStatsModel = VendorSubscriberModel.getSubscriberStatsByTimeForVendor(vendorId, startTime, endTime);
			List<Map<String, Object>> highestSpendingCustomersList = OrderUtils.getListOfHighestSpendingCustomers(inputModel, vendorId, vendorStoreIdList, startTime, endTime);

			outputMap.put("revenue", BusinessAction.df.format(revenue));
			outputMap.put("numberOfOrders", numberOfOrders);
			outputMap.put("numberOfSubscribers", vendorSubscriberStatsModel.getNumberOfSubscribers());
			outputMap.put("discountDiff", BusinessAction.df.format(discountDiff));
			outputMap.put("highestSpendingCustomersList", highestSpendingCustomersList);
		}

		return outputMap;
	}

	public static void sendOrderExpireNotificationToUser(OrderModel orderModel) {

		String notificationMessage = String.format(BusinessAction.messageForKeyAdmin("notificationOrderExpired"), orderModel.getOrderShortId());
		MyHubNotificationUtils.sendPushNotificationToUser(orderModel.getOrderUserId(), ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_EXPIRED, notificationMessage);
	}

	public static String formProductSKU(VendorProductModel vendorProductModel) {

		String productSku = vendorProductModel.getVendorId().concat("-").concat(MyHubUtils.removeWhiteSpaces(vendorProductModel.getProductName())).concat("-").concat(vendorProductModel.getProductWeight() + "").concat("-")
					.concat(MyHubUtils.removeWhiteSpaces(OrderUtils.getProductWeightUnit(vendorProductModel.getProductWeightUnit())));

		return productSku;
	}

	public static String getProductWeightUnit(int productWeightUnit) {

		String temp = "";

		switch (productWeightUnit) {
		case ProjectConstants.WeightConstants.GRAMS_ID:
			temp = ProjectConstants.WeightConstants.GRAMS;
			break;
		case ProjectConstants.WeightConstants.KILOGRAMS_ID:
			temp = ProjectConstants.WeightConstants.KILOGRAMS;
			break;
		case ProjectConstants.WeightConstants.MILLILITERS_ID:
			temp = ProjectConstants.WeightConstants.MILLILITERS;
			break;
		case ProjectConstants.WeightConstants.LITERS_ID:
			temp = ProjectConstants.WeightConstants.LITERS;
			break;
		default:
			break;
		}

		return (" " + temp);
	}

	public static List<OrderModel> getDeliveredOrdersByvendorIdAndOrderDeliveryStatusAndUpdatedAt(String vendorId, List<String> orderDeliveryStatusList, String startDate, String endDate) {

		long startDateLong = DateUtils.getStartOfDayDatatableUpdated(startDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, TimeZoneUtils.getTimeZone());
		long endDateLong = DateUtils.getEndOfDayDatatableUpdated(endDate, DateUtils.DATATABLE_DATE_FORMAT_PARSE, TimeZoneUtils.getTimeZone());

		List<OrderModel> orderModelList = OrderModel.getDeliveredOrdersByvendorIdAndOrderDeliveryStatusAndUpdatedAt(vendorId, orderDeliveryStatusList, startDateLong, endDateLong);
		return orderModelList;
	}

	public static List<OrderModel> placeStoreOrders(List<OrderModel> orderModelList, String loggedInUserId) {

		for (OrderModel orderModel : orderModelList) {

			VendorStoreModel vsm = VendorStoreModel.getVendorStoreDetailsById(orderModel.getVendorStoreId());

			String multicityCityRegionId = MultiCityAction.getMulticityRegionId(vsm.getStoreAddressLat(), vsm.getStoreAddressLng());

			List<String> vendorProductIds = new ArrayList<>();
			for (OrderItemModel orderItemModel : orderModel.getOrderItemList()) {
				vendorProductIds.add(orderItemModel.getVendorProductId());
			}

			List<VendorProductModel> vpmList = VendorProductModel.getProductDetailsByVendorProductsIdsAndVendorStoreIdList(vendorProductIds, orderModel.getVendorStoreId());

//			int orderNumberOfItems = 0;
//			double orderPromoCodeDiscount = 0;
//			double orderTotal = 0;
//			double orderDeliveryCharges = 0;
//			double orderCharges = 0;

			String orderId = UUIDGenerator.generateUUID();

			System.out.println("*** vpm list ***" + vpmList.size());
			for (VendorProductModel vendorProductModel : vpmList) {

				long numberOfItems = 0;

				for (OrderItemModel orderItemModel : orderModel.getOrderItemList()) {

					if (orderItemModel.getVendorProductId().equalsIgnoreCase(vendorProductModel.getVendorProductId())) {

						numberOfItems = orderItemModel.getNumberOfItemsOrdered();

						long remainingProductInventoryCount = vendorProductModel.getProductInventoryCount() - numberOfItems;
						vendorProductModel.setProductInventoryCount(remainingProductInventoryCount);

						orderItemModel.setOrderItemId(UUIDGenerator.generateUUID());
						orderItemModel.setOrderId(orderId);
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
						orderItemModel.setCreatedBy(orderItemModel.getCreatedBy());
						orderItemModel.setUpdatedBy(orderItemModel.getUpdatedBy());
						orderItemModel.setCreatedAt(orderItemModel.getCreatedAt());
						orderItemModel.setUpdatedAt(orderItemModel.getUpdatedAt());

					}
				}

				orderModel.setOrderId(orderId);
				orderModel.setOrderUserId(orderModel.getOrderUserId());
				orderModel.setOrderCreationTime(orderModel.getOrderCreationTime());
				orderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_OFFLINE);
				// orderModel.setOrderDeliveryAddressGeolocation(vsm.getStoreAddressGeolocation());
				// orderModel.setOrderPromoCodeDiscount(Double.parseDouble(BusinessAction.df.format(orderPromoCodeDiscount)));
				orderModel.setOrderTotal(orderModel.getOrderTotal());
				// orderModel.setOrderDeliveryCharges(Double.parseDouble(BusinessAction.df.format(orderDeliveryCharges)));
				// orderModel.setOrderCharges(Double.parseDouble(BusinessAction.df.format(orderCharges)));
				// orderModel.setOrderDeliveryDistance(Double.parseDouble(BusinessAction.df.format(orderDeliveryDistance)));
				orderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
				orderModel.setOrderNumberOfItems(orderModel.getOrderNumberOfItems());
				orderModel.setMulticityCityRegionId(multicityCityRegionId);
				orderModel.setRecordStatus(ProjectConstants.ACTIVATE_STATUS);
				orderModel.setMulticityCountryId(ProjectConstants.DEFAULT_COUNTRY_ID);
				orderModel.setCarTypeId(ProjectConstants.CAR_TYPES.BIKE_ID);
				orderModel.setServiceTypeId(ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);
				orderModel.setOrderDeliveryAddressGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + vsm.getStoreAddressLat() + "  " + vsm.getStoreAddressLng() + ")')");

				orderModel = OrderUtils.placeStoreOrder(orderModel);

				orderModel.setOrderDeliveryAddressGeolocation(vsm.getStoreAddressGeolocation());
				VendorProductModel.updateProductInventoryCount(loggedInUserId, vpmList);
			}

		}
		return orderModelList;
	}

	public static OrderModel placeStoreOrder(OrderModel orderModel) {

		orderModel = orderModel.insertStoreOrder();

		return orderModel;
	}

	public static Map<String, Object> getKPMartDashboardData(String vendorId, String vendorStoreId, String loggedInUserId) {

		long currentDate = DateUtils.nowAsGmtMillisec();
		long previousMonth = DateUtils.getPreviousMonth();
		long previousThreeMonth = DateUtils.getPreviousThreeMonth();

		Map<String, Object> dashboardOutput = new HashMap<>();
		List<String> trendingProductSkus = new ArrayList<>();
		List<String> popularProductSkus = new ArrayList<>();
		List<String> previousOrderItemProductSkus = new ArrayList<>();
		List<VendorProductModel> trendingProductList = new ArrayList<>();
		List<VendorProductModel> popularProductList = new ArrayList<>();
		List<VendorProductModel> previousOrderProductList = new ArrayList<>();

		// Preparing Trending products list

		List<StatsModel> orderItemsWithTrendingProductSku = OrderItemModel.getOrderItemListForKPMartTrendingProductSku(vendorId, vendorStoreId, currentDate, previousMonth);

		logger.info("\n\n\n\n\torderItemsWithTrendingProductSku size :: " + orderItemsWithTrendingProductSku.size());

		for (StatsModel statsModel : orderItemsWithTrendingProductSku) {
			trendingProductSkus.add(statsModel.getProductSku());
		}

		if (!trendingProductSkus.isEmpty()) {
			// trendingProductList =
			// VendorProductModel.getVendorProductListByProductSkus(trendingProductSkus,
			// vendorId, vendorStoreId);
			trendingProductList = VendorProductModel.getVendorProductListForKPMARTByProductSkus(trendingProductSkus, vendorId, vendorStoreId);
		}

		// Preparing Popular products list

		List<StatsModel> orderItemsWithPopularProductSku = OrderItemModel.getOrderItemListForKPMartPopularProductSku(vendorId, vendorStoreId, currentDate, previousThreeMonth);
		for (StatsModel statsModel : orderItemsWithPopularProductSku) {
			popularProductSkus.add(statsModel.getProductSku());
		}
		if (!popularProductSkus.isEmpty()) {

			// popularProductList =
			// VendorProductModel.getVendorProductListByProductSkus(popularProductSkus,
			// vendorId, vendorStoreId);
			popularProductList = VendorProductModel.getVendorProductListForKPMARTByProductSkus(popularProductSkus, vendorId, vendorStoreId);

		}

		// Preparing Newly add products list

		List<VendorProductModel> newlyAddProductsList = VendorProductModel.getNewlyAddedProductsList(vendorId, vendorStoreId);

		// Preparing Previous Order items list

		List<StatsModel> previousOrderItemsProductSku = OrderItemModel.getPreviousOrderItemListByUser(vendorId, vendorStoreId, loggedInUserId);
		for (StatsModel statsModel : previousOrderItemsProductSku) {
			previousOrderItemProductSkus.add(statsModel.getProductSku());
		}
		if (!previousOrderItemProductSkus.isEmpty()) {

			// previousOrderProductList =
			// VendorProductModel.getVendorProductListByProductSkus(previousOrderItemProductSkus,
			// vendorId, vendorStoreId);
			previousOrderProductList = VendorProductModel.getVendorProductListForKPMARTByProductSkus(previousOrderItemProductSkus, vendorId, vendorStoreId);

		}

		// KP E MART Organic products

		List<VendorProductModel> organicProudctList = VendorProductModel.getOrganicProductsByVendorIdAndVendorStoreIdAndProductCategory(vendorId, vendorStoreId, "ORGANIC");

		dashboardOutput.put("trendingProductList", trendingProductList);
		dashboardOutput.put("popularProductList", popularProductList);
		dashboardOutput.put("newlyProductList", newlyAddProductsList);
		dashboardOutput.put("previousOrderItemProductList", previousOrderProductList);
		dashboardOutput.put("organicProductList", organicProudctList);
		return dashboardOutput;
	}

	public static void updateDeliveryOrder(CcavenueResponseLogModel ccavenueResponseLogModel) {

		logger.info("\n\n\n\tccavenueResponseLogModel updateDeliveryOrder\t" + ccavenueResponseLogModel.toString());

		OrderModel orderModel = OrderModel.getOrderDetailsByOrderId(ccavenueResponseLogModel.getDeliveryOrderId());
		orderModel.setPaymentStatus(ProjectConstants.OrderDeliveryConstants.ORDER_PAYMENT_SUCCESS);
		orderModel.setOrderDeliveryStatus(ProjectConstants.OrderDeliveryConstants.ORDER_STATUS_NEW);
		orderModel.updatePaymentAndOrderStatusForCCavenuePayment(ccavenueResponseLogModel.getUserId());

		String pushMessage = String.format(BusinessAction.messageForKeyAdmin("successOrderCcavenue"), orderModel.getOrderShortId(), orderModel.getOrderCharges());

		ApnsDeviceModel apnsDeviceOfDriver = ApnsDeviceModel.getDeviseByUserId(ccavenueResponseLogModel.getUserId());

		ApnsMessageModel apnsMessage = new ApnsMessageModel();
		apnsMessage.setMessage(pushMessage);
		apnsMessage.setMessageType("push");
		apnsMessage.setToUserId(ccavenueResponseLogModel.getUserId());
		apnsMessage.insertPushMessage();

		if (apnsDeviceOfDriver != null) {
			apnsDeviceOfDriver.sendNotification("1", "Push", pushMessage, ProjectConstants.PAYMENT_CONFIRMATION, WebappPropertyUtils.getWebAppProperty("certificatePath"));
		}
	}

	public static Map<String, Object> getNewKPMartDashboardData(String vendorId, String vendorStoreId, String loggedInUserId) {
		
		long currentDate = DateUtils.nowAsGmtMillisec();
		long previousMonth = DateUtils.getPreviousMonth();
		long previousThreeMonth = DateUtils.getPreviousThreeMonth();

		Map<String, Object> dashboardOutput = new HashMap<>();
		List<String> trendingProductIds = new ArrayList<>();
		List<String> popularProductIds = new ArrayList<>();
		List<String> previousOrderItemProductIds = new ArrayList<>();
		List<VendorProductModel> trendingProductList = new ArrayList<>();
		List<VendorProductModel> popularProductList = new ArrayList<>();
		List<VendorProductModel> previousOrderProductList = new ArrayList<>();

		// Preparing Trending products list

		List<StatsModel> orderItemsWithTrendingProducts = OrderItemModel.getOrderItemListForKPMartTrendingProducts(vendorId, vendorStoreId, currentDate, previousMonth);

		logger.info("\n\n\n\n\torderItemsWithTrendingProductSku size :: " + orderItemsWithTrendingProducts.size());

		for (StatsModel statsModel : orderItemsWithTrendingProducts) {
			trendingProductIds.add(statsModel.getVendorProductId());
		}

		if (!trendingProductIds.isEmpty()) {
			trendingProductList = VendorProductModel.getVendorProductListForKPMARTByProductIds(trendingProductIds, vendorId, vendorStoreId);
		}

		// Preparing Popular products list

		List<StatsModel> orderItemsWithPopularProductIds = OrderItemModel.getOrderItemListForKPMartPopularProducts(vendorId, vendorStoreId, currentDate, previousThreeMonth);
		for (StatsModel statsModel : orderItemsWithPopularProductIds) {
			popularProductIds.add(statsModel.getVendorProductId());
		}
		if (!popularProductIds.isEmpty()) {

			popularProductList = VendorProductModel.getVendorProductListForKPMARTByProductIds(popularProductIds, vendorId, vendorStoreId);

		}

		// Preparing Newly add products list

		List<VendorProductModel> newlyAddProductsList = VendorProductModel.getNewlyAddedProductIdsList(vendorId, vendorStoreId);

		// Preparing Previous Order items list

		List<StatsModel> previousOrderItemsProducts = OrderItemModel.getPreviousOrderItemProductIdsListByUser(vendorId, vendorStoreId, loggedInUserId);
		for (StatsModel statsModel : previousOrderItemsProducts) {
			previousOrderItemProductIds.add(statsModel.getVendorProductId());
		}
		if (!previousOrderItemProductIds.isEmpty()) {

			previousOrderProductList = VendorProductModel.getVendorProductListForKPMARTByProductIds(previousOrderItemProductIds, vendorId, vendorStoreId);

		}
		
		// KP E MART Organic products
		
		List<VendorProductModel> organicProudctList =  VendorProductModel.getNewOrganicProductsByVendorIdAndVendorStoreIdAndProductCategory(vendorId, vendorStoreId, "ORGANIC");

		dashboardOutput.put("trendingProductList", trendingProductList);
		dashboardOutput.put("popularProductList", popularProductList);
		dashboardOutput.put("newlyProductList", newlyAddProductsList);
		dashboardOutput.put("previousOrderItemProductList", previousOrderProductList);
		dashboardOutput.put("organicProductList", organicProudctList);
		return dashboardOutput;
	}
}