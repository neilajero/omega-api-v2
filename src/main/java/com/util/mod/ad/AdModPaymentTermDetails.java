/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


import com.util.ad.AdPaymentTermDetails;

public class AdModPaymentTermDetails extends AdPaymentTermDetails implements java.io.Serializable {

    private String PT_GL_COA_ACCNT_NMBR;
    private String PT_GL_COA_ACCNT_DESC;
    
    public AdModPaymentTermDetails() {
    }
        
    public String getPtGlCoaAccountNumber() {
   	
   	    return PT_GL_COA_ACCNT_NMBR;
   	
    }
   
    public void setPtGlCoaAccountNumber(String PT_GL_COA_ACCNT_NMBR) {
   	
   	    this.PT_GL_COA_ACCNT_NMBR = PT_GL_COA_ACCNT_NMBR;
   	
    }
   
    public String getPtGlCoaAccountDescription() {
   	
   	    return PT_GL_COA_ACCNT_DESC;
   	 
    }
    
    public void setPtGlCoaAccountDescription(String PT_GL_COA_ACCNT_DESC) {
    	
    	this.PT_GL_COA_ACCNT_DESC = PT_GL_COA_ACCNT_DESC;
    	
    } 
    	    	    	      
} // AdModPaymentTermDetails class