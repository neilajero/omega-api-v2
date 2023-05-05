/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Date;

public class ArRepDeliveryReceiptPrintDetails implements java.io.Serializable {

	private String DRP_INV_NMBR;
	private Date DRP_INV_DT;
	private String DRP_INV_CRTD_BY;
	private String DRP_INV_APPRVD_RJCTD_BY;
	private String DRP_INV_CSTMR_NM;
	private String DRP_INV_CSTMR_ADDRSS;
	private double DRP_INV_AMNT_DUE;
	private String DRP_INV_CLNT_PO;
	private double DRP_ILI_QTY;
	private String DRP_ILI_UOM;
	private String DRP_ILI_UOM_SHRT_NM;
	private String DRP_ILI_II_NM;
	private String DRP_ILI_II_DSCRPTN;
	private double DRP_ILI_UNT_PRC;
	private double DRP_ILI_AMNT;
	private double DRP_ILI_TX_AMNT;

	private String DRP_ILI_PRPRTY_CD;
	private String DRP_ILI_SRL_NMBR;
	private String DRP_ILI_SPCS;
	private String DRP_ILI_CSTDN;
	private String DRP_ILI_EXPRY_DT;

	private String DRP_INV_CSTMR_CD;
	private String DRP_SLP_SLSPRSN_CODE;
	private String DRP_SO_DCMNT_NMBR;
	private String DRP_INV_PYMNT_TRM;

	private double DRP_ILI_DSCNT1;
	private double DRP_ILI_DSCNT2;
	private double DRP_ILI_DSCNT3;
	private double DRP_ILI_DSCNT4;
	private double DRP_ILI_TTL_DSCNT;
	private String DRP_INV_CRTD_BY_DSCPRPTN;

	private String DRP_INV_BLLNG_HDR;
	private String DRP_INV_BLLNG_FTR;
	private String DRP_INV_BLLNG_HDR2;
	private String DRP_INV_BLLNG_FTR2;
	private String DRP_INV_BLLNG_HDR3;
	private String DRP_INV_BLLNG_FTR3;

	private String DRP_INV_RFRNC_NMBR;
	private String DRP_INV_SO_RFRNC_NMBR;
	private String DRP_INV_SHP_TO_ADDRSS;
	private String DRP_INV_DSCPRTN;
	
	private String REPORT_PARAMETERS;

	public ArRepDeliveryReceiptPrintDetails() {
    }

	public Date getDrpInvDate() {

		return DRP_INV_DT;

	}

	public void setDrpInvDate(Date DRP_INV_DT) {

		this.DRP_INV_DT = DRP_INV_DT;

	}

	public String getDrpInvCreatedBy() {

		return DRP_INV_CRTD_BY;

	}

	public void setDrpInvCreatedBy(String DRP_INV_CRTD_BY) {

		this.DRP_INV_CRTD_BY = DRP_INV_CRTD_BY;

	}

	public String getDrpInvApprovedRejectedBy() {

		return DRP_INV_APPRVD_RJCTD_BY;

	}

	public void setDrpInvApprovedRejectedBy(String DRP_INV_APPRVD_RJCTD_BY) {

		this.DRP_INV_APPRVD_RJCTD_BY = DRP_INV_APPRVD_RJCTD_BY;

	}

	public double getDrpInvAmountDue() {

		return DRP_INV_AMNT_DUE;

	}

	public void setDrpInvAmountDue(double DRP_INV_AMNT_DUE) {

		this.DRP_INV_AMNT_DUE = DRP_INV_AMNT_DUE;

	}

	public double getDrpIliQuantity() {

		return DRP_ILI_QTY;

	}

	public void setDrpIliQuantity(double DRP_ILI_QTY) {

		this.DRP_ILI_QTY = DRP_ILI_QTY;

	}

	public String getDrpIliUom() {

		return DRP_ILI_UOM;

	}

	public void setDrpIliUom(String DRP_ILI_UOM) {

		this.DRP_ILI_UOM = DRP_ILI_UOM;

	}

	public String getDrpIliUomShortName() {

		return DRP_ILI_UOM_SHRT_NM;

	}

	public void setDrpIliUomShortName(String DRP_ILI_UOM_SHRT_NM) {

		this.DRP_ILI_UOM_SHRT_NM = DRP_ILI_UOM_SHRT_NM;

	}

	public String getDrIliIiName() {

		return DRP_ILI_II_NM;

	}

	public void setDrpIliIiName(String DRP_ILI_II_NM) {

		this.DRP_ILI_II_NM = DRP_ILI_II_NM;

	}

	public String getDrpIliDescription() {

		return DRP_ILI_II_DSCRPTN;

	}

	public void setDrpIliDescription(String DRP_ILI_II_DSCRPTN) {

		this.DRP_ILI_II_DSCRPTN = DRP_ILI_II_DSCRPTN;

	}

	public double getDrpIliUnitPrice() {

		return DRP_ILI_UNT_PRC;

	}

	public void setDrpIliUnitPrice(double DRP_ILI_UNT_PRC) {

		this.DRP_ILI_UNT_PRC = DRP_ILI_UNT_PRC;

	}

	public double getDrpIliAmount() {

		return DRP_ILI_AMNT;

	}

	public void setDrpIliAmount(double DRP_ILI_AMNT) {

		this.DRP_ILI_AMNT = DRP_ILI_AMNT;

	}

	public double getDrpIliTaxAmount() {

		return DRP_ILI_TX_AMNT;

	}

	public void setDrpIliTaxAmount(double DRP_ILI_TX_AMNT) {

		this.DRP_ILI_TX_AMNT = DRP_ILI_TX_AMNT;

	}


	public String getDrpIliPropertyCode() {

		return DRP_ILI_PRPRTY_CD;

	}

	public void setDrpIliPropertyCode(String DRP_ILI_PRPRTY_CD) {

		this.DRP_ILI_PRPRTY_CD = DRP_ILI_PRPRTY_CD;

	}


	public String getDrpIliSerialNumber() {

		return DRP_ILI_SRL_NMBR;

	}

	public void setDrpIliSerialNumber(String DRP_ILI_SRL_NMBR) {

		this.DRP_ILI_SRL_NMBR = DRP_ILI_SRL_NMBR;

	}


	public String getDrpIliSpecs() {

		return DRP_ILI_SPCS;

	}

	public void setDrpIliSpecs(String DRP_ILI_SPCS) {

		this.DRP_ILI_SPCS = DRP_ILI_SPCS;

	}


	public String getDrpIliCustodian() {

		return DRP_ILI_CSTDN;

	}

	public void setDrpIliCustodian(String DRP_ILI_CSTDN) {

		this.DRP_ILI_CSTDN = DRP_ILI_CSTDN;

	}

	public String getDrpIliExpiryDate() {

		return DRP_ILI_EXPRY_DT;

	}

	public void setDrpIliExpiryDate(String DRP_ILI_EXPRY_DT) {

		this.DRP_ILI_EXPRY_DT = DRP_ILI_EXPRY_DT;

	}

	public String getDrpInvNumber() {

		return DRP_INV_NMBR;

	}

	public void setDrpInvNumber(String DRP_INV_NMBR) {

		this.DRP_INV_NMBR = DRP_INV_NMBR;

	}

	public String getDrpInvCustomerName() {

		return DRP_INV_CSTMR_NM;

	}

	public void setDrpInvCustomerName(String DRP_INV_CSTMR_NM) {

		this.DRP_INV_CSTMR_NM = DRP_INV_CSTMR_NM;

	}

	public String getDrpInvCustomerAddress() {

		return DRP_INV_CSTMR_ADDRSS;

	}

	public void setDrpInvCustomerAddress(String DRP_INV_CSTMR_ADDRSS) {

		this.DRP_INV_CSTMR_ADDRSS = DRP_INV_CSTMR_ADDRSS;

	}

	public String getDrpInvCustomerCode() {

		return DRP_INV_CSTMR_CD;

	}

	public void setDrpInvCustomerCode(String DRP_INV_CSTMR_CD) {

		this.DRP_INV_CSTMR_CD = DRP_INV_CSTMR_CD;

	}

	public String getDrpSlpSalespersonCode() {

		return DRP_SLP_SLSPRSN_CODE;

	}

	public void setDrpSlpSalespersonCode(String DRP_SLP_SLSPRSN_CODE) {

		this.DRP_SLP_SLSPRSN_CODE = DRP_SLP_SLSPRSN_CODE;

	}

	public String getDrpSoDocumentNumber() {

		return DRP_SO_DCMNT_NMBR;

	}

	public void setDrpSoDocumentNumber(String DRP_SO_DCMNT_NMBR) {

		this.DRP_SO_DCMNT_NMBR = DRP_SO_DCMNT_NMBR;

	}

	public String getDrpInvPaymentTerm() {

		return DRP_INV_PYMNT_TRM;

	}

	public void setDrpInvPaymentTerm(String DRP_INV_PYMNT_TRM) {

		this.DRP_INV_PYMNT_TRM = DRP_INV_PYMNT_TRM;

	}

	public double getDrpIliDiscount1() {

		return DRP_ILI_DSCNT1;

	}

	public void setDrpIliDiscount1(double DRP_ILI_DSCNT1) {

		this.DRP_ILI_DSCNT1 = DRP_ILI_DSCNT1;

	}

	public double getDrpIliDiscount2() {

		return DRP_ILI_DSCNT2;

	}

	public void setDrpIliDiscount2(double DRP_ILI_DSCNT2) {

		this.DRP_ILI_DSCNT2 = DRP_ILI_DSCNT2;

	}

	public double getDrpIliDiscount3() {

		return DRP_ILI_DSCNT3;

	}

	public void setDrpIliDiscount3(double DRP_ILI_DSCNT3) {

		this.DRP_ILI_DSCNT3 = DRP_ILI_DSCNT3;

	}

	public double getDrpIliDiscount4() {

		return DRP_ILI_DSCNT4;

	}

	public void setDrpIliDiscount4(double DRP_ILI_DSCNT4) {

		this.DRP_ILI_DSCNT4 = DRP_ILI_DSCNT4;

	}

	public double getDrpIliTotalDiscount() {

		return DRP_ILI_TTL_DSCNT;

	}

	public void setDrpIliTotalDiscount(double DRP_ILI_TTL_DSCNT) {

		this.DRP_ILI_TTL_DSCNT = DRP_ILI_TTL_DSCNT;

	}

	public String getDrpInvCreatedByDescription() {

		return DRP_INV_CRTD_BY_DSCPRPTN;

	}

	public void setDrpInvCreatedByDescription(String DRP_INV_CRTD_BY_DSCPRPTN) {

		this.DRP_INV_CRTD_BY_DSCPRPTN = DRP_INV_CRTD_BY_DSCPRPTN;

	}

	public String getDrpInvBillingHeader() {

		return DRP_INV_BLLNG_HDR;

	}

	public void setDrpInvBillingHeader(String DRP_INV_BLLNG_HDR) {

		this.DRP_INV_BLLNG_HDR = DRP_INV_BLLNG_HDR;

	}

	public String getDrpInvBillingFooter() {

		return DRP_INV_BLLNG_FTR;

	}

	public void setDrpInvBillingFooter(String DRP_INV_BLLNG_FTR) {

		this.DRP_INV_BLLNG_FTR = DRP_INV_BLLNG_FTR;

	}

	public String getDrpInvBillingHeader2() {

		return DRP_INV_BLLNG_HDR2;

	}

	public void setDrpInvBillingHeader2(String DRP_INV_BLLNG_HDR2) {

		this.DRP_INV_BLLNG_HDR2 = DRP_INV_BLLNG_HDR2;

	}

	public String getDrpInvBillingFooter2() {

		return DRP_INV_BLLNG_FTR2;

	}

	public void setDrpInvBillingFooter2(String DRP_INV_BLLNG_FTR2) {

		this.DRP_INV_BLLNG_FTR2 = DRP_INV_BLLNG_FTR2;

	}

	public String getDrpInvBillingHeader3() {

		return DRP_INV_BLLNG_HDR3;

	}

	public void setDrpInvBillingHeader3(String DRP_INV_BLLNG_HDR3) {

		this.DRP_INV_BLLNG_HDR3 = DRP_INV_BLLNG_HDR3;

	}

	public String getDrpInvBillingFooter3() {

		return DRP_INV_BLLNG_FTR3;

	}

	public void setDrpInvBillingFooter3(String DRP_INV_BLLNG_FTR3) {

		this.DRP_INV_BLLNG_FTR3 = DRP_INV_BLLNG_FTR3;

	}

	public String getDrpInvReferenceNumber() {

		return DRP_INV_RFRNC_NMBR;
	}

	public void setDrpInvReferenceNumber(String DRP_INV_RFRNC_NMBR){

		this.DRP_INV_RFRNC_NMBR = DRP_INV_RFRNC_NMBR;
	}

	public String getDrpInvClientPo(){


		return DRP_INV_CLNT_PO;

	}

	public void setDrpInvClientPo(String DRP_INV_CLNT_PO) {

		this.DRP_INV_CLNT_PO = DRP_INV_CLNT_PO;

	}


	public String getDrpInvSoReferenceNumber(){

		return DRP_INV_SO_RFRNC_NMBR;
	}

	public void SetDrpInvSoReferenceNumber(String DRP_INV_SO_RFRNC_NMBR){

		this.DRP_INV_SO_RFRNC_NMBR = DRP_INV_SO_RFRNC_NMBR;
	}

	public String getDrpInvShipToAddress(){

		return DRP_INV_SHP_TO_ADDRSS;
	}

	public void setDrpInvShipToAddress(String DRP_INV_SHP_TO_ADDRSS){

		this.DRP_INV_SHP_TO_ADDRSS = DRP_INV_SHP_TO_ADDRSS;
	}
   public String getDrpInvDescription(){

		return DRP_INV_DSCPRTN;
	}

	public void setDrpInvInvDescription(String DRP_INV_DSCPRTN){

		this.DRP_INV_DSCPRTN = DRP_INV_DSCPRTN;
	}


	public String getReportParameters(){

		return REPORT_PARAMETERS;
	}

	public void setReportParameters(String REPORT_PARAMETERS){

		this.REPORT_PARAMETERS = REPORT_PARAMETERS;
	}

}  // ArRepDeliveryReceiptPrintDetails