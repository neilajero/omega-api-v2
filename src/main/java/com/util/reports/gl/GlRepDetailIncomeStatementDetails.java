/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;


public class GlRepDetailIncomeStatementDetails implements java.io.Serializable {

   private String DIS_ACCNT_NMBR;
   private String DIS_ACCNT_DESC;   
   private String DIS_ACCNT_TYP;
   private double DIS_BLNC;

   public GlRepDetailIncomeStatementDetails() {
   }

   public String getDisAccountNumber() {
   	
      return DIS_ACCNT_NMBR;
      
   }
   
   public void setDisAccountNumber(String DIS_ACCNT_NMBR) {
   	
   	  this.DIS_ACCNT_NMBR = DIS_ACCNT_NMBR;
   	
   }

   public String getDisAccountDescription() {
   	
      return DIS_ACCNT_DESC;
      
   }
   
   public void setDisAccountDescription(String DIS_ACCNT_DESC) {
   	
   	  this.DIS_ACCNT_DESC = DIS_ACCNT_DESC;
   	
   }
   
   public String getDisAccountType() {
   	
      return DIS_ACCNT_TYP;
      
   }
   
   public void setDisAccountType(String DIS_ACCNT_TYP) {
   	
   	  this.DIS_ACCNT_TYP = DIS_ACCNT_TYP;
   	
   }

   public double getDisBalance() {
   	
      return DIS_BLNC;
      
   }
   
   public void setDisBalance(double DIS_BLNC) {
   	
   	  this.DIS_BLNC = DIS_BLNC;
   	
   }
   
} // GlRepDetailIncomeStatementDetails class   