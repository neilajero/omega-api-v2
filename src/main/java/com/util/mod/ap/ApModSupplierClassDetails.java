/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;


import com.util.ap.ApSupplierClassDetails;

public class ApModSupplierClassDetails extends ApSupplierClassDetails implements java.io.Serializable {

    private String SC_TC_NM;
    private String SC_WTC_NM;
    private String SC_COA_PYBL_ACCNT_NMBR;
    private String SC_COA_EXPNS_ACCNT_NMBR;
    private String SC_COA_PYBL_ACCNT_DESC;
    private String SC_COA_EXPNS_ACCNT_DESC;

    public ApModSupplierClassDetails() {
    }
        
    public String getScTcName() {
    	
    	return SC_TC_NM;
    	
    }
    
    public void setScTcName(String SC_TC_NM) {
    	
    	this.SC_TC_NM = SC_TC_NM;
    	
    }
    
    public String getScWtcName() {
    	
    	return SC_WTC_NM;
    	
    }
    
    public void setScWtcName(String SC_WTC_NM) {
    	
    	this.SC_WTC_NM = SC_WTC_NM;
    	
    }   

    public String getScCoaGlPayableAccountNumber() {
   	
   	    return SC_COA_PYBL_ACCNT_NMBR;
   	
    }
   
    public void setScCoaGlPayableAccountNumber(String SC_COA_PYBL_ACCNT_NMBR) {
   	
   	    this.SC_COA_PYBL_ACCNT_NMBR = SC_COA_PYBL_ACCNT_NMBR;
   	
    }
   
    public String getScCoaGlPayableAccountDescription() {
   	
   	    return SC_COA_PYBL_ACCNT_DESC;
   	 
    }
    
    public void setScCoaGlPayableAccountDescription(String SC_COA_PYBL_ACCNT_DESC) {
    	
    	this.SC_COA_PYBL_ACCNT_DESC = SC_COA_PYBL_ACCNT_DESC;
    	
    }
    
    public String getScCoaGlExpenseAccountNumber() {
    	
    	return SC_COA_EXPNS_ACCNT_NMBR;
    	
    }
    
    public void setScCoaGlExpenseAccountNumber(String SC_COA_EXPNS_ACCNT_NMBR) {
    
        this.SC_COA_EXPNS_ACCNT_NMBR = SC_COA_EXPNS_ACCNT_NMBR;
        
    }
    
    public String getScCoaGlExpenseAccountDescription() {
    	
    	return SC_COA_EXPNS_ACCNT_DESC;
    	
    }
    
    public void setScCoaGlExpenseAccountDescription(String SC_COA_EXPNS_ACCNT_DESC) {
    	
    	this.SC_COA_EXPNS_ACCNT_DESC = SC_COA_EXPNS_ACCNT_DESC;
    	
    }
    	    	    	      
} // ApModSupplierClassDetails class