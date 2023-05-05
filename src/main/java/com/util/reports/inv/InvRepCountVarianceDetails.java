/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

public class InvRepCountVarianceDetails implements java.io.Serializable {

	private String CV_II_NM; 
	private double CV_BEG_INVNTRY; 
	private double CV_DLVRS;
	private double CV_ADJST_QTY;
	private double CV_STNDRD_USG;
	private double CV_END_INVNTRY;  
	private double CV_PHYSCL_INVNTRY;
	private double CV_WSTG;
	private double CV_VRNC;

    public InvRepCountVarianceDetails() {
    }

	public String getCvItemName() {
		
		return CV_II_NM;
		
	}
	
	public void setCvItemName(String CV_II_NM) {
		
		this.CV_II_NM = CV_II_NM;
		
	}
	
	public double getCvBegInventory() {
		
		return CV_BEG_INVNTRY;
		
	}
	
	public void setCvBegInventory(double CV_BEG_INVNTRY) {
		
		this.CV_BEG_INVNTRY = CV_BEG_INVNTRY;
		
	}
	
	public double getCvDeliveries() {
		
		return CV_DLVRS;
		
	}
	
	public void setCvDeliveries(double CV_DLVRS) {
		
		this.CV_DLVRS = CV_DLVRS;
		
	}
	
	public double getCvAdjustQuantity() {
		
		return CV_ADJST_QTY;
		
	}
	
	public void setCvAdjustQuantity(double CV_ADJST_QTY) {
		
		this.CV_ADJST_QTY = CV_ADJST_QTY;
		
	}
	
	public double getCvStandardUsage() {
		
		return CV_STNDRD_USG;
		
	}
	
	public void setCvStandardUsage(double CV_STNDRD_USG) {
		
		this.CV_STNDRD_USG = CV_STNDRD_USG;
		
	}	
	
	public double getCvEndInventory() {
		
		return CV_END_INVNTRY;
		
	}
	
	public void setCvEndInventory(double CV_END_INVNTRY) {
		
		this.CV_END_INVNTRY = CV_END_INVNTRY;
		
	}
	
	public double getCvPhysicalInventory() {
		
		return CV_PHYSCL_INVNTRY;
		
	}
	
	public void setCvPhysicalInventory(double CV_PHYSCL_INVNTRY) {
		
		this.CV_PHYSCL_INVNTRY = CV_PHYSCL_INVNTRY;
		
	}
	
	public double getCvWastage() {
		
		return CV_WSTG;
		
	}
	
	public void setCvWastage(double CV_WSTG) {
		
		this.CV_WSTG = CV_WSTG;
		
	}
		
	public double getCvVariance() {
		
		return CV_VRNC;
		
	}
	
	public void setCvVariance(double CV_VRNC) {
		
		this.CV_VRNC = CV_VRNC;
		
	}

} // InvRepCountVarianceDetails class