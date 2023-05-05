package com.ejb.restfulapi.inv.models;

import java.io.Serializable;

import com.ejb.restfulapi.OfsApiRequest;

public class ItemRequest extends OfsApiRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String itemName;
	private String itemDescription;
	private String itemLocation;
	private String itemClass;
	private String itemCategory;
	private String costMethod;
	private double itemQuantity;
	private double unitCost;
	private double itemSalesPrice;
	private String unitOfMeasure;
	
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
	
	public String getItemClass() {
		return itemClass;
	}
	
	public void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}
	
	public String getItemCategory() {
		return itemCategory;
	}
	
	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}
	
	public String getCostMethod() {
		return costMethod;
	}
	
	public void setCostMethod(String costMethod) {
		this.costMethod = costMethod;
	}
	
	public double getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(double itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public double getUnitCost() {
		return unitCost;
	}
	
	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}
	
	public double getItemSalesPrice() {
		return itemSalesPrice;
	}

	public void setSalesPrice(double itemSalesPrice) {
		this.itemSalesPrice = itemSalesPrice;
	}
	
	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}
	
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

}
