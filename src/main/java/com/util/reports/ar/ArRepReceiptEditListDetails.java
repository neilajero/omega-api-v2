/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Date;

public class ArRepReceiptEditListDetails implements java.io.Serializable {

	private String REL_RCT_BTCH_NM;
	private String REL_RCT_BTCH_DESC;
	private int REL_RCT_TRNSCTN_TTL;
	private Date REL_RCT_DT_CRTD;
	private String REL_RCT_CRTD_BY;
	private String REL_RCT_NMBR;
	private Date REL_RCT_DT;
	private String IEL_CST_CSTMR_CODE;
	private String IEL_CST_CSTMR_NM;
	private String REL_RCT_RFRNC_NMBR;
    private String REL_RCT_DESC;
	private double REL_RCT_AMNT;
	private String IEL_DR_ACCNT_NMBR;
	private String IEL_DR_ACCNT_DESC;
	private String IEL_DR_CLSS;
	private double IEL_DR_AMNT;
	private byte IEL_DR_DBT;
	private String REL_INV_NMBR;
	private double REL_INV_AMNT_APPLD;
	
	public ArRepReceiptEditListDetails() {
    }


	public String getRelDrAccountDescription() {
		
		return IEL_DR_ACCNT_DESC;
	
	}
	
	public void setRelDrAccountDescription(String rel_dr_accnt_desc) {
		
		IEL_DR_ACCNT_DESC = rel_dr_accnt_desc;
	
	}
	
	public String getRelDrAccountNumber() {
	
		return IEL_DR_ACCNT_NMBR;
	
	}
	
	public void setRelDrAccountNumber(String rel_dr_accnt_nmbr) {
	
		IEL_DR_ACCNT_NMBR = rel_dr_accnt_nmbr;
		
	}

	public double getRelDrAmount() {
	
		return IEL_DR_AMNT;
	
	}
	public void setRelDrAmount(double rel_dr_amnt) {
	
		IEL_DR_AMNT = rel_dr_amnt;
	
	}
	
	public String getRelDrClass() {
	
		return IEL_DR_CLSS;
	
	}
	public void setRelDrClass(String rel_dr_clss) {

		IEL_DR_CLSS = rel_dr_clss;
	
	}
	
	public String getRelCstCustomerCode() {
	
		return IEL_CST_CSTMR_CODE;
	
	}
	
	public void setRelCstCustomerCode(String rel_cst_cstmr_code) {
	
		IEL_CST_CSTMR_CODE = rel_cst_cstmr_code;
	
	}
	
	public String getRelCstCustomerName() {
		
			return IEL_CST_CSTMR_NM;
		
	}
	
	public void setRelCstCustomerName(String rel_cst_cstmr_nm) {
	
		IEL_CST_CSTMR_NM = rel_cst_cstmr_nm;
	
	}
	
	public double getRelRctAmount() {
	
		return REL_RCT_AMNT;
	
	}
	
	public void setRelRctAmount(double rel_rct_amnt) {
	
		REL_RCT_AMNT = rel_rct_amnt;
	
	}
	
	public String getRelRctCreatedBy() {
	
		return REL_RCT_CRTD_BY;
	
	}
	
	public void setRelRctCreatedBy(String rel_rct_crtd_by) {
	
		REL_RCT_CRTD_BY = rel_rct_crtd_by;
	
	}
	
	public String getRelRctReferenceNumber() {
	
		return REL_RCT_RFRNC_NMBR;
	
	}
	
	public void setRelRctReferenceNumber(String rel_rct_rfrnc_nmbr) {
	
		REL_RCT_RFRNC_NMBR = rel_rct_rfrnc_nmbr;
	
	}
	
	public Date getRelRctDate() {
	
		return REL_RCT_DT;
	
	}
	
	public void setRelRctDate(Date rel_rct_dt) {
	
		REL_RCT_DT = rel_rct_dt;
	
	}
	
	public Date getRelRctDateCreated() {
	
		return REL_RCT_DT_CRTD;
	
	}
	
	public void setRelRctDateCreated(Date rel_rct_dt_crtd) {
	
		REL_RCT_DT_CRTD = rel_rct_dt_crtd;
	
	}
	
	public String getRelRctNumber() {
	
		return REL_RCT_NMBR;
	
	}
	
	public void setRelRctNumber(String rel_rct_nmbr) {
	
		REL_RCT_NMBR = rel_rct_nmbr;
	
	}
	
	public byte getRelDrDebit() {
		
		return IEL_DR_DBT;
	
	}
	
	public void setRelDrDebit(byte rel_dr_dbt) {
	
		IEL_DR_DBT = rel_dr_dbt;
	
	}
	
	public String getRelRctBatchDescription() {
		
		return REL_RCT_BTCH_DESC;
	}
	
	public void setRelRctBatchDescription(String rel_rct_btch_desc) {
	
		REL_RCT_BTCH_DESC = rel_rct_btch_desc;
	
	}
	
	public String getRelRctBatchName() {
	
		return REL_RCT_BTCH_NM;
	}
	
	public void setRelRctBatchName(String rel_rct_btch_nm) {
	
		REL_RCT_BTCH_NM = rel_rct_btch_nm;
	
	}
	
	public int getRelRctTransactionTotal() {
	
		return REL_RCT_TRNSCTN_TTL;
	
	}
	
	public void setRelRctTransactionTotal(int rel_rct_trnsctn_ttl) {
	
		REL_RCT_TRNSCTN_TTL = rel_rct_trnsctn_ttl;
	}
	
	public String getIEL_CST_CSTMR_NM() {
		
		return IEL_CST_CSTMR_NM;
	
	}
	
	public void setIEL_CST_CSTMR_NM(String rel_cst_cstmr_nm) {
	
		IEL_CST_CSTMR_NM = rel_cst_cstmr_nm;
	
	}
	public String getRelRctDescription() {
		
		return REL_RCT_DESC;
	
	}
	
	public void setRelRctDescription(String rel_rct_desc) {
	
		REL_RCT_DESC = rel_rct_desc;
	
	}

	public double getRelInvAmountApplied() {
		
		return REL_INV_AMNT_APPLD;
	
	}
	
	public void setRelInvAmountApplied(double rel_inv_amnt_appld) {
	
		REL_INV_AMNT_APPLD = rel_inv_amnt_appld;
	
	}
	
	public String getRelInvNumber() {
	
		return REL_INV_NMBR;
	
	}
	
	public void setRelInvNumber(String rel_inv_nmbr) {
	
		REL_INV_NMBR = rel_inv_nmbr;
	
	}
 }  // ApRepVoucherEditListDetails