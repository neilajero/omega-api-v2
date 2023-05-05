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

public class InvRepItemLedgerDetails implements java.io.Serializable {

	private String RIL_II_NM;
	private String RIL_II_DESC;
	private String RIL_II_CTGRY;
	private String RIL_UNT;
	private Date RIL_DT;
	private String RIL_DCMNT_NMBR;
	private String RIL_RFRNC_NMBR;
	private String RIL_RFRNC_NMBR1;
	private String RIL_SRC;
	private String RIL_QC_NMBR;
	private String RIL_LOC_NM;
	private String RIL_SPL_NM;
	
	private double RIL_IN_QTY; 
	private double RIL_IN_UNT_CST;
	private double RIL_IN_AMNT;
	private double RIL_OUT_QTY; 
	private double RIL_OUT_UNT_CST;
	private double RIL_OUT_AMNT;
	private double RIL_RMNNG_QTY;
	private double RIL_RMNNG_UNT_CST;
	private double RIL_RMNNG_AMNT;
	private double RIL_BGNNNG_QTY; 
	private double RIL_BGNNNG_UNT_CST;
	private double RIL_BGNNNG_AMNT;
	private String RIL_ACCT;
	
	private String RIL_PRPRTY_CD;
	private String RIL_CSTDN_SRC;
	private String RIL_CSTDN;
		
    public InvRepItemLedgerDetails() {
    }

    
    public String getRilPropertyCode() {
    	
		return RIL_PRPRTY_CD;
	
	}
	
	public void setRilPropertyCode(String RIL_PRPRTY_CD) {
	
		this.RIL_PRPRTY_CD = RIL_PRPRTY_CD;
	
	}
	
	public String getRilCustodianSource() {
    	
		return RIL_CSTDN_SRC;
	
	}
	
	public void setRilCustodianSource(String RIL_CSTDN_SRC) {
	
		this.RIL_CSTDN_SRC = RIL_CSTDN_SRC;
	
	}
	
	public String getRilCustodian() {
    	
		return RIL_CSTDN;
	
	}
	
	public void setRilCustodian(String RIL_CSTDN) {
	
		this.RIL_CSTDN = RIL_CSTDN;
	
	}
	
	public String getRilItemName() {
	
		return RIL_II_NM;
	
	}
	
	public void setRilItemName(String RIL_II_NM) {
	
		this.RIL_II_NM = RIL_II_NM;
	
	}
	
	public String getRilItemDesc() {
		
		return RIL_II_DESC;
	
	}
	
	public void setRilItemDesc(String RIL_II_DESC) {
	
		this.RIL_II_DESC = RIL_II_DESC;
	
	}

	public String getRilItemCategory() {
	
		return RIL_II_CTGRY;
	
	}
	
	public void setRilItemCategory(String RIL_II_CTGRY) {
	
		this.RIL_II_CTGRY = RIL_II_CTGRY;
	
	}
	
	public String getRilUnit() {
		
		return RIL_UNT;
		
	}
		
	public void setRilUnit(String RIL_UNT) {
		
		this.RIL_UNT = RIL_UNT;
		
	}
	
	public Date getRilDate() {
	
		return RIL_DT;
	
	}
	
	public void setRilDate(Date RIL_DT) {
		
		this.RIL_DT = RIL_DT;
	
	}
	
	public String getRilDocumentNumber() {
		
		return RIL_DCMNT_NMBR;
	
	}
	
	public void setRilDocumentNumber(String RIL_DCMNT_NMBR) {
		
		this.RIL_DCMNT_NMBR = RIL_DCMNT_NMBR;
	
	}
	
	public String getRilReferenceNumber() {
		
		return RIL_RFRNC_NMBR;
	
	}
	
	public void setRilReferenceNumber(String RIL_RFRNC_NMBR) {
		
		this.RIL_RFRNC_NMBR = RIL_RFRNC_NMBR;
	
	}
	
	public String getRilReferenceNumber1() {
		
		return RIL_RFRNC_NMBR1;
	
	}
	
	public void setRilReferenceNumber1(String RIL_RFRNC_NMBR1) {
		
		this.RIL_RFRNC_NMBR1 = RIL_RFRNC_NMBR1;
	
	}
	
	public String getRilSource() {
		
		return RIL_SRC;
	
	}
	
	public void setRilSource(String RIL_SRC) {
		
		this.RIL_SRC = RIL_SRC;
	
	}
	
	public String getRilQcNumber() {
		
		return RIL_QC_NMBR;
	
	}
	
	public void setRilQcNumber(String RIL_QC_NMBR) {
		
		this.RIL_QC_NMBR = RIL_QC_NMBR;
	
	}
	
	public String getRilLocationName() {
		
		return RIL_LOC_NM;
	
	}
	
	public void setRilLocationName(String RIL_LOC_NM) {
		
		this.RIL_LOC_NM = RIL_LOC_NM;
	
	}
	
	public String getRilSupplierName() {
		
		return RIL_SPL_NM;
	
	}
	
	public void setRilSupplierName(String RIL_SPL_NM) {
		
		this.RIL_SPL_NM = RIL_SPL_NM;
	
	}
	
	public double getRilInQuantity() {
		
		return RIL_IN_QTY;
				
	}
	
	public void setRilInQuantity(double RIL_IN_QTY) {
		
		this.RIL_IN_QTY = RIL_IN_QTY;
		
	}
	
	public double getRilInUnitCost() {
		
		return RIL_IN_UNT_CST;
				
	}
	
	public void setRilInUnitCost(double RIL_IN_UNT_CST) {
		
		this.RIL_IN_UNT_CST = RIL_IN_UNT_CST;
		
	}
	
	public double getRilInAmount() {
		
		return RIL_IN_AMNT;
				
	}
	
	public void setRilInAmount(double RIL_IN_AMNT) {
		
		this.RIL_IN_AMNT = RIL_IN_AMNT;
		
	}
	
	
	public double getRilOutQuantity() {
		
		return RIL_OUT_QTY;
				
	}
	
	public void setRilOutQuantity(double RIL_OUT_QTY) {
		
		this.RIL_OUT_QTY = RIL_OUT_QTY;
		
	}
	
	public double getRilOutUnitCost() {
		
		return RIL_OUT_UNT_CST;
				
	}
	
	public void setRilOutUnitCost(double RIL_OUT_UNT_CST) {
		
		this.RIL_OUT_UNT_CST = RIL_OUT_UNT_CST;
		
	}
	
	public double getRilOutAmount() {
		
		return RIL_OUT_AMNT;
				
	}
	
	public void setRilOutAmount(double RIL_OUT_AMNT) {
		
		this.RIL_OUT_AMNT = RIL_OUT_AMNT;
		
	}
		
	public double getRilRemainingQuantity() {
		
		return RIL_RMNNG_QTY;
				
	}
	
	public void setRilRemainingQuantity(double RIL_RMNNG_QTY) {
		
		this.RIL_RMNNG_QTY = RIL_RMNNG_QTY;
		
	}
	
	public double getRilRemainingUnitCost() {
		
		return RIL_RMNNG_UNT_CST;
				
	}
	
	public void setRilRemainingUnitCost(double RIL_RMNNG_UNT_CST) {
		
		this.RIL_RMNNG_UNT_CST = RIL_RMNNG_UNT_CST;
		
	}
	
	public double getRilRemainingAmount() {
		
		return RIL_RMNNG_AMNT;
				
	}
	
	public void setRilRemainingAmount(double RIL_RMNNG_AMNT) {
		
		this.RIL_RMNNG_AMNT = RIL_RMNNG_AMNT;
		
	}
	
	public double getRilBeginningQuantity() {
		
		return RIL_BGNNNG_QTY;
				
	}
	
	public void setRilBeginningQuantity(double RIL_BGNNNG_QTY) {
		
		this.RIL_BGNNNG_QTY = RIL_BGNNNG_QTY;
		
	}
	
	public double getRilBeginningUnitCost() {
		
		return RIL_BGNNNG_UNT_CST;
				
	}
	
	public void setRilBeginningUnitCost(double RIL_BGNNNG_UNT_CST) {
		
		this.RIL_BGNNNG_UNT_CST = RIL_BGNNNG_UNT_CST;
		
	}
	
	public double getRilBeginningAmount() {
		
		return RIL_BGNNNG_AMNT;
				
	}
	
	public void setRilBeginningAmount(double RIL_BGNNNG_AMNT) {
		
		this.RIL_BGNNNG_AMNT = RIL_BGNNNG_AMNT;
		
	}
	
	public String getRilAccount() {
		
		return RIL_ACCT;
				
	}
	
	public void setRilAccount(String RIL_ACCT) {
		
		this.RIL_ACCT = RIL_ACCT;
		
	}
	
	public static Comparator ItemLedgerComparator = (RIL, anotherRIL) -> {

        String RIL_II_CGTRY1 = ((InvRepItemLedgerDetails) RIL).getRilItemCategory();
        String RIL_II_NM1 = ((InvRepItemLedgerDetails) RIL).getRilItemName();
        Date RIL_DT1 = ((InvRepItemLedgerDetails) RIL).getRilDate();
        String RIL_DCMNT_NMBR1 = ((InvRepItemLedgerDetails) RIL).getRilDocumentNumber();

        String RIL_II_CGTRY2 = ((InvRepItemLedgerDetails) anotherRIL).getRilItemCategory();
        String RIL_II_NM2 = ((InvRepItemLedgerDetails) anotherRIL).getRilItemName();
        Date RIL_DT2 = ((InvRepItemLedgerDetails) anotherRIL).getRilDate();
        String RIL_DCMNT_NMBR2 = ((InvRepItemLedgerDetails) anotherRIL).getRilDocumentNumber();

        if (!(RIL_II_CGTRY1.equals(RIL_II_CGTRY2))) {

            return RIL_II_CGTRY1.compareTo(RIL_II_CGTRY2);

        } else if (!(RIL_II_NM1.equals(RIL_II_NM2))) {

            return RIL_II_NM1.compareTo(RIL_II_NM2);

        } else  {

            return RIL_DT1.compareTo(RIL_DT2);
        }
    };
		   
	
	
} // InvRepItemLedgerDetails class