/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ar;

import java.util.Comparator;
import java.util.Date;

public class ArRepOrRegisterDetails implements java.io.Serializable {

   private Date ORR_DT;
   private String ORR_RCPT_NMBR;
   private String ORR_BTCH_NM;
   private String ORR_PYMNT_MTHD;
   private String ORR_RFRNC_NMBR;
   private String ORR_DESC;
   private String ORR_CST_CSTMR_CODE;
   private String ORR_CST_CSTMR_TYP;
   private String ORR_CST_CSTMR_CLSS;
   private String ORR_BNK_ACCNT;
   private double ORR_AMNT;
   private String ORDER_BY;
   private String ORR_CST_NM;
   private String ORR_DR_COA_ACCNT_NMBR;
   private String ORR_DR_COA_ACCNT_DESC;
   private double ORR_DR_DBT_AMNT;
   private double ORR_DR_CRDT_AMNT;
   private String ORR_SLP_SLSPRSN_CODE;
   private String ORR_SLP_NM;
   private double ORR_RCPT_NT_AMNT;
   private double ORR_RCPT_TX_AMNT;
   private String ORR_RCPT_APPLD_INVCS;
   private String ORR_PR_NMBR;
   private Date ORR_PR_DT;
   private String ORR_CHCK_NMBR;
   private Date ORR_CHCK_DT;
   private String ORR_INVC_NMBR;
   private String ORR_PSTD;
   private String ORR_INVC_RFRNC_NMBR;
   private double ORR_TTL_VAT;
  
   public ArRepOrRegisterDetails() {
   }

   public Date getOrrDate() {
   	
   	  return ORR_DT;
   	
   }
   
   public void setOrrDate(Date ORR_DT) {
   	
   	  this.ORR_DT = ORR_DT;
   	
   }
   
   public String getOrrReceiptNumber() {
   	
   	  return ORR_RCPT_NMBR;
   	
   }
   
   public void setOrrReceiptNumber(String ORR_RCPT_NMBR) {
   	
   	  this.ORR_RCPT_NMBR = ORR_RCPT_NMBR;
   	
   }
   
   public String getOrrBatchName() {
	   	
   	  return ORR_BTCH_NM;
   	
   }
   
   public void setOrrBatchName(String ORR_BTCH_NM) {
   	
   	  this.ORR_BTCH_NM = ORR_BTCH_NM;
   	
   }
	   
   public String getOrrPaymentMethod() {
	   	
   	  return ORR_PYMNT_MTHD;
   	
   }
   
   public void setOrrPaymentMethod(String ORR_PYMNT_MTHD) {
   	
   	  this.ORR_PYMNT_MTHD = ORR_PYMNT_MTHD;
   	
   }
   
   public String getOrrReferenceNumber() {
   	
   	  return ORR_RFRNC_NMBR;
   	  
   }
   
   public void setOrrReferenceNumber(String ORR_RFRNC_NMBR) {
   	
   	  this.ORR_RFRNC_NMBR = ORR_RFRNC_NMBR;
   	  
   }
   
   public String getOrrDescription() {
   	
   	  return ORR_DESC;
   	  
   }
   
   public void setOrrDescription(String ORR_DESC) {
   	
   	  this.ORR_DESC = ORR_DESC;
   	  
   }

   public String getOrrCstCustomerCode() {
   	
   	  return ORR_CST_CSTMR_CODE;
   	
   }
   
   public void setOrrCstCustomerCode(String ORR_CST_CSTMR_CODE) {
   	
   	  this.ORR_CST_CSTMR_CODE = ORR_CST_CSTMR_CODE;
   	
   }
   
   public String getOrrCstCustomerType() {
   	
   	  return ORR_CST_CSTMR_TYP;
   	
   }
   
   public void setOrrCstCustomerType(String ORR_CST_CSTMR_TYP) {
   	
   	  this.ORR_CST_CSTMR_TYP = ORR_CST_CSTMR_TYP;
   	
   }
   public String getOrrCstCustomerClass() {
   	
   	  return ORR_CST_CSTMR_CLSS;
   	
   }
   
   public void setOrrCstCustomerClass(String ORR_CST_CSTMR_CLSS) {
   	
   	  this.ORR_CST_CSTMR_CLSS = ORR_CST_CSTMR_CLSS;
   	
   }
   
   public String getOrrBankAccount() {
   	
   	  return ORR_BNK_ACCNT;
   	  
   }
   
   public void setOrrBankAccount(String ORR_BNK_ACCNT) {
   	
   	  this.ORR_BNK_ACCNT = ORR_BNK_ACCNT;
   	  
   }
   
   public double getOrrAmount() {
   	
   	  return ORR_AMNT;
   	  
   }
   
   public void setOrrAmount(double ORR_AMNT) {
   	
   	  this.ORR_AMNT = ORR_AMNT;
   	  
   }
   
   public String getOrderBy() {
   	
   	  return ORDER_BY;
   	  
   }
   
   public void setOrderBy(String ORDER_BY) {
   	
   	  this.ORDER_BY = ORDER_BY;
   	  
   }
   
   public String getOrrCstName() {
   	
   	  return ORR_CST_NM;
   	
   }
   
   public void setOrrCstName(String ORR_CST_NM) {
   	
   	  this.ORR_CST_NM = ORR_CST_NM;
   	
   }
   
   public String getOrrDrCoaAccountNumber() {
   	
   	  return ORR_DR_COA_ACCNT_NMBR;
   	
   }
   
   public void setOrrDrCoaAccountNumber(String ORR_DR_COA_ACCNT_NMBR) {
   	
   	  this.ORR_DR_COA_ACCNT_NMBR = ORR_DR_COA_ACCNT_NMBR;
   	
   }
   
   public String getOrrDrCoaAccountDescription() {
   	
   	  return ORR_DR_COA_ACCNT_DESC;
   	
   }
   
   public void setOrrDrCoaAccountDescription(String ORR_DR_COA_ACCNT_DESC) {
   	
   	  this.ORR_DR_COA_ACCNT_DESC = ORR_DR_COA_ACCNT_DESC;
   	
   }
   
   public double getOrrDrDebitAmount() {
   	
   	  return ORR_DR_DBT_AMNT;
   	
   }
   
   public void setOrrDrDebitAmount(double ORR_DR_DBT_AMNT) {
   	
   	  this.ORR_DR_DBT_AMNT = ORR_DR_DBT_AMNT;
   	
   }
   
   public double getOrrDrCreditAmount() {
   	
   	  return ORR_DR_CRDT_AMNT;
   	
   }
   
   public void setOrrDrCreditAmount(double ORR_DR_CRDT_AMNT) {
   	
   	  this.ORR_DR_CRDT_AMNT = ORR_DR_CRDT_AMNT;
   	
   }
   
   public String getOrrSlsSalespersonCode() {
   	
   	  return ORR_SLP_SLSPRSN_CODE;
   	
   }
   
   public void setOrrSlsSalespersonCode(String ORR_SLP_SLSPRSN_CODE) {
   	
   	  this.ORR_SLP_SLSPRSN_CODE = ORR_SLP_SLSPRSN_CODE;
   	
   }
   
   public String getOrrSlsName() {
   	
   	  return ORR_SLP_NM;
   	
   }
   
   public void setOrrSlsName(String ORR_SLP_NM) {
   	
   	  this.ORR_SLP_NM = ORR_SLP_NM;
   	
   }
   
   public double getOrrReceiptNetAmount() {
   	
   	  return ORR_RCPT_NT_AMNT;
   	
   }
   
   public void setOrrReceiptNetAmount(double ORR_RCPT_NT_AMNT) {
   	
   	  this.ORR_RCPT_NT_AMNT = ORR_RCPT_NT_AMNT;
   	
   }
   
   public double getOrrReceiptTaxAmount() {
   	
   	  return ORR_RCPT_TX_AMNT;
   	
   }
   
   public void setOrrReceiptTaxAmount(double ORR_RCPT_TX_AMNT) {
   	
   	  this.ORR_RCPT_TX_AMNT = ORR_RCPT_TX_AMNT;
   	
   }
   
   public String getOrrReceiptAppliedInvoices() {
	   
	   return ORR_RCPT_APPLD_INVCS;
	   
   }
   
   public void setOrrReceiptAppliedInvoices(String ORR_RCPT_APPLD_INVCS) {
	   
	   this.ORR_RCPT_APPLD_INVCS = ORR_RCPT_APPLD_INVCS;
	   
   }
   
   public String getOrrPrNumber() {
	   
	   return ORR_PR_NMBR;
	   
   }
   
   public void setOrrPrNumber(String ORR_PR_NMBR) {
	   
	   this.ORR_PR_NMBR = ORR_PR_NMBR;
	   
   }
   
   public Date getOrrPrDate() {
	   
	   return ORR_PR_DT;
	   
   }
   
   public void setOrrPrDate(Date ORR_PR_DT) {
	   
	   this.ORR_PR_DT = ORR_PR_DT;
	   
   }
   
   
   public String getOrrCheckNumber() {
	   
	   return ORR_CHCK_NMBR;
	   
   }
   
   public void setOrrCheckNumber(String ORR_CHCK_NMBR) {
	   
	   this.ORR_CHCK_NMBR = ORR_CHCK_NMBR;
	   
   }
   
   public Date getOrrCheckDate() {
	   
	   return ORR_CHCK_DT;
	   
   }
   
   public void setOrrCheckDate(Date ORR_CHCK_DT) {
	   
	   this.ORR_CHCK_DT = ORR_CHCK_DT;
	   
   }
   
   public String getOrrInvoiceNumber() {
	   
	   return ORR_INVC_NMBR;
	   
   }
   
   public void setOrrInvoiceNumber(String ORR_INVC_NMBR) {
	   
	   this.ORR_INVC_NMBR = ORR_INVC_NMBR;
	   
   }
   
   public String getOrrPosted() {
	   
	   return ORR_PSTD;
	   
   }
   
   public void setOrrPosted(String ORR_PSTD) {
	   
	   this.ORR_PSTD = ORR_PSTD;
	   
   }
   
   public String getOrrInvoiceReferenceNumber() {
	   
	   return ORR_INVC_RFRNC_NMBR;
	   
   }
   
   public void setOrrInvoiceReferenceNumber(String ORR_INVC_RFRNC_NMBR) {
	   
	   this.ORR_INVC_RFRNC_NMBR = ORR_INVC_RFRNC_NMBR;
	   
   }
   
   
   public double getOrrTotalVat(){
	   
	   return ORR_TTL_VAT;
   }
   
   public void setOrrTotalVat(double ORR_TTL_VAT){
	   this.ORR_TTL_VAT = ORR_TTL_VAT;
   }
   
   public static Comparator SalespersonComparator = (OR, anotherOR) -> {

       String ORR_CST_SLSPRSN1 = ((ArRepOrRegisterDetails) OR).getOrrSlsSalespersonCode();
       String ORR_CST_CSTMR_CODE1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerType();
       Date ORR_DT1 = ((ArRepOrRegisterDetails) OR).getOrrDate();
       String ORR_RCPT_NMBR1 = ((ArRepOrRegisterDetails) OR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT1 = ((ArRepOrRegisterDetails) OR).getOrrBankAccount();

       String ORR_CST_SLSPRSN2 = ((ArRepOrRegisterDetails) anotherOR).getOrrSlsSalespersonCode();
       String ORR_CST_CSTMR_CODE2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerType();
       Date ORR_DT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrDate();
       String ORR_RCPT_NMBR2 = ((ArRepOrRegisterDetails) anotherOR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrBankAccount();

       String ORDER_BY = ((ArRepOrRegisterDetails) OR).getOrderBy();
       String test = "";

       try{
           if (ORR_CST_SLSPRSN1==null) {

               ORR_CST_SLSPRSN1="AA";
               ORR_CST_SLSPRSN2="AA";
               return ORR_CST_SLSPRSN1.compareTo(ORR_CST_SLSPRSN2);

           } else if (ORR_CST_SLSPRSN2==null) {

               ORR_CST_SLSPRSN1="AA";
               ORR_CST_SLSPRSN2="AA";
               return ORR_CST_SLSPRSN1.compareTo(ORR_CST_SLSPRSN2);

           }
               else {
               return ORR_CST_SLSPRSN1.compareTo(ORR_CST_SLSPRSN2);
           }
       } catch(Exception e) {

           test="0";

       }
       if (!(ORR_CST_SLSPRSN1.equals(ORR_CST_SLSPRSN2))) {

           return ORR_CST_SLSPRSN1.compareTo(ORR_CST_SLSPRSN2);

       } else {

           if(ORDER_BY.equals("DATE") && !(ORR_DT1.equals(ORR_DT2))){

               return ORR_DT1.compareTo(ORR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(ORR_CST_CSTMR_TYP1.equals(ORR_CST_CSTMR_TYP2))){

               return ORR_CST_CSTMR_TYP1.compareTo(ORR_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(ORR_CST_CSTMR_CODE1.equals(ORR_CST_CSTMR_CODE2))){

               return ORR_CST_CSTMR_CODE1.compareTo(ORR_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("") && !(ORR_CST_CSTMR_CODE1.equals(ORR_CST_CSTMR_CODE2))){

               return ORR_CST_SLSPRSN1.compareTo(ORR_CST_SLSPRSN2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(ORR_BNK_ACCNT1.equals(ORR_BNK_ACCNT2))){

               return ORR_BNK_ACCNT1.compareTo(ORR_BNK_ACCNT2);

           } else {

               return ORR_CST_SLSPRSN1.compareTo(ORR_CST_SLSPRSN2);

           }

       }

   };
   public static Comparator CustomerCodeComparator = (OR, anotherOR) -> {

       String ORR_CST_SLSPRSN1 = ((ArRepOrRegisterDetails) OR).getOrrSlsSalespersonCode();
       String ORR_CST_CSTMR_CODE1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerType();
       Date ORR_DT1 = ((ArRepOrRegisterDetails) OR).getOrrDate();
       String ORR_RCPT_NMBR1 = ((ArRepOrRegisterDetails) OR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT1 = ((ArRepOrRegisterDetails) OR).getOrrBankAccount();

       String ORR_CST_SLSPRSN2 = ((ArRepOrRegisterDetails) anotherOR).getOrrSlsSalespersonCode();
       String ORR_CST_CSTMR_CODE2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerType();
       Date ORR_DT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrDate();
       String ORR_RCPT_NMBR2 = ((ArRepOrRegisterDetails) anotherOR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrBankAccount();

       String ORDER_BY = ((ArRepOrRegisterDetails) OR).getOrderBy();

       if (!(ORR_CST_CSTMR_CODE1.equals(ORR_CST_CSTMR_CODE2))) {

           return ORR_CST_CSTMR_CODE1.compareTo(ORR_CST_CSTMR_CODE2);

       } else {

           if(ORDER_BY.equals("DATE") && !(ORR_DT1.equals(ORR_DT2))){

               return ORR_DT1.compareTo(ORR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(ORR_CST_CSTMR_TYP1.equals(ORR_CST_CSTMR_TYP2))){

               return ORR_CST_CSTMR_TYP1.compareTo(ORR_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(ORR_BNK_ACCNT1.equals(ORR_BNK_ACCNT2))){

               return ORR_BNK_ACCNT1.compareTo(ORR_BNK_ACCNT2);

           } else {

               return ORR_RCPT_NMBR1.compareTo(ORR_RCPT_NMBR2);

           }

       }

   };
   
   public static Comparator CustomerTypeComparator = (OR, anotherOR) -> {

       String ORR_CST_CSTMR_CODE1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerType();
       Date ORR_DT1 = ((ArRepOrRegisterDetails) OR).getOrrDate();
       String ORR_RCPT_NMBR1 = ((ArRepOrRegisterDetails) OR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT1 = ((ArRepOrRegisterDetails) OR).getOrrBankAccount();

       String ORR_CST_CSTMR_CODE2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerType();
       Date ORR_DT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrDate();
       String ORR_RCPT_NMBR2 = ((ArRepOrRegisterDetails) anotherOR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrBankAccount();

       String ORDER_BY = ((ArRepOrRegisterDetails) OR).getOrderBy();

       if (!(ORR_CST_CSTMR_TYP1.equals(ORR_CST_CSTMR_TYP2))) {

           return ORR_CST_CSTMR_TYP1.compareTo(ORR_CST_CSTMR_TYP2);

       } else {

           if(ORDER_BY.equals("DATE") && !(ORR_DT1.equals(ORR_DT2))){

               return ORR_DT1.compareTo(ORR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(ORR_CST_CSTMR_CODE1.equals(ORR_CST_CSTMR_CODE2))){

               return ORR_CST_CSTMR_CODE1.compareTo(ORR_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(ORR_BNK_ACCNT1.equals(ORR_BNK_ACCNT2))){

               return ORR_BNK_ACCNT1.compareTo(ORR_BNK_ACCNT2);

           } else {

               return ORR_RCPT_NMBR1.compareTo(ORR_RCPT_NMBR2);

           }

       }

   };
   
   public static Comparator CustomerClassComparator = (OR, anotherOR) -> {

       String ORR_CST_CSTMR_CLSS1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerClass();
       String ORR_CST_CSTMR_CODE1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerType();
       Date ORR_DT1 = ((ArRepOrRegisterDetails) OR).getOrrDate();
       String ORR_RCPT_NMBR1 = ((ArRepOrRegisterDetails) OR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT1 = ((ArRepOrRegisterDetails) OR).getOrrBankAccount();

       String ORR_CST_CSTMR_CLSS2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerClass();
       String ORR_CST_CSTMR_CODE2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerType();
       Date ORR_DT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrDate();
       String ORR_RCPT_NMBR2 = ((ArRepOrRegisterDetails) anotherOR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrBankAccount();

       String ORDER_BY = ((ArRepOrRegisterDetails) OR).getOrderBy();

       if (!(ORR_CST_CSTMR_CLSS1.equals(ORR_CST_CSTMR_CLSS2))) {

           return ORR_CST_CSTMR_CLSS1.compareTo(ORR_CST_CSTMR_CLSS2);

       } else {

           if(ORDER_BY.equals("DATE") && !(ORR_DT1.equals(ORR_DT2))){

               return ORR_DT1.compareTo(ORR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(ORR_CST_CSTMR_CODE1.equals(ORR_CST_CSTMR_CODE2))){

               return ORR_CST_CSTMR_CODE1.compareTo(ORR_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(ORR_CST_CSTMR_TYP1.equals(ORR_CST_CSTMR_TYP2))){

               return ORR_CST_CSTMR_TYP1.compareTo(ORR_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(ORR_BNK_ACCNT1.equals(ORR_BNK_ACCNT2))){

               return ORR_BNK_ACCNT1.compareTo(ORR_BNK_ACCNT2);

           } else {

               return ORR_RCPT_NMBR1.compareTo(ORR_RCPT_NMBR2);

           }

       }

   };
   
   public static Comparator NoGroupComparator = (OR, anotherOR) -> {

       String ORR_CST_CSTMR_CODE1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerType();
       Date ORR_DT1 = ((ArRepOrRegisterDetails) OR).getOrrDate();
       String ORR_RCPT_NMBR1 = ((ArRepOrRegisterDetails) OR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT1 = ((ArRepOrRegisterDetails) OR).getOrrBankAccount();

       String ORR_CST_CSTMR_CODE2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerType();
       Date ORR_DT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrDate();
       String ORR_RCPT_NMBR2 = ((ArRepOrRegisterDetails) anotherOR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrBankAccount();

       String ORDER_BY = ((ArRepOrRegisterDetails) OR).getOrderBy();

       if(ORDER_BY.equals("DATE") && !(ORR_DT1.equals(ORR_DT2))){

           return ORR_DT1.compareTo(ORR_DT2);

       } else if(ORDER_BY.equals("CUSTOMER CODE") && !(ORR_CST_CSTMR_CODE1.equals(ORR_CST_CSTMR_CODE2))){

           return ORR_CST_CSTMR_CODE1.compareTo(ORR_CST_CSTMR_CODE2);

       } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(ORR_CST_CSTMR_TYP1.equals(ORR_CST_CSTMR_TYP2))){

           return ORR_CST_CSTMR_TYP1.compareTo(ORR_CST_CSTMR_TYP2);

       } else if(ORDER_BY.equals("BANK ACCOUNT") && !(ORR_BNK_ACCNT1.equals(ORR_BNK_ACCNT2))){

        return ORR_BNK_ACCNT1.compareTo(ORR_BNK_ACCNT2);

    } else {

           return ORR_RCPT_NMBR1.compareTo(ORR_RCPT_NMBR2);

       }

   };

   public static Comparator CoaAccountNumberComparator = (OR, anotherOR) -> {

       String ORR_CST_CSTMR_CLSS1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerClass();
       String ORR_CST_CSTMR_CODE1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP1 = ((ArRepOrRegisterDetails) OR).getOrrCstCustomerType();
       Date ORR_DT1 = ((ArRepOrRegisterDetails) OR).getOrrDate();
       String ORR_RCPT_NMBR1 = ((ArRepOrRegisterDetails) OR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT1 = ((ArRepOrRegisterDetails) OR).getOrrBankAccount();
       String ORR_DR_COA_ACCNT_NMBR1 = ((ArRepOrRegisterDetails) OR).getOrrDrCoaAccountNumber();

       String ORR_CST_CSTMR_CLSS2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerClass();
       String ORR_CST_CSTMR_CODE2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerCode();
       String ORR_CST_CSTMR_TYP2 = ((ArRepOrRegisterDetails) anotherOR).getOrrCstCustomerType();
       Date ORR_DT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrDate();
       String ORR_RCPT_NMBR2 = ((ArRepOrRegisterDetails) anotherOR).getOrrReceiptNumber();
       String ORR_BNK_ACCNT2 = ((ArRepOrRegisterDetails) anotherOR).getOrrBankAccount();
       String ORR_DR_COA_ACCNT_NMBR2 = ((ArRepOrRegisterDetails) anotherOR).getOrrDrCoaAccountNumber();

       String ORDER_BY = ((ArRepOrRegisterDetails) OR).getOrderBy();

       if (!(ORR_DR_COA_ACCNT_NMBR1.equals(ORR_DR_COA_ACCNT_NMBR2))) {

           return ORR_DR_COA_ACCNT_NMBR1.compareTo(ORR_DR_COA_ACCNT_NMBR2);

       } else {

           if(ORDER_BY.equals("DATE") && !(ORR_DT1.equals(ORR_DT2))){

               return ORR_DT1.compareTo(ORR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(ORR_CST_CSTMR_CODE1.equals(ORR_CST_CSTMR_CODE2))){

               return ORR_CST_CSTMR_CODE1.compareTo(ORR_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(ORR_CST_CSTMR_TYP1.equals(ORR_CST_CSTMR_TYP2))){

               return ORR_CST_CSTMR_TYP1.compareTo(ORR_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(ORR_BNK_ACCNT1.equals(ORR_BNK_ACCNT2))){

               return ORR_BNK_ACCNT1.compareTo(ORR_BNK_ACCNT2);

           } else {

               return ORR_RCPT_NMBR1.compareTo(ORR_RCPT_NMBR2);

           }

       }

   };
   
} // ArRepOrRegisterDetails class