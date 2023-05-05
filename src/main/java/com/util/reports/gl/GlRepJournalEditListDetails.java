/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;

import java.util.Date;

public class GlRepJournalEditListDetails implements java.io.Serializable {

	private String JEL_JR_BTCH_NM;
	private String JEL_JR_BTCH_DESC;
	private int JEL_JR_TRNSCTN_TTL;
	private Date JEL_JR_DT_CRTD;
	private String JEL_JR_CRTD_BY;
	private Date JEL_JR_DT;
	private String JEL_JR_DCMNT_NMBR;
	private String JEL_JR_RFRNC_NMBR;
	private String JEL_JR_DESC;
	private String JEL_JL_ACCNT_NMBR;
	private String JEL_JL_ACCNT_DESC;
	private double JEL_JL_AMNT;
	private byte JEL_JL_DBT;
	private String JEL_JL_DESC;
	
	public GlRepJournalEditListDetails() {
    }

	
	public String getJelJlAccountDescription() {
		
		return JEL_JL_ACCNT_DESC;
	
	}
	
	public void setJelJlAccountDescription(String jel_jl_accnt_desc) {
		
		JEL_JL_ACCNT_DESC = jel_jl_accnt_desc;
	
	}
	
	public String getJelJlAccountNumber() {
	
		return JEL_JL_ACCNT_NMBR;
	
	}
	
	public void setJelJlAccountNumber(String jel_jl_accnt_nmbr) {
	
		JEL_JL_ACCNT_NMBR = jel_jl_accnt_nmbr;
		
	}

	public double getJelJlAmount() {
	
		return JEL_JL_AMNT;
	
	}
	public void setJelJlAmount(double jel_jl_amnt) {
	
		JEL_JL_AMNT = jel_jl_amnt;
	
	}
	
	public String getJelJrCreatedBy() {
	
		return JEL_JR_CRTD_BY;
	
	}
	
	public void setJelJrCreatedBy(String jel_jr_crtd_by) {
	
		JEL_JR_CRTD_BY = jel_jr_crtd_by;
	
	}
	
	public String getJelJrDocumentNumber() {
	
		return JEL_JR_DCMNT_NMBR;
	
	}
	
	public void setJelJrDocumentNumber(String jel_jr_dcmnt_nmbr) {
	
		JEL_JR_DCMNT_NMBR = jel_jr_dcmnt_nmbr;
	
	}
	
	public String getJelJrReferenceNumber() {
		
		return JEL_JR_RFRNC_NMBR;
		
	}
	
	public void setJelJrReferenceNumber(String jel_jr_rfrnc_nmbr) {
	
		JEL_JR_RFRNC_NMBR = jel_jr_rfrnc_nmbr;
	
	}
	
	public String getJelJrDescription() {
		
	    return JEL_JR_DESC;
		
	}
	
	public void setJelJrDescription(String jel_jr_desc) {
	
		JEL_JR_DESC = jel_jr_desc;
	
	}
	
	public Date getJelJrDate() {
	
		return JEL_JR_DT;
	
	}
	
	public void setJelJrDate(Date jel_jr_dt) {
	
		JEL_JR_DT = jel_jr_dt;
	
	}
	
	public Date getJelJrDateCreated() {
	
		return JEL_JR_DT_CRTD;
	
	}
	
	public void setJelJrDateCreated(Date jel_jr_dt_crtd) {
	
		JEL_JR_DT_CRTD = jel_jr_dt_crtd;
	
	}
	
	public String getJelJrBatchDescription() {
		
		return JEL_JR_BTCH_DESC;
	}
	
	public void setJelJrBatchDescription(String jel_jr_btch_desc) {
	
		JEL_JR_BTCH_DESC = jel_jr_btch_desc;
	
	}
	
	public String getJelJrBatchName() {
	
		return JEL_JR_BTCH_NM;
	}
	
	public void setJelJrBatchName(String jel_jr_btch_nm) {
	
		JEL_JR_BTCH_NM = jel_jr_btch_nm;
	
	}
	
	public int getJelJrTransactionTotal() {
	
		return JEL_JR_TRNSCTN_TTL;
	
	}
	
	public void setJelJrTransactionTotal(int jel_jr_trnsctn_ttl) {
	
		JEL_JR_TRNSCTN_TTL = jel_jr_trnsctn_ttl;
	}

	public byte getJelJlDebit() {
		
		return JEL_JL_DBT;
	
	}

	public void setJelJlDebit(byte jel_jl_dbt) {
	
		JEL_JL_DBT = jel_jl_dbt;
	
	}
	
	public String getJelJlDescription() {

		return JEL_JL_DESC;

	}

	public void setJelJlDescription(String jel_jl_desc) {

		JEL_JL_DESC = jel_jl_desc;

	}
	
 }  // GlRepJournalEditListDetails