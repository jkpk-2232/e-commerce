package com.webapp.models;

public class DriverTripCommissionModel extends AbstractModel {

	private String driverTripCommissionId;
	private String tourId;
	private String invoiceId;
	private String driverId;
	private String driverCommissionTypeId;
	private double driverPercentrage;
	private double driverAmount;

	public String getDriverTripCommissionId() {
		return driverTripCommissionId;
	}

	public void setDriverTripCommissionId(String driverTripCommissionId) {
		this.driverTripCommissionId = driverTripCommissionId;
	}

	public String getTourId() {
		return tourId;
	}

	public void setTourId(String tourId) {
		this.tourId = tourId;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getDriverCommissionTypeId() {
		return driverCommissionTypeId;
	}

	public void setDriverCommissionTypeId(String driverCommissionTypeId) {
		this.driverCommissionTypeId = driverCommissionTypeId;
	}

	public double getDriverPercentrage() {
		return driverPercentrage;
	}

	public void setDriverPercentrage(double driverPercentrage) {
		this.driverPercentrage = driverPercentrage;
	}

	public double getDriverAmount() {
		return driverAmount;
	}

	public void setDriverAmount(double driverAmount) {
		this.driverAmount = driverAmount;
	}
}