/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.ArrayList;


public class InvRepInventoryListDetails implements java.io.Serializable {

	private String RIL_II_NM;
	private String RIL_II_DESC;
	private String RIL_II_CLSS;
	private String RIL_LOC_NM;
	private double RIL_QTY;
	private String RIL_UNT;
	private double RIL_UNT_CST;
	private double RIL_AMNT;
	private double RIL_SLS_PRC;
	private double RIL_AVE_CST;
	private String RIL_II_PRT_NMBR;
	private ArrayList RIL_PRC_LVLS;
	private String RIL_II_CTGRY_NM;
	private String RIL_II_EXPRY_DT;
	
	private String RIL_TG_PRPRTY_CD;
	private String RIL_TG_SRL_NMBR;
	private String RIL_TG_SPECS;
	private String RIL_TG_CSTDN;
	private String RIL_TG_CTGRY;
	
	private String RIL_DL_ITM_CTGRY;
	private String RIL_DL_ITM_NM;
	private String RIL_DL_PRPRTY_CD;
	private String RIL_DL_SRL_NMBR;
	private String RIL_DL_SPCS;
	private String RIL_DL_CSTDN;
	private String RIL_DL_DT;
	private double RIL_DL_ACQSTN_CST;
	private double RIL_DL_AMNT;
	private double RIL_DL_MNTH_LF_SPN;
	private double RIL_DL_CURR_BLNC;
	
	public InvRepInventoryListDetails() {
    }
	
	
	
	public String getRilDlItemCategory() {
		
		return RIL_DL_ITM_CTGRY;
	
	}
	
	public void setRilDlItemCategory(String RIL_DL_ITM_CTGRY) {
	
		this.RIL_DL_ITM_CTGRY = RIL_DL_ITM_CTGRY;
	
	}
	
	public String getRilDlItemName() {
		
		return RIL_DL_ITM_NM;
	
	}
	
	public void setRilDlItemName(String RIL_DL_ITM_NM) {
	
		this.RIL_DL_ITM_NM = RIL_DL_ITM_NM;
	
	}
	
	
	public String getRilDlPropertyCode() {
		
		return RIL_DL_PRPRTY_CD;
	
	}
	
	public void setRilDlPropertyCode(String RIL_DL_PRPRTY_CD) {
	
		this.RIL_DL_PRPRTY_CD = RIL_DL_PRPRTY_CD;
	
	}
	
	public String getRilDlSerialNumber() {
		
		return RIL_DL_SRL_NMBR;
	
	}
	
	public void setRilDlSerialNumber(String RIL_DL_SRL_NMBR) {
	
		this.RIL_DL_SRL_NMBR = RIL_DL_SRL_NMBR;
	
	}
	
	public String getRilDlSpecs() {
		
		return RIL_DL_SPCS;
	
	}
	
	public void setRilDlSpecs(String RIL_DL_SPCS) {
	
		this.RIL_DL_SPCS = RIL_DL_SPCS;
	
	}
	
	
	public String getRilDlCustodian() {
		
		return RIL_DL_CSTDN;
	
	}
	
	public void setRilDlCustodian(String RIL_DL_CSTDN) {
	
		this.RIL_DL_CSTDN = RIL_DL_CSTDN;
	
	}
	
	
	public String getRilDlDate() {

		return RIL_DL_DT;

	}

	public void setRilDlDate(String RIL_DL_DT) {

		this.RIL_DL_DT = RIL_DL_DT;

	}

	public double getRilDlAcquisitionCost() {
		
		return RIL_DL_ACQSTN_CST;
	
	}
	
	public void setRilDlAcquisitionCost(double RIL_DL_ACQSTN_CST) {
	
		this.RIL_DL_ACQSTN_CST = RIL_DL_ACQSTN_CST;
	
	}
	
	public double getRilDlAmount() {
		
		return RIL_DL_AMNT;
	
	}
	
	public void setRilDlAmount(double RIL_DL_AMNT) {
	
		this.RIL_DL_AMNT = RIL_DL_AMNT;
	
	}
	
	
	public double getRilDlMonthLifeSpan() {
		
		return RIL_DL_MNTH_LF_SPN;
	
	}
	
	public void setRilDlMonthLifeSpan(double RIL_DL_MNTH_LF_SPN) {
	
		this.RIL_DL_MNTH_LF_SPN = RIL_DL_MNTH_LF_SPN;
	
	}
	
	public double getRilDlCurrentBalance() {
		
		return RIL_DL_CURR_BLNC;
	
	}
	
	public void setRilDlCurrentBalance(double RIL_DL_CURR_BLNC) {
	
		this.RIL_DL_CURR_BLNC = RIL_DL_CURR_BLNC;
	
	}
	
	

	
	
	public String getRilPropertyCode() {
		
		return RIL_TG_PRPRTY_CD;
	
	}
	
	public void setRilPropertyCode(String RIL_TG_PRPRTY_CD) {
	
		this.RIL_TG_PRPRTY_CD = RIL_TG_PRPRTY_CD;
	
	}
	
	public String getRilSerialNumber() {
		
		return RIL_TG_SRL_NMBR;
	
	}
	
	public void setRilSerialNumber(String RIL_TG_SRL_NMBR) {
	
		this.RIL_TG_SRL_NMBR = RIL_TG_SRL_NMBR;
	
	}
	
	public String getRilSpecs() {
		
		return RIL_TG_SPECS;
	
	}
	
	public void setRilSpecs(String RIL_TG_SPECS) {
	
		this.RIL_TG_SPECS = RIL_TG_SPECS;
	
	}
	
	public String getRilCustodian() {
		
		return RIL_TG_CSTDN;
	
	}
	
	public void setRilCustodian(String RIL_TG_CSTDN) {
	
		this.RIL_TG_CSTDN = RIL_TG_CSTDN;
	
	}
	
	
	
	public String getRilItemName() {
		
		return RIL_II_NM;
	
	}
	
	public void setRilItemName(String RIL_II_NM) {
	
		this.RIL_II_NM = RIL_II_NM;
	
	}
	
	public String getRilItemDescription() {
		
		return RIL_II_DESC;
	
	}
	
	public void setRilItemDescription(String RIL_II_DESC) {
	
		this.RIL_II_DESC = RIL_II_DESC;
	
	}
	
	public String getRilItemClass() {
		
		return RIL_II_CLSS;
		
	}
	
	public void setRilItemClass(String RIL_II_CLSS) {
		
		this.RIL_II_CLSS = RIL_II_CLSS;
		
	}

	public String getRilLocation() {
		
		return RIL_LOC_NM;
	
	}
	
	public void setRilLocation(String RIL_LOC_NM) {
	
		this.RIL_LOC_NM = RIL_LOC_NM;
	
	}
	
	public double getRilQuantity() {
	
		return RIL_QTY;
	
	}
	
	public void setRilQuantity(double RIL_QTY) {
	
		this.RIL_QTY = RIL_QTY;
	
	}
		
	public String getRilUnit() {
		
		return RIL_UNT;
	
	}
	
	public void setRilUnit(String RIL_UNT) {
	
		this.RIL_UNT = RIL_UNT;
	
	}
	
	public double getRilUnitCost() {
		
		return RIL_UNT_CST;
	
	}
	
	public void setRilUnitCost(double RIL_UNT_CST) {
	
		this.RIL_UNT_CST = RIL_UNT_CST;
	
	}
	
	public double getRilAmount() {
		
		return RIL_AMNT;
	
	}
	
	public void setRilAmount(double RIL_AMNT) {
	
		this.RIL_AMNT = RIL_AMNT;
	
	}
	
	public double getRilSalesPrice() {
		
		return RIL_SLS_PRC;
		
	}
	
	public void setRilSalesPrice(double RIL_SLS_PRC) {
		
		this.RIL_SLS_PRC = RIL_SLS_PRC;
		
	}
	
	public double getRilAverageCost() {
		
		return RIL_AVE_CST;
		
	}
	
	public void setRilAverageCost(double RIL_AVE_CST) {
		
		this.RIL_AVE_CST = RIL_AVE_CST;
		
	}
	
	public String getRilIiPartNumber() {
		
		return RIL_II_PRT_NMBR;
		
	}
	
	public void setRilIiPartNumber(String RIL_II_PRT_NMBR) {
		
		this.RIL_II_PRT_NMBR = RIL_II_PRT_NMBR;
		
	}
	
	public ArrayList getRilPriceLevels(){
		
		return RIL_PRC_LVLS;
		
	}
	
	public void setRilPriceLevels(ArrayList RIL_PRC_LVLS){
	
		this.RIL_PRC_LVLS = RIL_PRC_LVLS;
		
	}
	
	public String getRilItemCategory() {
		
		return RIL_II_CTGRY_NM;
	
	}
	
	public void setRilItemCategory(String RIL_II_CTGRY_NM) {

		this.RIL_II_CTGRY_NM = RIL_II_CTGRY_NM;

	}

	public String getRilExpiryDate() {

		return RIL_II_EXPRY_DT;

	}

	public void setRilExpiryDate(String RIL_II_EXPRY_DT) {

		this.RIL_II_EXPRY_DT = RIL_II_EXPRY_DT;

	}
	
} // InvRepInventoryListDetaRILs class