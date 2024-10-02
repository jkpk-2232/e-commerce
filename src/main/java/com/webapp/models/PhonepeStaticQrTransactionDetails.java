package com.webapp.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhonepeStaticQrTransactionDetails {

	private String transactionId;
	private String providerReferenceId;
	private long amount;
	private String paymentState;
	private String transactionDate;
	private String payResponseCode;
	private String mobileNumber;
	private String phoneNumber;
	private String name;
	private List<PhonepeStaticQrPaymentModeModel> paymentModes;
	private PhonepeStaticQrTransactionContextModel transactionContext;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getProviderReferenceId() {
		return providerReferenceId;
	}

	public void setProviderReferenceId(String providerReferenceId) {
		this.providerReferenceId = providerReferenceId;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getPaymentState() {
		return paymentState;
	}

	public void setPaymentState(String paymentState) {
		this.paymentState = paymentState;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getPayResponseCode() {
		return payResponseCode;
	}

	public void setPayResponseCode(String payResponseCode) {
		this.payResponseCode = payResponseCode;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PhonepeStaticQrPaymentModeModel> getPaymentModes() {
		return paymentModes;
	}

	public void setPaymentModes(List<PhonepeStaticQrPaymentModeModel> paymentModes) {
		this.paymentModes = paymentModes;
	}

	public PhonepeStaticQrTransactionContextModel getTransactionContext() {
		return transactionContext;
	}

	public void setTransactionContext(PhonepeStaticQrTransactionContextModel transactionContext) {
		this.transactionContext = transactionContext;
	}

	@Override
	public String toString() {
		return "PhonepeStaticQrTransactionDetails [transactionId=" + transactionId + ", providerReferenceId=" + providerReferenceId + ", amount=" + amount + ", paymentState=" + paymentState + ", transactionDate=" + transactionDate + ", payResponseCode=" + payResponseCode
					+ ", mobileNumber=" + mobileNumber + ", phoneNumber=" + phoneNumber + ", name=" + name + ", paymentModes=" + paymentModes + ", transactionContext=" + transactionContext + "]";
	}

}
