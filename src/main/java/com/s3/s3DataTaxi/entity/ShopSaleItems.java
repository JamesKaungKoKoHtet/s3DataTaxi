package com.s3.s3DataTaxi.entity;

public class ShopSaleItems {
	private String product_code;
	private String product_name;
	private int retale_price;
	private int quantity;
	private int total_price;
	public String getProduct_code() {
		return product_code;
	}
	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public int getRetale_price() {
		return retale_price;
	}
	public void setRetale_price(int retale_price) {
		this.retale_price = retale_price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getTotal_price() {
		return total_price;
	}
	public void setTotal_price(int total_price) {
		this.total_price = total_price;
	}
}
