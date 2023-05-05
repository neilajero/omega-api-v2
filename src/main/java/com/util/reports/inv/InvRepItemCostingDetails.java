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

public class InvRepItemCostingDetails implements java.io.Serializable {

    public static Comparator ItemCostingComparator = (RIL, anotherRIL) -> {

        String IC_II_NM1 = ((InvRepItemCostingDetails) RIL).getIcItemName();
        Date IC_DT1 = ((InvRepItemCostingDetails) RIL).getIcDate();
        String IC_DCMNT_NMBR1 = ((InvRepItemCostingDetails) RIL).getIcSourceDocumentNumber();

        String IC_II_NM2 = ((InvRepItemCostingDetails) anotherRIL).getIcItemName();
        Date IC_DT2 = ((InvRepItemCostingDetails) anotherRIL).getIcDate();
        String IC_DCMNT_NMBR2 = ((InvRepItemCostingDetails) anotherRIL).getIcSourceDocumentNumber();

        Date test = null;

        try {
            if (IC_DT1 == null) {

                IC_DT1 = test;
                IC_DT2 = test;
                return IC_DT1.compareTo(IC_DT2);

            } else if (IC_DT2 == null) {

                IC_DT1 = test;
                IC_DT2 = test;
                return IC_DT1.compareTo(IC_DT2);

            } else {

                return IC_DT1.compareTo(IC_DT2);
            }
        }
        catch (Exception e) {


        }

        if (!(IC_II_NM1.equals(IC_II_NM2))) {

            return IC_II_NM1.compareTo(IC_II_NM2);

        } else if (!(IC_DT1.equals(IC_DT2))) {

            return IC_DT1.compareTo(IC_DT2);

        } else {

            return IC_DCMNT_NMBR1.compareTo(IC_DCMNT_NMBR2);
        }
    };
    private String IC_II_NM;
    private String IC_II_CTGRY;
    private String IC_II_CTGRY1;
    private String IC_II_UOM;
    private String IC_II_LCTN;
    private String IC_II_LCTN1;
    private Date IC_DT;
    private double IC_QTY_RCVD;
    private double IC_ITM_CST;
    private double IC_ACTL_CST;
    private double IC_ADJST_QTY;
    private double IC_ADJST_CST;
    private double IC_ASSMBLY_QTY;
    private double IC_ASSMBLY_CST;
    private double IC_QTY_SLD;
    private double IC_CST_OF_SLS;
    private double IC_RMNNG_QTY;
    private double IC_RMNNG_VL;
    private double IC_RMNNG_UNT_CST;
    private String IC_SRC_DCMNT;
    private String IC_SRC_DCMNT_NMBR;
    private double IC_BGNNNG_QTY;
    private double IC_BGNNNG_VL;
    private double IC_BGNNNG_UNT_CST;
    private String IC_RFRNC_NMBR;
    private double IC_OUT_QTY;
    private double IC_OUT_UNT_CST;
    private double IC_OUT_AMNT;
    private double IC_IN_QTY;
    private double IC_IN_UNT_CST;
    private double IC_IN_AMNT;

    public InvRepItemCostingDetails() {

    }

    public String getIcItemName() {

        return IC_II_NM;

    }

    public void setIcItemName(String IC_II_NM) {

        this.IC_II_NM = IC_II_NM;

    }

    public String getIcItemCategory() {

        return IC_II_CTGRY;

    }

    public void setIcItemCategory(String IC_II_CTGRY) {

        this.IC_II_CTGRY = IC_II_CTGRY;

    }

    public String getIcItemCategory1() {

        return IC_II_CTGRY1;

    }

    public void setIcItemCategory1(String IC_II_CTGRY1) {

        this.IC_II_CTGRY1 = IC_II_CTGRY1;

    }

    public String getIcItemUnitOfMeasure() {

        return IC_II_UOM;

    }

    public void setIcItemUnitOfMeasure(String IC_II_UOM) {

        this.IC_II_UOM = IC_II_UOM;

    }

    public String getIcItemLocation() {

        return IC_II_LCTN;

    }

    public void setIcItemLocation(String IC_II_LCTN) {

        this.IC_II_LCTN = IC_II_LCTN;

    }

    public String getIcItemLocation1() {

        return IC_II_LCTN1;

    }

    public void setIcItemLocation1(String IC_II_LCTN1) {

        this.IC_II_LCTN1 = IC_II_LCTN1;

    }

    public Date getIcDate() {

        return IC_DT;

    }

    public void setIcDate(Date IC_DT) {

        this.IC_DT = IC_DT;

    }

    public double getIcQuantityReceived() {

        return IC_QTY_RCVD;

    }

    public void setIcQuantityReceived(double IC_QTY_RCVD) {

        this.IC_QTY_RCVD = IC_QTY_RCVD;

    }

    public double getIcItemCost() {

        return IC_ITM_CST;

    }

    public void setIcItemCost(double IC_ITM_CST) {

        this.IC_ITM_CST = IC_ITM_CST;

    }

    public double getIcActualCost() {

        return IC_ACTL_CST;

    }

    public void setIcActualCost(double IC_ACTL_CST) {

        this.IC_ACTL_CST = IC_ACTL_CST;

    }

    public double getIcAdjustQuantity() {

        return IC_ADJST_QTY;

    }

    public void setIcAdjustQuantity(double IC_ADJST_QTY) {

        this.IC_ADJST_QTY = IC_ADJST_QTY;

    }

    public double getIcAdjustCost() {

        return IC_ADJST_CST;

    }

    public void setIcAdjustCost(double IC_ADJST_CST) {

        this.IC_ADJST_CST = IC_ADJST_CST;

    }

    public double getIcQuantitySold() {

        return IC_QTY_SLD;

    }

    public void setIcQuantitySold(double IC_QTY_SLD) {

        this.IC_QTY_SLD = IC_QTY_SLD;

    }

    public double getIcCostOfSales() {

        return IC_CST_OF_SLS;

    }

    public void setIcCostOfSales(double IC_CST_OF_SLS) {

        this.IC_CST_OF_SLS = IC_CST_OF_SLS;

    }

    public double getIcRemainingQuantity() {

        return IC_RMNNG_QTY;

    }

    public void setIcRemainingQuantity(double IC_RMNNG_QTY) {

        this.IC_RMNNG_QTY = IC_RMNNG_QTY;

    }

    public double getIcRemainingValue() {

        return IC_RMNNG_VL;

    }

    public void setIcRemainingValue(double IC_RMNNG_VL) {

        this.IC_RMNNG_VL = IC_RMNNG_VL;

    }

    public double getIcRemainingUnitCost() {

        return IC_RMNNG_UNT_CST;

    }

    public void setIcRemainingUnitCost(double IC_RMNNG_UNT_CST) {

        this.IC_RMNNG_UNT_CST = IC_RMNNG_UNT_CST;

    }

    public String getIcSourceDocument() {

        return IC_SRC_DCMNT;

    }

    public void setIcSourceDocument(String IC_SRC_DCMNT) {

        this.IC_SRC_DCMNT = IC_SRC_DCMNT;

    }

    public String getIcSourceDocumentNumber() {

        return IC_SRC_DCMNT_NMBR;

    }

    public void setIcSourceDocumentNumber(String IC_SRC_DCMNT_NMBR) {

        this.IC_SRC_DCMNT_NMBR = IC_SRC_DCMNT_NMBR;

    }

    public double getIcBeginningQuantity() {

        return IC_BGNNNG_QTY;
    }

    public void setIcBeginningQuantity(double IC_BGNNNG_QTY) {

        this.IC_BGNNNG_QTY = IC_BGNNNG_QTY;
    }

    public double getIcBeginningValue() {

        return IC_BGNNNG_VL;
    }

    public void setIcBeginningValue(double IC_BGNNNG_VL) {

        this.IC_BGNNNG_VL = IC_BGNNNG_VL;
    }

    public double getIcBeginningUnitCost() {

        return IC_BGNNNG_UNT_CST;
    }

    public void setIcBeginningUnitCost(double IC_BGNNNG_UNT_CST) {

        this.IC_BGNNNG_UNT_CST = IC_BGNNNG_UNT_CST;
    }

    public String getIcReferenceNumber() {

        return IC_RFRNC_NMBR;
    }

    public void setIcReferenceNumber(String IC_RFRNC_NMBR) {

        this.IC_RFRNC_NMBR = IC_RFRNC_NMBR;
    }

    public double getIcOutQuantity() {

        return IC_OUT_QTY;
    }

    public void setIcOutQuantity(double IC_OUT_QTY) {

        this.IC_OUT_QTY = IC_OUT_QTY;
    }

    public double getIcOutUnitCost() {

        return IC_OUT_UNT_CST;
    }

    public void setIcOutUnitCost(double IC_OUT_UNT_CST) {

        this.IC_OUT_UNT_CST = IC_OUT_UNT_CST;
    }

    public double getIcOutAmount() {

        return IC_OUT_AMNT;
    }

    public void setIcOutAmount(double IC_OUT_AMNT) {

        this.IC_OUT_AMNT = IC_OUT_AMNT;
    }

    public double getIcInQuantity() {

        return IC_IN_QTY;

    }

    public void setIcInQuantity(double IC_IN_QTY) {

        this.IC_IN_QTY = IC_IN_QTY;

    }

    public double getIcInUnitCost() {

        return IC_IN_UNT_CST;

    }

    public void setIcInUnitCost(double IC_IN_UNT_CST) {

        this.IC_IN_UNT_CST = IC_IN_UNT_CST;

    }

    public double getIcInAmount() {

        return IC_IN_AMNT;

    }

    public void setIcInAmount(double IC_IN_AMNT) {

        this.IC_IN_AMNT = IC_IN_AMNT;

    }


} // InvRepItemCostingDetails class