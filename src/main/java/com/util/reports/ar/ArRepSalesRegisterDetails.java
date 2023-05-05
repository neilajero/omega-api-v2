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

public class ArRepSalesRegisterDetails implements java.io.Serializable {

   private Date SR_DT;
   private String SR_INVC_NMBR;
   private String SR_OR_NMBR;
   private double SR_OR_AMNT;
   private String SR_RFRNC_NMBR;
   private double SR_RVN_AMNT;
   private String SR_DESC;
   private String SR_CST_CSTMR_CODE;
   private String SR_CST_CSTMR_TYP;
   private String SR_CST_CSTMR_CLSS;
   private double SR_II_PRCNT_MRKP;
   private double SR_AMNT;
   private String ORDER_BY;
   private double SR_GRSS_AMNT;
   private double SR_NT_AMNT;
   private double SR_LN_AMNT;
   private double SR_LN_TAX_AMNT;
   private double SR_LN_S_AMNT;
   private double SR_TX_AMNT;
   private String SR_SLS_SLSPRSN_CODE;
   private String SR_SLS_NM;
   private String SR_DR_GL_ACCNT_NMBR;
   private String SR_DR_GL_ACCNT_DESC;
   private double SR_DR_DBT_AMNT;
   private double SR_DR_CRDT_AMNT;
   private boolean SR_INV_CRDT_MMO;
   private double SR_INV_TTL_NT_AMNT;
   private double SR_INV_TTL_TX_AMNT;
   private double SR_INV_TTL_AMNT;
   private String SR_CST_CSTMR_CODE2;   
   private String SR_CST_NM;
   private String SR_RCT_TYP;
   private double SR_DSCNT_AMNT;
   private String SR_INV_RCPT_NMBRS;
   private String SR_INV_RCPT_DTS;
   private byte posted;   
   private String INV_CM_INVC_NMBR;
   private double SR_AI_APPLY_AMNT;
   private double SR_AI_CRDTBL_W_TXAMNT;
   private double SR_AI_DSCNT_AMNT;
   private double SR_AI_APPLD_DPST;
   private String SR_MMO_NM;
   private double SR_QTY;
   private double SR_CPF;
   private double SR_SRVC_AMNT;
   private double SR_CST_BLNC;
   private double SR_BLNC;
   
   public ArRepSalesRegisterDetails() {
   }

   public Date getSrDate() {
   	
   	  return SR_DT;
   	
   }
   
   public void setSrDate(Date SR_DT) {
   	
   	  this.SR_DT = SR_DT;
   	
   }
   
   public String getSrInvoiceNumber() {
   	
   	  return SR_INVC_NMBR;
   	
   }
   
   public void setSrInvoiceNumber(String SR_INVC_NMBR) {
   	
   	  this.SR_INVC_NMBR = SR_INVC_NMBR;
   	
   }
   
    public String getSrOrNumber() {
   	
   	  return SR_OR_NMBR;
   	
   }
   
   public void setSrOrNumber(String SR_OR_NMBR) {
   	
   	  this.SR_OR_NMBR = SR_OR_NMBR;
   	
   }
   
   public double getSrRevenueAmount() {
   	
   	  return SR_RVN_AMNT;
   	
   }
   
   public void setSrRevenueAmount(double SR_RVN_AMNT) {
   	
   	  this.SR_RVN_AMNT = SR_RVN_AMNT;
   	
   }
	   
	   
    public double getSrOrAmount() {
   	
   	  return SR_OR_AMNT;
   	
   }
   
   public void setSrOrAmount(double SR_OR_AMNT) {
   	
   	  this.SR_OR_AMNT = SR_OR_AMNT;
   	
   }
   
   public String getSrMemoName() {
	   	
	   return SR_MMO_NM;
	   	
   }
	   
   public void setSrMemoName(String SR_MMO_NM) {
	   	
	  this.SR_MMO_NM = SR_MMO_NM;
	   	
   }

   public String getSrReferenceNumber() {
   	
   	  return SR_RFRNC_NMBR;
   	  
   }
   
   public void setSrReferenceNumber(String SR_RFRNC_NMBR) {
   	
   	  this.SR_RFRNC_NMBR = SR_RFRNC_NMBR;
   	  
   }
   
   public String getSrDescription() {
   	
   	  return SR_DESC;
   	  
   }
   
   public void setSrDescription(String SR_DESC) {
   	
   	  this.SR_DESC = SR_DESC;
   	  
   }
   
   public String getSrCstCustomerCode() {
   	
   	  return SR_CST_CSTMR_CODE;
   	
   }
   
   public void setSrCstCustomerCode(String SR_CST_CSTMR_CODE) {
   	
   	  this.SR_CST_CSTMR_CODE = SR_CST_CSTMR_CODE;
   	
   }
   
   public String getSrCstCustomerType() {
   	
   	  return SR_CST_CSTMR_TYP;
   	
   }
   
   public void setSrCstCustomerType(String SR_CST_CSTMR_TYP) {
   	
   	  this.SR_CST_CSTMR_TYP = SR_CST_CSTMR_TYP;
   	
   }
   
   public String getSrCstCustomerClass() {
   	
   	  return SR_CST_CSTMR_CLSS;
   	
   }
   
   public void setSrCstCustomerClass(String SR_CST_CSTMR_CLSS) {
   	
   	  this.SR_CST_CSTMR_CLSS = SR_CST_CSTMR_CLSS;
   	
   }
   
   public double getSrIiPercentMarkup() {
	   	
   	  return SR_II_PRCNT_MRKP;
   	  
   }
   
   public void setSrIiPercentMarkup(double SR_II_PRCNT_MRKP) {
   	
   	  this.SR_II_PRCNT_MRKP = SR_II_PRCNT_MRKP;
   	  
   }
   
   
   public double getSrAmount() {
   	
   	  return SR_AMNT;
   	  
   }
   
   public void setSrAmount(double SR_AMNT) {
   	
   	  this.SR_AMNT = SR_AMNT;
   	  
   }
   
   public double getSrServiceAmount() {
	   	
	   	  return SR_SRVC_AMNT;
	   	  
   }
   
   public void setSrServiceAmount(double SR_SRVC_AMNT) {
   	
   	  this.SR_SRVC_AMNT = SR_SRVC_AMNT;
   	  
   }
   
   public String getOrderBy() {
   	
   	  return ORDER_BY;
   	  
   }
   
   public void setOrderBy(String ORDER_BY) {
   	
   	  this.ORDER_BY = ORDER_BY;
   	  
   }
   
   public double getSrGrossAmount() {
	   	
	   	  return SR_GRSS_AMNT;
	   	
	   }
	   
	   public void setSrGrossAmount(double SR_GRSS_AMNT) {
	   	
	   	  this.SR_GRSS_AMNT = SR_GRSS_AMNT;
	   	
	   }
   
   
   
   public double getSrNetAmount() {
   	
   	  return SR_NT_AMNT;
   	
   }
   
   public void setSrNetAmount(double SR_NT_AMNT) {
   	
   	  this.SR_NT_AMNT = SR_NT_AMNT;
   	
   }
   
   public double getSrLineAmount() {

	   return SR_LN_AMNT;

   }

   public void setSrLineAmount(double SR_LN_AMNT) {

	   this.SR_LN_AMNT = SR_LN_AMNT;

   }

   public double getSrLineTaxAmount() {

	   return SR_LN_TAX_AMNT;

   }

   public void setSrLineTaxAmount(double SR_LN_TAX_AMNT) {

	   this.SR_LN_TAX_AMNT = SR_LN_TAX_AMNT;

   }

   public double getSrLineSAmount() {

	   return SR_LN_S_AMNT;

   }

   public void setSrLineSAmount(double SR_LN_S_AMNT) {

	   this.SR_LN_S_AMNT = SR_LN_S_AMNT;

   }
   
   public double getSrTaxAmount() {
   	
   	  return SR_TX_AMNT;
   	
   }
   
   public void setSrTaxAmount(double SR_TX_AMNT) {
   	
   	  this.SR_TX_AMNT = SR_TX_AMNT;
   	
   }
   
   public String getSrSlsSalespersonCode() {
   	
   	  return SR_SLS_SLSPRSN_CODE;
   	
   }
   
   public void setSrSlsSalespersonCode(String SR_SLS_SLSPRSN_CODE) {
   	
   	  this.SR_SLS_SLSPRSN_CODE = SR_SLS_SLSPRSN_CODE;
   	  
   }
   
   public String getSrSlsName() {
   	
   	  return SR_SLS_NM;
   	
   }
   
   public void setSrSlsName(String SR_SLS_NM) {
   	
   	  this.SR_SLS_NM = SR_SLS_NM;
   	
   }
   
   public String getSrDrGlAccountNumber() {
   	
   	  return SR_DR_GL_ACCNT_NMBR;
   	
   }
   
   public void setSrDrGlAccountNumber(String SR_DR_GL_ACCNT_NMBR) {
   	
   	  this.SR_DR_GL_ACCNT_NMBR = SR_DR_GL_ACCNT_NMBR;
   	
   }
   
   public String getSrDrGlAccountDescription() {
   	
   	  return SR_DR_GL_ACCNT_DESC;
   	
   }
   
   public void setSrDrGlAccountDescription(String SR_DR_GL_ACCNT_DESC) {
   	
   	  this.SR_DR_GL_ACCNT_DESC = SR_DR_GL_ACCNT_DESC;
   	
   }
   
   public double getSrDrDebitAmount() {
   	
   	  return SR_DR_DBT_AMNT;
   	
   }
   
   public void setSrDrDebitAmount(double SR_DR_DBT_AMNT) {
   	
   	  this.SR_DR_DBT_AMNT = SR_DR_DBT_AMNT;
   	
   }
   
   public double getSrDrCreditAmount() {
   	
   	  return SR_DR_CRDT_AMNT;
   	
   }
   
   public void setSrDrCreditAmount(double SR_DR_CRDT_AMNT) {
   	
   	  this.SR_DR_CRDT_AMNT = SR_DR_CRDT_AMNT;
   	
   }
   
   public boolean getSrInvCreditMemo() {
   	
   	  return SR_INV_CRDT_MMO;
   	
   }
   
   public void setSrInvCreditMemo(boolean SR_INV_CRDT_MMO) {
   	
   	  this.SR_INV_CRDT_MMO = SR_INV_CRDT_MMO;
   	
   }
   
   public double getSrInvTotalNetAmount() {
   	
   	  return SR_INV_TTL_NT_AMNT;
   	
   }
   
   public void setSrInvTotalNetAmount(double SR_INV_TTL_NT_AMNT) {
   	
   	  this.SR_INV_TTL_NT_AMNT = SR_INV_TTL_NT_AMNT;
   	
   }
   
   public double getSrInvTotalTaxAmount() {
   	
   	  return SR_INV_TTL_TX_AMNT;
   	
   }
   
   public void setSrInvTotalTaxAmount(double SR_INV_TTL_TX_AMNT) {
   	
   	  this.SR_INV_TTL_TX_AMNT = SR_INV_TTL_TX_AMNT;
   	
   }
   
   public double getSrInvTotalAmount() {
   	
   	  return SR_INV_TTL_AMNT;
   	
   }
   
   public void setSrInvTotalAmount(double SR_INV_TTL_AMNT) {
   	
   	  this.SR_INV_TTL_AMNT = SR_INV_TTL_AMNT;
   	
   }
   
   public String getSrCstName() {
   	
   	  return SR_CST_NM;
   	
   }
   
   public void setSrCstName(String SR_CST_NM) {
   	
   	  this.SR_CST_NM = SR_CST_NM;
   	
   }
   
   public String getSrCstCustomerCode2() {
   	
   	  return SR_CST_CSTMR_CODE2;
   	
   }
   
   public void setSrCstCustomerCode2(String SR_CST_CSTMR_CODE2) {
   	
   	  this.SR_CST_CSTMR_CODE2 = SR_CST_CSTMR_CODE2;
   	
   }
   
   public String getSrRctType() {
   	
   	  return SR_RCT_TYP;
   	  
   }
   
   public void setSrRctType(String SR_RCT_TYP) {
   	  
   	  this.SR_RCT_TYP = SR_RCT_TYP;
   	  
   }
   
   public double getSrDiscountAmount() {
	   
	   return SR_DSCNT_AMNT;
	   
   }
   
   public void setSrDiscountAmount(double SR_DSCNT_AMNT) {
	   
	   this.SR_DSCNT_AMNT = SR_DSCNT_AMNT;
	   
   }
   
   public String getSrInvReceiptNumbers() {

	   return SR_INV_RCPT_NMBRS;

   }

   public void setSrInvReceiptNumbers(String SR_INV_RCPT_NMBRS) {

	   this.SR_INV_RCPT_NMBRS = SR_INV_RCPT_NMBRS;

   }
   
   public String getSrInvReceiptDates() {

	   return SR_INV_RCPT_DTS;

   }

   public void setSrInvReceiptDates(String SR_INV_RCPT_DTS) {

	   this.SR_INV_RCPT_DTS = SR_INV_RCPT_DTS;

   }

   public byte getPosted() {

	   return posted;

   }

   public void setPosted(byte posted) {

	   this.posted = posted;
	   
   }
   
   public String getCmInvoiceNumber() {

	   return INV_CM_INVC_NMBR;

   }

   public void setCmInvoiceNumber(String INV_CM_INVC_NMBR) {

	   this.INV_CM_INVC_NMBR = INV_CM_INVC_NMBR;

   }

   public double getSrAiApplyAmount() {
	   
	   return SR_AI_APPLY_AMNT;
	   
   }
	   
   public void setSrAiApplyAmount(double SR_AI_APPLD_AMNT) {
	   
	   this.SR_AI_APPLY_AMNT = SR_AI_APPLD_AMNT;
	   
   }
   
   public double getSrAiCreditableWTax() {
	   
	   return SR_AI_CRDTBL_W_TXAMNT;
	   
   }
	   
   public void setSrAiCreditableWTax(double SR_AI_CRDTBL_W_TXAMNT) {
	   
	   this.SR_AI_CRDTBL_W_TXAMNT = SR_AI_CRDTBL_W_TXAMNT;
	   
   }
   
   public double getSrAiDiscountAmount() {
	   
	   return SR_AI_DSCNT_AMNT;
	   
   }
	   
   public void setSrAiDiscountAmount(double SR_AI_DSCNT_AMNT) {
	   
	   this.SR_AI_DSCNT_AMNT = SR_AI_DSCNT_AMNT;
	   
   }
   
   public double getSrAiAppliedDeposit() {
	   
	   return SR_AI_APPLD_DPST;
	   
   }
	   
   public void setSrAiAppliedDeposit(double SR_AI_APPLD_DPST) {

	   this.SR_AI_APPLD_DPST = SR_AI_APPLD_DPST;

   }

   public double getSrQuantity() {

	   return SR_QTY;

   }

   public void setSrQuantity(double SR_QTY) {

	   this.SR_QTY = SR_QTY;

   }
   
   public double getSrCpf() {

	   return SR_CPF;

   }

   public void setSrCpf(double SR_CPF) {

	   this.SR_CPF = SR_CPF;

   }
	
   public double getSrCstBalance() {
   	
   	  return SR_CST_BLNC;
   	  
   }
   
   public void setSrCstBalance(double SR_CST_BLNC) {
   	
   	  this.SR_CST_BLNC = SR_CST_BLNC;
   	  
   }
	
   public double getSrBalance() {
   	
   	  return SR_BLNC;
   	  
   }
   
   public void setSrBalance(double SR_BLNC) {
   	
   	  this.SR_BLNC = SR_BLNC;
   	  
   }
   public static Comparator MemoLineComparator = (SR, anotherSR) -> {

       String SR_CST_SLSPRSN1 = ((ArRepSalesRegisterDetails) SR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerType();
       Date SR_DT1 = ((ArRepSalesRegisterDetails) SR).getSrDate();
       String SR_INVC_NMBR1 = ((ArRepSalesRegisterDetails) SR).getSrInvoiceNumber();
       String SR_MMO_NM1 = ((ArRepSalesRegisterDetails) SR).getSrMemoName();

       String SR_CST_SLSPRSN2 = ((ArRepSalesRegisterDetails) anotherSR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerType();
       Date SR_DT2 = ((ArRepSalesRegisterDetails) anotherSR).getSrDate();
       String SR_INVC_NMBR2 = ((ArRepSalesRegisterDetails) anotherSR).getSrInvoiceNumber();
       String SR_MMO_NM2 = ((ArRepSalesRegisterDetails) anotherSR).getSrMemoName();
       String ORDER_BY = ((ArRepSalesRegisterDetails) SR).getOrderBy();

       if (!(SR_MMO_NM1.equals(SR_MMO_NM2))) {

           return SR_MMO_NM1.compareTo(SR_MMO_NM2);

       } else {

           /*if(ORDER_BY.equals("DATE") && !(SR_DT1.equals(SR_DT2))){

               return SR_DT1.compareTo(SR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SR_CST_CSTMR_TYP1.equals(SR_CST_CSTMR_TYP2))){

               return SR_CST_CSTMR_TYP1.compareTo(SR_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SR_CST_CSTMR_CODE1.equals(SR_CST_CSTMR_CODE2))){

               return SR_CST_CSTMR_CODE1.compareTo(SR_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("SALESPERSON") && !(SR_CST_SLSPRSN1.equals(SR_CST_SLSPRSN2))){

                  return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

              } else if(ORDER_BY.equals("INVOICE NUMBER") && !(SR_INVC_NMBR1.equals(SR_INVC_NMBR2))){

               return SR_INVC_NMBR1.compareTo(SR_INVC_NMBR2);

           }else {*/

               return SR_MMO_NM1.compareTo(SR_MMO_NM2);

           //}

       }

   };
   
   public static Comparator CustomerCodeComparator = (SR, anotherSR) -> {

       String SR_CST_SLSPRSN1 = ((ArRepSalesRegisterDetails) SR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerType();
       Date SR_DT1 = ((ArRepSalesRegisterDetails) SR).getSrDate();
       String SR_INVC_NMBR1 = ((ArRepSalesRegisterDetails) SR).getSrInvoiceNumber();
       String SR_MMO_NM1 = ((ArRepSalesRegisterDetails) SR).getSrMemoName();

       String SR_CST_SLSPRSN2 = ((ArRepSalesRegisterDetails) anotherSR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerType();
       Date SR_DT2 = ((ArRepSalesRegisterDetails) anotherSR).getSrDate();
       String SR_INVC_NMBR2 = ((ArRepSalesRegisterDetails) anotherSR).getSrInvoiceNumber();
       String SR_MMO_NM2 = ((ArRepSalesRegisterDetails) anotherSR).getSrMemoName();

       String ORDER_BY = ((ArRepSalesRegisterDetails) SR).getOrderBy();

       if (!(SR_CST_CSTMR_CODE1.equals(SR_CST_CSTMR_CODE2))) {

           return SR_CST_CSTMR_CODE1.compareTo(SR_CST_CSTMR_CODE2);

       } else {

           if(ORDER_BY.equals("DATE") && !(SR_DT1.equals(SR_DT2))){

               return SR_DT1.compareTo(SR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SR_CST_CSTMR_TYP1.equals(SR_CST_CSTMR_TYP2))){

               return SR_CST_CSTMR_TYP1.compareTo(SR_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("SALESPERSON") && !(SR_CST_SLSPRSN1.equals(SR_CST_SLSPRSN2))){

                  return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

              } else if(ORDER_BY.equals("MEMO LINE") && !(SR_MMO_NM1.equals(SR_MMO_NM2))){

            return SR_MMO_NM1.compareTo(SR_MMO_NM2);

       }else {

               return SR_INVC_NMBR1.compareTo(SR_INVC_NMBR2);

           }

       }

   };
   
   public static Comparator CustomerTypeComparator = (SR, anotherSR) -> {

       String SR_CST_SLSPRSN1 = ((ArRepSalesRegisterDetails) SR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerType();
       Date SR_DT1 = ((ArRepSalesRegisterDetails) SR).getSrDate();
       String SR_INVC_NMBR1 = ((ArRepSalesRegisterDetails) SR).getSrInvoiceNumber();
       String SR_MMO_NM1 = ((ArRepSalesRegisterDetails) SR).getSrMemoName();

       String SR_CST_SLSPRSN2 = ((ArRepSalesRegisterDetails) anotherSR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerType();
       Date SR_DT2 = ((ArRepSalesRegisterDetails) anotherSR).getSrDate();
       String SR_INVC_NMBR2 = ((ArRepSalesRegisterDetails) anotherSR).getSrInvoiceNumber();
       String SR_MMO_NM2 = ((ArRepSalesRegisterDetails) anotherSR).getSrMemoName();

       String ORDER_BY = ((ArRepSalesRegisterDetails) SR).getOrderBy();

       if (!(SR_CST_CSTMR_TYP1.equals(SR_CST_CSTMR_TYP2))) {

           return SR_CST_CSTMR_TYP1.compareTo(SR_CST_CSTMR_TYP2);

       } else {

           if(ORDER_BY.equals("DATE") && !(SR_DT1.equals(SR_DT2))){

               return SR_DT1.compareTo(SR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SR_CST_CSTMR_CODE1.equals(SR_CST_CSTMR_CODE2))){

               return SR_CST_CSTMR_CODE1.compareTo(SR_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("SALESPERSON") && !(SR_CST_SLSPRSN1.equals(SR_CST_SLSPRSN2))){

                  return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

              } else if(ORDER_BY.equals("MEMO LINE") && !(SR_MMO_NM1.equals(SR_MMO_NM2))){

            return SR_MMO_NM1.compareTo(SR_MMO_NM2);

       }else {

               return SR_INVC_NMBR1.compareTo(SR_INVC_NMBR2);

           }

       }

   };
   
   public static Comparator CustomerClassComparator = (SR, anotherSR) -> {

       String SR_CST_SLSPRSN1 = ((ArRepSalesRegisterDetails) SR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CLSS1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerClass();
       String SR_CST_CSTMR_CODE1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerType();
       Date SR_DT1 = ((ArRepSalesRegisterDetails) SR).getSrDate();
       String SR_INVC_NMBR1 = ((ArRepSalesRegisterDetails) SR).getSrInvoiceNumber();
       String SR_MMO_NM1 = ((ArRepSalesRegisterDetails) SR).getSrMemoName();

       String SR_CST_SLSPRSN2 = ((ArRepSalesRegisterDetails) anotherSR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CLSS2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerClass();
       String SR_CST_CSTMR_CODE2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerType();
       Date SR_DT2 = ((ArRepSalesRegisterDetails) anotherSR).getSrDate();
       String SR_INVC_NMBR2 = ((ArRepSalesRegisterDetails) anotherSR).getSrInvoiceNumber();
       String SR_MMO_NM2 = ((ArRepSalesRegisterDetails) anotherSR).getSrMemoName();

       String ORDER_BY = ((ArRepSalesRegisterDetails) SR).getOrderBy();

       if (!(SR_CST_CSTMR_CLSS1.equals(SR_CST_CSTMR_CLSS2))) {

           return SR_CST_CSTMR_CLSS1.compareTo(SR_CST_CSTMR_CLSS2);

       } else {

           if(ORDER_BY.equals("DATE") && !(SR_DT1.equals(SR_DT2))){

               return SR_DT1.compareTo(SR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SR_CST_CSTMR_CODE1.equals(SR_CST_CSTMR_CODE2))){

               return SR_CST_CSTMR_CODE1.compareTo(SR_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SR_CST_CSTMR_TYP1.equals(SR_CST_CSTMR_TYP2))){

               return SR_CST_CSTMR_TYP1.compareTo(SR_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("SALESPERSON") && !(SR_CST_SLSPRSN1.equals(SR_CST_SLSPRSN2))){

                  return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

              } else if(ORDER_BY.equals("MEMO LINE") && !(SR_MMO_NM1.equals(SR_MMO_NM2))){

            return SR_MMO_NM1.compareTo(SR_MMO_NM2);

       }else {

               return SR_INVC_NMBR1.compareTo(SR_INVC_NMBR2);

           }

       }

   };
   public static Comparator ProductComparator = (SR, anotherSR) -> {

       String SR_CST_PRODUCT1 = ((ArRepSalesRegisterDetails) SR).getSrDescription();
       String SR_CST_SLSPRSN1 = ((ArRepSalesRegisterDetails) SR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CLSS1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerClass();
       String SR_CST_CSTMR_CODE1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerType();
       Date SR_DT1 = ((ArRepSalesRegisterDetails) SR).getSrDate();
       String SR_INVC_NMBR1 = ((ArRepSalesRegisterDetails) SR).getSrInvoiceNumber();
       String SR_MMO_NM1 = ((ArRepSalesRegisterDetails) SR).getSrMemoName();

       String SR_CST_PRODUCT2 = ((ArRepSalesRegisterDetails) anotherSR).getSrDescription();
       String SR_CST_SLSPRSN2 = ((ArRepSalesRegisterDetails) anotherSR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CLSS2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerClass();
       String SR_CST_CSTMR_CODE2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerType();
       Date SR_DT2 = ((ArRepSalesRegisterDetails) anotherSR).getSrDate();
       String SR_INVC_NMBR2 = ((ArRepSalesRegisterDetails) anotherSR).getSrInvoiceNumber();
       String SR_MMO_NM2 = ((ArRepSalesRegisterDetails) anotherSR).getSrMemoName();

       String ORDER_BY = ((ArRepSalesRegisterDetails) SR).getOrderBy();

       if (!(SR_CST_PRODUCT1.equals(SR_CST_PRODUCT2))) {

           return SR_CST_PRODUCT1.compareTo(SR_CST_PRODUCT2);

       } else {

           if(ORDER_BY.equals("DATE") && !(SR_DT1.equals(SR_DT2))){

               return SR_DT1.compareTo(SR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SR_CST_CSTMR_CODE1.equals(SR_CST_CSTMR_CODE2))){

               return SR_CST_CSTMR_CODE1.compareTo(SR_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SR_CST_CSTMR_TYP1.equals(SR_CST_CSTMR_TYP2))){

               return SR_CST_CSTMR_TYP1.compareTo(SR_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("SALESPERSON") && !(SR_CST_SLSPRSN1.equals(SR_CST_SLSPRSN2))){

                  return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

              }  else if(ORDER_BY.equals("MEMO LINE") && !(SR_MMO_NM1.equals(SR_MMO_NM2))){

            return SR_MMO_NM1.compareTo(SR_MMO_NM2);

       }else {

               return SR_INVC_NMBR1.compareTo(SR_INVC_NMBR2);

           }

       }

   };
   public static Comparator SalespersonComparator = (SR, anotherSR) -> {

       String SR_CST_SLSPRSN1 = ((ArRepSalesRegisterDetails) SR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerType();
       Date SR_DT1 = ((ArRepSalesRegisterDetails) SR).getSrDate();
       String SR_INVC_NMBR1 = ((ArRepSalesRegisterDetails) SR).getSrInvoiceNumber();
       String SR_MMO_NM1 = ((ArRepSalesRegisterDetails) SR).getSrMemoName();

       String SR_CST_SLSPRSN2 = ((ArRepSalesRegisterDetails) anotherSR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerType();
       Date SR_DT2 = ((ArRepSalesRegisterDetails) anotherSR).getSrDate();
       String SR_INVC_NMBR2 = ((ArRepSalesRegisterDetails) anotherSR).getSrInvoiceNumber();
       String SR_MMO_NM2 = ((ArRepSalesRegisterDetails) anotherSR).getSrMemoName();
       String ORDER_BY = ((ArRepSalesRegisterDetails) SR).getOrderBy();

       String test = "";

   try{
      if (SR_CST_SLSPRSN1==null) {

          SR_CST_SLSPRSN1=test;
          SR_CST_SLSPRSN2=test;
          return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

      } else if (SR_CST_SLSPRSN2==null) {

          SR_CST_SLSPRSN1=test;
          SR_CST_SLSPRSN2=test;
          return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

      }
          else {

          return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);
      }
  } catch(Exception e) {

      test="0";

  }

       if (!(SR_CST_SLSPRSN1.equals(SR_CST_SLSPRSN2))) {

           return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

       } else {

           if(ORDER_BY.equals("DATE") && !(SR_DT1.equals(SR_DT2))){

               return SR_DT1.compareTo(SR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SR_CST_CSTMR_TYP1.equals(SR_CST_CSTMR_TYP2))){

               return SR_CST_CSTMR_TYP1.compareTo(SR_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SR_CST_CSTMR_CODE1.equals(SR_CST_CSTMR_CODE2))){

               return SR_CST_CSTMR_CODE1.compareTo(SR_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("SALESPERSON") && !(SR_CST_SLSPRSN1.equals(SR_CST_SLSPRSN2))){

               return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

        }else if(ORDER_BY.equals("") && !(SR_CST_SLSPRSN1.equals(SR_CST_SLSPRSN2))){

               return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

        } else if(ORDER_BY.equals("MEMO LINE") && !(SR_MMO_NM1.equals(SR_MMO_NM2))){

            return SR_MMO_NM1.compareTo(SR_MMO_NM2);

       } else {

               return SR_INVC_NMBR1.compareTo(SR_INVC_NMBR2);

           }

       }


   };
   public static Comparator NoGroupComparator = (SR, anotherSR) -> {

       String SR_CST_SLSPRSN1 = ((ArRepSalesRegisterDetails) SR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerType();
       Date SR_DT1 = ((ArRepSalesRegisterDetails) SR).getSrDate();
       String SR_INVC_NMBR1 = ((ArRepSalesRegisterDetails) SR).getSrInvoiceNumber();
       String SR_MMO_NM1 = ((ArRepSalesRegisterDetails) SR).getSrMemoName();

       String SR_CST_SLSPRSN2 = ((ArRepSalesRegisterDetails) anotherSR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerType();
       Date SR_DT2 = ((ArRepSalesRegisterDetails) anotherSR).getSrDate();
       String SR_INVC_NMBR2 = ((ArRepSalesRegisterDetails) anotherSR).getSrInvoiceNumber();
       String SR_MMO_NM2 = ((ArRepSalesRegisterDetails) anotherSR).getSrMemoName();

       String ORDER_BY = ((ArRepSalesRegisterDetails) SR).getOrderBy();

       if(ORDER_BY.equals("DATE") && !(SR_DT1.equals(SR_DT2))){

           return SR_DT1.compareTo(SR_DT2);

       } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SR_CST_CSTMR_CODE1.equals(SR_CST_CSTMR_CODE2))){

           return SR_CST_CSTMR_CODE1.compareTo(SR_CST_CSTMR_CODE2);

       } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SR_CST_CSTMR_TYP1.equals(SR_CST_CSTMR_TYP2))){

           return SR_CST_CSTMR_TYP1.compareTo(SR_CST_CSTMR_TYP2);

       } else if(ORDER_BY.equals("SALESPERSON") && !(SR_CST_SLSPRSN1.equals(SR_CST_SLSPRSN2))){

           return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

       } else if(ORDER_BY.equals("MEMO LINE") && !(SR_MMO_NM1.equals(SR_MMO_NM2))){

            return SR_MMO_NM1.compareTo(SR_MMO_NM2);

    }else {

           return SR_INVC_NMBR1.compareTo(SR_INVC_NMBR2);

       }

   };
   
   public static Comparator CoaAccountNumberComparator = (SR, anotherSR) -> {

       String SR_CST_SLSPRSN1 = ((ArRepSalesRegisterDetails) SR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP1 = ((ArRepSalesRegisterDetails) SR).getSrCstCustomerType();
       Date SR_DT1 = ((ArRepSalesRegisterDetails) SR).getSrDate();
       String SR_INVC_NMBR1 = ((ArRepSalesRegisterDetails) SR).getSrInvoiceNumber();
       String SR_DR_COA_ACCNT_NMBR1 = ((ArRepSalesRegisterDetails) SR).getSrDrGlAccountNumber();
       String SR_MMO_NM1 = ((ArRepSalesRegisterDetails) SR).getSrMemoName();

       String SR_CST_SLSPRSN2 = ((ArRepSalesRegisterDetails) anotherSR).getSrSlsSalespersonCode();
       String SR_CST_CSTMR_CODE2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerCode();
       String SR_CST_CSTMR_TYP2 = ((ArRepSalesRegisterDetails) anotherSR).getSrCstCustomerType();
       Date SR_DT2 = ((ArRepSalesRegisterDetails) anotherSR).getSrDate();
       String SR_INVC_NMBR2 = ((ArRepSalesRegisterDetails) anotherSR).getSrInvoiceNumber();
       String SR_DR_COA_ACCNT_NMBR2 = ((ArRepSalesRegisterDetails) anotherSR).getSrDrGlAccountNumber();
       String SR_MMO_NM2 = ((ArRepSalesRegisterDetails) anotherSR).getSrMemoName();

       String ORDER_BY = ((ArRepSalesRegisterDetails) SR).getOrderBy();

       if (!(SR_DR_COA_ACCNT_NMBR1.equals(SR_DR_COA_ACCNT_NMBR2))) {

           return SR_DR_COA_ACCNT_NMBR1.compareTo(SR_DR_COA_ACCNT_NMBR2);

       } else {

           if(ORDER_BY.equals("DATE") && !(SR_DT1.equals(SR_DT2))){

               return SR_DT1.compareTo(SR_DT2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(SR_CST_CSTMR_CODE1.equals(SR_CST_CSTMR_CODE2))){

               return SR_CST_CSTMR_CODE1.compareTo(SR_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(SR_CST_CSTMR_TYP1.equals(SR_CST_CSTMR_TYP2))){

               return SR_CST_CSTMR_TYP1.compareTo(SR_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("SALESPERSON") && !(SR_CST_SLSPRSN1.equals(SR_CST_SLSPRSN2))){

               return SR_CST_SLSPRSN1.compareTo(SR_CST_SLSPRSN2);

           } else if(ORDER_BY.equals("MEMO LINE") && !(SR_MMO_NM1.equals(SR_MMO_NM2))){

                return SR_MMO_NM1.compareTo(SR_MMO_NM2);

           }else {

               return SR_INVC_NMBR1.compareTo(SR_INVC_NMBR2);

           }

       }

   };
   
} // ArRepSalesRegisterDetails class