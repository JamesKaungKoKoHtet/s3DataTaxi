package com.s3.s3DataTaxi.controller;

public class ShopInvoice {
	
	private String name;
	private String id;
	private int sales;
	
	public ShopInvoice(String name, String id, int sales) {
		super();
		this.name = name;
		this.id = id;
		this.sales = sales;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSales() {
		return sales;
	}

	public void setSales(int sales) {
		this.sales = sales;
	}

	
}
