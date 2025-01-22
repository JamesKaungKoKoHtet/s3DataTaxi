package com.s3.s3DataTaxi.entity;

import java.util.List;

public class ShopInvoice {
	
	private int net_total_price;
	private String company_name;
	
	private List<ShopSaleItems> sold_items;
	
	private int sub_total_quantity;
	private int gross_total_price;
	
	private int shipping_fees;
	public int getNet_total_price() {
		return net_total_price;
	}
	public void setNet_total_price(int net_total_price) {
		this.net_total_price = net_total_price;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public List<ShopSaleItems> getSold_items() {
		return sold_items;
	}
	public void setSold_items(List<ShopSaleItems> sold_items) {
		this.sold_items = sold_items;
	}
	public int getSub_total_quantity() {
		return sub_total_quantity;
	}
	public void setSub_total_quantity(int sub_total_quantity) {
		this.sub_total_quantity = sub_total_quantity;
	}
	public int getGross_total_price() {
		return gross_total_price;
	}
	public void setGross_total_price(int gross_total_price) {
		this.gross_total_price = gross_total_price;
	}
	public int getShipping_fees() {
		return shipping_fees;
	}
	public void setShipping_fees(int shipping_fees) {
		this.shipping_fees = shipping_fees;
	}
	public int getTax() {
		return tax;
	}
	public void setTax(int tax) {
		this.tax = tax;
	}
	private int tax;
	
}
