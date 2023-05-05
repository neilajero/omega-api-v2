/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Date;

public class ArRepJobOrderPrintDetails implements Cloneable, java.io.Serializable  {

	   private String JP_JA_PRSNL_ID_NMBR;
	   private String JP_JA_PRSNL_NM;
	   private double JP_JA_QTY;
	   private double JP_JA_UNT_CST;
	   private double JP_JA_AMNT;
	   private String JP_JA_RMRKS;
	   private String JP_JO_JB_ORDR_STTS;
	   private Date JP_JO_DT;
	   private String JP_JO_RFRNC_NMBR;
	   private String JP_JO_TRNSCTN_TYP;
	   private String JP_JO_DCMNT_NMBR;
	   private String JP_JO_DESC;
	   private String JP_JO_CRTD_BY;
	   private String JP_JO_APPRVD_RJCTD_BY;
	   private String JP_JO_BLL_TO;
	   private String JP_JO_SHP_TO;
	   private String JP_JO_TCHNCN;
	   private double JP_JOL_QTY;
	   private String JP_JOL_UOM;
	   private String JP_JOL_II_CODE;
	   private String JP_JOL_PRPRTY_CODE;
	   private String JP_JOL_SRL_NMBR;
	   private String JP_JOL_SPCS;
	   private String JP_JOL_CSTDN;
	   private String JP_JOL_EXPRY_DT;
	   private String SP_IL_CTGRY;
	   private String JP_JOL_LOC_NM;
	   private double JP_JOL_UNT_PRC;
	   private double JP_JOL_AMNT;
	   private String JP_JO_CST_CSTMR_CODE;
	   private String JP_JO_CST_NM;
	   private String JP_JO_CST_ADDRSS;
	   private double JP_JO_CST_CRDT_LMT;

	   private String JP_JO_CST_CNTCT_PRSN;
	   private String JP_JO_CST_PHN_NMBR;
	   private String JP_JO_CST_MBL_NMBR;
	   private String JP_JO_CST_FX;
	   private String JP_JO_CST_EML;

	   private String JP_JOL_II_DESC;
	   private char JP_JO_FC_SYMBL;
	   private double JP_JO_CST_CB_BLNC;
	   private double JP_JO_CST_PNDNG_ORDR;
	   private double JP_JO_AMNT;
	   private double JP_JO_ADVNC_AMNT;
	   private String JP_JOL_II_STCK_STATUS;
	   private double JP_JO_CST_LST_RCPT_AMNT;
	   private Date JP_JO_CST_LST_RCPT_DT;
	   private String JP_JO_PYMNT_TRM;
	   private double JP_JO_AG_BCKT0;
	   private double JP_JO_AG_BCKT1;
	   private double JP_JO_AG_BCKT2;
	   private double JP_JO_AG_BCKT3;
	   private double JP_JO_AG_BCKT4;
	   private double JP_JO_AG_BCKT5;
	   private String JP_JO_CST_SLS_SLSPRSN_CD;
	   private String JP_JO_CST_SLS_NM;
	   private String JP_JO_CST_CTY;
	   private double JP_JOL_TTL_DSCNT;
	   private double JP_JOL_DSCNT1;
	   private double JP_JOL_DSCNT2;
	   private double JP_JOL_DSCNT3;
	   private double JP_JOL_DSCNT4;
	   private double JP_JO_TX_RT;
	   private double JP_JOL_UNT_CST_WO_TX;
	   private double JP_JOL_TX_AMNT;
	   private String JP_JO_TX_TYP;
	   private double JP_JOL_QOH;
	   private String JP_JO_CST_DL_PRC;
	   private String JP_JO_MEMO;
	   
	
	   
	   
	   private String JP_REPORT_PARAMETER;

   public ArRepJobOrderPrintDetails() {
   }
   
   public Object clone() throws CloneNotSupportedException {
  	 

 		try {
 		   return super.clone();
 		 }
 		  catch (CloneNotSupportedException e) {
 			  throw e;
 		
 		  }	
 	 }


   public String getJpJaPeIdNumber() {
	return JP_JA_PRSNL_ID_NMBR;
	}
	
	
	public void setJpJaPeIdNumber(String jP_JA_PRSNL_ID_NMBR) {
		JP_JA_PRSNL_ID_NMBR = jP_JA_PRSNL_ID_NMBR;
	}
	
	
	public String getJpJaPeName() {
		return JP_JA_PRSNL_NM;
	}
	
	
	
	public void setJpJaPeName(String jP_JA_PRSNL_NM) {
		JP_JA_PRSNL_NM = jP_JA_PRSNL_NM;
	}
	
	
	
	public double getJpJaQuantity() {
		return JP_JA_QTY;
	}
	
	
	
	public void setJpJaQuantity(double jP_JA_QTY) {
		JP_JA_QTY = jP_JA_QTY;
	}
	
	
	
	public double getJpJaUnitCost() {
		return JP_JA_UNT_CST;
	}
	
	
	public void setJpJaUnitCost(double jP_JA_UNT_CST) {
		JP_JA_UNT_CST = jP_JA_UNT_CST;
	}
	
	
	
	public double getJpJaAmount() {
		return JP_JA_AMNT;
	}
	
	
	
	public void setJpJaAmount(double jP_JA_AMNT) {
		JP_JA_AMNT = jP_JA_AMNT;
	}
	
	
	
	public String getJpJaRemarks() {
		return JP_JA_RMRKS;
	}
	
	
	public void setJpJaRemarks(String jP_JA_RMRKS) {
		JP_JA_RMRKS = jP_JA_RMRKS;
	}
	
	public String getJpJoJobOrderStatus() {
		return JP_JO_JB_ORDR_STTS;
	}
	
	
	public void setJpJoJobOrderStatus(String JP_JO_JB_ORDR_STTS) {
		this.JP_JO_JB_ORDR_STTS = JP_JO_JB_ORDR_STTS;
	}
	
	
	public Date getJpJoDate() {
	
	       return JP_JO_DT;
	
	}
	
	public void setJpJoDate(Date JP_JO_DT) {
	
	       this.JP_JO_DT = JP_JO_DT;
	
	}
	
	public String getJpJoDocumentNumber() {
	
	    return JP_JO_DCMNT_NMBR;
	
	}
	
	public void setJpJoDocumentNumber(String JP_JO_DCMNT_NMBR) {
	
	    this.JP_JO_DCMNT_NMBR = JP_JO_DCMNT_NMBR;
	
	}
	
	public String getJpJoCreatedBy() {
	
	    return JP_JO_CRTD_BY;
	
	}
	
	public void setJpJoCreatedBy(String JP_JO_CRTD_BY) {
	
	    this.JP_JO_CRTD_BY = JP_JO_CRTD_BY;
	
	}
	
	public String getJpJoApprovedRejectedBy() {
	
	    return JP_JO_APPRVD_RJCTD_BY;
	
	}
	
	public void setJpJoApprovedRejectedBy(String JP_JO_APPRVD_RJCTD_BY) {
	
	    this.JP_JO_APPRVD_RJCTD_BY = JP_JO_APPRVD_RJCTD_BY;
	
	}
	
	public String getJpJoBillTo() {
	
	    return JP_JO_BLL_TO;
	
	}
	
	public void setJpJoBillTo(String JP_JO_BLL_TO) {
	
	    this.JP_JO_BLL_TO = JP_JO_BLL_TO;
	
	}
	
	
	public String getJpJoShipTo() {
	
	    return JP_JO_SHP_TO;
	
	}
	
	public void setJpJoShipTo(String JP_JO_SHP_TO) {
	
	    this.JP_JO_SHP_TO = JP_JO_SHP_TO;
	
	}
	
	
	public String getJpJoTechnician() {
		
	    return JP_JO_TCHNCN;
	
	}
	
	public void setJpJoTechnician(String JP_JO_TCHNCN) {
	
	    this.JP_JO_TCHNCN = JP_JO_TCHNCN;
	
	}
	
	
	
	public double getJpJolQuantity() {
	
	    return JP_JOL_QTY;
	
	}
	
	public void setJpJolQuantity(double JP_JOL_QTY) {
	
	    this.JP_JOL_QTY = JP_JOL_QTY;
	
	}
	
	public String getJpJolUom() {
	
	    return JP_JOL_UOM;
	
	}
	
	public void setJpJolUom(String JP_JOL_UOM) {
	
	    this.JP_JOL_UOM = JP_JOL_UOM;
	
	}
	
	public String getJpJolIiCode() {
	
	    return JP_JOL_II_CODE;
	
	}
	
	public void setJpJolIiCode(String JP_JOL_II_CODE) {
	
	    this.JP_JOL_II_CODE = JP_JOL_II_CODE;
	
	}
	
	public String getJpJolPropertyCode() {
	
	    return JP_JOL_PRPRTY_CODE;
	
	}
	
	public void setJpJolPropertyCode(String JP_JOL_PRPRTY_CODE) {
	
	    this.JP_JOL_PRPRTY_CODE = JP_JOL_PRPRTY_CODE;
	
	}
	
	
	
	public String getJpJolSerialNumber() {
	
	    return JP_JOL_SRL_NMBR;
	
	}
	
	public void setJpJolSerialNumber(String JP_JOL_SRL_NMBR) {
	
	    this.JP_JOL_SRL_NMBR = JP_JOL_SRL_NMBR;
	
	}
	
	
	public String getJpJolSpecs() {
	
	    return JP_JOL_SPCS;
	
	}
	
	public void setJpJolSpecs(String JP_JOL_SPCS) {
	
	    this.JP_JOL_SPCS = JP_JOL_SPCS;
	
	}
	
	public String getJpJolCustodian() {
	
	    return JP_JOL_CSTDN;
	
	}
	
	public void setJpJolCustodian(String JP_JOL_CSTDN) {
	
	    this.JP_JOL_CSTDN = JP_JOL_CSTDN;
	
	}
	
	
	public String getJpJolExpiryDate() {
	
	    return JP_JOL_EXPRY_DT;
	
	}
	
	public void setJpJolExpiryDate(String JP_JOL_EXPRY_DT) {
	
	    this.JP_JOL_EXPRY_DT = JP_JOL_EXPRY_DT;
	
	}
	
	public String getSpIlCategory() {
	
	        return SP_IL_CTGRY;
	
	}
	
	public void setSpIlCategory(String SP_IL_CTGRY) {
	
	        this.SP_IL_CTGRY = SP_IL_CTGRY;
	
	}
	
	public String getJpJolLocName() {
	
	    return JP_JOL_LOC_NM;
	
	}
	
	public void setJpJolLocName(String JP_JOL_LOC_NM) {
	
	    this.JP_JOL_LOC_NM = JP_JOL_LOC_NM;
	
	}
	
	public double getJpJolUnitPrice() {
	
	    return JP_JOL_UNT_PRC;
	
	}
	
	public void setJpJolUnitPrice(double JP_JOL_UNT_PRC) {
	
	    this.JP_JOL_UNT_PRC = JP_JOL_UNT_PRC;
	
	}
	
	public double getJpJolAmount() {
	
	    return JP_JOL_AMNT;
	
	}
	
	public void setJpJolAmount(double JP_JOL_AMNT) {
	
	    this.JP_JOL_AMNT = JP_JOL_AMNT;
	
	}
	
	public String getJpJoReferenceNumber() {
	
	    return JP_JO_RFRNC_NMBR;
	
	}
	
	public void setJpJoReferenceNumber(String JP_JO_RFRNC_NMBR) {
	
	    this.JP_JO_RFRNC_NMBR = JP_JO_RFRNC_NMBR;
	
	}
	
	public String getJpJoTransactionType() {
	
	    return JP_JO_TRNSCTN_TYP;
	
	}
	
	public void setJpJoTransactionType(String JP_JO_TRNSCTN_TYP) {
	
	    this.JP_JO_TRNSCTN_TYP = JP_JO_TRNSCTN_TYP;
	
	}
	
	public String getJpJoDescription() {
	
	    return JP_JO_DESC;
	
	}
	
	public void setJpJoDescription(String JP_JO_DESC) {
	
	    this.JP_JO_DESC = JP_JO_DESC;
	
	}
	
	public String getJpJoCstCustomerCode() {
	
	        return JP_JO_CST_CSTMR_CODE;
	
	}
	
	public void setJpJoCstCustomerCode(String JP_JO_CST_CSTMR_CODE) {
	
	        this.JP_JO_CST_CSTMR_CODE = JP_JO_CST_CSTMR_CODE;
	
	}
	
	public String getJpJoCstName () {
	
	        return JP_JO_CST_NM;
	
	}
	
	public void setJpJoCstName(String JP_JO_CST_NM) {
	
	        this.JP_JO_CST_NM = JP_JO_CST_NM;
	
	}
	
	public String getJpJoCstAddress() {
	
	        return JP_JO_CST_ADDRSS;
	
	}
	
	public void setJpJoCstAddress(String JP_JO_CST_ADDRSS) {
	
	        this.JP_JO_CST_ADDRSS = JP_JO_CST_ADDRSS;
	
	}
	
	public double getJpJoCstCreditLimit() {
	
	        return JP_JO_CST_CRDT_LMT;
	
	}
	
	public void setJpJoCstCreditLimit(double JP_JO_CST_CRDT_LMT) {
	
	        this.JP_JO_CST_CRDT_LMT = JP_JO_CST_CRDT_LMT;
	
	}
	
	
	public String getJpJoCstContactPerson() {
	
	        return JP_JO_CST_CNTCT_PRSN;
	
	}
	
	public void setJpJoCstContactPerson(String JP_JO_CST_CNTCT_PRSN) {
	
	        this.JP_JO_CST_CNTCT_PRSN = JP_JO_CST_CNTCT_PRSN;
	
	}
	
	
	public String getJpJoCstPhoneNumber() {
	
	        return JP_JO_CST_PHN_NMBR;
	
	}
	
	public void setJpJoCstPhoneNumber(String JP_JO_CST_PHN_NMBR) {
	
	        this.JP_JO_CST_PHN_NMBR = JP_JO_CST_PHN_NMBR;
	
	}
	
	public String getJpJoCstMobileNumber() {
	
	        return JP_JO_CST_MBL_NMBR;
	
	}
	
	public void setJpJoCstMobileNumber(String JP_JO_CST_MBL_NMBR) {
	
	        this.JP_JO_CST_MBL_NMBR = JP_JO_CST_MBL_NMBR;
	
	}
	
	
	public String getJpJoCstFax() {
	
	        return JP_JO_CST_FX;
	
	}
	
	public void setJpJoCstFax(String JP_JO_CST_FX) {
	
	        this.JP_JO_CST_FX = JP_JO_CST_FX;
	
	}
	
	public String getJpJoCstEmail() {
	
	        return JP_JO_CST_EML;
	
	}
	
	public void setJpJoCstEmail(String JP_JO_CST_EML) {
	
	        this.JP_JO_CST_EML = JP_JO_CST_EML;
	
	}
	
	
	public String getJpJolIiDescription() {
	
	        return JP_JOL_II_DESC;
	
	}
	
	public void setJpJolIiDescription(String JP_JOL_II_DESC) {
	
	        this.JP_JOL_II_DESC = JP_JOL_II_DESC;
	
	}
	
	public char getJpJoFcSymbol() {
	
	        return JP_JO_FC_SYMBL;
	
	}
	
	public void setJpJoFcSymbol(char JP_JO_FC_SYMBL) {
	
	        this.JP_JO_FC_SYMBL = JP_JO_FC_SYMBL;
	
	}
	
	public double getJpJoCstCbBalance() {
	
	        return JP_JO_CST_CB_BLNC;
	
	}
	
	public void setJpJoCstCbBalance(double JP_JO_CST_CB_BLNC) {
	
	        this.JP_JO_CST_CB_BLNC = JP_JO_CST_CB_BLNC;
	
	}
	
	public double getJpJoCstPendingOrder() {
	
	        return JP_JO_CST_PNDNG_ORDR;
	
	}
	
	public void setJpJoCstPendingOrder(double JP_JO_CST_PNDNG_ORDR) {
	
	        this.JP_JO_CST_PNDNG_ORDR = JP_JO_CST_PNDNG_ORDR;
	
	}
	
	public double getJpJoAmount() {
	
	        return JP_JO_AMNT;
	
	}
	
	public void setJpJoAmount(double JP_JO_AMNT) {
	
	        this.JP_JO_AMNT = JP_JO_AMNT;
	
	}
	
	public double getJpJoAdvanceAmount() {
	
	        return JP_JO_ADVNC_AMNT;
	
	}
	
	public void setJpJoAdvanceAmount(double JP_JO_ADVNC_AMNT) {
	
	        this.JP_JO_ADVNC_AMNT = JP_JO_ADVNC_AMNT;
	
	}
	
	public String getJpJolIiStockStatus() {
	
	        return JP_JOL_II_STCK_STATUS;
	
	}
	
	public void setJpJolIiStockStatus(String JP_JOL_II_STCK_STATUS) {
	
	        this.JP_JOL_II_STCK_STATUS = JP_JOL_II_STCK_STATUS;
	
	}
	
	public double getJpJoCstLastReceiptAmount() {
	
	        return JP_JO_CST_LST_RCPT_AMNT;
	
	}
	
	public void setJpJoCstLastReceiptAmount(double JP_JO_CST_LST_RCPT_AMNT) {
	
	        this.JP_JO_CST_LST_RCPT_AMNT = JP_JO_CST_LST_RCPT_AMNT;
	
	}
	
	public Date getJpJoCstLastReceiptDate() {
	
	        return JP_JO_CST_LST_RCPT_DT;
	
	}
	
	public void setJpJoCstLastReceiptDate(Date JP_JO_CST_LST_RCPT_DT) {
	
	        this.JP_JO_CST_LST_RCPT_DT = JP_JO_CST_LST_RCPT_DT;
	
	}
	
	public String getJpJoPaymentTerm() {
	
	        return JP_JO_PYMNT_TRM;
	
	}
	
	public void setJpJoPaymentTerm(String JP_JO_PYMNT_TRM) {
	
	        this.JP_JO_PYMNT_TRM = JP_JO_PYMNT_TRM;
	
	}
	
	public double getJpJoAgBucket0() {
	
	        return JP_JO_AG_BCKT0;
	
	}
	
	public void setJpJoAgBucket0(double JP_JO_AG_BCKT0) {
	
	        this.JP_JO_AG_BCKT0 = JP_JO_AG_BCKT0;
	
	}
	
	public double getJpJoAgBucket1() {
	
	        return JP_JO_AG_BCKT1;
	
	}
	
	public void setJpJoAgBucket1(double JP_JO_AG_BCKT1) {
	
	        this.JP_JO_AG_BCKT1 = JP_JO_AG_BCKT1;
	
	}
	
	public double getJpJoAgBucket2() {
	
	        return JP_JO_AG_BCKT2;
	
	}
	
	public void setJpJoAgBucket2(double JP_JO_AG_BCKT2) {
	
	        this.JP_JO_AG_BCKT2 = JP_JO_AG_BCKT2;
	
	}
	
	public double getJpJoAgBucket3() {
	
	        return JP_JO_AG_BCKT3;
	
	}
	
	public void setJpJoAgBucket3(double JP_JO_AG_BCKT3) {
	
	        this.JP_JO_AG_BCKT3 = JP_JO_AG_BCKT3;
	
	}
	
	public double getJpJoAgBucket4() {
	
	        return JP_JO_AG_BCKT4;
	
	}
	
	public void setJpJoAgBucket4(double JP_JO_AG_BCKT4) {
	
	        this.JP_JO_AG_BCKT4 = JP_JO_AG_BCKT4;
	
	}
	
	public double getJpJoAgBucket5() {
	
	        return JP_JO_AG_BCKT5;
	
	}
	
	public void setJpJoAgBucket5(double JP_JO_AG_BCKT5) {
	
	        this.JP_JO_AG_BCKT5 = JP_JO_AG_BCKT5;
	
	}
	
	public String getJpJoCstSlsSalespersonCode() {
	
	        return JP_JO_CST_SLS_SLSPRSN_CD;
	
	}
	
	public void setJpJoCstSlsSalespersonCode(String JP_JO_CST_SLS_SLSPRSN_CD) {
	
	        this.JP_JO_CST_SLS_SLSPRSN_CD = JP_JO_CST_SLS_SLSPRSN_CD;
	
	}
	
	public String getJpJoCstSlsName() {
	
	        return JP_JO_CST_SLS_NM;
	
	}
	
	public void setJpJoCstSlsName(String JP_JO_CST_SLS_NM) {
	
	        this.JP_JO_CST_SLS_NM = JP_JO_CST_SLS_NM;
	
	}
	
	public String getJpJoCstCity() {
	
	        return JP_JO_CST_CTY;
	
	}
	
	public void setJpJoCstCity(String JP_JO_CST_CTY) {
	
	        this.JP_JO_CST_CTY = JP_JO_CST_CTY;
	
	}
	
	public double getJpJolTotalDiscount() {
	
	        return JP_JOL_TTL_DSCNT;
	
	}
	
	public void setJpJolTotalDiscount(double JP_JOL_II_TTL_DSCNT) {
	
	        this.JP_JOL_TTL_DSCNT = JP_JOL_II_TTL_DSCNT;
	
	}
	
	
	public double getJpJolDiscount1() {
	
	        return JP_JOL_DSCNT1;
	
	}
	
	public void setJpJolDiscount1(double JP_JOL_DSCNT1) {
	
	        this.JP_JOL_DSCNT1 = JP_JOL_DSCNT1;
	
	}
	
	public double getJpJolDiscount2() {
	
	        return JP_JOL_DSCNT2;
	
	}
	
	public void setJpJolDiscount2(double JP_JOL_DSCNT2) {
	
	        this.JP_JOL_DSCNT2 = JP_JOL_DSCNT2;
	
	}
	
	public double getJpJolDiscount3() {
	
	        return JP_JOL_DSCNT3;
	
	}
	
	public void setJpJolDiscount3(double JP_JOL_DSCNT3) {
	
	        this.JP_JOL_DSCNT3 = JP_JOL_DSCNT3;
	
	}
	
	public double getJpJolDiscount4() {
	
	        return JP_JOL_DSCNT4;
	
	}
	
	public void setJpJolDiscount4(double JP_JOL_DSCNT4) {
	
	        this.JP_JOL_DSCNT4 = JP_JOL_DSCNT4;
	
	}

     public double getJpJoTaxRate() {

             return JP_JO_TX_RT;

     }

     public void setJpJoTaxRate(double JP_JO_TX_RT) {

             this.JP_JO_TX_RT = JP_JO_TX_RT;

     }

     public double getJpJolUnitPriceWoTax() {

             return JP_JOL_UNT_CST_WO_TX;

     }

     public void setJpJolUnitCostWoTax(double JP_JOL_UNT_CST_WO_TX) {

             this.JP_JOL_UNT_CST_WO_TX = JP_JOL_UNT_CST_WO_TX;

     }

     public double getJpJolTaxAmount() {

             return JP_JOL_TX_AMNT;

     }

     public void setJpJolTaxAmount(double JP_JOL_TX_AMNT) {

             this.JP_JOL_TX_AMNT = JP_JOL_TX_AMNT;

     }

     public String getJpJoTaxType() {

             return JP_JO_TX_TYP;

     }

     public void setJpJoTaxType(String JP_JO_TX_TYP) {

             this.JP_JO_TX_TYP = JP_JO_TX_TYP;

     }

     public double getJpJolQuantityOnHand() {

             return JP_JOL_QOH;

     }

     public void setJpJolQuantityOnHand (double JP_JOL_QOH) {

             this.JP_JOL_QOH = JP_JOL_QOH;

     }

     public String getJpJoCustomerDealPrice(){

             return JP_JO_CST_DL_PRC;
     }

     public void setJpJoCustomerDealPrice(String JP_JO_CST_DL_PRC){
             this.JP_JO_CST_DL_PRC = JP_JO_CST_DL_PRC;
     }

     public String getJpJoMemo(){

             return JP_JO_MEMO;
     }

     public void setJpJoMemo(String JP_JO_MEMO){
             this.JP_JO_MEMO = JP_JO_MEMO;
     }

     public String getJpReportParameter(){
             return JP_REPORT_PARAMETER;
     }

     public void setJpReportParameter(String JP_REPORT_PARAMETER){
             this.JP_REPORT_PARAMETER = JP_REPORT_PARAMETER;
     }
     
   

     
     
     
   
 }  // ArRepJobOrderPrintDetails