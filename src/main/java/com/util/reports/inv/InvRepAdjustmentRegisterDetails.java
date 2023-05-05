/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Comparator;
import java.util.Date;

public class InvRepAdjustmentRegisterDetails implements java.io.Serializable {

	   private Date ADJ_DT;
	   private String ADJ_DCMNT_NMBR;
	   private String ADJ_RFRNC_NMBR;
	   private String ADJ_DESC;
	   private String ADJ_TYP;
	   private String ADJ_ACCNT_DSCRPTN;
	   private double ADJ_AMNT;
	   private String ORDER_BY;
	   private String ADJ_II_NM;
	   private String ADJ_II_DESC;
	   private String ADJ_LOC_NM;
	   private double ADJ_QTY;
	   private String ADJ_UNT;
	   private double ADJ_UNT_CST;
	 	   
	   public InvRepAdjustmentRegisterDetails() {
       }

	   public Date getAdjDate() {
	   	
	   	  return ADJ_DT;
	   	
	   }
	   
	   public void setAdjDate(Date ADJ_DT) {
	   	
	   	  this.ADJ_DT = ADJ_DT;
	   	
	   }
	   
	   public String getAdjReferenceNumber() {
	   	
	   	  return ADJ_RFRNC_NMBR;
	   	  
	   }
	   
	   public void setAdjReferenceNumber(String ADJ_RFRNC_NMBR) {
	   	
	   	  this.ADJ_RFRNC_NMBR = ADJ_RFRNC_NMBR;
	   	  
	   }
	   
	   public String getAdjDocumentNumber() {
	   	
	   	  return ADJ_DCMNT_NMBR;
	   	  
	   }
	   
	   public void setAdjDocumentNumber(String ADJ_DCMNT_NMBR) {
	   	
	   	  this.ADJ_DCMNT_NMBR = ADJ_DCMNT_NMBR;
	   	  
	   }
	   
	   public String getAdjDescription() {
	   	
	   	  return ADJ_DESC;
	   	  
	   }
	   
	   public void setAdjDescription(String ADJ_DESC) {
	   	
	   	  this.ADJ_DESC = ADJ_DESC;
	   	  
	   }
	   
	   public double getAdjAmount() {
	   	
	   	  return ADJ_AMNT;
	   	  
	   }
	   
	   public void setAdjAmount(double ADJ_AMNT) {
	   	
	   	  this.ADJ_AMNT = ADJ_AMNT;
	   	  
	   }
	   
	   public String getOrderBy() {
	   	
	   	  return ORDER_BY;
	   	
	   }
	   
	   public void setOrderBy(String ORDER_BY) {
	   	
	   	  this.ORDER_BY = ORDER_BY;
	   	
	   }
	   
	   public String getAdjType() {
	   	
	   	  return ADJ_TYP;
	   	  
	   }
	   
	   public void setAdjType(String ADJ_TYP) {
	   	
	   	  this.ADJ_TYP = ADJ_TYP;
	   	  
	   }
	   public String getAdjAccountDescription() {
		   	
		   	  return ADJ_ACCNT_DSCRPTN;
		   	  
		   }
		   
		   public void setAdjAccountDescription(String ADJ_ACCNT_DSCRPTN) {
		   	
		   	  this.ADJ_ACCNT_DSCRPTN = ADJ_ACCNT_DSCRPTN;
		   	  
		   }
	   public String getAdjItemName() {
	   	
	   	  return ADJ_II_NM;
	   	  
	   }
	   
	   public void setAdjItemName(String ADJ_II_NM) {
	   	
	   	  this.ADJ_II_NM = ADJ_II_NM;
	   	  
	   }
	   
	   public String getAdjItemDesc() {
		   
		   return ADJ_II_DESC;
		   
	   }
	   
	   public void setAdjItemDesc(String ADJ_II_DESC) {
		   
		   this.ADJ_II_DESC = ADJ_II_DESC;
		   
	   }
	   
	   public String getAdjLocation() {
	   	
	   	  return ADJ_LOC_NM;
	   	  
	   }
	   
	   public void setAdjLocation(String ADJ_LOC_NM) {
	   	
	   	  this.ADJ_LOC_NM = ADJ_LOC_NM;
	   	  
	   }
	   
	   public double getAdjQuantity() {
	   	
	   	  return ADJ_QTY;
	   	  
	   }
	   
	   public void setAdjQuantity(double ADJ_QTY) {
	   	
	   	  this.ADJ_QTY = ADJ_QTY;
	   	  
	   }
	      
	   public String getAdjUnit() {
	   	
	   	  return ADJ_UNT;
	   	  
	   }
	   
	   public void setAdjUnit(String ADJ_UNT) {
	   	
	   	  this.ADJ_UNT = ADJ_UNT;
	   	  
	   }
	   
	   public double getAdjUnitCost() {
	   	
	   	  return ADJ_UNT_CST;
	   	  
	   }
	   
	   public void setAdjUnitCost(double ADJ_UNT_CST) {
	   	
	   	  this.ADJ_UNT_CST = ADJ_UNT_CST;
	   	  
	   }
	   
	   public static Comparator ItemNameComparator = (ADJ, anotherADJ) -> {

           String ADJ_II_NM1 = ((InvRepAdjustmentRegisterDetails) ADJ).getAdjItemName();
           Date ADJ_DT1 = ((InvRepAdjustmentRegisterDetails) ADJ).getAdjDate();
           String ADJ_DCMNT_NMBR1 = ((InvRepAdjustmentRegisterDetails) ADJ).getAdjDocumentNumber();

           String ADJ_II_NM2 = ((InvRepAdjustmentRegisterDetails) anotherADJ).getAdjItemName();
           Date ADJ_DT2 = ((InvRepAdjustmentRegisterDetails) anotherADJ).getAdjDate();
           String ADJ_DCMNT_NMBR2 = ((InvRepAdjustmentRegisterDetails) anotherADJ).getAdjDocumentNumber();

           String ORDER_BY = ((InvRepAdjustmentRegisterDetails) ADJ).getOrderBy();

           if (!(ADJ_II_NM1.equals(ADJ_II_NM2))) {

               return ADJ_II_NM1.compareTo(ADJ_II_NM2);

           } else {

               if(ORDER_BY.equals("DATE") && !(ADJ_DT1.equals(ADJ_DT2))){

                   return ADJ_DT1.compareTo(ADJ_DT2);

               } else {

                   return ADJ_DCMNT_NMBR1.compareTo(ADJ_DCMNT_NMBR2);

               }
           }

       };
	   
	   public static Comparator DateComparator = (ADJ, anotherADJ) -> {

           String ADJ_II_NM1 = ((InvRepAdjustmentRegisterDetails) ADJ).getAdjItemName();
           Date ADJ_DT1 = ((InvRepAdjustmentRegisterDetails) ADJ).getAdjDate();
           String ADJ_DCMNT_NMBR1 = ((InvRepAdjustmentRegisterDetails) ADJ).getAdjDocumentNumber();

           String ADJ_II_NM2 = ((InvRepAdjustmentRegisterDetails) anotherADJ).getAdjItemName();
           Date ADJ_DT2 = ((InvRepAdjustmentRegisterDetails) anotherADJ).getAdjDate();
           String ADJ_DCMNT_NMBR2 = ((InvRepAdjustmentRegisterDetails) anotherADJ).getAdjDocumentNumber();

           String ORDER_BY = ((InvRepAdjustmentRegisterDetails) ADJ).getOrderBy();

           if (!(ADJ_DT1.equals(ADJ_DT2))) {

               return ADJ_DT1.compareTo(ADJ_DT2);

           } else {

               return ADJ_DCMNT_NMBR1.compareTo(ADJ_DCMNT_NMBR2);

           }

       };
	   
	   public static Comparator NoGroupComparator = (ADJ, anotherADJ) -> {

           Date ADJ_DT1 = ((InvRepAdjustmentRegisterDetails) ADJ).getAdjDate();
           String ADJ_DCMNT_NMBR1 = ((InvRepAdjustmentRegisterDetails) ADJ).getAdjDocumentNumber();

           Date ADJ_DT2 = ((InvRepAdjustmentRegisterDetails) ADJ).getAdjDate();
           String ADJ_DCMNT_NMBR2 = ((InvRepAdjustmentRegisterDetails) ADJ).getAdjDocumentNumber();

           String ORDER_BY = ((InvRepAdjustmentRegisterDetails) ADJ).getOrderBy();

           if(ORDER_BY.equals("DATE") && !(ADJ_DT1.equals(ADJ_DT2))){

             return ADJ_DT1.compareTo(ADJ_DT2);

           } else {

             return ADJ_DCMNT_NMBR1.compareTo(ADJ_DCMNT_NMBR2);

           }

       };

} // InvRepAdjustmentRegisterDetails class