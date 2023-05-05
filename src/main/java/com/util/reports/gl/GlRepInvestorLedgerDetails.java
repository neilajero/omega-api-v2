/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.gl;

import java.util.Comparator;
import java.util.Date;

public class GlRepInvestorLedgerDetails implements java.io.Serializable {

    private String IL_INVTR_CODE;
    private String IL_INVTR_NM;
    private String IL_INVTR_CNTCT;
    private String IL_INVTR_ADDRSS;
    
    private Date IL_EFFCTV_DT;
    private String IL_DCMNT_NMBR;
    private String IL_RFRNC_NMBR;
    private String IL_SRC_NM;
    private String IL_DESC;
    private String IL_TYP;
    private byte IL_DBT;
    private double IL_AMNT;
    private double IL_BEG_BLNC;
    private byte IL_PSTD;
    private double IL_CNVRSN_RT;
    

    public GlRepInvestorLedgerDetails() {
    }
 

    public String getIlInvestorCode() {
    	
    	return IL_INVTR_CODE;
    	
    }
    
    public void setIlInvestorCode(String IL_INVTR_CODE) {
    	
    	this.IL_INVTR_CODE = IL_INVTR_CODE;
    	
    }
    
   

   
    public String getIlInvestorName() {
    	
    	return IL_INVTR_NM;
    	
    }
    
    public void setIlInvestorName(String IL_INVTR_NM) {
    	
    	this.IL_INVTR_NM = IL_INVTR_NM;
    	
    }
    
    public String getIlInvestorContact() {
    	
    	return IL_INVTR_CNTCT;
    	
    }
    
    public void setIlInvestorContact(String IL_INVTR_CNTCT) {
    	
    	this.IL_INVTR_CNTCT = IL_INVTR_CNTCT;
    	
    }
    
    
    public String getIlInvestorAddress() {
    	
    	return IL_INVTR_ADDRSS;
    	
    }
    
    public void setIlInvestorAddress(String IL_INVTR_ADDRSS) {
    	
    	this.IL_INVTR_ADDRSS = IL_INVTR_ADDRSS;
    	
    }
        
    public Date getIlEffectiveDate() {
    	
    	return IL_EFFCTV_DT;
    	
    }    
    
    public void setIlEffectiveDate(Date IL_EFFCTV_DT) {
    	
    	this.IL_EFFCTV_DT = IL_EFFCTV_DT;
    	
    }
    
    public String getIlDocumentNumber() {
    	
    	return IL_DCMNT_NMBR;
    	
    }
    
    public void setIlDocumentNumber(String IL_DCMNT_NMBR) {
    	
    	this.IL_DCMNT_NMBR = IL_DCMNT_NMBR;
    	
    }
    
   
    
    public byte getIlDebit() {
    	
    	return IL_DBT;
    	
    }
    
    public void setIlDebit(byte IL_DBT) {
    	
    	this.IL_DBT = IL_DBT;
    	
    }
    
  
    public double getIlAmount() {
    	
        return IL_AMNT;	
    	
    }   
    
    public void setIlAmount(double IL_AMNT) {
    	
    	this.IL_AMNT = IL_AMNT;
    	
    }
    
    public double getIlBeginningBalance() {
    	
        return IL_BEG_BLNC;	
    	
    }   
    
    public void setIlBeginningBalance(double IL_BEG_BLNC) {
    	
    	this.IL_BEG_BLNC = IL_BEG_BLNC;
    	
    }
       
    
        
    public String getIlType() {
    	
    	return IL_TYP;
    	
    }
    
    public void setIlType(String IL_TYP) {
    	
    	this.IL_TYP = IL_TYP;
    	
    }
    
    
    public byte getIlPosted() {
    	
    	return IL_PSTD;
    	
    }
    
    public void setIlPosted(byte IL_PSTD) {
    	
    	this.IL_PSTD = IL_PSTD;
    	
    }
    
    public String getIlReferenceNumber() {
    	
    	return IL_RFRNC_NMBR;
    	
    }
    
    public void setIlReferenceNumber(String IL_RFRNC_NMBR) {
    	
    	this.IL_RFRNC_NMBR = IL_RFRNC_NMBR;
    	
    }
    
    
    
    public String getIlSourceName() {
    	
    	return IL_SRC_NM;
    	
    }
    
    public void setIlSourceName(String IL_SRC_NM) {
    	
    	this.IL_SRC_NM = IL_SRC_NM;
    	
    }
    
    public String getIlDescription() {
    	
    	return IL_DESC;
    	
    }
    
    public void setIlDescription(String IL_DESC) {
    	
    	this.IL_DESC = IL_DESC;
    	
    }
 
    
    public double getIlConversionRate() {
    	
    	return IL_CNVRSN_RT;
    	
    }
    
    public void setIlConversionRate(double IL_CNVRSN_RT) {
    	
    	this.IL_CNVRSN_RT = IL_CNVRSN_RT;
    	
    }
    
 
    public static Comparator NoGroupComparator = (GL, anotherGL) -> {

        String IL_INVTR_CD1 = ((GlRepInvestorLedgerDetails) GL).getIlInvestorCode();
        Date IL_EFFCTV_DT1 = ((GlRepInvestorLedgerDetails) GL).getIlEffectiveDate();

        String IL_INVTR_CD2 = ((GlRepInvestorLedgerDetails) anotherGL).getIlInvestorCode();
        Date IL_EFFCTV_DT2 = ((GlRepInvestorLedgerDetails) anotherGL).getIlEffectiveDate();

        if(!(IL_INVTR_CD1.equals(IL_INVTR_CD2))){

            return IL_INVTR_CD1.compareTo(IL_INVTR_CD2);

        } else {

            return IL_EFFCTV_DT1.compareTo(IL_EFFCTV_DT2);

        }

    };
    
    
    public static Comparator InvestorNameGroupComparator = (GL, anotherGL) -> {

        String IL_INVTR_NM1 = ((GlRepInvestorLedgerDetails) GL).getIlInvestorName();
        Date IL_EFFCTV_DT1 = ((GlRepInvestorLedgerDetails) GL).getIlEffectiveDate();

        String IL_INVTR_NM2 = ((GlRepInvestorLedgerDetails) anotherGL).getIlInvestorName();
        Date IL_EFFCTV_DT2 = ((GlRepInvestorLedgerDetails) anotherGL).getIlEffectiveDate();

        if(!(IL_INVTR_NM1.equals(IL_INVTR_NM2))){

            return IL_INVTR_NM1.compareTo(IL_INVTR_NM2);

        } else {

            return IL_EFFCTV_DT1.compareTo(IL_EFFCTV_DT2);

        }

    };
    
    
   
    
}