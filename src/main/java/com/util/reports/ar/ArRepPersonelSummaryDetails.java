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

public class ArRepPersonelSummaryDetails implements Cloneable, java.io.Serializable {
	
	private Integer JO_CODE;
	private Date JO_DT;
	private String JO_TYP;
	private String JO_DCMNT_NMBR;
	private String JO_CST_CSTMR_CODE;


	private String JO_CST_CSTMR_NM;
	private String JO_CST_CSTMR_CLSS;
	private String JO_CST_CSTMR_TYP;
	private double JO_QTY;
	private double JO_AMNT;
	private Integer JO_AD_BRNCH;

	private Integer JOL_CODE;
	private double JOL_QTY;
	private double JOL_UNT_CST;
	private double JOL_AMNT;
	
	
	private Integer JA_CODE;
	private String JA_PRSNL_CODE;
	private String JA_PRSNL_NM;
	private double JA_QTY;
	private double JA_UNT_CST;
	private double JA_AMNT;
	
	private String JO_PYMNT_STTS;
		

	private final ArrayList arJobOrderInvoiceLines = new ArrayList();
	
	public ArRepPersonelSummaryDetails() {
    }
	
	
	
	public Object clone() throws CloneNotSupportedException {
	  	 

 		try {
 		   return super.clone();
 		 }
 		  catch (CloneNotSupportedException e) {
 			  throw e;
 		
 		  }	
 	 }

	
	public String getJoPaymentStatus() {
		
		return JO_PYMNT_STTS;
		
	}
	
	public void setJoPaymentStatus(String JO_PYMNT_STTS) {
		
		this.JO_PYMNT_STTS = JO_PYMNT_STTS;
		
	}
	
	
	public Integer getJoCode() {
		return JO_CODE;
	}



	public Date getJoDate() {
		return JO_DT;
	}



	public String getJoType() {
		return JO_TYP;
	}



	public String getJoDocumentNumber() {
		return JO_DCMNT_NMBR;
	}



	public String getJoCstCustomerCode() {
		return JO_CST_CSTMR_CODE;
	}



	public String getJoCstCustomerName() {
		return JO_CST_CSTMR_NM;
	}



	public String getJoCstCustomerClass() {
		return JO_CST_CSTMR_CLSS;
	}



	public String getJoCstCustomerType() {
		return JO_CST_CSTMR_TYP;
	}



	public double getJoAmount() {
		return JO_AMNT;
	}



	public Integer getJolCode() {
		return JOL_CODE;
	}



	public double getJolQuantity() {
		return JOL_QTY;
	}



	public double getJolUnitCost() {
		return JOL_UNT_CST;
	}



	public double getJolAmount() {
		return JOL_AMNT;
	}



	public Integer getJaCode() {
		return JA_CODE;
	}



	public String getJaPersonelCode() {
		return JA_PRSNL_CODE;
	}



	public String getJaPersonelName() {
		return JA_PRSNL_NM;
	}



	public double getJaQuantity() {
		return JA_QTY;
	}



	public double getJaUnitCost() {
		return JA_UNT_CST;
	}



	public double getJaAmount() {
		return JA_AMNT;
	}



	public void setJoCode(Integer jO_CODE) {
		JO_CODE = jO_CODE;
	}



	public void setJoDate(Date jO_DT) {
		JO_DT = jO_DT;
	}



	public void setJoType(String jO_TYP) {
		JO_TYP = jO_TYP;
	}



	public void setJoDocumentNumber(String jO_DCMNT_NMBR) {
		JO_DCMNT_NMBR = jO_DCMNT_NMBR;
	}



	public void setJoCstCustomerCode(String jO_CST_CSTMR_CODE) {
		JO_CST_CSTMR_CODE = jO_CST_CSTMR_CODE;
	}



	public void setJoCstCustomerName(String jO_CST_CSTMR_NM) {
		JO_CST_CSTMR_NM = jO_CST_CSTMR_NM;
	}



	public void setJoCstCustomerClass(String jO_CST_CSTMR_CLSS) {
		JO_CST_CSTMR_CLSS = jO_CST_CSTMR_CLSS;
	}



	public void setJoCstCustomerType(String jO_CST_CSTMR_TYP) {
		JO_CST_CSTMR_TYP = jO_CST_CSTMR_TYP;
	}



	public void setJoAmount(double jO_AMNT) {
		JO_AMNT = jO_AMNT;
	}



	public void setJolCode(Integer jOL_CODE) {
		JOL_CODE = jOL_CODE;
	}



	public void setJolQuantity(double jOL_QTY) {
		JOL_QTY = jOL_QTY;
	}



	public void setJolUnitCost(double jOL_UNT_CST) {
		JOL_UNT_CST = jOL_UNT_CST;
	}



	public void setJolAmount(double jOL_AMNT) {
		JOL_AMNT = jOL_AMNT;
	}



	public void setJaCode(Integer jA_CODE) {
		JA_CODE = jA_CODE;
	}



	public void setJaPersonelCode(String jA_PRSNL_CODE) {
		JA_PRSNL_CODE = jA_PRSNL_CODE;
	}



	public void setJaPersonelName(String jA_PRSNL_NM) {
		JA_PRSNL_NM = jA_PRSNL_NM;
	}



	public void setJaQuantity(double jA_QTY) {
		JA_QTY = jA_QTY;
	}



	public void setJaUnitCost(double jA_UNT_CST) {
		JA_UNT_CST = jA_UNT_CST;
	}



	public void setJaAmount(double jA_AMNT) {
		JA_AMNT = jA_AMNT;
	}


	public Integer getJoAdBranch(Integer JO_AD_BRNCH) {
		return JO_AD_BRNCH;
	}
	
	public void setJoAdBranch(Integer JO_AD_BRNCH) {
		this.JO_AD_BRNCH = JO_AD_BRNCH;
	}

	public ArrayList getArJobOrderInvoiceLines() {
		
		return arJobOrderInvoiceLines;
		
	}
	
	public void saveArJobOrderInvoiceLines(Object newArJobOrderInvoiceLine) {
		
		arJobOrderInvoiceLines.add(newArJobOrderInvoiceLine);
		
	}
	
	
	public Double getJoQuantity() {
		return JO_QTY;
	}
	
	public void setJoQuantity(Double JO_QTY) {
		this.JO_QTY = JO_QTY;
	}
	
	
	
	public static Comparator sortByCustomerCode = (r1, r2) -> {

        ArRepPersonelSummaryDetails customer1 = (ArRepPersonelSummaryDetails) r1;
        ArRepPersonelSummaryDetails customer2 = (ArRepPersonelSummaryDetails) r2;
        return customer1.getJoCstCustomerCode().compareTo(customer2.getJoCstCustomerCode());

    };
	
	public static Comparator sortByPersonelCode = (r1, r2) -> {

        ArRepPersonelSummaryDetails customer1 = (ArRepPersonelSummaryDetails) r1;
        ArRepPersonelSummaryDetails customer2 = (ArRepPersonelSummaryDetails) r2;
        return customer1.getJaPersonelCode().compareTo(customer2.getJaPersonelCode());

    };

	public static Comparator sortByCustomerType = (r1, r2) -> {

        ArRepPersonelSummaryDetails customer1 = (ArRepPersonelSummaryDetails) r1;
        ArRepPersonelSummaryDetails customer2 = (ArRepPersonelSummaryDetails) r2;
        return customer1.getJoCstCustomerType().compareTo(customer2.getJoCstCustomerType());

    };
	
} // ArRepSalespersonDetails class