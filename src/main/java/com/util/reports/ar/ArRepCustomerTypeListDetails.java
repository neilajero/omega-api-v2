/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;


public class ArRepCustomerTypeListDetails implements java.io.Serializable {

   private String CTL_CT_NM; 
   private String CTL_CT_DESC; 
   private String CTL_BNK_ACCNT; 
   private byte CTL_ENBL;

   public ArRepCustomerTypeListDetails() {
   }

   public byte getCtlEnable() {
   	
   	 return CTL_ENBL;
   
   }
   
   public void setCtlEnable(byte CTL_ENBL) {
   
   	 this.CTL_ENBL = CTL_ENBL;
   
   }

   public String getCtlCtDescription() {
   
   	 return CTL_CT_DESC;
   
   }
   
   public void setCtlCtDescription(String CTL_CT_DESC) {
   
   	 this.CTL_CT_DESC = CTL_CT_DESC;
   
   }
   
   public String getCtlCtName() {
   
   	 return CTL_CT_NM;
   
   }
   
   public void setCtlCtName(String CTL_CT_NM) {
   
   	 this.CTL_CT_NM = CTL_CT_NM;
   
   }
   
   public String getCtlBankAccount() {
   
   	 return CTL_BNK_ACCNT;
   
   }
   
   public void setCtlBankAccount(String CTL_BNK_ACCNT) {
   
   	 this.CTL_BNK_ACCNT = CTL_BNK_ACCNT;
   
   }
   
} // ArRepCustomerTypeListDetails class