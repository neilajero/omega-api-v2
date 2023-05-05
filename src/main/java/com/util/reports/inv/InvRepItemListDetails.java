/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.ArrayList;
import java.util.Date;

public class InvRepItemListDetails implements java.io.Serializable {

	private String IL_II_NM;
	private String IL_II_DESC;
	private String IL_II_CLSS;
	private byte IL_II_ENBL;
	private String IL_II_UOM_NM;
	private double IL_II_UNT_CST;
	private double IL_II_PRC;
	private String IL_II_CST_MTHD;
	private String IL_II_SPPLR_NM;
	private String IL_II_PRT_NMBR;
	private String IL_LOC_NM;
	private String IL_LOC_DPRTMNT;
	private String IL_LOC_BRNCH;
	private String IL_CTGRY_NM;
	private Date II_DT_ACQRD;
	private ArrayList II_PRC_LVLS;
	
	public InvRepItemListDetails() {
    }

	public String getIlIiName() {

		return IL_II_NM;

	}

	public void setIlIiName(String IL_II_NM) {

		this.IL_II_NM = IL_II_NM;

	}

	public String getIlIiCategoryName() {

		return IL_CTGRY_NM;

	}

	public void setIlIiCategoryName(String IL_CTGRY_NM) {

		this.IL_CTGRY_NM = IL_CTGRY_NM;

	}
	
	public String getIlIiLctn() {

		return IL_LOC_NM;

	}

	public void setIlIiLctn(String IL_II_LCTN) {

		this.IL_LOC_NM = IL_II_LCTN;

	}
	
	public String getIlIiBrnch() {

		return IL_LOC_BRNCH;

	}

	public void setIlIiBrnch(String IL_LOC_BRNCH) {

		this.IL_LOC_BRNCH = IL_LOC_BRNCH;

	}
	
	public String getIlIiDprtmnt() {

		return IL_LOC_DPRTMNT;

	}

	public void setIlIiDprtmnt(String IL_LOC_DPRTMNT) {

		this.IL_LOC_DPRTMNT = IL_LOC_DPRTMNT;

	}

	public String getIlIiDescription() {

		return IL_II_DESC;

	}

	public void setIlIiDescription(String IL_II_DESC) {

		this.IL_II_DESC = IL_II_DESC;

	}

	public String getIlIiClass() {

		return IL_II_CLSS;

	}

	public void setIlIiClass(String IL_II_CLSS) {

		this.IL_II_CLSS = IL_II_CLSS;

	}

	public byte getIlIiEnable() {

		return IL_II_ENBL;

	}

	public void setIlIiEnable(byte IL_II_ENBL) {

		this.IL_II_ENBL = IL_II_ENBL;

	}

	public String getIlIiUomName() {

		return IL_II_UOM_NM;

	}

	public void setIlIiUomName(String IL_II_UOM_NM){

		this.IL_II_UOM_NM = IL_II_UOM_NM;

	}

	public double getIlIiUnitCost() {

		return IL_II_UNT_CST;

	}

	public void setIlIiUnitCost(double IL_II_UNT_CST) {

		this.IL_II_UNT_CST = IL_II_UNT_CST;

	}

	public double getIlIiPrice() {

		return IL_II_PRC;

	}

	public void setIlIiPrice(double IL_II_PRC) {

		this.IL_II_PRC = IL_II_PRC;

	}

	public String getIlIiCostMethod() {

		return IL_II_CST_MTHD;

	}

	public void setIlIiCostMethod(String IL_II_CST_MTHD) {

		this.IL_II_CST_MTHD = IL_II_CST_MTHD;

	}

	public String getIlIiSupplierName() {

		return IL_II_SPPLR_NM;

	}

	public void setIlIiSupplierName(String IL_II_SPPLR_NM) {

		this.IL_II_SPPLR_NM = IL_II_SPPLR_NM;

	}

	public String getIlIiPartNumber() {

		return IL_II_PRT_NMBR;

	}

	public void setIlIiPartNumber(String IL_II_PRT_NMBR) {

		this.IL_II_PRT_NMBR = IL_II_PRT_NMBR;

	}
	
	public Date getIiDateAcquired() {
		
		return II_DT_ACQRD;
	
	}
	
	public void setIiDateAcquired(Date II_DT_ACQRD) {
		
		this.II_DT_ACQRD = II_DT_ACQRD;
	
	}
	
	public ArrayList getIiPriceLevels(){
		
		return II_PRC_LVLS;
		
	}
	
	public void setIiPriceLevels(ArrayList II_PRC_LVLS){
	
		this.II_PRC_LVLS = II_PRC_LVLS;
		
	}

} // InvRepItemListDetails class