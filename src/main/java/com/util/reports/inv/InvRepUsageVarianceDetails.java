/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

public class InvRepUsageVarianceDetails implements java.io.Serializable {

	private String UV_II_NM; 
	private double UV_BEG_INV; 
	private double UV_DLVRS;
	private double UV_ADJST_QTY;
	private double UV_END_INV; 
	private double UV_ACTL; 
	private double UV_STNDRD_USG; 
	private double UV_WSTG;
	private double UV_VRNC;

    public InvRepUsageVarianceDetails() {
    }

	public String getUvItemName() {
		
		return UV_II_NM;
		
	}
	
	public void setUvItemName(String UV_II_NM) {
		
		this.UV_II_NM = UV_II_NM;
		
	}
	
	public double getUvBegInventory() {
		
		return UV_BEG_INV;
		
	}
	
	public void setUvBegInventory(double UV_BEG_INV) {
		
		this.UV_BEG_INV = UV_BEG_INV;
		
	}
	
	public double getUvDeliveries() {
		
		return UV_DLVRS;
		
	}
	
	public void setUvDeliveries(double UV_DLVRS) {
		
		this.UV_DLVRS = UV_DLVRS;
		
	}
	
	public double getUvAdjustQuantity() {
		
		return UV_ADJST_QTY;
		
	}
	
	public void setUvAdjustQuantity(double UV_ADJST_QTY) {
		
		this.UV_ADJST_QTY = UV_ADJST_QTY;
		
	}
	
	public double getUvEndInventory() {
		
		return UV_END_INV;
		
	}
	
	public void setUvEndInventory(double UV_END_INV) {
		
		this.UV_END_INV = UV_END_INV;
		
	}
	
	public double getUvActual() {
		
		return UV_ACTL;
		
	}
	
	public void setUvActual(double UV_ACTL) {
		
		this.UV_ACTL = UV_ACTL;
		
	}
	
	public double getUvStandardUsage() {
		
		return UV_STNDRD_USG;
		
	}
	
	public void setUvStandardUsage(double UV_STNDRD_USG) {
		
		this.UV_STNDRD_USG = UV_STNDRD_USG;
		
	}
	
	public double getUvWastage() {
		
		return UV_WSTG;
		
	}
	
	public void setUvWastage(double UV_WSTG) {
		
		this.UV_WSTG = UV_WSTG;
		
	}
	
	public double getUvVariance() {
		
		return UV_VRNC;
		
	}
	
	public void setUvVariance(double UV_VRNC) {
		
		this.UV_VRNC = UV_VRNC;
		
	}

} // InvRepUsageVarianceDetails class