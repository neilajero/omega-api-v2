/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Date;

public class ArRepSalesOrderPrintDetails implements java.io.Serializable {

   private Date SP_SO_DT;
   private String SP_SO_RFRNC_NMBR;
   private String SP_SO_TRNSCTN_TYP;
   private String SP_SO_DCMNT_NMBR;
   private String SP_SO_DESC;
   private String SP_SO_CRTD_BY;
   private String SP_SO_APPRVD_RJCTD_BY;
   private String SP_SO_BLL_TO;
   private String SP_SO_SHP_TO;
   private double SP_SOL_QTY;
   private String SP_SOL_UOM;
   private String SP_SOL_II_CODE;
   private String SP_SOL_PRPRTY_CODE;
   private String SP_SOL_SRL_NMBR;
   private String SP_SOL_SPCS;
   private String SP_SOL_CSTDN;
   private String SP_SOL_EXPRY_DT;
   private String SP_IL_CTGRY;
   private String SP_SOL_LOC_NM;
   private double SP_SOL_UNT_PRC;
   private double SP_SOL_AMNT;
   private String SP_SO_CST_CSTMR_CODE;
   private String SP_SO_CST_NM;
   private String SP_SO_CST_ADDRSS;
   private double SP_SO_CST_CRDT_LMT;

   private String SP_SO_CST_CNTCT_PRSN;
   private String SP_SO_CST_PHN_NMBR;
   private String SP_SO_CST_MBL_NMBR;
   private String SP_SO_CST_FX;
   private String SP_SO_CST_EML;

   private String SP_SOL_II_DESC;
   private char SP_SO_FC_SYMBL;
   private double SP_SO_CST_CB_BLNC;
   private double SP_SO_CST_PNDNG_ORDR;
   private double SP_SO_AMNT;
   private double SP_SO_ADVNC_AMNT;
   private String SP_SOL_II_STCK_STATUS;
   private double SP_SO_CST_LST_RCPT_AMNT;
   private Date SP_SO_CST_LST_RCPT_DT;
   private String SP_SO_PYMNT_TRM;
   private double SP_SO_AG_BCKT0;
   private double SP_SO_AG_BCKT1;
   private double SP_SO_AG_BCKT2;
   private double SP_SO_AG_BCKT3;
   private double SP_SO_AG_BCKT4;
   private double SP_SO_AG_BCKT5;
   private String SP_SO_CST_SLS_SLSPRSN_CD;
   private String SP_SO_CST_SLS_NM;
   private String SP_SO_CST_CTY;
   private double SP_SOL_TTL_DSCNT;
   private double SP_SOL_DSCNT1;
   private double SP_SOL_DSCNT2;
   private double SP_SOL_DSCNT3;
   private double SP_SOL_DSCNT4;
   private double SP_SO_TX_RT;
   private double SP_SOL_UNT_CST_WO_TX;
   private double SP_SOL_TX_AMNT;
   private String SP_SO_TX_TYP;
   private double SP_SOL_QOH;
   private String SP_SO_CST_DL_PRC;
   private String SP_SO_MEMO;
   private String SP_REPORT_PARAMETER;
   private byte SP_SO_PSTD;

   public ArRepSalesOrderPrintDetails() {
   }


   public Date getSpSoDate() {

   	  return SP_SO_DT;

   }

   public void setSpSoDate(Date SP_SO_DT) {

   	  this.SP_SO_DT = SP_SO_DT;

   }

   public String getSpSoDocumentNumber() {

       return SP_SO_DCMNT_NMBR;

   }

   public void setSpSoDocumentNumber(String SP_SO_DCMNT_NMBR) {

       this.SP_SO_DCMNT_NMBR = SP_SO_DCMNT_NMBR;

   }

   public String getSpSoCreatedBy() {

       return SP_SO_CRTD_BY;

   }

   public void setSpSoCreatedBy(String SP_SO_CRTD_BY) {

       this.SP_SO_CRTD_BY = SP_SO_CRTD_BY;

   }

   public String getSpSoApprovedRejectedBy() {

       return SP_SO_APPRVD_RJCTD_BY;

   }

   public void setSpSoApprovedRejectedBy(String SP_SO_APPRVD_RJCTD_BY) {

       this.SP_SO_APPRVD_RJCTD_BY = SP_SO_APPRVD_RJCTD_BY;

   }

   public String getSpSoBillTo() {

       return SP_SO_BLL_TO;

   }

   public void setSpSoBillTo(String SP_SO_BLL_TO) {

       this.SP_SO_BLL_TO = SP_SO_BLL_TO;

   }


   public String getSpSoShipTo() {

       return SP_SO_SHP_TO;

   }

   public void setSpSoShipTo(String SP_SO_SHP_TO) {

       this.SP_SO_SHP_TO = SP_SO_SHP_TO;

   }



   public double getSpSolQuantity() {

       return SP_SOL_QTY;

   }

   public void setSpSolQuantity(double SP_SOL_QTY) {

       this.SP_SOL_QTY = SP_SOL_QTY;

   }

   public String getSpSolUom() {

       return SP_SOL_UOM;

   }

   public void setSpSolUom(String SP_SOL_UOM) {

       this.SP_SOL_UOM = SP_SOL_UOM;

   }

   public String getSpSolIiCode() {

       return SP_SOL_II_CODE;

   }

   public void setSpSolIiCode(String SP_SOL_II_CODE) {

       this.SP_SOL_II_CODE = SP_SOL_II_CODE;

   }

   public String getSpSolPropertyCode() {

       return SP_SOL_PRPRTY_CODE;

   }

   public void setSpSolPropertyCode(String SP_SOL_PRPRTY_CODE) {

       this.SP_SOL_PRPRTY_CODE = SP_SOL_PRPRTY_CODE;

   }



   public String getSpSolSerialNumber() {

       return SP_SOL_SRL_NMBR;

   }

   public void setSpSolSerialNumber(String SP_SOL_SRL_NMBR) {

       this.SP_SOL_SRL_NMBR = SP_SOL_SRL_NMBR;

   }


   public String getSpSolSpecs() {

       return SP_SOL_SPCS;

   }

   public void setSpSolSpecs(String SP_SOL_SPCS) {

       this.SP_SOL_SPCS = SP_SOL_SPCS;

   }

   public String getSpSolCustodian() {

       return SP_SOL_CSTDN;

   }

   public void setSpSolCustodian(String SP_SOL_CSTDN) {

       this.SP_SOL_CSTDN = SP_SOL_CSTDN;

   }


   public String getSpSolExpiryDate() {

       return SP_SOL_EXPRY_DT;

   }

   public void setSpSolExpiryDate(String SP_SOL_EXPRY_DT) {

       this.SP_SOL_EXPRY_DT = SP_SOL_EXPRY_DT;

   }

   public String getSpIlCategory() {

	   return SP_IL_CTGRY;

   }

   public void setSpIlCategory(String SP_IL_CTGRY) {

	   this.SP_IL_CTGRY = SP_IL_CTGRY;

   }

   public String getSpSolLocName() {

       return SP_SOL_LOC_NM;

   }

   public void setSpSolLocName(String SP_SOL_LOC_NM) {

       this.SP_SOL_LOC_NM = SP_SOL_LOC_NM;

   }

   public double getSpSolUnitPrice() {

       return SP_SOL_UNT_PRC;

   }

   public void setSpSolUnitPrice(double SP_SOL_UNT_PRC) {

       this.SP_SOL_UNT_PRC = SP_SOL_UNT_PRC;

   }

   public double getSpSolAmount() {

       return SP_SOL_AMNT;

   }

   public void setSpSolAmount(double SP_SOL_AMNT) {

       this.SP_SOL_AMNT = SP_SOL_AMNT;

   }

   public String getSpSoReferenceNumber() {

       return SP_SO_RFRNC_NMBR;

   }

   public void setSpSoReferenceNumber(String SP_SO_RFRNC_NMBR) {

       this.SP_SO_RFRNC_NMBR = SP_SO_RFRNC_NMBR;

   }

   public String getSpSoTransactionType() {

       return SP_SO_TRNSCTN_TYP;

   }

   public void setSpSoTransactionType(String SP_SO_TRNSCTN_TYP) {

       this.SP_SO_TRNSCTN_TYP = SP_SO_TRNSCTN_TYP;

   }

   public String getSpSoDescription() {

       return SP_SO_DESC;

   }

   public void setSpSoDescription(String SP_SO_DESC) {

       this.SP_SO_DESC = SP_SO_DESC;

   }

   public String getSpSoCstCustomerCode() {

   	   return SP_SO_CST_CSTMR_CODE;

   }

   public void setSpSoCstCustomerCode(String SP_SO_CST_CSTMR_CODE) {

   	   this.SP_SO_CST_CSTMR_CODE = SP_SO_CST_CSTMR_CODE;

   }

   public String getSpSoCstName () {

   	   return SP_SO_CST_NM;

   }

   public void setSpSoCstName(String SP_SO_CST_NM) {

   	   this.SP_SO_CST_NM = SP_SO_CST_NM;

   }

   public String getSpSoCstAddress() {

   	   return SP_SO_CST_ADDRSS;

   }

   public void setSpSoCstAddress(String SP_SO_CST_ADDRSS) {

   	   this.SP_SO_CST_ADDRSS = SP_SO_CST_ADDRSS;

   }

   public double getSpSoCstCreditLimit() {

   	   return SP_SO_CST_CRDT_LMT;

   }

   public void setSpSoCstCreditLimit(double SP_SO_CST_CRDT_LMT) {

   	   this.SP_SO_CST_CRDT_LMT = SP_SO_CST_CRDT_LMT;

   }


   public String getSpSoCstContactPerson() {

   	   return SP_SO_CST_CNTCT_PRSN;

   }

   public void setSpSoCstContactPerson(String SP_SO_CST_CNTCT_PRSN) {

   	   this.SP_SO_CST_CNTCT_PRSN = SP_SO_CST_CNTCT_PRSN;

   }


   public String getSpSoCstPhoneNumber() {

   	   return SP_SO_CST_PHN_NMBR;

   }

   public void setSpSoCstPhoneNumber(String SP_SO_CST_PHN_NMBR) {

   	   this.SP_SO_CST_PHN_NMBR = SP_SO_CST_PHN_NMBR;

   }

   public String getSpSoCstMobileNumber() {

   	   return SP_SO_CST_MBL_NMBR;

   }

   public void setSpSoCstMobileNumber(String SP_SO_CST_MBL_NMBR) {

   	   this.SP_SO_CST_MBL_NMBR = SP_SO_CST_MBL_NMBR;

   }


   public String getSpSoCstFax() {

   	   return SP_SO_CST_FX;

   }

   public void setSpSoCstFax(String SP_SO_CST_FX) {

   	   this.SP_SO_CST_FX = SP_SO_CST_FX;

   }

   public String getSpSoCstEmail() {

   	   return SP_SO_CST_EML;

   }

   public void setSpSoCstEmail(String SP_SO_CST_EML) {

   	   this.SP_SO_CST_EML = SP_SO_CST_EML;

   }


   public String getSpSolIiDescription() {

   	   return SP_SOL_II_DESC;

   }

   public void setSpSolIiDescription(String SP_SOL_II_DESC) {

   	   this.SP_SOL_II_DESC = SP_SOL_II_DESC;

   }

   public char getSpSoFcSymbol() {

   	   return SP_SO_FC_SYMBL;

   }

   public void setSpSoFcSymbol(char SP_SO_FC_SYMBL) {

   	   this.SP_SO_FC_SYMBL = SP_SO_FC_SYMBL;

   }

   public double getSpSoCstCbBalance() {

   	   return SP_SO_CST_CB_BLNC;

   }

   public void setSpSoCstCbBalance(double SP_SO_CST_CB_BLNC) {

   	   this.SP_SO_CST_CB_BLNC = SP_SO_CST_CB_BLNC;

   }

   public double getSpSoCstPendingOrder() {

   	   return SP_SO_CST_PNDNG_ORDR;

   }

   public void setSpSoCstPendingOrder(double SP_SO_CST_PNDNG_ORDR) {

   	   this.SP_SO_CST_PNDNG_ORDR = SP_SO_CST_PNDNG_ORDR;

   }

   public double getSpSoAmount() {

   	   return SP_SO_AMNT;

   }

   public void setSpSoAmount(double SP_SO_AMNT) {

   	   this.SP_SO_AMNT = SP_SO_AMNT;

   }

   public double getSpSoAdvanceAmount() {

   	   return SP_SO_ADVNC_AMNT;

   }

   public void setSpSoAdvanceAmount(double SP_SO_ADVNC_AMNT) {

   	   this.SP_SO_ADVNC_AMNT = SP_SO_ADVNC_AMNT;

   }

   public String getSpSolIiStockStatus() {

   	   return SP_SOL_II_STCK_STATUS;

   }

   public void setSpSolIiStockStatus(String SP_SOL_II_STCK_STATUS) {

   	   this.SP_SOL_II_STCK_STATUS = SP_SOL_II_STCK_STATUS;

   }

   public double getSpSoCstLastReceiptAmount() {

   	   return SP_SO_CST_LST_RCPT_AMNT;

   }

   public void setSpSoCstLastReceiptAmount(double SP_SO_CST_LST_RCPT_AMNT) {

   	   this.SP_SO_CST_LST_RCPT_AMNT = SP_SO_CST_LST_RCPT_AMNT;

   }

   public Date getSpSoCstLastReceiptDate() {

   	   return SP_SO_CST_LST_RCPT_DT;

   }

   public void setSpSoCstLastReceiptDate(Date SP_SO_CST_LST_RCPT_DT) {

   	   this.SP_SO_CST_LST_RCPT_DT = SP_SO_CST_LST_RCPT_DT;

   }

   public String getSpSoPaymentTerm() {

   	   return SP_SO_PYMNT_TRM;

   }

   public void setSpSoPaymentTerm(String SP_SO_PYMNT_TRM) {

   	   this.SP_SO_PYMNT_TRM = SP_SO_PYMNT_TRM;

   }

   public double getSpSoAgBucket0() {

   	   return SP_SO_AG_BCKT0;

   }

   public void setSpSoAgBucket0(double SP_SO_AG_BCKT0) {

   	   this.SP_SO_AG_BCKT0 = SP_SO_AG_BCKT0;

   }

   public double getSpSoAgBucket1() {

   	   return SP_SO_AG_BCKT1;

   }

   public void setSpSoAgBucket1(double SP_SO_AG_BCKT1) {

   	   this.SP_SO_AG_BCKT1 = SP_SO_AG_BCKT1;

   }

   public double getSpSoAgBucket2() {

   	   return SP_SO_AG_BCKT2;

   }

   public void setSpSoAgBucket2(double SP_SO_AG_BCKT2) {

   	   this.SP_SO_AG_BCKT2 = SP_SO_AG_BCKT2;

   }

   public double getSpSoAgBucket3() {

   	   return SP_SO_AG_BCKT3;

   }

   public void setSpSoAgBucket3(double SP_SO_AG_BCKT3) {

   	   this.SP_SO_AG_BCKT3 = SP_SO_AG_BCKT3;

   }

   public double getSpSoAgBucket4() {

   	   return SP_SO_AG_BCKT4;

   }

   public void setSpSoAgBucket4(double SP_SO_AG_BCKT4) {

   	   this.SP_SO_AG_BCKT4 = SP_SO_AG_BCKT4;

   }

   public double getSpSoAgBucket5() {

   	   return SP_SO_AG_BCKT5;

   }

   public void setSpSoAgBucket5(double SP_SO_AG_BCKT5) {

   	   this.SP_SO_AG_BCKT5 = SP_SO_AG_BCKT5;

   }

   public String getSpSoCstSlsSalespersonCode() {

   	   return SP_SO_CST_SLS_SLSPRSN_CD;

   }

   public void setSpSoCstSlsSalespersonCode(String SP_SO_CST_SLS_SLSPRSN_CD) {

   	   this.SP_SO_CST_SLS_SLSPRSN_CD = SP_SO_CST_SLS_SLSPRSN_CD;

   }

   public String getSpSoCstSlsName() {

   	   return SP_SO_CST_SLS_NM;

   }

   public void setSpSoCstSlsName(String SP_SO_CST_SLS_NM) {

   	   this.SP_SO_CST_SLS_NM = SP_SO_CST_SLS_NM;

   }

   public String getSpSoCstCity() {

   	   return SP_SO_CST_CTY;

   }

   public void setSpSoCstCity(String SP_SO_CST_CTY) {

   	   this.SP_SO_CST_CTY = SP_SO_CST_CTY;

   }

   public double getSpSolTotalDiscount() {

   	   return SP_SOL_TTL_DSCNT;

   }

   public void setSpSolTotalDiscount(double SP_SOL_II_TTL_DSCNT) {

   	   this.SP_SOL_TTL_DSCNT = SP_SOL_II_TTL_DSCNT;

   }


   public double getSpSolDiscount1() {

   	   return SP_SOL_DSCNT1;

   }

   public void setSpSolDiscount1(double SP_SOL_DSCNT1) {

   	   this.SP_SOL_DSCNT1 = SP_SOL_DSCNT1;

   }

   public double getSpSolDiscount2() {

   	   return SP_SOL_DSCNT2;

   }

   public void setSpSolDiscount2(double SP_SOL_DSCNT2) {

   	   this.SP_SOL_DSCNT2 = SP_SOL_DSCNT2;

   }

   public double getSpSolDiscount3() {

   	   return SP_SOL_DSCNT3;

   }

   public void setSpSolDiscount3(double SP_SOL_DSCNT3) {

   	   this.SP_SOL_DSCNT3 = SP_SOL_DSCNT3;

   }

   public double getSpSolDiscount4() {

   	   return SP_SOL_DSCNT4;

   }

   public void setSpSolDiscount4(double SP_SOL_DSCNT4) {

   	   this.SP_SOL_DSCNT4 = SP_SOL_DSCNT4;

   }

	public double getSpSoTaxRate() {

		return SP_SO_TX_RT;

	}

	public void setSpSoTaxRate(double SP_SO_TX_RT) {

		this.SP_SO_TX_RT = SP_SO_TX_RT;

	}

	public double getSpSolUnitPriceWoTax() {

		return SP_SOL_UNT_CST_WO_TX;

	}

	public void setSpSolUnitCostWoTax(double SP_SOL_UNT_CST_WO_TX) {

		this.SP_SOL_UNT_CST_WO_TX = SP_SOL_UNT_CST_WO_TX;

	}

	public double getSpSolTaxAmount() {

		return SP_SOL_TX_AMNT;

	}

	public void setSpSolTaxAmount(double SP_SOL_TX_AMNT) {

		this.SP_SOL_TX_AMNT = SP_SOL_TX_AMNT;

	}

	public String getSpSoTaxType() {

		return SP_SO_TX_TYP;

	}

	public void setSpSoTaxType(String SP_SO_TX_TYP) {

		this.SP_SO_TX_TYP = SP_SO_TX_TYP;

	}

	public double getSpSolQuantityOnHand() {

		return SP_SOL_QOH;

	}

	public void setSpSolQuantityOnHand (double SP_SOL_QOH) {

		this.SP_SOL_QOH = SP_SOL_QOH;

	}

	public String getSpSoCustomerDealPrice(){

		return SP_SO_CST_DL_PRC;
	}

	public void setSpSoCustomerDealPrice(String SP_SO_CST_DL_PRC){
		this.SP_SO_CST_DL_PRC = SP_SO_CST_DL_PRC;
	}

	public String getSpSoMemo(){

		return SP_SO_MEMO;
	}

	public void setSpSoMemo(String SP_SO_MEMO){
		this.SP_SO_MEMO = SP_SO_MEMO;
	}

	public String getSpReportParameter(){
		return SP_REPORT_PARAMETER;
	}

	public void setSpReportParameter(String SP_REPORT_PARAMETER){
		this.SP_REPORT_PARAMETER = SP_REPORT_PARAMETER;
	}
	
	   public byte getSpSoPosted(){
        return SP_SO_PSTD;
    }

    public void setSpSoPosted(byte SP_SO_PSTD){
        this.SP_SO_PSTD = SP_SO_PSTD;
    }


 }  // ArRepSalesOrderPrintDetails