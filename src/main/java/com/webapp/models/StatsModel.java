package com.webapp.models;

public class StatsModel extends AbstractModel {

	private String userId;
	private double revenue;
	private long numberOfOrders;
	private long numberOfSubscribers;
	private long productSkuCount;
	private String productSku;
	private long numberOfItemsOrdered;
	private double discountDiff;
	private String vendorProductId;

	public long getNumberOfItemsOrdered() {
		return numberOfItemsOrdered;
	}

	public void setNumberOfItemsOrdered(long numberOfItemsOrdered) {
		this.numberOfItemsOrdered = numberOfItemsOrdered;
	}

	public double getRevenue() {
		return revenue;
	}

	public void setRevenue(double revenue) {
		this.revenue = revenue;
	}

	public long getNumberOfOrders() {
		return numberOfOrders;
	}

	public void setNumberOfOrders(long numberOfOrders) {
		this.numberOfOrders = numberOfOrders;
	}

	public long getNumberOfSubscribers() {
		return numberOfSubscribers;
	}

	public void setNumberOfSubscribers(long numberOfSubscribers) {
		this.numberOfSubscribers = numberOfSubscribers;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getProductSkuCount() {
		return productSkuCount;
	}

	public void setProductSkuCount(long productSkuCount) {
		this.productSkuCount = productSkuCount;
	}

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public double getDiscountDiff() {
		return discountDiff;
	}

	public void setDiscountDiff(double discountDiff) {
		this.discountDiff = discountDiff;
	}

	public String getVendorProductId() {
		return vendorProductId;
	}

	public void setVendorProductId(String vendorProductId) {
		this.vendorProductId = vendorProductId;
	}
}