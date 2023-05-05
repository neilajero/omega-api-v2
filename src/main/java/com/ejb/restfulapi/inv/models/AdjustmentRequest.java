package com.ejb.restfulapi.inv.models;

import java.io.Serializable;
import java.util.List;

import com.ejb.restfulapi.OfsApiRequest;

public class AdjustmentRequest extends OfsApiRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String adjustmentType;
	private String adjustmentDate;
	private Double unitCost; // This is an option field from TT and will be ignored in Omega ERP
	private double quantity;
	private String adjustmentAction;
	private String identifier;
	private List<ItemRequest> items;
	
	public String getAdjustmentType() {
		return adjustmentType;
	}
	
	public void setAdjustmentType(String adjustmentType) {
		this.adjustmentType = adjustmentType;
	}
	
	public String getAdjustmentDate() {
		return adjustmentDate;
	}

	public void setAdjustmentDate(String adjustmentDate) {
		this.adjustmentDate = adjustmentDate;
	}

	public Double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getAdjustmentAction() {
		return adjustmentAction;
	}

	public void setAdjustmentAction(String adjustmentAction) {
		this.adjustmentAction = adjustmentAction;
	}

	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<ItemRequest> getItems() {
		return items;
	}

	public void setItems(List<ItemRequest> items) {
		this.items = items;
	}

}
