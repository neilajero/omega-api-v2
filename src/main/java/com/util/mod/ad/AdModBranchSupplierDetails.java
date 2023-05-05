/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;

/**
 * 
 * @author Franco Antonio R. Roig
 * Created: 10/26/2005	8:43 PM
 * 
 */

public class AdModBranchSupplierDetails {
	
	private Integer BR_CODE = null;
	private String BR_NM = null;
	private String BSPL_GL_COA_PYBL_ACCNT_NMBR = null;
	private String BSPL_GL_COA_PYBL_ACCNT_DESC = null;
	private String BSPL_GL_COA_EXPNS_ACCNT_NMBR = null;
	private String BSPL_GL_COA_EXPNS_ACCNT_DESC = null;
	private String BSPL_DWNLD_STATUS = null;

	public AdModBranchSupplierDetails() {
    }
	
	public String getBSplDownloadStatus() {
		
		return this.BSPL_DWNLD_STATUS;
		
	}
	
	public void setBSplDownloadStatus(String BSPL_DWNLD_STATUS) {
		
		this.BSPL_DWNLD_STATUS = BSPL_DWNLD_STATUS;
		
	}
	
	public Integer getBSplBranchCode() {
		
		return this.BR_CODE;
		
	}
	
	public void setBSplBranchCode(Integer BR_CODE) {
		
		this.BR_CODE = BR_CODE;
		
	}
	
	public String getBSplBranchName() {
		
		return this.BR_NM;
		
	}
	
	public void setBSplBranchName(String BR_NM) {
		
		this.BR_NM = BR_NM;
		
	}
	
	public String getBSplPayableAccountNumber() {
		
		return BSPL_GL_COA_PYBL_ACCNT_NMBR;
		
	}
	
	public void setBSplPayableAccountNumber(String BSPL_GL_COA_PYBL_ACCNT_NMBR) {
		
		this.BSPL_GL_COA_PYBL_ACCNT_NMBR = BSPL_GL_COA_PYBL_ACCNT_NMBR;
		
	}
	
	public String getBSplPayableAccountDesc() {
		
		return BSPL_GL_COA_PYBL_ACCNT_DESC;
		
	}
	
	public void setBSplPayableAccountDesc(String BSPL_GL_COA_PYBL_ACCNT_DESC) {
		
		this.BSPL_GL_COA_PYBL_ACCNT_DESC = BSPL_GL_COA_PYBL_ACCNT_DESC;
		
	}
	
	public String getBSplExpenseAccountNumber() {
		
		return BSPL_GL_COA_EXPNS_ACCNT_NMBR;
		
	}
	
	public void setBSplExpenseAccountNumber(String BSPL_GL_COA_EXPNS_ACCNT_NMBR) {
		
		this.BSPL_GL_COA_EXPNS_ACCNT_NMBR = BSPL_GL_COA_EXPNS_ACCNT_NMBR;
		
	}
	
	public String getBSplExpenseAccountDesc() {
		
		return BSPL_GL_COA_EXPNS_ACCNT_DESC;
	}
	
	public void setBSplExpenseAccountDesc(String BSPL_GL_COA_EXPNS_ACCNT_DESC) {
		
		this.BSPL_GL_COA_EXPNS_ACCNT_DESC = BSPL_GL_COA_EXPNS_ACCNT_DESC;
	}
	
}