package com.webapp.models;

public class OfflineOrderItemModel extends AbstractModel {

	private String offlineOrderItemId;
	private String offlineStoreOrderItemId;
	private String productName;
	private String productSku;
	private double price;
	private double quantity;
	private String offlineOrderId;
	private double mrp;
	private String prdQtyType;

	public String getOfflineOrderItemId() {
		return offlineOrderItemId;
	}

	public void setOfflineOrderItemId(String offlineOrderItemId) {
		this.offlineOrderItemId = offlineOrderItemId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductSku() {
		return productSku;
	}

	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getOfflineOrderId() {
		return offlineOrderId;
	}

	public void setOfflineOrderId(String offlineOrderId) {
		this.offlineOrderId = offlineOrderId;
	}

	public String getOfflineStoreOrderItemId() {
		return offlineStoreOrderItemId;
	}

	public void setOfflineStoreOrderItemId(String offlineStoreOrderItemId) {
		this.offlineStoreOrderItemId = offlineStoreOrderItemId;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getPrdQtyType() {
		return prdQtyType;
	}

	public void setPrdQtyType(String prdQtyType) {
		this.prdQtyType = prdQtyType;
	}

	

}
