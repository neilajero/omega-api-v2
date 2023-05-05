/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Date;



public class InvRepStockIssuancePrintDetails implements java.io.Serializable {
	
	private Date SIP_SI_DT;
	private String SIP_SI_DCMNT_NMBR;
	private String SIP_SI_RFRNC_NMBR;
	private String SIP_SI_DESC;
	private String SIP_SI_CRTD_BY;
	private String SIP_SI_APRVD_RJCTD;
	private double SIL_ISSUE_QTY;
	private String SIL_IL_LOC_NM;
	private String SIL_IL_II_NM;
	private String SIL_IL_II_DESC;
	private String SIL_IL_II_UOM_NM;
	private double SIL_BOS_QTY_RQRD;
	private double SIL_BOS_UNT_CST;
	
	public InvRepStockIssuancePrintDetails() {
    }
	
	public String getSipSiApprovedRejected() {
		
		return SIP_SI_APRVD_RJCTD;
		
	}
	
	public void setSipSiApprovedRejected(String SIP_SI_APRVD_RJCTD) {
		
		this.SIP_SI_APRVD_RJCTD = SIP_SI_APRVD_RJCTD;
		
	}
	
	public String getSipSiCreatedBy() {
		
		return SIP_SI_CRTD_BY;
		
	}
	
	public void setSipSiCreatedBy(String SIP_SI_CRTD_BY) {
		
		this.SIP_SI_CRTD_BY = SIP_SI_CRTD_BY;
		
	}
	
	public String getSipSiDocumentNumber() {
		
		return SIP_SI_DCMNT_NMBR;
		
	}
	
	public void setSipSiDocumentNumbert(String SIP_SI_DCMNT_NMBR) {
		
		this.SIP_SI_DCMNT_NMBR = SIP_SI_DCMNT_NMBR;
		
	}
	
	public Date getSipSiDate() {
		
		return SIP_SI_DT;
		
	}
	
	public void setSipSiDate(Date SIP_SI_DT) {
		
		this.SIP_SI_DT = SIP_SI_DT;
		
	}
	
	public String getSipSiReferenceNumber() {
		
		return SIP_SI_RFRNC_NMBR;
		
	}
	
	public void setSipSiReferenceNumber(String SIP_SI_RFRNC_NMBR) {
		
		this.SIP_SI_RFRNC_NMBR = SIP_SI_RFRNC_NMBR;
		
	}
	
	public String getSipSiDescription() {
		
		return SIP_SI_DESC;
		
	}
	
	public void setSipSiDescription(String SIP_SI_DESC) {
		
		this.SIP_SI_DESC = SIP_SI_DESC;
		
	}
	
	public double getSilBosQuantityRequired() {
		
		return SIL_BOS_QTY_RQRD;
		
	}
	
	public void setSilBosQuantityRequired(double SIL_BOS_QTY_RQRD) {
		
		this.SIL_BOS_QTY_RQRD = SIL_BOS_QTY_RQRD;
		
	}
	
	public String getSilIlIiName() {
		
		return SIL_IL_II_NM;
		
	}
	
	public void setSilIlIiName(String SIL_IL_II_NM) {
		
		this.SIL_IL_II_NM = SIL_IL_II_NM;
		
	}
	
	public String getSilIlIiDescription() {
		
		return SIL_IL_II_DESC;
		
	}
	
	public void setSilIlIiDescription(String SIL_IL_II_DESC) {
		
		this.SIL_IL_II_DESC = SIL_IL_II_DESC;
		
	}
	
	public String getSilIlIiUomName() {
		
		return SIL_IL_II_UOM_NM;
		
	}
	
	public void setSilIlIiUomName(String SIL_IL_II_UOM_NM) {
		
		this.SIL_IL_II_UOM_NM = SIL_IL_II_UOM_NM;
		
	}
	
	public String getSilIlLocationName() {
		
		return SIL_IL_LOC_NM;
		
	}
	
	public void setSilIlLocationName(String SIL_IL_LOC_NM) {
		
		this.SIL_IL_LOC_NM = SIL_IL_LOC_NM;
		
	}
	
	public double getSilIssueQuantity() {
		
		return SIL_ISSUE_QTY;
		
	}
	
	public void setSilIssueQuantity(double SIL_ISSUE_QTY) {
		
		this.SIL_ISSUE_QTY = SIL_ISSUE_QTY;
		
	}
	
	public double getSilUnitCost() {
		
		return SIL_BOS_UNT_CST;
		
	}
	
	public void setSilUnitCost(double SIL_BOS_UNT_CST) {
		
		this.SIL_BOS_UNT_CST = SIL_BOS_UNT_CST;
		
	}
	
} // InvRepStockIssuancePrintDetails