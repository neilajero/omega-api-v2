/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;


import com.util.ar.ArDistributionRecordDetails;

public class ArModDistributionRecordDetails extends ArDistributionRecordDetails implements java.io.Serializable {

   private String DR_COA_ACCNT_NMBR;
   private String DR_COA_ACCNT_DESC;
   private String DR_RCT_NMBR;
   private String DR_TC_NM;
   private String DR_WTC_NM;
   
   public ArModDistributionRecordDetails() {
   }

   public String getDrCoaAccountNumber() {
   	
   	  return DR_COA_ACCNT_NMBR;
   	
   }
   
   public void setDrCoaAccountNumber(String DR_COA_ACCNT_NMBR) {
   	
   	  this.DR_COA_ACCNT_NMBR = DR_COA_ACCNT_NMBR;
   	
   }
   
   public String getDrCoaAccountDescription() {
   	
   	  return DR_COA_ACCNT_DESC;
   	
   }
   
   public void setDrCoaAccountDescription(String DR_COA_ACCNT_DESC) {
   	
   	  this.DR_COA_ACCNT_DESC = DR_COA_ACCNT_DESC;
   	
   }
   
   public String getDrReceiptNumber() {
   	
   	  return DR_RCT_NMBR;
   	
   }
   
   public void setDrReceiptNumber(String DR_RCT_NMBR) {
   	
   	  this.DR_RCT_NMBR = DR_RCT_NMBR;
   	
   }
   
   public String getDrTcName() {
   	
   	  return DR_TC_NM;
   	
   }
   
   public void setDrTcName(String DR_TC_NM) {
   	
   	  this.DR_TC_NM = DR_TC_NM;
   	
   }
   
   public String getDrWtcName() {
   	
   	  return DR_WTC_NM;
   	
   }
   
   public void setDrWtcName(String DR_WTC_NM) {
   	
   	  this.DR_WTC_NM = DR_WTC_NM;
   	
   }

} // ArModDistributionRecordDetails class