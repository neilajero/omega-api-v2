/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;


public class GlRepQuarterlyVatReturnDetails implements java.io.Serializable {

   private double SLS_RCPT_FR_QTR;
   private double TX_OUTPUT_QTR;
   private double NET_PRCHS_QTR;
   private double INPT_TX_QTR;

   public GlRepQuarterlyVatReturnDetails() {
   }
   
   public GlRepQuarterlyVatReturnDetails(double SLS_RCPT_FR_QTR, 
       double TX_OUTPUT_QTR, double NET_PRCHS_QTR, double INPT_TX_QTR) {
       	
       this.SLS_RCPT_FR_QTR = SLS_RCPT_FR_QTR;
       this.TX_OUTPUT_QTR = TX_OUTPUT_QTR;
       this.NET_PRCHS_QTR = NET_PRCHS_QTR;
       this.INPT_TX_QTR = INPT_TX_QTR;
       	
	}
    
    public double getSalesReceiptForQuarter() {
    	
    	return SLS_RCPT_FR_QTR;
    	
    }
    
    public void setSalesReceiptForQuarter(double SLS_RCPT_FR_QTR) {
    	
    	this.SLS_RCPT_FR_QTR = SLS_RCPT_FR_QTR;
    	
    }
    
    public double getTaxOutputQuarter() {
    	
    	return TX_OUTPUT_QTR;
    	
    }
    
    public void setTaxOutputQuarter(double TX_OUTPUT_QTR) {
    	
    	this.TX_OUTPUT_QTR = TX_OUTPUT_QTR;
    	
    }
    
    public double getNetPurchasesQuarter() {
    	
    	return NET_PRCHS_QTR;
    	
    }
    
    public void setNetPurchasesQuarter(double NET_PRCHS_QTR) {
    	
    	this.NET_PRCHS_QTR = NET_PRCHS_QTR;
    	
    }
    
    public double getInputTaxQuarter() {
    	
    	return INPT_TX_QTR;
    	
    }
    
    public void setInputTaxQuarter(double INPT_TX_QTR) {
    	
    	this.INPT_TX_QTR = INPT_TX_QTR;
    	
    }

} // GlRepQuarterlyVatReturnDetailsDetails class   