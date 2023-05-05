/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

public class InvRepAdjustmentRegisterRmVarDetails implements java.io.Serializable {

	private String IL_II_NM;
	private String IL_II_DESC;
	private String IL_II_UOM;
	private Double IL_II_STD_UNIT_COST = 0d;
	private Double IL_II_ACTUAL_QTY_USED= 0d;
	private Double IL_II_STD_QTY= 0d;
	private Double IL_II_VARIANCE_QTY= 0d;
	private Double IL_II_ACTUAL_AMNT= 0d;
	private Double IL_II_STD_AMNT= 0d;
	private Double IL_II_VARIANCE_AMNT= 0d;
	
	

	
	public InvRepAdjustmentRegisterRmVarDetails() {
    }

	public String getIlIiName() {

		return IL_II_NM;

	}

	public void setIlIiName(String IL_II_NM) {

		this.IL_II_NM = IL_II_NM;

	}


	public String getIlIiDescription() {

		return IL_II_DESC;

	}

	public void setIlIiDescription(String IL_II_DESC) {

		this.IL_II_DESC = IL_II_DESC;

	}
	
	public String getIlIiUomName() {

		return IL_II_UOM;

	}

	public void setIlIiUomName(String IL_II_UOM) {

		this.IL_II_UOM = IL_II_UOM;

	}

	public Double getIlIiStdUnitCost() {

		return IL_II_STD_UNIT_COST;

	}
	
	public void setIlIiStdUnitCost(Double IL_II_STD_UNIT_COST) {

		this.IL_II_STD_UNIT_COST = IL_II_STD_UNIT_COST;

	}
	
	public Double getIlIiActualQtyUsed() {

		return IL_II_ACTUAL_QTY_USED;

	}
	
	public void setIlIiActualQtyUsed(Double IL_II_ACTUAL_QTY_USED) {

		this.IL_II_ACTUAL_QTY_USED = IL_II_ACTUAL_QTY_USED;

	}
	
	public Double getIlIiStdQty() {

		return IL_II_STD_QTY;

	}
	
	public void setIlIiStdQty(Double IL_II_STD_QTY) {

		this.IL_II_STD_QTY = IL_II_STD_QTY;

	}

	public Double getIlIiVarianceQty() {

		return IL_II_VARIANCE_QTY;

	}
	
	public void setIlIiVarianceQty(Double IL_II_VARIANCE_QTY) {

		this.IL_II_VARIANCE_QTY = IL_II_VARIANCE_QTY;

	}
	
	public Double getIlIiActualAmnt() {

		return IL_II_ACTUAL_AMNT;
		
	}
	
	public void setIlIiActualAmnt(Double IL_II_ACTUAL_AMNT) {

		this.IL_II_ACTUAL_AMNT = IL_II_ACTUAL_AMNT;

	}
	
	public Double getIlIiStdAmnt() {

		return IL_II_STD_AMNT;
		
	}
	
	public void setIlIiStdAmnt(Double IL_II_STD_AMNT) {

		this.IL_II_STD_AMNT = IL_II_STD_AMNT;

	}
	
	public Double getIlIiVarianceAmnt() {

		return IL_II_VARIANCE_AMNT;

	}
	
	public void setIlIiVarianceAmnt(Double IL_II_VARIANCE_AMNT) {

		this.IL_II_VARIANCE_AMNT = IL_II_VARIANCE_AMNT;

	}
	
	

} // InvRepItemListDetails class