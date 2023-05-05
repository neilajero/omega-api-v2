/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.Debug;
import com.util.ap.ApCheckDetails;

import java.util.ArrayList;
import java.util.Comparator;

public class ApModCheckDetails extends ApCheckDetails implements java.io.Serializable {

	private String CHK_FC_NM;
	private String CHK_SPL_SPPLR_CODE;
    private String CHK_SPPLR_CLSS_NM;
    private byte CHK_SC_INVT;
    
	private String CHK_BA_NM;
	private String CHK_TC_NM;
	private String CHK_TC_TYP;
	private double CHK_TC_RT;
	private String CHK_WTC_NM;
	private String CHK_SPL_NM;
	private String CHK_CB_NM;
	private String CHK_PYT_NM;
	private String CHK_AMNT_IN_WRDS;
	private String CHK_SPL_ADDRSS;
	private String CHK_BA_ACCNT_NMBR;
	private String CHK_BA_COA_GL_CSH_ACCNT;
	private String CHK_TYP;

	private double CHK_TTL_DBT;
	private double CHK_TTL_CRDT;

	private byte CHK_BA_ACCNT_NMBR_SHW;
	private int CHK_BA_ACCNT_NMBR_TP;
	private int CHK_BA_ACCNT_NMBR_LFT;

	private byte CHK_BA_ACCNT_NM_SHW;
	private int CHK_BA_ACCNT_NM_TP;
	private int CHK_BA_ACCNT_NM_LFT;

	private byte CHK_BA_NMBR_SHW;
	private int CHK_BA_NMBR_TP;
	private int CHK_BA_NMBR_LFT;

	private byte CHK_BA_DT_SHW;
	private int CHK_BA_DT_TP;
	private int CHK_BA_DT_LFT;

	private byte CHK_BA_PY_SHW;
	private int CHK_BA_PY_TP;
	private int CHK_BA_PY_LFT;

	private byte CHK_BA_AMNT_SHW;
	private int CHK_BA_AMNT_TP;
	private int CHK_BA_AMNT_LFT;

	private byte CHK_BA_WRD_AMNT_SHW;
	private int CHK_BA_WRD_AMNT_TP;
	private int CHK_BA_WRD_AMNT_LFT;

	private byte CHK_BA_CRRNCY_SHW;
	private int CHK_BA_CRRNCY_TP;
	private int CHK_BA_CRRNCY_LFT;

	private byte CHK_BA_ADDRSS_SHW;
	private int CHK_BA_ADDRSS_TP;
	private int CHK_BA_ADDRSS_LFT;

	private byte CHK_BA_MM_SHW;
	private int CHK_BA_MM_TP;
	private int CHK_BA_MM_LFT;
	
	private byte CHK_BA_DC_NMBR_SHW;
	private int CHK_BA_DC_NMBR_TP;
	private int CHK_BA_DC_NMBR_LFT;
	
	private int CHK_BA_FNT_SZ;
	private String CHK_BA_FNT_STYL;
	private String CHK_BA_FNT_WGHT;

	private ArrayList chkAvList;
	private ArrayList chkDrList;
	private ArrayList chkVliList;

	private byte CHK_DR_DBT;
	private double CHK_DR_AMNT;
	private String CHK_DR_ACCNT_DESC;
	private String CHK_DR_ACCNT_NMBR;

	private String CHK_CRTD_BY_DESC;
	private String CHK_APPRVD_RJCTD_BY_DESC;
	private String CHK_SPL_RMRKS;

	private String CHK_BRNCH_CDE;
	private String CHK_STATUS;
	private String CHK_BRNCH_NM;
	
	private String CHK_MISC1;
	private String CHK_MISC2;
	private String CHK_MISC3;
	private String CHK_MISC4;
	private String CHK_MISC5;
	private String CHK_MISC6;

	public ApModCheckDetails() {
    }

	public String getChkFcName() {

		return CHK_FC_NM;

	}

	public void setChkFcName(String CHK_FC_NM) {

		this.CHK_FC_NM = CHK_FC_NM;

	}

	public String getChkSplSupplierCode() {

		return CHK_SPL_SPPLR_CODE;

	}

	public void setChkSplSupplierCode(String CHK_SPL_SPPLR_CODE) {

		this.CHK_SPL_SPPLR_CODE = CHK_SPL_SPPLR_CODE;

	}
        
    public String getChkSupplierClassName() {

		return CHK_SPPLR_CLSS_NM;

	}

	public void setChkSupplierClassName(String CHK_SPPLR_CLSS_NM) {

		this.CHK_SPPLR_CLSS_NM = CHK_SPPLR_CLSS_NM;

	}
	
	
	public byte getChkScInvestment() {

		return CHK_SC_INVT;

	}

	public void setChkScInvestment(byte CHK_SC_INVT) {

		this.CHK_SC_INVT = CHK_SC_INVT;

	}

    
    

	public String getChkCbName() {

		return CHK_CB_NM;

	}

	public void setChkCbName(String CHK_CB_NM) {

		this.CHK_CB_NM = CHK_CB_NM;

	}

	public String getChkBaName() {

		return CHK_BA_NM;

	}

	public void setChkBaName(String CHK_BA_NM) {

		this.CHK_BA_NM = CHK_BA_NM;

	}
	
	public String getChkPytName() {
	   	
	   	  return CHK_PYT_NM;
	   	
	   }
	   
	   public void setChkPytName(String CHK_PYT_NM) {
	   	
	   	  this.CHK_PYT_NM = CHK_PYT_NM;
	   	
	   }
	   

	public String getChkTcName() {

		return CHK_TC_NM;

	}

	public void setChkTcName(String CHK_TC_NM) {

		this.CHK_TC_NM = CHK_TC_NM;

	}

	public String getChkWtcName() {

		return CHK_WTC_NM;

	}

	public void setChkWtcName(String CHK_WTC_NM) {

		this.CHK_WTC_NM = CHK_WTC_NM;

	}

	public String getChkSplName() {

		return CHK_SPL_NM;

	}

	public void setChkSplName(String CHK_SPL_NM) {

		this.CHK_SPL_NM = CHK_SPL_NM;

	}

	public String getChkAmountInWords() {

		return CHK_AMNT_IN_WRDS;

	}

	public void setChkAmountInWords(String CHK_AMNT_IN_WRDS) {

		this.CHK_AMNT_IN_WRDS = CHK_AMNT_IN_WRDS;

	}

	public String getChkSplAddress() {

		return CHK_SPL_ADDRSS;

	}

	public void setChkSplAddress(String CHK_SPL_ADDRSS) {

		this.CHK_SPL_ADDRSS = CHK_SPL_ADDRSS;

	}

	public String getChkBaAccountNumber() {

		return CHK_BA_ACCNT_NMBR;

	}

	public void setChkBaAccountNumber(String CHK_BA_ACCNT_NMBR) {

		this.CHK_BA_ACCNT_NMBR = CHK_BA_ACCNT_NMBR;

	}

	public String getChkBaCoaGlCashAccount() {

		return CHK_BA_COA_GL_CSH_ACCNT;

	}

	public void setChkBaCoaGlCashAccount(String CHK_BA_COA_GL_CSH_ACCNT) {

		this.CHK_BA_COA_GL_CSH_ACCNT = CHK_BA_COA_GL_CSH_ACCNT;

	}

	public byte getChkBaAccountNumberShow() {

		return CHK_BA_ACCNT_NMBR_SHW;

	}

	public void setChkBaAccountNumberShow(byte CHK_BA_ACCNT_NMBR_SHW) {

		this.CHK_BA_ACCNT_NMBR_SHW = CHK_BA_ACCNT_NMBR_SHW;

	}

	public int getChkBaAccountNumberTop() {

		return CHK_BA_ACCNT_NMBR_TP;

	}

	public void setChkBaAccountNumberTop(int CHK_BA_ACCNT_NMBR_TP) {

		this.CHK_BA_ACCNT_NMBR_TP = CHK_BA_ACCNT_NMBR_TP;

	}

	public int getChkBaAccountNumberLeft() {

		return CHK_BA_ACCNT_NMBR_LFT;

	}


	public void setChkBaAccountNumberLeft(int CHK_BA_ACCNT_NMBR_LFT) {

		this.CHK_BA_ACCNT_NMBR_LFT = CHK_BA_ACCNT_NMBR_LFT;

	}

	public byte getChkBaAccountNameShow() {

		return CHK_BA_ACCNT_NM_SHW;

	}

	public void setChkBaAccountNameShow(byte CHK_BA_ACCNT_NM_SHW) {

		this.CHK_BA_ACCNT_NM_SHW = CHK_BA_ACCNT_NM_SHW;

	}

	public int getChkBaAccountNameTop() {

		return CHK_BA_ACCNT_NM_TP;

	}

	public void setChkBaAccountNameTop(int CHK_BA_ACCNT_NM_TP) {

		this.CHK_BA_ACCNT_NM_TP = CHK_BA_ACCNT_NM_TP;

	}

	public int getChkBaAccountNameLeft() {

		return CHK_BA_ACCNT_NM_LFT;

	}


	public void setChkBaAccountNameLeft(int CHK_BA_ACCNT_NM_LFT) {

		this.CHK_BA_ACCNT_NM_LFT = CHK_BA_ACCNT_NM_LFT;

	}

	public byte getChkBaNumberShow() {

		return CHK_BA_NMBR_SHW;

	}

	public void setChkBaNumberShow(byte CHK_BA_NMBR_SHW) {

		this.CHK_BA_NMBR_SHW = CHK_BA_NMBR_SHW;

	}

	public int getChkBaNumberTop() {

		return CHK_BA_NMBR_TP;

	}

	public void setChkBaNumberTop(int CHK_BA_NMBR_TP) {

		this.CHK_BA_NMBR_TP = CHK_BA_NMBR_TP;

	}

	public int getChkBaNumberLeft() {

		return CHK_BA_NMBR_LFT;

	}


	public void setChkBaNumberLeft(int CHK_BA_NMBR_LFT) {

		this.CHK_BA_NMBR_LFT = CHK_BA_NMBR_LFT;

	}

	public byte getChkBaDateShow() {

		return CHK_BA_DT_SHW;

	}

	public void setChkBaDateShow(byte CHK_BA_DT_SHW) {

		this.CHK_BA_DT_SHW = CHK_BA_DT_SHW;

	}

	public int getChkBaDateTop() {

		return CHK_BA_DT_TP;

	}

	public void setChkBaDateTop(int CHK_BA_DT_TP) {

		this.CHK_BA_DT_TP = CHK_BA_DT_TP;

	}

	public int getChkBaDateLeft() {

		return CHK_BA_DT_LFT;

	}


	public void setChkBaDateLeft(int CHK_BA_DT_LFT) {

		this.CHK_BA_DT_LFT = CHK_BA_DT_LFT;

	}

	public byte getChkBaPayeeShow() {

		return CHK_BA_PY_SHW;

	}

	public void setChkBaPayeeShow(byte CHK_BA_PY_SHW) {

		this.CHK_BA_PY_SHW = CHK_BA_PY_SHW;

	}

	public int getChkBaPayeeTop() {

		return CHK_BA_PY_TP;

	}

	public void setChkBaPayeeTop(int CHK_BA_PY_TP) {

		this.CHK_BA_PY_TP = CHK_BA_PY_TP;

	}

	public int getChkBaPayeeLeft() {

		return CHK_BA_PY_LFT;

	}


	public void setChkBaPayeeLeft(int CHK_BA_PY_LFT) {

		this.CHK_BA_PY_LFT = CHK_BA_PY_LFT;

	}

	public byte getChkBaAmountShow() {

		return CHK_BA_AMNT_SHW;

	}

	public void setChkBaAmountShow(byte CHK_BA_AMNT_SHW) {

		this.CHK_BA_AMNT_SHW = CHK_BA_AMNT_SHW;

	}

	public int getChkBaAmountTop() {

		return CHK_BA_AMNT_TP;

	}

	public void setChkBaAmountTop(int CHK_BA_AMNT_TP) {

		this.CHK_BA_AMNT_TP = CHK_BA_AMNT_TP;

	}

	public int getChkBaAmountLeft() {

		return CHK_BA_AMNT_LFT;

	}


	public void setChkBaAmountLeft(int CHK_BA_AMNT_LFT) {

		this.CHK_BA_AMNT_LFT = CHK_BA_AMNT_LFT;

	}

	public byte getChkBaWordAmountShow() {

		return CHK_BA_WRD_AMNT_SHW;

	}

	public void setChkBaWordAmountShow(byte CHK_BA_WRD_AMNT_SHW) {

		this.CHK_BA_WRD_AMNT_SHW = CHK_BA_WRD_AMNT_SHW;

	}

	public int getChkBaWordAmountTop() {

		return CHK_BA_WRD_AMNT_TP;

	}

	public void setChkBaWordAmountTop(int CHK_BA_WRD_AMNT_TP) {

		this.CHK_BA_WRD_AMNT_TP = CHK_BA_WRD_AMNT_TP;

	}

	public int getChkBaWordAmountLeft() {

		return CHK_BA_WRD_AMNT_LFT;

	}


	public void setChkBaWordAmountLeft(int CHK_BA_WRD_AMNT_LFT) {

		this.CHK_BA_WRD_AMNT_LFT = CHK_BA_WRD_AMNT_LFT;

	}

	public byte getChkBaCurrencyShow() {

		return CHK_BA_CRRNCY_SHW;

	}

	public void setChkBaCurrencyShow(byte CHK_BA_CRRNCY_SHW) {

		this.CHK_BA_CRRNCY_SHW = CHK_BA_CRRNCY_SHW;

	}

	public int getChkBaCurrencyTop() {

		return CHK_BA_CRRNCY_TP;

	}

	public void setChkBaCurrencyTop(int CHK_BA_CRRNCY_TP) {

		this.CHK_BA_CRRNCY_TP = CHK_BA_CRRNCY_TP;

	}

	public int getChkBaCurrencyLeft() {

		return CHK_BA_CRRNCY_LFT;

	}


	public void setChkBaCurrencyLeft(int CHK_BA_CRRNCY_LFT) {

		this.CHK_BA_CRRNCY_LFT = CHK_BA_CRRNCY_LFT;

	}

	public byte getChkBaAddressShow() {

		return CHK_BA_ADDRSS_SHW;

	}

	public void setChkBaAddressShow(byte CHK_BA_ADDRSS_SHW) {

		this.CHK_BA_ADDRSS_SHW = CHK_BA_ADDRSS_SHW;

	}

	public int getChkBaAddressTop() {

		return CHK_BA_ADDRSS_TP;

	}

	public void setChkBaAddressTop(int CHK_BA_ADDRSS_TP) {

		this.CHK_BA_ADDRSS_TP = CHK_BA_ADDRSS_TP;

	}

	public int getChkBaAddressLeft() {

		return CHK_BA_ADDRSS_LFT;

	}


	public void setChkBaAddressLeft(int CHK_BA_ADDRSS_LFT) {

		this.CHK_BA_ADDRSS_LFT = CHK_BA_ADDRSS_LFT;

	}

	public byte getChkBaMemoShow() {

		return CHK_BA_MM_SHW;

	}

	public void setChkBaMemoShow(byte CHK_BA_MM_SHW) {

		this.CHK_BA_MM_SHW = CHK_BA_MM_SHW;

	}

	public int getChkBaMemoTop() {

		return CHK_BA_MM_TP;

	}

	public void setChkBaMemoTop(int CHK_BA_MM_TP) {

		this.CHK_BA_MM_TP = CHK_BA_MM_TP;

	}

	public int getChkBaMemoLeft() {

		return CHK_BA_MM_LFT;

	}

	public void setChkBaMemoLeft(int CHK_BA_MM_LFT) {

		this.CHK_BA_MM_LFT = CHK_BA_MM_LFT;

	}

	public byte getChkBaDocNumberShow() {

		return CHK_BA_DC_NMBR_SHW;

	}

	public void setChkBaDocNumberShow(byte CHK_BA_DC_NMBR_SHW) {

		this.CHK_BA_DC_NMBR_SHW = CHK_BA_DC_NMBR_SHW;

	}

	public int getChkBaDocNumberTop() {

		return CHK_BA_DC_NMBR_TP;

	}

	public void setChkBaDocNumberTop(int CHK_BA_DC_NMBR_TP) {

		this.CHK_BA_DC_NMBR_TP = CHK_BA_DC_NMBR_TP;

	}

	public int getChkBaDocNumberLeft() {

		return CHK_BA_DC_NMBR_LFT;

	}


	public void setChkBaDocNumberLeft(int CHK_BA_DC_NMBR_LFT) {

		this.CHK_BA_DC_NMBR_LFT = CHK_BA_DC_NMBR_LFT;

	}
	
	public String getChkType() {

		return CHK_TYP;

	}

	public void setChkType(String CHK_TYP){

		this.CHK_TYP = CHK_TYP;

	}


	public double getChkTotalDebit() {

		return CHK_TTL_DBT;

	}

	public void setChkTotalDebit(double CHK_TTL_DBT) {

		this.CHK_TTL_DBT = CHK_TTL_DBT;

	}

	public double getChkTotalCredit() {

		return CHK_TTL_CRDT;

	}

	public void setChkTotalCredit(double CHK_TTL_CRDT) {

		this.CHK_TTL_CRDT = CHK_TTL_CRDT;

	}

	public ArrayList getChkAvList() {

		return chkAvList;

	}

	public void setChkAvList(ArrayList chkAvList) {

		this.chkAvList = chkAvList;

	}

	public ArrayList getChkDrList() {

		return chkDrList;

	}

	public void setChkDrList(ArrayList chkDrList) {

		this.chkDrList = chkDrList;

	}

	public ArrayList getChkVliList() {

		return chkVliList;

	}

	public void setChkVliList(ArrayList chkVliList) {

		this.chkVliList = chkVliList;

	}

	public String getChkDrCoaAccountDescription() {

		return CHK_DR_ACCNT_DESC;

	}

	public void setChkDrCoaAccountDescription(String CHK_DR_ACCNT_DESC) {

		this.CHK_DR_ACCNT_DESC = CHK_DR_ACCNT_DESC;

	}

	public String getChkDrCoaAccountNumber() {

		return CHK_DR_ACCNT_NMBR;

	}

	public void setChkDrCoaAccountNumber(String CHK_DR_ACCNT_NMBR) {

		this.CHK_DR_ACCNT_NMBR = CHK_DR_ACCNT_NMBR;

	}

	public double getChkDrAmount() {

		return CHK_DR_AMNT;

	}

	public void setChkDrAmount(double CHK_DR_AMNT) {

		this.CHK_DR_AMNT = CHK_DR_AMNT;

	}

	public byte getChkDrDebit() {

		return CHK_DR_DBT;

	}

	public void setChkDrDebit(byte CHK_DR_DBT) {

		this.CHK_DR_DBT = CHK_DR_DBT;

	}

	public String getChkApprovedRejectedByDescription() {

		return CHK_APPRVD_RJCTD_BY_DESC;

	}

	public void setApprovedRejectedByDescription(String CHK_APPRVD_RJCTD_BY_DESC) {

		this.CHK_APPRVD_RJCTD_BY_DESC = CHK_APPRVD_RJCTD_BY_DESC;

	}

	public String getChkCreatedByDescription() {

		return CHK_CRTD_BY_DESC;

	}

	public void setChkCreatedByDescription(String CHK_CRTD_BY_DESC) {

		this.CHK_CRTD_BY_DESC = CHK_CRTD_BY_DESC;

	}

	public int getChkBaFontSize() {

		return CHK_BA_FNT_SZ;

	}

	public void setChkBaFontSize(int CHK_BA_FNT_SZ) {

		this.CHK_BA_FNT_SZ = CHK_BA_FNT_SZ;

	}

	public String getChkBaFontStyle() {

		return CHK_BA_FNT_STYL;

	}

	public void setChkBaFontStyle(String CHK_BA_FNT_STYL) {

		this.CHK_BA_FNT_STYL = CHK_BA_FNT_STYL;
	}
	
	public String getChkBaFontWeight() {

		return CHK_BA_FNT_WGHT;

	}

	public void setChkBaFontWeight(String CHK_BA_FNT_WGHT) {

		this.CHK_BA_FNT_WGHT = CHK_BA_FNT_WGHT;
	}

	public String getChkTcType() {

		return CHK_TC_TYP;

	}

	public void setChkTcType(String CHK_TC_TYP) {

		this.CHK_TC_TYP = CHK_TC_TYP;

	}   

	public double getChkTcRate() {

		return CHK_TC_RT;

	}

	public void setChkTcRate(double CHK_TC_RT) {

		this.CHK_TC_RT = CHK_TC_RT;

	}

	public String getChkSplRemarks() {

		return CHK_SPL_RMRKS;

	}

	public void setChkSplRemarks(String CHK_SPL_RMRKS) {

		this.CHK_SPL_RMRKS = CHK_SPL_RMRKS;

	}
	public String getBrnhCde() {

		return CHK_BRNCH_CDE;

	}

	public void setBrnhCde(String CHK_BRNCH_CDE) {

		this.CHK_BRNCH_CDE = CHK_BRNCH_CDE;

	}
	
	public String getChkStatus() {

        return CHK_STATUS;

    }

    public void setChkStatus(String CHK_STATUS) {

        this.CHK_STATUS = CHK_STATUS;

    }

	public String getBrnhNm() {

		return CHK_BRNCH_NM;

	}

	public void setBrnhNm(String CHK_BRNCH_NM) {

		this.CHK_BRNCH_NM = CHK_BRNCH_NM;

	}
	
	public String getChkMisc1() {

		return CHK_MISC1;

	}

	public void setChkMisc1(String CHK_MISC1) {

		this.CHK_MISC1 = CHK_MISC1;

	}
	
	public String getChkMisc2() {

		return CHK_MISC2;

	}

	public void setChkMisc2(String CHK_MISC2) {

		this.CHK_MISC2 = CHK_MISC2;

	}
	
	public String getChkMisc3() {

		return CHK_MISC3;

	}

	public void setChkMisc3(String CHK_MISC3) {

		this.CHK_MISC3 = CHK_MISC3;

	}
	
	public String getChkMisc4() {

		return CHK_MISC4;

	}

	public void setChkMisc4(String CHK_MISC4) {

		this.CHK_MISC4 = CHK_MISC4;

	}
	
	public String getChkMisc5() {

		return CHK_MISC5;

	}

	public void setChkMisc5(String CHK_MISC5) {

		this.CHK_MISC5 = CHK_MISC5;

	}
	
	public String getChkMisc6() {

		return CHK_MISC6;

	}

	public void setChkMisc6(String CHK_MISC6) {

		this.CHK_MISC6 = CHK_MISC6;

	}
	
	public static Comparator sortByAccount = (r1, r2) -> {

        ApModCheckDetails receipt1 = (ApModCheckDetails) r1;
        ApModCheckDetails receipt2 = (ApModCheckDetails) r2;
        Debug.print(receipt1.getChkDrCoaAccountNumber() + "  << ETO NA >>  " + receipt2.getChkDrCoaAccountNumber());
        return receipt1.getChkDrCoaAccountNumber().compareTo(receipt2.getChkDrCoaAccountNumber());

    };
} // ApModCheckDetails class