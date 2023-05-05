/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArCustomerDetails;

import java.util.ArrayList;
import java.util.Date;


public class ArModCustomerDetails extends ArCustomerDetails implements java.io.Serializable {
	
	private String CST_HR_EMPLYEE_NMBR;
	private String CST_HR_BIO_NMBR;
	private String CST_HR_DPLYD_BRNCH_NM;
	private String CST_HR_MNGNG_BRNCH;
	private String CST_HR_CRRNT_JB_PSTN;
    
    private String CST_CT_NM;
    private String CST_PYT_NM;
    private String CST_CC_NM;
    private String CST_SPPLR_CD;
    private String CST_CC_TC_NM;
    private String CST_CC_WTC_NM;
    private String CST_CT_BA_NM;
    private String CST_AD_BA_NM;
    private String CST_GL_COA_RCVBL_ACCNT_NMBR;
    private String CST_GL_COA_RCVBL_ACCNT_DESC;
    
    private String CST_GL_COA_RVNUE_ACCNT_NMBR;
    private String CST_GL_COA_RVNUE_ACCNT_DESC;
    
    private String CST_GL_COA_UNERND_INT_ACCNT_NMBR;
    private String CST_GL_COA_UNERND_INT_ACCNT_DESC;
    
    private String CST_GL_COA_ERND_INT_ACCNT_NMBR;
    private String CST_GL_COA_ERND_INT_ACCNT_DESC;
    
    private String CST_GL_COA_UNERND_PNT_ACCNT_NMBR;
    private String CST_GL_COA_UNERND_PNT_ACCNT_DESC;
    
    private String CST_GL_COA_ERND_PNT_ACCNT_NMBR;
    private String CST_GL_COA_ERND_PNT_ACCNT_DESC;
    
    private String CST_BA_NM;
    private String CST_LIT_NM;
    private String CST_PRKNG_ID;
    private double CST_RPT_RT;
    
    
    private String CST_AR;
    private Date CST_ENTRY_DT;
    private short CST_EFFCTVTY_DYS;
    
    // for sales 2550Q BIR FORM
    private double CST_EXMPT_SLS;
    private double CST_ZR_RTD_SLS;
    private double CST_TXBL_SLS;
    private double CST_OUPT_TX;
    private double CST_TTL_EXMPT_SLS;
    private double CST_TTL_ZR_RTD_SLS;
    private double CST_TTL_TXBL_SLS;
    private double CST_TTL_OUPT_TX;
    
    private ArrayList bCstList;
    
    private String CST_SLP_SLSPRSN_CODE = null;
    private String CST_SLP_NM = null;
    private double CST_CC_TC_RT;
    private String CST_CC_TC_TYP;
    
    private String CST_SLP_SLSPRSN_CODE2 = null;
    private String CST_SLP_NM2 = null;
    
    private double CST_NO_PRKNG;
    private double CST_SQ_MTR;

    private String companyShortName;
    
    
    public ArModCustomerDetails () {
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName;
    }


    public String getCstHrEmployeeNumber() {
 	   	
    	   return CST_HR_EMPLYEE_NMBR;
    	
    }  
    
    public void setCstHrEmployeeNumber(String CST_HR_EMPLYEE_NMBR) {
    	
    	  this.CST_HR_EMPLYEE_NMBR = CST_HR_EMPLYEE_NMBR;
    	
    }
    
    public String getCstHrBioNumber() {
 	   	
 	   return CST_HR_BIO_NMBR;
 	
 }  
 
 public void setCstHrBioNumber(String CST_HR_BIO_NMBR) {
 	
 	  this.CST_HR_BIO_NMBR = CST_HR_BIO_NMBR;
 	
 }

    public String getCstHrDeployedBranchName() {
 	   	
    	   return CST_HR_DPLYD_BRNCH_NM;
    	
    }  
    
    public void setCstHrDeployedBranchName(String CST_HR_DPLYD_BRNCH_NM) {
    	
    	  this.CST_HR_DPLYD_BRNCH_NM = CST_HR_DPLYD_BRNCH_NM;
    	
    }
    
    public String getCstHrManagingBranch() {
 	   	
 	   return CST_HR_MNGNG_BRNCH;
 	
 }  
 
 public void setCstHrManagingBranch(String CST_HR_MNGNG_BRNCH) {
 	
 	  this.CST_HR_MNGNG_BRNCH = CST_HR_MNGNG_BRNCH;
 	
 }
 
 
 public String getCstHrCurrentJobPosition() {
	   	
	   return CST_HR_CRRNT_JB_PSTN;
	
}  

public void setCstHrCurrentJobPosition(String CST_HR_CRRNT_JB_PSTN) {
	
	  this.CST_HR_CRRNT_JB_PSTN = CST_HR_CRRNT_JB_PSTN;
	
}
    
    
    public String getCstCtName() {
        
        return CST_CT_NM;
        
    }
    
    public void setCstCtName(String CST_CT_NM) {
        
        this.CST_CT_NM = CST_CT_NM;
        
    }
    
    public String getCstPytName() {
        
        return CST_PYT_NM;
        
    }
    
    public void setCstPytName(String CST_PYT_NM) {
        
        this.CST_PYT_NM = CST_PYT_NM;
        
    } 
    
    public String getCstCcName() {
        
        return CST_CC_NM;
        
    }
    
    public void setCstCcName(String CST_CC_NM) {
        
        this.CST_CC_NM = CST_CC_NM;
        
    }
    
    
public String getCstSupplierCode() {
        
        return CST_SPPLR_CD;
        
    }
    
    public void setCstSupplierCode(String CST_SPPLR_CD) {
        
        this.CST_SPPLR_CD = CST_SPPLR_CD;
        
    }
    
    public String getCstCcTcName() {
        
        return CST_CC_TC_NM;
        
    }
    
    public void setCstCcTcName(String CST_CC_TC_NM) {
        
        this.CST_CC_TC_NM = CST_CC_TC_NM;
        
    }
    
    public String getCstCcWtcName() {
        
        return CST_CC_WTC_NM;
        
    }
    
    public void setCstCcWtcName(String CST_CC_WTC_NM) {
        
        this.CST_CC_WTC_NM = CST_CC_WTC_NM;
        
    }
    
    public String getCstCtBaName() {
        
        return CST_CT_BA_NM;
        
    }
    
    public void setCstCtBaName(String CST_CT_BA_NM) {
        
        this.CST_CT_BA_NM = CST_CT_BA_NM;
        
    }
    
    public String getCstAdBaName() {
        
        return CST_AD_BA_NM;
        
    }
    
    public void setCstAdBaName(String CST_AD_BA_NM) {
        
        this.CST_AD_BA_NM = CST_AD_BA_NM;
        
    }   
    
    public String getCstGlCoaReceivableAccountNumber() {
        
        return CST_GL_COA_RCVBL_ACCNT_NMBR;
        
    }
    
    public void setCstGlCoaReceivableAccountNumber(String CST_GL_COA_RCVBL_ACCNT_NMBR) {
        
        this.CST_GL_COA_RCVBL_ACCNT_NMBR = CST_GL_COA_RCVBL_ACCNT_NMBR;
        
    }
    
    public String getCstGlCoaReceivableAccountDescription() {
        
        return CST_GL_COA_RCVBL_ACCNT_DESC;
        
    }
    
    public void setCstGlCoaReceivableAccountDescription(String CST_GL_COA_RCVBL_ACCNT_DESC) {
        
        this.CST_GL_COA_RCVBL_ACCNT_DESC = CST_GL_COA_RCVBL_ACCNT_DESC;
        
    }
    
    public String getCstGlCoaRevenueAccountNumber() {
        
        return CST_GL_COA_RVNUE_ACCNT_NMBR;
        
    }
    
    public void setCstGlCoaRevenueAccountNumber(String CST_GL_COA_RVNUE_ACCNT_NMBR) {
        
        this.CST_GL_COA_RVNUE_ACCNT_NMBR = CST_GL_COA_RVNUE_ACCNT_NMBR;
        
    }
    
    public String getCstGlCoaRevenueAccountDescription() {
        
        return CST_GL_COA_RVNUE_ACCNT_DESC;
        
    }
    
    public void setCstGlCoaRevenueAccountDescription(String CST_GL_COA_RVNUE_ACCNT_DESC) {
        
        this.CST_GL_COA_RVNUE_ACCNT_DESC = CST_GL_COA_RVNUE_ACCNT_DESC;
        
    }
    
    
    
    
    
public String getCstGlCoaUnEarnedInterestAccountNumber() {
        
        return CST_GL_COA_UNERND_INT_ACCNT_NMBR;
        
    }
    
    public void setCstGlCoaUnEarnedInterestAccountNumber(String CST_GL_COA_UNERND_INT_ACCNT_NMBR) {
        
        this.CST_GL_COA_UNERND_INT_ACCNT_NMBR = CST_GL_COA_UNERND_INT_ACCNT_NMBR;
        
    }
    
    public String getCstGlCoaUnEarnedInterestAccountDescription() {
        
        return CST_GL_COA_UNERND_INT_ACCNT_DESC;
        
    }
    
    public void setCstGlCoaUnEarnedInterestAccountDescription(String CST_GL_COA_UNERND_INT_ACCNT_DESC) {
        
        this.CST_GL_COA_UNERND_INT_ACCNT_DESC = CST_GL_COA_UNERND_INT_ACCNT_DESC;
        
    }
    
    
public String getCstGlCoaEarnedInterestAccountNumber() {
        
        return CST_GL_COA_ERND_INT_ACCNT_NMBR;
        
    }
    
    public void setCstGlCoaEarnedInterestAccountNumber(String CST_GL_COA_ERND_INT_ACCNT_NMBR) {
        
        this.CST_GL_COA_ERND_INT_ACCNT_NMBR = CST_GL_COA_ERND_INT_ACCNT_NMBR;
        
    }
    
    public String getCstGlCoaEarnedInterestAccountDescription() {
        
        return CST_GL_COA_ERND_INT_ACCNT_DESC;
        
    }
    
    public void setCstGlCoaEarnedInterestAccountDescription(String CST_GL_COA_ERND_INT_ACCNT_DESC) {
        
        this.CST_GL_COA_ERND_INT_ACCNT_DESC = CST_GL_COA_ERND_INT_ACCNT_DESC;
        
    }
    
    
public String getCstGlCoaUnEarnedPenaltyAccountNumber() {
        
        return CST_GL_COA_UNERND_PNT_ACCNT_NMBR;
        
    }
    
    public void setCstGlCoaUnEarnedPenaltyAccountNumber(String CST_GL_COA_UNERND_PNT_ACCNT_NMBR) {
        
        this.CST_GL_COA_UNERND_PNT_ACCNT_NMBR = CST_GL_COA_UNERND_PNT_ACCNT_NMBR;
        
    }
    
    public String getCstGlCoaUnEarnedPenaltyAccountDescription() {
        
        return CST_GL_COA_UNERND_PNT_ACCNT_DESC;
        
    }
    
    public void setCstGlCoaUnEarnedPenaltyAccountDescription(String CST_GL_COA_UNERND_PNT_ACCNT_DESC) {
        
        this.CST_GL_COA_UNERND_PNT_ACCNT_DESC = CST_GL_COA_UNERND_PNT_ACCNT_DESC;
        
    }
    
    
public String getCstGlCoaEarnedPenaltyAccountNumber() {
        
        return CST_GL_COA_ERND_PNT_ACCNT_NMBR;
        
    }
    
    public void setCstGlCoaEarnedPenaltyAccountNumber(String CST_GL_COA_ERND_PNT_ACCNT_NMBR) {
        
        this.CST_GL_COA_ERND_PNT_ACCNT_NMBR = CST_GL_COA_ERND_PNT_ACCNT_NMBR;
        
    }
    
    public String getCstGlCoaEarnedPenaltyAccountDescription() {
        
        return CST_GL_COA_ERND_PNT_ACCNT_DESC;
        
    }
    
    public void setCstGlCoaEarnedPenaltyAccountDescription(String CST_GL_COA_ERND_PNT_ACCNT_DESC) {
        
        this.CST_GL_COA_ERND_PNT_ACCNT_DESC = CST_GL_COA_ERND_PNT_ACCNT_DESC;
        
    }
    
    public String getCstBaName() {
        
        return CST_BA_NM;
        
    }
    
    public void setCstBaName(String CST_BA_NM) {
        
        this.CST_BA_NM = CST_BA_NM;
        
    }
    
    public double getCstExemptSales() {
        
        return CST_EXMPT_SLS;
        
    }
    
    public void setCstExemptSales(double cst_exmpt_sls) {
        
        CST_EXMPT_SLS = cst_exmpt_sls;
        
    }
    
    public double getCstOutputTax() {
        
        return CST_OUPT_TX;
        
    }
    
    public void setCstOutputTax(double cst_oupt_tx) {
        
        CST_OUPT_TX = cst_oupt_tx;
        
    }
    
    public double getCstTaxableSales() {
        
        return CST_TXBL_SLS;
        
    }
    
    public void setCstTaxableSales(double cst_txbl_sls) {
        
        CST_TXBL_SLS = cst_txbl_sls;
        
    }
    
    public double getCstZeroRatedSales() {
        
        return CST_ZR_RTD_SLS;
        
    }
    
    public void setCstZeroRatedSales(double cst_zr_rtd_sls) {
        
        CST_ZR_RTD_SLS = cst_zr_rtd_sls;
        
    }
    public double getCstTotalExemptSales() {
        
        return CST_TTL_EXMPT_SLS;
        
    }
    
    public void setCstTotalExemptSales(double cst_ttl_exmpt_sls) {
        
        CST_TTL_EXMPT_SLS = cst_ttl_exmpt_sls;
        
    }
    
    public double getCstTotalOutputTax() {
        
        return CST_TTL_OUPT_TX;
        
    }
    
    public void setCstTotalOutputTax(double cst_ttl_oupt_tx) {
        
        CST_TTL_OUPT_TX = cst_ttl_oupt_tx;
        
    }
    
    public double getCstTotalTaxableSales() {
        
        return CST_TTL_TXBL_SLS;
        
    }
    
    public void setCstTotalTaxableSales(double cst_ttl_txbl_sls) {
        
        CST_TTL_TXBL_SLS = cst_ttl_txbl_sls;
        
    }
    
    public double getCstTotalZeroRatedSales() {
        
        return CST_TTL_ZR_RTD_SLS;
        
    }
    
    public void setCstTotalZeroRatedSales(double cst_ttl_zr_rtd_sls) {
        
        CST_TTL_ZR_RTD_SLS = cst_ttl_zr_rtd_sls;
        
    }
    
    public ArrayList getBcstList() {
        
        return bCstList;
        
    }
    
    public void setBcstList(ArrayList bCstList) {
        
        this.bCstList = bCstList;
        
    }
    
    public String getCstSlpSalespersonCode() {
        
        return CST_SLP_SLSPRSN_CODE;
        
    }
    
    public void setCstSlpSalespersonCode(String CST_SLP_SLSPRSN_CODE) {
        
        this.CST_SLP_SLSPRSN_CODE = CST_SLP_SLSPRSN_CODE;
        
    }
    
    public String getCstSlpName() {
        
        return CST_SLP_NM;
        
    }
    
    public void setCstSlpName(String CST_SLP_NM) {
        
        this.CST_SLP_NM = CST_SLP_NM;
        
    }
    
    public String getCstSlpSalespersonCode2() {
        
        return CST_SLP_SLSPRSN_CODE2;
        
    }
    
    public void setCstSlpSalespersonCode2(String CST_SLP_SLSPRSN_CODE2) {
        
        this.CST_SLP_SLSPRSN_CODE2 = CST_SLP_SLSPRSN_CODE2;
        
    }
    
    public String getCstSlpName2() {
        
        return CST_SLP_NM2;
        
    }
    
    public void setCstSlpName2(String CST_SLP_NM2) {
        
        this.CST_SLP_NM2 = CST_SLP_NM2;
        
    }
    
    public double getCstCcTcRate() {
        
        return CST_CC_TC_RT;
        
    }
    
    public void setCstCcTcRate(double CST_CC_TC_RT) {
        
        this.CST_CC_TC_RT = CST_CC_TC_RT;
        
    }
    
    public String getCstCcTcType() {
        
        return CST_CC_TC_TYP;
        
    }
    
    public void setCstCcTcType(String CST_CC_TC_TYP) {
        
        this.CST_CC_TC_TYP = CST_CC_TC_TYP;
        
    }
    
    public String getCstLitName() {
        
        return CST_LIT_NM;
        
    }
    
    public void setCstLitName(String CST_LIT_NM) {
        
        this.CST_LIT_NM = CST_LIT_NM;
        
    }
    
    public String getCstArea() {
        
        return CST_AR;
        
    }
    
    public void setCstArea(String CST_AR) {
        
        this.CST_AR = CST_AR;
        
    }
    
    public Date getCstEntryDate() {
        
        return CST_ENTRY_DT;
        
    }
    
    public void setCstEntryDate(Date CST_ENTRY_DT) {
        
        this.CST_ENTRY_DT = CST_ENTRY_DT;
        
    }
    
    public short getCstEffectivityDays() {
        
        return CST_EFFCTVTY_DYS;
        
    }
    
    public void setCstEffectivityDays(short CST_EFFCTVTY_DYS) {
        
        this.CST_EFFCTVTY_DYS = CST_EFFCTVTY_DYS;
        
    }
    
    
  
    
    public double getCstNumbersParking() {
        
        return CST_NO_PRKNG;
        
    }
    
    public void setCstNumbersParking(double CST_NO_PRKNG) {
        
        this.CST_NO_PRKNG = CST_NO_PRKNG;
        
    }
    
    
    public String getCstParkingID(){
    	
    	return CST_PRKNG_ID;
    }
    
    public void setCstParkingID(String CST_PRKNG_ID){
    	
    	this.CST_PRKNG_ID = CST_PRKNG_ID;
    }
    
    public double getCstRealPropertyTaxRate(){
    	
    	return CST_RPT_RT;
    }
    
    public void setCstRealPropertyTaxRate(double CST_RPT_RT){
    	
    	this.CST_RPT_RT = CST_RPT_RT;
    }


    public double getCstSquareMeter() {
        
        return CST_SQ_MTR;
        
    }
    
    public void setCstSquareMeter(double CST_SQ_MTR) {
        
        this.CST_SQ_MTR = CST_SQ_MTR;
        
    }
    
} // ArModCustomerDetails class   