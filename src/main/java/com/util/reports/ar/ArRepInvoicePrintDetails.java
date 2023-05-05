/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import com.util.Debug;

import java.util.Date;

public class ArRepInvoicePrintDetails implements java.io.Serializable , Cloneable{

	private String IP_INV_TYP;
	private String IP_INV_DESC;
	private String IP_INV_NMBR;
	private String IP_INV_BTCH;
	private double IP_INV_AMNT;
	private double IP_INV_AMNT_UNEARND_INT;
	private String IP_INV_CM_NMBR;
	private String IP_INV_CM_RFRNC_NMBR;
	private Date IP_INV_DT;
	private String IP_INV_CST_NM;
	private String IP_INV_CST_TIN;
	private String IP_INV_CST_ADDRSS;
	private String IP_INV_CST_CTY;
	private String IP_INV_CST_CNTRY;

	private String IP_INV_CST_CNTCT_PRSN;
	private String IP_INV_CST_PHN_NMBR;
	private String IP_INV_CST_MBL_NMBR;
	private String IP_INV_CST_FX;
	private String IP_INV_CST_EML;

	private String IP_IL_DESC;

	private String IP_IL_ITM_PRPRTY_CD;
	private String IP_IL_ITM_SRL_NMBR;
	private String IP_IL_ITM_SPCS;
	private String IP_IL_ITM_CSTDN;
	private String IP_IL_ITM_EXPRY_DT;
	
	private String IP_JA_LN;
	private String IP_JA_RMRKS;
	private double IP_JA_QTY;
	private double IP_JA_UNT_CST;
	private double IP_JA_AMNT;
	private byte IP_JA_SO;
	private String IP_JA_PE_ID_NMBR;
	private String IP_JA_PE_NM;
	
	private byte IP_II_JB_SRVCS;
	
	

	private double IP_IL_QNTTY;
	private double IP_IL_AMNT;
	private String IP_IL_IS_TX;
	private double IP_IL_UNT_PRC;
	private double IP_IL_UNT_PRC_WO_VAT;
	private String IP_INV_CRTD_BY;
	private String IP_INV_APPRVD_RJCTD_BY;
	private byte IP_DR_DBT;
	private double IP_DR_AMNT;

	private String IP_DR_COA_ACCNT_NMBR;
	private String IP_DR_COA_ACCNT_DESC;
	private Date IP_INV_DUE_DT;
	private Date IP_INV_EFFCTVTY_DT;
	private String IP_INV_RFRNC_NMBR;
	private String IP_INV_CRRNCY;
	private char IP_INV_CRRNCY_SYMBOL;
	private String IP_INV_CRRNCY_DESC;
	private String IP_INV_AMNT_IN_WRDS;
	private byte IP_SHW_DPLCT;
	private String IP_INV_BRNCH_CDE;
	private String IP_INV_BRNCH_NM;
	private String IP_INV_CST_DL_PRC;

	// added fields
	private String IP_INV_BLLNG_HDR;
	private String IP_INV_BLLNG_FTR;
	private String IP_INV_BLLNG_HDR2;
	private String IP_INV_BLLNG_FTR2;
	private String IP_INV_BLLNG_HDR3;
	private String IP_INV_BLLNG_FTR3;
	private String IP_INV_BLLNG_SGNTRY;
	private String IP_INV_SGNTRY_TTL;
	private String IP_INV_BLL_TO_ADDRSS;
	private String IP_INV_BLL_TO_CNTCT;
	private String IP_INV_BLL_TO_ALT_CNTCT;
	private String IP_INV_BLL_TO_PHN;
	private String IP_INV_SHP_TO_ADDRSS;
	private String IP_INV_SHP_TO_CNTCT;
	private String IP_INV_SHP_TO_ALT_CNTCT;
	private String IP_INV_SHP_TO_PHN;
	private String IP_INV_SHP_TO;

	private double IP_INV_TTL_TX;
	private double IP_IL_AMNT_WO_VAT;

	private double IP_INV_DSCNT_AMNT;
	private String IP_INV_DSCNT_DESC;
	private String IP_INV_IL_SML_UNT_OF_MSR;
	private String IP_INV_PYT_NM;
	private String IP_INV_PYT_DESC;
	private String IP_IL_CTGRY;
	private String IP_INV_CST_CSTMR_CD;
	private String IP_IL_NM;
	private String IP_IL_UOM_SHRT_NM;
	private String IP_SLP_SLSPRSN_CODE;
	private String IP_SLP_NM;

	private String IP_INV_CST_DESC;
	private String IP_INV_SO_NMBR;
	private String IP_INV_JO_NMBR;
	private String IP_INV_CM_INVC_NMBR;
	private String IP_INV_CM_INVC_DT;

	private double IP_ILI_DSCNT1;
	private double IP_ILI_DSCNT2;
	private double IP_ILI_DSCNT3;
	private double IP_ILI_DSCNT4;
	private double IP_INV_TC_RT;

	private String IP_ILI_CLNT_PO;
	private String IP_ILI_SLS_PRSN;
	
	private String IP_JOL_TTL_HRS;
	

	private String IP_INV_PRT_NMBR;

    //	 added fields for payment schedule data source
	private Date IP_IPS_DUE_DT;
	private double IP_IPS_AMNT_DUE;

	private String IP_INV_TX_CD;
	private String IP_INV_WTHHLDNG_TX_CD;
	private double IP_INV_WTHHLDNG_TX_MNT;

	private double IP_ILI_TTL_DSCNT;

	private String IP_INV_CRTD_BY_DSC;
	private String IP_INV_CHCK_BY_DESC;
	private String IP_INV_APPRVD_RJCTD_BY_DESC;
	private String IP_INV_SO_RFRNC_NMBR;

	private String IP_INV_APPRVL_STATUS;
	private boolean IP_INV_IS_DRFT;
	private String IP_INV_CRTD_BY_DESC;
	private String IP_REPORT_PARAMETER;
	private String IP_INV_APPRVD_BY;
	
	

	public ArRepInvoicePrintDetails () {
    }

	
	 public Object clone() throws CloneNotSupportedException {
     	 

 		try {
 		   return super.clone();
 		 }
 		  catch (CloneNotSupportedException e) {
 			  throw e;
 		
 		  }	
 	 }
	
	public String getIpInvType() {

		return IP_INV_TYP;

	}

	public void setIpInvType(String IP_INV_TYP) {

		this.IP_INV_TYP = IP_INV_TYP;

	}
	
	
	
	public String getIpInvPartNumber() {

		return IP_INV_PRT_NMBR;

	}

	public void setIpInvPartNumber(String IP_INV_PRT_NMBR) {

		this.IP_INV_PRT_NMBR = IP_INV_PRT_NMBR;

	}

	public String getIpInvDescription() {

		return IP_INV_DESC;

	}

	public void setIpInvDescription(String IP_INV_DESC) {

		this.IP_INV_DESC = IP_INV_DESC;

	}

	public double getIpInvAmount() {

		return IP_INV_AMNT;

	}

	public void setIpInvAmount(double IP_INV_AMNT) {

		this.IP_INV_AMNT = IP_INV_AMNT;

	}

public double getIpInvAmountUnearnedInterest() {

		return IP_INV_AMNT_UNEARND_INT;

	}

	public void setIpInvAmountUnearnedInterest(double IP_INV_AMNT_UNEARND_INT) {

		this.IP_INV_AMNT_UNEARND_INT = IP_INV_AMNT_UNEARND_INT;

	}
	public String getIpInvNumber() {

		return IP_INV_NMBR;

	}

	public void setIpInvNumber(String IP_INV_NMBR) {

		this.IP_INV_NMBR = IP_INV_NMBR;

	}

public String getIpInvBatch() {

		return IP_INV_BTCH;

	}

	public void setIpInvBatch(String IP_INV_BTCH) {

		this.IP_INV_BTCH = IP_INV_BTCH;

	}

	public String getIpInvCmNumber() {

		return IP_INV_CM_NMBR;

	}

	public void setIpInvCmNumber(String IP_INV_CM_NMBR) {

		this.IP_INV_CM_NMBR = IP_INV_CM_NMBR;

	}


	public String getIpInvCmReferenceNumber() {

		return IP_INV_CM_RFRNC_NMBR;

	}

	public void setIpInvCmReferenceNumber(String IP_INV_CM_RFRNC_NMBR) {

		this.IP_INV_CM_RFRNC_NMBR = IP_INV_CM_RFRNC_NMBR;

	}

	public Date getIpInvDate() {

		return IP_INV_DT;

	}

	public void setIpInvDate(Date IP_INV_DT) {

		this.IP_INV_DT = IP_INV_DT;

	}

	public String getIpInvCustomerName() {

		return IP_INV_CST_NM;

	}

	public void setIpInvCustomerName(String IP_INV_CST_NM) {

		this.IP_INV_CST_NM = IP_INV_CST_NM;

	}

	public String getIpInvCustomerTin() {

		return IP_INV_CST_TIN;

	}

	public void setIpInvCustomerTin(String IP_INV_CST_TIN) {

		this.IP_INV_CST_TIN = IP_INV_CST_TIN;

	}

	public String getIpInvCustomerAddress() {

		return IP_INV_CST_ADDRSS;

	}

	public void setIpInvCustomerAddress(String IP_INV_CST_ADDRSS) {

		this.IP_INV_CST_ADDRSS = IP_INV_CST_ADDRSS;

	}

	public String getIpInvCustomerCity() {

		return IP_INV_CST_CTY;

	}

	public void setIpInvCustomerCity(String IP_INV_CST_CTY) {

		this.IP_INV_CST_CTY = IP_INV_CST_CTY;

	}

	public String getIpInvCustomerCountry() {

		return IP_INV_CST_CNTRY;

	}

	public void setIpInvCustomerCountry(String IP_INV_CST_CNTRY) {

		this.IP_INV_CST_CNTRY = IP_INV_CST_CNTRY;

	}


	public String getIpInvCustomerContactPerson() {

		return IP_INV_CST_CNTCT_PRSN;

	}

	public void setIpInvCustomerContactPerson(String IP_INV_CST_CNTCT_PRSN) {

		this.IP_INV_CST_CNTCT_PRSN = IP_INV_CST_CNTCT_PRSN;

	}

	public String getIpInvCustomerPhoneNumber() {

		return IP_INV_CST_PHN_NMBR;

	}

	public void setIpInvCustomerPhoneNumber(String IP_INV_CST_PHN_NMBR) {

		this.IP_INV_CST_PHN_NMBR = IP_INV_CST_PHN_NMBR;

	}


	public String getIpInvCustomerMobileNumber() {

		return IP_INV_CST_MBL_NMBR;

	}

	public void setIpInvCustomerMobileNumber(String IP_INV_CST_MBL_NMBR) {

		this.IP_INV_CST_MBL_NMBR = IP_INV_CST_MBL_NMBR;

	}


	public String getIpInvCustomerFax() {

		return IP_INV_CST_FX;

	}

	public void setIpInvCustomerFax(String IP_INV_CST_FX) {

		this.IP_INV_CST_FX = IP_INV_CST_FX;

	}


	public String getIpInvCustomerEmail() {

		return IP_INV_CST_EML;

	}

	public void setIpInvCustomerEmail(String IP_INV_CST_EML) {

		this.IP_INV_CST_EML = IP_INV_CST_EML;

	}

	public String getIpIlDescription() {

		return IP_IL_DESC;

	}

	public void setIpIlDescription(String IP_IL_DESC) {

		this.IP_IL_DESC = IP_IL_DESC;

	}


	public String getIpIlItemPropertyCode() {

		return IP_IL_ITM_PRPRTY_CD;

	}

	public void setIpIlItemPropertyCode(String IP_IL_ITM_PRPRTY_CD) {

		this.IP_IL_ITM_PRPRTY_CD = IP_IL_ITM_PRPRTY_CD;

	}


	public String getIpIlItemSerialNumber() {

		return IP_IL_ITM_SRL_NMBR;

	}

	public void setIpIlItemSerialNumber(String IP_IL_ITM_SRL_NMBR) {

		this.IP_IL_ITM_SRL_NMBR = IP_IL_ITM_SRL_NMBR;

	}

	public String getIpIlItemSpecs() {

		return IP_IL_ITM_SPCS;

	}

	public void setIpIlItemSpecs(String IP_IL_ITM_SPCS) {

		this.IP_IL_ITM_SPCS = IP_IL_ITM_SPCS;

	}

	public String getIpIlItemCustodian() {

		return IP_IL_ITM_CSTDN;

	}

	public void setIpIlItemCustodian(String IP_IL_ITM_CSTDN) {

		this.IP_IL_ITM_CSTDN = IP_IL_ITM_CSTDN;

	}

	public String getIpIlItemExpiryDate() {

		return IP_IL_ITM_EXPRY_DT;

	}

	public void setIpIlItemExpiryDate(String IP_IL_ITM_EXPRY_DT) {

		this.IP_IL_ITM_EXPRY_DT = IP_IL_ITM_EXPRY_DT;

	}

	public double getIpIlQuantity() {

		return IP_IL_QNTTY;

	}

	public void setIpIlQuantity(double IP_IL_QNTTY) {

		this.IP_IL_QNTTY = IP_IL_QNTTY;

	}

	public double getIpIlUnitPrice() {

		return IP_IL_UNT_PRC;

	}

	public void setIpIlUnitPrice(double IP_IL_UNT_PRC) {

		this.IP_IL_UNT_PRC = IP_IL_UNT_PRC;

	}

	public double getIpIlAmount() {

		return IP_IL_AMNT;

	}

	public void setIpIlAmount(double IP_IL_AMNT) {

		this.IP_IL_AMNT = IP_IL_AMNT;

	}

	public String getIpInvCreatedBy() {

		return IP_INV_CRTD_BY;

	}

	public void setIpInvCreatedBy(String IP_INV_CRTD_BY) {

		this.IP_INV_CRTD_BY = IP_INV_CRTD_BY;

	}

	public String getIpInvApprovedRejectedBy() {

		return IP_INV_APPRVD_RJCTD_BY;

	}

	public void setIpInvApprovedRejectedBy(String IP_INV_APPRVD_RJCTD_BY) {

		this.IP_INV_APPRVD_RJCTD_BY = IP_INV_APPRVD_RJCTD_BY;

	}

	public byte getIpDrDebit() {

		return IP_DR_DBT;

	}

	public void setIpDrDebit(byte IP_DR_DBT) {

		this.IP_DR_DBT = IP_DR_DBT;

	}

	public double getIpDrAmount() {

		return IP_DR_AMNT;

	}

	public void setIpDrAmount(double IP_DR_AMNT) {

		this.IP_DR_AMNT = IP_DR_AMNT;

	}

	public String getIpDrCoaAccountNumber() {

		return IP_DR_COA_ACCNT_NMBR;

	}

	public void setIpDrCoaAccountNumber(String IP_DR_COA_ACCNT_NMBR) {

		this.IP_DR_COA_ACCNT_NMBR = IP_DR_COA_ACCNT_NMBR;

	}

	public String getIpDrCoaAccountDescription() {

		return IP_DR_COA_ACCNT_DESC;

	}

	public void setIpDrCoaAccountDescription(String IP_DR_COA_ACCNT_DESC) {

		this.IP_DR_COA_ACCNT_DESC = IP_DR_COA_ACCNT_DESC;

	}

	public Date getIpInvDueDate() {

		return IP_INV_DUE_DT;

	}

	public void setIpInvDueDate(Date IP_INV_DUE_DT) {

		this.IP_INV_DUE_DT = IP_INV_DUE_DT;

	}

	public Date getIpInvEffectivityDate() {

		return IP_INV_EFFCTVTY_DT;

	}

	public void setIpInvEffectivityDate(Date IP_INV_EFFCTVTY_DT) {

		this.IP_INV_EFFCTVTY_DT = IP_INV_EFFCTVTY_DT;

	}

	public String getIpInvReferenceNumber() {

		return IP_INV_RFRNC_NMBR;

	}

	public void setIpInvReferenceNumber(String IP_INV_RFRNC_NMBR) {

		this.IP_INV_RFRNC_NMBR = IP_INV_RFRNC_NMBR;

	}

	public String getIpInvCurrency() {

		return IP_INV_CRRNCY;

	}

	public void setIpInvCurrency(String IP_INV_CRRNCY) {

		this.IP_INV_CRRNCY = IP_INV_CRRNCY;

	}

	public char getIpInvCurrencySymbol() {

		return IP_INV_CRRNCY_SYMBOL;

	}

	public void setIpInvCurrencySymbol(char IP_INV_CRRNCY_SYMBOL) {

		this.IP_INV_CRRNCY_SYMBOL = IP_INV_CRRNCY_SYMBOL;

	}

	public String getIpInvCurrencyDescription() {

		return IP_INV_CRRNCY_DESC;

	}

	public void setIpInvCurrencyDescription(String IP_INV_CRRNCY_DESC) {

		this.IP_INV_CRRNCY_DESC = IP_INV_CRRNCY_DESC;

	}

	public String getIpInvAmountInWords() {

		return IP_INV_AMNT_IN_WRDS;

	}

	public void setIpInvAmountInWords(String IP_INV_AMNT_IN_WRDS) {

		this.IP_INV_AMNT_IN_WRDS = IP_INV_AMNT_IN_WRDS;

	}

	public byte getIpShowDuplicate() {

		return IP_SHW_DPLCT;

	}

	public void setIpShowDuplicate(byte IP_SHW_DPLCT) {

		this.IP_SHW_DPLCT = IP_SHW_DPLCT;

	}

	public String getIpInvBillingHeader() {

		return IP_INV_BLLNG_HDR;

	}

	public void setIpInvBillingHeader(String IP_INV_BLLNG_HDR) {

		this.IP_INV_BLLNG_HDR = IP_INV_BLLNG_HDR;

	}

	public String getIpInvBillingFooter() {

		return IP_INV_BLLNG_FTR;

	}

	public void setIpInvBillingFooter(String IP_INV_BLLNG_FTR) {

		this.IP_INV_BLLNG_FTR = IP_INV_BLLNG_FTR;

	}

	public String getIpInvBillingHeader2() {

		return IP_INV_BLLNG_HDR2;

	}

	public void setIpInvBillingHeader2(String IP_INV_BLLNG_HDR2) {

		this.IP_INV_BLLNG_HDR2 = IP_INV_BLLNG_HDR2;

	}

	public String getIpInvBillingFooter2() {

		return IP_INV_BLLNG_FTR2;

	}

	public void setIpInvBillingFooter2(String IP_INV_BLLNG_FTR2) {

		this.IP_INV_BLLNG_FTR2 = IP_INV_BLLNG_FTR2;

	}

	public String getIpInvBillingHeader3() {

		return IP_INV_BLLNG_HDR3;

	}

	public void setIpInvBillingHeader3(String IP_INV_BLLNG_HDR3) {

		this.IP_INV_BLLNG_HDR3 = IP_INV_BLLNG_HDR3;

	}

	public String getIpInvBillingFooter3() {

		return IP_INV_BLLNG_FTR3;

	}

	public void setIpInvBillingFooter3(String IP_INV_BLLNG_FTR3) {

		this.IP_INV_BLLNG_FTR3 = IP_INV_BLLNG_FTR3;

	}

	public String getIpInvBillingSignatory() {

		return IP_INV_BLLNG_SGNTRY;

	}

	public void setIpInvBillingSignatory(String IP_INV_BLLNG_SGNTRY) {

		this.IP_INV_BLLNG_SGNTRY = IP_INV_BLLNG_SGNTRY;

	}

	public String getIpInvSignatoryTitle() {

		return IP_INV_SGNTRY_TTL;

	}

	public void setIpInvSignatoryTitle(String IP_INV_SGNTRY_TTL) {

		this.IP_INV_SGNTRY_TTL = IP_INV_SGNTRY_TTL;

	}

	public String getIpInvBillToAddress() {

		return IP_INV_BLL_TO_ADDRSS;

	}

	public void setIpInvBillToAddress(String IP_INV_BLL_TO_ADDRSS) {

		this.IP_INV_BLL_TO_ADDRSS = IP_INV_BLL_TO_ADDRSS;

	}

	public String getIpInvBillToContact() {

		return IP_INV_BLL_TO_CNTCT;

	}

	public void setIpInvBillToContact(String IP_INV_BLL_TO_CNTCT) {

		this.IP_INV_BLL_TO_CNTCT = IP_INV_BLL_TO_CNTCT;

	}

	public String getIpInvBillToAltContact() {

		return IP_INV_BLL_TO_ALT_CNTCT;

	}

	public void setIpInvBillToAltContact(String IP_INV_BLL_TO_ALT_CNTCT) {

		this.IP_INV_BLL_TO_ALT_CNTCT = IP_INV_BLL_TO_ALT_CNTCT;

	}

	public String getIpInvBillToPhone() {

		return IP_INV_BLL_TO_PHN;

	}

	public void setIpInvBillToPhone(String IP_INV_BLL_TO_PHN) {

		this.IP_INV_BLL_TO_PHN = IP_INV_BLL_TO_PHN;

	}

	public String getIpInvShipToAddress() {

		return IP_INV_SHP_TO_ADDRSS;

	}

	public void setIpInvShipToAddress(String IP_INV_SHP_TO_ADDRSS) {

		this.IP_INV_SHP_TO_ADDRSS = IP_INV_SHP_TO_ADDRSS;

	}

	public String getIpInvShipToContact() {

		return IP_INV_SHP_TO_CNTCT;

	}

	public void setIpInvShipToContact(String IP_INV_SHP_TO_CNTCT) {

		this.IP_INV_SHP_TO_CNTCT = IP_INV_SHP_TO_CNTCT;

	}

	public String getIpInvShipToAltContact() {

		return IP_INV_SHP_TO_ALT_CNTCT;

	}

	public void setIpInvShipToAltContact(String IP_INV_SHP_TO_ALT_CNTCT) {

		this.IP_INV_SHP_TO_ALT_CNTCT = IP_INV_SHP_TO_ALT_CNTCT;

	}

	public String getIpInvShipToPhone() {

		return IP_INV_SHP_TO_PHN;

	}

	public void setIpInvShipToPhone(String IP_INV_SHP_TO_PHN) {

		this.IP_INV_SHP_TO_PHN = IP_INV_SHP_TO_PHN;

	}

	public String getIpInvShipTo() {

		return IP_INV_SHP_TO;

	}

	public void setIpInvShipTo(String IP_INV_SHP_TO) {

		this.IP_INV_SHP_TO = IP_INV_SHP_TO;

	}

	public double getIpIlAmountWoVat() {

		return IP_IL_AMNT_WO_VAT;

	}

	public void setIpIlAmountWoVat(double IP_IL_AMNT_WO_VAT) {

		this.IP_IL_AMNT_WO_VAT = IP_IL_AMNT_WO_VAT;

	}

	public double getIpInvTotalTax() {

		return IP_INV_TTL_TX;

	}

	public void setIpInvTotalTax(double IP_INV_TTL_TX) {

		this.IP_INV_TTL_TX = IP_INV_TTL_TX;

	}

	public double getIpInvDiscountAmount() {

		return IP_INV_DSCNT_AMNT;

	}

	public void setIpInvDiscountAmount(double IP_INV_DSCNT_AMNT) {

		this.IP_INV_DSCNT_AMNT = IP_INV_DSCNT_AMNT;

	}

	public String getIpInvDiscountDescription() {

		return IP_INV_DSCNT_DESC;

	}

	public void setIpInvDiscountDescription(String IP_INV_DSCNT_DESC) {

		this.IP_INV_DSCNT_DESC = IP_INV_DSCNT_DESC;

	}

	public String getIpInvIlSmlUnitOfMeasure() {

		return IP_INV_IL_SML_UNT_OF_MSR;

	}

	public void setIpInvIlSmlUnitOfMeasure(String IP_INV_IL_SML_UNT_OF_MSR) {

		this.IP_INV_IL_SML_UNT_OF_MSR = IP_INV_IL_SML_UNT_OF_MSR;

	}

	public String getIpInvPytName() {

		return IP_INV_PYT_NM;

	}

	public void setIpInvPytName(String IP_INV_PYT_NM) {

		this.IP_INV_PYT_NM = IP_INV_PYT_NM;

	}

	public String getIpInvPytDescription(){

		return IP_INV_PYT_DESC;

	}

	public void setIpInvPytDescription(String IP_INV_PYT_DESC) {

		this.IP_INV_PYT_DESC = IP_INV_PYT_DESC;

	}

	public String getIpIlCategory() {

		return IP_IL_CTGRY;

	}

	public void setIpIlCategory(String IP_IL_CTGRY) {

		this.IP_IL_CTGRY = IP_IL_CTGRY;

	}

	public String getIpInvCstCustomerCode() {

		return IP_INV_CST_CSTMR_CD;

	}

	public void setIpInvCstCustomerCode(String IP_INV_CST_CSTMR_CD) {

		this.IP_INV_CST_CSTMR_CD = IP_INV_CST_CSTMR_CD;

	}

	public String getIpInvCustomerDescription() {

		return IP_INV_CST_DESC;

	}

	public void setIpInvCustomerDescription(String IP_INV_CST_DESC) {

		this.IP_INV_CST_DESC = IP_INV_CST_DESC;

	}

	public String getIpIlName() {

		return IP_IL_NM;

	}

	public void setIpIlName(String IP_IL_NM) {

		this.IP_IL_NM = IP_IL_NM;

	}

	public String getIpIlUomShortName() {

		return IP_IL_UOM_SHRT_NM;

	}

	public void setIpIlUomShortName(String IP_IL_UOM_SHRT_NM) {

		this.IP_IL_UOM_SHRT_NM = IP_IL_UOM_SHRT_NM;

	}

	public String getIpSlpSalespersonCode() {

		return IP_SLP_SLSPRSN_CODE;

	}

	public void setIpSlpSalespersonCode(String IP_SLP_SLSPRSN_CODE) {

		this.IP_SLP_SLSPRSN_CODE = IP_SLP_SLSPRSN_CODE;

	}

	public String getIpSlpName() {

		return IP_SLP_NM;

	}

	public void setIpSlpName(String IP_SLP_NM) {

		this.IP_SLP_NM = IP_SLP_NM;

	}

	public String getIpInvSoNumber() {

		return IP_INV_SO_NMBR;

	}

	public void setIpInvSoNumber(String IP_INV_SO_NMBR) {

		this.IP_INV_SO_NMBR = IP_INV_SO_NMBR;

	}
	
	public String getIpInvJoNumber() {

		return IP_INV_JO_NMBR;

	}

	public void setIpInvJoNumber(String IP_INV_JO_NMBR) {

		this.IP_INV_JO_NMBR = IP_INV_JO_NMBR;

	}

	public String getIpInvCmInvoiceNumber() {

	    return IP_INV_CM_INVC_NMBR;
	}

	public void setIpInvCmInvoiceNumber(String IP_INV_CM_INVC_NMBR) {

		this.IP_INV_CM_INVC_NMBR = IP_INV_CM_INVC_NMBR;

	}

	public String getIpInvCmInvoiceDate() {

		return IP_INV_CM_INVC_DT;

	}

	public void setIpInvCmInvoiceDate(String IP_INV_CM_INVC_DT) {

		this.IP_INV_CM_INVC_DT = IP_INV_CM_INVC_DT;

	}

	public double getIpIlUnitPriceWoVat() {

		return IP_IL_UNT_PRC_WO_VAT;

	}

	public void setIpIlUnitPriceWoVat(double IP_IL_UNT_PRC_WO_VAT) {

		this.IP_IL_UNT_PRC_WO_VAT = IP_IL_UNT_PRC_WO_VAT;

	}

	public double getIpIliDiscount1() {

		return IP_ILI_DSCNT1;

	}

	public void setIpIliDiscount1(double IP_ILI_DSCNT1) {

		this.IP_ILI_DSCNT1 = IP_ILI_DSCNT1;

	}

	public double getIpIliDiscount2() {

		return IP_ILI_DSCNT2;

	}

	public void setIpIliDiscount2(double IP_ILI_DSCNT2) {

		this.IP_ILI_DSCNT2 = IP_ILI_DSCNT2;

	}

	public double getIpIliDiscount3() {

		return IP_ILI_DSCNT3;

	}

	public void setIpIliDiscount3(double IP_ILI_DSCNT3) {

		this.IP_ILI_DSCNT3 = IP_ILI_DSCNT3;

	}

	public double getIpIliDiscount4() {

		return IP_ILI_DSCNT4;

	}

	public void setIpIliDiscount4(double IP_ILI_DSCNT4) {

		this.IP_ILI_DSCNT4 = IP_ILI_DSCNT4;

	}

	public double getIpInvTcRate() {

		return IP_INV_TC_RT;

	}

	public void setIpInvTcRate(double IP_INV_TC_RT) {

		this.IP_INV_TC_RT = IP_INV_TC_RT;

	}

	public Date getIpIpsDueDate() {

		return IP_IPS_DUE_DT;

	}

	public void setIpIpsDueDate(Date IP_IPS_DUE_DT) {

		this.IP_IPS_DUE_DT = IP_IPS_DUE_DT;

	}

	public double getIpIpsAmountDue() {

		return IP_IPS_AMNT_DUE;

	}

	public void setIpIpsAmountDue(double IP_IPS_AMNT_DUE) {

		this.IP_IPS_AMNT_DUE = IP_IPS_AMNT_DUE;

	}

	public String getIpIliSalesPerson() {

		return IP_ILI_SLS_PRSN;

	}

	public void setIpIliSalesPerson(String IP_ILI_SLS_PRSN) {

		this.IP_ILI_SLS_PRSN = IP_ILI_SLS_PRSN;

	}
	
	public String getIpJolTotalHours() {

		return IP_JOL_TTL_HRS;

	}

	public void setIpJolTotalHours(String IP_JOL_TTL_HRS) {

		this.IP_JOL_TTL_HRS = IP_JOL_TTL_HRS;

	}

	public String getIpInvTaxCode(){

		return IP_INV_TX_CD;

	}

	public void setIpInvTaxCode(String IP_INV_TX_CD) {

		this.IP_INV_TX_CD = IP_INV_TX_CD;

	}

	public String getIpIliClientPo(){

		return IP_ILI_CLNT_PO;

	}

	public void setIpIliClientPo(String IP_ILI_CLNT_PO) {

		this.IP_ILI_CLNT_PO = IP_ILI_CLNT_PO;

	}


	public String getIpInvWithholdingTaxCode() {

		return IP_INV_WTHHLDNG_TX_CD;

	}

	public void setIpInvWithholdingTaxCode(String IP_INV_WTHHLDNG_TX_CD) {

		this.IP_INV_WTHHLDNG_TX_CD = IP_INV_WTHHLDNG_TX_CD;

	}

	public double getIpInvWithholdingTaxAmount() {

		return IP_INV_WTHHLDNG_TX_MNT;

	}

	public void setIpInvWithholdingTaxAmount(double IP_INV_WTHHLDNG_TX_MNT) {

		this.IP_INV_WTHHLDNG_TX_MNT = IP_INV_WTHHLDNG_TX_MNT;

	}

	public double getIpIliTotalDiscount() {

		return IP_ILI_TTL_DSCNT;

	}

	public void setIpIliTotalDiscount(double IP_ILI_TTL_DSCNT) {

		this.IP_ILI_TTL_DSCNT = IP_ILI_TTL_DSCNT;

	}

	public String getIpInvCreatedByDesc() {

		return IP_INV_CRTD_BY_DSC;

	}

	public void setIpInvCreatedByDesc(String IP_INV_CRTD_BY_DSC) {

		this.IP_INV_CRTD_BY_DSC = IP_INV_CRTD_BY_DSC;

	}

	public String getIpInvCheckByDescription() {

		return IP_INV_CHCK_BY_DESC;

	}

	public void setIpInvCheckByDescription(String IP_INV_CHCK_BY_DESC) {

		this.IP_INV_CHCK_BY_DESC = IP_INV_CHCK_BY_DESC;

	}

	public String getIpInvApprovedRejectedByDescription() {

		return IP_INV_APPRVD_RJCTD_BY_DESC;

	}

	public void setIpInvApprovedRejectedByDescription(String IP_INV_APPRVD_RJCTD_BY_DESC) {

		this.IP_INV_APPRVD_RJCTD_BY_DESC = IP_INV_APPRVD_RJCTD_BY_DESC;

	}
	public String getIpInvBranchCode() {

		return IP_INV_BRNCH_CDE;

	}

	public void setIpInvBranchCode(String IP_INV_BRNCH_CDE) {

		this.IP_INV_BRNCH_CDE = IP_INV_BRNCH_CDE;

	}

	public String getIpInvBranchName() {

		return IP_INV_BRNCH_NM;

	}

	public void setIpInvBranchName(String IP_INV_BRNCH_NM) {
		Debug.print("IP_INV_BRNCH_NM : " + IP_INV_BRNCH_NM);
		this.IP_INV_BRNCH_NM = IP_INV_BRNCH_NM;

	}
	public String getIpInvSoReferenceNumber(){

		return IP_INV_SO_RFRNC_NMBR;
	}

	public void setIpInvSoReferenceNumber(String IP_INV_SO_RFRNC_NMBR){
		this.IP_INV_SO_RFRNC_NMBR = IP_INV_SO_RFRNC_NMBR;
	}
	public String getIpInvApprovalStatus() {

		return IP_INV_APPRVL_STATUS;

	}

	public void setIpInvApprovalStatus(String IP_INV_APPRVL_STATUS) {

		this.IP_INV_APPRVL_STATUS = IP_INV_APPRVL_STATUS;

	}
	
	
	public boolean getIpInvIsDraft() {

		return IP_INV_IS_DRFT;

	}

	public void setIpInvIsDraft(boolean IP_INV_IS_DRFT) {

		this.IP_INV_IS_DRFT = IP_INV_IS_DRFT;

	}
	
	
	public String getIpInvCreatedByDescription() {

		return IP_INV_CRTD_BY_DESC;

	}

	public void setIpInvCreatedByDescription(String IP_INV_CRTD_BY_DESC) {

		this.IP_INV_CRTD_BY_DESC = IP_INV_CRTD_BY_DESC;

	}
	
	public String getIpInvApprovedBy() {

        return IP_INV_APPRVD_BY;

    }

    public void setIpInvApprovedBy(String IP_INV_APPRVD_BY) {

        this.IP_INV_APPRVD_BY = IP_INV_APPRVD_BY;

    }

	public String getIpInvCustomerDealPrice(){

		return IP_INV_CST_DL_PRC;
	}

	public void setIpInvCustomerDealPrice(String IP_INV_CST_DL_PRC){
		this.IP_INV_CST_DL_PRC = IP_INV_CST_DL_PRC;
	}


	public String getIpIlIsTax(){

		return IP_IL_IS_TX;
	}

	public void setIpIlIsTax(String IP_IL_IS_TX){
		this.IP_IL_IS_TX = IP_IL_IS_TX;
	}

	public String getIpReportParameter(){

		return IP_REPORT_PARAMETER;
	}

	public void setIpReportParameter(String IP_REPORT_PARAMETER){
		this.IP_REPORT_PARAMETER = IP_REPORT_PARAMETER;
	}
	
	
	
	public String getIpJaLine() {

		return IP_JA_LN;

	}

	public void setIpJaLine(String IP_JA_LN) {

		this.IP_JA_LN = IP_JA_LN;

	}
	
	public String getIpJaRemarks() {

		return IP_JA_RMRKS;

	}

	public void setIpJaRemarks(String IP_JA_RMRKS) {

		this.IP_JA_RMRKS = IP_JA_RMRKS;

	}
	
	
	public double getIpJaQuantity() {

		return IP_JA_QTY;

	}

	public void setIpJaQuantity(double IP_JA_QTY) {

		this.IP_JA_QTY = IP_JA_QTY;

	}
	
	
	public double getIpJaUnitCost() {

		return IP_JA_UNT_CST;

	}

	public void setIpJaUnitCost(double IP_JA_UNT_CST) {

		this.IP_JA_UNT_CST = IP_JA_UNT_CST;

	}
	
	
	public double getIpJaAmount() {

		return IP_JA_AMNT;

	}

	public void setIpJaAmount(double IP_JA_AMNT) {

		this.IP_JA_AMNT = IP_JA_AMNT;

	}

	
	public byte getIpJaSo() {

		return IP_JA_SO;

	}

	public void setIpJaSo(byte IP_JA_SO) {

		this.IP_JA_SO = IP_JA_SO;

	}
	
	public String getIpJaIdNumber() {

		return IP_JA_PE_ID_NMBR;

	}

	public void setIpJaIdNumber(String IP_JA_PE_ID_NMBR) {

		this.IP_JA_PE_ID_NMBR = IP_JA_PE_ID_NMBR;

	}
	
	public String getIpJaPersonelName() {

		return IP_JA_PE_NM;

	}

	public void setIpJaPersonelName(String IP_JA_PE_NM) {

		this.IP_JA_PE_NM = IP_JA_PE_NM;

	}
	
	public byte getIpIiJobServices() {

		return IP_II_JB_SRVCS;

	}

	public void setIpIiJobServices(byte IP_II_JB_SRVCS) {

		this.IP_II_JB_SRVCS = IP_II_JB_SRVCS;

	}


} // ArRepInvoicePrintDetails class