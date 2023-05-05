/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


import com.util.ad.AdBranchBankAccountDetails;

public class AdModBranchBankAccountDetails extends AdBranchBankAccountDetails implements java.io.Serializable{
	
	private Integer BBA_BR_BRNCH_CODE = null;
	private String BBA_BR_NAME = null;
	
	
	private String BBA_CSH_ACCNT_NMBR; 
	   private String BBA_BNK_CHRG_ACCNT_NMBR;
	   private String BBA_INTRST_ACCNT_NMBR;
	   private String BBA_ADJSTMNT_ACCNT_NMBR;
	   private String BBA_SLS_DSCNT_ACCNT_NMBR; 
	   private String BBA_ADVNC_ACCNT_NMBR; 
	
	private String BBA_COA_GL_CSH_ACCNT_DESC; 
	   private String BBA_COA_GL_BNK_CHRG_ACCNT_DESC;
	   private String BBA_COA_GL_INTRST_ACCNT_DESC;
	   private String BBA_COA_GL_ADJSTMNT_ACCNT_DESC;
	   private String BBA_COA_GL_SLS_DSCNT_ACCNT_DESC;
	   private String BBA_COA_GL_ADVNC_ACCNT_DESC;

	public AdModBranchBankAccountDetails() {
    }
	
	public Integer getBbaBranchCode() {
		
		return this.BBA_BR_BRNCH_CODE;
		
	}
	
	public void setBbaBranchCode(Integer BBA_BR_BRNCH_CODE) {
		
		this.BBA_BR_BRNCH_CODE = BBA_BR_BRNCH_CODE;
		
	}
	
	public String getBbaBranchName() {
		
		return this.BBA_BR_NAME;
		
	}
	
	public void setBbaBranchName(String BBA_BR_NAME) {
		
		this.BBA_BR_NAME = BBA_BR_NAME;
		
	}
	
	
	public String getBbaCashAccountNumber() {
	   	
	   	   return BBA_CSH_ACCNT_NMBR;
	   	
	   }
	   
	   public void setBbaCashAccountNumber(String BBA_CSH_ACCNT_NMBR) {
	   	
	   	   this.BBA_CSH_ACCNT_NMBR = BBA_CSH_ACCNT_NMBR;
	   	
	   }

	  

	   public String getBbaBankChargeAccountNumber() {
	   	
	   	   return BBA_BNK_CHRG_ACCNT_NMBR;
	   	
	   }
	   
	   public void setBbaBankChargeAccountNumber(String BBA_BNK_CHRG_ACCNT_NMBR) {
	   	
	   	   this.BBA_BNK_CHRG_ACCNT_NMBR = BBA_BNK_CHRG_ACCNT_NMBR;
	   	
	   }


	   
	   public String getBbaInterestAccountNumber() {
	   	
	   	   return BBA_INTRST_ACCNT_NMBR;
	   	
	   }
	   
	   public void setBbaInterestAccountNumber(String BBA_INTRST_ACCNT_NMBR) {
	   	
	   	   this.BBA_INTRST_ACCNT_NMBR = BBA_INTRST_ACCNT_NMBR;
	   	
	   }
	   
	   public String getBbaAdjustmentAccountNumber() {
	   	
	   	   return BBA_ADJSTMNT_ACCNT_NMBR;
	   	
	   }
	   
	   public void setBbaAdjustmentAccountNumber(String BBA_ADJSTMNT_ACCNT_NMBR) {
	   	
	   	   this.BBA_ADJSTMNT_ACCNT_NMBR = BBA_ADJSTMNT_ACCNT_NMBR;
	   	
	   }
	   
	   public String getBbaSalesDiscountAccountNumber() {
		   	
	   	   return BBA_SLS_DSCNT_ACCNT_NMBR;
	   	
	   }
	   
	   public void setBbaSalesDiscountAccountNumber(String BBA_SLS_DSCNT_ACCNT_NMBR) {
	   	
	   	   this.BBA_SLS_DSCNT_ACCNT_NMBR = BBA_SLS_DSCNT_ACCNT_NMBR;
	   	
	   }


	   
	   

	

	   public String getBbaCashAccountDescription() {
	   	
	   	   return BBA_COA_GL_CSH_ACCNT_DESC;
	   	
	   }
	   
	   public void setBbaCashAccountDescription(String BBA_COA_GL_CSH_ACCNT_DESC) {
	   	
	   	   this.BBA_COA_GL_CSH_ACCNT_DESC = BBA_COA_GL_CSH_ACCNT_DESC;
	   	
	   }

	
	   public String getBbaBankChargeAccountDescription() {
	   	
	   	   return BBA_COA_GL_BNK_CHRG_ACCNT_DESC;
	   	
	   }
	   
	   public void setBbaBankChargeAccountDescription(String BBA_COA_GL_BNK_CHRG_ACCNT_DESC) {
	   	
	   	   this.BBA_COA_GL_BNK_CHRG_ACCNT_DESC = BBA_COA_GL_BNK_CHRG_ACCNT_DESC;
	   	
	   }


	   
	   public String getBbaInterestAccountDescription() {
	   	
	   	   return BBA_COA_GL_INTRST_ACCNT_DESC;
	   	
	   }
	   
	   public void setBbaInterestAccountDescription(String BBA_COA_GL_INTRST_ACCNT_DESC) {
	   	
	   	   this.BBA_COA_GL_INTRST_ACCNT_DESC = BBA_COA_GL_INTRST_ACCNT_DESC;
	   	
	   }
	   
	   public String getBbaAdjustmentAccountDescription() {
	   	
	   	   return BBA_COA_GL_ADJSTMNT_ACCNT_DESC;
	   	
	   }
	   
	   public void setBbaAdjustmentAccountDescription(String BBA_COA_GL_ADJSTMNT_ACCNT_DESC) {
	   	
	   	   this.BBA_COA_GL_ADJSTMNT_ACCNT_DESC = BBA_COA_GL_ADJSTMNT_ACCNT_DESC;
	   	
	   }

	   
	   public String getBbaSalesDiscountAccountDescription() {
		   	
	   	   return BBA_COA_GL_SLS_DSCNT_ACCNT_DESC;
	   	
	   }
	   
	   public void setBbaSalesDiscountAccountDescription(String BBA_COA_GL_SLS_DSCNT_ACCNT_DESC) {
	   	
	   	   this.BBA_COA_GL_SLS_DSCNT_ACCNT_DESC = BBA_COA_GL_SLS_DSCNT_ACCNT_DESC;
	   	
	   }   
	   
	   
	   public String getBbaAdvanceAccountNumber() {
		   	
	   	   return BBA_ADVNC_ACCNT_NMBR;
	   	
	   }
	   
	   public void setBbaAdvanceAccountNumber(String BBA_ADVNC_ACCNT_NMBR) {
	   	
	   	   this.BBA_ADVNC_ACCNT_NMBR = BBA_ADVNC_ACCNT_NMBR;
	   	
	   }
	   
	   public String getBbaAdvanceAccountDescription() {
		   	
	   	   return BBA_COA_GL_ADVNC_ACCNT_DESC;
	   	
	   }
	   
	   public void setBbaAdvanceAccountDescription(String BBA_COA_GL_ADVNC_ACCNT_DESC) {
	   	
	   	   this.BBA_COA_GL_ADVNC_ACCNT_DESC = BBA_COA_GL_ADVNC_ACCNT_DESC;
	   	
	   }
	
	
	
	
}//AdModBranchCustomerDetails