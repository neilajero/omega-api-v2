/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;


public class GlRepDetailTrialBalanceDetails implements java.io.Serializable {

   private String DTB_ACCNT_NMBR;
   private String DTB_ACCNT_SGMNT1;
   private String DTB_ACCNT_DESC;  
   private String DTB_ACCNT_DESC1;  
   private double DTB_DBT;
   private double DTB_CRDT;
   private double DTB_DBT_BEG;
   private double DTB_CRDT_BEG;
   
   private double DTB_TTL_DBT;
   private double DTB_TTL_CRDT;

   public GlRepDetailTrialBalanceDetails() {
   }

   public String getDtbAccountNumber() {
   	
      return DTB_ACCNT_NMBR;
      
   }
   
   public void setDtbAccountNumber(String DTB_ACCNT_NMBR) {
   	
   	  this.DTB_ACCNT_NMBR = DTB_ACCNT_NMBR;
   	
   }
   
   public String getDtbAccountSegment1() {
	   	
      return DTB_ACCNT_SGMNT1;
      
   }
   
   public void setDtbAccountSegment1(String DTB_ACCNT_SGMNT1) {
   	
   	  this.DTB_ACCNT_SGMNT1 = DTB_ACCNT_SGMNT1;
   	
   }

   public String getDtbAccountDescription() {
   	
      return DTB_ACCNT_DESC;
      
   }
   
   public void setDtbAccountDescription(String DTB_ACCNT_DESC) {
   	
   	  this.DTB_ACCNT_DESC = DTB_ACCNT_DESC;
   	
   }
   
   
   public String getDtbAccountDescription1() {
	   	
	      return DTB_ACCNT_DESC1;
	      
	   }
	   
	   public void setDtbAccountDescription1(String DTB_ACCNT_DESC1) {
	   	
	   	  this.DTB_ACCNT_DESC1 = DTB_ACCNT_DESC1;
	   	
	   }
   
   public double getDtbDebit() {
   	
      return DTB_DBT;
      
   }
   
   public void setDtbDebit(double DTB_DBT) {
   	
   	  this.DTB_DBT = DTB_DBT;
   	
   }

   public double getDtbCredit() {
   	
      return DTB_CRDT;
      
   }
   
   public void setDtbCredit(double DTB_CRDT) {
   	
   	  this.DTB_CRDT = DTB_CRDT;
   	
   }
   
   
   public double getDtbDebitBeg() {
	   	
	  return DTB_DBT_BEG;
	      
   }
	   
   public void setDtbDebitBeg(double DTB_DBT_BEG) {
	   	
	   this.DTB_DBT_BEG = DTB_DBT_BEG;
	   	
   }
   
   public double getDtbCreditBeg() {
	   	
	   return DTB_CRDT_BEG;
		      
   }
		   
   public void setDtbCreditBeg(double DTB_CRDT_BEG) {
		   	
	   this.DTB_CRDT_BEG = DTB_CRDT_BEG;
		   	
   }
   
   
   public double getDtbTotalDebit() {
	   
	   return DTB_TTL_DBT;
	   
   }
   
   public void setDtbTotalDebit (double DTB_TTL_DBT) {
	   
	   this.DTB_TTL_DBT = DTB_TTL_DBT;
	   
   }
   
   public double getDtbTotalCredit() {
	   
	   return DTB_TTL_CRDT;
	   
   }
   
   public void setDtbTotalCredit (double DTB_TTL_CRDT) {
	   
	   this.DTB_TTL_CRDT = DTB_TTL_CRDT;
	   
   }
   
   
   
   
   
   
} // GlRepDetailTrialBalanceDetails class   