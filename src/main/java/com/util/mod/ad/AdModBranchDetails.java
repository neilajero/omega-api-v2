/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


import com.util.ad.AdBranchDetails;

public class AdModBranchDetails extends AdBranchDetails implements java.io.Serializable {

    private String BR_GL_COA_ACCNT_NMBR;
    private String BR_GL_COA_ACCNT_DESC;
    
    public AdModBranchDetails() {
    }
        
    public String getBrGlCoaAccountNumber() {
   	
   	    return BR_GL_COA_ACCNT_NMBR;
   	
    }
   
    public void setBrGlCoaAccountNumber(String BR_GL_COA_ACCNT_NMBR) {
   	
   	    this.BR_GL_COA_ACCNT_NMBR = BR_GL_COA_ACCNT_NMBR;
   	
    }
   
    public String getBrGlCoaAccountDescription() {
   	
   	    return BR_GL_COA_ACCNT_DESC;
   	 
    }
    
    public void setBrGlCoaAccountDescription(String BR_GL_COA_ACCNT_DESC) {
    	
    	this.BR_GL_COA_ACCNT_DESC = BR_GL_COA_ACCNT_DESC;
    	
    } 
    	    	    	      
} // AdModBranchDetails class