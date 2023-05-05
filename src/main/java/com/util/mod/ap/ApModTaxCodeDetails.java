/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;


import com.util.ap.ApTaxCodeDetails;

public class ApModTaxCodeDetails extends ApTaxCodeDetails implements java.io.Serializable {

    private String TC_COA_ACCNT_NMBR;
    private String TC_COA_ACCNT_DESC;

    public ApModTaxCodeDetails() {
    }

    public String getTcCoaGlTaxAccountNumber() {
   	
   	    return TC_COA_ACCNT_NMBR;
   	
    }
   
    public void setTcCoaGlTaxAccountNumber(String TC_COA_ACCNT_NMBR) {
   	
   	    this.TC_COA_ACCNT_NMBR = TC_COA_ACCNT_NMBR;
   	
    }
   
    public String getTcCoaGlTaxDescription() {
   	
   	    return TC_COA_ACCNT_DESC;
   	 
    }
    
    public void setTcCoaGlTaxDescription(String TC_COA_ACCNT_DESC) {
    	
    	this.TC_COA_ACCNT_DESC = TC_COA_ACCNT_DESC;
    	
    }
    	    	      
} // ApModTaxCodeDetails class