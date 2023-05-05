/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

public class InvRepAdjustmentRegisterBalanceSheetDetails implements java.io.Serializable {

	private String IL_DATE;
	private Double IL_FRWRDD_BLNC;
	private Double IL_DLVRS;
	private String IL_DC_NMBR;
	private Double IL_ADJSTMNT_CST= 0d;
	private String IL_RFRNC_NMBR;
	private Double IL_WTHDRWLS= 0d;
	private Double IL_ENDNG_BLNC= 0d;

	
	

	
	public InvRepAdjustmentRegisterBalanceSheetDetails() {
    }

	public String getIlDate() {

		return IL_DATE;

	}

	public void setIlDate(String IL_DATE) {

		this.IL_DATE = IL_DATE;

	}


	public Double getIlForwardedBalance() {

		return IL_FRWRDD_BLNC;

	}

	public void setIlForwardedBalance(Double IL_FRWRDD_BLNC) {

		this.IL_FRWRDD_BLNC = IL_FRWRDD_BLNC;

	}
	
	public Double getIlDeliveries() {

		return IL_DLVRS;

	}

	public void setIlDeliveries(Double IL_DLVRS) {

		this.IL_DLVRS = IL_DLVRS;

	}

	public String getIlDocNumber() {

		return IL_DC_NMBR;

	}
	
	public void setIlDocNumber(String IL_DC_NMBR) {

		this.IL_DC_NMBR = IL_DC_NMBR;

	}
	
	public Double getIlAjustmentCost() {

		return IL_ADJSTMNT_CST;

	}
	
	public void setIlAjustmentCost(Double IL_ADJSTMNT_CST) {

		this.IL_ADJSTMNT_CST = IL_ADJSTMNT_CST;

	}
	
	public String getIlReferenceNumber() {

		return IL_RFRNC_NMBR;

	}
	
	public void setIlReferenceNumber(String IL_RFRNC_NMBR) {

		this.IL_RFRNC_NMBR = IL_RFRNC_NMBR;

	}

	public Double getIlWithdrawals() {

		return IL_WTHDRWLS;

	}
	
	public void setIlWithdrawals(Double IL_WTHDRWLS) {

		this.IL_WTHDRWLS = IL_WTHDRWLS;

	}
	
	public Double getIlEndingBalance() {

		return IL_ENDNG_BLNC;
		
	}
	
	public void setIlEndingBalance(Double IL_ENDNG_BLNC) {

		this.IL_ENDNG_BLNC = IL_ENDNG_BLNC;

	}
	
	
	
	

} // InvRepItemListDetails class