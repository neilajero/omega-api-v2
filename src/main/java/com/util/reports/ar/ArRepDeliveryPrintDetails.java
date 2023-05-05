/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

public class ArRepDeliveryPrintDetails implements java.io.Serializable, Cloneable {

  
   private String DP_REPORT_PARAMETER;
   
   private String DP_SO_CODE = null;
   private String DP_SO_DCMNT_NMBR = null;
   private String DP_SO_RFRNC_NMBR = null;
   private String DP_CST_CSTMR_CODE = null;
   private String DP_DV_CODE = null;
   private String DP_DV_CNTNR = null;
   private String DP_DV_DLVRY_NMBR = null;
   private String DP_DV_BKNG_TM = null;
   private String DP_DV_TBS_FEE_PLL_OUT = null;
   private String DP_DV_RLSD_DT = null;
   private String DP_DV_DLVRD_DT = null;
   private String DP_DV_DLVRD_TO = null;
   private String DP_DV_PLT_NO = null;
   private String DP_DV_DRVR_NM = null;
   private String DP_DV_EMPTY_RTRN_DT = null;
   private String DP_DV_EMPTY_RTRN_TO = null;
   private String DP_DV_TBS_FEE_RTRN = null;
   private String DP_DV_STTS = null;
   private String DP_DV_RMRKS = null;


   public ArRepDeliveryPrintDetails() {
   }

   
   public String getDpSoCode() {
	   return DP_SO_CODE;
   }
   
   public void setDpSoCode(String DP_SO_CODE) {
	   this.DP_SO_CODE = DP_SO_CODE;
   }
   
   
   public String getDpSoDocumentNumber() {
	   return DP_SO_DCMNT_NMBR;
   }
   
   public void setDpSoDocumentNumber(String DP_SO_DCMNT_NMBR) {
	   this.DP_SO_DCMNT_NMBR = DP_SO_DCMNT_NMBR;
   }
   
   public String getDpSoReferenceNumber() {
	   return DP_SO_RFRNC_NMBR;
   }
   
   public void setDpSoReferenceNumber(String DP_SO_RFRNC_NMBR) {
	   this.DP_SO_RFRNC_NMBR = DP_SO_RFRNC_NMBR;
   }
   
   public String getDpCstCustomerCode() {
	   return DP_CST_CSTMR_CODE;
   }
   
   public void setDpCstCustomerCode(String DP_CST_CSTMR_CODE) {
	   this.DP_CST_CSTMR_CODE = DP_CST_CSTMR_CODE;
   }
   
   public String getDpDvCode() {
	   return DP_DV_CODE;
   }
   
   public void setDpDvCode(String DP_DV_CODE) {
	   this.DP_DV_CODE = DP_DV_CODE;
   }
   
   public String getDpDvContainer() {
	   return DP_DV_CNTNR;
   }
   
   public void setDpDvContainer(String DP_DV_CNTNR) {
	   this.DP_DV_CNTNR = DP_DV_CNTNR;
   }
   
   public String getDpDvDeliveryNumber() {
	   return DP_DV_DLVRY_NMBR;
   }
   
   public void setDpDvDeliveryNumber(String DP_DV_DLVRY_NMBR) {
	   this.DP_DV_DLVRY_NMBR = DP_DV_DLVRY_NMBR;
   }
   
   public String getDpDvBookingTime() {
	   return DP_DV_BKNG_TM;
   }
   
   public void setDpDvBookingTime(String DP_DV_BKNG_TM) {
	   this.DP_DV_BKNG_TM = DP_DV_BKNG_TM;
   }
   
   
   public String getDpDvTabsFeePullOut() {
	   return DP_DV_TBS_FEE_PLL_OUT;
   }
   
   public void setDpDvTabsFeePullOut(String DP_DV_TBS_FEE_PLL_OUT) {
	   this.DP_DV_TBS_FEE_PLL_OUT = DP_DV_TBS_FEE_PLL_OUT;
   }
   
   public String getDpDvReleasedDate() {
	   return DP_DV_RLSD_DT;
   }
   
   public void setDpDvReleasedDate(String DP_DV_RLSD_DT) {
	   this.DP_DV_RLSD_DT = DP_DV_RLSD_DT;
   }
   
   public String getDpDvDeliveredDate() {
	   return DP_DV_DLVRD_DT;
   }
   
   public void setDpDvDeliveredDate(String DP_DV_DLVRD_DT) {
	   this.DP_DV_DLVRD_DT = DP_DV_DLVRD_DT;
   }
   
   public String getDpDvDeliveredTo() {
	   return DP_DV_DLVRD_TO;
   }
   
   public void setDpDvDeliveredTo(String DP_DV_DLVRD_TO) {
	   this.DP_DV_DLVRD_TO = DP_DV_DLVRD_TO;
   }
   
   public String getDpDvPlateNo() {
	   return DP_DV_PLT_NO;
   }
   
   public void setDpDvPlateNo(String DP_DV_PLT_NO) {
	   this.DP_DV_PLT_NO = DP_DV_PLT_NO;
   }
   
   public String getDpDvDriverName() {
	   return DP_DV_DRVR_NM;
   }
   
   public void setDpDvDriverName(String DP_DV_DRVR_NM) {
	   this.DP_DV_DRVR_NM = DP_DV_DRVR_NM;
   }
   
   public String getDpDvEmptyReturnDate() {
	   return DP_DV_EMPTY_RTRN_DT;
   }
   
   public void setDpDvEmptyReturnDate(String DP_DV_EMPTY_RTRN_DT) {
	   this.DP_DV_EMPTY_RTRN_DT = DP_DV_EMPTY_RTRN_DT;
   }
   
   public String getDpDvEmptyReturnTo() {
	   return DP_DV_EMPTY_RTRN_TO;
   }
   
   public void setDpDvEmptyReturnTo(String DP_DV_EMPTY_RTRN_TO) {
	   this.DP_DV_EMPTY_RTRN_TO = DP_DV_EMPTY_RTRN_TO;
   }
   
   public String getDpDvTabsFeeReturn() {
	   return DP_DV_TBS_FEE_RTRN;
   }
   
   public void setDpDvTabsFeeReturn(String DP_DV_TBS_FEE_RTRN) {
	   this.DP_DV_TBS_FEE_RTRN = DP_DV_TBS_FEE_RTRN;
   }
   
   public String getDpDvStatus() {
	   return DP_DV_STTS;
   }
   
   public void setDpDvStatus(String DP_DV_STTS) {
	   this.DP_DV_STTS = DP_DV_STTS;
   }
   
   public String getDpDvRemarks() {
	   return DP_DV_RMRKS;
   }
   
   public void setDpDvRemarks(String DP_DV_RMRKS) {
	   this.DP_DV_RMRKS = DP_DV_RMRKS;
   }
   
 
   public String getDpReportParameter(){

		return DP_REPORT_PARAMETER;
	}

	public void setDpReportParameter(String DP_REPORT_PARAMETER){
		this.DP_REPORT_PARAMETER = DP_REPORT_PARAMETER;
	}
   
   
   
   
   
	   public Object clone() throws CloneNotSupportedException {
	   	 
	
			try {
				
			   return super.clone();
			   
			 } catch (CloneNotSupportedException e) {
				  throw e;
			
			 }	
	   }
  
 }  // ArRepDeliveryPrintDetails