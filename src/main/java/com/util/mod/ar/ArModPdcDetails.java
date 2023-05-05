/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.mod.ar;

import com.util.ar.ArPdcDetails;

import java.util.ArrayList;

public class ArModPdcDetails extends ArPdcDetails implements java.io.Serializable {

   private String PDC_FC_NM;
   private String PDC_TC_NM;
   private String PDC_WTC_NM;
   private String PDC_CST_CSTMR_CODE;
   private String PDC_CST_NM;
   private String PDC_CST_ADDRSS;
   private String PDC_CST_CTY;
   private String PDC_CST_CNTRY;
   private String PDC_CST_TIN;
   private String PDC_PYT_NM;
   private String PDC_TYP;
   private ArrayList pdcIlList;
   private ArrayList pdcIliList;
   private ArrayList pdcRctList;
   private String RCT_BA_NM;
   private String PDC_BNK_NM;
   private String PDC_INVC_NMBR;
   private String PDC_PYMNT_MTHD;
   private String PDC_AMNT_IN_WRDS;
   
   
   public ArModPdcDetails () {
   }

   public String getPdcFcName() {
   	
   	   return PDC_FC_NM;
   	
   }  
   
   public void setPdcFcName(String PDC_FC_NM) {
   	
   	  this.PDC_FC_NM = PDC_FC_NM;
   	
   }
   
   public String getPdcTcName() {
   	
   	   return PDC_TC_NM;
   	
   }
   
   public void setPdcTcName(String PDC_TC_NM) {
   	
   	   this.PDC_TC_NM = PDC_TC_NM;
   	
   }
   
   public String getPdcWtcName() {
   	
   	   return PDC_WTC_NM;
   	
   }
   
   public void setPdcWtcName(String PDC_WTC_NM) {
   	
   	   this.PDC_WTC_NM = PDC_WTC_NM;
   	
   } 
   
   public String getPdcCstCustomerCode() {
   	
   	   return PDC_CST_CSTMR_CODE;
   	
   }
   
   public void setPdcCstCustomerCode(String PDC_CST_CSTMR_CODE) {
   	
   	   this.PDC_CST_CSTMR_CODE = PDC_CST_CSTMR_CODE;
   	
   }
   
   public String getPdcCstName() {
   	
   	   return PDC_CST_NM;
   	
   }
   
   public void setPdcCstName(String PDC_CST_NM) {
   	
   	   this.PDC_CST_NM = PDC_CST_NM;
   	
   }   

   public String getPdcCstAddress() {
   	
   	   return PDC_CST_ADDRSS;
   	
   }
   
   public void setPdcCstAddress(String PDC_CST_ADDRSS) {
   	
   	   this.PDC_CST_ADDRSS = PDC_CST_ADDRSS;
   	
   }
   
   public String getPdcCstCity() {
   	
   	   return PDC_CST_CTY;
   	
   }
   
   public void setPdcCstCity(String PDC_CST_CTY) {
   	
   	   this.PDC_CST_CTY = PDC_CST_CTY;
   	
   }
   
   public String getPdcCstCountry() {
   	
   	   return PDC_CST_CNTRY;
   	
   }
   
   public void setPdcCstCountry(String PDC_CST_CNTRY) {
   	
   	   this.PDC_CST_CNTRY = PDC_CST_CNTRY;
   	
   }
   
   public String getPdcCstTin() {
   	
   	   return PDC_CST_TIN;
   	
   }
   
   public void setPdcCstTin(String PDC_CST_TIN) {
   	
   	   this.PDC_CST_TIN = PDC_CST_TIN;
   	
   }
   
   
   public String getPdcPytName() {
   	
   	   return PDC_PYT_NM;
   	
   }
   
   public void setPdcPytName(String PDC_PYT_NM) {
   	
   	   this.PDC_PYT_NM = PDC_PYT_NM;
   	
   } 
   
   public String getPdcType() {
   	
   	   return PDC_TYP;
   	   
   }
   
   public void setPdcType(String PDC_TYP) {
   	
   	   this.PDC_TYP = PDC_TYP;
   	   
   }
   
   public ArrayList getPdcIlList() {
   	
   	   return pdcIlList;
   	
   }
   
   public void setPdcIlList(ArrayList pdcIlList) {
   	
   	   this.pdcIlList = pdcIlList;
   	
   }
   
   public ArrayList getPdcIliList() {
   	
   	   return pdcIliList;
   	
   }
   
   public void setPdcIliList(ArrayList pdcIliList) {
   	
   	   this.pdcIliList = pdcIliList;
   	
   }
   
   public ArrayList getPdcRctList() {
	   	
   	   return pdcRctList;
   	
   }
   
   public void setPdcRctList(ArrayList pdcRctList) {
   	
   	   this.pdcRctList = pdcRctList;
   	
   }
   
   public ArModAppliedInvoiceDetails getPdcRctListByIndex(int index){
	   	
   	  return ((ArModAppliedInvoiceDetails)pdcRctList.get(index));
   	
   }
   
   public int getPdcRctListSize(){
   	
   	  return(pdcRctList.size());
   	
   }
   
   public void savePdcRctList(Object newRctAiList){
   	
   	  pdcRctList.add(newRctAiList);   	  
   	
   }
   
   public String getRctBaName() {
	   	
   	  return RCT_BA_NM;
   	
   }
   
   public void setRctBaName(String RCT_BA_NM) {
   	
   	  this.RCT_BA_NM = RCT_BA_NM;
   	
   }
   
   public String getPdcBankName() {
	   	
   	  return PDC_BNK_NM;
   	
   }
   
   public void setPdcBankName(String PDC_BNK_NM) {
   	
   	  this.PDC_BNK_NM = PDC_BNK_NM;
   	
   }
   
   public String getPdcInvoiceNumbers() {
	   	
   	  return PDC_INVC_NMBR;
   	
   }
   
   public void setPdcInvoiceNumbers(String PDC_INVC_NMBR) {
   	
   	  this.PDC_INVC_NMBR = PDC_INVC_NMBR;
   	
   }
   
   public String getPdcPaymentMethod() {
	   	
   	  return PDC_PYMNT_MTHD;
   	
   }
   
   public void setPdcPaymentMethod(String PDC_PYMNT_MTHD) {
   	
   	  this.PDC_PYMNT_MTHD = PDC_PYMNT_MTHD;
   	
   }

   public String getPdcAmountInWords() {
	   	
   	  return PDC_AMNT_IN_WRDS;
   	
   }
   
   public void setPdcAmountInWords(String PDC_AMNT_IN_WRDS) {
   	
   	  this.PDC_AMNT_IN_WRDS = PDC_AMNT_IN_WRDS;
   	
   }
   
} // ArModPdcDetails class   