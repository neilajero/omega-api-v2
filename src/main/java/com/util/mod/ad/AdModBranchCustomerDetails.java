/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


import com.util.ad.AdBranchCustomerDetails;

public class AdModBranchCustomerDetails extends AdBranchCustomerDetails implements java.io.Serializable{
	
	private String BCST_BR_BRNCH_CODE = null;
	private String BCST_BR_NAME = null;
	private String BCST_GL_COA_RCVBL_ACCNT_NMBR = null;
	private String BCST_GL_COA_RCVBL_ACCNT_DESC = null;
	private String BCST_GL_COA_RVN_ACCNT_NMBR = null;
	private String BCST_GL_COA_RVN_ACCNT_DESC = null;
	
	private String BCST_GL_COA_UNERND_INT_ACCNT_NMBR = null;
	private String BCST_GL_COA_UNERND_INT_ACCNT_DESC = null;
	
	private String BCST_GL_COA_ERND_INT_ACCNT_NMBR = null;
	private String BCST_GL_COA_ERND_INT_ACCNT_DESC = null;

	
	private String BCST_GL_COA_UNERND_PNT_ACCNT_NMBR = null;
	private String BCST_GL_COA_UNERND_PNT_ACCNT_DESC = null;
	
	private String BCST_GL_COA_ERND_PNT_ACCNT_NMBR = null;
	private String BCST_GL_COA_ERND_PNT_ACCNT_DESC = null;
	
	public AdModBranchCustomerDetails() {
    }
	
	public String getBcstBranchCode() {
		
		return this.BCST_BR_BRNCH_CODE;
		
	}
	
	public void setBcstBranchCode(String BCST_BR_BRNCH_CODE) {
		
		this.BCST_BR_BRNCH_CODE = BCST_BR_BRNCH_CODE;
		
	}
	
	public String getBcstBranchName() {
		
		return this.BCST_BR_NAME;
		
	}
	
	public void setBcstBranchName(String BCST_BR_NAME) {
		
		this.BCST_BR_NAME = BCST_BR_NAME;
		
	}
	
	public String getBcstReceivableAccountNumber() {
		
		return BCST_GL_COA_RCVBL_ACCNT_NMBR;
		
	}
	
	public void setBcstReceivableAccountNumber(String BCST_GL_COA_RCVBL_ACCNT_NMBR) {
		
		this.BCST_GL_COA_RCVBL_ACCNT_NMBR = BCST_GL_COA_RCVBL_ACCNT_NMBR;
		
	}
	
	public String getBcstReceivableAccountDescription() {
		
		return BCST_GL_COA_RCVBL_ACCNT_DESC;
		
	}
	
	public void setBcstReceivableAccountDescription(String BCST_GL_COA_RCVBL_ACCNT_DESC) {
		
		this.BCST_GL_COA_RCVBL_ACCNT_DESC = BCST_GL_COA_RCVBL_ACCNT_DESC;
		
	}
	
	public String getBcstRevenueAccountNumber() {
		
		return BCST_GL_COA_RVN_ACCNT_NMBR;
		
	}
	
	public void setBcstRevenueAccountNumber(String BCST_GL_COA_RVN_ACCNT_NMBR) {
		
		this.BCST_GL_COA_RVN_ACCNT_NMBR = BCST_GL_COA_RVN_ACCNT_NMBR;
		
	}
	
	public String getBcstRevenueAccountDescription() {
		
		return BCST_GL_COA_RVN_ACCNT_DESC;
	}
	
	public void setBcstRevenueAccountDescription(String BCST_GL_COA_RVN_ACCNT_DESC) {
		
		this.BCST_GL_COA_RVN_ACCNT_DESC = BCST_GL_COA_RVN_ACCNT_DESC;
	}
	
	
	
	
	
	
public String getBcstUnEarnedInterestAccountNumber() {
		
		return BCST_GL_COA_UNERND_INT_ACCNT_NMBR;
		
	}
	
	public void setBcstUnEarnedInterestAccountNumber(String BCST_GL_COA_UNERND_INT_ACCNT_NMBR) {
		
		this.BCST_GL_COA_UNERND_INT_ACCNT_NMBR = BCST_GL_COA_UNERND_INT_ACCNT_NMBR;
		
	}
	
	public String getBcstUnEarnedInterestAccountDescription() {
		
		return BCST_GL_COA_UNERND_INT_ACCNT_DESC;
	}
	
	public void setBcstUnEarnedInterestAccountDescription(String BCST_GL_COA_UNERND_INT_ACCNT_DESC) {
		
		this.BCST_GL_COA_UNERND_INT_ACCNT_DESC = BCST_GL_COA_UNERND_INT_ACCNT_DESC;
	}
	
	
	public String getBcstEarnedInterestAccountNumber() {
		
		return BCST_GL_COA_ERND_INT_ACCNT_NMBR;
		
	}
	
	public void setBcstEarnedInterestAccountNumber(String BCST_GL_COA_ERND_INT_ACCNT_NMBR) {
		
		this.BCST_GL_COA_ERND_INT_ACCNT_NMBR = BCST_GL_COA_ERND_INT_ACCNT_NMBR;
		
	}
	
	public String getBcstEarnedInterestAccountDescription() {
		
		return BCST_GL_COA_ERND_INT_ACCNT_DESC;
	}
	
	public void setBcstEarnedInterestAccountDescription(String BCST_GL_COA_ERND_INT_ACCNT_DESC) {
		
		this.BCST_GL_COA_ERND_INT_ACCNT_DESC = BCST_GL_COA_ERND_INT_ACCNT_DESC;
	}
	
	
	
	
public String getBcstUnEarnedPenaltyAccountNumber() {
		
		return BCST_GL_COA_UNERND_PNT_ACCNT_NMBR;
		
	}
	
	public void setBcstUnEarnedPenaltyAccountNumber(String BCST_GL_COA_UNERND_PNT_ACCNT_NMBR) {
		
		this.BCST_GL_COA_UNERND_PNT_ACCNT_NMBR = BCST_GL_COA_UNERND_PNT_ACCNT_NMBR;
		
	}
	
	public String getBcstUnEarnedPenaltyAccountDescription() {
		
		return BCST_GL_COA_UNERND_PNT_ACCNT_DESC;
	}
	
	public void setBcstUnEarnedPenaltyAccountDescription(String BCST_GL_COA_UNERND_PNT_ACCNT_DESC) {
		
		this.BCST_GL_COA_UNERND_PNT_ACCNT_DESC = BCST_GL_COA_UNERND_PNT_ACCNT_DESC;
	}
	
	
	public String getBcstEarnedPenaltyAccountNumber() {
		
		return BCST_GL_COA_ERND_PNT_ACCNT_NMBR;
		
	}
	
	public void setBcstEarnedPenaltyAccountNumber(String BCST_GL_COA_ERND_PNT_ACCNT_NMBR) {
		
		this.BCST_GL_COA_ERND_PNT_ACCNT_NMBR = BCST_GL_COA_ERND_PNT_ACCNT_NMBR;
		
	}
	
	public String getBcstEarnedPenaltyAccountDescription() {
		
		return BCST_GL_COA_ERND_PNT_ACCNT_DESC;
	}
	
	public void setBcstEarnedPenaltyAccountDescription(String BCST_GL_COA_ERND_PNT_ACCNT_DESC) {
		
		this.BCST_GL_COA_ERND_PNT_ACCNT_DESC = BCST_GL_COA_ERND_PNT_ACCNT_DESC;
	}
	
	
	
	
	
	
}//AdModBranchCustomerDetails