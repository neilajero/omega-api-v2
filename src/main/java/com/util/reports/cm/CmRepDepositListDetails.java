/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.cm;

import java.util.Comparator;
import java.util.Date;

public class CmRepDepositListDetails implements java.io.Serializable {

   private Date DL_DT;
   private String DL_RCPT_NMBR;
   private String DL_RFRNC_NMBR;
   private String DL_DESC;
   private String DL_CST_CSTMR_CODE;
   private String DL_CST_CSTMR_TYP;
   private String DL_CST_CSTMR_CLSS;
   private String DL_BNK_ACCNT;
   private double DL_AMNT;
   private double DL_AMNT_DPSTD;
   private String ORDER_BY;

   public CmRepDepositListDetails() {
   }

   public Date getDlDate() {
   	
   	  return DL_DT;
   	
   }
   
   public void setDlDate(Date DL_DT) {
   	
   	  this.DL_DT = DL_DT;
   	
   }
   
   public String getDlReceiptNumber() {
   	
   	  return DL_RCPT_NMBR;
   	
   }
   
   public void setDlReceiptNumber(String DL_RCPT_NMBR) {
   	
   	  this.DL_RCPT_NMBR = DL_RCPT_NMBR;
   	
   }
   
   public String getDlReferenceNumber() {
   	
   	  return DL_RFRNC_NMBR;
   	  
   }
   
   public void setDlReferenceNumber(String DL_RFRNC_NMBR) {
   	
   	  this.DL_RFRNC_NMBR = DL_RFRNC_NMBR;
   	  
   }
   
   public String getDlDescription() {
   	
   	  return DL_DESC;
   	  
   }
   
   public void setDlDescription(String DL_DESC) {
   	
   	  this.DL_DESC = DL_DESC;
   	  
   }

   public String getDlCstCustomerCode() {
   	
   	  return DL_CST_CSTMR_CODE;
   	
   }
   
   public void setDlCstCustomerCode(String DL_CST_CSTMR_CODE) {
   	
   	  this.DL_CST_CSTMR_CODE = DL_CST_CSTMR_CODE;
   	
   }
   
   public String getDlCstCustomerType() {
   	
   	  return DL_CST_CSTMR_TYP;
   	
   }
   
   public void setDlCstCustomerType(String DL_CST_CSTMR_TYP) {
   	
   	  this.DL_CST_CSTMR_TYP = DL_CST_CSTMR_TYP;
   	
   }
   public String getDlCstCustomerClass() {
   	
   	  return DL_CST_CSTMR_CLSS;
   	
   }
   
   public void setDlCstCustomerClass(String DL_CST_CSTMR_CLSS) {
   	
   	  this.DL_CST_CSTMR_CLSS = DL_CST_CSTMR_CLSS;
   	
   }
   
   public String getDlBankAccount() {
   	
   	  return DL_BNK_ACCNT;
   	  
   }
   
   public void setDlBankAccount(String DL_BNK_ACCNT) {
   	
   	  this.DL_BNK_ACCNT = DL_BNK_ACCNT;
   	  
   }
   
   public double getDlAmount() {
   	
   	  return DL_AMNT;
   	  
   }
   
   public void setDlAmount(double DL_AMNT) {
   	
   	  this.DL_AMNT = DL_AMNT;
   	  
   }

   public double getDlAmountDeposited() {
   	
   	  return DL_AMNT_DPSTD;
   	  
   }
   
   public void setDlAmountDeposited(double DL_AMNT_DPSTD) {
   	
   	  this.DL_AMNT_DPSTD = DL_AMNT_DPSTD;
   	  
   }
   
   public String getOrderBy() {
   	
   	  return ORDER_BY;
   	  
   }
   
   public void setOrderBy(String ORDER_BY) {
   	
   	  this.ORDER_BY = ORDER_BY;
   	  
   }

   public static Comparator CustomerCodeComparator = (OR, anotherOR) -> {

       String DL_CST_CSTMR_CODE1 = ((CmRepDepositListDetails) OR).getDlCstCustomerCode();
       String DL_CST_CSTMR_TYP1 = ((CmRepDepositListDetails) OR).getDlCstCustomerType();
       Date DL_DT1 = ((CmRepDepositListDetails) OR).getDlDate();
       String DL_RCPT_NMBR1 = ((CmRepDepositListDetails) OR).getDlReceiptNumber();
       String DL_BNK_ACCNT1 = ((CmRepDepositListDetails) OR).getDlBankAccount();

       String DL_CST_CSTMR_CODE2 = ((CmRepDepositListDetails) anotherOR).getDlCstCustomerCode();
       String DL_CST_CSTMR_TYP2 = ((CmRepDepositListDetails) anotherOR).getDlCstCustomerType();
       Date DL_DT2 = ((CmRepDepositListDetails) anotherOR).getDlDate();
       String DL_RCPT_NMBR2 = ((CmRepDepositListDetails) anotherOR).getDlReceiptNumber();
       String DL_BNK_ACCNT2 = ((CmRepDepositListDetails) anotherOR).getDlBankAccount();

       String ORDER_BY = ((CmRepDepositListDetails) OR).getOrderBy();

       if (!(DL_CST_CSTMR_CODE1.equals(DL_CST_CSTMR_CODE2))) {

           return DL_CST_CSTMR_CODE1.compareTo(DL_CST_CSTMR_CODE2);

       } else {

           if(ORDER_BY.equals("DATE") && !(DL_DT1.equals(DL_DT2))){

               return DL_DT1.compareTo(DL_DT2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(DL_CST_CSTMR_TYP1.equals(DL_CST_CSTMR_TYP2))){

               return DL_CST_CSTMR_TYP1.compareTo(DL_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(DL_BNK_ACCNT1.equals(DL_BNK_ACCNT2))){

               return DL_BNK_ACCNT1.compareTo(DL_BNK_ACCNT2);

           } else {

               return DL_RCPT_NMBR1.compareTo(DL_RCPT_NMBR2);

           }

       }

   };
   
   public static Comparator CustomerTypeComparator = (OR, anotherOR) -> {

       String DL_CST_CSTMR_CODE1 = ((CmRepDepositListDetails) OR).getDlCstCustomerCode();
       String DL_CST_CSTMR_TYP1 = ((CmRepDepositListDetails) OR).getDlCstCustomerType();
       Date DL_DT1 = ((CmRepDepositListDetails) OR).getDlDate();
       String DL_RCPT_NMBR1 = ((CmRepDepositListDetails) OR).getDlReceiptNumber();
       String DL_BNK_ACCNT1 = ((CmRepDepositListDetails) OR).getDlBankAccount();

       String DL_CST_CSTMR_CODE2 = ((CmRepDepositListDetails) anotherOR).getDlCstCustomerCode();
       String DL_CST_CSTMR_TYP2 = ((CmRepDepositListDetails) anotherOR).getDlCstCustomerType();
       Date DL_DT2 = ((CmRepDepositListDetails) anotherOR).getDlDate();
       String DL_RCPT_NMBR2 = ((CmRepDepositListDetails) anotherOR).getDlReceiptNumber();
       String DL_BNK_ACCNT2 = ((CmRepDepositListDetails) anotherOR).getDlBankAccount();

       String ORDER_BY = ((CmRepDepositListDetails) OR).getOrderBy();

       if (!(DL_CST_CSTMR_TYP1.equals(DL_CST_CSTMR_TYP2))) {

           return DL_CST_CSTMR_TYP1.compareTo(DL_CST_CSTMR_TYP2);

       } else {

           if(ORDER_BY.equals("DATE") && !(DL_DT1.equals(DL_DT2))){

               return DL_DT1.compareTo(DL_DT2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(DL_CST_CSTMR_CODE1.equals(DL_CST_CSTMR_CODE2))){

               return DL_CST_CSTMR_CODE1.compareTo(DL_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(DL_BNK_ACCNT1.equals(DL_BNK_ACCNT2))){

               return DL_BNK_ACCNT1.compareTo(DL_BNK_ACCNT2);

           } else {

               return DL_RCPT_NMBR1.compareTo(DL_RCPT_NMBR2);

           }

       }

   };
   
   public static Comparator CustomerClassComparator = (OR, anotherOR) -> {

       String DL_CST_CSTMR_CLSS1 = ((CmRepDepositListDetails) OR).getDlCstCustomerClass();
       String DL_CST_CSTMR_CODE1 = ((CmRepDepositListDetails) OR).getDlCstCustomerCode();
       String DL_CST_CSTMR_TYP1 = ((CmRepDepositListDetails) OR).getDlCstCustomerType();
       Date DL_DT1 = ((CmRepDepositListDetails) OR).getDlDate();
       String DL_RCPT_NMBR1 = ((CmRepDepositListDetails) OR).getDlReceiptNumber();
       String DL_BNK_ACCNT1 = ((CmRepDepositListDetails) OR).getDlBankAccount();

       String DL_CST_CSTMR_CLSS2 = ((CmRepDepositListDetails) anotherOR).getDlCstCustomerClass();
       String DL_CST_CSTMR_CODE2 = ((CmRepDepositListDetails) anotherOR).getDlCstCustomerCode();
       String DL_CST_CSTMR_TYP2 = ((CmRepDepositListDetails) anotherOR).getDlCstCustomerType();
       Date DL_DT2 = ((CmRepDepositListDetails) anotherOR).getDlDate();
       String DL_RCPT_NMBR2 = ((CmRepDepositListDetails) anotherOR).getDlReceiptNumber();
       String DL_BNK_ACCNT2 = ((CmRepDepositListDetails) anotherOR).getDlBankAccount();

       String ORDER_BY = ((CmRepDepositListDetails) OR).getOrderBy();

       if (!(DL_CST_CSTMR_CLSS1.equals(DL_CST_CSTMR_CLSS2))) {

           return DL_CST_CSTMR_CLSS1.compareTo(DL_CST_CSTMR_CLSS2);

       } else {

           if(ORDER_BY.equals("DATE") && !(DL_DT1.equals(DL_DT2))){

               return DL_DT1.compareTo(DL_DT2);

           } else if(ORDER_BY.equals("CUSTOMER CODE") && !(DL_CST_CSTMR_CODE1.equals(DL_CST_CSTMR_CODE2))){

               return DL_CST_CSTMR_CODE1.compareTo(DL_CST_CSTMR_CODE2);

           } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(DL_CST_CSTMR_TYP1.equals(DL_CST_CSTMR_TYP2))){

               return DL_CST_CSTMR_TYP1.compareTo(DL_CST_CSTMR_TYP2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(DL_BNK_ACCNT1.equals(DL_BNK_ACCNT2))){

               return DL_BNK_ACCNT1.compareTo(DL_BNK_ACCNT2);

           } else {

               return DL_RCPT_NMBR1.compareTo(DL_RCPT_NMBR2);

           }

       }

   };
   
   public static Comparator NoGroupComparator = (OR, anotherOR) -> {

       String DL_CST_CSTMR_CODE1 = ((CmRepDepositListDetails) OR).getDlCstCustomerCode();
       String DL_CST_CSTMR_TYP1 = ((CmRepDepositListDetails) OR).getDlCstCustomerType();
       Date DL_DT1 = ((CmRepDepositListDetails) OR).getDlDate();
       String DL_RCPT_NMBR1 = ((CmRepDepositListDetails) OR).getDlReceiptNumber();
       String DL_BNK_ACCNT1 = ((CmRepDepositListDetails) OR).getDlBankAccount();

       String DL_CST_CSTMR_CODE2 = ((CmRepDepositListDetails) anotherOR).getDlCstCustomerCode();
       String DL_CST_CSTMR_TYP2 = ((CmRepDepositListDetails) anotherOR).getDlCstCustomerType();
       Date DL_DT2 = ((CmRepDepositListDetails) anotherOR).getDlDate();
       String DL_RCPT_NMBR2 = ((CmRepDepositListDetails) anotherOR).getDlReceiptNumber();
       String DL_BNK_ACCNT2 = ((CmRepDepositListDetails) anotherOR).getDlBankAccount();

       String ORDER_BY = ((CmRepDepositListDetails) OR).getOrderBy();

       if(ORDER_BY.equals("DATE") && !(DL_DT1.equals(DL_DT2))){

           return DL_DT1.compareTo(DL_DT2);

       } else if(ORDER_BY.equals("CUSTOMER CODE") && !(DL_CST_CSTMR_CODE1.equals(DL_CST_CSTMR_CODE2))){

           return DL_CST_CSTMR_CODE1.compareTo(DL_CST_CSTMR_CODE2);

       } else if(ORDER_BY.equals("CUSTOMER TYPE") && !(DL_CST_CSTMR_TYP1.equals(DL_CST_CSTMR_TYP2))){

           return DL_CST_CSTMR_TYP1.compareTo(DL_CST_CSTMR_TYP2);

       } else if(ORDER_BY.equals("BANK ACCOUNT") && !(DL_BNK_ACCNT1.equals(DL_BNK_ACCNT2))){

        return DL_BNK_ACCNT1.compareTo(DL_BNK_ACCNT2);

    } else {

           return DL_RCPT_NMBR1.compareTo(DL_RCPT_NMBR2);

       }

   };

} // ArRepOrRegisterDetails class