/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;


import com.util.ar.ArStandardMemoLineDetails;

public class ArModStandardMemoLineDetails extends ArStandardMemoLineDetails implements java.io.Serializable {

    private String SML_GL_COA_ACCNT_NMBR;
    private String SML_GL_COA_ACCNT_DESC;
    
    private String SML_GL_COA_RCVBL_ACCNT_NMBR;
    private String SML_GL_COA_RCVBL_ACCNT_DESC;
    private String SML_GL_COA_RVN_ACCNT_NMBR;
    private String SML_GL_COA_RVN_ACCNT_DESC;
    
    private String SML_INTRM_ACCNT_NMBR;
    private String SML_INTRM_ACCNT_DESC;

    public ArModStandardMemoLineDetails() {
    }
        
    public String getSmlGlCoaAccountNumber() {
   	
   	    return SML_GL_COA_ACCNT_NMBR;
   	
    }
   
    public void setSmlGlCoaAccountNumber(String SML_GL_COA_ACCNT_NMBR) {
   	
   	    this.SML_GL_COA_ACCNT_NMBR = SML_GL_COA_ACCNT_NMBR;
   	
    }
   
    public String getSmlGlCoaAccountDescription() {
   	
   	    return SML_GL_COA_ACCNT_DESC;
   	 
    }
    
    public void setSmlGlCoaAccountDescription(String SML_GL_COA_ACCNT_DESC) {
    	
    	this.SML_GL_COA_ACCNT_DESC = SML_GL_COA_ACCNT_DESC;
    	
    } 
    
    
    
    public String getSmlGlCoaReceivableAccountNumber() {
       	
   	    return SML_GL_COA_RCVBL_ACCNT_NMBR;
   	
    }
   
    public void setSmlGlCoaReceivableAccountNumber(String SML_GL_COA_RCVBL_ACCNT_NMBR) {
   	
   	    this.SML_GL_COA_RCVBL_ACCNT_NMBR = SML_GL_COA_RCVBL_ACCNT_NMBR;
   	
    }
   
    public String getSmlGlCoaReceivableAccountDescription() {
   	
   	    return SML_GL_COA_RCVBL_ACCNT_DESC;
   	 
    }
    
    public void setSmlGlCoaReceivableAccountDescription(String SML_GL_COA_RCVBL_ACCNT_DESC) {
    	
    	this.SML_GL_COA_RCVBL_ACCNT_DESC = SML_GL_COA_RCVBL_ACCNT_DESC;
    	
    }
    
    
    
    
    public String getSmlGlCoaRevenueAccountNumber() {
       	
   	    return SML_GL_COA_RVN_ACCNT_NMBR;
   	
    }
   
    public void setSmlGlCoaRevenueAccountNumber(String SML_GL_COA_RVN_ACCNT_NMBR) {
   	
   	    this.SML_GL_COA_RVN_ACCNT_NMBR = SML_GL_COA_RVN_ACCNT_NMBR;
   	
    }
   
    public String getSmlGlCoaRevenueAccountDescription() {
   	
   	    return SML_GL_COA_RVN_ACCNT_DESC;
   	 
    }
    
    public void setSmlGlCoaRevenueAccountDescription(String SML_GL_COA_RVN_ACCNT_DESC) {
    	
    	this.SML_GL_COA_RVN_ACCNT_DESC = SML_GL_COA_RVN_ACCNT_DESC;
    	
    }
    
    
    public String getSmlInterimAccountNumber() {
    	
    	return SML_INTRM_ACCNT_NMBR;
    	
    }
    
    public void setSmlInterimAccountNumber(String SML_INTRM_ACCNT_NMBR) {
    	
    	
    	this.SML_INTRM_ACCNT_NMBR = SML_INTRM_ACCNT_NMBR;
    }
    
    public String getSmlInterimAccountDescription() {
    	
    	return SML_INTRM_ACCNT_DESC;
    	
    }
    
    public void setSmlInterimAccountDescription(String SML_INTRM_ACCNT_DESC) {
    	    	
    	this.SML_INTRM_ACCNT_DESC = SML_INTRM_ACCNT_DESC;
    	
    }
    	    	    	      
} // ArModStandardMemoLineDetails class