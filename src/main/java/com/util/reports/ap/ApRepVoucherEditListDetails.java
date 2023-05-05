/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.util.Date;

public class ApRepVoucherEditListDetails implements java.io.Serializable {

	private String VEL_VOU_BTCH_NM;
	private String VEL_VOU_BTCH_DESC;
	private int VEL_VOU_TRNSCTN_TTL;
	private Date VEL_VOU_DT_CRTD;
	private String VEL_VOU_CRTD_BY;
	private String VEL_VOU_TYP;
	private String VEL_VOU_DCMNT_NMBR;
	private String VEL_VOU_RFRNC_NMBR;
	private Date VEL_VOU_DT;
	private String VEL_SPL_SPPLR_COD;
	private double VEL_VOU_AMNT_DUE;
	private String VEL_DR_ACCNT_NMBR;
	private String VEL_DR_ACCNT_DESC;
	private String VEL_DR_CLSS;
	private double VEL_DR_AMNT;
	private byte VEL_DR_DBT;
	private String VEL_VOU_DESC;
	
	private String VEL_SPL_SPPLR_NM;
	private String VEL_WTHHLDNG_TX_CODE;
	private String VEL_APPRVD_BY;
	private String VEL_PSTD_BY;
	private char VEL_CRRNCY_SYMBL;
	
	public ApRepVoucherEditListDetails() {
    }


	public String getVelDrAccountDescription() {
		
		return VEL_DR_ACCNT_DESC;
	
	}
	
	public void setVelDrAccountDescription(String vel_dr_accnt_desc) {
		
		VEL_DR_ACCNT_DESC = vel_dr_accnt_desc;
	
	}
	
	public String getVelDrAccountNumber() {
	
		return VEL_DR_ACCNT_NMBR;
	
	}
	
	public void setVelDrAccountNumber(String vel_dr_accnt_nmbr) {
	
		VEL_DR_ACCNT_NMBR = vel_dr_accnt_nmbr;
		
	}

	public double getVelDrAmount() {
	
		return VEL_DR_AMNT;
	
	}
	public void setVelDrAmount(double vel_dr_amnt) {
	
		VEL_DR_AMNT = vel_dr_amnt;
	
	}
	
	public String getVelDrClass() {
	
		return VEL_DR_CLSS;
	
	}
	public void setVelDrClass(String vel_dr_clss) {

		VEL_DR_CLSS = vel_dr_clss;
	
	}
	
	public String getVelSplSupplierCode() {
	
		return VEL_SPL_SPPLR_COD;
	
	}
	
	public void setVelSplSupplierCode(String vel_spl_spplr_cod) {
	
		VEL_SPL_SPPLR_COD = vel_spl_spplr_cod;
	
	}
	
	public double getVelVouAmountDue() {
	
		return VEL_VOU_AMNT_DUE;
	
	}
	
	public void setVelVouAmountDue(double vel_vou_amnt_due) {
	
		VEL_VOU_AMNT_DUE = vel_vou_amnt_due;
	
	}
	
	public String getVelVouCreatedBy() {
	
		return VEL_VOU_CRTD_BY;
	
	}
	
	public void setVelVouCreatedBy(String vel_vou_crtd_by) {
	
		VEL_VOU_CRTD_BY = vel_vou_crtd_by;
	
	}
	
	public String getVelVouDocumentNumber() {
	
		return VEL_VOU_DCMNT_NMBR;
	
	}
	
	public void setVelVouDocumentNumber(String vel_vou_dcmnt_nmbr) {
	
		VEL_VOU_DCMNT_NMBR = vel_vou_dcmnt_nmbr;
	
	}
	
	public Date getVelVouDate() {
	
		return VEL_VOU_DT;
	
	}
	
	public void setVelVouDate(Date vel_vou_dt) {
	
		VEL_VOU_DT = vel_vou_dt;
	
	}
	
	public Date getVelVouDateCreated() {
	
		return VEL_VOU_DT_CRTD;
	
	}
	
	public void setVelVouDateCreated(Date vel_vou_dt_crtd) {
	
		VEL_VOU_DT_CRTD = vel_vou_dt_crtd;
	
	}
	
	public String getVelVouReferenceNumber() {
	
		return VEL_VOU_RFRNC_NMBR;
	
	}
	
	public void setVelVouReferenceNumber(String vel_vou_rfrnc_nmbr) {
	
		VEL_VOU_RFRNC_NMBR = vel_vou_rfrnc_nmbr;
	
	}
	
	public String getVelVouType() {
	
		return VEL_VOU_TYP;
	
	}
	
	public void setVelVouType(String vel_vou_typ) {
	
		VEL_VOU_TYP = vel_vou_typ;
	
	}
	
	public byte getVelDrDebit() {
		
		return VEL_DR_DBT;
	
	}
	
	public void setVelDrDebit(byte vel_dr_dbt) {
	
		VEL_DR_DBT = vel_dr_dbt;
	
	}
	
	public String getVelVouBatchDescription() {
		
		return VEL_VOU_BTCH_DESC;
	}
	
	public void setVelVouBatchDescription(String vel_vou_btch_desc) {
	
		VEL_VOU_BTCH_DESC = vel_vou_btch_desc;
	
	}
	
	public String getVelVouBatchName() {
	
		return VEL_VOU_BTCH_NM;
	}
	
	public void setVelVouBatchName(String vel_vou_btch_nm) {
	
		VEL_VOU_BTCH_NM = vel_vou_btch_nm;
	
	}
	
	public int getVelVouTransactionTotal() {
	
		return VEL_VOU_TRNSCTN_TTL;
	
	}
	
	public void setVelVouTransactionTotal(int vel_vou_trnsctn_ttl) {
	
		VEL_VOU_TRNSCTN_TTL = vel_vou_trnsctn_ttl;
	}
	
	public String getVelVouDescription() {
		
		return VEL_VOU_DESC;
		
	}
	
	public void setVelVouDescription(String VEL_VOU_DESC) {
		
		this.VEL_VOU_DESC = VEL_VOU_DESC;
		
	}
	
	public String getVelSplSupplierName() {
		
		return VEL_SPL_SPPLR_NM;
	
	}
	
	public void setVelSplSupplierName(String VEL_SPL_SPPLR_NM) {
	
		this.VEL_SPL_SPPLR_NM = VEL_SPL_SPPLR_NM;
	
	}
	
	public String getVelWithholdingTaxCode() {
		
		return VEL_WTHHLDNG_TX_CODE;
		
	}

	public void setVelWithholdingTaxCode(String VEL_WTHHLDNG_TX_CODE) {
		
		this.VEL_WTHHLDNG_TX_CODE = VEL_WTHHLDNG_TX_CODE;
		
	}
	
	public String getVelApprovedBy() {
		
		return VEL_APPRVD_BY;
		
	}
	
	public void setVelApprovedBy(String VEL_APPRVD_BY) {
		
		this.VEL_APPRVD_BY = VEL_APPRVD_BY;
		
	}

	public String getVelPostedBy() {
		
		return VEL_PSTD_BY;
		
	}
	
	public void setVelPostedBy(String VEL_PSTD_BY) {
		
		this.VEL_PSTD_BY = VEL_PSTD_BY;
		
	}
	
	public char getVelCurrencySymbol() {
		
		return VEL_CRRNCY_SYMBL;
		
	}
	
	public void setVelCurrencySymbol(char VEL_CRRNCY_SYMBL) {
		
		this.VEL_CRRNCY_SYMBL = VEL_CRRNCY_SYMBL;
		
	}
	
	
 }  // ApRepVoucherEditListDetails