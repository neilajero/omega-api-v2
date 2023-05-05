/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.inv.InvBranchStockTransferLineDetails;

import java.util.ArrayList;

public class InvModBranchStockTransferLineDetails extends InvBranchStockTransferLineDetails implements java.io.Serializable {

    private String BSL_II_NM;
    private String BSL_II_DSC;
    private String BSL_UOM_NM;
    private String BSL_LOC_NM;
    private short BSL_LN_NMBR;
    private double BSL_SHPPNG_CST;
    private String BSL_MISC;
    private String BSL_PRT_NMBR;
    private ArrayList BSL_TG_LST = new ArrayList();
    private byte BSL_TRC_MISC;

    public InvModBranchStockTransferLineDetails() {
    }

    public byte getTraceMisc() {

		return BSL_TRC_MISC;

	}

	public void setTraceMisc(byte BSL_TRC_MISC) {

		this.BSL_TRC_MISC = BSL_TRC_MISC;

	}

    public String getBslIiName() {
        return BSL_II_NM;
    }

    public void setBslIiName(String BSL_II_NM) {
        this.BSL_II_NM = BSL_II_NM;
    }

    public String getBslIiDescription() {
        return BSL_II_DSC;
    }

    public void setBslIiDescription(String BSL_II_DSC) {
        this.BSL_II_DSC = BSL_II_DSC;
    }

    public String getBslUomName() {
        return BSL_UOM_NM;
    }

    public void setBslUomName(String BSL_UOM_NM) {
        this.BSL_UOM_NM = BSL_UOM_NM;
    }

    public String getBslLocationName() {
        return BSL_LOC_NM;
    }

    public void setBslLocationName(String BSL_LOC_NM) {
        this.BSL_LOC_NM = BSL_LOC_NM;
    }

    public short getBslLineNumber() {
        return BSL_LN_NMBR;
    }

    public void setBslLineNumber(short BSL_LN_NMBR) {
        this.BSL_LN_NMBR = BSL_LN_NMBR;
    }

    public double getBslShippingCost() {
    	return BSL_SHPPNG_CST;
    }

    public void setBslShippingCost(double BSL_SHPPNG_CST) {
    	this.BSL_SHPPNG_CST = BSL_SHPPNG_CST;
    }

    public String getBslMisc() {
    	return BSL_MISC;
    }

    public void setBslMisc(String BSL_MISC) {
    	this.BSL_MISC = BSL_MISC;
    }

    public String getBslPartNumber() {
    	return BSL_PRT_NMBR;
    }

    public void setBslPartNumber(String BSL_PRT_NMBR) {
    	this.BSL_PRT_NMBR = BSL_PRT_NMBR;
    }

    public ArrayList getBslTagList() {

		return BSL_TG_LST;

	}

	public void setBslTagList(ArrayList BSL_TG_LST) {

		this.BSL_TG_LST = BSL_TG_LST;

	}
} // InvModStockTransferLineDetails class