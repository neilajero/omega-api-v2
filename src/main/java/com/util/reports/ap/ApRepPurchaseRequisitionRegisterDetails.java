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

public class ApRepPurchaseRequisitionRegisterDetails implements java.io.Serializable {

	   private Date PR_DT;
	   private String PR_DCMNT_NMBR;
	   private String PR_RFRNC_NMBR;
	   private String PR_DESC;
	   private String PR_TYP;
	   private String PR_ACCNT_DSCRPTN;
	   private double PR_AMNT;
	   private String ORDER_BY;
	   private String PR_BY;
	   private String PR_II_NM;
	   private String PR_II_DESC;
	   private String PR_II_DPRTMNT;
	   private String PR_AP_SPPLR;
	   private String PR_AP_PO_NMBR;
	   private Date PR_PO_DT;
	   private String PR_LOC_NM;
	   private double PR_QTY;
	   private String PR_UNT;
	   private double PR_UNT_CST;
	   private String PR_BRNCH_CODE;
	   private String PR_BRNCH_NM;
	   private String PR_STATUS;
	 	   
	   public ApRepPurchaseRequisitionRegisterDetails() {
       }

	   public Date getPrDate() {
	   	
	   	  return PR_DT;
	   	
	   }
	   
	   public void setPrDate(Date PR_DT) {
	   	
	   	  this.PR_DT = PR_DT;
	   	
	   }
	   
	   public String getPrReferenceNumber() {
	   	
	   	  return PR_RFRNC_NMBR;
	   	  
	   }
	   
	   public void setPrReferenceNumber(String PR_RFRNC_NMBR) {
	   	
	   	  this.PR_RFRNC_NMBR = PR_RFRNC_NMBR;
	   	  
	   }
	   
	   public String getPrDocumentNumber() {
	   	
	   	  return PR_DCMNT_NMBR;
	   	  
	   }
	   
	   public void setPrDocumentNumber(String PR_DCMNT_NMBR) {
	   	
	   	  this.PR_DCMNT_NMBR = PR_DCMNT_NMBR;
	   	  
	   }
	   
	   public String getPrDescription() {
	   	
	   	  return PR_DESC;
	   	  
	   }
	   
	   public void setPrDescription(String PR_DESC) {
	   	
	   	  this.PR_DESC = PR_DESC;
	   	  
	   }
	   
	   public double getPrAmount() {
	   	
	   	  return PR_AMNT;
	   	  
	   }
	   
	   public void setPrAmount(double PR_AMNT) {
	   	
	   	  this.PR_AMNT = PR_AMNT;
	   	  
	   }
	   
	   public String getOrderBy() {
	   	
	   	  return ORDER_BY;
	   	
	   }
	   
	   public void setOrderBy(String ORDER_BY) {
	   	
	   	  this.ORDER_BY = ORDER_BY;
	   	
	   }
	   
	   public String getPrType() {
	   	
	   	  return PR_TYP;
	   	  
	   }
	   
	   public void setPrType(String PR_TYP) {
	   	
	   	  this.PR_TYP = PR_TYP;
	   	  
	   }
	   public String getPrAccountDescription() {
		   	
		   	  return PR_ACCNT_DSCRPTN;
		   	  
		   }
		   
		   public void setPrAccountDescription(String PR_ACCNT_DSCRPTN) {
		   	
		   	  this.PR_ACCNT_DSCRPTN = PR_ACCNT_DSCRPTN;
		   	  
		   }
	   public String getPrItemName() {
	   	
	   	  return PR_II_NM;
	   	  
	   }
	   
	   public void setPrItemName(String PR_II_NM) {
	   	
	   	  this.PR_II_NM = PR_II_NM;
	   	  
	   }
	   
	   public String getPrItemDesc() {
		   
		   return PR_II_DESC;
		   
	   }
	   
	   public void setPrItemDesc(String PR_II_DESC) {
		   
		   this.PR_II_DESC = PR_II_DESC;
		   
	   }
	   
	   public String getPrDepartment() {
		   
		   return PR_II_DPRTMNT;
		   
	   }
	   
	   public void setPrDepartment(String PR_II_DPRTMNT) {
		   
		   this.PR_II_DPRTMNT = PR_II_DPRTMNT;
		   
	   }
	   
	   public String getPrApSupplier() {
		   
		   return PR_AP_SPPLR;
		   
	   }
	   
	   public void setPrApSupplier(String PR_AP_SPPLR) {
		   
		   this.PR_AP_SPPLR = PR_AP_SPPLR;
		   
	   }
	   
	   public String getPrApPoNumber() {
		   
		   return PR_AP_PO_NMBR;
	   }
	   
	   public void setPrApPoNumber(String PR_AP_PO_NMBR) {
		   
		   this.PR_AP_PO_NMBR = PR_AP_PO_NMBR;
	   }
	   
	   public Date getPrPoDate() {
		   	
		   	  return PR_PO_DT;
		   	
       }
		   
	   public void setPrPoDate(Date PR_PO_DT) {
		   	
		   	  this.PR_PO_DT = PR_PO_DT;
		   	
       }
	   
	   public String getPrLocation() {
	   	
	   	  return PR_LOC_NM;
	   	  
	   }
	   
	   public void setPrLocation(String PR_LOC_NM) {
	   	
	   	  this.PR_LOC_NM = PR_LOC_NM;
	   	  
	   }
	   
	   public double getPrQuantity() {
	   	
	   	  return PR_QTY;
	   	  
	   }
	   
	   public void setPrQuantity(double PR_QTY) {
	   	
	   	  this.PR_QTY = PR_QTY;
	   	  
	   }
	      
	   public String getPrUnit() {
	   	
	   	  return PR_UNT;
	   	  
	   }
	   
	   public void setPrUnit(String PR_UNT) {
	   	
	   	  this.PR_UNT = PR_UNT;
	   	  
	   }
	   
	   public double getPrUnitCost() {
	   	
	   	  return PR_UNT_CST;
	   	  
	   }
	   
	   public void setPrUnitCost(double PR_UNT_CST) {
	   	
	   	  this.PR_UNT_CST = PR_UNT_CST;
	   	  
	   }
	   
	   
	   public String getPrBranchCode() {
        
          return PR_BRNCH_CODE;
          
       }
       
       public void setPrBranchCode(String PR_BRNCH_CODE) {
        
          this.PR_BRNCH_CODE = PR_BRNCH_CODE;
          
       }
       
       public String getPrBranchName() {
        
          return PR_BRNCH_NM;
          
       }
       
       public void setPrBranchName(String PR_BRNCH_NM) {
        
          this.PR_BRNCH_NM = PR_BRNCH_NM;
          
       }
       
      public String getPrStatus() {
        
          return PR_STATUS;
          
       }
       
       public void setPrStatus(String PR_STATUS) {
        
          this.PR_STATUS = PR_STATUS;
          
       }
	   
	   public static Comparator ItemNameComparator = (PR, anotherPR) -> {

           String PR_II_NM1 = ((ApRepPurchaseRequisitionRegisterDetails) PR).getPrItemName();
           Date PR_DT1 = ((ApRepPurchaseRequisitionRegisterDetails) PR).getPrDate();
           String PR_DCMNT_NMBR1 = ((ApRepPurchaseRequisitionRegisterDetails) PR).getPrDocumentNumber();

           String PR_II_NM2 = ((ApRepPurchaseRequisitionRegisterDetails) anotherPR).getPrItemName();
           Date PR_DT2 = ((ApRepPurchaseRequisitionRegisterDetails) anotherPR).getPrDate();
           String PR_DCMNT_NMBR2 = ((ApRepPurchaseRequisitionRegisterDetails) anotherPR).getPrDocumentNumber();

           String ORDER_BY = ((ApRepPurchaseRequisitionRegisterDetails) PR).getOrderBy();

           if (!(PR_II_NM1.equals(PR_II_NM2))) {

               return PR_II_NM1.compareTo(PR_II_NM2);

           } else {

               if(ORDER_BY.equals("DATE") && !(PR_DT1.equals(PR_DT2))){

                   return PR_DT1.compareTo(PR_DT2);

               } else {

                   return PR_DCMNT_NMBR1.compareTo(PR_DCMNT_NMBR2);

               }
           }

       };
	   
	   public static Comparator DateComparator = (PR, anotherPR) -> {

           String PR_II_NM1 = ((ApRepPurchaseRequisitionRegisterDetails) PR).getPrItemName();
           Date PR_DT1 = ((ApRepPurchaseRequisitionRegisterDetails) PR).getPrDate();
           String PR_DCMNT_NMBR1 = ((ApRepPurchaseRequisitionRegisterDetails) PR).getPrDocumentNumber();

           String PR_II_NM2 = ((ApRepPurchaseRequisitionRegisterDetails) anotherPR).getPrItemName();
           Date PR_DT2 = ((ApRepPurchaseRequisitionRegisterDetails) anotherPR).getPrDate();
           String PR_DCMNT_NMBR2 = ((ApRepPurchaseRequisitionRegisterDetails) anotherPR).getPrDocumentNumber();

           String ORDER_BY = ((ApRepPurchaseRequisitionRegisterDetails) PR).getOrderBy();

           if (!(PR_DT1.equals(PR_DT2))) {

               return PR_DT1.compareTo(PR_DT2);

           } else {

               return PR_DCMNT_NMBR1.compareTo(PR_DCMNT_NMBR2);

           }

       };
	   
	   public static Comparator NoGroupComparator = (PR, anotherPR) -> {

           Date PR_DT1 = ((ApRepPurchaseRequisitionRegisterDetails) PR).getPrDate();
           String PR_DCMNT_NMBR1 = ((ApRepPurchaseRequisitionRegisterDetails) PR).getPrDocumentNumber();

           Date PR_DT2 = ((ApRepPurchaseRequisitionRegisterDetails) PR).getPrDate();
           String PR_DCMNT_NMBR2 = ((ApRepPurchaseRequisitionRegisterDetails) PR).getPrDocumentNumber();

           String ORDER_BY = ((ApRepPurchaseRequisitionRegisterDetails) PR).getOrderBy();

           if(ORDER_BY.equals("DATE") && !(PR_DT1.equals(PR_DT2))){

             return PR_DT1.compareTo(PR_DT2);

           } else {

             return PR_DCMNT_NMBR1.compareTo(PR_DCMNT_NMBR2);

           }

       };

} // ApRepPurchaseRequistionDetails class