/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Date;

public class InvRepCostOfSaleDetails implements java.io.Serializable {

	private String CS_ITM_NM; 
	private String CS_ITM_DESC;
	private Date CS_DT;
	private String CS_DCMNT_NMBR;
	private double CS_QTY_SLD;
	private double CS_UNT_CST;
	private double CS_CST_OF_SLS;
	private String CS_UNT;
	private String CS_CSTMR_CODE;
	private String CS_BRNCH_CODE;
	private String CS_BRNCH_NM;
	private double CS_SOH;
	private String CS_SLSPRSN_NM;
	private double CS_SLS_AMNT;
	private double CS_GRSS_PRFT;
	private double CS_PRCNT_PRFT;

	
	public InvRepCostOfSaleDetails() {
    }
	
	public double getCsCostOfSales() {
		
		return CS_CST_OF_SLS;
	
	}
	
	public void setCsCostOfSales(double CS_CST_OF_SLS) {
	
		this.CS_CST_OF_SLS = CS_CST_OF_SLS;
	
	}
	
	public String getCsDocumentNumber() {
	
		return CS_DCMNT_NMBR;
	
	}
	
	public void setCsDocumentNumber(String CS_DCMNT_NMBR) {
	
		this.CS_DCMNT_NMBR = CS_DCMNT_NMBR;
	
	}
	
	public String getCsUnit() {
		
		return CS_UNT;
	
	}
	
	public void setCsUnit(String CS_UNT) {
	
		this.CS_UNT = CS_UNT;
	
	}
	
	public String getCsCustomerCode() {
		
		return CS_CSTMR_CODE;
	
	}
	
	public void setCsCustomerCode(String CS_CSTMR_CODE) {
	
		this.CS_CSTMR_CODE = CS_CSTMR_CODE;
	
	}
	
	public String getCsBranchCode() {
		
		return CS_BRNCH_CODE;
	
	}
	
	public void setCsBranchCode(String CS_BRNCH_CODE) {
	
		this.CS_BRNCH_CODE = CS_BRNCH_CODE;
	
	}

	
	public Date getCsDate() {
	
		return CS_DT;
	
	}
	
	public void setCsDate(Date CS_DT) {
	
		this.CS_DT = CS_DT;
	
	}

	public String getCsItemDescription() {
	
		return CS_ITM_DESC;
	
	}
	
	public void setCsItemDescription(String CS_ITM_DESC) {
	
		this.CS_ITM_DESC = CS_ITM_DESC;
	
	}
	
	public String getCsItemName() {
	
		return CS_ITM_NM;
	
	}
	
	public void setCsItemName(String CS_ITM_NM) {
	
		this.CS_ITM_NM = CS_ITM_NM;
	
	}
	
	public double getCsQuantitySold() {
	
		return CS_QTY_SLD;
	
	}
	
	public void setCsQuantitySold(double CS_QTY_SLD) {
	
		this.CS_QTY_SLD = CS_QTY_SLD;
	
	}
	
	public double getCsUnitCost() {
		
		return CS_UNT_CST;
	
	}
	
	public void setCsUnitCost(double CS_UNT_CST) {
	
		this.CS_UNT_CST = CS_UNT_CST;
	
	}
	
	
	public double getCsStockOnHand() {
		
		return CS_SOH;
		
	}
	
	public void setCsStockOnHand(double CS_SOH) {
		
		this.CS_SOH = CS_SOH;
		
	}
	
	public String getCsSalespersonName() {
		
		return CS_SLSPRSN_NM;
		
	}
	
	public void setCsSalespersonName(String CS_SLSPRSN_NM) {
		
		this.CS_SLSPRSN_NM = CS_SLSPRSN_NM;
		
	}
	
	public double getCsSalesAmount() {
		
		return CS_SLS_AMNT;
		
	}
	
	public void setCsSalesAmount(double CS_SLS_AMNT) {
		
		this.CS_SLS_AMNT = CS_SLS_AMNT;
		
	}

	public double getCsGrossProfit() {
		
		return CS_GRSS_PRFT;
		
	}
	
	public void setCsGrossProfit(double CS_GRSS_PRFT) {
		
		this.CS_GRSS_PRFT = CS_GRSS_PRFT;
		
	}

	public double getCsPercentProfit() {
		
		return CS_PRCNT_PRFT;
		
	}

	public void setCsPercentProfit(double CS_PRCNT_PRFT) {
		
		this.CS_PRCNT_PRFT = CS_PRCNT_PRFT;
		
	}


} // InvRepCostOfSaleDetails class