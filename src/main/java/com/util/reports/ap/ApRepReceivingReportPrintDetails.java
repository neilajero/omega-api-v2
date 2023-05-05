/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.util.Date;

public class ApRepReceivingReportPrintDetails implements java.io.Serializable {

	private Date RRP_PO_DT;
	private String RRP_PO_DCMNT_NMBR;
	private String RRP_PO_RFRNC_NMBR;
	private String RRP_PO_CRTD_BY;
	private String RRP_PO_APPRVD_RJCTD_BY;
	private String RRP_PO_RCV_PO_NMBR;
	private String RRP_PO_DESC;
	private String RRP_PL_II_NM;
	private String RRP_PL_LOC_NM;
	private String RRP_PL_UOM;
	private double RRP_PL_QTY_ORDRD;
	private double RRP_PL_QTY;
	private double RRP_PL_UNT_CST;
	private double RRP_PL_AMNT;
	private double RRP_PL_TAX_AMNT;
	private String RRP_PL_II_DESC;
	private String RRP_PO_SPPLR_NM;
	private String RRP_PO_SPPLR_ADDRSS;
	private String RRP_PO_CNTCT_PRSN;
	private String RRP_PO_CNTCT_NMBR;
	private double RRP_PL_ENDNG_BLNC;
	private String RRP_PO_CHCKD_BY;
	private Date RRP_PO_RCV_PO_DT;
	private String RRP_PO_TYPE;
	private String RRP_PYMNT_TRM;
	private double RRP_PO_TX_RT;

	private String RRP_PO_INSPCTD_BY;
	private String RRP_PO_INSPCTD_BY2;
	private String RRP_PO_INSPCTD_BY3;
	private String RRP_PO_INSPCTD_BY4;
	private Date RRP_PO_DT_INSPCTD;
	private String RRP_PO_RLSD_BY;
	private String RRP_PO_RLSR_PSTN;
	private Date RRP_PO_DT_RLSD;

	private double RRP_PL_UNT_CST_WO_TX;
	private double RRP_PL_UNT_CST_TX_INCLSV;
	private String RRP_PO_BRNCH_CDE;
	private String RRP_PO_BRNCH_NM;
	private String RRP_PL_LOC_PSTN;
	private String RRP_PL_LOC_BRNCH;
	private String RRP_PL_LOC_DPRTMNT;
	private String RRP_PL_LOC_EMPLYMNT_STTS;
	private String RRP_PL_LOC_DT_HRD;
	private String RRP_PL_II_RMRKS;
	private byte RRP_PO_FXD_ASST_RCVNG;
	private String RRP_PL_II_BARCODE;
	private boolean RRP_PL_ISSERVICE;
	private boolean RRP_PL_ISINVENTORIABLE;

	private String RRP_TG_SPCS;
	private String RRP_TG_DCMNT_NMBR;
	private String RRP_TG_SRL_NMBR;
	private Date RRP_TG_EXPRY_DT;
	private String RRP_TG_EXPRY_DT_STR;
	private String RRP_TG_PRPRTY_CD;
	private String RRP_TG_CSTDN;
	private String RRP_TG_CSTDN_PSTN;
	private String RRP_PO_DPRTMNT;

	private String RRP_PO_MISC1;
	private String RRP_PO_MISC2;
	private String RRP_PO_MISC3;
	private String RRP_PO_MISC4;
	private String RRP_PO_MISC5;
	private String RRP_PO_MISC6;
	private byte RRP_PO_PSTD;

	public ApRepReceivingReportPrintDetails() {
    }


	public String getRrpPoMisc1() {

		return RRP_PO_MISC1;

	}

	public void setRrpPoMisc1(String RRP_PO_MISC1) {

		this.RRP_PO_MISC1 = RRP_PO_MISC1;

	}

	public String getRrpPoMisc2() {

		return RRP_PO_MISC2;

	}

	public void setRrpPoMisc2(String RRP_PO_MISC2) {

		this.RRP_PO_MISC2 = RRP_PO_MISC2;

	}

	public String getRrpPoMisc3() {

		return RRP_PO_MISC3;

	}

	public void setRrpPoMisc3(String RRP_PO_MISC3) {

		this.RRP_PO_MISC3 = RRP_PO_MISC3;

	}


	public String getRrpPoMisc4() {

		return RRP_PO_MISC4;

	}

	public void setRrpPoMisc4(String RRP_PO_MISC4) {

		this.RRP_PO_MISC4 = RRP_PO_MISC4;

	}

	public String getRrpPoMisc5() {

		return RRP_PO_MISC5;

	}

	public void setRrpPoMisc5(String RRP_PO_MISC5) {

		this.RRP_PO_MISC5 = RRP_PO_MISC5;

	}

	public String getRrpPoMisc6() {

		return RRP_PO_MISC6;

	}

	public void setRrpPoMisc6(String RRP_PO_MISC6) {

		this.RRP_PO_MISC6 = RRP_PO_MISC6;

	}

	public String getRrpPoDepartment() {

		return RRP_PO_DPRTMNT;

	}

	public void setRrpPoDepartment(String RRP_PO_DPRTMNT) {

		this.RRP_PO_DPRTMNT = RRP_PO_DPRTMNT;

	}

	public String getRrpTgPropertyCode() {

		return RRP_TG_PRPRTY_CD;

	}

	public void setRrpTgPropertyCode(String RRP_TG_PRPRTY_CD) {

		this.RRP_TG_PRPRTY_CD = RRP_TG_PRPRTY_CD;

	}

	public String getRrpTgCustodian() {

		return RRP_TG_CSTDN;

	}

	public void setRrpTgCustodian(String RRP_TG_CSTDN) {

		this.RRP_TG_CSTDN = RRP_TG_CSTDN;

	}

	public String getRrpTgCustodianPosition() {

		return RRP_TG_CSTDN_PSTN;

	}

	public void setRrpTgCustodianPosition(String RRP_TG_CSTDN_PSTN) {

		this.RRP_TG_CSTDN_PSTN = RRP_TG_CSTDN_PSTN;

	}

	public String getRrpTgDocumentNumber() {

		return RRP_TG_DCMNT_NMBR;

	}

	public void setRrpTgDocumentNumber(String RRP_TG_DCMNT_NMBR) {

		this.RRP_TG_DCMNT_NMBR = RRP_TG_DCMNT_NMBR;

	}

	public String getRrpTgSerialNumber() {

		return RRP_TG_SRL_NMBR;

	}

	public void setRrpTgSerialNumber(String RRP_TG_SRL_NMBR) {

		this.RRP_TG_SRL_NMBR = RRP_TG_SRL_NMBR;

	}

	public String getRrpTgSpecs() {

		return RRP_TG_SPCS;

	}

	public void setRrpTgSpecs(String RRP_TG_SPCS) {

		this.RRP_TG_SPCS = RRP_TG_SPCS;

	}

	public Date getRrpTgExpiryDate() {

		return RRP_TG_EXPRY_DT;

	}

	public void setRrpTgExpiryDate(Date RRP_TG_EXPRY_DT) {

		this.RRP_TG_EXPRY_DT = RRP_TG_EXPRY_DT;

	}


	public String getRrpTgExpiryDateString() {

		return RRP_TG_EXPRY_DT_STR;

	}

	public void setRrpTgExpiryDateString(String RRP_TG_EXPRY_DT_STR) {

		this.RRP_TG_EXPRY_DT_STR = RRP_TG_EXPRY_DT_STR;

	}

	public Date getRrpPoDate() {

		return RRP_PO_DT;

	}

	public void setRrpPoDate(Date RRP_PO_DT) {

		this.RRP_PO_DT = RRP_PO_DT;

	}

	public String getRrpPoDocumentNumber() {

		return RRP_PO_DCMNT_NMBR;

	}

	public void setRrpPoDocumentNumber(String RRP_PO_DCMNT_NMBR) {

		this.RRP_PO_DCMNT_NMBR = RRP_PO_DCMNT_NMBR;

	}

	public String getRrpPoReferenceNumber() {

		return RRP_PO_RFRNC_NMBR;

	}

	public void setRrpPoReferenceNumber(String RRP_PO_RFRNC_NMBR) {

		this.RRP_PO_RFRNC_NMBR = RRP_PO_RFRNC_NMBR;

	}

	public String getRrpPoCreatedBy() {

		return RRP_PO_CRTD_BY;

	}

	public void setRrpPoCreatedBy(String RRP_PO_CRTD_BY) {

		this.RRP_PO_CRTD_BY = RRP_PO_CRTD_BY;

	}

	public String getRrpPoApprovedRejectedBy() {

		return RRP_PO_APPRVD_RJCTD_BY;

	}

	public void setRrpPoApprovedRejectedBy(String RRP_PO_APPRVD_RJCTD_BY) {

		this.RRP_PO_APPRVD_RJCTD_BY = RRP_PO_APPRVD_RJCTD_BY;

	}

	public Boolean getRrpPlIsService() {

		return RRP_PL_ISSERVICE;

	}

	public void setRrpPlIsService(Boolean RRP_PL_ISSERVICE) {

		this.RRP_PL_ISSERVICE = RRP_PL_ISSERVICE;

	}

	public Boolean getRrpPlIsInventoriable() {

		return RRP_PL_ISINVENTORIABLE;

	}

	public void setRrpPlIsInventoriable(Boolean RRP_PL_ISINVENTORIABLE) {

		this.RRP_PL_ISINVENTORIABLE = RRP_PL_ISINVENTORIABLE;

	}

	public String getRrpPoRcvPoNumber() {

		return RRP_PO_RCV_PO_NMBR;

	}

	public void setRrpPoRcvPoNumber(String RRP_PO_RCV_PO_NMBR) {

		this.RRP_PO_RCV_PO_NMBR = RRP_PO_RCV_PO_NMBR;

	}

	public String getRrpPoDescription() {

		return RRP_PO_DESC;

	}

	public void setRrpPoDescription(String RRP_PO_DESC) {

		this.RRP_PO_DESC = RRP_PO_DESC;

	}

	public String getRrpPllIiName() {

		return RRP_PL_II_NM;

	}

	public void setRrpPllIiName(String RRP_PL_II_NM) {

		this.RRP_PL_II_NM = RRP_PL_II_NM;

	}


	public String getRrpPlLocName() {

		return RRP_PL_LOC_NM;

	}

	public void setRrpPlLocName(String RRP_PL_LOC_NM) {

		this.RRP_PL_LOC_NM = RRP_PL_LOC_NM;

	}

	public String getRrpPlUomName() {

		return RRP_PL_UOM;

	}

	public void setRrpPlUomName(String RRP_PL_UOM) {

		this.RRP_PL_UOM = RRP_PL_UOM;

	}

	public double getRrpPlQuantityOrdered() {

		return RRP_PL_QTY_ORDRD;

	}

	public void setRrpPlQuantityOrdered(double RRP_PL_QTY_ORDRD) {

		this.RRP_PL_QTY_ORDRD = RRP_PL_QTY_ORDRD;

	}

	public double getRrpPlQuantity() {

		return RRP_PL_QTY;

	}

	public void setRrpPlQuantity(double RRP_PL_QTY) {

		this.RRP_PL_QTY = RRP_PL_QTY;

	}

	public double getRrpPlUnitCost() {

		return RRP_PL_UNT_CST;

	}

	public void setRrpPlUnitCost(double RRP_PL_UNT_CST) {

		this.RRP_PL_UNT_CST = RRP_PL_UNT_CST;

	}

	public double getRrpPlAmount() {

		return RRP_PL_AMNT;

	}

	public void setRrpPlAmount(double RRP_PL_AMNT) {

		this.RRP_PL_AMNT = RRP_PL_AMNT;

	}

	public double getRrpPlTaxAmount() {

		return RRP_PL_TAX_AMNT;

	}

	public void setRrpPlTaxAmount(double RRP_PL_TAX_AMNT) {

		this.RRP_PL_TAX_AMNT = RRP_PL_TAX_AMNT;

	}

	public String getRrpPlItemDescription() {

		return RRP_PL_II_DESC;

	}

	public void setRrpPlItemDescription(String RRP_PL_II_DESC) {

		this.RRP_PL_II_DESC = RRP_PL_II_DESC;

	}

	public String getRrpPlItemBarcode() {

		return RRP_PL_II_BARCODE;

	}

	public void setRrpPlItemBarcode(String RRP_PL_II_BARCODE) {

		this.RRP_PL_II_BARCODE = RRP_PL_II_BARCODE;

	}

	public String getRrpPoSupplierName() {

		return RRP_PO_SPPLR_NM;

	}

	public void setRrpPoSupplierName(String RRP_PO_SPPLR_NM) {

		this.RRP_PO_SPPLR_NM = RRP_PO_SPPLR_NM;

	}

	public void setRrpPoSupplierAddress(String RRP_PO_SPPLR_ADDRSS) {

		this.RRP_PO_SPPLR_ADDRSS = RRP_PO_SPPLR_ADDRSS;

	}

	public String getRrpPoSupplierAddress() {

		return RRP_PO_SPPLR_ADDRSS;

	}



	public String getRrpPoContactPerson() {

		return RRP_PO_CNTCT_PRSN;

	}

	public void setRrpPoContactPerson(String RRP_PO_CNTCT_PRSN) {

		this.RRP_PO_CNTCT_PRSN = RRP_PO_CNTCT_PRSN;

	}

	public String getRrpPoContactNumber() {

		return RRP_PO_CNTCT_NMBR;

	}

	public void setRrpPoContactNumber(String RRP_PO_CNTCT_NMBR) {

		this.RRP_PO_CNTCT_NMBR = RRP_PO_CNTCT_NMBR;

	}

	public double getRrpPlEndingBalance() {

		return RRP_PL_ENDNG_BLNC;
	}

	public void setRrpPlEndingBalance(double RRP_PL_ENDNG_BLNC) {

		this.RRP_PL_ENDNG_BLNC = RRP_PL_ENDNG_BLNC;

	}

	public String getRrpPoCheckedBy() {

		return RRP_PO_CHCKD_BY;

	}

	public void setRrpPoCheckedBy(String RRP_PO_CHCKD_BY) {

		this.RRP_PO_CHCKD_BY = RRP_PO_CHCKD_BY;

	}

	public Date getRrpPoRcvPoDate() {

		return RRP_PO_RCV_PO_DT;

	}

	public void setRrpPoRcvPoDate(Date RRP_PO_RCV_PO_DT) {

		this.RRP_PO_RCV_PO_DT = RRP_PO_RCV_PO_DT;

	}

	public String getRrpPoType() {

		return RRP_PO_TYPE;

	}

	public void setRrpPoType(String RRP_PO_TYPE) {

		this.RRP_PO_TYPE = RRP_PO_TYPE;

	}

    public String getRrpPaymentTerm() {

		return RRP_PYMNT_TRM;

	}

	public void setRrpPaymentTerm(String RRP_PYMNT_TRM) {

		this.RRP_PYMNT_TRM = RRP_PYMNT_TRM;

	}

	public double getRrpPoTaxRate() {

		return RRP_PO_TX_RT;

	}

	public void setRrpPoTaxRate(double RRP_PO_TX_RT) {

		this.RRP_PO_TX_RT = RRP_PO_TX_RT;

	}





	public String getRrpPoInspectedBy() {

		return RRP_PO_INSPCTD_BY;

	}

	public void setRrpPoInspectedBy(String RRP_PO_INSPCTD_BY) {

		this.RRP_PO_INSPCTD_BY = RRP_PO_INSPCTD_BY;

	}

	public String getRrpPoInspectedBy2() {

		return RRP_PO_INSPCTD_BY2;

	}

	public void setRrpPoInspectedBy2(String RRP_PO_INSPCTD_BY2) {

		this.RRP_PO_INSPCTD_BY2 = RRP_PO_INSPCTD_BY2;

	}

	public String getRrpPoInspectedBy3() {

		return RRP_PO_INSPCTD_BY3;

	}

	public void setRrpPoInspectedBy3(String RRP_PO_INSPCTD_BY3) {

		this.RRP_PO_INSPCTD_BY3 = RRP_PO_INSPCTD_BY3;

	}

	public String getRrpPoInspectedBy4() {

		return RRP_PO_INSPCTD_BY4;

	}

	public void setRrpPoInspectedBy4(String RRP_PO_INSPCTD_BY4) {

		this.RRP_PO_INSPCTD_BY4 = RRP_PO_INSPCTD_BY4;

	}
	public Date getRrpPoDateInspected() {

		return RRP_PO_DT_INSPCTD;

	}

	public void setRrpPoDateInspected(Date RRP_PO_DT_INSPCTD) {

		this.RRP_PO_DT_INSPCTD = RRP_PO_DT_INSPCTD;

	}




	public String getRrpPoReleasedBy() {

		return RRP_PO_RLSD_BY;

	}

	public void setRrpPoReleasedBy(String RRP_PO_RLSD_BY) {

		this.RRP_PO_RLSD_BY = RRP_PO_RLSD_BY;

	}

	public String getRrpPoReleaserPosition() {

		return RRP_PO_RLSR_PSTN;

	}

	public void setRrpPoReleaserPosition(String RRP_PO_RLSR_PSTN) {

		this.RRP_PO_RLSR_PSTN = RRP_PO_RLSR_PSTN;

	}

	public Date getRrpPoDateReleased() {

		return RRP_PO_DT_RLSD;

	}

	public void setRrpPoDateReleased(Date RRP_PO_DT_RLSD) {

		this.RRP_PO_DT_RLSD = RRP_PO_DT_RLSD;

	}








	public double getRrpPlUnitCostWoTax() {

		return RRP_PL_UNT_CST_WO_TX;

	}

	public void setRrpPlUnitCostWoTax(double RRP_PL_UNT_CST_WO_TX) {

		this.RRP_PL_UNT_CST_WO_TX = RRP_PL_UNT_CST_WO_TX;

	}

	public double getRrpPlUnitCostTaxInclusive() {

		return RRP_PL_UNT_CST_TX_INCLSV;

	}

	public void setRrpPlUnitCostTaxInclusive(double RRP_PL_UNT_CST_TX_INCLSV) {

		this.RRP_PL_UNT_CST_TX_INCLSV = RRP_PL_UNT_CST_TX_INCLSV;

	}

	public String getBranchCode() {

		return RRP_PO_BRNCH_CDE;

	}

	public void setBranchCode(String RRP_PO_BRNCH_CDE) {

		this.RRP_PO_BRNCH_CDE = RRP_PO_BRNCH_CDE;

	}

	public String getBranchName() {

		return RRP_PO_BRNCH_NM;

	}

	public void setBranchName(String RRP_PO_BRNCH_NM) {

		this.RRP_PO_BRNCH_NM = RRP_PO_BRNCH_NM;

	}

	public String getRrpPlLocPosition() {

		return RRP_PL_LOC_PSTN;

	}

	public void setRrpPlLocPosition(String RRP_PL_LOC_PSTN) {

		this.RRP_PL_LOC_PSTN = RRP_PL_LOC_PSTN;

	}

	public String getRrpPlLocBranch() {

		return RRP_PL_LOC_BRNCH;

	}

	public void setRrpPlLocBranch(String RRP_PL_LOC_BRNCH) {

		this.RRP_PL_LOC_BRNCH = RRP_PL_LOC_BRNCH;

	}

	public String getRrpPlLocDepartment() {

		return RRP_PL_LOC_DPRTMNT;

	}

	public void setRrpPlLocDepartment(String RRP_PL_LOC_DPRTMNT) {

		this.RRP_PL_LOC_DPRTMNT = RRP_PL_LOC_DPRTMNT;

	}

	public String getRrpPlLocEmploymentStatus() {

		return RRP_PL_LOC_EMPLYMNT_STTS;

	}

	public void setRrpPlLocEmploymentStatus(String RRP_PL_LOC_EMPLYMNT_STTS) {

		this.RRP_PL_LOC_EMPLYMNT_STTS = RRP_PL_LOC_EMPLYMNT_STTS;

	}

	public String getRrpPlLocDateHired() {

		return RRP_PL_LOC_DT_HRD;

	}

	public void setRrpPlLocDateHired(String RRP_PL_LOC_DT_HRD) {

		this.RRP_PL_LOC_DT_HRD = RRP_PL_LOC_DT_HRD;

	}

	public String getRrpPlIiRemarks() {

		return RRP_PL_II_RMRKS;

	}

	public void setRrpPlIiRemarks(String RRP_PL_II_RMRKS) {

		this.RRP_PL_II_RMRKS = RRP_PL_II_RMRKS;

	}

	public byte getRrpFixedAssetReceiving() {

		return RRP_PO_FXD_ASST_RCVNG;

	}

	public void setRrpFixedAssetReceiving(byte RRP_PO_FXD_ASST_RCVNG) {

		this.RRP_PO_FXD_ASST_RCVNG = RRP_PO_FXD_ASST_RCVNG;

	}
	
	public byte getRrpPoPosted() {

        return RRP_PO_PSTD;

    }

    public void setRrpPoPosted(byte RRP_PO_PSTD) {

        this.RRP_PO_PSTD = RRP_PO_PSTD;

    }
}  // ApRepReceivingReportPrintDetails