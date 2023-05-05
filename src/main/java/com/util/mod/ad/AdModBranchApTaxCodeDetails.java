/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


import com.util.ad.AdBranchApTaxCodeDetails;

public class AdModBranchApTaxCodeDetails extends AdBranchApTaxCodeDetails implements java.io.Serializable{
	
	private Integer BTC_BR_BRNCH_CODE = null;
	private String BTC_BR_NAME = null;
	
	
	private String BTC_TX_CD_ACCNT_NMBR; 
	private String BTC_COA_GL_TX_CD_DESC; 


	public AdModBranchApTaxCodeDetails() {
    }
	
	public Integer getBtcBranchCode() {
		
		return this.BTC_BR_BRNCH_CODE;
		
	}
	
	public void setBtcBranchCode(Integer BTC_BR_BRNCH_CODE) {
		
		this.BTC_BR_BRNCH_CODE = BTC_BR_BRNCH_CODE;
		
	}
	
	public String getBtcBranchName() {
		
		return this.BTC_BR_NAME;
		
	}
	
	public void setBtcBranchName(String BTC_BR_NAME) {
		
		this.BTC_BR_NAME = BTC_BR_NAME;
		
	}
	
	
	public String getBtcTaxCodeAccountNumber() {
	   	
	   	   return BTC_TX_CD_ACCNT_NMBR;
	   	
	   }
	   
	   public void setBtcTaxCodeAccountNumber(String BTC_TX_CD_ACCNT_NMBR) {
	   	
	   	   this.BTC_TX_CD_ACCNT_NMBR = BTC_TX_CD_ACCNT_NMBR;
	   	
	   }

	  

	  
	

	   public String getBtcTaxCodeAccountDescription() {
	   	
	   	   return BTC_COA_GL_TX_CD_DESC;
	   	
	   }
	   
	   public void setBtcTaxCodeAccountDescription(String BTC_COA_GL_TX_CD_DESC) {
	   	
	   	   this.BTC_COA_GL_TX_CD_DESC = BTC_COA_GL_TX_CD_DESC;
	   	
	   }

	
	 
	
	
	
}