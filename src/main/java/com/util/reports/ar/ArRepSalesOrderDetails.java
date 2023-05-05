/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Comparator;
import java.util.Date;

public class ArRepSalesOrderDetails implements java.io.Serializable {
	
	private Date SO_DT;
	private String SO_DCMNT_NMBR;
	private String SO_RFRNC_NMBR;
	private String SO_DESC;
	private String SO_CST_CSTMR_CODE;
	private String SO_CST_CSTMR_TYP;
	private String SO_CST_CSTMR_CLSS;
	private double SO_ORDR_QTY;
	private double SO_INVC_QTY;
	private double SO_AMNT;
	private double SO_TAX_AMNT;	
	private String ORDER_BY;
	private String SO_SLS_SLSPRSN_CODE;
	private String SO_SLS_NM;
	private String SO_CST_CSTMR_CODE2;   
	private String SO_CST_NM;
	private String SOL_II_NM;
	private double SOL_QTY;
	private String SO_ORDR_STTS;
	private String SO_APPRVL_STTS;
	private String SO_APPRVDRJCTD_BY;
	private String SO_INV_NMBRS;
	
	public ArRepSalesOrderDetails() {
    }
	
	public Date getSoDate() {
		
		return SO_DT;
		
	}
	
	public void setSoDate(Date SO_DT) {
		
		this.SO_DT = SO_DT;
		
	}
	
	public String getSoDocumentNumber() {
		
		return SO_DCMNT_NMBR;
		
	}
	
	public void setSoDocumentNumber(String SO_DCMNT_NMBR) {
		
		this.SO_DCMNT_NMBR = SO_DCMNT_NMBR;
		
	}
	
	public String getSoReferenceNumber() {
		
		return SO_RFRNC_NMBR;
		
	}
	
	public void setSoReferenceNumber(String SO_RFRNC_NMBR) {
		
		this.SO_RFRNC_NMBR = SO_RFRNC_NMBR;
		
	}
	
	public String getSoDescription() {
		
		return SO_DESC;
		
	}
	
	public void setSoDescription(String SO_DESC) {
		
		this.SO_DESC = SO_DESC;
		
	}
	
	public String getSoCstCustomerCode() {
		
		return SO_CST_CSTMR_CODE;
		
	}
	
	public void setSoCstCustomerCode(String SO_CST_CSTMR_CODE) {
		
		this.SO_CST_CSTMR_CODE = SO_CST_CSTMR_CODE;
		
	}
	
	public String getSoCstCustomerType() {
		
		return SO_CST_CSTMR_TYP;
		
	}
	
	public void setSoCstCustomerType(String SO_CST_CSTMR_TYP) {
		
		this.SO_CST_CSTMR_TYP = SO_CST_CSTMR_TYP;
		
	}
	
	public String getSoCstCustomerClass() {
		
		return SO_CST_CSTMR_CLSS;
		
	}
	
	public void setSoCstCustomerClass(String SO_CST_CSTMR_CLSS) {
		
		this.SO_CST_CSTMR_CLSS = SO_CST_CSTMR_CLSS;
		
	}
	
	
	public double getSoOrderQty() {
		
		return SO_ORDR_QTY;
		
	}
	
	public void setSoOrderQty(double SO_ORDR_QTY) {
		
		this.SO_ORDR_QTY = SO_ORDR_QTY;
		
	}
	

	
	public double getSoInvoiceQty() {
		
		return SO_INVC_QTY;
		
	}
	
	public void setSoInvoiceQty(double SO_INVC_QTY) {
		
		this.SO_INVC_QTY = SO_INVC_QTY;
		
	}

	
	public double getSoAmount() {
		
		return SO_AMNT;
		
	}
	
	public void setSoAmount(double SO_AMNT) {
		
		this.SO_AMNT = SO_AMNT;
		
	}
	
	public double getSoTaxAmount() {
		
		return SO_TAX_AMNT;
		
	}
	
	public void setSoTaxAmount(double SO_TAX_AMNT) {
		
		this.SO_TAX_AMNT = SO_TAX_AMNT;
		
	}
	
	public String getOrderBy() {
		
		return ORDER_BY;
		
	}
	
	public void setOrderBy(String ORDER_BY) {
		
		this.ORDER_BY = ORDER_BY;
		
	}
	
	public String getSoCstCustomerCode2() {
		
		return SO_CST_CSTMR_CODE2;
	
	}
	
	public void setSoCstCustomerCode2(String SO_CST_CSTMR_CODE2) {
	
		this.SO_CST_CSTMR_CODE2 = SO_CST_CSTMR_CODE2;
	
	}
	
	public String getSoSlsSalespersonCode() {
		
		return SO_SLS_SLSPRSN_CODE;
		
	}
	
	public void setSoSlsSalespersonCode(String SO_SLS_SLSPRSN_CODE) {
		
		this.SO_SLS_SLSPRSN_CODE = SO_SLS_SLSPRSN_CODE;
		
	}
	
	public String getSoSlsName() {
		
		return SO_SLS_NM;
		
	}
	
	public void setSoSlsName(String SO_SLS_NM) {
		
		this.SO_SLS_NM = SO_SLS_NM;
		
	}
	
	public String getSoCstName() {
		
		return SO_CST_NM;
		
	}
	
	public void setSoCstName(String SO_CST_NM) {
		
		this.SO_CST_NM = SO_CST_NM;
		
	}
	public String getSolIIName() {
		
		return SOL_II_NM;
		
	}
	
	public void setSolIIName(String SOL_II_NM) {
		
		this.SOL_II_NM = SOL_II_NM;
		
	}
	
	public double getSolQty() {
		
		return SOL_QTY;
		
	}

	public void setSolQty(double SOL_QTY) {
		
		this.SOL_QTY = SOL_QTY;
		
	}
	public String getSoOrderStatus() {
		
		return SO_ORDR_STTS;
		
	}
	
	public void setSoOrderStatus(String SO_ORDR_STTS) {
		
		this.SO_ORDR_STTS = SO_ORDR_STTS;
		
	}
	
	public String getSoApprovalStatus() {
		
		return SO_APPRVL_STTS;
		
	}
	
	public void setSoApprovalStatus(String SO_APPRVL_STTS) {
		
		this.SO_APPRVL_STTS = SO_APPRVL_STTS;
		
	}
	
	public String getSoApprovedRejectedBy() {
		
		return SO_APPRVDRJCTD_BY;
		
	}
	
	public void setSoApprovedRejectedBy(String SO_APPRVDRJCTD_BY) {
		
		this.SO_APPRVDRJCTD_BY = SO_APPRVDRJCTD_BY;
		
	}	
	
	public String getSoInvoiceNumbers() {
		
		return SO_INV_NMBRS;
		
	}
	
	public void setSoInvoiceNumbers(String SO_INV_NMBRS) {
		
		this.SO_INV_NMBRS = SO_INV_NMBRS;
		
	}
	
	public static Comparator CustomerCodeComparator = (SR, anotherSR) -> {

        String SO_CST_CSTMR_CODE1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerCode();
        String SO_CST_CSTMR_TYP1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerType();
        Date SO_DT1 = ((ArRepSalesOrderDetails) SR).getSoDate();
        String SO_DCMNT_NMBR1 = ((ArRepSalesOrderDetails) SR).getSoDocumentNumber();

        String SO_CST_CSTMR_CODE2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerCode();
        String SO_CST_CSTMR_TYP2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerType();
        Date SO_DT2 = ((ArRepSalesOrderDetails) anotherSR).getSoDate();
        String SO_DCMNT_NMBR2 = ((ArRepSalesOrderDetails) SR).getSoDocumentNumber();

        String ORDER_BY = ((ArRepSalesOrderDetails) SR).getOrderBy();

        if (!(SO_CST_CSTMR_CODE1.equals(SO_CST_CSTMR_CODE2))) {

            return SO_CST_CSTMR_CODE1.compareTo(SO_CST_CSTMR_CODE2);

        } else {

            if(ORDER_BY.equals("DATE") && !(SO_DT1.equals(SO_DT2))){

                return SO_DT1.compareTo(SO_DT2);

            } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SO_CST_CSTMR_TYP1.equals(SO_CST_CSTMR_TYP2))){

                return SO_CST_CSTMR_TYP1.compareTo(SO_CST_CSTMR_TYP2);

            } else {

                return SO_DCMNT_NMBR1.compareTo(SO_DCMNT_NMBR2);

            }

        }

    };
	
	public static Comparator CustomerTypeComparator = (SR, anotherSR) -> {

        String SO_CST_CSTMR_CODE1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerCode();
        String SO_CST_CSTMR_TYP1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerType();
        Date SO_DT1 = ((ArRepSalesOrderDetails) SR).getSoDate();
        String SO_DCMNT_NMBR1 = ((ArRepSalesOrderDetails) SR).getSoDocumentNumber();

        String SO_CST_CSTMR_CODE2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerCode();
        String SO_CST_CSTMR_TYP2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerType();
        Date SO_DT2 = ((ArRepSalesOrderDetails) anotherSR).getSoDate();
        String SO_DCMNT_NMBR2 = ((ArRepSalesOrderDetails) SR).getSoDocumentNumber();

        String ORDER_BY = ((ArRepSalesOrderDetails) SR).getOrderBy();

        if (!(SO_CST_CSTMR_TYP1.equals(SO_CST_CSTMR_TYP2))) {

            return SO_CST_CSTMR_TYP1.compareTo(SO_CST_CSTMR_TYP2);

        } else {

            if(ORDER_BY.equals("DATE") && !(SO_DT1.equals(SO_DT2))){

                return SO_DT1.compareTo(SO_DT2);

            } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SO_CST_CSTMR_CODE1.equals(SO_CST_CSTMR_CODE2))){

                return SO_CST_CSTMR_CODE1.compareTo(SO_CST_CSTMR_CODE2);

            } else {

                return SO_DCMNT_NMBR1.compareTo(SO_DCMNT_NMBR2);

            }

        }

    };
	
	public static Comparator CustomerClassComparator = (SR, anotherSR) -> {

        String SO_CST_CSTMR_CLSS1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerClass();
        String SO_CST_CSTMR_CODE1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerCode();
        String SO_CST_CSTMR_TYP1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerType();
        Date SO_DT1 = ((ArRepSalesOrderDetails) SR).getSoDate();
        String SO_DCMNT_NMBR1 = ((ArRepSalesOrderDetails) SR).getSoDocumentNumber();

        String SO_CST_CSTMR_CLSS2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerClass();
        String SO_CST_CSTMR_CODE2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerCode();
        String SO_CST_CSTMR_TYP2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerType();
        Date SO_DT2 = ((ArRepSalesOrderDetails) anotherSR).getSoDate();
        String SO_DCMNT_NMBR2 = ((ArRepSalesOrderDetails) anotherSR).getSoDocumentNumber();

        String ORDER_BY = ((ArRepSalesOrderDetails) SR).getOrderBy();

        if (!(SO_CST_CSTMR_CLSS1.equals(SO_CST_CSTMR_CLSS2))) {

            return SO_CST_CSTMR_CLSS1.compareTo(SO_CST_CSTMR_CLSS2);

        } else {

            if(ORDER_BY.equals("DATE") && !(SO_DT1.equals(SO_DT2))){

                return SO_DT1.compareTo(SO_DT2);

            } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SO_CST_CSTMR_CODE1.equals(SO_CST_CSTMR_CODE2))){

                return SO_CST_CSTMR_CODE1.compareTo(SO_CST_CSTMR_CODE2);

            } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SO_CST_CSTMR_TYP1.equals(SO_CST_CSTMR_TYP2))){

                return SO_CST_CSTMR_TYP1.compareTo(SO_CST_CSTMR_TYP2);

            } else {

                return SO_DCMNT_NMBR1.compareTo(SO_DCMNT_NMBR2);

            }

        }

    };
	
	public static Comparator SONumberComparator = (SR, anotherSR) -> {


        String SO_CST_CSTMR_CODE1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerCode();
        String SO_CST_CSTMR_TYP1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerType();
        Date SO_DT1 = ((ArRepSalesOrderDetails) SR).getSoDate();
        String SO_DCMNT_NMBR1 = ((ArRepSalesOrderDetails) SR).getSoDocumentNumber();

        String SO_CST_CSTMR_CODE2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerCode();
        String SO_CST_CSTMR_TYP2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerType();
        Date SO_DT2 = ((ArRepSalesOrderDetails) anotherSR).getSoDate();

        String SO_DCMNT_NMBR2 = ((ArRepSalesOrderDetails) anotherSR).getSoDocumentNumber();
        String ORDER_BY = ((ArRepSalesOrderDetails) SR).getOrderBy();

        if (!(SO_DCMNT_NMBR1.equals(SO_DCMNT_NMBR2))) {

            return SO_DCMNT_NMBR1.compareTo(SO_DCMNT_NMBR2);

        } else {


        if(ORDER_BY.equals("DATE") && !(SO_DT1.equals(SO_DT2))){

            return SO_DT1.compareTo(SO_DT2);

                } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SO_CST_CSTMR_CODE1.equals(SO_CST_CSTMR_CODE2))){

                    return SO_CST_CSTMR_CODE1.compareTo(SO_CST_CSTMR_CODE2);

                } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SO_CST_CSTMR_TYP1.equals(SO_CST_CSTMR_TYP2))){

                    return SO_CST_CSTMR_TYP1.compareTo(SO_CST_CSTMR_TYP2);

                } else {

                    return SO_DCMNT_NMBR1.compareTo(SO_DCMNT_NMBR2);

                }
        }
    };
	public static Comparator NoGroupComparator = (SR, anotherSR) -> {

        String SO_CST_CSTMR_CODE1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerCode();
        String SO_CST_CSTMR_TYP1 = ((ArRepSalesOrderDetails) SR).getSoCstCustomerType();
        Date SO_DT1 = ((ArRepSalesOrderDetails) SR).getSoDate();
        String SO_DCMNT_NMBR1 = ((ArRepSalesOrderDetails) SR).getSoDocumentNumber();

        String SO_CST_CSTMR_CODE2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerCode();
        String SO_CST_CSTMR_TYP2 = ((ArRepSalesOrderDetails) anotherSR).getSoCstCustomerType();
        Date SO_DT2 = ((ArRepSalesOrderDetails) anotherSR).getSoDate();
        String SO_DCMNT_NMBR2 = ((ArRepSalesOrderDetails) anotherSR).getSoDocumentNumber();

        String ORDER_BY = ((ArRepSalesOrderDetails) SR).getOrderBy();

        if(ORDER_BY.equals("DATE") && !(SO_DT1.equals(SO_DT2))){

            return SO_DT1.compareTo(SO_DT2);

        } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SO_CST_CSTMR_CODE1.equals(SO_CST_CSTMR_CODE2))){

            return SO_CST_CSTMR_CODE1.compareTo(SO_CST_CSTMR_CODE2);

        } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SO_CST_CSTMR_TYP1.equals(SO_CST_CSTMR_TYP2))){

            return SO_CST_CSTMR_TYP1.compareTo(SO_CST_CSTMR_TYP2);

        } else {

            return SO_DCMNT_NMBR1.compareTo(SO_DCMNT_NMBR2);

        }

    };
	
} // ArRepSalesRegisterDetails class