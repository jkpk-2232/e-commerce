package com.webapp.models;


public class CustomerModel extends AbstractModel {

	private String customerId;
	private String phoneNum;
	private String customerName;
	private String offlineOrderId;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getOfflineOrderId() {
		return offlineOrderId;
	}

	public void setOfflineOrderId(String offlineOrderId) {
		this.offlineOrderId = offlineOrderId;
	}

}
