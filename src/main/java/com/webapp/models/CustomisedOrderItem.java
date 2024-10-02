package com.webapp.models;

public class CustomisedOrderItem {

	private String id;
	private String product_name;
	private String sku;
	private double price;
	private double quantity;
	private double mrp;
	private String prd_qty_type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
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

	public String getPrd_qty_type() {
		return prd_qty_type;
	}

	public void setPrd_qty_type(String prd_qty_type) {
		this.prd_qty_type = prd_qty_type;
	}

}
