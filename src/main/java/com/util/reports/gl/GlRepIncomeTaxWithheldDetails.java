/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;


public class GlRepIncomeTaxWithheldDetails implements java.io.Serializable {

   private String NTR_OF_INCM_PYMNT;
   private String ATC_CD;
   private double TX_BS;
   private double TX_RT;
   private double TX_RQURD_WTHHLD;

   public GlRepIncomeTaxWithheldDetails() {
   }
   
   public GlRepIncomeTaxWithheldDetails(String NTR_OF_INCM_PYMNT, String ATC_CD, double TX_BS, 
       double TX_RT, double TX_RQURD_WTHHLD) {
       	
       this.NTR_OF_INCM_PYMNT = NTR_OF_INCM_PYMNT;
       this.ATC_CD = ATC_CD;
       this.TX_BS = TX_BS;
       this.TX_RT = TX_RT;
       this.TX_RQURD_WTHHLD = TX_RQURD_WTHHLD;
       	
	}

    public String getNatureOfIncomePayment() {
    	
    	return NTR_OF_INCM_PYMNT;
    	
    }
    
    public void setNatureOfIncomePayment(String NTR_OF_INCM_PYMNT) {
    	
    	this.NTR_OF_INCM_PYMNT = NTR_OF_INCM_PYMNT;
    	
    }
    
    public String getAtcCode() {
    	
    	return ATC_CD;
    	
    }
    
    public void setAtcCode(String ATC_CD) {
    	
    	this.ATC_CD = ATC_CD;
    	
    }
    
    public double getTaxBase() {
    	
    	return TX_BS;
    	
    }
    
    public void setTaxBase(double TX_BS) {
    	
    	this.TX_BS = TX_BS;
    	
    }
    
    public double getTaxRate() {
    	
    	return TX_RT;
    	
    }
    
    public void setTaxRate(double TX_RT) {
    	
    	this.TX_RT = TX_RT;
    	
    }
    
    public double getTaxRequiredWithheld() {
    	
    	return TX_RQURD_WTHHLD;
    	
    }
    
    public void setTaxRequiredWithheld(double TX_RQURD_WTHHLD) {
    	
    	this.TX_RQURD_WTHHLD = TX_RQURD_WTHHLD;
    	
    }

} // GlRepIncomeTaxWithheldDetailsDetails class   