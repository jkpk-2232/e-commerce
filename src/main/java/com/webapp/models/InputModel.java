package com.webapp.models;

public class InputModel extends AbstractModel {

	private String oldPassword;
	private String newPassword;

	private String filterDayType;
	private int numberOfLastXDays;
	private String vendorStoreId;
	private int trendingProductsStart;
	private int trendingProductsLength;
	private int highestSpendingCustomersStart;
	private int highestSpendingCustomersLength;
	private boolean isOnlyTrendingProductsApiCall;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getFilterDayType() {
		return filterDayType;
	}

	public void setFilterDayType(String filterDayType) {
		this.filterDayType = filterDayType;
	}

	public int getNumberOfLastXDays() {
		return numberOfLastXDays;
	}

	public void setNumberOfLastXDays(int numberOfLastXDays) {
		this.numberOfLastXDays = numberOfLastXDays;
	}

	public String getVendorStoreId() {
		return vendorStoreId;
	}

	public void setVendorStoreId(String vendorStoreId) {
		this.vendorStoreId = vendorStoreId;
	}

	public int getTrendingProductsStart() {
		return trendingProductsStart;
	}

	public void setTrendingProductsStart(int trendingProductsStart) {
		this.trendingProductsStart = trendingProductsStart;
	}

	public int getTrendingProductsLength() {
		return trendingProductsLength;
	}

	public void setTrendingProductsLength(int trendingProductsLength) {
		this.trendingProductsLength = trendingProductsLength;
	}

	public int getHighestSpendingCustomersStart() {
		return highestSpendingCustomersStart;
	}

	public void setHighestSpendingCustomersStart(int highestSpendingCustomersStart) {
		this.highestSpendingCustomersStart = highestSpendingCustomersStart;
	}

	public int getHighestSpendingCustomersLength() {
		return highestSpendingCustomersLength;
	}

	public void setHighestSpendingCustomersLength(int highestSpendingCustomersLength) {
		this.highestSpendingCustomersLength = highestSpendingCustomersLength;
	}

	public boolean isOnlyTrendingProductsApiCall() {
		return isOnlyTrendingProductsApiCall;
	}

	public void setOnlyTrendingProductsApiCall(boolean isOnlyTrendingProductsApiCall) {
		this.isOnlyTrendingProductsApiCall = isOnlyTrendingProductsApiCall;
	}
}