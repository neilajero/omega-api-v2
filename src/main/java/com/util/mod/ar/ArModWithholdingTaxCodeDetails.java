/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;


import com.util.ar.ArWithholdingTaxCodeDetails;

public class ArModWithholdingTaxCodeDetails extends ArWithholdingTaxCodeDetails implements java.io.Serializable {

    private String WTC_COA_ACCNT_NMBR;
    private String WTC_COA_ACCNT_DESC;

    public ArModWithholdingTaxCodeDetails() {
    }

    public String getWtcCoaGlWithholdingTaxAccountNumber() {
   	
   	    return WTC_COA_ACCNT_NMBR;
   	
    }
   
    public void setWtcCoaGlWithholdingTaxAccountNumber(String WTC_COA_ACCNT_NMBR) {
   	
   	    this.WTC_COA_ACCNT_NMBR = WTC_COA_ACCNT_NMBR;
   	
    }
   
    public String getWtcCoaGlWithholdingTaxAccountDescription() {
   	
   	    return WTC_COA_ACCNT_DESC;
   	 
    }
    
    public void setWtcCoaGlWithholdingTaxAccountDescription(String WTC_COA_ACCNT_DESC) {
    	
    	this.WTC_COA_ACCNT_DESC = WTC_COA_ACCNT_DESC;
    	
    }
    	    	      
} // ArModWithholdingTaxCodeDetails class