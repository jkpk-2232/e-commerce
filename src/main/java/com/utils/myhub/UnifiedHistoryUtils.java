package com.utils.myhub;

import java.util.ArrayList;
import java.util.List;

import com.utils.UUIDGenerator;
import com.webapp.ProjectConstants;
import com.webapp.models.AppointmentModel;
import com.webapp.models.OrderModel;
import com.webapp.models.TourModel;
import com.webapp.models.UnifiedHistoryModel;

public class UnifiedHistoryUtils {

	public static List<String> getStatusListAsPerType(String type) {

		List<String> statusList = new ArrayList<>();

		statusList.addAll(OrderUtils.getOrderStatusListAsPerOrderType(type));
		statusList.addAll(TourUtils.getTourStatusListAsPerType(type));

		return statusList;
	}

	public static void insertEntryForToursAndCouries(String tourId) {

		TourModel tourModel = TourModel.getTourDetailsByTourId(tourId);
		UnifiedHistoryModel unifiedHistoryModel = setToursData(tourModel);
		unifiedHistoryModel.insertUnifiedHistory(tourModel.getCreatedBy());
	}

	public static UnifiedHistoryModel setToursData(TourModel tourModel) {

		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();

		unifiedHistoryModel.setUnifiedHistoryId(UUIDGenerator.generateUUID());

		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setShortId(tourModel.getUserTourId());
		unifiedHistoryModel.setPassengerId(tourModel.getPassengerId());
		unifiedHistoryModel.setPassengerVendorId(tourModel.getPassengerVendorId());
		unifiedHistoryModel.setCarTypeId(tourModel.getCarTypeId());
		unifiedHistoryModel.setServiceTypeId(tourModel.getServiceTypeId());
		unifiedHistoryModel.setMulticityCityRegionId(tourModel.getMulticityCityRegionId());
		unifiedHistoryModel.setMulticityCountryId(tourModel.getMulticityCountryId());

		unifiedHistoryModel.setSourceAddress(tourModel.getSourceAddress());
		unifiedHistoryModel.setDestinationAddress(tourModel.getDestinationAddress());
		unifiedHistoryModel.setpFirstName(tourModel.getpFirstName());
		unifiedHistoryModel.setpLastName(tourModel.getpLastName());
		unifiedHistoryModel.setpEmail(tourModel.getpEmail());
		unifiedHistoryModel.setpPhone(tourModel.getpPhone());
		unifiedHistoryModel.setpPhoneCode(tourModel.getpPhoneCode());
		unifiedHistoryModel.setpPhotoUrl(tourModel.getpPhotoUrl());

		unifiedHistoryModel.setTourBookedBy(tourModel.getTourBookedBy());
		unifiedHistoryModel.setBookingType(tourModel.getBookingType());
		unifiedHistoryModel.setRideLaterPickupTime(tourModel.getRideLaterPickupTime());
		unifiedHistoryModel.setRideLater(tourModel.isRideLater());
		unifiedHistoryModel.setOrderReceivedAgainstVendorId(tourModel.getCourierOrderReceivedAgainstVendorId());

		unifiedHistoryModel.setCourierPickupAddress(tourModel.getCourierPickupAddress());
		unifiedHistoryModel.setCourierContactPersonName(tourModel.getCourierContactPersonName());
		unifiedHistoryModel.setCourierContactPhoneNo(tourModel.getCourierContactPhoneNo());
		unifiedHistoryModel.setCourierDropAddress(tourModel.getCourierDropAddress());
		unifiedHistoryModel.setCourierDropContactPersonName(tourModel.getCourierDropContactPersonName());
		unifiedHistoryModel.setCourierDropContactPhoneNo(tourModel.getCourierDropContactPhoneNo());
		unifiedHistoryModel.setCourierDetails(tourModel.getCourierDetails());

		unifiedHistoryModel.setDriverId(tourModel.getDriverId());
		unifiedHistoryModel.setDriverVendorId(tourModel.getDriverVendorId());
		unifiedHistoryModel.setCarId(tourModel.getCarId());

		unifiedHistoryModel.setTotal(tourModel.getTotal());
		unifiedHistoryModel.setCharges(tourModel.getCharges());
		unifiedHistoryModel.setStatus(tourModel.getStatus());
		unifiedHistoryModel.setPromoCodeId(tourModel.getPromoCodeId());
		unifiedHistoryModel.setPromoCodeApplied(tourModel.isPromoCodeApplied());
		unifiedHistoryModel.setTourRideLater(tourModel.isTourRideLater());
		unifiedHistoryModel.setCriticalTourRideLater(tourModel.isCriticalTourRideLater());
		unifiedHistoryModel.setTakeRide(tourModel.isTakeRide());
		unifiedHistoryModel.setTourTakeRide(tourModel.isTourTakeRide());

		unifiedHistoryModel.setCreatedBy(tourModel.getCreatedBy());
		unifiedHistoryModel.setCreatedAt(tourModel.getCreatedAt());
		unifiedHistoryModel.setUpdatedBy(tourModel.getUpdatedBy());
		unifiedHistoryModel.setUpdatedAt(tourModel.getUpdatedAt());

		return unifiedHistoryModel;
	}

	public static void insertEntryForOrders(String orderId) {

		OrderModel orderModel = OrderModel.getOrderDetailsByOrderId(orderId);
		UnifiedHistoryModel unifiedHistoryModel = setOrdersData(orderModel);
		unifiedHistoryModel.insertUnifiedHistory(orderModel.getCreatedBy());
	}

	public static void deleteEntryByHistoryId(String historyId) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(historyId);
		unifiedHistoryModel.deleteEntryByHistoryId();
	}

	public static void insertEntryForAppointments(String appointmentId) {

		AppointmentModel appointmentModel = AppointmentModel.getAppointmentDetailsByAppointmentId(appointmentId);
		UnifiedHistoryModel unifiedHistoryModel = setAppointmentsData(appointmentModel);
		unifiedHistoryModel.insertUnifiedHistory(appointmentModel.getCreatedBy());
	}

	public static UnifiedHistoryModel setOrdersData(OrderModel orderModel) {

		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();

		unifiedHistoryModel.setUnifiedHistoryId(UUIDGenerator.generateUUID());

		unifiedHistoryModel.setHistoryId(orderModel.getOrderId());
		unifiedHistoryModel.setShortId(orderModel.getOrderShortId());
		unifiedHistoryModel.setPassengerId(orderModel.getOrderUserId());
		unifiedHistoryModel.setCarTypeId(orderModel.getCarTypeId());
		unifiedHistoryModel.setServiceTypeId(orderModel.getServiceTypeId());
		unifiedHistoryModel.setMulticityCityRegionId(orderModel.getMulticityCityRegionId());
		unifiedHistoryModel.setMulticityCountryId(orderModel.getMulticityCountryId());

		unifiedHistoryModel.setOrderNumberOfItems(orderModel.getOrderNumberOfItems());
		unifiedHistoryModel.setOrderDeliveryAddress(orderModel.getOrderDeliveryAddress());
		unifiedHistoryModel.setOrderDeliveryCharges(orderModel.getOrderDeliveryCharges());
		unifiedHistoryModel.setOrderCreationTime(orderModel.getOrderCreationTime());
		unifiedHistoryModel.setVendorStoreId(orderModel.getVendorStoreId());
		unifiedHistoryModel.setOrderReceivedAgainstVendorId(orderModel.getOrderReceivedAgainstVendorId());

		unifiedHistoryModel.setDriverId(orderModel.getDriverId());
		unifiedHistoryModel.setDelieveryManagedByVendorDriver(orderModel.isDelieveryManagedByVendorDriver());
		unifiedHistoryModel.setTotal(orderModel.getOrderTotal());
		unifiedHistoryModel.setCharges(orderModel.getOrderCharges());
		unifiedHistoryModel.setStatus(orderModel.getOrderDeliveryStatus());
		unifiedHistoryModel.setPromoCodeId(orderModel.getOrderPromoCodeId());
		unifiedHistoryModel.setPromoCodeApplied(orderModel.getOrderPromoCodeId() != null ? true : false);
		unifiedHistoryModel.setEndOtp(orderModel.getEndOtp());

		unifiedHistoryModel.setCreatedBy(orderModel.getCreatedBy());
		unifiedHistoryModel.setCreatedAt(orderModel.getCreatedAt());
		unifiedHistoryModel.setUpdatedBy(orderModel.getUpdatedBy());
		unifiedHistoryModel.setUpdatedAt(orderModel.getUpdatedAt());

		return unifiedHistoryModel;
	}

	public static UnifiedHistoryModel setAppointmentsData(AppointmentModel appointmentModel) {

		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();

		unifiedHistoryModel.setUnifiedHistoryId(UUIDGenerator.generateUUID());

		unifiedHistoryModel.setHistoryId(appointmentModel.getAppointmentId());
		unifiedHistoryModel.setShortId(appointmentModel.getAppointmentShortId());
		unifiedHistoryModel.setPassengerId(appointmentModel.getAppointmentUserId());
		unifiedHistoryModel.setCarTypeId(ProjectConstants.CAR_TYPES.BIKE_ID);// dummy value
		unifiedHistoryModel.setServiceTypeId(appointmentModel.getServiceTypeId());
		unifiedHistoryModel.setMulticityCityRegionId(appointmentModel.getMulticityCityRegionId());
		unifiedHistoryModel.setMulticityCountryId(appointmentModel.getMulticityCountryId());

		unifiedHistoryModel.setOrderNumberOfItems(appointmentModel.getAppointmentNumberOfItems());
		unifiedHistoryModel.setOrderCreationTime(appointmentModel.getAppointmentCreationTime());
		unifiedHistoryModel.setVendorStoreId(appointmentModel.getAppointmentVendorStoreId());
		unifiedHistoryModel.setOrderReceivedAgainstVendorId(appointmentModel.getAppointmentReceivedAgainstVendorId());

		unifiedHistoryModel.setDriverId(ProjectConstants.DEFAULT_NUMBER_FOR_COMPARE);
		unifiedHistoryModel.setDelieveryManagedByVendorDriver(false);
		unifiedHistoryModel.setTotal(appointmentModel.getAppointmentTotal());
		unifiedHistoryModel.setCharges(appointmentModel.getAppointmentCharges());
		unifiedHistoryModel.setStatus(appointmentModel.getAppointmentStatus());
		unifiedHistoryModel.setPromoCodeId(appointmentModel.getAppointmentPromoCodeId());
		unifiedHistoryModel.setPromoCodeApplied(appointmentModel.getAppointmentPromoCodeId() != null ? true : false);
		unifiedHistoryModel.setEndOtp(appointmentModel.getEndOtp());

		unifiedHistoryModel.setCreatedBy(appointmentModel.getCreatedBy());
		unifiedHistoryModel.setCreatedAt(appointmentModel.getCreatedAt());
		unifiedHistoryModel.setUpdatedBy(appointmentModel.getUpdatedBy());
		unifiedHistoryModel.setUpdatedAt(appointmentModel.getUpdatedAt());

		return unifiedHistoryModel;
	}

	public static void updateOrderDeliveryStatus(OrderModel orderModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(orderModel.getOrderId());
		unifiedHistoryModel.setStatus(orderModel.getOrderDeliveryStatus());
		unifiedHistoryModel.setUpdatedAt(orderModel.getUpdatedAt());
		unifiedHistoryModel.setUpdatedBy(orderModel.getUpdatedBy());
		unifiedHistoryModel.updateOrderDeliveryStatus();
	}

	public static void updateOrderDeliveryStatus(AppointmentModel appointmentModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(appointmentModel.getAppointmentId());
		unifiedHistoryModel.setStatus(appointmentModel.getAppointmentStatus());
		unifiedHistoryModel.setUpdatedAt(appointmentModel.getUpdatedAt());
		unifiedHistoryModel.setUpdatedBy(appointmentModel.getUpdatedBy());
		unifiedHistoryModel.updateOrderDeliveryStatus();
	}

	public static void updateDelieveryManagedByVendorDriver(OrderModel orderModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(orderModel.getOrderId());
		unifiedHistoryModel.setDelieveryManagedByVendorDriver(orderModel.isDelieveryManagedByVendorDriver());
		unifiedHistoryModel.setUpdatedAt(orderModel.getUpdatedAt());
		unifiedHistoryModel.setUpdatedBy(orderModel.getUpdatedBy());
		unifiedHistoryModel.updateDelieveryManagedByVendorDriver();
	}

	public static void updateDriverIdAgainstOrder(OrderModel orderModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(orderModel.getOrderId());
		unifiedHistoryModel.setDriverId(orderModel.getDriverId());
		unifiedHistoryModel.setUpdatedAt(orderModel.getUpdatedAt());
		unifiedHistoryModel.setUpdatedBy(orderModel.getUpdatedBy());
		unifiedHistoryModel.updateDriverIdAgainstOrder();
	}

	public static void updateCarTypeIdAgainstOrder(OrderModel orderModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(orderModel.getOrderId());
		unifiedHistoryModel.setCarTypeId(orderModel.getCarTypeId());
		unifiedHistoryModel.setUpdatedAt(orderModel.getUpdatedAt());
		unifiedHistoryModel.setUpdatedBy(orderModel.getUpdatedBy());
		unifiedHistoryModel.updateCarTypeIdAgainstOrder();
	}

	public static void assignTourDriver(TourModel tourModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setDriverId(tourModel.getDriverId());
		unifiedHistoryModel.setStatus(tourModel.getStatus());
		unifiedHistoryModel.setUpdatedAt(tourModel.getUpdatedAt());
		unifiedHistoryModel.setUpdatedBy(tourModel.getUpdatedBy());
		unifiedHistoryModel.assignTourDriver();
	}

	public static void updateDriverVendorId(TourModel tourModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setDriverVendorId(tourModel.getDriverVendorId());
		unifiedHistoryModel.updateDriverVendorId();
	}

	public static void updateTourCarIdByTourId(TourModel tourModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setCarId(tourModel.getCarId());
		unifiedHistoryModel.updateTourCarIdByTourId();
	}

	public static void updateChargesAndDriverAmount(TourModel tourModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setTotal(tourModel.getTotal());
		unifiedHistoryModel.setCharges(tourModel.getCharges());
		unifiedHistoryModel.setPromoCodeId(tourModel.getPromoCodeId());
		unifiedHistoryModel.setPromoCodeApplied(tourModel.isPromoCodeApplied());
		unifiedHistoryModel.updateChargesAndDriverAmount();
	}

	public static void updatePromoCodeStatus(TourModel tourModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setTotal(tourModel.getTotal());
		unifiedHistoryModel.setPromoCodeId(tourModel.getPromoCodeId());
		unifiedHistoryModel.setPromoCodeApplied(tourModel.isPromoCodeApplied());
		unifiedHistoryModel.updatePromoCodeStatus();
	}

	public static void updateCharges(TourModel tourModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setCharges(tourModel.getCharges());
		unifiedHistoryModel.setUpdatedAt(tourModel.getUpdatedAt());
		unifiedHistoryModel.updateCharges();
	}

	public static void updateTourStatusByTourId(TourModel tourModel) {
		// updateTourStatusByPassenger
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setStatus(tourModel.getStatus());
		unifiedHistoryModel.setUpdatedAt(tourModel.getUpdatedAt());
		unifiedHistoryModel.setUpdatedBy(tourModel.getUpdatedBy());
		unifiedHistoryModel.updateTourStatusByTourId();
	}

	public static void expireToursBatch(List<String> historyIds) {
		UnifiedHistoryModel.expireToursBatch(historyIds);
	}

	public static void updateRideLaterTourFlag(TourModel tourModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setTourRideLater(tourModel.isTourRideLater());
		unifiedHistoryModel.updateRideLaterTourFlag();
	}

	public static void updateTourStatusCritical(TourModel tourModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setCriticalTourRideLater(tourModel.isCriticalTourRideLater());
		unifiedHistoryModel.updateTourStatusCritical();
	}

	public static void updateTourAsTakeRide(TourModel tourModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setTakeRide(tourModel.isTakeRide());
		unifiedHistoryModel.setTourTakeRide(tourModel.isTourTakeRide());
		unifiedHistoryModel.updateTourAsTakeRide();
	}

	public static void updateTourAddress(TourModel tourModel) {
		UnifiedHistoryModel unifiedHistoryModel = new UnifiedHistoryModel();
		unifiedHistoryModel.setHistoryId(tourModel.getTourId());
		unifiedHistoryModel.setSourceAddress(tourModel.getSourceAddress());
		unifiedHistoryModel.setDestinationAddress(tourModel.getDestinationAddress());
		unifiedHistoryModel.updateTourAddress();
	}
}
