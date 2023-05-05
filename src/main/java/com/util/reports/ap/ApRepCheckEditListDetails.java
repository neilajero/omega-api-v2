/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.util.Date;

public class ApRepCheckEditListDetails implements java.io.Serializable {

	private String CEL_CHK_BTCH_NM;
	private String CEL_CHK_BTCH_DESC;
	private int CEL_CHK_TRNSCTN_TTL;
	private Date CEL_CHK_DT_CRTD;
	private String CEL_CHK_CRTD_BY;
	private String CEL_CHK_NMBR;
	private String CEL_CHK_DESC;
	private String CEL_CHK_DCMNT_NMBR;
	private Date CEL_CHK_DT;
	private String CEL_SPL_SPPLR_CODE;
	private String CEL_SPL_SPPLR_NM;
	private String CEL_CHK_AD_BA_NM;
	private double CEL_CHK_AMNT;
	private String CEL_DR_ACCNT_NMBR;
	private String CEL_DR_ACCNT_DESC;
	private String CEL_DR_CLSS;
	private double CEL_DR_AMNT;
	private byte CEL_DR_DBT;
	private String CEL_VOU_NMBR;
	private double CEL_APPLD_AMNT;
	
	public ApRepCheckEditListDetails() {
    }

	public double getCelAppliedAmount() {
		
		return CEL_APPLD_AMNT;
	
	}
	
	public void setCelAppliedAmount(double cel_appld_amnt) {
	
		CEL_APPLD_AMNT = cel_appld_amnt;
	
	}
	
	public double getCelChkAmount() {
	
		return CEL_CHK_AMNT;
	
	}
	
	public void setCelChkAmount(double cel_chk_amnt) {
	
		CEL_CHK_AMNT = cel_chk_amnt;
	
	}
	
	public String getCelChkBatchDescription() {
	
		return CEL_CHK_BTCH_DESC;
	
	}
	
	public void setCelChkBatchDescription(String cel_chk_btch_desc) {
	
		CEL_CHK_BTCH_DESC = cel_chk_btch_desc;
	
	}
	
	public String getCelChkBatchName() {
	
		return CEL_CHK_BTCH_NM;
	
	}
	
	public void setCelChkBatchName(String cel_chk_btch_nm) {
	
		CEL_CHK_BTCH_NM = cel_chk_btch_nm;
	
	}
	
	public String getCelChkCreatedBy() {
	
		return CEL_CHK_CRTD_BY;
	
	}
	
	public void setCelChkCreatedBy(String cel_chk_crtd_by) {
	
		CEL_CHK_CRTD_BY = cel_chk_crtd_by;
	
	}
	
	public String getCelChkDocumentNumber() {
	
		return CEL_CHK_DCMNT_NMBR;
	
	}
	
	public void setCelChkDocumentNumber(String cel_chk_dcmnt_nmbr) {
	
		CEL_CHK_DCMNT_NMBR = cel_chk_dcmnt_nmbr;
	
	}
	
	public String getCelChkDescription() {
	
		return CEL_CHK_DESC;
	
	}
	
	public void setCelChkDescription(String cel_chk_desc) {
	
		CEL_CHK_DESC = cel_chk_desc;
	
	}
	
	public Date getCelChkDate() {
	
		return CEL_CHK_DT;
	
	}
	
	public void setCelChkDate(Date cel_chk_dt) {
	
		CEL_CHK_DT = cel_chk_dt;
	
	}
	
	public Date getCelChkDateCreated() {
	
		return CEL_CHK_DT_CRTD;
	
	}
	
	public void setCelChkDateCreated(Date cel_chk_dt_crtd) {
	
		CEL_CHK_DT_CRTD = cel_chk_dt_crtd;
	
	}
	
	public String getCelChkNumber() {
	
		return CEL_CHK_NMBR;
	
	}
	
	public void setCelChkNumber(String cel_chk_nmbr) {
	
		CEL_CHK_NMBR = cel_chk_nmbr;
	
	}

	public int getCelChkTransactionTotal() {
	
		return CEL_CHK_TRNSCTN_TTL;
	
	}
	
	public void setCelChkTransactionTotal(int cel_chk_trnsctn_ttl) {
	
		CEL_CHK_TRNSCTN_TTL = cel_chk_trnsctn_ttl;
	
	}
	
	public String getCelDrAccountDescription() {
	
		return CEL_DR_ACCNT_DESC;
	
	}
	
	public void setCelDrAccountDescription(String cel_dr_accnt_desc) {
	
		CEL_DR_ACCNT_DESC = cel_dr_accnt_desc;
	
	}
	
	public String getCelDrAccountNumber() {
	
		return CEL_DR_ACCNT_NMBR;
	
	}
	
	public void setCelDrAccountNumber(String cel_dr_accnt_nmbr) {
	
		CEL_DR_ACCNT_NMBR = cel_dr_accnt_nmbr;
	
	}
	
	public double getCelDrAmount() {
	
		return CEL_DR_AMNT;
	
	}
	
	public void setCelDrAmount(double cel_dr_amnt) {
	
		CEL_DR_AMNT = cel_dr_amnt;
	
	}
	
	public String getCelDrClass() {
	
		return CEL_DR_CLSS;
	
	}
	
	public void setCelDrClass(String cel_dr_clss) {
	
		CEL_DR_CLSS = cel_dr_clss;
	
	}
	
	public byte getCelDrDebit() {
	
		return CEL_DR_DBT;
	
	}
	
	public void setCelDrDebit(byte cel_dr_dbt) {
	
		CEL_DR_DBT = cel_dr_dbt;
	
	}
	
	public String getCelVouNumber() {
	
		return CEL_VOU_NMBR;
	
	}
	
	public void setCelVouNumber(String cel_vou_nmbr) {
	
		CEL_VOU_NMBR = cel_vou_nmbr;
	
	}
	
	public String getCelChkAdBaName() {
	
		return CEL_CHK_AD_BA_NM;
	
	}
	
	public void setCelChkAdBaName(String cel_chk_ad_ba_nm) {
	
		CEL_CHK_AD_BA_NM = cel_chk_ad_ba_nm;
	
	}
	
	public String getCelSplSupplierCode() {
	
		return CEL_SPL_SPPLR_CODE;
	
	}
	
	public void setCelSplSupplierCode(String cel_spl_spplr_code) {
	
		CEL_SPL_SPPLR_CODE = cel_spl_spplr_code;
	
	}
	
	public String getCelSplSupplierName() {
	
		return CEL_SPL_SPPLR_NM;
	
	}
	
	public void setCelSplSupplierName(String cel_spl_spplr_nm) {
	
		CEL_SPL_SPPLR_NM = cel_spl_spplr_nm;
	
	}
 }  // ApRepVoucherEditListDetails