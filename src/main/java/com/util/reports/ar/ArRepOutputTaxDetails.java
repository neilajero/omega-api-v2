/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;


public class ArRepOutputTaxDetails implements java.io.Serializable{

   private String OT_TIN_OF_CSTMR;
   private String OT_RGSTRD_NM;
   private String OT_LST_NM;
   private String OT_FRST_NM;
   private String OT_MDDL_NM;
   private String OT_ADDRSS1;
   private String OT_ADDRSS2;   
   private double OT_NET_AMNT;
   private double OT_OUTPT_TX;
   private String OT_TRNSCTN;

   public ArRepOutputTaxDetails() {
   }
   
   public String getOtTinOfCustomer() {
   	
   		return OT_TIN_OF_CSTMR;
   		
   }
   
   public void setOtTinOfCustomer(String OT_TIN_OF_CSTMR) {
   	
   		this.OT_TIN_OF_CSTMR = OT_TIN_OF_CSTMR;
   		
   }
   
   public String getOtRegisteredName() {
   	
   		return OT_RGSTRD_NM;
   		
   }
   
   public void setOtRegisteredName(String OT_RGSTRD_NM) {
   	
   		this.OT_RGSTRD_NM = OT_RGSTRD_NM;
   		
   }
   
   public String getOtLastName() {
   	
   		return OT_LST_NM;
   		
   }
   
   public void setOtLastName(String OT_LST_NM) {
   	
   		this.OT_LST_NM = OT_LST_NM;
   		
   }
   
   public String getOtFirstName() {
   	
   		return OT_FRST_NM;
   		
   }
   
   public void setOtFirstName(String OT_FRST_NM) {
   	
   		this.OT_FRST_NM = OT_FRST_NM;
   		
   }
   
   public String getOtMiddleName() {
   	
   		return OT_MDDL_NM;
   		
   }
   
   public void setOtMiddleName(String OT_MDDL_NM) {
   	
   		this.OT_MDDL_NM = OT_MDDL_NM;
   		
   }
   
   public String getOtAddress1() {
   	
   		return OT_ADDRSS1;
   		
   }
   
   public void setOtAddress1(String OT_ADDRSS1) {
   	
   		this.OT_ADDRSS1 = OT_ADDRSS1;
   		
   }
   
   public String getOtAddress2() {
   	
   		return OT_ADDRSS2;
   		
   }
   
   public void setOtAddress2(String OT_ADDRSS2) {
   	
   		this.OT_ADDRSS2 = OT_ADDRSS2;
   		
   }
      
   public double getOtNetAmount() {
   	
   		return OT_NET_AMNT;
   		
   }
   
   public void setOtNetAmount(double OT_NET_AMNT) {
   	
   		this.OT_NET_AMNT = OT_NET_AMNT;
   		
   }
   
   public double getOtOutputTax() {
   	
   		return OT_OUTPT_TX;
   		
   }
   
   public void setOtOutputTax(double OT_OUTPT_TX) {
   	
   		this.OT_OUTPT_TX = OT_OUTPT_TX;
   		
   }
   
   public String getOtTransaction() {
	   
	   return OT_TRNSCTN;
	   
   }
   
   public void setOtTransaction(String OT_TRNSCTN) {
	   
	   this.OT_TRNSCTN = OT_TRNSCTN;
	   
   }
   
} // ArRepOutputTaxDetails class