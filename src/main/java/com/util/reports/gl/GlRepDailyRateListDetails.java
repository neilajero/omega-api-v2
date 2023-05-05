/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;

import java.util.Date;


public class GlRepDailyRateListDetails implements java.io.Serializable {
	
	private String DR_FR_FC_NM;
	private Date DR_FR_DT;
	private double DR_FR_X_USD;
	private double DR_INVRS_RT;
	
	public GlRepDailyRateListDetails() {
    }
	
	public String getDrFrFunctionalCurrencyName(){
		
		return DR_FR_FC_NM;
		
	}
	
	public void setDrFrFuncationalCurrrencyName(String DR_FR_FC_NM){
		
		this.DR_FR_FC_NM = DR_FR_FC_NM;
		
	}

	public Date getDrFrDate(){
		
		return DR_FR_DT;
		
	}
	
	public void setDrFrDate(Date DR_FR_DT){
		
		this.DR_FR_DT = DR_FR_DT;
		
	}
	
	public double getDrFrConversionToUSD() {
		
		return DR_FR_X_USD;
		
	}
	
	public void setDrFrConversionToUSD(double DR_FR_X_USD) {
		
		this.DR_FR_X_USD = DR_FR_X_USD;
		
	}

	public double getDrInverseRate() {
		
		return DR_INVRS_RT;
		
	}
	
	public void setDrInverseRate(double DR_INVRS_RT) {
		
		this.DR_INVRS_RT = DR_INVRS_RT;
		
	}
	
} // GlRepDailyRateListDetails