/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Date;

public class InvRepBuildOrderPrintDetails implements java.io.Serializable {

   private Date BOP_BOR_DT;
   private String BOP_BOR_DCMNT_NMBR;
   private String BOP_BOR_RFRNC_NMBR;
   private String BOP_BOR_CRTD_BY;
   private String BOP_BOR_APPRVD_RJCTD_BY;
   private String BOP_BOR_DESC;
   private String BOP_BOL_II_NM;
   private String BOP_BOL_II_DESC;
   private String BOP_BOL_LOC_NM;
   private String BOP_BOL_UOM;
   private double BOP_BOL_QTY_RQRD;
   private double BOP_BOL_UNT_CST;

   public InvRepBuildOrderPrintDetails() {
   }

  
   public Date getBopBorDate() {
   	
   	  return BOP_BOR_DT;
   	
   }
   
   public void setBopBorDate(Date BOP_BOR_DT) {
   	
   	  this.BOP_BOR_DT = BOP_BOR_DT;
   	
   }
   
  public String getBopBorDocumentNumber() {
   	
   	  return BOP_BOR_DCMNT_NMBR;
   	
   }
   
   public void setBopBorDocumentNumber(String BOP_BOR_DCMNT_NMBR) {
   	
   	  this.BOP_BOR_DCMNT_NMBR = BOP_BOR_DCMNT_NMBR;
   	
   }
   
  public String getBopBorReferenceNumber() {
   	
   	  return BOP_BOR_RFRNC_NMBR;
   	
   }
   
   public void setBopBorReferenceNumber(String BOP_BOR_RFRNC_NMBR) {
   	
   	  this.BOP_BOR_RFRNC_NMBR = BOP_BOR_RFRNC_NMBR;
   	
   }
   
  public String getBopBorCreatedBy() {
   	
   	  return BOP_BOR_CRTD_BY;
   	
   }
   
   public void setBopBorCreatedBy(String BOP_BOR_CRTD_BY) {
   	
   	  this.BOP_BOR_CRTD_BY= BOP_BOR_CRTD_BY;
   	
   }
   
   public String getBopBorApprovedRejectedBy() {
   	
   	  return BOP_BOR_APPRVD_RJCTD_BY;
   	
   }
   
   public void setBopBorApprovedRejectedBy(String BOP_BOR_APPRVD_RJCTD_BY) {
   	
   	  this.BOP_BOR_APPRVD_RJCTD_BY = BOP_BOR_APPRVD_RJCTD_BY;
   	
   }
   
   
   public String getBopBorDescription() {
   	
   	  return BOP_BOR_DESC;
   	
   }
   
   public void setBopBorDescription(String BOP_BOR_DESC) {
   	
   	  this.BOP_BOR_DESC = BOP_BOR_DESC;
   	
   }
   
   public String getBopBolIiName() {
   	
   	  return BOP_BOL_II_NM;
   	
   }
   
   public void setBopBolIiName(String BOP_BOL_II_NM) {
   	
   	  this.BOP_BOL_II_NM = BOP_BOL_II_NM;
   	
   }
   
   public String getBopBolIiDescription() {
   	
   	  return BOP_BOL_II_DESC;
   	
   }
   
   public void setBopBolIiDescription(String BOP_BOL_II_DESC) {
   	
   	  this.BOP_BOL_II_DESC = BOP_BOL_II_DESC;
   	
   }
   
   
   public String getBopBolLocName() {
   	
   	  return BOP_BOL_LOC_NM;
   	
   }
   
   public void setBopBolLocName(String BOP_BOL_LOC_NM) {
   	
   	  this.BOP_BOL_LOC_NM = BOP_BOL_LOC_NM;
   	
   }
   
   public String getBopBolUomName() {
   	
   	  return BOP_BOL_UOM;
   	
   }
   
   public void setBopBolUomName(String BOP_BOL_UOM) {
   	
   	  this.BOP_BOL_UOM = BOP_BOL_UOM;
   	
   }
   
   public double getBopBolQuantityRequired() {
   	
   	  return BOP_BOL_QTY_RQRD;
   	
   }
   
   public void setBopBolQuantityRequired(double BOP_BOL_QTY_RQRD) {
   	
   	  this.BOP_BOL_QTY_RQRD = BOP_BOL_QTY_RQRD;
   	
   }

   public double getBopBolUnitCost() {
      	
      	  return BOP_BOL_UNT_CST;
      	
      }
      
      public void setBopBolUnitCost(double BOP_BOL_UNT_CST) {
      	
      	  this.BOP_BOL_UNT_CST = BOP_BOL_UNT_CST;
      	
      }

   
 }  // InvRepBuildOrderPrintDetails