/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.io.Serializable;
import java.util.Date;

public class ApRepPurchaseRequisitionPrintDetails implements Serializable {

	private String PRP_PR_NMBR;
	private Date PRP_PR_DT;
	private Date PRP_PR_DLVRY_PRD;
	private String PRP_PR_DESC;
	private String PRP_PRL_II_NM;
	private String PRP_PRL_II_DESC;
	private String PRP_PRL_II_PRT_NMBR;
	private String PRP_PRL_II_BR_CD_1;
	private String PRP_PRL_II_BR_CD_2;
	private String PRP_PRL_II_BR_CD_3;
	private String PRP_PRL_II_BRND;
	private String PRP_PRL_LOC_NM;
	private String PRP_PRL_PRPRTY_CD;
	private String PRP_PRL_SRL_NMBR;
	private String PRP_PRL_SPCS;
	private String PRP_PRL_CSTDN;
	private String PRP_PRL_EXPRY_DT;
	private double PRP_PRL_QTTY;
	private String PRP_PRL_UOM_SHRT_NM;
	private double PRP_PRL_AMNT;
	private String PRP_PRL_RMRKS;
	private char PRP_FC_SYMBL;
	private String PRP_PRL_APPRVR;
	private double PRP_PRL_UNT_CST;
	private String PRP_PRL_SPPLR;
	private String PRP_PRL_SPPLR_CD;

	private boolean PRP_PL_ISSERVICE;
	private String PRP_PR_DPRTMNT;
	private double PRP_PR_GRNDTTLAMNT;
	private String PRP_PR_MISC1;
	private String PRP_PR_MISC2;
	private String PRP_PR_MISC3;
	private String PRP_PR_MISC4;
	private String PRP_PR_MISC5;
	private String PRP_PR_MISC6;
    private byte PRP_PR_PSTD;
    private String PRP_PR_CRTD_BY;
    private String PRP_PR_APPRVD_BY;

    
	public String getPrpPrNumber() {

		return PRP_PR_NMBR;

	}

	public void setPrpPrNumber(String PRP_PR_NMBR) {

		this.PRP_PR_NMBR = PRP_PR_NMBR;

	}

	public Date getPrpPrDate() {

		return PRP_PR_DT;

	}

	public void setPrpPrDate(Date PRP_PR_DT) {

		this.PRP_PR_DT = PRP_PR_DT;

	}


	public Date getPrpPrDeliveryPeriod() {

		return PRP_PR_DLVRY_PRD;

	}

	public void setPrpPrDeliveryPeriod(Date PRP_PR_DLVRY_PRD) {

		this.PRP_PR_DLVRY_PRD = PRP_PR_DLVRY_PRD;

	}

	public String getPrpPrDescription() {

		return PRP_PR_DESC;

	}

	public Boolean getPrpPrIsService() {

		return PRP_PL_ISSERVICE;

	}

	public void setPrpPrIsService(Boolean PRP_PL_ISSERVICE) {

		this.PRP_PL_ISSERVICE = PRP_PL_ISSERVICE;

	}
	public void setPrpPrDescription(String PRP_PR_DESC) {

		this.PRP_PR_DESC = PRP_PR_DESC;

	}

	public String getPrpPrlIiName() {

		return PRP_PRL_II_NM;

	}

	public void setPrpPrlIiName(String PRP_PRL_II_NM) {

		this.PRP_PRL_II_NM = PRP_PRL_II_NM;

	}

	public String getPrpPrlIiDescription() {

		return PRP_PRL_II_DESC;

	}

	public void setPrpPrlIiDescription(String PRP_PRL_II_DESC) {

		this.PRP_PRL_II_DESC = PRP_PRL_II_DESC;

	}


	public String getPrpPrlIiPartNumber() {

		return PRP_PRL_II_PRT_NMBR;

	}

	public void setPrpPrlIiPartNumber(String PRP_PRL_II_PRT_NMBR) {

		this.PRP_PRL_II_PRT_NMBR = PRP_PRL_II_PRT_NMBR;

	}

	public String getPrpPrlIiBarCode1() {

		return PRP_PRL_II_BR_CD_1;

	}

	public void setPrpPrlIiBarCode1(String PRP_PRL_II_BR_CD_1) {

		this.PRP_PRL_II_BR_CD_1 = PRP_PRL_II_BR_CD_1;

	}

	public String getPrpPrlIiBarCode2() {

		return PRP_PRL_II_BR_CD_2;

	}

	public void setPrpPrlIiBarCode2(String PRP_PRL_II_BR_CD_2) {

		this.PRP_PRL_II_BR_CD_2 = PRP_PRL_II_BR_CD_2;

	}

	public String getPrpPrlIiBarCode3() {

		return PRP_PRL_II_BR_CD_3;

	}

	public void setPrpPrlIiBarCode3(String PRP_PRL_II_BR_CD_3) {

		this.PRP_PRL_II_BR_CD_3 = PRP_PRL_II_BR_CD_3;

	}


	public String getPrpPrlIiBrand() {

		return PRP_PRL_II_BRND;

	}

	public void setPrpPrlIiBrand(String PRP_PRL_II_BRND) {

		this.PRP_PRL_II_BRND = PRP_PRL_II_BRND;

	}

	public String getPrpPrlLocName() {

		return PRP_PRL_LOC_NM;

	}

	public void setPrpPrlLocName(String PRP_PRL_LOC_NM) {

		this.PRP_PRL_LOC_NM = PRP_PRL_LOC_NM;

	}

	public String getPrpPrlPropertyCode() {

		return PRP_PRL_PRPRTY_CD;

	}

	public void setPrpPrlPropertyCode(String PRP_PRL_PRPRTY_CD) {

		this.PRP_PRL_PRPRTY_CD = PRP_PRL_PRPRTY_CD;

	}


	public String getPrpPrlSerialNumber() {

		return PRP_PRL_SRL_NMBR;

	}

	public void setPrpPrlSerialNumber(String PRP_PRL_SRL_NMBR) {

		this.PRP_PRL_SRL_NMBR = PRP_PRL_SRL_NMBR;

	}


	public String getPrpPrlSpecs() {

		return PRP_PRL_SPCS;

	}

	public void setPrpPrlSpecs(String PRP_PRL_SPCS) {

		this.PRP_PRL_SPCS = PRP_PRL_SPCS;

	}


	public String getPrpPrlCustodian() {

		return PRP_PRL_CSTDN;

	}

	public void setPrpPrlCustodian(String PRP_PRL_CSTDN) {

		this.PRP_PRL_CSTDN = PRP_PRL_CSTDN;

	}

	public String getPrpPrlExpiryDate() {

		return PRP_PRL_EXPRY_DT;

	}

	public void setPrpPrlExpiryDate(String PRP_PRL_EXPRY_DT) {

		this.PRP_PRL_EXPRY_DT = PRP_PRL_EXPRY_DT;

	}

	public double getPrpPrlQuantity() {

		return PRP_PRL_QTTY;

	}

	public void setPrpPrlQuantity(double PRP_PRL_QTTY) {

		this.PRP_PRL_QTTY = PRP_PRL_QTTY;

	}

	public double getPrpPrlGrandTotalAmount() {

		return PRP_PR_GRNDTTLAMNT;

	}

	public void setPrpPrlGrandTotalAmount(double PRP_PR_GRNDTTLAMNT) {

		this.PRP_PR_GRNDTTLAMNT = PRP_PR_GRNDTTLAMNT;

	}

	public String getPrpPrlUomShortName() {

		return PRP_PRL_UOM_SHRT_NM;

	}

	public void setPrpPrlUomShortName(String PRP_PRL_UOM_SHRT_NM) {

		this.PRP_PRL_UOM_SHRT_NM = PRP_PRL_UOM_SHRT_NM;

	}

	public double getPrpPrlAmount() {

		return PRP_PRL_AMNT;

	}

	public void setPrpPrlAmount(double PRP_PRL_AMNT) {

		this.PRP_PRL_AMNT = PRP_PRL_AMNT;

	}

    public String getPrpPrlRemarks() {

        return PRP_PRL_RMRKS;

    }

    public void setPrpPrlRemarks(String PRP_PRL_RMRKS) {

        this.PRP_PRL_RMRKS = PRP_PRL_RMRKS;

    }

	public char getPrpFcSymbol() {

		return PRP_FC_SYMBL;

	}

	public void setPrpFcSymbol(char PRP_FC_SYMBL) {

		this.PRP_FC_SYMBL = PRP_FC_SYMBL;

	}
   public String getPrpPrlApprovedBy() {

		return PRP_PRL_APPRVR;

	}

	public void setPrpPrlApprovedBy(String PRP_PRL_APPRVR) {

		this.PRP_PRL_APPRVR = PRP_PRL_APPRVR;

	}

	public double getPrpPrlUnitCost() {

		return PRP_PRL_UNT_CST;

	}

	public void setPrpPrlUnitCost(double PRP_PRL_UNT_CST) {

		this.PRP_PRL_UNT_CST = PRP_PRL_UNT_CST;

	}

	public String getPrpPrDepartment() {

		return PRP_PR_DPRTMNT;

	}

	public void setPrpPrDepartment(String PRP_PR_DPRTMNT) {

		this.PRP_PR_DPRTMNT = PRP_PR_DPRTMNT;

	}


	public String getPrpPrMisc1() {

		return PRP_PR_MISC1;

	}

	public void setPrpPrMisc1(String PRP_PR_MISC1) {

		this.PRP_PR_MISC1 = PRP_PR_MISC1;

	}

public String getPrpPrMisc2() {

		return PRP_PR_MISC2;

	}

	public void setPrpPrMisc2(String PRP_PR_MISC2) {

		this.PRP_PR_MISC2 = PRP_PR_MISC2;

	}

	public String getPrpPrMisc3() {

		return PRP_PR_MISC3;

	}

	public void setPrpPrMisc3(String PRP_PR_MISC3) {

		this.PRP_PR_MISC3 = PRP_PR_MISC3;

	}

	public String getPrpPrMisc4() {

		return PRP_PR_MISC4;

	}

	public void setPrpPrMisc4(String PRP_PR_MISC4) {

		this.PRP_PR_MISC4 = PRP_PR_MISC4;

	}

	public String getPrpPrMisc5() {

		return PRP_PR_MISC5;

	}

	public void setPrpPrMisc5(String PRP_PR_MISC5) {

		this.PRP_PR_MISC5 = PRP_PR_MISC5;

	}

	public String getPrpPrMisc6() {

		return PRP_PR_MISC6;

	}

	public void setPrpPrMisc6(String PRP_PR_MISC6) {

		this.PRP_PR_MISC6 = PRP_PR_MISC6;

	}

	
	public byte getPrpPrPosted() {

        return PRP_PR_PSTD;

    }

    public void setPrpPrPosted(byte PRP_PR_PSTD) {

        this.PRP_PR_PSTD = PRP_PR_PSTD;

    }
    
    public String getPrpPrCreatedBy() {

        return PRP_PR_CRTD_BY;

    }

    public void setPrpPrCreatedBy(String PRP_PR_CRTD_BY) {

        this.PRP_PR_CRTD_BY = PRP_PR_CRTD_BY;

    }
    
    
     public String getPrpPrApprovedBy() {

        return PRP_PR_APPRVD_BY;

    }

    public void setPrpPrApprovedBy(String PRP_PR_APPRVD_BY) {

        this.PRP_PR_APPRVD_BY = PRP_PR_APPRVD_BY;

    }


}