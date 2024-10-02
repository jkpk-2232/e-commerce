package com.webapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhonepeStaticQrTransactionContextModel {

	private String qrCodeId;
	private String posDeviceId;
	private String storeId;
	private String terminalId;

	public String getQrCodeId() {
		return qrCodeId;
	}

	public void setQrCodeId(String qrCodeId) {
		this.qrCodeId = qrCodeId;
	}

	public String getPosDeviceId() {
		return posDeviceId;
	}

	public void setPosDeviceId(String posDeviceId) {
		this.posDeviceId = posDeviceId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

}
