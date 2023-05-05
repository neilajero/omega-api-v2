/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.cm;

import java.util.Comparator;
import java.util.Date;


public class CmModBankReconciliationDetails implements java.io.Serializable {

   private Integer BR_CODE;
   private String BR_TYP;
   private Date BR_DT;
   private String BR_DCMNT_NMBR;
   private String BR_NMBR;
   private double BR_AMNT;
   private String BR_NM;
   private String BR_RFRNC_NMBR;
   private Date BR_DT_CLRD;

   public CmModBankReconciliationDetails () {
   }

   public CmModBankReconciliationDetails (Integer BR_CODE,
		String BR_TYP,
		Date BR_DT,
		String BR_DCMNT_NMBR,
		String BR_NMBR,
		double BR_AMNT,
		String BR_NM,
		String BR_RFRNC_NMBR,
		Date BR_DT_CLRD) {               
        
      this.BR_CODE = BR_CODE;
      this.BR_TYP = BR_TYP;
      this.BR_DT = BR_DT;
      this.BR_DCMNT_NMBR = BR_DCMNT_NMBR;
      this.BR_NMBR = BR_NMBR;
      this.BR_AMNT = BR_AMNT;
      this.BR_NM = BR_NM;
      this.BR_RFRNC_NMBR = BR_RFRNC_NMBR;
      this.BR_DT_CLRD = BR_DT_CLRD;

   }
   
   public Integer getBrCode() {
   	
   	  return BR_CODE;
   	
   }
   
   public void setBrCode(Integer BR_CODE) {
   	
   	  this.BR_CODE = BR_CODE;
   	
   }
   
   public String getBrType() {
   	
   	  return BR_TYP;
   	
   }
   
   public void setBrType(String BR_TYP) {
   	
   	  this.BR_TYP = BR_TYP;
   	
   }
   
   public Date getBrDate() {
   	
   	  return BR_DT;
   	
   }
   
   public void setBrDate(Date BR_DT) {
   	
   	  this.BR_DT = BR_DT;
   	
   }
   
   
   public String getBrDocumentNumber() {
	   	
   	  return BR_DCMNT_NMBR;
   	
   }
   
   public void setBrDocumentNumber(String BR_DCMNT_NMBR) {
   	
   	  this.BR_DCMNT_NMBR = BR_DCMNT_NMBR;
   	
   }
   
   public String getBrNumber() {
   	
   	  return BR_NMBR;
   	
   }
   
   public void setBrNumber(String BR_NMBR) {
   	
   	  this.BR_NMBR = BR_NMBR;
   	
   }
  
   public double getBrAmount() {
   	
   	  return BR_AMNT;
   	
   }
   
   public void setBrAmount(double BR_AMNT) {
   	
   	  this.BR_AMNT = BR_AMNT;
   	
   }
   
   public String getBrName() {
   	
   	  return BR_NM;
   	
   }
   
   public void setBrName(String BR_NM) {
   	
   	  this.BR_NM = BR_NM;
   	
   }
   
   public String getBrReferenceNumber() {
   	
   	  return BR_RFRNC_NMBR;
   	
   }
   
   public void setBrReferenceNumber(String BR_RFRNC_NMBR) {
   	
   	  this.BR_RFRNC_NMBR = BR_RFRNC_NMBR;
   	
   }
   
   public Date getBrDateCleared() {
	   
	   return BR_DT_CLRD;
	   
   }
   
   public void setBrDateCleared(Date BR_DT_CLRD) {
	   
	   this.BR_DT_CLRD = BR_DT_CLRD;
   
   }
   
   
   public static Comparator NoGroupComparator = (SR, anotherSR) -> {


       Date BR_DT1 = ((CmModBankReconciliationDetails) SR).getBrDate();
       String BR_TYPE_1 = ((CmModBankReconciliationDetails) SR).getBrType();
       String BR_NMBR_1 = ((CmModBankReconciliationDetails) SR).getBrNumber() == null ? "" : ((CmModBankReconciliationDetails) SR).getBrNumber() ;

       Date BR_DT2 = ((CmModBankReconciliationDetails) anotherSR).getBrDate();
       String BR_TYPE_2 = ((CmModBankReconciliationDetails) anotherSR).getBrType();
       String BR_NMBR_2= ((CmModBankReconciliationDetails) anotherSR).getBrNumber() == null ? "" : ((CmModBankReconciliationDetails) anotherSR).getBrNumber() ;



       if (!(BR_DT1.equals(BR_DT2))){

           return BR_DT1.compareTo(BR_DT2);

       } else	if (!(BR_TYPE_1.equals(BR_TYPE_2))) {

           return BR_TYPE_1.compareTo(BR_TYPE_2);

       } else {

           return BR_NMBR_1.compareTo(BR_NMBR_2);

       }

   };
   
}
 

// CmBankReconciliationDetails class   