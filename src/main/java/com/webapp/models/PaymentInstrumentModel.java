package com.webapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInstrumentModel {

	private String type;
	private String utr;
	private String cardType;
	private String pgTransactionId;
	private String bankTransactionId;
	private String pgAuthorizationCode;
	private String arn;
	private String bankId;
	private String pgServiceTransactionId;
	private String brn;
	private String responseCodeDescription;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUtr() {
		return utr;
	}

	public void setUtr(String utr) {
		this.utr = utr;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getPgTransactionId() {
		return pgTransactionId;
	}

	public void setPgTransactionId(String pgTransactionId) {
		this.pgTransactionId = pgTransactionId;
	}

	public String getBankTransactionId() {
		return bankTransactionId;
	}

	public void setBankTransactionId(String bankTransactionId) {
		this.bankTransactionId = bankTransactionId;
	}

	public String getPgAuthorizationCode() {
		return pgAuthorizationCode;
	}

	public void setPgAuthorizationCode(String pgAuthorizationCode) {
		this.pgAuthorizationCode = pgAuthorizationCode;
	}

	public String getArn() {
		return arn;
	}

	public void setArn(String arn) {
		this.arn = arn;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getPgServiceTransactionId() {
		return pgServiceTransactionId;
	}

	public void setPgServiceTransactionId(String pgServiceTransactionId) {
		this.pgServiceTransactionId = pgServiceTransactionId;
	}

	public String getBrn() {
		return brn;
	}

	public void setBrn(String brn) {
		this.brn = brn;
	}

	public String getResponseCodeDescription() {
		return responseCodeDescription;
	}

	public void setResponseCodeDescription(String responseCodeDescription) {
		this.responseCodeDescription = responseCodeDescription;
	}

}
