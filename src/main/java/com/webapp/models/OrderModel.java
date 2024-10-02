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
import com.webapp.daos.CustomerDao;
import com.webapp.daos.OfflineOrderDao;
import com.webapp.daos.OfflineOrderItemDao;
import com.webapp.daos.OrderDao;
import com.webapp.daos.OrderItemDao;

public class OrderModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(OrderModel.class);

	private String orderId;
	private String orderShortId;
	private String orderUserId;
	private String orderReceivedAgainstVendorId;
	private long orderCreationTime;
	private String orderDeliveryStatus;
	private String orderDeliveryAddress;
	private String orderDeliveryAddressGeolocation;
	private String orderDeliveryLat;
	private String orderDeliveryLng;
	private String orderPromoCodeId;
	private double orderPromoCodeDiscount;
	private double orderTotal;
	private double orderDeliveryCharges;
	private double orderCharges;
	private double orderDeliveryDistance;
	private String paymentMode;
	private String paymentStatus;
	private int orderNumberOfItems;
	private String vendorStoreId;
	private boolean isDelieveryManagedByVendorDriver;
	private String driverId;
	private String multicityCityRegionId;
	private String multicityCountryId;
	private String carTypeId;

	private List<OrderItemModel> orderItemList;

	private String promoCode;
	private String customerName;
	private String customerPhoneNo;
	private String customerPhoneNoCode;
	private String storeName;
	private String vendorName;
	private String storeAddressLat;
	private String storeAddressLng;
	private String storeAddress;
	private int orderJobCancellationTimeHours;
	private int orderNewCancellationTimeHours;

	private String serviceTypeId;
	private boolean isSelfDeliveryWithinXKm;
	private String endOtp;

	private String paymentToken;
	private long paymentTokenGeneratedTime;
	
	private String vendorAddress;
	
	private String orderDate;
	private int offlineOrderCount;
	private int onlineOrderCount;
	
	private String vendorPhoneNo;

	public OrderModel insertOrder(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {

			this.setOrderDeliveryAddressGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.getOrderDeliveryLng() + "  " + this.getOrderDeliveryLat() + ")')");
			this.createdBy = userId;
			this.updatedBy = userId;
			this.createdAt = this.orderCreationTime;
			this.updatedAt = this.createdAt;
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.setServiceTypeId(ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);
			this.setEndOtp(MyHubUtils.generateVerificationCode());

			orderDao.insertOrder(this);
			orderItemDao.insertOrderItem(this.getOrderItemList());

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertOrder : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.insertEntryForOrders(this.orderId);

		return OrderModel.getOrderLimitedDetailsByOrderId(this.orderId);
	}

	public OrderModel updateOrder(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {

			this.setOrderDeliveryAddressGeolocation("ST_GeographyFromText('SRID=4326;POINT(" + this.getOrderDeliveryLng() + "  " + this.getOrderDeliveryLat() + ")')");
			this.updatedBy = userId;
			this.updatedAt = this.orderCreationTime;
			this.recordStatus = ProjectConstants.ACTIVATE_STATUS;
			this.setServiceTypeId(ProjectConstants.SUPER_SERVICE_TYPE_ID.ECOMMERCE_ID);
			this.setEndOtp(MyHubUtils.generateVerificationCode());

			orderDao.updateOrder(this);
			orderItemDao.deleteOrderItem(this.getOrderId());
			orderItemDao.insertOrderItem(this.getOrderItemList());

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertOrder : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.deleteEntryByHistoryId(this.orderId);
		UnifiedHistoryUtils.insertEntryForOrders(this.orderId);

		return OrderModel.getOrderLimitedDetailsByOrderId(this.orderId);
	}

	public static List<OrderModel> getOrdersByUserId(String userId, int start, int length, int orderShortId, String roleId, List<String> statusNotToBeConsidered) {

		List<OrderModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderList = orderDao.getOrdersByUserId(userId, start, length, orderShortId, roleId, statusNotToBeConsidered);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrdersByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public static OrderModel getOrderDetailsByOrderId(String orderId) {

		OrderModel orderModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderModel = orderDao.getOrderDetailsByOrderId(orderId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderDetailsByOrderId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderModel;
	}

	public static OrderModel getOrderDetailsByOrderIdWithOrderItems(String orderId) {

		OrderModel orderModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderModel = orderDao.getOrderDetailsByOrderIdWithOrderItems(orderId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderDetailsByOrderIdWithOrderItems : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderModel;
	}

	public static OrderModel getOrderLimitedDetailsByOrderId(String orderId) {

		OrderModel orderModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderModel = orderDao.getOrderLimitedDetailsByOrderId(orderId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderLimitedDetailsByOrderId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderModel;
	}

	public void updateOrderDeliveryStatus(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = loggedInUserId;
			orderDao.updateOrderDeliveryStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateOrderDeliveryStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateOrderDeliveryStatus(this);
	}

	public static int getOrdersCount(long startDatelong, long endDatelong, String vendorId, String serviceId, String categoryId, List<String> orderStatus, int orderShortIdSearch, String orderStatusFilter, String vendorOrderManagement, List<String> vendorStoreIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			count = orderDao.getOrdersCount(startDatelong, endDatelong, vendorId, serviceId, categoryId, orderStatus, orderShortIdSearch, orderStatusFilter, vendorOrderManagement, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrdersCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<OrderModel> getOrdersSearch(long startDatelong, long endDatelong, String searchKey, int start, int length, String displayType, String vendorId, String serviceId, String categoryId, List<String> orderStatus, int orderShortIdSearch,
				String orderStatusFilter, String vendorOrderManagement, List<String> vendorStoreIdList) {

		List<OrderModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderList = orderDao.getOrdersSearch(startDatelong, endDatelong, searchKey, start, length, displayType, vendorId, serviceId, categoryId, orderStatus, orderShortIdSearch, orderStatusFilter, vendorOrderManagement, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrdersSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public static List<OrderModel> getOrdersSearchAPI(String searchKey, int start, int length, String vendorId, List<String> orderStatus, int orderShortIdSearch, String vendorOrderManagement, List<String> vendorStoreIdList) {

		List<OrderModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderList = orderDao.getOrdersSearchAPI(searchKey, start, length, vendorId, orderStatus, orderShortIdSearch, vendorOrderManagement, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrdersSearchAPI : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public static int getOrdersSearchCount(long startDatelong, long endDatelong, String searchKey, String vendorId, String serviceId, String categoryId, List<String> orderStatus, int orderShortIdSearch, String orderStatusFilter, String vendorOrderManagement,
				List<String> vendorStoreIdList) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			count = orderDao.getOrdersSearchCount(startDatelong, endDatelong, searchKey, vendorId, serviceId, categoryId, orderStatus, orderShortIdSearch, orderStatusFilter, vendorOrderManagement, vendorStoreIdList);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrdersSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public void updateDelieveryManagedByVendorDriver(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			this.updatedBy = loggedInUserId;
			orderDao.updateDelieveryManagedByVendorDriver(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDelieveryManagedByVendorDriver : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateDelieveryManagedByVendorDriver(this);
	}

	public static List<OrderModel> getOrdersForProcessingCronJob(List<String> orderStatus, int start, int length) {

		List<OrderModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderList = orderDao.getOrdersForProcessingCronJob(orderStatus, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrdersForProcessingCronJob : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public void updateDriverIdAgainstOrder() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			orderDao.updateDriverIdAgainstOrder(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateDriverIdAgainstOrder : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateDriverIdAgainstOrder(this);
	}

	public void updateCarTypeIdAgainstOrder() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			orderDao.updateCarTypeIdAgainstOrder(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateCarTypeIdAgainstOrder : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.updateCarTypeIdAgainstOrder(this);
	}

	public void updatePaymentStatus(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			this.updatedBy = loggedInUserId;
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			orderDao.updatePaymentStatus(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePaymentStatus : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static StatsModel getOrderStatsByTimeForVendor(String vendorId, List<String> vendorStoreIdList, long startTime, long endTime) {

		StatsModel statsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			statsModel = orderDao.getOrderStatsByTimeForVendor(vendorId, vendorStoreIdList, startTime, endTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderStatsByTimeForVendor : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return statsModel;
	}

	public static List<StatsModel> getOrderListForHighestSpendingCustomers(String vendorId, List<String> vendorStoreIdList, long startTime, long endTime, int start, int length) {

		List<StatsModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderList = orderDao.getOrderListForHighestSpendingCustomers(vendorId, vendorStoreIdList, startTime, endTime, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderListForHighestSpendingCustomers : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public static OrderModel getCurrentOrderByDriverId(String driverId, List<String> orderStatus) {

		OrderModel orderModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderModel = orderDao.getCurrentOrderByDriverId(driverId, orderStatus);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getCurrentOrderByDriverId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderModel;
	}

	public static List<OrderModel> getAllOrdersDataForMigration(int start, int length) {

		List<OrderModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderList = orderDao.getAllOrdersDataForMigration(start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getAllOrdersDataForMigration : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public void updatePaymentAndOrderStatusForCCavenuePayment(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			this.updatedBy = loggedInUserId;
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			orderDao.updatePaymentAndOrderStatusForCCavenuePayment(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePaymentAndOrderStatusForCCavenuePayment : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static OrderModel getOrderDetailsByPaymentToken(String paymentToken) {

		OrderModel orderModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderModel = orderDao.getOrderDetailsByPaymentToken(paymentToken);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderDetailsByPaymentToken : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderModel;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderShortId() {
		return orderShortId;
	}

	public void setOrderShortId(String orderShortId) {
		this.orderShortId = orderShortId;
	}

	public String getOrderUserId() {
		return orderUserId;
	}

	public void setOrderUserId(String orderUserId) {
		this.orderUserId = orderUserId;
	}

	public String getOrderReceivedAgainstVendorId() {
		return orderReceivedAgainstVendorId;
	}

	public void setOrderReceivedAgainstVendorId(String orderReceivedAgainstVendorId) {
		this.orderReceivedAgainstVendorId = orderReceivedAgainstVendorId;
	}

	public long getOrderCreationTime() {
		return orderCreationTime;
	}

	public void setOrderCreationTime(long orderCreationTime) {
		this.orderCreationTime = orderCreationTime;
	}

	public String getOrderDeliveryStatus() {
		return orderDeliveryStatus;
	}

	public void setOrderDeliveryStatus(String orderDeliveryStatus) {
		this.orderDeliveryStatus = orderDeliveryStatus;
	}

	public String getOrderDeliveryAddress() {
		return orderDeliveryAddress;
	}

	public void setOrderDeliveryAddress(String orderDeliveryAddress) {
		this.orderDeliveryAddress = orderDeliveryAddress;
	}

	public String getOrderDeliveryAddressGeolocation() {
		return orderDeliveryAddressGeolocation;
	}

	public void setOrderDeliveryAddressGeolocation(String orderDeliveryAddressGeolocation) {
		this.orderDeliveryAddressGeolocation = orderDeliveryAddressGeolocation;
	}

	public String getOrderDeliveryLat() {
		return orderDeliveryLat;
	}

	public void setOrderDeliveryLat(String orderDeliveryLat) {
		this.orderDeliveryLat = orderDeliveryLat;
	}

	public String getOrderDeliveryLng() {
		return orderDeliveryLng;
	}

	public void setOrderDeliveryLng(String orderDeliveryLng) {
		this.orderDeliveryLng = orderDeliveryLng;
	}

	public String getOrderPromoCodeId() {
		return orderPromoCodeId;
	}

	public void setOrderPromoCodeId(String orderPromoCodeId) {
		this.orderPromoCodeId = orderPromoCodeId;
	}

	public double getOrderPromoCodeDiscount() {
		return orderPromoCodeDiscount;
	}

	public void setOrderPromoCodeDiscount(double orderPromoCodeDiscount) {
		this.orderPromoCodeDiscount = orderPromoCodeDiscount;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public double getOrderCharges() {
		return orderCharges;
	}

	public void setOrderCharges(double orderCharges) {
		this.orderCharges = orderCharges;
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

	public int getOrderNumberOfItems() {
		return orderNumberOfItems;
	}

	public void setOrderNumberOfItems(int orderNumberOfItems) {
		this.orderNumberOfItems = orderNumberOfItems;
	}

	public List<OrderItemModel> getOrderItemList() {
		return orderItemList;
	}

	public void setOrderItemList(List<OrderItemModel> orderItemList) {
		this.orderItemList = orderItemList;
	}

	public double getOrderDeliveryCharges() {
		return orderDeliveryCharges;
	}

	public void setOrderDeliveryCharges(double orderDeliveryCharges) {
		this.orderDeliveryCharges = orderDeliveryCharges;
	}

	public double getOrderDeliveryDistance() {
		return orderDeliveryDistance;
	}

	public void setOrderDeliveryDistance(double orderDeliveryDistance) {
		this.orderDeliveryDistance = orderDeliveryDistance;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public boolean isDelieveryManagedByVendorDriver() {
		return isDelieveryManagedByVendorDriver;
	}

	public void setDelieveryManagedByVendorDriver(boolean isDelieveryManagedByVendorDriver) {
		this.isDelieveryManagedByVendorDriver = isDelieveryManagedByVendorDriver;
	}

	public int getOrderJobCancellationTimeHours() {
		return orderJobCancellationTimeHours;
	}

	public void setOrderJobCancellationTimeHours(int orderJobCancellationTimeHours) {
		this.orderJobCancellationTimeHours = orderJobCancellationTimeHours;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
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

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
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

	public int getOrderNewCancellationTimeHours() {
		return orderNewCancellationTimeHours;
	}

	public void setOrderNewCancellationTimeHours(int orderNewCancellationTimeHours) {
		this.orderNewCancellationTimeHours = orderNewCancellationTimeHours;
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(String serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public boolean isSelfDeliveryWithinXKm() {
		return isSelfDeliveryWithinXKm;
	}

	public void setSelfDeliveryWithinXKm(boolean isSelfDeliveryWithinXKm) {
		this.isSelfDeliveryWithinXKm = isSelfDeliveryWithinXKm;
	}

	public String getEndOtp() {
		return endOtp;
	}

	public void setEndOtp(String endOtp) {
		this.endOtp = endOtp;
	}

	public static List<OrderModel> getDeliveredOrdersByvendorIdAndOrderDeliveryStatusAndUpdatedAt(String vendorId, List<String> orderDeliveryStatusList, long startDateLong, long endDateLong) {

		List<OrderModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderList = orderDao.getDeliveredOrdersByvendorIdAndOrderDeliveryStatusAndUpdatedAt(vendorId, orderDeliveryStatusList, startDateLong, endDateLong);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getDeliveredOrdersByvendorIdAndOrderDeliveryStatusAndUpdatedAt : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public static List<OrderModel> getOrdersListByUserId(String userId, int orderShortId, String roleId, List<String> statusNotToBeConsidered) {

		List<OrderModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderList = orderDao.getOrdersListByUserId(userId, orderShortId, roleId, statusNotToBeConsidered);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrdersListByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public static int getOrdersCountByUserId(String userId, String roleId, List<String> statusNotToBeConsidered) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			count = orderDao.getOrdersCountByUserId(userId, roleId, statusNotToBeConsidered);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrdersCountByUserId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<OrderModel> getOrderListBasedOnStoreAndVendorAndSearchAPI(String searchKey, int start, int length, String vendorId, String vendorStoreId, List<String> orderStatus, int orderShortIdSearch, String vendorOrderManagement) {

		List<OrderModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			orderList = orderDao.getOrderListBasedOnStoreAndVendorAndSearchAPI(searchKey, start, length, vendorId, vendorStoreId, orderStatus, orderShortIdSearch, vendorOrderManagement);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderListBasedOnStoreAndVendorAndSearchAPI : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public OrderModel insertStoreOrder() {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {

			orderDao.insertOrder(this);
			orderItemDao.insertOrderItem(this.getOrderItemList());

			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertStoreOrder : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.insertEntryForOrders(this.orderId);

		return OrderModel.getOrderLimitedDetailsByOrderId(this.orderId);
	}

	public OrderModel insertOfflineStoreOrder(OfflineOrderModel offlineOrderModel, CustomerModel customerModel) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);
		OfflineOrderDao offlineOrderDao = session.getMapper(OfflineOrderDao.class);
		OfflineOrderItemDao offlineOrderItemDao = session.getMapper(OfflineOrderItemDao.class);
		CustomerDao customerDao = session.getMapper(CustomerDao.class);

		try {

			orderDao.insertOrder(this);
			orderItemDao.insertOrderItem(this.getOrderItemList());
			offlineOrderDao.insertOfflineOrder(offlineOrderModel);
			offlineOrderItemDao.insertOfflineOrderitem(offlineOrderModel.getOfflineOrderItemList());
			customerDao.insertCustomer(customerModel);
			session.commit();

		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertStoreOrder : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		UnifiedHistoryUtils.insertEntryForOrders(this.orderId);
		return OrderModel.getOrderLimitedDetailsByOrderId(this.orderId);
	}

	public void updatePaymentAndOrderStatusForKPMart(String loggedInUserId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);

		try {
			this.updatedBy = loggedInUserId;
			this.updatedAt = DateUtils.nowAsGmtMillisec();
			orderDao.updatePaymentAndOrderStatusForKPMart(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updatePaymentAndOrderStatusForKPMart : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
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

	public String getVendorAddress() {
		return vendorAddress;
	}

	public void setVendorAddress(String vendorAddress) {
		this.vendorAddress = vendorAddress;
	}

	public static List<OrderModel> getOnlineAndOfflineSalesDashboardByBrand(String brandId) {
		
		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderDao orderDao = session.getMapper(OrderDao.class);
		
		List<OrderModel> orderList = new ArrayList<>();
		
		try {
			orderList = orderDao.getOnlineAndOfflineSalesDashboardByBrand(brandId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOnlineAndOfflineSalesDashboardByBrand : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
		
		return orderList;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public int getOfflineOrderCount() {
		return offlineOrderCount;
	}

	public void setOfflineOrderCount(int offlineOrderCount) {
		this.offlineOrderCount = offlineOrderCount;
	}

	public int getOnlineOrderCount() {
		return onlineOrderCount;
	}

	public void setOnlineOrderCount(int onlineOrderCount) {
		this.onlineOrderCount = onlineOrderCount;
	}

	public String getVendorPhoneNo() {
		return vendorPhoneNo;
	}

	public void setVendorPhoneNo(String vendorPhoneNo) {
		this.vendorPhoneNo = vendorPhoneNo;
	}
}