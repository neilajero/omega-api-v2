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


public class InvRepStockOnHandDetails implements java.io.Serializable, java.lang.Cloneable{

    public Object clone() throws CloneNotSupportedException {

        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw e;

        }
    }

	private String SH_II_NM;
	private String SH_II_CTGRY;
	private String SH_II_DESC;
	private String SH_II_CLSS;
	private String SH_LOC_NM;
	private String SH_UNT;
	private double SH_QTY;
	private boolean SH_II_FR_RRDR;
	private double SH_CMMTTD_QTY;
	private double SH_AVE_CST;
	private String SH_LST_PO_DT;
	private Date SH_LST_RCV_DT;
	private double SH_AGD_IN_MNTH;
	private double SH_VL;
	private double SH_UNPSTD_QTY;
	private String SH_PRT_NMBR;
	private String SH_CTGRY_NM;
	private String SH_ORDR_BY;
	
	private double SH_UNSRVD_PO;
	private double SH_FRCST_QTY;
	private double SH_UNT_CST;
	private double SH_SRP;

	private String SH_MISC;
	private String SH_BRNCH_CD;
	
	public InvRepStockOnHandDetails() {
    }


	public String getShItemClass() {

		return SH_II_CLSS;

	}

	public void setShItemClass(String SH_II_CLSS) {

		this.SH_II_CLSS = SH_II_CLSS;

	}

	public String getShCategoryName() {

		return SH_CTGRY_NM;

	}

	public void setShCategoryName(String SH_CTGRY_NM) {

		this.SH_CTGRY_NM = SH_CTGRY_NM;

	}
	
	public String getShPartNumber() {

		return SH_PRT_NMBR;

	}

	public void setShPartNumber(String SH_PRT_NMBR) {

		this.SH_PRT_NMBR = SH_PRT_NMBR;

	}

	public String getShItemDescription() {

		return SH_II_DESC;

	}

	public void setShItemDescription(String SH_II_DESC) {

		this.SH_II_DESC = SH_II_DESC;

	}

	public String getShItemName() {

		return SH_II_NM;

	}

	public void setShItemName(String SH_II_NM) {

		this.SH_II_NM = SH_II_NM;

	}
	
	
	public String getShItemCategory() {

		return SH_II_CTGRY;

	}

	public void setShItemCategory(String SH_II_CTGRY) {

		this.SH_II_CTGRY = SH_II_CTGRY;

	}

	public double getShQuantity() {

		return SH_QTY;

	}

	public void setShQuantity(double SH_QTY) {

		this.SH_QTY = SH_QTY;

	}

	public String getShLocation() {

		return SH_LOC_NM;

	}

	public void setShLocation(String SH_LOC_NM) {

		this.SH_LOC_NM = SH_LOC_NM;

	}

	public String getShUnit() {

		return SH_UNT;

	}

	public void setShUnit(String SH_UNT) {

		this.SH_UNT = SH_UNT;

	}

	public boolean getShItemForReorder() {

		return SH_II_FR_RRDR;

	}

	public void setShItemForReorder(boolean SH_II_FR_RRDR) {

		this.SH_II_FR_RRDR = SH_II_FR_RRDR;

	}

	public double getShCommittedQuantity() {

		return SH_CMMTTD_QTY;

	}

	public void setShCommittedQuantity(double SH_CMMTTD_QTY) {

		this.SH_CMMTTD_QTY = SH_CMMTTD_QTY;

	}

	public double getShAverageCost() {

		return SH_AVE_CST;

	}

	public void setShAverageCost(double SH_AVE_CST) {

		this.SH_AVE_CST = SH_AVE_CST;

	}

	public String getShLastPoDate() {

		return SH_LST_PO_DT;

	}

	public void setShLastPoDate(String SH_LST_PO_DT) {

		this.SH_LST_PO_DT = SH_LST_PO_DT;

	}
	
	
	
	public Date getShLastReceiveDate() {

		return SH_LST_RCV_DT;

	}

	public void setShLastReceiveDate(Date SH_LST_RCV_DT) {

		this.SH_LST_RCV_DT = SH_LST_RCV_DT;

	}

	public double getShAgedInMonth() {

		return SH_AGD_IN_MNTH;

	}

	public void setShAgedInMonth(double SH_AGD_IN_MNTH) {

		this.SH_AGD_IN_MNTH = SH_AGD_IN_MNTH;
	}

	
	
	public double getShValue() {

		return SH_VL;

	}

	public void setShValue(double SH_VL) {

		this.SH_VL = SH_VL;
	}

	public double getShUnpostedQuantity () {

		return SH_UNPSTD_QTY;

	}

	public void setShUnpostedQuantity (double SH_UNPSTD_QTY) {

		this.SH_UNPSTD_QTY = SH_UNPSTD_QTY;

	}

	public double getShUnservedPo () {

		return SH_UNSRVD_PO;
	}

	public void setShUnservedPo (double SH_UNSRVD_PO) {

		this.SH_UNSRVD_PO = SH_UNSRVD_PO;
	}

	public double getShForecastQuantity() {

		return SH_FRCST_QTY;
	}

	public void setShForecastQuantity(double SH_FRCST_QTY) {

		this.SH_FRCST_QTY = SH_FRCST_QTY;
	}

	public double getShUnitCost() {

		return SH_UNT_CST;
	}

	public void setShUnitCost(double SH_UNT_CST) {

		this.SH_UNT_CST = SH_UNT_CST;
	}
	
	
	public double getShSalesPrice() {

		return SH_SRP;
	}
	
	
	public void setShSalesPrice(double SH_SRP) {

		this.SH_SRP = SH_SRP;
	}
	
	public String getShOrderBy() {

		return SH_ORDR_BY;

	}

	public void setShOrderBy(String SH_ORDR_BY) {

		this.SH_ORDR_BY = SH_ORDR_BY;

	}
	
	public String getShMisc() {

		return SH_MISC;

	}

	public void setShMisc(String SH_MISC) {

		this.SH_MISC = SH_MISC;

	}

	public String getShBranchCode() {

		return SH_BRNCH_CD;

	}

	public void setShBranchCode(String SH_BRNCH_CD) {

		this.SH_BRNCH_CD = SH_BRNCH_CD;

	}
	
	
	
	
	public static Comparator ConsolidatedReportComparator = (r1, r2) -> {

        String SLS_BRNCH_CD1 = ((InvRepStockOnHandDetails) r1).getShBranchCode();
        String SLS_ITM_CTGRY1 = ((InvRepStockOnHandDetails) r1).getShCategoryName();
        String SLS_ITM_NM1 = ((InvRepStockOnHandDetails) r1).getShItemName();


        String SLS_ITM_NM2 = ((InvRepStockOnHandDetails) r2).getShItemName();
        String SLS_ITM_CTGRY2 = ((InvRepStockOnHandDetails) r2).getShCategoryName();
        String SLS_BRNCH_CD2 = ((InvRepStockOnHandDetails) r2).getShBranchCode();


        if (!(SLS_ITM_NM1.equals(SLS_ITM_NM1))) {

            return SLS_ITM_NM1.compareTo(SLS_ITM_NM1);

        } else {

            return SLS_ITM_NM1.compareTo(SLS_ITM_NM1);

        }

    };
	
	public static Comparator NoGroupComparator = (r1, r2) -> {

        String SLS_BRNCH_CD1 = ((InvRepStockOnHandDetails) r1).getShBranchCode();
        String SLS_ITM_NM1 = ((InvRepStockOnHandDetails) r1).getShItemName();
        String SLS_ITM_CTGRY1 = ((InvRepStockOnHandDetails) r1).getShItemCategory();

        String SLS_BRNCH_CD2 = ((InvRepStockOnHandDetails) r2).getShBranchCode();
        String SLS_ITM_NM2 = ((InvRepStockOnHandDetails) r2).getShItemName();
        String SLS_ITM_CTGRY2 = ((InvRepStockOnHandDetails) r2).getShItemCategory();


        if (!(SLS_BRNCH_CD1.equals(SLS_BRNCH_CD2))) {

            return SLS_BRNCH_CD1.compareTo(SLS_BRNCH_CD2);

        } else if (!(SLS_ITM_CTGRY1.equals(SLS_ITM_CTGRY2))) {

               return SLS_ITM_CTGRY1.compareTo(SLS_ITM_CTGRY2);

        } else {

            return SLS_ITM_NM1.compareTo(SLS_ITM_NM2);

        }

    };
} // InvRepStockOnHandDetails class