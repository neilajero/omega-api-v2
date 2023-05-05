/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Date;

public class InvRepStockCardDetails implements java.io.Serializable {

	private String RSC_II_NM; 
	private String RSC_UNT;
	private Date RSC_DT;
	private String RSC_DCMNT_NMBR;
	private String RSC_RFRNC_NMBR;
	private String RSC_SRC;
        private String RSC_LCTN;
	private double RSC_IN_QTY; 
	private double RSC_OUT_QTY; 
	private double RSC_RMNNG_QTY;
	private double RSC_BGNNNG_QTY; 
	private String RSC_ACCNT;
	
    public InvRepStockCardDetails() {
    }

	public String getRscItemName() {
	
		return RSC_II_NM;
	
	}
	
	public void setRscItemName(String RSC_II_NM) {
	
		this.RSC_II_NM = RSC_II_NM;
	
	}
	
	public String getRscUnit() {
		
		return RSC_UNT;
		
	}
		
	public void setRscUnit(String RSC_UNT) {
		
		this.RSC_UNT = RSC_UNT;
		
	}
	
	public Date getRscDate() {
	
		return RSC_DT;
	
	}
	
	public void setRscDate(Date RSC_DT) {
		
		this.RSC_DT = RSC_DT;
	
	}
	
	public String getRscDocumentNumber() {
		
		return RSC_DCMNT_NMBR;
	
	}
	
	public void setRscDocumentNumber(String RSC_DCMNT_NMBR) {
		
		this.RSC_DCMNT_NMBR = RSC_DCMNT_NMBR;
	
	}
	
	public String getRscReferenceNumber() {
		
		return RSC_RFRNC_NMBR;
	
	}
	
	public void setRscReferenceNumber(String RSC_RFRNC_NMBR) {
		
		this.RSC_RFRNC_NMBR = RSC_RFRNC_NMBR;
	
	}
	
	public String getRscSource() {
		
		return RSC_SRC;
	
	}
	
	public void setRscSource(String RSC_SRC) {
		
		this.RSC_SRC = RSC_SRC;
	
	}
	
        public String getRscLocation() {
		
		return RSC_LCTN;
	
	}
	
	public void setRscLocation(String RSC_LCTN) {
		
		this.RSC_LCTN = RSC_LCTN;
	
	}
        
	public double getRscInQuantity() {
		
		return RSC_IN_QTY;
				
	}
	
	public void setRscInQuantity(double RSC_IN_QTY) {
		
		this.RSC_IN_QTY = RSC_IN_QTY;
		
	}
	
	public double getRscOutQuantity() {
		
		return RSC_OUT_QTY;
				
	}
	
	public void setRscOutQuantity(double RSC_OUT_QTY) {
		
		this.RSC_OUT_QTY = RSC_OUT_QTY;
		
	}
	
	public double getRscRemainingQuantity() {
		
		return RSC_RMNNG_QTY;
				
	}
	
	public void setRscRemainingQuantity(double RSC_RMNNG_QTY) {
		
		this.RSC_RMNNG_QTY = RSC_RMNNG_QTY;
		
	}
	
	public double getRscBeginningQuantity() {
		
		return RSC_BGNNNG_QTY;
				
	}
	
	public void setRscBeginningQuantity(double RSC_BGNNNG_QTY) {
		
		this.RSC_BGNNNG_QTY = RSC_BGNNNG_QTY;
		
	}
	
	public String getRscAccount() {
		
		return RSC_ACCNT;
		
	}
	
	public void setRscAccount(String RSC_ACCNT) {
		
		this.RSC_ACCNT = RSC_ACCNT;
	}
	
} // InvRepStockCardDetails class