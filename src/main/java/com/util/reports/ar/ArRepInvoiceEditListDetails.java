/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Date;

public class ArRepInvoiceEditListDetails implements java.io.Serializable {

	private String IEL_INV_BTCH_NM;
	private String IEL_INV_BTCH_DESC;
	private int IEL_INV_TRNSCTN_TTL;
	private Date IEL_INV_DT_CRTD;
	private String IEL_INV_CRTD_BY;
	private String IEL_INV_TYP;
	private String IEL_INV_DCMNT_NMBR;
	private Date IEL_INV_DT;
	private String IEL_CST_CSTMR_CODE;
	private String IEL_SLS_PRSN_CODE;
	private String IEL_CST_CSTMR_NM;
	private double IEL_INV_AMNT_DUE;
	private String IEL_DR_ACCNT_NMBR;
	private String IEL_DR_ACCNT_DESC;
	private String IEL_DR_CLSS;
	private double IEL_DR_AMNT;
	private byte IEL_DR_DBT;
	private String IEL_INV_DESC;
	
	public ArRepInvoiceEditListDetails() {
    }


	public String getIelDrAccountDescription() {
		
		return IEL_DR_ACCNT_DESC;
	
	}
	
	public void setIelDrAccountDescription(String iel_dr_accnt_desc) {
		
		IEL_DR_ACCNT_DESC = iel_dr_accnt_desc;
	
	}
	
	public String getIelDrAccountNumber() {
	
		return IEL_DR_ACCNT_NMBR;
	
	}
	
	public void setIelDrAccountNumber(String iel_dr_accnt_nmbr) {
	
		IEL_DR_ACCNT_NMBR = iel_dr_accnt_nmbr;
		
	}

	public double getIelDrAmount() {
	
		return IEL_DR_AMNT;
	
	}
	public void setIelDrAmount(double iel_dr_amnt) {
	
		IEL_DR_AMNT = iel_dr_amnt;
	
	}
	
	public String getIelDrClass() {
	
		return IEL_DR_CLSS;
	
	}
	public void setIelDrClass(String iel_dr_clss) {

		IEL_DR_CLSS = iel_dr_clss;
	
	}
	
	public String getIelCstCustomerCode() {
	
		return IEL_CST_CSTMR_CODE;
	
	}
	
	public void setIelCstCustomerCode(String iel_cst_cstmr_code) {
	
		IEL_CST_CSTMR_CODE = iel_cst_cstmr_code;
	
	}
	
	public String getIelCstCustomerName() {
		
			return IEL_CST_CSTMR_NM;
		
	}
	
	public void setIelCstCustomerName(String iel_cst_cstmr_nm) {
	
		IEL_CST_CSTMR_NM = iel_cst_cstmr_nm;
	
	}
	
	public double getIelInvAmountDue() {
	
		return IEL_INV_AMNT_DUE;
	
	}
	
	public void setIelInvAmountDue(double iel_inv_amnt_due) {
	
		IEL_INV_AMNT_DUE = iel_inv_amnt_due;
	
	}
	
	public String getIelInvCreatedBy() {
	
		return IEL_INV_CRTD_BY;
	
	}
	
	public void setIelInvCreatedBy(String iel_inv_crtd_by) {
	
		IEL_INV_CRTD_BY = iel_inv_crtd_by;
	
	}
	
	public String getIelInvDocumentNumber() {
	
		return IEL_INV_DCMNT_NMBR;
	
	}
	
	public void setIelInvDocumentNumber(String iel_inv_dcmnt_nmbr) {
	
		IEL_INV_DCMNT_NMBR = iel_inv_dcmnt_nmbr;
	
	}
	
	public Date getIelInvDate() {
	
		return IEL_INV_DT;
	
	}
	
	public void setIelInvDate(Date iel_inv_dt) {
	
		IEL_INV_DT = iel_inv_dt;
	
	}
	
	public Date getIelInvDateCreated() {
	
		return IEL_INV_DT_CRTD;
	
	}
	
	public void setIelInvDateCreated(Date iel_inv_dt_crtd) {
	
		IEL_INV_DT_CRTD = iel_inv_dt_crtd;
	
	}
	
	public String getIelInvType() {
	
		return IEL_INV_TYP;
	
	}
	
	public void setIelInvType(String iel_inv_typ) {
	
		IEL_INV_TYP = iel_inv_typ;
	
	}
	
	public byte getIelDrDebit() {
		
		return IEL_DR_DBT;
	
	}
	
	public void setIelDrDebit(byte iel_dr_dbt) {
	
		IEL_DR_DBT = iel_dr_dbt;
	
	}
	
	public String getIelInvBatchDescription() {
		
		return IEL_INV_BTCH_DESC;
	}
	
	public void setIelInvBatchDescription(String iel_inv_btch_desc) {
	
		IEL_INV_BTCH_DESC = iel_inv_btch_desc;
	
	}
	
	public String getIelInvBatchName() {
	
		return IEL_INV_BTCH_NM;
	}
	
	public void setIelInvBatchName(String iel_inv_btch_nm) {
	
		IEL_INV_BTCH_NM = iel_inv_btch_nm;
	
	}
	
	public int getIelInvTransactionTotal() {
	
		return IEL_INV_TRNSCTN_TTL;
	
	}
	
	public void setIelInvTransactionTotal(int iel_inv_trnsctn_ttl) {
	
		IEL_INV_TRNSCTN_TTL = iel_inv_trnsctn_ttl;
	}
	
	public String getIelInvDescription() {
		
		return IEL_INV_DESC;
	
	}
	
	public void setIelInvDescription(String iel_vou_desc) {
	
		IEL_INV_DESC = iel_vou_desc;
	
	}
	
	public String getIelSalesPersonCode() {
		
		return IEL_SLS_PRSN_CODE;
	
	}
	
	public void setIelSalesPersonCode(String iel_sls_prsn_code) {
	
		IEL_SLS_PRSN_CODE = iel_sls_prsn_code;
	
	}
	
 }  // ApRepVoucherEditListDetails