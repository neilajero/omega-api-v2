/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;


public class GlRepDetailBalanceSheetDetails implements java.io.Serializable {

   private String DBS_ACCNT_NMBR;
   private String DBS_ACCNT_DESC;   
   private String DBS_ACCNT_TYP;
   private double DBS_BLNC;
   private double DBS_BLNC_PRV;
   private String DBS_PRVS_PRD;
   private double DBS_BLNC_ADVNCS;

   public GlRepDetailBalanceSheetDetails() {
   }

   public String getDbsAccountNumber() {
   	
      return DBS_ACCNT_NMBR;
      
   }
   
   public void setDbsAccountNumber(String DBS_ACCNT_NMBR) {
   	
   	  this.DBS_ACCNT_NMBR = DBS_ACCNT_NMBR;
   	
   }

   public String getDbsAccountDescription() {
   	
      return DBS_ACCNT_DESC;
      
   }
   
   public void setDbsAccountDescription(String DBS_ACCNT_DESC) {
   	
   	  this.DBS_ACCNT_DESC = DBS_ACCNT_DESC;
   	
   }
   
   public String getDbsAccountType() {
   	
      return DBS_ACCNT_TYP;
      
   }
   
   public void setDbsAccountType(String DBS_ACCNT_TYP) {
   	
   	  this.DBS_ACCNT_TYP = DBS_ACCNT_TYP;
   	
   }

   public double getDbsBalance() {
   	
      return DBS_BLNC;
      
   }
   
   public void setDbsBalance(double DBS_BLNC) {
   	
   	  this.DBS_BLNC = DBS_BLNC;
   	
   }

   public double getDbsPreviousBalance() {
   	
      return DBS_BLNC_PRV;
      
   }
   public void setDbsPreviousBalance(double DBS_BLNC_PRV) {
   	
   	  this.DBS_BLNC_PRV = DBS_BLNC_PRV;
   	
   }

   public String getDbsPreviousPeriod() {
   	
      return DBS_PRVS_PRD;
      
   }
   public void setDbsPreviousPeriod(String DBS_PRVS_PRD) {
   	
   	  this.DBS_PRVS_PRD = DBS_PRVS_PRD;
   	
   }

   public double getDbsBalanceAdvances() {
   	
      return DBS_BLNC_ADVNCS;
      
   }
   public void setDbsBalanceAdvances(double DBS_BLNC_ADVNCS) {
   	
   	  this.DBS_BLNC_ADVNCS = DBS_BLNC_ADVNCS;
   	
   }

} // GlRepDetailBalanceSheetDetails class   