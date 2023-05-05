/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.io.Serializable;
import java.util.Date;


public class ArRepPostDatedCheckDetails implements Serializable {
	
	private String PDC_CSTMR_CODE;
	private String PDC_CSTMR_NM;
	private String PDC_DESC;
	private Date PDC_DATE_RCVD;
	private Date PDC_MTRTY_DATE;
	private String PDC_CHK_NMBR;
	private double PDC_AMNT;
	
	public ArRepPostDatedCheckDetails() {
    }
	
	public String getPdcCustomerCode() {
		
		return PDC_CSTMR_CODE;
		
	}
	
	public void setPdcCustomerCode(String PDC_CSTMR_CODE) {
		
		 this.PDC_CSTMR_CODE = PDC_CSTMR_CODE;
		
	}
	
	public String getPdcCustomerName() {

		return PDC_CSTMR_NM;

	}

	public void setPdcCustomerName(String PDC_CSTMR_NM) {

		this.PDC_CSTMR_NM = PDC_CSTMR_NM;

	}
	
	public String getPdcDescription() {
		
		return PDC_DESC;
		
	}
	
	public void setPdcDescription(String PDC_DESC) {
		
		this.PDC_DESC = PDC_DESC;
		
	}
	
	public Date getPdcDateReceived() {
		
		return PDC_DATE_RCVD;
		
	}
	
	public void setPdcDateReceived(Date PDC_DATE_RCVD) {
		
		this.PDC_DATE_RCVD = PDC_DATE_RCVD;
		
	}
	
	public Date getPdcMaturityDate() {
		
		return PDC_MTRTY_DATE;
		
	}
	
	public void setPdcMaturityDate(Date PDC_MTRTY_DATE) {
		
		this.PDC_MTRTY_DATE = PDC_MTRTY_DATE;
	}
	
	public String getPdcCheckNumber() {
		
		return PDC_CHK_NMBR;
		
	}
	
	public void setPdcCheckNumber(String PDC_CHK_NMBR) {
		
		this.PDC_CHK_NMBR = PDC_CHK_NMBR;
		
	}
	
	public double getPdcAmount() {
		
		return PDC_AMNT;
		
	}
	
	public void setPdcAmount(double PDC_AMNT) {
		
		this.PDC_AMNT = PDC_AMNT;
		
	}
}