/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.inv;

import com.util.inv.InvItemLocationDetails;
import com.util.mod.ad.AdModBranchItemLocationDetails;

import java.util.ArrayList;


public class InvModItemLocationDetails extends InvItemLocationDetails implements java.io.Serializable {
	
	private String IL_II_NM;
	private String IL_II_DESC;
	private String IL_LOC_NM;
	private String IL_COA_SLS_ACCNT_NMBR;
	private String IL_COA_SLS_ACCNT_DESC;
	private String IL_COA_INVNTRY_ACCNT_NMBR;
	private String IL_COA_INVNTRY_ACCNT_DESC;
	private String IL_COA_CST_OF_SLS_ACCNT_NMBR;
	private String IL_COA_CST_OF_SLS_ACCNT_DESC;
	private String IL_COA_WIP_ACCNT_NMBR;
	private String IL_COA_WIP_ACCNT_DESC;
	private String IL_COA_ACCRD_INVNTRY_ACCNT_NMBR;
	private String IL_COA_ACCRD_INVNTRY_ACCNT_DESC;
	
	private String IL_COA_SLS_RTRN_ACCNT_NMBR;
	private String IL_COA_SLS_RTRN_ACCNT_DESC;
	
	private String IL_COA_EXPNS_ACCNT_NMBR;
	private String IL_COA_EXPNS_ACCNT_DESC;
	
	private String IL_AD_LV_CTGRY;
	
	private String IL_LST_MDFD_BY;
	private java.util.Date IL_DT_LST_MDFD;
	
	
	private final ArrayList brIlList = new ArrayList();
	
	public InvModItemLocationDetails () {
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
	
	public String getIlLocName() {
		
		return IL_LOC_NM;
		
	}
	
	public void setIlLocName(String IL_LOC_NM) {
		
		this.IL_LOC_NM = IL_LOC_NM;
		
	}	
	
	public String getIlCoaGlSalesAccountNumber() {
		
		return IL_COA_SLS_ACCNT_NMBR;
		
	}
	
	public void setIlCoaGlSalesAccountNumber(String IL_COA_SLS_ACCNT_NMBR) {
		
		this.IL_COA_SLS_ACCNT_NMBR = IL_COA_SLS_ACCNT_NMBR;
		
	}
	
	public String getIlCoaGlSalesAccountDescription() {
		
		return IL_COA_SLS_ACCNT_DESC;
		
	}
	
	public void setIlCoaGlSalesAccountDescription(String IL_COA_SLS_ACCNT_DESC) {
		
		this.IL_COA_SLS_ACCNT_DESC = IL_COA_SLS_ACCNT_DESC;
		
	}
	
	
public String getIlCoaGlSalesReturnAccountNumber() {
		
		return IL_COA_SLS_RTRN_ACCNT_NMBR;
		
	}
	
	public void setIlCoaGlSalesReturnAccountNumber(String IL_COA_SLS_RTRN_ACCNT_NMBR) {
		
		this.IL_COA_SLS_RTRN_ACCNT_NMBR = IL_COA_SLS_RTRN_ACCNT_NMBR;
		
	}
	
	public String getIlCoaGlSalesReturnAccountDescription() {
		
		return IL_COA_SLS_RTRN_ACCNT_DESC;
		
	}
	
	public void setIlCoaGlSalesReturnAccountDescription(String IL_COA_SLS_RTRN_ACCNT_DESC) {
		
		this.IL_COA_SLS_RTRN_ACCNT_DESC = IL_COA_SLS_RTRN_ACCNT_DESC;
		
	}
	
	public String getIlCoaGlInventoryAccountNumber() {
		
		return IL_COA_INVNTRY_ACCNT_NMBR;
		
	}
	
	public void setIlCoaGlInventoryAccountNumber(String IL_COA_INVNTRY_ACCNT_NMBR) {
		
		this.IL_COA_INVNTRY_ACCNT_NMBR = IL_COA_INVNTRY_ACCNT_NMBR;
		
	}
	
	public String getIlCoaGlInventoryAccountDescription() {
		
		return IL_COA_INVNTRY_ACCNT_DESC;
		
	}
	
	public void setIlCoaGlInventoryAccountDescription(String IL_COA_INVNTRY_ACCNT_DESC) {
		
		this.IL_COA_INVNTRY_ACCNT_DESC = IL_COA_INVNTRY_ACCNT_DESC;
		
	}	
	
	public String getIlCoaGlCostOfSalesAccountNumber() {
		
		return IL_COA_CST_OF_SLS_ACCNT_NMBR;
		
	}
	
	public void setIlCoaGlCostOfSalesAccountNumber(String IL_COA_CST_OF_SLS_ACCNT_NMBR) {
		
		this.IL_COA_CST_OF_SLS_ACCNT_NMBR = IL_COA_CST_OF_SLS_ACCNT_NMBR;
		
	}
	
	public String getIlCoaGlCostOfSalesAccountDescription() {
		
		return IL_COA_CST_OF_SLS_ACCNT_DESC;
		
	}
	
	public void setIlCoaGlCostOfSalesAccountDescription(String IL_COA_CST_OF_SLS_ACCNT_DESC) {
		
		this.IL_COA_CST_OF_SLS_ACCNT_DESC = IL_COA_CST_OF_SLS_ACCNT_DESC;
		
	}	
	
	
	public String getIlCoaGlWipAccountNumber() {
		
		return IL_COA_WIP_ACCNT_NMBR;
		
	}
	
	public void setIlCoaGlWipAccountNumber(String IL_COA_WIP_ACCNT_NMBR) {
		
		this.IL_COA_WIP_ACCNT_NMBR = IL_COA_WIP_ACCNT_NMBR;
		
	}
	
	public String getIlCoaGlWipAccountDescription() {
		
		return IL_COA_WIP_ACCNT_DESC;
		
	}
	
	public void setIlCoaGlWipAccountDescription(String IL_COA_WIP_ACCNT_DESC) {
		
		this.IL_COA_WIP_ACCNT_DESC = IL_COA_WIP_ACCNT_DESC;
		
	}
	
	public String getIlCoaGlAccruedInventoryAccountNumber() {
		
		return IL_COA_ACCRD_INVNTRY_ACCNT_NMBR;
		
	}
	
	public void setIlCoaGlAccruedInventoryAccountNumber(String IL_COA_ACCRD_INVNTRY_ACCNT_NMBR) {
		
		this.IL_COA_ACCRD_INVNTRY_ACCNT_NMBR = IL_COA_ACCRD_INVNTRY_ACCNT_NMBR;
		
	}
	
	public String getIlCoaGlAccruedInventoryAccountDescription() {
		
		return IL_COA_ACCRD_INVNTRY_ACCNT_DESC;
		
	}
	
	public void setIlCoaGlAccruedInventoryAccountDescription(String IL_COA_ACCRD_INVNTRY_ACCNT_DESC) {
		
		this.IL_COA_ACCRD_INVNTRY_ACCNT_DESC = IL_COA_ACCRD_INVNTRY_ACCNT_DESC;
		
	}
	
	public String getIlCoaGlExpenseAccountNumber() {
		
		return IL_COA_EXPNS_ACCNT_NMBR;
		
	}
	
	public void setIlCoaGlExpenseAccountNumber(String IL_COA_EXPNS_ACCNT_NMBR) {
		
		this.IL_COA_EXPNS_ACCNT_NMBR = IL_COA_EXPNS_ACCNT_NMBR;
		
	}
	
	public String getIlCoaGlExpenseAccountDescription() {
		
		return IL_COA_EXPNS_ACCNT_DESC;
		
	}
	
	public void setIlCoaGlExpenseAccountDescription(String IL_COA_EXPNS_ACCNT_DESC) {
		
		this.IL_COA_EXPNS_ACCNT_DESC = IL_COA_EXPNS_ACCNT_DESC;
		
	}	
	
	public Object[] getBrIlList(){
		
		return brIlList.toArray();
		
	}
	
	public AdModBranchItemLocationDetails getBrIlListByIndex(int index){
		
		return ((AdModBranchItemLocationDetails)brIlList.get(index));
		
	}
	
	public int getBrIlListSize(){
		
		return(brIlList.size());
		
	}
	
	public void saveBrIlList(Object newBrIlList){
		
		brIlList.add(newBrIlList);   	  
		
	}
	
	
	public String getIlLastModifiedBy() {
		
		return IL_LST_MDFD_BY;
		
	}
	
	public void setIlLastModifiedBy(String IL_LST_MDFD_BY) {
		
		this.IL_LST_MDFD_BY = IL_LST_MDFD_BY;
		
	}
	
	
	public java.util.Date getIlDateLastModified() {
		
		return IL_DT_LST_MDFD;
		
	}
	
	public void setIlDateLastModified(java.util.Date IL_DT_LST_MDFD) {
		
		this.IL_DT_LST_MDFD = IL_DT_LST_MDFD;
		
	}
	
	public String getIlAdLvCategory() {
		
		return IL_AD_LV_CTGRY;
		
	}
	
	public void setIlAdLvCategory(String IL_AD_LV_CTGRY) {
		
		this.IL_AD_LV_CTGRY = IL_AD_LV_CTGRY;
		
	}
	
	
} // InvModItemLocationDetails class   