package com.ejb.restfulapi.ar.models;

import java.io.Serializable;

public class LineItemRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String itemName;
	private String itemDescription;
	private String itemLocation;
	private double itemQuantity;
	private double itemSalesPrice;
	private double normalDiscAmt;
	private double transDiscAmt;
	private double taxAmt;
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getItemLocation() {
		return itemLocation;
	}

	public void setItemLocation(String itemLocation) {
		this.itemLocation = itemLocation;
	}
	
	public double getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(double itemQuantity) {
		this.itemQuantity = itemQuantity;
	}
	
	public double getItemSalesPrice() {
		return itemSalesPrice;
	}

	public void setItemSalesPrice(double itemSalesPrice) {
		this.itemSalesPrice = itemSalesPrice;
	}
	
	public double getNormalDiscAmt() {
		return normalDiscAmt;
	}

	public void setNormalDiscAmt(double normalDiscAmt) {
		this.normalDiscAmt = normalDiscAmt;
	}
	
	public double getTransDiscAmt() {
		return transDiscAmt;
	}

	public void setTransDiscAmt(double transDiscAmt) {
		this.transDiscAmt = transDiscAmt;
	}
	
	public double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = taxAmt;
	}
}
