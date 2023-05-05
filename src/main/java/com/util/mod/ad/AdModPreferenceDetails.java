/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;


import com.util.ad.AdPreferenceDetails;

public class AdModPreferenceDetails extends AdPreferenceDetails implements java.io.Serializable {
   
   private String PRF_WTC_NM;
   private String PRF_AP_GL_COA_ACCRD_VAT_ACCNT_NMBR;
   private String PRF_AP_GL_COA_ACCRD_VAT_ACCNT_DESC;
   private String PRF_AP_GL_COA_PTTY_CSH_ACCNT_NMBR;
   private String PRF_AP_GL_COA_PTTY_CSH_ACCNT_DESC;
   
   private String PRF_INV_GL_COA_POS_ADJSTMNT_ACCNT_NMBR;
   private String PRF_INV_GL_COA_POS_ADJSTMNT_ACCNT_DESC;
   
   private String PRF_MISC_GL_COA_POS_DSCNT_ACCNT_NMBR;
   private String PRF_MISC_GL_COA_POS_DSCNT_ACCNT_DESC;
   private String PRF_MISC_GL_COA_POS_GFT_CRTFCT_ACCNT_NMBR;
   private String PRF_MISC_GL_COA_POS_GFT_CRTFCT_ACCNT_DESC;
   private String PRF_MISC_GL_COA_POS_SRVC_CHRG_ACCNT_NMBR;
   private String PRF_MISC_GL_COA_POS_SRVC_CHRG_ACCNT_DESC;
   private String PRF_MISC_GL_COA_POS_DN_IN_CHRG_ACCNT_NMBR;
   private String PRF_MISC_GL_COA_POS_DN_IN_CHRG_ACCNT_DESC;
   
   private String PRF_AR_GL_COA_CSTMR_DPST_ACCNT_NMBR;
   private String PRF_AR_GL_COA_CSTMR_DPST_ACCNT_DESC;
   
   private String PRF_INV_GL_COA_VRNC_ACCNT_NMBR;
   private String PRF_INV_GL_COA_VRNC_ACCNT_DESC;
   
   private String PRF_INV_GL_COA_GNRL_ADJSTMNT_ACCNT_NMBR;
   private String PRF_INV_GL_COA_GNRL_ADJSTMNT_ACCNT_DESC;
   private String PRF_INV_GL_COA_ISSNC_ADJSTMNT_ACCNT_NMBR;
   private String PRF_INV_GL_COA_ISSNC_ADJSTMNT_ACCNT_DESC;
   private String PRF_INV_GL_COA_PRDCTN_ADJSTMNT_ACCNT_NMBR;
   private String PRF_INV_GL_COA_PRDCTN_ADJSTMNT_ACCNT_DESC;
   private String PRF_INV_GL_COA_WSTG_ADJSTMNT_ACCNT_NMBR;
   private String PRF_INV_GL_COA_WSTG_ADJSTMNT_ACCNT_DESC;
   
   private String PRF_INV_GL_COA_ADJSTMNT_RQST_ACCNT_NMBR;
   private String PRF_INV_GL_COA_ADJSTMNT_RQST_ACCNT_DESC;
   
   private String PRF_INV_GL_COA_SLS_TX_ACCNT_NMBR;
   private String PRF_INV_GL_COA_SLS_TX_ACCNT_DESC;
   
   private String PRF_INV_GL_COA_PRPRD_FD_TX_ACCNT_NMBR;
   private String PRF_INV_GL_COA_PRPRD_FD_TX_ACCNT_DESC;
   
   public AdModPreferenceDetails() {
   }

   public String getPrfWtcName() {
   	
   	   return PRF_WTC_NM;
   	
   }  
   
   public void setPrfWtcName(String PRF_WTC_NM) {
   	
   	  this.PRF_WTC_NM = PRF_WTC_NM;
   	
   }
   
   public String getPrfApGlCoaAccruedVatAccountNumber() {
   	
   		return PRF_AP_GL_COA_ACCRD_VAT_ACCNT_NMBR;
   	
   }
   
   public void setPrfApGlCoaAccruedVatAccountNumber(String PRF_AP_GL_COA_ACCRD_VAT_ACCNT_NBMR) {
   	
   		this.PRF_AP_GL_COA_ACCRD_VAT_ACCNT_NMBR = PRF_AP_GL_COA_ACCRD_VAT_ACCNT_NBMR;
   	
   }
   
   public String getPrfApGlCoaAccruedVatAccountDescription() {
   	
   		return PRF_AP_GL_COA_ACCRD_VAT_ACCNT_DESC;
   	
   }
   
   public void setPrfApGlCoaAccruedVatAccountDescription(String PRF_AP_GL_COA_ACCRD_VAT_ACCNT_DESC) {
   	
   		this.PRF_AP_GL_COA_ACCRD_VAT_ACCNT_DESC = PRF_AP_GL_COA_ACCRD_VAT_ACCNT_DESC;
   	
   }
   
   public String getPrfApGlCoaPettyCashAccountNumber() {
   	
   		return PRF_AP_GL_COA_PTTY_CSH_ACCNT_NMBR;
   	
   }
   
   public void setPrfApGlCoaPettyCashAccountNumber(String PRF_AP_GL_COA_PTTY_CSH_ACCNT_NMBR) {
   	
   		this.PRF_AP_GL_COA_PTTY_CSH_ACCNT_NMBR = PRF_AP_GL_COA_PTTY_CSH_ACCNT_NMBR;
   	
   }
   
   public String getPrfApGlCoaPettyCashAccountDescription() {
   	
   		return PRF_AP_GL_COA_PTTY_CSH_ACCNT_DESC;
   	
   }
   
   public void setPrfApGlCoaPettyCashAccountDescription(String PRF_AP_GL_COA_PTTY_CSH_ACCNT_DESC) {
   	
   		this.PRF_AP_GL_COA_PTTY_CSH_ACCNT_DESC = PRF_AP_GL_COA_PTTY_CSH_ACCNT_DESC;
   	
   }
   
   public String getPrfInvGlCoaPosAdjustmentAccountNumber() {
   	
   		return PRF_INV_GL_COA_POS_ADJSTMNT_ACCNT_NMBR;
   	
   }
   
   public void setPrfInvGlCoaPosAdjustmentAccountNumber(String PRF_INV_GL_COA_POS_ADJSTMNT_ACCNT_NMBR) {
   	
   		this.PRF_INV_GL_COA_POS_ADJSTMNT_ACCNT_NMBR = PRF_INV_GL_COA_POS_ADJSTMNT_ACCNT_NMBR;
   	
   }
   
   public String getPrfInvGlCoaPosAdjustmentAccountDescription() {
   	
   		return PRF_INV_GL_COA_POS_ADJSTMNT_ACCNT_DESC;
   	
   }
   
   public void setPrfInvGlCoaPosAdjustmentAccountDescription(String PRF_INV_GL_COA_POS_ADJSTMNT_ACCNT_DESC) {
   	
   		this.PRF_INV_GL_COA_POS_ADJSTMNT_ACCNT_DESC = PRF_INV_GL_COA_POS_ADJSTMNT_ACCNT_DESC;
   	
   }
   
   public String getPrfMiscGlCoaPosDiscountAccountNumber() {
   	
   		return PRF_MISC_GL_COA_POS_DSCNT_ACCNT_NMBR;
   	
   }
   
   public void setPrfMiscGlCoaPosDiscountAccountNumber(String PRF_MISC_GL_COA_POS_DSCNT_ACCNT_NMBR) {
   	
   		this.PRF_MISC_GL_COA_POS_DSCNT_ACCNT_NMBR = PRF_MISC_GL_COA_POS_DSCNT_ACCNT_NMBR;
   	
   }
   
   public String getPrfMiscGlCoaPosDiscountAccountDescription() {
   	
   		return PRF_MISC_GL_COA_POS_DSCNT_ACCNT_DESC;
   	
   }
   
   public void setPrfMiscGlCoaPosDiscountAccountDescription(String PRF_MISC_GL_COA_POS_DSCNT_ACCNT_DESC) {
   	
   		this.PRF_MISC_GL_COA_POS_DSCNT_ACCNT_DESC = PRF_MISC_GL_COA_POS_DSCNT_ACCNT_DESC;
   	
   }

   public String getPrfMiscGlCoaPosGiftCertificateAccountNumber() {
   	
   		return PRF_MISC_GL_COA_POS_GFT_CRTFCT_ACCNT_NMBR;
   	
   }
   
   public void setPrfMiscGlCoaPosGiftCertificateAccountNumber(String PRF_MISC_GL_COA_POS_GFT_CRTFCT_ACCNT_NMBR) {
   	
   		this.PRF_MISC_GL_COA_POS_GFT_CRTFCT_ACCNT_NMBR = PRF_MISC_GL_COA_POS_GFT_CRTFCT_ACCNT_NMBR;
   	
   }
   
   public String getPrfMiscGlCoaPosGiftCertificateAccountDescription() {
   	
   		return PRF_MISC_GL_COA_POS_GFT_CRTFCT_ACCNT_DESC;
   	
   }
   
   public void setPrfMiscGlCoaPosGiftCertificateAccountDescription(String PRF_MISC_GL_COA_POS_GFT_CRTFCT_ACCNT_DESC) {
   	
   		this.PRF_MISC_GL_COA_POS_GFT_CRTFCT_ACCNT_DESC = PRF_MISC_GL_COA_POS_GFT_CRTFCT_ACCNT_DESC;
   	
   }
   
   public String getPrfMiscGlCoaPosServiceChargeAccountNumber() {
   	
   		return PRF_MISC_GL_COA_POS_SRVC_CHRG_ACCNT_NMBR;
   	
   }
   
   public void setPrfMiscGlCoaPosServiceChargeAccountNumber(String PRF_MISC_GL_COA_POS_SRVC_CHRG_ACCNT_NMBR) {
   	
   		this.PRF_MISC_GL_COA_POS_SRVC_CHRG_ACCNT_NMBR = PRF_MISC_GL_COA_POS_SRVC_CHRG_ACCNT_NMBR;
   	
   }
   
   public String getPrfMiscGlCoaPosServiceChargeAccountDescription() {
   	
   		return PRF_MISC_GL_COA_POS_SRVC_CHRG_ACCNT_DESC;
   	
   }
   
   public void setPrfMiscGlCoaPosServiceChargeAccountDescription(String PRF_MISC_GL_COA_POS_SRVC_CHRG_ACCNT_DESC) {
   	
   		this.PRF_MISC_GL_COA_POS_SRVC_CHRG_ACCNT_DESC = PRF_MISC_GL_COA_POS_SRVC_CHRG_ACCNT_DESC;
   	
   }
   
   public String getPrfMiscGlCoaPosDineInChargeAccountNumber() {
   	
   		return PRF_MISC_GL_COA_POS_DN_IN_CHRG_ACCNT_NMBR;
   	
   }
   
   public void setPrfMiscGlCoaPosDineInChargeAccountNumber(String PRF_MISC_GL_COA_POS_DN_IN_CHRG_ACCNT_NMBR) {
   	
   		this.PRF_MISC_GL_COA_POS_DN_IN_CHRG_ACCNT_NMBR = PRF_MISC_GL_COA_POS_DN_IN_CHRG_ACCNT_NMBR;
   	
   }
   
   public String getPrfMiscGlCoaPosDineInChargeAccountDescription() {
   	
   		return PRF_MISC_GL_COA_POS_DN_IN_CHRG_ACCNT_DESC;
   	
   }
   
   public void setPrfMiscGlCoaPosDineInChargeAccountDescription(String PRF_MISC_GL_COA_POS_DN_IN_CHRG_ACCNT_DESC) {
   	
   		this.PRF_MISC_GL_COA_POS_DN_IN_CHRG_ACCNT_DESC = PRF_MISC_GL_COA_POS_DN_IN_CHRG_ACCNT_DESC;
   	
   }
   
   public String getPrfArGlCoaCustomerDepositAccountNumber() {
   	
   		return PRF_AR_GL_COA_CSTMR_DPST_ACCNT_NMBR;
   	
   }
   
   public void setPrfArGlCoaCustomerDepositAccountNumber(String PRF_AR_GL_COA_CSTMR_DPST_ACCNT_NMBR) {
   	
   		this.PRF_AR_GL_COA_CSTMR_DPST_ACCNT_NMBR = PRF_AR_GL_COA_CSTMR_DPST_ACCNT_NMBR;
   	
   }
   
   public String getPrfArGlCoaCustomerDepositAccountDescription() {
   	
   		return PRF_AR_GL_COA_CSTMR_DPST_ACCNT_DESC;
   	
   }
   
   public void setPrfArGlCoaCustomerDepositAccountDescription(String PRF_AR_GL_COA_CSTMR_DPST_ACCNT_DESC) {
   	
   		this.PRF_AR_GL_COA_CSTMR_DPST_ACCNT_DESC = PRF_AR_GL_COA_CSTMR_DPST_ACCNT_DESC;
   	
   }
   public String getPrfInvGlCoaVarianceAccountNumber() {
   	
   		return PRF_INV_GL_COA_VRNC_ACCNT_NMBR;
   	
   }
   
   public void setPrfInvGlCoaVarianceAccountNumber(String PRF_INV_GL_COA_VRNC_ACCNT_NMBR) {
   	
   		this.PRF_INV_GL_COA_VRNC_ACCNT_NMBR = PRF_INV_GL_COA_VRNC_ACCNT_NMBR;
   	
   }
   
   public String getPrfInvGlCoaVarianceAccountDescription() {
   	
   		return PRF_INV_GL_COA_VRNC_ACCNT_DESC;
   	
   }
   
   public void setPrfInvGlCoaVarianceAccountDescription(String PRF_INV_GL_COA_VRNC_ACCNT_DESC) {
   	
   		this.PRF_INV_GL_COA_VRNC_ACCNT_DESC = PRF_INV_GL_COA_VRNC_ACCNT_DESC;
   	
   }
   
  public String getPrfInvGlCoaGeneralAdjustmentAccountNumber() {
	   	
  		return PRF_INV_GL_COA_GNRL_ADJSTMNT_ACCNT_NMBR;
  	
  }
  
  public void setPrfInvGlCoaGeneralAdjustmentAccountNumber(String PRF_INV_GL_COA_GNRL_ADJSTMNT_ACCNT_NMBR) {
  	
  		this.PRF_INV_GL_COA_GNRL_ADJSTMNT_ACCNT_NMBR = PRF_INV_GL_COA_GNRL_ADJSTMNT_ACCNT_NMBR;
  	
  }
  
  public String getPrfInvGlCoaGeneralAdjustmentAccountDescription() {
  	
  		return PRF_INV_GL_COA_GNRL_ADJSTMNT_ACCNT_DESC;
  	
  }
  
  public void setPrfInvGlCoaGeneralAdjustmentAccountDescription(String PRF_INV_GL_COA_GNRL_ADJSTMNT_ACCNT_DESC) {
  	
  		this.PRF_INV_GL_COA_GNRL_ADJSTMNT_ACCNT_DESC = PRF_INV_GL_COA_GNRL_ADJSTMNT_ACCNT_DESC;
  	
  }
  
  public String getPrfInvGlCoaIssuanceAdjustmentAccountNumber() {
	   	
		return PRF_INV_GL_COA_ISSNC_ADJSTMNT_ACCNT_NMBR;
	
  }

  public void setPrfInvGlCoaIssuanceAdjustmentAccountNumber(String PRF_INV_GL_COA_ISSNC_ADJSTMNT_ACCNT_NMBR) {
	
		this.PRF_INV_GL_COA_ISSNC_ADJSTMNT_ACCNT_NMBR = PRF_INV_GL_COA_ISSNC_ADJSTMNT_ACCNT_NMBR;
	
  }

  public String getPrfInvGlCoaIssuanceAdjustmentAccountDescription() {
	
		return PRF_INV_GL_COA_ISSNC_ADJSTMNT_ACCNT_DESC;
	
  }

  public void setPrfInvGlCoaIssuanceAdjustmentAccountDescription(String PRF_INV_GL_COA_ISSNC_ADJSTMNT_ACCNT_DESC) {
	
		this.PRF_INV_GL_COA_ISSNC_ADJSTMNT_ACCNT_DESC = PRF_INV_GL_COA_ISSNC_ADJSTMNT_ACCNT_DESC;
	
  }
  
  public String getPrfInvGlCoaProductionAdjustmentAccountNumber() {
	   	
		return PRF_INV_GL_COA_PRDCTN_ADJSTMNT_ACCNT_NMBR;
	
  }

  public void setPrfInvGlCoaProductionAdjustmentAccountNumber(String PRF_INV_GL_COA_PRDCTN_ADJSTMNT_ACCNT_NMBR) {
	
		this.PRF_INV_GL_COA_PRDCTN_ADJSTMNT_ACCNT_NMBR = PRF_INV_GL_COA_PRDCTN_ADJSTMNT_ACCNT_NMBR;
	
  }

  public String getPrfInvGlCoaProductionAdjustmentAccountDescription() {
	
		return PRF_INV_GL_COA_PRDCTN_ADJSTMNT_ACCNT_DESC;
	
  }

  public void setPrfInvGlCoaProductionAdjustmentAccountDescription(String PRF_INV_GL_COA_PRDCTN_ADJSTMNT_ACCNT_DESC) {
	
		this.PRF_INV_GL_COA_PRDCTN_ADJSTMNT_ACCNT_DESC = PRF_INV_GL_COA_PRDCTN_ADJSTMNT_ACCNT_DESC;
	
  }
  
  public String getPrfInvGlCoaWastageAdjustmentAccountNumber() {
	   	
		return PRF_INV_GL_COA_WSTG_ADJSTMNT_ACCNT_NMBR;
	
  }

  public void setPrfInvGlCoaWastageAdjustmentAccountNumber(String PRF_INV_GL_COA_WSTG_ADJSTMNT_ACCNT_NMBR) {
	
		this.PRF_INV_GL_COA_WSTG_ADJSTMNT_ACCNT_NMBR = PRF_INV_GL_COA_WSTG_ADJSTMNT_ACCNT_NMBR;
	
  }

  public String getPrfInvGlCoaWastageAdjustmentAccountDescription() {
	
		return PRF_INV_GL_COA_WSTG_ADJSTMNT_ACCNT_DESC;
	
  }

  public void setPrfInvGlCoaWastageAdjustmentAccountDescription(String PRF_INV_GL_COA_WSTG_ADJSTMNT_ACCNT_DESC) {
	
		this.PRF_INV_GL_COA_WSTG_ADJSTMNT_ACCNT_DESC = PRF_INV_GL_COA_WSTG_ADJSTMNT_ACCNT_DESC;
	
  }
  
  public String getPrfInvGlCoaAdjustmentRequestAccountNumber() {
	   	
		return PRF_INV_GL_COA_ADJSTMNT_RQST_ACCNT_NMBR;
	
  }

  public void setPrfInvGlCoaAdjustmentRequestAccountNumber(String PRF_INV_GL_COA_ADJSTMNT_RQST_ACCNT_NMBR) {
	
		this.PRF_INV_GL_COA_ADJSTMNT_RQST_ACCNT_NMBR = PRF_INV_GL_COA_ADJSTMNT_RQST_ACCNT_NMBR;
	
  }

  public String getPrfInvGlCoaAdjustmentRequestAccountDescription() {
	
		return PRF_INV_GL_COA_ADJSTMNT_RQST_ACCNT_DESC;
	
  }

  public void setPrfInvGlCoaAdjustmentRequestAccountDescription(String PRF_INV_GL_COA_ADJSTMNT_RQST_ACCNT_DESC) {
	
		this.PRF_INV_GL_COA_ADJSTMNT_RQST_ACCNT_DESC = PRF_INV_GL_COA_ADJSTMNT_RQST_ACCNT_DESC;
	
  }
   
   public String getPrfInvGlCoaSalesTaxAccountNumber() {
	   	
  		return PRF_INV_GL_COA_SLS_TX_ACCNT_NMBR;
  	
  }
  
  public void setPrfInvGlCoaSalesTaxAccountNumber(String PRF_INV_GL_COA_SLS_TX_ACCNT_NMBR) {
  	
  		this.PRF_INV_GL_COA_SLS_TX_ACCNT_NMBR = PRF_INV_GL_COA_SLS_TX_ACCNT_NMBR;
  	
  }
  
  public String getPrfInvGlCoaSalesTaxAccountDescription() {
  	
  		return PRF_INV_GL_COA_SLS_TX_ACCNT_DESC;
  	
  }
  
  public void setPrfInvGlCoaSalesTaxAccountDescription(String PRF_INV_GL_COA_SLS_TX_ACCNT_DESC) {
  	
  		this.PRF_INV_GL_COA_SLS_TX_ACCNT_DESC = PRF_INV_GL_COA_SLS_TX_ACCNT_DESC;
  	
  }

  public String getPrfInvGlCoaPreparedFoodTaxAccountNumber() {

	  return PRF_INV_GL_COA_PRPRD_FD_TX_ACCNT_NMBR;

  }

  public void setPrfInvGlCoaPreparedFoodTaxAccountNumber(String PRF_INV_GL_COA_PRPRD_FD_TX_ACCNT_NMBR) {

	  this.PRF_INV_GL_COA_PRPRD_FD_TX_ACCNT_NMBR = PRF_INV_GL_COA_PRPRD_FD_TX_ACCNT_NMBR;

  }

  public String getPrfInvGlCoaPreparedFoodTaxAccountDescription() {

	  return PRF_INV_GL_COA_PRPRD_FD_TX_ACCNT_DESC;

  }

  public void setPrfInvGlCoaPreparedFoodTaxAccountDescription(String PRF_INV_GL_COA_PRPRD_FD_TX_ACCNT_DESC) {

	  this.PRF_INV_GL_COA_PRPRD_FD_TX_ACCNT_DESC = PRF_INV_GL_COA_PRPRD_FD_TX_ACCNT_DESC;

  }
   
} // AdModPreferenceDetails class