/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;


import com.util.ar.ArCustomerClassDetails;

public class ArModCustomerClassDetails extends ArCustomerClassDetails implements java.io.Serializable {

    private String CC_TC_NM;
    private String CC_WTC_NM;
    private String CC_GL_COA_FNNC_CHRG_ACCNT_NMBR;
    private String CC_GL_COA_RCVBL_ACCNT_NMBR;
    private String CC_GL_COA_RVNUE_ACCNT_NMBR;
    private String CC_GL_COA_FNNC_CHRG_ACCNT_DESC;
    private String CC_GL_COA_RCVBL_ACCNT_DESC;
    private String CC_GL_COA_RVNUE_ACCNT_DESC;
    
    private String CC_GL_COA_UNERND_INT_ACCNT_NMBR;
    private String CC_GL_COA_UNERND_INT_ACCNT_DESC;
    
    private String CC_GL_COA_ERND_INT_ACCNT_NMBR;
    private String CC_GL_COA_ERND_INT_ACCNT_DESC;
    
    private String CC_GL_COA_UNERND_PNT_ACCNT_NMBR;
    private String CC_GL_COA_UNERND_PNT_ACCNT_DESC;
    
    private String CC_GL_COA_ERND_PNT_ACCNT_NMBR;
    private String CC_GL_COA_ERND_PNT_ACCNT_DESC;
    
    private double CC_CRDT_LMT;

    public ArModCustomerClassDetails() {
    }
        
    public String getCcTcName() {
    	
    	return CC_TC_NM;
    	
    }
    
    public void setCcTcName(String CC_TC_NM) {
    	
    	this.CC_TC_NM = CC_TC_NM;
    	
    }
    
    public String getCcWtcName() {
    	
    	return CC_WTC_NM;
    	
    }
    
    public void setCcWtcName(String CC_WTC_NM) {
    	
    	this.CC_WTC_NM = CC_WTC_NM;
    	
    }   

    public String getCcGlCoaFinanceChargeAccountNumber() {
   	
   	    return CC_GL_COA_FNNC_CHRG_ACCNT_NMBR;
   	
    }
   
    public void setCcGlCoaFinanceChargeAccountNumber(String CC_GL_COA_FNNC_CHRG_ACCNT_NMBR) {
   	
   	    this.CC_GL_COA_FNNC_CHRG_ACCNT_NMBR = CC_GL_COA_FNNC_CHRG_ACCNT_NMBR;
   	
    }
   
    public String getCcGlCoaFinanceChargeAccountDescription() {
   	
   	    return CC_GL_COA_FNNC_CHRG_ACCNT_DESC;
   	 
    }
    
    public void setCcGlCoaFinanceChargeAccountDescription(String CC_GL_COA_FNNC_CHRG_ACCNT_DESC) {
    	
    	this.CC_GL_COA_FNNC_CHRG_ACCNT_DESC = CC_GL_COA_FNNC_CHRG_ACCNT_DESC;
    	
    }
    
    public String getCcGlCoaReceivableAccountNumber() {
    	
    	return CC_GL_COA_RCVBL_ACCNT_NMBR;
    	
    }
    
    public void setCcGlCoaReceivableAccountNumber(String CC_GL_COA_RCVBL_ACCNT_NMBR) {
    
        this.CC_GL_COA_RCVBL_ACCNT_NMBR = CC_GL_COA_RCVBL_ACCNT_NMBR;
        
    }
    
    public String getCcGlCoaReceivableAccountDescription() {
    	
    	return CC_GL_COA_RCVBL_ACCNT_DESC;
    	
    }
    
    public void setCcGlCoaReceivableAccountDescription(String CC_GL_COA_RCVBL_ACCNT_DESC) {
    	
    	this.CC_GL_COA_RCVBL_ACCNT_DESC = CC_GL_COA_RCVBL_ACCNT_DESC;
    	
    }
    
    public String getCcGlCoaRevenueAccountNumber() {
    	
    	return CC_GL_COA_RVNUE_ACCNT_NMBR;
    	
    }
    
    public void setCcGlCoaRevenueAccountNumber(String CC_GL_COA_RVNUE_ACCNT_NMBR) {
    
        this.CC_GL_COA_RVNUE_ACCNT_NMBR = CC_GL_COA_RVNUE_ACCNT_NMBR;
        
    }
    
    public String getCcGlCoaRevenueAccountDescription() {
    	
    	return CC_GL_COA_RVNUE_ACCNT_DESC;
    	
    }
    
    public void setCcGlCoaRevenueAccountDescription(String CC_GL_COA_RVNUE_ACCNT_DESC) {
    	
    	this.CC_GL_COA_RVNUE_ACCNT_DESC = CC_GL_COA_RVNUE_ACCNT_DESC;
    	
    } 
    
    public double getCcCreditLimit() {
    	
    	return CC_CRDT_LMT;
    	
    }
    
    public void setCcCreditLimit(double CC_CRDT_LMT) {
    	
    	this.CC_CRDT_LMT = CC_CRDT_LMT;
    	
    }
    
    
public String getCcGlCoaUnEarnedInterestAccountNumber() {
    	
    	return CC_GL_COA_UNERND_INT_ACCNT_NMBR;
    	
    }
    
    public void setCcGlCoaUnEarnedInterestAccountNumber(String CC_GL_COA_UNERND_INT_ACCNT_NMBR) {
    
        this.CC_GL_COA_UNERND_INT_ACCNT_NMBR = CC_GL_COA_UNERND_INT_ACCNT_NMBR;
        
    }
    
    public String getCcGlCoaUnEarnedInterestAccountDescription() {
    	
    	return CC_GL_COA_UNERND_INT_ACCNT_DESC;
    	
    }
    
    public void setCcGlCoaUnEarnedInterestAccountDescription(String CC_GL_COA_UNERND_INT_ACCNT_DESC) {
    	
    	this.CC_GL_COA_UNERND_INT_ACCNT_DESC = CC_GL_COA_UNERND_INT_ACCNT_DESC;
    	
    } 
    
    
public String getCcGlCoaEarnedInterestAccountNumber() {
    	
    	return CC_GL_COA_ERND_INT_ACCNT_NMBR;
    	
    }
    
    public void setCcGlCoaEarnedInterestAccountNumber(String CC_GL_COA_ERND_INT_ACCNT_NMBR) {
    
        this.CC_GL_COA_ERND_INT_ACCNT_NMBR = CC_GL_COA_ERND_INT_ACCNT_NMBR;
        
    }
    
    public String getCcGlCoaEarnedInterestAccountDescription() {
    	
    	return CC_GL_COA_ERND_INT_ACCNT_DESC;
    	
    }
    
    public void setCcGlCoaEarnedInterestAccountDescription(String CC_GL_COA_ERND_INT_ACCNT_DESC) {
    	
    	this.CC_GL_COA_ERND_INT_ACCNT_DESC = CC_GL_COA_ERND_INT_ACCNT_DESC;
    	
    } 
    
    
    
public String getCcGlCoaUnEarnedPenaltyAccountNumber() {
    	
    	return CC_GL_COA_UNERND_PNT_ACCNT_NMBR;
    	
    }
    
    public void setCcGlCoaUnEarnedPenaltyAccountNumber(String CC_GL_COA_UNERND_PNT_ACCNT_NMBR) {
    
        this.CC_GL_COA_UNERND_PNT_ACCNT_NMBR = CC_GL_COA_UNERND_PNT_ACCNT_NMBR;
        
    }
    
    public String getCcGlCoaUnEarnedPenaltyAccountDescription() {
    	
    	return CC_GL_COA_UNERND_PNT_ACCNT_DESC;
    	
    }
    
    public void setCcGlCoaUnEarnedPenaltyAccountDescription(String CC_GL_COA_UNERND_PNT_ACCNT_DESC) {
    	
    	this.CC_GL_COA_UNERND_PNT_ACCNT_DESC = CC_GL_COA_UNERND_PNT_ACCNT_DESC;
    	
    } 
    
    
public String getCcGlCoaEarnedPenaltyAccountNumber() {
    	
    	return CC_GL_COA_ERND_PNT_ACCNT_NMBR;
    	
    }
    
    public void setCcGlCoaEarnedPenaltyAccountNumber(String CC_GL_COA_ERND_PNT_ACCNT_NMBR) {
    
        this.CC_GL_COA_ERND_PNT_ACCNT_NMBR = CC_GL_COA_ERND_PNT_ACCNT_NMBR;
        
    }
    
    public String getCcGlCoaEarnedPenaltyAccountDescription() {
    	
    	return CC_GL_COA_ERND_PNT_ACCNT_DESC;
    	
    }
    
    public void setCcGlCoaEarnedPenaltyAccountDescription(String CC_GL_COA_ERND_PNT_ACCNT_DESC) {
    	
    	this.CC_GL_COA_ERND_PNT_ACCNT_DESC = CC_GL_COA_ERND_PNT_ACCNT_DESC;
    	
    } 
    
	    
    	    	    	      
} // ArModCustomerClassDetails class