/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;
import java.util.Date;


public class ApRepWithholdingTaxExpandedDetails implements java.io.Serializable {

   private long WTE_SQNC_NMBR;
   private String WTE_SPL_TIN;
   private String WTE_SPL_SPPLR_NM;
   private String WTE_NTR_OF_INCM_PYMNT;
   private String WTE_ATC_CD;
   private double WTE_TX_BS;
   private double WTE_RT;
   private double WTE_TX_WTHHLD;
   private Date WTE_TXN_DT;

   public ApRepWithholdingTaxExpandedDetails() {
   }

	public long getWteSequenceNumber() {
		return (WTE_SQNC_NMBR); 
	}

	public void setWteSequenceNumber(long WTE_SQNC_NMBR) {
		this.WTE_SQNC_NMBR = WTE_SQNC_NMBR; 
	}

	public String getWteSplTinNumber() {
		return (WTE_SPL_TIN); 
	}

	public void setWteSplTinNumber(String WTE_SPL_TIN) {
		this.WTE_SPL_TIN = WTE_SPL_TIN; 
	}

	public String getWteSplSupplierName() {
		return (WTE_SPL_SPPLR_NM); 
	}

	public void setWteSplSupplierName(String WTE_SPL_SPPLR_NM) {
		this.WTE_SPL_SPPLR_NM = WTE_SPL_SPPLR_NM; 
	}

	public String getWteNatureOfPayment() {
		return (WTE_NTR_OF_INCM_PYMNT); 
	}

	public void setWteNatureOfIncomePayment(String WTE_NTR_OF_INCM_PYMNT) {
		this.WTE_NTR_OF_INCM_PYMNT = WTE_NTR_OF_INCM_PYMNT; 
	}

	public String getWteAtcCode() {
		return (WTE_ATC_CD); 
	}

	public void setWteAtcCode(String WTE_ATC_CD) {
		this.WTE_ATC_CD = WTE_ATC_CD; 
	}

	public double getWteTaxBase() {
		return (WTE_TX_BS); 
	}

	public void setWteTaxBase(double WTE_TX_BS) {
		this.WTE_TX_BS = WTE_TX_BS; 
	}

	public double getWteRate() {
		return (WTE_RT); 
	}

	public void setWteRate(double WTE_RT) {
		this.WTE_RT = WTE_RT; 
	}

	public double getWteTaxWithheld() {
		return (WTE_TX_WTHHLD); 
	}

	public void setWteTaxWithheld(double WTE_TX_WTHHLD) {
		this.WTE_TX_WTHHLD = WTE_TX_WTHHLD; 
	}
	
	public Date getWteTxnDate() {
		return (WTE_TXN_DT); 
	}

	public void setWteTxnDate(Date WTE_TXN_DT) {
		this.WTE_TXN_DT = WTE_TXN_DT; 
	}

	
} // ApRepWithholdingTaxExpandedDetails class