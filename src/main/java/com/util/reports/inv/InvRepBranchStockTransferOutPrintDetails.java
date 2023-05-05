/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Date;

public class InvRepBranchStockTransferOutPrintDetails implements java.io.Serializable {
    
    private Date BSTP_BST_DT; 
    private String BSTP_BST_TYP;
    private String BSTP_BST_NMBR; 
    private String BSTP_BST_CRTD_BY;
    private String BSTP_BST_APPRVD_RJCTD_BY;
    private String BSTP_BST_DSCRPTN;
    private String BSTP_BST_TRNST_LCTN;
    private String BSTP_BST_LCTN;
    private String BSTP_BST_BRNCH;

    private double BSTP_BSL_QTY; 
    private double BSTP_BSL_ORDRD_QTY; 
    private String BSTP_BSL_UNIT_OF_MEASURE;
    private String BSTP_BSL_ITM_NM;
    private String BSTP_BSL_ITM_DSCRPTN;
    private double BSTP_BSL_UNT_PRC;
    private double BSTP_BSL_AMNT;
    private double BSTP_BSL_SLS_PRC;
    private String BSTP_BSL_MSC_ITM;
    
    public InvRepBranchStockTransferOutPrintDetails() {
    }
    
    public Date getBstpBstDate() {
        
        return BSTP_BST_DT;
        
    }
    
    public void setBstpBstDate(Date BSTP_BST_DT) {
        
        this.BSTP_BST_DT = BSTP_BST_DT;
        
    }

    public String getBstpBstType() {
        
        return BSTP_BST_TYP;
        
    }
    
    public void setBstpBstType(String BSTP_BST_TYP) {
        
        this.BSTP_BST_TYP = BSTP_BST_TYP;
        
    }
    
    public String getBstpBstNumber() {
        
        return BSTP_BST_NMBR;
        
    }
    
    public void setBstpBstNumber(String BSTP_BST_NMBR) {
        
        this.BSTP_BST_NMBR = BSTP_BST_NMBR;
        
    }
    
    public String getBstpBstCreatedBy() {
        
        return BSTP_BST_CRTD_BY;
        
    }
    
    public void setBstpBstCreatedBy(String BSTP_BST_CRTD_BY) {
        
        this.BSTP_BST_CRTD_BY = BSTP_BST_CRTD_BY;
        
    }
    
    public String getBstpBstApprovedRejectedBy() {
        
        return BSTP_BST_APPRVD_RJCTD_BY;
        
    }
    
    public void setBstpBstApprovedRejectedBy(String BSTP_BST_APPRVD_RJCTD_BY) {
        
        this.BSTP_BST_APPRVD_RJCTD_BY = BSTP_BST_APPRVD_RJCTD_BY;
        
    }
    
    public String getBstpBstDescription() {
        
        return BSTP_BST_DSCRPTN;
        
    }
    
    public void setBstpBstDescription(String BSTP_BST_DSCRPTN) {
        
        this.BSTP_BST_DSCRPTN = BSTP_BST_DSCRPTN;
        
    }
    
    public String getBstpBstTransitLocation() {
        
        return BSTP_BST_TRNST_LCTN;
        
    }
    
    public void setBstpBstTransitLocation(String BSTP_BST_TRNST_LCTN) {
        
        this.BSTP_BST_TRNST_LCTN = BSTP_BST_TRNST_LCTN;
        
    }
    
    public String getBstpBstLocation() {
        
        return BSTP_BST_LCTN;
        
    }
    
    public void setBstpBstLocation(String BSTP_BST_LCTN) {
        
        this.BSTP_BST_LCTN = BSTP_BST_LCTN;
        
    }
    
    public String getBstpBstBranch() {
        
        return BSTP_BST_BRNCH;
        
    }
    
    public void setBstpBstBranch(String BSTP_BST_BRNCH) {
        
        this.BSTP_BST_BRNCH = BSTP_BST_BRNCH;
        
    }

    public double getBstpBslQuantity() {
        
        return BSTP_BSL_QTY;
        
    }
    
    public void setBstpBslQuantity(double BSTP_BSL_QTY) {
        
        this.BSTP_BSL_QTY = BSTP_BSL_QTY;
        
    }

    public double getBstpBslOrderedQuantity() {
        
        return BSTP_BSL_ORDRD_QTY;
        
    }
    
    public void setBstpBslOrderedQuantity(double BSTP_BSL_ORDRD_QTY) {
        
        this.BSTP_BSL_ORDRD_QTY = BSTP_BSL_ORDRD_QTY;
        
    }
    
    public String getBstpBslUnitOfMeasure() {
        
        return BSTP_BSL_UNIT_OF_MEASURE;
        
    }
    
    public void setBstpBslUnitOfMeasure(String BSTP_BSL_UNIT_OF_MEASURE) {
        
        this.BSTP_BSL_UNIT_OF_MEASURE = BSTP_BSL_UNIT_OF_MEASURE;
        
    }
    
    public String getBstpBslItemName() {
        
        return BSTP_BSL_ITM_NM;
        
    }
    
    public void setBstpBslItemName(String BSTP_BSL_ITM_NM) {
        
        this.BSTP_BSL_ITM_NM = BSTP_BSL_ITM_NM;
        
    }
    
    public String getBstpBslItemDescription() {
        
        return BSTP_BSL_ITM_DSCRPTN;
        
    }
    
    public void setBstpBslItemDescription(String BSTP_BSL_ITM_DSCRPTN) {
        
        this.BSTP_BSL_ITM_DSCRPTN = BSTP_BSL_ITM_DSCRPTN;
        
    }
    
    public double getBstpBslUnitPrice() {
        
        return BSTP_BSL_UNT_PRC;
        
    }
    
    public void setBstpBslUnitPrice(double BSTP_BSL_UNT_PRC) {
        
        this.BSTP_BSL_UNT_PRC = BSTP_BSL_UNT_PRC;
        
    }
    
    public double getBstpBslAmount() {
        
        return BSTP_BSL_AMNT;
        
    }
    
    public void setBstpBslAmount(double BSTP_BSL_AMNT) {
        
        this.BSTP_BSL_AMNT = BSTP_BSL_AMNT;
        
    }

    public double getBstpBslSlsPrc() {

    	return BSTP_BSL_SLS_PRC;

    }

    public void setBstpSlsPrc(double BSTP_BSL_SLS_PRC) {

    	this.BSTP_BSL_SLS_PRC = BSTP_BSL_SLS_PRC;

    }
    
    public String getBstpBslMscItm() {

    	return BSTP_BSL_MSC_ITM;

    }
    
    public void setBstpBslMscItm(String BSTP_BSL_MSC_ITM) {

    	this.BSTP_BSL_MSC_ITM = BSTP_BSL_MSC_ITM;

    }
    
} // InvRepBranchStockTransferOutPrintDetails class 