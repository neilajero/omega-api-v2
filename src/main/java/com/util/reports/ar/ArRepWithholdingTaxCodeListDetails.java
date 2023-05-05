/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;


public class ArRepWithholdingTaxCodeListDetails implements java.io.Serializable {

   private String WTL_TX_NM; 
   private String WTL_TX_DESC;
   private double WTL_TX_RT; 
   private String WTL_COA_GL_TX_ACCNT_NMBR; 
   private String WTL_COA_GL_TX_ACCNT_DESC; 
   private byte WTL_ENBL;

   public ArRepWithholdingTaxCodeListDetails() {
   }

   public String getWtlCoaGlTaxAccountDescription() {
   	
   	 return WTL_COA_GL_TX_ACCNT_DESC;
   
   }
   
   public void setWtlCoaGlTaxAccountDescription(String WTL_COA_GL_TX_ACCNT_DESC) {
   
   	 this.WTL_COA_GL_TX_ACCNT_DESC = WTL_COA_GL_TX_ACCNT_DESC;
   
   }
   
   public String getWtlCoaGlTaxAccountNumber() {
   
     return WTL_COA_GL_TX_ACCNT_NMBR;
   
   }
   
   public void setWtlCoaGlTaxAccountNumber(String WTL_COA_GL_TX_ACCNT_NMBR) {
   
     this.WTL_COA_GL_TX_ACCNT_NMBR = WTL_COA_GL_TX_ACCNT_NMBR;
   
   }
   
   public byte getWtlEnable() {
     
     return WTL_ENBL;
   
   }
   
   public void setWtlEnable(byte WTL_ENBL) {
   
   	 this.WTL_ENBL = WTL_ENBL;
   
   }
   
   public String getWtlTaxDescription() {
   	 
   	 return WTL_TX_DESC;
   
   }
   public void setWtlTaxDescription(String WTL_TX_DESC) {
   	
   	 this.WTL_TX_DESC = WTL_TX_DESC;
   
   }
   
   public String getWtlTaxName() {
   	
   	 return WTL_TX_NM;
   
   }
   
   public void setWtlTaxName(String WTL_TX_NM) {
   	
   	 this.WTL_TX_NM = WTL_TX_NM;
   
   }
   
   public double getWtlTaxRate() {
   	 
   	 return WTL_TX_RT;
   
   }
   
   public void setWtlTaxRate(double WTL_TX_RT) {
   	
   	 this.WTL_TX_RT = WTL_TX_RT;
   
   }
   
} // ApRepWithholdingTaxCodeListDetails class