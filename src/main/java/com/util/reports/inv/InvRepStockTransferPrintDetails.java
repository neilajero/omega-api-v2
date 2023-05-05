/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Comparator;
import java.util.Date;

public class InvRepStockTransferPrintDetails implements java.io.Serializable {
    
    private Date STP_ST_DT; 
    private String STP_ST_DCMNT_NMBR; 
    private String STP_ST_RFRNC_NMBR;
    private String STP_ST_CRTD_BY;
    private String STP_ST_APPRVD_RJCTD_BY;
    private String STP_ST_DESC;
    private String STP_STL_LOC_TO;
    private double STP_STL_QTY_DLVRD; 
    private String STP_STL_UNIT;
    private String STP_STL_ITM_NM;
    private String STP_STL_ITM_DESC;
    private String STP_STL_LOC_FRM;
    private double STP_STL_UNT_PRC;
    private double STP_STL_AMNT;
    private String STP_STL_ITM_CTGRY;
    
    public InvRepStockTransferPrintDetails() {
    }
    
    public Date getStpStDate() {
        
        return STP_ST_DT;
        
    }
    
    public void setStpStDate(Date STP_ST_DT) {
        
        this.STP_ST_DT = STP_ST_DT;
        
    }
    
    public String getStpStDocumentNumber() {
        
        return STP_ST_DCMNT_NMBR;
        
    }
    
    public void setStpStDocumentNumber(String STP_ST_DCMNT_NMBR) {
        
        this.STP_ST_DCMNT_NMBR = STP_ST_DCMNT_NMBR;
        
    }
    
    public String getStpStReferenceNumber() {
        
        return STP_ST_RFRNC_NMBR;
        
    }
    
    public void setStpStReferenceNumber(String STP_ST_RFRNC_NMBR) {
        
        this.STP_ST_RFRNC_NMBR = STP_ST_RFRNC_NMBR;
                
    }
    
    public String getStpStCreatedBy() {
        
        return STP_ST_CRTD_BY;
        
    }
    
    public void setStpStCreatedBy(String STP_ST_CRTD_BY) {
        
        this.STP_ST_CRTD_BY = STP_ST_CRTD_BY;
        
    }
    
    public String getStpStApprovedRejectedBy() {
        
        return STP_ST_APPRVD_RJCTD_BY;
        
    }
    
    public void setStpStApprovedRejectedBy(String STP_ST_APPRVD_RJCTD_BY) {
        
        this.STP_ST_APPRVD_RJCTD_BY = STP_ST_APPRVD_RJCTD_BY;
        
    }
    
    public String getStpStDescription() {
        
        return STP_ST_DESC;
        
    }
    
    public void setStpStDescription(String STP_ST_DESC) {
        
        this.STP_ST_DESC = STP_ST_DESC;
        
    }

    public String getStpStlLocationTo() {
        
        return STP_STL_LOC_TO;
        
    }
    
    public void setStpStlLocationTo(String STP_STL_LOC_TO) {
        
        this.STP_STL_LOC_TO = STP_STL_LOC_TO;
        
    }
    
    public double getStpStlQuantityDelivered() {
        
        return STP_STL_QTY_DLVRD;
        
    }
    
    public void setStpStlQuantityDelivered(double STP_STL_QTY_DLVRD) {
        
        this.STP_STL_QTY_DLVRD = STP_STL_QTY_DLVRD;
        
    }
    
    public String getStpStlUnit() {
        
        return STP_STL_UNIT;
        
    }
    
    public void setStpStlUnit(String STP_STL_UNIT) {
        
        this.STP_STL_UNIT = STP_STL_UNIT;
        
    }
    
    public String getStpStlItemName() {
        
        return STP_STL_ITM_NM;
        
    }
    
    public void setStpStlItemName(String STP_STL_ITM_NM) {
        
        this.STP_STL_ITM_NM = STP_STL_ITM_NM;
        
    }
    
    public String getStpStlItemDescription() {
        
        return STP_STL_ITM_DESC;
        
    }
    
    public void setStpStlItemDescription(String STP_STL_ITM_DESC) {
        
        this.STP_STL_ITM_DESC = STP_STL_ITM_DESC;
        
    }
    
    public String getStpStlLocationFrom() {
        
        return STP_STL_LOC_FRM;
        
    }
    
    public void setStpStlLocationFrom(String STP_STL_LOC_FRM) {
        
        this.STP_STL_LOC_FRM = STP_STL_LOC_FRM;
        
    }
    
    public double getStpStlUnitPrice() {
        
        return STP_STL_UNT_PRC;
        
    }
    
    public void setStpStlUnitPrice(double STP_STL_UNT_PRC) {
        
        this.STP_STL_UNT_PRC = STP_STL_UNT_PRC;
        
    }
    
    public double getStpStlAmount() {
        
        return STP_STL_AMNT;
        
    }
    
    public void setStpStlAmount(double STP_STL_AMNT) {
        
        this.STP_STL_AMNT = STP_STL_AMNT;
        
    }
    
    public String getStpStlItemCategory() {
    	
    	return STP_STL_ITM_CTGRY;
    	
    }
    
    public void setStpStlItemCategory(String STP_STL_ITM_CTGRY) {
    	
    	this.STP_STL_ITM_CTGRY = STP_STL_ITM_CTGRY;
    	
    }
    
    public static Comparator LocationToComparator = (STP, anotherSTP) -> {

        String STP_STL_LCTN_TO1 = ((InvRepStockTransferPrintDetails)STP).getStpStlLocationTo();
        String STP_STL_LCTN_TO2 = ((InvRepStockTransferPrintDetails)anotherSTP).getStpStlLocationTo();

        return STP_STL_LCTN_TO1.compareTo(STP_STL_LCTN_TO2);

    };
    
} // InvRepStockTransferPrintDetails class 