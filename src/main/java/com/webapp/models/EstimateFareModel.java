package com.webapp.models;

public class EstimateFareModel extends AbstractModel {

	private String sLatitude;
	private String sLongitude;
	private String dLatitude;
	private String dLongitude;
	private String carTypeId;
	private String promoCode;
	private boolean isRideLater;

	public boolean isRideLater() {
		return isRideLater;
	}

	public void setRideLater(boolean isRideLater) {
		this.isRideLater = isRideLater;
	}

	public String getsLatitude() {
		return sLatitude;
	}

	public void setsLatitude(String sLatitude) {
		this.sLatitude = sLatitude;
	}

	public String getsLongitude() {
		return sLongitude;
	}

	public void setsLongitude(String sLongitude) {
		this.sLongitude = sLongitude;
	}

	public String getdLatitude() {
		return dLatitude;
	}

	public void setdLatitude(String dLatitude) {
		this.dLatitude = dLatitude;
	}

	public String getdLongitude() {
		return dLongitude;
	}

	public void setdLongitude(String dLongitude) {
		this.dLongitude = dLongitude;
	}

	public String getCarTypeId() {
		return carTypeId;
	}

	public void setCarTypeId(String carTypeId) {
		this.carTypeId = carTypeId;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	@Override
	public String toString() {
		return "EstimateFareModel [sLatitude=" + sLatitude + ", sLongitude=" + sLongitude + ", dLatitude=" + dLatitude + ", dLongitude=" + dLongitude + ", carTypeId=" + carTypeId + ", promoCode=" + promoCode + ", isRideLater=" + isRideLater + "]";
	}

}