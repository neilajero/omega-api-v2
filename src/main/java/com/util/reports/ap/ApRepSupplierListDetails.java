/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;


public class ApRepSupplierListDetails implements java.io.Serializable {

   private String SL_SPL_SPPLR_CODE;
   private String SL_SPL_NM;
   private String SL_SPL_CNTCT;
   private String SL_SPL_PHN;
   private String SL_SPL_TIN;
   private String SL_SPL_ADDRSS;
   private double SL_SPL_BLNC;
   private double SL_SPL_PTD_BLNC;
   private double SL_SPL_PTD_INTRST;

   private double SL_SPL_BEG_BLNC;
   private double SL_SPL_NET_TRNSCTNS;
   private double SL_SPL_INTRST_INCM;
   private double SL_SPL_AMNT_DC;

   private String SL_SPL_PYMNT_TRM;

   public ApRepSupplierListDetails() {
   }


   public String getSlSplSupplierCode() {

   	  return SL_SPL_SPPLR_CODE;

   }

   public void setSlSplSupplierCode(String SL_SPL_SPPLR_CODE) {

   	  this.SL_SPL_SPPLR_CODE = SL_SPL_SPPLR_CODE;

   }

   public String getSlSplName() {

   	  return SL_SPL_NM;

   }

   public void setSlSplName(String SL_SPL_NM) {

   	  this.SL_SPL_NM = SL_SPL_NM;

   }

   public String getSlSplContact() {

   	  return SL_SPL_CNTCT;

   }

   public void setSlSplContact(String SL_SPL_CNTCT) {

   	  this.SL_SPL_CNTCT = SL_SPL_CNTCT;

   }

   public String getSlSplPhone() {

   	  return SL_SPL_PHN;

   }

   public void setSlSplPhone(String SL_SPL_PHN) {

   	  this.SL_SPL_PHN = SL_SPL_PHN;

   }

   public String getSlSplTin() {

   	  return SL_SPL_TIN;

   }

   public void setSlSplTin(String SL_SPL_TIN) {

   	  this.SL_SPL_TIN = SL_SPL_TIN;

   }


   public String getSlSplAddress() {

   	  return SL_SPL_ADDRSS;

   }

   public void setSlSplAddress(String SL_SPL_ADDRSS) {

   	  this.SL_SPL_ADDRSS = SL_SPL_ADDRSS;

   }

   public double getSlSplBalance() {

   	  return SL_SPL_BLNC;

   }

   public void setSlSplBalance(double SL_SPL_BLNC) {

   	  this.SL_SPL_BLNC = SL_SPL_BLNC;

   }

   public double getSlSplPtdBalance() {

	   return SL_SPL_PTD_BLNC;

   }

   public void setSlSplPtdBalance(double SL_SPL_PTD_BLNC) {

	   this.SL_SPL_PTD_BLNC = SL_SPL_PTD_BLNC;

   }

   public double getSlSplPtdInterest() {

	   return SL_SPL_PTD_INTRST;

   }

   public void setSlSplPtdInterest(double SL_SPL_PTD_INTRST) {

	   this.SL_SPL_PTD_INTRST = SL_SPL_PTD_INTRST;

   }

   public double getSlSplBegBalance () {

	   return SL_SPL_BEG_BLNC;

   }

   public void setSlSplBegBalance (double SL_SPL_BEG_BLNC) {

	   this.SL_SPL_BEG_BLNC = SL_SPL_BEG_BLNC;

   }

   public double getSlSplNetTransactions() {

	   return SL_SPL_NET_TRNSCTNS;

   }

   public void setSlSplNetTransactions(double SL_SPL_NET_TRNSCTNS) {

	   this.SL_SPL_NET_TRNSCTNS = SL_SPL_NET_TRNSCTNS;

   }

   public double getSlSplInterestIncome() {

	   return(SL_SPL_INTRST_INCM);

   }

   public void setSlSplInterestIncome(double SL_SPL_INTRST_INCM) {

	   this.SL_SPL_INTRST_INCM = SL_SPL_INTRST_INCM;

   }

   public double getSlSplAmntDC() {

	   return(SL_SPL_AMNT_DC);

   }

   public void setSlSplAmntDC(double SL_SPL_AMNT_DC) {

	   this.SL_SPL_AMNT_DC = SL_SPL_AMNT_DC;

   }

   public String getSlSplPaymentTerm() {

	   return SL_SPL_PYMNT_TRM;

   }

   public void setSlSplPaymentTerm(String SL_SPL_PYMNT_TRM) {

	   this.SL_SPL_PYMNT_TRM = SL_SPL_PYMNT_TRM;

   }

} // ApRepSupplierListDetails class