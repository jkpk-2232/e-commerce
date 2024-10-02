package com.webapp.models;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.jeeutils.db.ConnectionBuilderAction;
import com.webapp.daos.OfflineOrderDao;

public class OfflineOrderModel extends AbstractModel {

	private static Logger logger = Logger.getLogger(OfflineOrderModel.class);

	private String offlineOrderId;
	private String offlineStoreOrderId;
	private String refNumber;
	private double discount;
	private String status;
	private double subTotal;
	private double tax;
	private String orderType;
	private long date;
	private String paymentType;
	private String paymentInfo;
	private double total;
	private double paid;
	private double change;
	private String userId;
	private String cloudOrderId;
	private String counterNo;
	private double roundedValue;

	private List<OfflineOrderItemModel> offlineOrderItemList;

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
 
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getPaid() {
		return paid;
	}

	public void setPaid(double paid) {
		this.paid = paid;
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCloudOrderId() {
		return cloudOrderId;
	}

	public void setCloudOrderId(String cloudOrderId) {
		this.cloudOrderId = cloudOrderId;
	}

	public List<OfflineOrderItemModel> getOfflineOrderItemList() {
		return offlineOrderItemList;
	}

	public void setOfflineOrderItemList(List<OfflineOrderItemModel> offlineOrderItemList) {
		this.offlineOrderItemList = offlineOrderItemList;
	}

	public String getCounterNo() {
		return counterNo;
	}

	public void setCounterNo(String counterNo) {
		this.counterNo = counterNo;
	}

	public String getOfflineOrderId() {
		return offlineOrderId;
	}

	public void setOfflineOrderId(String offlineOrderId) {
		this.offlineOrderId = offlineOrderId;
	}

	public String getOfflineStoreOrderId() {
		return offlineStoreOrderId;
	}

	public void setOfflineStoreOrderId(String offlineStoreOrderId) {
		this.offlineStoreOrderId = offlineStoreOrderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public static OfflineOrderModel getOrderDetailsByOfflineStoreOrderId(String offlineStoreOrderId) {

		OfflineOrderModel offlineOrderModel = null;

		SqlSession session = ConnectionBuilderAction.getSqlSession();
		OfflineOrderDao offlineOrderDao = session.getMapper(OfflineOrderDao.class);

		try {
			offlineOrderModel = offlineOrderDao.getOrderDetailsByOfflineStoreOrderId(offlineStoreOrderId);
			session.commit();
		} catch (Throwable t) {
			session.rollback();
			logger.error("Exception occured during getOrderDetailsByOfflineStoreOrderId : ", t);
			throw new PersistenceException(t);
		} finally {
			session.close();
		}

		return offlineOrderModel;
	}

	public double getRoundedValue() {
		return roundedValue;
	}

	public void setRoundedValue(double roundedValue) {
		this.roundedValue = roundedValue;
	}

}
