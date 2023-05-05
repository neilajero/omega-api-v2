/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.Comparator;

public class InvRepInventoryProfitabilityDetails implements java.io.Serializable {

	private String IP_ITM_NM; 
	private String IP_ITM_DESC;
	private String IP_UNT_MSR;
	private double IP_QTY_SLD;
	private double IP_UNT_PRC_CST;
	private double IP_AMNT;
	private String IP_TYP;
	private String IP_ITM_AD_LV_CTGRY;

	public InvRepInventoryProfitabilityDetails() {
    }
	
	public double getIpAmount() {
		
		return IP_AMNT;
	
	}
	
	public void setIpAmount(double IP_AMNT) {
	
		this.IP_AMNT = IP_AMNT;
	
	}
	
	public String getIpItemDescription() {
	
		return IP_ITM_DESC;
	
	}
	
	public void setIpItemDescription(String IP_ITM_DESC) {
	
		this.IP_ITM_DESC = IP_ITM_DESC;
	
	}
	
	public String getIpItemName() {
	
		return IP_ITM_NM;
	
	}
	
	public void setIpItemName(String IP_ITM_NM) {
	
		this.IP_ITM_NM = IP_ITM_NM;
	
	}
	
	public double getIpQuantitySold() {
	
		return IP_QTY_SLD;
	
	}
	
	public void setIpQuantitySold(double IP_QTY_SLD) {
	
		this.IP_QTY_SLD = IP_QTY_SLD;
	
	}
	
	public String getIpUnitOfMeasure() {
	
		return IP_UNT_MSR;
	
	}
	
	public void setIpUnitOfMeasure(String IP_UNT_MSR) {
	
		this.IP_UNT_MSR = IP_UNT_MSR;
	
	}
	
	public double getIpUnitPriceCost() {
	
		return IP_UNT_PRC_CST;
	
	}
	
	public void setIpUnitPriceCost(double IP_UNT_PRC_CST) {
	
		this.IP_UNT_PRC_CST = IP_UNT_PRC_CST;
	
	}
	
	public String getIpType() {
		
		return IP_TYP;

	}
	
	public void setIpType(String IP_TYP) {
		
		this.IP_TYP = IP_TYP;
		
	}

	public String getIpItemAdLvCategory() {

		return IP_ITM_AD_LV_CTGRY;

	}

	public void setIpItemAdLvCategory(String IP_ITM_AD_LV_CTGRY) {

		this.IP_ITM_AD_LV_CTGRY = IP_ITM_AD_LV_CTGRY;
	}
	
	public static Comparator sortByItemCategory = (r1, r2) -> {

        InvRepInventoryProfitabilityDetails itemCategory1 = (InvRepInventoryProfitabilityDetails) r1;
        InvRepInventoryProfitabilityDetails itemCategory2 = (InvRepInventoryProfitabilityDetails) r2;
        return itemCategory1.getIpItemAdLvCategory().compareTo(itemCategory2.getIpItemAdLvCategory());

    };
	
} // InvRepCostOfSaleDetails class