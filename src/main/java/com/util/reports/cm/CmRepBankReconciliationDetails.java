/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.cm;

public class CmRepBankReconciliationDetails implements java.io.Serializable {

	private String BR_BNK_ACCNT;
	private String BR_DT_FRM;
	private String BR_DT_TO;
	
	private double BR_BGNNNG_BLNC_BK;
	private double BR_BGNNNG_BLNC_BNK;
	private double BR_DPST_AMNT_BK;
	private double BR_DPST_AMNT_BNK;
	private double BR_DSBRSMNT_AMNT_BK;
	private double BR_DSBRSMNT_AMNT_BNK;
	private double BR_DPST_IN_TRNST_AMNT;
	private double BR_OUTSTNDNG_CHCK_AMNT;
	private double BR_BNK_CRDT_AMNT;
	private double BR_BNK_DBT_AMNT;

	public CmRepBankReconciliationDetails() {
    }
	
	public String getBrBankAccount() {
		
		return BR_BNK_ACCNT;
		
	}
	
	public void setBrBankAccount(String BR_BNK_ACCNT) {
		
		this.BR_BNK_ACCNT = BR_BNK_ACCNT;
		
	}

	public String getBrDateFrom() {

		return BR_DT_FRM;

	}

	public void setBrDateFrom(String BR_DT_FRM) {

		this.BR_DT_FRM = BR_DT_FRM;

	}
	
	public String getBrDateTo() {
		
		return BR_DT_TO;
		
	}
	
	public void setBrDateTo(String BR_DT_TO) {
		
		this.BR_DT_TO = BR_DT_TO;
		
	}
	
	public double getBrBeginningBalancePerBook() {
		
		return BR_BGNNNG_BLNC_BK;
		
	}
	
	public void setBrBeginningBalancePerBook(double BR_BGNNNG_BLNC_BK) {
		
		this.BR_BGNNNG_BLNC_BK = BR_BGNNNG_BLNC_BK;
		
	}
	
	public double getBrBeginningBalancePerBank() {

		return BR_BGNNNG_BLNC_BNK;

	}

	public void setBrBeginningBalancePerBank(double BR_BGNNNG_BLNC_BNK) {

		this.BR_BGNNNG_BLNC_BNK = BR_BGNNNG_BLNC_BNK;

	}
	
	public double getBrDepositAmountPerBook() {
		
		return BR_DPST_AMNT_BK;
		
	}
	
	public void setBrDepositAmountPerBook(double BR_DPST_AMNT_BK) {
		
		this.BR_DPST_AMNT_BK = BR_DPST_AMNT_BK;
		
	}
	
	public double getBrDepositAmountPerBank() {

		return BR_DPST_AMNT_BNK;

	}

	public void setBrDepositAmountPerBank(double BR_DPST_AMNT_BNK) {

		this.BR_DPST_AMNT_BNK = BR_DPST_AMNT_BNK;

	}

	public double getBrDisbursementAmountPerBook() {
		
		return BR_DSBRSMNT_AMNT_BK;
		
	}
	
	public void setBrDisbursementAmountPerBook(double BR_DSBRSMNT_AMNT_BK) {
		
		this.BR_DSBRSMNT_AMNT_BK = BR_DSBRSMNT_AMNT_BK;
		
	}
	
	public double getBrDisbursementAmountPerBank() {

		return BR_DSBRSMNT_AMNT_BNK;

	}

	public void setBrDisbursementAmountPerBank(double BR_DSBRSMNT_AMNT_BNK) {

		this.BR_DSBRSMNT_AMNT_BNK = BR_DSBRSMNT_AMNT_BNK;

	}
	
	public double getBrDepositInTransitAmount() {
		
		return BR_DPST_IN_TRNST_AMNT;
		
	}
	
	public void setBrDepositInTransitAmount(double BR_DPST_IN_TRNST_AMNT) {
		
		this.BR_DPST_IN_TRNST_AMNT = BR_DPST_IN_TRNST_AMNT;
		
	}
	
	public double getBrOutstandingChecksAmount() {
		
		return BR_OUTSTNDNG_CHCK_AMNT;
		
	}
	
	public void setBrOutstandingChecksAmount(double BR_OUTSTNDNG_CHCK_AMNT) {
		
		this.BR_OUTSTNDNG_CHCK_AMNT = BR_OUTSTNDNG_CHCK_AMNT;
		
	}
	
	
	
	public double getBrBankCreditAmount() {
		
		return BR_BNK_CRDT_AMNT;
		
	}
	
	public void setBrBankCreditAmount(double BR_BNK_CRDT_AMNT) {
		
		this.BR_BNK_CRDT_AMNT = BR_BNK_CRDT_AMNT;
		
	}
	
	
	
	public double getBrBankDebitAmount() {
		
		return BR_BNK_DBT_AMNT;
		
	}
	
	public void setBrBankDebitAmount(double BR_BNK_DBT_AMNT) {
		
		this.BR_BNK_DBT_AMNT = BR_BNK_DBT_AMNT;
		
	}
	
	
	

} // CmRepBankReconciliationDetails class