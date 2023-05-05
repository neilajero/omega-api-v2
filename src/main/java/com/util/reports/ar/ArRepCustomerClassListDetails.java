/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;


public class ArRepCustomerClassListDetails implements java.io.Serializable {

   private String CCL_CC_NM; 
   private String CCL_CC_DESC; 
   private String CCL_TX_NM; 
   private String CCL_WT_NM; 
   private String CCL_RVNUE_ACCNT_NMBR;  
   private String CCL_RVNUE_ACCNT_DESC; 
   private String CCL_RCVBL_ACCNT_NMBR;  
   private String CCL_RCVBL_ACCNT_DESC; 
   private byte CCL_ENBL;

   public ArRepCustomerClassListDetails() {
   }

   public byte getCclEnable() {
   	
   	 return CCL_ENBL;
   
   }
   
   public void setCclEnable(byte CCL_ENBL) {
   
   	 this.CCL_ENBL = CCL_ENBL;
   
   }
   
   public String getCclReceivableAccountDescription() {
   
   	 return CCL_RCVBL_ACCNT_DESC;
   
   }
   
   public void setCclReceivableAccountDescription(String CCL_RCVBL_ACCNT_DESC) {
   
   	 this.CCL_RCVBL_ACCNT_DESC = CCL_RCVBL_ACCNT_DESC;
   
   }
   
   public String getCclReceivableAccountNumber() {
   
   	 return CCL_RCVBL_ACCNT_NMBR;
   
   }
   
   public void setCclReceivableAccountNumber(String CCL_RCVBL_ACCNT_NMBR) {
   
   	 this.CCL_RCVBL_ACCNT_NMBR = CCL_RCVBL_ACCNT_NMBR;
   
   }
   
   public String getCclRevenueAccountDescription() {
   
   	 return CCL_RVNUE_ACCNT_DESC;
   
   }
   
   public void setCclRevenueAccountDescription(String CCL_RVNUE_ACCNT_DESC) {
  
   	 this.CCL_RVNUE_ACCNT_DESC = CCL_RVNUE_ACCNT_DESC;
   
   }
   
   public String getCclRevenueAccountNumber() {
   
   	 return CCL_RVNUE_ACCNT_NMBR;
   
   }
   
   public void setCclRevenueAccountNumber(String CCL_RVNUE_ACCNT_NMBR) {
   
   	 this.CCL_RVNUE_ACCNT_NMBR = CCL_RVNUE_ACCNT_NMBR;
   
   }
   
   public String getCclCcDescription() {
   
   	 return CCL_CC_DESC;
   
   }
   
   public void setCclCcDescription(String CCL_CC_DESC) {
   
   	 this.CCL_CC_DESC = CCL_CC_DESC;
   
   }
   
   public String getCclCcName() {
   
   	 return CCL_CC_NM;
   
   }
   
   public void setCclCcName(String CCL_CC_NM) {
   
   	 this.CCL_CC_NM = CCL_CC_NM;
   
   }
   
   public String getCclTaxName() {
   
   	 return CCL_TX_NM;
   
   }
   
   public void setCclTaxName(String CCL_TX_NM) {
   
   	 this.CCL_TX_NM = CCL_TX_NM;
   
   }
   
   public String getCclWithholdingTaxName() {
   
   	 return CCL_WT_NM;
   
   }
   
   public void setCclWithholdingTaxName(String CCL_WT_NM) {
   
   	 this.CCL_WT_NM = CCL_WT_NM;
   
   }
   
} // ArRepCustomerClassListDetails class