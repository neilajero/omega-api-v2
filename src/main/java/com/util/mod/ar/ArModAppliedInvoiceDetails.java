/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArAppliedInvoiceDetails;

import java.util.Date;

public class ArModAppliedInvoiceDetails extends ArAppliedInvoiceDetails implements java.io.Serializable {

   private Integer AI_IPS_CODE;
   private short AI_IPS_NMBR;
   private Date AI_IPS_DUE_DT;
   private Date AI_IPS_INV_DT;
   private double AI_IPS_AMNT_DUE;
   private double AI_IPS_PNLTY_DUE;
   private String AI_IPS_INV_NMBR;
   private String AI_IPS_INV_RFRNC_NMBR;
   private String AI_IPS_INV_FC_NM;
   private String AI_RCPT_NMBR;
   private int AI_AD_BRANCH;
   

   public ArModAppliedInvoiceDetails() {
   }

   public Integer getAiIpsCode() {
   	
       return AI_IPS_CODE;
   	
   }
   
   public void setAiIpsCode(Integer AI_IPS_CODE) {
   	
   	   this.AI_IPS_CODE = AI_IPS_CODE;
   	
   }
   
   public short getAiIpsNumber() {
   	
   	   return AI_IPS_NMBR;
   	
   }
   
   public void setAiIpsNumber(short AI_IPS_NMBR) {
   	
   	   this.AI_IPS_NMBR = AI_IPS_NMBR;
   	
   }
   
   public Date getAiIpsDueDate() {
   	
   	   return AI_IPS_DUE_DT;
   	
   }
   
   public void setAiIpsDueDate(Date AI_IPS_DUE_DT) {
   	
   	   this.AI_IPS_DUE_DT = AI_IPS_DUE_DT;
   	
   }
   
   public Date getAiIpsInvDate() {
	   	
   	   return AI_IPS_INV_DT;
   	
   }
   
   public void setAiIpsInvDate(Date AI_IPS_INV_DT) {
   	
   	   this.AI_IPS_INV_DT = AI_IPS_INV_DT;
   	
   }
   
   public double getAiIpsAmountDue() {
   	
   	   return AI_IPS_AMNT_DUE;
   	
   }
   
   public void setAiIpsAmountDue(double AI_IPS_AMNT_DUE) {
   	
   	   this.AI_IPS_AMNT_DUE = AI_IPS_AMNT_DUE;
   	
   }
   
   
   public double getAiIpsPenaltyDue() {
	   	
   	   return AI_IPS_PNLTY_DUE;
   	
   }
   
   public void setAiIpsPenaltyDue(double AI_IPS_PNLTY_DUE) {
   	
   	   this.AI_IPS_PNLTY_DUE = AI_IPS_PNLTY_DUE;
   	
   }
   
   public String getAiIpsInvNumber() {
   	
   	   return AI_IPS_INV_NMBR;
   	
   }
   
   public void setAiIpsInvNumber(String AI_IPS_INV_NMBR) {
   	
   	   this.AI_IPS_INV_NMBR = AI_IPS_INV_NMBR;
   	
   }
   
   public String getAiIpsInvReferenceNumber() {
   	
   	   return AI_IPS_INV_RFRNC_NMBR;
   	
   }
   
   public void setAiIpsInvReferenceNumber(String AI_IPS_INV_RFRNC_NMBR) {
   	
   	   this.AI_IPS_INV_RFRNC_NMBR = AI_IPS_INV_RFRNC_NMBR;
   	
   }
   
   public String getAiIpsInvFcName() {
   	
   	   return AI_IPS_INV_FC_NM;
   	
   }
   
   public void setAiIpsInvFcName(String AI_IPS_INV_FC_NM) {
   	
   	  this.AI_IPS_INV_FC_NM = AI_IPS_INV_FC_NM;
   	
   }
   
   public String getAiReceiptNumber() {
   	
   	  return AI_RCPT_NMBR;
   	
   }
   
   public void setAiReceiptNumber(String AI_RCPT_NMBR) {
   	
   	  this.AI_RCPT_NMBR = AI_RCPT_NMBR;
   	  
   }
   
   public int getAiAdBranch() {
	   	
   	  return AI_AD_BRANCH;
   	
   }
   
   public void setAiAdBranch(int AI_AD_BRANCH) {
   	
   	  this.AI_AD_BRANCH = AI_AD_BRANCH;
   	  
   }

   

} // ArModAppliedInvoiceDetails class