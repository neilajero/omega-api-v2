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

public class InvRepStockTransferDetails implements java.io.Serializable {

	private Date ST_DT;
	private String ST_II_NM;
	private String ST_II_DESC;
	private String ST_II_CTGRY;
	private String ST_DCMNT_NMBR;
	private String ST_LCTN_TO;
	private String ST_LCTN_FRM;
	private String ST_UNT;
	private double ST_QTY;
	private double ST_UNT_CST;
	private double ST_AMNT;

	public InvRepStockTransferDetails() {
    }
	
	public Date getStDate() {
		
		return ST_DT;
		
	}
	
	public void setStDate(Date ST_DT) {
		
		this.ST_DT = ST_DT;
		
	}
	
	public String getStDocNumber() {
		
		return ST_DCMNT_NMBR;
		
	}
	
	public void setStDocNumber(String ST_DCMNT_NMBR) {
		
		this.ST_DCMNT_NMBR = ST_DCMNT_NMBR;
		
	}
	
	
	public String getStLocationTo() {
		
		return ST_LCTN_TO;
		
	}
	
	public void setStLocationTo(String ST_LCTN_TO) {
		
		this.ST_LCTN_TO = ST_LCTN_TO;
		
	}
	
	public double getStAmount() {
		
		return ST_AMNT;
	
	}
	
	public void setStAmount(double ST_AMNT) {
	
		this.ST_AMNT = ST_AMNT;
	
	}
	
	public String getStItemDescription() {
	
		return ST_II_DESC;
	
	}
	
	public void setStItemDescription(String ST_II_DESC) {
	
		this.ST_II_DESC = ST_II_DESC;
	
	}
	
	public String getStItemName() {
	
		return ST_II_NM;
	
	}
	
	public void setStItemName(String ST_II_NM) {
	
		this.ST_II_NM = ST_II_NM;
	
	}
	
	public String getStItemCategory() {
		
		return ST_II_CTGRY;
		
	}
	
	public void setStItemCategory(String ST_II_CTGRY) {
		
		this.ST_II_CTGRY = ST_II_CTGRY;
		
	}
	
	public double getStQuantity() {
	
		return ST_QTY;
	
	}
	
	public void setStQuantity(double ST_QTY) {
	
		this.ST_QTY = ST_QTY;
	
	}
	
	public String getStLocationFrom() {
	
		return ST_LCTN_FRM;
	
	}
	
	public void setStLocationFrom(String ST_LCTN_FRM) {
	
		this.ST_LCTN_FRM = ST_LCTN_FRM;
	
	}
	
	public String getStUnit() {
		
		return ST_UNT;
	
	}
	
	public void setStUnit(String ST_UNT) {
	
		this.ST_UNT = ST_UNT;
	
	}
	
	public double getStUnitCost() {
	
		return ST_UNT_CST;
	
	}
	
	public void setStUnitCost(double ST_UNT_CST) {
	
		this.ST_UNT_CST = ST_UNT_CST;
	
	}
	
    public static Comparator LocationToComparator = (STP, anotherSTP) -> {

        String STL_LCTN_TO1 = ((InvRepStockTransferDetails)STP).getStLocationTo();
        String STL_LCTN_TO2 = ((InvRepStockTransferDetails)anotherSTP).getStLocationTo();

        return STL_LCTN_TO1.compareTo(STL_LCTN_TO2);

    };
    
    public static Comparator LocationFromComparator = (STP, anotherSTP) -> {

        String STL_LCTN_FROM1 = ((InvRepStockTransferDetails)STP).getStLocationFrom();
        String STL_LCTN_FROM2 = ((InvRepStockTransferDetails)anotherSTP).getStLocationFrom();

        return STL_LCTN_FROM1.compareTo(STL_LCTN_FROM2);

    };

} // InvRepStockTranferDetails class