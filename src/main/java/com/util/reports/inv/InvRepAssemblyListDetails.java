/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

public class InvRepAssemblyListDetails implements java.io.Serializable {

	private String AL_II_NM;
	private String AL_II_DESC;
	private String AL_II_UOM_NM;
	private String AL_CMP_NM;
	private String AL_CMP_DESC;
	private double AL_QTY_NDD;
	private String AL_CMP_UOM_NM;
	private byte AL_II_ENBL;
	private String AL_II_PRT_NMBR;
	private double AL_II_UNT_CST;
	
    public InvRepAssemblyListDetails() {
    }

	public String getAlIiName() {
		
		return AL_II_NM;
		
	}
	
	public void setAlIiName(String AL_II_NM) {
		
		this.AL_II_NM = AL_II_NM;
		
	}
	
	public String getAlIiDescription() {
		
		return AL_II_DESC;
		
	}
	
	public void setAlIiDescription(String AL_II_DESC) {
		
		this.AL_II_DESC = AL_II_DESC;
		
	}
	
	public String getAlIiUomName() {
		
		return AL_II_UOM_NM;
		
	}
	
	public void setAlIiUomName(String AL_II_UOM_NM) {
		
		this.AL_II_UOM_NM = AL_II_UOM_NM;
		
	}
	
	public String getAlComponentName() {
		
		return AL_CMP_NM;
		
	}
	
	public void setAlComponentName(String AL_CMP_NM) {
		
		this.AL_CMP_NM = AL_CMP_NM;
		
	}
	
	public String getAlComponentDescription() {
		
		return AL_CMP_DESC;
		
	}
	
	public void setAlComponentDescription(String AL_CMP_DESC) {
		
		this.AL_CMP_DESC = AL_CMP_DESC;
		
	}
	
	public double getAlQuantityNeeded() {
		
		return AL_QTY_NDD;
		
	}
	
	public void setAlQuantityNeeded(double AL_QTY_NDD) {
		
		this.AL_QTY_NDD = AL_QTY_NDD;
		
	}
	
	public String getAlComponentUomName() {
		
		return AL_CMP_UOM_NM;
		
	}
	
	public void setAlComponentUomName(String AL_CMP_UOM_NM) {
		
		this.AL_CMP_UOM_NM = AL_CMP_UOM_NM;
		
	}
	
	public byte getAlIiEnable() {
		
		return AL_II_ENBL;
		
	}
	
	public void setAlIiEnable(byte AL_II_ENBL) {
		
		this.AL_II_ENBL = AL_II_ENBL;
		
	}
	
	public String getAlIiPartNumber() {
		
		return AL_II_PRT_NMBR;
		
	}
	
	public void setAlIiPartNumber(String AL_II_PRT_NMBR) {
		
		this.AL_II_PRT_NMBR = AL_II_PRT_NMBR;
		
	}

	public double getAlIiUnitCost() {
		
		return AL_II_UNT_CST;
		
	}
	
	public void setAlIiUnitCost(double AL_II_UNT_CST) {
		
		this.AL_II_UNT_CST = AL_II_UNT_CST;
		
	}

} // InvRepAssemblyListDetails class