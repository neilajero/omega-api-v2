/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.ap.ApVoucherPaymentScheduleDetails;

import java.util.Date;

public class ApModVoucherPaymentScheduleDetails extends ApVoucherPaymentScheduleDetails implements java.io.Serializable {

   private Integer VPS_VOU_CODE;
   private String VPS_VOU_DCMNT_NMBR;
   private String VPS_VOU_RFRNC_NMBR;
   private String VPS_VOU_DESC;
   private String VPS_VOU_MISC1;
   private String VPS_VOU_MISC2;
   private String VPS_VOU_MISC3;
   private String VPS_VOU_MISC4;
   private String VPS_VOU_MISC5;
   private String VPS_VOU_MISC6;
   private String VPS_VOU_FC_NM;
   private Date VPS_VOU_CNVRSN_DT;
   private double VPS_VOU_CNVRSN_RT;
   private double VPS_AV_APPLY_AMNT;
   private double VPS_AV_TX_WTHHLD;
   private double VPS_AV_DSCNT_AMNT;

   public ApModVoucherPaymentScheduleDetails() {
   }

   public Integer getVpsVouCode() {
   	
   	   return VPS_VOU_CODE;
   	
   }
   
   public void setVpsVouCode(Integer VPS_VOU_CODE) {
   	
   	   this.VPS_VOU_CODE = VPS_VOU_CODE;
   	
   }
   
   public String getVpsVouDocumentNumber() {
   	
   	   return VPS_VOU_DCMNT_NMBR;
   	
   }
   
   public void setVpsVouDocumentNumber(String VPS_VOU_DCMNT_NMBR) {
   	
   	   this.VPS_VOU_DCMNT_NMBR = VPS_VOU_DCMNT_NMBR;
   	
   }
   
   public String getVpsVouReferenceNumber() {
   	
   	   return VPS_VOU_RFRNC_NMBR;
   	
   }
   
   public void setVpsVouReferenceNumber(String VPS_VOU_RFRNC_NMBR) {
   	
   	   this.VPS_VOU_RFRNC_NMBR = VPS_VOU_RFRNC_NMBR;
   	
   }
   
   public String getVpsVouDescription() {
   	
   	   return VPS_VOU_DESC;
   	
   }
   
   public void setVpsVouDescription(String VPS_VOU_DESC) {
   	
   	   this.VPS_VOU_DESC = VPS_VOU_DESC;
   	
   }
   
   public String getVpsVouMisc1() {
	   	
   	   return VPS_VOU_MISC1;
   	
   }
   
   public void setVpsVouMisc1(String VPS_VOU_MISC1) {
   	
   	   this.VPS_VOU_MISC1 = VPS_VOU_MISC1;
   	
   }
   
   public String getVpsVouMisc2() {
	   	
   	   return VPS_VOU_MISC2;
   	
   }
   
   public void setVpsVouMisc2(String VPS_VOU_MISC2) {
   	
   	   this.VPS_VOU_MISC2 = VPS_VOU_MISC2;
   	
   }

   public String getVpsVouMisc3() {
	   	
   	   return VPS_VOU_MISC3;
   	
   }
   
   public void setVpsVouMisc3(String VPS_VOU_MISC3) {
   	
   	   this.VPS_VOU_MISC3 = VPS_VOU_MISC3;
   	
   }
   
   public String getVpsVouMisc4() {
	   	
   	   return VPS_VOU_MISC4;
   	
   }
   
   public void setVpsVouMisc4(String VPS_VOU_MISC4) {
   	
   	   this.VPS_VOU_MISC4 = VPS_VOU_MISC4;
   	
   }
   
   public String getVpsVouMisc5() {
	   	
   	   return VPS_VOU_MISC5;
   	
   }
   
   public void setVpsVouMisc5(String VPS_VOU_MISC5) {
   	
   	   this.VPS_VOU_MISC5 = VPS_VOU_MISC5;
   	
   }
   
   public String getVpsVouMisc6() {
	   	
   	   return VPS_VOU_MISC6;
   	
   }
   
   public void setVpsVouMisc6(String VPS_VOU_MISC6) {
   	
   	   this.VPS_VOU_MISC6 = VPS_VOU_MISC6;
   	
   }
   
   public String getVpsVouFcName() {
   	
   	   return VPS_VOU_FC_NM;
   	
   }
   
   public void setVpsVouFcName(String VPS_VOU_FC_NM) {
   	
   	  this.VPS_VOU_FC_NM = VPS_VOU_FC_NM;
   	
   }
   
   
   public Date getVpsVouConversionDate() {
	   	
   	   return VPS_VOU_CNVRSN_DT;
   	
   }
   
   public void setVpsVouConversionDate(Date VPS_VOU_CNVRSN_DT) {
   	
   	  this.VPS_VOU_CNVRSN_DT = VPS_VOU_CNVRSN_DT;
   	
   }
   
   
   public double getVpsVouConversionRate() {
	   	
   	  return VPS_VOU_CNVRSN_RT;
   	
   }
   
   public void setVpsVouConversionRate(double VPS_VOU_CNVRSN_RT) {
   	
   	  this.VPS_VOU_CNVRSN_RT = VPS_VOU_CNVRSN_RT;
   	
   }
   
   
   
   public double getVpsAvApplyAmount() {
   	
   	  return VPS_AV_APPLY_AMNT;
   	
   }
   
   public void setVpsAvApplyAmount(double VPS_AV_APPLY_AMNT) {
   	
   	  this.VPS_AV_APPLY_AMNT = VPS_AV_APPLY_AMNT;
   	
   }
   
   public double getVpsAvTaxWithheld() {
   	
   	  return VPS_AV_TX_WTHHLD;
   	
   }
   
   public void setVpsAvTaxWithheld(double VPS_AV_TX_WTHHLD) {
   	
   	  this.VPS_AV_TX_WTHHLD = VPS_AV_TX_WTHHLD;
   	
   }
   
   public double getVpsAvDiscountAmount() {
   	
   	  return VPS_AV_DSCNT_AMNT;
   	
   }
   
   public void setVpsAvDiscountAmount(double VPS_AV_DSCNT_AMNT) {
   	
   	  this.VPS_AV_DSCNT_AMNT = VPS_AV_DSCNT_AMNT;
   	
   }
   

} // ApModVoucherPaymentScheduleDetails class