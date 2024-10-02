package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.myhub.MyHubUtils;
import com.utils.myhub.UnifiedHistoryUtils;
import com.webapp.ProjectConstants;
import com.webapp.daos.AppointmentDao;
import com.webapp.daos.OrderItemDao;

public class AppointmentModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(AppointmentModel.class);

	private String appointmentId;
	private String appointmentShortId;
	private String appointmentUserId;
	private String appointmentReceivedAgainstVendorId;
	private String appointmentVendorStoreId;
	private long appointmentCreationTime;
	private long appointmentTime;
	private String appointmentTimeString;
	private String appointmentStatus;
	private String appointmentPromoCodeId;
	private double appointmentPromoCodeDiscount;
	private double appointmentTotal;
	private double appointmentCharges;
	private int appointmentNumberOfItems;
	private String paymentMode;
	private String paymentStatus;
	private String serviceTypeId;
	private String multicityCityRegionId;
	private String multicityCountryId;
	private String endOtp;
	private String paymentToken;
	private long paymentTokenGeneratedTime;

	private List<OrderItemModel> appointmentItemList;

	private int cronJobExpireTimeMins;

	private String promoCode;
	private String customerName;
	private String customerPhoneNo;
	private String customerPhoneNoCode;
	private String storeName;
	private String vendorName;
	private String storeAddressLat;
	private String storeAddressLng;
	private String storeAddress;

	public AppointmentModel insertAppointment(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {

			this.createdBy = userId;
			this.updatedBy = userId;
			this.updatedAt = this.createdAt;
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.serviceTypeId = ProjectConstants.SUPER_SERVICE_TYPE_ID.APPOINTMENT_ID;
			this.setEndOtp(MyHubUtils.generateVerificationCode());

			appointmentDao.insertAppointment(this);
			orderItemDao.insertOrderItem(this.getAppointmentItemList());

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertAppointment : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.insertEntryForAppointments(this.appointmentId);

		return AppointmentModel.getAppointmentLimitedDetailsByAppointmentId(this.appointmentId);
	}

	public AppointmentModel updateAppointment(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {

			this.updatedBy = userId;
			this.updatedAt = this.appointmentCreationTime;
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.setServiceTypeId(ProjectConstants.SUPER_SERVICE_TYPE_ID.APPOINTMENT_ID);
			this.setEndOtp(MyHubUtils.generateVerificationCode());

			appointmentDao.updateAppointment(this);
			orderItemDao.deleteOrderItem(this.getAppointmentId());
			orderItemDao.insertOrderItem(this.getAppointmentItemList());

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAppointment : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.deleteEntryByHistoryId(this.appointmentId);
		UnifiedHistoryUtils.insertEntryForAppointments(this.appointmentId);

		return AppointmentModel.getAppointmentLimitedDetailsByAppointmentId(this.appointmentId);
	}

	public static AppointmentModel getAppointmentLimitedDetailsByAppointmentId(String appointmentId) {

		AppointmentModel appointmentModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			appointmentModel = appointmentDao.getAppointmentLimitedDetailsByAppointmentId(appointmentId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentLimitedDetailsByAppointmentId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return appointmentModel;
	}

	public static List<AppointmentModel> getAppointmentsByUserId(String userId, int start, int length, int appointmentShortId, List<String> statusNotToBeConsidered) {

		List<AppointmentModel> appointmentList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			appointmentList = appointmentDao.getAppointmentsByUserId(userId, start, length, appointmentShortId, statusNotToBeConsidered);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentsByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return appointmentList;
	}

	public static AppointmentModel getAppointmentDetailsByAppointmentIdWithOrderItems(String appointmentId) {

		AppointmentModel appointmentModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			appointmentModel = appointmentDao.getAppointmentDetailsByAppointmentIdWithOrderItems(appointmentId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentDetailsByAppointmentIdWithOrderItems : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return appointmentModel;
	}

	public static AppointmentModel getAppointmentDetailsByPaymentToken(String paymentToken) {

		AppointmentModel appointmentModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			appointmentModel = appointmentDao.getAppointmentDetailsByPaymentToken(paymentToken);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentDetailsByPaymentToken : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return appointmentModel;
	}

	public static AppointmentModel getAppointmentDetailsByAppointmentId(String appointmentId) {

		AppointmentModel appointmentModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			appointmentModel = appointmentDao.getAppointmentDetailsByAppointmentId(appointmentId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentDetailsByAppointmentId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return appointmentModel;
	}

	public void updateAppointmentStatus(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = loggedInUserId;
			appointmentDao.updateAppointmentStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateAppointmentStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateOrderDeliveryStatus(this);
	}

	public void updatePaymentStatus(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			this.updatedBy = loggedInUserId;
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			appointmentDao.updatePaymentStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePaymentStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static List<AppointmentModel> getAppointmentsSearchAPI(String searchKey, int start, int length, String vendorId, List<String> appointmentStatus, int appointmentShortIdSearch, List<String> vendorStoreIdList) {

		List<AppointmentModel> appointmentList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			appointmentList = appointmentDao.getAppointmentsSearchAPI(searchKey, start, length, vendorId, appointmentStatus, appointmentShortIdSearch, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentsSearchAPI : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return appointmentList;
	}

	public static int getAppointmentsCount(long startDatelong, long endDatelong, String vendorId, String serviceId, String categoryId, List<String> appointmentStatus, int appointmentShortIdSearch, String appointmentStatusFilter, List<String> vendorStoreIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			count = appointmentDao.getAppointmentsCount(startDatelong, endDatelong, vendorId, serviceId, categoryId, appointmentStatus, appointmentShortIdSearch, appointmentStatusFilter, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentsCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<AppointmentModel> getAppointmentsSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, String displayType, String vendorId, String serviceId, String categoryId, List<String> appointmentStatus, int appointmentShortIdSearch,
				String appointmentStatusFilter, List<String> vendorStoreIdList) {

		List<AppointmentModel> appointmentList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			appointmentList = appointmentDao.getAppointmentsSearch(startDatelong, endDatelong, searchKey, start, length, displayType, vendorId, serviceId, categoryId, appointmentStatus, appointmentShortIdSearch, appointmentStatusFilter, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentsSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return appointmentList;
	}

	public static int getAppointmentsSearchCount(long startDatelong, long endDatelong, String searchKey, String vendorId, String serviceId, String categoryId, List<String> appointmentStatus, int appointmentShortIdSearch, String appointmentStatusFilter,
				List<String> vendorStoreIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			count = appointmentDao.getAppointmentsSearchCount(startDatelong, endDatelong, searchKey, vendorId, serviceId, categoryId, appointmentStatus, appointmentShortIdSearch, appointmentStatusFilter, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentsSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<AppointmentModel> getAppointmentForProcessingCronJob(List<String> appointmentStatus, int start, int length) {

		List<AppointmentModel> appointmentList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			appointmentList = appointmentDao.getAppointmentForProcessingCronJob(appointmentStatus, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentForProcessingCronJob : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return appointmentList;
	}

	public static StatsModel getAppointmentStatsByTimeForVendor(String vendorId, List<String> vendorStoreIdList, long startTime, long endTime) {

		StatsModel statsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			statsModel = appointmentDao.getAppointmentStatsByTimeForVendor(vendorId, vendorStoreIdList, startTime, endTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentStatsByTimeForVendor : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return statsModel;
	}

	public static List<StatsModel> getAppointmentListForHighestSpendingCustomers(String vendorId, List<String> vendorStoreIdList, long startTime, long endTime, int start, int length) {

		List<StatsModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		AppointmentDao appointmentDao = session.getMapper(AppointmentDao.class);

		try {
			orderList = appointmentDao.getAppointmentListForHighestSpendingCustomers(vendorId, vendorStoreIdList, startTime, endTime, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAppointmentListForHighestSpendingCustomers : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public String getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(String appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getAppointmentShortId() {
		return appointmentShortId;
	}

	public void setAppointmentShortId(String appointmentShortId) {
		this.appointmentShortId = appointmentShortId;
	}

	public String getAppointmentUserId() {
		return appointmentUserId;
	}

	public void setAppointmentUserId(String appointmentUserId) {
		this.appointmentUserId = appointmentUserId;
	}

	public String getAppointmentReceivedAgainstVendorId() {
		return appointmentReceivedAgainstVendorId;
	}

	public void setAppointmentReceivedAgainstVendorId(String appointmentReceivedAgainstVendorId) {
		this.appointmentReceivedAgainstVendorId = appointmentReceivedAgainstVendorId;
	}

	public String getAppointmentVendorStoreId() {
		return appointmentVendorStoreId;
	}

	public void setAppointmentVendorStoreId(String appointmentVendorStoreId) {
		this.appointmentVendorStoreId = appointmentVendorStoreId;
	}

	public long getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(long appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

	public String getAppointmentPromoCodeId() {
		return appointmentPromoCodeId;
	}

	public void setAppointmentPromoCodeId(String appointmentPromoCodeId) {
		this.appointmentPromoCodeId = appointmentPromoCodeId;
	}

	public double getAppointmentPromoCodeDiscount() {
		return appointmentPromoCodeDiscount;
	}

	public void setAppointmentPromoCodeDiscount(double appointmentPromoCodeDiscount) {
		this.appointmentPromoCodeDiscount = appointmentPromoCodeDiscount;
	}

	public double getAppointmentTotal() {
		return appointmentTotal;
	}

	public void setAppointmentTotal(double appointmentTotal) {
		this.appointmentTotal = appointmentTotal;
	}

	public double getAppointmentCharges() {
		return appointmentCharges;
	}

	public void setAppointmentCharges(double appointmentCharges) {
		this.appointmentCharges = appointmentCharges;
	}

	public int getAppointmentNumberOfItems() {
		return appointmentNumberOfItems;
	}

	public void setAppointmentNumberOfItems(int appointmentNumberOfItems) {
		this.appointmentNumberOfItems = appointmentNumberOfItems;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
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

	public List<OrderItemModel> getAppointmentItemList() {
		return appointmentItemList;
	}

	public void setAppointmentItemList(List<OrderItemModel> appointmentItemList) {
		this.appointmentItemList = appointmentItemList;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhoneNo() {
		return customerPhoneNo;
	}

	public void setCustomerPhoneNo(String customerPhoneNo) {
		this.customerPhoneNo = customerPhoneNo;
	}

	public String getCustomerPhoneNoCode() {
		return customerPhoneNoCode;
	}

	public void setCustomerPhoneNoCode(String customerPhoneNoCode) {
		this.customerPhoneNoCode = customerPhoneNoCode;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getStoreAddressLat() {
		return storeAddressLat;
	}

	public void setStoreAddressLat(String storeAddressLat) {
		this.storeAddressLat = storeAddressLat;
	}

	public String getStoreAddressLng() {
		return storeAddressLng;
	}

	public void setStoreAddressLng(String storeAddressLng) {
		this.storeAddressLng = storeAddressLng;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getEndOtp() {
		return endOtp;
	}

	public void setEndOtp(String endOtp) {
		this.endOtp = endOtp;
	}

	public String getPaymentToken() {
		return paymentToken;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}

	public long getPaymentTokenGeneratedTime() {
		return paymentTokenGeneratedTime;
	}

	public void setPaymentTokenGeneratedTime(long paymentTokenGeneratedTime) {
		this.paymentTokenGeneratedTime = paymentTokenGeneratedTime;
	}

	public long getAppointmentCreationTime() {
		return appointmentCreationTime;
	}

	public void setAppointmentCreationTime(long appointmentCreationTime) {
		this.appointmentCreationTime = appointmentCreationTime;
	}

	public String getAppointmentTimeString() {
		return appointmentTimeString;
	}

	public void setAppointmentTimeString(String appointmentTimeString) {
		this.appointmentTimeString = appointmentTimeString;
	}

	public int getCronJobExpireTimeMins() {
		return cronJobExpireTimeMins;
	}

	public void setCronJobExpireTimeMins(int cronJobExpireTimeMins) {
		this.cronJobExpireTimeMins = cronJobExpireTimeMins;
	}
}