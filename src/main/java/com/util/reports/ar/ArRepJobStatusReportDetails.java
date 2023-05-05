/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Date;

public class ArRepJobStatusReportDetails implements Cloneable, java.io.Serializable {
	
	private Date JO_DT;
	private String JO_DCMNT_NMBR;
	private String JO_RFRNC_NMBR;
	private String JO_DESC;
	private String JO_TYP;
	private String JO_CST_CSTMR_CODE;
	private String JO_CST_CSTMR_TYP;
	private String JO_CST_CSTMR_CLSS;

	private double JO_ORDR_QTY;
	private double JO_INVC_QTY;
	private double JO_AMNT;
	private double JO_TAX_AMNT;	
	private String ORDER_BY;
	private String JO_SLS_SLSPRSN_CODE;
	private String JO_SLS_NM;
	private String JO_CST_CSTMR_CODE2;   
	private String JO_CST_NM;
	private String JO_JB_ORDR_STTS;


	private String JO_ORDR_STTS;
	private String JO_APPRVL_STTS;
	private String JO_APPRVDRJCTD_BY;
	private String JO_INV_NMBRS;
	
	private String JOL_II_NM;
	private String JOL_DESC;
	private double JOL_QTTY;
	private double JOL_UNT_PRC;
	private double JOL_AMNT;
	private String JOL_UNT;

	
	private String JA_PRSNL_CD;
	private String JA_PRSNL_NM;
	private double JA_UNT_CST;
	private double JA_QTTY;
	private double JA_AMNT;
	private String JA_RMRKS;
	
	
	

	
	
	  public ArRepJobStatusReportDetails() {
      }
	   
	   public Object clone() throws CloneNotSupportedException {
	  	 

	 		try {
	 		   return super.clone();
	 		 }
	 		  catch (CloneNotSupportedException e) {
	 			  throw e;
	 		
	 		  }	
	 	 }
	
	public String getJolDescription() {
		return JOL_DESC;
	}
	
	public void setJolDescription(String JOL_DESC) {
		this.JOL_DESC = JOL_DESC;
	}
	
	public double getJolQuantity() {
		return JOL_QTTY;
	}
	
	public void setJolQuantity(double JOL_QTTY) {
		this.JOL_QTTY = JOL_QTTY;
	}
	
	public double getJolUnitPrice() {
		return JOL_UNT_PRC;
	}
	
	public void setJolUnitPrice(double JOL_UNT_PRC) {
		this.JOL_UNT_PRC = JOL_UNT_PRC;
	}
	
	public double getJolAmount() {
		return JOL_AMNT;
	}
	
	public void setJolAmount(double JOL_AMNT) {
		this.JOL_AMNT = JOL_AMNT;
	}
	
	public String getJolUnit() {
		return JOL_UNT;
	}
	
	public void setJolUnit(String JOL_UNT) {
		this.JOL_UNT = JOL_UNT;
	}
	

	public String getJolIIName() {
		
		return JOL_II_NM;
		
	}
	
	public void setJolIIName(String JOL_II_NM) {
		
		this.JOL_II_NM = JOL_II_NM;
		
	}
	
	
	public String getJaPersonelCode() {
		return JA_PRSNL_CD;
	}
	
	public void setJaPersonelCode(String JA_PRSNL_CD) {
		this.JA_PRSNL_CD = JA_PRSNL_CD;
	}
	
	
	public String getJaPersonelName() {
		return JA_PRSNL_NM;
	}
	
	public void setJaPersonelName(String JA_PRSNL_NM) {
		this.JA_PRSNL_NM = JA_PRSNL_NM;
	}
	
	public double getJaUnitCost() {
		return JA_UNT_CST;
	}
	
	public void setJaUnitCost(double JA_UNT_CST) {
		this.JA_UNT_CST = JA_UNT_CST;
	}
	
	
	public double getJaQuantity() {
		return JA_QTTY;
	}
	
	public void setJaQuantity(double JA_QTTY) {
		this.JA_QTTY = JA_QTTY;
	}
	
	public double getJaAmount() {
		return JA_AMNT;
	}
	
	public void setJaAmount(double JA_AMNT) {
		this.JA_AMNT = JA_AMNT;
	}
	
	
	public String getJaRemarks() {
		return JA_RMRKS;
	}
	
	public void setJaRemarks(String JA_RMRKS) {
		this.JA_RMRKS = JA_RMRKS;
	}
	
	public Date getJoDate() {
		
		return JO_DT;
		
	}
	
	public void setJoDate(Date JO_DT) {
		
		this.JO_DT = JO_DT;
		
	}
	
	public String getJoDocumentNumber() {
		
		return JO_DCMNT_NMBR;
		
	}
	
	public void setJoDocumentNumber(String JO_DCMNT_NMBR) {
		
		this.JO_DCMNT_NMBR = JO_DCMNT_NMBR;
		
	}
	
	public String getJoReferenceNumber() {
		
		return JO_RFRNC_NMBR;
		
	}
	
	public void setJoReferenceNumber(String JO_RFRNC_NMBR) {
		
		this.JO_RFRNC_NMBR = JO_RFRNC_NMBR;
		
	}
	
	public String getJoDescription() {
		
		return JO_DESC;
		
	}
	
	public void setJoDescription(String JO_DESC) {
		
		this.JO_DESC = JO_DESC;
		
	}
	
	public String getJoType() {
		
		return JO_TYP;
		
	}
	
	public void setJoType(String JO_TYP) {
		
		this.JO_TYP = JO_TYP;
		
	}
	
	public String getJoCstCustomerCode() {
		
		return JO_CST_CSTMR_CODE;
		
	}
	
	public void setJoCstCustomerCode(String JO_CST_CSTMR_CODE) {
		
		this.JO_CST_CSTMR_CODE = JO_CST_CSTMR_CODE;
		
	}
	
	public String getJoCstCustomerType() {
		
		return JO_CST_CSTMR_TYP;
		
	}
	
	public void setJoCstCustomerType(String JO_CST_CSTMR_TYP) {
		
		this.JO_CST_CSTMR_TYP = JO_CST_CSTMR_TYP;
		
	}
	
	public String getJoCstCustomerClass() {
		
		return JO_CST_CSTMR_CLSS;
		
	}
	
	public void setJoCstCustomerClass(String JO_CST_CSTMR_CLSS) {
		
		this.JO_CST_CSTMR_CLSS = JO_CST_CSTMR_CLSS;
		
	}
	

	
	
	public double getJoOrderQty() {
		
		return JO_ORDR_QTY;
		
	}
	
	public void setJoOrderQty(double JO_ORDR_QTY) {
		
		this.JO_ORDR_QTY = JO_ORDR_QTY;
		
	}
	

	
	public double getJoInvoiceQty() {
		
		return JO_INVC_QTY;
		
	}
	
	public void setJoInvoiceQty(double JO_INVC_QTY) {
		
		this.JO_INVC_QTY = JO_INVC_QTY;
		
	}

	
	public double getJoAmount() {
		
		return JO_AMNT;
		
	}
	
	public void setJoAmount(double JO_AMNT) {
		
		this.JO_AMNT = JO_AMNT;
		
	}
	
	public double getJoTaxAmount() {
		
		return JO_TAX_AMNT;
		
	}
	
	public void setJoTaxAmount(double JO_TAX_AMNT) {
		
		this.JO_TAX_AMNT = JO_TAX_AMNT;
		
	}
	
	public String getOrderBy() {
		
		return ORDER_BY;
		
	}
	
	public void setOrderBy(String ORDER_BY) {
		
		this.ORDER_BY = ORDER_BY;
		
	}
	
	public String getJoCstCustomerCode2() {
		
		return JO_CST_CSTMR_CODE2;
	
	}
	
	public void setJoCstCustomerCode2(String JO_CST_CSTMR_CODE2) {
	
		this.JO_CST_CSTMR_CODE2 = JO_CST_CSTMR_CODE2;
	
	}
	
	public String getJoSlsSalespersonCode() {
		
		return JO_SLS_SLSPRSN_CODE;
		
	}
	
	public void setJoSlsSalespersonCode(String JO_SLS_SLSPRSN_CODE) {
		
		this.JO_SLS_SLSPRSN_CODE = JO_SLS_SLSPRSN_CODE;
		
	}
	
	public String getJoSlsName() {
		
		return JO_SLS_NM;
		
	}
	
	public void setJoSlsName(String JO_SLS_NM) {
		
		this.JO_SLS_NM = JO_SLS_NM;
		
	}
	
	public String getJoCstName() {
		
		return JO_CST_NM;
		
	}
	
	public void setJoCstName(String JO_CST_NM) {
		
		this.JO_CST_NM = JO_CST_NM;
		
	}

	
	public String getJoJobOrderStatus() {
		
		return JO_JB_ORDR_STTS;
		
	}
	
	public void setJoJobOrderStatus(String JO_JB_ORDR_STTS) {
		
		this.JO_JB_ORDR_STTS = JO_JB_ORDR_STTS;
		
	}

	
	
	public String getJoOrderStatus() {
		
		return JO_ORDR_STTS;
		
	}
	
	public void setJoOrderStatus(String JO_ORDR_STTS) {
		
		this.JO_ORDR_STTS = JO_ORDR_STTS;
		
	}
	
	public String getJoApprovalStatus() {
		
		return JO_APPRVL_STTS;
		
	}
	
	public void setJoApprovalStatus(String JO_APPRVL_STTS) {
		
		this.JO_APPRVL_STTS = JO_APPRVL_STTS;
		
	}
	
	public String getJoApprovedRejectedBy() {
		
		return JO_APPRVDRJCTD_BY;
		
	}
	
	public void setJoApprovedRejectedBy(String JO_APPRVDRJCTD_BY) {
		
		this.JO_APPRVDRJCTD_BY = JO_APPRVDRJCTD_BY;
		
	}	
	
	public String getJoInvoiceNumbers() {
		
		return JO_INV_NMBRS;
		
	}
	
	public void setJoInvoiceNumbers(String JO_INV_NMBRS) {
		
		this.JO_INV_NMBRS = JO_INV_NMBRS;
		
	}
	
	
	
} // ArRepJobStatusReportDetails class