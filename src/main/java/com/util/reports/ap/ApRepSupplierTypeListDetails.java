/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;


public class ApRepSupplierTypeListDetails implements java.io.Serializable {

   private String STL_ST_NM; 
   private String STL_ST_DESC; 
   private String STL_BNK_ACCNT; 
   private byte STL_ENBL;

   public ApRepSupplierTypeListDetails() {
   }

   public byte getStlEnable() {
   	
   	 return STL_ENBL;
   
   }
   
   public void setStlEnable(byte STL_ENBL) {
   
   	 this.STL_ENBL = STL_ENBL;
   
   }

   public String getStlStDescription() {
   
   	 return STL_ST_DESC;
   
   }
   
   public void setStlStDescription(String STL_ST_DESC) {
   
   	 this.STL_ST_DESC = STL_ST_DESC;
   
   }
   
   public String getStlStName() {
   
   	 return STL_ST_NM;
   
   }
   
   public void setStlStName(String STL_ST_NM) {
   
   	 this.STL_ST_NM = STL_ST_NM;
   
   }
   
   public String getStlBankAccount() {
   
   	 return STL_BNK_ACCNT;
   
   }
   
   public void setStlBankAccount(String STL_BNK_ACCNT) {
   
   	 this.STL_BNK_ACCNT = STL_BNK_ACCNT;
   
   }
   
} // ApRepSupplierTypeListDetails class