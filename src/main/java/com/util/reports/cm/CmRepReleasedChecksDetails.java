/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.cm;

import java.util.Comparator;
import java.util.Date;

public class CmRepReleasedChecksDetails implements java.io.Serializable {

   private Date RRC_DT;
   private String RRC_CHK_NMBR;
   private String RRC_RFRNC_NMBR;
   private String RRC_DESC;
   private String RRC_SPL_SPPLR_CODE;
   private String RRC_SPL_SPPLR_TYP;
   private String RRC_SPL_SPPLR_CLSS;
   private String RRC_BNK_ACCNT;
   private double RRC_AMNT;
   private String ORDER_BY;
   private String RRC_CHK_RLSD;
   private Date RRC_CHK_DT; 
   private Date RRC_CHK_DT_RLSD;
   private Date RRC_VOU_DT;
   private String RRC_VOU_RFRNC_NMBR;
   private Date RRC_VPS_DUE_DT;
   private String RRC_MMO;
   private Date RRC_VOU_DUE_DT;   
   
   public CmRepReleasedChecksDetails() {
   }

   public Date getRrcDate() {
   	
   	  return RRC_DT;
   	
   }
   
   public void setRrcDate(Date RRC_DT) {
   	
   	  this.RRC_DT = RRC_DT;
   	
   }
   
   public String getRrcCheckNumber() {
   	
   	  return RRC_CHK_NMBR;
   	
   }
   
   public void setRrcCheckNumber(String RRC_CHK_NMBR) {
   	
   	  this.RRC_CHK_NMBR = RRC_CHK_NMBR;
   	
   }

   public String getRrcReferenceNumber() {
   	
   	  return RRC_RFRNC_NMBR;
   	  
   }
   
   public void setRrcReferenceNumber(String RRC_RFRNC_NMBR) {
   	
   	  this.RRC_RFRNC_NMBR = RRC_RFRNC_NMBR;
   	  
   }
   
   public String getRrcDescription() {
   	
   	  return RRC_DESC;
   	  
   }
   
   public void setRrcDescription(String RRC_DESC) {
   	
   	  this.RRC_DESC = RRC_DESC;
   	  
   }
   
   public String getRrcSplSupplierCode() {
   	
   	  return RRC_SPL_SPPLR_CODE;
   	
   }
   
   public void setRrcSplSupplierCode(String RRC_SPL_SPPLR_CODE) {
   	
   	  this.RRC_SPL_SPPLR_CODE = RRC_SPL_SPPLR_CODE;
   	
   }
   
   public String getRrcSplSupplierType() {
   	
   	  return RRC_SPL_SPPLR_TYP;
   	
   }
   
   public void setRrcSplSupplierType(String RRC_SPL_SPPLR_TYP) {
   	
   	  this.RRC_SPL_SPPLR_TYP = RRC_SPL_SPPLR_TYP;
   	
   }
   
   public String getRrcSplSupplierClass() {
   	
   	  return RRC_SPL_SPPLR_CLSS;
   	
   }
   
   public void setRrcSplSupplierClass(String RRC_SPL_SPPLR_CLSS) {
   	
   	  this.RRC_SPL_SPPLR_CLSS = RRC_SPL_SPPLR_CLSS;
   	
   }
   
   public String getRrcBankAccount() {
   	
   	  return RRC_BNK_ACCNT;
   	
   }
   
   public void setRrcBankAccount(String RRC_BNK_ACCNT) {
   	
   	  this.RRC_BNK_ACCNT = RRC_BNK_ACCNT;
   	
   }
   
   public double getRrcAmount() {
   	
   	  return RRC_AMNT;
   	  
   }
   
   public void setRrcAmount(double RRC_AMNT) {
   	
   	  this.RRC_AMNT = RRC_AMNT;
   	  
   }
   
   public String getOrderBy() {
   	
   	  return ORDER_BY;
   	
   }
   
   public void setOrderBy(String ORDER_BY) {
   	
   	  this.ORDER_BY = ORDER_BY;
   	
   }
   
   public String getRrcChkReleased() {
   	
   	  return RRC_CHK_RLSD;
   	
   }
   
   public void setRrcChkReleased(String RRC_CHK_RLSD) {
   	
   	  this.RRC_CHK_RLSD = RRC_CHK_RLSD;
   	
   }
   
   public Date getRrcChkDate() {
	   
	   return RRC_CHK_DT;
   }
   
   public void setRrcChkDate(Date RRC_CHK_DT) {
	   
	   this.RRC_CHK_DT = RRC_CHK_DT;
	   
   }
   
   public Date getRrcChkDateReleased(){
	   
	   return RRC_CHK_DT_RLSD;
	   
   }
   public void setRrcChkDateReleased(Date RRC_CHK_DT_RLSD)   {
	   
	   this.RRC_CHK_DT_RLSD = RRC_CHK_DT_RLSD;
	   
   }
   
   public Date getRrcVouDate(){
	   
	   return RRC_VOU_DT;
	   
   }
   public void setRrcVouDate(Date RRC_VOU_DT){
	   
	   this.RRC_VOU_DT = RRC_VOU_DT;
	   
   }
   public String getRrcVouReferenceNumber(){
	   
	   return RRC_VOU_RFRNC_NMBR;
	   
   }
   public void setRrcVouReferenceNumber(String RRC_VOU_RFRNC_NMBR){
	   
	   this.RRC_VOU_RFRNC_NMBR = RRC_VOU_RFRNC_NMBR;
	   
   }
   public Date getRrcVpsDueDate(){
	   
	   return RRC_VPS_DUE_DT;
	   
   }
   public void setRrcVpsDueDate(Date RRC_VPS_DUE_DT){
	   
	   this.RRC_VPS_DUE_DT = RRC_VPS_DUE_DT;
	   
   }
   
   public String getRrcMemo(){
	   
	   return RRC_MMO;
	   
   }
   
   public void setRrcMemo(String RRC_MMO){
	   
	   this.RRC_MMO = RRC_MMO;
	   
   }
   
   public Date getVouDueDate(){
	   
	   return RRC_VOU_DUE_DT;
	   
   }
   
   public void setVouDueDate(Date RRC_VOU_DUE_DT){
	   
	   this.RRC_VOU_DUE_DT = RRC_VOU_DUE_DT;
	   
   }
   
   public static Comparator SupplierCodeComparator = (CR, anotherCR) -> {

       String RRC_SPL_SPPLR_CODE1 = ((CmRepReleasedChecksDetails) CR).getRrcSplSupplierCode();
       String RRC_SPL_SPPLR_TYP1 = ((CmRepReleasedChecksDetails) CR).getRrcSplSupplierType();
       Date RRC_DT1 = ((CmRepReleasedChecksDetails) CR).getRrcDate();
       String RRC_RFRNC_NMBR1 = ((CmRepReleasedChecksDetails) CR).getRrcReferenceNumber();
       String RRC_CHCK_NMBR1 = ((CmRepReleasedChecksDetails) CR).getRrcCheckNumber();
       String RRC_BNK_ACCNT1 = ((CmRepReleasedChecksDetails) CR).getRrcBankAccount();

       String RRC_SPL_SPPLR_CODE2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcSplSupplierCode();
       String RRC_SPL_SPPLR_TYP2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcSplSupplierType();
       Date RRC_DT2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcDate();
       String RRC_RFRNC_NMBR2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcReferenceNumber();
       String RRC_CHCK_NMBR2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcCheckNumber();
       String RRC_BNK_ACCNT2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcBankAccount();

       String ORDER_BY = ((CmRepReleasedChecksDetails) CR).getOrderBy();

       if (!(RRC_SPL_SPPLR_CODE1.equals(RRC_SPL_SPPLR_CODE2))) {

           return RRC_SPL_SPPLR_CODE1.compareTo(RRC_SPL_SPPLR_CODE2);

       } else {

           if(ORDER_BY.equals("DATE") && !(RRC_DT1.equals(RRC_DT2))){

               return RRC_DT1.compareTo(RRC_DT2);

           } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(RRC_SPL_SPPLR_TYP1.equals(RRC_SPL_SPPLR_TYP2))){

               return RRC_SPL_SPPLR_TYP1.compareTo(RRC_SPL_SPPLR_TYP2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(RRC_BNK_ACCNT1.equals(RRC_BNK_ACCNT2))){

               return RRC_BNK_ACCNT1.compareTo(RRC_BNK_ACCNT2);

           } else if(ORDER_BY.equals("DOCUMENT NUMBER") && !(RRC_RFRNC_NMBR1.equals(RRC_RFRNC_NMBR2))){

               return RRC_RFRNC_NMBR1.compareTo(RRC_RFRNC_NMBR2);

           } else {

               return RRC_CHCK_NMBR1.compareTo(RRC_CHCK_NMBR2);

           }


       }

   };
   
   public static Comparator SupplierTypeComparator = (CR, anotherCR) -> {

       String RRC_SPL_SPPLR_CODE1 = ((CmRepReleasedChecksDetails) CR).getRrcSplSupplierCode();
       String RRC_SPL_SPPLR_TYP1 = ((CmRepReleasedChecksDetails) CR).getRrcSplSupplierType();
       Date RRC_DT1 = ((CmRepReleasedChecksDetails) CR).getRrcDate();
       String RRC_RFRNC_NMBR1 = ((CmRepReleasedChecksDetails) CR).getRrcReferenceNumber();
       String RRC_CHCK_NMBR1 = ((CmRepReleasedChecksDetails) CR).getRrcCheckNumber();
       String RRC_BNK_ACCNT1 = ((CmRepReleasedChecksDetails) CR).getRrcBankAccount();

       String RRC_SPL_SPPLR_CODE2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcSplSupplierCode();
       String RRC_SPL_SPPLR_TYP2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcSplSupplierType();
       Date RRC_DT2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcDate();
       String RRC_RFRNC_NMBR2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcReferenceNumber();
       String RRC_CHCK_NMBR2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcCheckNumber();
       String RRC_BNK_ACCNT2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcBankAccount();

       String ORDER_BY = ((CmRepReleasedChecksDetails) CR).getOrderBy();

       if (!(RRC_SPL_SPPLR_TYP1.equals(RRC_SPL_SPPLR_TYP2))) {

           return RRC_SPL_SPPLR_TYP1.compareTo(RRC_SPL_SPPLR_TYP2);

       } else {

           if(ORDER_BY.equals("DATE") && !(RRC_DT1.equals(RRC_DT2))){

               return RRC_DT1.compareTo(RRC_DT2);

           } else if(ORDER_BY.equals("SUPPLIER CODE") && !(RRC_SPL_SPPLR_CODE1.equals(RRC_SPL_SPPLR_CODE2))){

               return RRC_SPL_SPPLR_CODE1.compareTo(RRC_SPL_SPPLR_CODE2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(RRC_BNK_ACCNT1.equals(RRC_BNK_ACCNT2))){

               return RRC_BNK_ACCNT1.compareTo(RRC_BNK_ACCNT2);

           } else if(ORDER_BY.equals("DOCUMENT NUMBER") && !(RRC_RFRNC_NMBR1.equals(RRC_RFRNC_NMBR2))){

               return RRC_RFRNC_NMBR1.compareTo(RRC_RFRNC_NMBR2);

           } else {

               return RRC_CHCK_NMBR1.compareTo(RRC_CHCK_NMBR2);

           }

       }

   };
   
   public static Comparator SupplierClassComparator = (CR, anotherCR) -> {

       String RRC_SPL_SPPLR_CLSS1 = ((CmRepReleasedChecksDetails) CR).getRrcSplSupplierClass();
       String RRC_SPL_SPPLR_CODE1 = ((CmRepReleasedChecksDetails) CR).getRrcSplSupplierCode();
       String RRC_SPL_SPPLR_TYP1 = ((CmRepReleasedChecksDetails) CR).getRrcSplSupplierType();
       Date RRC_DT1 = ((CmRepReleasedChecksDetails) CR).getRrcDate();
       String RRC_RFRNC_NMBR1 = ((CmRepReleasedChecksDetails) CR).getRrcReferenceNumber();
       String RRC_CHCK_NMBR1 = ((CmRepReleasedChecksDetails) CR).getRrcCheckNumber();
       String RRC_BNK_ACCNT1 = ((CmRepReleasedChecksDetails) CR).getRrcBankAccount();

       String RRC_SPL_SPPLR_CLSS2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcSplSupplierClass();
       String RRC_SPL_SPPLR_CODE2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcSplSupplierCode();
       String RRC_SPL_SPPLR_TYP2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcSplSupplierType();
       Date RRC_DT2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcDate();
       String RRC_RFRNC_NMBR2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcReferenceNumber();
       String RRC_CHCK_NMBR2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcCheckNumber();
       String RRC_BNK_ACCNT2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcBankAccount();

       String ORDER_BY = ((CmRepReleasedChecksDetails) CR).getOrderBy();

       if (!(RRC_SPL_SPPLR_CLSS1.equals(RRC_SPL_SPPLR_CLSS2))) {

           return RRC_SPL_SPPLR_CLSS1.compareTo(RRC_SPL_SPPLR_CLSS2);

       } else {

           if(ORDER_BY.equals("DATE") && !(RRC_DT1.equals(RRC_DT2))){

               return RRC_DT1.compareTo(RRC_DT2);

           } else if(ORDER_BY.equals("SUPPLIER CODE") && !(RRC_SPL_SPPLR_CODE1.equals(RRC_SPL_SPPLR_CODE2))){

               return RRC_SPL_SPPLR_CODE1.compareTo(RRC_SPL_SPPLR_CODE2);

           } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(RRC_SPL_SPPLR_TYP1.equals(RRC_SPL_SPPLR_TYP2))){

               return RRC_SPL_SPPLR_TYP1.compareTo(RRC_SPL_SPPLR_TYP2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(RRC_BNK_ACCNT1.equals(RRC_BNK_ACCNT2))){

               return RRC_BNK_ACCNT1.compareTo(RRC_BNK_ACCNT2);

           } else if(ORDER_BY.equals("DOCUMENT NUMBER") && !(RRC_RFRNC_NMBR1.equals(RRC_RFRNC_NMBR2))){

               return RRC_RFRNC_NMBR1.compareTo(RRC_RFRNC_NMBR2);

           } else {

               return RRC_CHCK_NMBR1.compareTo(RRC_CHCK_NMBR2);

           }

       }

   };
   
   public static Comparator NoGroupComparator = (CR, anotherCR) -> {

       String RRC_SPL_SPPLR_CODE1 = ((CmRepReleasedChecksDetails) CR).getRrcSplSupplierCode();
       String RRC_SPL_SPPLR_TYP1 = ((CmRepReleasedChecksDetails) CR).getRrcSplSupplierType();
       Date RRC_DT1 = ((CmRepReleasedChecksDetails) CR).getRrcDate();
       String RRC_RFRNC_NMBR1 = ((CmRepReleasedChecksDetails) CR).getRrcReferenceNumber();
       String RRC_CHCK_NMBR1 = ((CmRepReleasedChecksDetails) CR).getRrcCheckNumber();
       String RRC_BNK_ACCNT1 = ((CmRepReleasedChecksDetails) CR).getRrcBankAccount();

       String RRC_SPL_SPPLR_CODE2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcSplSupplierCode();
       String RRC_SPL_SPPLR_TYP2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcSplSupplierType();
       Date RRC_DT2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcDate();
       String RRC_RFRNC_NMBR2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcReferenceNumber();
       String RRC_CHCK_NMBR2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcCheckNumber();
       String RRC_BNK_ACCNT2 = ((CmRepReleasedChecksDetails) anotherCR).getRrcBankAccount();

       String ORDER_BY = ((CmRepReleasedChecksDetails) CR).getOrderBy();

       if(ORDER_BY.equals("DATE") && !(RRC_DT1.equals(RRC_DT2))){

           return RRC_DT1.compareTo(RRC_DT2);

       } else if(ORDER_BY.equals("SUPPLIER CODE") && !(RRC_SPL_SPPLR_CODE1.equals(RRC_SPL_SPPLR_CODE2))){

           return RRC_SPL_SPPLR_CODE1.compareTo(RRC_SPL_SPPLR_CODE2);

       } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(RRC_SPL_SPPLR_TYP1.equals(RRC_SPL_SPPLR_TYP2))){

           return RRC_SPL_SPPLR_TYP1.compareTo(RRC_SPL_SPPLR_TYP2);

       } else if(ORDER_BY.equals("BANK ACCOUNT") && !(RRC_BNK_ACCNT1.equals(RRC_BNK_ACCNT2))){

           return RRC_BNK_ACCNT1.compareTo(RRC_BNK_ACCNT2);

       } else if(ORDER_BY.equals("DOCUMENT NUMBER") && !(RRC_RFRNC_NMBR1.equals(RRC_RFRNC_NMBR2))){

           return RRC_RFRNC_NMBR1.compareTo(RRC_RFRNC_NMBR2);

       } else {

           return RRC_CHCK_NMBR1.compareTo(RRC_CHCK_NMBR2);

       }

   };

} // ApRepCheckRegisterDetails class