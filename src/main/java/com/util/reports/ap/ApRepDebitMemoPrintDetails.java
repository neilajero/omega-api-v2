/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.util.Date;

public class ApRepDebitMemoPrintDetails implements java.io.Serializable {

   private String DP_VOU_SPL_SPPLR_CODE;
   private Date DP_VOU_DT;
   private String DP_VOU_SPL_ADDRSS;
   private String DP_VOU_DCMNT_NMBR;
   private String DP_VOU_DR_NMBR;
   private String DP_VOU_SPL_TIN;
   private String DP_VOU_DM_VCHR_NMBR;
   private String DP_VOU_DESC;
   private double DP_VOU_BLL_AMNT;
   private String DP_VOU_CRTD_BY;
   private String DP_VOU_APPRVD_BY;
   private String DP_DR_COA_ACCNT_NMBR;
   private String DP_DR_COA_ACCNT_DESC;
   private byte DP_DR_DBT;
   private double DP_DR_AMNT;
   private String DP_VOU_CRRNCY;
   private String DP_VOU_AMNT_IN_WRDS;
   private byte DP_SHW_DPLCT;
   private String DP_VOU_APPRVL_STATUS;
   private String DP_VOU_APPRVD_RJCTD_BY_DESC;
   private String DP_VOU_CRTD_BY_DESC;
   
   public ApRepDebitMemoPrintDetails() {
   }

   public String getDpVouSplSupplierCode() {
   	
   	  return DP_VOU_SPL_SPPLR_CODE;
   	
   }
   
   public void setDpVouSplSupplierCode(String DP_VOU_SPL_SPPLR_CODE) {
   	
   	  this.DP_VOU_SPL_SPPLR_CODE = DP_VOU_SPL_SPPLR_CODE;
   	
   }
   
   public Date getDpVouDate() {
   	
   	  return DP_VOU_DT;
   	
   }
   
   public void setDpVouDate(Date DP_VOU_DT) {
   	
   	  this.DP_VOU_DT = DP_VOU_DT;
   	
   }
   
   public String getDpVouSplAddress() {
   	
   	  return DP_VOU_SPL_ADDRSS;
   	
   }
   
   public void setDpVouSplAddress(String DP_VOU_SPL_ADDRSS) {
   	
   	  this.DP_VOU_SPL_ADDRSS = DP_VOU_SPL_ADDRSS;
   	
   }
   
   public String getDpVouDocumentNumber() {
   	
   	  return DP_VOU_DCMNT_NMBR;
   	
   }
   
   public void setDpVouDocumentNumber(String DP_VOU_DCMNT_NMBR) {
   	
   	  this.DP_VOU_DCMNT_NMBR = DP_VOU_DCMNT_NMBR;
   	
   }
   
   
   
   public String getDpVouDrNumber() {
	   	
	  return DP_VOU_DR_NMBR;
	   	
   }
   
   public void setDpVouDrNumber(String DP_VOU_DR_NMBR) {
   	
   	  this.DP_VOU_DR_NMBR = DP_VOU_DR_NMBR;
   	
   }
   
   public String getDpVouSplTin() {
   	
   	  return DP_VOU_SPL_TIN;
   	
   }
   
   public void setDpVouSplTin(String DP_VOU_SPL_TIN) {
   	
   	  this.DP_VOU_SPL_TIN = DP_VOU_SPL_TIN;
   	
   }
   
   public String getDpVouDmVoucherNumber() {
   	
   	  return DP_VOU_DM_VCHR_NMBR;
   	
   }
   
   public void setDpVouDmVoucherNumber(String DP_VOU_DM_VCHR_NMBR) {
   	
   	  this.DP_VOU_DM_VCHR_NMBR = DP_VOU_DM_VCHR_NMBR;
   	
   }
   
   public String getDpVouDescription() {
   	
   	  return DP_VOU_DESC;
   	
   }
   
   public void setDpVouDescription(String DP_VOU_DESC) {
   	
   	  this.DP_VOU_DESC = DP_VOU_DESC;
   	
   }
   
   public double getDpVouBillAmount() {
   	
   	  return DP_VOU_BLL_AMNT;
   	
   }
   
   public void setDpVouBillAmount(double DP_VOU_BLL_AMNT) {
   	
   	  this.DP_VOU_BLL_AMNT = DP_VOU_BLL_AMNT;
   	
   }
   
   public String getDpVouCreatedBy() {
   	
   	  return DP_VOU_CRTD_BY;
   	
   }
   
   public void setDpVouCreatedBy(String DP_VOU_CRTD_BY) {
   	
   	  this.DP_VOU_CRTD_BY = DP_VOU_CRTD_BY;
   	
   }
   
   public String getDpVouApprovedBy() {
   	
   	  return DP_VOU_APPRVD_BY;
   	
   }
   
   public void setDpVouApprovedBy(String DP_VOU_APPRVD_BY) {
   	
   	  this.DP_VOU_APPRVD_BY = DP_VOU_APPRVD_BY;
   	
   }
   
   public String getDpDrCoaAccountNumber() {
   	
   	  return DP_DR_COA_ACCNT_NMBR;
   	
   }
   
   public void setDpDrCoaAccountNumber(String DP_DR_COA_ACCNT_NMBR) {
   	
   	  this.DP_DR_COA_ACCNT_NMBR = DP_DR_COA_ACCNT_NMBR;
   	
   }
   
   public String getDpDrCoaAccountDescription() {
   	
   	  return DP_DR_COA_ACCNT_DESC;
   	
   }
   
   public void setDpDrCoaAccountDescription(String DP_DR_COA_ACCNT_DESC) {
   	
   	  this.DP_DR_COA_ACCNT_DESC = DP_DR_COA_ACCNT_DESC;
   	
   }
   
   public byte getDpDrDebit() {
   	
   	  return DP_DR_DBT;
   	
   }
   
   public void setDpDrDebit(byte DP_DR_DBT) {
   	
   	  this.DP_DR_DBT = DP_DR_DBT;
   	
   }
   
   public double getDpDrAmount() {
   	
   	  return DP_DR_AMNT;
   	
   }
   
   public void setDpDrAmount(double DP_DR_AMNT) {
   	
   	  this.DP_DR_AMNT = DP_DR_AMNT;
   	
   }
   
   public String getDpVouCurrency() {
   	
   	  return DP_VOU_CRRNCY;
   	
   }
   
   public void setDpVouCurrency(String DP_VOU_CRRNCY) {
   	
   	  this.DP_VOU_CRRNCY = DP_VOU_CRRNCY;
   	
   }
   
   public String getDpVouAmountInWords() {
   	
   	  return DP_VOU_AMNT_IN_WRDS;
   	
   }
   
   public void setDpVouAmountInWords(String DP_VOU_AMNT_IN_WRDS) {
   	
   	  this.DP_VOU_AMNT_IN_WRDS = DP_VOU_AMNT_IN_WRDS;
   	
   }
   
   public byte getDpShowDuplicate() {
   	
   	  return DP_SHW_DPLCT;
   	
   }
   
   public void setDpShowDuplicate(byte DP_SHW_DPLCT) {

	   this.DP_SHW_DPLCT = DP_SHW_DPLCT;

   }

   public String getDpVouApprovalStatus() {

	   return DP_VOU_APPRVL_STATUS;

   }

   public void setDpVouApprovalStatus(String DP_VOU_APPRVL_STATUS) {

	   this.DP_VOU_APPRVL_STATUS = DP_VOU_APPRVL_STATUS;

   }

   public String getDpVouApprovedRejectedByDescription() {

	   return DP_VOU_APPRVD_RJCTD_BY_DESC;

   }

   public void setDpVouApprovedRejectedByDescription(String DP_VOU_APPRVD_RJCTD_BY_DESC) {

	   this.DP_VOU_APPRVD_RJCTD_BY_DESC = DP_VOU_APPRVD_RJCTD_BY_DESC;

   }

   public String getDpVouCreatedByDescription() {

	   return DP_VOU_CRTD_BY_DESC;

   }

   public void setDpVouCreatedByDescription(String DP_VOU_CRTD_BY_DESC) {

	   this.DP_VOU_CRTD_BY_DESC = DP_VOU_CRTD_BY_DESC;

   }

 }  // ApRepDebitMemoPrintDetails