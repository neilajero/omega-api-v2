/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;

import com.util.ad.AdApprovalQueueDetails;

import java.util.Date;

public class AdModApprovalQueueDetails extends AdApprovalQueueDetails implements java.io.Serializable {
   
   private Date AQ_DT;
   private String AQ_SPPLR_CODE;
   private String AQ_CSTMR_CODE;
   private String AQ_JR_NM;
   private String AQ_DCMNT_NMBR;
   private String AQ_DESC;
   private String AQ_AD_BRNCH_CD;
   private double AQ_AMNT;
   private String AQ_DCMNT_TYP;
   private String AQ_RFRNC_NMBR;
   private String AQ_TYP;
   private String AQ_CSTMR_NM;
   private String AQ_SPPLR_NM;
   private String AQ_APPRVR_NM;
   private String AQ_STS;
   private Integer AQ_BR_CODE;

   public AdModApprovalQueueDetails() {
   }

   public Date getAqDate() {
   	
   	   return AQ_DT;
   	
   }  
   
   public void setAqDate(Date AQ_DT) {
   	
   	  this.AQ_DT = AQ_DT;
   	
   }
   
   public String getAqSupplierCode() {
   	
   	   return AQ_SPPLR_CODE;
   	
   } 
   
   public void setAqSupplierCode(String AQ_SPPLR_CODE) {
   	
   	  this.AQ_SPPLR_CODE = AQ_SPPLR_CODE;
   	
   }
   
   public String getAqCustomerCode() {
   	
   	   return AQ_CSTMR_CODE;
   	
   } 
   
   public void setAqCustomerCode(String AQ_CSTMR_CODE) {
   	
   	  this.AQ_CSTMR_CODE = AQ_CSTMR_CODE;
   	
   }
   
   public String getAqJrName() {
   	
   	  return AQ_JR_NM;
   	
   }
   
   public void setAqJrName(String AQ_JR_NM) {
   	
   	  this.AQ_JR_NM = AQ_JR_NM;
   	
   }
   
   public String getAqDocumentNumber() {
   	
   	   return AQ_DCMNT_NMBR;
   	
   }  
   
   public void setAqDocumentNumber(String AQ_DCMNT_NMBR) {
   	
   	  this.AQ_DCMNT_NMBR = AQ_DCMNT_NMBR;
   	
   }
   
    public String getAqDescription() {
    
       return AQ_DESC;
    
   }  
   
   public void setAqDescription(String AQ_DESC) {
    
      this.AQ_DESC = AQ_DESC;
    
   }
   
   
   

   public String getAqAdBranchCode() {
	   	
   	   return AQ_AD_BRNCH_CD;
   	
   }  
   
   public void setAqAdBranchCode(String AQ_AD_BRNCH_CD) {
   	
   	  this.AQ_AD_BRNCH_CD = AQ_AD_BRNCH_CD;
   	
   }
   
   public double getAqAmount() {
   	
   	   return AQ_AMNT;
   	
   }  
   
   public void setAqAmount(double AQ_AMNT) {
   	
   	  this.AQ_AMNT = AQ_AMNT;
   	
   }
   
   public String getAqDocumentType() {
   	
   	  return AQ_DCMNT_TYP;
   	
   }
   
    public void setAqDocumentType(String AQ_DCMNT_TYP) {
   	
   	  this.AQ_DCMNT_TYP = AQ_DCMNT_TYP;
   	
   }
   
   public String getAqReferenceNumber() {
   	
   	  return AQ_RFRNC_NMBR;
   	  
   }
   
   public void setAqReferenceNumber(String AQ_RFRNC_NMBR) {
   	
   	  this.AQ_RFRNC_NMBR = AQ_RFRNC_NMBR;
   	
   }
   
   public String getAqType() {
   	
   	  return AQ_TYP;
   	  
   }
   
   public void setAqType(String AQ_TYP) {
   	
   	  this.AQ_TYP = AQ_TYP;
   	  
   }
   
   public String getAqCustomerName() {
	   	
   	   return AQ_CSTMR_NM;
   	
   } 
   
   public void setAqCustomerName(String AQ_CSTMR_NM) {
   	
   	  this.AQ_CSTMR_NM = AQ_CSTMR_NM;
   	
   }
   
   public String getAqSupplierName() {
	   	
   	   return AQ_SPPLR_NM;
   	
   } 
   
   public void setAqSupplierName(String AQ_SPPLR_NM) {
   	
   	  this.AQ_SPPLR_NM = AQ_SPPLR_NM;
   	
   }
   
   public String getAqApproverName() {
	   	
   	   return AQ_APPRVR_NM;
   	
   } 
   
   public void setAqApproverName(String AQ_APPRVR_NM) {
   	
   	  this.AQ_APPRVR_NM = AQ_APPRVR_NM;
   	
   }
   
   
   public String getAqStatus() {
	   	
   	   return AQ_STS;
   	
   } 
   
   public void setAqStatus(String AQ_STS) {
   	
   	  this.AQ_STS = AQ_STS;
   	
   }
   
   public Integer getAqBrCode() {
	   
	   return AQ_BR_CODE;
   }
   
   public void setAqBrCode(Integer AQ_BR_CODE) {
	   this.AQ_BR_CODE = AQ_BR_CODE;
   }

} // AdModApprovalQueueDetails class