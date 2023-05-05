/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import com.util.Debug;

import java.util.Comparator;
import java.util.Date;

public class ApRepVoucherPrintDetails implements java.io.Serializable {

   private String VP_VOU_TYP;
   private String VP_VOU_SPL_NM;
   private String VP_VOU_SC_NM;
   private Date VP_VOU_DT;
   private String VP_VOU_SPL_ADDRSS;
   private String VP_VOU_DCMNT_NMBR;
   private String VP_VOU_SPL_TIN;
   private String VP_VOU_RFRNC_NMBR;
   private String VP_VOU_DESC;
   private double VP_VOU_AMNT_DUE;
   private String VP_VOU_CRTD_BY;
   private String VP_VOU_CRTD_BY_DESC;
   private String VP_VOU_CHCKD_BY;
   private String VP_VOU_APPRVD_BY;
   private String VP_DR_COA_ACCNT_NMBR;
   private String VP_DR_COA_ACCNT_DESC;
   private String VP_DR_TX_COA_ACCNT_DESC;
   private byte VP_DR_DBT;
   private double VP_DR_AMNT;
   private Date VP_VOU_DUE_DT;
   private String VP_VOU_TRMS;
   private byte VP_SHW_DPLCT;
   private String VP_PO_NMBR;
   private String VP_VOU_WTC_NM;
   private String VP_VOU_TC_NM;
   private String VP_VOU_PSTD_BY;
   private String VP_VOU_CHCK_BY_DESC;
	private String VP_VOU_APPRVD_RJCTD_BY_DESC;
	private Date VP_VOU_DT_CRTD;
	private String VP_VOU_SPL_CD;
	private String VP_VOU_MISC1;
	private String VP_VOU_MISC2;
	private String VP_VOU_MISC3;
	private String VP_VOU_MISC4;
	private String VP_VOU_MISC5;
	private String VP_VOU_MISC6;

	//voucher line item fields
   private byte VP_VOU_SC_VT_RLF_VCHR_ITM ;
   private byte VP_VLI_VT_RLF;
   private String VP_VLI_ITM_CD ;
   private String VP_VLI_ITM_DESC ;
   private String VP_VLI_LCTN ;
   private String VP_VLI_UNT;
   private double VP_VLI_UNT_AMNT;
   private double VP_VLI_QTY;
   private double VP_VLI_AMNT ;
   private double VP_VLI_DSCNT;
   private String VP_VLI_SPPLR_NM ;
   private String VP_VLI_TN ;
   private String VP_VLI_ADDRSS;
   private String VP_VLI_PRJCT_CD;
   private String VP_VLI_PRJCT_NM;
   private String VP_VLI_PRJCT_CLNT_ID;
   private String VP_VLI_PRJCT_TYP_CD ;
   private String VP_VLI_PRJCT_TYP_VL;
   private String VP_VLI_PRJCT_PHS_NM;
   private byte VP_VLI_TX ;

   private String VP_VLI_PRPRTY_CD;
   private String VP_VLI_SRL_NMBR;
   private String VP_VLI_SPCS;
   private String VP_VLI_CSTDN;
   private String VP_VLI_EXPRY_DT;

   // added fields
   private String VP_APPRVL_STTS;
   private byte VP_PSTD;
   private double VP_AMNT_WO_TX;
   private String VP_DR_COA_NTRL_DESC;
   private char VP_VOU_FC_SYMBL;

   private String VP_AMNT_IN_WRDS;

   private String VP_BR_NM;
   private String VP_BR_CDE;
   private double VP_PL_TX_AMNT;
   private double VP_PL_AMNT;
   private String VP_PL_TX_ACCNT_DSCRPTN;
   private String VP_PO_RFRNV_NMBR;
   private double VP_PL_QNTTY;

   public ApRepVoucherPrintDetails() {
   }


   public String getVpVouType() {

	   	  return VP_VOU_TYP;

	}



	public void setVpVouType(String VP_VOU_TYP) {

	   	  this.VP_VOU_TYP = VP_VOU_TYP;

	}






   public String getVpBrName() {

	   	  return VP_BR_NM;

   }



   public void setVpBrName(String VP_BR_NM) {

	   	  this.VP_BR_NM = VP_BR_NM;

   }

   public String getVpScSupplierClassName() {

	   	  return VP_VOU_SC_NM;

   }

   public void setVpScSupplierClassName(String VP_VOU_SC_NM) {
	   this.VP_VOU_SC_NM = VP_VOU_SC_NM;
   }

   public String getVpBrCode() {

	   	  return VP_BR_CDE;

	}

	public void setVpBrCode(String VP_BR_CDE) {

		   	  this.VP_BR_CDE = VP_BR_CDE;

	}

   public String getVpVouSplName() {

   	  return VP_VOU_SPL_NM;

   }

   public void setVpVouSplName(String VP_VOU_SPL_NM) {

   	  this.VP_VOU_SPL_NM = VP_VOU_SPL_NM;

   }

   public Date getVpVouDate() {

   	  return VP_VOU_DT;

   }

   public void setVpVouDate(Date VP_VOU_DT) {

	   this.VP_VOU_DT = VP_VOU_DT;

   }

   public Date getVpVouDateCreated() {

	   return VP_VOU_DT_CRTD;

   }

   public void setVpVouDateCreated(Date VP_VOU_DT_CRTD) {

	   this.VP_VOU_DT_CRTD = VP_VOU_DT_CRTD;

   }

   public String getVpVouSplAddress() {

   	  return VP_VOU_SPL_ADDRSS;

   }

   public void setVpVouSplAddress(String VP_VOU_SPL_ADDRSS) {

   	  this.VP_VOU_SPL_ADDRSS = VP_VOU_SPL_ADDRSS;

   }

   public String getVpVouDocumentNumber() {

   	  return VP_VOU_DCMNT_NMBR;

   }

   public void setVpVouDocumentNumber(String VP_VOU_DCMNT_NMBR) {

   	  this.VP_VOU_DCMNT_NMBR = VP_VOU_DCMNT_NMBR;

   }

   public String getVpVouSplTin() {

   	  return VP_VOU_SPL_TIN;

   }

   public void setVpVouSplTin(String VP_VOU_SPL_TIN) {

   	  this.VP_VOU_SPL_TIN = VP_VOU_SPL_TIN;

   }

   public String getVpVouReferenceNumber() {

   	  return VP_VOU_RFRNC_NMBR;

   }

   public void setVpVouReferenceNumber(String VP_VOU_RFRNC_NMBR) {

   	  this.VP_VOU_RFRNC_NMBR = VP_VOU_RFRNC_NMBR;

   }

   public String getVpVouDescription() {

   	  return VP_VOU_DESC;

   }

   public void setVpVouDescription(String VP_VOU_DESC) {

   	  this.VP_VOU_DESC = VP_VOU_DESC;

   }

   public double getVpVouAmountDue() {

   	  return VP_VOU_AMNT_DUE;

   }

   public void setVpVouAmountDue(double VP_VOU_AMNT_DUE) {

   	  this.VP_VOU_AMNT_DUE = VP_VOU_AMNT_DUE;

   }

   public String getVpVouCreatedBy() {

   	  return VP_VOU_CRTD_BY;

   }

   public void setVpVouCreatedBy(String VP_VOU_CRTD_BY) {

   	  this.VP_VOU_CRTD_BY = VP_VOU_CRTD_BY;

   }

   public String getVpChkCreatedByDescription() {

		return VP_VOU_CRTD_BY_DESC;

	}

	public void setVpChkCreatedByDescription(String VP_VOU_CRTD_BY_DESC) {

		this.VP_VOU_CRTD_BY_DESC = VP_VOU_CRTD_BY_DESC;

	}

	public String getVpChkCheckByDescription() {

		return VP_VOU_CHCK_BY_DESC;

	}

	public void setVpChkCheckByDescription(String VP_VOU_CHCK_BY_DESC) {

		this.VP_VOU_CHCK_BY_DESC = VP_VOU_CHCK_BY_DESC;

	}

	public String getVpChkApprovedRejectedByDescription() {

		return VP_VOU_APPRVD_RJCTD_BY_DESC;

	}

	public void setVpChkApprovedRejectedByDescription(String VP_VOU_APPRVD_RJCTD_BY_DESC) {

		this.VP_VOU_APPRVD_RJCTD_BY_DESC = VP_VOU_APPRVD_RJCTD_BY_DESC;

	}

   public String getVpVouCheckedBy() {

   	  return VP_VOU_CHCKD_BY;

   }

   public void setVpVouCheckedBy(String VP_VOU_CHCKD_BY) {

   	  this.VP_VOU_CHCKD_BY = VP_VOU_CHCKD_BY;

   }

   public String getVpVouApprovedBy() {

   	  return VP_VOU_APPRVD_BY;

   }

   public void setVpVouApprovedBy(String VP_VOU_APPRVD_BY) {

   	  this.VP_VOU_APPRVD_BY = VP_VOU_APPRVD_BY;

   }

   public String getVpDrCoaAccountNumber() {

   	  return VP_DR_COA_ACCNT_NMBR;

   }

   public void setVpDrCoaAccountNumber(String VP_DR_COA_ACCNT_NMBR) {

   	  this.VP_DR_COA_ACCNT_NMBR = VP_DR_COA_ACCNT_NMBR;

   }

   public String getVpDrCoaAccountDescription() {

   	  return VP_DR_COA_ACCNT_DESC;

   }

   public void setVpDrCoaAccountDescription(String VP_DR_COA_ACCNT_DESC) {

   	  this.VP_DR_COA_ACCNT_DESC = VP_DR_COA_ACCNT_DESC;

   }

   public String getVpDrTaxCoaAccountDescription() {

   	  return VP_DR_TX_COA_ACCNT_DESC;

   }

   public void setVpDrTaxCoaAccountDescription(String VP_DR_TX_COA_ACCNT_DESC) {

   	  this.VP_DR_TX_COA_ACCNT_DESC = VP_DR_TX_COA_ACCNT_DESC;

   }

   public byte getVpDrDebit() {

   	  return VP_DR_DBT;

   }

   public void setVpDrDebit(byte VP_DR_DBT) {

   	  this.VP_DR_DBT = VP_DR_DBT;

   }

   public double getVpDrAmount() {

   	  return VP_DR_AMNT;

   }

   public void setVpDrAmount(double VP_DR_AMNT) {

   	  this.VP_DR_AMNT = VP_DR_AMNT;

   }

   public String getVpVouTerms() {

   	  return VP_VOU_TRMS;

   }

   public void setVpVouTerms(String VP_VOU_TRMS) {

   	  this.VP_VOU_TRMS = VP_VOU_TRMS;

   }

   public Date getVpVouDueDate() {

   	  return VP_VOU_DUE_DT;

   }

   public void setVpVouDueDate(Date VP_VOU_DUE_DT) {

   	  this.VP_VOU_DUE_DT = VP_VOU_DUE_DT;

   }

   public byte getVpShowDuplicate() {

   	  return VP_SHW_DPLCT;

   }

   public void setVpShowDuplicate(byte VP_SHW_DPLCT) {

   	  this.VP_SHW_DPLCT = VP_SHW_DPLCT;

   }

   public String getVpApprovalStatus() {

   	  return VP_APPRVL_STTS;

   }

   public void setVpApprovalStatus(String VP_APPRVL_STTS) {

   	  this.VP_APPRVL_STTS = VP_APPRVL_STTS;

   }



   public String getVpVliPropertyCode() {

   	  return VP_VLI_PRPRTY_CD;

   }

   public void setVpVliPropertyCode(String VP_VLI_PRPRTY_CD) {

   	  this.VP_VLI_PRPRTY_CD = VP_VLI_PRPRTY_CD;

   }


   public String getVpVliSerialNumber() {

   	  return VP_VLI_SRL_NMBR;

   }

   public void setVpVliSerialNumber(String VP_VLI_SRL_NMBR) {

   	  this.VP_VLI_SRL_NMBR = VP_VLI_SRL_NMBR;

   }

   public String getVpVliSpecs() {

   	  return VP_VLI_SPCS;

   }

   public void setVpVliSpecs(String VP_VLI_SPCS) {

   	  this.VP_VLI_SPCS = VP_VLI_SPCS;

   }

   public String getVpVliCustodian() {

   	  return VP_VLI_CSTDN;

   }

   public void setVpVliCustodian(String VP_VLI_CSTDN) {

   	  this.VP_VLI_CSTDN = VP_VLI_CSTDN;

   }

   public String getVpVliExpiryDate() {

   	  return VP_VLI_EXPRY_DT;

   }

   public void setVpVliExpiryDate(String VP_VLI_EXPRY_DT) {

   	  this.VP_VLI_EXPRY_DT = VP_VLI_EXPRY_DT;

   }

   public byte getVpPosted() {

   	  return VP_PSTD;

   }

   public void setVpPosted(byte VP_PSTD) {

   	 this.VP_PSTD = VP_PSTD;

   }

   public String getVpPoNumber() {

   	 return VP_PO_NMBR;

   }

   public void setVpPoNumber(String VP_PO_NMBR) {

   	 this.VP_PO_NMBR = VP_PO_NMBR;

   }

   public double getVpAmountWoTax() {

   	 return VP_AMNT_WO_TX;

   }

   public void setVpAmountWoTax(double VP_AMNT_WO_TX) {

   	 this.VP_AMNT_WO_TX = VP_AMNT_WO_TX;

   }

   public String getVpVouWithholdingTaxName() {

   	 return VP_VOU_WTC_NM;

   }

   public void setVpVouWithholdingTaxName(String VP_VOU_WTC_NM) {

   	 this.VP_VOU_WTC_NM = VP_VOU_WTC_NM;

   }

   public String getVpVouTaxName() {

	   return VP_VOU_TC_NM;

   }

   public void setVpVouTaxName(String VP_VOU_TC_NM) {

	   this.VP_VOU_TC_NM = VP_VOU_TC_NM;

   }

   public String getVpVouPostedBy() {

   	 return VP_VOU_PSTD_BY;

   }

   public void setVpVouPostedBy(String VP_VOU_PSTD_BY) {

   	 this.VP_VOU_PSTD_BY = VP_VOU_PSTD_BY;

   }

   public String getVpDrCoaNaturalDesc(){

   	return VP_DR_COA_NTRL_DESC;

   }

   public void setVpDrCoaNaturalDesc(String VP_DR_COA_NTRL_DESC){

   		this.VP_DR_COA_NTRL_DESC = VP_DR_COA_NTRL_DESC;

   }

   public char getVpVouFcSymbol(){

   	return VP_VOU_FC_SYMBL;

   }

   public void setVpVouFcSymbol(char VP_VOU_FC_SYMBL){

   	this.VP_VOU_FC_SYMBL = VP_VOU_FC_SYMBL;

   }

   public String getVpAmountInWords() {

	   return VP_AMNT_IN_WRDS;

   }

   public void setVpAmountInWords(String VP_AMNT_IN_WRDS) {

	   this.VP_AMNT_IN_WRDS = VP_AMNT_IN_WRDS;
   }

   public double getVpPlTaxAmount() {

   	  return VP_PL_TX_AMNT;

   }

   public void setVpPlTaxAmount(double VP_PL_TX_AMNT) {

   	  this.VP_PL_TX_AMNT = VP_PL_TX_AMNT;

   }

   public double getVpPlAmount() {

   	  return VP_PL_AMNT;

   }

   public void setVpPlAmount(double VP_PL_AMNT) {

   	  this.VP_PL_AMNT = VP_PL_AMNT;

   }

   public String getVpTaxAccountDescription() {

	   return VP_PL_TX_ACCNT_DSCRPTN;

   }

   public void setVpTaxAccountDescription(String VP_PL_TX_ACCNT_DSCRPTN) {

	   this.VP_PL_TX_ACCNT_DSCRPTN = VP_PL_TX_ACCNT_DSCRPTN;
   }

   public String getVpPoReferenceNumber() {

	   return VP_PO_RFRNV_NMBR;

   }

   public void setVpPoReferenceNumber(String VP_PO_RFRNV_NMBR) {

	   this.VP_PO_RFRNV_NMBR = VP_PO_RFRNV_NMBR;
   }

   public double getVpPlQuantity() {

   	  return VP_PL_QNTTY;

   }

   public void setVpPlQuantity(double VP_PL_QNTTY) {

   	  this.VP_PL_QNTTY = VP_PL_QNTTY;

   }
   public String getVpVouSplCode() {

	   	  return VP_VOU_SPL_CD;

	   }

   public void setVpVouSplCode(String VP_VOU_SPL_CD) {

	   	  this.VP_VOU_SPL_CD = VP_VOU_SPL_CD;

	   }

   public String getVpVouMisc1() {

	   	  return VP_VOU_MISC1;

	   }

	public void setVpVouMisc1(String VP_VOU_MISC1) {

		   	  this.VP_VOU_MISC1 = VP_VOU_MISC1;

		   }

	public String getVpVouMisc2() {

	 	  return VP_VOU_MISC2;

	 }

	public void setVpVouMisc2(String VP_VOU_MISC2) {

	 	  this.VP_VOU_MISC2 = VP_VOU_MISC2;

	 }

	public String getVpVouMisc3() {

	 	  return VP_VOU_MISC3;

	 }

	public void setVpVouMisc3(String VP_VOU_MISC3) {

	 	  this.VP_VOU_MISC3 = VP_VOU_MISC3;

	 }

	public String getVpVouMisc4() {

	 	  return VP_VOU_MISC4;

	 }

	public void setVpVouMisc4(String VP_VOU_MISC4) {

	 	  this.VP_VOU_MISC4 = VP_VOU_MISC4;

	 }

	public String getVpVouMisc5() {

	 	  return VP_VOU_MISC5;

	 }

	public void setVpVouMisc5(String VP_VOU_MISC5) {

	 	  this.VP_VOU_MISC5 = VP_VOU_MISC5;

	 }

	public String getVpVouMisc6() {

	 	  return VP_VOU_MISC6;

	 }

	public void setVpVouMisc6(String VP_VOU_MISC6) {

	 	  this.VP_VOU_MISC6 = VP_VOU_MISC6;

	 }




	public byte getVpVouScVatReliefVoucherItem() {
		return VP_VOU_SC_VT_RLF_VCHR_ITM ;
	}

	public void setVpVouScVatReliefVoucherItem(byte VP_VOU_SC_VT_RLF_VCHR_ITM ) {
		this.VP_VOU_SC_VT_RLF_VCHR_ITM = VP_VOU_SC_VT_RLF_VCHR_ITM;
	}


	public byte getVpVliVatRelief() {
		return VP_VLI_VT_RLF  ;
	}

	public void setVpVliVatRelief(byte VP_VOU_SC_VT_RLF_VCHR_ITM ) {
		this.VP_VLI_VT_RLF  = VP_VLI_VT_RLF ;
	}


	public String getVpVliItemCode() {
		return VP_VLI_ITM_CD   ;
	}

	public void setVpVliItemCode(String VP_VLI_ITM_CD  ) {
		this.VP_VLI_ITM_CD   = VP_VLI_ITM_CD  ;
	}

	public String getVpVliItemDescription() {
		return VP_VLI_ITM_DESC    ;
	}

	public void setVpVliItemDescription(String VP_VLI_ITM_DESC   ) {
		this.VP_VLI_ITM_DESC    = VP_VLI_ITM_DESC   ;
	}


	public String getVpVliLocation() {
		return VP_VLI_LCTN     ;
	}

	public void setVpVliLocation(String VP_VLI_LCTN    ) {
		this.VP_VLI_LCTN   = VP_VLI_LCTN    ;
	}

	public String getVpVliUnit() {
		return VP_VLI_UNT     ;
	}

	public void setVpVliUnit(String VP_VLI_UNT) {
		this.VP_VLI_UNT  = VP_VLI_UNT     ;
	}

	public double getVpVliUnitAmount() {
		return VP_VLI_UNT_AMNT      ;
	}

	public void setVpVliUnitAmount(double VP_VLI_UNT_AMNT ) {
		this.VP_VLI_UNT_AMNT   = VP_VLI_UNT_AMNT ;
	}

	public double getVpVliQuantity() {
		return VP_VLI_QTY      ;
	}

	public void setVpVliQuantity(double VP_VLI_QTY ) {
		this.VP_VLI_QTY   = VP_VLI_QTY ;
	}

	public double getVpVliAmount() {
		return VP_VLI_AMNT      ;
	}

	public void setVpVliAmount(double VP_VLI_AMNT ) {
		this.VP_VLI_AMNT   = VP_VLI_AMNT ;
	}

	public double getVpVliDiscount() {
		return VP_VLI_DSCNT;
	}

	public void setVpVliDiscount(double VP_VLI_DSCNT ) {
		this.VP_VLI_DSCNT   = VP_VLI_DSCNT ;
	}

	public String getVpVliSupplierName() {
		return VP_VLI_SPPLR_NM;
	}

	public void setVpVliSupplierName(String VP_VLI_SPPLR_NM ) {
		this.VP_VLI_SPPLR_NM   = VP_VLI_SPPLR_NM ;
	}

	public String getVpVliTin() {
		return VP_VLI_TN;
	}

	public void setVpVliTin(String VP_VLI_TN ) {
		this.VP_VLI_TN   = VP_VLI_TN ;
	}

	public String getVpVliAddress() {
		return VP_VLI_ADDRSS;
	}

	public void setVpVliAddress(String VP_VLI_ADDRSS ) {
		this.VP_VLI_ADDRSS   = VP_VLI_ADDRSS ;
	}

	public String getVpVliProjectCode() {
		return VP_VLI_PRJCT_CD;
	}

	public void setVpVliProjectCode(String VP_VLI_PRJCT_CD ) {
		this.VP_VLI_PRJCT_CD   = VP_VLI_PRJCT_CD ;
	}

	public String getVpVliProjectName() {
		return VP_VLI_PRJCT_NM;
	}

	public void setVpVliProjectName(String VP_VLI_PRJCT_NM ) {
		this.VP_VLI_PRJCT_NM   = VP_VLI_PRJCT_NM ;
	}

	public String getVpVliProjectClientId() {
		return VP_VLI_PRJCT_CLNT_ID;
	}

	public void setVpVliProjectClientId(String VP_VLI_PRJCT_CLNT_ID ) {
		this.VP_VLI_PRJCT_CLNT_ID   = VP_VLI_PRJCT_CLNT_ID ;
	}

	public String getVpVliProjectTypeCode() {
		return VP_VLI_PRJCT_TYP_CD;
	}

	public void setVpVliProjectTypeCode(String VP_VLI_PRJCT_TYP_CD ) {
		this.VP_VLI_PRJCT_TYP_CD   = VP_VLI_PRJCT_TYP_CD ;
	}

	public String getVpVliProjectTypeValue() {
		return VP_VLI_PRJCT_TYP_VL;
	}

	public void setVpVliProjectTypeValue(String VP_VLI_PRJCT_TYP_VL ) {
		this.VP_VLI_PRJCT_TYP_VL   = VP_VLI_PRJCT_TYP_VL ;
	}

	public String getVpVliProjectPhaseName() {
		return VP_VLI_PRJCT_PHS_NM ;
	}

	public void setVpVliProjectPhaseName(String VP_VLI_PRJCT_PHS_NM  ) {
		this.VP_VLI_PRJCT_PHS_NM    = VP_VLI_PRJCT_PHS_NM  ;
	}

	public byte getVpVliTax() {
		return VP_VLI_TX;
	}

	public void setVpVliTax(byte VP_VLI_TX  ) {
		this.VP_VLI_TX  = VP_VLI_TX  ;
	}

   public static Comparator sortByAccount = new Comparator() {

	   String asd = "";
		public int compare(Object r1, Object r2) {
			int cmp=1;
			try{
				String receipt1 = ((ApRepCheckVoucherPrintDetails) r1).getCvChkDrCoaAccountNumber();
				String receipt2 = ((ApRepCheckVoucherPrintDetails) r2).getCvChkDrCoaAccountNumber();
				String docNum1 = ((ApRepCheckVoucherPrintDetails) r1).getCvChkDocumentNumber();
				String docNum2 = ((ApRepCheckVoucherPrintDetails) r2).getCvChkDocumentNumber();

				Debug.print(docNum1 + "  ***********  " + docNum2);

				if(docNum1.equals(docNum2)){
					cmp = receipt1.compareTo(receipt2);
					asd=receipt2;
				}

				Debug.print("EY ES DI: "+asd);
			}catch(Exception e){
				cmp=0;
			}
			return cmp;


		}


   };

 }  // ApRepVoucherPrintDetails