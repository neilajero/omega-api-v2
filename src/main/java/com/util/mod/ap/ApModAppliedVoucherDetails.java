/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.ap.ApAppliedVoucherDetails;

import java.util.Date;

public class ApModAppliedVoucherDetails extends ApAppliedVoucherDetails implements java.io.Serializable {

   private Integer AV_VPS_CODE;
   private Integer AV_VPS_VOU_CODE;
   private short AV_VPS_NMBR;
   private Date AV_VPS_DUE_DT;
   private double AV_VPS_AMNT_DUE;
   private String AV_VPS_VOU_DCMNT_NMBR;
   private String AV_VPS_VOU_RFRNC_NMBR;
   private String AV_VPS_VOU_DESC;
   private String AV_VPS_VOU_FC_NM;
   private String AV_VPS_CHK_DCMNT_NMBR; 
   private String AV_VPS_CHK_CHCK_NMBR;
   private String AV_VPS_CHK_BNK_ACCNT;
   private String AP_VPS_CNVRSN_DT;
   private double AP_VPS_CNVRSN_RT;
   
   public ApModAppliedVoucherDetails() {
   }

   public Integer getAvVpsCode() {
   	
       return AV_VPS_CODE;
   	
   }
   
   public void setAvVpsCode(Integer AV_VPS_CODE) {
   	
   	   this.AV_VPS_CODE = AV_VPS_CODE;
   	
   }
   
   public Integer getAvVpsVouCode() {
   	
       return AV_VPS_VOU_CODE;
   	
   }
   
   public void setAvVpsVouCode(Integer AV_VPS_VOU_CODE) {
   	
   	   this.AV_VPS_VOU_CODE = AV_VPS_VOU_CODE;
   	
   }
   
   public short getAvVpsNumber() {
   	
   	   return AV_VPS_NMBR;
   	
   }
   
   public void setAvVpsNumber(short AV_VPS_NMBR) {
   	
   	   this.AV_VPS_NMBR = AV_VPS_NMBR;
   	
   }
   
   public Date getAvVpsDueDate() {
   	
   	   return AV_VPS_DUE_DT;
   	
   }
   
   public void setAvVpsDueDate(Date AV_VPS_DUE_DT) {
   	
   	   this.AV_VPS_DUE_DT = AV_VPS_DUE_DT;
   	
   }
   
   public double getAvVpsAmountDue() {
   	
   	   return AV_VPS_AMNT_DUE;
   	
   }
   
   public void setAvVpsAmountDue(double AV_VPS_AMNT_DUE) {
   	
   	   this.AV_VPS_AMNT_DUE = AV_VPS_AMNT_DUE;
   	
   }
   
   public String getAvVpsVouDocumentNumber() {
   	
   	   return AV_VPS_VOU_DCMNT_NMBR;
   	
   }
   
   public void setAvVpsVouDocumentNumber(String AV_VPS_VOU_DCMNT_NMBR) {
   	
   	   this.AV_VPS_VOU_DCMNT_NMBR = AV_VPS_VOU_DCMNT_NMBR;
   	
   }
   
   public String getAvVpsVouReferenceNumber() {
   	
   	   return AV_VPS_VOU_RFRNC_NMBR;
   	
   }
   
   public void setAvVpsVouReferenceNumber(String AV_VPS_VOU_RFRNC_NMBR) {
   	
   	   this.AV_VPS_VOU_RFRNC_NMBR = AV_VPS_VOU_RFRNC_NMBR;
   	
   }
   
   public String getAvVpsVouDescription() {
   	
   	   return AV_VPS_VOU_DESC;
   	
   }
   
   public void setAvVpsVouDescription(String AV_VPS_VOU_DESC) {
   	
   	   this.AV_VPS_VOU_DESC = AV_VPS_VOU_DESC;
   	
   }
   
   public String getAvVpsVouFcName() {
   	
   	   return AV_VPS_VOU_FC_NM;
   	
   }
   
   public void setAvVpsVouFcName(String AV_VPS_VOU_FC_NM) {
   	
   	  this.AV_VPS_VOU_FC_NM = AV_VPS_VOU_FC_NM;
   	
   }
   
   public String getAvVpsChkDocumentNumber() {
   	
   	  return this.AV_VPS_CHK_DCMNT_NMBR;
   	
   }

   public void setAvVpsChkDocumentNumber(String AV_VPS_CHK_DCMNT_NMBR) {
   	
   	  this.AV_VPS_CHK_DCMNT_NMBR = AV_VPS_CHK_DCMNT_NMBR;
   	
   }
   
   public String getAvVpsChkCheckNumber(){
   	
   	  return this.AV_VPS_CHK_CHCK_NMBR;
   	
   }

   public void setAvVpsChkCheckNumber(String AV_VPS_CHK_CHCK_NMBR) {
   	
   	  this.AV_VPS_CHK_CHCK_NMBR = AV_VPS_CHK_CHCK_NMBR;
   	
   }

   public String getAvVpsChkBankAccount() {
   	
   	  return this.AV_VPS_CHK_BNK_ACCNT;
   	
   }
   
   public void setAvVpsChkBankAccount(String AV_VPS_CHK_BNK_ACCNT) {
   	
   	  this.AV_VPS_CHK_BNK_ACCNT = AV_VPS_CHK_BNK_ACCNT;
   	
   }
   
   
   public String getAvVpsConversionDate() {
	   	
   	  return this.AP_VPS_CNVRSN_DT;
   	
   }
   
   public void setAvVpsConversionDate(String AP_VPS_CNVRSN_DT) {
   	
   	  this.AP_VPS_CNVRSN_DT = AP_VPS_CNVRSN_DT;
   	
   }
   
   public double getAvVpsConversionRate() {
	   	
   	  return this.AP_VPS_CNVRSN_RT;
   	
   }
   
   public void setAvVpsConversionRate(double AP_VPS_CNVRSN_RT) {
   	
   	  this.AP_VPS_CNVRSN_RT = AP_VPS_CNVRSN_RT;
   	
   }

} // ApModAppliedVoucherDetails class