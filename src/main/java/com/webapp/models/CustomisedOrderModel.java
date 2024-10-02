package com.webapp.models;

import java.util.List;

public class CustomisedOrderModel {

	private String order;
	private String ref_number;
	private double discount;
	private String status;
	private double subtotal;
	private double tax;
	private String Order_type;
	private String date;
	private String payment_type;
	private String payment_info;
	private double total;
	private double paid;
	private double change;
	private String user_id;
	private String cloud_order_id;
	private String till;
	private String _id;
	private String user;
	private double rounded_value;

	private List<CustomisedOrderItem> items;

	private CustomisedCustomerModel customer;

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getRef_number() {
		return ref_number;
	}

	public void setRef_number(String ref_number) {
		this.ref_number = ref_number;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public String getOrder_type() {
		return Order_type;
	}

	public void setOrder_type(String order_type) {
		Order_type = order_type;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getPayment_info() {
		return payment_info;
	}

	public void setPayment_info(String payment_info) {
		this.payment_info = payment_info;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getPaid() {
		return paid;
	}

	public void setPaid(double paid) {
		this.paid = paid;
	}

	public double getChange() {
		return change;
	}

	public void setChange(double change) {
		this.change = change;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getCloud_order_id() {
		return cloud_order_id;
	}

	public void setCloud_order_id(String cloud_order_id) {
		this.cloud_order_id = cloud_order_id;
	}

	public List<CustomisedOrderItem> getItems() {
		return items;
	}

	public void setItems(List<CustomisedOrderItem> items) {
		this.items = items;
	}

	public CustomisedCustomerModel getCustomer() {
		return customer;
	}

	public void setCustomer(CustomisedCustomerModel customer) {
		this.customer = customer;
	}

	/*
	 * public long getDate() { return date; }
	 * 
	 * public void setDate(long date) { this.date = date; }
	 */

	public String getTill() {
		return till;
	}

	public void setTill(String till) {
		this.till = till;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public double getRounded_value() {
		return rounded_value;
	}

	public void setRounded_value(double rounded_value) {
		this.rounded_value = rounded_value;
	}

}
