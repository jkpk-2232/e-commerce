package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.OrderItemDao;

public class OrderItemModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(OrderItemModel.class);

	private String orderItemId;
	private String orderId;
	private int numberOfItemsOrdered;
	private String vendorProductId;
	private String vendorId;
	private String productName;
	private String productInformation;
	private double productActualPrice;
	private double productDiscountedPrice;
	private double productWeight;
	private int productWeightUnit;
	private String productSpecification;
	private String productImage;
	private boolean isPaid;
	private String productSku;
	private String productCategory;

	public static int getOrderItemCount(String orderId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			count = orderItemDao.getOrderItemCount(orderId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderItemCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<OrderItemModel> getOrderItemSearch(String searchKey, String orderId, int start, int length, double numberSearch) {

		List<OrderItemModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			orderList = orderItemDao.getOrderItemSearch(searchKey, orderId, start, length, numberSearch);
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

	public static int getOrderItemSearchCount(String searchKey, String orderId, double numberSearch) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			count = orderItemDao.getOrderItemSearchCount(searchKey, orderId, numberSearch);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderItemSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<StatsModel> getOrderItemListForTrendingProductSkuForOrders(String vendorId, List<String> vendorStoreIdList, long startTime, long endTime, int start, int length) {

		List<StatsModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			orderList = orderItemDao.getOrderItemListForTrendingProductSkuForOrders(vendorId, vendorStoreIdList, startTime, endTime, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderItemListForTrendingProductSkuForOrders : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public static List<StatsModel> getOrderItemListForTrendingProductSkuForAppointments(String vendorId, List<String> vendorStoreIdList, long startTime, long endTime, int start, int length) {

		List<StatsModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			orderList = orderItemDao.getOrderItemListForTrendingProductSkuForAppointments(vendorId, vendorStoreIdList, startTime, endTime, start, length);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderItemListForTrendingProductSkuForAppointments : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public static StatsModel getOrderItemStatsByTimeForVendorForOrders(String vendorId, List<String> vendorStoreIdList, long startTime, long endTime) {

		StatsModel statsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			statsModel = orderItemDao.getOrderItemStatsByTimeForVendorForOrders(vendorId, vendorStoreIdList, startTime, endTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderItemStatsByTimeForVendorForOrders : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return statsModel;
	}

	public static StatsModel getOrderItemStatsByTimeForVendorForAppointments(String vendorId, List<String> vendorStoreIdList, long startTime, long endTime) {

		StatsModel statsModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			statsModel = orderItemDao.getOrderItemStatsByTimeForVendorForAppointments(vendorId, vendorStoreIdList, startTime, endTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderItemStatsByTimeForVendorForAppointments : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return statsModel;
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getNumberOfItemsOrdered() {
		return numberOfItemsOrdered;
	}

	public void setNumberOfItemsOrdered(int numberOfItemsOrdered) {
		this.numberOfItemsOrdered = numberOfItemsOrdered;
	}

	public String getVendorProductId() {
		return vendorProductId;
	}

	public void setVendorProductId(String vendorProductId) {
		this.vendorProductId = vendorProductId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductInformation() {
		return productInformation;
	}

	public void setProductInformation(String productInformation) {
		this.productInformation = productInformation;
	}

	public double getProductActualPrice() {
		return productActualPrice;
	}

	public void setProductActualPrice(double productActualPrice) {
		this.productActualPrice = productActualPrice;
	}

	public double getProductDiscountedPrice() {
		return productDiscountedPrice;
	}

	public void setProductDiscountedPrice(double productDiscountedPrice) {
		this.productDiscountedPrice = productDiscountedPrice;
	}

	public double getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(double productWeight) {
		this.productWeight = productWeight;
	}

	public int getProductWeightUnit() {
		return productWeightUnit;
	}

	public void setProductWeightUnit(int productWeightUnit) {
		this.productWeightUnit = productWeightUnit;
	}

	public String getProductSpecification() {
		return productSpecification;
	}

	public void setProductSpecification(String productSpecification) {
		this.productSpecification = productSpecification;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public static List<StatsModel> getOrderItemListForKPMartTrendingProductSku(String vendorId, String vendorStoreId, long startTime, long endTime) {
		
		List<StatsModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			orderList = orderItemDao.getOrderItemListForKPMartTrendingProductSku(vendorId, vendorStoreId, startTime, endTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderItemListForKPMartTrendingProductSku : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public static List<StatsModel> getOrderItemListForKPMartPopularProductSku(String vendorId, String vendorStoreId, long currentDate, long previousThreeMonth) {
		 
		List<StatsModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			orderList = orderItemDao.getOrderItemListForKPMartPopularProductSku(vendorId, vendorStoreId, currentDate, previousThreeMonth);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderItemListForKPMartPopularProductSku : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}

	public static List<StatsModel> getPreviousOrderItemListByUser(String vendorId, String vendorStoreId, String loggedInUserId) {
		
		List<StatsModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			orderList = orderItemDao.getPreviousOrderItemListByUser(vendorId, vendorStoreId, loggedInUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPreviousOrderItemListByUser : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
		
	}
	
	public static List<StatsModel> getOrderItemListForKPMartTrendingProducts(String vendorId, String vendorStoreId, long startTime, long endTime) {
		
		List<StatsModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			orderList = orderItemDao.getOrderItemListForKPMartTrendingProducts(vendorId, vendorStoreId, startTime, endTime);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderItemListForKPMartTrendingProducts : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}
	
	public static List<StatsModel> getOrderItemListForKPMartPopularProducts(String vendorId, String vendorStoreId, long currentDate, long previousThreeMonth) {
		 
		List<StatsModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			orderList = orderItemDao.getOrderItemListForKPMartPopularProducts(vendorId, vendorStoreId, currentDate, previousThreeMonth);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderItemListForKPMartPopularProducts : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
	}
	
	public static List<StatsModel> getPreviousOrderItemProductIdsListByUser(String vendorId, String vendorStoreId, String loggedInUserId) {
		
		List<StatsModel> orderList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderItemDao orderItemDao = session.getMapper(OrderItemDao.class);

		try {
			orderList = orderItemDao.getPreviousOrderItemProductIdsListByUser(vendorId, vendorStoreId, loggedInUserId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getPreviousOrderItemProductIdsListByUser : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderList;
		
	}
}