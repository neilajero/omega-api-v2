/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;

import com.util.ad.AdBranchStandardMemoLineDetails;

/**
 * 
 * @author Franco Antonio R. Roig
 * Created: 10/27/2005	9:55 PM
 * 
 */

public class AdModBranchStandardMemoLineDetails extends AdBranchStandardMemoLineDetails {
	
	private Integer BR_CODE = null;
	private String BR_NM = null;
	private String BSML_GL_COA_ACCNT_NMBR = null;
	private String BSML_GL_COA_ACCNT_NMBR_DESC = null;
	
	private String BSML_GL_COA_RCVBL_ACCNT_NMBR = null;
	private String BSML_GL_COA_RCVBL_ACCNT_NMBR_DESC = null;
	private String BSML_GL_COA_RVN_ACCNT_NMBR = null;
	private String BSML_GL_COA_RVN_ACCNT_NMBR_DESC = null;

	public AdModBranchStandardMemoLineDetails() {
    }
	
	public Integer getBsmlBranchCode() {
		
		return this.BR_CODE;
		
	}
	
	public void setBsmlBranchCode(Integer BR_CODE) {
		
		this.BR_CODE = BR_CODE;
		
	}
	
	public String getBsmlBranchName() {
		
		return this.BR_NM;
		
	}
	
	public void setBsmlBranchName(String BR_NM) {
		
		this.BR_NM = BR_NM;
		
	}
	
	
	
	
	public String getBsmlAccountNumber() {
		
		return BSML_GL_COA_ACCNT_NMBR;
		
	}
	
	public void setBsmlAccountNumber(String BSML_GL_COA_ACCNT_NMBR) {
		
		this.BSML_GL_COA_ACCNT_NMBR = BSML_GL_COA_ACCNT_NMBR;
		
	}
	
	public String getBsmlAccountNumberDescription() {
		
		return BSML_GL_COA_ACCNT_NMBR_DESC;
		
	}
	
	public void setBsmlAccountNumberDescription(String BSML_GL_COA_ACCNT_NMBR_DESC) {
		
		this.BSML_GL_COA_ACCNT_NMBR_DESC = BSML_GL_COA_ACCNT_NMBR_DESC;
		
	}
	
	
	
	
	
	
public String getBsmlReceivableAccountNumber() {
		
		return BSML_GL_COA_RCVBL_ACCNT_NMBR;
		
	}
	
	public void setBsmlReceivableAccountNumber(String BSML_GL_COA_RCVBL_ACCNT_NMBR) {
		
		this.BSML_GL_COA_RCVBL_ACCNT_NMBR = BSML_GL_COA_RCVBL_ACCNT_NMBR;
		
	}
	
	public String getBsmlReceivableAccountNumberDescription() {
		
		return BSML_GL_COA_RCVBL_ACCNT_NMBR_DESC;
		
	}
	
	public void setBsmlReceivableAccountNumberDescription(String BSML_GL_COA_RCVBL_ACCNT_NMBR_DESC) {
		
		this.BSML_GL_COA_RCVBL_ACCNT_NMBR_DESC = BSML_GL_COA_RCVBL_ACCNT_NMBR_DESC;
		
	}
	
	
	
	
	
	public String getBsmlRevenueAccountNumber() {
		
		return BSML_GL_COA_RVN_ACCNT_NMBR;
		
	}
	
	public void setBsmlRevenueAccountNumber(String BSML_GL_COA_RVN_ACCNT_NMBR) {
		
		this.BSML_GL_COA_RVN_ACCNT_NMBR = BSML_GL_COA_RVN_ACCNT_NMBR;
		
	}
	
	public String getBsmlRevenueAccountNumberDescription() {
		
		return BSML_GL_COA_RVN_ACCNT_NMBR_DESC;
	}
	
	public void setBsmlRevenueAccountNumberDescription(String BSML_GL_COA_RVN_ACCNT_NMBR_DESC) {
		
		this.BSML_GL_COA_RVN_ACCNT_NMBR_DESC = BSML_GL_COA_RVN_ACCNT_NMBR_DESC;
	}
	
}