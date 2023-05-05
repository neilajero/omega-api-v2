/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.Debug;
import com.util.inv.InvAdjustmentLineDetails;

import java.util.ArrayList;

public class InvModAdjustmentLineDetails extends InvAdjustmentLineDetails implements java.io.Serializable {

    private String alUomName;
    private String alLocName;
    private String alIiName;
    private String alIiCategory;
    private String alIiDescription;
    private Double alActualQuantity;
    private double alActualQuantityFind;
    private short alLineNumber;
    private String AL_MISC = null;
    private byte traceMisc;
    private String alPartNumber;

    private ArrayList alTagList = new ArrayList();

    public InvModAdjustmentLineDetails() {
    }

	public String getAlIiName() {
		return alIiName;
	}

	public void setAlIiName(String alIiName) {
		this.alIiName = alIiName;
	}
	
	public String getAlIiCategory() {
		return alIiCategory;
	}

	public void setAlIiCategory(String alIiCategory) {
		this.alIiCategory = alIiCategory;
	}

	public String getAlIiDescription() {
		return alIiDescription;
	}

	public void setAlIiDescription(String alIiDescription) {
		this.alIiDescription = alIiDescription;
	}

	public String getAlLocName() {
		return alLocName;
	}

	public void setAlLocName(String alLocName) {
		this.alLocName = alLocName;
	}

	public String getAlUomName() {
		return alUomName;
	}

	public void setAlUomName(String alUomName) {
		this.alUomName = alUomName;
	}



	public Double getAlActualQuantity() {
		return alActualQuantity;
	}

	public void setAlActualQuantity(Double alActualQuantity) {
		this.alActualQuantity = alActualQuantity;
	}
	public Double getAlActualQuantityFind() {
		return alActualQuantityFind;
	}

	public void setAlActualQuantityFind(Double alActualQuantityFind) {
		this.alActualQuantityFind = alActualQuantityFind;
	}
	public short getAlLineNumber() {
		return alLineNumber;
	}

	public void setAlLineNumber(short alLineNumber) {
		this.alLineNumber = alLineNumber;
	}

	public String getAlMisc() {
		return AL_MISC;

	}

	public void setAlMisc(String AL_MISC) {
		this.AL_MISC = AL_MISC;
		Debug.print("AL_MISC: " + AL_MISC);
	}

	public String getAlPartNumber() {
		return alPartNumber;
	}

	public void setAlPartNumber(String alPartNumber) {
		this.alPartNumber = alPartNumber;
	}

	public byte getTraceMisc() {

		return traceMisc;

	}

	public void setTraceMisc(byte traceMisc) {

		this.traceMisc = traceMisc;

	}

	public ArrayList getAlTagList() {

		   return alTagList;

	}

	public void setAlTagList(ArrayList alTagList) {

		this.alTagList = alTagList;

	}

} // InvModAdjustmentLineDetails class