/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Comparator;
import java.util.Date;

public class InvRepBranchStockTransferRegisterDetails implements java.io.Serializable {

	private String BSTR_BST_TYP;
	private String BSTR_BST_STTS;
	private String BSTR_BST_DCMNT_NMBR;
	private Date BSTR_BST_DT;
	private String BSTR_BST_TRNSFR_OUT_NMBR;
	private String BSTR_BST_BRNCH;
	private String BSTR_BST_TRNST_LCTN;
	private String BSTR_BST_CRTD_BY;
	
	private String BSTR_BSTL_II_NM;
	private String BSTR_BSTL_II_DESC;
	private String BSTR_BSTL_LCTN;
	private String BSTR_BSTL_UNT;
	private double BSTR_BSTL_QTY;
	private double BSTR_BSTL_UNT_CST;
	private double BSTR_BSTL_AMNT;
	
	private double BSTR_BSTL_QTY_TRNSFRD;
	
	private String ORDER_BY;

	public InvRepBranchStockTransferRegisterDetails() {
    }
	
	public String getBstrBstType() {
		
		return BSTR_BST_TYP;
		
	}
	
	public void setBstrBstType(String BSTR_BST_TYP) {
		
		this.BSTR_BST_TYP = BSTR_BST_TYP;
		
	}

	public String getBstrBstStatus() {

		return BSTR_BST_STTS;

	}

	public void setBstrBstStatus(String BSTR_BST_STTS) {

		this.BSTR_BST_STTS = BSTR_BST_STTS;

	}

	public String getBstrBstDocumentNumber() {

		return BSTR_BST_DCMNT_NMBR;

	}

	public void setBstrBstDocumentNumber(String BSTR_BST_DCMNT_NMBR) {

		this.BSTR_BST_DCMNT_NMBR = BSTR_BST_DCMNT_NMBR;

	}
	
	public Date getBstrBstDate() {
		
		return BSTR_BST_DT;
		
	}
	
	public void setBstrBstDate(Date BSTR_BST_DT) {
		
		this.BSTR_BST_DT = BSTR_BST_DT;
		
	}
	
	public String getBstrBstTransferOutNumber() {

		return BSTR_BST_TRNSFR_OUT_NMBR;

	}

	public void setBstrBstTransferOutNumber(String BSTR_BST_TRNSFR_OUT_NMBR) {

		this.BSTR_BST_TRNSFR_OUT_NMBR = BSTR_BST_TRNSFR_OUT_NMBR;

	}
	
	public String getBstrBstBranch() {

		return BSTR_BST_BRNCH;

	}

	public void setBstrBstBranch(String BSTR_BST_BRNCH) {

		this.BSTR_BST_BRNCH = BSTR_BST_BRNCH;

	}
	
	public String getBstrBstTransitLocation() {

		return BSTR_BST_TRNST_LCTN;

	}

	public void setBstrBstTransitLocation(String BSTR_BST_TRNST_LCTN) {

		this.BSTR_BST_TRNST_LCTN = BSTR_BST_TRNST_LCTN;

	}

	public String getBstrBstCreatedBy() {

		return BSTR_BST_CRTD_BY;

	}

	public void setBstrBstCreatedBy(String BSTR_BST_CRTD_BY) {

		this.BSTR_BST_CRTD_BY = BSTR_BST_CRTD_BY;

	}
	
	public String getBstrBstlItemName() {
		
		return BSTR_BSTL_II_NM;
	
	}
	
	public void setBstrBstlItemName(String BSTR_BSTL_II_NM) {
	
		this.BSTR_BSTL_II_NM = BSTR_BSTL_II_NM;
	
	}
	
	public String getBstrBstlItemDescription() {
		
		return BSTR_BSTL_II_DESC;
	
	}
	
	public void setBstrBstlItemDescription(String BSTR_BSTL_II_DESC) {
	
		this.BSTR_BSTL_II_DESC = BSTR_BSTL_II_DESC;
	
	}
	
	public String getBstrBstlLocation() {
		
		return BSTR_BSTL_LCTN;
		
	}
	
	public void setBstrBstlLocation(String BSTR_BSTL_LCTN) {
		
		this.BSTR_BSTL_LCTN = BSTR_BSTL_LCTN;
		
	}
	
	public String getBstrBstlUnit() {

		return BSTR_BSTL_UNT;

	}

	public void setBstrBstlUnit(String BSTR_BSTL_UNT) {

		this.BSTR_BSTL_UNT = BSTR_BSTL_UNT;

	}
	
	public double getBstrBstlQuantity() {
		
		return BSTR_BSTL_QTY;
	
	}
	
	public void setBstrBstlQuantity(double BSTR_BSTL_QTY) {
	
		this.BSTR_BSTL_QTY = BSTR_BSTL_QTY;
	
	}
	
	public double getBstrBstlUnitCost() {
		
		return BSTR_BSTL_UNT_CST;
	
	}
	
	public void setBstrBstlUnitCost(double BSTR_BSTL_UNT_CST) {
	
		this.BSTR_BSTL_UNT_CST = BSTR_BSTL_UNT_CST;
	
	}
	
	public double getBstrBstlAmount() {
		
		return BSTR_BSTL_AMNT;
	
	}
	
	public void setBstrBstlAmount(double BSTR_BSTL_AMNT) {
	
		this.BSTR_BSTL_AMNT = BSTR_BSTL_AMNT;
	
	}
	
	public double getBstrBstlQuantityTransfered() {
		
		return BSTR_BSTL_QTY_TRNSFRD;
		
	}
	
	public void setBstrBstlQuantityTransfered(double BSTR_BSTL_QTY_TRNSFRD) {
		
		this.BSTR_BSTL_QTY_TRNSFRD = BSTR_BSTL_QTY_TRNSFRD;
		
	}	
	
	public String getOrderBy() {

		return ORDER_BY;

	}

	public void setOrderBy(String ORDER_BY) {

		this.ORDER_BY = ORDER_BY;

	}

	public static Comparator StatusComparator = (BSTR, anotherBSTR) -> {

        String BSTR_BST_STTS1 = ((InvRepBranchStockTransferRegisterDetails)BSTR).getBstrBstStatus();
        String BSTR_BST_DCMNT_NMBR1 = ((InvRepBranchStockTransferRegisterDetails)BSTR).getBstrBstDocumentNumber();
           Date BSTR_BST_DT1 = ((InvRepBranchStockTransferRegisterDetails)BSTR).getBstrBstDate();

           String BSTR_BST_STTS2 = ((InvRepBranchStockTransferRegisterDetails)BSTR).getBstrBstStatus();
        String BSTR_BST_DCMNT_NMBR2 = ((InvRepBranchStockTransferRegisterDetails)anotherBSTR).getBstrBstDocumentNumber();
           Date BSTR_BST_DT2 = ((InvRepBranchStockTransferRegisterDetails)anotherBSTR).getBstrBstDate();

        String ORDER_BY = ((InvRepBranchStockTransferRegisterDetails) BSTR).getOrderBy();

        if (!(BSTR_BST_STTS1.equals(BSTR_BST_STTS2))) {

            return BSTR_BST_STTS1.compareTo(BSTR_BST_STTS2);

        } else {

            if(ORDER_BY.equals("DATE") && !(BSTR_BST_DT1.equals(BSTR_BST_DT2))){

                return BSTR_BST_DT1.compareTo(BSTR_BST_DT2);

            } else {

                return BSTR_BST_DCMNT_NMBR1.compareTo(BSTR_BST_DCMNT_NMBR2);

            }
        }

    };
    public static Comparator ItemNameComparator = (BSTR, anotherBSTR) -> {

        String BSTR_II_NM1 = ((InvRepBranchStockTransferRegisterDetails) BSTR).getBstrBstlItemName();
        Date BSTR_DT1 = ((InvRepBranchStockTransferRegisterDetails) BSTR).getBstrBstDate();
        String BSTR_DCMNT_NMBR1 = ((InvRepBranchStockTransferRegisterDetails) BSTR).getBstrBstDocumentNumber();

        String BSTR_II_NM2 = ((InvRepBranchStockTransferRegisterDetails) anotherBSTR).getBstrBstlItemName();
        Date BSTR_DT2 = ((InvRepBranchStockTransferRegisterDetails) anotherBSTR).getBstrBstDate();
        String BSTR_DCMNT_NMBR2 = ((InvRepBranchStockTransferRegisterDetails) anotherBSTR).getBstrBstDocumentNumber();

        String ORDER_BY = ((InvRepBranchStockTransferRegisterDetails) BSTR).getOrderBy();

        if (!(BSTR_II_NM1.equals(BSTR_II_NM2))) {

            return BSTR_II_NM1.compareTo(BSTR_II_NM2);

        } else {

            if(ORDER_BY.equals("DATE") && !(BSTR_DT1.equals(BSTR_DT2))){

                return BSTR_DT1.compareTo(BSTR_DT2);

            } else {

                return BSTR_DCMNT_NMBR1.compareTo(BSTR_DCMNT_NMBR2);

            }
        }

    };

} // InvRepBranchStockTransferRegisterDetails class