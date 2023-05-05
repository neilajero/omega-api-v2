/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.inv;

import java.util.ArrayList;


public class InvRepMarkupListDetails implements java.io.Serializable {

	private String ML_II_NM;
	private String ML_II_DESC;
	private String ML_II_CLSS;
	private String ML_LOC_NM;
	private double ML_QTY;
	private String ML_UNT;
	private double ML_UNT_CST;
	private double ML_AMNT;
	private double ML_SLS_PRC;
	private double ML_AVE_CST;
	private String ML_II_PRT_NMBR;
	private ArrayList ML_PRC_LVLS;
	private double ML_MU_PCT;
	private double ML_SHPPNG_CST;
	private double ML_GRS_PFT;
	
	public InvRepMarkupListDetails() {
    }
	
	public String getMlItemName() {
		
		return ML_II_NM;
	
	}
	
	public void setMlItemName(String ML_II_NM) {
	
		this.ML_II_NM = ML_II_NM;
	
	}
	
	public String getMlItemDescription() {
		
		return ML_II_DESC;
	
	}
	
	public void setMlItemDescription(String ML_II_DESC) {
	
		this.ML_II_DESC = ML_II_DESC;
	
	}
	
	public String getMlItemClass() {
		
		return ML_II_CLSS;
		
	}
	
	public void setMlItemClass(String ML_II_CLSS) {
		
		this.ML_II_CLSS = ML_II_CLSS;
		
	}

	public String getMlLocation() {
		
		return ML_LOC_NM;
	
	}
	
	public void setMlLocation(String ML_LOC_NM) {
	
		this.ML_LOC_NM = ML_LOC_NM;
	
	}
	
	public double getMlQuantity() {
	
		return ML_QTY;
	
	}
	
	public void setMlQuantity(double ML_QTY) {
	
		this.ML_QTY = ML_QTY;
	
	}
		
	public String getMlUnit() {
		
		return ML_UNT;
	
	}
	
	public void setMlUnit(String ML_UNT) {
	
		this.ML_UNT = ML_UNT;
	
	}
	
	public double getMlUnitCost() {
		
		return ML_UNT_CST;
	
	}
	
	public void setMlUnitCost(double ML_UNT_CST) {
	
		this.ML_UNT_CST = ML_UNT_CST;
	
	}
	
	public double getMlAmount() {
		
		return ML_AMNT;
	
	}
	
	public void setMlAmount(double ML_AMNT) {
	
		this.ML_AMNT = ML_AMNT;
	
	}
	
	public double getMlSalesPrice() {
		
		return ML_SLS_PRC;
		
	}
	
	public void setMlSalesPrice(double ML_SLS_PRC) {
		
		this.ML_SLS_PRC = ML_SLS_PRC;
		
	}
	
	public double getMlAverageCost() {
		
		return ML_AVE_CST;
		
	}
	
	public void setMlAverageCost(double ML_AVE_CST) {
		
		this.ML_AVE_CST = ML_AVE_CST;
		
	}
	
	public String getMlIiPartNumber() {
		
		return ML_II_PRT_NMBR;
		
	}
	
	public void setMlIiPartNumber(String ML_II_PRT_NMBR) {
		
		this.ML_II_PRT_NMBR = ML_II_PRT_NMBR;
		
	}
	
	public ArrayList getMlPriceLevels(){
		
		return ML_PRC_LVLS;
		
	}
	
	public void setMlPriceLevels(ArrayList ML_PRC_LVLS){
	
		this.ML_PRC_LVLS = ML_PRC_LVLS;
		
	}
	
	public double getMlMarkupPercent(){
		
		return ML_MU_PCT;
		
	}
	
	public void setMlMarkupPercent(double ML_MU_PCT){
		
		this.ML_MU_PCT = ML_MU_PCT;
		
	}

	public double getMlShippingCost(){
		
		return ML_SHPPNG_CST;
		
	}
	
	public void setMlShippingCost(double ML_SHPPNG_CST){
		
		this.ML_SHPPNG_CST = ML_SHPPNG_CST;
		
	}

	public double getMlGrossProfit(){
		
		return ML_GRS_PFT;
		
	}
	
	public void setMlGrossProfit(double ML_GRS_PFT){
		
		this.ML_GRS_PFT = ML_GRS_PFT;
		
	}
	
} // InvRepMarkupListDetaMLs class