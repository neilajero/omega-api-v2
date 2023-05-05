/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.ad;


public class AdRepBankAccountListDetails implements java.io.Serializable {

   private String BAL_BNK_NM;	
   private String BAL_BA_NM;
   private String BAL_BA_DESC;
   private String BAL_BA_ACCNT_TYP;
   private String BAL_BA_ACCNT_NMBR;
   private String BAL_BA_ACCNT_USE;
   private double BAL_BA_AVLBL_BLNC;
   private byte BAL_BA_ENBL; 
   
   // account numbers and description
   private String BAL_BA_CSH_ACCNT;  
   private String BAL_BA_CSH_ACCNT_DESC;
   private String BAL_BA_ADJSTMNT_ACCNT;  
   private String BAL_BA_ADJSTMNT_ACCNT_DESC;  
   private String BAL_BA_INTRST_ACCNT;  
   private String BAL_BA_INTRST_ACCNT_DESC; 
   private String BAL_BA_BNK_CHRG_ACCNT;  
   private String BAL_BA_BNK_CHRG_ACCNT_DESC; 
   private String BAL_BA_SLS_DSCNT;  
   private String BAL_BA_SLS_DSCNT_DESC;

   public AdRepBankAccountListDetails() {
   }

   public double getBalBaAvailableBalance() {
   	
   	return BAL_BA_AVLBL_BLNC;
   
   }
   public void setBalBaAvailableBalance(double BAL_BA_AVLBL_BLNC) {
   
   	this.BAL_BA_AVLBL_BLNC = BAL_BA_AVLBL_BLNC;
   
   }
   
   public String getBalBaAccountNumber() {
   
   	return BAL_BA_ACCNT_NMBR;
   
   }
   
   public void setBalBaAccountNumber(String BAL_BA_ACCNT_NMBR) {
   
   	this.BAL_BA_ACCNT_NMBR = BAL_BA_ACCNT_NMBR;
   
   }
   
   public String getBalBaAccountType() {
   
   	return BAL_BA_ACCNT_TYP;
   
   }
   
   public void setBalBaAccountType(String BAL_BA_ACCNT_TYP) {
   
   	this.BAL_BA_ACCNT_TYP = BAL_BA_ACCNT_TYP;
   
   }
   
   public String getBalBaAccountUse() {
   
   	return BAL_BA_ACCNT_USE;
   
   }
   
   public void setBalBaAccountUse(String BAL_BA_ACCNT_USE) {
   
   	this.BAL_BA_ACCNT_USE = BAL_BA_ACCNT_USE;
   
   }
   
   public String getBalBaAdjustmentAccount() {
   
   	return BAL_BA_ADJSTMNT_ACCNT;
   
   }
   
   public void setBalBaAdjustmentAccount(String BAL_BA_ADJSTMNT_ACCNT) {
   
   	this.BAL_BA_ADJSTMNT_ACCNT = BAL_BA_ADJSTMNT_ACCNT;
   
   }
   
   public String getBalBaAdjustmentAccountDescription() {
   
   	return BAL_BA_ADJSTMNT_ACCNT_DESC;
   
   }
   
   public void setBalBaAdjustmentAccountDescription(String BAL_BA_ADJSTMNT_ACCNT_DESC) {
   
   	this.BAL_BA_ADJSTMNT_ACCNT_DESC = BAL_BA_ADJSTMNT_ACCNT_DESC;
   
   }
   
   public String getBalBaBankChargeAccount() {
   
   	return BAL_BA_BNK_CHRG_ACCNT;
   
   }
   
   public void setBalBaBankChargeAccount(String BAL_BA_BNK_CHRG_ACCNT) {
   
   	this.BAL_BA_BNK_CHRG_ACCNT = BAL_BA_BNK_CHRG_ACCNT;
   
   }
   
   public String getBalBaBankChargeAccountDescription() {
   
   	return BAL_BA_BNK_CHRG_ACCNT_DESC;
   
   }
   
   public void setBalBaBankChargeAccountDescription(String BAL_BA_BNK_CHRG_ACCNT_DESC) {
   
   	this.BAL_BA_BNK_CHRG_ACCNT_DESC = BAL_BA_BNK_CHRG_ACCNT_DESC;
   
   }
   
   public String getBalBaCashAccount() {
   
   	return BAL_BA_CSH_ACCNT;
   
   }
   
   public void setBalBaCashAccount(String BAL_BA_CSH_ACCNT) {
   
   	this.BAL_BA_CSH_ACCNT = BAL_BA_CSH_ACCNT;
   
   }
   
   public String getBalBaCashAccountDescription() {
   
   	return BAL_BA_CSH_ACCNT_DESC;
   
   }
   
   public void setBalBaCashAccountDescription(String BAL_BA_CSH_ACCNT_DESC) {
   
   	this.BAL_BA_CSH_ACCNT_DESC = BAL_BA_CSH_ACCNT_DESC;
   
   }
   
   public String getBalBaDescription() {
   	
   	return BAL_BA_DESC;
   
   }
   
   public void setBalBaDescription(String BAL_BA_DESC) {
   	
   	this.BAL_BA_DESC = BAL_BA_DESC;
   
   }
   
   public byte getBalBaEnable() {
   
   	return BAL_BA_ENBL;
   
   }
   
   public void setBalBaEnable(byte BAL_BA_ENBL) {
   
   	this.BAL_BA_ENBL = BAL_BA_ENBL;
   
   }
   
   public String getBalBaInterestAccount() {
   
   	return BAL_BA_INTRST_ACCNT;
   
   }
   
   public void setBalBaInterestAccount(String BAL_BA_INTRST_ACCNT) {
   
   	this.BAL_BA_INTRST_ACCNT = BAL_BA_INTRST_ACCNT;
   
   }
   
   public String getBalBaInterestAccountDescription() {
   
   	return BAL_BA_INTRST_ACCNT_DESC;
   
   }
   
   public void setBalBaInterestAccountDescription(String BAL_BA_INTRST_ACCNT_DESC) {
   
   	this.BAL_BA_INTRST_ACCNT_DESC = BAL_BA_INTRST_ACCNT_DESC;
   
   }
   
   public String getBalBaName() {
   
    return BAL_BA_NM;
   
   }
   
   public void setBalBaName(String BAL_BA_NM) {
   
   	this.BAL_BA_NM = BAL_BA_NM;
   
   }
   
   public String getBalBaSalesDiscount() {
   
   	return BAL_BA_SLS_DSCNT;
   
   }
   
   public void setBalBaSalesDiscount(String BAL_BA_SLS_DSCNT) {
   
   	this.BAL_BA_SLS_DSCNT = BAL_BA_SLS_DSCNT;
   
   }
   
   public String getBalBaSalesDiscountDescription() {
   
   	return BAL_BA_SLS_DSCNT_DESC;
   
   }
   
   public void setBalBaSalesDiscountDescription(String BAL_BA_SLS_DSCNT_DESC) {
   
   	this.BAL_BA_SLS_DSCNT_DESC = BAL_BA_SLS_DSCNT_DESC;
   
   }
   
   public String getBalBankName() {
    
    return BAL_BNK_NM;
    
   }
    
   public void setBalBankName(String BAL_BNK_NM) {
    
    this.BAL_BNK_NM = BAL_BNK_NM;
    
   }
   
} // AdRepBankAccountListDetails class