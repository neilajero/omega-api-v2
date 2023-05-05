/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;


import com.util.ap.ApDistributionRecordDetails;

public class ApModDistributionRecordDetails extends ApDistributionRecordDetails implements java.io.Serializable {

   private String DR_COA_ACCNT_NMBR;
   private String DR_COA_ACCNT_DESC;
   private byte DR_COA_DBT;
   private double DR_AMNT;
   private String DR_CLSS;
   private String ERROR_MSG;
   
   public ApModDistributionRecordDetails() {
   }

   public String getDrCoaAccountNumber() {
   	
   	  return DR_COA_ACCNT_NMBR;
   	
   }
   
   public void setDrCoaAccountNumber(String DR_COA_ACCNT_NMBR) {
   	
   	  this.DR_COA_ACCNT_NMBR = DR_COA_ACCNT_NMBR;
   	
   }
   
   public String getDrCoaAccountDescription() {
   	
   	  return DR_COA_ACCNT_DESC;
   	
   }
   
   public void setDrCoaAccountDescription(String DR_COA_ACCNT_DESC) {
   	
   	  this.DR_COA_ACCNT_DESC = DR_COA_ACCNT_DESC;
   	
   }
   
   public byte getDrDebit()
   {
	  return this.DR_COA_DBT;
   }

   public void setDrDebit( byte DR_COA_DBT )
   {
	  this.DR_COA_DBT = DR_COA_DBT;

   }
   
   public double getDrAmount()
   {
	  return this.DR_AMNT;
   }

   public void setDrAmount( double DR_AMNT )
   {
	  this.DR_AMNT = DR_AMNT;


   }
   
   public java.lang.String getDrClass()
   {
	  return this.DR_CLSS;
   }

   public void setDrClass( java.lang.String DR_CLSS )
   {
	  this.DR_CLSS = DR_CLSS;

   }
   
    public java.lang.String getErrorMessage()
   {
      return this.ERROR_MSG;
   }

   public void setErrorMessage( java.lang.String ERROR_MSG )
   {
      this.ERROR_MSG = ERROR_MSG;

   }

} // ApModDistributionRecordDetails class