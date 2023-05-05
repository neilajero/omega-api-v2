/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;

import com.util.ap.ApPurchaseRequisitionDetails;

import java.util.ArrayList;

public class ApModPurchaseRequisitionDetails extends ApPurchaseRequisitionDetails implements java.io.Serializable {

	private String PR_TC_NM = null;
	private String PR_FC_NM = null;
	private double PR_AMNT;
	private String PR_AD_NTFD_USR1;
	private String PR_AD_BRNCH_CD = null;
	private Integer PR_BR_CODE = null;
	
	private String PR_SUPPLIER_NAME = null;

	private ArrayList prPrlList;
	private ArrayList prAPRList;
	
	public String getPrSupplierName() {
		return PR_SUPPLIER_NAME;
	}
	
	public void setPrSupplierName(String PR_SUPPLIER_NAME) {
		this.PR_SUPPLIER_NAME = PR_SUPPLIER_NAME;
	}

	public String getPrTcName() {

		return PR_TC_NM;

	}

	public void setPrTcName(String PR_TC_NM) {

		this.PR_TC_NM = PR_TC_NM;

	}

	public String getPrFcName() {

		return PR_FC_NM;

	}

	public void setPrFcName(String PR_FC_NM) {

		this.PR_FC_NM = PR_FC_NM;

	}

	public ArrayList getPrPrlList() {

		return prPrlList;

	}

	public void setPrPrlList(ArrayList prPrlList) {

		this.prPrlList = prPrlList;

	}

	public ArrayList getPrAPRList() {

		return prAPRList;

	}

	public void setPrAPRList(ArrayList prAPRList) {

		this.prAPRList = prAPRList;

	}

	public double getPrAmount() {

		return PR_AMNT;

	}

	public void setPrAmount(double PR_AMNT) {

		this.PR_AMNT = PR_AMNT;

	}

	public String getPrAdNotifiedUser1() {

		return PR_AD_NTFD_USR1;

	}

	public void setPrAdNotifiedUser1(String PR_AD_NTFD_USR1) {

		this.PR_AD_NTFD_USR1 = PR_AD_NTFD_USR1;

	}

	public String getPrAdBranchCode() {

		return PR_AD_BRNCH_CD;

	}

	public void setPrAdBranchCode(String PR_AD_BRNCH_CD) {

		this.PR_AD_BRNCH_CD = PR_AD_BRNCH_CD;

	}

	public Integer getPrBrCode() {
		return PR_BR_CODE;

	}

	public void setPrBrCode(Integer PR_BR_CODE) {
		this.PR_BR_CODE = PR_BR_CODE;
	}

}