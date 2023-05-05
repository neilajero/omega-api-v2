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

public class ArRepDeliveryReceiptDetails implements java.io.Serializable {
	
	private String DR_CSTMR_CODE;
	private String DR_CSTMR_NM;
	private Date DR_DT;
	private String DR_DCMNT_NMBR;
	private String DR_RFRNC_NMBR;
	private String DR_DESC;
	private String DR_II_NM;
	private String DR_LOC_NM;
	private double DR_QTY;
	private String DR_UNT;
	private double DR_UNT_PRC;
	private double DR_AMNT;
	private String ORDER_BY;
	
	
	public ArRepDeliveryReceiptDetails() {
    }

	public String getDrCustomerCode() {

		return DR_CSTMR_CODE;

	}

	public void setDrCustomerCode(String DR_CSTMR_CODE) {

		this.DR_CSTMR_CODE = DR_CSTMR_CODE;

	}
	
	public String getDrCustomerName() {
		
		return DR_CSTMR_NM;
		
	}
	
	public void setDrCustomerName(String DR_CSTMR_NM) {
		
		this.DR_CSTMR_NM = DR_CSTMR_NM;
		
	}
	
	public Date getDrDate() {
		
		return DR_DT;
		
	}
	
	public void setDrDate(Date DR_DT) {
		
		this.DR_DT = DR_DT;
		
	}
	
	public String getDrDocumentNumber() {
		
		return DR_DCMNT_NMBR;
		
	}
	
	public void setDrDocumentNumber(String DR_DCMNT_NMBR) {
		
		this.DR_DCMNT_NMBR = DR_DCMNT_NMBR;
		
	}
	
	public String getDrReferenceNumber() {
		
		return DR_RFRNC_NMBR;
		
	}
	
	public void setDrReferenceNumber(String DR_RFRNC_NMBR) {
		
		this.DR_RFRNC_NMBR = DR_RFRNC_NMBR;
		
	}
		
	public String getDrDescription() {
		
		return DR_DESC;
		
	}
	
	public void setDrDescription(String DR_DESC) {
		
		this.DR_DESC = DR_DESC;
		
	}
	
	public double getDrAmount() {
		
		return DR_AMNT;
		
	}
	
	public void setDrAmount(double DR_AMNT) {
		
		this.DR_AMNT = DR_AMNT;
		
	}
	
	public String getOrderBy() {
		
		return ORDER_BY;
		
	}
	
	public void setOrderBy(String ORDER_BY) {
		
		this.ORDER_BY = ORDER_BY;
		
	}
		
	public String getDrItemName() {
		
		return DR_II_NM;
		
	}
	
	public void setDrItemName(String DR_II_NM) {
		
		this.DR_II_NM = DR_II_NM;
		
	}
	
	public String getDrLocation() {
		
		return DR_LOC_NM;
		
	}
	
	public void setDrLocation(String DR_LOC_NM) {
		
		this.DR_LOC_NM = DR_LOC_NM;
		
	}
	
	public double getDrQuantity() {
		
		return DR_QTY;
		
	}
	
	public void setDrQuantity(double DR_QTY) {
		
		this.DR_QTY = DR_QTY;
		
	}
	
	public String getDrUnit() {
		
		return DR_UNT;
		
	}
	
	public void setDrUnit(String DR_UNT) {
		
		this.DR_UNT = DR_UNT;
		
	}
	
	public double getDrUnitPrice() {
		
		return DR_UNT_PRC;
		
	}
	
	public void setDrUnitPrice(double DR_UNT_PRC) {
		
		this.DR_UNT_PRC = DR_UNT_PRC;
		
	}
	
	public static Comparator ItemNameComparator = (DR, anotherDR) -> {

        String DR_II_NM1 = ((ArRepDeliveryReceiptDetails) DR).getDrItemName();
        Date DR_DT1 = ((ArRepDeliveryReceiptDetails) DR).getDrDate();
        String DR_DCMNT_NMBR1 = ((ArRepDeliveryReceiptDetails) DR).getDrDocumentNumber();


        String DR_II_NM2 = ((ArRepDeliveryReceiptDetails) anotherDR).getDrItemName();
        Date DR_DT2 = ((ArRepDeliveryReceiptDetails) anotherDR).getDrDate();
        String DR_DCMNT_NMBR2 = ((ArRepDeliveryReceiptDetails) anotherDR).getDrDocumentNumber();

        String ORDER_BY = ((ArRepDeliveryReceiptDetails) DR).getOrderBy();

        if (!(DR_II_NM1.equals(DR_II_NM2))) {

            return DR_II_NM1.compareTo(DR_II_NM2);

        } else {

            if(ORDER_BY.equals("DATE") && !(DR_DT1.equals(DR_DT2))){

                return DR_DT1.compareTo(DR_DT2);

            } else {

                return DR_DCMNT_NMBR1.compareTo(DR_DCMNT_NMBR2);

            }
        }

    };
	
	public static Comparator CustomerComparator = (DR, anotherDR) -> {

        String DR_CSTMR_NM1 = ((ArRepDeliveryReceiptDetails) DR).getDrCustomerName();
        Date DR_DT1 = ((ArRepDeliveryReceiptDetails) DR).getDrDate();
        String DR_DCMNT_NMBR1 = ((ArRepDeliveryReceiptDetails) DR).getDrDocumentNumber();


        String DR_CSTMR_NM2 = ((ArRepDeliveryReceiptDetails) anotherDR).getDrCustomerName();
        Date DR_DT2 = ((ArRepDeliveryReceiptDetails) anotherDR).getDrDate();
        String DR_DCMNT_NMBR2 = ((ArRepDeliveryReceiptDetails) anotherDR).getDrDocumentNumber();

        String ORDER_BY = ((ArRepDeliveryReceiptDetails) anotherDR).getOrderBy();

        if (!(DR_CSTMR_NM1.equals(DR_CSTMR_NM2))) {

            return DR_CSTMR_NM1.compareTo(DR_CSTMR_NM2);

        } else {

            if(ORDER_BY.equals("DATE") && !(DR_DT1.equals(DR_DT2))){

                return DR_DT1.compareTo(DR_DT2);

            } else {

                return DR_DCMNT_NMBR1.compareTo(DR_DCMNT_NMBR2);

            }
        }

    };
	
	public static Comparator NoGroupComparator = (DR, anotherDR) -> {

        Date DR_DT1 = ((ArRepDeliveryReceiptDetails) DR).getDrDate();
        String DR_DCMNT_NMBR1 = ((ArRepDeliveryReceiptDetails) DR).getDrDocumentNumber();

        Date DR_DT2 = ((ArRepDeliveryReceiptDetails) anotherDR).getDrDate();
        String DR_DCMNT_NMBR2 = ((ArRepDeliveryReceiptDetails) anotherDR).getDrDocumentNumber();

        String ORDER_BY = ((ArRepDeliveryReceiptDetails) DR).getOrderBy();

            if(ORDER_BY.equals("DATE") && !(DR_DT1.equals(DR_DT2))){

                return DR_DT1.compareTo(DR_DT2);

            } else {

                return DR_DCMNT_NMBR1.compareTo(DR_DCMNT_NMBR2);

            }

    };
	
} // ArRepDeliveryReceiptDetails class