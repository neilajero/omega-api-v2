/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ad;

import com.util.ad.AdBankAccountDetails;

import java.util.ArrayList;
import java.util.Date;

public class AdModBankAccountDetails extends AdBankAccountDetails implements java.io.Serializable {

   private String BA_BNK_NM; 
   
   private String BA_CSH_ACCNT; 
   private String BA_BNK_CHRG_ACCNT;
   private String BA_INTRST_ACCNT;
   private String BA_ADJSTMNT_ACCNT;
   private String BA_SLS_DSCNT; 
   private String BA_ADVNC_ACCNT; 
   
   private String BA_CLRNG_ACCNT; 
   private String BA_ON_ACCNT_RCPT;
   private String BA_UNPPLD_RCPT;
   private String BA_UNPPLD_CHK; 
   private String BA_CSH_DSCNT;  
   
   private String BA_COA_GL_CSH_ACCNT_DESC; 
   private String BA_COA_GL_BNK_CHRG_ACCNT_DESC;
   private String BA_COA_GL_INTRST_ACCNT_DESC;
   private String BA_COA_GL_ADJSTMNT_ACCNT_DESC;
   private String BA_COA_GL_SLS_DSCNT_DESC;
   private String BA_COA_GL_ADVNC_ACCNT_DESC;
   
   private String BA_COA_GL_ON_ACCNT_RCPT_DESC;
   private String BA_COA_GL_UNPPLD_RCPT_DESC; 
   private String BA_COA_GL_UNPPLD_CHK_DESC;
   private String BA_COA_GL_CLRNG_ACCNT_DESC; 
   private String BA_COA_GL_CSH_DSCNT_DESC;
   
   private String BA_FC_NM;

   private double BA_AVLBL_BLNC;
   
   private ArrayList bBaList;
   
   public AdModBankAccountDetails () {
   }

   public AdModBankAccountDetails (Integer BA_CODE, String BA_NM, String BA_DESC,
		String BA_ACCNT_TYP, String BA_ACCNT_NMBR, String BA_ACCNT_USE,
		Integer BA_COA_GL_CSH_ACCNT, Integer BA_COA_GL_ON_ACCNT_RCPT,
		Integer BA_COA_GL_UNPPLD_RCPT, Integer BA_COA_GL_UNPPLD_CHK, Integer BA_COA_GL_BNK_CHRG_ACCNT,
		Integer BA_COA_GL_CLRNG_ACCNT, Integer BA_COA_GL_INTRST_ACCNT,
		Integer BA_COA_GL_ADJSTMNT_ACCNT, Integer BA_COA_GL_CSH_DSCNT,
		Integer BA_COA_GL_SLS_DSCNT, 
		Integer BA_COA_GL_ADVNC_ACCNT,
		double BA_AVLBL_BLNC, double BA_FLT_BLNC,
		Date BA_LST_RCNCLD_DT, double BA_LST_RCNCLD_BLNC,
		String BA_NXT_CHK_NMBR, byte BA_ENBL, byte BA_ACCNT_NMBR_SHW, int BA_ACCNT_NMBR_TP,
         int BA_ACCNT_NMBR_LFT, byte BA_ACCNT_NM_SHW, int BA_ACCNT_NM_TP, int BA_ACCNT_NM_LFT,
         byte BA_NMBR_SHW, int BA_NMBR_TP, int BA_NMBR_LFT,
         byte BA_DT_SHW, int BA_DT_TP, int BA_DT_LFT,
         byte BA_PY_SHW, int BA_PY_TP, int BA_PY_LFT,
         byte BA_AMNT_SHW, int BA_AMNT_TP, int BA_AMNT_LFT,
         byte BA_WRD_AMNT_SHW, int BA_WRD_AMNT_TP, int BA_WRD_AMNT_LFT,
         byte BA_CRRNCY_SHW, int BA_CRRNCY_TP, int BA_CRRNCY_LFT,
         byte BA_ADDRSS_SHW, int BA_ADDRSS_TP, int BA_ADDRSS_LFT,
         byte BA_MM_SHW, int BA_MM_TP, int BA_MM_LFT, 
         byte BA_DC_NMBR_SHW, int BA_DC_NMBR_TP, int BA_DC_NMBR_LFT, int BA_FNT_SZ, String BA_FNT_STYL, byte BA_IS_CSH_ACCNT, String BA_BNK_NM, String BA_CSH_ACCNT, 
		String BA_ON_ACCNT_RCPT, String BA_UNPPLD_RCPT, String BA_UNPPLD_CHK, String BA_BNK_CHRG_ACCNT, 
		String BA_CLRNG_ACCNT, String BA_INTRST_ACCNT, 
		String BA_ADJSTMNT_ACCNT, String BA_CSH_DSCNT, String BA_SLS_DSCNT, String BA_ADVNC_ACCNT,
		String BA_COA_GL_CSH_ACCNT_DESC, String BA_COA_GL_ON_ACCNT_RCPT_DESC, 
		String BA_COA_GL_UNPPLD_RCPT_DESC, String BA_COA_GL_UNPPLD_CHK_DESC, String BA_COA_GL_BNK_CHRG_ACCNT_DESC, 
		String BA_COA_GL_CLRNG_ACCNT_DESC, String BA_COA_GL_INTRST_ACCNT_DESC,
		String BA_COA_GL_ADJSTMNT_ACCNT_DESC, String BA_COA_GL_CSH_DSCNT_DESC, 
		String BA_COA_GL_SLS_DSCNT_DESC, 
		String BA_COA_GL_ADVNC_ACCNT_DESC,
		String BA_FC_NM) {
         	
   
		
		this.BA_BNK_NM = BA_BNK_NM;
		this.BA_CSH_ACCNT = BA_CSH_ACCNT;
		this.BA_ON_ACCNT_RCPT = BA_ON_ACCNT_RCPT;
		this.BA_UNPPLD_RCPT = BA_UNPPLD_RCPT;
		this.BA_UNPPLD_CHK = BA_UNPPLD_CHK;
		this.BA_BNK_CHRG_ACCNT = BA_BNK_CHRG_ACCNT;
		this.BA_CLRNG_ACCNT = BA_CLRNG_ACCNT;
		this.BA_INTRST_ACCNT = BA_INTRST_ACCNT;
		this.BA_ADJSTMNT_ACCNT = BA_ADJSTMNT_ACCNT;
		this.BA_CSH_DSCNT = BA_CSH_DSCNT;
		this.BA_SLS_DSCNT = BA_SLS_DSCNT;	
		this.BA_ADVNC_ACCNT = BA_ADVNC_ACCNT;
		this.BA_COA_GL_CSH_ACCNT_DESC = BA_COA_GL_CSH_ACCNT_DESC;
		this.BA_COA_GL_ON_ACCNT_RCPT_DESC = BA_COA_GL_ON_ACCNT_RCPT_DESC;
		this.BA_COA_GL_UNPPLD_RCPT_DESC = BA_COA_GL_UNPPLD_RCPT_DESC;
		this.BA_COA_GL_UNPPLD_CHK_DESC = BA_COA_GL_UNPPLD_CHK_DESC;
		this.BA_COA_GL_BNK_CHRG_ACCNT_DESC = BA_COA_GL_BNK_CHRG_ACCNT_DESC;
		this.BA_COA_GL_CLRNG_ACCNT_DESC = BA_COA_GL_CLRNG_ACCNT_DESC;
		this.BA_COA_GL_INTRST_ACCNT_DESC = BA_COA_GL_INTRST_ACCNT_DESC;
		this.BA_COA_GL_ADJSTMNT_ACCNT_DESC = BA_COA_GL_ADJSTMNT_ACCNT_DESC;
		this.BA_COA_GL_CSH_DSCNT_DESC = BA_COA_GL_CSH_DSCNT_DESC;		
		this.BA_COA_GL_SLS_DSCNT_DESC = BA_COA_GL_SLS_DSCNT_DESC;
		this.BA_COA_GL_ADVNC_ACCNT_DESC = BA_COA_GL_ADVNC_ACCNT_DESC;
		
		this.BA_FC_NM = BA_FC_NM;

      
   }
   
   public String getBaBankName() {
   	
   	   return BA_BNK_NM;
   	
   }
   
   public void setBaBankName(String BA_BNK_NM) {
   	
   	   this.BA_BNK_NM = BA_BNK_NM;
   	
   }
   
   public String getBaCoaCashAccount() {
   	
   	   return BA_CSH_ACCNT;
   	
   }
   
   public void setBaCoaCashAccount(String BA_CSH_ACCNT) {
   	
   	   this.BA_CSH_ACCNT = BA_CSH_ACCNT;
   	
   }

   public String getBaCoaOnAccountReceipt() {
   	
   	   return BA_ON_ACCNT_RCPT;
   	
   }
   
   public void setBaCoaOnAccountReceipt(String BA_ON_ACCNT_RCPT) {
   	
   	   this.BA_ON_ACCNT_RCPT = BA_ON_ACCNT_RCPT;
   	
   }

   public String getBaCoaUnappliedReceipt() {
   	
   	   return BA_UNPPLD_RCPT;
   	
   }
   
   public void setBaCoaUnappliedReceipt(String BA_UNPPLD_RCPT) {
   	
   	   this.BA_UNPPLD_RCPT = BA_UNPPLD_RCPT;
   	
   }
   
   public String getBaCoaUnappliedCheck() {
   	
   	   return BA_UNPPLD_CHK;
   	
   }
   
   public void setBaCoaUnappliedCheck(String BA_UNPPLD_CHK) {
   	
   	   this.BA_UNPPLD_CHK = BA_UNPPLD_CHK;
   	
   }   

   public String getBaCoaBankChargeAccount() {
   	
   	   return BA_BNK_CHRG_ACCNT;
   	
   }
   
   public void setBaCoaBankChargeAccount(String BA_BNK_CHRG_ACCNT) {
   	
   	   this.BA_BNK_CHRG_ACCNT = BA_BNK_CHRG_ACCNT;
   	
   }

   public String getBaCoaClearingAccount() {
   	
   	   return BA_CLRNG_ACCNT;
   	
   }
   
   public void setBaCoaClearingAccount(String BA_CLRNG_ACCNT) {
   	
   	   this.BA_CLRNG_ACCNT = BA_CLRNG_ACCNT;
   	
   }
   
   public String getBaCoaInterestAccount() {
   	
   	   return BA_INTRST_ACCNT;
   	
   }
   
   public void setBaCoaInterestAccount(String BA_INTRST_ACCNT) {
   	
   	   this.BA_INTRST_ACCNT = BA_INTRST_ACCNT;
   	
   }
   
   public String getBaCoaAdjustmentAccount() {
   	
   	   return BA_ADJSTMNT_ACCNT;
   	
   }
   
   public void setBaCoaAdjustmentAccount(String BA_ADJSTMNT_ACCNT) {
   	
   	   this.BA_ADJSTMNT_ACCNT = BA_ADJSTMNT_ACCNT;
   	
   }


   public String getBaCoaCashDiscount() {
   	
   	   return BA_CSH_DSCNT;
   	
   }
   
   public void setBaCoaCashDiscount(String BA_CSH_DSCNT) {
   	
   	   this.BA_CSH_DSCNT = BA_CSH_DSCNT;
   	
   }

   public String getBaCoaSalesDiscount() {
   	
   	   return BA_SLS_DSCNT;
   	
   }
   
   public void setBaCoaSalesDiscount(String BA_SLS_DSCNT) {
   	
   	   this.BA_SLS_DSCNT = BA_SLS_DSCNT;
   	
   }
   
   
   public String getBaCoaAdvanceAccount() {
	   	
   	   return BA_ADVNC_ACCNT;
   	
   }
   
   public void setBaCoaAdvanceAccount(String BA_ADVNC_ACCNT) {
   	
   	   this.BA_ADVNC_ACCNT = BA_ADVNC_ACCNT;
   	
   }

   public String getBaCashAccountDescription() {
   	
   	   return BA_COA_GL_CSH_ACCNT_DESC;
   	
   }
   
   public void setBaCashAccountDescription(String BA_COA_GL_CSH_ACCNT_DESC) {
   	
   	   this.BA_COA_GL_CSH_ACCNT_DESC = BA_COA_GL_CSH_ACCNT_DESC;
   	
   }

   public String getBaOnAccountReceiptDescription() {
   	
   	   return BA_COA_GL_ON_ACCNT_RCPT_DESC;
   	
   }
   
   public void setBaOnAccountReceiptDescription(String BA_COA_GL_ON_ACCNT_RCPT_DESC) {
   	
   	   this.BA_COA_GL_ON_ACCNT_RCPT_DESC = BA_COA_GL_ON_ACCNT_RCPT_DESC;
   	
   }

   public String getBaUnappliedReceiptDescription() {
   	
   	   return BA_COA_GL_UNPPLD_RCPT_DESC;
   	
   }
   
   public void setBaUnappliedReceiptDescription(String BA_COA_GL_UNPPLD_RCPT_DESC) {
   	
   	   this.BA_COA_GL_UNPPLD_RCPT_DESC = BA_COA_GL_UNPPLD_RCPT_DESC;
   	
   }
   
   public String getBaUnappliedCheckDescription() {
   	
   	   return BA_COA_GL_UNPPLD_CHK_DESC;
   	
   }
   
   public void setBaUnappliedCheckDescription(String BA_COA_GL_UNPPLD_CHK_DESC) {
   	
   	   this.BA_COA_GL_UNPPLD_CHK_DESC = BA_COA_GL_UNPPLD_CHK_DESC;
   	
   }   

   public String getBaBankChargeAccountDescription() {
   	
   	   return BA_COA_GL_BNK_CHRG_ACCNT_DESC;
   	
   }
   
   public void setBaBankChargeAccountDescription(String BA_COA_GL_BNK_CHRG_ACCNT_DESC) {
   	
   	   this.BA_COA_GL_BNK_CHRG_ACCNT_DESC = BA_COA_GL_BNK_CHRG_ACCNT_DESC;
   	
   }

   public String getBaClearingAccountDescription() {
   	
   	   return BA_COA_GL_CLRNG_ACCNT_DESC;
   	
   }
   
   public void setBaClearingAccountDescription(String BA_COA_GL_CLRNG_ACCNT_DESC) {
   	
   	   this.BA_COA_GL_CLRNG_ACCNT_DESC = BA_COA_GL_CLRNG_ACCNT_DESC;
   	
   }
   
   public String getBaInterestAccountDescription() {
   	
   	   return BA_COA_GL_INTRST_ACCNT_DESC;
   	
   }
   
   public void setBaInterestAccountDescription(String BA_COA_GL_INTRST_ACCNT_DESC) {
   	
   	   this.BA_COA_GL_INTRST_ACCNT_DESC = BA_COA_GL_INTRST_ACCNT_DESC;
   	
   }
   
   public String getBaAdjustmentAccountDescription() {
   	
   	   return BA_COA_GL_ADJSTMNT_ACCNT_DESC;
   	
   }
   
   public void setBaAdjustmentAccountDescription(String BA_COA_GL_ADJSTMNT_ACCNT_DESC) {
   	
   	   this.BA_COA_GL_ADJSTMNT_ACCNT_DESC = BA_COA_GL_ADJSTMNT_ACCNT_DESC;
   	
   }

   public String getBaCashDiscountDescription() {
   	
   	   return BA_COA_GL_CSH_DSCNT_DESC;
   	
   }
   
   public void setBaCashDiscountDescription(String BA_COA_GL_CSH_DSCNT_DESC) {
   	
   	   this.BA_COA_GL_CSH_DSCNT_DESC = BA_COA_GL_CSH_DSCNT_DESC;
   	
   }

   public String getBaSalesDiscountDescription() {
   	
   	   return BA_COA_GL_SLS_DSCNT_DESC;
   	
   }
   
   public void setBaSalesDiscountDescription(String BA_COA_GL_SLS_DSCNT_DESC) {
   	
   	   this.BA_COA_GL_SLS_DSCNT_DESC = BA_COA_GL_SLS_DSCNT_DESC;
   	
   }
   
   public String getBaAdvanceAccountDescription() {
	   	
   	   return BA_COA_GL_ADVNC_ACCNT_DESC;
   	
   }
   
   public void setBaAdvanceAccountDescription(String BA_COA_GL_ADVNC_ACCNT_DESC) {
   	
   	   this.BA_COA_GL_ADVNC_ACCNT_DESC = BA_COA_GL_ADVNC_ACCNT_DESC;
   	
   }
   
   public String getBaFcName() {
   	
   	   return BA_FC_NM;
   	
   }
   
   public void setBaFcName(String BA_FC_NM) {
   	
   	   this.BA_FC_NM = BA_FC_NM;
   	
   }
   
   public double getBaAvailableBalance() {
   	
   	   return BA_AVLBL_BLNC;
   	
   }
   
   public void setBaAvailableBalance(double BA_AVLBL_BLNC) {
   	
   	   this.BA_AVLBL_BLNC = BA_AVLBL_BLNC;
   	
   }
   
   public ArrayList getBbaList() {
       
       return bBaList;
       
   }
   
   public void setBbaList(ArrayList bBaList) {
       
       this.bBaList = bBaList;
       
   }
   	      	      	      	      	      	      	                 
} // AdModBankAccountDetails class   