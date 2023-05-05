/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;


public class ApRepSupplierClassListDetails implements java.io.Serializable {

   private String SCL_SC_NM; 
   private String SCL_SC_DESC; 
   private String SCL_TX_NM; 
   private String SCL_WT_NM; 
   private String SCL_PYBL_ACCNT_NMBR;  
   private String SCL_PYBL_ACCNT_DESC; 
   private String SCL_EXP_ACCNT_NMBR;  
   private String SCL_EXP_ACCNT_DESC; 
   private byte SCL_ENBL;

   public ApRepSupplierClassListDetails() {
   }

   public byte getSclEnable() {
   	
   	 return SCL_ENBL;
   
   }
   
   public void setSclEnable(byte SCL_ENBL) {
   
   	 this.SCL_ENBL = SCL_ENBL;
   
   }
   
   public String getSclExpenseAccountDescription() {
   
   	 return SCL_EXP_ACCNT_DESC;
   
   }
   
   public void setSclExpenseAccountDescription(String SCL_EXP_ACCNT_DESC) {
   
   	 this.SCL_EXP_ACCNT_DESC = SCL_EXP_ACCNT_DESC;
   
   }
   
   public String getSclExpenseAccountNumber() {
   
   	 return SCL_EXP_ACCNT_NMBR;
   
   }
   
   public void setSclExpenseAccountNumber(String SCL_EXP_ACCNT_NMBR) {
   
   	 this.SCL_EXP_ACCNT_NMBR = SCL_EXP_ACCNT_NMBR;
   
   }
   
   public String getSclPayableAccountDescription() {
   
   	 return SCL_PYBL_ACCNT_DESC;
   
   }
   
   public void setSclPayableAccountDescription(String SCL_PYBL_ACCNT_DESC) {
  
   	 this.SCL_PYBL_ACCNT_DESC = SCL_PYBL_ACCNT_DESC;
   
   }
   
   public String getSclPayableAccountNumber() {
   
   	 return SCL_PYBL_ACCNT_NMBR;
   
   }
   
   public void setSclPayableAccountNumber(String SCL_PYBL_ACCNT_NMBR) {
   
   	 this.SCL_PYBL_ACCNT_NMBR = SCL_PYBL_ACCNT_NMBR;
   
   }
   
   public String getSclScDescription() {
   
   	 return SCL_SC_DESC;
   
   }
   
   public void setSclScDescription(String SCL_SC_DESC) {
   
   	 this.SCL_SC_DESC = SCL_SC_DESC;
   
   }
   
   public String getSclScName() {
   
   	 return SCL_SC_NM;
   
   }
   
   public void setSclScName(String SCL_SC_NM) {
   
   	 this.SCL_SC_NM = SCL_SC_NM;
   
   }
   
   public String getSclTaxName() {
   
   	 return SCL_TX_NM;
   
   }
   
   public void setSclTaxName(String SCL_TX_NM) {
   
   	 this.SCL_TX_NM = SCL_TX_NM;
   
   }
   
   public String getSclWithholdingTaxName() {
   
   	 return SCL_WT_NM;
   
   }
   
   public void setSclWithholdingTaxName(String SCL_WT_NM) {
   
   	 this.SCL_WT_NM = SCL_WT_NM;
   
   }
   
} // ApRepSupplierClassListDetails class