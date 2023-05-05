
/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ap;


public class ApModAnnualProcurementDetails implements java.io.Serializable{
	
	private String AP_ITM_NM;
	private String AP_ITM_DESC;
	private String AP_YR;
	private String AP_MNTH;
	private String AP_UNT;
	private String AP_DCMNT_NMBR;
	private String AP_INV_ITM_CTGRY;
	private double AP_UNT_CST;
	private double AP_QTY_JAN;
	private double AP_QTY_FEB;
	private double AP_QTY_MRCH;
	private double AP_QTY_APRL;
	private double AP_QTY_MAY;
	private double AP_QTY_JUN;
	private double AP_QTY_JUL;
	private double AP_QTY_AUG;
	private double AP_QTY_SEP;
	private double AP_QTY_OCT;
	private double AP_QTY_NOV;
	private double AP_QTY_DEC;
	
	private double AP_QTY_MNTH;
	private double AP_TTL_CST;
	
	
	
	public ApModAnnualProcurementDetails() {
    }
	
	public String getApItemName() {
		
		return AP_ITM_NM;
		
	}
	
	public void setApItemName(String AP_ITM_NM) {
		
		this.AP_ITM_NM = AP_ITM_NM;
		
	}
	
	public String getApDocumentNumber() {
		
		return AP_DCMNT_NMBR;
		
	}
	
	public void setApDocumentNumber(String AP_DCMNT_NMBR) {
		
		this.AP_DCMNT_NMBR = AP_DCMNT_NMBR;
		
	}
	
	public String getApMonth() {
		
		return AP_MNTH;
		
	}
	
	public void setApMonth(String AP_MNTH) {
		
		this.AP_MNTH = AP_MNTH;
		
	}
	
	public String getApItemDesc() {
		
		return AP_ITM_DESC;
		
	}
	
	public void setApItemDesc(String AP_ITM_DESC) {
		
		this.AP_ITM_DESC = AP_ITM_DESC;
		
	}
	
	public String getApYear() {
		
		return AP_YR;
		
	}
	
	public void setApYear(String AP_YR) {
		
		this.AP_YR = AP_YR;
		
	}
	public String getApUnit() {
		
		return AP_UNT;
		
	}
	
	public void setApUnit(String AP_UNT) {
		
		this.AP_UNT = AP_UNT;
		
	}
	public String getApInvItemCategory() {
		
		return AP_INV_ITM_CTGRY;
		
	}
	
	public void setApInvItemCategory(String AP_INV_ITM_CTGRY) {
		
		this.AP_INV_ITM_CTGRY = AP_INV_ITM_CTGRY;
		
	}
	
	public double getApUnitCost() {
		
		return AP_UNT_CST;
		
	}
	
	public void setApUnitCost(double AP_UNT_CST) {
		
		this.AP_UNT_CST = AP_UNT_CST;
		
	}
	
	public double getApTotalCost() {
		
		return AP_TTL_CST;
		
	}
	
	public void setApTotalCost(double AP_TTL_CST) {
		
		this.AP_TTL_CST = AP_TTL_CST;
		
	}
	
	
	public double getApQtyMonth() {
		
		return AP_QTY_MNTH;
		
	}
	
	public void setApQtyMonth(double AP_QTY_MNTH) {
		
		this.AP_QTY_MNTH = AP_QTY_MNTH;
		
	}
	
	public double getApQtyJan() {
		
		return AP_QTY_JAN;
		
	}
	
	public void setApQtyJan(double AP_QTY_JAN) {
		
		this.AP_QTY_JAN = AP_QTY_JAN;
		
	}
	
	public double getApQtyFeb() {
		
		return AP_QTY_FEB;
		
	}
	
	public void setApQtyFeb(double AP_QTY_FEB) {
		
		this.AP_QTY_FEB = AP_QTY_FEB;
		
	}
	
	public double getApQtyMrch() {
		
		return AP_QTY_MRCH;
		
	}
	
	public void setApQtyMrch(double AP_QTY_MRCH) {
		
		this.AP_QTY_MRCH = AP_QTY_MRCH;
		
	}
	
	public double getApQtyAprl() {
		
		return AP_QTY_APRL;
		
	}
	
	public void setApQtyAprl(double AP_QTY_APRL) {
		
		this.AP_QTY_APRL = AP_QTY_APRL;
		
	}
	
	public double getApQtyMay() {
		
		return AP_QTY_MAY;
		
	}
	
	public void setApQtyMay(double AP_QTY_MAY) {
		
		this.AP_QTY_MAY = AP_QTY_MAY;
		
	}
	
	public double getApQtyJun() {
		
		return AP_QTY_JUN;
		
	}
	
	public void setApQtyJun(double AP_QTY_JUN) {
		
		this.AP_QTY_JUN = AP_QTY_JUN;
		
	}
	
	public double getApQtyJul() {
		
		return AP_QTY_JUL;
		
	}
	
	public void setApQtyJul(double AP_QTY_JUL) {
		
		this.AP_QTY_JUL = AP_QTY_JUL;
		
	}
	
	public double getApQtyAug() {
		
		return AP_QTY_AUG;
		
	}
	
	public void setApQtyAug(double AP_QTY_AUG) {
		
		this.AP_QTY_JUN = AP_QTY_AUG;
		
	}
	
	public double getApQtySep() {
		
		return AP_QTY_SEP;
		
	}
	
	public void setApQtySep(double AP_QTY_SEP) {
		
		this.AP_QTY_SEP = AP_QTY_SEP;
		
	}
	
	public double getApQtyOct() {
		
		return AP_QTY_OCT;
		
	}
	
	public void setApQtyOct(double AP_QTY_OCT) {
		
		this.AP_QTY_OCT = AP_QTY_OCT;
		
	}
	
	public double getApQtyNov() {
		
		return AP_QTY_NOV;
		
	}
	
	public void setApQtyNov(double AP_QTY_NOV) {
		
		this.AP_QTY_NOV = AP_QTY_NOV;
		
	}
	
public double getApQtyDec() {
		
		return AP_QTY_DEC;
		
	}
	
	public void setApQtyDec(double AP_QTY_DEC) {
		
		this.AP_QTY_DEC = AP_QTY_DEC;
		
	}
}