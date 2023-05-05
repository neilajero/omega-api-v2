/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.util.Date;

public class ApRepPurchaseOrderPrintDetails implements java.io.Serializable {

	private Date POP_PO_DT;
	private Date POP_PO_DLVRY_PRD;
	private String POP_PO_SPPLR_NM;
	private String POP_PO_CNTCT_PRSN;
	private String POP_PO_CNTCT_NMBR;
	private String POP_PO_DCMNT_NMBR;
	private String POP_PO_RFRNC_NMBR;
	private String POP_PO_CRTD_BY;
	private String POP_PO_APPRVD_BY;
	private String POP_PO_APPRVD_RJCTD_BY;
	private String POP_PO_CHCKD_BY;
	private String POP_PO_DESC;
	private String POP_PL_II_NM;
	private String POP_PL_II_DESC;
	private String POP_PL_II_PRT_NMBR;
	private String POP_PL_II_BR_CODE1;
	private String POP_PL_II_BR_CODE2;
	private String POP_PL_II_BR_CODE3;
	private String POP_PL_II_BRND;
	private String POP_PL_LOC_NM;
	private String POP_PL_UOM;
	private double POP_PL_QTY;
	private double POP_PL_UNT_CST;
	private double POP_PL_AMNT;
	private double POP_PL_TOTAL_AMNT;

	private String POP_PL_PRPRTY_CD;
	private String POP_PL_SRL_NMBR;
	private String POP_PL_SPCS;
	private String POP_PL_CSTDN;
	private String POP_PL_EXPRY_DT;

	private String POP_PO_TOTAL_AMNT_IN_WRDS;
	private double POP_PL_ENDNG_BLNC;
	private String POP_PO_SHP_TO;
	private String POP_PO_PYT_TRM_NM;
	private double POP_PL_DSCNT1;
	private double POP_PL_DSCNT2;
	private double POP_PL_DSCNT3;
	private double POP_PL_DSCNT4;
	private double POP_PL_TTL_DSCNT;
	private String POP_PL_DSCNT;
	private String POP_PO_BLL_TO;
	private String POP_PL_UOM_SHRT_NM;
	private double POP_PO_TX_RT;
	private double POP_PL_UNT_CST_WO_TX;
	private double POP_PL_TX_AMNT;
	private String POP_PO_TX_TYP;
	private String POP_PO_CRRNCY;
	private String POP_PO_SPPLR_ADDRSS;
	private String POP_PO_SPPLR_CODE;
	private double POP_PL_UNT_CST_TX_INCLSV;
	private String POP_PO_BRNCH_NM;
	private String POP_PO_BRNCH_CODE;
	private String POP_APPRVD_RJCTD_BY;
	private String POP_PO_SPPLR_PHN_NMBR;
	private String POP_PO_SPPLR_FX_NMBR;
	private String POP_PO_USR_RSPNSBLTY;
	private String POP_PO_APPRVL_STTS;
	private String POP_PO_MISC1;
	private String POP_PO_MISC2;
	private String POP_PO_MISC3;
	private String POP_PO_MISC4;
	private String POP_PO_MISC5;
	private String POP_PO_MISC6;
	private boolean POP_PL_ISSERVICE;
	private byte POP_PO_PSTD;
	private String POP_PL_RMRKS;
	private String POP_DPRTMNT;

	public ApRepPurchaseOrderPrintDetails() {
    }


	public Boolean getPopPlIsService() {

		return POP_PL_ISSERVICE;

	}

	public void setPopPlIsService(Boolean POP_PL_ISSERVICE) {

		this.POP_PL_ISSERVICE = POP_PL_ISSERVICE;

	}
	
	public byte getPopPoPosted() {

        return POP_PO_PSTD;

    }

    public void setPopPoPosted(byte POP_PO_PSTD) {

        this.POP_PO_PSTD = POP_PO_PSTD;

    }
	public Date getPopPoDate() {

		return POP_PO_DT;

	}

	public void setPopPoDate(Date POP_PO_DT) {

		this.POP_PO_DT = POP_PO_DT;

	}


	public Date getPopPoDeliveryPeriod() {

		return POP_PO_DLVRY_PRD;

	}

	public void setPopPoDeliveryPeriod(Date POP_PO_DLVRY_PRD) {

		this.POP_PO_DLVRY_PRD = POP_PO_DLVRY_PRD;

	}

	public String getPopPoSupplierName() {

		return POP_PO_SPPLR_NM;

	}

	public void setPopPoSupplierName(String POP_PO_SPPLR_NM) {

		this.POP_PO_SPPLR_NM = POP_PO_SPPLR_NM;

	}

	public String getPopPoContactPerson() {

		return POP_PO_CNTCT_PRSN;

	}

	public void setPopPoContactPerson(String POP_PO_CNTCT_PRSN) {

		this.POP_PO_CNTCT_PRSN = POP_PO_CNTCT_PRSN;

	}

	public String getPopPoContactNumber() {

		return POP_PO_CNTCT_NMBR;

	}

	public void setPopPoContactNumber(String POP_PO_CNTCT_NMBR) {

		this.POP_PO_CNTCT_NMBR = POP_PO_CNTCT_NMBR;

	}

	public String getPopPoDocumentNumber() {

		return POP_PO_DCMNT_NMBR;

	}

	public void setPopPoDocumentNumber(String POP_PO_DCMNT_NMBR) {

		this.POP_PO_DCMNT_NMBR = POP_PO_DCMNT_NMBR;

	}

	public String getPopPoReferenceNumber() {

		return POP_PO_RFRNC_NMBR;

	}

	public void setPopPoReferenceNumber(String POP_PO_RFRNC_NMBR) {

		this.POP_PO_RFRNC_NMBR = POP_PO_RFRNC_NMBR;

	}

	public String getPopPoCreatedBy() {

		return POP_PO_CRTD_BY;

	}

	public void setPopPoCreatedBy(String POP_PO_CRTD_BY) {

		this.POP_PO_CRTD_BY = POP_PO_CRTD_BY;

	}
	
	public String getPopPoApprovedBy() {

        return POP_PO_APPRVD_BY;

    }

    public void setPopPoApprovedBy(String POP_PO_APPRVD_BY) {

        this.POP_PO_APPRVD_BY = POP_PO_APPRVD_BY;

    }

	public String getPopPoApprovedRejectedBy() {

		return POP_PO_APPRVD_RJCTD_BY;

	}

	public void setPopPoApprovedRejectedBy(String POP_PO_APPRVD_RJCTD_BY) {

		this.POP_PO_APPRVD_RJCTD_BY = POP_PO_APPRVD_RJCTD_BY;

	}

	public String getPopPoCheckedBy() {

		return POP_PO_CHCKD_BY;

	}

	public void setPopPoCheckedBy(String POP_PO_CHCKD_BY) {

		this.POP_PO_CHCKD_BY = POP_PO_CHCKD_BY;

	}

	public String getPopPoDescription() {

		return POP_PO_DESC;

	}

	public void setPopPoDescription(String POP_PO_DESC) {

		this.POP_PO_DESC = POP_PO_DESC;

	}

	public String getPopPllIiName() {

		return POP_PL_II_NM;

	}

	public void setPopPllIiName(String POP_PL_II_NM) {

		this.POP_PL_II_NM = POP_PL_II_NM;

	}

	public String getPopPlIiDescription() {

		return POP_PL_II_DESC;

	}

	public void setPopPlIiDescription(String POP_PL_II_DESC) {

		this.POP_PL_II_DESC = POP_PL_II_DESC;

	}


	public String getPopPlIiPartNumber() {

		return POP_PL_II_PRT_NMBR;

	}

	public void setPopPlIiPartNumber(String POP_PL_II_PRT_NMBR) {

		this.POP_PL_II_PRT_NMBR = POP_PL_II_PRT_NMBR;

	}


	public String getPopPlIiBarCode1() {

		return POP_PL_II_BR_CODE1;

	}

	public void setPopPlIiBarCode1(String POP_PL_II_BR_CODE1) {

		this.POP_PL_II_BR_CODE1 = POP_PL_II_BR_CODE1;

	}

	public String getPopPlIiBarCode2() {

		return POP_PL_II_BR_CODE2;

	}

	public void setPopPlIiBarCode2(String POP_PL_II_BR_CODE2) {

		this.POP_PL_II_BR_CODE2 = POP_PL_II_BR_CODE2;

	}


	public String getPopPlIiBarCode3() {

		return POP_PL_II_BR_CODE3;

	}

	public void setPopPlIiBarCode3(String POP_PL_II_BR_CODE3) {

		this.POP_PL_II_BR_CODE3 = POP_PL_II_BR_CODE3;

	}

	public String getPopPlIiBrand() {

		return POP_PL_II_BRND;

	}

	public void setPopPlIiBrand(String POP_PL_II_BRND) {

		this.POP_PL_II_BRND = POP_PL_II_BRND;

	}
	public String getPopPlLocName() {

		return POP_PL_LOC_NM;

	}

	public void setPopPlLocName(String POP_PL_LOC_NM) {

		this.POP_PL_LOC_NM = POP_PL_LOC_NM;

	}

	public String getPopPlUomName() {

		return POP_PL_UOM;

	}

	public void setPopPlUomName(String POP_PL_UOM) {

		this.POP_PL_UOM = POP_PL_UOM;

	}

	public double getPopPlQuantity() {

		return POP_PL_QTY;

	}

	public void setPopPlQuantity(double POP_PL_QTY) {

		this.POP_PL_QTY = POP_PL_QTY;

	}

	public double getPopPlUnitCost() {

		return POP_PL_UNT_CST;

	}

	public void setPopPlUnitCost(double POP_PL_UNT_CST) {

		this.POP_PL_UNT_CST = POP_PL_UNT_CST;

	}

	public double getPopPlAmount() {

		return POP_PL_AMNT;

	}

	public void setPopPlAmount(double POP_PL_AMNT) {

		this.POP_PL_AMNT = POP_PL_AMNT;

	}

	public double getPopPlTotalAmount() {

		return POP_PL_TOTAL_AMNT;

	}

	public void setPopPlTotalAmount(double POP_PL_TOTAL_AMNT) {

		this.POP_PL_TOTAL_AMNT = POP_PL_TOTAL_AMNT;

	}


	public String getPopPlPropertyCode() {

		return POP_PL_PRPRTY_CD;

	}

	public void setPopPlPropertyCode(String POP_PL_PRPRTY_CD) {

		this.POP_PL_PRPRTY_CD = POP_PL_PRPRTY_CD;

	}

	public String getPopPlSerialNumber() {

		return POP_PL_SRL_NMBR;

	}

	public void setPopPlSerialNumber(String POP_PL_SRL_NMBR) {

		this.POP_PL_SRL_NMBR = POP_PL_SRL_NMBR;

	}


	public String getPopPlSpecs() {

		return POP_PL_SPCS;

	}

	public void setPopPlSpecs(String POP_PL_SPCS) {

		this.POP_PL_SPCS = POP_PL_SPCS;

	}


	public String getPopPlCustodian() {

		return POP_PL_CSTDN;

	}

	public void setPopPlCustodian(String POP_PL_CSTDN) {

		this.POP_PL_CSTDN = POP_PL_CSTDN;

	}


	public String getPopPlExpiryDate() {

		return POP_PL_EXPRY_DT;

	}

	public void setPopPlExpiryDate(String POP_PL_EXPRY_DT) {

		this.POP_PL_EXPRY_DT = POP_PL_EXPRY_DT;

	}


	public String getPopPlTotalAmountInWords() {

		return POP_PO_TOTAL_AMNT_IN_WRDS;

	}

	public void setPopPlTotalAmountInWords(String POP_PO_TOTAL_AMNT_IN_WRDS) {

		this.POP_PO_TOTAL_AMNT_IN_WRDS = POP_PO_TOTAL_AMNT_IN_WRDS;

	}

	public double getPopPlEndingBalance() {

		return POP_PL_ENDNG_BLNC;

	}

	public void setPopPlEndingBalance(double POP_PL_ENDNG_BLNC) {

		this.POP_PL_ENDNG_BLNC = POP_PL_ENDNG_BLNC;

	}

	public String getPopPoShipTo() {

		return POP_PO_SHP_TO;

	}

	public void setPopPoShipTo(String POP_PO_SHP_TO) {

		this.POP_PO_SHP_TO = POP_PO_SHP_TO;

	}

	public String getPopPoPaymentTermName() {

		return POP_PO_PYT_TRM_NM;

	}

	public void setPopPoPaymentTermName(String POP_PO_PYT_TRM_NM) {

		this.POP_PO_PYT_TRM_NM = POP_PO_PYT_TRM_NM;

	}

	public double getPopPlDiscount1() {

		return POP_PL_DSCNT1;

	}

	public void setPopPlDiscount1(double POP_PL_DSCNT1) {

		this.POP_PL_DSCNT1 = POP_PL_DSCNT1;

	}

	public double getPopPlDiscount2() {

		return POP_PL_DSCNT2;

	}

	public void setPopPlDiscount2(double POP_PL_DSCNT2) {

		this.POP_PL_DSCNT2 = POP_PL_DSCNT2;

	}

	public double getPopPlDiscount3() {

		return POP_PL_DSCNT3;

	}

	public void setPopPlDiscount3(double POP_PL_DSCNT3) {

		this.POP_PL_DSCNT3 = POP_PL_DSCNT3;

	}

	public double getPopPlDiscount4() {

		return POP_PL_DSCNT4;

	}

	public void setPopPlDiscount4(double POP_PL_DSCNT4) {

		this.POP_PL_DSCNT4 = POP_PL_DSCNT4;

	}

	public double getPopPlTotalDiscount() {

		return POP_PL_TTL_DSCNT;

	}

	public void setPopPlTotalDiscount(double POP_PL_TTL_DSCNT) {

		this.POP_PL_TTL_DSCNT = POP_PL_TTL_DSCNT;

	}

	public String getPopPlDiscount() {

		return POP_PL_DSCNT;

	}

	public void setPopPlDiscount(String POP_PL_DSCNT) {

		this.POP_PL_DSCNT = POP_PL_DSCNT;

	}

	public String getPopPoBillTo() {

		return POP_PO_BLL_TO;

	}

	public void setPopPoBillTo(String POP_PO_BLL_TO) {

		this.POP_PO_BLL_TO = POP_PO_BLL_TO;

	}

	public String getPopPlUnitOfMeasureShortName() {

		return POP_PL_UOM_SHRT_NM;

	}

	public void setPopPlUnitOfMeasureShortName(String POP_PL_UOM_SHRT_NM) {

		this.POP_PL_UOM_SHRT_NM = POP_PL_UOM_SHRT_NM;

	}

	public double getPopPoTaxRate() {

		return POP_PO_TX_RT;

	}

	public void setPopPoTaxRate(double POP_PO_TX_RT) {

		this.POP_PO_TX_RT = POP_PO_TX_RT;

	}

	public double getPopPlUnitCostWoTax() {

		return POP_PL_UNT_CST_WO_TX;

	}

	public void setPopPlUnitCostWoTax(double POP_PL_UNT_CST_WO_TX) {

		this.POP_PL_UNT_CST_WO_TX = POP_PL_UNT_CST_WO_TX;

	}

	public double getPopPlTaxAmount() {

		return POP_PL_TX_AMNT;

	}

	public void setPopPlTaxAmount(double POP_PL_TX_AMNT) {

		this.POP_PL_TX_AMNT = POP_PL_TX_AMNT;

	}

	public String getPopPoTaxType() {

		return POP_PO_TX_TYP;

	}

	public void setPopPoTaxType(String POP_PO_TX_TYP) {

		this.POP_PO_TX_TYP = POP_PO_TX_TYP;

	}

	public String getPopPoCurrency() {

		return POP_PO_CRRNCY;

	}

	public void setPopPoCurrency(String POP_PO_CRRNCY) {

		this.POP_PO_CRRNCY = POP_PO_CRRNCY;

	}

	public String getPopPoSupplierAddress() {

		return POP_PO_SPPLR_ADDRSS;

	}

	public void setPopPoSupplierAddress(String POP_PO_SPPLR_ADDRSS) {

		this.POP_PO_SPPLR_ADDRSS = POP_PO_SPPLR_ADDRSS;

	}

	public String getPopPoSupplierCode() {

		return POP_PO_SPPLR_CODE;

	}

	public void setPopPoSupplierCode(String POP_PO_SPPLR_CODE) {

		this.POP_PO_SPPLR_CODE = POP_PO_SPPLR_CODE;

	}

	public double getPopPlUnitCostTaxInclusive() {

		return POP_PL_UNT_CST_TX_INCLSV;

	}

	public void setPopPlUnitCostTaxInclusive(double POP_PL_UNT_CST_TX_INCLSV) {

		this.POP_PL_UNT_CST_TX_INCLSV = POP_PL_UNT_CST_TX_INCLSV;

	}

	public String getPopPlBranchName() {

		return POP_PO_BRNCH_NM;

	}

	public void setPopPlBranchName(String POP_PO_BRNCH_NM) {

		this.POP_PO_BRNCH_NM = POP_PO_BRNCH_NM;

	}

	public String getPopPlBranchCode() {

		return POP_PO_BRNCH_CODE;

	}

	public void setPopPlBranchCode(String POP_PO_BRNCH_CODE) {

		this.POP_PO_BRNCH_CODE = POP_PO_BRNCH_CODE;

	}

	public String getPopPlApprovedRejectedBy() {

		return POP_APPRVD_RJCTD_BY;

	}

	public void setPopPlApprovedRejectedBy(String POP_APPRVD_RJCTD_BY) {

		this.POP_APPRVD_RJCTD_BY = POP_APPRVD_RJCTD_BY;

	}

	public String getPopPlSupplierPhoneNumber() {

		return POP_PO_SPPLR_PHN_NMBR;

	}

	public void setPopPlSupplierPhoneNumber(String POP_PO_SPPLR_PHN_NMBR) {

		this.POP_PO_SPPLR_PHN_NMBR = POP_PO_SPPLR_PHN_NMBR;

	}

	public String getPopPlSupplierFaxNumber() {

		return POP_PO_SPPLR_FX_NMBR;

	}

	public void setPopPlSupplierFaxNumber(String POP_PO_SPPLR_FX_NMBR) {

		this.POP_PO_SPPLR_FX_NMBR = POP_PO_SPPLR_FX_NMBR;

	}

	public String getPopPoUserResponsibility() {

		return POP_PO_USR_RSPNSBLTY;

	}

	public void setPopPoUserResponsibility(String POP_PO_USR_RSPNSBLTY) {

		this.POP_PO_USR_RSPNSBLTY = POP_PO_USR_RSPNSBLTY;

	}


	public String getPopPoApprovalStatus() {

		return POP_PO_APPRVL_STTS;

	}

	public void setPopPoApprovalStatus(String POP_PO_APPRVL_STTS) {

		this.POP_PO_APPRVL_STTS = POP_PO_APPRVL_STTS;

	}

	public String getPopPoMisc1() {

		return POP_PO_MISC1;

	}

	public void setPopPoMisc1(String POP_PO_MISC1) {

		this.POP_PO_MISC1 = POP_PO_MISC1;

	}

	public String getPopPoMisc2() {

		return POP_PO_MISC2;

	}

	public void setPopPoMisc2(String POP_PO_MISC2) {

		this.POP_PO_MISC2 = POP_PO_MISC2;

	}

	public String getPopPoMisc3() {

		return POP_PO_MISC3;

	}

	public void setPopPoMisc3(String POP_PO_MISC3) {

		this.POP_PO_MISC3 = POP_PO_MISC3;

	}

	public String getPopPoMisc4() {

		return POP_PO_MISC4;

	}

	public void setPopPoMisc4(String POP_PO_MISC4) {

		this.POP_PO_MISC4 = POP_PO_MISC4;

	}

	public String getPopPoMisc5() {

		return POP_PO_MISC5;

	}

	public void setPopPoMisc5(String POP_PO_MISC5) {

		this.POP_PO_MISC5 = POP_PO_MISC5;

	}

	public String getPopPoMisc6() {

		return POP_PO_MISC6;

	}

	public void setPopPoMisc6(String POP_PO_MISC6) {

		this.POP_PO_MISC6 = POP_PO_MISC6;

	}
	
	   public String getPopPlRemarks() {

        return POP_PL_RMRKS;

    }

    public void setPopPlRemarks(String POP_PL_RMRKS) {

        this.POP_PL_RMRKS = POP_PL_RMRKS;

    }
    
     public String getPopDepartment() {

        return POP_DPRTMNT;

    }

    public void setPopDepartment(String POP_DPRTMNT) {

        this.POP_DPRTMNT = POP_DPRTMNT;

    }
}  // ApRepPurchaseOrderPrintDetails