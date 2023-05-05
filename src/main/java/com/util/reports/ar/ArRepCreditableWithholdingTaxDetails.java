/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;


public class ArRepCreditableWithholdingTaxDetails implements java.io.Serializable {

   private long CWT_SQNC_NMBR;
   private String CWT_CST_TIN;
   private String CWT_CST_CSTMR_NM;
   private String CWT_NTR_OF_INCM_PYMNT;
   private String CWT_ATC_CD;
   private String CWT_INVC_NMBR;
   private double CWT_TX_BS;
   private double CWT_RT;
   private double CWT_TX_WTHHLD;

   public ArRepCreditableWithholdingTaxDetails() {
   }

	public long getCwtSequenceNumber() {
		return (CWT_SQNC_NMBR); 
	}

	public void setCwtSequenceNumber(long CWT_SQNC_NMBR) {
		this.CWT_SQNC_NMBR = CWT_SQNC_NMBR; 
	}

	public String getCwtCstTinNumber() {
		return (CWT_CST_TIN); 
	}

	public void setCwtCstTinNumber(String CWT_CST_TIN) {
		this.CWT_CST_TIN = CWT_CST_TIN; 
	}

	public String getCwtCstCustomerName() {
		return (CWT_CST_CSTMR_NM); 
	}

	public void setCwtCstCustomerName(String CWT_CST_CSTMR_NM) {
		this.CWT_CST_CSTMR_NM = CWT_CST_CSTMR_NM; 
	}

	public String getCwtNatureOfPayment() {
		return (CWT_NTR_OF_INCM_PYMNT); 
	}

	public void setCwtNatureOfIncomePayment(String CWT_NTR_OF_INCM_PYMNT) {
		this.CWT_NTR_OF_INCM_PYMNT = CWT_NTR_OF_INCM_PYMNT; 
	}

	public String getCwtAtcCode() {
		return (CWT_ATC_CD); 
	}

	public void setCwtAtcCode(String CWT_ATC_CD) {
		this.CWT_ATC_CD = CWT_ATC_CD; 
	}
    
    public String getCwtInvoiceNumber() {
		return (CWT_INVC_NMBR); 
	}

	public void setCwtInvoiceNumber(String CWT_INVC_NMBR) {
		this.CWT_INVC_NMBR = CWT_INVC_NMBR; 
	}
    
	public double getCwtTaxBase() {
		return (CWT_TX_BS); 
	}

	public void setCwtTaxBase(double CWT_TX_BS) {
		this.CWT_TX_BS = CWT_TX_BS; 
	}

	public double getCwtRate() {
		return (CWT_RT); 
	}

	public void setCwtRate(double CWT_RT) {
		this.CWT_RT = CWT_RT; 
	}

	public double getCwtTaxWithheld() {
		return (CWT_TX_WTHHLD); 
	}

	public void setCwtTaxWithheld(double CWT_TX_WTHHLD) {
		this.CWT_TX_WTHHLD = CWT_TX_WTHHLD; 
	}
   
} // ArRepCreditableWithholdingTaxDetails class