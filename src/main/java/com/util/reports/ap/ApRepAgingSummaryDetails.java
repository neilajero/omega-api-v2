/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.util.Comparator;


public class ApRepAgingSummaryDetails implements java.io.Serializable {

   private String AGS_SPPLR_CODE;         
   private String AGS_SPPLR_TYPE;
   private String AGS_SPPLR_CLASS;
   private double AGS_AMNT;
   private double AGS_BCKT0;
   private double AGS_BCKT1;
   private double AGS_BCKT2;
   private double AGS_BCKT3;
   private double AGS_BCKT4;
   private double AGS_BCKT5;   
   private String AGS_GROUP_BY;
   private char AG_VOU_FC_SYMBL;
   private String AG_DSCRPTN;
   
   public ApRepAgingSummaryDetails() {
   }
   
   public String getAgSupplierCode() {
   	
   	  return AGS_SPPLR_CODE;
   	
   }
   
   public void setAgSupplierCode(String AGS_SPPLR_CODE) {
   	
   	  this.AGS_SPPLR_CODE = AGS_SPPLR_CODE;
   	
   }
   
   public double getAgAmount() {
   	
   	  return AGS_AMNT;
   	
   }
   
   public void setAgAmount(double AGS_AMNT) {
   	
   	  this.AGS_AMNT = AGS_AMNT;
   	
   }
   
   public double getAgBucket0() {
   	
   	  return AGS_BCKT0;
   	
   }
   
   public void setAgBucket0(double AGS_BCKT0) {
   	
   	  this.AGS_BCKT0 = AGS_BCKT0;
   	
   }
   
   public double getAgBucket1() {
   	
   	  return AGS_BCKT1;
   	
   }
   
   public void setAgBucket1(double AGS_BCKT1) {
   	
   	  this.AGS_BCKT1 = AGS_BCKT1;
   	
   }
   
   public double getAgBucket2() {
   	
   	  return AGS_BCKT2;
   	
   }
   
   public void setAgBucket2(double AGS_BCKT2) {
   	
   	  this.AGS_BCKT2 = AGS_BCKT2;
   	
   }
   
   public double getAgBucket3() {
   	
   	  return AGS_BCKT3;
   	
   }
   
   public void setAgBucket3(double AGS_BCKT3) {
   	
   	  this.AGS_BCKT3 = AGS_BCKT3;
   	
   }
   
   
   public double getAgBucket4() {
   	
   	  return AGS_BCKT4;
   	
   }
   
   public void setAgBucket4(double AGS_BCKT4) {
   	
   	  this.AGS_BCKT4 = AGS_BCKT4;
   	
   }
   
   
   public double getAgBucket5() {
   	
   	  return AGS_BCKT5;
   	
   }
   
   public void setAgBucket5(double AGS_BCKT5) {
   	
   	  this.AGS_BCKT5 = AGS_BCKT5;
   	
   }      
   
   public String getAgSupplierType() {
   	
   		return AGS_SPPLR_TYPE;
   		
   }
   
   public void setAgSupplierType(String AGS_SPPLR_TYPE) {
   	
   		this.AGS_SPPLR_TYPE = AGS_SPPLR_TYPE;
   		
   }
   
   public String getAgSupplierClass() {
   	
   		return AGS_SPPLR_CLASS;
   		
   }
   
   public void setAgSupplierClass(String AGS_SPPLR_CLASS) {
   	
   		this.AGS_SPPLR_CLASS = AGS_SPPLR_CLASS;
   		
   }   
   
   public String getGroupBy() {
   	
   		return AGS_GROUP_BY;
   		
   }
   
   public void setGroupBy(String AGS_GROUP_BY) {
   	
   		this.AGS_GROUP_BY = AGS_GROUP_BY;

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
   
   public static Comparator SupplierCodeComparator = (AG, anotherAG) -> {

       String AG_SPL_SPPLR_CODE1 = ((ApRepAgingSummaryDetails)AG).getAgSupplierCode();
       String AG_SPL_SPPLR_CODE2 = ((ApRepAgingSummaryDetails)anotherAG).getAgSupplierCode();

    return AG_SPL_SPPLR_CODE1.compareTo(AG_SPL_SPPLR_CODE2);

};
   
   public static Comparator SupplierTypeComparator = (AG, anotherAG) -> {

       String AG_SPL_SPPLR_TYP1 = ((ApRepAgingSummaryDetails)AG).getAgSupplierType();
       String AG_SPL_SPPLR_TYP2 = ((ApRepAgingSummaryDetails)anotherAG).getAgSupplierType();

       return AG_SPL_SPPLR_TYP1.compareTo(AG_SPL_SPPLR_TYP2);

};
   
   public static Comparator SupplierClassComparator = (AG, anotherAG) -> {

       String AG_SPL_SPPLR_CLSS1 = ((ApRepAgingSummaryDetails)AG).getAgSupplierClass();
       String AG_SPL_SPPLR_CLSS2 = ((ApRepAgingSummaryDetails)anotherAG).getAgSupplierClass();

    return AG_SPL_SPPLR_CLSS1.compareTo(AG_SPL_SPPLR_CLSS2);

   };
   
} // ApRepAgingDetails class