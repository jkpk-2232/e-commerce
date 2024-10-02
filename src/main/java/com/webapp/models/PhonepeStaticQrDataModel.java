package com.webapp.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhonepeStaticQrDataModel {

	private Integer resultCount;
	private long startTimestamp;
	private long endTimestamp;
	private List<PhonepeStaticQrTransactionDetails> transactions;
	private String merchantId;
	private String phonepeTransactionId;
	private String schemaVersionNumber;
	

	public Integer getResultCount() {
		return resultCount;
	}

	public void setResultCount(Integer resultCount) {
		this.resultCount = resultCount;
	}

	public long getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(long startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public long getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(long endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

	public List<PhonepeStaticQrTransactionDetails> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<PhonepeStaticQrTransactionDetails> transactions) {
		this.transactions = transactions;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getPhonepeTransactionId() {
		return phonepeTransactionId;
	}

	public void setPhonepeTransactionId(String phonepeTransactionId) {
		this.phonepeTransactionId = phonepeTransactionId;
	}

	public String getSchemaVersionNumber() {
		return schemaVersionNumber;
	}

	public void setSchemaVersionNumber(String schemaVersionNumber) {
		this.schemaVersionNumber = schemaVersionNumber;
	}

}
