package com.webapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhonepeStaticQrResponseModel {

	private String success;
	private String code;
	private String message;
	private PhonepeStaticQrDataModel data;

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public PhonepeStaticQrDataModel getData() {
		return data;
	}

	public void setData(PhonepeStaticQrDataModel data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PhonepeStaticQrResponseModel [success=" + success + ", code=" + code + ", message=" + message + ", data=" + data + "]";
	}

	
	
}
