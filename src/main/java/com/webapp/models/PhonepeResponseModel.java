package com.webapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PhonepeResponseModel {

	private String response;
	private boolean success;
	private String code;
	private String message;
	private PhonepeTransactionDetailsModel data;

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
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

	public PhonepeTransactionDetailsModel getData() {
		return data;
	}

	public void setData(PhonepeTransactionDetailsModel data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PhonepeResponseModel [response=" + response + ", success=" + success + ", code=" + code + ", message=" + message + ", data=" + data + "]";
	}
	
	

}
