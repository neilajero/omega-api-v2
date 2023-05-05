
/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;


import com.util.inv.InvTransactionalBudgetDetails;

public class InvModTransactionalBudgetDetails extends InvTransactionalBudgetDetails implements java.io.Serializable{
	
	private String TB_ITM_NM;
	private String TB_ITM_DESC;
	private Integer TB_YR;
	private String TB_MNTH;
	private String TB_UNT;
	private String TB_DCMNT_NMBR;
	private String TB_INV_ITM_CTGRY;
	private double TB_UNT_CST;
	private double TB_QTY_JAN;
	private double TB_QTY_FEB;
	private double TB_QTY_MRCH;
	private double TB_QTY_APRL;
	private double TB_QTY_MAY;
	private double TB_QTY_JUN;
	private double TB_QTY_JUL;
	private double TB_QTY_AUG;
	private double TB_QTY_SEP;
	private double TB_QTY_OCT;
	private double TB_QTY_NOV;
	private double TB_QTY_DEC;
	
	private double TB_QTY_MNTH;
	private double TB_TTL_CST;
	
	
	
	public InvModTransactionalBudgetDetails() {
    }
	
	public String getTbItemName() {
		
		return TB_ITM_NM;
		
	}
	
	public void setTbItemName(String TB_ITM_NM) {
		
		this.TB_ITM_NM = TB_ITM_NM;
		
	}
	
	public String getTbDocumentNumber() {
		
		return TB_DCMNT_NMBR;
		
	}
	
	public void setTbDocumentNumber(String TB_DCMNT_NMBR) {
		
		this.TB_DCMNT_NMBR = TB_DCMNT_NMBR;
		
	}
	
	public String getTbMonth() {
		
		return TB_MNTH;
		
	}
	
	public void setTbMonth(String TB_MNTH) {
		
		this.TB_MNTH = TB_MNTH;
		
	}
	
	public String getTbItemDesc() {
		
		return TB_ITM_DESC;
		
	}
	
	public void setTbItemDesc(String TB_ITM_DESC) {
		
		this.TB_ITM_DESC = TB_ITM_DESC;
		
	}
	
	public Integer getTbYear() {
		
		return TB_YR;
		
	}
	
	public void setTbYear(Integer TB_YR) {
		
		this.TB_YR = TB_YR;
		
	}
	public String getTbUnit() {
		
		return TB_UNT;
		
	}
	
	public void setTbUnit(String TB_UNT) {
		
		this.TB_UNT = TB_UNT;
		
	}
	public String getTbInvItemCategory() {
		
		return TB_INV_ITM_CTGRY;
		
	}
	
	public void setTbInvItemCategory(String TB_INV_ITM_CTGRY) {
		
		this.TB_INV_ITM_CTGRY = TB_INV_ITM_CTGRY;
		
	}
	
	public double getTbUnitCost() {
		
		return TB_UNT_CST;
		
	}
	
	public void setTbUnitCost(double TB_UNT_CST) {
		
		this.TB_UNT_CST = TB_UNT_CST;
		
	}
	
	public double getTbTotalCost() {
		
		return TB_TTL_CST;
		
	}
	
	public void setTbTotalCost(double TB_TTL_CST) {
		
		this.TB_TTL_CST = TB_TTL_CST;
		
	}
	
	
	public double getTbQtyMonth() {
		
		return TB_QTY_MNTH;
		
	}
	
	public void setTbQtyMonth(double TB_QTY_MNTH) {
		
		this.TB_QTY_MNTH = TB_QTY_MNTH;
		
	}
	
	public double getTbQtyJan() {
		
		return TB_QTY_JAN;
		
	}
	
	public void setTbQtyJan(double TB_QTY_JAN) {
		
		this.TB_QTY_JAN = TB_QTY_JAN;
		
	}
	
	public double getTbQtyFeb() {
		
		return TB_QTY_FEB;
		
	}
	
	public void setTbQtyFeb(double TB_QTY_FEB) {
		
		this.TB_QTY_FEB = TB_QTY_FEB;
		
	}
	
	public double getTbQtyMrch() {
		
		return TB_QTY_MRCH;
		
	}
	
	public void setTbQtyMrch(double TB_QTY_MRCH) {
		
		this.TB_QTY_MRCH = TB_QTY_MRCH;
		
	}
	
	public double getTbQtyAprl() {
		
		return TB_QTY_APRL;
		
	}
	
	public void setTbQtyAprl(double TB_QTY_APRL) {
		
		this.TB_QTY_APRL = TB_QTY_APRL;
		
	}
	
	public double getTbQtyMay() {
		
		return TB_QTY_MAY;
		
	}
	
	public void setTbQtyMay(double TB_QTY_MAY) {
		
		this.TB_QTY_MAY = TB_QTY_MAY;
		
	}
	
	public double getTbQtyJun() {
		
		return TB_QTY_JUN;
		
	}
	
	public void setTbQtyJun(double TB_QTY_JUN) {
		
		this.TB_QTY_JUN = TB_QTY_JUN;
		
	}
	
	public double getTbQtyJul() {
		
		return TB_QTY_JUL;
		
	}
	
	public void setTbQtyJul(double TB_QTY_JUL) {
		
		this.TB_QTY_JUL = TB_QTY_JUL;
		
	}
	
	public double getTbQtyAug() {
		
		return TB_QTY_AUG;
		
	}
	
	public void setTbQtyAug(double TB_QTY_AUG) {
		
		this.TB_QTY_AUG = TB_QTY_AUG;
		
	}
	
	public double getTbQtySep() {
		
		return TB_QTY_SEP;
		
	}
	
	public void setTbQtySep(double TB_QTY_SEP) {
		
		this.TB_QTY_SEP = TB_QTY_SEP;
		
	}
	
	public double getTbQtyOct() {
		
		return TB_QTY_OCT;
		
	}
	
	public void setTbQtyOct(double TB_QTY_OCT) {
		
		this.TB_QTY_OCT = TB_QTY_OCT;
		
	}
	
	public double getTbQtyNov() {
		
		return TB_QTY_NOV;
		
	}
	
	public void setTbQtyNov(double TB_QTY_NOV) {
		
		this.TB_QTY_NOV = TB_QTY_NOV;
		
	}
	
public double getTbQtyDec() {
		
		return TB_QTY_DEC;
		
	}
	
	public void setTbQtyDec(double TB_QTY_DEC) {
		
		this.TB_QTY_DEC = TB_QTY_DEC;
		
	}
}