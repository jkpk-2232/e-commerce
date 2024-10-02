package com.webapp.models;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.DateUtils;
import com.jeeutils.db.ConnectionBuilderAction;
import com.utils.UUIDGenerator;
import com.webapp.daos.OrderSettingDao;

public class OrderSettingModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(OrderSettingModel.class);

	private String orderSettingId;
	private String serviceId;
	private double maxNumberOfItems;
	private double maxWeightAllowed;
	private int freeCancellationTimeMins;
	private double deliveryBaseFee;
	private double deliveryBaseKm;
	private double deliveryFeePerKm;
	private int orderJobCancellationTimeHours;
	private int orderNewCancellationTimeHours;

	private String serviceName;

	public OrderSettingModel() {

	}

	public OrderSettingModel(String serviceId, String userId) {
		this.orderSettingId = UUIDGenerator.generateUUID();
		this.serviceId = serviceId;
		this.maxNumberOfItems = 10;
		this.maxWeightAllowed = 10;
		this.freeCancellationTimeMins = 0;
		this.deliveryBaseFee = 0;
		this.deliveryBaseKm = 0;
		this.deliveryFeePerKm = 0;
		this.orderJobCancellationTimeHours = 1;
		this.orderNewCancellationTimeHours = 2;
	}

	public String insertOrderSettings(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderSettingDao orderSettingDao = session.getMapper(OrderSettingDao.class);

		this.orderSettingId = UUIDGenerator.generateUUID();
		this.createdAt = DateUtils.nowAsGmtMillisec();
		this.updatedAt = this.createdAt;
		this.createdBy = userId;
		this.updatedBy = userId;

		try {
			orderSettingDao.insertOrderSettings(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during insertOrderSettings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return this.orderSettingId;
	}

	public void updateOrderSettings(String userId) {

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderSettingDao orderSettingDao = session.getMapper(OrderSettingDao.class);

		this.updatedAt = DateUtils.nowAsGmtMillisec();
		this.updatedBy = userId;

		try {
			orderSettingDao.updateOrderSettings(this);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during updateOrderSettings : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}
	}

	public static int getOrderSettingCount(String serviceId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderSettingDao orderSettingDao = session.getMapper(OrderSettingDao.class);

		try {
			count = orderSettingDao.getOrderSettingCount(serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderSettingCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static List<OrderSettingModel> getOrderSettingSearch(String searchKey, int start, int length, String orderColumn, String serviceId) {

		List<OrderSettingModel> orderSettingList = new ArrayList<>();

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderSettingDao orderSettingDao = session.getMapper(OrderSettingDao.class);

		try {
			orderSettingList = orderSettingDao.getOrderSettingSearch(searchKey, start, length, orderColumn, serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderSettingSearch : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderSettingList;
	}

	public static int getOrderSettingSearchCount(String searchKey, String serviceId) {

		int count = 0;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderSettingDao orderSettingDao = session.getMapper(OrderSettingDao.class);

		try {
			count = orderSettingDao.getOrderSettingSearchCount(searchKey, serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderSettingSearchCount : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return count;
	}

	public static OrderSettingModel getOrderSettingDetailsByServiceId(String serviceId) {

		OrderSettingModel orderSettingModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OrderSettingDao orderSettingDao = session.getMapper(OrderSettingDao.class);

		try {
			orderSettingModel = orderSettingDao.getOrderSettingDetailsByServiceId(serviceId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderSettingDetailsByServiceId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return orderSettingModel;
	}

	public String getOrderSettingId() {
		return orderSettingId;
	}

	public void setOrderSettingId(String orderSettingId) {
		this.orderSettingId = orderSettingId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public double getMaxNumberOfItems() {
		return maxNumberOfItems;
	}

	public void setMaxNumberOfItems(double maxNumberOfItems) {
		this.maxNumberOfItems = maxNumberOfItems;
	}

	public double getMaxWeightAllowed() {
		return maxWeightAllowed;
	}

	public void setMaxWeightAllowed(double maxWeightAllowed) {
		this.maxWeightAllowed = maxWeightAllowed;
	}

	public int getFreeCancellationTimeMins() {
		return freeCancellationTimeMins;
	}

	public void setFreeCancellationTimeMins(int freeCancellationTimeMins) {
		this.freeCancellationTimeMins = freeCancellationTimeMins;
	}

	public double getDeliveryBaseFee() {
		return deliveryBaseFee;
	}

	public void setDeliveryBaseFee(double deliveryBaseFee) {
		this.deliveryBaseFee = deliveryBaseFee;
	}

	public double getDeliveryBaseKm() {
		return deliveryBaseKm;
	}

	public void setDeliveryBaseKm(double deliveryBaseKm) {
		this.deliveryBaseKm = deliveryBaseKm;
	}

	public double getDeliveryFeePerKm() {
		return deliveryFeePerKm;
	}

	public void setDeliveryFeePerKm(double deliveryFeePerKm) {
		this.deliveryFeePerKm = deliveryFeePerKm;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getOrderJobCancellationTimeHours() {
		return orderJobCancellationTimeHours;
	}

	public void setOrderJobCancellationTimeHours(int orderJobCancellationTimeHours) {
		this.orderJobCancellationTimeHours = orderJobCancellationTimeHours;
	}

	public int getOrderNewCancellationTimeHours() {
		return orderNewCancellationTimeHours;
	}

	public void setOrderNewCancellationTimeHours(int orderNewCancellationTimeHours) {
		this.orderNewCancellationTimeHours = orderNewCancellationTimeHours;
	}
}