/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Comparator;
import java.util.Date;

public class ArRepAgingSummaryDetails implements java.io.Serializable {

   private String AG_CSTMR_CODE;
   private String AG_CSTMR_NM;
   private String AG_CSTMR_TYPE;
   private String AG_CSTMR_CLASS;
   private Date AG_LST_OR_DT;
   private double AG_AMNT;
   private double AG_INSTL_AMNT;
   private double AG_BCKT0;
   private double AG_BCKT1;
   private double AG_BCKT2;
   private double AG_BCKT3;
   private double AG_BCKT4;
   private double AG_BCKT5;      
   private String AG_GROUP_BY;
   private char AG_VOU_FC_SYMBL;
   private String AG_DSCRPTN;
   
   public ArRepAgingSummaryDetails() {
   }
   
   public String getAgCustomerCode() {
   	
   	  return AG_CSTMR_CODE;
   	
   }
   
   public void setAgCustomerCode(String AG_CSTMR_CODE) {
   	
   	  this.AG_CSTMR_CODE = AG_CSTMR_CODE;
   	
   }

   public String getAgCustomerName() {

	   return AG_CSTMR_NM;

   }

   public void setAgCustomerName(String AG_CSTMR_NM) {

	   this.AG_CSTMR_NM = AG_CSTMR_NM;

   }
         
   public double getAgAmount() {
   	
   	  return AG_AMNT;
   	
   }
   
   public void setAgAmount(double AG_AMNT) {
   	
   	  this.AG_AMNT = AG_AMNT;
   	
   }
   
   public double getAgInstallmentAmount() {
	   	
   	  return AG_INSTL_AMNT;
   	
   }
   
   public void setAgInstallmentAmount(double AG_INSTL_AMNT) {
   	
   	  this.AG_INSTL_AMNT = AG_INSTL_AMNT;
   	
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
   
   public String getAgCustomerType() {
   	
   		return AG_CSTMR_TYPE;
   		
   }
   
   public void setAgCustomerType(String AG_CSTMR_TYPE) {
   	
   		this.AG_CSTMR_TYPE = AG_CSTMR_TYPE;
   		
   }
   
   public String getAgCustomerClass() {
   	
   		return AG_CSTMR_CLASS;
   		
   }
   
   public void setAgCustomerClass(String AG_CSTMR_CLASS) {
   	
   		this.AG_CSTMR_CLASS = AG_CSTMR_CLASS;
   		
   }
   
   
   public Date getAgLastOrDate() {
	   	
  		return AG_LST_OR_DT;
  		
  }
  
  public void setAgLastOrDate(Date AG_LST_OR_DT) {
  	
  		this.AG_LST_OR_DT = AG_LST_OR_DT;
  		
  }
   
   public String getGroupBy() {
   	
   		return AG_GROUP_BY;
   		
   }
   
   public void setGroupBy(String AG_GROUP_BY) {
   	
   		this.AG_GROUP_BY = AG_GROUP_BY;
   		
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
   
   public static Comparator CustomerCodeComparator = (AG, anotherAG) -> {

       String AG_CUST_CSTMR_CODE1 = ((ArRepAgingSummaryDetails)AG).getAgCustomerCode();
       String AG_CUST_CSTMR_CODE2 = ((ArRepAgingSummaryDetails)anotherAG).getAgCustomerCode();

       return AG_CUST_CSTMR_CODE1.compareTo(AG_CUST_CSTMR_CODE2);

   };

   public static Comparator CustomerNameComparator = (AG, anotherAG) -> {

       String AG_CUST_CSTMR_NAME1 = ((ArRepAgingSummaryDetails)AG).getAgCustomerName();
       String AG_CUST_CSTMR_NAME2 = ((ArRepAgingSummaryDetails)anotherAG).getAgCustomerName();

       return AG_CUST_CSTMR_NAME1.compareTo(AG_CUST_CSTMR_NAME2);

   };
   
   public static Comparator CustomerTypeComparator = (AG, anotherAG) -> {


       String AG_CUST_CSTMR_TYP1 = ((ArRepAgingSummaryDetails)AG).getAgCustomerType();
       String AG_CUST_CSTMR_TYP2 = ((ArRepAgingSummaryDetails)anotherAG).getAgCustomerType();

    return AG_CUST_CSTMR_TYP1.compareTo(AG_CUST_CSTMR_TYP2);

   };

   public static Comparator CustomerClassComparator = (AG, anotherAG) -> {

       String AG_CUST_CSTMR_CLSS1 = ((ArRepAgingSummaryDetails)AG).getAgCustomerClass();
       String AG_CUST_CSTMR_CLSS2 = ((ArRepAgingSummaryDetails)anotherAG).getAgCustomerClass();

    return AG_CUST_CSTMR_CLSS1.compareTo(AG_CUST_CSTMR_CLSS2);

   };
   
} // ArRepAgingDetails class