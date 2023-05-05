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

public class GlRepJournalPrintDetails implements java.io.Serializable {

   private String JP_JR_NM;
   private String JP_JR_DESC;
   private Date JP_JR_EFFCTV_DT;
   private String JP_JR_DCMNT_NMBR;
   private String JP_JR_CRTD_BY;
   private String JP_JR_APPRVD_RJCTD_BY;
   private byte JP_JL_DBT;
   private double JP_JL_AMNT;
   private String JP_JL_COA_ACCNT_NMBR;
   private String JP_JL_COA_ACCNT_DESC;
   private byte JP_SHW_DPLCT;
   private String ORDER_BY;
   
   // added field
   private String JP_JR_APPRVL_STTS;
   private byte JP_JR_PSTD;
   
   private Date JP_JR_DT_RVRSL;
   private byte JP_JR_RVRSD;
   private String JP_JR_PSTD_BY;
   private String JP_JR_JC_NM;
   private String JP_JR_JS_NM;
   private String JP_JR_FC_NM;
   private String JP_JR_JB_NM;
   private String JP_JR_CRTD_BY_DSCRPTN;
   private String JP_JR_APPRVD_BY_DSCRPTN;
   private String JP_JR_RFRNC_NMBR;
   private String JP_JR_BR_BRNCH_CODE;
   private String JP_JR_BR_NM;
   private String JP_JL_DESC;
   private char JP_JR_FC_SYMBL;
   
   public GlRepJournalPrintDetails() {
   }

   public String getJpJrName() {
   	
   	  return JP_JR_NM;
   	
   }
   
   public void setJpJrName(String JP_JR_NM) {
   	
   	  this.JP_JR_NM = JP_JR_NM;
   	
   }
   
   public String getJpJrDescription() {
   	
   	  return JP_JR_DESC;
   	
   }
   
   public void setJpJrDescription(String JP_JR_DESC) {
   	
   	  this.JP_JR_DESC = JP_JR_DESC;
   	
   }
   
   public Date getJpJrEffectiveDate() {
   	
   	  return JP_JR_EFFCTV_DT;
   	
   }
   
   public void setJpJrEffectiveDate(Date JP_JR_EFFCTV_DT) {
   	
   	  this.JP_JR_EFFCTV_DT = JP_JR_EFFCTV_DT;
   	
   }
   
   public String getJpJrDocumentNumber() {
   	
   	  return JP_JR_DCMNT_NMBR;
   	
   }
   
   public void setJpJrDocumentNumber(String JP_JR_DCMNT_NMBR) {
   	
   	  this.JP_JR_DCMNT_NMBR = JP_JR_DCMNT_NMBR;
   	
   }
   
   public String getJpJrCreatedBy() {
   	
   	  return JP_JR_CRTD_BY;
   	
   }
   
   public void setJpJrCreatedBy(String JP_JR_CRTD_BY) {
   	
   	  this.JP_JR_CRTD_BY = JP_JR_CRTD_BY;
   	
   }
   
   public String getJpJrApprovedRejectedBy() {
   	
   	  return JP_JR_APPRVD_RJCTD_BY;
   	
   }
   
   public void setJpJrApprovedRejectedBy(String JP_JR_APPRVD_RJCTD_BY) {
   	
   	  this.JP_JR_APPRVD_RJCTD_BY = JP_JR_APPRVD_RJCTD_BY;
   	
   }
   
   public byte getJpJlDebit() {
   	
   	  return JP_JL_DBT;
   	
   }
   
   public void setJpJlDebit(byte JP_JL_DBT) {
   	
   	  this.JP_JL_DBT = JP_JL_DBT;
   	
   }
   
   public double getJpJlAmount() {
   	
   	  return JP_JL_AMNT;
   	
   }
   
   public void setJpJlAmount(double JP_JL_AMNT) {
   	
   	  this.JP_JL_AMNT = JP_JL_AMNT;
   	
   }
   
   public String getJpJlCoaAccountNumber() {
   	
   	  return JP_JL_COA_ACCNT_NMBR;
   	
   }
   
   public void setJpJlCoaAccountNumber(String JP_JL_COA_ACCNT_NMBR) {
   	
   	  this.JP_JL_COA_ACCNT_NMBR = JP_JL_COA_ACCNT_NMBR;
   	
   }
   
   public String getJpJlCoaAccountDescription() {
   	
   	  return JP_JL_COA_ACCNT_DESC;
   	
   }
   
   public void setJpJlCoaAccountDescription(String JP_JL_COA_ACCNT_DESC) {
   	
   	  this.JP_JL_COA_ACCNT_DESC = JP_JL_COA_ACCNT_DESC;
   	
   }
   
   public byte getJpShowDuplicate() {
   	
   	  return JP_SHW_DPLCT;
   	
   }
   
   public void setJpShowDuplicate(byte JP_SHW_DPLCT) {
   	
   	  this.JP_SHW_DPLCT = JP_SHW_DPLCT;
   	
   }
   
   public String getJpJrApprovalStatus() {
   	
   	  return JP_JR_APPRVL_STTS;
   	  
   }
   
   public void setJpJrApprovalStatus(String JP_JR_APPRVL_STTS) {
   	
   	  this.JP_JR_APPRVL_STTS = JP_JR_APPRVL_STTS;
   	
   }
   
   public byte getJpJrPosted() {
   	
   	  return JP_JR_PSTD;
   	  
   }
   
   public void setJpJrPosted(byte JP_JR_PSTD) {
   	
   	  this.JP_JR_PSTD = JP_JR_PSTD;
   	
   }
   
   public Date getJpJrDateReversal() {
   	
   	  return JP_JR_DT_RVRSL;
   	  
   }
   
   public void setJpJrDateReversal(Date JP_JR_DT_RVRSL) {
   	
   	  this.JP_JR_DT_RVRSL = JP_JR_DT_RVRSL;
   	
   }
   
   public byte getJpJrReversed() {
   	
   	  return JP_JR_RVRSD;
   	  
   }
   
   public void setJpJrReversed(byte JP_JR_RVRSD) {
   	
   	  this.JP_JR_RVRSD = JP_JR_RVRSD;
   	
   }
   
   public String getJpJrPostedBy() {
   	
   	  return JP_JR_PSTD_BY;
   	  
   }
   
   public void setJpJrPostedBy(String JP_JR_PSTD_BY) {
   	
   	  this.JP_JR_PSTD_BY = JP_JR_PSTD_BY;
   	
   }
   
   public String getJpJrJcName() {
   	
   	  return JP_JR_JC_NM;
   	  
   }
   
   public void setJpJrJcName(String JP_JR_JC_NM) {
   	
   	  this.JP_JR_JC_NM = JP_JR_JC_NM;
   	
   }
   
   public String getJpJrJsName() {
   	
   	  return JP_JR_JS_NM;
   	  
   }
   
   public void setJpJrJsName(String JP_JR_JS_NM) {
   	
   	  this.JP_JR_JS_NM = JP_JR_JS_NM;
   	
   }
   
   public String getJpJrFcName() {
   	
   	  return JP_JR_FC_NM;
   	  
   }
   
   public void setJpJrFcName(String JP_JR_FC_NM) {
   	
   	  this.JP_JR_FC_NM = JP_JR_FC_NM;
   	
   }
   
   public String getJpJrJbName() {
   	
   	  return JP_JR_JB_NM;
   	  
   }
   
   public void setJpJrJbName(String JP_JR_JB_NM) {
   	
   	  this.JP_JR_JB_NM = JP_JR_JB_NM;
   	
   }

   public String getJpJrCreatedByDescription() {
   	
   	  return JP_JR_CRTD_BY_DSCRPTN;
   	  
   }

   public void setJpJrCreatedByDescription(String JP_JR_CRTD_BY_DSCRPTN) {

	   this.JP_JR_CRTD_BY_DSCRPTN = JP_JR_CRTD_BY_DSCRPTN;

   }

   public String getJpJrApprovedByDescription() {

	   return JP_JR_APPRVD_BY_DSCRPTN;

   }

   public void setJpJrApprovedByDescription(String JP_JR_APPRVD_BY_DSCRPTN) {

	   this.JP_JR_APPRVD_BY_DSCRPTN = JP_JR_APPRVD_BY_DSCRPTN;

   }
   
   public String getJpJrReferenceNumber() {
	   
	   return JP_JR_RFRNC_NMBR;
	   
   }
   
   public void setJpJrReferenceNumber(String JP_JR_RFRNC_NMBR) {
	   
	   this.JP_JR_RFRNC_NMBR = JP_JR_RFRNC_NMBR;
	   
   }
   
   public String getBrBranchCode() {
	   
	   return JP_JR_BR_BRNCH_CODE;
	   
   }
   
   public void setBrBranchCode(String JP_JR_BR_BRNCH_CODE) {
	   
	   this.JP_JR_BR_BRNCH_CODE = JP_JR_BR_BRNCH_CODE;
	   
   }
   
   public String getBrName() {
	   
	   return JP_JR_BR_NM;
	   
   }
   
   public void setBrName(String JP_JR_BR_NM) {
	   
	   this.JP_JR_BR_NM = JP_JR_BR_NM;
	   
   }
   
   public String getJpJlDescription() {
	   
	   return JP_JL_DESC;
	   
   }
   
   public void setJpJlDescription(String JP_JL_DESC) {
	   
	   this.JP_JL_DESC = JP_JL_DESC;
	   
   }
   
   public String getOrderBy() {
	   	
	   	  return ORDER_BY;
	   	
   }

   public void setOrderBy(String ORDER_BY) {

	   this.ORDER_BY = ORDER_BY;

   }

   public char getJpJrFcSymbol(){

	   return JP_JR_FC_SYMBL;	

   }

   public void setJpJrFcSymbol(char JP_JR_FC_SYMBL){

	   this.JP_JR_FC_SYMBL = JP_JR_FC_SYMBL; 

   }
   
   public static Comparator accountDescriptionComparator = (CR, anotherCR) -> {
       int cmp=1;
       String GL_JRNL_ACCNT_DSCRPTN = ((GlRepJournalPrintDetails) CR).getJpJlCoaAccountDescription();
       Byte GL_JRNL_DBT = ((GlRepJournalPrintDetails) CR).getJpJlDebit();
       String docNum1 = ((GlRepJournalPrintDetails) CR).getJpJrDocumentNumber();

       String GL_JRNL_ACCNT_DSCRPTN2 = ((GlRepJournalPrintDetails) anotherCR).getJpJlCoaAccountDescription();
       Byte GL_JRNL_DBT2 = ((GlRepJournalPrintDetails) anotherCR).getJpJlDebit();
       String docNum2 = ((GlRepJournalPrintDetails) anotherCR).getJpJrDocumentNumber();

       String ORDER_BY = "ACCOUNT DESCRIPTION";

       if(ORDER_BY.equals("ACCOUNT DESCRIPTION") && !(GL_JRNL_ACCNT_DSCRPTN.equals(GL_JRNL_ACCNT_DSCRPTN2)) && docNum1.equals(docNum2)){

           cmp=  GL_JRNL_ACCNT_DSCRPTN.compareTo(GL_JRNL_ACCNT_DSCRPTN2);

       } else if(ORDER_BY.equals("DEBIT") && !(GL_JRNL_DBT.equals(GL_JRNL_DBT2))&& docNum1.equals(docNum2)){

           cmp=  GL_JRNL_DBT.compareTo(GL_JRNL_DBT2);

       } else{
           if(docNum1.equals(docNum2)){
               cmp=  GL_JRNL_ACCNT_DSCRPTN.compareTo(GL_JRNL_ACCNT_DSCRPTN2);
           }

       }
       return cmp;
   };
	   
	   public static Comparator debitComparator = (CR, anotherCR) -> {
           int cmp=1;
           String GL_JRNL_ACCNT_DSCRPTN = ((GlRepJournalPrintDetails) CR).getJpJlCoaAccountDescription();
           Byte GL_JRNL_DBT = ((GlRepJournalPrintDetails) CR).getJpJlDebit();
           String docNum1 = ((GlRepJournalPrintDetails) CR).getJpJrDocumentNumber();

           String GL_JRNL_ACCNT_DSCRPTN2 = ((GlRepJournalPrintDetails) anotherCR).getJpJlCoaAccountDescription();
           Byte GL_JRNL_DBT2 = ((GlRepJournalPrintDetails) anotherCR).getJpJlDebit();
           String docNum2 = ((GlRepJournalPrintDetails) anotherCR).getJpJrDocumentNumber();

           String ORDER_BY = "DEBIT";

           if(ORDER_BY.equals("ACCOUNT DESCRIPTION") && !(GL_JRNL_ACCNT_DSCRPTN.equals(GL_JRNL_ACCNT_DSCRPTN2)) && docNum1.equals(docNum2)){

               cmp= GL_JRNL_ACCNT_DSCRPTN.compareTo(GL_JRNL_ACCNT_DSCRPTN2);

           } else if(ORDER_BY.equals("DEBIT") && !(GL_JRNL_DBT.equals(GL_JRNL_DBT2)) && docNum1.equals(docNum2)){

               cmp= GL_JRNL_DBT2.compareTo(GL_JRNL_DBT);

           } else{
               if(docNum1.equals(docNum2))
                   cmp= GL_JRNL_ACCNT_DSCRPTN.compareTo(GL_JRNL_ACCNT_DSCRPTN2);
           }

           return cmp;

       };
   
 }  // GlRepJournalPrintDetails