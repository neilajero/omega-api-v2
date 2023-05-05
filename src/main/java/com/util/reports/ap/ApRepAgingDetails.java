/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.util.Comparator;
import java.util.Date;

public class ApRepAgingDetails implements java.io.Serializable {

   private String AG_SPPLR_NM;
   private String AG_VCHR_NMBR;
   private String AG_RFRNC_NMBR;
   private String AG_DSCRPTN;
   private short AG_INSTLLMNT_NMBR;
   private String AG_SPPLR_TYPE;
   private String AG_SPPLR_CLASS;
   private double AG_AMNT;
   private double AG_BCKT0;
   private double AG_BCKT1;
   private double AG_BCKT2;
   private double AG_BCKT3;
   private double AG_BCKT4;
   private double AG_BCKT5;
   private String AG_ORDER_BY;
   private String AG_GROUP_BY;	
   
   // additional fields
   private Date AG_TRNSCTN_DT;
   private double AG_VCHR_AGE;

   private char AG_VOU_FC_SYMBL;
   public ApRepAgingDetails() {
   }
   
   public String getAgSupplierName() {
   	
   	  return AG_SPPLR_NM;
   	
   }
   
   public void setAgSupplierName(String AG_SPPLR_NM) {
   	
   	  this.AG_SPPLR_NM = AG_SPPLR_NM;
   	
   }
   
   public String getAgVoucherNumber() {
   	
   	  return AG_VCHR_NMBR;
   	
   }
   
   public void setAgVoucherNumber(String AG_VCHR_NMBR) {
   	
   	  this.AG_VCHR_NMBR = AG_VCHR_NMBR;
   	
   }
   
   public String getAgReferenceNumber() {
   	
   	  return AG_RFRNC_NMBR;
   	
   }
   
   public void setAgReferenceNumber(String AG_RFRNC_NMBR) {
   	
   	  this.AG_RFRNC_NMBR = AG_RFRNC_NMBR;
   	
   }
   
   public short getAgInstallmentNumber() {
   	
   	  return AG_INSTLLMNT_NMBR;
   	   	
   }
   
   public void setAgInstallmentNumber(short AG_INSTLLMNT_NMBR) {
   	
   	  this.AG_INSTLLMNT_NMBR = AG_INSTLLMNT_NMBR;
   	   	
   }
   
   public double getAgAmount() {
   	
   	  return AG_AMNT;
   	
   }
   
   public void setAgAmount(double AG_AMNT) {
   	
   	  this.AG_AMNT = AG_AMNT;
   	
   }
   
   public double getAgBucket0() {
   	
   	  return AG_BCKT0;
   	
   }
   
   public void setAgBucket0(double AG_BCKT0) {
   	
   	  this.AG_BCKT0 = AG_BCKT0;
   	
   }
   
   public double getAgBucket1() {
   	
   	  return AG_BCKT1;
   	
   }
   
   public void setAgBucket1(double AG_BCKT1) {
   	
   	  this.AG_BCKT1 = AG_BCKT1;
   	
   }
   
   public double getAgBucket2() {
   	
   	  return AG_BCKT2;
   	
   }
   
   public void setAgBucket2(double AG_BCKT2) {
   	
   	  this.AG_BCKT2 = AG_BCKT2;
   	
   }
   
   public double getAgBucket3() {
   	
   	  return AG_BCKT3;
   	
   }
   
   public void setAgBucket3(double AG_BCKT3) {
   	
   	  this.AG_BCKT3 = AG_BCKT3;
   	
   }
   
   
   public double getAgBucket4() {
   	
   	  return AG_BCKT4;
   	
   }
   
   public void setAgBucket4(double AG_BCKT4) {
   	
   	  this.AG_BCKT4 = AG_BCKT4;
   	
   }
   
   
   public double getAgBucket5() {
   	
   	  return AG_BCKT5;
   	
   }
   
   public void setAgBucket5(double AG_BCKT5) {
   	
   	  this.AG_BCKT5 = AG_BCKT5;
   	
   }
   
   public Date getAgTransactionDate() {
   	
   	  return AG_TRNSCTN_DT;
   	
   }
   
   public void setAgTransactionDate(Date AG_TRNSCTN_DT) {
   	
   	  this.AG_TRNSCTN_DT = AG_TRNSCTN_DT;
   	
   }
   
   public String getAgSupplierType() {
   	
   		return AG_SPPLR_TYPE;
   		
   }
   
   public void setAgSupplierType(String AG_SPPLR_TYPE) {
   	
   		this.AG_SPPLR_TYPE = AG_SPPLR_TYPE;
   		
   }
   
   public String getAgSupplierClass() {
   	
   		return AG_SPPLR_CLASS;
   		
   }
   
   public void setAgSupplierClass(String AG_SPPLR_CLASS) {
   	
   		this.AG_SPPLR_CLASS = AG_SPPLR_CLASS;
   		   		   		
   }
   
   public String getOrderBy() {
   	
   		return AG_ORDER_BY;
   		
   }
         
   public void setOrderBy(String AG_ORDER_BY) {
   	
   		this.AG_ORDER_BY = AG_ORDER_BY;
		
   }
   
   public String getGroupBy() {
   	
   		return AG_GROUP_BY;
   		
   }
   
   public void setGroupBy(String AG_GROUP_BY) {
   	
   		this.AG_GROUP_BY = AG_GROUP_BY;
   		
   }
   
   public double getAgVoucherAge() {
	   
	   return AG_VCHR_AGE;
	   
   }
   
   public void setAgVoucherAge(double AG_VCHR_AGE) {
	   
	   this.AG_VCHR_AGE = AG_VCHR_AGE;
	   
   }
   
   public char getAgVouFcSymbol(){

	   return AG_VOU_FC_SYMBL;	

   }
   
   public void setAgVouFcSymbol(char AG_VOU_FC_SYMBL){

	   this.AG_VOU_FC_SYMBL = AG_VOU_FC_SYMBL; 

   }
   
   public String getAgDescription() {
	   	
  		return AG_DSCRPTN;
  		
  }
  
  public void setAgDescription(String AG_DSCRPTN) {
  	
  		this.AG_DSCRPTN = AG_DSCRPTN;
  		   		   		
  }
   
   public static Comparator SupplierNameComparator = (AG, anotherAG) -> {

       String AG_SPL_SPPLR_NM1 = ((ApRepAgingDetails)AG).getAgSupplierName();
       String AG_SPL_SPPLR_TYP1 = ((ApRepAgingDetails)AG).getAgSupplierType();
       String AG_SPL_SPPLR_CLSS1 = ((ApRepAgingDetails)AG).getAgSupplierClass();
       Date AG_DT1 = ((ApRepAgingDetails)AG).getAgTransactionDate();
       String AG_VCHR_NMBR1 =  ((ApRepAgingDetails)AG).getAgVoucherNumber();

       String AG_SPL_SPPLR_NM2 = ((ApRepAgingDetails)anotherAG).getAgSupplierName();
       String AG_SPL_SPPLR_TYP2 = ((ApRepAgingDetails)anotherAG).getAgSupplierType();
       String AG_SPL_SPPLR_CLSS2 = ((ApRepAgingDetails)anotherAG).getAgSupplierClass();
       Date AG_DT2 = ((ApRepAgingDetails)anotherAG).getAgTransactionDate();
       String AG_VCHR_NMBR2 =  ((ApRepAgingDetails)anotherAG).getAgVoucherNumber();

       String ORDER_BY = ((ApRepAgingDetails)AG).getOrderBy();

       if(!(AG_SPL_SPPLR_NM1.equals(AG_SPL_SPPLR_NM2))) {

           return AG_SPL_SPPLR_NM1.compareTo(AG_SPL_SPPLR_NM2);

       } else {

           if(ORDER_BY.equals("DATE") && !(AG_DT1.equals(AG_DT2))) {

               return AG_DT1.compareTo(AG_DT2);

           } else if(ORDER_BY.equals("SUPPLIER NAME") && !(AG_SPL_SPPLR_NM1.equals(AG_SPL_SPPLR_NM2))){

                  return AG_SPL_SPPLR_NM1.compareTo(AG_SPL_SPPLR_NM2);

              } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(AG_SPL_SPPLR_TYP1.equals(AG_SPL_SPPLR_TYP2))){

                  return AG_SPL_SPPLR_TYP1.compareTo(AG_SPL_SPPLR_TYP2);

              } else {

                  return AG_VCHR_NMBR1.compareTo(AG_VCHR_NMBR2);

              }

       }
   };
   
   public static Comparator SupplierTypeComparator = (AG, anotherAG) -> {

       String AG_SPL_SPPLR_NM1 = ((ApRepAgingDetails)AG).getAgSupplierName();
       String AG_SPL_SPPLR_TYP1 = ((ApRepAgingDetails)AG).getAgSupplierType();
       String AG_SPL_SPPLR_CLSS1 = ((ApRepAgingDetails)AG).getAgSupplierClass();
       Date AG_DT1 = ((ApRepAgingDetails)AG).getAgTransactionDate();
       String AG_VCHR_NMBR1 =  ((ApRepAgingDetails)AG).getAgVoucherNumber();

       String AG_SPL_SPPLR_NM2 = ((ApRepAgingDetails)anotherAG).getAgSupplierName();
       String AG_SPL_SPPLR_TYP2 = ((ApRepAgingDetails)anotherAG).getAgSupplierType();
       String AG_SPL_SPPLR_CLSS2 = ((ApRepAgingDetails)anotherAG).getAgSupplierClass();
       Date AG_DT2 = ((ApRepAgingDetails)anotherAG).getAgTransactionDate();
       String AG_VCHR_NMBR2 =  ((ApRepAgingDetails)anotherAG).getAgVoucherNumber();

       String ORDER_BY = ((ApRepAgingDetails)AG).getOrderBy();

       if(!(AG_SPL_SPPLR_TYP1.equals(AG_SPL_SPPLR_TYP2))) {

           return AG_SPL_SPPLR_TYP1.compareTo(AG_SPL_SPPLR_TYP2);

       } else {

           if(ORDER_BY.equals("DATE") && !(AG_DT1.equals(AG_DT2))) {

               return AG_DT1.compareTo(AG_DT2);

           } else if(ORDER_BY.equals("SUPPLIER NAME") && !(AG_SPL_SPPLR_NM1.equals(AG_SPL_SPPLR_NM2))){

                  return AG_SPL_SPPLR_NM1.compareTo(AG_SPL_SPPLR_NM2);

              } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(AG_SPL_SPPLR_TYP1.equals(AG_SPL_SPPLR_TYP2))){

                  return AG_SPL_SPPLR_TYP1.compareTo(AG_SPL_SPPLR_TYP2);

              } else {

                  return AG_VCHR_NMBR1.compareTo(AG_VCHR_NMBR2);

              }

       }
   };
   
   public static Comparator SupplierClassComparator = (AG, anotherAG) -> {

       String AG_SPL_SPPLR_NM1 = ((ApRepAgingDetails)AG).getAgSupplierName();
       String AG_SPL_SPPLR_TYP1 = ((ApRepAgingDetails)AG).getAgSupplierType();
       String AG_SPL_SPPLR_CLSS1 = ((ApRepAgingDetails)AG).getAgSupplierClass();
       Date AG_DT1 = ((ApRepAgingDetails)AG).getAgTransactionDate();
       String AG_VCHR_NMBR1 =  ((ApRepAgingDetails)AG).getAgVoucherNumber();

       String AG_SPL_SPPLR_NM2 = ((ApRepAgingDetails)anotherAG).getAgSupplierName();
       String AG_SPL_SPPLR_TYP2 = ((ApRepAgingDetails)anotherAG).getAgSupplierType();
       String AG_SPL_SPPLR_CLSS2 = ((ApRepAgingDetails)anotherAG).getAgSupplierClass();
       Date AG_DT2 = ((ApRepAgingDetails)anotherAG).getAgTransactionDate();
       String AG_VCHR_NMBR2 =  ((ApRepAgingDetails)anotherAG).getAgVoucherNumber();

       String ORDER_BY = ((ApRepAgingDetails)AG).getOrderBy();

       if(!(AG_SPL_SPPLR_CLSS1.equals(AG_SPL_SPPLR_CLSS2))) {

           return AG_SPL_SPPLR_CLSS1.compareTo(AG_SPL_SPPLR_CLSS2);

       } else {

           if(ORDER_BY.equals("DATE") && !(AG_DT1.equals(AG_DT2))) {

               return AG_DT1.compareTo(AG_DT2);

           } else if(ORDER_BY.equals("SUPPLIER NAME") && !(AG_SPL_SPPLR_NM1.equals(AG_SPL_SPPLR_NM2))){

                  return AG_SPL_SPPLR_NM1.compareTo(AG_SPL_SPPLR_NM2);

              } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(AG_SPL_SPPLR_TYP1.equals(AG_SPL_SPPLR_TYP2))){

                  return AG_SPL_SPPLR_TYP1.compareTo(AG_SPL_SPPLR_TYP2);

              } else {

                  return AG_VCHR_NMBR1.compareTo(AG_VCHR_NMBR2);

              }

       }
   };
   
   public static Comparator NoClassComparator = (AG, anotherAG) -> {

       String AG_SPL_SPPLR_NM1 = ((ApRepAgingDetails)AG).getAgSupplierName();
       String AG_SPL_SPPLR_TYP1 = ((ApRepAgingDetails)AG).getAgSupplierType();
       Date AG_DT1 = ((ApRepAgingDetails)AG).getAgTransactionDate();
       String AG_VCHR_NMBR1 =  ((ApRepAgingDetails)AG).getAgVoucherNumber();

       String AG_SPL_SPPLR_NM2 = ((ApRepAgingDetails)anotherAG).getAgSupplierName();
       String AG_SPL_SPPLR_TYP2 = ((ApRepAgingDetails)anotherAG).getAgSupplierType();
       Date AG_DT2 = ((ApRepAgingDetails)anotherAG).getAgTransactionDate();
       String AG_VCHR_NMBR2 =  ((ApRepAgingDetails)anotherAG).getAgVoucherNumber();

       String ORDER_BY = ((ApRepAgingDetails)AG).getOrderBy();

       if(ORDER_BY.equals("DATE") && !(AG_DT1.equals(AG_DT2))) {

        return AG_DT1.compareTo(AG_DT2);

    } else if(ORDER_BY.equals("SUPPLIER NAME") && !(AG_SPL_SPPLR_NM1.equals(AG_SPL_SPPLR_NM2))){

           return AG_SPL_SPPLR_NM1.compareTo(AG_SPL_SPPLR_NM2);

       } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(AG_SPL_SPPLR_TYP1.equals(AG_SPL_SPPLR_TYP2))){

           return AG_SPL_SPPLR_TYP1.compareTo(AG_SPL_SPPLR_TYP2);

       } else {

           return AG_VCHR_NMBR1.compareTo(AG_VCHR_NMBR2);

       }

   };
   
} // ApRepAgingDetails class