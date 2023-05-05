/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import com.util.Debug;

import java.util.Comparator;
import java.util.Date;

public class ApRepCheckRegisterDetails implements java.io.Serializable {

	   private Date CR_DT;
	   private String CR_CHK_NMBR;
	   private String CR_RFRNC_NMBR;
	   private String CR_DCMNT_NMBR;
	   private String CR_DESC;
	   private String CR_SPL_SPPLR_CODE;
	   private String CR_SPL_SPPLR_TYP;
	   private String CR_SPL_SPPLR_CLSS;
	   private String CR_BNK_ACCNT;
	   private double CR_AMNT;
	   private String ORDER_BY;
	   private String CR_DR_GL_COA_ACCNT_NMBR;
	   private String CR_DR_GL_COA_ACCNT_DESC;
	   private double CR_DR_DEBIT;
	   private double CR_DR_CREDIT;
	   private String CR_SPL_SPPLR_CODE2;
	   private String CR_SPL_NM;
	   private Date CR_CHK_DT;
	   private Byte CR_CHK_VD;
	   private Byte CR_CHK_VD_PSTD;
	   private char CR_FC_SYMBL;
           
           private Byte CHK_INVT_IS;
           private Byte CHK_INVT_TB;
           private Date CHK_INVT_STTLMNT_DT;
           private Date CHK_INVT_NXT_RN_DT;
           private Date CHK_INVT_MTRTY_DT;
           private Double INVT_NUMBER_OF_DAYS;
           private Double CHK_INVT_BD_YLD;
           private Double CHK_INVT_CPN_RT;
           private Double CHK_INVT_STTLMNT_AMNT;
           private Double CHK_INVT_FC_VL;
           private Double CHK_INVT_PM_AMNT;
           
	   
	   public ApRepCheckRegisterDetails() {
       }

	   public Date getCrDate() {
	   	
	   	  return CR_DT;
	   	
	   }
	   
	   public void setCrDate(Date CR_DT) {
	   	
	   	  this.CR_DT = CR_DT;
	   	
	   }
	   
	   public Byte getCrCheckVoid() {
		   	
		   	  return CR_CHK_VD;
		   	
		   }
		   
	   public void setCrCheckVoid(Byte CR_CHK_VD) {
		   	
		   	  this.CR_CHK_VD = CR_CHK_VD;
		   	
	   }
	   
	   public Byte getCrCheckVoidPosted() {
		   	
		   	  return CR_CHK_VD_PSTD;
		   	
		   }
		   
	   public void setCrCheckVoidPosted(Byte CR_CHK_VD_PSTD) {
		   	
		   	  this.CR_CHK_VD_PSTD = CR_CHK_VD_PSTD;
		   	
	   }
	   
	   public String getCrCheckNumber() {
	   	
	   	  return CR_CHK_NMBR;
	   	
	   }
	   
	   public void setCrCheckNumber(String CR_CHK_NMBR) {
	   	
	   	  this.CR_CHK_NMBR = CR_CHK_NMBR;
	   	
	   }

	   public String getCrReferenceNumber() {
	   	
	   	  return CR_RFRNC_NMBR;
	   	  
	   }
	   
	   public void setCrReferenceNumber(String CR_RFRNC_NMBR) {
	   	
	   	  this.CR_RFRNC_NMBR = CR_RFRNC_NMBR;
	   	  
	   }
	   
	   public String getCrDocumentNumber() {
	   	
	   	  return CR_DCMNT_NMBR;
	   	  
	   }
	   
	   public void setCrDocumentNumber(String CR_DCMNT_NMBR) {
	   	
	   	  this.CR_DCMNT_NMBR = CR_DCMNT_NMBR;
	   	  
	   }
	   
	   public String getCrDescription() {
	   	
	   	  return CR_DESC;
	   	  
	   }
	   
	   public void setCrDescription(String CR_DESC) {
	   	
	   	  this.CR_DESC = CR_DESC;
	   	  
	   }
	   
	   public String getCrSplSupplierCode() {
	   	
	   	  return CR_SPL_SPPLR_CODE;
	   	
	   }
	   
	   public void setCrSplSupplierCode(String CR_SPL_SPPLR_CODE) {
	   	
	   	  this.CR_SPL_SPPLR_CODE = CR_SPL_SPPLR_CODE;
	   	
	   }
	   
	   public String getCrSplSupplierType() {
	   	
	   	  return CR_SPL_SPPLR_TYP;
	   	
	   }
	   
	   public void setCrSplSupplierType(String CR_SPL_SPPLR_TYP) {
	   	
	   	  this.CR_SPL_SPPLR_TYP = CR_SPL_SPPLR_TYP;
	   	
	   }
	   
	   public String getCrSplSupplierClass() {
	   	
	   	  return CR_SPL_SPPLR_CLSS;
	   	
	   }
	   
	   public void setCrSplSupplierClass(String CR_SPL_SPPLR_CLSS) {
	   	
	   	  this.CR_SPL_SPPLR_CLSS = CR_SPL_SPPLR_CLSS;
	   	
	   }
	   
	   public String getCrBankAccount() {
	   	
	   	  return CR_BNK_ACCNT;
	   	
	   }
	   
	   public void setCrBankAccount(String CR_BNK_ACCNT) {
	   	
	   	  this.CR_BNK_ACCNT = CR_BNK_ACCNT;
	   	
	   }
	   
	   public double getCrAmount() {
	   	
	   	  return CR_AMNT;
	   	  
	   }
	   
	   public void setCrAmount(double CR_AMNT) {
	   	
	   	  this.CR_AMNT = CR_AMNT;
	   	  
	   }
	   
	   public String getOrderBy() {
	   	
	   	  return ORDER_BY;
	   	
	   }
	   
	   public void setOrderBy(String ORDER_BY) {
	   	
	   	  this.ORDER_BY = ORDER_BY;
	   	
	   }
	   
	   public String getCrDrGlCoaAccountNumber() {
	   	
	   	  return CR_DR_GL_COA_ACCNT_NMBR;
	   	
	   }
	   
	   public void setCrDrGlCoaAccountNumber(String CR_DR_GL_COA_ACCNT_NMBR) {
	   	
	   	  this.CR_DR_GL_COA_ACCNT_NMBR = CR_DR_GL_COA_ACCNT_NMBR;
	   	
	   }
	   
	   public String getCrDrGlCoaAccountDescription() {
	   	
	   	  return CR_DR_GL_COA_ACCNT_DESC;
	   	
	   }
	   
	   public void setCrDrGlCoaAccountDescription(String CR_DR_GL_COA_ACCNT_DESC) {
	   	
	   	  this.CR_DR_GL_COA_ACCNT_DESC = CR_DR_GL_COA_ACCNT_DESC;
	   	
	   }
	   
	   public double getCrDrDebit() {
	   	
	   	  return CR_DR_DEBIT;
	   	
	   }
	   
	   public void setCrDrDebit(double CR_DR_DEBIT) {
	   	
	   	  this.CR_DR_DEBIT = CR_DR_DEBIT;
	   	
	   }
	   
	   public double getCrDrCredit() {
	   	
	   	  return CR_DR_CREDIT;
	   	
	   }
	   
	   public void setCrDrCredit(double CR_DR_CREDIT) {
	   	
	   	  this.CR_DR_CREDIT = CR_DR_CREDIT;
	   	
	   }

	   public String getCrSplSupplierCode2() {
	   	
	   	  return CR_SPL_SPPLR_CODE2;
	   	
	   }
	   
	   public void setCrSplSupplierCode2(String CR_SPL_SPPLR_CODE2) {
	   	
	   	  this.CR_SPL_SPPLR_CODE2 = CR_SPL_SPPLR_CODE2;
	   	
	   }
	   
	   public String getCrSplName() {
	   	
	   	  return CR_SPL_NM;
	   	
	   }
	   
	   public void setCrSplName(String CR_SPL_NM) {
	   	
	   	  this.CR_SPL_NM = CR_SPL_NM;
	   	
	   }

	   public Date getCrCheckDate() {

		   return CR_CHK_DT;

	   }

	   public void setCrCheckDate(Date CR_CHK_DT) {

		   this.CR_CHK_DT = CR_CHK_DT;

	   }

	   public char getCrFcSymbol(){

		   return CR_FC_SYMBL;	

	   }
	   
	   public void setCrFcSymbol(char CR_FC_SYMBL){

		   this.CR_FC_SYMBL = CR_FC_SYMBL; 

	   }
           
            public Byte getCrInvestInscribeStock(){

		   return CHK_INVT_IS;	

	   }
	   
	   public void setCrInvestInscribeStock(Byte CHK_INVT_IS){

		   this.CHK_INVT_IS = CHK_INVT_IS; 

	   }
            public Byte getCrInvestTreasuryBills(){

		   return CHK_INVT_TB;	

	   }
	   
	   public void setCrInvestTreasuryBills(Byte CHK_INVT_TB){

		   this.CHK_INVT_TB = CHK_INVT_TB; 

	   }
           
           
           
           public Date getCrInvestSettlementDate(){

		   return CHK_INVT_STTLMNT_DT;	

	   }
	   
	   public void setCrInvestSettlementDate(Date CHK_INVT_STTLMNT_DT){

		   this.CHK_INVT_STTLMNT_DT = CHK_INVT_STTLMNT_DT; 

	   }
           
           
           public Date getCrInvestNextRunDate(){

		   return CHK_INVT_NXT_RN_DT;	

	   }
	   
	   public void setCrInvestNextRunDate(Date CHK_INVT_NXT_RN_DT){

		   this.CHK_INVT_NXT_RN_DT = CHK_INVT_NXT_RN_DT; 

	   }
           
           public Date getCrInvestMaturityDate(){

		   return CHK_INVT_MTRTY_DT;	

	   }
	   
	   public void setCrInvestMaturityDate(Date CHK_INVT_MTRTY_DT){

		   this.CHK_INVT_MTRTY_DT = CHK_INVT_MTRTY_DT; 

	   }
           
            public Double getCrInvestNumberOfDays(){

		   return INVT_NUMBER_OF_DAYS;	

	   }
	   
	   public void setCrInvestNumberOfDays(Double INVT_NUMBER_OF_DAYS){

		   this.INVT_NUMBER_OF_DAYS = INVT_NUMBER_OF_DAYS; 

	   }
           
           public Double getCrInvestBidYield(){

		   return CHK_INVT_BD_YLD;	

	   }
	   
	   public void setCrInvestBidYield(Double CHK_INVT_BD_YLD){

		   this.CHK_INVT_BD_YLD = CHK_INVT_BD_YLD; 

	   }
           
           public Double getCrInvestCouponRate(){

		   return CHK_INVT_CPN_RT;	

	   }
	   
	   public void setCrInvestCouponRate(Double CHK_INVT_CPN_RT){

		   this.CHK_INVT_CPN_RT = CHK_INVT_CPN_RT; 

	   }
           
           public Double getCrInvestSettlementAmount(){

		   return CHK_INVT_STTLMNT_AMNT;	

	   }
	   
	   public void setCrInvestSettlementAmount(Double CHK_INVT_STTLMNT_AMNT){

		   this.CHK_INVT_STTLMNT_AMNT = CHK_INVT_STTLMNT_AMNT; 
           }
           
           public Double getCrInvestFaceValue(){

		   return CHK_INVT_FC_VL;	

	   }
	   
	   public void setCrInvestFaceValue(Double CHK_INVT_FC_VL){

		   this.CHK_INVT_FC_VL = CHK_INVT_FC_VL; 

	   }
           
           public Double getCrInvestPremAmount(){

		   return CHK_INVT_PM_AMNT;	

	   }
	   
	   public void setCrInvestPremAmount(Double CHK_INVT_PM_AMNT){

		   this.CHK_INVT_PM_AMNT = CHK_INVT_PM_AMNT; 

	   }
           
           
	   
	   	   
	   public static Comparator SupplierCodeComparator = (CR, anotherCR) -> {

           String CR_SPL_SPPLR_CODE1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierType();
           Date CR_DT1 = ((ApRepCheckRegisterDetails) CR).getCrDate();
           String CR_DCMNT_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrDocumentNumber();
           String CR_RFRNC_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrReferenceNumber();
           String CR_CHCK_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrCheckNumber();
           String CR_BNK_ACCNT1 = ((ApRepCheckRegisterDetails) CR).getCrBankAccount();
           Byte CR_VD = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();

           String CR_SPL_SPPLR_CODE2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierType();
           Date CR_DT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDate();
           String CR_DCMNT_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDocumentNumber();
           String CR_RFRNC_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrReferenceNumber();
           String CR_CHCK_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrCheckNumber();
           String CR_BNK_ACCNT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrBankAccount();
           Byte CR_VD2 = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();
           String ORDER_BY = ((ApRepCheckRegisterDetails) CR).getOrderBy();

           if (!(CR_SPL_SPPLR_CODE1.equals(CR_SPL_SPPLR_CODE2))) {

               return CR_SPL_SPPLR_CODE1.compareTo(CR_SPL_SPPLR_CODE2);

           } else {

               if(ORDER_BY.equals("DATE") && !(CR_DT1.equals(CR_DT2))){

                   return CR_DT1.compareTo(CR_DT2);

               } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(CR_SPL_SPPLR_TYP1.equals(CR_SPL_SPPLR_TYP2))){

                   return CR_SPL_SPPLR_TYP1.compareTo(CR_SPL_SPPLR_TYP2);

               } else if(ORDER_BY.equals("BANK ACCOUNT") && !(CR_BNK_ACCNT1.equals(CR_BNK_ACCNT2))){

                   return CR_BNK_ACCNT1.compareTo(CR_BNK_ACCNT2);

               } else if(ORDER_BY.equals("DOCUMENT NUMBER") && !(CR_DCMNT_NMBR1.equals(CR_DCMNT_NMBR2))){

                   return CR_DCMNT_NMBR1.compareTo(CR_DCMNT_NMBR2);

               } else {

                   return CR_CHCK_NMBR1.compareTo(CR_CHCK_NMBR2);

               }


           }

       };
	   
	   public static Comparator checkVoid = (CR, anotherCR) -> {


           String CR_SPL_SPPLR_CODE1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierType();
           Date CR_DT1 = ((ApRepCheckRegisterDetails) CR).getCrDate();
           String CR_DCMNT_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrDocumentNumber();
           String CR_RFRNC_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrReferenceNumber();
           String CR_CHCK_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrCheckNumber();
           String CR_BNK_ACCNT1 = ((ApRepCheckRegisterDetails) CR).getCrBankAccount();
           Byte CR_VD = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();


           String CR_SPL_SPPLR_CODE2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierType();
           Date CR_DT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDate();
           String CR_DCMNT_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDocumentNumber();
           String CR_RFRNC_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrReferenceNumber();
           String CR_CHCK_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrCheckNumber();
           String CR_BNK_ACCNT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrBankAccount();
           Byte CR_VD2 = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();

           String ORDER_BY = ((ApRepCheckRegisterDetails) CR).getOrderBy();

           if (!(CR_VD.equals(CR_VD2))) {

               return CR_VD2.compareTo(CR_VD);

           } else {

               if(ORDER_BY.equals("DATE") && !(CR_DT1.equals(CR_DT2))){

                   return CR_DT1.compareTo(CR_DT2);

               } else if(ORDER_BY.equals("SUPPLIER CODE") && !(CR_SPL_SPPLR_CODE1.equals(CR_SPL_SPPLR_CODE2))){

                   return CR_SPL_SPPLR_CODE1.compareTo(CR_SPL_SPPLR_CODE2);

               } else if(ORDER_BY.equals("BANK ACCOUNT") && !(CR_BNK_ACCNT1.equals(CR_BNK_ACCNT2))){

                   return CR_BNK_ACCNT1.compareTo(CR_BNK_ACCNT2);

               } else if(ORDER_BY.equals("DOCUMENT NUMBER") && !(CR_DCMNT_NMBR1.equals(CR_DCMNT_NMBR2))){

                   return CR_DCMNT_NMBR1.compareTo(CR_DCMNT_NMBR2);

               } else {

                   return CR_VD2.compareTo(CR_VD);

               }

           }
       };
		   
		   public static Comparator checkVoidPosted = (CR, anotherCR) -> {

               Byte CR_VD = ((ApRepCheckRegisterDetails) CR).getCrCheckVoidPosted();

               Byte CR_VD2 = ((ApRepCheckRegisterDetails) CR).getCrCheckVoidPosted();

                   return CR_VD2.compareTo(CR_VD);

           };
	   
	   public static Comparator SupplierTypeComparator = (CR, anotherCR) -> {

           String CR_SPL_SPPLR_CODE1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierType();
           Date CR_DT1 = ((ApRepCheckRegisterDetails) CR).getCrDate();
           String CR_DCMNT_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrDocumentNumber();
           String CR_RFRNC_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrReferenceNumber();
           String CR_CHCK_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrCheckNumber();
           String CR_BNK_ACCNT1 = ((ApRepCheckRegisterDetails) CR).getCrBankAccount();

           Byte CR_VD = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();
           String CR_SPL_SPPLR_CODE2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierType();
           Date CR_DT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDate();
           String CR_DCMNT_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDocumentNumber();
           String CR_RFRNC_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrReferenceNumber();
           String CR_CHCK_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrCheckNumber();
           String CR_BNK_ACCNT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrBankAccount();
           Byte CR_VD2 = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();

           String ORDER_BY = ((ApRepCheckRegisterDetails) CR).getOrderBy();

           if (!(CR_SPL_SPPLR_TYP1.equals(CR_SPL_SPPLR_TYP2))) {

               return CR_SPL_SPPLR_TYP1.compareTo(CR_SPL_SPPLR_TYP2);

           } else {

               if(ORDER_BY.equals("DATE") && !(CR_DT1.equals(CR_DT2))){

                   return CR_DT1.compareTo(CR_DT2);

               } else if(ORDER_BY.equals("SUPPLIER CODE") && !(CR_SPL_SPPLR_CODE1.equals(CR_SPL_SPPLR_CODE2))){

                   return CR_SPL_SPPLR_CODE1.compareTo(CR_SPL_SPPLR_CODE2);

               } else if(ORDER_BY.equals("BANK ACCOUNT") && !(CR_BNK_ACCNT1.equals(CR_BNK_ACCNT2))){

                   return CR_BNK_ACCNT1.compareTo(CR_BNK_ACCNT2);

               } else if(ORDER_BY.equals("DOCUMENT NUMBER") && !(CR_DCMNT_NMBR1.equals(CR_DCMNT_NMBR2))){

                   return CR_DCMNT_NMBR1.compareTo(CR_DCMNT_NMBR2);

               } else {

                   return CR_CHCK_NMBR1.compareTo(CR_CHCK_NMBR2);

               }

           }

       };
	   
	   public static Comparator SupplierClassComparator = (CR, anotherCR) -> {

           String CR_SPL_SPPLR_CLSS1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierClass();
           String CR_SPL_SPPLR_CODE1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierType();
           Date CR_DT1 = ((ApRepCheckRegisterDetails) CR).getCrDate();
           String CR_DCMNT_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrDocumentNumber();
           String CR_RFRNC_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrReferenceNumber();
           String CR_CHCK_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrCheckNumber();
           String CR_BNK_ACCNT1 = ((ApRepCheckRegisterDetails) CR).getCrBankAccount();
           Byte CR_VD = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();

           String CR_SPL_SPPLR_CLSS2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierClass();
           String CR_SPL_SPPLR_CODE2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierType();
           Date CR_DT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDate();
           String CR_DCMNT_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDocumentNumber();
           String CR_RFRNC_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrReferenceNumber();
           String CR_CHCK_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrCheckNumber();
           String CR_BNK_ACCNT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrBankAccount();
           Byte CR_VD2 = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();

           String ORDER_BY = ((ApRepCheckRegisterDetails) CR).getOrderBy();

           if (!(CR_SPL_SPPLR_CLSS1.equals(CR_SPL_SPPLR_CLSS2))) {

               return CR_SPL_SPPLR_CLSS1.compareTo(CR_SPL_SPPLR_CLSS2);

           } else {

               if(ORDER_BY.equals("DATE") && !(CR_DT1.equals(CR_DT2))){

                   return CR_DT1.compareTo(CR_DT2);

               } else if(ORDER_BY.equals("SUPPLIER CODE") && !(CR_SPL_SPPLR_CODE1.equals(CR_SPL_SPPLR_CODE2))){

                   return CR_SPL_SPPLR_CODE1.compareTo(CR_SPL_SPPLR_CODE2);

               } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(CR_SPL_SPPLR_TYP1.equals(CR_SPL_SPPLR_TYP2))){

                   return CR_SPL_SPPLR_TYP1.compareTo(CR_SPL_SPPLR_TYP2);

               } else if(ORDER_BY.equals("BANK ACCOUNT") && !(CR_BNK_ACCNT1.equals(CR_BNK_ACCNT2))){

                   return CR_BNK_ACCNT1.compareTo(CR_BNK_ACCNT2);

               } else if(ORDER_BY.equals("DOCUMENT NUMBER") && !(CR_DCMNT_NMBR1.equals(CR_DCMNT_NMBR2))){

                   return CR_DCMNT_NMBR1.compareTo(CR_DCMNT_NMBR2);

               } else {

                   return CR_CHCK_NMBR1.compareTo(CR_CHCK_NMBR2);

               }

           }

       };
	   
	   public static Comparator NoGroupComparator = (CR, anotherCR) -> {

           String CR_SPL_SPPLR_CODE1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierType();
           Date CR_DT1 = ((ApRepCheckRegisterDetails) CR).getCrDate();
           String CR_DCMNT_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrDocumentNumber();
           String CR_RFRNC_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrReferenceNumber();
           String CR_CHCK_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrCheckNumber();
           String CR_BNK_ACCNT1 = ((ApRepCheckRegisterDetails) CR).getCrBankAccount();
           Byte CR_VD = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();

           String CR_SPL_SPPLR_CODE2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierType();
           Date CR_DT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDate();
           String CR_DCMNT_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDocumentNumber();
           String CR_RFRNC_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrReferenceNumber();
           String CR_CHCK_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrCheckNumber();
           String CR_BNK_ACCNT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrBankAccount();
           Byte CR_VD2 = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();
           String ORDER_BY = ((ApRepCheckRegisterDetails) CR).getOrderBy();

           Debug.print(CR_DCMNT_NMBR1);
           Debug.print(CR_DCMNT_NMBR2);
           if(ORDER_BY.equals("DATE") && !(CR_DT1.equals(CR_DT2))){

               return CR_DT1.compareTo(CR_DT2);

           } else if(ORDER_BY.equals("SUPPLIER CODE") && !(CR_SPL_SPPLR_CODE1.equals(CR_SPL_SPPLR_CODE2))){

               return CR_SPL_SPPLR_CODE1.compareTo(CR_SPL_SPPLR_CODE2);

           } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(CR_SPL_SPPLR_TYP1.equals(CR_SPL_SPPLR_TYP2))){

               return CR_SPL_SPPLR_TYP1.compareTo(CR_SPL_SPPLR_TYP2);

           } else if(ORDER_BY.equals("BANK ACCOUNT") && !(CR_BNK_ACCNT1.equals(CR_BNK_ACCNT2))){

               return CR_BNK_ACCNT1.compareTo(CR_BNK_ACCNT2);

           } else if(ORDER_BY.equals("DOCUMENT NUMBER") && !(CR_DCMNT_NMBR1.equals(CR_DCMNT_NMBR2))){

                return CR_DCMNT_NMBR1.compareTo(CR_DCMNT_NMBR2);

        } else {

               return CR_CHCK_NMBR1.compareTo(CR_CHCK_NMBR2);

           }

       };
	   
	   public static Comparator CoaAccountNumberComparator = (CR, anotherCR) -> {

           String CR_SPL_SPPLR_CODE1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP1 = ((ApRepCheckRegisterDetails) CR).getCrSplSupplierType();
           Date CR_DT1 = ((ApRepCheckRegisterDetails) CR).getCrDate();
           String CR_DCMNT_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrDocumentNumber();
           String CR_RFRNC_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrReferenceNumber();
           String CR_CHCK_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrCheckNumber();
           String CR_BNK_ACCNT1 = ((ApRepCheckRegisterDetails) CR).getCrBankAccount();
           String CR_DR_GL_COA_ACCNT_NMBR1 = ((ApRepCheckRegisterDetails) CR).getCrDrGlCoaAccountNumber();
           Byte CR_VD = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();

           String CR_SPL_SPPLR_CODE2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierCode();
           String CR_SPL_SPPLR_TYP2 = ((ApRepCheckRegisterDetails) anotherCR).getCrSplSupplierType();
           Date CR_DT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDate();
           String CR_DCMNT_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDocumentNumber();
           String CR_RFRNC_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrReferenceNumber();
           String CR_CHCK_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrCheckNumber();
           String CR_BNK_ACCNT2 = ((ApRepCheckRegisterDetails) anotherCR).getCrBankAccount();
           String CR_DR_GL_COA_ACCNT_NMBR2 = ((ApRepCheckRegisterDetails) anotherCR).getCrDrGlCoaAccountNumber();
           Byte CR_VD2 = ((ApRepCheckRegisterDetails) CR).getCrCheckVoid();
           String ORDER_BY = ((ApRepCheckRegisterDetails) CR).getOrderBy();

           if (!(CR_DR_GL_COA_ACCNT_NMBR1.equals(CR_DR_GL_COA_ACCNT_NMBR2))) {

               return CR_DR_GL_COA_ACCNT_NMBR1.compareTo(CR_DR_GL_COA_ACCNT_NMBR2);

           } else {

               if(ORDER_BY.equals("DATE") && !(CR_DT1.equals(CR_DT2))){

                   return CR_DT1.compareTo(CR_DT2);

               } else if(ORDER_BY.equals("SUPPLIER CODE") && !(CR_SPL_SPPLR_CODE1.equals(CR_SPL_SPPLR_CODE2))){

                   return CR_SPL_SPPLR_CODE1.compareTo(CR_SPL_SPPLR_CODE2);

               } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(CR_SPL_SPPLR_TYP1.equals(CR_SPL_SPPLR_TYP2))){

                   return CR_SPL_SPPLR_TYP1.compareTo(CR_SPL_SPPLR_TYP2);

               } else if(ORDER_BY.equals("BANK ACCOUNT") && !(CR_BNK_ACCNT1.equals(CR_BNK_ACCNT2))){

                   return CR_BNK_ACCNT1.compareTo(CR_BNK_ACCNT2);

               } else if(ORDER_BY.equals("DOCUMENT NUMBER") && !(CR_DCMNT_NMBR1.equals(CR_DCMNT_NMBR2))){

                    return CR_DCMNT_NMBR1.compareTo(CR_DCMNT_NMBR2);

            } else {

                   return CR_CHCK_NMBR1.compareTo(CR_CHCK_NMBR2);

               }

           }

       };

} // ApRepCheckRegisterDetails class