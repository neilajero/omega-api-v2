/*
 * Copyright (c) 2022. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.util.reports.ap;

import java.util.Comparator;
import java.util.Date;

public class ApRepApRegisterDetails implements java.io.Serializable {

	 private Date ARG_DT;
	   private String ARG_DCMNT_NMBR;
	   private String ARG_RFRNC_NMBR;
	   private String ARG_DESC;
           private int ARG_VPS_CODE;
	   private String ARG_SPL_SPPLR_CODE;
	   private String ARG_SPL_SPPLR_TYP;
	   private String ARG_SPL_SPPLR_CLSS;
           private String ARG_SPL_SPPLR_ACCNT_NMBR;
	   private String ARG_SPL_CNTRY;
           private String ARG_FNCTNL_CRRNCY_NM;
	   private double ARG_AMNT;
           private double ARG_AMNT_PD;
           private String ARG_CRRNCY;
	   private String ARG_UOM;
	   private String ARG_ITM_NM;
	   private String ARG_ITM_CTGRY;
           private boolean ARG_PSTD;
           private boolean ARG_VD;
	   private double ARG_QTY;
	   private double ARG_TAX_AMNT;
	   private String ORDER_BY;
	   private String ARG_SPL_NM;
	   private String ARG_SPL_TIN;
	   private String ARG_DR_COA_ACCNT_NMBR;
	   private String ARG_DR_COA_ACCNT_DESC;
	   private double ARG_DR_DBT_AMNT;
	   private double ARG_DR_CRDT_AMNT;
	   private double ARG_BLNC;
	   private String ARG_TYP;
	   private String ARG_SPL_ADDRSS;
	   private double ARG_BGNNG_BLNC;
	   private double ARG_PREV_INTRST_INCM;
	   private String ARG_PO_NMBR;
	   private String ARG_CHK_TYP;
	   

	   public ApRepApRegisterDetails() {
       }

	   public Date getArgDate() {
	   	
	   	  return ARG_DT;
	   	
	   }
	   
	   public void setArgDate(Date ARG_DT) {
	   	
	   	  this.ARG_DT = ARG_DT;
	   	
	   }
	   
	   public String getArgDocumentNumber() {
	   	
	   	  return ARG_DCMNT_NMBR;
	   	
	   }
	   
	   public void setArgDocumentNumber(String ARG_DCMNT_NMBR) {
	   	
	   	  this.ARG_DCMNT_NMBR = ARG_DCMNT_NMBR;
	   	
	   }
	   
	   public String getArgReferenceNumber() {
	   	
	   	  return ARG_RFRNC_NMBR;
	   	  
	   }
	   
	   public void setArgReferenceNumber(String ARG_RFRNC_NMBR) {
	   	
	   	  this.ARG_RFRNC_NMBR = ARG_RFRNC_NMBR;
	   	  
	   }

	   public String getArgDescription(){
	   	
	   	  return ARG_DESC;
	   	  
	   }
	   
	   public void setArgDescription(String ARG_DESC) {
	   	
	   	  this.ARG_DESC = ARG_DESC;
	   	  
	   }
           
           public int getArgVpsCode(){
	   	
	   	  return ARG_VPS_CODE;
	   	  
	   }
	   
	   public void setArgVpsCode(int ARG_VPS_CODE) {
	   	
	   	  this.ARG_VPS_CODE = ARG_VPS_CODE;
	   	  
	   }
	   
	   public String getArgSplSupplierCode() {
	   	
	   	  return ARG_SPL_SPPLR_CODE;
	   	
	   }
	   
	   public void setArgSplSupplierCode(String ARG_SPL_SPPLR_CODE) {
	   	
	   	  this.ARG_SPL_SPPLR_CODE = ARG_SPL_SPPLR_CODE;
	   	
	   }
	   
	   public String getArgSplSupplierType() {
	   	
	   	  return ARG_SPL_SPPLR_TYP;
	   	
	   }
	   
	   public void setArgSplSupplierType(String ARG_SPL_SPPLR_TYP) {
	   	
	   	  this.ARG_SPL_SPPLR_TYP = ARG_SPL_SPPLR_TYP;
	   	
	   }
	   public String getArgSplSupplierClass() {
	   	
	   	  return ARG_SPL_SPPLR_CLSS;
	   	
	   }
	   
	   public void setArgSplSupplierClass(String ARG_SPL_SPPLR_CLSS) {
	   	
	   	  this.ARG_SPL_SPPLR_CLSS = ARG_SPL_SPPLR_CLSS;
	   	
	   }
           
            public String getArgSplSupplierAccountNumber() {
	   	
	   	  return ARG_SPL_SPPLR_ACCNT_NMBR;
	   	
	   }
	   
	   public void setArgSplSupplierAccountNumber(String ARG_SPL_SPPLR_ACCNT_NMBR) {
	   	
	   	  this.ARG_SPL_SPPLR_ACCNT_NMBR = ARG_SPL_SPPLR_ACCNT_NMBR;
	   	
	   }
	   
	   
	   public String getArgSplSupplierCountry() {
		   	
	   	  return ARG_SPL_CNTRY;
	   	
	   }
	   
	   public void setArgSplSupplierCountry(String ARG_SPL_CNTRY) {
	   	
	   	  this.ARG_SPL_CNTRY = ARG_SPL_CNTRY;
	   	
	   }
		   
	  public String getArgFunctionalCurrencyName() {
		   	
	   	  return ARG_FNCTNL_CRRNCY_NM;
	   	
	   }
	   
	   public void setArgFunctionalCurrencyName(String ARG_FNCTNL_CRRNCY_NM) {
	   	
	   	  this.ARG_FNCTNL_CRRNCY_NM = ARG_FNCTNL_CRRNCY_NM;
	   	
	   }	   
	   
	   public double getArgAmount() {
	   	
	   	  return ARG_AMNT;
	   	  
	   }
	   
	   public void setArgAmount(double ARG_AMNT) {
	   	
	   	  this.ARG_AMNT = ARG_AMNT;
	   	  
	   }
           
           
           public double getArgAmountPaid() {
	   	
	   	  return ARG_AMNT_PD;
	   	  
	   }
	   
	   public void setArgAmountPaid(double ARG_AMNT_PD) {
	   	
	   	  this.ARG_AMNT_PD = ARG_AMNT_PD;
	   	  
	   }
	   
           public String getArgCurrency() {
	   	
	   	  return ARG_CRRNCY;
	   	  
	   }
	   
	   public void setArgCurrency(String ARG_CRRNCY) {
	   	
	   	  this.ARG_CRRNCY = ARG_CRRNCY;
	   	  
	   }
           
	   public String getArgUomName() {
		   	
		   	  return ARG_UOM;
		   	  
	   }
	   
	   public void setArgUomName(String ARG_UOM) {
	   	
	   	  this.ARG_UOM = ARG_UOM;
	   	  
	   }
	   
	   public String getArgItemName() {
		   	
		   	  return ARG_ITM_NM;
		   	  
		}
		
		public void setArgItemName(String ARG_ITM_NM) {
			
			  this.ARG_ITM_NM = ARG_ITM_NM;
			  
		}
		
		
		public String getArgItemCategory() {
		   	
		   	  return ARG_ITM_CTGRY;
		   	  
		}
		
		public void setArgItemCategory(String ARG_ITM_CTGRY) {
			
			  this.ARG_ITM_CTGRY = ARG_ITM_CTGRY;
			  
		}
                
            public boolean getArgPosted() {
		   	
                    return this.ARG_PSTD;
		   	  
            }
		
            public void setArgPosted(boolean ARG_PSTD) {

                      this.ARG_PSTD = ARG_PSTD;

            }
            
            public boolean getArgVoid() {
		   	
                    return this.ARG_VD;
		   	  
            }
		
            public void setArgVoid(boolean ARG_VD) {

                      this.ARG_VD = ARG_VD;

            }

	   public double getArgQuantity() {
		   	
	   	  return ARG_QTY;
		   	  
	   }
	   
	   public void setArgQuantity(double ARG_QTY) {
	   	
	   	  this.ARG_QTY = ARG_QTY;
	   	  
	   }
		   
	   
	   public double getArgTaxAmount() {
		   	
		   	  return ARG_TAX_AMNT;
		   	  
	   }
	   
	   public void setArgTaxAmount(double ARG_TAX_AMNT) {
	   	
	   	  this.ARG_TAX_AMNT = ARG_TAX_AMNT;
	   	  
	   }
		   
		   
	   
	   public String getOrderBy() {
	   	
	   	  return ORDER_BY;
	   	  
	   }
	   
	   public void setOrderBy(String ORDER_BY) {
	   	
	   	  this.ORDER_BY = ORDER_BY;
	   	  
	   }
	   
	   public String getArgSplName() {
	   	
	   	  return ARG_SPL_NM;
	   	
	   }
	   
	   public void setArgSplName(String ARG_SPL_NM) {
	   	
	   	  this.ARG_SPL_NM = ARG_SPL_NM;
	   	
	   }
	   
	   public String getArgSplTin() {
	   	
	   	  return ARG_SPL_TIN;
	   	
	   }
	   
	   public void setArgSplTin(String ARG_SPL_TIN) {
	   	
	   	  this.ARG_SPL_TIN = ARG_SPL_TIN;
	   	
	   }
	   
	   public String getArgDrCoaAccountNumber() {
	   	
	   	  return ARG_DR_COA_ACCNT_NMBR;
	   	
	   }
	   
	   public void setArgDrCoaAccountNumber(String ARG_DR_COA_ACCNT_NMBR) {
	   	
	   	  this.ARG_DR_COA_ACCNT_NMBR = ARG_DR_COA_ACCNT_NMBR;
	   	
	   }
	   
	   public String getArgDrCoaAccountDescription() {
	   	
	   	  return ARG_DR_COA_ACCNT_DESC;
	   	
	   }

	   public void setArgDrCoaAccountDescription(String ARG_DR_COA_ACCNT_DESC) {
	   	
	   	  this.ARG_DR_COA_ACCNT_DESC = ARG_DR_COA_ACCNT_DESC;
	   	
	   }
	   
	   public double getArgDrDebitAmount() {
	   	
	   	  return ARG_DR_DBT_AMNT;
	   	
	   }
	   
	   public void setArgDrDebitAmount(double ARG_DR_DBT_AMNT) {
	   	
	   	  this.ARG_DR_DBT_AMNT = ARG_DR_DBT_AMNT;
	   	
	   }
	   
	   public double getArgDrCreditAmount() {
	   	
	   	  return ARG_DR_CRDT_AMNT;
	   	
	   }
	   
	   public void setArgDrCreditAmount(double ARG_DR_CRDT_AMNT) {
	   	
	   	  this.ARG_DR_CRDT_AMNT = ARG_DR_CRDT_AMNT;
	   	
	   }

	   public double getArgBalance() {

		   return ARG_BLNC;

	   }

	   public void setArgBalance(double ARG_BLNC) {

		   this.ARG_BLNC = ARG_BLNC;

	   }
	   
	   public String getArgType() {
		   
		   return ARG_TYP;
		   
	   }
	   
	   public void setArgType(String ARG_TYP){
		   
		   this.ARG_TYP = ARG_TYP;
		   
	   }
	   
	   public String getArgSplAddress() {
		   
		   return ARG_SPL_ADDRSS;
		   
	   }
	   
	   public void setArgSplAddress(String ARG_SPL_ADDRSS) {
		   
		   this.ARG_SPL_ADDRSS = ARG_SPL_ADDRSS;
		   
	   }
	   
	   public String getArgPoNumber() {
		   
		   return ARG_PO_NMBR;
		   
	   }
	   
	   public void setArgPoNumber(String ARG_PO_NMBR) {
		   
		   this.ARG_PO_NMBR = ARG_PO_NMBR;
		   
	   }
	   
	   public String getArgCheckType() {
		   
		   return ARG_CHK_TYP;
		   
	   }
	   
	   public void setArgCheckType(String ARG_CHK_TYP) {
		   
		   this.ARG_CHK_TYP = ARG_CHK_TYP;
		   
	   }
	   
	   public double getArgBeginningBalance() {

		   return ARG_BGNNG_BLNC;

	   }

	   public void setArgBeginningBalance(double ARG_BGNNG_BLNC) {

		   this.ARG_BGNNG_BLNC = ARG_BGNNG_BLNC;

	   }
	   
	   public double getArgPrevInterestIncome() {
		   
		   return ARG_PREV_INTRST_INCM;
		   
	   }
	   
	   public void setArgPrevInterestIncome(double ARG_PREV_INTRST_INCM) {
		   
		   this.ARG_PREV_INTRST_INCM = ARG_PREV_INTRST_INCM;
		   
	   }
	   
	   public static Comparator SupplierCodeComparator = (AR, anotherAR) -> {

           String ARG_SPL_SPPLR_CODE1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierCode();
           String ARG_SPL_SPPLR_TYP1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierType();
           Date ARG_DT1 = ((ApRepApRegisterDetails) AR).getArgDate();
           String ARG_DCMNT_NMBR1 = ((ApRepApRegisterDetails) AR).getArgDocumentNumber();

           String ARG_SPL_SPPLR_CODE2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierCode();
           String ARG_SPL_SPPLR_TYP2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierType();
           Date ARG_DT2 = ((ApRepApRegisterDetails) anotherAR).getArgDate();
           String ARG_DCMNT_NMBR2 = ((ApRepApRegisterDetails) anotherAR).getArgDocumentNumber();

           String ORDER_BY = ((ApRepApRegisterDetails) AR).getOrderBy();

           if (!(ARG_SPL_SPPLR_CODE1.equals(ARG_SPL_SPPLR_CODE2))) {

               return ARG_SPL_SPPLR_CODE1.compareTo(ARG_SPL_SPPLR_CODE2);

           } else {

               if(ORDER_BY.equals("DATE") && !(ARG_DT1.equals(ARG_DT2))){

                   return ARG_DT1.compareTo(ARG_DT2);

               } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(ARG_SPL_SPPLR_TYP1.equals(ARG_SPL_SPPLR_TYP2))){

                   return ARG_SPL_SPPLR_TYP1.compareTo(ARG_SPL_SPPLR_TYP2);

               } else {

                   return ARG_DCMNT_NMBR1.compareTo(ARG_DCMNT_NMBR2);

               }

           }

       };
	   
	   public static Comparator SupplierTypeComparator = (AR, anotherAR) -> {

           String ARG_SPL_SPPLR_CODE1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierCode();
           String ARG_SPL_SPPLR_TYP1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierType();
           Date ARG_DT1 = ((ApRepApRegisterDetails) AR).getArgDate();
           String ARG_DCMNT_NMBR1 = ((ApRepApRegisterDetails) AR).getArgDocumentNumber();

           String ARG_SPL_SPPLR_CODE2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierCode();
           String ARG_SPL_SPPLR_TYP2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierType();
           Date ARG_DT2 = ((ApRepApRegisterDetails) anotherAR).getArgDate();
           String ARG_DCMNT_NMBR2 = ((ApRepApRegisterDetails) anotherAR).getArgDocumentNumber();

           String ORDER_BY = ((ApRepApRegisterDetails) AR).getOrderBy();

           if (!(ARG_SPL_SPPLR_TYP1.equals(ARG_SPL_SPPLR_TYP2))) {

               return ARG_SPL_SPPLR_TYP1.compareTo(ARG_SPL_SPPLR_TYP2);

           } else {

               if(ORDER_BY.equals("DATE") && !(ARG_DT1.equals(ARG_DT2))){

                   return ARG_DT1.compareTo(ARG_DT2);

               } else if(ORDER_BY.equals("SUPPLIER CODE") && !(ARG_SPL_SPPLR_CODE1.equals(ARG_SPL_SPPLR_CODE2))){

                   return ARG_SPL_SPPLR_CODE1.compareTo(ARG_SPL_SPPLR_CODE2);

               } else {

                   return ARG_DCMNT_NMBR1.compareTo(ARG_DCMNT_NMBR2);

               }

           }

       };
	   
	   public static Comparator SupplierClassComparator = (AR, anotherAR) -> {

           String ARG_SPL_SPPLR_CLSS1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierClass();
           String ARG_SPL_SPPLR_CODE1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierCode();
           String ARG_SPL_SPPLR_TYP1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierType();
           Date ARG_DT1 = ((ApRepApRegisterDetails) AR).getArgDate();
           String ARG_DCMNT_NMBR1 = ((ApRepApRegisterDetails) AR).getArgDocumentNumber();

           String ARG_SPL_SPPLR_CLSS2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierClass();
           String ARG_SPL_SPPLR_CODE2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierCode();
           String ARG_SPL_SPPLR_TYP2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierType();
           Date ARG_DT2 = ((ApRepApRegisterDetails) anotherAR).getArgDate();
           String ARG_DCMNT_NMBR2 = ((ApRepApRegisterDetails) anotherAR).getArgDocumentNumber();

           String ORDER_BY = ((ApRepApRegisterDetails) AR).getOrderBy();

           if (!(ARG_SPL_SPPLR_CLSS1.equals(ARG_SPL_SPPLR_CLSS2))) {

               return ARG_SPL_SPPLR_CLSS1.compareTo(ARG_SPL_SPPLR_CLSS2);

           } else {

               if(ORDER_BY.equals("DATE") && !(ARG_DT1.equals(ARG_DT2))){

                   return ARG_DT1.compareTo(ARG_DT2);

               } else if(ORDER_BY.equals("SUPPLIER CODE") && !(ARG_SPL_SPPLR_CODE1.equals(ARG_SPL_SPPLR_CODE2))){

                   return ARG_SPL_SPPLR_CODE1.compareTo(ARG_SPL_SPPLR_CODE2);

               } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(ARG_SPL_SPPLR_TYP1.equals(ARG_SPL_SPPLR_TYP2))){

                   return ARG_SPL_SPPLR_TYP1.compareTo(ARG_SPL_SPPLR_TYP2);

               } else {

                   return ARG_DCMNT_NMBR1.compareTo(ARG_DCMNT_NMBR2);

               }

           }

       };
	   
	   public static Comparator NoGroupComparator = (AR, anotherAR) -> {

           String ARG_SPL_SPPLR_CODE1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierCode();
           String ARG_SPL_SPPLR_TYP1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierType();
           Date ARG_DT1 = ((ApRepApRegisterDetails) AR).getArgDate();
           String ARG_DCMNT_NMBR1 = ((ApRepApRegisterDetails) AR).getArgDocumentNumber();

           String ARG_SPL_SPPLR_CODE2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierCode();
           String ARG_SPL_SPPLR_TYP2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierType();
           Date ARG_DT2 = ((ApRepApRegisterDetails) anotherAR).getArgDate();
           String ARG_DCMNT_NMBR2 = ((ApRepApRegisterDetails) anotherAR).getArgDocumentNumber();

           String ORDER_BY = ((ApRepApRegisterDetails) AR).getOrderBy();

           if(ORDER_BY.equals("DATE") && !(ARG_DT1.equals(ARG_DT2))){

               return ARG_DT1.compareTo(ARG_DT2);

           } else if(ORDER_BY.equals("SUPPLIER CODE") && !(ARG_SPL_SPPLR_CODE1.equals(ARG_SPL_SPPLR_CODE2))){

               return ARG_SPL_SPPLR_CODE1.compareTo(ARG_SPL_SPPLR_CODE2);

           } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(ARG_SPL_SPPLR_TYP1.equals(ARG_SPL_SPPLR_TYP2))){

               return ARG_SPL_SPPLR_TYP1.compareTo(ARG_SPL_SPPLR_TYP2);

           } else {

               return ARG_DCMNT_NMBR1.compareTo(ARG_DCMNT_NMBR2);

           }

       };
	   
	   public static Comparator CoaAccountNumberComparator = (AR, anotherAR) -> {

           String ARG_SPL_SPPLR_CODE1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierCode();
           String ARG_SPL_SPPLR_TYP1 = ((ApRepApRegisterDetails) AR).getArgSplSupplierType();
           Date ARG_DT1 = ((ApRepApRegisterDetails) AR).getArgDate();
           String ARG_DCMNT_NMBR1 = ((ApRepApRegisterDetails) AR).getArgDocumentNumber();
           String ARG_DR_GL_COA_ACCNT_NMBR1 = ((ApRepApRegisterDetails) AR).getArgDrCoaAccountNumber();

           String ARG_SPL_SPPLR_CODE2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierCode();
           String ARG_SPL_SPPLR_TYP2 = ((ApRepApRegisterDetails) anotherAR).getArgSplSupplierType();
           Date ARG_DT2 = ((ApRepApRegisterDetails) anotherAR).getArgDate();
           String ARG_DCMNT_NMBR2 = ((ApRepApRegisterDetails) anotherAR).getArgDocumentNumber();
           String ARG_DR_GL_COA_ACCNT_NMBR2 = ((ApRepApRegisterDetails) anotherAR).getArgDrCoaAccountNumber();

           String ORDER_BY = ((ApRepApRegisterDetails) AR).getOrderBy();

           if (!(ARG_DR_GL_COA_ACCNT_NMBR1.equals(ARG_DR_GL_COA_ACCNT_NMBR2))) {

               return ARG_DR_GL_COA_ACCNT_NMBR1.compareTo(ARG_DR_GL_COA_ACCNT_NMBR2);

           } else {

               if(ORDER_BY.equals("DATE") && !(ARG_DT1.equals(ARG_DT2))){

                      return ARG_DT1.compareTo(ARG_DT2);

                  } else if(ORDER_BY.equals("SUPPLIER CODE") && !(ARG_SPL_SPPLR_CODE1.equals(ARG_SPL_SPPLR_CODE2))){

                      return ARG_SPL_SPPLR_CODE1.compareTo(ARG_SPL_SPPLR_CODE2);

                  } else if(ORDER_BY.equals("SUPPLIER TYPE") && !(ARG_SPL_SPPLR_TYP1.equals(ARG_SPL_SPPLR_TYP2))){

                      return ARG_SPL_SPPLR_TYP1.compareTo(ARG_SPL_SPPLR_TYP2);

                  } else {

                      return ARG_DCMNT_NMBR1.compareTo(ARG_DCMNT_NMBR2);

                  }

           }

       };

} // ApRepApRegisterDetails class