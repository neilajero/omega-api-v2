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

public class ArRepSalesDetails implements java.io.Serializable, Comparable {

   private String SLS_ITM_NM;
   private String SLS_ITM_DESC;
   private Date SLS_DT;
   private String SLS_SRC;
   private Date SLS_EFFCTVTY_DT;
   private String SLS_CSTMR_CODE;
   private String SLS_CSTMR_BTCH;
   private String SLS_UNT;
   private String SLS_DCMNT_NMBR;
   private double SLS_QTY_SLD;
   private double SLS_UNT_PRC;
   private double SLS_AMNT;
   private double SLS_OUTPT_VAT;
   private String SLS_CSTMR_NM;
   private double SLS_GRSS_UNT_PRC;
   private double SLS_DFLT_UNT_PRC;
   private double SLS_DSCNT;
   private String SLS_ITM_AD_LV_CTGRY;
   private String ORDER_BY;
   private String SLS_CSTMR_ADDRSS;
   private String SLS_CM_INVC_NMBR;
   private String SLS_SLSPRSN_CODE;
   private String SLS_SLSPRSN_NM;   
   private String SLS_SLS_ORDR_NMBR;
   private Date SLS_SLS_ORDR_DT;
   private double SLS_SLS_ORDR_QTY;
   private double SLS_SLS_ORDR_PRC;
   private double SLS_SLS_ORDR_AMNT;
   private String SLS_CSTMR_STT_PRNVC;
   private String SLS_CSTMR_RGN;
   private String SLS_CSTMR_CLSS;
   private String SLS_DSC;
   private String SLS_ORDR_STTS;
   private String SLS_RFRNC_NMBR;
   private boolean SLS_SHW_ENTRS;
   private Integer SLS_AD_BRNCH;
   
   
   public ArRepSalesDetails() {
   }

   public double getSlsAmount() {
   	
   	  return SLS_AMNT;
   
   }
   
   public void setSlsAmount(double SLS_AMNT) {
   
   	  this.SLS_AMNT = SLS_AMNT;
   
   }
   
   public String getSlsDocumentNumber() {
   
   	  return SLS_DCMNT_NMBR;
   
   }
   
   public void setSlsDocumentNumber(String SLS_DCMNT_NMBR) {
   
   	  this.SLS_DCMNT_NMBR = SLS_DCMNT_NMBR;
   
   }
   
   public String getSlsCustomerCode() {
    
    	  return SLS_CSTMR_CODE;
    
    }
    
    public void setSlsCustomerCode(String SLS_CSTMR_CODE) {
    
    	  this.SLS_CSTMR_CODE = SLS_CSTMR_CODE;
    
    }
    
    public String getSlsCustomerBatch() {
        
  	  return SLS_CSTMR_BTCH;
  
  }
  
  public void setSlsCustomerBatch(String SLS_CSTMR_BTCH) {
  
  	  this.SLS_CSTMR_BTCH = SLS_CSTMR_BTCH;
  
  }
    
    public String getSlsUnit() {
        
    	  return SLS_UNT;
    
    }
    
    public void setSlsUnit(String SLS_UNT) {
    
    	  this.SLS_UNT = SLS_UNT;
    
    }
   
   public Date getSlsDate() {
   
   	  return SLS_DT;
   
   }
   
   public void setSlsDate(Date SLS_DT) {
   
   	  this.SLS_DT = SLS_DT;
   
   }
   
   public String getSlsSource() {
       
 	  return SLS_SRC;
 
   }
 
   public void setSlsSource(String SLS_SRC) {
 
 	  this.SLS_SRC = SLS_SRC;
 
   }
   
   public Date getSlsEffectivityDate() {
	   
	   	  return SLS_EFFCTVTY_DT;
	   
	   }
	   
   public void setSlsEffectivityDate(Date SLS_EFFCTVTY_DT) {
   
   	  this.SLS_EFFCTVTY_DT = SLS_EFFCTVTY_DT;
   
   }
   
   public String getSlsItemDescription() {
   
   	  return SLS_ITM_DESC;
   
   }
   
   public void setSlsItemDescription(String SLS_ITM_DESC) {
   
   	  this.SLS_ITM_DESC = SLS_ITM_DESC;
   
   }
   
   public String getSlsItemName() {
   
   	  return SLS_ITM_NM;
   
   }
   
   public void setSlsItemName(String SLS_ITM_NM) {
   
   	  this.SLS_ITM_NM = SLS_ITM_NM;
   
   }
   
   public double getSlsOutputVat() {
   
   	  return SLS_OUTPT_VAT;
   
   }
   
   public void setSlsOutputVat(double SLS_OUTPT_VAT) {
   
   	  this.SLS_OUTPT_VAT = SLS_OUTPT_VAT;
   
   }
   
   public double getSlsQuantitySold() {
   
   	  return SLS_QTY_SLD;
   
   }
   
   public void setSlsQuantitySold(double SLS_QTY_SLD) {
   
   	  this.SLS_QTY_SLD = SLS_QTY_SLD;
   
   }
   
   public double getSlsUnitPrice() {
   
      return SLS_UNT_PRC;
   
   }
   
   public void setSlsUnitPrice(double SLS_UNT_PRC) {
   
   	  this.SLS_UNT_PRC = SLS_UNT_PRC;
   
   }
   
   public String getSlsCustomerName() {
	   
	   return SLS_CSTMR_NM;
	   
   }
   
   public void setSlsCustomerName(String SLS_CSTMR_NM) {
	   
	   this.SLS_CSTMR_NM = SLS_CSTMR_NM;
	   
   }
   
   public double getSlsGrossUnitPrice() {

	   return SLS_GRSS_UNT_PRC;

   }

   public void setSlsGrossUnitPrice(double SLS_GRSS_UNT_PRC) {

	   this.SLS_GRSS_UNT_PRC = SLS_GRSS_UNT_PRC;

   }
   
   public double getSlsDefaultUnitPrice() {

	   return SLS_DFLT_UNT_PRC;

   }

   public void setSlsDefaultUnitPrice(double SLS_DFLT_UNT_PRC) {

	   this.SLS_DFLT_UNT_PRC = SLS_DFLT_UNT_PRC;

   }
   
   public double getSlsDiscount() {

	   return SLS_DSCNT;

   }

   public void setSlsDiscount(double SLS_DSCNT) {

	   this.SLS_DSCNT = SLS_DSCNT;

   }   
   
   public String getSlsItemAdLvCategory() {
	   
	   return SLS_ITM_AD_LV_CTGRY;
	   
   }
   
   public void setSlsItemAdLvCategory(String SLS_ITM_AD_LV_CTGRY) {
	   
	   this.SLS_ITM_AD_LV_CTGRY = SLS_ITM_AD_LV_CTGRY;
	   
   }
   
   public String getOrderBy() {

	   return ORDER_BY;

   }

   public void setOrderBy(String ORDER_BY) {

	   this.ORDER_BY = ORDER_BY;

   }

   public String getSlsCustomerAddress() {
	   
	   return SLS_CSTMR_ADDRSS;
	   
   }
   
   public void setSlsCustomerAddress(String SLS_CSTMR_ADDRSS) {
	   
	   this.SLS_CSTMR_ADDRSS = SLS_CSTMR_ADDRSS;
	   
   }
   
   
   public String getSlsCmInvoiceNumber() {
	   
	   return SLS_CM_INVC_NMBR;
	   
   }
   
   public void setSlsCmInvoiceNumber(String SLS_CM_INVC_NMBR) {
	   
	   this.SLS_CM_INVC_NMBR = SLS_CM_INVC_NMBR;
	   
   }
   
   
   public String getSlsSalespersonCode() {
	   
	   return SLS_SLSPRSN_CODE;
	   
   }
   
   public void setSlsSalespersonCode(String SLS_SLSPRSN_CODE) {
	   
	   this.SLS_SLSPRSN_CODE = SLS_SLSPRSN_CODE;
	   
   }
   
   public String getSlsSalespersonName() {
	   
	   return SLS_SLSPRSN_NM;
	   
   }
   
   public void setSlsSalespersonName(String SLS_SLSPRSN_NM) {
	   
	   this.SLS_SLSPRSN_NM = SLS_SLSPRSN_NM;
	   
   }
   
   public String getSlsSalesOrderNumber() {

	   return SLS_SLS_ORDR_NMBR;

   }

   public void setSlsSalesOrderNumber(String SLS_SLS_ORDR_NMBR) {

	   this.SLS_SLS_ORDR_NMBR = SLS_SLS_ORDR_NMBR;

   }

   public Date getSlsSalesOrderDate() {

	   return SLS_SLS_ORDR_DT;

   }

   public void setSlsSalesOrderDate(Date SLS_SLS_ORDR_DT) {

	   this.SLS_SLS_ORDR_DT = SLS_SLS_ORDR_DT;

   }

   public double getSlsSalesOrderQuantity() {

	   return SLS_SLS_ORDR_QTY;

   }

   public void setSlsSalesOrderQuantity(double SLS_SLS_ORDR_QTY) {

	   this.SLS_SLS_ORDR_QTY = SLS_SLS_ORDR_QTY;

   }
   
   public double getSlsSalesOrderSalesPrice() {

	   return SLS_SLS_ORDR_PRC;

   }

   public void setSlsSalesOrderSalesPrice(double SLS_SLS_ORDR_PRC) {

	   this.SLS_SLS_ORDR_PRC = SLS_SLS_ORDR_PRC;

   }
   
   public double getSlsSalesOrderAmount() {

	   return SLS_SLS_ORDR_AMNT;

   }

   public void setSlsSalesOrderAmount(double SLS_SLS_ORDR_AMNT) {

	   this.SLS_SLS_ORDR_AMNT = SLS_SLS_ORDR_AMNT;

   }
   
   public String getSlsCustomerRegion() {
	   
	   return SLS_CSTMR_RGN;
	   
   }
   
   public void setSlsCustomerRegion(String SLS_CSTMR_RGN) {
	   
	   this.SLS_CSTMR_RGN = SLS_CSTMR_RGN;
	   
   }
   
   public String getSlsCustomerClass() {
	   
	   return SLS_CSTMR_CLSS;
	   
   }
   
   public void setSlsCustomerClass(String SLS_CSTMR_CLSS) {
	   
	   this.SLS_CSTMR_CLSS = SLS_CSTMR_CLSS;
	   
   }
   
   public String getSlsDescription() {
	   
	   return SLS_DSC;
	   
   }
   
   public void setSlsDescription(String SLS_DSC) {
	   
	   this.SLS_DSC = SLS_DSC;
	   
   }
   
   
   
 public String getSlsOrderStatus() {
	   
	   return SLS_ORDR_STTS;
	   
   }
   
   public void setSlsOrderStatus(String SLS_ORDR_STTS) {
	   
	   this.SLS_ORDR_STTS = SLS_ORDR_STTS;
	   
   }
   
   public String getSlsReferenceNumber() {
	   
	   return SLS_RFRNC_NMBR;
	   
   }
   
   public void setSlsReferenceNumber(String SLS_RFRNC_NMBR) {
	   
	   this.SLS_RFRNC_NMBR = SLS_RFRNC_NMBR;
	   
   }
   
   
   public boolean getSlsShowEntries() {
	   
	   return SLS_SHW_ENTRS;
	   
   }
   
   public void setSlsShowEntries(boolean SLS_SHW_ENTRS) {
	   
	   this.SLS_SHW_ENTRS = SLS_SHW_ENTRS;
	   
   }
   
   public Integer getSlsAdBranch() {
	   
	   return SLS_AD_BRNCH;
	   
   }
   
   public void setSlsAdBranch(Integer SLS_AD_BRNCH) {
	   
	   this.SLS_AD_BRNCH = SLS_AD_BRNCH;
	   
   }
   
   public String getSlsCustomerStateProvince() {
	   
	   return SLS_CSTMR_STT_PRNVC;
	   
   }
   
   public void setSlsCustomerStateProvince(String SLS_CSTMR_STT_PRNVC) {
	   
	   this.SLS_CSTMR_STT_PRNVC = SLS_CSTMR_STT_PRNVC;
	   
   }
   

   public int compareTo(Object obj) {
   	
   	  ArRepSalesDetails details = (ArRepSalesDetails)obj;   	
   	  return this.getSlsDate().compareTo(details.getSlsDate());
   	
   }
   
   
   public static Comparator DSS3Comparator = (r1, r2) -> {


       Integer SLS_AD_BRNCH1 = ((ArRepSalesDetails) r1).getSlsAdBranch();
       String SLS_DCMNT_NMBR1 = ((ArRepSalesDetails) r1).getSlsDocumentNumber();
       String SLS_ITM_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();
       String SLS_PRSN_CD1 = ((ArRepSalesDetails) r1).getSlsSalespersonCode();
       String SLS_SRC1 = ((ArRepSalesDetails) r1).getSlsSource();

       Integer SLS_AD_BRNCH2 = ((ArRepSalesDetails) r2).getSlsAdBranch();
       String SLS_DCMNT_NMBR2 = ((ArRepSalesDetails) r2).getSlsDocumentNumber();
       String SLS_ITM_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();
       String SLS_PRSN_CD2 = ((ArRepSalesDetails) r2).getSlsSalespersonCode();
       String SLS_SRC2 = ((ArRepSalesDetails) r2).getSlsSource();

       String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

       if (!(SLS_ITM_CTGRY1.equals(SLS_ITM_CTGRY2))) {

           return SLS_ITM_CTGRY1.compareTo(SLS_ITM_CTGRY2);

       } else {

           return SLS_AD_BRNCH1.compareTo(SLS_AD_BRNCH2);
       }

   };
	
   
   public static Comparator DSS2Comparator = (r1, r2) -> {


       Integer SLS_AD_BRNCH1 = ((ArRepSalesDetails) r1).getSlsAdBranch();
       String SLS_DCMNT_NMBR1 = ((ArRepSalesDetails) r1).getSlsDocumentNumber();
       String SLS_ITM_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();
       String SLS_PRSN_CD1 = ((ArRepSalesDetails) r1).getSlsSalespersonCode();
       String SLS_SRC1 = ((ArRepSalesDetails) r1).getSlsSource();

       Integer SLS_AD_BRNCH2 = ((ArRepSalesDetails) r2).getSlsAdBranch();
       String SLS_DCMNT_NMBR2 = ((ArRepSalesDetails) r2).getSlsDocumentNumber();
       String SLS_ITM_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();
       String SLS_PRSN_CD2 = ((ArRepSalesDetails) r2).getSlsSalespersonCode();
       String SLS_SRC2 = ((ArRepSalesDetails) r2).getSlsSource();

       String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

       if (!(SLS_ITM_CTGRY1.equals(SLS_ITM_CTGRY2))) {

           return SLS_ITM_CTGRY1.compareTo(SLS_ITM_CTGRY2);

       } else {

           return SLS_PRSN_CD1.compareTo(SLS_PRSN_CD2);
       }

   };
	
   
   public static Comparator DSSComparator = (r1, r2) -> {


       Integer SLS_AD_BRNCH1 = ((ArRepSalesDetails) r1).getSlsAdBranch();
       String SLS_DCMNT_NMBR1 = ((ArRepSalesDetails) r1).getSlsDocumentNumber();
       String SLS_ITM_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();
       String SLS_PRSN_CD1 = ((ArRepSalesDetails) r1).getSlsSalespersonCode();
       String SLS_SRC1 = ((ArRepSalesDetails) r1).getSlsSource();

       Integer SLS_AD_BRNCH2 = ((ArRepSalesDetails) r2).getSlsAdBranch();
       String SLS_DCMNT_NMBR2 = ((ArRepSalesDetails) r2).getSlsDocumentNumber();
       String SLS_ITM_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();
       String SLS_PRSN_CD2 = ((ArRepSalesDetails) r2).getSlsSalespersonCode();
       String SLS_SRC2 = ((ArRepSalesDetails) r2).getSlsSource();

       String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

       if (!(SLS_ITM_CTGRY1.equals(SLS_ITM_CTGRY2))) {

           return SLS_ITM_CTGRY1.compareTo(SLS_ITM_CTGRY2);

       } else {

           return SLS_SRC1.compareTo(SLS_SRC2);
       }

   };
	
   
	public static Comparator SBSComparator = (r1, r2) -> {


        Integer SLS_AD_BRNCH1 = ((ArRepSalesDetails) r1).getSlsAdBranch();
        String SLS_DCMNT_NMBR1 = ((ArRepSalesDetails) r1).getSlsDocumentNumber();
        String SLS_ITM_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();
        String SLS_PRSN_CD1 = ((ArRepSalesDetails) r1).getSlsSalespersonCode();
        Date SLS_DT1 = ((ArRepSalesDetails) r1).getSlsDate();

        Integer SLS_AD_BRNCH2 = ((ArRepSalesDetails) r2).getSlsAdBranch();
        String SLS_DCMNT_NMBR2 = ((ArRepSalesDetails) r2).getSlsDocumentNumber();
        String SLS_ITM_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();
        String SLS_PRSN_CD2 = ((ArRepSalesDetails) r2).getSlsSalespersonCode();
        Date SLS_DT2 = ((ArRepSalesDetails) r2).getSlsDate();

        String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

        if (!(SLS_DT1.equals(SLS_DT2))) {

            return SLS_DT1.compareTo(SLS_DT2);

        } else {

            return SLS_DCMNT_NMBR1.compareTo(SLS_DCMNT_NMBR2);

        }

    };
	
   
   public static Comparator SISComparator = (r1, r2) -> {


       Integer SLS_AD_BRNCH1 = ((ArRepSalesDetails) r1).getSlsAdBranch();
       String SLS_DCMNT_NMBR1 = ((ArRepSalesDetails) r1).getSlsDocumentNumber();
       String SLS_ITM_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();
       String SLS_PRSN_CD1 = ((ArRepSalesDetails) r1).getSlsSalespersonCode();

       Integer SLS_AD_BRNCH2 = ((ArRepSalesDetails) r2).getSlsAdBranch();
       String SLS_DCMNT_NMBR2 = ((ArRepSalesDetails) r2).getSlsDocumentNumber();
       String SLS_ITM_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();
       String SLS_PRSN_CD2 = ((ArRepSalesDetails) r2).getSlsSalespersonCode();

       String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

       if (!(SLS_PRSN_CD1.equals(SLS_PRSN_CD2))) {

           return SLS_PRSN_CD1.compareTo(SLS_PRSN_CD2);

       } else if (!(SLS_DCMNT_NMBR1.equals(SLS_DCMNT_NMBR2))) {

           return SLS_DCMNT_NMBR1.compareTo(SLS_DCMNT_NMBR2);

       } else {

           return SLS_ITM_CTGRY1.compareTo(SLS_ITM_CTGRY2);
       }

   };
	
	public static Comparator CMSComparator = (r1, r2) -> {


        Integer SLS_AD_BRNCH1 = ((ArRepSalesDetails) r1).getSlsAdBranch();
        String SLS_DCMNT_NMBR1 = ((ArRepSalesDetails) r1).getSlsDocumentNumber();
        String SLS_ITM_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();
        String SLS_PRSN_CD1 = ((ArRepSalesDetails) r1).getSlsSalespersonCode();

        Integer SLS_AD_BRNCH2 = ((ArRepSalesDetails) r2).getSlsAdBranch();
        String SLS_DCMNT_NMBR2 = ((ArRepSalesDetails) r2).getSlsDocumentNumber();
        String SLS_ITM_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();
        String SLS_PRSN_CD2 = ((ArRepSalesDetails) r2).getSlsSalespersonCode();

        String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

        if (!(SLS_PRSN_CD1.equals(SLS_PRSN_CD2))) {

            return SLS_PRSN_CD1.compareTo(SLS_PRSN_CD2);

        } else if (!(SLS_DCMNT_NMBR1.equals(SLS_DCMNT_NMBR2))) {

            return SLS_DCMNT_NMBR1.compareTo(SLS_DCMNT_NMBR2);

        } else {

            return SLS_ITM_CTGRY1.compareTo(SLS_ITM_CTGRY2);
        }

    };
	
	public static Comparator CSSComparator = (r1, r2) -> {


        Integer SLS_AD_BRNCH1 = ((ArRepSalesDetails) r1).getSlsAdBranch();
        String SLS_DCMNT_NMBR1 = ((ArRepSalesDetails) r1).getSlsDocumentNumber();
        String SLS_ITM_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();
        String SLS_PRSN_CD1 = ((ArRepSalesDetails) r1).getSlsSalespersonCode();

        Integer SLS_AD_BRNCH2 = ((ArRepSalesDetails) r2).getSlsAdBranch();
        String SLS_DCMNT_NMBR2 = ((ArRepSalesDetails) r2).getSlsDocumentNumber();
        String SLS_ITM_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();
        String SLS_PRSN_CD2 = ((ArRepSalesDetails) r2).getSlsSalespersonCode();

        String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

        if (!(SLS_AD_BRNCH1.equals(SLS_AD_BRNCH2))) {

            return SLS_AD_BRNCH1.compareTo(SLS_AD_BRNCH2);

        } else if (!(SLS_DCMNT_NMBR1.equals(SLS_DCMNT_NMBR2))) {

            return SLS_DCMNT_NMBR1.compareTo(SLS_DCMNT_NMBR2);

        } else {

            return SLS_ITM_CTGRY1.compareTo(SLS_ITM_CTGRY2);
        }

    };
	
	
	public static Comparator USRSIComparator = (r1, r2) -> {

        String SLS_ITM_NM1 = ((ArRepSalesDetails) r1).getSlsItemName();
        String SLS_CSTMR_NM1 = ((ArRepSalesDetails) r1).getSlsCustomerName();
        Date SLS_DT1 = ((ArRepSalesDetails) r1).getSlsDate();
        String SLS_ITM_UOM1 = ((ArRepSalesDetails) r1).getSlsUnit();
        Integer SLS_AD_BRNCH1 = ((ArRepSalesDetails) r1).getSlsAdBranch();
        String SLS_ITM_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();
        String SLS_PRSN_CD1 = ((ArRepSalesDetails) r1).getSlsSalespersonCode();

        String SLS_ITM_NM2 = ((ArRepSalesDetails) r2).getSlsItemName();
        String SLS_CSTMR_NM2 = ((ArRepSalesDetails) r2).getSlsCustomerName();
        Date SLS_DT2 = ((ArRepSalesDetails) r2).getSlsDate();
        String SLS_ITM_UOM2 = ((ArRepSalesDetails) r2).getSlsUnit();
        Integer SLS_AD_BRNCH2 = ((ArRepSalesDetails) r2).getSlsAdBranch();
        String SLS_ITM_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();
        String SLS_PRSN_CD2 = ((ArRepSalesDetails) r2).getSlsSalespersonCode();

        String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

        if (!(SLS_PRSN_CD1.equals(SLS_PRSN_CD2))) {

            return SLS_PRSN_CD1.compareTo(SLS_PRSN_CD2);

        } else if (!(SLS_ITM_CTGRY1.equals(SLS_ITM_CTGRY2))) {

            return SLS_ITM_CTGRY1.compareTo(SLS_ITM_CTGRY2);

        } else if (!(SLS_ITM_NM1.equals(SLS_ITM_NM2))) {

            return SLS_ITM_NM1.compareTo(SLS_ITM_NM2);

        } else {

            return SLS_ITM_UOM1.compareTo(SLS_ITM_UOM2);
        }

    };
	
	
   
   public static Comparator USRCSComparator = (r1, r2) -> {

       String SLS_ITM_NM1 = ((ArRepSalesDetails) r1).getSlsItemName();
       String SLS_CSTMR_NM1 = ((ArRepSalesDetails) r1).getSlsCustomerName();
       Date SLS_DT1 = ((ArRepSalesDetails) r1).getSlsDate();
       String SLS_ITM_UOM1 = ((ArRepSalesDetails) r1).getSlsUnit();
       Integer SLS_AD_BRNCH1 = ((ArRepSalesDetails) r1).getSlsAdBranch();
       String SLS_ITM_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();

       String SLS_ITM_NM2 = ((ArRepSalesDetails) r2).getSlsItemName();
       String SLS_CSTMR_NM2 = ((ArRepSalesDetails) r2).getSlsCustomerName();
       Date SLS_DT2 = ((ArRepSalesDetails) r2).getSlsDate();
       String SLS_ITM_UOM2 = ((ArRepSalesDetails) r2).getSlsUnit();
       Integer SLS_AD_BRNCH2 = ((ArRepSalesDetails) r2).getSlsAdBranch();
       String SLS_ITM_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();

       String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

       if (!(SLS_AD_BRNCH1.equals(SLS_AD_BRNCH2))) {

           return SLS_AD_BRNCH1.compareTo(SLS_AD_BRNCH2);

       } else if (!(SLS_ITM_CTGRY1.equals(SLS_ITM_CTGRY2))) {

           return SLS_ITM_CTGRY1.compareTo(SLS_ITM_CTGRY2);

       } else if (!(SLS_ITM_NM1.equals(SLS_ITM_NM2))) {

           return SLS_ITM_NM1.compareTo(SLS_ITM_NM2);

       } else {

           return SLS_ITM_UOM1.compareTo(SLS_ITM_UOM2);
       }

   };
	
	
	
   
   public static Comparator ItemComparator = (r1, r2) -> {

       String SLS_ITM_NM1 = ((ArRepSalesDetails) r1).getSlsItemName();
       String SLS_CSTMR_NM1 = ((ArRepSalesDetails) r1).getSlsCustomerName();
       Date SLS_DT1 = ((ArRepSalesDetails) r1).getSlsDate();
       String SLS_ITM_UOM1 = ((ArRepSalesDetails) r1).getSlsUnit();
       Integer SLS_AD_BRNCH1 = ((ArRepSalesDetails) r1).getSlsAdBranch();


       String SLS_ITM_NM2 = ((ArRepSalesDetails) r2).getSlsItemName();
       String SLS_CSTMR_NM2 = ((ArRepSalesDetails) r2).getSlsCustomerName();
       Date SLS_DT2 = ((ArRepSalesDetails) r2).getSlsDate();
       String SLS_ITM_UOM2 = ((ArRepSalesDetails) r2).getSlsUnit();
       Integer SLS_AD_BRNCH2 = ((ArRepSalesDetails) r2).getSlsAdBranch();

       String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

       if (!(SLS_ITM_NM1.equals(SLS_ITM_NM2))) {

           return SLS_ITM_NM1.compareTo(SLS_ITM_NM2);

       } else {

           if(ORDER_BY.equals("DATE") && !(SLS_DT1.equals(SLS_DT2))){

               return SLS_DT1.compareTo(SLS_DT2);

           } else if(ORDER_BY.equals("CUSTOMER") && !(SLS_CSTMR_NM1.equals(SLS_CSTMR_NM2))){

               return SLS_CSTMR_NM1.compareTo(SLS_CSTMR_NM2);

           } else if(ORDER_BY.equals("BRANCH") && !(SLS_AD_BRNCH1.equals(SLS_AD_BRNCH2))){

                   return SLS_AD_BRNCH1.compareTo(SLS_AD_BRNCH2);

           } else {

               return SLS_ITM_UOM1.compareTo(SLS_ITM_UOM2);
           }

       }

   };

	public static Comparator CustomerComparator = (r1, r2) -> {

        String SLS_ITM_NM1 = ((ArRepSalesDetails) r1).getSlsItemName();
        String SLS_CSTMR_NM1 = ((ArRepSalesDetails) r1).getSlsCustomerName();
        Date SLS_DT1 = ((ArRepSalesDetails) r1).getSlsDate();

        String SLS_ITM_NM2 = ((ArRepSalesDetails) r2).getSlsItemName();
        String SLS_CSTMR_NM2 = ((ArRepSalesDetails) r2).getSlsCustomerName();
        Date SLS_DT2 = ((ArRepSalesDetails) r2).getSlsDate();

        String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

        if (!(SLS_CSTMR_NM1.equals(SLS_CSTMR_NM2))) {

            return SLS_CSTMR_NM1.compareTo(SLS_CSTMR_NM2);

        } else {

            if(ORDER_BY.equals("DATE") && !(SLS_DT1.equals(SLS_DT2))){

                return SLS_DT1.compareTo(SLS_DT2);

            } else {

                return SLS_ITM_NM1.compareTo(SLS_ITM_NM2);

            }

        }

    };
	
	public static Comparator ItemCategoryComparator = (r1, r2) -> {

        String SLS_ITM_NM1 = ((ArRepSalesDetails) r1).getSlsItemName();
        String SLS_CSTMR_NM1 = ((ArRepSalesDetails) r1).getSlsCustomerName();
        Date SLS_DT1 = ((ArRepSalesDetails) r1).getSlsDate();
        String SLS_ITM_AD_LV_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();



        String SLS_ITM_NM2 = ((ArRepSalesDetails) r2).getSlsItemName();
        String SLS_CSTMR_NM2 = ((ArRepSalesDetails) r2).getSlsCustomerName();
        Date SLS_DT2 = ((ArRepSalesDetails) r2).getSlsDate();
        String SLS_ITM_AD_LV_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();


        String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

        if (!(SLS_ITM_AD_LV_CTGRY1.equals(SLS_ITM_AD_LV_CTGRY2))) {

            return SLS_ITM_AD_LV_CTGRY1.compareTo(SLS_ITM_AD_LV_CTGRY2);

        } else {

            if (!(SLS_ITM_NM1.equals(SLS_ITM_NM2))) {

                return SLS_ITM_NM1.compareTo(SLS_ITM_NM2);

            } else {

                if(ORDER_BY.equals("DATE") && !(SLS_DT1.equals(SLS_DT2))){

                    return SLS_DT1.compareTo(SLS_DT2);

                } else {

                    return SLS_CSTMR_NM1.compareTo(SLS_CSTMR_NM2);

                }

            }

        }

    };

	public static Comparator SubComparator = (r1, r2) -> {

        String SLS_CSTMR_NM1 = ((ArRepSalesDetails) r1).getSlsCustomerName();
        String SLS_ITM_AD_LV_CTGRY1 = ((ArRepSalesDetails) r1).getSlsItemAdLvCategory();
        String SLS_ITM_NM1 = ((ArRepSalesDetails) r1).getSlsItemName();
        String SLS_UNT1 = ((ArRepSalesDetails) r1).getSlsUnit();

        String SLS_CSTMR_NM2 = ((ArRepSalesDetails) r2).getSlsCustomerName();
        String SLS_ITM_AD_LV_CTGRY2 = ((ArRepSalesDetails) r2).getSlsItemAdLvCategory();
        String SLS_ITM_NM2 = ((ArRepSalesDetails) r2).getSlsItemName();
        String SLS_UNT2 = ((ArRepSalesDetails) r2).getSlsUnit();

        if (!(SLS_CSTMR_NM1.equals(SLS_CSTMR_NM2))) {

            return SLS_CSTMR_NM1.compareTo(SLS_CSTMR_NM2);

        } else {

            if (!(SLS_ITM_AD_LV_CTGRY1.equals(SLS_ITM_AD_LV_CTGRY2))) {

                return SLS_ITM_AD_LV_CTGRY1.compareTo(SLS_ITM_AD_LV_CTGRY2);

            } else {

                if (!(SLS_UNT1.equals(SLS_UNT2))) {

                    return SLS_UNT1.compareTo(SLS_UNT2);

                } else {

                    return SLS_ITM_NM1.compareTo(SLS_ITM_NM2);

                }

            }

        }

    };
	
	public static Comparator NoGroupComparator = (r1, r2) -> {

        String SLS_ITM_NM1 = ((ArRepSalesDetails) r1).getSlsItemName();
        String SLS_CSTMR_NM1 = ((ArRepSalesDetails) r1).getSlsCustomerName();
        Date SLS_DT1 = ((ArRepSalesDetails) r1).getSlsDate();

        String SLS_ITM_NM2 = ((ArRepSalesDetails) r2).getSlsItemName();
        String SLS_CSTMR_NM2 = ((ArRepSalesDetails) r2).getSlsCustomerName();
        Date SLS_DT2 = ((ArRepSalesDetails) r2).getSlsDate();

        String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

        if(ORDER_BY.equals("DATE") && !(SLS_DT1.equals(SLS_DT2))){

            return SLS_DT1.compareTo(SLS_DT2);

        } else {

            return SLS_CSTMR_NM1.compareTo(SLS_CSTMR_NM2);

        }

    };
	
	public static Comparator SalesOrderNumberComparator = (r1, r2) -> {

        String SLS_ITM_NM1 = ((ArRepSalesDetails) r1).getSlsItemName();
        String SLS_CSTMR_NM1 = ((ArRepSalesDetails) r1).getSlsCustomerName();
        Date SLS_DT1 = ((ArRepSalesDetails) r1).getSlsDate();
        String SLS_SLS_ORDR_NMBR1 = ((ArRepSalesDetails) r1).getSlsSalesOrderNumber() == null ? "" : ((ArRepSalesDetails) r1).getSlsSalesOrderNumber();

        String SLS_ITM_NM2 = ((ArRepSalesDetails) r2).getSlsItemName();
        String SLS_CSTMR_NM2 = ((ArRepSalesDetails) r2).getSlsCustomerName();
        Date SLS_DT2 = ((ArRepSalesDetails) r2).getSlsDate();
        String SLS_SLS_ORDR_NMBR2 = ((ArRepSalesDetails) r2).getSlsSalesOrderNumber() ==  null ? "" : ((ArRepSalesDetails) r2).getSlsSalesOrderNumber();

        String ORDER_BY = ((ArRepSalesDetails) r1).getOrderBy();

        if (!(SLS_SLS_ORDR_NMBR1.equals(SLS_SLS_ORDR_NMBR2))) {

            return SLS_SLS_ORDR_NMBR1.compareTo(SLS_SLS_ORDR_NMBR2);

        } else {

            if (!(SLS_ITM_NM1.equals(SLS_ITM_NM2))) {

                return SLS_ITM_NM1.compareTo(SLS_ITM_NM2);

            } else {

                if(ORDER_BY.equals("DATE") && !(SLS_DT1.equals(SLS_DT2))){

                    return SLS_DT1.compareTo(SLS_DT2);

                } else {

                    return SLS_CSTMR_NM1.compareTo(SLS_CSTMR_NM2);

                }

            }

        }

    };
	
} // ArRepSalesDetails class