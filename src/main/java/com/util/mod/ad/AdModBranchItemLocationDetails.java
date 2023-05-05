/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;

import com.util.ad.AdBranchItemLocationDetails;

public class AdModBranchItemLocationDetails extends AdBranchItemLocationDetails
		implements java.io.Serializable, java.lang.Cloneable {

	public Object clone() throws CloneNotSupportedException {

		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw e;

		}
	}

	private Integer BIL_COA_BR_CODE;
	private String BIL_COA_BR_NM;
	private String BIL_COA_SLS_ACCNT_NMBR;
	private String BIL_COA_SLS_ACCNT_DESC;
	private String BIL_COA_INVNTRY_ACCNT_NMBR;
	private String BIL_COA_INVNTRY_ACCNT_DESC;
	private String BIL_COA_CST_OF_SLS_ACCNT_NMBR;
	private String BIL_COA_CST_OF_SLS_ACCNT_DESC;
	private String BIL_COA_WIP_ACCNT_NMBR;
	private String BIL_COA_WIP_ACCNT_DESC;
	private String BIL_COA_ACCRD_INVNTRY_ACCNT_NMBR;
	private String BIL_COA_ACCRD_INVNTRY_ACCNT_DESC;
	private String BIL_COA_SLS_RTRN_ACCNT_NMBR;
	private String BIL_COA_SLS_RTRN_ACCNT_DESC;
	
	private String BIL_COA_EXPNS_ACCNT_NMBR;
	private String BIL_COA_EXPNS_ACCNT_DESC;

	private String BIL_IL_II_NM;
	private String BIL_IL_II_DESC;
	private String BIL_IL_II_CLSS;
	private String BIL_IL_LOC_NM;
	private double BIL_QTY_ON_HND;
	private double BIL_QTY;

	private String BIL_LST_MDFD_BY;
	private java.util.Date BIL_DT_LST_MDFD;

	public AdModBranchItemLocationDetails() {
    }

	public Integer getBilBrCode() {

		return BIL_COA_BR_CODE;

	}

	public void setBilBrCode(Integer BIL_COA_BR_CODE) {

		this.BIL_COA_BR_CODE = BIL_COA_BR_CODE;

	}

	public String getBilBrName() {

		return BIL_COA_BR_NM;

	}

	public void setBilBrName(String BIL_COA_BR_NM) {

		this.BIL_COA_BR_NM = BIL_COA_BR_NM;

	}

	public String getBilCoaGlSalesAccountNumber() {

		return BIL_COA_SLS_ACCNT_NMBR;

	}

	public void setBilCoaGlSalesAccountNumber(String BIL_COA_SLS_ACCNT_NMBR) {

		this.BIL_COA_SLS_ACCNT_NMBR = BIL_COA_SLS_ACCNT_NMBR;

	}

	public String getBilCoaGlSalesAccountDescription() {

		return BIL_COA_SLS_ACCNT_DESC;

	}

	public void setBilCoaGlSalesAccountDescription(String BIL_COA_SLS_ACCNT_DESC) {

		this.BIL_COA_SLS_ACCNT_DESC = BIL_COA_SLS_ACCNT_DESC;

	}

	public String getBilCoaGlSalesReturnAccountNumber() {

		return BIL_COA_SLS_RTRN_ACCNT_NMBR;

	}

	public void setBilCoaGlSalesReturnAccountNumber(String BIL_COA_SLS_RTRN_ACCNT_NMBR) {

		this.BIL_COA_SLS_RTRN_ACCNT_NMBR = BIL_COA_SLS_RTRN_ACCNT_NMBR;

	}

	public String getBilCoaGlSalesReturnAccountDescription() {

		return BIL_COA_SLS_RTRN_ACCNT_DESC;

	}

	public void setBilCoaGlSalesReturnAccountDescription(String BIL_COA_SLS_RTRN_ACCNT_DESC) {

		this.BIL_COA_SLS_RTRN_ACCNT_DESC = BIL_COA_SLS_RTRN_ACCNT_DESC;

	}

	public String getBilCoaGlInventoryAccountNumber() {

		return BIL_COA_INVNTRY_ACCNT_NMBR;

	}

	public void setBilCoaGlInventoryAccountNumber(String BIL_COA_INVNTRY_ACCNT_NMBR) {

		this.BIL_COA_INVNTRY_ACCNT_NMBR = BIL_COA_INVNTRY_ACCNT_NMBR;

	}

	public String getBilCoaGlInventoryAccountDescription() {

		return BIL_COA_INVNTRY_ACCNT_DESC;

	}

	public void setBilCoaGlInventoryAccountDescription(String BIL_COA_INVNTRY_ACCNT_DESC) {

		this.BIL_COA_INVNTRY_ACCNT_DESC = BIL_COA_INVNTRY_ACCNT_DESC;

	}

	public String getBilCoaGlCostOfSalesAccountNumber() {

		return BIL_COA_CST_OF_SLS_ACCNT_NMBR;

	}

	public void setBilCoaGlCostOfSalesAccountNumber(String BIL_COA_CST_OF_SLS_ACCNT_NMBR) {

		this.BIL_COA_CST_OF_SLS_ACCNT_NMBR = BIL_COA_CST_OF_SLS_ACCNT_NMBR;

	}

	public String getBilCoaGlCostOfSalesAccountDescription() {

		return BIL_COA_CST_OF_SLS_ACCNT_DESC;

	}

	public void setBilCoaGlCostOfSalesAccountDescription(String BIL_COA_CST_OF_SLS_ACCNT_DESC) {

		this.BIL_COA_CST_OF_SLS_ACCNT_DESC = BIL_COA_CST_OF_SLS_ACCNT_DESC;

	}

	public String getBilCoaGlWipAccountNumber() {

		return BIL_COA_WIP_ACCNT_NMBR;

	}

	public void setBilCoaGlWipAccountNumber(String BIL_COA_WIP_ACCNT_NMBR) {

		this.BIL_COA_WIP_ACCNT_NMBR = BIL_COA_WIP_ACCNT_NMBR;

	}

	public String getBilCoaGlWipAccountDescription() {

		return BIL_COA_WIP_ACCNT_DESC;

	}

	public void setBilCoaGlWipAccountDescription(String BIL_COA_WIP_ACCNT_DESC) {

		this.BIL_COA_WIP_ACCNT_DESC = BIL_COA_WIP_ACCNT_DESC;

	}

	public String getBilCoaGlAccruedInventoryAccountNumber() {

		return BIL_COA_ACCRD_INVNTRY_ACCNT_NMBR;

	}

	public void setBilCoaGlAccruedInventoryAccountNumber(String BIL_COA_ACCRD_INVNTRY_ACCNT_NMBR) {

		this.BIL_COA_ACCRD_INVNTRY_ACCNT_NMBR = BIL_COA_ACCRD_INVNTRY_ACCNT_NMBR;

	}

	public String getBilCoaGlAccruedInventoryAccountDescription() {

		return BIL_COA_ACCRD_INVNTRY_ACCNT_DESC;

	}

	public void setBilCoaGlAccruedInventoryAccountDescription(String BIL_COA_ACCRD_INVNTRY_ACCNT_DESC) {

		this.BIL_COA_ACCRD_INVNTRY_ACCNT_DESC = BIL_COA_ACCRD_INVNTRY_ACCNT_DESC;

	}
	
	public String getBilCoaGlExpenseAccountNumber() {

		return BIL_COA_EXPNS_ACCNT_NMBR;

	}

	public void setBilCoaGlExpenseAccountNumber(String BIL_COA_EXPNS_ACCNT_NMBR) {

		this.BIL_COA_EXPNS_ACCNT_NMBR = BIL_COA_EXPNS_ACCNT_NMBR;

	}

	public String getBilCoaGlExpenseAccountDescription() {

		return BIL_COA_EXPNS_ACCNT_DESC;

	}

	public void setBilCoaGlExpenseAccountDescription(String BIL_COA_EXPNS_ACCNT_DESC) {

		this.BIL_COA_EXPNS_ACCNT_DESC = BIL_COA_EXPNS_ACCNT_DESC;

	}


	public String getBilIlIiName() {

		return BIL_IL_II_NM;

	}

	public void setBilIlIiName(String BIL_IL_II_NM) {

		this.BIL_IL_II_NM = BIL_IL_II_NM;

	}

	public String getBilIlIiDescription() {

		return BIL_IL_II_DESC;

	}

	public void setBilIlIiDescription(String BIL_IL_II_DESC) {

		this.BIL_IL_II_DESC = BIL_IL_II_DESC;

	}

	public String getBilIlIiClass() {

		return BIL_IL_II_CLSS;

	}

	public void setBilIlIiClass(String BIL_IL_II_CLSS) {

		this.BIL_IL_II_CLSS = BIL_IL_II_CLSS;

	}

	public String getBilIlLocName() {

		return BIL_IL_LOC_NM;

	}

	public void setBilIlLocName(String BIL_IL_LOC_NM) {

		this.BIL_IL_LOC_NM = BIL_IL_LOC_NM;

	}

	public double getBilQuantityOnHand() {

		return BIL_QTY_ON_HND;

	}

	public void setBilQuantityOnHand(double BIL_QTY_ON_HND) {

		this.BIL_QTY_ON_HND = BIL_QTY_ON_HND;

	}

	public double getBilQuantity() {

		return BIL_QTY;

	}

	public void setBilQuantity(double BIL_QTY) {

		this.BIL_QTY = BIL_QTY;

	}

	public String getBilLastModifiedBy() {

		return BIL_LST_MDFD_BY;

	}

	public void setBilLastModifiedBy(String BIL_LST_MDFD_BY) {

		this.BIL_LST_MDFD_BY = BIL_LST_MDFD_BY;

	}

	public java.util.Date getBilDateLastModified() {

		return BIL_DT_LST_MDFD;

	}

	public void setBilDateLastModified(java.util.Date BIL_DT_LST_MDFD) {

		this.BIL_DT_LST_MDFD = BIL_DT_LST_MDFD;
	}

} // AdModBranchItemLocationDetails class