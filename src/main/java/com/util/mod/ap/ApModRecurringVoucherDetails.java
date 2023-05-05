/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.ap.ApRecurringVoucherDetails;

import java.util.ArrayList;
import java.util.Date;

public class ApModRecurringVoucherDetails extends ApRecurringVoucherDetails implements java.io.Serializable {

   private String RV_FC_NM;
   private String RV_TC_NM;
   private String RV_WTC_NM;
   private String RV_SPL_SPPLR_CODE;
   private String RV_PYT_NM;
   private ArrayList rvDrList;
   private String RV_AD_NTFD_USR1;
   private String RV_AD_NTFD_USR2;
   private String RV_AD_NTFD_USR3;
   private String RV_AD_NTFD_USR4;
   private String RV_AD_NTFD_USR5;
   private double RV_TTL_DBT;
   private double RV_TTL_CRDT;
   
   private long RV_VOU_DCMNT_NMBR;
   private Date RV_VOU_DT;  
   private Date RV_NW_NXT_RN_DT;  
   private String RV_VB_NM;
   
   private double RV_INTRST_RT;
   
   public ApModRecurringVoucherDetails() {
   }

   public String getRvFcName() {
   	
   	  return RV_FC_NM;
   	
   }
   
   public void setRvFcName(String RV_FC_NM) {
   	
   	  this.RV_FC_NM = RV_FC_NM;
   	
   }
   
   public String getRvTcName() {
   	
   	  return RV_TC_NM;
   	
   }
   
   public void setRvTcName(String RV_TC_NM) {
   	
   	  this.RV_TC_NM = RV_TC_NM;
   	
   }
   
   public String getRvWtcName() {
   	
   	  return RV_WTC_NM;
   	
   }
   
   public void setRvWtcName(String RV_WTC_NM) {
   	
   	  this.RV_WTC_NM = RV_WTC_NM;
   	
   }
   
   public String getRvSplSupplierCode() {
   	
   	  return RV_SPL_SPPLR_CODE;
   	
   }
   
   public void setRvSplSupplierCode(String RV_SPL_SPPLR_CODE) {
   	
   	  this.RV_SPL_SPPLR_CODE = RV_SPL_SPPLR_CODE;
   	
   }
   
   public String getRvPytName() {
   	
   	  return RV_PYT_NM;
   	
   }
   
   public void setRvPytName(String RV_PYT_NM) {
   	
   	  this.RV_PYT_NM = RV_PYT_NM;
   	
   }
   
   public ArrayList getRvDrList() {
   	
   	  return rvDrList;
   	
   }
   
   public void setRvDrList(ArrayList rvDrList) {
   	
   	  this.rvDrList = rvDrList;
   	
   }
   
   public String getRvAdNotifiedUser1() {
   	
   	  return RV_AD_NTFD_USR1;
   
   }
   
   public void setRvAdNotifiedUser1(String RV_AD_NTFD_USR1) {
   	
   	  this.RV_AD_NTFD_USR1 = RV_AD_NTFD_USR1;
   	  
   }

   public String getRvAdNotifiedUser2() {
   	
   	  return RV_AD_NTFD_USR2;
   
   }
   
   public void setRvAdNotifiedUser2(String RV_AD_NTFD_USR2) {
   	
   	  this.RV_AD_NTFD_USR2 = RV_AD_NTFD_USR2;
   	  
   }

   public String getRvAdNotifiedUser3() {
   	
   	  return RV_AD_NTFD_USR3;
   
   }
   
   public void setRvAdNotifiedUser3(String RV_AD_NTFD_USR3) {
   	
   	  this.RV_AD_NTFD_USR3 = RV_AD_NTFD_USR3;
   	  
   }

   public String getRvAdNotifiedUser4() {
   	
   	  return RV_AD_NTFD_USR4;
   
   }
   
   public void setRvAdNotifiedUser4(String RV_AD_NTFD_USR4) {
   	
   	  this.RV_AD_NTFD_USR4 = RV_AD_NTFD_USR4;
   	  
   }
   
   public String getRvAdNotifiedUser5() {
   	
   	  return RV_AD_NTFD_USR5;
   
   }
   
   public void setRvAdNotifiedUser5(String RV_AD_NTFD_USR5) {
   	
   	  this.RV_AD_NTFD_USR5 = RV_AD_NTFD_USR5;
   	  
   }
   
   public double getRvTotalDebit() {
   	
   	  return RV_TTL_DBT;
   	
   }
   
   public void setRvTotalDebit(double RV_TTL_DBT) {
   	
   	  this.RV_TTL_DBT = RV_TTL_DBT;
   	
   }
   
   public double getRvTotalCredit() {
   	
   	  return RV_TTL_CRDT;
   	
   }
   
   public void setRvTotalCredit(double RV_TTL_CRDT) {
   	
   	  this.RV_TTL_CRDT = RV_TTL_CRDT;
   	
   }
   
   public long getRvVouDocumentNumber() {
   	
   	  return RV_VOU_DCMNT_NMBR;  
   	  
   }
   
   public void setRvVouDocumentNumber(long  RV_VOU_DCMNT_NMBR) {
   	
   	  this.RV_VOU_DCMNT_NMBR = RV_VOU_DCMNT_NMBR;
   	  
   }
   
   public Date getRvVouDate() {
   	
   	  return RV_VOU_DT;
   	  
   }
   
   public void setRvVouDate(Date RV_VOU_DT) {
   	
   	  this.RV_VOU_DT = RV_VOU_DT;
   	  
   }

   public Date getRvNewNextRunDate() {
   	
   	    return RV_NW_NXT_RN_DT;
   	 
   }
   
   public void setRvNewNextRunDate(Date RV_NW_NXT_RN_DT) {
   	
   	    this.RV_NW_NXT_RN_DT = RV_NW_NXT_RN_DT;
   	
   }
   
   public String getRvVbName() {
   	
   	  return RV_VB_NM;
   	
   }
   
   public void setRvVbName(String RV_VB_NM) {
   	
   	  this.RV_VB_NM = RV_VB_NM;
   	
   }
   
   public double getRvInterestRate() {
	   
	   return RV_INTRST_RT;
	   
   }
   
   public void setRvInterestRate(double RV_INTRST_RT) {
	   
	   this.RV_INTRST_RT = RV_INTRST_RT;
	   
   }
   

} // ApModVoucherDetails class