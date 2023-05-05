/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Date;

public class InvRepAssemblyTransferPrintDetails implements java.io.Serializable {

	private Date ATP_ATR_DT;
	private String ATP_ATR_DCMNT_NMBR;
	private String ATP_ATR_RFRNC_NMBR;
	private String ATP_ATR_CRTD_BY;
	private String ATP_ATR_APPRVD_RJCTD_BY;
	private String ATP_ATR_DESC;
	private double ATP_ATL_ASSMBL_QTY;
	private double ATP_ATL_BOL_QTY_RQRD;
	private String ATP_ATL_BOL_IL_II_NM;
	private String ATP_ATL_BOL_IL_II_DESC;
	private String ATP_ATL_BOL_IL_LOC_NM;
	private String ATP_ATL_BOL_IL_II_UOM_NM;
	private String ATP_ATL_BOL_BOR_DCMNT_NMBR;
	private double ATP_ATL_ASSMBL_CST;
	
	public InvRepAssemblyTransferPrintDetails() {
    }
    
    public Date getAtpAtrDate() {
	
    	return ATP_ATR_DT;
	
    }
	
    public void setAtpAtrDate(Date ATP_ATR_DT) {
	
    	this.ATP_ATR_DT = ATP_ATR_DT;
	
    }
    
    public String getAtpAtrDocumentNumber() {
	
    	return ATP_ATR_DCMNT_NMBR;
	
    }
	
    public void setAtpAtrDocumentNumber(String ATP_ATR_DCMNT_NMBR) {
	
    	this.ATP_ATR_DCMNT_NMBR = ATP_ATR_DCMNT_NMBR;
	
    }
    
    public String getAtpAtrReferenceNumber() {
	
    	return ATP_ATR_RFRNC_NMBR;
	
    }
	
    public void setAtpAtrReferenceNumber(String ATP_ATR_RFRNC_NMBR) {
	
    	this.ATP_ATR_RFRNC_NMBR = ATP_ATR_RFRNC_NMBR;
	
    }
    
    public String getAtpAtrCreatedBy() {
	
    	return ATP_ATR_CRTD_BY;
	
    }
	
    public void setAtpAtrCreatedBy(String ATP_ATR_CRTD_BY) {
	
    	this.ATP_ATR_CRTD_BY = ATP_ATR_CRTD_BY;
	
    }
    
    public String getAtpAtrApprovedRejectedBy() {   
	
    	return ATP_ATR_APPRVD_RJCTD_BY;
	
    }
	
    public void setAtpAtrApprovedRejectedBy(String ATP_ATR_APPRVD_RJCTD_BY) {
	
    	this.ATP_ATR_APPRVD_RJCTD_BY = ATP_ATR_APPRVD_RJCTD_BY;
	
    }
    
    public String getAtpAtrDescription() {
    	
    	return ATP_ATR_DESC;
    	
    }
    
    public void setAtpAtrDescription(String ATP_ATR_DESC) {
    	
    	this.ATP_ATR_DESC = ATP_ATR_DESC;
    }
    
    public double getAtpAtlAssembleQuantity() {
	
    	return ATP_ATL_ASSMBL_QTY;
	
    }
	
    public void setAtpAtlAssembleQuantity(double ATP_ATL_ASSMBL_QTY) {
	
    	this.ATP_ATL_ASSMBL_QTY = ATP_ATL_ASSMBL_QTY;
	
    }
	
    public String getAtpAtlBolIlIiName() {
	
    	return ATP_ATL_BOL_IL_II_NM;
	
    }
	
    public void setAtpAtlBolIlIiName(String ATP_ATL_BOL_IL_II_NM) {
	
    	this.ATP_ATL_BOL_IL_II_NM = ATP_ATL_BOL_IL_II_NM;
	
    }
    
    public String getAtpAtlBolIlIiDescription() {
    	
    	return ATP_ATL_BOL_IL_II_DESC;
	
    }
	
    public void setAtpAtlBolIlIiDescription(String ATP_ATL_BOL_IL_II_DESC) {
	
    	this.ATP_ATL_BOL_IL_II_DESC = ATP_ATL_BOL_IL_II_DESC;
	
    }
	
    public String getAtpAtlBolIlLocName() {
	
    	return ATP_ATL_BOL_IL_LOC_NM;
	
    }
	
    public void setAtpAtlBolIlLocName(String ATP_ATL_BOL_IL_LOC_NM) {
	
    	this.ATP_ATL_BOL_IL_LOC_NM = ATP_ATL_BOL_IL_LOC_NM;
	
    }
    
    public String getAtpAtlBolIlIiUomName() {
	
    	return ATP_ATL_BOL_IL_II_UOM_NM;
	
    }
	
    public void setAtpAtlBolIlIiUomName(String ATP_ATL_BOL_IL_II_UOM_NM) {
	
    	this.ATP_ATL_BOL_IL_II_UOM_NM = ATP_ATL_BOL_IL_II_UOM_NM;
	
    }
    
    public String getAtpAtlBolBorDocumentNumber() {
	
    	return ATP_ATL_BOL_BOR_DCMNT_NMBR;
	
    }
	
    public void setAtpAtlBolBorDocumentNumber(String ATP_ATL_BOL_BOR_DCMNT_NMBR) {
	
    	this.ATP_ATL_BOL_BOR_DCMNT_NMBR = ATP_ATL_BOL_BOR_DCMNT_NMBR;
	
    }
	
    public double getAtpAtlBolQtyRequired() {
	
    	return ATP_ATL_BOL_QTY_RQRD;
	
    }
	
    public void setAtpAtlBolQtyRequired(double ATP_ATL_BOL_QTY_RQRD) {
	
    	this.ATP_ATL_BOL_QTY_RQRD = ATP_ATL_BOL_QTY_RQRD;
	
    }
	
    public double getAtpAtlAssembleCost() {
    	
        	return ATP_ATL_ASSMBL_CST;
    	
        }
    	
        public void setAtpAtlAssembleCost(double ATP_ATL_ASSMBL_CST) {
    	
        	this.ATP_ATL_ASSMBL_CST = ATP_ATL_ASSMBL_CST;
    	
        }

} // InvRepAssemblyListDetails class