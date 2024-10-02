package com.webapp.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.UnifiedHistoryDao;

public class UnifiedHistoryModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(UnifiedHistoryModel.class);

	private String vendorName;
	private String customerName;
	private String storeName;
	private String driverName;
	private String promoCode;

	private String unifiedHistoryId;

	private String historyId;
	private String shortId;
	private String passengerId;
	private String passengerVendorId;
	private String carTypeId;
	private String serviceTypeId;
	private String multicityCityRegionId;
	private String multicityCountryId;

	private String sourceAddress;
	private String destinationAddress;
	private String pFirstName;
	private String pLastName;
	private String pEmail;
	private String pPhone;
	private String pPhoneCode;
	private String pPhotoUrl;

	private String tourBookedBy;
	private String bookingType;
	private long rideLaterPickupTime;
	private boolean isRideLater;

	private int orderNumberOfItems;
	private String orderDeliveryAddress;
	private double orderDeliveryCharges;
	private long orderCreationTime;
	private String vendorStoreId;
	private String orderReceivedAgainstVendorId;

	private String courierPickupAddress;
	private String courierContactPersonName;
	private String courierContactPhoneNo;
	private String courierDropAddress;
	private String courierDropContactPersonName;
	private String courierDropContactPhoneNo;
	private String courierDetails;

	private String driverId;
	private String driverVendorId;
	private String carId;
	private boolean isDelieveryManagedByVendorDriver;
	private double total;
	private double charges;
	private String status;
	private String promoCodeId;
	private boolean isPromoCodeApplied;
	private boolean isTourRideLater;
	private boolean isCriticalTourRideLater;
	private boolean isTakeRide;
	private boolean isTourTakeRide;

	private String endOtp;

	public String insertUnifiedHistory(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.insertUnifiedHistory(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertUnifiedHistory : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.unifiedHistoryId;
	}

	public void deleteEntryByHistoryId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.deleteEntryByHistoryId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during deleteEntryByHistoryId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static void batchInsertUnifiedHistoryData(List<UnifiedHistoryModel> unifiedHistoryList) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.batchInsertUnifiedHistoryData(unifiedHistoryList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during batchInsertUnifiedHistoryData : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateOrderDeliveryStatus() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateOrderDeliveryStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateOrderDeliveryStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateDelieveryManagedByVendorDriver() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateDelieveryManagedByVendorDriver(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDelieveryManagedByVendorDriver : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateDriverIdAgainstOrder() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateDriverIdAgainstOrder(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverIdAgainstOrder : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateCarTypeIdAgainstOrder() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateCarTypeIdAgainstOrder(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCarTypeIdAgainstOrder : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void assignTourDriver() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.assignTourDriver(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during assignTourDriver : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateDriverVendorId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateDriverVendorId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverVendorId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateTourCarIdByTourId() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateTourCarIdByTourId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourCarIdByTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateChargesAndDriverAmount() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateChargesAndDriverAmount(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateChargesAndDriverAmount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updatePromoCodeStatus() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updatePromoCodeStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePromoCodeStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateCharges() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateCharges(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCharges : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateTourStatusByTourId() {

		// updateTourStatusByPassenger
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateTourStatusByTourId(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourStatusByTourId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static int expireToursBatch(List<String> historyIds) {

		int updatedStatus = 0;

		Map<String, Object> inputMap = new HashMap<String, Object>();
		inputMap.put("historyIds", historyIds);

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			updatedStatus = unifiedHistoryDao.expireToursBatch(inputMap);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during expireToursBatch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return updatedStatus;
	}

	public void updateRideLaterTourFlag() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateRideLaterTourFlag(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateRideLaterTourFlag : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateTourStatusCritical() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateTourStatusCritical(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourStatusCritical : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateTourAsTakeRide() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateTourAsTakeRide(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourAsTakeRide : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public void updateTourAddress() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryDao.updateTourAddress(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateTourAddress : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<UnifiedHistoryModel> getUnifiedHistoryList(String passengerId, int start, int length, String roleId, String searchKey, List<String> statusList, String vendorOrderManagement) {

		List<UnifiedHistoryModel> unifiedHistoryList;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		UnifiedHistoryDao unifiedHistoryDao = session.getMapper(UnifiedHistoryDao.class);

		try {
			unifiedHistoryList = unifiedHistoryDao.getUnifiedHistoryList(passengerId, start, length, roleId, searchKey, statusList, vendorOrderManagement);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getUnifiedHistoryList : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return unifiedHistoryList;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getUnifiedHistoryId() {
		return unifiedHistoryId;
	}

	public void setUnifiedHistoryId(String unifiedHistoryId) {
		this.unifiedHistoryId = unifiedHistoryId;
	}

	public String getHistoryId() {
		return historyId;
	}

	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}

	public String getShortId() {
		return shortId;
	}

	public void setShortId(String shortId) {
		this.shortId = shortId;
	}

	public String getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(String passengerId) {
		this.passengerId = passengerId;
	}

	public String getPassengerVendorId() {
		return passengerVendorId;
	}

	public void setPassengerVendorId(String passengerVendorId) {
		this.passengerVendorId = passengerVendorId;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public String getMulticityCityRegionId() {
		return multicityCityRegionId;
	}

	public void setMulticityCityRegionId(String multicityCityRegionId) {
		this.multicityCityRegionId = multicityCityRegionId;
	}

	public String getMulticityCountryId() {
		return multicityCountryId;
	}

	public void setMulticityCountryId(String multicityCountryId) {
		this.multicityCountryId = multicityCountryId;
	}

	public String getSourceAddress() {
		return sourceAddress;
	}

	public void setSourceAddress(String sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getpFirstName() {
		return pFirstName;
	}

	public void setpFirstName(String pFirstName) {
		this.pFirstName = pFirstName;
	}

	public String getpLastName() {
		return pLastName;
	}

	public void setpLastName(String pLastName) {
		this.pLastName = pLastName;
	}

	public String getpEmail() {
		return pEmail;
	}

	public void setpEmail(String pEmail) {
		this.pEmail = pEmail;
	}

	public String getpPhone() {
		return pPhone;
	}

	public void setpPhone(String pPhone) {
		this.pPhone = pPhone;
	}

	public String getpPhoneCode() {
		return pPhoneCode;
	}

	public void setpPhoneCode(String pPhoneCode) {
		this.pPhoneCode = pPhoneCode;
	}

	public String getpPhotoUrl() {
		return pPhotoUrl;
	}

	public void setpPhotoUrl(String pPhotoUrl) {
		this.pPhotoUrl = pPhotoUrl;
	}

	public String getTourBookedBy() {
		return tourBookedBy;
	}

	public void setTourBookedBy(String tourBookedBy) {
		this.tourBookedBy = tourBookedBy;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public long getRideLaterPickupTime() {
		return rideLaterPickupTime;
	}

	public void setRideLaterPickupTime(long rideLaterPickupTime) {
		this.rideLaterPickupTime = rideLaterPickupTime;
	}

	public boolean isRideLater() {
		return isRideLater;
	}

	public void setRideLater(boolean isRideLater) {
		this.isRideLater = isRideLater;
	}

	public int getOrderNumberOfItems() {
		return orderNumberOfItems;
	}

	public void setOrderNumberOfItems(int orderNumberOfItems) {
		this.orderNumberOfItems = orderNumberOfItems;
	}

	public String getOrderDeliveryAddress() {
		return orderDeliveryAddress;
	}

	public void setOrderDeliveryAddress(String orderDeliveryAddress) {
		this.orderDeliveryAddress = orderDeliveryAddress;
	}

	public double getOrderDeliveryCharges() {
		return orderDeliveryCharges;
	}

	public void setOrderDeliveryCharges(double orderDeliveryCharges) {
		this.orderDeliveryCharges = orderDeliveryCharges;
	}

	public long getOrderCreationTime() {
		return orderCreationTime;
	}

	public void setOrderCreationTime(long orderCreationTime) {
		this.orderCreationTime = orderCreationTime;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public String getOrderReceivedAgainstVendorId() {
		return orderReceivedAgainstVendorId;
	}

	public void setOrderReceivedAgainstVendorId(String orderReceivedAgainstVendorId) {
		this.orderReceivedAgainstVendorId = orderReceivedAgainstVendorId;
	}

	public String getCourierPickupAddress() {
		return courierPickupAddress;
	}

	public void setCourierPickupAddress(String courierPickupAddress) {
		this.courierPickupAddress = courierPickupAddress;
	}

	public String getCourierContactPersonName() {
		return courierContactPersonName;
	}

	public void setCourierContactPersonName(String courierContactPersonName) {
		this.courierContactPersonName = courierContactPersonName;
	}

	public String getCourierContactPhoneNo() {
		return courierContactPhoneNo;
	}

	public void setCourierContactPhoneNo(String courierContactPhoneNo) {
		this.courierContactPhoneNo = courierContactPhoneNo;
	}

	public String getCourierDropAddress() {
		return courierDropAddress;
	}

	public void setCourierDropAddress(String courierDropAddress) {
		this.courierDropAddress = courierDropAddress;
	}

	public String getCourierDropContactPersonName() {
		return courierDropContactPersonName;
	}

	public void setCourierDropContactPersonName(String courierDropContactPersonName) {
		this.courierDropContactPersonName = courierDropContactPersonName;
	}

	public String getCourierDropContactPhoneNo() {
		return courierDropContactPhoneNo;
	}

	public void setCourierDropContactPhoneNo(String courierDropContactPhoneNo) {
		this.courierDropContactPhoneNo = courierDropContactPhoneNo;
	}

	public String getCourierDetails() {
		return courierDetails;
	}

	public void setCourierDetails(String courierDetails) {
		this.courierDetails = courierDetails;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getDriverVendorId() {
		return driverVendorId;
	}

	public void setDriverVendorId(String driverVendorId) {
		this.driverVendorId = driverVendorId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public boolean isDelieveryManagedByVendorDriver() {
		return isDelieveryManagedByVendorDriver;
	}

	public void setDelieveryManagedByVendorDriver(boolean isDelieveryManagedByVendorDriver) {
		this.isDelieveryManagedByVendorDriver = isDelieveryManagedByVendorDriver;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPromoCodeId() {
		return promoCodeId;
	}

	public void setPromoCodeId(String promoCodeId) {
		this.promoCodeId = promoCodeId;
	}

	public boolean isPromoCodeApplied() {
		return isPromoCodeApplied;
	}

	public void setPromoCodeApplied(boolean isPromoCodeApplied) {
		this.isPromoCodeApplied = isPromoCodeApplied;
	}

	public boolean isTourRideLater() {
		return isTourRideLater;
	}

	public void setTourRideLater(boolean isTourRideLater) {
		this.isTourRideLater = isTourRideLater;
	}

	public boolean isCriticalTourRideLater() {
		return isCriticalTourRideLater;
	}

	public void setCriticalTourRideLater(boolean isCriticalTourRideLater) {
		this.isCriticalTourRideLater = isCriticalTourRideLater;
	}

	public boolean isTakeRide() {
		return isTakeRide;
	}

	public void setTakeRide(boolean isTakeRide) {
		this.isTakeRide = isTakeRide;
	}

	public boolean isTourTakeRide() {
		return isTourTakeRide;
	}

	public void setTourTakeRide(boolean isTourTakeRide) {
		this.isTourTakeRide = isTourTakeRide;
	}

	public String getEndOtp() {
		return endOtp;
	}

	public void setEndOtp(String endOtp) {
		this.endOtp = endOtp;
	}
}