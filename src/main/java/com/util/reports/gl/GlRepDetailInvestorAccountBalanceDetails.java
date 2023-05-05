/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;


public class GlRepDetailInvestorAccountBalanceDetails implements java.io.Serializable {

   private String IRAB_SPPLR_CODE;
   private String IRAB_SPPLR_NM;

   private double IRAB_DBT;
   private double IRAB_CRDT;
   private double IRAB_DBT_BEG;
   private double IRAB_CRDT_BEG;
   private double IRAB_TTL_DBT;
   private double IRAB_TTL_CRDT;

   public GlRepDetailInvestorAccountBalanceDetails() {
   }

   public String getIrabSupplierCode() {
   	
      return IRAB_SPPLR_CODE;
      
   }
   
   public void setIrabSupplierCode(String IRAB_SPPLR_CODE) {
   	
   	  this.IRAB_SPPLR_CODE = IRAB_SPPLR_CODE;
   	
   }
   
   public String getIrabSupplierName() {
	   	
      return IRAB_SPPLR_NM;
      
   }
   
   public void setIrabSupplierName(String IRAB_SPPLR_NM) {
   	
   	  this.IRAB_SPPLR_NM = IRAB_SPPLR_NM;
   	
   }
   
   
   
   public double getIrabDebit() {
   	
      return IRAB_DBT;
      
   }
   
   public void setIrabDebit(double IRAB_DBT) {
   	
   	  this.IRAB_DBT = IRAB_DBT;
   	
   }

   public double getIrabCredit() {
   	
      return IRAB_CRDT;
      
   }
   
   public void setIrabCredit(double IRAB_CRDT) {
   	
   	  this.IRAB_CRDT = IRAB_CRDT;
   	
   }
   
   
   public double getIrabDebitBeg() {
	   	
	  return IRAB_DBT_BEG;
	      
   }
	   
   public void setIrabDebitBeg(double IRAB_DBT_BEG) {
	   	
	   this.IRAB_DBT_BEG = IRAB_DBT_BEG;
	   	
   }
   
   public double getIrabCreditBeg() {
	   	
	   return IRAB_CRDT_BEG;
		      
   }
		   
   public void setIrabCreditBeg(double IRAB_CRDT_BEG) {
		   	
	   this.IRAB_CRDT_BEG = IRAB_CRDT_BEG;
		   	
   }
   
   
   public double getIrabTotalDebit() {
	   
	   return IRAB_TTL_DBT;
	   
   }
   
   public void setIrabTotalDebit (double IRAB_TTL_DBT) {
	   
	   this.IRAB_TTL_DBT = IRAB_TTL_DBT;
	   
   }
   
   public double getIrabTotalCredit() {
	   
	   return IRAB_TTL_CRDT;
	   
   }
   
   public void setIrabTotalCredit (double IRAB_TTL_CRDT) {
	   
	   this.IRAB_TTL_CRDT = IRAB_TTL_CRDT;
	   
   }
   
   
   
   
   
   
} // GlRepDetailInvestorAccountBalanceDetails class   