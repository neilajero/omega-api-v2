/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;


public class GlRepIncomeTaxReturnDetails implements java.io.Serializable {

   private double GRSS_SLS_RCPT;
   private double TX_DUE;
   private double ON_TXBL_GDS;
   private double OUTPUT_TX;
   private double INPUT_TX;
   private double SLS_AMNT;
   private double RVNE;
   private double CST_OF_SLS;
   private double OTHR_TXBL_INCM;
   private double SLS_AMNT_EXMPT;
   private double SLS_AMNT_ZR_RTD;
   private double TTL_SLS_EXMPT_ZR;
   private double REV_AMNT_EXMPT;
   private double REV_AMNT_ZR_RTD;
   private double TTL_REV_EXMPT_ZR;
   private String COA_ACCNT_NMBR;
   private String DCMNT_TYP;
   private java.util.Date TXN_DT;



   public GlRepIncomeTaxReturnDetails() {
   }

   public GlRepIncomeTaxReturnDetails(double GRSS_SLS_RCPT,
       double TX_DUE, double ON_TXBL_GDS, double OUTPUT_TX, double INPUT_TX, double SLS_AMNT, double RVNE, double CST_OF_SLS, double OTHR_TXBL_INCM,  double SLS_AMNT_EXMPT , double SLS_AMNT_ZR_RTD , double TTL_SLS_EXMPT_ZR, double REV_AMNT_EXMPT, double REV_AMNT_ZR_RTD, double TTL_REV_EXMPT_ZR) {

       this.GRSS_SLS_RCPT = GRSS_SLS_RCPT;
       this.TX_DUE = TX_DUE;
       this.ON_TXBL_GDS = ON_TXBL_GDS;
       this.OUTPUT_TX = OUTPUT_TX;
       this.INPUT_TX = INPUT_TX;
       this.SLS_AMNT = SLS_AMNT;
       this.RVNE = RVNE;
       this.CST_OF_SLS = CST_OF_SLS;
       this.OTHR_TXBL_INCM = OTHR_TXBL_INCM;
       this.SLS_AMNT_EXMPT = SLS_AMNT_EXMPT;
       this.SLS_AMNT_ZR_RTD = SLS_AMNT_ZR_RTD;
       this.TTL_SLS_EXMPT_ZR = TTL_SLS_EXMPT_ZR;
       this.REV_AMNT_EXMPT = REV_AMNT_EXMPT;
       this.REV_AMNT_ZR_RTD = REV_AMNT_ZR_RTD;
       this.TTL_REV_EXMPT_ZR = TTL_REV_EXMPT_ZR;


	}

    public double getGrossSalesReceipt() {

    	return GRSS_SLS_RCPT;

    }

    public void setGrossSalesReceipt(double GRSS_SLS_RCPT) {

    	this.GRSS_SLS_RCPT = GRSS_SLS_RCPT;

    }

    public double getTaxDue() {

    	return TX_DUE;

    }

    public void setTaxDue(double TX_DUE) {

    	this.TX_DUE = TX_DUE;

    }

    public double getOnTaxableGoods() {

    	return ON_TXBL_GDS;

    }

    public void setOnTaxableGoods(double ON_TXBL_GDS) {

    	this.ON_TXBL_GDS = ON_TXBL_GDS;

    }

    public double getOutputTax() {

    	return OUTPUT_TX;

    }

    public void setOutputTax(double OUTPUT_TX) {

    	this.OUTPUT_TX = OUTPUT_TX;

    }

    public double getInputTax() {

    	return INPUT_TX;

    }

    public void setInputTax(double INPUT_TX) {

    	this.INPUT_TX = INPUT_TX;

    }

    public double getSalesAmount() {

    	return SLS_AMNT;

    }

    public void setSalesAmount(double SLS_AMNT) {

    	this.SLS_AMNT = SLS_AMNT;

    }

    public double getRevenue() {

    	return RVNE;

    }

    public void setRevenue(double RVNE) {

    	this.RVNE = RVNE;

    }

    public double getOtherTaxableIncome() {

    	return OTHR_TXBL_INCM;

    }

    public void setOtherTaxableIncome(double OTHR_TXBL_INCM) {

    	this.OTHR_TXBL_INCM = OTHR_TXBL_INCM;

    }


    public double getCostOfSales() {

    	return CST_OF_SLS;

    }

    public void setCostOfSales(double CST_OF_SLS) {

    	this.CST_OF_SLS = CST_OF_SLS;

    }

    public double getSalesAmountExempt() {

    	return SLS_AMNT_EXMPT;

    }

    public void setSalesAmountExempt(double SLS_AMNT_EXMPT) {

    	this.SLS_AMNT_EXMPT = SLS_AMNT_EXMPT;

    }


    public double getSaleAmountZeroRated() {

    	return SLS_AMNT_ZR_RTD;

    }

    public void setSaleAmountZeroRated(double SLS_AMNT_ZR_RTD) {

    	this.SLS_AMNT_ZR_RTD = SLS_AMNT_ZR_RTD;

    }

    public double getTotalSalesExemptZero() {

    	return TTL_SLS_EXMPT_ZR;

    }

    public void setTotalSalesExemptZero(double TTL_SLS_EXMPT_ZR) {

    	this.TTL_SLS_EXMPT_ZR = TTL_SLS_EXMPT_ZR;

    }


    public double getRevenueAmountExempt() {

    	return REV_AMNT_EXMPT;

    }

    public void setRevenueAmountExempt(double REV_AMNT_EXMPT) {

    	this.REV_AMNT_EXMPT = REV_AMNT_EXMPT;

    }




    public double getRevenueAmountZeroRated() {

    	return REV_AMNT_ZR_RTD;

    }

    public void setRevenueAmountZeroRated(double REV_AMNT_ZR_RTD) {

    	this.REV_AMNT_ZR_RTD = REV_AMNT_ZR_RTD;

    }




    public double getTotalRevenueExemptZero() {

    	return TTL_REV_EXMPT_ZR;

    }

    public void setTotalRevenueExemptZero(double TTL_REV_EXMPT_ZR) {

    	this.TTL_REV_EXMPT_ZR = TTL_REV_EXMPT_ZR;

    }


    public String getCoaAccountNumber() {
    	return COA_ACCNT_NMBR;

    }

    public void setCoaAccountNumber(String COA_ACCNT_NMBR) {
    	this.COA_ACCNT_NMBR = COA_ACCNT_NMBR;
    }

    public String getDocumentType() {
    	return DCMNT_TYP;
    }

    public void setDocumentType(String DCMNT_TYP) {
    	this.DCMNT_TYP = DCMNT_TYP;
    }

    public java.util.Date getTransactionDate() {
    	return TXN_DT;
    }

    public void setTransactionDate(java.util.Date TXN_DT) {
    	this.TXN_DT = TXN_DT;
    }


} // GlRepIncomeTaxReturnDetails class