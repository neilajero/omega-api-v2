/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class ArRepSalespersonDetails implements java.io.Serializable {
	
	private Date SLP_DT;
	private String SLP_INVC_NMBR;
	private String SLP_CST_CSTMR_CODE;
	private String SLP_CST_CSTMR_NM;
	private String SLP_CST_CSTMR_CLSS;
	private String SLP_CST_CSTMR_TYP;
	private double SLP_AMNT;
	private double SLP_CMMSSN_AMNT = 0;
	private double SLP_NON_CMMSSN_AMNT = 0;
	private double SLP_GRND_TTL_SLS = 0;
	private byte SLP_SBJCT_TO_CMMSSN;
	private String SLP_SLSPRSN_CODE;
	private String SLP_TYP;
	private Integer SLP_PRMRY_KEY;
	private Integer SLP_AD_BRNCH;
	private String SLP_SLSPRSN_NM;
	private String SLP_PYMNT_STTS;
	private Date SLP_CLLCTN_DT;
	private Boolean SLP_RCT_CSTMR_DPST;
	private double SLP_INVC_AMNT_PD;	
		
	private final ArrayList slpArInvoiceLines = new ArrayList();
	private final ArrayList slpArInvoiceLineItems = new ArrayList();
	private final ArrayList slpArSalesOrderInvoiceLines = new ArrayList();
	
	public ArRepSalespersonDetails() {
    }
	
	public Date getSlpDate() {
		
		return SLP_DT;
		
	}
	
	public void setSlpDate(Date SLP_DT) {
		
		this.SLP_DT = SLP_DT;
		
	}
	
	public String getSlpInvoiceNumber() {
		
		return SLP_INVC_NMBR;
		
	}
	
	public void setSlpInvoiceNumber(String SLP_INVC_NMBR) {
		
		this.SLP_INVC_NMBR = SLP_INVC_NMBR;
		
	}
	
	public String getSlpCstCustomerCode() {
		
		return SLP_CST_CSTMR_CODE;
		
	}
	
	public void setSlpCstCustomerCode(String SLP_CST_CSTMR_CODE) {
		
		this.SLP_CST_CSTMR_CODE = SLP_CST_CSTMR_CODE;
		
	}
	
	public String getSlpCstCustomerClass() {
		
		return SLP_CST_CSTMR_CLSS;
		
	}
	
	public void setSlpCstCustomerClass(String SLP_CST_CSTMR_CLSS) {
		
		this.SLP_CST_CSTMR_CLSS = SLP_CST_CSTMR_CLSS;
		
	}
	
	public double getSlpAmount() {
		
		return SLP_AMNT;
		
	}
	
	public void setSlpAmount(double SLP_AMNT) {
		
		this.SLP_AMNT = SLP_AMNT;
		
	}
	
	public double getSlpCommissionAmount() {
		
		return this.SLP_CMMSSN_AMNT;
		
	}
	
	public void setSlpCommissionAmount(double SLP_CMMSSN_AMNT) {
		
		this.SLP_CMMSSN_AMNT = SLP_CMMSSN_AMNT;
		
	}
	
	public double getSlpNonCommissionAmount() {
		
		return this.SLP_NON_CMMSSN_AMNT;
		
	}
	
	public void setSlpNonCommissionAmount(double SLP_NON_CMMSSN_AMNT) {
		
		this.SLP_NON_CMMSSN_AMNT = SLP_NON_CMMSSN_AMNT;
		
	}
	
	public double getSlpGrandTotalSales() {
		
		return this.SLP_GRND_TTL_SLS;
		
	}
	
	public void setSlpGrandTotalSales(double SLP_GRND_TTL_SLS) {
		
		this.SLP_GRND_TTL_SLS = SLP_GRND_TTL_SLS;
		
	}
	
	public byte getSlpSubjectToCommission() {
		
		return SLP_SBJCT_TO_CMMSSN;
		
	}
	
	public void setSlpSubjectToCommission(byte SLP_SBJCT_TO_CMMSSN) {
		
		this.SLP_SBJCT_TO_CMMSSN = SLP_SBJCT_TO_CMMSSN;
		
	}

	public String getSlpSalespersonCode() {
		
		return SLP_SLSPRSN_CODE;
		
	}
	
	public void setSlpSalespersonCode(String SLP_SLSPRSN_CODE) {
		
		this.SLP_SLSPRSN_CODE = SLP_SLSPRSN_CODE;
		
	}
	
	public String getSlpType() {
		
		return SLP_TYP;
		
	}
	
	public void setSlpType(String SLP_TYP) {
		
		this.SLP_TYP = SLP_TYP;
		
	}
	
	public Integer getSlpPrimaryKey() {
		
		return SLP_PRMRY_KEY;
		
	}
	
	public void setSlpPrimaryKey(Integer SLP_PRMRY_KEY) {
		
		this.SLP_PRMRY_KEY = SLP_PRMRY_KEY;
		
	}
	
	public ArrayList getSlpArInvoiceLines() {
		
		return slpArInvoiceLines;
		
	}
	
	public void saveSlpArInvoiceLines(Object newSlpArInvoiceLine) {
		
		slpArInvoiceLines.add(newSlpArInvoiceLine);
		
	}
	
	public ArrayList getSlpArInvoiceLineItems() {
		
		return slpArInvoiceLineItems;
		
	}
	
	public void saveSlpArInvoiceLineItems(Object newSlpArInvoiceLineItem) {
		
		slpArInvoiceLineItems.add(newSlpArInvoiceLineItem);
		
	}
	
	public ArrayList getSlpArSalesOrderInvoiceLines() {
		
		return slpArSalesOrderInvoiceLines;
		
	}
	
	public void saveSlpArSalesOrderInvoiceLines(Object newSlpArSalesOrderInvoiceLines) {
	
		slpArSalesOrderInvoiceLines.add(newSlpArSalesOrderInvoiceLines);

	}
	
	public Integer getSlpAdBranch() {
		
		return SLP_AD_BRNCH;
		
	}
	
	public void setSlpAdBranch(Integer SLP_AD_BRNCH) {
		
		this.SLP_AD_BRNCH = SLP_AD_BRNCH;
		
	}
	
	public String getSlpSalespersonName() {

		return SLP_SLSPRSN_NM;

	}

	public void setSlpSalespersonName(String SLP_SLSPRSN_NM) {

		this.SLP_SLSPRSN_NM = SLP_SLSPRSN_NM;

	}
	
	public String getSlpPaymentStatus() {
		
		return SLP_PYMNT_STTS;
		
	}
	
	public void setSlpPaymentStatus(String SLP_PYMNT_STTS) {
		
		this.SLP_PYMNT_STTS = SLP_PYMNT_STTS;
		
	}
	
	public String getSlpCstCustomerType() {
		
		return SLP_CST_CSTMR_TYP;
		
	}
	
	public void setSlpCstCustomerType(String SLP_CST_CSTMR_TYP) {
		
		this.SLP_CST_CSTMR_TYP = SLP_CST_CSTMR_TYP;
		
	}
	
	public String getSlpCstCustomerName() {

		return SLP_CST_CSTMR_NM;

	}

	public void setSlpCstCustomerName(String SLP_CST_CSTMR_NM) {

		this.SLP_CST_CSTMR_NM = SLP_CST_CSTMR_NM;

	}
	
	public Date getSlpCollectionDate() {
		
		return SLP_CLLCTN_DT;
		
	}
	
	public void setSlpCollectionDate(Date SLP_CLLCTN_DT) {
		
		this.SLP_CLLCTN_DT = SLP_CLLCTN_DT;
		
	}
	
	public Boolean getSlpRctCustomerDeposit() {
		
		return SLP_RCT_CSTMR_DPST;
		
	}
	
	public void setSlpRctCustomerDeposit(Boolean SLP_RCT_CSTMR_DPST) {
		
		this.SLP_RCT_CSTMR_DPST = SLP_RCT_CSTMR_DPST;
		
	}
	
	public double getSlpInvcAmountPd() {
		
		return SLP_INVC_AMNT_PD;
		
	}
	
	public void setSlpInvcAmountPd(double SLP_INVC_AMNT_PD) {
		
		this.SLP_INVC_AMNT_PD = SLP_INVC_AMNT_PD;
		
	}
	
	public static Comparator sortByCustomerCode = (r1, r2) -> {

        ArRepSalespersonDetails customer1 = (ArRepSalespersonDetails) r1;
        ArRepSalespersonDetails customer2 = (ArRepSalespersonDetails) r2;
        return customer1.getSlpCstCustomerCode().compareTo(customer2.getSlpCstCustomerCode());

    };
	
	public static Comparator sortBySalespersonCode = (r1, r2) -> {

        ArRepSalespersonDetails customer1 = (ArRepSalespersonDetails) r1;
        ArRepSalespersonDetails customer2 = (ArRepSalespersonDetails) r2;
        return customer1.getSlpSalespersonName().compareTo(customer2.getSlpSalespersonName());

    };

	public static Comparator sortByCustomerType = (r1, r2) -> {

        ArRepSalespersonDetails customer1 = (ArRepSalespersonDetails) r1;
        ArRepSalespersonDetails customer2 = (ArRepSalespersonDetails) r2;
        return customer1.getSlpCstCustomerType().compareTo(customer2.getSlpCstCustomerType());

    };
	
} // ArRepSalespersonDetails class